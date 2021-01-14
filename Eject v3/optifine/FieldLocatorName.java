package optifine;

import java.lang.reflect.Field;

public class FieldLocatorName
        implements IFieldLocator {
    private ReflectorClass reflectorClass = null;
    private String targetFieldName = null;

    public FieldLocatorName(ReflectorClass paramReflectorClass, String paramString) {
        this.reflectorClass = paramReflectorClass;
        this.targetFieldName = paramString;
    }

    public Field getField() {
        Class localClass = this.reflectorClass.getTargetClass();
        if (localClass == null) {
            return null;
        }
        try {
            Field localField = localClass.getDeclaredField(this.targetFieldName);
            localField.setAccessible(true);
            return localField;
        } catch (NoSuchFieldException localNoSuchFieldException) {
            Config.log("(Reflector) Field not present: " + localClass.getName() + "." + this.targetFieldName);
            return null;
        } catch (SecurityException localSecurityException) {
            localSecurityException.printStackTrace();
            return null;
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return null;
    }
}




