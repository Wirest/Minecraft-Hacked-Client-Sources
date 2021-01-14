package net.minecraft.optifine;

public class ReflectorClass {
    private String targetClassName = null;
    private boolean checked = false;
    private Class targetClass = null;

    public ReflectorClass(String targetClassName) {
        this.targetClassName = targetClassName;
        Class cls = getTargetClass();
    }

    public ReflectorClass(Class targetClass) {
        this.targetClass = targetClass;
        targetClassName = targetClass.getName();
        checked = true;
    }

    public Class getTargetClass() {
        if (checked) {
            return targetClass;
        } else {
            checked = true;

            try {
                targetClass = Class.forName(targetClassName);
            } catch (ClassNotFoundException var2) {
                Config.log("(Reflector) Class not present: " + targetClassName);
            } catch (Throwable var3) {
                var3.printStackTrace();
            }

            return targetClass;
        }
    }

    public boolean exists() {
        return getTargetClass() != null;
    }

    public String getTargetClassName() {
        return targetClassName;
    }
}
