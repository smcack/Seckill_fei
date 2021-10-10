package com.fei.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**秒杀信息
 * @author ChenweiLin
 * @create 2021-08-08 21:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {

    private User user;
    private Long goodsId;
}
