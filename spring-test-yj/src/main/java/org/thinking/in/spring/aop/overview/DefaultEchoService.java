package org.thinking.in.spring.aop.overview;

/**
 * 默认 {@link org.thinking.in.spring.aop.overview.EchoService} 实现
 */
public class DefaultEchoService implements EchoService {

    @Override
    public String echo(String message) {
        return "[ECHO] " + message;
    }
}
