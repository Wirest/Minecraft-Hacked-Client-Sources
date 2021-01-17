// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.properties;

import java.util.Collection;
import com.google.common.collect.ImmutableSet;

public class PropertyBool extends PropertyHelper<Boolean>
{
    private final ImmutableSet<Boolean> allowedValues;
    
    protected PropertyBool(final String name) {
        super(name, Boolean.class);
        this.allowedValues = ImmutableSet.of(true, false);
    }
    
    @Override
    public Collection<Boolean> getAllowedValues() {
        return this.allowedValues;
    }
    
    public static PropertyBool create(final String name) {
        return new PropertyBool(name);
    }
    
    @Override
    public String getName(final Boolean value) {
        return value.toString();
    }
}
