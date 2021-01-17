// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;
import com.google.common.collect.Sets;
import java.lang.reflect.Type;
import java.util.Set;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
abstract class TypeVisitor
{
    private final Set<Type> visited;
    
    TypeVisitor() {
        this.visited = (Set<Type>)Sets.newHashSet();
    }
    
    public final void visit(final Type... types) {
        for (final Type type : types) {
            if (type != null) {
                if (this.visited.add(type)) {
                    boolean succeeded = false;
                    try {
                        if (type instanceof TypeVariable) {
                            this.visitTypeVariable((TypeVariable<?>)type);
                        }
                        else if (type instanceof WildcardType) {
                            this.visitWildcardType((WildcardType)type);
                        }
                        else if (type instanceof ParameterizedType) {
                            this.visitParameterizedType((ParameterizedType)type);
                        }
                        else if (type instanceof Class) {
                            this.visitClass((Class<?>)type);
                        }
                        else {
                            if (!(type instanceof GenericArrayType)) {
                                throw new AssertionError((Object)("Unknown type: " + type));
                            }
                            this.visitGenericArrayType((GenericArrayType)type);
                        }
                        succeeded = true;
                    }
                    finally {
                        if (!succeeded) {
                            this.visited.remove(type);
                        }
                    }
                }
            }
        }
    }
    
    void visitClass(final Class<?> t) {
    }
    
    void visitGenericArrayType(final GenericArrayType t) {
    }
    
    void visitParameterizedType(final ParameterizedType t) {
    }
    
    void visitTypeVariable(final TypeVariable<?> t) {
    }
    
    void visitWildcardType(final WildcardType t) {
    }
}
