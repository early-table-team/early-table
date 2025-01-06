package com.gotcha.earlytable.global.annotation;


import com.gotcha.earlytable.global.enums.Auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserAuth {
    Auth[] requiredAuthorities() default {}; // 필요한 유저 권한
}

