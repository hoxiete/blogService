package com.project.config.redis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PutRedis {
    String key();         //redis 的key的前缀
    String fieldKey() ;  //输入的字段名（条件）
    //缓存多少天,默认无限期
    int expire() default 0;
}
