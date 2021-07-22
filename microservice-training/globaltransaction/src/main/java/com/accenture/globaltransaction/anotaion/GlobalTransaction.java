package com.accenture.globaltransaction.anotaion;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Retention(RUNTIME)
@Target(METHOD)
@Transaction
public @interface GlobalTransaction {
	boolean start() default false;
}
