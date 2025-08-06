package com.yusufemrebilgin.marketplace.auth.application.port.out;

public interface UserEventPublisher {

    void publish(UserCreatedEvent event);

}
