package com.fei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fei.pojo.Goods;
import com.fei.vo.GoodsVo;

import java.util.List;

/**
 * *
 *  Mapper 接口
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
public interface GoodsMapper extends BaseMapper<Goods> {


    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 获取商品详情
     * @return
     */

    GoodsVo findGoodsVoById(Long goodsId);
}
