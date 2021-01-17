// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.Field;

public class FieldLocatorName implements IFieldLocator
{
    private ReflectorClass reflectorClass;
    private String targetFieldName;
    
    public FieldLocatorName(final ReflectorClass p_i38_1_, final String p_i38_2_) {
        this.reflectorClass = null;
        this.targetFieldName = null;
        this.reflectorClass = p_i38_1_;
        this.targetFieldName = p_i38_2_;
    }
    
    @Override
    public Field getField() {
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            final Field field = oclass.getDeclaredField(this.targetFieldName);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException var3) {
            Config.log("(Reflector) Field not present: " + oclass.getName() + "." + this.targetFieldName);
            return null;
        }
        catch (SecurityException securityexception) {
            securityexception.printStackTrace();
            return null;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
