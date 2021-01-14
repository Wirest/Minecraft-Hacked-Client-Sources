package optifine;

import java.lang.reflect.Field;

public class FieldLocatorFixed implements IFieldLocator
{
    private Field field;

    public FieldLocatorFixed(Field p_i42_1_)
    {
        this.field = p_i42_1_;
    }

    public Field getField()
    {
        return this.field;
    }
}
