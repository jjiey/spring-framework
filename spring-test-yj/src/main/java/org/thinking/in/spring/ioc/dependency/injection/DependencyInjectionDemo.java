package org.thinking.in.spring.ioc.dependency.injection;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.thinking.in.spring.ioc.repository.UserRepository;

/**
 * 依赖注入示例
 */
public class DependencyInjectionDemo {

	public static void main(String[] args) {
		// 配置 XML 配置文件
		// 启动 Spring 应用上下文
//        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-injection-context.xml");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-injection-context.xml");

		// 依赖来源一：自定义 Bean
		UserRepository userRepository = applicationContext.getBean("userRepository", UserRepository.class);
		System.out.println(userRepository.getUsers());

		// 依赖来源二：依赖注入（內建依赖）
		System.out.println(userRepository.getBeanFactory());
		whoIsIoCContainer(userRepository, applicationContext);

		ObjectFactory userFactory = userRepository.getObjectFactory();
		System.out.println("userFactory.getObject() == applicationContext: " + (userFactory.getObject() == applicationContext));

		// 依赖查找（错误）：No qualifying bean of type 'org.springframework.beans.factory.BeanFactory' available
//        System.out.println(beanFactory.getBean(BeanFactory.class));

		// 依赖来源三：容器內建 Bean
		Environment environment = applicationContext.getBean(Environment.class);
		System.out.println("获取 Environment 类型的 Bean：" + environment);
	}

	/**
	 * BeanFactory 和 ApplicationContext 谁才是 Spring IoC 容器?
	 */
	private static void whoIsIoCContainer(UserRepository userRepository, ApplicationContext applicationContext) {
		// ConfigurableApplicationContext extends ApplicationContext extends BeanFactory
		// ConfigurableApplicationContext is BeanFactory, ConfigurableApplicationContext#getBeanFactory(), why?

		// 这个表达式为什么不会成立
		// 因为前者是 AbstractRefreshableApplicationContext 里的 DefaultListableBeanFactory，后者是 ClassPathXmlApplicationContext。尽管复用了同一个接口，但底层实现上毕竟是两个对象
		System.out.println(userRepository.getBeanFactory() == applicationContext);
	}

}
