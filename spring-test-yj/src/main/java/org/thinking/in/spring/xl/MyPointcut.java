package org.thinking.in.spring.xl;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;

public class MyPointcut implements Pointcut {

	@Override
	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	@Override
	public MethodMatcher getMethodMatcher() {

		return new MethodMatcher() {

			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return method.getName().equals("eat");
			}

			@Override
			public boolean isRuntime() {
				return false;
			}

			@Override
			public boolean matches(Method method, Class<?> targetClass, Object... args) {
				return false;
			}
		};
	}
}
