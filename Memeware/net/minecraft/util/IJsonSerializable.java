package net.minecraft.util;

import com.google.gson.JsonElement;

public interface IJsonSerializable {
    void func_152753_a(JsonElement var1);

    /**
     * Gets the JsonElement that can be serialized.
     */
    JsonElement getSerializableElement();
}
