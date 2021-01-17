// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Arrays;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.google.common.annotations.Beta;
import java.lang.reflect.GenericDeclaration;

@Beta
public abstract class Invokable<T, R> extends Element implements GenericDeclaration
{
     <M extends java.lang.reflect.AccessibleObject> Invokable(final M member) {
        super(member);
    }
    
    public static Invokable<?, Object> from(final Method method) {
        return new MethodInvokable<Object>(method);
    }
    
    public static <T> Invokable<T, T> from(final Constructor<T> constructor) {
        return (Invokable<T, T>)new ConstructorInvokable((Constructor<?>)constructor);
    }
    
    public abstract boolean isOverridable();
    
    public abstract boolean isVarArgs();
    
    public final R invoke(@Nullable final T receiver, final Object... args) throws InvocationTargetException, IllegalAccessException {
        return (R)this.invokeInternal(receiver, Preconditions.checkNotNull(args));
    }
    
    public final TypeToken<? extends R> getReturnType() {
        return (TypeToken<? extends R>)TypeToken.of(this.getGenericReturnType());
    }
    
    public final ImmutableList<Parameter> getParameters() {
        final Type[] parameterTypes = this.getGenericParameterTypes();
        final Annotation[][] annotations = this.getParameterAnnotations();
        final ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
        for (int i = 0; i < parameterTypes.length; ++i) {
            builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i]));
        }
        return builder.build();
    }
    
    public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
        final ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
        for (final Type type : this.getGenericExceptionTypes()) {
            final TypeToken<? extends Throwable> exceptionType = (TypeToken<? extends Throwable>)TypeToken.of(type);
            builder.add(exceptionType);
        }
        return builder.build();
    }
    
    public final <R1 extends R> Invokable<T, R1> returning(final Class<R1> returnType) {
        return this.returning((TypeToken<R1>)TypeToken.of((Class<R1>)returnType));
    }
    
    public final <R1 extends R> Invokable<T, R1> returning(final TypeToken<R1> returnType) {
        if (!returnType.isAssignableFrom(this.getReturnType())) {
            throw new IllegalArgumentException("Invokable is known to return " + this.getReturnType() + ", not " + returnType);
        }
        final Invokable<T, R1> specialized = (Invokable<T, R1>)this;
        return specialized;
    }
    
    @Override
    public final Class<? super T> getDeclaringClass() {
        return (Class<? super T>)super.getDeclaringClass();
    }
    
    @Override
    public TypeToken<T> getOwnerType() {
        return TypeToken.of(this.getDeclaringClass());
    }
    
    abstract Object invokeInternal(@Nullable final Object p0, final Object[] p1) throws InvocationTargetException, IllegalAccessException;
    
    abstract Type[] getGenericParameterTypes();
    
    abstract Type[] getGenericExceptionTypes();
    
    abstract Annotation[][] getParameterAnnotations();
    
    abstract Type getGenericReturnType();
    
    static class MethodInvokable<T> extends Invokable<T, Object>
    {
        final Method method;
        
        MethodInvokable(final Method method) {
            super(method);
            this.method = method;
        }
        
        @Override
        final Object invokeInternal(@Nullable final Object receiver, final Object[] args) throws InvocationTargetException, IllegalAccessException {
            return this.method.invoke(receiver, args);
        }
        
        @Override
        Type getGenericReturnType() {
            return this.method.getGenericReturnType();
        }
        
        @Override
        Type[] getGenericParameterTypes() {
            return this.method.getGenericParameterTypes();
        }
        
        @Override
        Type[] getGenericExceptionTypes() {
            return this.method.getGenericExceptionTypes();
        }
        
        @Override
        final Annotation[][] getParameterAnnotations() {
            return this.method.getParameterAnnotations();
        }
        
        @Override
        public final TypeVariable<?>[] getTypeParameters() {
            return this.method.getTypeParameters();
        }
        
        @Override
        public final boolean isOverridable() {
            return !this.isFinal() && !this.isPrivate() && !this.isStatic() && !Modifier.isFinal(this.getDeclaringClass().getModifiers());
        }
        
        @Override
        public final boolean isVarArgs() {
            return this.method.isVarArgs();
        }
    }
    
    static class ConstructorInvokable<T> extends Invokable<T, T>
    {
        final Constructor<?> constructor;
        
        ConstructorInvokable(final Constructor<?> constructor) {
            super(constructor);
            this.constructor = constructor;
        }
        
        @Override
        final Object invokeInternal(@Nullable final Object receiver, final Object[] args) throws InvocationTargetException, IllegalAccessException {
            try {
                return this.constructor.newInstance(args);
            }
            catch (InstantiationException e) {
                throw new RuntimeException(this.constructor + " failed.", e);
            }
        }
        
        @Override
        Type getGenericReturnType() {
            final Class<?> declaringClass = this.getDeclaringClass();
            final TypeVariable<?>[] typeParams = declaringClass.getTypeParameters();
            if (typeParams.length > 0) {
                return Types.newParameterizedType(declaringClass, (Type[])typeParams);
            }
            return declaringClass;
        }
        
        @Override
        Type[] getGenericParameterTypes() {
            final Type[] types = this.constructor.getGenericParameterTypes();
            if (types.length > 0 && this.mayNeedHiddenThis()) {
                final Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
                if (types.length == rawParamTypes.length && rawParamTypes[0] == this.getDeclaringClass().getEnclosingClass()) {
                    return Arrays.copyOfRange(types, 1, types.length);
                }
            }
            return types;
        }
        
        @Override
        Type[] getGenericExceptionTypes() {
            return this.constructor.getGenericExceptionTypes();
        }
        
        @Override
        final Annotation[][] getParameterAnnotations() {
            return this.constructor.getParameterAnnotations();
        }
        
        @Override
        public final TypeVariable<?>[] getTypeParameters() {
            final TypeVariable<?>[] declaredByClass = this.getDeclaringClass().getTypeParameters();
            final TypeVariable<?>[] declaredByConstructor = this.constructor.getTypeParameters();
            final TypeVariable<?>[] result = (TypeVariable<?>[])new TypeVariable[declaredByClass.length + declaredByConstructor.length];
            System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
            System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
            return result;
        }
        
        @Override
        public final boolean isOverridable() {
            return false;
        }
        
        @Override
        public final boolean isVarArgs() {
            return this.constructor.isVarArgs();
        }
        
        private boolean mayNeedHiddenThis() {
            final Class<?> declaringClass = this.constructor.getDeclaringClass();
            if (declaringClass.getEnclosingConstructor() != null) {
                return true;
            }
            final Method enclosingMethod = declaringClass.getEnclosingMethod();
            if (enclosingMethod != null) {
                return !Modifier.isStatic(enclosingMethod.getModifiers());
            }
            return declaringClass.getEnclosingClass() != null && !Modifier.isStatic(declaringClass.getModifiers());
        }
    }
}
