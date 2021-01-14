package com.google.gson.internal.bind;

import com.google.gson.*;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

.Gson.Types;

public final class MapTypeAdapterFactory
        implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final boolean complexMapKeySerialization;

    public MapTypeAdapterFactory(ConstructorConstructor paramConstructorConstructor, boolean paramBoolean) {
        this.constructorConstructor = paramConstructorConstructor;
        this.complexMapKeySerialization = paramBoolean;
    }

    public <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken) {
        Type localType = paramTypeToken.getType();
        Class localClass1 = paramTypeToken.getRawType();
        if (!Map.class.isAssignableFrom(localClass1)) {
            return null;
        }
        Class localClass2 = .Gson.Types.getRawType(localType);
        Type[] arrayOfType = .Gson.Types.getMapKeyAndValueTypes(localType, localClass2);
        TypeAdapter localTypeAdapter1 = getKeyAdapter(paramGson, arrayOfType[0]);
        TypeAdapter localTypeAdapter2 = paramGson.getAdapter(TypeToken.get(arrayOfType[1]));
        ObjectConstructor localObjectConstructor = this.constructorConstructor.get(paramTypeToken);
        Adapter localAdapter = new Adapter(paramGson, arrayOfType[0], localTypeAdapter1, arrayOfType[1], localTypeAdapter2, localObjectConstructor);
        return localAdapter;
    }

    private TypeAdapter<?> getKeyAdapter(Gson paramGson, Type paramType) {
        return (paramType == Boolean.TYPE) || (paramType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : paramGson.getAdapter(TypeToken.get(paramType));
    }

    private final class Adapter<K, V>
            extends TypeAdapter<Map<K, V>> {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;

        public Adapter(Type paramType1, TypeAdapter<K> paramTypeAdapter, Type paramType2, TypeAdapter<V> paramTypeAdapter1, ObjectConstructor<? extends Map<K, V>> paramObjectConstructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper(paramType1, paramType2, paramTypeAdapter);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper(paramType1, paramObjectConstructor, paramTypeAdapter1);
            ObjectConstructor localObjectConstructor;
            this.constructor = localObjectConstructor;
        }

        public Map<K, V> read(JsonReader paramJsonReader)
                throws IOException {
            JsonToken localJsonToken = paramJsonReader.peek();
            if (localJsonToken == JsonToken.NULL) {
                paramJsonReader.nextNull();
                return null;
            }
            Map localMap = (Map) this.constructor.construct();
            Object localObject1;
            Object localObject2;
            Object localObject3;
            if (localJsonToken == JsonToken.BEGIN_ARRAY) {
                paramJsonReader.beginArray();
                while (paramJsonReader.hasNext()) {
                    paramJsonReader.beginArray();
                    localObject1 = this.keyTypeAdapter.read(paramJsonReader);
                    localObject2 = this.valueTypeAdapter.read(paramJsonReader);
                    localObject3 = localMap.put(localObject1, localObject2);
                    if (localObject3 != null) {
                        throw new JsonSyntaxException("duplicate key: " + localObject1);
                    }
                    paramJsonReader.endArray();
                }
                paramJsonReader.endArray();
            } else {
                paramJsonReader.beginObject();
                while (paramJsonReader.hasNext()) {
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue(paramJsonReader);
                    localObject1 = this.keyTypeAdapter.read(paramJsonReader);
                    localObject2 = this.valueTypeAdapter.read(paramJsonReader);
                    localObject3 = localMap.put(localObject1, localObject2);
                    if (localObject3 != null) {
                        throw new JsonSyntaxException("duplicate key: " + localObject1);
                    }
                }
                paramJsonReader.endObject();
            }
            return localMap;
        }

        public void write(JsonWriter paramJsonWriter, Map<K, V> paramMap)
                throws IOException {
            if (paramMap == null) {
                paramJsonWriter.nullValue();
                return;
            }
            if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
                paramJsonWriter.beginObject();
                Iterator localIterator1 = paramMap.entrySet().iterator();
                while (localIterator1.hasNext()) {
                    localObject1 = (Map.Entry) localIterator1.next();
                    paramJsonWriter.name(String.valueOf(((Map.Entry) localObject1).getKey()));
                    this.valueTypeAdapter.write(paramJsonWriter, ((Map.Entry) localObject1).getValue());
                }
                paramJsonWriter.endObject();
                return;
            }
            int i = 0;
            Object localObject1 = new ArrayList(paramMap.size());
            ArrayList localArrayList = new ArrayList(paramMap.size());
            Iterator localIterator2 = paramMap.entrySet().iterator();
            Object localObject2;
            while (localIterator2.hasNext()) {
                localObject2 = (Map.Entry) localIterator2.next();
                JsonElement localJsonElement = this.keyTypeAdapter.toJsonTree(((Map.Entry) localObject2).getKey());
                ((List) localObject1).add(localJsonElement);
                localArrayList.add(((Map.Entry) localObject2).getValue());
                i ^= ((localJsonElement.isJsonArray()) || (localJsonElement.isJsonObject()) ? 1 : 0);
            }
            int j;
            if (i != 0) {
                paramJsonWriter.beginArray();
                for (j = 0; j < ((List) localObject1).size(); j++) {
                    paramJsonWriter.beginArray();
                    Streams.write((JsonElement) ((List) localObject1).get(j), paramJsonWriter);
                    this.valueTypeAdapter.write(paramJsonWriter, localArrayList.get(j));
                    paramJsonWriter.endArray();
                }
                paramJsonWriter.endArray();
            } else {
                paramJsonWriter.beginObject();
                for (j = 0; j < ((List) localObject1).size(); j++) {
                    localObject2 = (JsonElement) ((List) localObject1).get(j);
                    paramJsonWriter.name(keyToString((JsonElement) localObject2));
                    this.valueTypeAdapter.write(paramJsonWriter, localArrayList.get(j));
                }
                paramJsonWriter.endObject();
            }
        }

        private String keyToString(JsonElement paramJsonElement) {
            if (paramJsonElement.isJsonPrimitive()) {
                JsonPrimitive localJsonPrimitive = paramJsonElement.getAsJsonPrimitive();
                if (localJsonPrimitive.isNumber()) {
                    return String.valueOf(localJsonPrimitive.getAsNumber());
                }
                if (localJsonPrimitive.isBoolean()) {
                    return Boolean.toString(localJsonPrimitive.getAsBoolean());
                }
                if (localJsonPrimitive.isString()) {
                    return localJsonPrimitive.getAsString();
                }
                throw new AssertionError();
            }
            if (paramJsonElement.isJsonNull()) {
                return "null";
            }
            throw new AssertionError();
        }
    }
}




