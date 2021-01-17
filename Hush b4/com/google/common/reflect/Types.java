// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Iterator;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.lang.reflect.Array;
import com.google.common.collect.Iterables;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.lang.reflect.GenericArrayType;
import java.util.concurrent.atomic.AtomicReference;
import com.google.common.annotations.VisibleForTesting;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.lang.reflect.WildcardType;
import com.google.common.base.Joiner;
import java.lang.reflect.Type;
import com.google.common.base.Function;

final class Types
{
    private static final Function<Type, String> TYPE_TO_STRING;
    private static final Joiner COMMA_JOINER;
    
    static Type newArrayType(final Type componentType) {
        if (!(componentType instanceof WildcardType)) {
            return JavaVersion.CURRENT.newArrayType(componentType);
        }
        final WildcardType wildcard = (WildcardType)componentType;
        final Type[] lowerBounds = wildcard.getLowerBounds();
        Preconditions.checkArgument(lowerBounds.length <= 1, (Object)"Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        final Type[] upperBounds = wildcard.getUpperBounds();
        Preconditions.checkArgument(upperBounds.length == 1, (Object)"Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(upperBounds[0]));
    }
    
    static ParameterizedType newParameterizedTypeWithOwner(@Nullable final Type ownerType, final Class<?> rawType, final Type... arguments) {
        if (ownerType == null) {
            return newParameterizedType(rawType, arguments);
        }
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(rawType.getEnclosingClass() != null, "Owner type for unenclosed %s", rawType);
        return new ParameterizedTypeImpl(ownerType, rawType, arguments);
    }
    
    static ParameterizedType newParameterizedType(final Class<?> rawType, final Type... arguments) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(rawType), rawType, arguments);
    }
    
    static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(final D declaration, final String name, final Type... bounds) {
        return new TypeVariableImpl<D>(declaration, name, (bounds.length == 0) ? new Type[] { Object.class } : bounds);
    }
    
    @VisibleForTesting
    static WildcardType subtypeOf(final Type upperBound) {
        return new WildcardTypeImpl(new Type[0], new Type[] { upperBound });
    }
    
    @VisibleForTesting
    static WildcardType supertypeOf(final Type lowerBound) {
        return new WildcardTypeImpl(new Type[] { lowerBound }, new Type[] { Object.class });
    }
    
    static String toString(final Type type) {
        return (type instanceof Class) ? ((Class)type).getName() : type.toString();
    }
    
    @Nullable
    static Type getComponentType(final Type type) {
        Preconditions.checkNotNull(type);
        final AtomicReference<Type> result = new AtomicReference<Type>();
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> t) {
                result.set(subtypeOfComponentType(t.getBounds()));
            }
            
            @Override
            void visitWildcardType(final WildcardType t) {
                result.set(subtypeOfComponentType(t.getUpperBounds()));
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType t) {
                result.set(t.getGenericComponentType());
            }
            
