// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.Collection;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.collect.ForwardingSet;

public class JsonSerializableSet extends ForwardingSet<String> implements IJsonSerializable
{
    private final Set<String> underlyingSet;
    
    public JsonSerializableSet() {
        this.underlyingSet = (Set<String>)Sets.newHashSet();
    }
    
    @Override
    public void fromJson(final JsonElement json) {
        if (json.isJsonArray()) {
            for (final JsonElement jsonelement : json.getAsJsonArray()) {
                this.add(jsonelement.getAsString());
            }
        }
    }
    
    @Override
    public JsonElement getSerializableElement() {
        final JsonArray jsonarray = new JsonArray();
        for (final String s : this) {
            jsonarray.add(new JsonPrimitive(s));
        }
        return jsonarray;
    }
    
    @Override
    protected Set<String> delegate() {
        return this.underlyingSet;
    }
}
