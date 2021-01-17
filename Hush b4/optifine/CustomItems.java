// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.entity.Entity;
import shadersmod.client.ShadersRender;
import shadersmod.client.Shaders;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.GlStateManager;
import java.util.HashSet;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.potion.Potion;
import java.util.LinkedHashMap;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.Arrays;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import java.util.Map;

public class CustomItems
{
    private static CustomItemProperties[][] itemProperties;
    private static CustomItemProperties[][] enchantmentProperties;
    private static Map mapPotionIds;
    private static ItemModelGenerator itemModelGenerator;
    private static boolean useGlint;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[] EMPTY_INT_ARRAY;
    private static final int[][] EMPTY_INT2_ARRAY;
    
    static {
        CustomItems.itemProperties = null;
        CustomItems.enchantmentProperties = null;
        CustomItems.mapPotionIds = null;
        CustomItems.itemModelGenerator = new ItemModelGenerator();
        CustomItems.useGlint = true;
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_INT2_ARRAY = new int[0][];
    }
    
    public static void updateIcons(final TextureMap p_updateIcons_0_) {
        CustomItems.itemProperties = null;
        CustomItems.enchantmentProperties = null;
        CustomItems.useGlint = true;
        if (Config.isCustomItems()) {
            readCitProperties("mcpatcher/cit.properties");
            final IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                final IResourcePack iresourcepack = airesourcepack[i];
                updateIcons(p_updateIcons_0_, iresourcepack);
            }
            updateIcons(p_updateIcons_0_, Config.getDefaultResourcePack());
            if (CustomItems.itemProperties.length <= 0) {
                CustomItems.itemProperties = null;
            }
            if (CustomItems.enchantmentProperties.length <= 0) {
                CustomItems.enchantmentProperties = null;
            }
        }
    }
    
    private static void readCitProperties(final String p_readCitProperties_0_) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(p_readCitProperties_0_);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            Config.dbg("CustomItems: Loading " + p_readCitProperties_0_);
            final Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            CustomItems.useGlint = Config.parseBoolean(properties.getProperty("useGlint"), true);
        }
        catch (FileNotFoundException var4) {}
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private static void updateIcons(final TextureMap p_updateIcons_0_, final IResourcePack p_updateIcons_1_) {
        String[] astring = ResUtils.collectFiles(p_updateIcons_1_, "mcpatcher/cit/", ".properties", null);
        final Map map = makeAutoImageProperties(p_updateIcons_1_);
        if (map.size() > 0) {
            final Set set = map.keySet();
            final String[] astring2 = set.toArray(new String[set.size()]);
            astring = (String[])Config.addObjectsToArray(astring, astring2);
        }
        Arrays.sort(astring);
        final List list = makePropertyList(CustomItems.itemProperties);
        final List list2 = makePropertyList(CustomItems.enchantmentProperties);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("CustomItems: " + s);
            try {
                CustomItemProperties customitemproperties = null;
                if (map.containsKey(s)) {
                    customitemproperties = map.get(s);
                }
                if (customitemproperties == null) {
                    final ResourceLocation resourcelocation = new ResourceLocation(s);
                    final InputStream inputstream = p_updateIcons_1_.getInputStream(resourcelocation);
                    if (inputstream == null) {
                        Config.warn("CustomItems file not found: " + s);
                        continue;
                    }
                    final Properties properties = new Properties();
                    properties.load(inputstream);
                    customitemproperties = new CustomItemProperties(properties, s);
                }
                if (customitemproperties.isValid(s)) {
                    customitemproperties.updateIcons(p_updateIcons_0_);
                    addToItemList(customitemproperties, list);
                    addToEnchantmentList(customitemproperties, list2);
                }
            }
            catch (FileNotFoundException var12) {
                Config.warn("CustomItems file not found: " + s);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        CustomItems.itemProperties = propertyListToArray(list);
        CustomItems.enchantmentProperties = propertyListToArray(list2);
        final Comparator comparator = getPropertiesComparator();
        for (int j = 0; j < CustomItems.itemProperties.length; ++j) {
            final CustomItemProperties[] acustomitemproperties = CustomItems.itemProperties[j];
            if (acustomitemproperties != null) {
                Arrays.sort(acustomitemproperties, comparator);
            }
        }
        for (int k = 0; k < CustomItems.enchantmentProperties.length; ++k) {
            final CustomItemProperties[] acustomitemproperties2 = CustomItems.enchantmentProperties[k];
            if (acustomitemproperties2 != null) {
                Arrays.sort(acustomitemproperties2, comparator);
            }
        }
    }
    
    private static Comparator getPropertiesComparator() {
        final Comparator comparator = new Comparator() {
            @Override
            public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                final CustomItemProperties customitemproperties = (CustomItemProperties)p_compare_1_;
                final CustomItemProperties customitemproperties2 = (CustomItemProperties)p_compare_2_;
                return (customitemproperties.layer != customitemproperties2.layer) ? (customitemproperties.layer - customitemproperties2.layer) : ((customitemproperties.weight != customitemproperties2.weight) ? (customitemproperties2.weight - customitemproperties.weight) : (customitemproperties.basePath.equals(customitemproperties2.basePath) ? customitemproperties.name.compareTo(customitemproperties2.name) : customitemproperties.basePath.compareTo(customitemproperties2.basePath)));
            }
        };
        return comparator;
    }
    
    public static void updateModels() {
        if (CustomItems.itemProperties != null) {
            for (int i = 0; i < CustomItems.itemProperties.length; ++i) {
                final CustomItemProperties[] acustomitemproperties = CustomItems.itemProperties[i];
                if (acustomitemproperties != null) {
                    for (int j = 0; j < acustomitemproperties.length; ++j) {
                        final CustomItemProperties customitemproperties = acustomitemproperties[j];
                        if (customitemproperties != null && customitemproperties.type == 1) {
                            final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
                            customitemproperties.updateModel(texturemap, CustomItems.itemModelGenerator);
                        }
                    }
                }
            }
        }
    }
    
    private static Map makeAutoImageProperties(final IResourcePack p_makeAutoImageProperties_0_) {
        final Map map = new HashMap();
        map.putAll(makePotionImageProperties(p_makeAutoImageProperties_0_, false));
        map.putAll(makePotionImageProperties(p_makeAutoImageProperties_0_, true));
        return map;
    }
    
    private static Map makePotionImageProperties(final IResourcePack p_makePotionImageProperties_0_, final boolean p_makePotionImageProperties_1_) {
        final Map map = new HashMap();
        final String s = p_makePotionImageProperties_1_ ? "splash/" : "normal/";
        final String[] astring = { "mcpatcher/cit/potion/" + s, "mcpatcher/cit/Potion/" + s };
        final String[] astring2 = { ".png" };
        final String[] astring3 = ResUtils.collectFiles(p_makePotionImageProperties_0_, astring, astring2);
        for (int i = 0; i < astring3.length; ++i) {
            final String s2 = astring3[i];
            final String name = StrUtils.removePrefixSuffix(s2, astring, astring2);
            final Properties properties = makePotionProperties(name, p_makePotionImageProperties_1_, s2);
            if (properties != null) {
                final String s3 = String.valueOf(StrUtils.removeSuffix(s2, astring2)) + ".properties";
                final CustomItemProperties customitemproperties = new CustomItemProperties(properties, s3);
                map.put(s3, customitemproperties);
            }
        }
        return map;
    }
    
    private static Properties makePotionProperties(final String p_makePotionProperties_0_, final boolean p_makePotionProperties_1_, final String p_makePotionProperties_2_) {
        if (StrUtils.endsWith(p_makePotionProperties_0_, new String[] { "_n", "_s" })) {
            return null;
        }
        if (p_makePotionProperties_0_.equals("empty") && !p_makePotionProperties_1_) {
            final int l = Item.getIdFromItem(Items.glass_bottle);
            final Properties properties = new Properties();
            properties.put("type", "item");
            properties.put("items", new StringBuilder().append(l).toString());
            return properties;
        }
        final int i = Item.getIdFromItem(Items.potionitem);
        final int[] aint = getMapPotionIds().get(p_makePotionProperties_0_);
        if (aint == null) {
            Config.warn("Potion not found for image: " + p_makePotionProperties_2_);
            return null;
        }
        final StringBuffer stringbuffer = new StringBuffer();
        for (int j = 0; j < aint.length; ++j) {
            int k = aint[j];
            if (p_makePotionProperties_1_) {
                k |= 0x4000;
            }
            if (j > 0) {
                stringbuffer.append(" ");
            }
            stringbuffer.append(k);
        }
        final int i2 = 16447;
        final Properties properties2 = new Properties();
        properties2.put("type", "item");
        properties2.put("items", new StringBuilder().append(i).toString());
        properties2.put("damage", new StringBuilder().append(stringbuffer.toString()).toString());
        properties2.put("damageMask", new StringBuilder().append(i2).toString());
        if (p_makePotionProperties_1_) {
            properties2.put("texture.potion_bottle_splash", p_makePotionProperties_0_);
        }
        else {
            properties2.put("texture.potion_bottle_drinkable", p_makePotionProperties_0_);
        }
        return properties2;
    }
    
    private static Map getMapPotionIds() {
        if (CustomItems.mapPotionIds == null) {
            (CustomItems.mapPotionIds = new LinkedHashMap()).put("water", new int[1]);
            CustomItems.mapPotionIds.put("awkward", new int[] { 16 });
            CustomItems.mapPotionIds.put("thick", new int[] { 32 });
            CustomItems.mapPotionIds.put("potent", new int[] { 48 });
            CustomItems.mapPotionIds.put("regeneration", getPotionIds(1));
            CustomItems.mapPotionIds.put("moveSpeed", getPotionIds(2));
            CustomItems.mapPotionIds.put("fireResistance", getPotionIds(3));
            CustomItems.mapPotionIds.put("poison", getPotionIds(4));
            CustomItems.mapPotionIds.put("heal", getPotionIds(5));
            CustomItems.mapPotionIds.put("nightVision", getPotionIds(6));
            CustomItems.mapPotionIds.put("clear", getPotionIds(7));
            CustomItems.mapPotionIds.put("bungling", getPotionIds(23));
            CustomItems.mapPotionIds.put("charming", getPotionIds(39));
            CustomItems.mapPotionIds.put("rank", getPotionIds(55));
            CustomItems.mapPotionIds.put("weakness", getPotionIds(8));
            CustomItems.mapPotionIds.put("damageBoost", getPotionIds(9));
            CustomItems.mapPotionIds.put("moveSlowdown", getPotionIds(10));
            CustomItems.mapPotionIds.put("diffuse", getPotionIds(11));
            CustomItems.mapPotionIds.put("smooth", getPotionIds(27));
            CustomItems.mapPotionIds.put("refined", getPotionIds(43));
            CustomItems.mapPotionIds.put("acrid", getPotionIds(59));
            CustomItems.mapPotionIds.put("harm", getPotionIds(12));
            CustomItems.mapPotionIds.put("waterBreathing", getPotionIds(13));
            CustomItems.mapPotionIds.put("invisibility", getPotionIds(14));
            CustomItems.mapPotionIds.put("thin", getPotionIds(15));
            CustomItems.mapPotionIds.put("debonair", getPotionIds(31));
            CustomItems.mapPotionIds.put("sparkling", getPotionIds(47));
            CustomItems.mapPotionIds.put("stinky", getPotionIds(63));
        }
        return CustomItems.mapPotionIds;
    }
    
    private static int[] getPotionIds(final int p_getPotionIds_0_) {
        return new int[] { p_getPotionIds_0_, p_getPotionIds_0_ + 16, p_getPotionIds_0_ + 32, p_getPotionIds_0_ + 48 };
    }
    
    private static int getPotionNameDamage(final String p_getPotionNameDamage_0_) {
        final String s = "potion." + p_getPotionNameDamage_0_;
        final Potion[] apotion = Potion.potionTypes;
        for (int i = 0; i < apotion.length; ++i) {
            final Potion potion = apotion[i];
            if (potion != null) {
                final String s2 = potion.getName();
                if (s.equals(s2)) {
                    return potion.getId();
                }
            }
        }
        return -1;
    }
    
    private static List makePropertyList(final CustomItemProperties[][] p_makePropertyList_0_) {
        final List list = new ArrayList();
        if (p_makePropertyList_0_ != null) {
            for (int i = 0; i < p_makePropertyList_0_.length; ++i) {
                final CustomItemProperties[] acustomitemproperties = p_makePropertyList_0_[i];
                List list2 = null;
                if (acustomitemproperties != null) {
                    list2 = new ArrayList(Arrays.asList(acustomitemproperties));
                }
                list.add(list2);
            }
        }
        return list;
    }
    
    private static CustomItemProperties[][] propertyListToArray(final List p_propertyListToArray_0_) {
        final CustomItemProperties[][] acustomitemproperties = new CustomItemProperties[p_propertyListToArray_0_.size()][];
        for (int i = 0; i < p_propertyListToArray_0_.size(); ++i) {
            final List list = p_propertyListToArray_0_.get(i);
            if (list != null) {
                final CustomItemProperties[] acustomitemproperties2 = list.toArray(new CustomItemProperties[list.size()]);
                Arrays.sort(acustomitemproperties2, new CustomItemsComparator());
                acustomitemproperties[i] = acustomitemproperties2;
            }
        }
        return acustomitemproperties;
    }
    
    private static void addToItemList(final CustomItemProperties p_addToItemList_0_, final List p_addToItemList_1_) {
        if (p_addToItemList_0_.items != null) {
            for (int i = 0; i < p_addToItemList_0_.items.length; ++i) {
                final int j = p_addToItemList_0_.items[i];
                if (j <= 0) {
                    Config.warn("Invalid item ID: " + j);
                }
                else {
                    addToList(p_addToItemList_0_, p_addToItemList_1_, j);
                }
            }
        }
    }
    
    private static void addToEnchantmentList(final CustomItemProperties p_addToEnchantmentList_0_, final List p_addToEnchantmentList_1_) {
        if (p_addToEnchantmentList_0_.type == 2 && p_addToEnchantmentList_0_.enchantmentIds != null) {
            for (int i = 0; i < 256; ++i) {
                if (p_addToEnchantmentList_0_.enchantmentIds.isInRange(i)) {
                    addToList(p_addToEnchantmentList_0_, p_addToEnchantmentList_1_, i);
                }
            }
        }
    }
    
    private static void addToList(final CustomItemProperties p_addToList_0_, final List p_addToList_1_, final int p_addToList_2_) {
        while (p_addToList_2_ >= p_addToList_1_.size()) {
            p_addToList_1_.add(null);
        }
        List list = p_addToList_1_.get(p_addToList_2_);
        if (list == null) {
            list = new ArrayList();
            p_addToList_1_.set(p_addToList_2_, list);
        }
        list.add(p_addToList_0_);
    }
    
    public static IBakedModel getCustomItemModel(final ItemStack p_getCustomItemModel_0_, final IBakedModel p_getCustomItemModel_1_, final ModelResourceLocation p_getCustomItemModel_2_) {
        if (p_getCustomItemModel_1_.isGui3d()) {
            return p_getCustomItemModel_1_;
        }
        if (CustomItems.itemProperties == null) {
            return p_getCustomItemModel_1_;
        }
        final CustomItemProperties customitemproperties = getCustomItemProperties(p_getCustomItemModel_0_, 1);
        return (customitemproperties == null) ? p_getCustomItemModel_1_ : customitemproperties.getModel(p_getCustomItemModel_2_);
    }
    
    public static boolean bindCustomArmorTexture(final ItemStack p_bindCustomArmorTexture_0_, final int p_bindCustomArmorTexture_1_, final String p_bindCustomArmorTexture_2_) {
        if (CustomItems.itemProperties == null) {
            return false;
        }
        final ResourceLocation resourcelocation = getCustomArmorLocation(p_bindCustomArmorTexture_0_, p_bindCustomArmorTexture_1_, p_bindCustomArmorTexture_2_);
        if (resourcelocation == null) {
            return false;
        }
        Config.getTextureManager().bindTexture(resourcelocation);
        return true;
    }
    
    private static ResourceLocation getCustomArmorLocation(final ItemStack p_getCustomArmorLocation_0_, final int p_getCustomArmorLocation_1_, final String p_getCustomArmorLocation_2_) {
        final CustomItemProperties customitemproperties = getCustomItemProperties(p_getCustomArmorLocation_0_, 3);
        if (customitemproperties == null) {
            return null;
        }
        if (customitemproperties.mapTextureLocations == null) {
            return null;
        }
        final Item item = p_getCustomArmorLocation_0_.getItem();
        if (!(item instanceof ItemArmor)) {
            return null;
        }
        final ItemArmor itemarmor = (ItemArmor)item;
        final String s = itemarmor.getArmorMaterial().getName();
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("texture.");
        stringbuffer.append(s);
        stringbuffer.append("_layer_");
        stringbuffer.append(p_getCustomArmorLocation_1_);
        if (p_getCustomArmorLocation_2_ != null) {
            stringbuffer.append("_");
            stringbuffer.append(p_getCustomArmorLocation_2_);
        }
        final String s2 = stringbuffer.toString();
        final ResourceLocation resourcelocation = customitemproperties.mapTextureLocations.get(s2);
        return resourcelocation;
    }
    
    private static CustomItemProperties getCustomItemProperties(final ItemStack p_getCustomItemProperties_0_, final int p_getCustomItemProperties_1_) {
        if (CustomItems.itemProperties == null) {
            return null;
        }
        if (p_getCustomItemProperties_0_ == null) {
            return null;
        }
        final Item item = p_getCustomItemProperties_0_.getItem();
        final int i = Item.getIdFromItem(item);
        if (i >= 0 && i < CustomItems.itemProperties.length) {
            final CustomItemProperties[] acustomitemproperties = CustomItems.itemProperties[i];
            if (acustomitemproperties != null) {
                for (int j = 0; j < acustomitemproperties.length; ++j) {
                    final CustomItemProperties customitemproperties = acustomitemproperties[j];
                    if (customitemproperties.type == p_getCustomItemProperties_1_ && matchesProperties(customitemproperties, p_getCustomItemProperties_0_, null)) {
                        return customitemproperties;
                    }
                }
            }
        }
        return null;
    }
    
    private static boolean matchesProperties(final CustomItemProperties p_matchesProperties_0_, final ItemStack p_matchesProperties_1_, final int[][] p_matchesProperties_2_) {
        final Item item = p_matchesProperties_1_.getItem();
        if (p_matchesProperties_0_.damage != null) {
            int i = p_matchesProperties_1_.getItemDamage();
            if (p_matchesProperties_0_.damageMask != 0) {
                i &= p_matchesProperties_0_.damageMask;
            }
            if (p_matchesProperties_0_.damagePercent) {
                final int j = item.getMaxDamage();
                i = (int)(i * 100 / (double)j);
            }
            if (!p_matchesProperties_0_.damage.isInRange(i)) {
                return false;
            }
        }
        if (p_matchesProperties_0_.stackSize != null && !p_matchesProperties_0_.stackSize.isInRange(p_matchesProperties_1_.stackSize)) {
            return false;
        }
        int[][] aint = p_matchesProperties_2_;
        if (p_matchesProperties_0_.enchantmentIds != null) {
            if (p_matchesProperties_2_ == null) {
                aint = getEnchantmentIdLevels(p_matchesProperties_1_);
            }
            boolean flag = false;
            for (int k = 0; k < aint.length; ++k) {
                final int l = aint[k][0];
                if (p_matchesProperties_0_.enchantmentIds.isInRange(l)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }
        if (p_matchesProperties_0_.enchantmentLevels != null) {
            if (aint == null) {
                aint = getEnchantmentIdLevels(p_matchesProperties_1_);
            }
            boolean flag2 = false;
            for (int i2 = 0; i2 < aint.length; ++i2) {
                final int k2 = aint[i2][1];
                if (p_matchesProperties_0_.enchantmentLevels.isInRange(k2)) {
                    flag2 = true;
                    break;
                }
            }
            if (!flag2) {
                return false;
            }
        }
        if (p_matchesProperties_0_.nbtTagValues != null) {
            final NBTTagCompound nbttagcompound = p_matchesProperties_1_.getTagCompound();
            for (int j2 = 0; j2 < p_matchesProperties_0_.nbtTagValues.length; ++j2) {
                final NbtTagValue nbttagvalue = p_matchesProperties_0_.nbtTagValues[j2];
                if (!nbttagvalue.matches(nbttagcompound)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static int[][] getEnchantmentIdLevels(final ItemStack p_getEnchantmentIdLevels_0_) {
        final Item item = p_getEnchantmentIdLevels_0_.getItem();
        final NBTTagList nbttaglist = (item == Items.enchanted_book) ? Items.enchanted_book.getEnchantments(p_getEnchantmentIdLevels_0_) : p_getEnchantmentIdLevels_0_.getEnchantmentTagList();
        if (nbttaglist != null && nbttaglist.tagCount() > 0) {
            final int[][] aint = new int[nbttaglist.tagCount()][2];
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final int j = nbttagcompound.getShort("id");
                final int k = nbttagcompound.getShort("lvl");
                aint[i][0] = j;
                aint[i][1] = k;
            }
            return aint;
        }
        return CustomItems.EMPTY_INT2_ARRAY;
    }
    
    public static boolean renderCustomEffect(final RenderItem p_renderCustomEffect_0_, final ItemStack p_renderCustomEffect_1_, final IBakedModel p_renderCustomEffect_2_) {
        if (CustomItems.enchantmentProperties == null) {
            return false;
        }
        if (p_renderCustomEffect_1_ == null) {
            return false;
        }
        final int[][] aint = getEnchantmentIdLevels(p_renderCustomEffect_1_);
        if (aint.length <= 0) {
            return false;
        }
        Set set = null;
        boolean flag = false;
        final TextureManager texturemanager = Config.getTextureManager();
        for (int i = 0; i < aint.length; ++i) {
            final int j = aint[i][0];
            if (j >= 0 && j < CustomItems.enchantmentProperties.length) {
                final CustomItemProperties[] acustomitemproperties = CustomItems.enchantmentProperties[j];
                if (acustomitemproperties != null) {
                    for (int k = 0; k < acustomitemproperties.length; ++k) {
                        final CustomItemProperties customitemproperties = acustomitemproperties[k];
                        if (set == null) {
                            set = new HashSet();
                        }
                        if (set.add(j) && matchesProperties(customitemproperties, p_renderCustomEffect_1_, aint) && customitemproperties.textureLocation != null) {
                            texturemanager.bindTexture(customitemproperties.textureLocation);
                            final float f = customitemproperties.getTextureWidth(texturemanager);
                            if (!flag) {
                                flag = true;
                                GlStateManager.depthMask(false);
                                GlStateManager.depthFunc(514);
                                GlStateManager.disableLighting();
                                GlStateManager.matrixMode(5890);
                            }
                            Blender.setupBlend(customitemproperties.blend, 1.0f);
                            GlStateManager.pushMatrix();
                            GlStateManager.scale(f / 2.0f, f / 2.0f, f / 2.0f);
                            final float f2 = customitemproperties.speed * (Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                            GlStateManager.translate(f2, 0.0f, 0.0f);
                            GlStateManager.rotate(customitemproperties.rotation, 0.0f, 0.0f, 1.0f);
                            p_renderCustomEffect_0_.renderModel(p_renderCustomEffect_2_, -1);
                            GlStateManager.popMatrix();
                        }
                    }
                }
            }
        }
        if (flag) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            texturemanager.bindTexture(TextureMap.locationBlocksTexture);
        }
        return flag;
    }
    
    public static boolean renderCustomArmorEffect(final EntityLivingBase p_renderCustomArmorEffect_0_, final ItemStack p_renderCustomArmorEffect_1_, final ModelBase p_renderCustomArmorEffect_2_, final float p_renderCustomArmorEffect_3_, final float p_renderCustomArmorEffect_4_, final float p_renderCustomArmorEffect_5_, final float p_renderCustomArmorEffect_6_, final float p_renderCustomArmorEffect_7_, final float p_renderCustomArmorEffect_8_, final float p_renderCustomArmorEffect_9_) {
        if (CustomItems.enchantmentProperties == null) {
            return false;
        }
        if (Config.isShaders() && Shaders.isShadowPass) {
            return false;
        }
        if (p_renderCustomArmorEffect_1_ == null) {
            return false;
        }
        final int[][] aint = getEnchantmentIdLevels(p_renderCustomArmorEffect_1_);
        if (aint.length <= 0) {
            return false;
        }
        Set set = null;
        boolean flag = false;
        final TextureManager texturemanager = Config.getTextureManager();
        for (int i = 0; i < aint.length; ++i) {
            final int j = aint[i][0];
            if (j >= 0 && j < CustomItems.enchantmentProperties.length) {
                final CustomItemProperties[] acustomitemproperties = CustomItems.enchantmentProperties[j];
                if (acustomitemproperties != null) {
                    for (int k = 0; k < acustomitemproperties.length; ++k) {
                        final CustomItemProperties customitemproperties = acustomitemproperties[k];
                        if (set == null) {
                            set = new HashSet();
                        }
                        if (set.add(j) && matchesProperties(customitemproperties, p_renderCustomArmorEffect_1_, aint) && customitemproperties.textureLocation != null) {
                            texturemanager.bindTexture(customitemproperties.textureLocation);
                            final float f = customitemproperties.getTextureWidth(texturemanager);
                            if (!flag) {
                                flag = true;
                                if (Config.isShaders()) {
                                    ShadersRender.renderEnchantedGlintBegin();
                                }
                                GlStateManager.enableBlend();
                                GlStateManager.depthFunc(514);
                                GlStateManager.depthMask(false);
                            }
                            Blender.setupBlend(customitemproperties.blend, 1.0f);
                            GlStateManager.disableLighting();
                            GlStateManager.matrixMode(5890);
                            GlStateManager.loadIdentity();
                            GlStateManager.rotate(customitemproperties.rotation, 0.0f, 0.0f, 1.0f);
                            final float f2 = f / 8.0f;
                            GlStateManager.scale(f2, f2 / 2.0f, f2);
                            final float f3 = customitemproperties.speed * (Minecraft.getSystemTime() % 3000L) / 3000.0f / 8.0f;
                            GlStateManager.translate(0.0f, f3, 0.0f);
                            GlStateManager.matrixMode(5888);
                            p_renderCustomArmorEffect_2_.render(p_renderCustomArmorEffect_0_, p_renderCustomArmorEffect_3_, p_renderCustomArmorEffect_4_, p_renderCustomArmorEffect_6_, p_renderCustomArmorEffect_7_, p_renderCustomArmorEffect_8_, p_renderCustomArmorEffect_9_);
                        }
                    }
                }
            }
        }
        if (flag) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if (Config.isShaders()) {
                ShadersRender.renderEnchantedGlintEnd();
            }
        }
        return flag;
    }
    
    public static boolean isUseGlint() {
        return CustomItems.useGlint;
    }
}
