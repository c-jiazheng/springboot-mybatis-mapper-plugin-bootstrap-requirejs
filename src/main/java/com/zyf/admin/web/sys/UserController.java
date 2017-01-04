package com.zyf.admin.web.sys;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
@Api(value = "用户管理", description = "用户增删改查")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@ApiOperation(value = "查看用户列表", notes = "")
	@ApiResponses({
			@ApiResponse(code = 200, message = "查询成功", response = User.class, responseContainer = "asd"),
			@ApiResponse(code = 400, message = "查询失败"),
	})
	@RequestMapping(value = "queryList", method = RequestMethod.GET)
	@ResponseBody
	public String queryList(@RequestParam(required = false, name = "pn", defaultValue = "0") int pn,
	                        @RequestParam(required = false, name = "ps", defaultValue = "10") int ps) {
		logger.info("查询用户信息");
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "queryList fail");
		map.put("result", false);
		PageHelper.startPage(pn, ps);
		List<User> userList = userService.queryList();
		PageInfo<User> pages = new PageInfo<>(userList);
		if (userList != null && !userList.isEmpty()) {
			map.put("result", true);
			map.put("msg", "queryList success");
			map.put("pages", pages);
			map.put("draw", 1);
		}
		return JSONObject.toJSONString(map);
	}

	@ApiOperation(value = "查看用户信息", notes = "根据账户查询用户信息")
	@ApiImplicitParam(name = "account", value = "账户", required = true, dataType = "String")
	@ApiResponses({
			@ApiResponse(code = 200, message = "查询成功"),
			@ApiResponse(code = 400, message = "查询失败"),
			@ApiResponse(code = 401, message = "账号不存在"),
			@ApiResponse(code = 402, message = "账号被冻结")
	})
	@RequestMapping(value = "query", method = RequestMethod.OPTIONS)
	@ResponseBody
	public String query(String account) {
		logger.info("account:[{}]", account);
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "common fail");
		map.put("result", false);
		User user = userService.queryByAccont(account);
		map.put("user", user);
		return JSONObject.toJSONString(map);
	}

	@ApiOperation(value = "添加用户", notes = "添加用户信息")
	@ApiImplicitParam(name = "用户实体", value = "用户信息", required = true, paramType = "User.class")
	@ApiResponses({
			@ApiResponse(code = 200, message = "查询成功"),
			@ApiResponse(code = 400, message = "查询失败"),
			@ApiResponse(code = 401, message = "账号不存在"),
			@ApiResponse(code = 402, message = "账号被冻结")
	})
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	public String add(User user) {
		logger.info("user:[{}]", user);
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "add user fail");
		map.put("result", false);
		int effect = userService.insert(user);
		if(effect>0){
			map.put("msg", "add user success");
			map.put("result", true);
		}
		return JSONObject.toJSONString(map);
	}


	@ApiOperation(value = "修改用户", notes = "修改用户信息")
	@ApiImplicitParam(name = "用户实体", value = "用户信息", required = true, paramType = "User.class")
	@ApiResponses({
			@ApiResponse(code = 200, message = "修改成功"),
			@ApiResponse(code = 400, message = "修改失败"),
			@ApiResponse(code = 401, message = "账号不存在"),
			@ApiResponse(code = 402, message = "账号被冻结")
	})
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String update(User user) {
		logger.info("user:[{}]", user);
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "update user fail");
		map.put("result", false);
		int effect = userService.update(user);
		if(effect>0){
			map.put("msg", "update user success");
			map.put("result", true);
		}
		return JSONObject.toJSONString(map);
	}

}
