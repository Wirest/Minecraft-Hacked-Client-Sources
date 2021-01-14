package com.etb.client.utils.thealtening.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtility {

	public ReflectionUtility(String className) {
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Class<?> clazz;
	public void setStaticField(String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);

		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}

}
