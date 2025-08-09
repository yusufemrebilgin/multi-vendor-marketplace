package com.yusufemrebilgin.marketplace.common.application;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as an application service (use case) in a DDD architecture.
 * <p>
 * Use this to indicate that the class contains application-level logic
 * that coordinates between the domain and infrastructure layers.
 * <p>
 * This annotation is a specialization of {@link Service},
 * so the class will be detected by Spring's component scanning.
 */
@Service
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
}
