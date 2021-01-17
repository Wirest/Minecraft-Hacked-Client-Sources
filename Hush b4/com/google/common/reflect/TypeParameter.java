// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import com.google.common.base.Preconditions;
import java.lang.reflect.TypeVariable;
import com.google.common.annotations.Beta;

@Beta
public abstract class TypeParameter<T> extends TypeCapture<T>
{
    final TypeVariable<?> typeVariable;
    
    protected TypeParameter() {
        final Type type = this.capture();
        Preconditions.checkArgument(type instanceof TypeVariable, "%s should be a type variable.", type);
        this.typeVariable = (TypeVariable<?>)type;
    }
    
    @Override
    public final int hashCode() {
        return this.typeVariable.hashCode();
    }
    
    @Override
    public final boolean equals(@Nullable final Object o) {
        if (o instanceof TypeParameter) {
            final TypeParameter<?> that = (TypeParameter<?>)o;
            return this.typeVariable.equals(that.typeVariable);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.typeVariable.toString();
    }
}
