package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.vecmath.Vector3f;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;

public class BlockPart {
    public final Vector3f field_178241_a;
    public final Vector3f field_178239_b;
    public final Map field_178240_c;
    public final BlockPartRotation field_178237_d;
    public final boolean field_178238_e;
    private static final String __OBFID = "CL_00002511";

    public BlockPart(Vector3f p_i46231_1_, Vector3f p_i46231_2_, Map p_i46231_3_, BlockPartRotation p_i46231_4_, boolean p_i46231_5_) {
        this.field_178241_a = p_i46231_1_;
        this.field_178239_b = p_i46231_2_;
        this.field_178240_c = p_i46231_3_;
        this.field_178237_d = p_i46231_4_;
        this.field_178238_e = p_i46231_5_;
        this.func_178235_a();
    }

    private void func_178235_a() {
        Iterator var1 = this.field_178240_c.entrySet().iterator();

        while (var1.hasNext()) {
            Entry var2 = (Entry) var1.next();
            float[] var3 = this.func_178236_a((EnumFacing) var2.getKey());
            ((BlockPartFace) var2.getValue()).field_178243_e.func_178349_a(var3);
        }
    }

    private float[] func_178236_a(EnumFacing p_178236_1_) {
        float[] var2;

        switch (BlockPart.SwitchEnumFacing.field_178234_a[p_178236_1_.ordinal()]) {
            case 1:
            case 2:
                var2 = new float[]{this.field_178241_a.x, this.field_178241_a.z, this.field_178239_b.x, this.field_178239_b.z};
                break;
            case 3:
            case 4:
                var2 = new float[]{this.field_178241_a.x, 16.0F - this.field_178239_b.y, this.field_178239_b.x, 16.0F - this.field_178241_a.y};
                break;
            case 5:
            case 6:
                var2 = new float[]{this.field_178241_a.z, 16.0F - this.field_178239_b.y, this.field_178239_b.z, 16.0F - this.field_178241_a.y};
                break;
            default:
                throw new NullPointerException();
        }

        return var2;
    }

    static class Deserializer implements JsonDeserializer {
        private static final String __OBFID = "CL_00002509";

        public BlockPart func_178254_a(JsonElement p_178254_1_, Type p_178254_2_, JsonDeserializationContext p_178254_3_) {
            JsonObject var4 = p_178254_1_.getAsJsonObject();
            Vector3f var5 = this.func_178249_e(var4);
            Vector3f var6 = this.func_178247_d(var4);
            BlockPartRotation var7 = this.func_178256_a(var4);
            Map var8 = this.func_178250_a(p_178254_3_, var4);

            if (var4.has("shade") && !JsonUtils.func_180199_c(var4, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            } else {
                boolean var9 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "shade", true);
                return new BlockPart(var5, var6, var8, var7, var9);
            }
        }

        private BlockPartRotation func_178256_a(JsonObject p_178256_1_) {
            BlockPartRotation var2 = null;

            if (p_178256_1_.has("rotation")) {
                JsonObject var3 = JsonUtils.getJsonObject(p_178256_1_, "rotation");
                Vector3f var4 = this.func_178251_a(var3, "origin");
                var4.scale(0.0625F);
                EnumFacing.Axis var5 = this.func_178252_c(var3);
                float var6 = this.func_178255_b(var3);
                boolean var7 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var3, "rescale", false);
                var2 = new BlockPartRotation(var4, var5, var6, var7);
            }

            return var2;
        }

        private float func_178255_b(JsonObject p_178255_1_) {
            float var2 = JsonUtils.getJsonObjectFloatFieldValue(p_178255_1_, "angle");

            if (var2 != 0.0F && MathHelper.abs(var2) != 22.5F && MathHelper.abs(var2) != 45.0F) {
                throw new JsonParseException("Invalid rotation " + var2 + " found, only -45/-22.5/0/22.5/45 allowed");
            } else {
                return var2;
            }
        }

        private EnumFacing.Axis func_178252_c(JsonObject p_178252_1_) {
            String var2 = JsonUtils.getJsonObjectStringFieldValue(p_178252_1_, "axis");
            EnumFacing.Axis var3 = EnumFacing.Axis.byName(var2.toLowerCase());

            if (var3 == null) {
                throw new JsonParseException("Invalid rotation axis: " + var2);
            } else {
                return var3;
            }
        }

        private Map func_178250_a(JsonDeserializationContext p_178250_1_, JsonObject p_178250_2_) {
            Map var3 = this.func_178253_b(p_178250_1_, p_178250_2_);

            if (var3.isEmpty()) {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            } else {
                return var3;
            }
        }

        private Map func_178253_b(JsonDeserializationContext p_178253_1_, JsonObject p_178253_2_) {
            EnumMap var3 = Maps.newEnumMap(EnumFacing.class);
            JsonObject var4 = JsonUtils.getJsonObject(p_178253_2_, "faces");
            Iterator var5 = var4.entrySet().iterator();

            while (var5.hasNext()) {
                Entry var6 = (Entry) var5.next();
                EnumFacing var7 = this.func_178248_a((String) var6.getKey());
                var3.put(var7, (BlockPartFace) p_178253_1_.deserialize((JsonElement) var6.getValue(), BlockPartFace.class));
            }

            return var3;
        }

        private EnumFacing func_178248_a(String p_178248_1_) {
            EnumFacing var2 = EnumFacing.byName(p_178248_1_);

            if (var2 == null) {
                throw new JsonParseException("Unknown facing: " + p_178248_1_);
            } else {
                return var2;
            }
        }

        private Vector3f func_178247_d(JsonObject p_178247_1_) {
            Vector3f var2 = this.func_178251_a(p_178247_1_, "to");

            if (var2.x >= -16.0F && var2.y >= -16.0F && var2.z >= -16.0F && var2.x <= 32.0F && var2.y <= 32.0F && var2.z <= 32.0F) {
                return var2;
            } else {
                throw new JsonParseException("\'to\' specifier exceeds the allowed boundaries: " + var2);
            }
        }

        private Vector3f func_178249_e(JsonObject p_178249_1_) {
            Vector3f var2 = this.func_178251_a(p_178249_1_, "from");

            if (var2.x >= -16.0F && var2.y >= -16.0F && var2.z >= -16.0F && var2.x <= 32.0F && var2.y <= 32.0F && var2.z <= 32.0F) {
                return var2;
            } else {
                throw new JsonParseException("\'from\' specifier exceeds the allowed boundaries: " + var2);
            }
        }

        private Vector3f func_178251_a(JsonObject p_178251_1_, String p_178251_2_) {
            JsonArray var3 = JsonUtils.getJsonObjectJsonArrayField(p_178251_1_, p_178251_2_);

            if (var3.size() != 3) {
                throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + var3.size());
            } else {
                float[] var4 = new float[3];

                for (int var5 = 0; var5 < var4.length; ++var5) {
                    var4[var5] = JsonUtils.getJsonElementFloatValue(var3.get(var5), p_178251_2_ + "[" + var5 + "]");
                }

                return new Vector3f(var4);
            }
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) {
            return this.func_178254_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }

    static final class SwitchEnumFacing {
        static final int[] field_178234_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002510";

        static {
            try {
                field_178234_a[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
                ;
            }

            try {
                field_178234_a[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_178234_a[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_178234_a[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_178234_a[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_178234_a[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
