/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.beans.factory;

/**
 * Callback interface triggered at the end of the singleton pre-instantiation phase
 * during {@link BeanFactory} bootstrap. This interface can be implemented by
 * singleton beans in order to perform some initialization after the regular
 * singleton instantiation algorithm, avoiding side effects with accidental early
 * initialization (e.g. from {@link ListableBeanFactory#getBeansOfType} calls).
 * In that sense, it is an alternative to {@link InitializingBean} which gets
 * triggered right at the end of a bean's local construction phase.
 * 在 BeanFactory 引导(bootstrap)期间单例预实例化阶段结束时触发的回调接口。该接口可以由单例 bean 实现，以便在常规的单例实例化算法之后执行一些初始化，避免意外的早期初始化带来的副作用（例如，来自 ListableBeanFactory#getBeansOfType 的调用）。从这个意义上说，它是 InitializingBean 的替代方法，后者(InitializingBean)在 bean 的本地构造阶段结束时立即触发。
 *
 * <p>This callback variant is somewhat similar to
 * {@link org.springframework.context.event.ContextRefreshedEvent} but doesn't
 * require an implementation of {@link org.springframework.context.ApplicationListener},
 * with no need to filter context references across a context hierarchy etc.
 * It also implies a more minimal dependency on just the {@code beans} package
 * and is being honored by standalone {@link ListableBeanFactory} implementations,
 * not just in an {@link org.springframework.context.ApplicationContext} environment.
 * 这个回调变体有点类似于 org.springframework.context.event.ContextRefreshedEvent，但是不需要实现 org.springframework.context.ApplicationListener，无需在整个上下文层次结构中过滤上下文引用等。这也意味着对 bean 包的依赖性最小，并且受到独立的 ListableBeanFactory 实现所认可，而不仅是在 org.springframework.context.ApplicationContext 环境中。
 *
 * <p><b>NOTE:</b> If you intend to start/manage asynchronous tasks, preferably
 * implement {@link org.springframework.context.Lifecycle} instead which offers
 * a richer model for runtime management and allows for phased startup/shutdown.
 * 注意：如果你打算 开始/管理 异步任务，则最好是实现 org.springframework.context.Lifecycle，它为运行时管理提供了更丰富的模型，并允许分阶段的 启动/关闭。
 *
 * @author Juergen Hoeller
 * @since 4.1
 * @see org.springframework.beans.factory.config.ConfigurableListableBeanFactory#preInstantiateSingletons()
 */
public interface SmartInitializingSingleton {

	/**
	 * Invoked right at the end of the singleton pre-instantiation phase,
	 * with a guarantee that all regular singleton beans have been created
	 * already. {@link ListableBeanFactory#getBeansOfType} calls within
	 * this method won't trigger accidental side effects during bootstrap.
	 * <p><b>NOTE:</b> This callback won't be triggered for singleton beans
	 * lazily initialized on demand after {@link BeanFactory} bootstrap,
	 * and not for any other bean scope either. Carefully use it for beans
	 * with the intended bootstrap semantics only.
	 *
	 * 单例预实例化阶段结束时立即调用，以确保已经创建了所有常规单例 bean。此方法内的 ListableBeanFactory#getBeansOfType 调用不会在引导(bootstrap)期间触发意外的副作用。
	 * 注意：对于 BeanFactory 引导后按需延迟初始化的单例 bean，该回调不会被触发，对于任何其他 bean 作用域也不会触发。对于仅具有预期引导语义的 bean 小心使用它。
	 */
	void afterSingletonsInstantiated();

}
