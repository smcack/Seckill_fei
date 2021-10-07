package com.fei.vo;

import com.fei.Validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @program: Seckill
 * @description: 登录参数
 * @author: fei
 * @create: 2021-09-23 17:28
 */
@Data
public class LoginVo {
    @IsMobile
    @NotNull
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}
