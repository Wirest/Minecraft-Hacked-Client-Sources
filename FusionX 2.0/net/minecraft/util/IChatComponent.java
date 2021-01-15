package net.minecraft.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public interface IChatComponent extends Iterable
{
    IChatComponent setChatStyle(ChatStyle var1);

    ChatStyle getChatStyle();

    /**
     * Appends the given text to the end of this component.
     */
    IChatComponent appendText(String var1);

    /**
     * Appends the given component to the end of this one.
     */
    IChatComponent appendSibling(IChatComponent var1);

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    String getUnformattedTextForChat();

    /**
     * Gets the text of this component, without any special formatting codes added.  TODO: why is this two different
     * methods?
     */
    String getUnformattedText();

    /**
     * Gets the text of this component, with formatting codes added for rendering.
     */
    String getFormattedText();

    /**
     * Gets the sibling components of this one.
     */
    List getSiblings();

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    IChatComponent createCopy();

    public static class Serializer implements JsonDeserializer, JsonSerializer
    {
        private static final Gson GSON;
        private static final String __OBFID = "CL_00001263";

        public IChatComponent deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            if (p_deserialize_1_.isJsonPrimitive())
            {
                return new ChatComponentText(p_deserialize_1_.getAsString());
            }
            else if (!p_deserialize_1_.isJsonObject())
            {
                if (p_deserialize_1_.isJsonArray())
                {
                    JsonArray var11 = p_deserialize_1_.getAsJsonArray();
                    IChatComponent var12 = null;
                    Iterator var15 = var11.iterator();

                    while (var15.hasNext())
                    {
                        JsonElement var17 = (JsonElement)var15.next();
                        IChatComponent var18 = this.deserialize(var17, var17.getClass(), p_deserialize_3_);

                        if (var12 == null)
                        {
                            var12 = var18;
                        }
                        else
                        {
                            var12.appendSibling(var18);
                        }
                    }

                    return var12;
                }
                else
                {
                    throw new JsonParseException("Don\'t know how to turn " + p_deserialize_1_.toString() + " into a Component");
                }
            }
            else
            {
                JsonObject var4 = p_deserialize_1_.getAsJsonObject();
                Object var5;

                if (var4.has("text"))
                {
                    var5 = new ChatComponentText(var4.get("text").getAsString());
                }
                else if (var4.has("translate"))
                {
                    String var6 = var4.get("translate").getAsString();

                    if (var4.has("with"))
                    {
                        JsonArray var7 = var4.getAsJsonArray("with");
                        Object[] var8 = new Object[var7.size()];

                        for (int var9 = 0; var9 < var8.length; ++var9)
                        {
                            var8[var9] = this.deserialize(var7.get(var9), p_deserialize_2_, p_deserialize_3_);

                            if (var8[var9] instanceof ChatComponentText)
                            {
                                ChatComponentText var10 = (ChatComponentText)var8[var9];

                                if (var10.getChatStyle().isEmpty() && var10.getSiblings().isEmpty())
                                {
                                    var8[var9] = var10.getChatComponentText_TextValue();
                                }
                            }
                        }

                        var5 = new ChatComponentTranslation(var6, var8);
                    }
                    else
                    {
                        var5 = new ChatComponentTranslation(var6, new Object[0]);
                    }
                }
                else if (var4.has("score"))
                {
                    JsonObject var13 = var4.getAsJsonObject("score");

                    if (!var13.has("name") || !var13.has("objective"))
                    {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }

                    var5 = new ChatComponentScore(JsonUtils.getJsonObjectStringFieldValue(var13, "name"), JsonUtils.getJsonObjectStringFieldValue(var13, "objective"));

                    if (var13.has("value"))
                    {
                        ((ChatComponentScore)var5).func_179997_b(JsonUtils.getJsonObjectStringFieldValue(var13, "value"));
                    }
                }
                else
                {
                    if (!var4.has("selector"))
                    {
                        throw new JsonParseException("Don\'t know how to turn " + p_deserialize_1_.toString() + " into a Component");
                    }

                    var5 = new ChatComponentSelector(JsonUtils.getJsonObjectStringFieldValue(var4, "selector"));
                }

                if (var4.has("extra"))
                {
                    JsonArray var14 = var4.getAsJsonArray("extra");

                    if (var14.size() <= 0)
                    {
                        throw new JsonParseException("Unexpected empty array of components");
                    }

                    for (int var16 = 0; var16 < var14.size(); ++var16)
                    {
                        ((IChatComponent)var5).appendSibling(this.deserialize(var14.get(var16), p_deserialize_2_, p_deserialize_3_));
                    }
                }

                ((IChatComponent)var5).setChatStyle((ChatStyle)p_deserialize_3_.deserialize(p_deserialize_1_, ChatStyle.class));
                return (IChatComponent)var5;
            }
        }

        private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx)
        {
            JsonElement var4 = ctx.serialize(style);

            if (var4.isJsonObject())
            {
                JsonObject var5 = (JsonObject)var4;
                Iterator var6 = var5.entrySet().iterator();

                while (var6.hasNext())
                {
                    Entry var7 = (Entry)var6.next();
                    object.add((String)var7.getKey(), (JsonElement)var7.getValue());
                }
            }
        }

        public JsonElement serialize(IChatComponent p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            if (p_serialize_1_ instanceof ChatComponentText && p_serialize_1_.getChatStyle().isEmpty() && p_serialize_1_.getSiblings().isEmpty())
            {
                return new JsonPrimitive(((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
            }
            else
            {
                JsonObject var4 = new JsonObject();

                if (!p_serialize_1_.getChatStyle().isEmpty())
                {
                    this.serializeChatStyle(p_serialize_1_.getChatStyle(), var4, p_serialize_3_);
                }

                if (!p_serialize_1_.getSiblings().isEmpty())
                {
                    JsonArray var5 = new JsonArray();
                    Iterator var6 = p_serialize_1_.getSiblings().iterator();

                    while (var6.hasNext())
                    {
                        IChatComponent var7 = (IChatComponent)var6.next();
                        var5.add(this.serialize(var7, var7.getClass(), p_serialize_3_));
                    }

                    var4.add("extra", var5);
                }

                if (p_serialize_1_ instanceof ChatComponentText)
                {
                    var4.addProperty("text", ((ChatComponentText)p_serialize_1_).getChatComponentText_TextValue());
                }
                else if (p_serialize_1_ instanceof ChatComponentTranslation)
                {
                    ChatComponentTranslation var11 = (ChatComponentTranslation)p_serialize_1_;
                    var4.addProperty("translate", var11.getKey());

                    if (var11.getFormatArgs() != null && var11.getFormatArgs().length > 0)
                    {
                        JsonArray var14 = new JsonArray();
                        Object[] var16 = var11.getFormatArgs();
                        int var8 = var16.length;

                        for (int var9 = 0; var9 < var8; ++var9)
                        {
                            Object var10 = var16[var9];

                            if (var10 instanceof IChatComponent)
                            {
                                var14.add(this.serialize((IChatComponent)var10, var10.getClass(), p_serialize_3_));
                            }
                            else
                            {
                                var14.add(new JsonPrimitive(String.valueOf(var10)));
                            }
                        }

                        var4.add("with", var14);
                    }
                }
                else if (p_serialize_1_ instanceof ChatComponentScore)
                {
                    ChatComponentScore var12 = (ChatComponentScore)p_serialize_1_;
                    JsonObject var15 = new JsonObject();
                    var15.addProperty("name", var12.func_179995_g());
                    var15.addProperty("objective", var12.func_179994_h());
                    var15.addProperty("value", var12.getUnformattedTextForChat());
                    var4.add("score", var15);
                }
                else
                {
                    if (!(p_serialize_1_ instanceof ChatComponentSelector))
                    {
                        throw new IllegalArgumentException("Don\'t know how to serialize " + p_serialize_1_ + " as a Component");
                    }

                    ChatComponentSelector var13 = (ChatComponentSelector)p_serialize_1_;
                    var4.addProperty("selector", var13.func_179992_g());
                }

                return var4;
            }
        }

        public static String componentToJson(IChatComponent component)
        {
            return GSON.toJson(component);
        }

        public static IChatComponent jsonToComponent(String json)
        {
            return (IChatComponent)GSON.fromJson(json, IChatComponent.class);
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return this.serialize((IChatComponent)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }


        static
        {
            GsonBuilder var0 = new GsonBuilder();
            var0.registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer());
            var0.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
            var0.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
            GSON = var0.create();
        }
    }
}
