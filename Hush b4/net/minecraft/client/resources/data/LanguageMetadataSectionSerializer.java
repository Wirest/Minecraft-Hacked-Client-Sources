// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import java.util.Iterator;
import java.util.Set;
import com.google.gson.JsonObject;
import java.util.Collection;
import net.minecraft.client.resources.Language;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import java.util.Map;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;

public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer<LanguageMetadataSection>
{
    @Override
    public LanguageMetadataSection deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
        final Set<Language> set = (Set<Language>)Sets.newHashSet();
        for (final Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
            final String s = entry.getKey();
            final JsonObject jsonobject2 = JsonUtils.getJsonObject(entry.getValue(), "language");
            final String s2 = JsonUtils.getString(jsonobject2, "region");
            final String s3 = JsonUtils.getString(jsonobject2, "name");
            final boolean flag = JsonUtils.getBoolean(jsonobject2, "bidirectional", false);
            if (s2.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + s + "'->region: empty value");
            }
            if (s3.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + s + "'->name: empty value");
            }
            if (!set.add(new Language(s, s2, s3, flag))) {
                throw new JsonParseException("Duplicate language->'" + s + "' defined");
            }
        }
        return new LanguageMetadataSection(set);
    }
    
    @Override
    public String getSectionName() {
        return "language";
    }
}
