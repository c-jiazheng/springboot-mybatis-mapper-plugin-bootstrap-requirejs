package com.zyf.framework.utils;

import org.springframework.web.util.JavaScriptUtils;

import java.util.Collection;
import java.util.Random;

/**
 * 字符串操作
 *
 * @author L.cm
 * @email: 596392912@qq.com
 * @site: http://www.dreamlu.net
 * @date 2015年4月20日下午8:18:02
 */
public class StringUtils extends org.springframework.util.StringUtils {

	/**
	 * isNotEmpty
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * isBlank
	 *
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		return !hasText(cs);
	}

	/**
	 * isNotBlank
	 *
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return hasText(cs);
	}

	private static final String _INT = "0123456789";
	private static final String _STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String _ALL = _INT + _STR;

	private static final Random RANDOM = new Random();

	/**
	 * 生成的随机数类型
	 *
	 * @author L.cm
	 * @email: 596392912@qq.com
	 * @site: http://www.dreamlu.net
	 * @date 2015年4月20日下午9:15:23
	 */
	public static enum RandomType {
		INT, STRING, ALL;
	}

	/**
	 * 随机数生成
	 *
	 * @param count
	 * @return
	 */
	public static String random(int count, RandomType randomType) {
		if (count == 0) return "";
		if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
		}
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			if (randomType.equals(RandomType.INT)) {
				buffer[i] = _INT.charAt(RANDOM.nextInt(_INT.length()));
			} else if (randomType.equals(RandomType.STRING)) {
				buffer[i] = _STR.charAt(RANDOM.nextInt(_STR.length()));
			} else {
				buffer[i] = _ALL.charAt(RANDOM.nextInt(_ALL.length()));
			}
		}
		return new String(buffer);
	}

	/**
	 * join
	 *
	 * @param coll
	 * @param delim
	 * @return
	 */
	public static String join(Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim);
	}

	/**
	 * join
	 *
	 * @param arr
	 * @param delim
	 * @return
	 */
	public static String join(Object[] arr, String delim) {
		return arrayToDelimitedString(arr, delim);
	}

	public static void main(String[] args) {
		System.out.println(JavaScriptUtils.javaScriptEscape("<script>alert('hello')</script>"));
	}
}
