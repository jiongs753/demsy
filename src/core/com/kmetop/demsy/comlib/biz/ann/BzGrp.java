package com.kmetop.demsy.comlib.biz.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BzGrp {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String desc() default "";

	public int order() default 0;

	public BzFld[] fields() default {};
}
