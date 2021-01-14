package optifine;

import java.lang.reflect.Field;

public class FieldLocatorType
        implements IFieldLocator {
    private ReflectorClass reflectorClass = null;
    private Class targetFieldType = null;
    private int targetFieldIndex;

    public FieldLocatorType(ReflectorClass paramReflectorClass, Class paramClass) {
        this(paramReflectorClass, paramClass, 0);
    }

    public FieldLocatorType(ReflectorClass paramReflectorClass, Class paramClass, int paramInt) {
        this.reflectorClass = paramReflectorClass;
        this.targetFieldType = paramClass;
        this.targetFieldIndex = paramInt;
    }

    public Field getField() {
        Class localClass = this.reflectorClass.getTargetClass();
        if (localClass == null) {
            return null;
        }
        try {
            Field[] arrayOfField = localClass.getDeclaredFields();
            int i = 0;
            for (int j = 0; j < arrayOfField.length; j++) {
                Field localField = arrayOfField[j];
                if (localField.getType() == this.targetFieldType) {
                    if (i == this.targetFieldIndex) {
                        localField.setAccessible(true);
                        return localField;
                    }
                    i++;
                }
            }
            Config.log("(Reflector) Field not present: " + localClass.getName() + ".(type: " + this.targetFieldType + ", index: " + this.targetFieldIndex + ")");
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




