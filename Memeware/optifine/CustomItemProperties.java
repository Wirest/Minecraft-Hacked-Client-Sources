package optifine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CustomItemProperties {
    public String name = null;
    public String basePath = null;
    public int type = 1;
    public int[] items = null;
    public String texture = null;
    public Map<String, String> mapTextures = null;
    public RangeListInt damage = null;
    public boolean damagePercent = false;
    public int damageMask = 0;
    public RangeListInt stackSize = null;
    public RangeListInt enchantmentIds = null;
    public RangeListInt enchantmentLevels = null;
    public NbtTagValue[] nbtTagValues = null;
    public int blend = 1;
    public float speed = 0.0F;
    public float rotation = 0.0F;
    public int layer = 0;
    public float duration = 1.0F;
    public int weight = 0;
    public ResourceLocation textureLocation = null;
    public Map mapTextureLocations = null;
    public TextureAtlasSprite sprite = null;
    public Map mapSprites = null;
    public IBakedModel model = null;
    public Map<String, IBakedModel> mapModels = null;
    private int textureWidth = 0;
    private int textureHeight = 0;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;

    public CustomItemProperties(Properties props, String path) {
        this.name = parseName(path);
        this.basePath = parseBasePath(path);
        this.type = this.parseType(props.getProperty("type"));
        this.items = this.parseItems(props.getProperty("items"), props.getProperty("matchItems"));
        this.mapTextures = parseTextures(props, this.basePath);
        this.texture = parseTexture(props.getProperty("texture"), props.getProperty("tile"), props.getProperty("source"), path, this.basePath, this.type, this.mapTextures);
        String damageStr = props.getProperty("damage");

        if (damageStr != null) {
            this.damagePercent = damageStr.contains("%");
            damageStr.replace("%", "");
            this.damage = this.parseRangeListInt(damageStr);
            this.damageMask = this.parseInt(props.getProperty("damageMask"), 0);
        }

        this.stackSize = this.parseRangeListInt(props.getProperty("stackSize"));
        this.enchantmentIds = this.parseRangeListInt(props.getProperty("enchantmentIDs"));
        this.enchantmentLevels = this.parseRangeListInt(props.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(props);
        this.blend = Blender.parseBlend(props.getProperty("blend"));
        this.speed = this.parseFloat(props.getProperty("speed"), 0.0F);
        this.rotation = this.parseFloat(props.getProperty("rotation"), 0.0F);
        this.layer = this.parseInt(props.getProperty("layer"), 0);
        this.weight = this.parseInt(props.getProperty("weight"), 0);
        this.duration = this.parseFloat(props.getProperty("duration"), 1.0F);
    }

    private static String parseName(String path) {
        String str = path;
        int pos = path.lastIndexOf(47);

        if (pos >= 0) {
            str = path.substring(pos + 1);
        }

        int pos2 = str.lastIndexOf(46);

        if (pos2 >= 0) {
            str = str.substring(0, pos2);
        }

        return str;
    }

    private static String parseBasePath(String path) {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }

    private int parseType(String str) {
        if (str == null) {
            return 1;
        } else if (str.equals("item")) {
            return 1;
        } else if (str.equals("enchantment")) {
            return 2;
        } else if (str.equals("armor")) {
            return 3;
        } else {
            Config.warn("Unknown method: " + str);
            return 0;
        }
    }

    private int[] parseItems(String str, String str2) {
        if (str == null) {
            str = str2;
        }

        if (str == null) {
            return null;
        } else {
            str = str.trim();
            TreeSet setItemIds = new TreeSet();
            String[] tokens = Config.tokenize(str, " ");
            int i;
            label58:

            for (int integers = 0; integers < tokens.length; ++integers) {
                String ints = tokens[integers];
                i = Config.parseInt(ints, -1);

                if (i >= 0) {
                    setItemIds.add(new Integer(i));
                } else {
                    int id;

                    if (ints.contains("-")) {
                        String[] item = Config.tokenize(ints, "-");

                        if (item.length == 2) {
                            id = Config.parseInt(item[0], -1);
                            int val2 = Config.parseInt(item[1], -1);

                            if (id >= 0 && val2 >= 0) {
                                int min = Math.min(id, val2);
                                int max = Math.max(id, val2);
                                int x = min;

                                while (true) {
                                    if (x > max) {
                                        continue label58;
                                    }

                                    setItemIds.add(new Integer(x));
                                    ++x;
                                }
                            }
                        }
                    }

                    Item var16 = Item.getByNameOrId(ints);

                    if (var16 == null) {
                        Config.warn("Item not found: " + ints);
                    } else {
                        id = Item.getIdFromItem(var16);

                        if (id < 0) {
                            Config.warn("Item not found: " + ints);
                        } else {
                            setItemIds.add(new Integer(id));
                        }
                    }
                }
            }

            Integer[] var14 = (Integer[]) ((Integer[]) setItemIds.toArray(new Integer[setItemIds.size()]));
            int[] var15 = new int[var14.length];

            for (i = 0; i < var15.length; ++i) {
                var15[i] = var14[i].intValue();
            }

            return var15;
        }
    }

    private static String parseTexture(String texStr, String texStr2, String texStr3, String path, String basePath, int type, Map<String, String> mapTexs) {
        if (texStr == null) {
            texStr = texStr2;
        }

        if (texStr == null) {
            texStr = texStr3;
        }

        String str;

        if (texStr != null) {
            str = ".png";

            if (texStr.endsWith(str)) {
                texStr = texStr.substring(0, texStr.length() - str.length());
            }

            texStr = fixTextureName(texStr, basePath);
            return texStr;
        } else if (type == 3) {
            return null;
        } else {
            if (mapTexs != null) {
                str = (String) mapTexs.get("texture.bow_standby");

                if (str != null) {
                    return str;
                }
            }

            str = path;
            int pos = path.lastIndexOf(47);

            if (pos >= 0) {
                str = path.substring(pos + 1);
            }

            int pos2 = str.lastIndexOf(46);

            if (pos2 >= 0) {
                str = str.substring(0, pos2);
            }

            str = fixTextureName(str, basePath);
            return str;
        }
    }

    private static Map parseTextures(Properties props, String basePath) {
        String prefix = "texture.";
        Map mapProps = getMatchingProperties(props, prefix);

        if (mapProps.size() <= 0) {
            return null;
        } else {
            Set keySet = mapProps.keySet();
            LinkedHashMap mapTex = new LinkedHashMap();
            Iterator it = keySet.iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String val = (String) mapProps.get(key);
                val = fixTextureName(val, basePath);
                mapTex.put(key, val);
            }

            return mapTex;
        }
    }

    private static String fixTextureName(String iconName, String basePath) {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);

        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("mcpatcher/")) {
            iconName = basePath + "/" + iconName;
        }

        if (iconName.endsWith(".png")) {
            iconName = iconName.substring(0, iconName.length() - 4);
        }

        String pathBlocks = "textures/blocks/";

        if (iconName.startsWith(pathBlocks)) {
            iconName = iconName.substring(pathBlocks.length());
        }

        if (iconName.startsWith("/")) {
            iconName = iconName.substring(1);
        }

        return iconName;
    }

    private int parseInt(String str, int defVal) {
        if (str == null) {
            return defVal;
        } else {
            str = str.trim();
            int val = Config.parseInt(str, Integer.MIN_VALUE);

            if (val == Integer.MIN_VALUE) {
                Config.warn("Invalid integer: " + str);
                return defVal;
            } else {
                return val;
            }
        }
    }

    private float parseFloat(String str, float defVal) {
        if (str == null) {
            return defVal;
        } else {
            str = str.trim();
            float val = Config.parseFloat(str, Float.MIN_VALUE);

            if (val == Float.MIN_VALUE) {
                Config.warn("Invalid float: " + str);
                return defVal;
            } else {
                return val;
            }
        }
    }

    private RangeListInt parseRangeListInt(String str) {
        if (str == null) {
            return null;
        } else {
            String[] tokens = Config.tokenize(str, " ");
            RangeListInt rangeList = new RangeListInt();

            for (int i = 0; i < tokens.length; ++i) {
                String token = tokens[i];
                RangeInt range = this.parseRangeInt(token);

                if (range == null) {
                    Config.warn("Invalid range list: " + str);
                    return null;
                }

                rangeList.addRange(range);
            }

            return rangeList;
        }
    }

    private RangeInt parseRangeInt(String str) {
        if (str == null) {
            return null;
        } else {
            str = str.trim();
            int countMinus = str.length() - str.replace("-", "").length();

            if (countMinus > 1) {
                Config.warn("Invalid range: " + str);
                return null;
            } else {
                String[] tokens = Config.tokenize(str, "- ");
                int[] vals = new int[tokens.length];
                int min;

                for (min = 0; min < tokens.length; ++min) {
                    String max = tokens[min];
                    int val = Config.parseInt(max, -1);

                    if (val < 0) {
                        Config.warn("Invalid range: " + str);
                        return null;
                    }

                    vals[min] = val;
                }

                if (vals.length == 1) {
                    min = vals[0];

                    if (str.startsWith("-")) {
                        return new RangeInt(0, min);
                    } else if (str.endsWith("-")) {
                        return new RangeInt(min, 255);
                    } else {
                        return new RangeInt(min, min);
                    }
                } else if (vals.length == 2) {
                    min = Math.min(vals[0], vals[1]);
                    int var8 = Math.max(vals[0], vals[1]);
                    return new RangeInt(min, var8);
                } else {
                    Config.warn("Invalid range: " + str);
                    return null;
                }
            }
        }
    }

    private NbtTagValue[] parseNbtTagValues(Properties props) {
        String PREFIX_NBT = "nbt.";
        Map mapNbt = getMatchingProperties(props, PREFIX_NBT);

        if (mapNbt.size() <= 0) {
            return null;
        } else {
            ArrayList listNbts = new ArrayList();
            Set keySet = mapNbt.keySet();
            Iterator nbts = keySet.iterator();

            while (nbts.hasNext()) {
                String key = (String) nbts.next();
                String val = (String) mapNbt.get(key);
                String id = key.substring(PREFIX_NBT.length());
                NbtTagValue nbt = new NbtTagValue(id, val);
                listNbts.add(nbt);
            }

            NbtTagValue[] nbts1 = (NbtTagValue[]) ((NbtTagValue[]) listNbts.toArray(new NbtTagValue[listNbts.size()]));
            return nbts1;
        }
    }

    private static Map getMatchingProperties(Properties props, String keyPrefix) {
        LinkedHashMap map = new LinkedHashMap();
        Set keySet = props.keySet();
        Iterator it = keySet.iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            String val = props.getProperty(key);

            if (key.startsWith(keyPrefix)) {
                map.put(key, val);
            }
        }

        return map;
    }

    public boolean isValid(String path) {
        if (this.name != null && this.name.length() > 0) {
            if (this.basePath == null) {
                Config.warn("No base path found: " + path);
                return false;
            } else if (this.type == 0) {
                Config.warn("No type defined: " + path);
                return false;
            } else if ((this.type == 1 || this.type == 3) && this.items == null) {
                Config.warn("No items defined: " + path);
                return false;
            } else if (this.texture == null && this.mapTextures == null) {
                Config.warn("No texture specified: " + path);
                return false;
            } else if (this.type == 2 && this.enchantmentIds == null) {
                Config.warn("No enchantmentIDs specified: " + path);
                return false;
            } else {
                return true;
            }
        } else {
            Config.warn("No name found: " + path);
            return false;
        }
    }

    public void updateIcons(TextureMap textureMap) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);

            if (this.type == 1) {
                ResourceLocation keySet = this.getSpriteLocation(this.textureLocation);
                this.sprite = textureMap.func_174942_a(keySet);
            }
        }

        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            Set keySet1 = this.mapTextures.keySet();
            Iterator it = keySet1.iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String val = (String) this.mapTextures.get(key);
                ResourceLocation locTex = this.getTextureLocation(val);
                this.mapTextureLocations.put(key, locTex);

                if (this.type == 1) {
                    ResourceLocation locSprite = this.getSpriteLocation(locTex);
                    TextureAtlasSprite icon = textureMap.func_174942_a(locSprite);
                    this.mapSprites.put(key, icon);
                }
            }
        }
    }

    private ResourceLocation getTextureLocation(String texName) {
        if (texName == null) {
            return null;
        } else {
            ResourceLocation resLoc = new ResourceLocation(texName);
            String domain = resLoc.getResourceDomain();
            String path = resLoc.getResourcePath();

            if (!path.contains("/")) {
                path = "textures/blocks/" + path;
            }

            String filePath = path + ".png";
            ResourceLocation locFile = new ResourceLocation(domain, filePath);
            boolean exists = Config.hasResource(locFile);

            if (!exists) {
                Config.warn("File not found: " + filePath);
            }

            return locFile;
        }
    }

    private ResourceLocation getSpriteLocation(ResourceLocation resLoc) {
        String pathTex = resLoc.getResourcePath();
        pathTex = StrUtils.removePrefix(pathTex, "textures/");
        pathTex = StrUtils.removeSuffix(pathTex, ".png");
        ResourceLocation locTex = new ResourceLocation(resLoc.getResourceDomain(), pathTex);
        return locTex;
    }

    public void updateModel(TextureMap textureMap, ItemModelGenerator itemModelGenerator) {
        String[] textures = this.getModelTextures();
        boolean useTint = this.isUseTint();
        this.model = makeBakedModel(textureMap, itemModelGenerator, textures, useTint);

        if (this.type == 1 && this.mapTextures != null) {
            Set keySet = this.mapTextures.keySet();
            Iterator it = keySet.iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String tex = (String) this.mapTextures.get(key);
                String path = StrUtils.removePrefix(key, "texture.");

                if (path.startsWith("bow") || path.startsWith("fishing_rod")) {
                    String[] texNames = new String[]{tex};
                    IBakedModel modelTex = makeBakedModel(textureMap, itemModelGenerator, texNames, useTint);

                    if (this.mapModels == null) {
                        this.mapModels = new HashMap();
                    }

                    this.mapModels.put(path, modelTex);
                }
            }
        }
    }

    private boolean isUseTint() {
        return true;
    }

    private static IBakedModel makeBakedModel(TextureMap textureMap, ItemModelGenerator itemModelGenerator, String[] textures, boolean useTint) {
        ModelBlock modelBlockBase = makeModelBlock(textures);
        ModelBlock modelBlock = itemModelGenerator.func_178392_a(textureMap, modelBlockBase);
        IBakedModel model = bakeModel(textureMap, modelBlock, useTint);
        return model;
    }

    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            Item item = Item.getItemById(this.items[0]);
            String key;
            String texMain;

            if (item == Items.potionitem && this.damage != null && this.damage.getCountRanges() > 0) {
                RangeInt itemArmor1 = this.damage.getRange(0);
                int material1 = itemArmor1.getMin();
                boolean type1 = (material1 & 16384) != 0;
                key = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
                texMain = null;

                if (type1) {
                    texMain = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
                } else {
                    texMain = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                }

                return new String[]{key, texMain};
            }

            if (item instanceof ItemArmor) {
                ItemArmor itemArmor = (ItemArmor) item;

                if (itemArmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                    String material = "leather";
                    String type = "helmet";

                    if (itemArmor.armorType == 0) {
                        type = "helmet";
                    }

                    if (itemArmor.armorType == 1) {
                        type = "chestplate";
                    }

                    if (itemArmor.armorType == 2) {
                        type = "leggings";
                    }

                    if (itemArmor.armorType == 3) {
                        type = "boots";
                    }

                    key = material + "_" + type;
                    texMain = this.getMapTexture(this.mapTextures, "texture." + key, "items/" + key);
                    String texOverlay = this.getMapTexture(this.mapTextures, "texture." + key + "_overlay", "items/" + key + "_overlay");
                    return new String[]{texMain, texOverlay};
                }
            }
        }

        return new String[]{this.texture};
    }

    private String getMapTexture(Map<String, String> map, String key, String def) {
        if (map == null) {
            return def;
        } else {
            String str = (String) map.get(key);
            return str == null ? def : str;
        }
    }

    private static ModelBlock makeModelBlock(String[] modelTextures) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"parent\": \"builtin/generated\",\"textures\": {");

        for (int modelStr = 0; modelStr < modelTextures.length; ++modelStr) {
            String model = modelTextures[modelStr];

            if (modelStr > 0) {
                sb.append(", ");
            }

            sb.append("\"layer" + modelStr + "\": \"" + model + "\"");
        }

        sb.append("}}");
        String var4 = sb.toString();
        ModelBlock var5 = ModelBlock.deserialize(var4);
        return var5;
    }

    private static IBakedModel bakeModel(TextureMap textureMap, ModelBlock modelBlockIn, boolean useTint) {
        ModelRotation modelRotationIn = ModelRotation.X0_Y0;
        boolean uvLocked = false;
        TextureAtlasSprite var4 = textureMap.getSpriteSafe(modelBlockIn.resolveTextureName("particle"));
        SimpleBakedModel.Builder var5 = (new SimpleBakedModel.Builder(modelBlockIn)).func_177646_a(var4);
        Iterator var6 = modelBlockIn.getElements().iterator();

        while (var6.hasNext()) {
            BlockPart var7 = (BlockPart) var6.next();
            Iterator var8 = var7.field_178240_c.keySet().iterator();

            while (var8.hasNext()) {
                EnumFacing var9 = (EnumFacing) var8.next();
                BlockPartFace var10 = (BlockPartFace) var7.field_178240_c.get(var9);

                if (!useTint) {
                    var10 = new BlockPartFace(var10.field_178244_b, -1, var10.field_178242_d, var10.field_178243_e);
                }

                TextureAtlasSprite var11 = textureMap.getSpriteSafe(modelBlockIn.resolveTextureName(var10.field_178242_d));
                BakedQuad quad = makeBakedQuad(var7, var10, var11, var9, modelRotationIn, uvLocked);

                if (var10.field_178244_b == null) {
                    var5.func_177648_a(quad);
                } else {
                    var5.func_177650_a(modelRotationIn.func_177523_a(var10.field_178244_b), quad);
                }
            }
        }

        return var5.func_177645_b();
    }

    private static BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace blockPartFace, TextureAtlasSprite textureAtlasSprite, EnumFacing enumFacing, ModelRotation modelRotation, boolean uvLocked) {
        FaceBakery faceBakery = new FaceBakery();
        return faceBakery.func_178414_a(blockPart.field_178241_a, blockPart.field_178239_b, blockPartFace, textureAtlasSprite, enumFacing, modelRotation, blockPart.field_178237_d, uvLocked, blockPart.field_178238_e);
    }

    public String toString() {
        return "" + this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
    }

    public float getTextureWidth(TextureManager textureManager) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                ITextureObject tex = textureManager.getTexture(this.textureLocation);
                int texId = tex.getGlTextureId();
                int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager.func_179144_i(texId);
                this.textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                GlStateManager.func_179144_i(prevTexId);
            }

            if (this.textureWidth <= 0) {
                this.textureWidth = 16;
            }
        }

        return (float) this.textureWidth;
    }

    public float getTextureHeight(TextureManager textureManager) {
        if (this.textureHeight <= 0) {
            if (this.textureLocation != null) {
                ITextureObject tex = textureManager.getTexture(this.textureLocation);
                int texId = tex.getGlTextureId();
                int prevTexId = GlStateManager.getBoundTexture();
                GlStateManager.func_179144_i(texId);
                this.textureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
                GlStateManager.func_179144_i(prevTexId);
            }

            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }

        return (float) this.textureHeight;
    }

    public IBakedModel getModel(ModelResourceLocation modelLocation) {
        if (modelLocation != null && this.mapTextures != null) {
            String modelPath = modelLocation.getResourcePath();

            if (this.mapModels != null) {
                IBakedModel customModel = (IBakedModel) this.mapModels.get(modelPath);

                if (customModel != null) {
                    return customModel;
                }
            }
        }

        return this.model;
    }
}
