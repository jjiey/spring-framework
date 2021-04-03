package org.thinking.in.spring.dependency.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.thinking.in.spring.ioc.domain.User;

import javax.annotation.Resource;

/**
 * 基于 Java 注解的依赖字段注入示例
 */
public class AnnotationDependencyFieldInjectionDemo {

	@Autowired
	private
//    static // @Autowired 会忽略掉静态字段
			UserHolder userHolder;

	@Resource
	private UserHolder userHolder2;

	public static void main(String[] args) {

		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 注册 Configuration Class（配置类） -> Spring Bean
		applicationContext.register(AnnotationDependencyFieldInjectionDemo.class);

		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);

		String xmlResourcePath = "classpath:/META-INF/dependency-lookup-context.xml";
		// 加载 XML 资源，解析并且生成 BeanDefinition
		beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);

		// 启动 Spring 应用上下文
		applicationContext.refresh();

		// 依赖查找 AnnotationDependencyFieldInjectionDemo Bean
		AnnotationDependencyFieldInjectionDemo demo = applicationContext.getBean(AnnotationDependencyFieldInjectionDemo.class);

		// 字段关联
		System.out.println(demo.userHolder);
		System.out.println(demo.userHolder2);

		System.out.println(demo.userHolder == demo.userHolder2); // true


		// 显示地关闭 Spring 应用上下文
		applicationContext.close();
	}

	@Bean
	public UserHolder userHolder(User user) {
		return new UserHolder(user);
	}
}
