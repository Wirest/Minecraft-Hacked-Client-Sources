package optifine;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectorMethod {
    private ReflectorClass reflectorClass = null;
    private String targetMethodName = null;
    private Class[] targetMethodParameterTypes = null;
    private boolean checked = false;
    private Method targetMethod = null;

    public ReflectorMethod(ReflectorClass paramReflectorClass, String paramString) {
        this(paramReflectorClass, paramString, (Class[]) null, false);
    }

    public ReflectorMethod(ReflectorClass paramReflectorClass, String paramString, Class[] paramArrayOfClass) {
        this(paramReflectorClass, paramString, paramArrayOfClass, false);
    }

    public ReflectorMethod(ReflectorClass paramReflectorClass, String paramString, Class[] paramArrayOfClass, boolean paramBoolean) {
        this.reflectorClass = paramReflectorClass;
        this.targetMethodName = paramString;
        this.targetMethodParameterTypes = paramArrayOfClass;
        if (!paramBoolean) {
            Method localMethod = getTargetMethod();
        }
    }

    public static Method getMethod(Class paramClass, String paramString, Class[] paramArrayOfClass) {
        Method[] arrayOfMethod = paramClass.getDeclaredMethods();
        for (int i = 0; i < arrayOfMethod.length; i++) {
            Method localMethod = arrayOfMethod[i];
            if (localMethod.getName().equals(paramString)) {
                Class[] arrayOfClass = localMethod.getParameterTypes();
                if (Reflector.matchesTypes(paramArrayOfClass, arrayOfClass)) {
                    return localMethod;
                }
            }
        }
        return null;
    }

    public static Method[] getMethods(Class paramClass, String paramString) {
        ArrayList localArrayList = new ArrayList();
        Method[] arrayOfMethod1 = paramClass.getDeclaredMethods();
        for (int i = 0; i < arrayOfMethod1.length; i++) {
            Method localMethod = arrayOfMethod1[i];
            if (localMethod.getName().equals(paramString)) {
                localArrayList.add(localMethod);
            }
        }
        Method[] arrayOfMethod2 = (Method[]) (Method[]) localArrayList.toArray(new Method[localArrayList.size()]);
        return arrayOfMethod2;
    }

    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        Class localClass = this.reflectorClass.getTargetClass();
        if (localClass == null) {
            return null;
        }
        try {
            if (this.targetMethodParameterTypes == null) {
                Method[] arrayOfMethod = getMethods(localClass, this.targetMethodName);
                if (arrayOfMethod.length <= 0) {
                    Config.log("(Reflector) Method not present: " + localClass.getName() + "." + this.targetMethodName);
                    return null;
                }
                if (arrayOfMethod.length > 1) {
                    Config.warn("(Reflector) More than one method found: " + localClass.getName() + "." + this.targetMethodName);
                    for (int i = 0; i < arrayOfMethod.length; i++) {
                        Method localMethod = arrayOfMethod[i];
                        Config.warn("(Reflector)  - " + localMethod);
                    }
                    return null;
                }
                this.targetMethod = arrayOfMethod[0];
            } else {
                this.targetMethod = getMethod(localClass, this.targetMethodName, this.targetMethodParameterTypes);
            }
            if (this.targetMethod == null) {
                Config.log("(Reflector) Method not present: " + localClass.getName() + "." + this.targetMethodName);
                return null;
            }
            this.targetMethod.setAccessible(true);
            return this.targetMethod;
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return null;
    }

    public boolean exists() {
        return this.targetMethod != null;
    }

    public Class getReturnType() {
        Method localMethod = getTargetMethod();
        return localMethod == null ? null : localMethod.getReturnType();
    }

    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }
}




