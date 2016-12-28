package com.zyf.admin.web.common;

import com.alibaba.fastjson.JSONObject;
import com.zyf.common.entity.sys.Resource;
import com.zyf.common.service.sys.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("commons")
public class IndexController {

	@Autowired
	private ResourceService resourceService;

}
