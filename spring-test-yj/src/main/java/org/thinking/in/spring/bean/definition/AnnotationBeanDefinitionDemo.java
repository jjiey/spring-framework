package org.thinking.in.spring.bean.definition;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thinking.in.spring.ioc.domain.User;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * 注解 BeanDefinition 示例
 *
 * 1：Java 注解配置元信息（1.2 和 1.3 不会重复注册）
 * 2：Java API 配置元信息
 */
// 1.3.通过 @Import 来进行导入
@Import(AnnotationBeanDefinitionDemo.Config.class)
public class AnnotationBeanDefinitionDemo {

	public static void main(String[] args) {
		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 2.3.注册 Configuration Class（配置类）
		applicationContext.register(AnnotationBeanDefinitionDemo.class);

		// 通过 BeanDefinition 注册 API 实现
		// 2.1.命名 Bean 的注册方式
		registerUserBeanDefinition(applicationContext, "jjiey-user");
		// 2.2.非命名 Bean 的注册方法
		registerUserBeanDefinition(applicationContext);

		// 启动 Spring 应用上下文
		applicationContext.refresh();
		// 按照类型依赖查找
		System.out.println("Config 类型的所有 Beans" + applicationContext.getBeansOfType(Config.class));
		System.out.println("User 类型的所有 Beans" + applicationContext.getBeansOfType(User.class));
		// 显示地关闭 Spring 应用上下文
		applicationContext.close();
	}

	/**
	 * 命名 Bean 的注册方式
	 */
	public static void registerUserBeanDefinition(BeanDefinitionRegistry registry, String beanName) {
		BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(User.class);
		beanDefinitionBuilder
				.addPropertyValue("id", 1L)
				.addPropertyValue("name", "yangsanity");

		// 判断如果 beanName 参数存在时
		if (StringUtils.hasText(beanName)) {
			// 注册 BeanDefinition
			registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
		} else {
			// 非命名 Bean 注册方法
			BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinitionBuilder.getBeanDefinition(), registry);
		}
	}

	/**
	 * 非命名 Bean 的注册方法
	 */
	public static void registerUserBeanDefinition(BeanDefinitionRegistry registry) {
		registerUserBeanDefinition(registry, null);
	}

	// 1.2.通过 @Component 方式
	@Component // 定义当前类作为 Spring Bean（组件）
	public static class Config {

		// 1.1通过 @Bean 方式定义

		/**
		 * 通过 Java 注解的方式，定义了一个 Bean
		 */
		@Bean(name = {"user", "yangsanity-user"})
		public User user() {
			User user = new User();
			user.setId(1L);
			user.setName("yangsanity");
			return user;
		}
	}
}
