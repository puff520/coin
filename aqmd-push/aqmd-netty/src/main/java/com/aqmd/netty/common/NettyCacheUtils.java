package com.aqmd.netty.common;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyCacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(NettyCacheUtils.class);
    private static Map<String, Set<Channel>> channelIdCache = new HashMap();
    public static Map<Channel, String> keyChannelCache = new HashMap();
    public static Map<String, Set<String>> userKey = new HashMap();

    public static void storeChannel(String key, Channel channel) {
        logger.debug("store channel with key:{}, channel id:{}", key, channel.id().asLongText());
        Set<Channel> set = channelIdCache.get(key);
        if (set == null) {
            set = new HashSet();
            set.add(channel);
            channelIdCache.put(key, set);
        } else if (!set.contains(channel)) {
            set.add(channel);
        }

    }

    public static Set<Channel> getChannel(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.debug("没有订阅[{}]的channel!", key);
        }

        return (Set) channelIdCache.get(key);
    }

    public static void removeChannel(String key) {
        if (StringUtils.isEmpty(key)) {
            logger.debug("没有订阅[{}]的channel!", key);
        }

        channelIdCache.remove(key);
    }

    public static Set<Channel> getAllChannels() {
        Set<Channel> channels = new HashSet();
        channelIdCache.forEach((key, value) -> {
            channels.addAll(value);
        });
        return channels;
    }
}
