/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kmetop.demsy.lang;

import java.beans.Introspector;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import org.nutz.aop.ClassAgent;
import org.nutz.lang.Mirror;

import com.kmetop.demsy.Demsy;
import com.kmetop.demsy.ctx.aop.LazyClassAgent;

public abstract class Cls {

	public static final String ARRAY_SUFFIX = "[]";

	private static final String INTERNAL_ARRAY_PREFIX = "[L";

	private static final char PACKAGE_SEPARATOR = '.';

	private static final char INNER_CLASS_SEPARATOR = '$';

	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	public static final String CLASS_FILE_SUFFIX = ".class";

	private static final Map primitiveWrapperTypeMap = new HashMap(8);

	private static final Map primitiveTypeNameMap = new HashMap(16);

	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);

		Set primitiveTypeNames = new HashSet(16);
		primitiveTypeNames.addAll(primitiveWrapperTypeMap.values());
		primitiveTypeNames.addAll(Arrays.asList(new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class }));
		for (Iterator it = primitiveTypeNames.iterator(); it.hasNext();) {
			Class primitiveClass = (Class) it.next();
			primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
		}
	}

	// private static DemsyClassLoader demsyClassLoader;

	private static DemsyClassLoader getDemsyClassLoader() {
		// if (demsyClassLoader == null) {
		// demsyClassLoader = new DemsyClassLoader(getDefaultClassLoader());
		// }
		// return demsyClassLoader;
		return new DemsyClassLoader(getDefaultClassLoader());
	}

	public static Class loadClass(String className, File file) throws IOException {
		return getDemsyClassLoader().defineClass(className, file);
	}

	public static Class loadClass(String className, byte[] bytecodes) {
		return getDemsyClassLoader().defineClass(className, bytecodes);
	}

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system
			// class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = Cls.class.getClassLoader();
		}
		return cl;
	}

	public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse) {
		Thread currentThread = Thread.currentThread();
		ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
		if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader)) {
			currentThread.setContextClassLoader(classLoaderToUse);
			return threadContextClassLoader;
		} else {
			return null;
		}
	}

	public static boolean isPresent(String className) {
		return isPresent(className, getDefaultClassLoader());
	}

	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			forName(className, classLoader);
			return true;
		} catch (Throwable ex) {
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	public static Class reloadClass(String clsname) throws ClassNotFoundException {
		return getDemsyClassLoader().reloadClass(clsname);
	}

	public static Class forName(String name) throws ClassNotFoundException, LinkageError {
		return forName(name, getDefaultClassLoader());
	}

	public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
		Assert.notNull(name, "Name must not be null");

		Class clazz = resolvePrimitiveClassName(name);
		if (clazz != null) {
			return clazz;
		}

		// "java.lang.String[]" style arrays
		if (name.endsWith(ARRAY_SUFFIX)) {
			String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
			Class elementClass = forName(elementClassName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}

		// "[Ljava.lang.String;" style arrays
		int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
		if (internalArrayMarker != -1 && name.endsWith(";")) {
			String elementClassName = null;
			if (internalArrayMarker == 0) {
				elementClassName = name.substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
			} else if (name.startsWith("[")) {
				elementClassName = name.substring(1);
			}
			Class elementClass = forName(elementClassName, classLoader);
			return Array.newInstance(elementClass, 0).getClass();
		}

		return classLoader.loadClass(name);
	}

	public static Class resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
		try {
			return forName(className, classLoader);
		} catch (ClassNotFoundException ex) {
			IllegalArgumentException iae = new IllegalArgumentException("Cannot find class [" + className + "]");
			iae.initCause(ex);
			throw iae;
		} catch (LinkageError ex) {
			IllegalArgumentException iae = new IllegalArgumentException("Error loading class [" + className + "]: problem with class file or dependent class.");
			iae.initCause(ex);
			throw iae;
		}
	}

	public static Class resolvePrimitiveClassName(String name) {
		Class result = null;
		// Most class names will be quite long, considering that they
		// SHOULD sit in a package, so a length check is worthwhile.
		if (name != null && name.length() <= 8) {
			// Could be a primitive - likely.
			result = (Class) primitiveTypeNameMap.get(name);
		}
		return result;
	}

	public static Class getUserClass(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		return getUserClass(instance.getClass());
	}

	public static Class getUserClass(Class clazz) {
		return (clazz != null && clazz.getName().indexOf(CGLIB_CLASS_SEPARATOR) != -1 ? clazz.getSuperclass() : clazz);
	}

	public static boolean isCacheSafe(Class clazz, ClassLoader classLoader) {
		Assert.notNull(clazz, "Class must not be null");
		ClassLoader target = clazz.getClassLoader();
		if (target == null) {
			return false;
		}
		ClassLoader cur = classLoader;
		if (cur == target) {
			return true;
		}
		while (cur != null) {
			cur = cur.getParent();
			if (cur == target) {
				return true;
			}
		}
		return false;
	}

	public static String getShortName(String className) {
		Assert.hasLength(className, "Class name must not be empty");
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
		if (nameEndIndex == -1) {
			nameEndIndex = className.length();
		}
		String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
		shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
		return shortName;
	}

	public static String getShortName(Class clazz) {
		return getShortName(getQualifiedName(clazz));
	}

	public static String getShortNameAsProperty(Class clazz) {
		String shortName = Cls.getShortName(clazz);
		int dotIndex = shortName.lastIndexOf('.');
		shortName = (dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName);
		return Introspector.decapitalize(shortName);
	}

	public static String getClassFileName(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}

	public static String getPackageName(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
	}

	public static String getQualifiedName(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isArray()) {
			return getQualifiedNameForArray(clazz);
		} else {
			return clazz.getName();
		}
	}

	private static String getQualifiedNameForArray(Class clazz) {
		StringBuffer buffer = new StringBuffer();
		while (clazz.isArray()) {
			clazz = clazz.getComponentType();
			buffer.append(Cls.ARRAY_SUFFIX);
		}
		buffer.insert(0, clazz.getName());
		return buffer.toString();
	}

	public static String getQualifiedMethodName(Method method) {
		Assert.notNull(method, "Method must not be null");
		return method.getDeclaringClass().getName() + "." + method.getName();
	}

	public static String getDescriptiveType(Object value) {
		if (value == null) {
			return null;
		}
		Class clazz = value.getClass();
		if (Proxy.isProxyClass(clazz)) {
			StringBuffer buf = new StringBuffer(clazz.getName());
			buf.append(" implementing ");
			Class[] ifcs = clazz.getInterfaces();
			for (int i = 0; i < ifcs.length; i++) {
				buf.append(ifcs[i].getName());
				if (i < ifcs.length - 1) {
					buf.append(',');
				}
			}
			return buf.toString();
		} else if (clazz.isArray()) {
			return getQualifiedNameForArray(clazz);
		} else {
			return clazz.getName();
		}
	}

	public static boolean hasConstructor(Class clazz, Class[] paramTypes) {
		return (getConstructorIfAvailable(clazz, paramTypes) != null);
	}

	public static Constructor getConstructorIfAvailable(Class clazz, Class[] paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		try {
			return clazz.getConstructor(paramTypes);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	public static boolean hasMethod(Class clazz, String methodName, Class[] paramTypes) {
		return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
	}

	/**
	 * Determine whether the given class has a method with the given signature,
	 * and return it if available (else return <code>null</code>).
	 * <p>
	 * Essentially translates <code>NoSuchMethodException</code> to
	 * <code>null</code>.
	 * 
	 * @param clazz
	 *            the clazz to analyze
	 * @param methodName
	 *            the name of the method
	 * @param paramTypes
	 *            the parameter types of the method
	 * @return the method, or <code>null</code> if not found
	 * @see java.lang.Class#getMethod
	 */
	public static Method getMethodIfAvailable(Class clazz, String methodName, Class[] paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * Return the number of methods with a given name (with any argument types),
	 * for the given class and/or its superclasses. Includes non-public methods.
	 * 
	 * @param clazz
	 *            the clazz to check
	 * @param methodName
	 *            the name of the method
	 * @return the number of methods with the given name
	 */
	public static int getMethodCountForName(Class clazz, String methodName) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		int count = 0;
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			Method method = declaredMethods[i];
			if (methodName.equals(method.getName())) {
				count++;
			}
		}
		Class[] ifcs = clazz.getInterfaces();
		for (int i = 0; i < ifcs.length; i++) {
			count += getMethodCountForName(ifcs[i], methodName);
		}
		if (clazz.getSuperclass() != null) {
			count += getMethodCountForName(clazz.getSuperclass(), methodName);
		}
		return count;
	}

	/**
	 * Does the given class and/or its superclasses at least have one or more
	 * methods (with any argument types)? Includes non-public methods.
	 * 
	 * @param clazz
	 *            the clazz to check
	 * @param methodName
	 *            the name of the method
	 * @return whether there is at least one method with the given name
	 */
	public static boolean hasAtLeastOneMethodWithName(Class clazz, String methodName) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (int i = 0; i < declaredMethods.length; i++) {
			Method method = declaredMethods[i];
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		Class[] ifcs = clazz.getInterfaces();
		for (int i = 0; i < ifcs.length; i++) {
			if (hasAtLeastOneMethodWithName(ifcs[i], methodName)) {
				return true;
			}
		}
		return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
	}

	/**
	 * Given a method, which may come from an interface, and a target class used
	 * in the current reflective invocation, find the corresponding target
	 * method if there is one. E.g. the method may be <code>IFoo.bar()</code>
	 * and the target class may be <code>DefaultFoo</code>. In this case, the
	 * method may be <code>DefaultFoo.bar()</code>. This enables attributes on
	 * that method to be found.
	 * <p>
	 * <b>NOTE:</b> In contrast to
	 * {@link org.springframework.aop.support.AopUtils#getMostSpecificMethod},
	 * this method does <i>not</i> resolve Java 5 bridge methods automatically.
	 * Call
	 * {@link org.springframework.core.BridgeMethodResolver#findBridgedMethod}
	 * if bridge method resolution is desirable (e.g. for obtaining metadata
	 * from the original method definition).
	 * 
	 * @param method
	 *            the method to be invoked, which may come from an interface
	 * @param targetClass
	 *            the target class for the current invocation. May be
	 *            <code>null</code> or may not even implement the method.
	 * @return the specific target method, or the original method if the
	 *         <code>targetClass</code> doesn't implement it or is
	 *         <code>null</code>
	 * @see org.springframework.aop.support.AopUtils#getMostSpecificMethod
	 */
	public static Method getMostSpecificMethod(Method method, Class targetClass) {
		if (method != null && targetClass != null && !targetClass.equals(method.getDeclaringClass())) {
			try {
				method = targetClass.getMethod(method.getName(), method.getParameterTypes());
			} catch (NoSuchMethodException ex) {
				// Perhaps the target class doesn't implement this method:
				// that's fine, just use the original method.
			}
		}
		return method;
	}

	/**
	 * Return a static method of a class.
	 * 
	 * @param methodName
	 *            the static method name
	 * @param clazz
	 *            the class which defines the method
	 * @param args
	 *            the parameter types to the method
	 * @return the static method, or <code>null</code> if no static method was
	 *         found
	 * @throws IllegalArgumentException
	 *             if the method name is blank or the clazz is null
	 */
	public static Method getStaticMethod(Class clazz, String methodName, Class[] args) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(methodName, "Method name must not be null");
		try {
			Method method = clazz.getDeclaredMethod(methodName, args);
			if ((method.getModifiers() & Modifier.STATIC) != 0) {
				return method;
			}
		} catch (NoSuchMethodException ex) {
		}
		return null;
	}

	/**
	 * Check if the given class represents a primitive wrapper, i.e. Boolean,
	 * Byte, Character, Short, Integer, Long, Float, or Double.
	 * 
	 * @param clazz
	 *            the class to check
	 * @return whether the given class is a primitive wrapper class
	 */
	public static boolean isPrimitiveWrapper(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return primitiveWrapperTypeMap.containsKey(clazz);
	}

	/**
	 * Check if the given class represents a primitive (i.e. boolean, byte,
	 * char, short, int, long, float, or double) or a primitive wrapper (i.e.
	 * Boolean, Byte, Character, Short, Integer, Long, Float, or Double).
	 * 
	 * @param clazz
	 *            the class to check
	 * @return whether the given class is a primitive or primitive wrapper class
	 */
	public static boolean isPrimitiveOrWrapper(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * Check if the given class represents an array of primitives, i.e. boolean,
	 * byte, char, short, int, long, float, or double.
	 * 
	 * @param clazz
	 *            the class to check
	 * @return whether the given class is a primitive array class
	 */
	public static boolean isPrimitiveArray(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isArray() && clazz.getComponentType().isPrimitive());
	}

	/**
	 * Check if the given class represents an array of primitive wrappers, i.e.
	 * Boolean, Byte, Character, Short, Integer, Long, Float, or Double.
	 * 
	 * @param clazz
	 *            the class to check
	 * @return whether the given class is a primitive wrapper array class
	 */
	public static boolean isPrimitiveWrapperArray(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
	}

	/**
	 * Check if the right-hand side type may be assigned to the left-hand side
	 * type, assuming setting by reflection. Considers primitive wrapper classes
	 * as assignable to the corresponding primitive types.
	 * 
	 * @param lhsType
	 *            the target type
	 * @param rhsType
	 *            the value type that should be assigned to the target type
	 * @return if the target type is assignable from the value type
	 * @see TypeUtils#isAssignable
	 */
	public static boolean isAssignable(Class lhsType, Class rhsType) {
		Assert.notNull(lhsType, "Left-hand side type must not be null");
		Assert.notNull(rhsType, "Right-hand side type must not be null");
		return (lhsType.isAssignableFrom(rhsType) || lhsType.equals(primitiveWrapperTypeMap.get(rhsType)));
	}

	/**
	 * Determine if the given type is assignable from the given value, assuming
	 * setting by reflection. Considers primitive wrapper classes as assignable
	 * to the corresponding primitive types.
	 * 
	 * @param type
	 *            the target type
	 * @param value
	 *            the value that should be assigned to the type
	 * @return if the type is assignable from the value
	 */
	public static boolean isAssignableValue(Class type, Object value) {
		Assert.notNull(type, "Type must not be null");
		return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
	}

	/**
	 * Convert a "/"-based resource path to a "."-based fully qualified class
	 * name.
	 * 
	 * @param resourcePath
	 *            the resource path pointing to a class
	 * @return the corresponding fully qualified class name
	 */
	public static String convertResourcePathToClassName(String resourcePath) {
		return resourcePath.replace('/', '.');
	}

	/**
	 * Convert a "."-based fully qualified class name to a "/"-based resource
	 * path.
	 * 
	 * @param className
	 *            the fully qualified class name
	 * @return the corresponding resource path, pointing to the class
	 */
	public static String convertClassNameToResourcePath(String className) {
		return className.replace('.', '/');
	}

	/**
	 * Return a path suitable for use with <code>ClassLoader.getResource</code>
	 * (also suitable for use with <code>Class.getResource</code> by prepending
	 * a slash ('/') to the return value. Built by taking the package of the
	 * specified class file, converting all dots ('.') to slashes ('/'), adding
	 * a trailing slash if necesssary, and concatenating the specified resource
	 * name to this. <br/>
	 * As such, this function may be used to build a path suitable for loading a
	 * resource file that is in the same package as a class file, although
	 * {@link org.springframework.core.io.ClassPathResource} is usually even
	 * more convenient.
	 * 
	 * @param clazz
	 *            the Class whose package will be used as the base
	 * @param resourceName
	 *            the resource name to append. A leading slash is optional.
	 * @return the built-up resource path
	 * @see java.lang.ClassLoader#getResource
	 * @see java.lang.Class#getResource
	 */
	public static String addResourcePathToPackagePath(Class clazz, String resourceName) {
		Assert.notNull(resourceName, "Resource name must not be null");
		if (!resourceName.startsWith("/")) {
			return classPackageAsResourcePath(clazz) + "/" + resourceName;
		}
		return classPackageAsResourcePath(clazz) + resourceName;
	}

	/**
	 * Given an input class object, return a string which consists of the
	 * class's package name as a pathname, i.e., all dots ('.') are replaced by
	 * slashes ('/'). Neither a leading nor trailing slash is added. The result
	 * could be concatenated with a slash and the name of a resource, and fed
	 * directly to <code>ClassLoader.getResource()</code>. For it to be fed to
	 * <code>Class.getResource</code> instead, a leading slash would also have
	 * to be prepended to the returned value.
	 * 
	 * @param clazz
	 *            the input class. A <code>null</code> value or the default
	 *            (empty) package will result in an empty string ("") being
	 *            returned.
	 * @return a path which represents the package name
	 * @see ClassLoader#getResource
	 * @see Class#getResource
	 */
	public static String classPackageAsResourcePath(Class clazz) {
		if (clazz == null) {
			return "";
		}
		String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf('.');
		if (packageEndIndex == -1) {
			return "";
		}
		String packageName = className.substring(0, packageEndIndex);
		return packageName.replace('.', '/');
	}

	/**
	 * Build a String that consists of the names of the classes/interfaces in
	 * the given array.
	 * <p>
	 * Basically like <code>AbstractCollection.toString()</code>, but stripping
	 * the "class "/"interface " prefix before every class name.
	 * 
	 * @param classes
	 *            a Collection of Class objects (may be <code>null</code>)
	 * @return a String of form "[com.foo.Bar, com.foo.Baz]"
	 * @see java.util.AbstractCollection#toString()
	 */
	public static String classNamesToString(Class[] classes) {
		return classNamesToString(Arrays.asList(classes));
	}

	/**
	 * Build a String that consists of the names of the classes/interfaces in
	 * the given collection.
	 * <p>
	 * Basically like <code>AbstractCollection.toString()</code>, but stripping
	 * the "class "/"interface " prefix before every class name.
	 * 
	 * @param classes
	 *            a Collection of Class objects (may be <code>null</code>)
	 * @return a String of form "[com.foo.Bar, com.foo.Baz]"
	 * @see java.util.AbstractCollection#toString()
	 */
	public static String classNamesToString(Collection classes) {
		if (Lists.isEmpty(classes)) {
			return "[]";
		}
		StringBuffer sb = new StringBuffer("[");
		for (Iterator it = classes.iterator(); it.hasNext();) {
			Class clazz = (Class) it.next();
			sb.append(clazz.getName());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Return all interfaces that the given instance implements as array,
	 * including ones implemented by superclasses.
	 * 
	 * @param instance
	 *            the instance to analyse for interfaces
	 * @return all interfaces that the given instance implements as array
	 */
	public static Class[] getAllInterfaces(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		return getAllInterfacesForClass(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as array, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyse for interfaces
	 * @return all interfaces that the given object implements as array
	 */
	public static Class[] getAllInterfacesForClass(Class clazz) {
		return getAllInterfacesForClass(clazz, null);
	}

	/**
	 * Return all interfaces that the given class implements as array, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyse for interfaces
	 * @param classLoader
	 *            the ClassLoader that the interfaces need to be visible in (may
	 *            be <code>null</code> when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as array
	 */
	public static Class[] getAllInterfacesForClass(Class clazz, ClassLoader classLoader) {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			return new Class[] { clazz };
		}
		List interfaces = new ArrayList();
		while (clazz != null) {
			for (int i = 0; i < clazz.getInterfaces().length; i++) {
				Class ifc = clazz.getInterfaces()[i];
				if (!interfaces.contains(ifc) && (classLoader == null || isVisible(ifc, classLoader))) {
					interfaces.add(ifc);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
	}

	/**
	 * Return all interfaces that the given instance implements as Set,
	 * including ones implemented by superclasses.
	 * 
	 * @param instance
	 *            the instance to analyse for interfaces
	 * @return all interfaces that the given instance implements as Set
	 */
	public static Set getAllInterfacesAsSet(Object instance) {
		Assert.notNull(instance, "Instance must not be null");
		return getAllInterfacesForClassAsSet(instance.getClass());
	}

	/**
	 * Return all interfaces that the given class implements as Set, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyse for interfaces
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set getAllInterfacesForClassAsSet(Class clazz) {
		return getAllInterfacesForClassAsSet(clazz, null);
	}

	/**
	 * Return all interfaces that the given class implements as Set, including
	 * ones implemented by superclasses.
	 * <p>
	 * If the class itself is an interface, it gets returned as sole interface.
	 * 
	 * @param clazz
	 *            the class to analyse for interfaces
	 * @param classLoader
	 *            the ClassLoader that the interfaces need to be visible in (may
	 *            be <code>null</code> when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader) {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			return Collections.singleton(clazz);
		}
		Set interfaces = new LinkedHashSet();
		while (clazz != null) {
			for (int i = 0; i < clazz.getInterfaces().length; i++) {
				Class ifc = clazz.getInterfaces()[i];
				if (classLoader == null || isVisible(ifc, classLoader)) {
					interfaces.add(ifc);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return interfaces;
	}

	/**
	 * Create a composite interface Class for the given interfaces, implementing
	 * the given interfaces in one single Class.
	 * <p>
	 * This implementation builds a JDK proxy class for the given interfaces.
	 * 
	 * @param interfaces
	 *            the interfaces to merge
	 * @param classLoader
	 *            the ClassLoader to create the composite Class in
	 * @return the merged interface as Class
	 * @see java.lang.reflect.Proxy#getProxyClass
	 */
	public static Class createCompositeInterface(Class[] interfaces, ClassLoader classLoader) {
		Assert.notEmpty(interfaces, "Interfaces must not be empty");
		Assert.notNull(classLoader, "ClassLoader must not be null");
		return Proxy.getProxyClass(classLoader, interfaces);
	}

	/**
	 * Check whether the given class is visible in the given ClassLoader.
	 * 
	 * @param clazz
	 *            the class to check (typically an interface)
	 * @param classLoader
	 *            the ClassLoader to check against (may be <code>null</code>, in
	 *            which case this method will always return <code>true</code>)
	 */
	public static boolean isVisible(Class clazz, ClassLoader classLoader) {
		if (classLoader == null) {
			return true;
		}
		try {
			Class actualClass = classLoader.loadClass(clazz.getName());
			return (clazz == actualClass);
			// Else: different interface class found...
		} catch (ClassNotFoundException ex) {
			// No interface class found...
			return false;
		}
	}

	private static class DemsyClassLoader extends ClassLoader {

		private DemsyClassLoader(ClassLoader parent) {
			super(parent);
		}

		protected Class defineClass(String className, File file) throws IOException {
			FileInputStream fis = null;
			ByteArrayOutputStream baos = null;

			byte[] bytecodes = new byte[1024];
			int ln = 0;

			try {
				fis = new FileInputStream(file);
				baos = new ByteArrayOutputStream();

				while ((ln = fis.read(bytecodes)) > 0) {
					baos.write(bytecodes, 0, ln);
				}

				baos.flush();

				return defineClass(className, baos.toByteArray());
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
					}
				}

				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}

		}

		protected Class defineClass(String className, byte[] bytecodes) {
			Class cls = defineClass(className, bytecodes, 0, bytecodes.length);
			this.resolveClass(cls);

			return cls;
		}

		protected Class reloadClass(String clsname) throws ClassNotFoundException {
			try {
				return defineClass(clsname, new File(Demsy.appconfig.getClassDir() + File.separator + clsname.replace(".", File.separator) + ".class"));
			} catch (IOException e) {
				throw new ClassNotFoundException(e.getMessage());
			}
			// return super.loadClass(clsname);
		}

	}

	public static Class getType(Class type, String prop) throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		if (type == null || Str.isEmpty(prop))
			return null;

		int dot = prop.indexOf(".");
		if (dot > 0) {
			Class t = Mirror.me(type).getField(prop.substring(0, dot)).getType();
			return getType(t, prop.substring(dot + 1));
		} else {
			Mirror me = Mirror.me(type);
			try {
				return me.getField(prop).getType();
			} catch (Throwable e) {
				return type.getMethod("get" + prop.substring(0, 1).toUpperCase() + prop.substring(1)).getReturnType();
			}
		}
	}

	public static boolean isLazy(Object type) {
		if (type == null) {
			return false;
		}
		if (type instanceof Class) {
			return Cls.isLazy((Class) type);
		}
		return Cls.isLazy(type.getClass());
	}

	public static boolean isLazy(Class type) {
		if (type == null)
			return false;

		return type.getSimpleName().endsWith(LazyClassAgent.LAZY_CLASSNAME_SUFFIX);
	}

	public static boolean isEntityType(Class type) {
		if (type == null)
			return false;

		if (isLazy(type) || Cls.isAgent(type)) {
			return true;
		}
		return type.getAnnotation(Entity.class) != null;
	}

	public static boolean isAgent(Class type) {
		if (type == null)
			return false;

		return type.getSimpleName().endsWith(ClassAgent.CLASSNAME_SUFFIX);
	}

	public static Class getType(Class proxyClass) {
		if (isAgent(proxyClass) || isLazy(proxyClass))
			return proxyClass.getSuperclass();

		return proxyClass;
	}

	public static String getDisplayName(Field field) {
		if (field == null) {
			return "";
		}
		return "(" + field.getName() + ")";
	}

	public static String getDisplayName(Class classOfEntity) {
		if (classOfEntity == null) {
			return "";
		}
		return "(" + classOfEntity.getSimpleName() + ")";
	}

	public static Class getObjectType(Class t) {
		if (int.class.equals(t)) {
			return Integer.class;
		}
		String type = t.getName();
		if (type.indexOf(".") < 0) {
			try {
				return forName("java.lang." + type.substring(0, 1).toUpperCase() + type.substring(1));
			} catch (Throwable e) {
			}
		}

		return t;
	}

	public static boolean isSimpleType(Class cls) {
		return Cls.isString(cls) || Cls.isBoolean(cls) || Cls.isNumber(cls);
	}

	public static boolean isDate(Class cls) {
		return Date.class.isAssignableFrom(cls);
	}

	public static boolean isString(Class cls) {
		return cls.equals(String.class);
	}

	public static boolean isBoolean(Class kls) {
		if (kls.equals(boolean.class) || kls.equals(Boolean.class)) {
			return true;
		}

		return false;
	}

	public static boolean isNumber(Class cls) {
		if (Cls.isInteger(cls) || Cls.isLong(cls) || Cls.isByte(cls) || Cls.isShort(cls) || Cls.isDouble(cls) || Cls.isFloat(cls) || Number.class.isAssignableFrom(cls)) {
			return true;
		}

		return false;
	}

	public static boolean isFloat(Class kls) {
		if (kls.equals(float.class) || kls.equals(Float.class)) {
			return true;
		}

		return false;
	}

	public static boolean isDouble(Class kls) {
		if (kls.equals(double.class) || kls.equals(Double.class)) {
			return true;
		}

		return false;
	}

	public static boolean isByte(Class kls) {
		if (kls.equals(byte.class) || kls.equals(Byte.class)) {
			return true;
		}

		return false;
	}

	public static boolean isShort(Class kls) {
		if (kls.equals(short.class) || kls.equals(Short.class)) {
			return true;
		}

		return false;
	}

	public static boolean isInteger(Class kls) {
		if (kls.equals(int.class) || kls.equals(Integer.class)) {
			return true;
		}

		return false;
	}

	public static boolean isLong(Class kls) {
		if (kls.equals(long.class) || kls.equals(Long.class)) {
			return true;
		}

		return false;
	}

	public static boolean hasField(Class klass, String fldname) {
		try {
			return Mirror.me(klass).getField(fldname) != null;
		} catch (Throwable e) {
			return false;
		}
	}
}
