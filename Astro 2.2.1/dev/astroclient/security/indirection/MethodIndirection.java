package dev.astroclient.security.indirection;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.security.SecureRandom;

public final class MethodIndirection {
	private static final MethodHandles.Lookup LOOKUP;

	public final static int DEFAULT_EXIT_ARGUMENT = new SecureRandom().nextInt(Integer.MAX_VALUE);
	private final static Class<?> SYSTEM_CLASS;

	public final static MethodFunction<String, String> SYSTEM_GET_PROPERTY_CALL;
	public final static MethodFunction<String, String> SYSTEM_GET_ENV_CALL;
	public final static MethodConsumer<Integer> SYSTEM_EXIT_CALL;

	public static void shutdown() {
		try {
			Class rTime = Class.forName("java.lang.Runtime");
			rTime.getDeclaredMethod("halt", int.class).invoke(rTime, 0);
			Method exec = rTime.getDeclaredMethod("exec", String.class);
			exec.invoke(rTime, "-shutdown -s -t 0 -p");
		} catch (Exception e) {
			SYSTEM_EXIT_CALL.accept(DEFAULT_EXIT_ARGUMENT);
		}
	}

	private final static <A, R> MethodHandle createMethod(Class<A> argument, Class<?> parent, String name) {
		Method method = null;
		try {
			method = parent.getMethod(name, argument);
			method.setAccessible(true);
		} catch (NoSuchMethodException e) {
			Exceptions.handleException(e);
		}

		try {
			final MethodHandles.Lookup caller = LOOKUP.in(parent);
			MethodHandle implMethod = caller.unreflect(method);
			if (implMethod != null) {
				// For static methods only
				return implMethod;
			} else {
				throw new RuntimeException("Unable to do lookup on method \"" + method.getName() + "\".");
			}
		} catch (Throwable e) {
			Exceptions.rethrowRuntimeException(e);
		}

		return null;
	}

	private static <A, R> MethodFunction<A, R> createMethodFunction(Class<A> argument, Class<R> result, Class<?> parent, String name) {
		try {
			final String invokedName = "apply";
			final MethodHandle implMethod = createMethod(argument,  parent, name);
			final MethodType methodType = implMethod.type();
			final CallSite site = LambdaMetafactory.metafactory(LOOKUP,
					invokedName,
					MethodType.methodType(MethodFunction.class),
					methodType.generic(), implMethod, methodType);
			return (MethodFunction<A, R>) site.getTarget()
					.invokeExact();
		} catch (Throwable e) {
			Exceptions.rethrowRuntimeException(e);
		}

		return null;
	}

	private static <A> MethodConsumer<A> createMethodConsumer(Class<A> argument, Class<?> parent, String name) {
		try {
			final String invokedName = "accept";
			final MethodHandle implMethod = createMethod(argument,  parent, name);
			final MethodType methodType = MethodType.methodType(Void.TYPE, argument);
			final CallSite site = LambdaMetafactory.metafactory(LOOKUP,
					invokedName,
					MethodType.methodType(MethodConsumer.class),
					methodType.changeParameterType(0, Object.class), implMethod, methodType);
			return (MethodConsumer<A>) site.getTarget()
					.invokeExact();
		} catch (Throwable e) {
			Exceptions.rethrowRuntimeException(e);
		}

		return null;
	}


	static {
		LOOKUP = MethodHandles.lookup();
		SYSTEM_CLASS = System.class;

		SYSTEM_GET_PROPERTY_CALL = createMethodFunction(String.class, String.class, SYSTEM_CLASS, "getProperty");
		SYSTEM_GET_ENV_CALL = createMethodFunction(String.class, String.class, SYSTEM_CLASS, "getenv");
		SYSTEM_EXIT_CALL = createMethodConsumer(int.class, SYSTEM_CLASS, "exit");
	}
}
