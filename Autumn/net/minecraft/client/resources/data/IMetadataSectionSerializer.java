package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializer;

public interface IMetadataSectionSerializer extends JsonDeserializer {
   String getSectionName();
}
