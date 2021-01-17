// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal.bind;

import java.io.IOException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import java.util.ArrayList;
import com.google.gson.JsonElement;
import java.util.List;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import com.google.gson.stream.JsonWriter;

public final class JsonTreeWriter extends JsonWriter
{
    private static final Writer UNWRITABLE_WRITER;
    private static final JsonPrimitive SENTINEL_CLOSED;
    private final List<JsonElement> stack;
    private String pendingName;
    private JsonElement product;
    
    public JsonTreeWriter() {
        super(JsonTreeWriter.UNWRITABLE_WRITER);
        this.stack = new ArrayList<JsonElement>();
        this.product = JsonNull.INSTANCE;
    }
    
    public JsonElement get() {
        if (!this.stack.isEmpty()) {
            throw new IllegalStateException("Expected one JSON element but was " + this.stack);
        }
        return this.product;
    }
    
    private JsonElement peek() {
        return this.stack.get(this.stack.size() - 1);
    }
    
    private void put(final JsonElement value) {
        if (this.pendingName != null) {
            if (!value.isJsonNull() || this.getSerializeNulls()) {
                final JsonObject object = (JsonObject)this.peek();
                object.add(this.pendingName, value);
            }
            this.pendingName = null;
        }
        else if (this.stack.isEmpty()) {
            this.product = value;
        }
        else {
            final JsonElement element = this.peek();
            if (!(element instanceof JsonArray)) {
                throw new IllegalStateException();
            }
            ((JsonArray)element).add(value);
        }
    }
    
    @Override
    public JsonWriter beginArray() throws IOException {
        final JsonArray array = new JsonArray();
        this.put(array);
        this.stack.add(array);
        return this;
    }
    
    @Override
    public JsonWriter endArray() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        final JsonElement element = this.peek();
        if (element instanceof JsonArray) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public JsonWriter beginObject() throws IOException {
        final JsonObject object = new JsonObject();
        this.put(object);
        this.stack.add(object);
        return this;
    }
    
    @Override
    public JsonWriter endObject() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        final JsonElement element = this.peek();
        if (element instanceof JsonObject) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public JsonWriter name(final String name) throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        final JsonElement element = this.peek();
        if (element instanceof JsonObject) {
            this.pendingName = name;
            return this;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public JsonWriter value(final String value) throws IOException {
        if (value == null) {
            return this.nullValue();
        }
        this.put(new JsonPrimitive(value));
        return this;
    }
    
    @Override
    public JsonWriter nullValue() throws IOException {
        this.put(JsonNull.INSTANCE);
        return this;
    }
    
    @Override
    public JsonWriter value(final boolean value) throws IOException {
        this.put(new JsonPrimitive(value));
        return this;
    }
    
    @Override
    public JsonWriter value(final double value) throws IOException {
        if (!this.isLenient() && (Double.isNaN(value) || Double.isInfinite(value))) {
            throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
        }
        this.put(new JsonPrimitive(value));
        return this;
    }
    
    @Override
    public JsonWriter value(final long value) throws IOException {
        this.put(new JsonPrimitive(value));
        return this;
    }
    
    @Override
    public JsonWriter value(final Number value) throws IOException {
        if (value == null) {
            return this.nullValue();
        }
        if (!this.isLenient()) {
            final double d = value.doubleValue();
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
            }
        }
        this.put(new JsonPrimitive(value));
        return this;
    }
    
    @Override
    public void flush() throws IOException {
    }
    
    @Override
    public void close() throws IOException {
        if (!this.stack.isEmpty()) {
            throw new IOException("Incomplete document");
        }
        this.stack.add(JsonTreeWriter.SENTINEL_CLOSED);
    }
    
    static {
        UNWRITABLE_WRITER = new Writer() {
            @Override
            public void write(final char[] buffer, final int offset, final int counter) {
                throw new AssertionError();
            }
            
            @Override
            public void flush() throws IOException {
                throw new AssertionError();
            }
            
            @Override
            public void close() throws IOException {
                throw new AssertionError();
            }
        };
        SENTINEL_CLOSED = new JsonPrimitive("closed");
    }
}
