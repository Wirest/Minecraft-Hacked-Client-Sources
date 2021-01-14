package optifine;

public class ReflectorFields {
    private ReflectorClass reflectorClass;
    private Class fieldType;
    private int fieldCount;
    private ReflectorField[] reflectorFields;

    public ReflectorFields(ReflectorClass paramReflectorClass, Class paramClass, int paramInt) {
        this.reflectorClass = paramReflectorClass;
        this.fieldType = paramClass;
        if ((paramReflectorClass.exists()) && (paramClass != null)) {
            this.reflectorFields = new ReflectorField[paramInt];
            for (int i = 0; i < this.reflectorFields.length; i++) {
                this.reflectorFields[i] = new ReflectorField(paramReflectorClass, paramClass, i);
            }
        }
    }

    public ReflectorClass getReflectorClass() {
        return this.reflectorClass;
    }

    public Class getFieldType() {
        return this.fieldType;
    }

    public int getFieldCount() {
        return this.fieldCount;
    }

    public ReflectorField getReflectorField(int paramInt) {
        return (paramInt >= 0) && (paramInt < this.reflectorFields.length) ? this.reflectorFields[paramInt] : null;
    }
}




