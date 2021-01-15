package dev.astroclient.security.indirection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public final class Exceptions {

	public static void handleException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Couldn't find the method:\n" + ex.getMessage());
		}

		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Unable to access method:\n" + ex.getMessage());
		}

		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}

		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}

		throw new UndeclaredThrowableException(ex);
	}


	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}


	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}

		if (ex instanceof Error) {
			throw (Error) ex;
		}

		throw new UndeclaredThrowableException(ex);
	}

}
