package com.ljl.note.collection.support.framework.service;

import com.google.common.base.Joiner;
import com.ljl.note.collection.common.utils.IdWorker;
import com.ljl.note.collection.support.framework.model.RedisKeyConstant;
import com.ljl.note.collection.support.framework.util.NettyConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebsocketUtilService {

    @Autowired
    private RedisUtil redisUtil;

    public String getToken() {
        Long tokenLong = IdWorker.;
        String token = tokenLong.toString();
        String key = String.format(RedisKeyConstant.AUTHKEY, token);
        log.info("发放token:{}", key);
        redisUtil.setString(key, key);
        redisUtil.setExpireTime(key, 10, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 获取用户wid,放入redis（负载均衡）
     * @param redisKey
     * @return
     */
    public Boolean getWidFromRedis(String redisKey) {
        Assert.notNull(redisKey, "入参不能为空！");
        String[] split = redisKey.split(":");
        if (split == null || split.length == 0) {
            log.error("格式校验错误！");
            return false;
        }
        List<Long> nows = NettyConnectionUtil.userChannelMap.keySet().stream().collect(Collectors.toList());
        String widString = !CollectionUtils.isEmpty(nows) ? Joiner.on(",").join(nows) : "";
        String redisVal = redisUtil.getString(redisKey);
        String string = StringUtils.isEmpty(redisVal) ? "" : redisVal + ",";
        redisUtil.setString(redisKey, string + widString, 20L, TimeUnit.SECONDS);

        return true;
    }

}
