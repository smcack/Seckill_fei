package com.fei.vo;

import com.fei.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: Seckill
 * @description: 商品返回对象
 * @author: fei
 * @create: 2021-09-30 10:39
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo  extends Goods {

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}
