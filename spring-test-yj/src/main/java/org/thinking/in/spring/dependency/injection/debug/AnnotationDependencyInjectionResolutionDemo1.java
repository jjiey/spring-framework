package org.thinking.in.spring.dependency.injection.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thinking.in.spring.ioc.domain.User;

/**
 * 注解驱动的依赖注入处理过程
 *
 * @Autowired 注入 User
 *
 * @see Qualifier
 */
public class AnnotationDependencyInjectionResolutionDemo1 {

	/**
	 * DependencyDescriptor.getDependencyType() 返回 org.thinking.in.spring.ioc.domain.User 类
	 * DefaultListableBeanFactory.resolveMultipleBeans() 返回 null（因为不是集合类型）
	 */
	// DependencyDescriptor ->
	// 必须（required=true）
	// 实时注入（eager=true)
	// 通过类型（User.class）
	// 字段名称（"user"）
	// 是否首要（primary = true)
	@Autowired          // 依赖查找（处理）
	private User user;

	public static void main(String[] args) {

		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 注册 Configuration Class（配置类） -> Spring Bean
		applicationContext.register(AnnotationDependencyInjectionResolutionDemo1.class);

		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);

		String xmlResourcePath = "classpath:/META-INF/dependency-lookup-context.xml";
		// 加载 XML 资源，解析并且生成 BeanDefinition
		beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);

		// 启动 Spring 应用上下文
		applicationContext.refresh();

		// 依赖查找 QualifierAnnotationDependencyInjectionDemo Bean
	AnnotationDependencyInjectionResolutionDemo1 demo = applicationContext.getBean(AnnotationDependencyInjectionResolutionDemo1.class);

		// 期待输出 superUser Bean
		System.out.println("demo.user = " + demo.user);

		// 显示地关闭 Spring 应用上下文
		applicationContext.close();
	}

}
