package com.zyf.admin.support.valid.annotation;

import com.zyf.admin.support.valid.validator.BetweenImplForCalendar;
import com.zyf.admin.support.valid.validator.BetweenImplForDate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
import javax.validation.Constraint;  
import javax.validation.Payload;  
  
@Documented  
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD})  
@Constraint(validatedBy = {BetweenImplForCalendar.class, BetweenImplForDate.class})
public @interface Between {  
  
    public String message();  
  
    public String startDate();  
  
    public String endDate();  
  
    public String format() default "yyyy-MM-dd";  
  
    public Class<?>[] groups() default {};  
  
    Class<? extends Payload>[] payload() default {};  
}  