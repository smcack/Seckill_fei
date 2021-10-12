package com.fei.config;


import com.fei.pojo.User;
/**
 * @author zhoubin
 * @since 1.0.0
 */
public class UserContext {
    //将当前用户存入各自的当前线程，
    //可理解为一个放东西的盒子
    //通过下面的get/set方法来设置/获取值
	private static ThreadLocal<User> userHolder = new ThreadLocal<User>();

	public static void setUser(User user) {
		userHolder.set(user);
	}

	public static User getUser() {
		return userHolder.get();
	}
}
