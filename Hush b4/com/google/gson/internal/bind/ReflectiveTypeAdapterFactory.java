// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal.bind;

import java.util.Iterator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.$Gson$Types;
import java.util.LinkedHashMap;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import com.google.gson.internal.Primitives;
import java.util.Map;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import com.google.gson.internal.Excluder;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.TypeAdapterFactory;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    
    public ReflectiveTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final FieldNamingStrategy fieldNamingPolicy, final Excluder excluder) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
    }
    
    public boolean excludeField(final Field f, final boolean serialize) {
        return !this.excluder.excludeClass(f.getType(), serialize) && !this.excluder.excludeField(f, serialize);
    }
    
    private String getFieldName(final Field f) {
        final SerializedName serializedName = f.getAnnotation(SerializedName.class);
        return (serializedName == null) ? this.fieldNamingPolicy.translateName(f) : serializedName.value();
    }
    
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        final ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
        return new Adapter<T>((ObjectConstructor)constructor, (Map)this.getBoundFields(gson, type, raw));
    }
    
    private BoundField createBoundField(final Gson context, final Field field, final String name, final TypeToken<?> fieldType, final boolean serialize, final boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        return new BoundField(name, serialize, deserialize) {
            final TypeAdapter<?> typeAdapter = context.getAdapter(fieldType);
            
            @Override
            void write(final JsonWriter writer, final Object value) throws IOException, IllegalAccessException {
                final Object fieldValue = field.get(value);
                final TypeAdapter t = new TypeAdapterRuntimeTypeWrapper(context, this.typeAdapter, fieldType.getType());
                t.write(writer, fieldValue);
            }
            
            @Override
            void read(final JsonReader reader, final Object value) throws IOException, IllegalAccessException {
                final Object fieldValue = this.typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    field.set(value, fieldValue);
                }
            }
        };
    }
    
    private Map<String, BoundField> getBoundFields(final Gson context, TypeToken<?> type, Class<?> raw) {
        final Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
        if (raw.isInterface()) {
            return result;
        }
        final Type declaredType = type.getType();
        while (raw != Object.class) {
            final Field[] arr$;
            final Field[] fields = arr$ = raw.getDeclaredFields();
            for (final Field field : arr$) {
                final boolean serialize = this.excludeField(field, true);
                final boolean deserialize = this.excludeField(field, false);
                if (serialize || deserialize) {
                    field.setAccessible(true);
                    final Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
                    final BoundField boundField = this.createBoundField(context, field, this.getFieldName(field), TypeToken.get(fieldType), serialize, deserialize);
                    final BoundField previous = result.put(boundField.name, boundField);
                    if (previous != null) {
                        throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
                    }
                }
            }
            type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return result;
    }
    
    abstract static class BoundField
    {
        final String name;
        final boolean serialized;
        final boolean deserialized;
        
        protected BoundField(final String name, final boolean serialized, final boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
        
        abstract void write(final JsonWriter p0, final Object p1) throws IOException, IllegalAccessException;
        
        abstract void read(final JsonReader p0, final Object p1) throws IOException, IllegalAccessException;
    }
    
    public static final class Adapter<T> extends TypeAdapter<T>
    {
        private final ObjectConstructor<T> constructor;
        private final Map<String, BoundField> boundFields;
        
        private Adapter(final ObjectConstructor<T> constructor, final Map<String, BoundField> boundFields) {
            this.constructor = constructor;
            this.boundFields = boundFields;
        }
        
        @Override
        public T read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            final T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    final String name = in.nextName();
                    final BoundField field = this.boundFields.get(name);
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    }
                    else {
                        field.read(in, instance);
                    }
                }
            }
            catch (IllegalStateException e) {
                throw new JsonSyntaxException(e);
            }
            catch (IllegalAccessException e2) {
                throw new AssertionError((Object)e2);
            }
            in.endObject();
            return instance;
        }
        
        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (final BoundField boundField : this.boundFields.values()) {
                    if (boundField.serialized) {
                        out.name(boundField.name);
                        boundField.write(out, value);
                    }
                }
            }
            catch (IllegalAccessException e) {
                throw new AssertionError();
            }
            out.endObject();
        }
    }
}
