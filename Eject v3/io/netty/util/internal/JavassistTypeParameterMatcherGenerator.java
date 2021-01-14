package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import javassist.*;

import java.lang.reflect.Method;

public final class JavassistTypeParameterMatcherGenerator {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(JavassistTypeParameterMatcherGenerator.class);
    private static final ClassPool classPool = new ClassPool(true);

    static {
        classPool.appendClassPath(new ClassClassPath(NoOpTypeParameterMatcher.class));
    }

    public static void appendClassPath(ClassPath paramClassPath) {
        classPool.appendClassPath(paramClassPath);
    }

    public static void appendClassPath(String paramString)
            throws NotFoundException {
        classPool.appendClassPath(paramString);
    }

    public static TypeParameterMatcher generate(Class<?> paramClass) {
        ClassLoader localClassLoader = PlatformDependent.getContextClassLoader();
        if (localClassLoader == null) {
            localClassLoader = PlatformDependent.getSystemClassLoader();
        }
        return generate(paramClass, localClassLoader);
    }

    public static TypeParameterMatcher generate(Class<?> paramClass, ClassLoader paramClassLoader) {
        String str1 = typeName(paramClass);
        String str2 = "io.netty.util.internal.__matchers__." + str1 + "Matcher";
        try {
            return (TypeParameterMatcher) Class.forName(str2, true, paramClassLoader).newInstance();
        } catch (Exception localException1) {
            CtClass localCtClass = classPool.getAndRename(NoOpTypeParameterMatcher.class.getName(), str2);
            localCtClass.setModifiers(localCtClass.getModifiers() ^ 0x10);
            localCtClass.getDeclaredMethod("match").setBody("{ return $1 instanceof " + str1 + "; }");
            byte[] arrayOfByte = localCtClass.toBytecode();
            localCtClass.detach();
            Method localMethod = ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE});
            localMethod.setAccessible(true);
            Class localClass = (Class) localMethod.invoke(paramClassLoader, new Object[]{str2, arrayOfByte, Integer.valueOf(0), Integer.valueOf(arrayOfByte.length)});
            if (paramClass != Object.class) {
                logger.debug("Generated: {}", localClass.getName());
            }
            return (TypeParameterMatcher) localClass.newInstance();
        } catch (RuntimeException localRuntimeException) {
            throw localRuntimeException;
        } catch (Exception localException2) {
            throw new RuntimeException(localException2);
        }
    }

    private static String typeName(Class<?> paramClass) {
        if (paramClass.isArray()) {
            return typeName(paramClass.getComponentType()) + "[]";
        }
        return paramClass.getName();
    }
}




