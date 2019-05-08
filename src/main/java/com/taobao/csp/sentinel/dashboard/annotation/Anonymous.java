package com.taobao.csp.sentinel.dashboard.annotation;

import java.lang.annotation.*;

/**
 * 用于标记某个方法允许匿名访问
 *
 * @author jiashuai.xie
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Anonymous {
}
