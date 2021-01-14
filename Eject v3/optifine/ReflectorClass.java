package optifine;

public class ReflectorClass {
    private String targetClassName = null;
    private boolean checked = false;
    private Class targetClass = null;

    public ReflectorClass(String paramString) {
        this(paramString, false);
    }

    public ReflectorClass(String paramString, boolean paramBoolean) {
        this.targetClassName = paramString;
        if (!paramBoolean) {
            Class localClass = getTargetClass();
        }
    }

    public ReflectorClass(Class paramClass) {
        this.targetClass = paramClass;
        this.targetClassName = paramClass.getName();
        this.checked = true;
    }

    public Class getTargetClass() {
        if (this.checked) {
            return this.targetClass;
        }
        this.checked = true;
        try {
            this.targetClass = Class.forName(this.targetClassName);
        } catch (ClassNotFoundException localClassNotFoundException) {
            Config.log("(Reflector) Class not present: " + this.targetClassName);
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return this.targetClass;
    }

    public boolean exists() {
        return getTargetClass() != null;
    }

    public String getTargetClassName() {
        return this.targetClassName;
    }

    public boolean isInstance(Object paramObject) {
        return getTargetClass() == null ? false : getTargetClass().isInstance(paramObject);
    }

    public ReflectorField makeField(String paramString) {
        return new ReflectorField(this, paramString);
    }

    public ReflectorMethod makeMethod(String paramString) {
        return new ReflectorMethod(this, paramString);
    }

    public ReflectorMethod makeMethod(String paramString, Class[] paramArrayOfClass) {
        return new ReflectorMethod(this, paramString, paramArrayOfClass);
    }

    public ReflectorMethod makeMethod(String paramString, Class[] paramArrayOfClass, boolean paramBoolean) {
        return new ReflectorMethod(this, paramString, paramArrayOfClass, paramBoolean);
    }
}




