package org.thinking.in.spring.bean.lifecycle;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.thinking.in.spring.ioc.domain.User;

/**
 * Bean 实例化生命周期示例
 */
public class BeanInstantiationLifecycleDemo {

    public static void main(String[] args) {
    	// 这里有 BeanNameAware, BeanClassLoaderAware, BeanFactoryAware 的回调，这三个属于 bean 的初始化操作
		// 源码位置：org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean() 一开始 invokeAwareMethods()
        executeBeanFactory();

        System.out.println("--------------------------------");

        // 这里有 EnvironmentAware 及其之后的 Aware 的回调，因为这些 Aware 是属于 ApplicationContext 生命周期里面的，也属于初始化操作里（实现上是一个 BeanPostProcessor(ApplicationContextAwareProcessor) 的 postProcessBeforeInitialization 方法里）
		// ApplicationContextAwareProcessor 是所有 BeanPostProcessor 的第一个（最先 add 的）
        executeApplicationContext();
    }

    private static void executeBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 添加 BeanPostProcessor 实现 MyInstantiationAwareBeanPostProcessor
		// 注意，这里不能将 MyInstantiationAwareBeanPostProcessor 作为 Bean 注册。因为 DefaultListableBeanFactory 无法进行 bean 的注册，所以这种方式对于 DefaultListableBeanFactory 是无效的。bean 的注册只能在 ApplicationContext 里运用。在底层实现上也是通过调用 addBeanPostProcessor() 来实现的。源码位置：org.springframework.context.support.AbstractApplicationContext.registerBeanPostProcessors() 和 org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors()
		beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
        // 基于 XML 资源 BeanDefinitionReader 实现
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        String[] locations = {"META-INF/dependency-lookup-context.xml", "META-INF/bean-constructor-dependency-injection.xml"};
        int beanNumbers = beanDefinitionReader.loadBeanDefinitions(locations);
        System.out.println("已加载 BeanDefinition 数量：" + beanNumbers);

        // 通过 Bean Id 和类型进行依赖查找
        User user = beanFactory.getBean("user", User.class);
        System.out.println(user);
        User superUser = beanFactory.getBean("superUser", User.class);
        System.out.println(superUser);
        // 构造器注入按照类型注入，源码 resolveDependency
        UserHolder userHolder = beanFactory.getBean("userHolder", UserHolder.class);
        System.out.println(userHolder);
    }

    private static void executeApplicationContext() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
        String[] locations = {"META-INF/dependency-lookup-context.xml", "META-INF/bean-constructor-dependency-injection.xml"};
        applicationContext.setConfigLocations(locations);
        // 启动应用上下文
        applicationContext.refresh();

        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);
        User superUser = applicationContext.getBean("superUser", User.class);
        System.out.println(superUser);
        // 构造器注入按照类型注入，resolveDependency
        UserHolder userHolder = applicationContext.getBean("userHolder", UserHolder.class);
        System.out.println(userHolder);

        // 关闭应用上下文
        applicationContext.close();
    }
}

