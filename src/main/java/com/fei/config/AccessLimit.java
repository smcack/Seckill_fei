package com.fei.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 乐字节：专注线上IT培训
 * 答疑老师微信：lezijie
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)//运行时
@Target(ElementType.METHOD)//放在方法上
public @interface AccessLimit {

	int second();

	int maxCount();

	boolean needLogin() default true;
}
