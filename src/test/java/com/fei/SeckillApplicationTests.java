package com.fei;

import com.fei.mapper.UserMapper;
import com.fei.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {


    }

}
