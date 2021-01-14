package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class CustomItems
{
    private static CustomItemProperties[][] itemProperties = (CustomItemProperties[][])null;
    private static CustomItemProperties[][] enchantmentProperties = (CustomItemProperties[][])null;
    private static Map mapPotionIds = null;
    private static ItemModelGenerator itemModelGenerator = new ItemModelGenerator();
    private static boolean useGlint = true;
    public static final int MASK_POTION_SPLASH = 16384;
    public static final int MASK_POTION_NAME = 63;
    public static final String KEY_TEXTURE_OVERLAY = "texture.potion_overlay";
    public static final String KEY_TEXTURE_SPLASH = "texture.potion_bottle_splash";
    public static final String KEY_TEXTURE_DRINKABLE = "texture.potion_bottle_drinkable";
    public static final String DEFAULT_TEXTURE_OVERLAY = "items/potion_overlay";
    public static final String DEFAULT_TEXTURE_SPLASH = "items/potion_bottle_splash";
    public static final String DEFAULT_TEXTURE_DRINKABLE = "items/potion_bottle_drinkable";
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final int[][] EMPTY_INT2_ARRAY = new int[0][];

    public static void updateIcons(TextureMap p_updateIcons_0_)
    {
        itemProperties = (CustomItemProperties[][])null;
        enchantmentProperties = (CustomItemProperties[][])null;
        useGlint = true;

        if (Config.isCustomItems())
        {
            readCitProperties("mcpatcher/cit.properties");
            IResourcePack[] airesourcepack = Config.getResourcePacks();

            for (int i = airesourcepack.length - 1; i >= 0; --i)
            {
                IResourcePack iresourcepack = airesourcepack[i];
                updateIcons(p_updateIcons_0_, iresourcepack);
            }

            updateIcons(p_updateIcons_0_, Config.getDefaultResourcePack());

            if (itemProperties.length <= 0)
            {
                itemProperties = (CustomItemProperties[][])null;
            }

            if (enchantmentProperties.length <= 0)
            {
                enchantmentProperties = (CustomItemProperties[][])null;
            }
        }
    }

    private static void readCitProperties(String p_readCitProperties_0_)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(p_readCitProperties_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return;
            }

            Config.dbg("CustomItems: Loading " + p_readCitProperties_0_);
            Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            useGlint = Config.parseBoolean(properties.getProperty("useGlint"), true);
        }
        catch (FileNotFoundException var4)
        {
            return;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private static void updateIcons(TextureMap p_updateIcons_0_, IResourcePack p_updateIcons_1_)
    {
        String[] astring = ResUtils.collectFiles(p_updateIcons_1_, (String)"mcpatcher/cit/", (String)".properties", (String[])null);
        Map map = makeAutoImageProperties(p_updateIcons_1_);

        if (map.size() > 0)
        {
            Set set = map.keySet();
            String[] astring1 = (String[])((String[])set.toArray(new String[set.size()]));
            astring = (String[])((String[])Config.addObjectsToArray(astring, astring1));
        }

        Arrays.sort((Object[])astring);
        List list = makePropertyList(itemProperties);
        List list1 = makePropertyList(enchantmentProperties);

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            Config.dbg("CustomItems: " + s);

            try
            {
                CustomItemProperties customitemproperties = null;

                if (map.containsKey(s))
                {
                    customitemproperties = (CustomItemProperties)map.get(s);
                }

                if (customitemproperties == null)
                {
                    ResourceLocation resourcelocation = new ResourceLocation(s);
                    InputStream inputstream = p_updateIcons_1_.getInputStream(resourcelocation);

                    if (inputstream == null)
                    {
                        Config.warn("CustomItems file not found: " + s);
                        continue;
                    }

                    Properties properties = new Properties();
                    properties.load(inputstream);
                    customitemproperties = new CustomItemProperties(properties, s);
                }

                if (customitemproperties.isValid(s))
                {
                    customitemproperties.updateIcons(p_updateIcons_0_);
                    addToItemList(customitemproperties, list);
                    addToEnchantmentList(customitemproperties, list1);
                }
            }
            catch (FileNotFoundException var12)
            {
                Config.warn("CustomItems file not found: " + s);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        itemProperties = propertyListToArray(list);
        enchantmentProperties = propertyListToArray(list1);
        Comparator comparator = getPropertiesComparator();

        for (int j = 0; j < itemProperties.length; ++j)
        {
            CustomItemProperties[] acustomitemproperties = itemProperties[j];

            if (acustomitemproperties != null)
            {
                Arrays.sort(acustomitemproperties, comparator);
            }
        }

        for (int k = 0; k < enchantmentProperties.length; ++k)
        {
            CustomItemProperties[] acustomitemproperties1 = enchantmentProperties[k];

            if (acustomitemproperties1 != null)
            {
                Arrays.sort(acustomitemproperties1, comparator);
            }
        }
    }

    private static Comparator getPropertiesComparator()
    {
        Comparator comparator = new Comparator()
        {
            public int compare(Object p_compare_1_, Object p_compare_2_)
            {
                CustomItemProperties customitemproperties = (CustomItemProperties)p_compare_1_;
                CustomItemProperties customitemproperties1 = (CustomItemProperties)p_compare_2_;
                return customitemproperties.layer != customitemproperties1.layer ? customitemproperties.layer - customitemproperties1.layer : (customitemproperties.weight != customitemproperties1.weight ? customitemproperties1.weight - customitemproperties.weight : (!customitemproperties.basePath.equals(customitemproperties1.basePath) ? customitemproperties.basePath.compareTo(customitemproperties1.basePath) : customitemproperties.name.compareTo(customitemproperties1.name)));
            }
        };
        return comparator;
    }

    public static void updateModels()
    {
        if (itemProperties != null)
        {
            for (int i = 0; i < itemProperties.length; ++i)
            {
                CustomItemProperties[] acustomitemproperties = itemProperties[i];

                if (acustomitemproperties != null)
                {
                    for (int j = 0; j < acustomitemproperties.length; ++j)
                    {
                        CustomItemProperties customitemproperties = acustomitemproperties[j];

                        if (customitemproperties != null && customitemproperties.type == 1)
                        {
                            TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
                            customitemproperties.updateModel(texturemap, itemModelGenerator);
                        }
                    }
                }
            }
        }
    }

    private static Map makeAutoImageProperties(IResourcePack p_makeAutoImageProperties_0_)
    {
        Map map = new HashMap();
        map.putAll(makePotionImageProperties(p_makeAutoImageProperties_0_, false));
        map.putAll(makePotionImageProperties(p_makeAutoImageProperties_0_, true));
        return map;
    }

    private static Map makePotionImageProperties(IResourcePack p_makePotionImageProperties_0_, boolean p_makePotionImageProperties_1_)
    {
        Map map = new HashMap();
        String s = p_makePotionImageProperties_1_ ? "splash/" : "normal/";
        String[] astring = new String[] {"mcpatcher/cit/potion/" + s, "mcpatcher/cit/Potion/" + s};
        String[] astring1 = new String[] {".png"};
        String[] astring2 = ResUtils.collectFiles(p_makePotionImageProperties_0_, astring, astring1);

        for (int i = 0; i < astring2.length; ++i)
        {
            String s1 = astring2[i];
            String name = StrUtils.removePrefixSuffix(s1, astring, astring1);
            Properties properties = makePotionProperties(name, p_makePotionImageProperties_1_, s1);

            if (properties != null)
            {
                String s3 = StrUtils.removeSuffix(s1, astring1) + ".properties";
                CustomItemProperties customitemproperties = new CustomItemProperties(properties, s3);
                map.put(s3, customitemproperties);
            }
        }

        return map;
    }

    private static Properties makePotionProperties(String p_makePotionProperties_0_, boolean p_makePotionProperties_1_, String p_makePotionProperties_2_)
    {
        if (StrUtils.endsWith(p_makePotionProperties_0_, new String[] {"_n", "_s"}))
        {
            return null;
        }
        else if (p_makePotionProperties_0_.equals("empty") && !p_makePotionProperties_1_)
        {
            int l = Item.getIdFromItem(Items.glass_bottle);
            Properties properties = new Properties();
            properties.put("type", "item");
            properties.put("items", "" + l);
            return properties;
        }
        else
        {
            int i = Item.getIdFromItem(Items.potionitem);
            int[] aint = (int[])((int[])getMapPotionIds().get(p_makePotionProperties_0_));

            if (aint == null)
            {
                Config.warn("Potion not found for image: " + p_makePotionProperties_2_);
                return null;
            }
            else
            {
                StringBuffer stringbuffer = new StringBuffer();

                for (int j = 0; j < aint.length; ++j)
                {
                    int k = aint[j];

                    if (p_makePotionProperties_1_)
                    {
                        k |= 16384;
                    }

                    if (j > 0)
                    {
                        stringbuffer.append(" ");
                    }

                    stringbuffer.append(k);
                }

                int i1 = 16447;
                Properties properties1 = new Properties();
                properties1.put("type", "item");
                properties1.put("items", "" + i);
                properties1.put("damage", "" + stringbuffer.toString());
                properties1.put("damageMask", "" + i1);

                if (p_makePotionProperties_1_)
                {
                    properties1.put("texture.potion_bottle_splash", p_makePotionProperties_0_);
                }
                else
                {
                    properties1.put("texture.potion_bottle_drinkable", p_makePotionProperties_0_);
                }

                return properties1;
            }
        }
    }

    private static Map getMapPotionIds()
    {
        if (mapPotionIds == null)
        {
            mapPotionIds = new LinkedHashMap();
            mapPotionIds.put("water", new int[] {0});
            mapPotionIds.put("awkward", new int[] {16});
            mapPotionIds.put("thick", new int[] {32});
            mapPotionIds.put("potent", new int[] {48});
            mapPotionIds.put("regeneration", getPotionIds(1));
            mapPotionIds.put("moveSpeed", getPotionIds(2));
            mapPotionIds.put("fireResistance", getPotionIds(3));
            mapPotionIds.put("poison", getPotionIds(4));
            mapPotionIds.put("heal", getPotionIds(5));
            mapPotionIds.put("nightVision", getPotionIds(6));
            mapPotionIds.put("clear", getPotionIds(7));
            mapPotionIds.put("bungling", getPotionIds(23));
            mapPotionIds.put("charming", getPotionIds(39));
            mapPotionIds.put("rank", getPotionIds(55));
            mapPotionIds.put("weakness", getPotionIds(8));
            mapPotionIds.put("damageBoost", getPotionIds(9));
            mapPotionIds.put("moveSlowdown", getPotionIds(10));
            mapPotionIds.put("diffuse", getPotionIds(11));
            mapPotionIds.put("smooth", getPotionIds(27));
            mapPotionIds.put("refined", getPotionIds(43));
            mapPotionIds.put("acrid", getPotionIds(59));
            mapPotionIds.put("harm", getPotionIds(12));
            mapPotionIds.put("waterBreathing", getPotionIds(13));
            mapPotionIds.put("invisibility", getPotionIds(14));
            mapPotionIds.put("thin", getPotionIds(15));
            mapPotionIds.put("debonair", getPotionIds(31));
            mapPotionIds.put("sparkling", getPotionIds(47));
            mapPotionIds.put("stinky", getPotionIds(63));
        }

        return mapPotionIds;
    }

    private static int[] getPotionIds(int p_getPotionIds_0_)
    {
        return new int[] {p_getPotionIds_0_, p_getPotionIds_0_ + 16, p_getPotionIds_0_ + 32, p_getPotionIds_0_ + 48};
    }

    private static int getPotionNameDamage(String p_getPotionNameDamage_0_)
    {
        String s = "potion." + p_getPotionNameDamage_0_;
        Potion[] apotion = Potion.potionTypes;

        for (int i = 0; i < apotion.length; ++i)
        {
            Potion potion = apotion[i];

            if (potion != null)
            {
                String s1 = potion.getName();

                if (s.equals(s1))
                {
                    return potion.getId();
                }
            }
        }

        return -1;
    }

    private static List makePropertyList(CustomItemProperties[][] p_makePropertyList_0_)
    {
        List list = new ArrayList();

        if (p_makePropertyList_0_ != null)
        {
            for (int i = 0; i < p_makePropertyList_0_.length; ++i)
            {
                CustomItemProperties[] acustomitemproperties = p_makePropertyList_0_[i];
                List list1 = null;

                if (acustomitemproperties != null)
                {
                    list1 = new ArrayList(Arrays.asList(acustomitemproperties));
                }

                list.add(list1);
            }
        }

        return list;
    }

    private static CustomItemProperties[][] propertyListToArray(List p_propertyListToArray_0_)
    {
        CustomItemProperties[][] acustomitemproperties = new CustomItemProperties[p_propertyListToArray_0_.size()][];

        for (int i = 0; i < p_propertyListToArray_0_.size(); ++i)
        {
            List list = (List)p_propertyListToArray_0_.get(i);

            if (list != null)
            {
                CustomItemProperties[] acustomitemproperties1 = (CustomItemProperties[])((CustomItemProperties[])list.toArray(new CustomItemProperties[list.size()]));
                Arrays.sort(acustomitemproperties1, new CustomItemsComparator());
                acustomitemproperties[i] = acustomitemproperties1;
            }
        }

        return acustomitemproperties;
    }

    private static void addToItemList(CustomItemProperties p_addToItemList_0_, List p_addToItemList_1_)
    {
        if (p_addToItemList_0_.items != null)
        {
            for (int i = 0; i < p_addToItemList_0_.items.length; ++i)
            {
                int j = p_addToItemList_0_.items[i];

                if (j <= 0)
                {
                    Config.warn("Invalid item ID: " + j);
                }
                else
                {
                    addToList(p_addToItemList_0_, p_addToItemList_1_, j);
                }
            }
        }
    }

    private static void addToEnchantmentList(CustomItemProperties p_addToEnchantmentList_0_, List p_addToEnchantmentList_1_)
    {
        if (p_addToEnchantmentList_0_.type == 2)
        {
            if (p_addToEnchantmentList_0_.enchantmentIds != null)
            {
                for (int i = 0; i < 256; ++i)
                {
                    if (p_addToEnchantmentList_0_.enchantmentIds.isInRange(i))
                    {
                        addToList(p_addToEnchantmentList_0_, p_addToEnchantmentList_1_, i);
                    }
                }
            }
        }
    }

    private static void addToList(CustomItemProperties p_addToList_0_, List p_addToList_1_, int p_addToList_2_)
    {
        while (p_addToList_2_ >= p_addToList_1_.size())
        {
            p_addToList_1_.add(null);
        }

        List list = (List)p_addToList_1_.get(p_addToList_2_);

        if (list == null)
        {
            list = new ArrayList();
            p_addToList_1_.set(p_addToList_2_, list);
        }

        list.add(p_addToList_0_);
    }

    public static IBakedModel getCustomItemModel(ItemStack p_getCustomItemModel_0_, IBakedModel p_getCustomItemModel_1_, ModelResourceLocation p_getCustomItemModel_2_)
    {
        if (p_getCustomItemModel_1_.isGui3d())
        {
            return p_getCustomItemModel_1_;
        }
        else if (itemProperties == null)
        {
            return p_getCustomItemModel_1_;
        }
        else
        {
            CustomItemProperties customitemproperties = getCustomItemProperties(p_getCustomItemModel_0_, 1);
            return customitemproperties == null ? p_getCustomItemModel_1_ : customitemproperties.getModel(p_getCustomItemModel_2_);
        }
    }

    public static boolean bindCustomArmorTexture(ItemStack p_bindCustomArmorTexture_0_, int p_bindCustomArmorTexture_1_, String p_bindCustomArmorTexture_2_)
    {
        if (itemProperties == null)
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = getCustomArmorLocation(p_bindCustomArmorTexture_0_, p_bindCustomArmorTexture_1_, p_bindCustomArmorTexture_2_);

            if (resourcelocation == null)
            {
                return false;
            }
            else
            {
                Config.getTextureManager().bindTexture(resourcelocation);
                return true;
            }
        }
    }

    private static ResourceLocation getCustomArmorLocation(ItemStack p_getCustomArmorLocation_0_, int p_getCustomArmorLocation_1_, String p_getCustomArmorLocation_2_)
    {
        CustomItemProperties customitemproperties = getCustomItemProperties(p_getCustomArmorLocation_0_, 3);

        if (customitemproperties == null)
        {
            return null;
        }
        else if (customitemproperties.mapTextureLocations == null)
        {
            return null;
        }
        else
        {
            Item item = p_getCustomArmorLocation_0_.getItem();

            if (!(item instanceof ItemArmor))
            {
                return null;
            }
            else
            {
                ItemArmor itemarmor = (ItemArmor)item;
                String s = itemarmor.getArmorMaterial().getName();
                StringBuffer stringbuffer = new StringBuffer();
                stringbuffer.append("texture.");
                stringbuffer.append(s);
                stringbuffer.append("_layer_");
                stringbuffer.append(p_getCustomArmorLocation_1_);

                if (p_getCustomArmorLocation_2_ != null)
                {
                    stringbuffer.append("_");
                    stringbuffer.append(p_getCustomArmorLocation_2_);
                }

                String s1 = stringbuffer.toString();
                ResourceLocation resourcelocation = (ResourceLocation)customitemproperties.mapTextureLocations.get(s1);
                return resourcelocation;
            }
        }
    }

    private static CustomItemProperties getCustomItemProperties(ItemStack p_getCustomItemProperties_0_, int p_getCustomItemProperties_1_)
    {
        if (itemProperties == null)
        {
            return null;
        }
        else if (p_getCustomItemProperties_0_ == null)
        {
            return null;
        }
        else
        {
            Item item = p_getCustomItemProperties_0_.getItem();
            int i = Item.getIdFromItem(item);

            if (i >= 0 && i < itemProperties.length)
            {
                CustomItemProperties[] acustomitemproperties = itemProperties[i];

                if (acustomitemproperties != null)
                {
                    for (int j = 0; j < acustomitemproperties.length; ++j)
                    {
                        CustomItemProperties customitemproperties = acustomitemproperties[j];

                        if (customitemproperties.type == p_getCustomItemProperties_1_ && matchesProperties(customitemproperties, p_getCustomItemProperties_0_, (int[][])null))
                        {
                            return customitemproperties;
                        }
                    }
                }
            }

            return null;
        }
    }

    private static boolean matchesProperties(CustomItemProperties p_matchesProperties_0_, ItemStack p_matchesProperties_1_, int[][] p_matchesProperties_2_)
    {
        Item item = p_matchesProperties_1_.getItem();

        if (p_matchesProperties_0_.damage != null)
        {
            int i = p_matchesProperties_1_.getItemDamage();

            if (p_matchesProperties_0_.damageMask != 0)
            {
                i &= p_matchesProperties_0_.damageMask;
            }

            if (p_matchesProperties_0_.damagePercent)
            {
                int j = item.getMaxDamage();
                i = (int)((double)(i * 100) / (double)j);
            }

            if (!p_matchesProperties_0_.damage.isInRange(i))
            {
                return false;
            }
        }

        if (p_matchesProperties_0_.stackSize != null && !p_matchesProperties_0_.stackSize.isInRange(p_matchesProperties_1_.stackSize))
        {
            return false;
        }
        else
        {
            int[][] aint = p_matchesProperties_2_;

            if (p_matchesProperties_0_.enchantmentIds != null)
            {
                if (p_matchesProperties_2_ == null)
                {
                    aint = getEnchantmentIdLevels(p_matchesProperties_1_);
                }

                boolean flag = false;

                for (int k = 0; k < aint.length; ++k)
                {
                    int l = aint[k][0];

                    if (p_matchesProperties_0_.enchantmentIds.isInRange(l))
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    return false;
                }
            }

            if (p_matchesProperties_0_.enchantmentLevels != null)
            {
                if (aint == null)
                {
                    aint = getEnchantmentIdLevels(p_matchesProperties_1_);
                }

                boolean flag1 = false;

                for (int i1 = 0; i1 < aint.length; ++i1)
                {
                    int k1 = aint[i1][1];

                    if (p_matchesProperties_0_.enchantmentLevels.isInRange(k1))
                    {
                        flag1 = true;
                        break;
                    }
                }

                if (!flag1)
                {
                    return false;
                }
            }

            if (p_matchesProperties_0_.nbtTagValues != null)
            {
                NBTTagCompound nbttagcompound = p_matchesProperties_1_.getTagCompound();

                for (int j1 = 0; j1 < p_matchesProperties_0_.nbtTagValues.length; ++j1)
                {
                    NbtTagValue nbttagvalue = p_matchesProperties_0_.nbtTagValues[j1];

                    if (!nbttagvalue.matches(nbttagcompound))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private static int[][] getEnchantmentIdLevels(ItemStack p_getEnchantmentIdLevels_0_)
    {
        Item item = p_getEnchantmentIdLevels_0_.getItem();
        NBTTagList nbttaglist = item == Items.enchanted_book ? Items.enchanted_book.getEnchantments(p_getEnchantmentIdLevels_0_) : p_getEnchantmentIdLevels_0_.getEnchantmentTagList();

        if (nbttaglist != null && nbttaglist.tagCount() > 0)
        {
            int[][] aint = new int[nbttaglist.tagCount()][2];

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getShort("id");
                int k = nbttagcompound.getShort("lvl");
                aint[i][0] = j;
                aint[i][1] = k;
            }

            return aint;
        }
        else
        {
            return EMPTY_INT2_ARRAY;
        }
    }

    public static boolean renderCustomEffect(RenderItem p_renderCustomEffect_0_, ItemStack p_renderCustomEffect_1_, IBakedModel p_renderCustomEffect_2_)
    {
        if (enchantmentProperties == null)
        {
            return false;
        }
        else if (p_renderCustomEffect_1_ == null)
        {
            return false;
        }
        else
        {
            int[][] aint = getEnchantmentIdLevels(p_renderCustomEffect_1_);

            if (aint.length <= 0)
            {
                return false;
            }
            else
            {
                Set set = null;
                boolean flag = false;
                TextureManager texturemanager = Config.getTextureManager();

                for (int i = 0; i < aint.length; ++i)
                {
                    int j = aint[i][0];

                    if (j >= 0 && j < enchantmentProperties.length)
                    {
                        CustomItemProperties[] acustomitemproperties = enchantmentProperties[j];

                        if (acustomitemproperties != null)
                        {
                            for (int k = 0; k < acustomitemproperties.length; ++k)
                            {
                                CustomItemProperties customitemproperties = acustomitemproperties[k];

                                if (set == null)
                                {
                                    set = new HashSet();
                                }

                                if (set.add(Integer.valueOf(j)) && matchesProperties(customitemproperties, p_renderCustomEffect_1_, aint) && customitemproperties.textureLocation != null)
                                {
                                    texturemanager.bindTexture(customitemproperties.textureLocation);
                                    float f = customitemproperties.getTextureWidth(texturemanager);

                                    if (!flag)
                                    {
                                        flag = true;
                                        GlStateManager.depthMask(false);
                                        GlStateManager.depthFunc(514);
                                        GlStateManager.disableLighting();
                                        GlStateManager.matrixMode(5890);
                                    }

                                    Blender.setupBlend(customitemproperties.blend, 1.0F);
                                    GlStateManager.pushMatrix();
                                    GlStateManager.scale(f / 2.0F, f / 2.0F, f / 2.0F);
                                    float f1 = customitemproperties.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                                    GlStateManager.translate(f1, 0.0F, 0.0F);
                                    GlStateManager.rotate(customitemproperties.rotation, 0.0F, 0.0F, 1.0F);
                                    p_renderCustomEffect_0_.renderModel(p_renderCustomEffect_2_, -1);
                                    GlStateManager.popMatrix();
                                }
                            }
                        }
                    }
                }

                if (flag)
                {
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableLighting();
                    GlStateManager.depthFunc(515);
                    GlStateManager.depthMask(true);
                    texturemanager.bindTexture(TextureMap.locationBlocksTexture);
                }

                return flag;
            }
        }
    }

    public static boolean renderCustomArmorEffect(EntityLivingBase p_renderCustomArmorEffect_0_, ItemStack p_renderCustomArmorEffect_1_, ModelBase p_renderCustomArmorEffect_2_, float p_renderCustomArmorEffect_3_, float p_renderCustomArmorEffect_4_, float p_renderCustomArmorEffect_5_, float p_renderCustomArmorEffect_6_, float p_renderCustomArmorEffect_7_, float p_renderCustomArmorEffect_8_, float p_renderCustomArmorEffect_9_)
    {
        if (enchantmentProperties == null)
        {
            return false;
        }
        else if (Config.isShaders() && Shaders.isShadowPass)
        {
            return false;
        }
        else if (p_renderCustomArmorEffect_1_ == null)
        {
            return false;
        }
        else
        {
            int[][] aint = getEnchantmentIdLevels(p_renderCustomArmorEffect_1_);

            if (aint.length <= 0)
            {
                return false;
            }
            else
            {
                Set set = null;
                boolean flag = false;
                TextureManager texturemanager = Config.getTextureManager();

                for (int i = 0; i < aint.length; ++i)
                {
                    int j = aint[i][0];

                    if (j >= 0 && j < enchantmentProperties.length)
                    {
                        CustomItemProperties[] acustomitemproperties = enchantmentProperties[j];

                        if (acustomitemproperties != null)
                        {
                            for (int k = 0; k < acustomitemproperties.length; ++k)
                            {
                                CustomItemProperties customitemproperties = acustomitemproperties[k];

                                if (set == null)
                                {
                                    set = new HashSet();
                                }

                                if (set.add(Integer.valueOf(j)) && matchesProperties(customitemproperties, p_renderCustomArmorEffect_1_, aint) && customitemproperties.textureLocation != null)
                                {
                                    texturemanager.bindTexture(customitemproperties.textureLocation);
                                    float f = customitemproperties.getTextureWidth(texturemanager);

                                    if (!flag)
                                    {
                                        flag = true;

                                        if (Config.isShaders())
                                        {
                                            ShadersRender.renderEnchantedGlintBegin();
                                        }

                                        GlStateManager.enableBlend();
                                        GlStateManager.depthFunc(514);
                                        GlStateManager.depthMask(false);
                                    }

                                    Blender.setupBlend(customitemproperties.blend, 1.0F);
                                    GlStateManager.disableLighting();
                                    GlStateManager.matrixMode(5890);
                                    GlStateManager.loadIdentity();
                                    GlStateManager.rotate(customitemproperties.rotation, 0.0F, 0.0F, 1.0F);
                                    float f1 = f / 8.0F;
                                    GlStateManager.scale(f1, f1 / 2.0F, f1);
                                    float f2 = customitemproperties.speed * (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                                    GlStateManager.translate(0.0F, f2, 0.0F);
                                    GlStateManager.matrixMode(5888);
                                    p_renderCustomArmorEffect_2_.render(p_renderCustomArmorEffect_0_, p_renderCustomArmorEffect_3_, p_renderCustomArmorEffect_4_, p_renderCustomArmorEffect_6_, p_renderCustomArmorEffect_7_, p_renderCustomArmorEffect_8_, p_renderCustomArmorEffect_9_);
                                }
                            }
                        }
                    }
                }

                if (flag)
                {
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.loadIdentity();
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableLighting();
                    GlStateManager.depthMask(true);
                    GlStateManager.depthFunc(515);
                    GlStateManager.disableBlend();

                    if (Config.isShaders())
                    {
                        ShadersRender.renderEnchantedGlintEnd();
                    }
                }

                return flag;
            }
        }
    }

    public static boolean isUseGlint()
    {
        return useGlint;
    }
}
