package com.fei.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fei.pojo.User;
import com.fei.service.IUserService;
import com.fei.utils.CookieUtil;
import com.fei.vo.ResBean;
import com.fei.vo.ResBeanE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

	@Autowired
	private IUserService userService;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			User user = getUser(request, response);
			UserContext.setUser(user);
			HandlerMethod hm = (HandlerMethod) handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				return true;
			}
			int second = accessLimit.second();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if (needLogin) {
				if (user == null) {
					render(response, ResBeanE.SESSION_ERROR);
					return false;
				}
				key += ":" + user.getId();
			}
			ValueOperations valueOperations = redisTemplate.opsForValue();
			Integer count = (Integer) valueOperations.get(key);
			if (count == null) {
				valueOperations.set(key, 1, second, TimeUnit.SECONDS);
			} else if (count < maxCount) {
				valueOperations.increment(key);
			} else {
				render(response, ResBeanE.ACCESS_LIMIT_REAHCED);
				return false;
			}
		}
		return true;
	}


	/**
	 * ????????????: ??????????????????
	 */
	private void render(HttpServletResponse response, ResBeanE resBeanE) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		ResBean respBean = ResBean.error(resBeanE);
		out.write(new ObjectMapper().writeValueAsString(respBean));
		out.flush();
		out.close();
	}

	/**
	 * ????????????: ??????????????????????????????
	 */
	private User getUser(HttpServletRequest request, HttpServletResponse response) {
		String ticket = CookieUtil.getCookieValue(request, "userTicket");
		if (StringUtils.isEmpty(ticket)) {
			return null;
		}
		return userService.getUserByCookie(ticket, request, response);
	}
}
