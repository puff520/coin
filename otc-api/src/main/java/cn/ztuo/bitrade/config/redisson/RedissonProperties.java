//package cn.ztuo.bitrade.config.redisson;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @author puff
// * @desc redisson 连接配置类
// */
//@Component
//@ConfigurationProperties(prefix = "spring.redis")
//@Data
//public class RedissonProperties {
//
//    private String password;
//    private cluster cluster;
//    private String host;
//    private String port;
//
//    public static class cluster {
//        private List<String> nodes;
//
//        public List<String> getNodes() {
//            return nodes;
//        }
//
//        public void setNodes(List<String> nodes) {
//            this.nodes = nodes;
//        }
//    }
//
//
//}
