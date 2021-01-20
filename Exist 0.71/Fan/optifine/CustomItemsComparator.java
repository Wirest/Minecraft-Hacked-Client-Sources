package optifine;

import java.util.Comparator;

public class CustomItemsComparator implements Comparator
{
    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
        CustomItemProperties customitemproperties = (CustomItemProperties)p_compare_1_;
        CustomItemProperties customitemproperties1 = (CustomItemProperties)p_compare_2_;
        return customitemproperties.weight != customitemproperties1.weight ? customitemproperties1.weight - customitemproperties.weight : (!Config.equals(customitemproperties.basePath, customitemproperties1.basePath) ? customitemproperties.basePath.compareTo(customitemproperties1.basePath) : customitemproperties.name.compareTo(customitemproperties1.name));
    }
}
