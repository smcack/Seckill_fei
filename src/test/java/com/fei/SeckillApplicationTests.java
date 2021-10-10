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

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Test
    void contextLoads() {
        User user=new User();
        user.setId(18378353211L);
        Goods goods=new Goods();
        goods.setId(1L);
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id",
                user.getId())
        .eq("goods_id",goods.getId()));

        System.out.println(seckillOrder);

    }

}
