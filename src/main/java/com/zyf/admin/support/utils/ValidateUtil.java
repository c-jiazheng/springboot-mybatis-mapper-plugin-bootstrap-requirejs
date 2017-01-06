package com.zyf.admin.support.utils;

import org.hibernate.validator.HibernateValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：ej 	<br><br>
 * <p>
 * 类名称：ValidateUtil 		<br><br>
 * <p>
 * 创建人：LinApex@163.com 	<br><br>
 * <p>
 * 创建时间：2014-2-26 下午1:10:03 	<br><br>
 * <p>
 * 版本：1.0					<br><br>
 * <p>
 * 功能描述：验证工具类，后台校验对象
 */
public class ValidateUtil {
	static Validator validator;

	static {
		//消息国际化对象
		//		ReloadableResourceBundleMessageSource localMessageSource = new ReloadableResourceBundleMessageSource();
		//		localMessageSource.setBasename("classpath:i18n/messages");
		//		localMessageSource.setDefaultEncoding("UTF-8");
		//		localMessageSource.setCacheSeconds(60);

		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
		//		localValidatorFactoryBean.setValidationMessageSource(localMessageSource);	//消息国际化对象
		localValidatorFactoryBean.afterPropertiesSet();
		validator = localValidatorFactoryBean.getValidator();
	}

	public static <T> Map<String, String> validate(T t) {
		Map<String, String> resultMap = new HashMap<>(0);
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<T> constraintViolation : constraintViolations) {
				resultMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
			}
		}
		return resultMap;
	}

}