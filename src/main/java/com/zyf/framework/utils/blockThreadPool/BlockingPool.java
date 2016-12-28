package com.zyf.framework.utils.blockThreadPool;

/**
 *
 * Created by zengyufei on 2016/11/25.
 */
public interface BlockingPool<T> extends Pool<T> {

	/**
	 * it valid that not working, release resource
	 */
	void valid();

	void release(T t);

	void releaseAll();
}
