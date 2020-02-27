package com.stoneknife.annotation;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface RedisCache {
    String key() default "";
}
