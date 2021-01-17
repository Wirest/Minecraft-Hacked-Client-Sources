package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.vecmath.Vector3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;

public class ItemTransformVec3f
{
    public static final ItemTransformVec3f field_178366_a = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));
    public final Vector3f field_178364_b;
    public final Vector3f field_178365_c;
    public final Vector3f field_178363_d;
    private static final String __OBFID = "CL_00002484";

    public ItemTransformVec3f(Vector3f p_i46214_1_, Vector3f p_i46214_2_, Vector3f p_i46214_3_)
    {
        this.field_178364_b = new Vector3f(p_i46214_1_);
        this.field_178365_c = new Vector3f(p_i46214_2_);
        this.field_178363_d = new Vector3f(p_i46214_3_);
    }

    static class Deserializer implements JsonDeserializer
    {
        private static final Vector3f field_178362_a = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f field_178360_b = new Vector3f(0.0F, 0.0F, 0.0F);
        private static final Vector3f field_178361_c = new Vector3f(1.0F, 1.0F, 1.0F);
        private static final String __OBFID = "CL_00002483";

        public ItemTransformVec3f func_178359_a(JsonElement p_178359_1_, Type p_178359_2_, JsonDeserializationContext p_178359_3_)
        {
            JsonObject var4 = p_178359_1_.getAsJsonObject();
            Vector3f var5 = this.func_178358_a(var4, "rotation", field_178362_a);
            Vector3f var6 = this.func_178358_a(var4, "translation", field_178360_b);
            var6.scale(0.0625F);
            MathHelper.clamp_double((double)var6.x, -1.5D, 1.5D);
            MathHelper.clamp_double((double)var6.y, -1.5D, 1.5D);
            MathHelper.clamp_double((double)var6.z, -1.5D, 1.5D);
            Vector3f var7 = this.func_178358_a(var4, "scale", field_178361_c);
            MathHelper.clamp_double((double)var7.x, -1.5D, 1.5D);
            MathHelper.clamp_double((double)var7.y, -1.5D, 1.5D);
            MathHelper.clamp_double((double)var7.z, -1.5D, 1.5D);
            return new ItemTransformVec3f(var5, var6, var7);
        }

        private Vector3f func_178358_a(JsonObject p_178358_1_, String p_178358_2_, Vector3f p_178358_3_)
        {
            if (!p_178358_1_.has(p_178358_2_))
            {
                return p_178358_3_;
            }
            else
            {
                JsonArray var4 = JsonUtils.getJsonObjectJsonArrayField(p_178358_1_, p_178358_2_);

                if (var4.size() != 3)
                {
                    throw new JsonParseException("Expected 3 " + p_178358_2_ + " values, found: " + var4.size());
                }
                else
                {
                    float[] var5 = new float[3];

                    for (int var6 = 0; var6 < var5.length; ++var6)
                    {
                        var5[var6] = JsonUtils.getJsonElementFloatValue(var4.get(var6), p_178358_2_ + "[" + var6 + "]");
                    }

                    return new Vector3f(var5);
                }
            }
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.func_178359_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
}
