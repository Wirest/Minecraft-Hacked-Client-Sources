package store.shadowclient.client.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventManager {
	private static Map<Class<? extends Event>, ArrayHelper<Data>> REGISTRY_MAP = new HashMap();

	public static void register(Object o) {
		for (Method method : o.getClass().getDeclaredMethods()) {
			if (!isMethodBad(method)) {
				register(method, o);
			}
		}
	}

	public void register(Object o, Class<? extends Event> clazz) {
		for (Method method : o.getClass().getDeclaredMethods()) {
			if (!isMethodBad(method, clazz)) {
				register(method, o);
			}
		}
	}

	private static void register(Method method, Object o) {
		Class<?> clazz = method.getParameterTypes()[0];
		final Data methodData = new Data(o, method, method.getAnnotation(EventTarget.class).value());

		if (!methodData.target.isAccessible()) {
			methodData.target.setAccessible(true);
		}

		if (REGISTRY_MAP.containsKey(clazz)) {
			if (!REGISTRY_MAP.get(clazz).contains(methodData)) {
				REGISTRY_MAP.get(clazz).add(methodData);
				sortListValue((Class<? extends Event>) clazz);
			}
		} else {
			REGISTRY_MAP.put((Class<? extends Event>) clazz, new ArrayHelper<Data>() {
				{
					this.add(methodData);
				}
			});
		}
	}

	public static void unregister(final Object o) {
		for (ArrayHelper<Data> flexibalArray : REGISTRY_MAP.values()) {
			for (Data methodData : flexibalArray) {
				if (methodData.source.equals(o)) {
					flexibalArray.remove(methodData);
				}
			}
		}

		cleanMap(true);
	}

	public void unregister(final Object o, final Class<? extends Event> clazz) {
		if (REGISTRY_MAP.containsKey(clazz)) {
			for (final Data methodData : REGISTRY_MAP.get(clazz)) {
				if (methodData.source.equals(o)) {
					REGISTRY_MAP.get(clazz).remove(methodData);
				}
			}

			cleanMap(true);
		}
	}

	public static void cleanMap(boolean b) {
		Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();

		while (iterator.hasNext()) {
			if (!b || iterator.next().getValue().isEmpty()) {
				iterator.remove();
			}
		}
	}

	public void removeEnty(Class<? extends Event> clazz) {
		Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = REGISTRY_MAP.entrySet().iterator();

		while (iterator.hasNext()) {
			if (iterator.next().getKey().equals(clazz)) {
				iterator.remove();
				break;
			}
		}
	}

	private static void sortListValue(Class<? extends Event> clazz) {
		ArrayHelper<Data> flexibleArray = new ArrayHelper<Data>();

		for (byte b : Priority.VALUE_ARRAY) {
			for (Data methodData : REGISTRY_MAP.get(clazz)) {
				if (methodData.priority == b) {
					flexibleArray.add(methodData);
				}
			}
		}

		REGISTRY_MAP.put(clazz, flexibleArray);
	}

	private static boolean isMethodBad(final Method method) {
		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
	}

	private boolean isMethodBad(Method method, Class<? extends Event> clazz) {
		return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
	}

	public ArrayHelper<Data> get(final Class<? extends Event> clazz) {
		return REGISTRY_MAP.get(clazz);
	}

	public void shutdown() {
		REGISTRY_MAP.clear();
	}
}
