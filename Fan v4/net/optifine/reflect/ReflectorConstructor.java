package net.optifine.reflect;

import java.lang.reflect.Constructor;
import net.minecraft.src.Config;

public class ReflectorConstructor
{
    private ReflectorClass reflectorClass;
    private Class[] parameterTypes;
    private boolean checked = false;
    private Constructor targetConstructor = null;

    public ReflectorConstructor(ReflectorClass reflectorClass, Class[] parameterTypes)
    {
        this.reflectorClass = reflectorClass;
        this.parameterTypes = parameterTypes;
        Constructor constructor = this.getTargetConstructor();
    }

    public Constructor getTargetConstructor()
    {
        if (this.checked)
        {
            return this.targetConstructor;
        }
        else
        {
            this.checked = true;
            Class oclass = this.reflectorClass.getTargetClass();

            if (oclass == null)
            {
                return null;
            }
            else
            {
                try
                {
                    this.targetConstructor = findConstructor(oclass, this.parameterTypes);

                    if (this.targetConstructor == null)
                    {
                        Config.dbg("(Reflector) Constructor not present: " + oclass.getName() + ", params: " + Config.arrayToString(this.parameterTypes));
                    }

                    if (this.targetConstructor != null)
                    {
                        this.targetConstructor.setAccessible(true);
                    }
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }

                return this.targetConstructor;
            }
        }
    }

    private static Constructor findConstructor(Class cls, Class[] paramTypes)
    {
        Constructor[] aconstructor = cls.getDeclaredConstructors();

        for (Constructor constructor : aconstructor) {
            Class[] aclass = constructor.getParameterTypes();

            if (Reflector.matchesTypes(paramTypes, aclass)) {
                return constructor;
            }
        }

        return null;
    }

    public boolean exists()
    {
        return this.checked ? this.targetConstructor != null : this.getTargetConstructor() != null;
    }

    public void deactivate()
    {
        this.checked = true;
        this.targetConstructor = null;
    }
}
