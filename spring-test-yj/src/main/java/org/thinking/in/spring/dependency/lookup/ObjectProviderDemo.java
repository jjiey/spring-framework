package org.thinking.in.spring.dependency.lookup;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.thinking.in.spring.ioc.domain.User;

/**
 * 通过 {@link ObjectProvider} 进行依赖查找
 *
 * ObjectFactory: ObjectFactoryCreatingFactoryBean.getObject() return ObjectFactory, see DependencyLookupDemo#lookupInLazy()   这个是 FactoryBean#getObject()
 */
public class ObjectProviderDemo { // @Configuration 是非必须注解

	public static void main(String[] args) {
		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 将当前类 ObjectProviderDemo 作为配置类（Configuration Class）
		applicationContext.register(ObjectProviderDemo.class);
		// 启动应用上下文
		applicationContext.refresh();

		// 依赖查找集合对象
		lookupByObjectProvider(applicationContext);
		lookupIfAvailable(applicationContext);
		lookupByStreamOps(applicationContext);

		// 关闭应用上下文
		applicationContext.close();
	}

	private static void lookupByStreamOps(AnnotationConfigApplicationContext applicationContext) {
		ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
		// ObjectProvider extends Iterable
        Iterable<String> stringIterable = objectProvider;
        for (String string : stringIterable) {
            System.out.println(string);
        }
		// Stream -> Method reference
		objectProvider.stream().forEach(System.out::println);
	}

	/**
	 * ObjectProvider#getIfAvailable demo
	 */
	private static void lookupIfAvailable(AnnotationConfigApplicationContext applicationContext) {
		ObjectProvider<User> userObjectProvider = applicationContext.getBeanProvider(User.class);
		User user = userObjectProvider.getIfAvailable(User::createUser);
		System.out.println("当前 User 对象：" + user);
	}

	@Bean
	@Primary
	public String helloWorld() { // 方法名就是 Bean 名称 = "helloWorld"
		return "Hello, World";
	}

	@Bean
	public String message() {
		return "Message";
	}

	private static void lookupByObjectProvider(AnnotationConfigApplicationContext applicationContext) {
		ObjectProvider<String> objectProvider = applicationContext.getBeanProvider(String.class);
		// ObjectFactory#getObject() 不是 FactoryBean#getObject()
		System.out.println(objectProvider.getObject());
	}
}
