// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.util;

import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils
{
    public static String getStringOr(final String key, final JsonObject node, final String defaultValue) {
        final JsonElement element = node.get(key);
        if (element != null) {
            return element.isJsonNull() ? defaultValue : element.getAsString();
        }
        return defaultValue;
    }
    
    public static int getIntOr(final String key, final JsonObject node, final int defaultValue) {
        final JsonElement element = node.get(key);
        if (element != null) {
            return element.isJsonNull() ? defaultValue : element.getAsInt();
        }
        return defaultValue;
    }
    
    public static long getLongOr(final String key, final JsonObject node, final long defaultValue) {
        final JsonElement element = node.get(key);
        if (element != null) {
            return element.isJsonNull() ? defaultValue : element.getAsLong();
        }
        return defaultValue;
    }
    
    public static boolean getBooleanOr(final String key, final JsonObject node, final boolean defaultValue) {
        final JsonElement element = node.get(key);
        if (element != null) {
            return element.isJsonNull() ? defaultValue : element.getAsBoolean();
        }
        return defaultValue;
    }
    
    public static Date getDateOr(final String key, final JsonObject node) {
        final JsonElement element = node.get(key);
        if (element != null) {
            return new Date(Long.parseLong(element.getAsString()));
        }
        return new Date();
    }
}
