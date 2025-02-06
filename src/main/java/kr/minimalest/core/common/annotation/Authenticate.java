package kr.minimalest.core.common.annotation;

import kr.minimalest.core.domain.member.UserLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticate {
    UserLevel level() default UserLevel.MEMBER;
}
