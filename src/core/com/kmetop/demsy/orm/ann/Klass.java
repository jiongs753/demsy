package com.kmetop.demsy.orm.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注释用于描述实体类与遗留系统中实体类的兼容性
 * 
 * @author yongshan.ji
 */
@Target(value = { ElementType.TYPE, ElementType.PACKAGE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Klass {
	public String value() default "";
}
