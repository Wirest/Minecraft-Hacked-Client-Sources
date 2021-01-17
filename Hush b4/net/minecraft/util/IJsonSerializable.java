// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.gson.JsonElement;

public interface IJsonSerializable
{
    void fromJson(final JsonElement p0);
    
    JsonElement getSerializableElement();
}
