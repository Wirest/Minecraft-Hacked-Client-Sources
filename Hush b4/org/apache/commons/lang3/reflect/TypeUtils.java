// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.builder.Builder;
import java.lang.reflect.GenericDeclaration;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Collections;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import org.apache.commons.lang3.Validate;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.lang3.ClassUtils;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class TypeUtils
{
    public static final WildcardType WILDCARD_ALL;
    
    public static boolean isAssignable(final Type type, final Type toType) {
        return isAssignable(type, toType, null);
    }
    
    private static boolean isAssignable(final Type type, final Type toType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (toType == null || toType instanceof Class) {
            return isAssignable(type, (Class<?>)toType);
        }
        if (toType instanceof ParameterizedType) {
            return isAssignable(type, (ParameterizedType)toType, typeVarAssigns);
        }
        if (toType instanceof GenericArrayType) {
            return isAssignable(type, (GenericArrayType)toType, typeVarAssigns);
        }
        if (toType instanceof WildcardType) {
            return isAssignable(type, (WildcardType)toType, typeVarAssigns);
        }
        if (toType instanceof TypeVariable) {
            return isAssignable(type, (TypeVariable<?>)toType, typeVarAssigns);
        }
        throw new IllegalStateException("found an unhandled type: " + toType);
    }
    
    private static boolean isAssignable(final Type type, final Class<?> toClass) {
        if (type == null) {
            return toClass == null || !toClass.isPrimitive();
        }
        if (toClass == null) {
            return false;
        }
        if (toClass.equals(type)) {
            return true;
        }
        if (type instanceof Class) {
            return ClassUtils.isAssignable((Class<?>)type, toClass);
        }
        if (type instanceof ParameterizedType) {
            return isAssignable(getRawType((ParameterizedType)type), toClass);
        }
        if (type instanceof TypeVariable) {
            for (final Type bound : ((TypeVariable)type).getBounds()) {
                if (isAssignable(bound, toClass)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof GenericArrayType) {
            return toClass.equals(Object.class) || (toClass.isArray() && isAssignable(((GenericArrayType)type).getGenericComponentType(), toClass.getComponentType()));
        }
        if (type instanceof WildcardType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }
    
    private static boolean isAssignable(final Type type, final ParameterizedType toParameterizedType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toParameterizedType == null) {
            return false;
        }
        if (toParameterizedType.equals(type)) {
            return true;
        }
        final Class<?> toClass = getRawType(toParameterizedType);
        final Map<TypeVariable<?>, Type> fromTypeVarAssigns = getTypeArguments(type, toClass, null);
        if (fromTypeVarAssigns == null) {
            return false;
        }
        if (fromTypeVarAssigns.isEmpty()) {
            return true;
        }
        final Map<TypeVariable<?>, Type> toTypeVarAssigns = getTypeArguments(toParameterizedType, toClass, typeVarAssigns);
        for (final TypeVariable<?> var : toTypeVarAssigns.keySet()) {
            final Type toTypeArg = unrollVariableAssignments(var, toTypeVarAssigns);
            final Type fromTypeArg = unrollVariableAssignments(var, fromTypeVarAssigns);
            if (fromTypeArg != null && !toTypeArg.equals(fromTypeArg) && (!(toTypeArg instanceof WildcardType) || !isAssignable(fromTypeArg, toTypeArg, typeVarAssigns))) {
                return false;
            }
        }
        return true;
    }
    
    private static Type unrollVariableAssignments(TypeVariable<?> var, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        Type result;
        while (true) {
            result = typeVarAssigns.get(var);
            if (!(result instanceof TypeVariable) || result.equals(var)) {
                break;
            }
            var = (TypeVariable<?>)result;
        }
        return result;
    }
    
    private static boolean isAssignable(final Type type, final GenericArrayType toGenericArrayType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toGenericArrayType == null) {
            return false;
        }
        if (toGenericArrayType.equals(type)) {
            return true;
        }
        final Type toComponentType = toGenericArrayType.getGenericComponentType();
        if (type instanceof Class) {
            final Class<?> cls = (Class<?>)type;
            return cls.isArray() && isAssignable(cls.getComponentType(), toComponentType, typeVarAssigns);
        }
        if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType)type).getGenericComponentType(), toComponentType, typeVarAssigns);
        }
        if (type instanceof WildcardType) {
            for (final Type bound : getImplicitUpperBounds((WildcardType)type)) {
                if (isAssignable(bound, toGenericArrayType)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof TypeVariable) {
            for (final Type bound : getImplicitBounds((TypeVariable<?>)type)) {
                if (isAssignable(bound, toGenericArrayType)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof ParameterizedType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }
    
    private static boolean isAssignable(final Type type, final WildcardType toWildcardType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toWildcardType == null) {
            return false;
        }
        if (toWildcardType.equals(type)) {
            return true;
        }
        final Type[] toUpperBounds = getImplicitUpperBounds(toWildcardType);
        final Type[] toLowerBounds = getImplicitLowerBounds(toWildcardType);
        if (type instanceof WildcardType) {
            final WildcardType wildcardType = (WildcardType)type;
            final Type[] upperBounds = getImplicitUpperBounds(wildcardType);
            final Type[] lowerBounds = getImplicitLowerBounds(wildcardType);
            for (Type toBound : toUpperBounds) {
                toBound = substituteTypeVariables(toBound, typeVarAssigns);
                for (final Type bound : upperBounds) {
                    if (!isAssignable(bound, toBound, typeVarAssigns)) {
                        return false;
                    }
                }
            }
            for (Type toBound : toLowerBounds) {
                toBound = substituteTypeVariables(toBound, typeVarAssigns);
                for (final Type bound : lowerBounds) {
                    if (!isAssignable(toBound, bound, typeVarAssigns)) {
                        return false;
                    }
                }
            }
            return true;
        }
        for (final Type toBound2 : toUpperBounds) {
            if (!isAssignable(type, substituteTypeVariables(toBound2, typeVarAssigns), typeVarAssigns)) {
                return false;
            }
        }
        for (final Type toBound2 : toLowerBounds) {
            if (!isAssignable(substituteTypeVariables(toBound2, typeVarAssigns), type, typeVarAssigns)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isAssignable(final Type type, final TypeVariable<?> toTypeVariable, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (type == null) {
            return true;
        }
        if (toTypeVariable == null) {
            return false;
        }
        if (toTypeVariable.equals(type)) {
            return true;
        }
        if (type instanceof TypeVariable) {
            final Type[] arr$;
            final Type[] bounds = arr$ = getImplicitBounds((TypeVariable<?>)type);
            for (final Type bound : arr$) {
                if (isAssignable(bound, toTypeVariable, typeVarAssigns)) {
                    return true;
                }
            }
        }
        if (type instanceof Class || type instanceof ParameterizedType || type instanceof GenericArrayType || type instanceof WildcardType) {
            return false;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }
    
    private static Type substituteTypeVariables(final Type type, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        if (!(type instanceof TypeVariable) || typeVarAssigns == null) {
            return type;
        }
        final Type replacementType = typeVarAssigns.get(type);
        if (replacementType == null) {
            throw new IllegalArgumentException("missing assignment type for type variable " + type);
        }
        return replacementType;
    }
    
    public static Map<TypeVariable<?>, Type> getTypeArguments(final ParameterizedType type) {
        return getTypeArguments(type, getRawType(type), null);
    }
    
    public static Map<TypeVariable<?>, Type> getTypeArguments(final Type type, final Class<?> toClass) {
        return getTypeArguments(type, toClass, null);
    }
    
    private static Map<TypeVariable<?>, Type> getTypeArguments(final Type type, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        if (type instanceof Class) {
            return getTypeArguments((Class<?>)type, toClass, subtypeVarAssigns);
        }
        if (type instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType)type, toClass, subtypeVarAssigns);
        }
        if (type instanceof GenericArrayType) {
            return getTypeArguments(((GenericArrayType)type).getGenericComponentType(), toClass.isArray() ? toClass.getComponentType() : toClass, subtypeVarAssigns);
        }
        if (type instanceof WildcardType) {
            for (final Type bound : getImplicitUpperBounds((WildcardType)type)) {
                if (isAssignable(bound, toClass)) {
                    return getTypeArguments(bound, toClass, subtypeVarAssigns);
                }
            }
            return null;
        }
        if (type instanceof TypeVariable) {
            for (final Type bound : getImplicitBounds((TypeVariable<?>)type)) {
                if (isAssignable(bound, toClass)) {
                    return getTypeArguments(bound, toClass, subtypeVarAssigns);
                }
            }
            return null;
        }
        throw new IllegalStateException("found an unhandled type: " + type);
    }
    
    private static Map<TypeVariable<?>, Type> getTypeArguments(final ParameterizedType parameterizedType, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        final Class<?> cls = getRawType(parameterizedType);
        if (!isAssignable(cls, toClass)) {
            return null;
        }
        final Type ownerType = parameterizedType.getOwnerType();
        Map<TypeVariable<?>, Type> typeVarAssigns;
        if (ownerType instanceof ParameterizedType) {
            final ParameterizedType parameterizedOwnerType = (ParameterizedType)ownerType;
            typeVarAssigns = getTypeArguments(parameterizedOwnerType, getRawType(parameterizedOwnerType), subtypeVarAssigns);
        }
        else {
            typeVarAssigns = ((subtypeVarAssigns == null) ? new HashMap<TypeVariable<?>, Type>() : new HashMap<TypeVariable<?>, Type>(subtypeVarAssigns));
        }
        final Type[] typeArgs = parameterizedType.getActualTypeArguments();
        final TypeVariable<?>[] typeParams = cls.getTypeParameters();
        for (int i = 0; i < typeParams.length; ++i) {
            final Type typeArg = typeArgs[i];
            typeVarAssigns.put(typeParams[i], typeVarAssigns.containsKey(typeArg) ? typeVarAssigns.get(typeArg) : typeArg);
        }
        if (toClass.equals(cls)) {
            return typeVarAssigns;
        }
        return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
    }
    
    private static Map<TypeVariable<?>, Type> getTypeArguments(Class<?> cls, final Class<?> toClass, final Map<TypeVariable<?>, Type> subtypeVarAssigns) {
        if (!isAssignable(cls, toClass)) {
            return null;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive()) {
                return new HashMap<TypeVariable<?>, Type>();
            }
            cls = ClassUtils.primitiveToWrapper(cls);
        }
        final HashMap<TypeVariable<?>, Type> typeVarAssigns = (subtypeVarAssigns == null) ? new HashMap<TypeVariable<?>, Type>() : new HashMap<TypeVariable<?>, Type>(subtypeVarAssigns);
        if (toClass.equals(cls)) {
            return typeVarAssigns;
        }
        return getTypeArguments(getClosestParentType(cls, toClass), toClass, typeVarAssigns);
    }
    
    public static Map<TypeVariable<?>, Type> determineTypeArguments(final Class<?> cls, final ParameterizedType superType) {
        Validate.notNull(cls, "cls is null", new Object[0]);
        Validate.notNull(superType, "superType is null", new Object[0]);
        final Class<?> superClass = getRawType(superType);
        if (!isAssignable(cls, superClass)) {
            return null;
        }
        if (cls.equals(superClass)) {
            return getTypeArguments(superType, superClass, null);
        }
        final Type midType = getClosestParentType(cls, superClass);
        if (midType instanceof Class) {
            return determineTypeArguments((Class<?>)midType, superType);
        }
        final ParameterizedType midParameterizedType = (ParameterizedType)midType;
        final Class<?> midClass = getRawType(midParameterizedType);
        final Map<TypeVariable<?>, Type> typeVarAssigns = determineTypeArguments(midClass, superType);
        mapTypeVariablesToArguments(cls, midParameterizedType, typeVarAssigns);
        return typeVarAssigns;
    }
    
    private static <T> void mapTypeVariablesToArguments(final Class<T> cls, final ParameterizedType parameterizedType, final Map<TypeVariable<?>, Type> typeVarAssigns) {
        final Type ownerType = parameterizedType.getOwnerType();
        if (ownerType instanceof ParameterizedType) {
            mapTypeVariablesToArguments((Class<Object>)cls, (ParameterizedType)ownerType, typeVarAssigns);
        }
        final Type[] typeArgs = parameterizedType.getActualTypeArguments();
        final TypeVariable<?>[] typeVars = getRawType(parameterizedType).getTypeParameters();
        final List<TypeVariable<Class<T>>> typeVarList = Arrays.asList(cls.getTypeParameters());
        for (int i = 0; i < typeArgs.length; ++i) {
            final TypeVariable<?> typeVar = typeVars[i];
            final Type typeArg = typeArgs[i];
            if (typeVarList.contains(typeArg) && typeVarAssigns.containsKey(typeVar)) {
                typeVarAssigns.put((TypeVariable<?>)typeArg, typeVarAssigns.get(typeVar));
            }
        }
    }
    
    private static Type getClosestParentType(final Class<?> cls, final Class<?> superClass) {
        if (superClass.isInterface()) {
            final Type[] interfaceTypes = cls.getGenericInterfaces();
            Type genericInterface = null;
            for (final Type midType : interfaceTypes) {
                Class<?> midClass = null;
                if (midType instanceof ParameterizedType) {
                    midClass = getRawType((ParameterizedType)midType);
                }
                else {
                    if (!(midType instanceof Class)) {
                        throw new IllegalStateException("Unexpected generic interface type found: " + midType);
                    }
                    midClass = (Class<?>)midType;
                }
                if (isAssignable(midClass, superClass) && isAssignable(genericInterface, (Type)midClass)) {
                    genericInterface = midType;
                }
            }
            if (genericInterface != null) {
                return genericInterface;
            }
        }
        return cls.getGenericSuperclass();
    }
    
    public static boolean isInstance(final Object value, final Type type) {
        return type != null && ((value == null) ? (!(type instanceof Class) || !((Class)type).isPrimitive()) : isAssignable(value.getClass(), type, null));
    }
    
    public static Type[] normalizeUpperBounds(final Type[] bounds) {
        Validate.notNull(bounds, "null value specified for bounds array", new Object[0]);
        if (bounds.length < 2) {
            return bounds;
        }
        final Set<Type> types = new HashSet<Type>(bounds.length);
        for (final Type type1 : bounds) {
            boolean subtypeFound = false;
            for (final Type type2 : bounds) {
                if (type1 != type2 && isAssignable(type2, type1, null)) {
                    subtypeFound = true;
                    break;
                }
            }
            if (!subtypeFound) {
                types.add(type1);
            }
        }
        return types.toArray(new Type[types.size()]);
    }
    
    public static Type[] getImplicitBounds(final TypeVariable<?> typeVariable) {
        Validate.notNull(typeVariable, "typeVariable is null", new Object[0]);
        final Type[] bounds = typeVariable.getBounds();
        return (bounds.length == 0) ? new Type[] { Object.class } : normalizeUpperBounds(bounds);
    }
    
    public static Type[] getImplicitUpperBounds(final WildcardType wildcardType) {
        Validate.notNull(wildcardType, "wildcardType is null", new Object[0]);
        final Type[] bounds = wildcardType.getUpperBounds();
        return (bounds.length == 0) ? new Type[] { Object.class } : normalizeUpperBounds(bounds);
    }
    
    public static Type[] getImplicitLowerBounds(final WildcardType wildcardType) {
        Validate.notNull(wildcardType, "wildcardType is null", new Object[0]);
        final Type[] bounds = wildcardType.getLowerBounds();
        return (bounds.length == 0) ? new Type[] { null } : bounds;
    }
    
    public static boolean typesSatisfyVariables(final Map<TypeVariable<?>, Type> typeVarAssigns) {
        Validate.notNull(typeVarAssigns, "typeVarAssigns is null", new Object[0]);
        for (final Map.Entry<TypeVariable<?>, Type> entry : typeVarAssigns.entrySet()) {
            final TypeVariable<?> typeVar = entry.getKey();
            final Type type = entry.getValue();
            for (final Type bound : getImplicitBounds(typeVar)) {
                if (!isAssignable(type, substituteTypeVariables(bound, typeVarAssigns), typeVarAssigns)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static Class<?> getRawType(final ParameterizedType parameterizedType) {
        final Type rawType = parameterizedType.getRawType();
        if (!(rawType instanceof Class)) {
            throw new IllegalStateException("Wait... What!? Type of rawType: " + rawType);
        }
        return (Class<?>)rawType;
    }
    
    public static Class<?> getRawType(final Type type, final Type assigningType) {
        if (type instanceof Class) {
            return (Class<?>)type;
        }
        if (type instanceof ParameterizedType) {
            return getRawType((ParameterizedType)type);
        }
        if (type instanceof TypeVariable) {
            if (assigningType == null) {
                return null;
            }
            final Object genericDeclaration = ((TypeVariable)type).getGenericDeclaration();
            if (!(genericDeclaration instanceof Class)) {
                return null;
            }
            final Map<TypeVariable<?>, Type> typeVarAssigns = getTypeArguments(assigningType, (Class<?>)genericDeclaration);
            if (typeVarAssigns == null) {
                return null;
            }
            final Type typeArgument = typeVarAssigns.get(type);
            if (typeArgument == null) {
                return null;
            }
            return getRawType(typeArgument, assigningType);
        }
        else {
            if (type instanceof GenericArrayType) {
                final Class<?> rawComponentType = getRawType(((GenericArrayType)type).getGenericComponentType(), assigningType);
                return Array.newInstance(rawComponentType, 0).getClass();
            }
            if (type instanceof WildcardType) {
                return null;
            }
            throw new IllegalArgumentException("unknown type: " + type);
        }
    }
    
    public static boolean isArrayType(final Type type) {
        return type instanceof GenericArrayType || (type instanceof Class && ((Class)type).isArray());
    }
    
    public static Type getArrayComponentType(final Type type) {
        if (type instanceof Class) {
            final Class<?> clazz = (Class<?>)type;
            return clazz.isArray() ? clazz.getComponentType() : null;
        }
        if (type instanceof GenericArrayType) {
            return ((GenericArrayType)type).getGenericComponentType();
        }
        return null;
    }
    
    public static Type unrollVariables(Map<TypeVariable<?>, Type> typeArguments, final Type type) {
        if (typeArguments == null) {
            typeArguments = Collections.emptyMap();
        }
        if (containsTypeVariables(type)) {
            if (type instanceof TypeVariable) {
                return unrollVariables(typeArguments, typeArguments.get(type));
            }
            if (type instanceof ParameterizedType) {
                final ParameterizedType p = (ParameterizedType)type;
                Map<TypeVariable<?>, Type> parameterizedTypeArguments;
                if (p.getOwnerType() == null) {
                    parameterizedTypeArguments = typeArguments;
                }
                else {
                    parameterizedTypeArguments = new HashMap<TypeVariable<?>, Type>(typeArguments);
                    parameterizedTypeArguments.putAll(getTypeArguments(p));
                }
                final Type[] args = p.getActualTypeArguments();
                for (int i = 0; i < args.length; ++i) {
                    final Type unrolled = unrollVariables(parameterizedTypeArguments, args[i]);
                    if (unrolled != null) {
                        args[i] = unrolled;
                    }
                }
                return parameterizeWithOwner(p.getOwnerType(), (Class<?>)p.getRawType(), args);
            }
            if (type instanceof WildcardType) {
                final WildcardType wild = (WildcardType)type;
                return wildcardType().withUpperBounds(unrollBounds(typeArguments, wild.getUpperBounds())).withLowerBounds(unrollBounds(typeArguments, wild.getLowerBounds())).build();
            }
        }
        return type;
    }
    
    private static Type[] unrollBounds(final Map<TypeVariable<?>, Type> typeArguments, final Type[] bounds) {
        Type[] result = bounds;
        for (int i = 0; i < result.length; ++i) {
            final Type unrolled = unrollVariables(typeArguments, result[i]);
            if (unrolled == null) {
                result = ArrayUtils.remove(result, i--);
            }
            else {
                result[i] = unrolled;
            }
        }
        return result;
    }
    
    public static boolean containsTypeVariables(final Type type) {
        if (type instanceof TypeVariable) {
            return true;
        }
        if (type instanceof Class) {
            return ((Class)type).getTypeParameters().length > 0;
        }
        if (type instanceof ParameterizedType) {
            for (final Type arg : ((ParameterizedType)type).getActualTypeArguments()) {
                if (containsTypeVariables(arg)) {
                    return true;
                }
            }
            return false;
        }
        if (type instanceof WildcardType) {
            final WildcardType wild = (WildcardType)type;
            return containsTypeVariables(getImplicitLowerBounds(wild)[0]) || containsTypeVariables(getImplicitUpperBounds(wild)[0]);
        }
        return false;
    }
    
    public static final ParameterizedType parameterize(final Class<?> raw, final Type... typeArguments) {
        return parameterizeWithOwner(null, raw, typeArguments);
    }
    
    public static final ParameterizedType parameterize(final Class<?> raw, final Map<TypeVariable<?>, Type> typeArgMappings) {
        Validate.notNull(raw, "raw class is null", new Object[0]);
        Validate.notNull(typeArgMappings, "typeArgMappings is null", new Object[0]);
        return parameterizeWithOwner(null, raw, extractTypeArgumentsFrom(typeArgMappings, raw.getTypeParameters()));
    }
    
    public static final ParameterizedType parameterizeWithOwner(final Type owner, final Class<?> raw, final Type... typeArguments) {
        Validate.notNull(raw, "raw class is null", new Object[0]);
        Type useOwner;
        if (raw.getEnclosingClass() == null) {
            Validate.isTrue(owner == null, "no owner allowed for top-level %s", raw);
            useOwner = null;
        }
        else if (owner == null) {
            useOwner = raw.getEnclosingClass();
        }
        else {
            Validate.isTrue(isAssignable(owner, raw.getEnclosingClass()), "%s is invalid owner type for parameterized %s", owner, raw);
            useOwner = owner;
        }
        Validate.noNullElements(typeArguments, "null type argument at index %s", new Object[0]);
        Validate.isTrue(raw.getTypeParameters().length == typeArguments.length, "invalid number of type parameters specified: expected %s, got %s", raw.getTypeParameters().length, typeArguments.length);
        return new ParameterizedTypeImpl((Class)raw, useOwner, typeArguments);
    }
    
    public static final ParameterizedType parameterizeWithOwner(final Type owner, final Class<?> raw, final Map<TypeVariable<?>, Type> typeArgMappings) {
        Validate.notNull(raw, "raw class is null", new Object[0]);
        Validate.notNull(typeArgMappings, "typeArgMappings is null", new Object[0]);
        return parameterizeWithOwner(owner, raw, extractTypeArgumentsFrom(typeArgMappings, raw.getTypeParameters()));
    }
    
    private static Type[] extractTypeArgumentsFrom(final Map<TypeVariable<?>, Type> mappings, final TypeVariable<?>[] variables) {
        final Type[] result = new Type[variables.length];
        int index = 0;
        for (final TypeVariable<?> var : variables) {
            Validate.isTrue(mappings.containsKey(var), "missing argument mapping for %s", toString(var));
            result[index++] = mappings.get(var);
        }
        return result;
    }
    
    public static WildcardTypeBuilder wildcardType() {
        return new WildcardTypeBuilder();
    }
    
    public static GenericArrayType genericArrayType(final Type componentType) {
        return new GenericArrayTypeImpl((Type)Validate.notNull(componentType, "componentType is null", new Object[0]));
    }
    
    public static boolean equals(final Type t1, final Type t2) {
        if (ObjectUtils.equals(t1, t2)) {
            return true;
        }
        if (t1 instanceof ParameterizedType) {
            return equals((ParameterizedType)t1, t2);
        }
        if (t1 instanceof GenericArrayType) {
            return equals((GenericArrayType)t1, t2);
        }
        return t1 instanceof WildcardType && equals((WildcardType)t1, t2);
    }
    
    private static boolean equals(final ParameterizedType p, final Type t) {
        if (t instanceof ParameterizedType) {
            final ParameterizedType other = (ParameterizedType)t;
            if (equals(p.getRawType(), other.getRawType()) && equals(p.getOwnerType(), other.getOwnerType())) {
                return equals(p.getActualTypeArguments(), other.getActualTypeArguments());
            }
        }
        return false;
    }
    
    private static boolean equals(final GenericArrayType a, final Type t) {
        return t instanceof GenericArrayType && equals(a.getGenericComponentType(), ((GenericArrayType)t).getGenericComponentType());
    }
    
    private static boolean equals(final WildcardType w, final Type t) {
        if (t instanceof WildcardType) {
            final WildcardType other = (WildcardType)t;
            return equals(w.getLowerBounds(), other.getLowerBounds()) && equals(getImplicitUpperBounds(w), getImplicitUpperBounds(other));
        }
        return true;
    }
    
    private static boolean equals(final Type[] t1, final Type[] t2) {
        if (t1.length == t2.length) {
            for (int i = 0; i < t1.length; ++i) {
                if (!equals(t1[i], t2[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public static String toString(final Type type) {
        Validate.notNull(type);
        if (type instanceof Class) {
            return classToString((Class<?>)type);
        }
        if (type instanceof ParameterizedType) {
            return parameterizedTypeToString((ParameterizedType)type);
        }
        if (type instanceof WildcardType) {
            return wildcardTypeToString((WildcardType)type);
        }
        if (type instanceof TypeVariable) {
            return typeVariableToString((TypeVariable<?>)type);
        }
        if (type instanceof GenericArrayType) {
            return genericArrayTypeToString((GenericArrayType)type);
        }
        throw new IllegalArgumentException(ObjectUtils.identityToString(type));
    }
    
    public static String toLongString(final TypeVariable<?> var) {
        Validate.notNull(var, "var is null", new Object[0]);
        final StringBuilder buf = new StringBuilder();
        final GenericDeclaration d = (GenericDeclaration)var.getGenericDeclaration();
        if (d instanceof Class) {
            Class<?> c;
            for (c = (Class<?>)d; c.getEnclosingClass() != null; c = c.getEnclosingClass()) {
                buf.insert(0, c.getSimpleName()).insert(0, '.');
            }
            buf.insert(0, c.getName());
        }
        else if (d instanceof Type) {
            buf.append(toString((Type)d));
        }
        else {
            buf.append(d);
        }
        return buf.append(':').append(typeVariableToString(var)).toString();
    }
    
    public static <T> Typed<T> wrap(final Type type) {
        return new Typed<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }
    
    public static <T> Typed<T> wrap(final Class<T> type) {
        return wrap((Type)type);
    }
    
    private static String classToString(final Class<?> c) {
        final StringBuilder buf = new StringBuilder();
        if (c.getEnclosingClass() != null) {
            buf.append(classToString(c.getEnclosingClass())).append('.').append(c.getSimpleName());
        }
        else {
            buf.append(c.getName());
        }
        if (c.getTypeParameters().length > 0) {
            buf.append('<');
            appendAllTo(buf, ", ", (Type[])c.getTypeParameters());
            buf.append('>');
        }
        return buf.toString();
    }
    
    private static String typeVariableToString(final TypeVariable<?> v) {
        final StringBuilder buf = new StringBuilder(v.getName());
        final Type[] bounds = v.getBounds();
        if (bounds.length > 0 && (bounds.length != 1 || !Object.class.equals(bounds[0]))) {
            buf.append(" extends ");
            appendAllTo(buf, " & ", v.getBounds());
        }
        return buf.toString();
    }
    
    private static String parameterizedTypeToString(final ParameterizedType p) {
        final StringBuilder buf = new StringBuilder();
        final Type useOwner = p.getOwnerType();
        final Class<?> raw = (Class<?>)p.getRawType();
        final Type[] typeArguments = p.getActualTypeArguments();
        if (useOwner == null) {
            buf.append(raw.getName());
        }
        else {
            if (useOwner instanceof Class) {
                buf.append(((Class)useOwner).getName());
            }
            else {
                buf.append(useOwner.toString());
            }
            buf.append('.').append(raw.getSimpleName());
        }
        appendAllTo(buf.append('<'), ", ", typeArguments).append('>');
        return buf.toString();
    }
    
    private static String wildcardTypeToString(final WildcardType w) {
        final StringBuilder buf = new StringBuilder().append('?');
        final Type[] lowerBounds = w.getLowerBounds();
        final Type[] upperBounds = w.getUpperBounds();
        if (lowerBounds.length > 0) {
            appendAllTo(buf.append(" super "), " & ", lowerBounds);
        }
        else if (upperBounds.length != 1 || !Object.class.equals(upperBounds[0])) {
            appendAllTo(buf.append(" extends "), " & ", upperBounds);
        }
        return buf.toString();
    }
    
    private static String genericArrayTypeToString(final GenericArrayType g) {
        return String.format("%s[]", toString(g.getGenericComponentType()));
    }
    
    private static StringBuilder appendAllTo(final StringBuilder buf, final String sep, final Type... types) {
        Validate.notEmpty(Validate.noNullElements(types));
        if (types.length > 0) {
            buf.append(toString(types[0]));
            for (int i = 1; i < types.length; ++i) {
                buf.append(sep).append(toString(types[i]));
            }
        }
        return buf;
    }
    
    static {
        WILDCARD_ALL = wildcardType().withUpperBounds(Object.class).build();
    }
    
    public static class WildcardTypeBuilder implements Builder<WildcardType>
    {
        private Type[] upperBounds;
        private Type[] lowerBounds;
        
        private WildcardTypeBuilder() {
        }
        
        public WildcardTypeBuilder withUpperBounds(final Type... bounds) {
            this.upperBounds = bounds;
            return this;
        }
        
        public WildcardTypeBuilder withLowerBounds(final Type... bounds) {
            this.lowerBounds = bounds;
            return this;
        }
        
        @Override
        public WildcardType build() {
            return new WildcardTypeImpl(this.upperBounds, this.lowerBounds);
        }
    }
    
    private static final class GenericArrayTypeImpl implements GenericArrayType
    {
        private final Type componentType;
        
        private GenericArrayTypeImpl(final Type componentType) {
            this.componentType = componentType;
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj == this || (obj instanceof GenericArrayType && equals(this, (Type)obj));
        }
        
        @Override
        public int hashCode() {
            int result = 1072;
            result |= this.componentType.hashCode();
            return result;
        }
    }
    
    private static final class ParameterizedTypeImpl implements ParameterizedType
    {
        private final Class<?> raw;
        private final Type useOwner;
        private final Type[] typeArguments;
        
        private ParameterizedTypeImpl(final Class<?> raw, final Type useOwner, final Type[] typeArguments) {
            this.raw = raw;
            this.useOwner = useOwner;
            this.typeArguments = typeArguments;
        }
        
        @Override
        public Type getRawType() {
            return this.raw;
        }
        
        @Override
        public Type getOwnerType() {
            return this.useOwner;
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return this.typeArguments.clone();
        }
        
        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj == this || (obj instanceof ParameterizedType && equals(this, (Type)obj));
        }
        
        @Override
        public int hashCode() {
            int result = 1136;
            result |= this.raw.hashCode();
            result <<= 4;
            result |= ObjectUtils.hashCode(this.useOwner);
            result <<= 8;
            result |= Arrays.hashCode(this.typeArguments);
            return result;
        }
    }
    
    private static final class WildcardTypeImpl implements WildcardType
    {
        private static final Type[] EMPTY_BOUNDS;
        private final Type[] upperBounds;
        private final Type[] lowerBounds;
        
        private WildcardTypeImpl(final Type[] upperBounds, final Type[] lowerBounds) {
            this.upperBounds = ObjectUtils.defaultIfNull(upperBounds, WildcardTypeImpl.EMPTY_BOUNDS);
            this.lowerBounds = ObjectUtils.defaultIfNull(lowerBounds, WildcardTypeImpl.EMPTY_BOUNDS);
        }
        
        @Override
        public Type[] getUpperBounds() {
            return this.upperBounds.clone();
        }
        
        @Override
        public Type[] getLowerBounds() {
            return this.lowerBounds.clone();
        }
        
        @Override
        public String toString() {
            return TypeUtils.toString(this);
        }
        
        @Override
        public boolean equals(final Object obj) {
            return obj == this || (obj instanceof WildcardType && equals(this, (Type)obj));
        }
        
        @Override
        public int hashCode() {
            int result = 18688;
            result |= Arrays.hashCode(this.upperBounds);
            result <<= 8;
            result |= Arrays.hashCode(this.lowerBounds);
            return result;
        }
        
        static {
            EMPTY_BOUNDS = new Type[0];
        }
    }
}
