package com.rocketpt.server.config;


import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.Async;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Async("defaultThreadPool")
public @interface DefautAsync {

    @AliasFor(
            annotation = Async.class
    )
    String value() default "defaultThreadPool";

}
