package net.minecraft.client.renderer.block.model;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ModelBlockDefinition
{
    static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(ModelBlockDefinition.class, new ModelBlockDefinition.Deserializer()).registerTypeAdapter(ModelBlockDefinition.Variant.class, new ModelBlockDefinition.Variant.Deserializer()).create();
    private final Map mapVariants = Maps.newHashMap();

    public static ModelBlockDefinition parseFromReader(Reader p_178331_0_)
    {
        return GSON.fromJson(p_178331_0_, ModelBlockDefinition.class);
    }

    public ModelBlockDefinition(Collection p_i46221_1_)
    {
        Iterator var2 = p_i46221_1_.iterator();

        while (var2.hasNext())
        {
            ModelBlockDefinition.Variants var3 = (ModelBlockDefinition.Variants)var2.next();
            this.mapVariants.put(var3.name, var3);
        }
    }

    public ModelBlockDefinition(List p_i46222_1_)
    {
        Iterator var2 = p_i46222_1_.iterator();

        while (var2.hasNext())
        {
            ModelBlockDefinition var3 = (ModelBlockDefinition)var2.next();
            this.mapVariants.putAll(var3.mapVariants);
        }
    }

    public ModelBlockDefinition.Variants getVariants(String p_178330_1_)
    {
        ModelBlockDefinition.Variants var2 = (ModelBlockDefinition.Variants)this.mapVariants.get(p_178330_1_);

        if (var2 == null)
        {
            throw new ModelBlockDefinition.MissingVariantException();
        }
        else
        {
            return var2;
        }
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ instanceof ModelBlockDefinition)
        {
            ModelBlockDefinition var2 = (ModelBlockDefinition)p_equals_1_;
            return this.mapVariants.equals(var2.mapVariants);
        }
        else
        {
            return false;
        }
    }

    @Override
	public int hashCode()
    {
        return this.mapVariants.hashCode();
    }

    public static class Deserializer implements JsonDeserializer
    {

        public ModelBlockDefinition parseModelBlockDefinition(JsonElement p_178336_1_, Type p_178336_2_, JsonDeserializationContext p_178336_3_)
        {
            JsonObject var4 = p_178336_1_.getAsJsonObject();
            List var5 = this.parseVariantsList(p_178336_3_, var4);
            return new ModelBlockDefinition((Collection)var5);
        }

        protected List parseVariantsList(JsonDeserializationContext p_178334_1_, JsonObject p_178334_2_)
        {
            JsonObject var3 = JsonUtils.getJsonObject(p_178334_2_, "variants");
            ArrayList var4 = Lists.newArrayList();
            Iterator var5 = var3.entrySet().iterator();

            while (var5.hasNext())
            {
                Entry var6 = (Entry)var5.next();
                var4.add(this.parseVariants(p_178334_1_, var6));
            }

            return var4;
        }

        protected ModelBlockDefinition.Variants parseVariants(JsonDeserializationContext p_178335_1_, Entry p_178335_2_)
        {
            String var3 = (String)p_178335_2_.getKey();
            ArrayList var4 = Lists.newArrayList();
            JsonElement var5 = (JsonElement)p_178335_2_.getValue();

            if (var5.isJsonArray())
            {
                Iterator var6 = var5.getAsJsonArray().iterator();

                while (var6.hasNext())
                {
                    JsonElement var7 = (JsonElement)var6.next();
                    var4.add(p_178335_1_.deserialize(var7, ModelBlockDefinition.Variant.class));
                }
            }
            else
            {
                var4.add(p_178335_1_.deserialize(var5, ModelBlockDefinition.Variant.class));
            }

            return new ModelBlockDefinition.Variants(var3, var4);
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.parseModelBlockDefinition(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }

    public class MissingVariantException extends RuntimeException
    {
    }

    public static class Variant
    {
        private final ResourceLocation modelLocation;
        private final ModelRotation modelRotation;
        private final boolean uvLock;
        private final int weight;

        public Variant(ResourceLocation modelLocationIn, ModelRotation modelRotationIn, boolean uvLockIn, int weightIn)
        {
            this.modelLocation = modelLocationIn;
            this.modelRotation = modelRotationIn;
            this.uvLock = uvLockIn;
            this.weight = weightIn;
        }

        public ResourceLocation getModelLocation()
        {
            return this.modelLocation;
        }

        public ModelRotation getRotation()
        {
            return this.modelRotation;
        }

        public boolean isUvLocked()
        {
            return this.uvLock;
        }

        public int getWeight()
        {
            return this.weight;
        }

        @Override
		public boolean equals(Object p_equals_1_)
        {
            if (this == p_equals_1_)
            {
                return true;
            }
            else if (!(p_equals_1_ instanceof ModelBlockDefinition.Variant))
            {
                return false;
            }
            else
            {
                ModelBlockDefinition.Variant var2 = (ModelBlockDefinition.Variant)p_equals_1_;
                return this.modelLocation.equals(var2.modelLocation) && this.modelRotation == var2.modelRotation && this.uvLock == var2.uvLock;
            }
        }

        @Override
		public int hashCode()
        {
            int var1 = this.modelLocation.hashCode();
            var1 = 31 * var1 + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
            var1 = 31 * var1 + (this.uvLock ? 1 : 0);
            return var1;
        }

        public static class Deserializer implements JsonDeserializer
        {

            public ModelBlockDefinition.Variant parseVariant(JsonElement p_178425_1_, Type p_178425_2_, JsonDeserializationContext p_178425_3_)
            {
                JsonObject var4 = p_178425_1_.getAsJsonObject();
                String var5 = this.parseModel(var4);
                ModelRotation var6 = this.parseRotation(var4);
                boolean var7 = this.parseUvLock(var4);
                int var8 = this.parseWeight(var4);
                return new ModelBlockDefinition.Variant(this.makeModelLocation(var5), var6, var7, var8);
            }

            private ResourceLocation makeModelLocation(String p_178426_1_)
            {
                ResourceLocation var2 = new ResourceLocation(p_178426_1_);
                var2 = new ResourceLocation(var2.getResourceDomain(), "block/" + var2.getResourcePath());
                return var2;
            }

            private boolean parseUvLock(JsonObject p_178429_1_)
            {
                return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(p_178429_1_, "uvlock", false);
            }

            protected ModelRotation parseRotation(JsonObject p_178428_1_)
            {
                int var2 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178428_1_, "x", 0);
                int var3 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178428_1_, "y", 0);
                ModelRotation var4 = ModelRotation.getModelRotation(var2, var3);

                if (var4 == null)
                {
                    throw new JsonParseException("Invalid BlockModelRotation x: " + var2 + ", y: " + var3);
                }
                else
                {
                    return var4;
                }
            }

            protected String parseModel(JsonObject p_178424_1_)
            {
                return JsonUtils.getJsonObjectStringFieldValue(p_178424_1_, "model");
            }

            protected int parseWeight(JsonObject p_178427_1_)
            {
                return JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178427_1_, "weight", 1);
            }

            @Override
			public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseVariant(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }
    }

    public static class Variants
    {
        private final String name;
        private final List listVariants;

        public Variants(String nameIn, List listVariantsIn)
        {
            this.name = nameIn;
            this.listVariants = listVariantsIn;
        }

        public List getVariants()
        {
            return this.listVariants;
        }

        @Override
		public boolean equals(Object p_equals_1_)
        {
            if (this == p_equals_1_)
            {
                return true;
            }
            else if (!(p_equals_1_ instanceof ModelBlockDefinition.Variants))
            {
                return false;
            }
            else
            {
                ModelBlockDefinition.Variants var2 = (ModelBlockDefinition.Variants)p_equals_1_;
                return !this.name.equals(var2.name) ? false : this.listVariants.equals(var2.listVariants);
            }
        }

        @Override
		public int hashCode()
        {
            int var1 = this.name.hashCode();
            var1 = 31 * var1 + this.listVariants.hashCode();
            return var1;
        }
    }
}
