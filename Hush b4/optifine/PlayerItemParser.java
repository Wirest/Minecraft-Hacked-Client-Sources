// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import java.util.List;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.JsonArray;
import java.awt.Dimension;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlayerItemParser
{
    private static JsonParser jsonParser;
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_TEXTURE_SIZE = "textureSize";
    public static final String ITEM_USE_PLAYER_TEXTURE = "usePlayerTexture";
    public static final String ITEM_MODELS = "models";
    public static final String MODEL_ID = "id";
    public static final String MODEL_BASE_ID = "baseId";
    public static final String MODEL_TYPE = "type";
    public static final String MODEL_ATTACH_TO = "attachTo";
    public static final String MODEL_INVERT_AXIS = "invertAxis";
    public static final String MODEL_MIRROR_TEXTURE = "mirrorTexture";
    public static final String MODEL_TRANSLATE = "translate";
    public static final String MODEL_ROTATE = "rotate";
    public static final String MODEL_SCALE = "scale";
    public static final String MODEL_BOXES = "boxes";
    public static final String MODEL_SPRITES = "sprites";
    public static final String MODEL_SUBMODEL = "submodel";
    public static final String MODEL_SUBMODELS = "submodels";
    public static final String BOX_TEXTURE_OFFSET = "textureOffset";
    public static final String BOX_COORDINATES = "coordinates";
    public static final String BOX_SIZE_ADD = "sizeAdd";
    public static final String ITEM_TYPE_MODEL = "PlayerItem";
    public static final String MODEL_TYPE_BOX = "ModelBox";
    
    static {
        PlayerItemParser.jsonParser = new JsonParser();
    }
    
    public static PlayerItemModel parseItemModel(final JsonObject p_parseItemModel_0_) {
        final String s = Json.getString(p_parseItemModel_0_, "type");
        if (!Config.equals(s, "PlayerItem")) {
            throw new JsonParseException("Unknown model type: " + s);
        }
        final int[] aint = Json.parseIntArray(p_parseItemModel_0_.get("textureSize"), 2);
        checkNull(aint, "Missing texture size");
        final Dimension dimension = new Dimension(aint[0], aint[1]);
        final boolean flag = Json.getBoolean(p_parseItemModel_0_, "usePlayerTexture", false);
        final JsonArray jsonarray = (JsonArray)p_parseItemModel_0_.get("models");
        checkNull(jsonarray, "Missing elements");
        final Map map = new HashMap();
        final List list = new ArrayList();
        new ArrayList();
        for (int i = 0; i < jsonarray.size(); ++i) {
            final JsonObject jsonobject = (JsonObject)jsonarray.get(i);
            final String s2 = Json.getString(jsonobject, "baseId");
            if (s2 != null) {
                final JsonObject jsonobject2 = map.get(s2);
                if (jsonobject2 == null) {
                    Config.warn("BaseID not found: " + s2);
                    continue;
                }
                for (final Map.Entry<String, JsonElement> entry : jsonobject2.entrySet()) {
                    if (!jsonobject.has(entry.getKey())) {
                        jsonobject.add(entry.getKey(), entry.getValue());
                    }
                }
            }
            final String s3 = Json.getString(jsonobject, "id");
            if (s3 != null) {
                if (!map.containsKey(s3)) {
                    map.put(s3, jsonobject);
                }
                else {
                    Config.warn("Duplicate model ID: " + s3);
                }
            }
            final PlayerItemRenderer playeritemrenderer = parseItemRenderer(jsonobject, dimension);
            if (playeritemrenderer != null) {
                list.add(playeritemrenderer);
            }
        }
        final PlayerItemRenderer[] aplayeritemrenderer = list.toArray(new PlayerItemRenderer[list.size()]);
        return new PlayerItemModel(dimension, flag, aplayeritemrenderer);
    }
    
    private static void checkNull(final Object p_checkNull_0_, final String p_checkNull_1_) {
        if (p_checkNull_0_ == null) {
            throw new JsonParseException(p_checkNull_1_);
        }
    }
    
    private static ResourceLocation makeResourceLocation(final String p_makeResourceLocation_0_) {
        final int i = p_makeResourceLocation_0_.indexOf(58);
        if (i < 0) {
            return new ResourceLocation(p_makeResourceLocation_0_);
        }
        final String s = p_makeResourceLocation_0_.substring(0, i);
        final String s2 = p_makeResourceLocation_0_.substring(i + 1);
        return new ResourceLocation(s, s2);
    }
    
    private static int parseAttachModel(final String p_parseAttachModel_0_) {
        if (p_parseAttachModel_0_ == null) {
            return 0;
        }
        if (p_parseAttachModel_0_.equals("body")) {
            return 0;
        }
        if (p_parseAttachModel_0_.equals("head")) {
            return 1;
        }
        if (p_parseAttachModel_0_.equals("leftArm")) {
            return 2;
        }
        if (p_parseAttachModel_0_.equals("rightArm")) {
            return 3;
        }
        if (p_parseAttachModel_0_.equals("leftLeg")) {
            return 4;
        }
        if (p_parseAttachModel_0_.equals("rightLeg")) {
            return 5;
        }
        if (p_parseAttachModel_0_.equals("cape")) {
            return 6;
        }
        Config.warn("Unknown attachModel: " + p_parseAttachModel_0_);
        return 0;
    }
    
    private static PlayerItemRenderer parseItemRenderer(final JsonObject p_parseItemRenderer_0_, final Dimension p_parseItemRenderer_1_) {
        final String s = Json.getString(p_parseItemRenderer_0_, "type");
        if (!Config.equals(s, "ModelBox")) {
            Config.warn("Unknown model type: " + s);
            return null;
        }
        final String s2 = Json.getString(p_parseItemRenderer_0_, "attachTo");
        final int i = parseAttachModel(s2);
        final float f = Json.getFloat(p_parseItemRenderer_0_, "scale", 1.0f);
        final ModelBase modelbase = new ModelPlayerItem();
        modelbase.textureWidth = p_parseItemRenderer_1_.width;
        modelbase.textureHeight = p_parseItemRenderer_1_.height;
        final ModelRenderer modelrenderer = parseModelRenderer(p_parseItemRenderer_0_, modelbase);
        final PlayerItemRenderer playeritemrenderer = new PlayerItemRenderer(i, f, modelrenderer);
        return playeritemrenderer;
    }
    
    private static ModelRenderer parseModelRenderer(final JsonObject p_parseModelRenderer_0_, final ModelBase p_parseModelRenderer_1_) {
        final ModelRenderer modelrenderer = new ModelRenderer(p_parseModelRenderer_1_);
        final String s = Json.getString(p_parseModelRenderer_0_, "invertAxis", "").toLowerCase();
        final boolean flag = s.contains("x");
        final boolean flag2 = s.contains("y");
        final boolean flag3 = s.contains("z");
        final float[] afloat = Json.parseFloatArray(p_parseModelRenderer_0_.get("translate"), 3, new float[3]);
        if (flag) {
            afloat[0] = -afloat[0];
        }
        if (flag2) {
            afloat[1] = -afloat[1];
        }
        if (flag3) {
            afloat[2] = -afloat[2];
        }
        final float[] afloat2 = Json.parseFloatArray(p_parseModelRenderer_0_.get("rotate"), 3, new float[3]);
        for (int i = 0; i < afloat2.length; ++i) {
            afloat2[i] = afloat2[i] / 180.0f * 3.1415927f;
        }
        if (flag) {
            afloat2[0] = -afloat2[0];
        }
        if (flag2) {
            afloat2[1] = -afloat2[1];
        }
        if (flag3) {
            afloat2[2] = -afloat2[2];
        }
        modelrenderer.setRotationPoint(afloat[0], afloat[1], afloat[2]);
        modelrenderer.rotateAngleX = afloat2[0];
        modelrenderer.rotateAngleY = afloat2[1];
        modelrenderer.rotateAngleZ = afloat2[2];
        final String s2 = Json.getString(p_parseModelRenderer_0_, "mirrorTexture", "").toLowerCase();
        final boolean flag4 = s2.contains("u");
        final boolean flag5 = s2.contains("v");
        if (flag4) {
            modelrenderer.mirror = true;
        }
        if (flag5) {
            modelrenderer.mirrorV = true;
        }
        final JsonArray jsonarray = p_parseModelRenderer_0_.getAsJsonArray("boxes");
        if (jsonarray != null) {
            for (int j = 0; j < jsonarray.size(); ++j) {
                final JsonObject jsonobject = jsonarray.get(j).getAsJsonObject();
                final int[] aint = Json.parseIntArray(jsonobject.get("textureOffset"), 2);
                if (aint == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                final float[] afloat3 = Json.parseFloatArray(jsonobject.get("coordinates"), 6);
                if (afloat3 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (flag) {
                    afloat3[0] = -afloat3[0] - afloat3[3];
                }
                if (flag2) {
                    afloat3[1] = -afloat3[1] - afloat3[4];
                }
                if (flag3) {
                    afloat3[2] = -afloat3[2] - afloat3[5];
                }
                final float f = Json.getFloat(jsonobject, "sizeAdd", 0.0f);
                modelrenderer.setTextureOffset(aint[0], aint[1]);
                modelrenderer.addBox(afloat3[0], afloat3[1], afloat3[2], (int)afloat3[3], (int)afloat3[4], (int)afloat3[5], f);
            }
        }
        final JsonArray jsonarray2 = p_parseModelRenderer_0_.getAsJsonArray("sprites");
        if (jsonarray2 != null) {
            for (int k = 0; k < jsonarray2.size(); ++k) {
                final JsonObject jsonobject2 = jsonarray2.get(k).getAsJsonObject();
                final int[] aint2 = Json.parseIntArray(jsonobject2.get("textureOffset"), 2);
                if (aint2 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }
                final float[] afloat4 = Json.parseFloatArray(jsonobject2.get("coordinates"), 6);
                if (afloat4 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }
                if (flag) {
                    afloat4[0] = -afloat4[0] - afloat4[3];
                }
                if (flag2) {
                    afloat4[1] = -afloat4[1] - afloat4[4];
                }
                if (flag3) {
                    afloat4[2] = -afloat4[2] - afloat4[5];
                }
                final float f2 = Json.getFloat(jsonobject2, "sizeAdd", 0.0f);
                modelrenderer.setTextureOffset(aint2[0], aint2[1]);
                modelrenderer.addSprite(afloat4[0], afloat4[1], afloat4[2], (int)afloat4[3], (int)afloat4[4], (int)afloat4[5], f2);
            }
        }
        final JsonObject jsonobject3 = (JsonObject)p_parseModelRenderer_0_.get("submodel");
        if (jsonobject3 != null) {
            final ModelRenderer modelrenderer2 = parseModelRenderer(jsonobject3, p_parseModelRenderer_1_);
            modelrenderer.addChild(modelrenderer2);
        }
        final JsonArray jsonarray3 = (JsonArray)p_parseModelRenderer_0_.get("submodels");
        if (jsonarray3 != null) {
            for (int l = 0; l < jsonarray3.size(); ++l) {
                final JsonObject jsonobject4 = (JsonObject)jsonarray3.get(l);
                final ModelRenderer modelrenderer3 = parseModelRenderer(jsonobject4, p_parseModelRenderer_1_);
                modelrenderer.addChild(modelrenderer3);
            }
        }
        return modelrenderer;
    }
}
