package net.minecraft.src;

import java.lang.reflect.Method;

public class ReflectorMethod
{
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName)
    {
        this(reflectorClass, targetMethodName, (Class[])null);
    }

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName, Class[] targetMethodParameterTypes)
    {
        this.reflectorClass = null;
        this.targetMethodName = null;
        this.targetMethodParameterTypes = null;
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = reflectorClass;
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;
        Method m = this.getTargetMethod();
    }

    public Method getTargetMethod()
    {
        if (this.checked)
        {
            return this.targetMethod;
        }
        else
        {
            this.checked = true;
            Class cls = this.reflectorClass.getTargetClass();

            if (cls == null)
            {
                return null;
            }
            else
            {
                Method[] ms = cls.getDeclaredMethods();
                int i = 0;
                Method m;

                while (true)
                {
                    if (i >= ms.length)
                    {
                        Config.log("(Reflector) Method not pesent: " + cls.getName() + "." + this.targetMethodName);
                        return null;
                    }

                    m = ms[i];

                    if (m.getName().equals(this.targetMethodName))
                    {
                        if (this.targetMethodParameterTypes == null)
                        {
                            break;
                        }

                        Class[] types = m.getParameterTypes();

                        if (Reflector.matchesTypes(this.targetMethodParameterTypes, types))
                        {
                            break;
                        }
                    }

                    ++i;
                }

                this.targetMethod = m;

                if (!this.targetMethod.isAccessible())
                {
                    this.targetMethod.setAccessible(true);
                }

                return this.targetMethod;
            }
        }
    }

    public boolean exists()
    {
        return this.checked ? this.targetMethod != null : this.getTargetMethod() != null;
    }

    public Class getReturnType()
    {
        Method tm = this.getTargetMethod();
        return tm == null ? null : tm.getReturnType();
    }

    public void deactivate()
    {
        this.checked = true;
        this.targetMethod = null;
    }
}
