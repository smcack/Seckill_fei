package com.fei.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Seckill
 * @description: 公共返回对象
 * @author: fei
 * @create: 2021-09-23 11:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResBean {
    private long code;
    private String message;
    private Object obj;

    //四个静态方法返回的都是ResBean对象

    /**
     * 功能描述: 成功返回结果
     */
    public static ResBean success(){
        return new ResBean(ResBeanE.SUCCESS.getCode(),ResBeanE.SUCCESS.getMessage(),null);
    }

    /**重载
     * 功能描述: 成功返回结果
     */
    public static ResBean success(Object obj){
        return new ResBean(ResBeanE.SUCCESS.getCode(),ResBean.success().getMessage(),obj);
    }


    /**重载F
     * 功能描述: 失败返回结果
     */
    public static ResBean error(ResBeanE ResBeanE){
        return new ResBean(ResBeanE.getCode(),ResBeanE.getMessage(),null);
    }


    /**
     * 功能描述: 失败返回结果
     *
     */
    public static ResBean error(ResBeanE ResBeanE,Object obj){
        return new ResBean(ResBeanE.getCode(),ResBeanE.getMessage(),obj);
    }

}
