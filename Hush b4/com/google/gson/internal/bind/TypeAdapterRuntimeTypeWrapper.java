// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal.bind;

import java.lang.reflect.TypeVariable;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T>
{
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;
    
    TypeAdapterRuntimeTypeWrapper(final Gson context, final TypeAdapter<T> delegate, final Type type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }
    
    @Override
    public T read(final JsonReader in) throws IOException {
        return this.delegate.read(in);
    }
    
    @Override
    public void write(final JsonWriter out, final T value) throws IOException {
        TypeAdapter chosen = this.delegate;
        final Type runtimeType = this.getRuntimeTypeIfMoreSpecific(this.type, value);
        if (runtimeType != this.type) {
            final TypeAdapter runtimeTypeAdapter = this.context.getAdapter(TypeToken.get(runtimeType));
            if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                chosen = runtimeTypeAdapter;
            }
            else if (!(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                chosen = this.delegate;
            }
            else {
                chosen = runtimeTypeAdapter;
            }
        }
        chosen.write(out, value);
    }
    
    private Type getRuntimeTypeIfMoreSpecific(Type type, final Object value) {
        if (value != null && (type == Object.class || type instanceof TypeVariable || type instanceof Class)) {
            type = value.getClass();
        }
        return type;
    }
}
