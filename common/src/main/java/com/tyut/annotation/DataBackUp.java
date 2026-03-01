package com.tyut.annotation;

import com.tyut.constant.ModuleConstant;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataBackUp {
    ModuleConstant module();
}
