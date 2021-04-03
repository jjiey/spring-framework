package org.thinking.in.spring.dependency.injection.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.thinking.in.spring.dependency.injection.annotation.InjectedUser;
import org.thinking.in.spring.dependency.injection.annotation.MyAutowired;
import org.thinking.in.spring.ioc.domain.User;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

/**
 * 注解驱动的依赖注入处理过程
 *
 * @Autowired 注入 User
 * @Autowired 注入集合 Map
 * @Autowired 注入 Optional
 * @Autowired 注入 @Lazy 标注的类
 * （JSR-330）@Inject 注入 User
 * @MyAutowired 自定义注解注入 Optional
 * @InjectedUser 自定义注解注入 User（两种解析方式）
 *
 * @see Qualifier
 */
public class AnnotationDependencyInjectionResolutionDemo7 {

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

	/**
	 * DependencyDescriptor.getDependencyType() 返回 java.util.Map 类
	 * DefaultListableBeanFactory.resolveMultipleBeans() 返回一个 LinkedHashMap，然后直接返回
	 * 主要源码在 resolveMultipleBeans()
	 */
	@Autowired          // 集合类型依赖注入
	private Map<String, User> users; // user superUser

	@Autowired
	private Optional<User> userOptional; // superUser

	@Autowired          // 依赖查找（处理） + 延迟
	@Lazy
	private User lazyUser;

	@Inject
	private User injectedUser;

	@MyAutowired
	private Optional<User> myUserOptional;

	@InjectedUser
	private User myInjectedUser;

	/**
	 * 注意点：static
	 * 问题：Inject 有可能会报 ClassNotFoundException
	 * 因为名字一样，都叫 org.springframework.context.annotation.internalAutowiredAnnotationProcessor，所以 spring 里只会有一个 AutowiredAnnotationBeanPostProcessor。
	 * 这种方式相当于替换了 spring 原生的 AutowiredAnnotationBeanPostProcessor
	 */
//	@Bean(name = AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)
//    public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
//        AutowiredAnnotationBeanPostProcessor beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
//        // @Autowired + @Inject +  新注解 @InjectedUser
//        Set<Class<? extends java.lang.annotation.Annotation>> autowiredAnnotationTypes =
//                new LinkedHashSet<>(Arrays.asList(Autowired.class, Inject.class, InjectedUser.class));
//        beanPostProcessor.setAutowiredAnnotationTypes(autowiredAnnotationTypes);
//        return beanPostProcessor;
//    }

	/**
	 * 修改 beanPostProcessor() order（和 CommonAnnotationBeanPostProcessor 的优先级一样），这样会优先处理 @InjectedUser
	 * spring 里会有两个 AutowiredAnnotationBeanPostProcessor，一个叫 org.springframework.context.annotation.internalAutowiredAnnotationProcessor，一个叫 beanPostProcessor。
	 * 这种方式相当于提供了一个新的 AutowiredAnnotationBeanPostProcessor
	 */
	@Bean
	@Order(org.springframework.core.Ordered.LOWEST_PRECEDENCE - 3)
	public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
		AutowiredAnnotationBeanPostProcessor beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
		beanPostProcessor.setAutowiredAnnotationType(InjectedUser.class);
		return beanPostProcessor;
	}

	public static void main(String[] args) {

		// 创建 BeanFactory 容器
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 注册 Configuration Class（配置类） -> Spring Bean
		applicationContext.register(AnnotationDependencyInjectionResolutionDemo7.class);

		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(applicationContext);

		String xmlResourcePath = "classpath:/META-INF/dependency-lookup-context.xml";
		// 加载 XML 资源，解析并且生成 BeanDefinition
		beanDefinitionReader.loadBeanDefinitions(xmlResourcePath);

		// 启动 Spring 应用上下文
		applicationContext.refresh();

		// 依赖查找 QualifierAnnotationDependencyInjectionDemo Bean
	AnnotationDependencyInjectionResolutionDemo7 demo = applicationContext.getBean(AnnotationDependencyInjectionResolutionDemo7.class);

		// 期待输出 superUser Bean
		System.out.println("demo.user = " + demo.user);
		System.out.println("demo.injectedUser = " + demo.injectedUser);
		// 期待输出 user superUser
		System.out.println("demo.users = " + demo.users);
		// 期待输出 superUser Bean
		System.out.println("demo.userOptional = " + demo.userOptional);
		// 期待输出 superUser Bean
		System.out.println("demo.myUserOptional = " + demo.myUserOptional);
		// 期待输出 superUser Bean
		System.out.println("demo.myInjectedUser = " + demo.myInjectedUser);

		// 显示地关闭 Spring 应用上下文
		applicationContext.close();
	}

}
