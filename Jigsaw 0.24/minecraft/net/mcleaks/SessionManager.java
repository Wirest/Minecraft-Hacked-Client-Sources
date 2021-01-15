package net.mcleaks;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionManager {
	public static void setSession(String mcName) {
		setSession(new Session(mcName, "", "", "mojang"));
	}

	public static void setSession(Session session) {
		setFieldByClass(Minecraft.getMinecraft(), session);
	}

	public static void setFieldByClass(Object parentObject, Object newObject) {
		Field field = null;
		for (Field f : parentObject.getClass().getDeclaredFields()) {
			if (f.getType().isInstance(newObject)) {
				field = f;
				break;
			}
		}
		if (field == null) {
			return;
		}
		try {
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			field.set(parentObject, newObject);
			field.setAccessible(accessible);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
