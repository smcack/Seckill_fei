package com.fei;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fei.mapper.SeckillOrderMapper;
import com.fei.mapper.UserMapper;
import com.fei.pojo.Goods;
import com.fei.pojo.SeckillOrder;
import com.fei.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import javax.swing.*;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Long> script;

    @Test
    public void contextLoads() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //setIfAbsent方法 如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        //如果占位成功，进行正常操作
        if(isLock){
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            //操作结束，删除锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程在使用，请稍后");
        }

    }

    @Test
    public void testLock02(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //setIfAbsent方法 如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
    }

    //使用lua脚本实现redis分布式锁
    //lua脚本优点：1.原子性 2.不受网络波动影响
    @Test
    public void testLock03(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        //上个有时间的随机锁
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 20, TimeUnit.SECONDS);
        if(isLock){

            valueOperations.set("name", "xxx");
            String name = (String)valueOperations.get("name");
            System.out.println("name = " + name);
            System.out.println(valueOperations.get("k1"));
            Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + 2),Collections.EMPTY_LIST);
            System.out.println(stock);
        }else {
            System.out.println("有线程在使用，请稍后");
        }
    }

}
