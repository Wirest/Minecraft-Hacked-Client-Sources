package net.minecraft.client.renderer.block.model;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Vector3f;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;

public class BlockPart
{
    public final Vector3f positionFrom;
    public final Vector3f positionTo;
    public final Map mapFaces;
    public final BlockPartRotation partRotation;
    public final boolean shade;

    public BlockPart(Vector3f positionFromIn, Vector3f positionToIn, Map mapFacesIn, BlockPartRotation partRotationIn, boolean shadeIn)
    {
        this.positionFrom = positionFromIn;
        this.positionTo = positionToIn;
        this.mapFaces = mapFacesIn;
        this.partRotation = partRotationIn;
        this.shade = shadeIn;
        this.setDefaultUvs();
    }

    private void setDefaultUvs()
    {
        Iterator var1 = this.mapFaces.entrySet().iterator();

        while (var1.hasNext())
        {
            Entry var2 = (Entry)var1.next();
            float[] var3 = this.getFaceUvs((EnumFacing)var2.getKey());
            ((BlockPartFace)var2.getValue()).blockFaceUV.setUvs(var3);
        }
    }

    private float[] getFaceUvs(EnumFacing p_178236_1_)
    {
        float[] var2;

        switch (BlockPart.SwitchEnumFacing.field_178234_a[p_178236_1_.ordinal()])
        {
            case 1:
            case 2:
                var2 = new float[] {this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z};
                break;
            case 3:
            case 4:
                var2 = new float[] {this.positionFrom.x, 16.0F - this.positionTo.y, this.positionTo.x, 16.0F - this.positionFrom.y};
                break;
            case 5:
            case 6:
                var2 = new float[] {this.positionFrom.z, 16.0F - this.positionTo.y, this.positionTo.z, 16.0F - this.positionFrom.y};
                break;
            default:
                throw new NullPointerException();
        }

        return var2;
    }

    static class Deserializer implements JsonDeserializer
    {

        public BlockPart parseBlockPart(JsonElement p_178254_1_, Type p_178254_2_, JsonDeserializationContext p_178254_3_)
        {
            JsonObject var4 = p_178254_1_.getAsJsonObject();
            Vector3f var5 = this.parsePositionFrom(var4);
            Vector3f var6 = this.parsePositionTo(var4);
            BlockPartRotation var7 = this.parseRotation(var4);
            Map var8 = this.parseFacesCheck(p_178254_3_, var4);

            if (var4.has("shade") && !JsonUtils.isJsonObjectBooleanField(var4, "shade"))
            {
                throw new JsonParseException("Expected shade to be a Boolean");
            }
            else
            {
                boolean var9 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var4, "shade", true);
                return new BlockPart(var5, var6, var8, var7, var9);
            }
        }

        private BlockPartRotation parseRotation(JsonObject p_178256_1_)
        {
            BlockPartRotation var2 = null;

            if (p_178256_1_.has("rotation"))
            {
                JsonObject var3 = JsonUtils.getJsonObject(p_178256_1_, "rotation");
                Vector3f var4 = this.parsePosition(var3, "origin");
                var4.scale(0.0625F);
                EnumFacing.Axis var5 = this.parseAxis(var3);
                float var6 = this.parseAngle(var3);
                boolean var7 = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var3, "rescale", false);
                var2 = new BlockPartRotation(var4, var5, var6, var7);
            }

            return var2;
        }

        private float parseAngle(JsonObject p_178255_1_)
        {
            float var2 = JsonUtils.getJsonObjectFloatFieldValue(p_178255_1_, "angle");

            if (var2 != 0.0F && MathHelper.abs(var2) != 22.5F && MathHelper.abs(var2) != 45.0F)
            {
                throw new JsonParseException("Invalid rotation " + var2 + " found, only -45/-22.5/0/22.5/45 allowed");
            }
            else
            {
                return var2;
            }
        }

        private EnumFacing.Axis parseAxis(JsonObject p_178252_1_)
        {
            String var2 = JsonUtils.getJsonObjectStringFieldValue(p_178252_1_, "axis");
            EnumFacing.Axis var3 = EnumFacing.Axis.byName(var2.toLowerCase());

            if (var3 == null)
            {
                throw new JsonParseException("Invalid rotation axis: " + var2);
            }
            else
            {
                return var3;
            }
        }

        private Map parseFacesCheck(JsonDeserializationContext p_178250_1_, JsonObject p_178250_2_)
        {
            Map var3 = this.parseFaces(p_178250_1_, p_178250_2_);

            if (var3.isEmpty())
            {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            }
            else
            {
                return var3;
            }
        }

        private Map parseFaces(JsonDeserializationContext p_178253_1_, JsonObject p_178253_2_)
        {
            EnumMap var3 = Maps.newEnumMap(EnumFacing.class);
            JsonObject var4 = JsonUtils.getJsonObject(p_178253_2_, "faces");
            Iterator var5 = var4.entrySet().iterator();

            while (var5.hasNext())
            {
                Entry var6 = (Entry)var5.next();
                EnumFacing var7 = this.parseEnumFacing((String)var6.getKey());
                var3.put(var7, p_178253_1_.deserialize((JsonElement)var6.getValue(), BlockPartFace.class));
            }

            return var3;
        }

        private EnumFacing parseEnumFacing(String name)
        {
            EnumFacing var2 = EnumFacing.byName(name);

            if (var2 == null)
            {
                throw new JsonParseException("Unknown facing: " + name);
            }
            else
            {
                return var2;
            }
        }

        private Vector3f parsePositionTo(JsonObject p_178247_1_)
        {
            Vector3f var2 = this.parsePosition(p_178247_1_, "to");

            if (var2.x >= -16.0F && var2.y >= -16.0F && var2.z >= -16.0F && var2.x <= 32.0F && var2.y <= 32.0F && var2.z <= 32.0F)
            {
                return var2;
            }
            else
            {
                throw new JsonParseException("\'to\' specifier exceeds the allowed boundaries: " + var2);
            }
        }

        private Vector3f parsePositionFrom(JsonObject p_178249_1_)
        {
            Vector3f var2 = this.parsePosition(p_178249_1_, "from");

            if (var2.x >= -16.0F && var2.y >= -16.0F && var2.z >= -16.0F && var2.x <= 32.0F && var2.y <= 32.0F && var2.z <= 32.0F)
            {
                return var2;
            }
            else
            {
                throw new JsonParseException("\'from\' specifier exceeds the allowed boundaries: " + var2);
            }
        }

        private Vector3f parsePosition(JsonObject p_178251_1_, String p_178251_2_)
        {
            JsonArray var3 = JsonUtils.getJsonObjectJsonArrayField(p_178251_1_, p_178251_2_);

            if (var3.size() != 3)
            {
                throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + var3.size());
            }
            else
            {
                float[] var4 = new float[3];

                for (int var5 = 0; var5 < var4.length; ++var5)
                {
                    var4[var5] = JsonUtils.getJsonElementFloatValue(var3.get(var5), p_178251_2_ + "[" + var5 + "]");
                }

                return new Vector3f(var4);
            }
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.parseBlockPart(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }

    static final class SwitchEnumFacing
    {
        static final int[] field_178234_a = new int[EnumFacing.values().length];

        static
        {
            try
            {
                field_178234_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_178234_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_178234_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_178234_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_178234_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_178234_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
