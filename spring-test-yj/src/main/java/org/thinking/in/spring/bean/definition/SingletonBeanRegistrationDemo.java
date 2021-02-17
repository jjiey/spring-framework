package org.thinking.in.spring.bean.definition;

import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thinking.in.spring.bean.factory.DefaultUserFactory;
import org.thinking.in.spring.bean.factory.UserFactory;

/**
 * 单体 Bean 注册实例
 *
 * ConfigurableListableBeanFactory 是个综合接口
 * registerSingleton() 是在 SingletonBeanRegistry 里
 */
public class SingletonBeanRegistrationDemo {

	public static void main(String[] args) {
		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		// 创建一个外部 UserFactory 对象
		UserFactory userFactory = new DefaultUserFactory();
		SingletonBeanRegistry singletonBeanRegistry = applicationContext.getBeanFactory();
		// 注册外部单例对象
		singletonBeanRegistry.registerSingleton("userFactory", userFactory);
		// 启动 Spring 应用上下文
		applicationContext.refresh();

		// 通过依赖查找的方式来获取 UserFactory
		UserFactory userFactoryByLookup = applicationContext.getBean("userFactory", UserFactory.class);
		System.out.println("userFactory  == userFactoryByLookup : " + (userFactory == userFactoryByLookup));
		// 关闭 Spring 应用上下文
		applicationContext.close();
	}

}
