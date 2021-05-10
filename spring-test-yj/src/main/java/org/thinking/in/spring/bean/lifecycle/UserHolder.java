package org.thinking.in.spring.bean.lifecycle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.thinking.in.spring.ioc.domain.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * User Holder 类
 */
public class UserHolder implements BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, EnvironmentAware,
		InitializingBean, SmartInitializingSingleton, DisposableBean {

	private final User user;

	@Getter
	@Setter
	private Integer number;

	@Getter
	@Setter
	private String description;

	/**
	 * 参数 user 到底是首选类型还是名称？
	 * 答案：类型，因为底层源码走的是 resolveDependency
	 */
	public UserHolder(User user) {
		this.user = user;
	}

	/**
	 * 依赖于注解驱动
	 * 当前场景：BeanFactory
	 */
	@PostConstruct
	public void initPostConstruct() {
		// postProcessBeforeInitialization V3 -> initPostConstruct V4
		this.description = "The user holder V4";
		System.out.println("initPostConstruct() = " + description);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// initPostConstruct V4 -> afterPropertiesSet V5
		this.description = "The user holder V5";
		System.out.println("afterPropertiesSet() = " + description);
	}

	/**
	 * 自定义初始化方法
	 */
	public void init() {
		// initPostConstruct V5 -> afterPropertiesSet V6
		this.description = "The user holder V6";
		System.out.println("init() = " + description);
	}

	@PreDestroy
	public void preDestroy() {
		this.description = "The user holder V10";
		System.out.println("preDestroy() = " + description);
	}

	@Override
	public void destroy() throws Exception {
		this.description = "The user holder V11";
		System.out.println("destroy() = " + description);
	}

	public void doDestroy() {
		this.description = "The user holder V12";
		System.out.println("doDestroy() = " + description);
	}

	@Override
	public void afterSingletonsInstantiated() {
		// postProcessAfterInitialization V7 -> afterSingletonsInstantiated V8
		this.description = "The user holder V8";
		System.out.println("afterSingletonsInstantiated() = " + description);
	}

	protected void finalize() throws Throwable {
		System.out.println("The UserHolder is finalized...");
	}

	@Override
	public String toString() {
		return "UserHolder{" +
				"user=" + user +
				", number=" + number +
				", description='" + description + '\'' +
				", beanName='" + beanName + '\'' +
				'}';
	}

	//---------------------------------------------------------------------
	// Aware 属性 及 回调方法（按回调顺序）
	//---------------------------------------------------------------------

	private String beanName;

	private ClassLoader classLoader;

	private BeanFactory beanFactory;

	private Environment environment;

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * 该方法及其之后的 Aware 是属于 ApplicationContext 生命周期里面的
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
