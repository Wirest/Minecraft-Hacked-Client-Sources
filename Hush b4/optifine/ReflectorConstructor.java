// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.Constructor;

public class ReflectorConstructor
{
    private ReflectorClass reflectorClass;
    private Class[] parameterTypes;
    private boolean checked;
    private Constructor targetConstructor;
    
    public ReflectorConstructor(final ReflectorClass p_i84_1_, final Class[] p_i84_2_) {
        this.reflectorClass = null;
        this.parameterTypes = null;
        this.checked = false;
        this.targetConstructor = null;
        this.reflectorClass = p_i84_1_;
        this.parameterTypes = p_i84_2_;
        final Constructor constructor = this.getTargetConstructor();
    }
    
    public Constructor getTargetConstructor() {
        if (this.checked) {
            return this.targetConstructor;
        }
        this.checked = true;
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            this.targetConstructor = findConstructor(oclass, this.parameterTypes);
            if (this.targetConstructor == null) {
                Config.dbg("(Reflector) Constructor not present: " + oclass.getName() + ", params: " + Config.arrayToString(this.parameterTypes));
            }
            if (this.targetConstructor != null) {
                this.targetConstructor.setAccessible(true);
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return this.targetConstructor;
    }
    
    private static Constructor findConstructor(final Class p_findConstructor_0_, final Class[] p_findConstructor_1_) {
        final Constructor[] aconstructor = p_findConstructor_0_.getDeclaredConstructors();
        for (int i = 0; i < aconstructor.length; ++i) {
            final Constructor constructor = aconstructor[i];
            final Class[] aclass = constructor.getParameterTypes();
            if (Reflector.matchesTypes(p_findConstructor_1_, aclass)) {
                return constructor;
            }
        }
        return null;
    }
    
    public boolean exists() {
        return this.checked ? (this.targetConstructor != null) : (this.getTargetConstructor() != null);
    }
    
    public void deactivate() {
        this.checked = true;
        this.targetConstructor = null;
    }
}
