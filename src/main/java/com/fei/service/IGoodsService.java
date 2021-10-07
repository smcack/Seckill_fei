package com.fei.service;

import com.fei.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fei.vo.GoodsVo;

import java.util.List;

/**
 * *
 *  服务类
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();


    /**
     * 获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoById(Long goodsId);
}
