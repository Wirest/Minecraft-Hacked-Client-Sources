// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.reflect;

import java.lang.reflect.TypeVariable;
import java.lang.reflect.GenericArrayType;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.ParameterizedType;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;

public class TypeToken<T>
{
    final Class<? super T> rawType;
    final Type type;
    final int hashCode;
    
    protected TypeToken() {
        this.type = getSuperclassTypeParameter(this.getClass());
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    TypeToken(final Type type) {
        this.type = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(type));
        this.rawType = (Class<? super T>)$Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }
    
    static Type getSuperclassTypeParameter(final Class<?> subclass) {
        final Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        final ParameterizedType parameterized = (ParameterizedType)superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
    
    public final Class<? super T> getRawType() {
        return this.rawType;
    }
    
    public final Type getType() {
        return this.type;
    }
    
    @Deprecated
    public boolean isAssignableFrom(final Class<?> cls) {
        return this.isAssignableFrom((Type)cls);
    }
    
    @Deprecated
    public boolean isAssignableFrom(final Type from) {
        if (from == null) {
            return false;
        }
        if (this.type.equals(from)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(from));
        }
        if (this.type instanceof ParameterizedType) {
            return isAssignableFrom(from, (ParameterizedType)this.type, new HashMap<String, Type>());
        }
        if (this.type instanceof GenericArrayType) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(from)) && isAssignableFrom(from, (GenericArrayType)this.type);
        }
        throw buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
    }
    
    @Deprecated
    public boolean isAssignableFrom(final TypeToken<?> token) {
        return this.isAssignableFrom(token.getType());
    }
    
    private static boolean isAssignableFrom(final Type from, final GenericArrayType to) {
        final Type toGenericComponentType = to.getGenericComponentType();
        if (toGenericComponentType instanceof ParameterizedType) {
            Type t = from;
            if (from instanceof GenericArrayType) {
                t = ((GenericArrayType)from).getGenericComponentType();
            }
            else if (from instanceof Class) {
                Class<?> classType;
                for (classType = (Class<?>)from; classType.isArray(); classType = classType.getComponentType()) {}
                t = classType;
            }
            return isAssignableFrom(t, (ParameterizedType)toGenericComponentType, new HashMap<String, Type>());
        }
        return true;
    }
    
    private static boolean isAssignableFrom(final Type from, final ParameterizedType to, final Map<String, Type> typeVarMap) {
        if (from == null) {
            return false;
        }
        if (to.equals(from)) {
            return true;
        }
        final Class<?> clazz = $Gson$Types.getRawType(from);
        ParameterizedType ptype = null;
        if (from instanceof ParameterizedType) {
            ptype = (ParameterizedType)from;
        }
        if (ptype != null) {
            final Type[] tArgs = ptype.getActualTypeArguments();
            final TypeVariable<?>[] tParams = clazz.getTypeParameters();
            for (int i = 0; i < tArgs.length; ++i) {
                Type arg = tArgs[i];
                final TypeVariable<?> var = tParams[i];
                while (arg instanceof TypeVariable) {
                    final TypeVariable<?> v = (TypeVariable<?>)arg;
                    arg = typeVarMap.get(v.getName());
                }
                typeVarMap.put(var.getName(), arg);
            }
            if (typeEquals(ptype, to, typeVarMap)) {
                return true;
            }
        }
        for (final Type itype : clazz.getGenericInterfaces()) {
            if (isAssignableFrom(itype, to, new HashMap<String, Type>(typeVarMap))) {
                return true;
            }
        }
        final Type sType = clazz.getGenericSuperclass();
        return isAssignableFrom(sType, to, new HashMap<String, Type>(typeVarMap));
    }
    
    private static boolean typeEquals(final ParameterizedType from, final ParameterizedType to, final Map<String, Type> typeVarMap) {
        if (from.getRawType().equals(to.getRawType())) {
            final Type[] fromArgs = from.getActualTypeArguments();
            final Type[] toArgs = to.getActualTypeArguments();
            for (int i = 0; i < fromArgs.length; ++i) {
                if (!matches(fromArgs[i], toArgs[i], typeVarMap)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private static AssertionError buildUnexpectedTypeError(final Type token, final Class<?>... expected) {
        final StringBuilder exceptionMessage = new StringBuilder("Unexpected type. Expected one of: ");
        for (final Class<?> clazz : expected) {
            exceptionMessage.append(clazz.getName()).append(", ");
        }
        exceptionMessage.append("but got: ").append(token.getClass().getName()).append(", for type token: ").append(token.toString()).append('.');
        return new AssertionError((Object)exceptionMessage.toString());
    }
    
    private static boolean matches(final Type from, final Type to, final Map<String, Type> typeMap) {
        return to.equals(from) || (from instanceof TypeVariable && to.equals(typeMap.get(((TypeVariable)from).getName())));
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)o).type);
    }
    
    @Override
    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }
    
    public static TypeToken<?> get(final Type type) {
        return new TypeToken<Object>(type);
    }
    
    public static <T> TypeToken<T> get(final Class<T> type) {
        return new TypeToken<T>(type);
    }
}
