package com.fei.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fei.mapper.GoodsMapper;
import com.fei.pojo.Goods;
import com.fei.service.IGoodsService;
import com.fei.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * *
 *  服务实现类
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {


    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 获取商品列表
     * @return
     */

    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }


    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo findGoodsVoById(Long goodsId) {
        return goodsMapper.findGoodsVoById(goodsId);
    }
}
