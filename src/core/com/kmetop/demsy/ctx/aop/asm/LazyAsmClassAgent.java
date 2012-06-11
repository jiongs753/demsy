package com.kmetop.demsy.ctx.aop.asm;

import java.lang.reflect.Constructor;

import org.nutz.aop.ClassDefiner;
import org.nutz.aop.asm.AsmClassAgent;

import com.kmetop.demsy.ctx.aop.LazyClassAgent;

public class LazyAsmClassAgent extends AsmClassAgent implements LazyClassAgent {

	@SuppressWarnings("unchecked")
	protected <T> Class<T> generate(ClassDefiner cd, Pair2[] pair2s, String newName, Class<T> klass, Constructor<T>[] constructors) {
		newName = klass.getName() + LAZY_CLASSNAME_SUFFIX;
		return super.generate(cd, pair2s, newName, klass, constructors);
	}

	public <T> Class<T> define(ClassDefiner cd, Class<T> klass) {
		Class ret = super.define(cd, klass);
		if (ret.equals(klass) && !klass.getName().endsWith(CLASSNAME_SUFFIX)) {
			return generate(cd, new Pair2[0], null, klass, getEffectiveConstructors(klass));
		} else {
			return ret;
		}
	}
}
