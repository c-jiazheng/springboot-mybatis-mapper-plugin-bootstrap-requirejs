package com.zyf.framework.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zengyufei on 2017/1/4/004.
 * QQ: 312636208
 */
public class MessageUtils {

	private static final String success_msg = "success";

	public static Map<String,Object> toMap(BindingResult result) {
		Map<String,Object> map = new HashMap<>();
		if(result.hasErrors()) {
			int code = 400;
			List<String> list = new ArrayList<>();
			List<ObjectError> errorList = result.getAllErrors();
			map.put("errorSize", errorList.size());
			if(errorList.size() == 1){
				map.put("msg", errorList.get(0).getDefaultMessage());
			}else{
				for (ObjectError error : errorList) {
					list.add(error.getDefaultMessage());
					code++;
				}
				map.put("msg", list);
			}
			map.put("code", code);
		}else{
			map.put("code", 200);
		}
		return map;
	}

	public static String parse(BindingResult result) {
		return MessageUtils.parse(result, null);
	}

	public static String parse(BindingResult result, String successMsg) {
		Map<String,Object> map = new HashMap<>();
		if(result.hasErrors()) {
			int code = 400;
			List<String> list = new ArrayList<>();
			List<ObjectError> errorList = result.getAllErrors();
			for (ObjectError error : errorList) {
				list.add(error.getDefaultMessage());
				code++;
			}
			map.put("code", code);
			map.put("msg", list);
		}else{
			map.put("code", 200);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(successMsg))
				map.put("msg", successMsg);
			else
				map.put("msg", success_msg);
		}
		return JSONObject.toJSONString(map);
	}

}
