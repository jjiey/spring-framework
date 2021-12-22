package org.thinking.in.spring.questions;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.thinking.in.spring.ioc.domain.User;

/**
 * {@link ObjectFactory} 延迟依赖查找示例
 *
 * @see ObjectFactory
 * @see ObjectProvider
 */
public class ObjectFactoryLazyLookupDemo {

	@Autowired
	private ObjectFactory<User> userObjectFactory;

	@Autowired
	private ObjectProvider<User> userObjectProvider;

	@Bean
	@Lazy
	public static User user() {
		User user = new User();
		user.setId(1L);
		user.setName("小马哥");
		return user;
	}

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class
        context.register(ObjectFactoryLazyLookupDemo.class);
        // 启动 Spring 应用上下文
        context.refresh();

        ObjectFactoryLazyLookupDemo objectFactoryLazyLookupDemo = context.getBean(ObjectFactoryLazyLookupDemo.class);

        // userObjectFactory userObjectProvider;

        // 代理对象
        ObjectFactory<User> userObjectFactory = objectFactoryLazyLookupDemo.userObjectFactory;
        ObjectFactory<User> userObjectProvider = objectFactoryLazyLookupDemo.userObjectProvider;

        // false
        System.out.println("userObjectFactory == userObjectProvider : " +
				(userObjectFactory == userObjectProvider));
        // true
        System.out.println("userObjectFactory.getClass() == userObjectProvider.getClass() : " +
				(userObjectFactory.getClass() == userObjectProvider.getClass()));

        // 实际对象（延迟查找）
        System.out.println("user = " + userObjectFactory.getObject());
        System.out.println("user = " + userObjectProvider.getObject());
        System.out.println("user = " + context.getBean(User.class));

        // 关闭 Spring 应用上下文
        context.close();
    }
}
