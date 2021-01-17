// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializer;

public interface IMetadataSectionSerializer<T extends IMetadataSection> extends JsonDeserializer<T>
{
    String getSectionName();
}
