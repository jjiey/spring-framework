/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.framework;

import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.IntroductionAwareMethodMatcher;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple but definitive way of working out an advice chain for a Method,
 * given an {@link Advised} object. Always rebuilds each advice chain;
 * caching can be provided by subclasses.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Adrian Colyer
 * @since 2.0.3
 */
@SuppressWarnings("serial")
public class DefaultAdvisorChainFactory implements AdvisorChainFactory, Serializable {

	/**
	 * 该方法的目的就是 查找出适合当前方法的增强
	 * config：ProxyFactory，它掌握着 AOP 的所有资料
	 * method：目标对象的方法
	 * targetClass：目标对象的类型
	 */
	@Override
	public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
			Advised config, Method method, @Nullable Class<?> targetClass) {

		// This is somewhat tricky... We have to process introductions first,
		// but we need to preserve order in the ultimate list.
		// AdvisorAdapterRegistry 接口有两个作用，一个作用是可以向里面注册 AdvisorAdapter 适配器
		// 适配器目的：1.将非 Advisor 类型增强，包装成为 Advisor
		//          2.将 Advisor 类型的增强提取出来，对应 MethodInterceptor
		AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
		// 获取 ProxyFactory 内部持有的增强信息：addAdvice() 和 AddAdvisor() 最终在 ProxyFactory 内都会包装成为 Advisor
		Advisor[] advisors = config.getAdvisors();
		// 拦截器列表
		List<Object> interceptorList = new ArrayList<>(advisors.length);
		// 真实的目标对象类型
		Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
		// 引介增强，暂不考虑
		Boolean hasIntroductions = null;

		for (Advisor advisor : advisors) {
			// 条件成立：说明当前 advisor 是包含 切点 信息的，所以 if 内部逻辑，就是做匹配算法
			if (advisor instanceof PointcutAdvisor) {
				// Add it conditionally.
				// 转换成可以获取到切点信息的接口 PointcutAdvisor
				PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
				// 条件一成立，说明已经做过 ClassFilter 匹配（AbstractAutoProxyCreator.createProxy 里设置）
				// 条件二成立，说明当前被代理对象的 Class 匹配成功
				if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
					// 获取 切点信息 的 方法匹配器
					MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
					// 表示方法是否匹配
					boolean match;
					// 引介增强，暂不考虑
					if (mm instanceof IntroductionAwareMethodMatcher) {
						if (hasIntroductions == null) {
							hasIntroductions = hasMatchingIntroductions(advisors, actualClass);
						}
						match = ((IntroductionAwareMethodMatcher) mm).matches(method, actualClass, hasIntroductions);
					}
					else {
						// match 为 true 表示 目标方法 匹配成功，即静态匹配成功
						match = mm.matches(method, actualClass);
					}
					// 静态匹配成功，再检查是否需要 运行时匹配
					if (match) {
						// 提取出来 advisor 内持有的拦截器信息
						// 前面说的，2.将 Advisor 类型的增强提取出来，对应 MethodInterceptor
						MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
						// 是否运行时匹配
						if (mm.isRuntime()) {
							// Creating a new object instance in the getInterceptors() method
							// isn't a problem as we normally cache created chains.
							for (MethodInterceptor interceptor : interceptors) {
								interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
							}
						}
						else {
							// 将当前 advisor 内部的方法拦截器追加到 interceptorList
							interceptorList.addAll(Arrays.asList(interceptors));
						}
					}
				}
			}
			// 引介增强，暂不考虑
			else if (advisor instanceof IntroductionAdvisor) {
				IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
				if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
					Interceptor[] interceptors = registry.getInterceptors(advisor);
					interceptorList.addAll(Arrays.asList(interceptors));
				}
			}
			// 说明当前 Advisor 要匹配全部 class 的全部 method
			else {
				Interceptor[] interceptors = registry.getInterceptors(advisor);
				interceptorList.addAll(Arrays.asList(interceptors));
			}
		}

		// 返回所有匹配当前 method 的方法拦截器
		return interceptorList;
	}

	/**
	 * Determine whether the Advisors contain matching introductions.
	 */
	private static boolean hasMatchingIntroductions(Advisor[] advisors, Class<?> actualClass) {
		for (Advisor advisor : advisors) {
			if (advisor instanceof IntroductionAdvisor) {
				IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
				if (ia.getClassFilter().matches(actualClass)) {
					return true;
				}
			}
		}
		return false;
	}

}
