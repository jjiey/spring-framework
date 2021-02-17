package org.thinking.in.spring.bean.factory;

import org.springframework.beans.factory.FactoryBean;
import org.thinking.in.spring.ioc.domain.User;

/**
 * {@link User} Bean 的 {@link FactoryBean} 实现
 */
public class UserFactoryBean implements FactoryBean {

	@Override
	public Object getObject() throws Exception {
		return User.createUser();
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}
}
