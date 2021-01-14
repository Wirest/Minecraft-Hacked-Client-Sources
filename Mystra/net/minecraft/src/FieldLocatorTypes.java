package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FieldLocatorTypes implements IFieldLocator
{
    private Field field = null;

    public FieldLocatorTypes(Class p_i47_1_, Class[] p_i47_2_, Class p_i47_3_, Class[] p_i47_4_, String p_i47_5_)
    {
        Field[] afield = p_i47_1_.getDeclaredFields();
        List<Class> list = new ArrayList();

        for (int i = 0; i < afield.length; ++i)
        {
            Field field = afield[i];
            list.add(field.getType());
        }

        List<Class> list1 = new ArrayList();
        list1.addAll(Arrays.<Class>asList(p_i47_2_));
        list1.add(p_i47_3_);
        list1.addAll(Arrays.<Class>asList(p_i47_4_));
        int l = Collections.indexOfSubList(list, list1);

        if (l < 0)
        {
            Config.warn("Field not found: " + p_i47_5_);
        }
        else
        {
            int j = Collections.indexOfSubList(list.subList(l + 1, list.size()), list1);

            if (j >= 0)
            {
                Config.warn("More than one match found for field: " + p_i47_5_);
            }
            else
            {
                int k = l + p_i47_2_.length;
                this.field = afield[k];
            }
        }
    }

    public Field getField()
    {
        return this.field;
    }
}
