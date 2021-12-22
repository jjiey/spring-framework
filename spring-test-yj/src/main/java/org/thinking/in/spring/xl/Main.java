package org.thinking.in.spring.xl;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class Main {

	public static void main(String[] args) {
		// 1. 创建被代理对象
		Cat cat = new Cat();

		// 2. 创建 Spring 代理工厂对象 ProxyFactory
		// ProxyFactory 是 Config + Factory 的存在，持有 AOP 操作所有的生产资料
		ProxyFactory proxyFactory = new ProxyFactory(cat);

		// MethodInterceptor extends Interceptor extends Advice
		MethodInterceptor m1 = invocation -> {
			System.out.println("methodInterceptor01 begin");
			Object ret = invocation.proceed();
			System.out.println("methodInterceptor01 end");
			return ret;
		};
		MethodInterceptor m2 = invocation -> {
			System.out.println("MethodInterceptor02 begin");
			Object ret = invocation.proceed();
			System.out.println("MethodInterceptor02 end");
			return ret;
		};

		//3. 添加方法拦截器
		// 代理所有类所有方法，因为 DefaultPointcutAdvisor 里持有的是 Pointcut.TRUE
		// proxyFactory.addAdvice(m1);
		// proxyFactory.addAdvice(m2);
		// 自定义 Pointcut，只增强 eat() 不增强 go()
		MyPointcut pointcut = new MyPointcut();
		proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, m1));
		proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, m2));

		//4. 获取代理对象
		Animal proxy = (Animal) proxyFactory.getProxy();
		proxy.eat();
		System.out.println("--------------------");
		proxy.run();
	}
}
