package com.fei.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)

    private Long id;

    private Long userId;

    private Long goodsId;

    private String goodsName;

    private Long deliveryAddId;

    private Integer goodsCount;

    private BigDecimal goodsPrice;

    /**
     * 1pc,2android,3ios
     */
    private Integer orderChannel;

    /**
     * 订单状态：0未支付，1已支付，2已发货，3已收货，4已退款，5已完成
     */
    private Integer status;

    private Date createDate;

    private Date payDate;


}
