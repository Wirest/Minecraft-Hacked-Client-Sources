// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;

public class BlockFaceUV
{
    public float[] uvs;
    public final int rotation;
    
    public BlockFaceUV(final float[] uvsIn, final int rotationIn) {
        this.uvs = uvsIn;
        this.rotation = rotationIn;
    }
    
    public float func_178348_a(final int p_178348_1_) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        final int i = this.func_178347_d(p_178348_1_);
        return (i != 0 && i != 1) ? this.uvs[2] : this.uvs[0];
    }
    
    public float func_178346_b(final int p_178346_1_) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        }
        final int i = this.func_178347_d(p_178346_1_);
        return (i != 0 && i != 3) ? this.uvs[3] : this.uvs[1];
    }
    
    private int func_178347_d(final int p_178347_1_) {
        return (p_178347_1_ + this.rotation / 90) % 4;
    }
    
    public int func_178345_c(final int p_178345_1_) {
        return (p_178345_1_ + (4 - this.rotation / 90)) % 4;
    }
    
    public void setUvs(final float[] uvsIn) {
        if (this.uvs == null) {
            this.uvs = uvsIn;
        }
    }
    
    static class Deserializer implements JsonDeserializer<BlockFaceUV>
    {
        @Override
        public BlockFaceUV deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final float[] afloat = this.parseUV(jsonobject);
            final int i = this.parseRotation(jsonobject);
            return new BlockFaceUV(afloat, i);
        }
        
        protected int parseRotation(final JsonObject p_178291_1_) {
            final int i = JsonUtils.getInt(p_178291_1_, "rotation", 0);
            if (i >= 0 && i % 90 == 0 && i / 90 <= 3) {
                return i;
            }
            throw new JsonParseException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");
        }
        
        private float[] parseUV(final JsonObject p_178292_1_) {
            if (!p_178292_1_.has("uv")) {
                return null;
            }
            final JsonArray jsonarray = JsonUtils.getJsonArray(p_178292_1_, "uv");
            if (jsonarray.size() != 4) {
                throw new JsonParseException("Expected 4 uv values, found: " + jsonarray.size());
            }
            final float[] afloat = new float[4];
            for (int i = 0; i < afloat.length; ++i) {
                afloat[i] = JsonUtils.getFloat(jsonarray.get(i), "uv[" + i + "]");
            }
            return afloat;
        }
    }
}
