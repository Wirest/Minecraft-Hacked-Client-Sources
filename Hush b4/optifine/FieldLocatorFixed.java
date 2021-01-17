// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.Field;

public class FieldLocatorFixed implements IFieldLocator
{
    private Field field;
    
    public FieldLocatorFixed(final Field p_i37_1_) {
        this.field = p_i37_1_;
    }
    
    @Override
    public Field getField() {
        return this.field;
    }
}
