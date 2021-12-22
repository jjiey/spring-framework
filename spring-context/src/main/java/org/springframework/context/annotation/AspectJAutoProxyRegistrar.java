/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.annotation;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Registers an {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 * AnnotationAwareAspectJAutoProxyCreator} against the current {@link BeanDefinitionRegistry}
 * as appropriate based on a given @{@link EnableAspectJAutoProxy} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 * @see EnableAspectJAutoProxy
 */
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	/**
	 * Register, escalate, and configure the AspectJ auto proxy creator based on the value
	 * of the @{@link EnableAspectJAutoProxy#proxyTargetClass()} attribute on the importing
	 * {@code @Configuration} class.
	 */
	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		// 往容器中注册 AnnotationAwareAspectJAutoProxyCreator
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		if (enableAspectJAutoProxy != null) {
			// 默认 @EnableAspectJAutoProxy 中 proxyTargetClass = false，即底层默认使用 jdk 动态代理
			// 如果 proxyTargetClass = true，则底层使用 cglib 代理
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				// 给 bean org.springframework.aop.config.internalAutoProxyCreator 添加属性 proxyTargetClass 为 true
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			// 默认 @EnableAspectJAutoProxy 中 exposeProxy = false，即不暴露代理对象
			// 如果 exposeProxy = true，则暴露当前代理对象到 Aop 上下文内，代理对象内部的真实对象可以拿到代理对象
			// see org.springframework.aop.framework.JdkDynamicAopProxy#invoke
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				// 给 bean org.springframework.aop.config.internalAutoProxyCreator 添加属性 exposeProxy 为 true
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}

}
