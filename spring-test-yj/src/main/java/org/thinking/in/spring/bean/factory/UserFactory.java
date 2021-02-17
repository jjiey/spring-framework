package org.thinking.in.spring.bean.factory;

import org.thinking.in.spring.ioc.domain.User;

/**
 * {@link User} 工厂类
 */
public interface UserFactory {

	default User createUser() {
		return User.createUser();
	}
}
