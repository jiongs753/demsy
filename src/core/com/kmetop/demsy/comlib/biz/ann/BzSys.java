package com.kmetop.demsy.comlib.biz.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BzSys {

	public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String desc() default "";

	public String template() default "";

	BzGrp[] groups() default {};

	BzAct[] actions() default {};

	public byte layout() default 0;

	public String catalog() default "";

	public String jsonData() default "";

	public boolean buildin() default false;

	public int orderby() default 1000;

	public String version() default "";

}
