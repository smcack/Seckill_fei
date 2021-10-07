package com.fei.exception;

import com.fei.vo.ResBean;
import com.fei.vo.ResBeanE;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: Seckill
 * @description:
 * @author: fei
 * @create: 2021-09-29 16:09
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //所有异常都会被处理
    @ExceptionHandler(Exception.class)
    public ResBean ExceptionHandler(Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException)e;
            return ResBean.error(ex.getResBeanE());
        }else if (e instanceof BindException){
            BindException ex = (BindException)e;
            ResBean respBean = ResBean.error(ResBeanE.BIND_ERROR);
            respBean.setMessage("参数校验异常:" + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());

            return respBean;
        }

        return ResBean.error(ResBeanE.ERROR);
    }
}
