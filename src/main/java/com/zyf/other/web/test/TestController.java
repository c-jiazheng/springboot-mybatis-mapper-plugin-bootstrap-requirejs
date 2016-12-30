package com.zyf.other.web.test;

import com.alibaba.fastjson.JSONObject;
import com.zyf.common.entity.sys.User;
import com.zyf.framework.exception.MyException;
import com.zyf.framework.utils.EHCacheUtils;
import com.zyf.framework.utils.PathUtils;
import com.zyf.framework.utils.zip.ZipUtils;
import com.zyf.other.rabbitmq.Sender;
import io.swagger.annotations.*;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("test")
@Api(value = "测试使用的接口", description = "测试接口都写这")
public class TestController {

	@Autowired
	private AmqpTemplate rabbitTemplate;
	@Autowired
	private Sender sender;

	@ApiOperation(value="测试下载功能", notes="直接根据URL进行下载测试")
	@RequestMapping(value = "down", method = RequestMethod.GET)
	@ResponseBody
	public void down(HttpServletResponse response) throws IOException {
		String path = PathUtils.getPath("static/ehcache/ehcache.xml");
		ZipUtils.down(new File(path), "ss.zip", response);
	}

	@ApiOperation(value="测试缓存存取功能", notes="直接根据URL进行缓存测试")
	@RequestMapping(value = "cache", method = RequestMethod.GET)
	@ResponseBody
	public String cache() throws IOException {
		//测试将json对象存入缓存中
		JSONObject obj = new JSONObject();
		obj.put("name", "lsz");
		EHCacheUtils.addToCache("cache_json", obj);
		//从缓存中获取
		JSONObject getobj = (JSONObject) EHCacheUtils.getCacheElement("cache_json");
		return getobj.toString();
	}

	@ApiOperation(value="测试全局异常抛出错误统一处理", notes="直接根据URL进行测试")
	@RequestMapping(value = "error",method = RequestMethod.GET)
	public void error() throws Exception {
		throw new Exception("错误");
	}

	@ApiOperation(value="测试全局异常抛出自定义错误处理", notes="直接根据URL进行测试")
	@RequestMapping(value = "error2",method = RequestMethod.GET)
	@ResponseBody
	public void error2() throws Exception {
		throw new MyException("错误2");
	}

	@ApiOperation(value="测试thymeleaf模板", notes="直接根据URL进行测试，需要在配置文件中打开thymeleaf支持，thymeleaf优先级比jsp高")
	@ApiImplicitParam
	@RequestMapping(value = "/thymeleaf", method = RequestMethod.GET)
	public String testThymeleaf(ModelMap map) {
		// 加入一个属性，用来在模板中读取
		map.addAttribute("host", "http://blog.didispace.com");
		// return模板文件的名称，对应src/main/resources/templates/index.html
		return "index";
	}

	@ApiOperation(value = "测试MQ访问方式1", notes = "通过直接访问的方式，跟/mq2访问不同在于使用代码的方式上")
	@ApiResponse(code = 200, message = "返回传输的方式及时间")
	@RequestMapping(value = "/mq", method = RequestMethod.GET)
	@ResponseBody
	public String send() {
		String context = "default hello " + new Date();
		this.rabbitTemplate.convertAndSend("hello", context);
		return "Sender default : " + context;
	}

	@ApiOperation(value = "测试MQ访问方式2", notes = "通过直接访问的方式，跟/mq访问不同在于使用代码的方式上")
	@ApiResponse(code = 200, message = "返回传输的方式及时间")
	@RequestMapping(value = "/mq2", method = RequestMethod.GET)
	@ResponseBody
	public String send2() {
		String content = sender.send("controller");
		return content;
	}

	@ApiOperation(value = "测试JSR303验证参数", notes = "通过直接访问/test/valid")
	@ApiResponse(code = 200, message = "错误列表")
	@RequestMapping(value = "/valid", method = RequestMethod.GET)
	@ResponseBody
	public String valid(@Valid User user, BindingResult result) {
		System.out.println(user.getAccount());
		if(result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println(error.getCode() + "---" + error.getArguments() + "---" + error.getDefaultMessage());
			}
		}
		if("admin".equals(user.getAccount()))
			result.rejectValue("account", "exists", "这个account已经存在了！");
		//return JSONObject.toJSONString(result.getFieldErrors());
		//return JSONObject.toJSONString(result.getFieldError("account"));
		if(result.hasErrors())
			return JSONObject.toJSONString(result.getAllErrors());
		else
			return JSONObject.toJSONString("not error");
	}


}
