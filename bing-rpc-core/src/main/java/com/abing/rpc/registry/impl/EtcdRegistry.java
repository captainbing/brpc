package com.abing.rpc.registry.impl;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.abing.rpc.config.RegistryConfig;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryCache;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Author CaptainBing
 * @Date 2024/6/7 14:13
 * @Description Etcd注册中心实现
 */
public class EtcdRegistry implements Registry {


    private Client client;

    private KV kvClient;

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    /**
     * 本地注册的服务节点key集合
     */
    private static final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 注册中心缓存
     */
    private static final RegistryCache registryCache = new RegistryCache();

    /**
     * 正在监听的key集合
     */
    private static final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                       .endpoints(registryConfig.getAddress())
                       .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                       .build();
        kvClient = client.getKVClient();
        // 开启心跳机制
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception{

        Lease leaseClient = client.getLeaseClient();

        // 创建租约
        long leaseId = leaseClient.grant(30).get().getID();

        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();

        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);

        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();

        kvClient.put(key, value, putOption).get();

        // 注册成功后，将key添加到本地缓存中
        localRegisterNodeKeySet.add(registryKey);

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception{
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8)).get();
        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        List<ServiceMetaInfo> serviceMetaInfoList = registryCache.readCache();
        if (!serviceMetaInfoList.isEmpty()){
            return serviceMetaInfoList;
        }
        // 缓存为空，则从etcd中获取
        return getServiceMetaInfoFromEtcd(serviceKey);
    }

    /**
     * 从etcd中获取服务注册信息
     * @param serviceKey
     * @return
     */
    private List<ServiceMetaInfo> getServiceMetaInfoFromEtcd(String serviceKey) {
        // 前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption)
                                         .get()
                                         .getKvs();

            List<ServiceMetaInfo> serviceMetaInfoList = kvs.stream().map(keyValue -> {
                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                // 监听注册中心key的变化
                watch(key);
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
            // 将获取到的服务列表缓存到本地
            registryCache.writeCache(serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destroy() {

        offlineNode();
        close();

    }

    @Override
    public void heartBeat() {

        // 续约,每十秒检查服务端的服务注册的信息
        CronUtil.schedule("*/10 * * * * *", (Task) () -> {
            // 获取租约列表
            for (String key : localRegisterNodeKeySet) {
                try {
                    List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                                 .get()
                                                 .getKvs();

                    if (keyValues.isEmpty()){
                        continue;
                    }
                    KeyValue keyValue = keyValues.get(0);
                    String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    register(serviceMetaInfo);

                } catch (Exception e) {
                    throw new RuntimeException(key + "续签失败" + e);
                }

            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();

    }

    /**
     * 下线所有节点
     */
    private void offlineNode(){

        for (String key : localRegisterNodeKeySet) {

            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败" + e);
            }

        }

    }

    /**
     * 关闭资源
     */
    private void close(){
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void watch(String serviceNodeKey) {

        Watch watchClient = client.getWatchClient();
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch){
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()){
                        case DELETE:
                            registryCache.clearCache();
                        case PUT:
                            break;
                        case UNRECOGNIZED:
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // create client using endpoints
        Client client = Client.builder().endpoints("http://localhost:2379")
                              .build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        // put the key-value
        kvClient.put(key, value).get();

        // get the CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        // get the value from CompletableFuture
        GetResponse response = getFuture.get();

        // delete the key
        DeleteResponse deleteResponse = kvClient.delete(key).get();
        deleteResponse.getDeleted();
        System.out.println();
    }
}
