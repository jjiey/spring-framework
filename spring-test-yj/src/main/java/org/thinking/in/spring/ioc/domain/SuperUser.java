package org.thinking.in.spring.ioc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.thinking.in.spring.ioc.annotation.Super;

/**
 * 超级用户
 */
@Super
@Data
@EqualsAndHashCode(callSuper = true)
public class SuperUser extends User {

    private String address;
}
