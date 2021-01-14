package optifine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class PlayerItemParser {
    private static JsonParser jsonParser = new JsonParser();
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

    public static PlayerItemModel parseItemModel(JsonObject obj) {
        String type = Json.getString(obj, "type");

        if (!Config.equals(type, "PlayerItem")) {
            throw new JsonParseException("Unknown model type: " + type);
        } else {
            int[] textureSize = Json.parseIntArray(obj.get("textureSize"), 2);
            checkNull(textureSize, "Missing texture size");
            Dimension textureDim = new Dimension(textureSize[0], textureSize[1]);
            boolean usePlayerTexture = Json.getBoolean(obj, "usePlayerTexture", false);
            JsonArray models = (JsonArray) obj.get("models");
            checkNull(models, "Missing elements");
            HashMap mapModelJsons = new HashMap();
            ArrayList listModels = new ArrayList();
            new ArrayList();

            for (int modelRenderers = 0; modelRenderers < models.size(); ++modelRenderers) {
                JsonObject elem = (JsonObject) models.get(modelRenderers);
                String baseId = Json.getString(elem, "baseId");

                if (baseId != null) {
                    JsonObject id = (JsonObject) mapModelJsons.get(baseId);

                    if (id == null) {
                        Config.warn("BaseID not found: " + baseId);
                        continue;
                    }

                    Set mr = id.entrySet();
                    Iterator iterator = mr.iterator();

                    while (iterator.hasNext()) {
                        Entry entry = (Entry) iterator.next();

                        if (!elem.has((String) entry.getKey())) {
                            elem.add((String) entry.getKey(), (JsonElement) entry.getValue());
                        }
                    }
                }

                String var17 = Json.getString(elem, "id");

                if (var17 != null) {
                    if (!mapModelJsons.containsKey(var17)) {
                        mapModelJsons.put(var17, elem);
                    } else {
                        Config.warn("Duplicate model ID: " + var17);
                    }
                }

                PlayerItemRenderer var18 = parseItemRenderer(elem, textureDim);

                if (var18 != null) {
                    listModels.add(var18);
                }
            }

            PlayerItemRenderer[] var16 = (PlayerItemRenderer[]) ((PlayerItemRenderer[]) listModels.toArray(new PlayerItemRenderer[listModels.size()]));
            return new PlayerItemModel(textureDim, usePlayerTexture, var16);
        }
    }

    private static void checkNull(Object obj, String msg) {
        if (obj == null) {
            throw new JsonParseException(msg);
        }
    }

    private static ResourceLocation makeResourceLocation(String texture) {
        int pos = texture.indexOf(58);

        if (pos < 0) {
            return new ResourceLocation(texture);
        } else {
            String domain = texture.substring(0, pos);
            String path = texture.substring(pos + 1);
            return new ResourceLocation(domain, path);
        }
    }

    private static int parseAttachModel(String attachModelStr) {
        if (attachModelStr == null) {
            return 0;
        } else if (attachModelStr.equals("body")) {
            return 0;
        } else if (attachModelStr.equals("head")) {
            return 1;
        } else if (attachModelStr.equals("leftArm")) {
            return 2;
        } else if (attachModelStr.equals("rightArm")) {
            return 3;
        } else if (attachModelStr.equals("leftLeg")) {
            return 4;
        } else if (attachModelStr.equals("rightLeg")) {
            return 5;
        } else if (attachModelStr.equals("cape")) {
            return 6;
        } else {
            Config.warn("Unknown attachModel: " + attachModelStr);
            return 0;
        }
    }

    private static PlayerItemRenderer parseItemRenderer(JsonObject elem, Dimension textureDim) {
        String type = Json.getString(elem, "type");

        if (!Config.equals(type, "ModelBox")) {
            Config.warn("Unknown model type: " + type);
            return null;
        } else {
            String attachToStr = Json.getString(elem, "attachTo");
            int attachTo = parseAttachModel(attachToStr);
            float scale = Json.getFloat(elem, "scale", 1.0F);
            ModelPlayerItem modelBase = new ModelPlayerItem();
            modelBase.textureWidth = textureDim.width;
            modelBase.textureHeight = textureDim.height;
            ModelRenderer mr = parseModelRenderer(elem, modelBase);
            PlayerItemRenderer pir = new PlayerItemRenderer(attachTo, scale, mr);
            return pir;
        }
    }

    private static ModelRenderer parseModelRenderer(JsonObject elem, ModelBase modelBase) {
        ModelRenderer mr = new ModelRenderer(modelBase);
        String invertAxis = Json.getString(elem, "invertAxis", "").toLowerCase();
        boolean invertX = invertAxis.contains("x");
        boolean invertY = invertAxis.contains("y");
        boolean invertZ = invertAxis.contains("z");
        float[] translate = Json.parseFloatArray(elem.get("translate"), 3, new float[3]);

        if (invertX) {
            translate[0] = -translate[0];
        }

        if (invertY) {
            translate[1] = -translate[1];
        }

        if (invertZ) {
            translate[2] = -translate[2];
        }

        float[] rotateAngles = Json.parseFloatArray(elem.get("rotate"), 3, new float[3]);

        for (int mirrorTexture = 0; mirrorTexture < rotateAngles.length; ++mirrorTexture) {
            rotateAngles[mirrorTexture] = rotateAngles[mirrorTexture] / 180.0F * (float) Math.PI;
        }

        if (invertX) {
            rotateAngles[0] = -rotateAngles[0];
        }

        if (invertY) {
            rotateAngles[1] = -rotateAngles[1];
        }

        if (invertZ) {
            rotateAngles[2] = -rotateAngles[2];
        }

        mr.setRotationPoint(translate[0], translate[1], translate[2]);
        mr.rotateAngleX = rotateAngles[0];
        mr.rotateAngleY = rotateAngles[1];
        mr.rotateAngleZ = rotateAngles[2];
        String var19 = Json.getString(elem, "mirrorTexture", "").toLowerCase();
        boolean invertU = var19.contains("u");
        boolean invertV = var19.contains("v");

        if (invertU) {
            mr.mirror = true;
        }

        if (invertV) {
            mr.mirrorV = true;
        }

        JsonArray boxes = elem.getAsJsonArray("boxes");
        JsonObject submodel;

        if (boxes != null) {
            for (int sprites = 0; sprites < boxes.size(); ++sprites) {
                submodel = boxes.get(sprites).getAsJsonObject();
                int[] submodels = Json.parseIntArray(submodel.get("textureOffset"), 2);

                if (submodels == null) {
                    throw new JsonParseException("Texture offset not specified");
                }

                float[] i = Json.parseFloatArray(submodel.get("coordinates"), 6);

                if (i == null) {
                    throw new JsonParseException("Coordinates not specified");
                }

                if (invertX) {
                    i[0] = -i[0] - i[3];
                }

                if (invertY) {
                    i[1] = -i[1] - i[4];
                }

                if (invertZ) {
                    i[2] = -i[2] - i[5];
                }

                float sm = Json.getFloat(submodel, "sizeAdd", 0.0F);
                mr.setTextureOffset(submodels[0], submodels[1]);
                mr.addBox(i[0], i[1], i[2], (int) i[3], (int) i[4], (int) i[5], sm);
            }
        }

        JsonArray var20 = elem.getAsJsonArray("sprites");

        if (var20 != null) {
            for (int var21 = 0; var21 < var20.size(); ++var21) {
                JsonObject var22 = var20.get(var21).getAsJsonObject();
                int[] var25 = Json.parseIntArray(var22.get("textureOffset"), 2);

                if (var25 == null) {
                    throw new JsonParseException("Texture offset not specified");
                }

                float[] var27 = Json.parseFloatArray(var22.get("coordinates"), 6);

                if (var27 == null) {
                    throw new JsonParseException("Coordinates not specified");
                }

                if (invertX) {
                    var27[0] = -var27[0] - var27[3];
                }

                if (invertY) {
                    var27[1] = -var27[1] - var27[4];
                }

                if (invertZ) {
                    var27[2] = -var27[2] - var27[5];
                }

                float subMr = Json.getFloat(var22, "sizeAdd", 0.0F);
                mr.setTextureOffset(var25[0], var25[1]);
                mr.addSprite(var27[0], var27[1], var27[2], (int) var27[3], (int) var27[4], (int) var27[5], subMr);
            }
        }

        submodel = (JsonObject) elem.get("submodel");

        if (submodel != null) {
            ModelRenderer var23 = parseModelRenderer(submodel, modelBase);
            mr.addChild(var23);
        }

        JsonArray var24 = (JsonArray) elem.get("submodels");

        if (var24 != null) {
            for (int var26 = 0; var26 < var24.size(); ++var26) {
                JsonObject var28 = (JsonObject) var24.get(var26);
                ModelRenderer var29 = parseModelRenderer(var28, modelBase);
                mr.addChild(var29);
            }
        }

        return mr;
    }
}
