package com.zyf.admin.support.valid.validator;

import com.zyf.admin.support.valid.annotation.Between;
import com.zyf.framework.utils.DateJodaTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class BetweenImplForCalendar implements ConstraintValidator<Between, Calendar> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BetweenImplForCalendar.class);

	private Calendar startDate;
	private Calendar endDate;

	public void initialize(Between annotation) {
		this.startDate = DateJodaTimeUtils.stringToCalendar(annotation.startDate(), annotation.format());
		this.endDate = DateJodaTimeUtils.stringToCalendar(annotation.endDate(), annotation.format());
	}

	public boolean isValid(Calendar value, ConstraintValidatorContext context) {
		return (startDate.getTimeInMillis() <= value.getTimeInMillis()) && (value.getTimeInMillis() < endDate.getTimeInMillis());
	}
}  