package com.zyf.admin.web.sys;

import com.alibaba.fastjson.JSONObject;
import com.zyf.common.entity.sys.Resource;
import com.zyf.common.service.sys.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("resource")
public class ResourceController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ResourceService resourceService;

	@RequestMapping("/index")
	public String index() {
		return "sys/role/index";
	}

	@RequestMapping("queryList")
	@ResponseBody
	public String queryList(){
		return JSONObject.toJSONString("");
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	@ResponseBody
	public String insert(Resource resource){
		int effect = resourceService.insert(resource);
		if(effect > 0){

		}else{

		}
		return JSONObject.toJSONString("");
	}
}
