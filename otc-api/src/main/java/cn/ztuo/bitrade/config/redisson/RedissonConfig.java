//package cn.ztuo.bitrade.config.redisson;
//
//import cn.hutool.core.util.StrUtil;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.ClusterServersConfig;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author puff
// * @desc Redisson 配置
// */
//@Configuration
//public class RedissonConfig {
//
//    @Autowired
//    private RedissonProperties redissonProperties;
//
//    @Bean
//    public RedissonClient redisson() {
//        if (redissonProperties.getHost() == null) {
//            return Redisson.create(clusterConfiguration());
//        } else {
//            return Redisson.create(singleConfiguration());
//        }
//    }
//
//    private Config singleConfiguration() {
//        Config config = new Config();
//        SingleServerConfig singleServerConfig = config.useSingleServer();
//        singleServerConfig.setAddress("redis://" + redissonProperties.getHost() + ":" + redissonProperties.getPort());
//        String password = redissonProperties.getPassword();
//        if (StrUtil.isNotBlank(password)) {
//            singleServerConfig.setPassword(password);
//        }
//        return config;
//    }
//
//    private Config clusterConfiguration() {
//        Config config = new Config();
//        List<String> clusterNodes = new ArrayList<>();
//        for (int i = 0; i < redissonProperties.getCluster().getNodes().size(); i++) {
//            clusterNodes.add("redis://" + redissonProperties.getCluster().getNodes().get(i));
//        }
//        ClusterServersConfig clusterServersConfig = config.useClusterServers()
//                .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]));
//        clusterServersConfig.setPassword(redissonProperties.getPassword());//设置密码
//        return config;
//    }
//
//}
