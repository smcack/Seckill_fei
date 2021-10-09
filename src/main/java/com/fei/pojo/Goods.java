package com.fei.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * *
 * 
 * *
 *
 * @author fei
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)

    private Long id;

    private String goodsName;

    private String goodsTitle;

    private String goodsImg;

    /**
     * 商品详情
     */
    private String goodsDetail;

    private BigDecimal goodsPrice;

    /**
     * 库存：-1表示没有限制
     */
    private Integer goodsStock;


}
