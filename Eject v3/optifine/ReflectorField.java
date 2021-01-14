package optifine;

import java.lang.reflect.Field;

public class ReflectorField {
    private IFieldLocator fieldLocator = null;
    private boolean checked = false;
    private Field targetField = null;

    public ReflectorField(ReflectorClass paramReflectorClass, String paramString) {
        this(new FieldLocatorName(paramReflectorClass, paramString));
    }

    public ReflectorField(ReflectorClass paramReflectorClass, Class paramClass) {
        this(paramReflectorClass, paramClass, 0);
    }

    public ReflectorField(ReflectorClass paramReflectorClass, Class paramClass, int paramInt) {
        this(new FieldLocatorType(paramReflectorClass, paramClass, paramInt));
    }

    public ReflectorField(Field paramField) {
        this(new FieldLocatorFixed(paramField));
    }

    public ReflectorField(IFieldLocator paramIFieldLocator) {
        this.fieldLocator = paramIFieldLocator;
        getTargetField();
    }

    public Field getTargetField() {
        if (this.checked) {
            return this.targetField;
        }
        this.checked = true;
        this.targetField = this.fieldLocator.getField();
        if (this.targetField != null) {
            this.targetField.setAccessible(true);
        }
        return this.targetField;
    }

    public Object getValue() {
        return Reflector.getFieldValue(null, this);
    }

    public void setValue(Object paramObject) {
        Reflector.setFieldValue(null, this, paramObject);
    }

    public void setValue(Object paramObject1, Object paramObject2) {
        Reflector.setFieldValue(paramObject1, this, paramObject2);
    }

    public boolean exists() {
        return getTargetField() != null;
    }
}




