// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.ItemArmor;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import net.minecraft.item.Item;
import java.util.TreeSet;
import java.util.Properties;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CustomItemProperties
{
    public String name;
    public String basePath;
    public int type;
    public int[] items;
    public String texture;
    public Map<String, String> mapTextures;
    public RangeListInt damage;
    public boolean damagePercent;
    public int damageMask;
    public RangeListInt stackSize;
    public RangeListInt enchantmentIds;
    public RangeListInt enchantmentLevels;
    public NbtTagValue[] nbtTagValues;
    public int blend;
    public float speed;
    public float rotation;
    public int layer;
    public float duration;
    public int weight;
    public ResourceLocation textureLocation;
    public Map mapTextureLocations;
    public TextureAtlasSprite sprite;
    public Map mapSprites;
    public IBakedModel model;
    public Map<String, IBakedModel> mapModels;
    private int textureWidth;
    private int textureHeight;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;
    
    public CustomItemProperties(final Properties p_i34_1_, final String p_i34_2_) {
        this.name = null;
        this.basePath = null;
        this.type = 1;
        this.items = null;
        this.texture = null;
        this.mapTextures = null;
        this.damage = null;
        this.damagePercent = false;
        this.damageMask = 0;
        this.stackSize = null;
        this.enchantmentIds = null;
        this.enchantmentLevels = null;
        this.nbtTagValues = null;
        this.blend = 1;
        this.speed = 0.0f;
        this.rotation = 0.0f;
        this.layer = 0;
        this.duration = 1.0f;
        this.weight = 0;
        this.textureLocation = null;
        this.mapTextureLocations = null;
        this.sprite = null;
        this.mapSprites = null;
        this.model = null;
        this.mapModels = null;
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.name = parseName(p_i34_2_);
        this.basePath = parseBasePath(p_i34_2_);
        this.type = this.parseType(p_i34_1_.getProperty("type"));
        this.items = this.parseItems(p_i34_1_.getProperty("items"), p_i34_1_.getProperty("matchItems"));
        this.mapTextures = (Map<String, String>)parseTextures(p_i34_1_, this.basePath);
        this.texture = parseTexture(p_i34_1_.getProperty("texture"), p_i34_1_.getProperty("tile"), p_i34_1_.getProperty("source"), p_i34_2_, this.basePath, this.type, this.mapTextures);
        final String s = p_i34_1_.getProperty("damage");
        if (s != null) {
            this.damagePercent = s.contains("%");
            s.replace("%", "");
            this.damage = this.parseRangeListInt(s);
            this.damageMask = this.parseInt(p_i34_1_.getProperty("damageMask"), 0);
        }
        this.stackSize = this.parseRangeListInt(p_i34_1_.getProperty("stackSize"));
        this.enchantmentIds = this.parseRangeListInt(p_i34_1_.getProperty("enchantmentIDs"));
        this.enchantmentLevels = this.parseRangeListInt(p_i34_1_.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(p_i34_1_);
        this.blend = Blender.parseBlend(p_i34_1_.getProperty("blend"));
        this.speed = this.parseFloat(p_i34_1_.getProperty("speed"), 0.0f);
        this.rotation = this.parseFloat(p_i34_1_.getProperty("rotation"), 0.0f);
        this.layer = this.parseInt(p_i34_1_.getProperty("layer"), 0);
        this.weight = this.parseInt(p_i34_1_.getProperty("weight"), 0);
        this.duration = this.parseFloat(p_i34_1_.getProperty("duration"), 1.0f);
    }
    
    private static String parseName(final String p_parseName_0_) {
        String s = p_parseName_0_;
        final int i = p_parseName_0_.lastIndexOf(47);
        if (i >= 0) {
            s = p_parseName_0_.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    private static String parseBasePath(final String p_parseBasePath_0_) {
        final int i = p_parseBasePath_0_.lastIndexOf(47);
        return (i < 0) ? "" : p_parseBasePath_0_.substring(0, i);
    }
    
    private int parseType(final String p_parseType_1_) {
        if (p_parseType_1_ == null) {
            return 1;
        }
        if (p_parseType_1_.equals("item")) {
            return 1;
        }
        if (p_parseType_1_.equals("enchantment")) {
            return 2;
        }
        if (p_parseType_1_.equals("armor")) {
            return 3;
        }
        Config.warn("Unknown method: " + p_parseType_1_);
        return 0;
    }
    
    private int[] parseItems(String p_parseItems_1_, final String p_parseItems_2_) {
        if (p_parseItems_1_ == null) {
            p_parseItems_1_ = p_parseItems_2_;
        }
        if (p_parseItems_1_ == null) {
            return null;
        }
        p_parseItems_1_ = p_parseItems_1_.trim();
        final Set set = new TreeSet();
        final String[] astring = Config.tokenize(p_parseItems_1_, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final int j = Config.parseInt(s, -1);
            if (j >= 0) {
                set.add(new Integer(j));
            }
            else {
                if (s.contains("-")) {
                    final String[] astring2 = Config.tokenize(s, "-");
                    if (astring2.length == 2) {
                        final int k = Config.parseInt(astring2[0], -1);
                        final int l = Config.parseInt(astring2[1], -1);
                        if (k >= 0 && l >= 0) {
                            final int i2 = Math.min(k, l);
                            for (int j2 = Math.max(k, l), k2 = i2; k2 <= j2; ++k2) {
                                set.add(new Integer(k2));
                            }
                            continue;
                        }
                    }
                }
                final Item item = Item.getByNameOrId(s);
                if (item == null) {
                    Config.warn("Item not found: " + s);
                }
                else {
                    final int i3 = Item.getIdFromItem(item);
                    if (i3 < 0) {
                        Config.warn("Item not found: " + s);
                    }
                    else {
                        set.add(new Integer(i3));
                    }
                }
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = new int[ainteger.length];
        for (int l2 = 0; l2 < aint.length; ++l2) {
            aint[l2] = ainteger[l2];
        }
        return aint;
    }
    
    private static String parseTexture(String p_parseTexture_0_, final String p_parseTexture_1_, final String p_parseTexture_2_, final String p_parseTexture_3_, final String p_parseTexture_4_, final int p_parseTexture_5_, final Map<String, String> p_parseTexture_6_) {
        if (p_parseTexture_0_ == null) {
            p_parseTexture_0_ = p_parseTexture_1_;
        }
        if (p_parseTexture_0_ == null) {
            p_parseTexture_0_ = p_parseTexture_2_;
        }
        if (p_parseTexture_0_ != null) {
            final String s2 = ".png";
            if (p_parseTexture_0_.endsWith(s2)) {
                p_parseTexture_0_ = p_parseTexture_0_.substring(0, p_parseTexture_0_.length() - s2.length());
            }
            p_parseTexture_0_ = fixTextureName(p_parseTexture_0_, p_parseTexture_4_);
            return p_parseTexture_0_;
        }
        if (p_parseTexture_5_ == 3) {
            return null;
        }
        if (p_parseTexture_6_ != null) {
            final String s3 = p_parseTexture_6_.get("texture.bow_standby");
            if (s3 != null) {
                return s3;
            }
        }
        String s4 = p_parseTexture_3_;
        final int i = p_parseTexture_3_.lastIndexOf(47);
        if (i >= 0) {
            s4 = p_parseTexture_3_.substring(i + 1);
        }
        final int j = s4.lastIndexOf(46);
        if (j >= 0) {
            s4 = s4.substring(0, j);
        }
        s4 = fixTextureName(s4, p_parseTexture_4_);
        return s4;
    }
    
    private static Map parseTextures(final Properties p_parseTextures_0_, final String p_parseTextures_1_) {
        final String s = "texture.";
        final Map map = getMatchingProperties(p_parseTextures_0_, s);
        if (map.size() <= 0) {
            return null;
        }
        final Set set = map.keySet();
        final Map map2 = new LinkedHashMap();
        for (final Object s2 : set) {
            String s3 = map.get(s2);
            s3 = fixTextureName(s3, p_parseTextures_1_);
            map2.put(s2, s3);
        }
        return map2;
    }
    
    private static String fixTextureName(String p_fixTextureName_0_, final String p_fixTextureName_1_) {
        p_fixTextureName_0_ = TextureUtils.fixResourcePath(p_fixTextureName_0_, p_fixTextureName_1_);
        if (!p_fixTextureName_0_.startsWith(p_fixTextureName_1_) && !p_fixTextureName_0_.startsWith("textures/") && !p_fixTextureName_0_.startsWith("mcpatcher/")) {
            p_fixTextureName_0_ = String.valueOf(p_fixTextureName_1_) + "/" + p_fixTextureName_0_;
        }
        if (p_fixTextureName_0_.endsWith(".png")) {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(0, p_fixTextureName_0_.length() - 4);
        }
        final String s = "textures/blocks/";
        if (p_fixTextureName_0_.startsWith(s)) {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(s.length());
        }
        if (p_fixTextureName_0_.startsWith("/")) {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(1);
        }
        return p_fixTextureName_0_;
    }
    
    private int parseInt(String p_parseInt_1_, final int p_parseInt_2_) {
        if (p_parseInt_1_ == null) {
            return p_parseInt_2_;
        }
        p_parseInt_1_ = p_parseInt_1_.trim();
        final int i = Config.parseInt(p_parseInt_1_, Integer.MIN_VALUE);
        if (i == Integer.MIN_VALUE) {
            Config.warn("Invalid integer: " + p_parseInt_1_);
            return p_parseInt_2_;
        }
        return i;
    }
    
    private float parseFloat(String p_parseFloat_1_, final float p_parseFloat_2_) {
        if (p_parseFloat_1_ == null) {
            return p_parseFloat_2_;
        }
        p_parseFloat_1_ = p_parseFloat_1_.trim();
        final float f = Config.parseFloat(p_parseFloat_1_, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid float: " + p_parseFloat_1_);
            return p_parseFloat_2_;
        }
        return f;
    }
    
    private RangeListInt parseRangeListInt(final String p_parseRangeListInt_1_) {
        if (p_parseRangeListInt_1_ == null) {
            return null;
        }
        final String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ");
        final RangeListInt rangelistint = new RangeListInt();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                Config.warn("Invalid range list: " + p_parseRangeListInt_1_);
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }
    
    private RangeInt parseRangeInt(String p_parseRangeInt_1_) {
        if (p_parseRangeInt_1_ == null) {
            return null;
        }
        p_parseRangeInt_1_ = p_parseRangeInt_1_.trim();
        final int i = p_parseRangeInt_1_.length() - p_parseRangeInt_1_.replace("-", "").length();
        if (i > 1) {
            Config.warn("Invalid range: " + p_parseRangeInt_1_);
            return null;
        }
        final String[] astring = Config.tokenize(p_parseRangeInt_1_, "- ");
        final int[] aint = new int[astring.length];
        for (int j = 0; j < astring.length; ++j) {
            final String s = astring[j];
            final int k = Config.parseInt(s, -1);
            if (k < 0) {
                Config.warn("Invalid range: " + p_parseRangeInt_1_);
                return null;
            }
            aint[j] = k;
        }
        if (aint.length == 1) {
            final int i2 = aint[0];
            if (p_parseRangeInt_1_.startsWith("-")) {
                return new RangeInt(0, i2);
            }
            if (p_parseRangeInt_1_.endsWith("-")) {
                return new RangeInt(i2, 255);
            }
            return new RangeInt(i2, i2);
        }
        else {
            if (aint.length == 2) {
                final int l = Math.min(aint[0], aint[1]);
                final int j2 = Math.max(aint[0], aint[1]);
                return new RangeInt(l, j2);
            }
            Config.warn("Invalid range: " + p_parseRangeInt_1_);
            return null;
        }
    }
    
    private NbtTagValue[] parseNbtTagValues(final Properties p_parseNbtTagValues_1_) {
        final String s = "nbt.";
        final Map map = getMatchingProperties(p_parseNbtTagValues_1_, s);
        if (map.size() <= 0) {
            return null;
        }
        final List list = new ArrayList();
        for (final Object s2 : map.keySet()) {
            final String s3 = map.get(s2);
            final String s4 = ((String)s2).substring(s.length());
            final NbtTagValue nbttagvalue = new NbtTagValue(s4, s3);
            list.add(nbttagvalue);
        }
        final NbtTagValue[] anbttagvalue = list.toArray(new NbtTagValue[list.size()]);
        return anbttagvalue;
    }
    
    private static Map getMatchingProperties(final Properties p_getMatchingProperties_0_, final String p_getMatchingProperties_1_) {
        final Map map = new LinkedHashMap();
        for (final Object s : p_getMatchingProperties_0_.keySet()) {
            final String s2 = p_getMatchingProperties_0_.getProperty((String)s);
            if (((String)s).startsWith(p_getMatchingProperties_1_)) {
                map.put(s, s2);
            }
        }
        return map;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.name == null || this.name.length() <= 0) {
            Config.warn("No name found: " + p_isValid_1_);
            return false;
        }
        if (this.basePath == null) {
            Config.warn("No base path found: " + p_isValid_1_);
            return false;
        }
        if (this.type == 0) {
            Config.warn("No type defined: " + p_isValid_1_);
            return false;
        }
        if ((this.type == 1 || this.type == 3) && this.items == null) {
            Config.warn("No items defined: " + p_isValid_1_);
            return false;
        }
        if (this.texture == null && this.mapTextures == null) {
            Config.warn("No texture specified: " + p_isValid_1_);
            return false;
        }
        if (this.type == 2 && this.enchantmentIds == null) {
            Config.warn("No enchantmentIDs specified: " + p_isValid_1_);
            return false;
        }
        return true;
    }
    
    public void updateIcons(final TextureMap p_updateIcons_1_) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);
            if (this.type == 1) {
                final ResourceLocation resourcelocation = this.getSpriteLocation(this.textureLocation);
                this.sprite = p_updateIcons_1_.registerSprite(resourcelocation);
            }
        }
        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            for (final String s : this.mapTextures.keySet()) {
                final String s2 = this.mapTextures.get(s);
                final ResourceLocation resourcelocation2 = this.getTextureLocation(s2);
                this.mapTextureLocations.put(s, resourcelocation2);
                if (this.type == 1) {
                    final ResourceLocation resourcelocation3 = this.getSpriteLocation(resourcelocation2);
                    final TextureAtlasSprite textureatlassprite = p_updateIcons_1_.registerSprite(resourcelocation3);
                    this.mapSprites.put(s, textureatlassprite);
                }
            }
        }
    }
    
    private ResourceLocation getTextureLocation(final String p_getTextureLocation_1_) {
        if (p_getTextureLocation_1_ == null) {
            return null;
        }
        final ResourceLocation resourcelocation = new ResourceLocation(p_getTextureLocation_1_);
        final String s = resourcelocation.getResourceDomain();
        String s2 = resourcelocation.getResourcePath();
        if (!s2.contains("/")) {
            s2 = "textures/blocks/" + s2;
        }
        final String s3 = String.valueOf(s2) + ".png";
        final ResourceLocation resourcelocation2 = new ResourceLocation(s, s3);
        final boolean flag = Config.hasResource(resourcelocation2);
        if (!flag) {
            Config.warn("File not found: " + s3);
        }
        return resourcelocation2;
    }
    
    private ResourceLocation getSpriteLocation(final ResourceLocation p_getSpriteLocation_1_) {
        String s = p_getSpriteLocation_1_.getResourcePath();
        s = StrUtils.removePrefix(s, "textures/");
        s = StrUtils.removeSuffix(s, ".png");
        final ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteLocation_1_.getResourceDomain(), s);
        return resourcelocation;
    }
    
    public void updateModel(final TextureMap p_updateModel_1_, final ItemModelGenerator p_updateModel_2_) {
        final String[] astring = this.getModelTextures();
        final boolean flag = this.isUseTint();
        this.model = makeBakedModel(p_updateModel_1_, p_updateModel_2_, astring, flag);
        if (this.type == 1 && this.mapTextures != null) {
            for (final String s : this.mapTextures.keySet()) {
                final String s2 = this.mapTextures.get(s);
                final String s3 = StrUtils.removePrefix(s, "texture.");
                if (s3.startsWith("bow") || s3.startsWith("fishing_rod")) {
                    final String[] astring2 = { s2 };
                    final IBakedModel ibakedmodel = makeBakedModel(p_updateModel_1_, p_updateModel_2_, astring2, flag);
                    if (this.mapModels == null) {
                        this.mapModels = new HashMap<String, IBakedModel>();
                    }
                    this.mapModels.put(s3, ibakedmodel);
                }
            }
        }
    }
    
    private boolean isUseTint() {
        return true;
    }
    
    private static IBakedModel makeBakedModel(final TextureMap p_makeBakedModel_0_, final ItemModelGenerator p_makeBakedModel_1_, final String[] p_makeBakedModel_2_, final boolean p_makeBakedModel_3_) {
        final ModelBlock modelblock = makeModelBlock(p_makeBakedModel_2_);
        final ModelBlock modelblock2 = p_makeBakedModel_1_.makeItemModel(p_makeBakedModel_0_, modelblock);
        final IBakedModel ibakedmodel = bakeModel(p_makeBakedModel_0_, modelblock2, p_makeBakedModel_3_);
        return ibakedmodel;
    }
    
    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            final Item item = Item.getItemById(this.items[0]);
            if (item == Items.potionitem && this.damage != null && this.damage.getCountRanges() > 0) {
                final RangeInt rangeint = this.damage.getRange(0);
                final int i = rangeint.getMin();
                final boolean flag = (i & 0x4000) != 0x0;
                final String s5 = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
                String s6 = null;
                if (flag) {
                    s6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
                }
                else {
                    s6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                }
                return new String[] { s5, s6 };
            }
            if (item instanceof ItemArmor) {
                final ItemArmor itemarmor = (ItemArmor)item;
                if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                    final String s7 = "leather";
                    String s8 = "helmet";
                    if (itemarmor.armorType == 0) {
                        s8 = "helmet";
                    }
                    if (itemarmor.armorType == 1) {
                        s8 = "chestplate";
                    }
                    if (itemarmor.armorType == 2) {
                        s8 = "leggings";
                    }
                    if (itemarmor.armorType == 3) {
                        s8 = "boots";
                    }
                    final String s9 = String.valueOf(s7) + "_" + s8;
                    final String s10 = this.getMapTexture(this.mapTextures, "texture." + s9, "items/" + s9);
                    final String s11 = this.getMapTexture(this.mapTextures, "texture." + s9 + "_overlay", "items/" + s9 + "_overlay");
                    return new String[] { s10, s11 };
                }
            }
        }
        return new String[] { this.texture };
    }
    
    private String getMapTexture(final Map<String, String> p_getMapTexture_1_, final String p_getMapTexture_2_, final String p_getMapTexture_3_) {
        if (p_getMapTexture_1_ == null) {
            return p_getMapTexture_3_;
        }
        final String s = p_getMapTexture_1_.get(p_getMapTexture_2_);
        return (s == null) ? p_getMapTexture_3_ : s;
    }
    
    private static ModelBlock makeModelBlock(final String[] p_makeModelBlock_0_) {
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("{\"parent\": \"builtin/generated\",\"textures\": {");
        for (int i = 0; i < p_makeModelBlock_0_.length; ++i) {
            final String s = p_makeModelBlock_0_[i];
            if (i > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append("\"layer" + i + "\": \"" + s + "\"");
        }
        stringbuffer.append("}}");
        final String s2 = stringbuffer.toString();
        final ModelBlock modelblock = ModelBlock.deserialize(s2);
        return modelblock;
    }
    
    private static IBakedModel bakeModel(final TextureMap p_bakeModel_0_, final ModelBlock p_bakeModel_1_, final boolean p_bakeModel_2_) {
        final ModelRotation modelrotation = ModelRotation.X0_Y0;
        final boolean flag = false;
        final TextureAtlasSprite textureatlassprite = p_bakeModel_0_.getSpriteSafe(p_bakeModel_1_.resolveTextureName("particle"));
        final SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(p_bakeModel_1_).setTexture(textureatlassprite);
        for (final BlockPart blockpart : p_bakeModel_1_.getElements()) {
            for (final EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                if (!p_bakeModel_2_) {
                    blockpartface = new BlockPartFace(blockpartface.cullFace, -1, blockpartface.texture, blockpartface.blockFaceUV);
                }
                final TextureAtlasSprite textureatlassprite2 = p_bakeModel_0_.getSpriteSafe(p_bakeModel_1_.resolveTextureName(blockpartface.texture));
                final BakedQuad bakedquad = makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelrotation, flag);
                if (blockpartface.cullFace == null) {
                    simplebakedmodel$builder.addGeneralQuad(bakedquad);
                }
                else {
                    simplebakedmodel$builder.addFaceQuad(modelrotation.rotateFace(blockpartface.cullFace), bakedquad);
                }
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }
    
    private static BakedQuad makeBakedQuad(final BlockPart p_makeBakedQuad_0_, final BlockPartFace p_makeBakedQuad_1_, final TextureAtlasSprite p_makeBakedQuad_2_, final EnumFacing p_makeBakedQuad_3_, final ModelRotation p_makeBakedQuad_4_, final boolean p_makeBakedQuad_5_) {
        final FaceBakery facebakery = new FaceBakery();
        return facebakery.makeBakedQuad(p_makeBakedQuad_0_.positionFrom, p_makeBakedQuad_0_.positionTo, p_makeBakedQuad_1_, p_makeBakedQuad_2_, p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_0_.partRotation, p_makeBakedQuad_5_, p_makeBakedQuad_0_.shade);
    }
    
    @Override
    public String toString() {
        return this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
    }
    
    public float getTextureWidth(final TextureManager p_getTextureWidth_1_) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject itextureobject = p_getTextureWidth_1_.getTexture(this.textureLocation);
                final int i = itextureobject.getGlTextureId();
                final int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                this.textureWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                GlStateManager.bindTexture(j);
            }
            if (this.textureWidth <= 0) {
                this.textureWidth = 16;
            }
        }
        return (float)this.textureWidth;
    }
    
    public float getTextureHeight(final TextureManager p_getTextureHeight_1_) {
        if (this.textureHeight <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject itextureobject = p_getTextureHeight_1_.getTexture(this.textureLocation);
                final int i = itextureobject.getGlTextureId();
                final int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                this.textureHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                GlStateManager.bindTexture(j);
            }
            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }
        return (float)this.textureHeight;
    }
    
    public IBakedModel getModel(final ModelResourceLocation p_getModel_1_) {
        if (p_getModel_1_ != null && this.mapTextures != null) {
            final String s = p_getModel_1_.getResourcePath();
            if (this.mapModels != null) {
                final IBakedModel ibakedmodel = this.mapModels.get(s);
                if (ibakedmodel != null) {
                    return ibakedmodel;
                }
            }
        }
        return this.model;
    }
}
