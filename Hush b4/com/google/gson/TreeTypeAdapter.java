// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.reflect.TypeToken;

final class TreeTypeAdapter<T> extends TypeAdapter<T>
{
    private final JsonSerializer<T> serializer;
    private final JsonDeserializer<T> deserializer;
    private final Gson gson;
    private final TypeToken<T> typeToken;
    private final TypeAdapterFactory skipPast;
    private TypeAdapter<T> delegate;
    
    private TreeTypeAdapter(final JsonSerializer<T> serializer, final JsonDeserializer<T> deserializer, final Gson gson, final TypeToken<T> typeToken, final TypeAdapterFactory skipPast) {
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.gson = gson;
        this.typeToken = typeToken;
        this.skipPast = skipPast;
    }
    
    @Override
    public T read(final JsonReader in) throws IOException {
        if (this.deserializer == null) {
            return this.delegate().read(in);
        }
        final JsonElement value = Streams.parse(in);
        if (value.isJsonNull()) {
            return null;
        }
        return this.deserializer.deserialize(value, this.typeToken.getType(), this.gson.deserializationContext);
    }
    
    @Override
    public void write(final JsonWriter out, final T value) throws IOException {
        if (this.serializer == null) {
            this.delegate().write(out, value);
            return;
        }
        if (value == null) {
            out.nullValue();
            return;
        }
        final JsonElement tree = this.serializer.serialize(value, this.typeToken.getType(), this.gson.serializationContext);
        Streams.write(tree, out);
    }
    
    private TypeAdapter<T> delegate() {
        final TypeAdapter<T> d = this.delegate;
        return (d != null) ? d : (this.delegate = this.gson.getDelegateAdapter(this.skipPast, this.typeToken));
    }
    
    public static TypeAdapterFactory newFactory(final TypeToken<?> exactType, final Object typeAdapter) {
        return new SingleTypeFactory(typeAdapter, (TypeToken)exactType, false, (Class)null);
    }
    
    public static TypeAdapterFactory newFactoryWithMatchRawType(final TypeToken<?> exactType, final Object typeAdapter) {
        final boolean matchRawType = exactType.getType() == exactType.getRawType();
        return new SingleTypeFactory(typeAdapter, (TypeToken)exactType, matchRawType, (Class)null);
    }
    
    public static TypeAdapterFactory newTypeHierarchyFactory(final Class<?> hierarchyType, final Object typeAdapter) {
        return new SingleTypeFactory(typeAdapter, (TypeToken)null, false, (Class)hierarchyType);
    }
    
    private static class SingleTypeFactory implements TypeAdapterFactory
    {
        private final TypeToken<?> exactType;
        private final boolean matchRawType;
        private final Class<?> hierarchyType;
        private final JsonSerializer<?> serializer;
        private final JsonDeserializer<?> deserializer;
        
        private SingleTypeFactory(final Object typeAdapter, final TypeToken<?> exactType, final boolean matchRawType, final Class<?> hierarchyType) {
            this.serializer = (JsonSerializer<?>)((typeAdapter instanceof JsonSerializer) ? ((JsonSerializer)typeAdapter) : null);
            this.deserializer = (JsonDeserializer<?>)((typeAdapter instanceof JsonDeserializer) ? ((JsonDeserializer)typeAdapter) : null);
            $Gson$Preconditions.checkArgument(this.serializer != null || this.deserializer != null);
            this.exactType = exactType;
            this.matchRawType = matchRawType;
            this.hierarchyType = hierarchyType;
        }
        
        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
            final boolean matches = (this.exactType != null) ? (this.exactType.equals(type) || (this.matchRawType && this.exactType.getType() == type.getRawType())) : this.hierarchyType.isAssignableFrom(type.getRawType());
            return matches ? new TreeTypeAdapter<T>(this.serializer, this.deserializer, gson, type, this, null) : null;
        }
    }
}
