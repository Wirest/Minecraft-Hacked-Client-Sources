// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.Validate;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TypeLiteral<T> implements Typed<T>
{
    private static final TypeVariable<Class<TypeLiteral>> T;
    public final Type value;
    private final String toString;
    
    protected TypeLiteral() {
        this.value = Validate.notNull(TypeUtils.getTypeArguments(this.getClass(), TypeLiteral.class).get(TypeLiteral.T), "%s does not assign type parameter %s", this.getClass(), TypeUtils.toLongString(TypeLiteral.T));
        this.toString = String.format("%s<%s>", TypeLiteral.class.getSimpleName(), TypeUtils.toString(this.value));
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TypeLiteral)) {
            return false;
        }
        final TypeLiteral<?> other = (TypeLiteral<?>)obj;
        return TypeUtils.equals(this.value, other.value);
    }
    
    @Override
    public int hashCode() {
        return 0x250 | this.value.hashCode();
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
    
    @Override
    public Type getType() {
        return this.value;
    }
    
    static {
        T = TypeLiteral.class.getTypeParameters()[0];
    }
}
