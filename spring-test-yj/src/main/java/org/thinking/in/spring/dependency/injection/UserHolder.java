package org.thinking.in.spring.dependency.injection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thinking.in.spring.ioc.domain.User;

/**
 * {@link User} 的 Holder 类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHolder {

	private User user;
}
