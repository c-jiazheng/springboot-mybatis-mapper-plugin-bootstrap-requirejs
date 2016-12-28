package com.zyf.admin.web.sys;

import com.alibaba.fastjson.JSONObject;
import com.zyf.common.entity.sys.Resource;
import com.zyf.common.entity.sys.Role;
import com.zyf.common.entity.sys.User;
import com.zyf.common.service.sys.ResourceService;
import com.zyf.common.service.sys.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
@Api(value = "角色管理", description = "角色增删改查")
public class RoleController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;

	@ApiOperation(value = "查看角色列表", notes = "")
	@ApiResponses({
			@ApiResponse(code = 200, message = "查询成功", response = Role.class, responseContainer = "asd"),
			@ApiResponse(code = 400, message = "查询失败"),
	})
	@RequestMapping(value = "queryList", method = RequestMethod.GET)
	@ResponseBody
	public String queryList() {
		logger.info("查询角色列表信息");
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "queryList fail");
		map.put("result", false);
		List<Role> roleList = roleService.queryList(null);
		if (roleList != null && !roleList.isEmpty()) {
			map.put("result", true);
			map.put("msg", "queryList success");
			map.put("dataList", roleList);
		}
		return JSONObject.toJSONString(map);
	}

	@ApiOperation(value = "查看角色信息", notes = "")
	@RequestMapping(value = "query", method = RequestMethod.GET)
	@ResponseBody
	public String query(long id) {
		logger.info("role id :[{}]", id);
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "query fail");
		map.put("result", false);
		Role role = roleService.query(id);
		if (role != null) {
			map.put("result", true);
			map.put("msg", "queryList success");
			map.put("data", role);
			List<Resource> allResources = resourceService.queryList();
			List<Resource> roleResources;
			if("all".equals(role.getResourceId())){
				roleResources = resourceService.queryList();
			}else{
				String[] split = role.getResourceId().split(",");
				roleResources = resourceService.queryList(Arrays.asList(split));
			}
			map.put("allResources", allResources);
			map.put("roleResources", roleResources);
		}
		return JSONObject.toJSONString(map);
	}


}
