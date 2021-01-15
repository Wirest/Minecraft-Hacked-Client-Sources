package net.minecraft.util;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class JsonSerializableSet extends ForwardingSet implements IJsonSerializable
{
    /** The set for this ForwardingSet to forward methods to. */
    private final Set underlyingSet = Sets.newHashSet();

    @Override
	public void func_152753_a(JsonElement p_152753_1_)
    {
        if (p_152753_1_.isJsonArray())
        {
            Iterator var2 = p_152753_1_.getAsJsonArray().iterator();

            while (var2.hasNext())
            {
                JsonElement var3 = (JsonElement)var2.next();
                this.add(var3.getAsString());
            }
        }
    }

    /**
     * Gets the JsonElement that can be serialized.
     */
    @Override
	public JsonElement getSerializableElement()
    {
        JsonArray var1 = new JsonArray();
        Iterator var2 = this.iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            var1.add(new JsonPrimitive(var3));
        }

        return var1;
    }

    @Override
	protected Set delegate()
    {
        return this.underlyingSet;
    }
}
