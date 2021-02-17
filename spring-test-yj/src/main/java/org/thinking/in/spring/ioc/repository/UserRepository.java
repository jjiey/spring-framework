package org.thinking.in.spring.ioc.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.thinking.in.spring.ioc.domain.User;

import java.util.Collection;

/**
 * 用户信息仓库
 */
@Getter
@Setter
public class UserRepository {

	/**
	 * 自定义 Bean
	 */
	private Collection<User> users;

	/**
	 * 內建非 Bean 对象（依赖）
	 */
	private BeanFactory beanFactory;

	private ObjectFactory<ApplicationContext> objectFactory;
}
