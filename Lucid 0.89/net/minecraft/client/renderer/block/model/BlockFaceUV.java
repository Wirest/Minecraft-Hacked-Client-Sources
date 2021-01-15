package net.minecraft.client.renderer.block.model;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.JsonUtils;

public class BlockFaceUV
{
    public float[] uvs;
    public final int rotation;

    public BlockFaceUV(float[] uvsIn, int rotationIn)
    {
        this.uvs = uvsIn;
        this.rotation = rotationIn;
    }

    public float func_178348_a(int p_178348_1_)
    {
        if (this.uvs == null)
        {
            throw new NullPointerException("uvs");
        }
        else
        {
            int var2 = this.func_178347_d(p_178348_1_);
            return var2 != 0 && var2 != 1 ? this.uvs[2] : this.uvs[0];
        }
    }

    public float func_178346_b(int p_178346_1_)
    {
        if (this.uvs == null)
        {
            throw new NullPointerException("uvs");
        }
        else
        {
            int var2 = this.func_178347_d(p_178346_1_);
            return var2 != 0 && var2 != 3 ? this.uvs[3] : this.uvs[1];
        }
    }

    private int func_178347_d(int p_178347_1_)
    {
        return (p_178347_1_ + this.rotation / 90) % 4;
    }

    public int func_178345_c(int p_178345_1_)
    {
        return (p_178345_1_ + (4 - this.rotation / 90)) % 4;
    }

    public void setUvs(float[] uvsIn)
    {
        if (this.uvs == null)
        {
            this.uvs = uvsIn;
        }
    }

    static class Deserializer implements JsonDeserializer
    {

        public BlockFaceUV parseBlockFaceUV(JsonElement p_178293_1_, Type p_178293_2_, JsonDeserializationContext p_178293_3_)
        {
            JsonObject var4 = p_178293_1_.getAsJsonObject();
            float[] var5 = this.parseUV(var4);
            int var6 = this.parseRotation(var4);
            return new BlockFaceUV(var5, var6);
        }

        protected int parseRotation(JsonObject p_178291_1_)
        {
            int var2 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178291_1_, "rotation", 0);

            if (var2 >= 0 && var2 % 90 == 0 && var2 / 90 <= 3)
            {
                return var2;
            }
            else
            {
                throw new JsonParseException("Invalid rotation " + var2 + " found, only 0/90/180/270 allowed");
            }
        }

        private float[] parseUV(JsonObject p_178292_1_)
        {
            if (!p_178292_1_.has("uv"))
            {
                return null;
            }
            else
            {
                JsonArray var2 = JsonUtils.getJsonObjectJsonArrayField(p_178292_1_, "uv");

                if (var2.size() != 4)
                {
                    throw new JsonParseException("Expected 4 uv values, found: " + var2.size());
                }
                else
                {
                    float[] var3 = new float[4];

                    for (int var4 = 0; var4 < var3.length; ++var4)
                    {
                        var3[var4] = JsonUtils.getJsonElementFloatValue(var2.get(var4), "uv[" + var4 + "]");
                    }

                    return var3;
                }
            }
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.parseBlockFaceUV(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
}
