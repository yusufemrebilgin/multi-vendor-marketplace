package com.yusufemrebilgin.marketplace.auth.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusufemrebilgin.marketplace.auth.application.port.out.UserCreatedEvent;
import com.yusufemrebilgin.marketplace.auth.application.port.out.UserEventPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class MockMessagePublisher implements UserEventPublisher {

    private final static Logger logger = LoggerFactory.getLogger(MockMessagePublisher.class);

    private final ObjectMapper objectMapper;

    @Override
    public void publish(UserCreatedEvent event) {
        try {
            var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
            logger.info("[{}] simulating event publish:\n{}", MockMessagePublisher.class.getSimpleName(), json);
        } catch (JsonProcessingException ex) {
            logger.error("Failed to serialize event: {}", ex.getMessage(), ex);
        }


    }

}
