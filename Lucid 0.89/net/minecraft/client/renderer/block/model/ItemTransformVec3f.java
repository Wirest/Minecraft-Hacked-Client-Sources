package net.minecraft.client.renderer.block.model;

import java.lang.reflect.Type;

import javax.vecmath.Vector3f;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;

public class ItemTransformVec3f
{
    public static final ItemTransformVec3f DEFAULT = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));
    public final Vector3f rotation;
    public final Vector3f translation;
    public final Vector3f scale;

    public ItemTransformVec3f(Vector3f rotation, Vector3f translation, Vector3f scale)
    {
        this.rotation = new Vector3f(rotation);
        this.translation = new Vector3f(translation);
        this.scale = new Vector3f(scale);
    }

    static class Deserializer implements JsonDeserializer
    {
        private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);

        public ItemTransformVec3f parseItemTransformVec3f(JsonElement element, Type type, JsonDeserializationContext context)
        {
            JsonObject var4 = element.getAsJsonObject();
            Vector3f var5 = this.parseVector3f(var4, "rotation", ROTATION_DEFAULT);
            Vector3f var6 = this.parseVector3f(var4, "translation", TRANSLATION_DEFAULT);
            var6.scale(0.0625F);
            MathHelper.clamp_double(var6.x, -1.5D, 1.5D);
            MathHelper.clamp_double(var6.y, -1.5D, 1.5D);
            MathHelper.clamp_double(var6.z, -1.5D, 1.5D);
            Vector3f var7 = this.parseVector3f(var4, "scale", SCALE_DEFAULT);
            MathHelper.clamp_double(var7.x, -1.5D, 1.5D);
            MathHelper.clamp_double(var7.y, -1.5D, 1.5D);
            MathHelper.clamp_double(var7.z, -1.5D, 1.5D);
            return new ItemTransformVec3f(var5, var6, var7);
        }

        private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue)
        {
            if (!jsonObject.has(key))
            {
                return defaultValue;
            }
            else
            {
                JsonArray var4 = JsonUtils.getJsonObjectJsonArrayField(jsonObject, key);

                if (var4.size() != 3)
                {
                    throw new JsonParseException("Expected 3 " + key + " values, found: " + var4.size());
                }
                else
                {
                    float[] var5 = new float[3];

                    for (int var6 = 0; var6 < var5.length; ++var6)
                    {
                        var5[var6] = JsonUtils.getJsonElementFloatValue(var4.get(var6), key + "[" + var6 + "]");
                    }

                    return new Vector3f(var5);
                }
            }
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.parseItemTransformVec3f(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
}
