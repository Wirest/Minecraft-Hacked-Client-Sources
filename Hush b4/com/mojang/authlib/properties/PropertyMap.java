// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.properties;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import java.util.Iterator;
import com.google.gson.JsonArray;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ForwardingMultimap;

public class PropertyMap extends ForwardingMultimap<String, Property>
{
    private final Multimap<String, Property> properties;
    
    public PropertyMap() {
        this.properties = (Multimap<String, Property>)LinkedHashMultimap.create();
    }
    
    @Override
    protected Multimap<String, Property> delegate() {
        return this.properties;
    }
    
    public static class Serializer implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap>
    {
        @Override
        public PropertyMap deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final PropertyMap result = new PropertyMap();
            if (json instanceof JsonObject) {
                final JsonObject object = (JsonObject)json;
                for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    if (entry.getValue() instanceof JsonArray) {
                        for (final JsonElement element : entry.getValue()) {
                            result.put(entry.getKey(), new Property(entry.getKey(), element.getAsString()));
                        }
                    }
                }
            }
            else if (json instanceof JsonArray) {
                for (final JsonElement element2 : (JsonArray)json) {
                    if (element2 instanceof JsonObject) {
                        final JsonObject object2 = (JsonObject)element2;
                        final String name = object2.getAsJsonPrimitive("name").getAsString();
                        final String value = object2.getAsJsonPrimitive("value").getAsString();
                        if (object2.has("signature")) {
                            result.put(name, new Property(name, value, object2.getAsJsonPrimitive("signature").getAsString()));
                        }
                        else {
                            result.put(name, new Property(name, value));
                        }
                    }
                }
            }
            return result;
        }
        
        @Override
        public JsonElement serialize(final PropertyMap src, final Type typeOfSrc, final JsonSerializationContext context) {
            final JsonArray result = new JsonArray();
            for (final Property property : ((ForwardingMultimap<K, Property>)src).values()) {
                final JsonObject object = new JsonObject();
                object.addProperty("name", property.getName());
                object.addProperty("value", property.getValue());
                if (property.hasSignature()) {
                    object.addProperty("signature", property.getSignature());
                }
                result.add(object);
            }
            return result;
        }
    }
}
