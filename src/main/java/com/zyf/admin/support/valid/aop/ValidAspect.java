package com.zyf.admin.support.valid.aop;

import com.alibaba.fastjson.JSONObject;
import com.zyf.admin.support.valid.response.Resp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(8)   //切面优先级
public class ValidAspect {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	private void pointCut() {
	}

	@Around("pointCut()")
	public Object valid(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length == 0) {
			Object proceed = joinPoint.proceed();
			return proceed;
		}
		BindingResult result = null;
		if((result = getBindingResult(joinPoint)) == null && !hasValidated(joinPoint)){
			Object proceed = joinPoint.proceed();
			return proceed;
		}else{
			if(result == null){
				log.error(this.getClazz(joinPoint) + " 方法缺少 BindingResult 形参");
				Object proceed = joinPoint.proceed();
				return proceed;
			}else if(!hasValidated(joinPoint)){
				log.error(this.getClazz(joinPoint) + " 方法形参缺少 @Validated 注解");
				Object proceed = joinPoint.proceed();
				return proceed;
			}else{
				//验证业务
				if (result != null && result.hasErrors()) {
					FieldError fieldError = result.getFieldError();
					String firstCode = fieldError.getCode();
					if (isMissingParamsError(firstCode)) {
						return JSONObject.toJSONString(Resp.fail(fieldError.getDefaultMessage()));
					} else {
						return JSONObject.toJSONString(Resp.fail(fieldError.getDefaultMessage()));
					}
				}
				return joinPoint.proceed();
			}
		}

	}

	/**
	 * 判断方法形参集合中是否存在 Validated
	 *
	 * @param joinPoint 连接点
	 * @return
	 */
	public boolean hasValidated(ProceedingJoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		Method method = this.currentMethod(joinPoint, methodName);
		Annotation[][] an = method.getParameterAnnotations();
		if(an.length>0){
			for(int i=0;i<an.length;i++){
				for(int j=0;j<an[i].length;j++){
					if(an[i][j] instanceof Validated){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断方法形参集合中是否存在 BindingResult
	 * @param joinPoint 连接点
	 * @return
	 */
	public boolean hasBindingResult(ProceedingJoinPoint joinPoint) {
		for (Object obj : joinPoint.getArgs())
			if (obj instanceof BindingResult)
				return true;
		return false;
	}

	/**
	 * getBindingResult
	 *
	 * @param joinPoint 连接点
	 * @return
	 */
	public BindingResult getBindingResult(ProceedingJoinPoint joinPoint) {
		for (Object obj : joinPoint.getArgs())
			if (obj instanceof BindingResult)
				return (BindingResult) obj;
		return null;
	}

	/**
	 * 获取当前执行的方法的形参类型
	 *
	 * @param joinPoint 连接点
	 * @param clazz     需要判断的类型
	 * @return
	 */
	public Object getArg(ProceedingJoinPoint joinPoint, Class clazz) {
		for (Object obj : joinPoint.getArgs())
			if (obj instanceof BindingResult)
				return (BindingResult) obj;
		return null;
	}

	/**
	 * 获取当前执行的方法的形参集合
	 *
	 * @param joinPoint 连接点
	 * @return
	 */
	public Object[] getArgs(ProceedingJoinPoint joinPoint) {
		return joinPoint.getArgs();
	}

	/**
	 * 判断验证错误代码是否属于字段为空的情况
	 *
	 * @param code 验证错误代码
	 */
	private boolean isMissingParamsError(String code) {
		if (code.equals(NotNull.class.getSimpleName()) || code.equals(NotBlank.class.getSimpleName()) || code.equals(NotEmpty.class.getSimpleName()))
			return true;
		else
			return false;
	}

	/**
	 * 获取当前方法的类
	 *
	 * @param joinPoint  连接点
	 * @return
	 */
	private Class getClazz(ProceedingJoinPoint joinPoint) {
		return joinPoint.getTarget().getClass();
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
		for (Method method : methods)
			if (method.getName().equals(methodName)) {
				resultMethod = method;
				break;
			}
		return resultMethod;
	}


	/*
	手动验证
	* protected static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<QueryParam>> commonValidate = validator.validate(param, CommonGroup.class);
		if (CollectionUtils.isNotEmpty(commonValidate)){
		    throw new IllegalArgumentException(commonValidate.iterator().next().getMessage());
		}
	* */

}