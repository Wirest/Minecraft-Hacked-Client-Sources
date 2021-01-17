// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

public class ReflectorMethod
{
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;
    
    public ReflectorMethod(final ReflectorClass p_i91_1_, final String p_i91_2_) {
        this(p_i91_1_, p_i91_2_, null, false);
    }
    
    public ReflectorMethod(final ReflectorClass p_i92_1_, final String p_i92_2_, final Class[] p_i92_3_) {
        this(p_i92_1_, p_i92_2_, p_i92_3_, false);
    }
    
    public ReflectorMethod(final ReflectorClass p_i93_1_, final String p_i93_2_, final Class[] p_i93_3_, final boolean p_i93_4_) {
        this.reflectorClass = null;
        this.targetMethodName = null;
        this.targetMethodParameterTypes = null;
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = p_i93_1_;
        this.targetMethodName = p_i93_2_;
        this.targetMethodParameterTypes = p_i93_3_;
        if (!p_i93_4_) {
            this.getTargetMethod();
        }
    }
    
    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            if (this.targetMethodParameterTypes == null) {
                final Method[] amethod = getMethods(oclass, this.targetMethodName);
                if (amethod.length <= 0) {
                    Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                    return null;
                }
                if (amethod.length > 1) {
                    Config.warn("(Reflector) More than one method found: " + oclass.getName() + "." + this.targetMethodName);
                    for (int i = 0; i < amethod.length; ++i) {
                        final Method method = amethod[i];
                        Config.warn("(Reflector)  - " + method);
                    }
                    return null;
                }
                this.targetMethod = amethod[0];
            }
            else {
                this.targetMethod = getMethod(oclass, this.targetMethodName, this.targetMethodParameterTypes);
            }
            if (this.targetMethod == null) {
                Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                return null;
            }
            this.targetMethod.setAccessible(true);
            return this.targetMethod;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    public boolean exists() {
        return this.checked ? (this.targetMethod != null) : (this.getTargetMethod() != null);
    }
    
    public Class getReturnType() {
        final Method method = this.getTargetMethod();
        return (method == null) ? null : method.getReturnType();
    }
    
    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }
    
    public static Method getMethod(final Class p_getMethod_0_, final String p_getMethod_1_, final Class[] p_getMethod_2_) {
        final Method[] amethod = p_getMethod_0_.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            final Method method = amethod[i];
            if (method.getName().equals(p_getMethod_1_)) {
                final Class[] aclass = method.getParameterTypes();
                if (Reflector.matchesTypes(p_getMethod_2_, aclass)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    public static Method[] getMethods(final Class p_getMethods_0_, final String p_getMethods_1_) {
        final List list = new ArrayList();
        final Method[] amethod = p_getMethods_0_.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            final Method method = amethod[i];
            if (method.getName().equals(p_getMethods_1_)) {
                list.add(method);
            }
        }
        final Method[] amethod2 = list.toArray(new Method[list.size()]);
        return amethod2;
    }
}
