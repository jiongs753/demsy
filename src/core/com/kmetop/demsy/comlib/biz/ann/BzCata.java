package com.kmetop.demsy.comlib.biz.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BzCata {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String desc() default "";

	public int orderby() default 1000;

}