            @Override
            void visitClass(final Class<?> t) {
                result.set(t.getComponentType());
            }
        }.visit(type);
        return result.get();
    }
    
    @Nullable
    private static Type subtypeOfComponentType(final Type[] bounds) {
        for (final Type bound : bounds) {
            final Type componentType = getComponentType(bound);
            if (componentType != null) {
                if (componentType instanceof Class) {
                    final Class<?> componentClass = (Class<?>)componentType;
                    if (componentClass.isPrimitive()) {
                        return componentClass;
                    }
                }
                return subtypeOf(componentType);
            }
        }
        return null;
    }
    
    private static Type[] toArray(final Collection<Type> types) {
        return types.toArray(new Type[types.size()]);
    }
    
    private static Iterable<Type> filterUpperBounds(final Iterable<Type> bounds) {
        return Iterables.filter(bounds, (Predicate<? super Type>)Predicates.not((Predicate<? super T>)Predicates.equalTo((T)Object.class)));
    }
    
    private static void disallowPrimitiveType(final Type[] types, final String usedAs) {
        for (final Type type : types) {
            if (type instanceof Class) {
                final Class<?> cls = (Class<?>)type;
                Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
            }
        }
    }
    
    static Class<?> getArrayClass(final Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }
    
    private Types() {
    }
    
    static {
        TYPE_TO_STRING = new Function<Type, String>() {
            @Override
            public String apply(final Type from) {
                return Types.toString(from);
            }
        };
        COMMA_JOINER = Joiner.on(", ").useForNull("null");
    }
    
    private enum ClassOwnership
    {
        OWNED_BY_ENCLOSING_CLASS {
            @Nullable
            @Override
            Class<?> getOwnerType(final Class<?> rawType) {
                return rawType.getEnclosingClass();
            }
        }, 
        LOCAL_CLASS_HAS_NO_OWNER {
            @Nullable
            @Override
            Class<?> getOwnerType(final Class<?> rawType) {
                if (rawType.isLocalClass()) {
                    return null;
                }
                return rawType.getEnclosingClass();
            }
        };
        
        static final ClassOwnership JVM_BEHAVIOR;
        
        @Nullable
        abstract Class<?> getOwnerType(final Class<?> p0);
        
        private static ClassOwnership detectJvmBehavior() {
            final Class<?> subclass = new LocalClass<String>() {}.getClass();
            final ParameterizedType parameterizedType = (ParameterizedType)subclass.getGenericSuperclass();
            for (final ClassOwnership behavior : values()) {
                class LocalClass<T>
                {
                }
                if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
                    return behavior;
                }
            }
            throw new AssertionError();
        }
        
        static {
            JVM_BEHAVIOR = detectJvmBehavior();
        }
    }
    
    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable
    {
        private final Type componentType;
        private static final long serialVersionUID = 0L;
        
        GenericArrayTypeImpl(final Type componentType) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(componentType);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public String toString() {
            return Types.toString(this.componentType) + "[]";
        }
        
        @Override
        public int hashCode() {
            return this.componentType.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof GenericArrayType) {
                final GenericArrayType that = (GenericArrayType)obj;
                return Objects.equal(this.getGenericComponentType(), that.getGenericComponentType());
            }
            return false;
        }
    }
    
    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable
    {
        private final Type ownerType;
        private final ImmutableList<Type> argumentsList;
        private final Class<?> rawType;
        private static final long serialVersionUID = 0L;
        
        ParameterizedTypeImpl(@Nullable final Type ownerType, final Class<?> rawType, final Type[] typeArguments) {
            Preconditions.checkNotNull(rawType);
            Preconditions.checkArgument(typeArguments.length == rawType.getTypeParameters().length);
            disallowPrimitiveType(typeArguments, "type parameter");
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(typeArguments);
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return toArray(this.argumentsList);
        }
        
        @Override
        public Type getRawType() {
            return this.rawType;
        }
        
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            if (this.ownerType != null) {
                builder.append(Types.toString(this.ownerType)).append('.');
            }
            builder.append(this.rawType.getName()).append('<').append(Types.COMMA_JOINER.join(Iterables.transform((Iterable<Type>)this.argumentsList, (Function<? super Type, ?>)Types.TYPE_TO_STRING))).append('>');
            return builder.toString();
        }
        
        @Override
        public int hashCode() {
            return ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode() ^ this.rawType.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            final ParameterizedType that = (ParameterizedType)other;
            return this.getRawType().equals(that.getRawType()) && Objects.equal(this.getOwnerType(), that.getOwnerType()) && Arrays.equals(this.getActualTypeArguments(), that.getActualTypeArguments());
        }
    }
    
    private static final class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D>
    {
        private final D genericDeclaration;
        private final String name;
        private final ImmutableList<Type> bounds;
        
        TypeVariableImpl(final D genericDeclaration, final String name, final Type[] bounds) {
            disallowPrimitiveType(bounds, "bound for type variable");
            this.genericDeclaration = Preconditions.checkNotNull(genericDeclaration);
            this.name = Preconditions.checkNotNull(name);
            this.bounds = ImmutableList.copyOf(bounds);
        }
        
        @Override
        public Type[] getBounds() {
            return toArray(this.bounds);
        }
        
        @Override
        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
                if (obj instanceof TypeVariableImpl) {
                    final TypeVariableImpl<?> that = (TypeVariableImpl<?>)obj;
                    return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()) && this.bounds.equals(that.bounds);
                }
                return false;
            }
            else {
                if (obj instanceof TypeVariable) {
                    final TypeVariable<?> that2 = (TypeVariable<?>)obj;
                    return this.name.equals(that2.getName()) && this.genericDeclaration.equals(that2.getGenericDeclaration());
                }
                return false;
            }
        }
    }
    
    static final class WildcardTypeImpl implements WildcardType, Serializable
    {
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;
        private static final long serialVersionUID = 0L;
        
        WildcardTypeImpl(final Type[] lowerBounds, final Type[] upperBounds) {
            disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
            disallowPrimitiveType(upperBounds, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(lowerBounds);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(upperBounds);
        }
        
        @Override
        public Type[] getLowerBounds() {
            return toArray(this.lowerBounds);
        }
        
        @Override
        public Type[] getUpperBounds() {
            return toArray(this.upperBounds);
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof WildcardType) {
                final WildcardType that = (WildcardType)obj;
                return this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(that.getUpperBounds()));
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("?");
            for (final Type lowerBound : this.lowerBounds) {
                builder.append(" super ").append(Types.toString(lowerBound));
            }
            for (final Type upperBound : filterUpperBounds(this.upperBounds)) {
                builder.append(" extends ").append(Types.toString(upperBound));
            }
            return builder.toString();
        }
    }
    
    enum JavaVersion
    {
        JAVA6 {
            @Override
            GenericArrayType newArrayType(final Type componentType) {
                return new GenericArrayTypeImpl(componentType);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                Preconditions.checkNotNull(type);
                if (type instanceof Class) {
                    final Class<?> cls = (Class<?>)type;
                    if (cls.isArray()) {
                        return new GenericArrayTypeImpl(cls.getComponentType());
                    }
                }
                return type;
            }
        }, 
        JAVA7 {
            @Override
            Type newArrayType(final Type componentType) {
                if (componentType instanceof Class) {
                    return Types.getArrayClass((Class<?>)componentType);
                }
                return new GenericArrayTypeImpl(componentType);
            }
            
            @Override
            Type usedInGenericType(final Type type) {
                return Preconditions.checkNotNull(type);
            }
        };
        
        static final JavaVersion CURRENT;
        
        abstract Type newArrayType(final Type p0);
        
        abstract Type usedInGenericType(final Type p0);
        
        final ImmutableList<Type> usedInGenericType(final Type[] types) {
            final ImmutableList.Builder<Type> builder = ImmutableList.builder();
            for (final Type type : types) {
                builder.add(this.usedInGenericType(type));
            }
            return builder.build();
        }
        
        static {
            CURRENT = ((new TypeCapture<int[]>() {}.capture() instanceof Class) ? JavaVersion.JAVA7 : JavaVersion.JAVA6);
        }
    }
    
    static final class NativeTypeVariableEquals<X>
    {
        static final boolean NATIVE_TYPE_VARIABLE_ONLY;
        
        static {
            NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
        }
    }
}
