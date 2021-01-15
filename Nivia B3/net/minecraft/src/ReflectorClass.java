package net.minecraft.src;

public class ReflectorClass
{
    private String targetClassName = null;
    private boolean checked = false;
    private Class targetClass = null;

    public ReflectorClass(String targetClassName)
    {
        this.targetClassName = targetClassName;
        Class cls = this.getTargetClass();
    }

    public ReflectorClass(Class targetClass)
    {
        this.targetClass = targetClass;
        this.targetClassName = targetClass.getName();
        this.checked = true;
    }

    public Class getTargetClass()
    {
        if (this.checked)
        {
            return this.targetClass;
        }
        else
        {
            this.checked = true;

            try
            {
                this.targetClass = Class.forName(this.targetClassName);
            }
            catch (ClassNotFoundException var2)
            {
                Config.log("(Reflector) Class not present: " + this.targetClassName);
            }
            catch (Throwable var3)
            {
                var3.printStackTrace();
            }

            return this.targetClass;
        }
    }

    public boolean exists()
    {
        return this.getTargetClass() != null;
    }

    public String getTargetClassName()
    {
        return this.targetClassName;
    }
}
