package org.thinking.in.spring.questions;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.thinking.in.spring.ioc.domain.User;

/**
 * Bean 是否缓存示例
 */
public class BeanCachingDemo {

	@Bean
	@Scope("prototype") // 原型 scope
	public static User user() {
		User user = new User();
		user.setId(1L);
		user.setName("小马哥");
		return user;
	}

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class
        context.register(BeanCachingDemo.class);

        // 启动 Spring 应用上下文
        context.refresh();

        // BeanCachingDemo 是 Configuration Class，Singleton Scope Bean
        BeanCachingDemo beanCachingDemo = context.getBean(BeanCachingDemo.class);

        for (int i = 0; i < 9; i++) {
            // Singleton Scope Bean 是存在缓存
            System.out.println(beanCachingDemo == context.getBean(BeanCachingDemo.class));
        }

        User user = context.getBean(User.class);

        for (int i = 0; i < 9; i++) {
            // Prototype Scope Bean
            System.out.println(user == context.getBean(User.class));
        }

        // 关闭 Spring 应用上下文
        context.close();
    }
}
