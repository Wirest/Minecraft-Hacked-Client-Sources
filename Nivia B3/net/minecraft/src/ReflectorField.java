package net.minecraft.src;

import java.lang.reflect.Field;

public class ReflectorField
{
    private ReflectorClass reflectorClass = null;
    private String targetFieldName = null;
    private boolean checked = false;
    private Field targetField = null;

    public ReflectorField(ReflectorClass reflectorClass, String targetFieldName)
    {
        this.reflectorClass = reflectorClass;
        this.targetFieldName = targetFieldName;
        Field f = this.getTargetField();
    }

    public Field getTargetField()
    {
        if (this.checked)
        {
            return this.targetField;
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
                try
                {
                    this.targetField = cls.getDeclaredField(this.targetFieldName);

                    if (!this.targetField.isAccessible())
                    {
                        this.targetField.setAccessible(true);
                    }
                }
                catch (SecurityException var3)
                {
                    var3.printStackTrace();
                }
                catch (NoSuchFieldException var4)
                {
                    Config.log("(Reflector) Field not present: " + cls.getName() + "." + this.targetFieldName);
                }

                return this.targetField;
            }
        }
    }

    public Object getValue()
    {
        return Reflector.getFieldValue((Object)null, this);
    }

    public void setValue(Object value)
    {
        Reflector.setFieldValue((Object)null, this, value);
    }

    public boolean exists()
    {
        return this.checked ? this.targetField != null : this.getTargetField() != null;
    }
}
