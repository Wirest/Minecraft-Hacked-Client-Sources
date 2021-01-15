package net.minecraft.client.renderer.block.model;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ItemCameraTransforms
{
    public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    public final ItemTransformVec3f thirdPerson;
    public final ItemTransformVec3f firstPerson;
    public final ItemTransformVec3f head;
    public final ItemTransformVec3f gui;

    public ItemCameraTransforms(ItemTransformVec3f thirdPersonIn, ItemTransformVec3f firstPersonIn, ItemTransformVec3f headIn, ItemTransformVec3f guiIn)
    {
        this.thirdPerson = thirdPersonIn;
        this.firstPerson = firstPersonIn;
        this.head = headIn;
        this.gui = guiIn;
    }

    static class Deserializer implements JsonDeserializer
    {

        public ItemCameraTransforms parseItemCameraTransforms(JsonElement p_178352_1_, Type p_178352_2_, JsonDeserializationContext p_178352_3_)
        {
            JsonObject var4 = p_178352_1_.getAsJsonObject();
            ItemTransformVec3f var5 = ItemTransformVec3f.DEFAULT;
            ItemTransformVec3f var6 = ItemTransformVec3f.DEFAULT;
            ItemTransformVec3f var7 = ItemTransformVec3f.DEFAULT;
            ItemTransformVec3f var8 = ItemTransformVec3f.DEFAULT;

            if (var4.has("thirdperson"))
            {
                var5 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("thirdperson"), ItemTransformVec3f.class);
            }

            if (var4.has("firstperson"))
            {
                var6 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("firstperson"), ItemTransformVec3f.class);
            }

            if (var4.has("head"))
            {
                var7 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("head"), ItemTransformVec3f.class);
            }

            if (var4.has("gui"))
            {
                var8 = (ItemTransformVec3f)p_178352_3_.deserialize(var4.get("gui"), ItemTransformVec3f.class);
            }

            return new ItemCameraTransforms(var5, var6, var7, var8);
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.parseItemCameraTransforms(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }

    public static enum TransformType
    {
        NONE("NONE", 0),
        THIRD_PERSON("THIRD_PERSON", 1),
        FIRST_PERSON("FIRST_PERSON", 2),
        HEAD("HEAD", 3),
        GUI("GUI", 4); 

        private TransformType(String p_i46212_1_, int p_i46212_2_) {}
    }
}
