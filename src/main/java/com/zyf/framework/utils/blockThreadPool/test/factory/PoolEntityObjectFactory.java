package com.zyf.framework.utils.blockThreadPool.test.factory;

import com.zyf.common.entity.sys.User;
import com.zyf.framework.utils.blockThreadPool.ObjectFactory;

public class PoolEntityObjectFactory implements ObjectFactory {

		@Override
		public Object create() {
			return new User();
		}
	}