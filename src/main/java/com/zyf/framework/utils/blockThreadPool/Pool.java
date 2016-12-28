package com.zyf.framework.utils.blockThreadPool;

import java.util.concurrent.TimeUnit;

/**
 * 队列线程池接口
 * Created by zengyufei on 2016/11/25.
 */
public interface Pool<T> {

	T get();

	T get(long time, TimeUnit timeUnit);

}
