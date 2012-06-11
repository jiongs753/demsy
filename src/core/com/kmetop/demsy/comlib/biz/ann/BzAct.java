package com.kmetop.demsy.comlib.biz.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BzAct {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String desc() default "";

	public int order() default 0;

	public int typeCode() default 0;

	public String mode() default "";

	public String plugin() default "";

	public String image() default "";

	public String logo() default "";

	public String template() default "";

	public String targetUrl() default "";

	public String targetWindow() default "";

	public boolean disabled() default false;

	public String info() default "";

	public String error() default "";

	public String warn() default "";

	public String params() default "";

	public String jsonData() default "";
}
