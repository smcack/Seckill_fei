package com.fei.utils;

import java.util.UUID;

/**
 * @program: Seckill
 * @description: UUID工具类
 * @author: fei
 * @create: 2021-09-29 16:22
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
