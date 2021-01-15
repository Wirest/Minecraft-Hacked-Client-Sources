package net.minecraft.client.renderer.block.model;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ModelBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    static final Gson SERIALIZER = (new GsonBuilder()).registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer()).registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).create();
    private final List elements;
    private final boolean gui3d;
    private final boolean ambientOcclusion;
    private ItemCameraTransforms cameraTransforms;
    public String name;
    protected final Map textures;
    protected ModelBlock parent;
    protected ResourceLocation parentLocation;

    public static ModelBlock deserialize(Reader p_178307_0_)
    {
        return SERIALIZER.fromJson(p_178307_0_, ModelBlock.class);
    }

    public static ModelBlock deserialize(String p_178294_0_)
    {
        return deserialize(new StringReader(p_178294_0_));
    }

    protected ModelBlock(List p_i46225_1_, Map p_i46225_2_, boolean p_i46225_3_, boolean p_i46225_4_, ItemCameraTransforms p_i46225_5_)
    {
        this((ResourceLocation)null, p_i46225_1_, p_i46225_2_, p_i46225_3_, p_i46225_4_, p_i46225_5_);
    }

    protected ModelBlock(ResourceLocation p_i46226_1_, Map p_i46226_2_, boolean p_i46226_3_, boolean p_i46226_4_, ItemCameraTransforms p_i46226_5_)
    {
        this(p_i46226_1_, Collections.emptyList(), p_i46226_2_, p_i46226_3_, p_i46226_4_, p_i46226_5_);
    }

    private ModelBlock(ResourceLocation parentLocationIn, List elementsIn, Map texturesIn, boolean ambientOcclusionIn, boolean gui3dIn, ItemCameraTransforms cameraTransformsIn)
    {
        this.name = "";
        this.elements = elementsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.textures = texturesIn;
        this.parentLocation = parentLocationIn;
        this.cameraTransforms = cameraTransformsIn;
    }

    public List getElements()
    {
        return this.hasParent() ? this.parent.getElements() : this.elements;
    }

    private boolean hasParent()
    {
        return this.parent != null;
    }

    public boolean isAmbientOcclusion()
    {
        return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3d;
    }

    public boolean isResolved()
    {
        return this.parentLocation == null || this.parent != null && this.parent.isResolved();
    }

    public void getParentFromMap(Map p_178299_1_)
    {
        if (this.parentLocation != null)
        {
            this.parent = (ModelBlock)p_178299_1_.get(this.parentLocation);
        }
    }

    public boolean isTexturePresent(String textureName)
    {
        return !"missingno".equals(this.resolveTextureName(textureName));
    }

    public String resolveTextureName(String textureName)
    {
        if (!this.startsWithHash(textureName))
        {
            textureName = '#' + textureName;
        }

        return this.resolveTextureName(textureName, new ModelBlock.Bookkeep(null));
    }

    private String resolveTextureName(String textureName, ModelBlock.Bookkeep p_178302_2_)
    {
        if (this.startsWithHash(textureName))
        {
            if (this == p_178302_2_.modelExt)
            {
                LOGGER.warn("Unable to resolve texture due to upward reference: " + textureName + " in " + this.name);
                return "missingno";
            }
            else
            {
                String var3 = (String)this.textures.get(textureName.substring(1));

                if (var3 == null && this.hasParent())
                {
                    var3 = this.parent.resolveTextureName(textureName, p_178302_2_);
                }

                p_178302_2_.modelExt = this;

                if (var3 != null && this.startsWithHash(var3))
                {
                    var3 = p_178302_2_.model.resolveTextureName(var3, p_178302_2_);
                }

                return var3 != null && !this.startsWithHash(var3) ? var3 : "missingno";
            }
        }
        else
        {
            return textureName;
        }
    }

    private boolean startsWithHash(String hash)
    {
        return hash.charAt(0) == 35;
    }

    public ResourceLocation getParentLocation()
    {
        return this.parentLocation;
    }

    public ModelBlock getRootModel()
    {
        return this.hasParent() ? this.parent.getRootModel() : this;
    }

    public ItemTransformVec3f getThirdPersonTransform()
    {
        return this.parent != null && this.cameraTransforms.thirdPerson == ItemTransformVec3f.DEFAULT ? this.parent.getThirdPersonTransform() : this.cameraTransforms.thirdPerson;
    }

    public ItemTransformVec3f getFirstPersonTransform()
    {
        return this.parent != null && this.cameraTransforms.firstPerson == ItemTransformVec3f.DEFAULT ? this.parent.getFirstPersonTransform() : this.cameraTransforms.firstPerson;
    }

    public ItemTransformVec3f getHeadTransform()
    {
        return this.parent != null && this.cameraTransforms.head == ItemTransformVec3f.DEFAULT ? this.parent.getHeadTransform() : this.cameraTransforms.head;
    }

    public ItemTransformVec3f getInGuiTransform()
    {
        return this.parent != null && this.cameraTransforms.gui == ItemTransformVec3f.DEFAULT ? this.parent.getInGuiTransform() : this.cameraTransforms.gui;
    }

    public static void checkModelHierarchy(Map p_178312_0_)
    {
        Iterator var1 = p_178312_0_.values().iterator();

        while (var1.hasNext())
        {
            ModelBlock var2 = (ModelBlock)var1.next();

            try
            {
                ModelBlock var3 = var2.parent;

                for (ModelBlock var4 = var3.parent; var3 != var4; var4 = var4.parent.parent)
                {
                    var3 = var3.parent;
                }

                throw new ModelBlock.LoopException();
            }
            catch (NullPointerException var5)
            {
                ;
            }
        }
    }

    final class Bookkeep
    {
        public final ModelBlock model;
        public ModelBlock modelExt;

        private Bookkeep()
        {
            this.model = ModelBlock.this;
        }

        Bookkeep(Object p_i46224_2_)
        {
            this();
        }
    }

    public static class Deserializer implements JsonDeserializer
    {

        public ModelBlock parseModelBlock(JsonElement p_178327_1_, Type p_178327_2_, JsonDeserializationContext p_178327_3_)
        {
            JsonObject var4 = p_178327_1_.getAsJsonObject();
            List var5 = this.getModelElements(p_178327_3_, var4);
            String var6 = this.getParent(var4);
            boolean var7 = StringUtils.isEmpty(var6);
            boolean var8 = var5.isEmpty();

            if (var8 && var7)
            {
                throw new JsonParseException("BlockModel requires either elements or parent, found neither");
            }
            else if (!var7 && !var8)
            {
                throw new JsonParseException("BlockModel requires either elements or parent, found both");
            }
            else
            {
                Map var9 = this.getTextures(var4);
                boolean var10 = this.getAmbientOcclusionEnabled(var4);
                ItemCameraTransforms var11 = ItemCameraTransforms.DEFAULT;

                if (var4.has("display"))
                {
                    JsonObject var12 = JsonUtils.getJsonObject(var4, "display");
                    var11 = (ItemCameraTransforms)p_178327_3_.deserialize(var12, ItemCameraTransforms.class);
                }

                return var8 ? new ModelBlock(new ResourceLocation(var6), var9, var10, true, var11) : new ModelBlock(var5, var9, var10, true, var11);
            }
        }

        private Map getTextures(JsonObject p_178329_1_)
        {
            HashMap var2 = Maps.newHashMap();

            if (p_178329_1_.has("textures"))
            {
                JsonObject var3 = p_178329_1_.getAsJsonObject("textures");
                Iterator var4 = var3.entrySet().iterator();

                while (var4.hasNext())
                {
                    Entry var5 = (Entry)var4.next();
                    var2.put(var5.getKey(), ((JsonElement)var5.getValue()).getAsString());
                }
            }

            return var2;
        }

        private String getParent(JsonObject p_178326_1_)
        {
            return JsonUtils.getJsonObjectStringFieldValueOrDefault(p_178326_1_, "parent", "");
        }

        protected boolean getAmbientOcclusionEnabled(JsonObject p_178328_1_)
        {
            return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(p_178328_1_, "ambientocclusion", true);
        }

        protected List getModelElements(JsonDeserializationContext p_178325_1_, JsonObject p_178325_2_)
        {
            ArrayList var3 = Lists.newArrayList();

            if (p_178325_2_.has("elements"))
            {
                Iterator var4 = JsonUtils.getJsonObjectJsonArrayField(p_178325_2_, "elements").iterator();

                while (var4.hasNext())
                {
                    JsonElement var5 = (JsonElement)var4.next();
                    var3.add(p_178325_1_.deserialize(var5, BlockPart.class));
                }
            }

            return var3;
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.parseModelBlock(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }

    public static class LoopException extends RuntimeException
    {
    }
}
