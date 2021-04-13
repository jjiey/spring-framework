
package org.thinking.in.spring.ioc.domain;

import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.core.io.Resource;
import org.thinking.in.spring.ioc.enums.City;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Properties;

/**
 * 用户类
 */
@Data
public class User implements BeanNameAware {

	private Long id;

	private String name;

	private City city;

	private City[] workCities;

	private List<City> lifeCities;

	private Resource configFileLocation;

	private Company company;

	private Properties context;

	private String contextAsText;

	/**
	 * 当前 Bean 的名称
	 * 不需要序列化或反序列化，只是容器的一个标识
	 */
	private transient String beanName;

	public static User createUser() {
		User user = new User();
		user.setId(1L);
		user.setName("yangsanity");
		return user;
	}

	@PostConstruct
	public void init() {
		System.out.println("User Bean [" + beanName + "] 初始化...");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("User Bean [" + beanName + "] 销毁中...");
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}
}
