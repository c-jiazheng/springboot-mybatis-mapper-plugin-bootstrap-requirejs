package com.zyf.admin.web.common;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(value = "登录", description = "登录一些跳转")
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@ApiOperation(value="跳转到登录页面", notes="")
	@ApiResponse(code = 303, reference = "/pages/jsp/index.jsp", message = "")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@ApiOperation(value="登录", notes="根据传入的账户和密码进行shiro登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "account", value = "账户", required = true, dataType = "String"),
			@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
	})
	@ApiResponses({
			@ApiResponse(code = 201, message = "成功登录"),
			@ApiResponse(code = 400, message = "账号不存在"),
			@ApiResponse(code = 401, message = "账号禁用"),
			@ApiResponse(code = 402, message = "登录失败")
	})
	@RequestMapping(value = "/loginIn", method = RequestMethod.POST)
	@ResponseBody
	public String loginIn(String account, String password, HttpServletRequest request) {
		logger.info("account:[{}]，password:[{}]", account, password);
		Map<String, Object> map = new HashMap<>();
		String url = "/index";
		map.put("msg", "login fail");
		map.put("result", false);
		map.put("redirect", url);
		UsernamePasswordToken upt = new UsernamePasswordToken(account, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			if(subject != null && subject.getPrincipal() != null){
				subject.logout();
			}
			subject.login(upt);
			map.put("msg", "login success");
			map.put("result", true);
			SavedRequest savedRequest = null;
			if((savedRequest = WebUtils.getAndClearSavedRequest(request)) != null){
				url = savedRequest.getRequestUrl().replaceFirst(request.getContextPath(), "");
				map.put("redirect", url);
			}
		} catch (IncorrectCredentialsException e) {
			map.put("msg", "input account or password fail, try again");
		} catch (AuthenticationException e) {
			map.put("msg", "input account or password fail, try again");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toJSONString(map);
	}

	@ApiOperation(value="退出登录", notes="跳转到登录页面，如果已经登录则退出登录")
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		if(subject != null && subject.getPrincipal() != null){
			subject.logout();
		}
		return "index";
	}

	@ApiOperation(value="一键登录", notes="")
	@ApiResponses({
			@ApiResponse(code = 201, message = "成功登录", responseContainer = ""),
			@ApiResponse(code = 400, message = "账号不存在"),
			@ApiResponse(code = 401, message = "账号禁用"),
			@ApiResponse(code = 402, message = "登录失败")
	})
	@RequestMapping(value = "/onceLoginIn", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String onceLoginIn(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String url = "/index";
		map.put("msg", "loginIn fail");
		map.put("result", false);
		UsernamePasswordToken upt = new UsernamePasswordToken("a", "a");
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(upt);
			map.put("msg", "loginIn success");
			map.put("result", true);
			SavedRequest savedRequest = null;
			if((savedRequest = WebUtils.getAndClearSavedRequest(request)) != null){
				url = savedRequest.getRequestUrl().replaceFirst(request.getContextPath(), "");
				map.put("redirect", url);
			}
		} catch (IncorrectCredentialsException e) {
			map.put("msg", "input account or password fail, try again");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			map.put("msg", "input account or password fail, try again");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toJSONString(map);
	}
}
