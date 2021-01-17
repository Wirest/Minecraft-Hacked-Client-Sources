// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal.bind;

import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.Iterator;
import com.google.gson.internal.Streams;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import java.util.Map;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.TypeAdapterFactory;

public final class MapTypeAdapterFactory implements TypeAdapterFactory
{
    private final ConstructorConstructor constructorConstructor;
    private final boolean complexMapKeySerialization;
    
    public MapTypeAdapterFactory(final ConstructorConstructor constructorConstructor, final boolean complexMapKeySerialization) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = complexMapKeySerialization;
    }
    
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        if (!Map.class.isAssignableFrom(rawType)) {
            return null;
        }
        final Class<?> rawTypeOfSrc = $Gson$Types.getRawType(type);
        final Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
        final TypeAdapter<?> keyAdapter = this.getKeyAdapter(gson, keyAndValueTypes[0]);
        final TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
        final ObjectConstructor<T> constructor = this.constructorConstructor.get(typeToken);
        final TypeAdapter<T> result = (TypeAdapter<T>)new Adapter(gson, keyAndValueTypes[0], (TypeAdapter<Object>)keyAdapter, keyAndValueTypes[1], (TypeAdapter<Object>)valueAdapter, (ObjectConstructor<? extends Map<Object, Object>>)constructor);
        return result;
    }
    
    private TypeAdapter<?> getKeyAdapter(final Gson context, final Type keyType) {
        return (keyType == Boolean.TYPE || keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context.getAdapter(TypeToken.get(keyType));
    }
    
    private final class Adapter<K, V> extends TypeAdapter<Map<K, V>>
    {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        
        public Adapter(final Gson context, final Type keyType, final TypeAdapter<K> keyTypeAdapter, final Type valueType, final TypeAdapter<V> valueTypeAdapter, final ObjectConstructor<? extends Map<K, V>> constructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(context, keyTypeAdapter, keyType);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(context, valueTypeAdapter, valueType);
            this.constructor = constructor;
        }
        
        @Override
        public Map<K, V> read(final JsonReader in) throws IOException {
            final JsonToken peek = in.peek();
            if (peek == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            final Map<K, V> map = (Map<K, V>)this.constructor.construct();
            if (peek == JsonToken.BEGIN_ARRAY) {
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    final K key = this.keyTypeAdapter.read(in);
                    final V value = this.valueTypeAdapter.read(in);
                    final V replaced = map.put(key, value);
                    if (replaced != null) {
                        throw new JsonSyntaxException("duplicate key: " + key);
                    }
                    in.endArray();
                }
                in.endArray();
            }
            else {
                in.beginObject();
                while (in.hasNext()) {
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
                    final K key = this.keyTypeAdapter.read(in);
                    final V value = this.valueTypeAdapter.read(in);
                    final V replaced = map.put(key, value);
                    if (replaced != null) {
                        throw new JsonSyntaxException("duplicate key: " + key);
                    }
                }
                in.endObject();
            }
            return map;
        }
        
        @Override
        public void write(final JsonWriter out, final Map<K, V> map) throws IOException {
            if (map == null) {
                out.nullValue();
                return;
            }
            if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
                out.beginObject();
                for (final Map.Entry<K, V> entry : map.entrySet()) {
                    out.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(out, entry.getValue());
                }
                out.endObject();
                return;
            }
            boolean hasComplexKeys = false;
            final List<JsonElement> keys = new ArrayList<JsonElement>(map.size());
            final List<V> values = new ArrayList<V>(map.size());
            for (final Map.Entry<K, V> entry2 : map.entrySet()) {
                final JsonElement keyElement = this.keyTypeAdapter.toJsonTree(entry2.getKey());
                keys.add(keyElement);
                values.add(entry2.getValue());
                hasComplexKeys |= (keyElement.isJsonArray() || keyElement.isJsonObject());
            }
            if (hasComplexKeys) {
                out.beginArray();
                for (int i = 0; i < keys.size(); ++i) {
                    out.beginArray();
                    Streams.write(keys.get(i), out);
                    this.valueTypeAdapter.write(out, values.get(i));
                    out.endArray();
                }
                out.endArray();
            }
            else {
                out.beginObject();
                for (int i = 0; i < keys.size(); ++i) {
                    final JsonElement keyElement2 = keys.get(i);
                    out.name(this.keyToString(keyElement2));
                    this.valueTypeAdapter.write(out, values.get(i));
                }
                out.endObject();
            }
        }
        
        private String keyToString(final JsonElement keyElement) {
            if (keyElement.isJsonPrimitive()) {
                final JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    return String.valueOf(primitive.getAsNumber());
                }
                if (primitive.isBoolean()) {
                    return Boolean.toString(primitive.getAsBoolean());
                }
                if (primitive.isString()) {
                    return primitive.getAsString();
                }
                throw new AssertionError();
            }
            else {
                if (keyElement.isJsonNull()) {
                    return "null";
                }
                throw new AssertionError();
            }
        }
    }
}
