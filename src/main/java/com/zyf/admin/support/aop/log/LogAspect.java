package com.zyf.admin.support.aop.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;

@Aspect
@Component
@Order(9)   //切面优先级
public class LogAspect {

	private static final String LOG_CONTENT = "[类名]:%s,[方法]:%s,[参数]:%s";

	private List<String> exclude;

	@Around("@annotation(com.zyf.admin.support.aop.log.Log)")
	public Object saveLog(ProceedingJoinPoint joinPoint) throws Throwable {
		Object proceed;
		String methodName = joinPoint.getSignature().getName();
		Method method = currentMethod(joinPoint, methodName);
		Log log = (Log) method.getAnnotation(Log.class);
		if (log != null) {
			long start = System.currentTimeMillis();
			//执行方法前逻辑
			proceed = joinPoint.proceed();//返回值有三中，String ，ModelView，其中String还包含json
			//执行方法后逻辑
			long end = System.currentTimeMillis();
		} else {
			proceed = joinPoint.proceed();
		}
		return proceed;
	}

	/**
	 * 获取当前执行的方法
	 *
	 * @param joinPoint  连接点
	 * @param methodName 方法名称
	 * @return
	 */
	private Method currentMethod(ProceedingJoinPoint joinPoint, String methodName) {
		Method[] methods = joinPoint.getTarget().getClass().getMethods();
		Method resultMethod = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				resultMethod = method;
				break;
			}
		}
		return resultMethod;
	}

	/**
	 * 获取当前操作内容
	 *
	 * @param joinPoint  连接点
	 * @param methodName 方法名称
	 * @return 操作内容
	 */
	public String operateContent(ProceedingJoinPoint joinPoint, String methodName, HttpServletRequest request) {
		String className = joinPoint.getTarget().getClass().getName();
		Object[] params = joinPoint.getArgs();
		StringBuffer bf = new StringBuffer();
		if (params != null && params.length > 0) {
			Enumeration<String> paraNames = request.getParameterNames();
			while (paraNames.hasMoreElements()) {
				String key = paraNames.nextElement();
				if (!exclude.isEmpty() && exclude.contains(key)) {
					continue;
				}
				bf.append(key).append("=");
				bf.append(request.getParameter(key)).append("&");
			}
			int lastIndexOf = bf.lastIndexOf("&");
			if (lastIndexOf > -1 && lastIndexOf == (bf.length() - 1)) {
				bf.deleteCharAt(lastIndexOf);
			}
			/*if (StringUtils.isBlank(bf.toString())) {
				bf.append(request.getQueryString());
			}*/
		}
		return String.format(LOG_CONTENT, className, methodName, bf.toString());
	}

	public List<String> getExclude() {
		return exclude;
	}

	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}

}