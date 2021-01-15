package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.resources.Language;
import net.minecraft.util.JsonUtils;

public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer
{
    private static final String __OBFID = "CL_00001111";

    public LanguageMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
    {
        JsonObject var4 = p_deserialize_1_.getAsJsonObject();
        HashSet var5 = Sets.newHashSet();
        Iterator var6 = var4.entrySet().iterator();
        String var8;
        String var10;
        String var11;
        boolean var12;

        do
        {
            if (!var6.hasNext())
            {
                return new LanguageMetadataSection(var5);
            }

            Entry var7 = (Entry)var6.next();
            var8 = (String)var7.getKey();
            JsonObject var9 = JsonUtils.getElementAsJsonObject((JsonElement)var7.getValue(), "language");
            var10 = JsonUtils.getJsonObjectStringFieldValue(var9, "region");
            var11 = JsonUtils.getJsonObjectStringFieldValue(var9, "name");
            var12 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var9, "bidirectional", false);

            if (var10.isEmpty())
            {
                throw new JsonParseException("Invalid language->\'" + var8 + "\'->region: empty value");
            }

            if (var11.isEmpty())
            {
                throw new JsonParseException("Invalid language->\'" + var8 + "\'->name: empty value");
            }
        }
        while (var5.add(new Language(var8, var10, var11, var12)));

        throw new JsonParseException("Duplicate language->\'" + var8 + "\' defined");
    }

    /**
     * The name of this section type as it appears in JSON.
     */
    public String getSectionName()
    {
        return "language";
    }

}
