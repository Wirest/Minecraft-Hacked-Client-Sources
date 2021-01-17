// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Arrays;
import java.util.Comparator;
import com.google.common.collect.Ordering;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import java.util.Set;
import com.google.common.collect.ForwardingSet;
import com.google.common.annotations.VisibleForTesting;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import com.google.common.base.Joiner;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.google.common.primitives.Primitives;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import java.lang.reflect.WildcardType;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.base.Preconditions;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Type;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable
{
    private final Type runtimeType;
    private transient TypeResolver typeResolver;
    
    protected TypeToken() {
        this.runtimeType = this.capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }
    
    protected TypeToken(final Class<?> declaringClass) {
        final Type captured = super.capture();
        if (captured instanceof Class) {
            this.runtimeType = captured;
        }
        else {
            this.runtimeType = of(declaringClass).resolveType(captured).runtimeType;
        }
    }
    
    private TypeToken(final Type type) {
        this.runtimeType = Preconditions.checkNotNull(type);
    }
    
    public static <T> TypeToken<T> of(final Class<T> type) {
        return new SimpleTypeToken<T>((Type)type);
    }
    
    public static TypeToken<?> of(final Type type) {
        return new SimpleTypeToken<Object>(type);
    }
    
    public final Class<? super T> getRawType() {
        final Class<? super T> result;
        final Class<?> rawType = result = (Class<? super T>)getRawType(this.runtimeType);
        return result;
    }
    
    private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
        final ImmutableSet<Class<? super T>> result = (ImmutableSet<Class<? super T>>)getRawTypes(this.runtimeType);
        return result;
    }
    
    public final Type getType() {
        return this.runtimeType;
    }
    
    public final <X> TypeToken<T> where(final TypeParameter<X> typeParam, final TypeToken<X> typeArg) {
        final TypeResolver resolver = new TypeResolver().where(ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
        return new SimpleTypeToken<T>(resolver.resolveType(this.runtimeType));
    }
    
    public final <X> TypeToken<T> where(final TypeParameter<X> typeParam, final Class<X> typeArg) {
        return this.where(typeParam, (TypeToken<X>)of((Class<X>)typeArg));
    }
    
    public final TypeToken<?> resolveType(final Type type) {
        Preconditions.checkNotNull(type);
        TypeResolver resolver = this.typeResolver;
        if (resolver == null) {
            final TypeResolver accordingTo = TypeResolver.accordingTo(this.runtimeType);
            this.typeResolver = accordingTo;
            resolver = accordingTo;
        }
        return of(resolver.resolveType(type));
    }
    
    private Type[] resolveInPlace(final Type[] types) {
        for (int i = 0; i < types.length; ++i) {
            types[i] = this.resolveType(types[i]).getType();
        }
        return types;
    }
    
    private TypeToken<?> resolveSupertype(final Type type) {
        final TypeToken<?> supertype = this.resolveType(type);
        supertype.typeResolver = this.typeResolver;
        return supertype;
    }
    
    @Nullable
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
        }
        final Type superclass = this.getRawType().getGenericSuperclass();
        if (superclass == null) {
            return null;
        }
        final TypeToken<? super T> superToken = (TypeToken<? super T>)this.resolveSupertype(superclass);
        return superToken;
    }
    
    @Nullable
    private TypeToken<? super T> boundAsSuperclass(final Type bound) {
        final TypeToken<?> token = of(bound);
        if (token.getRawType().isInterface()) {
            return null;
        }
        final TypeToken<? super T> superclass = (TypeToken<? super T>)token;
        return superclass;
    }
    
    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return this.boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
        }
        final ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (final Type interfaceType : this.getRawType().getGenericInterfaces()) {
            final TypeToken<? super T> resolvedInterface = (TypeToken<? super T>)this.resolveSupertype(interfaceType);
            builder.add(resolvedInterface);
        }
        return builder.build();
    }
    
    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(final Type[] bounds) {
        final ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
        for (final Type bound : bounds) {
            final TypeToken<? super T> boundType = (TypeToken<? super T>)of(bound);
            if (boundType.getRawType().isInterface()) {
                builder.add(boundType);
            }
        }
        return builder.build();
    }
    
    public final TypeSet getTypes() {
        return new TypeSet();
    }
    
    public final TypeToken<? super T> getSupertype(final Class<? super T> superclass) {
        Preconditions.checkArgument(superclass.isAssignableFrom(this.getRawType()), "%s is not a super class of %s", superclass, this);
        if (this.runtimeType instanceof TypeVariable) {
            return this.getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return this.getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
        }
        if (superclass.isArray()) {
            return this.getArraySupertype(superclass);
        }
        final TypeToken<? super T> supertype = (TypeToken<? super T>)this.resolveSupertype(toGenericType(superclass).runtimeType);
        return supertype;
    }
    
    public final TypeToken<? extends T> getSubtype(final Class<?> subclass) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if (this.runtimeType instanceof WildcardType) {
            return this.getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
        }
        Preconditions.checkArgument(this.getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
        if (this.isArray()) {
            return this.getArraySubtype(subclass);
        }
        final TypeToken<? extends T> subtype = (TypeToken<? extends T>)of(this.resolveTypeArgsForSubclass(subclass));
        return subtype;
    }
    
    public final boolean isAssignableFrom(final TypeToken<?> type) {
        return this.isAssignableFrom(type.runtimeType);
    }
    
    public final boolean isAssignableFrom(final Type type) {
        return isAssignable(Preconditions.checkNotNull(type), this.runtimeType);
    }
    
    public final boolean isArray() {
        return this.getComponentType() != null;
    }
    
    public final boolean isPrimitive() {
        return this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive();
    }
    
    public final TypeToken<T> wrap() {
        if (this.isPrimitive()) {
            final Class<T> type = (Class<T>)this.runtimeType;
            return of((Class<T>)Primitives.wrap((Class<T>)type));
        }
        return this;
    }
    
    private boolean isWrapper() {
        return Primitives.allWrapperTypes().contains(this.runtimeType);
    }
    
    public final TypeToken<T> unwrap() {
        if (this.isWrapper()) {
            final Class<T> type = (Class<T>)this.runtimeType;
            return of((Class<T>)Primitives.unwrap((Class<T>)type));
        }
        return this;
    }
    
    @Nullable
    public final TypeToken<?> getComponentType() {
        final Type componentType = Types.getComponentType(this.runtimeType);
        if (componentType == null) {
            return null;
        }
        return of(componentType);
    }
    
    public final Invokable<T, Object> method(final Method method) {
        Preconditions.checkArgument(of(method.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", method, this);
        return (Invokable<T, Object>)new Invokable.MethodInvokable<T>(method) {
            @Override
            Type getGenericReturnType() {
                return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
            }
            
            @Override
            Type[] getGenericParameterTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
            }
            
            @Override
            Type[] getGenericExceptionTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
            }
            
            @Override
            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }
            
            @Override
            public String toString() {
                return this.getOwnerType() + "." + super.toString();
            }
        };
    }
    
    public final Invokable<T, T> constructor(final Constructor<?> constructor) {
        Preconditions.checkArgument(constructor.getDeclaringClass() == this.getRawType(), "%s not declared by %s", constructor, this.getRawType());
        return (Invokable<T, T>)new Invokable.ConstructorInvokable<T>(constructor) {
            @Override
            Type getGenericReturnType() {
                return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
            }
            
            @Override
            Type[] getGenericParameterTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
            }
            
            @Override
            Type[] getGenericExceptionTypes() {
                return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
            }
            
            @Override
            public TypeToken<T> getOwnerType() {
                return TypeToken.this;
            }
            
            @Override
            public String toString() {
                return this.getOwnerType() + "(" + Joiner.on(", ").join(this.getGenericParameterTypes()) + ")";
            }
        };
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (o instanceof TypeToken) {
            final TypeToken<?> that = (TypeToken<?>)o;
            return this.runtimeType.equals(that.runtimeType);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.runtimeType.hashCode();
    }
    
    @Override
    public String toString() {
        return Types.toString(this.runtimeType);
    }
    
    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }
    
    final TypeToken<T> rejectTypeVariables() {
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> type) {
                throw new IllegalArgumentException(TypeToken.this.runtimeType + "contains a type variable and is not safe for the operation");
            }
            
            @Override
            void visitWildcardType(final WildcardType type) {
                this.visit(type.getLowerBounds());
                this.visit(type.getUpperBounds());
            }
            
            @Override
            void visitParameterizedType(final ParameterizedType type) {
                this.visit(type.getActualTypeArguments());
                this.visit(type.getOwnerType());
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType type) {
                this.visit(type.getGenericComponentType());
            }
        }.visit(this.runtimeType);
        return this;
    }
    
    private static boolean isAssignable(final Type from, final Type to) {
        if (to.equals(from)) {
            return true;
        }
        if (to instanceof WildcardType) {
            return isAssignableToWildcardType(from, (WildcardType)to);
        }
        if (from instanceof TypeVariable) {
            return isAssignableFromAny(((TypeVariable)from).getBounds(), to);
        }
        if (from instanceof WildcardType) {
            return isAssignableFromAny(((WildcardType)from).getUpperBounds(), to);
        }
        if (from instanceof GenericArrayType) {
            return isAssignableFromGenericArrayType((GenericArrayType)from, to);
        }
        if (to instanceof Class) {
            return isAssignableToClass(from, (Class<?>)to);
        }
        if (to instanceof ParameterizedType) {
            return isAssignableToParameterizedType(from, (ParameterizedType)to);
        }
        return to instanceof GenericArrayType && isAssignableToGenericArrayType(from, (GenericArrayType)to);
    }
    
    private static boolean isAssignableFromAny(final Type[] fromTypes, final Type to) {
        for (final Type from : fromTypes) {
            if (isAssignable(from, to)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isAssignableToClass(final Type from, final Class<?> to) {
        return to.isAssignableFrom(getRawType(from));
    }
    
    private static boolean isAssignableToWildcardType(final Type from, final WildcardType to) {
        return isAssignable(from, supertypeBound(to)) && isAssignableBySubtypeBound(from, to);
    }
    
    private static boolean isAssignableBySubtypeBound(final Type from, final WildcardType to) {
        final Type toSubtypeBound = subtypeBound(to);
        if (toSubtypeBound == null) {
            return true;
        }
        final Type fromSubtypeBound = subtypeBound(from);
        return fromSubtypeBound != null && isAssignable(toSubtypeBound, fromSubtypeBound);
    }
    
    private static boolean isAssignableToParameterizedType(final Type from, final ParameterizedType to) {
        final Class<?> matchedClass = getRawType(to);
        if (!matchedClass.isAssignableFrom(getRawType(from))) {
            return false;
        }
        final Type[] typeParams = matchedClass.getTypeParameters();
        final Type[] toTypeArgs = to.getActualTypeArguments();
        final TypeToken<?> fromTypeToken = of(from);
        for (int i = 0; i < typeParams.length; ++i) {
            final Type fromTypeArg = fromTypeToken.resolveType(typeParams[i]).runtimeType;
            if (!matchTypeArgument(fromTypeArg, toTypeArgs[i])) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isAssignableToGenericArrayType(final Type from, final GenericArrayType to) {
        if (from instanceof Class) {
            final Class<?> fromClass = (Class<?>)from;
            return fromClass.isArray() && isAssignable(fromClass.getComponentType(), to.getGenericComponentType());
        }
        if (from instanceof GenericArrayType) {
            final GenericArrayType fromArrayType = (GenericArrayType)from;
            return isAssignable(fromArrayType.getGenericComponentType(), to.getGenericComponentType());
        }
        return false;
    }
    
    private static boolean isAssignableFromGenericArrayType(final GenericArrayType from, final Type to) {
        if (to instanceof Class) {
            final Class<?> toClass = (Class<?>)to;
            if (!toClass.isArray()) {
                return toClass == Object.class;
            }
            return isAssignable(from.getGenericComponentType(), toClass.getComponentType());
        }
        else {
            if (to instanceof GenericArrayType) {
                final GenericArrayType toArrayType = (GenericArrayType)to;
                return isAssignable(from.getGenericComponentType(), toArrayType.getGenericComponentType());
            }
            return false;
        }
    }
    
    private static boolean matchTypeArgument(final Type from, final Type to) {
        return from.equals(to) || (to instanceof WildcardType && isAssignableToWildcardType(from, (WildcardType)to));
    }
    
    private static Type supertypeBound(final Type type) {
        if (type instanceof WildcardType) {
            return supertypeBound((WildcardType)type);
        }
        return type;
    }
    
    private static Type supertypeBound(final WildcardType type) {
        final Type[] upperBounds = type.getUpperBounds();
        if (upperBounds.length == 1) {
            return supertypeBound(upperBounds[0]);
        }
        if (upperBounds.length == 0) {
            return Object.class;
        }
        throw new AssertionError((Object)("There should be at most one upper bound for wildcard type: " + type));
    }
    
    @Nullable
    private static Type subtypeBound(final Type type) {
        if (type instanceof WildcardType) {
            return subtypeBound((WildcardType)type);
        }
        return type;
    }
    
    @Nullable
    private static Type subtypeBound(final WildcardType type) {
        final Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds.length == 1) {
            return subtypeBound(lowerBounds[0]);
        }
        if (lowerBounds.length == 0) {
            return null;
        }
        throw new AssertionError((Object)("Wildcard should have at most one lower bound: " + type));
    }
    
    @VisibleForTesting
    static Class<?> getRawType(final Type type) {
        return getRawTypes(type).iterator().next();
    }
    
    @VisibleForTesting
    static ImmutableSet<Class<?>> getRawTypes(final Type type) {
        Preconditions.checkNotNull(type);
        final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> t) {
                this.visit(t.getBounds());
            }
            
            @Override
            void visitWildcardType(final WildcardType t) {
                this.visit(t.getUpperBounds());
            }
            
            @Override
            void visitParameterizedType(final ParameterizedType t) {
                builder.add(t.getRawType());
            }
            
            @Override
            void visitClass(final Class<?> t) {
                builder.add(t);
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType t) {
                builder.add(Types.getArrayClass(TypeToken.getRawType(t.getGenericComponentType())));
            }
        }.visit(type);
        return builder.build();
    }
    
    @VisibleForTesting
    static <T> TypeToken<? extends T> toGenericType(final Class<T> cls) {
        if (cls.isArray()) {
            final Type arrayOfGenericType = Types.newArrayType(toGenericType(cls.getComponentType()).runtimeType);
            final TypeToken<? extends T> result = (TypeToken<? extends T>)of(arrayOfGenericType);
            return result;
        }
        final TypeVariable<Class<T>>[] typeParams = cls.getTypeParameters();
        if (typeParams.length > 0) {
            final TypeToken<? extends T> type = (TypeToken<? extends T>)of(Types.newParameterizedType(cls, (Type[])typeParams));
            return type;
        }
        return of((Class<? extends T>)cls);
    }
    
    private TypeToken<? super T> getSupertypeFromUpperBounds(final Class<? super T> supertype, final Type[] upperBounds) {
        for (final Type upperBound : upperBounds) {
            final TypeToken<? super T> bound = (TypeToken<? super T>)of(upperBound);
            if (of(supertype).isAssignableFrom(bound)) {
                final TypeToken<? super T> result = bound.getSupertype(supertype);
                return result;
            }
        }
        throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
    }
    
    private TypeToken<? extends T> getSubtypeFromLowerBounds(final Class<?> subclass, final Type[] lowerBounds) {
        final Type[] arr$ = lowerBounds;
        final int len$ = arr$.length;
        final int i$ = 0;
        if (i$ < len$) {
            final Type lowerBound = arr$[i$];
            final TypeToken<? extends T> bound = (TypeToken<? extends T>)of(lowerBound);
            return bound.getSubtype(subclass);
        }
        throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
    }
    
    private TypeToken<? super T> getArraySupertype(final Class<? super T> supertype) {
        final TypeToken componentType = Preconditions.checkNotNull(this.getComponentType(), "%s isn't a super type of %s", supertype, this);
        final TypeToken<?> componentSupertype = (TypeToken<?>)componentType.getSupertype(supertype.getComponentType());
        final TypeToken<? super T> result = (TypeToken<? super T>)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
        return result;
    }
    
    private TypeToken<? extends T> getArraySubtype(final Class<?> subclass) {
        final TypeToken<?> componentSubtype = this.getComponentType().getSubtype(subclass.getComponentType());
        final TypeToken<? extends T> result = (TypeToken<? extends T>)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
        return result;
    }
    
    private Type resolveTypeArgsForSubclass(final Class<?> subclass) {
        if (this.runtimeType instanceof Class) {
            return subclass;
        }
        final TypeToken<?> genericSubtype = toGenericType(subclass);
        final Type supertypeWithArgsFromSubtype = genericSubtype.getSupertype(this.getRawType()).runtimeType;
        return new TypeResolver().where(supertypeWithArgsFromSubtype, this.runtimeType).resolveType(genericSubtype.runtimeType);
    }
    
    private static Type newArrayClassOrGenericArrayType(final Type componentType) {
        return Types.JavaVersion.JAVA7.newArrayType(componentType);
    }
    
    public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable
    {
        private transient ImmutableSet<TypeToken<? super T>> types;
        private static final long serialVersionUID = 0L;
        
        TypeSet() {
        }
        
        public TypeSet interfaces() {
            return new InterfaceSet(this);
        }
        
        public TypeSet classes() {
            return new ClassSet();
        }
        
        @Override
        protected Set<TypeToken<? super T>> delegate() {
            final ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
            if (filteredTypes == null) {
                final ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList<TypeToken<? super T>>)TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
                return this.types = FluentIterable.from(collectedTypes).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            }
            return filteredTypes;
        }
        
        public Set<Class<? super T>> rawTypes() {
            final ImmutableList<Class<? super T>> collectedTypes = (ImmutableList<Class<? super T>>)TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getImmediateRawTypes());
            return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection<?>)collectedTypes);
        }
    }
    
    private final class InterfaceSet extends TypeSet
    {
        private final transient TypeSet allTypes;
        private transient ImmutableSet<TypeToken<? super T>> interfaces;
        private static final long serialVersionUID = 0L;
        
        InterfaceSet(final TypeSet allTypes) {
            this.allTypes = allTypes;
        }
        
        @Override
        protected Set<TypeToken<? super T>> delegate() {
            final ImmutableSet<TypeToken<? super T>> result = this.interfaces;
            if (result == null) {
                return this.interfaces = FluentIterable.from((Iterable<TypeToken<? super T>>)this.allTypes).filter(TypeFilter.INTERFACE_ONLY).toSet();
            }
            return result;
        }
        
        @Override
        public TypeSet interfaces() {
            return this;
        }
        
        @Override
        public Set<Class<? super T>> rawTypes() {
            final ImmutableList<Class<? super T>> collectedTypes = (ImmutableList<Class<? super T>>)TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getImmediateRawTypes());
            return FluentIterable.from(collectedTypes).filter(new Predicate<Class<?>>() {
                @Override
                public boolean apply(final Class<?> type) {
                    return type.isInterface();
                }
            }).toSet();
        }
        
        @Override
        public TypeSet classes() {
            throw new UnsupportedOperationException("interfaces().classes() not supported.");
        }
        
        private Object readResolve() {
            return TypeToken.this.getTypes().interfaces();
        }
    }
    
    private final class ClassSet extends TypeSet
    {
        private transient ImmutableSet<TypeToken<? super T>> classes;
        private static final long serialVersionUID = 0L;
        
        @Override
        protected Set<TypeToken<? super T>> delegate() {
            final ImmutableSet<TypeToken<? super T>> result = this.classes;
            if (result == null) {
                final ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList<TypeToken<? super T>>)TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
                return this.classes = FluentIterable.from(collectedTypes).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
            }
            return result;
        }
        
        @Override
        public TypeSet classes() {
            return this;
        }
        
        @Override
        public Set<Class<? super T>> rawTypes() {
            final ImmutableList<Class<? super T>> collectedTypes = (ImmutableList<Class<? super T>>)TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.this.getImmediateRawTypes());
            return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection<?>)collectedTypes);
        }
        
        @Override
        public TypeSet interfaces() {
            throw new UnsupportedOperationException("classes().interfaces() not supported.");
        }
        
        private Object readResolve() {
            return TypeToken.this.getTypes().classes();
        }
    }
    
    private enum TypeFilter implements Predicate<TypeToken<?>>
    {
        IGNORE_TYPE_VARIABLE_OR_WILDCARD {
            @Override
            public boolean apply(final TypeToken<?> type) {
                return !(((TypeToken<Object>)type).runtimeType instanceof TypeVariable) && !(((TypeToken<Object>)type).runtimeType instanceof WildcardType);
            }
        }, 
        INTERFACE_ONLY {
            @Override
            public boolean apply(final TypeToken<?> type) {
                return type.getRawType().isInterface();
            }
        };
    }
    
    private static final class SimpleTypeToken<T> extends TypeToken<T>
    {
        private static final long serialVersionUID = 0L;
        
        SimpleTypeToken(final Type type) {
            super(type, null);
        }
    }
    
    private abstract static class TypeCollector<K>
    {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE;
        static final TypeCollector<Class<?>> FOR_RAW_TYPE;
        
        final TypeCollector<K> classesOnly() {
            return new ForwardingTypeCollector<K>(this) {
                @Override
                Iterable<? extends K> getInterfaces(final K type) {
                    return (Iterable<? extends K>)ImmutableSet.of();
                }
                
                @Override
                ImmutableList<K> collectTypes(final Iterable<? extends K> types) {
                    final ImmutableList.Builder<K> builder = ImmutableList.builder();
                    for (final K type : types) {
                        if (!this.getRawType(type).isInterface()) {
                            builder.add(type);
                        }
                    }
                    return super.collectTypes((Iterable<? extends K>)builder.build());
                }
            };
        }
        
        final ImmutableList<K> collectTypes(final K type) {
            return this.collectTypes((Iterable<? extends K>)ImmutableList.of(type));
        }
        
        ImmutableList<K> collectTypes(final Iterable<? extends K> types) {
            final Map<K, Integer> map = (Map<K, Integer>)Maps.newHashMap();
            for (final K type : types) {
                this.collectTypes(type, map);
            }
            return sortKeysByValue(map, Ordering.natural().reverse());
        }
        
        private int collectTypes(final K type, final Map<? super K, Integer> map) {
            final Integer existing = map.get(this);
            if (existing != null) {
                return existing;
            }
            int aboveMe = this.getRawType(type).isInterface() ? 1 : 0;
            for (final K interfaceType : this.getInterfaces(type)) {
                aboveMe = Math.max(aboveMe, this.collectTypes(interfaceType, map));
            }
            final K superclass = this.getSuperclass(type);
            if (superclass != null) {
                aboveMe = Math.max(aboveMe, this.collectTypes(superclass, map));
            }
            map.put((Object)type, Integer.valueOf(aboveMe + 1));
            return aboveMe + 1;
        }
        
        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
            final Ordering<K> keyOrdering = new Ordering<K>() {
                @Override
                public int compare(final K left, final K right) {
                    return valueComparator.compare(map.get(left), map.get(right));
                }
            };
            return keyOrdering.immutableSortedCopy(map.keySet());
        }
        
        abstract Class<?> getRawType(final K p0);
        
        abstract Iterable<? extends K> getInterfaces(final K p0);
        
        @Nullable
        abstract K getSuperclass(final K p0);
        
        static {
            FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>() {
                @Override
                Class<?> getRawType(final TypeToken<?> type) {
                    return type.getRawType();
                }
                
                @Override
                Iterable<? extends TypeToken<?>> getInterfaces(final TypeToken<?> type) {
                    return type.getGenericInterfaces();
                }
                
                @Nullable
                @Override
                TypeToken<?> getSuperclass(final TypeToken<?> type) {
                    return type.getGenericSuperclass();
                }
            };
            FOR_RAW_TYPE = new TypeCollector<Class<?>>() {
                @Override
                Class<?> getRawType(final Class<?> type) {
                    return type;
                }
                
                @Override
                Iterable<? extends Class<?>> getInterfaces(final Class<?> type) {
                    return Arrays.asList(type.getInterfaces());
                }
                
                @Nullable
                @Override
                Class<?> getSuperclass(final Class<?> type) {
                    return type.getSuperclass();
                }
            };
        }
        
        private static class ForwardingTypeCollector<K> extends TypeCollector<K>
        {
            private final TypeCollector<K> delegate;
            
            ForwardingTypeCollector(final TypeCollector<K> delegate) {
                this.delegate = delegate;
            }
            
            @Override
            Class<?> getRawType(final K type) {
                return this.delegate.getRawType(type);
            }
            
            @Override
            Iterable<? extends K> getInterfaces(final K type) {
                return this.delegate.getInterfaces(type);
            }
            
            @Override
            K getSuperclass(final K type) {
                return this.delegate.getSuperclass(type);
            }
        }
    }
}
