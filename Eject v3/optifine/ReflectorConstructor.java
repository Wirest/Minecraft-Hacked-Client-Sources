package optifine;

import java.lang.reflect.Constructor;

public class ReflectorConstructor {
    private ReflectorClass reflectorClass = null;
    private Class[] parameterTypes = null;
    private boolean checked = false;
    private Constructor targetConstructor = null;

    public ReflectorConstructor(ReflectorClass paramReflectorClass, Class[] paramArrayOfClass) {
        this.reflectorClass = paramReflectorClass;
        this.parameterTypes = paramArrayOfClass;
        Constructor localConstructor = getTargetConstructor();
    }

    private static Constructor findConstructor(Class paramClass, Class[] paramArrayOfClass) {
        Constructor[] arrayOfConstructor = paramClass.getDeclaredConstructors();
        for (int i = 0; i < arrayOfConstructor.length; i++) {
            Constructor localConstructor = arrayOfConstructor[i];
            Class[] arrayOfClass = localConstructor.getParameterTypes();
            if (Reflector.matchesTypes(paramArrayOfClass, arrayOfClass)) {
                return localConstructor;
            }
        }
        return null;
    }

    public Constructor getTargetConstructor() {
        if (this.checked) {
            return this.targetConstructor;
        }
        this.checked = true;
        Class localClass = this.reflectorClass.getTargetClass();
        if (localClass == null) {
            return null;
        }
        try {
            this.targetConstructor = findConstructor(localClass, this.parameterTypes);
            if (this.targetConstructor == null) {
                Config.dbg("(Reflector) Constructor not present: " + localClass.getName() + ", params: " + Config.arrayToString((Object[]) this.parameterTypes));
            }
            if (this.targetConstructor != null) {
                this.targetConstructor.setAccessible(true);
            }
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return this.targetConstructor;
    }

    public boolean exists() {
        return this.targetConstructor != null;
    }

    public void deactivate() {
        this.checked = true;
        this.targetConstructor = null;
    }
}




