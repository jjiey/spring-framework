package org.thinking.in.spring.bean.definition;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.thinking.in.spring.ioc.domain.User;

/**
 * {@link BeanDefinition} 构建示例
 *
 * RootBeanDefinition 没有 parent
 */
public class BeanDefinitionCreationDemo {

	public static void main(String[] args) {

		// 1.通过 BeanDefinitionBuilder 构建
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
		// 通过属性设置
		beanDefinitionBuilder
				.addPropertyValue("id", 1)
				.addPropertyValue("name", "yangsanity");
		// 获取 BeanDefinition 实例
		BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
		// BeanDefinition 并非 Bean 终态，可以自定义修改

		// 2.通过 AbstractBeanDefinition 以及派生类
		GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
		// 设置 Bean 类型
		genericBeanDefinition.setBeanClass(User.class);
		// 通过 MutablePropertyValues 批量操作属性
		MutablePropertyValues propertyValues = new MutablePropertyValues();
//        propertyValues.addPropertyValue("id", 1);
//        propertyValues.addPropertyValue("name", "yangsanity");
		propertyValues
				.add("id", 1)
				.add("name", "yangsanity");
		// 通过 set MutablePropertyValues 批量操作属性
		genericBeanDefinition.setPropertyValues(propertyValues);
	}
}
