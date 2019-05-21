package com.github.ideahut.sbms.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil {

	private ReflectionUtil() {}
	
	/*
	 * CLONE OBJECT
	 */
	@SuppressWarnings("unchecked")
	public static<T> T clone(T t, int maxParent) throws Exception {
		Class<?> clazz = t.getClass();
		T o = (T)clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			f.set(o, f.get(t));
		}
		if (maxParent > 0) {
			int count = 0;
			Class<?> parent = clazz.getSuperclass();
			do {
				if (Object.class.equals(parent)) {
					count = maxParent;
				} else {
					fields = parent.getDeclaredFields();
					for (Field f : fields) {
						f.setAccessible(true);
						f.set(o, f.get(t));
					}
					parent = parent.getSuperclass();
				}
				count ++;
			} while (count < maxParent);
		} 
		else if (maxParent < 0) {
			Class<?> parent = clazz.getSuperclass();
			while (!Object.class.equals(parent)) {
				fields = parent.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(true);
					f.set(o, f.get(t));
				}
				parent = parent.getSuperclass();
			}
		}
		return o;
	}
	
	public static<T> T clone(T t) throws Exception {
		return clone(t, -1);
	}
	
	public static<T> T cloneForRuntime(T t, int maxParent) {
		try {
			return clone(t, maxParent);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static<T> T cloneForRuntime(T t) {
		return cloneForRuntime(t, -1);
	}
	
	
	
	/*
	 * INVOKE FIELD FOR METHOD GET OR IS
	 */
	@SuppressWarnings("unchecked")
	public static<T> T invokeFieldForMethodGetOrIs(Object object, String field) {
		String name = field.substring(0, 1).toUpperCase() + field.substring(1);
		Method method = null;
		try {
			method = object.getClass().getMethod("get" + name);
		} catch (Exception e1) {
			try {
				method = object.getClass().getMethod("is" + name);
			} catch (Exception e2) {
				throw new RuntimeException("Cannot find field '" + field + "' on class: " + object.getClass().getName());
			}
		}
		if (method != null) {
			try {
				return (T)method.invoke(object);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	
	/*
	 * GET DECLARED FIELD
	 */
	public static Field getDeclaredField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/*
	 * INVOKE DECLARED FIELD
	 */
	@SuppressWarnings("unchecked")
	public static<T> T getDeclaredFieldValue(Class<?> clazz, Object target, String name) throws Exception {
		Field f = clazz != null ? clazz.getDeclaredField(name) : target.getClass().getDeclaredField(name);
		f.setAccessible(true);
		return (T) f.get(target);
	}
	
	public static<T> T getDeclaredFieldValue(Object target, String name) throws Exception {
		return getDeclaredFieldValue(null, target, name);
	}
	
	public static<T> T getDeclaredFieldValueForRuntime(Class<?> clazz, Object target, String name) {
		try {
			return getDeclaredFieldValue(clazz, target, name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static<T> T getDeclaredFieldValueForRuntime(Object target, String name) {
		return getDeclaredFieldValueForRuntime(null, target, name);
	}
	
	public static void setDeclaredFieldValue(Class<?> clazz, Object target, String name, Object value) throws Exception {
		Field f = clazz != null ? clazz.getDeclaredField(name) : target.getClass().getDeclaredField(name);
		f.setAccessible(true);
		f.set(target, value);
	}
	
	public static void setDeclaredFieldValue(Object target, String name, Object value) throws Exception {
		setDeclaredFieldValue(null, target, name, value);
	}
	
	public static void setDeclaredFieldValueForRuntime(Class<?> clazz, Object target, String name, Object value) {
		try {
			setDeclaredFieldValue(clazz, target, name, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void setDeclaredFieldValueForRuntime(Object target, String name, Object value) {
		setDeclaredFieldValueForRuntime(null, target, name, value);
	}
	
	
	/*
	 * INVOKE DECLARED METHOD
	 */
	@SuppressWarnings("unchecked")
	public static<T> T invokeDeclaredMethod(Object target, Class<?> clazz, String name, Class<?>[] types, Object...args) throws Exception {
		if (types == null && args != null) {
			types = new Class<?>[args.length];
			for (int i = 0; i < types.length; i++) {
				types[i] = args[i].getClass();
			}
		}
		Method m = (clazz != null ? clazz : target.getClass()).getDeclaredMethod(name, types);
		m.setAccessible(true);
		return (T) m.invoke(target, args);
	}
	
	public static<T> T invokeDeclaredMethod(Object target, Class<?> clazz, String name, Object...args) throws Exception {
		return invokeDeclaredMethod(target, clazz, name, null, args);
	}
	
	public static<T> T invokeDeclaredMethodForRuntime(Object target, Class<?> clazz, String name, Class<?>[] types, Object...args) {
		try {
			return invokeDeclaredMethod(target, clazz, name, types, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static<T> T invokeDeclaredMethodForRuntime(Object target, Class<?> clazz, String name, Object...args) {
		return invokeDeclaredMethodForRuntime(target, clazz, name, null, args);
	}
	
	
	/*
	 * INVOKE METHOD
	 */
	@SuppressWarnings("unchecked")
	public static<T> T invokeMethod(Object target, Class<?> clazz, String name, Class<?>[] types, Object...args) throws Exception {
		if (types == null && args != null) {
			types = new Class<?>[args.length];
			for (int i = 0; i < types.length; i++) {
				types[i] = args[i].getClass();
			}
		}
		Method m = (clazz != null ? clazz : target.getClass()).getMethod(name, types);
		return (T) m.invoke(target, args);
	}
	
	public static<T> T invokeMethod(Object target, Class<?> clazz, String name, Object...args) throws Exception {
		return invokeMethod(target, clazz, name, null, args);
	}
	
	public static<T> T invokeMethodForRuntime(Object target, Class<?> clazz, String name, Class<?>[] types, Object...args) {
		try {
			return invokeMethod(target, clazz, name, types, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static<T> T invokeMethodForRuntime(Object target, Class<?> clazz, String name, Object...args) {
		return invokeMethodForRuntime(target, clazz, name, null, args);
	}
	
	
	/*
	 * IS METHOD EXIST	
	 */
	public static boolean isMethodExist(Class<?> clazz, String name, Class<?>...parameterTypes) {
		try {
			clazz.getMethod(name, parameterTypes);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}