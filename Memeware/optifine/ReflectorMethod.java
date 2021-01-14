package optifine;

import java.lang.reflect.Method;

public class ReflectorMethod {
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName) {
        this(reflectorClass, targetMethodName, (Class[]) null, false);
    }

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName, Class[] targetMethodParameterTypes) {
        this(reflectorClass, targetMethodName, targetMethodParameterTypes, false);
    }

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName, Class[] targetMethodParameterTypes, boolean lazyResolve) {
        this.reflectorClass = null;
        this.targetMethodName = null;
        this.targetMethodParameterTypes = null;
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = reflectorClass;
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;

        if (!lazyResolve) {
            Method m = this.getTargetMethod();
        }
    }

    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        } else {
            this.checked = true;
            Class cls = this.reflectorClass.getTargetClass();

            if (cls == null) {
                return null;
            } else {
                try {
                    if (this.targetMethodParameterTypes == null) {
                        Method[] e = Reflector.getMethods(cls, this.targetMethodName);

                        if (e.length <= 0) {
                            Config.log("(Reflector) Method not present: " + cls.getName() + "." + this.targetMethodName);
                            return null;
                        }

                        if (e.length > 1) {
                            Config.warn("(Reflector) More than one method found: " + cls.getName() + "." + this.targetMethodName);

                            for (int i = 0; i < e.length; ++i) {
                                Method m = e[i];
                                Config.warn("(Reflector)  - " + m);
                            }

                            return null;
                        }

                        this.targetMethod = e[0];
                    } else {
                        this.targetMethod = Reflector.getMethod(cls, this.targetMethodName, this.targetMethodParameterTypes);
                    }

                    if (this.targetMethod == null) {
                        Config.log("(Reflector) Method not present: " + cls.getName() + "." + this.targetMethodName);
                        return null;
                    } else {
                        this.targetMethod.setAccessible(true);
                        return this.targetMethod;
                    }
                } catch (Throwable var5) {
                    var5.printStackTrace();
                    return null;
                }
            }
        }
    }

    public boolean exists() {
        return this.checked ? this.targetMethod != null : this.getTargetMethod() != null;
    }

    public Class getReturnType() {
        Method tm = this.getTargetMethod();
        return tm == null ? null : tm.getReturnType();
    }

    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }
}
