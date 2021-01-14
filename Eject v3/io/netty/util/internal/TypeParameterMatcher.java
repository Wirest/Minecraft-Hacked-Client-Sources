package io.netty.util.internal;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public abstract class TypeParameterMatcher {
    private static final TypeParameterMatcher NOOP = new NoOpTypeParameterMatcher();
    private static final Object TEST_OBJECT = new Object();

    public static TypeParameterMatcher get(Class<?> paramClass) {
        Map localMap = InternalThreadLocalMap.get().typeParameterMatcherGetCache();
        Object localObject = (TypeParameterMatcher) localMap.get(paramClass);
        if (localObject == null) {
            if (paramClass == Object.class) {
                localObject = NOOP;
            } else if (PlatformDependent.hasJavassist()) {
                try {
                    localObject = JavassistTypeParameterMatcherGenerator.generate(paramClass);
                    ((TypeParameterMatcher) localObject).match(TEST_OBJECT);
                } catch (IllegalAccessError localIllegalAccessError) {
                    localObject = null;
                } catch (Exception localException) {
                    localObject = null;
                }
            }
            if (localObject == null) {
                localObject = new ReflectiveMatcher(paramClass);
            }
            localMap.put(paramClass, localObject);
        }
        return (TypeParameterMatcher) localObject;
    }

    public static TypeParameterMatcher find(Object paramObject, Class<?> paramClass, String paramString) {
        Map localMap = InternalThreadLocalMap.get().typeParameterMatcherFindCache();
        Class localClass = paramObject.getClass();
        Object localObject = (Map) localMap.get(localClass);
        if (localObject == null) {
            localObject = new HashMap();
            localMap.put(localClass, localObject);
        }
        TypeParameterMatcher localTypeParameterMatcher = (TypeParameterMatcher) ((Map) localObject).get(paramString);
        if (localTypeParameterMatcher == null) {
            localTypeParameterMatcher = get(find0(paramObject, paramClass, paramString));
            ((Map) localObject).put(paramString, localTypeParameterMatcher);
        }
        return localTypeParameterMatcher;
    }

    private static Class<?> find0(Object paramObject, Class<?> paramClass, String paramString) {
        Class localClass1 = paramObject.getClass();
        Class localClass2 = localClass1;
        do {
            while (localClass2.getSuperclass() == paramClass) {
                int i = -1;
                TypeVariable[] arrayOfTypeVariable = localClass2.getSuperclass().getTypeParameters();
                for (int j = 0; j < arrayOfTypeVariable.length; j++) {
                    if (paramString.equals(arrayOfTypeVariable[j].getName())) {
                        i = j;
                        break;
                    }
                }
                if (i < 0) {
                    throw new IllegalStateException("unknown type parameter '" + paramString + "': " + paramClass);
                }
                Type localType1 = localClass2.getGenericSuperclass();
                if (!(localType1 instanceof ParameterizedType)) {
                    return Object.class;
                }
                Type[] arrayOfType = ((ParameterizedType) localType1).getActualTypeArguments();
                Type localType2 = arrayOfType[i];
                if ((localType2 instanceof ParameterizedType)) {
                    localType2 = ((ParameterizedType) localType2).getRawType();
                }
                if ((localType2 instanceof Class)) {
                    return (Class) localType2;
                }
                Object localObject;
                if ((localType2 instanceof GenericArrayType)) {
                    localObject = ((GenericArrayType) localType2).getGenericComponentType();
                    if ((localObject instanceof ParameterizedType)) {
                        localObject = ((ParameterizedType) localObject).getRawType();
                    }
                    if ((localObject instanceof Class)) {
                        return Array.newInstance((Class) localObject, 0).getClass();
                    }
                }
                if ((localType2 instanceof TypeVariable)) {
                    localObject = (TypeVariable) localType2;
                    localClass2 = localClass1;
                    if (!(((TypeVariable) localObject).getGenericDeclaration() instanceof Class)) {
                        return Object.class;
                    }
                    paramClass = (Class) ((TypeVariable) localObject).getGenericDeclaration();
                    paramString = ((TypeVariable) localObject).getName();
                    if (!paramClass.isAssignableFrom(localClass1)) {
                        return Object.class;
                    }
                } else {
                    return fail(localClass1, paramString);
                }
            }
            localClass2 = localClass2.getSuperclass();
        } while (localClass2 != null);
        return fail(localClass1, paramString);
    }

    private static Class<?> fail(Class<?> paramClass, String paramString) {
        throw new IllegalStateException("cannot determine the type of the type parameter '" + paramString + "': " + paramClass);
    }

    public abstract boolean match(Object paramObject);

    private static final class ReflectiveMatcher
            extends TypeParameterMatcher {
        private final Class<?> type;

        ReflectiveMatcher(Class<?> paramClass) {
            this.type = paramClass;
        }

        public boolean match(Object paramObject) {
            return this.type.isInstance(paramObject);
        }
    }
}




