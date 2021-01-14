package net.optifine.reflect;

import net.minecraft.src.Config;

public class ReflectorClass
{
    private String targetClassName;
    private boolean checked;
    private Class targetClass;

    public ReflectorClass(String targetClassName)
    {
        this(targetClassName, false);
    }

    public ReflectorClass(String targetClassName, boolean lazyResolve)
    {
        this.targetClassName = null;
        this.checked = false;
        this.targetClass = null;
        this.targetClassName = targetClassName;

        if (!lazyResolve)
        {
            Class oclass = this.getTargetClass();
        }
    }

    public ReflectorClass(Class targetClass)
    {
        this.targetClassName = null;
        this.checked = false;
        this.targetClass = null;
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
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
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

    public boolean isInstance(Object obj)
    {
        return this.getTargetClass() == null ? false : this.getTargetClass().isInstance(obj);
    }

    public ReflectorField makeField(String name)
    {
        return new ReflectorField(this, name);
    }

    public ReflectorMethod makeMethod(String name)
    {
        return new ReflectorMethod(this, name);
    }

    public ReflectorMethod makeMethod(String name, Class[] paramTypes)
    {
        return new ReflectorMethod(this, name, paramTypes);
    }

    public ReflectorMethod makeMethod(String name, Class[] paramTypes, boolean lazyResolve)
    {
        return new ReflectorMethod(this, name, paramTypes, lazyResolve);
    }
}
