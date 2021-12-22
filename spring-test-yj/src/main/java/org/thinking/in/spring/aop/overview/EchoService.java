package org.thinking.in.spring.aop.overview;

/**
 * Echo 服务
 */
public interface EchoService {

    String echo(String message) throws NullPointerException;
}
