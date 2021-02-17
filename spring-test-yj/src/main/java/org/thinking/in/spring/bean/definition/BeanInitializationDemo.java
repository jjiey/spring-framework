package org.thinking.in.spring.bean.definition;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.thinking.in.spring.bean.factory.DefaultUserFactory;
import org.thinking.in.spring.bean.factory.UserFactory;

/**
 * Bean 初始化 Demo
 *
 * init order:
 * 	@PostConstruct > InitializingBean#afterPropertiesSet() > initMethod
 * destroy order: (applicationContext.close() 触发)
 * 	@PreDestroy > DisposableBean#destroy() > destroyMethod
 *
 * DisposableBean#destroy() 触发时机：
 * 	applicationContext.close() -> doClose() -> destroyBeans() -> destroySingletons() -> super.destroySingletons() -> destroySingleton(String beanName) -> 强转为 DisposableBean -> destroyBean() 里调用 destroy()
 */
@Configuration // Configuration Class
public class BeanInitializationDemo {

	public static void main(String[] args) {
		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 注册 Configuration Class（配置类）
		applicationContext.register(BeanInitializationDemo.class);
		// 启动 Spring 应用上下文
		applicationContext.refresh();
		// 非延迟初始化在 Spring 应用上下文启动完成后，被初始化
		System.out.println("Spring 应用上下文已启动...");

		// 依赖查找 UserFactory
		UserFactory userFactory = applicationContext.getBean(UserFactory.class);
		System.out.println(userFactory);

		// 关闭 Spring 应用上下文
		System.out.println("Spring 应用上下文准备关闭...");
		applicationContext.close();
		System.out.println("Spring 应用上下文已关闭...");
	}

	@Bean(initMethod = "initUserFactory", destroyMethod = "doDestroy")
	@Lazy(value = false)
	public UserFactory userFactory() {
		return new DefaultUserFactory();
	}
}
