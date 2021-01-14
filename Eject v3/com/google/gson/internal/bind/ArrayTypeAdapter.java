package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

.Gson.Types;

public final class ArrayTypeAdapter<E>
        extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson paramAnonymousGson, TypeToken<T> paramAnonymousTypeToken) {
            Type localType1 = paramAnonymousTypeToken.getType();
            if ((!(localType1 instanceof GenericArrayType)) && ((!(localType1 instanceof Class)) || (!((Class) localType1).isArray()))) {
                return null;
            }
            Type localType2 = .Gson.Types.getArrayComponentType(localType1);
            TypeAdapter localTypeAdapter = paramAnonymousGson.getAdapter(TypeToken.get(localType2));
            return new ArrayTypeAdapter(paramAnonymousGson, localTypeAdapter,.Gson.Types.getRawType(localType2));
        }
    };
    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Gson paramGson, TypeAdapter<E> paramTypeAdapter, Class<E> paramClass) {
        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper(paramGson, paramTypeAdapter, paramClass);
        this.componentType = paramClass;
    }

    public Object read(JsonReader paramJsonReader)
            throws IOException {
        if (paramJsonReader.peek() == JsonToken.NULL) {
            paramJsonReader.nextNull();
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        paramJsonReader.beginArray();
        while (paramJsonReader.hasNext()) {
            localObject = this.componentTypeAdapter.read(paramJsonReader);
            localArrayList.add(localObject);
        }
        paramJsonReader.endArray();
        Object localObject = Array.newInstance(this.componentType, localArrayList.size());
        for (int i = 0; i < localArrayList.size(); i++) {
            Array.set(localObject, i, localArrayList.get(i));
        }
        return localObject;
    }

    public void write(JsonWriter paramJsonWriter, Object paramObject)
            throws IOException {
        if (paramObject == null) {
            paramJsonWriter.nullValue();
            return;
        }
        paramJsonWriter.beginArray();
        int i = 0;
        int j = Array.getLength(paramObject);
        while (i < j) {
            Object localObject = Array.get(paramObject, i);
            this.componentTypeAdapter.write(paramJsonWriter, localObject);
            i++;
        }
        paramJsonWriter.endArray();
    }
}




