package net.minecraft.block.state;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public abstract class BlockStateBase implements IBlockState
{
    private static final Joiner COMMA_JOINER = Joiner.on(',');
    private static final Function MAP_ENTRY_TO_STRING = new Function()
    {
        public String apply0(Entry entry)
        {
            if (entry == null)
            {
                return "<NULL>";
            }
            else
            {
                IProperty var2 = (IProperty)entry.getKey();
                return var2.getName() + "=" + var2.getName((Comparable)entry.getValue());
            }
        }
        @Override
		public Object apply(Object p_apply_1_)
        {
            return this.apply0((Entry)p_apply_1_);
        }
    };

    /**
     * Create a version of this BlockState with the given property cycled to the next value in order. If the property
     * was at the highest possible value, it is set to the lowest one instead.
     */
    @Override
	public IBlockState cycleProperty(IProperty property)
    {
        return this.withProperty(property, (Comparable)cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));
    }

    /**
     * Helper method for cycleProperty.
     *  
     * @param values The collection of values
     * @param currentValue The current value
     */
    protected static Object cyclePropertyValue(Collection values, Object currentValue)
    {
        Iterator var2 = values.iterator();

        do
        {
            if (!var2.hasNext())
            {
                return var2.next();
            }
        }
        while (!var2.next().equals(currentValue));

        if (var2.hasNext())
        {
            return var2.next();
        }
        else
        {
            return values.iterator().next();
        }
    }

    @Override
	public String toString()
    {
        StringBuilder var1 = new StringBuilder();
        var1.append(Block.blockRegistry.getNameForObject(this.getBlock()));

        if (!this.getProperties().isEmpty())
        {
            var1.append("[");
            COMMA_JOINER.appendTo(var1, Iterables.transform(this.getProperties().entrySet(), MAP_ENTRY_TO_STRING));
            var1.append("]");
        }

        return var1.toString();
    }
}
