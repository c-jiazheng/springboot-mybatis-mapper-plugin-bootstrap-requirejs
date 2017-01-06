package com.zyf.admin.support.valid.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collection;

/**
 * 响应对象
 *
 * @author chaijunkun
 * @since 2015年4月3日
 */
@JsonPropertyOrder(alphabetic = false)
public class Resp<T> {

	/**
	 * 生成成功返回对象
	 *
	 * @param msg
	 * @return
	 */
	public static <T> Resp<T> success(T msg) {
		Resp<T> resp = new Resp<T>();
		resp.setCode(200);
		resp.setMsg(msg);
		return resp;
	}

	/**
	 * 生成失败返回对象
	 *
	 * @param msg
	 * @return
	 */
	public static Resp<Object> fail(String msg) {
		Resp<Object> resp = new Resp<>();
		resp.setCode(400);
		resp.setMsg(msg);
		return resp;
	}

	/**
	 * 生成失败返回对象
	 *
	 * @param msg
	 * @return
	 */
	public static <T> Resp<T> fail(T msg) {
		Resp<T> resp = new Resp<>();
		resp.setCode(400);
		if(msg instanceof String){
			resp.setMsg(msg);
		}else if(msg instanceof Collection){
			resp.setMsg(msg);
		}
		return resp;
	}

	/** 响应代码 */
	private Integer code;

	/** 响应消息 */
	private T msg;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public T getMsg() {
		return msg;
	}

	public void setMsg(T msg) {
		this.msg = msg;
	}
}