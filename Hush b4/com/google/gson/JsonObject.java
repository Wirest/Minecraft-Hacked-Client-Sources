// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import com.google.gson.internal.LinkedTreeMap;

public final class JsonObject extends JsonElement
{
    private final LinkedTreeMap<String, JsonElement> members;
    
    public JsonObject() {
        this.members = new LinkedTreeMap<String, JsonElement>();
    }
    
    @Override
    JsonObject deepCopy() {
        final JsonObject result = new JsonObject();
        for (final Map.Entry<String, JsonElement> entry : this.members.entrySet()) {
            result.add(entry.getKey(), entry.getValue().deepCopy());
        }
        return result;
    }
    
    public void add(final String property, JsonElement value) {
        if (value == null) {
            value = JsonNull.INSTANCE;
        }
        this.members.put(property, value);
    }
    
    public JsonElement remove(final String property) {
        return this.members.remove(property);
    }
    
    public void addProperty(final String property, final String value) {
        this.add(property, this.createJsonElement(value));
    }
    
    public void addProperty(final String property, final Number value) {
        this.add(property, this.createJsonElement(value));
    }
    
    public void addProperty(final String property, final Boolean value) {
        this.add(property, this.createJsonElement(value));
    }
    
    public void addProperty(final String property, final Character value) {
        this.add(property, this.createJsonElement(value));
    }
    
    private JsonElement createJsonElement(final Object value) {
        return (value == null) ? JsonNull.INSTANCE : new JsonPrimitive(value);
    }
    
    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return this.members.entrySet();
    }
    
    public boolean has(final String memberName) {
        return this.members.containsKey(memberName);
    }
    
    public JsonElement get(final String memberName) {
        return this.members.get(memberName);
    }
    
    public JsonPrimitive getAsJsonPrimitive(final String memberName) {
        return this.members.get(memberName);
    }
    
    public JsonArray getAsJsonArray(final String memberName) {
        return this.members.get(memberName);
    }
    
    public JsonObject getAsJsonObject(final String memberName) {
        return this.members.get(memberName);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof JsonObject && ((JsonObject)o).members.equals(this.members));
    }
    
    @Override
    public int hashCode() {
        return this.members.hashCode();
    }
}
