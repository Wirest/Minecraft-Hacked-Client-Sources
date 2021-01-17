// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import com.google.gson.JsonArray;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.common.collect.Lists;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;

public class TextureMetadataSectionSerializer extends BaseMetadataSectionSerializer<TextureMetadataSection>
{
    @Override
    public TextureMetadataSection deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        final boolean flag = JsonUtils.getBoolean(jsonobject, "blur", false);
        final boolean flag2 = JsonUtils.getBoolean(jsonobject, "clamp", false);
        final List<Integer> list = (List<Integer>)Lists.newArrayList();
        if (jsonobject.has("mipmaps")) {
            try {
                final JsonArray jsonarray = jsonobject.getAsJsonArray("mipmaps");
                for (int i = 0; i < jsonarray.size(); ++i) {
                    final JsonElement jsonelement = jsonarray.get(i);
                    if (jsonelement.isJsonPrimitive()) {
                        try {
                            list.add(jsonelement.getAsInt());
                            continue;
                        }
                        catch (NumberFormatException numberformatexception) {
                            throw new JsonParseException("Invalid texture->mipmap->" + i + ": expected number, was " + jsonelement, numberformatexception);
                        }
                    }
                    if (jsonelement.isJsonObject()) {
                        throw new JsonParseException("Invalid texture->mipmap->" + i + ": expected number, was " + jsonelement);
                    }
                }
            }
            catch (ClassCastException classcastexception) {
                throw new JsonParseException("Invalid texture->mipmaps: expected array, was " + jsonobject.get("mipmaps"), classcastexception);
            }
        }
        return new TextureMetadataSection(flag, flag2, list);
    }
    
    @Override
    public String getSectionName() {
        return "texture";
    }
}
