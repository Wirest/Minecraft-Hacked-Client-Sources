// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal.bind;

import java.util.Map;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import com.google.gson.JsonObject;
import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonToken;
import java.util.ArrayList;
import com.google.gson.JsonElement;
import java.util.List;
import java.io.Reader;
import com.google.gson.stream.JsonReader;

public final class JsonTreeReader extends JsonReader
{
    private static final Reader UNREADABLE_READER;
    private static final Object SENTINEL_CLOSED;
    private final List<Object> stack;
    
    public JsonTreeReader(final JsonElement element) {
        super(JsonTreeReader.UNREADABLE_READER);
        (this.stack = new ArrayList<Object>()).add(element);
    }
    
    @Override
    public void beginArray() throws IOException {
        this.expect(JsonToken.BEGIN_ARRAY);
        final JsonArray array = (JsonArray)this.peekStack();
        this.stack.add(array.iterator());
    }
    
    @Override
    public void endArray() throws IOException {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
    }
    
    @Override
    public void beginObject() throws IOException {
        this.expect(JsonToken.BEGIN_OBJECT);
        final JsonObject object = (JsonObject)this.peekStack();
        this.stack.add(object.entrySet().iterator());
    }
    
    @Override
    public void endObject() throws IOException {
        this.expect(JsonToken.END_OBJECT);
        this.popStack();
        this.popStack();
    }
    
    @Override
    public boolean hasNext() throws IOException {
        final JsonToken token = this.peek();
        return token != JsonToken.END_OBJECT && token != JsonToken.END_ARRAY;
    }
    
    @Override
    public JsonToken peek() throws IOException {
        if (this.stack.isEmpty()) {
            return JsonToken.END_DOCUMENT;
        }
        final Object o = this.peekStack();
        if (o instanceof Iterator) {
            final boolean isObject = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
            final Iterator<?> iterator = (Iterator<?>)o;
            if (!iterator.hasNext()) {
                return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            }
            if (isObject) {
                return JsonToken.NAME;
            }
            this.stack.add(iterator.next());
            return this.peek();
        }
        else {
            if (o instanceof JsonObject) {
                return JsonToken.BEGIN_OBJECT;
            }
            if (o instanceof JsonArray) {
                return JsonToken.BEGIN_ARRAY;
            }
            if (o instanceof JsonPrimitive) {
                final JsonPrimitive primitive = (JsonPrimitive)o;
                if (primitive.isString()) {
                    return JsonToken.STRING;
                }
                if (primitive.isBoolean()) {
                    return JsonToken.BOOLEAN;
                }
                if (primitive.isNumber()) {
                    return JsonToken.NUMBER;
                }
                throw new AssertionError();
            }
            else {
                if (o instanceof JsonNull) {
                    return JsonToken.NULL;
                }
                if (o == JsonTreeReader.SENTINEL_CLOSED) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                throw new AssertionError();
            }
        }
    }
    
    private Object peekStack() {
        return this.stack.get(this.stack.size() - 1);
    }
    
    private Object popStack() {
        return this.stack.remove(this.stack.size() - 1);
    }
    
    private void expect(final JsonToken expected) throws IOException {
        if (this.peek() != expected) {
            throw new IllegalStateException("Expected " + expected + " but was " + this.peek());
        }
    }
    
    @Override
    public String nextName() throws IOException {
        this.expect(JsonToken.NAME);
        final Iterator<?> i = (Iterator<?>)this.peekStack();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
        this.stack.add(entry.getValue());
        return (String)entry.getKey();
    }
    
    @Override
    public String nextString() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.STRING && token != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + token);
        }
        return ((JsonPrimitive)this.popStack()).getAsString();
    }
    
    @Override
    public boolean nextBoolean() throws IOException {
        this.expect(JsonToken.BOOLEAN);
        return ((JsonPrimitive)this.popStack()).getAsBoolean();
    }
    
    @Override
    public void nextNull() throws IOException {
        this.expect(JsonToken.NULL);
        this.popStack();
    }
    
    @Override
    public double nextDouble() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token);
        }
        final double result = ((JsonPrimitive)this.peekStack()).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(result) || Double.isInfinite(result))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + result);
        }
        this.popStack();
        return result;
    }
    
    @Override
    public long nextLong() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token);
        }
        final long result = ((JsonPrimitive)this.peekStack()).getAsLong();
        this.popStack();
        return result;
    }
    
    @Override
    public int nextInt() throws IOException {
        final JsonToken token = this.peek();
        if (token != JsonToken.NUMBER && token != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token);
        }
        final int result = ((JsonPrimitive)this.peekStack()).getAsInt();
        this.popStack();
        return result;
    }
    
    @Override
    public void close() throws IOException {
        this.stack.clear();
        this.stack.add(JsonTreeReader.SENTINEL_CLOSED);
    }
    
    @Override
    public void skipValue() throws IOException {
        if (this.peek() == JsonToken.NAME) {
            this.nextName();
        }
        else {
            this.popStack();
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    public void promoteNameToValue() throws IOException {
        this.expect(JsonToken.NAME);
        final Iterator<?> i = (Iterator<?>)this.peekStack();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
        this.stack.add(entry.getValue());
        this.stack.add(new JsonPrimitive((String)entry.getKey()));
    }
    
    static {
        UNREADABLE_READER = new Reader() {
            @Override
            public int read(final char[] buffer, final int offset, final int count) throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public void close() throws IOException {
                throw new AssertionError();
            }
        };
        SENTINEL_CLOSED = new Object();
    }
}
