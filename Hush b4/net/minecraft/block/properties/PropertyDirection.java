// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.properties;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import net.minecraft.util.EnumFacing;

public class PropertyDirection extends PropertyEnum<EnumFacing>
{
    protected PropertyDirection(final String name, final Collection<EnumFacing> values) {
        super(name, EnumFacing.class, values);
    }
    
    public static PropertyDirection create(final String name) {
        return create(name, Predicates.alwaysTrue());
    }
    
    public static PropertyDirection create(final String name, final Predicate<EnumFacing> filter) {
        return create(name, Collections2.filter(Lists.newArrayList(EnumFacing.values()), filter));
    }
    
    public static PropertyDirection create(final String name, final Collection<EnumFacing> values) {
        return new PropertyDirection(name, values);
    }
}
