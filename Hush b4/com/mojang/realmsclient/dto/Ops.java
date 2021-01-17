// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import java.util.Iterator;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.HashSet;
import java.util.Set;

public class Ops
{
    public Set<String> ops;
    
    public Ops() {
        this.ops = new HashSet<String>();
    }
    
    public static Ops parse(final String json) {
        final Ops ops = new Ops();
        final JsonParser parser = new JsonParser();
        try {
            final JsonElement jsonElement = parser.parse(json);
            final JsonObject jsonObject = jsonElement.getAsJsonObject();
            final JsonElement opsArray = jsonObject.get("ops");
            if (opsArray.isJsonArray()) {
                for (final JsonElement jsonElement2 : opsArray.getAsJsonArray()) {
                    ops.ops.add(jsonElement2.getAsString());
                }
            }
        }
        catch (Exception ex) {}
        return ops;
    }
}
