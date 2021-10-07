package com.fei.exception;

import com.fei.vo.ResBeanE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: Seckill
 * @description: 全局异常
 * @author: fei
 * @create: 2021-09-29 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private ResBeanE resBeanE;
}