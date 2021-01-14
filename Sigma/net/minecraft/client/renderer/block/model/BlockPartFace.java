package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;

public class BlockPartFace {
    public static final EnumFacing field_178246_a = null;
    public final EnumFacing field_178244_b;
    public final int field_178245_c;
    public final String field_178242_d;
    public final BlockFaceUV field_178243_e;
    private static final String __OBFID = "CL_00002508";

    public BlockPartFace(EnumFacing p_i46230_1_, int p_i46230_2_, String p_i46230_3_, BlockFaceUV p_i46230_4_) {
        this.field_178244_b = p_i46230_1_;
        this.field_178245_c = p_i46230_2_;
        this.field_178242_d = p_i46230_3_;
        this.field_178243_e = p_i46230_4_;
    }

    static class Deserializer implements JsonDeserializer {
        private static final String __OBFID = "CL_00002507";

        public BlockPartFace func_178338_a(JsonElement p_178338_1_, Type p_178338_2_, JsonDeserializationContext p_178338_3_) {
            JsonObject var4 = p_178338_1_.getAsJsonObject();
            EnumFacing var5 = this.func_178339_c(var4);
            int var6 = this.func_178337_a(var4);
            String var7 = this.func_178340_b(var4);
            BlockFaceUV var8 = (BlockFaceUV) p_178338_3_.deserialize(var4, BlockFaceUV.class);
            return new BlockPartFace(var5, var6, var7, var8);
        }

        protected int func_178337_a(JsonObject p_178337_1_) {
            return JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178337_1_, "tintindex", -1);
        }

        private String func_178340_b(JsonObject p_178340_1_) {
            return JsonUtils.getJsonObjectStringFieldValue(p_178340_1_, "texture");
        }

        private EnumFacing func_178339_c(JsonObject p_178339_1_) {
            String var2 = JsonUtils.getJsonObjectStringFieldValueOrDefault(p_178339_1_, "cullface", "");
            return EnumFacing.byName(var2);
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            return this.func_178338_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
}
