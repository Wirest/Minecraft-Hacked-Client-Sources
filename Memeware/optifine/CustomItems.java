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

public class CustomItems {
    private static CustomItemProperties[][] itemProperties = (CustomItemProperties[][]) null;
    private static CustomItemProperties[][] enchantmentProperties = (CustomItemProperties[][]) null;
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

    public static void updateIcons(TextureMap textureMap) {
        itemProperties = (CustomItemProperties[][]) null;
        enchantmentProperties = (CustomItemProperties[][]) null;
        useGlint = true;

        if (Config.isCustomItems()) {
            readCitProperties("mcpatcher/cit.properties");
            IResourcePack[] rps = Config.getResourcePacks();

            for (int i = rps.length - 1; i >= 0; --i) {
                IResourcePack rp = rps[i];
                updateIcons(textureMap, rp);
            }

            updateIcons(textureMap, Config.getDefaultResourcePack());

            if (itemProperties.length <= 0) {
                itemProperties = (CustomItemProperties[][]) null;
            }

            if (enchantmentProperties.length <= 0) {
                enchantmentProperties = (CustomItemProperties[][]) null;
            }
        }
    }

    private static void readCitProperties(String fileName) {
        try {
            ResourceLocation e = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream(e);

            if (in == null) {
                return;
            }

            Config.dbg("CustomItems: Loading " + fileName);
            Properties props = new Properties();
            props.load(in);
            in.close();
            useGlint = Config.parseBoolean(props.getProperty("useGlint"), true);
        } catch (FileNotFoundException var4) {
            return;
        } catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    private static void updateIcons(TextureMap textureMap, IResourcePack rp) {
        String[] names = ResUtils.collectFiles(rp, "mcpatcher/cit/", ".properties", (String[]) null);
        Map mapAutoProperties = makeAutoImageProperties(rp);

        if (mapAutoProperties.size() > 0) {
            Set itemList = mapAutoProperties.keySet();
            String[] enchantmentList = (String[]) ((String[]) itemList.toArray(new String[itemList.size()]));
            names = (String[]) ((String[]) Config.addObjectsToArray(names, enchantmentList));
        }

        Arrays.sort(names);
        List var14 = makePropertyList(itemProperties);
        List var15 = makePropertyList(enchantmentProperties);

        for (int comp = 0; comp < names.length; ++comp) {
            String i = names[comp];
            Config.dbg("CustomItems: " + i);

            try {
                CustomItemProperties cips = null;

                if (mapAutoProperties.containsKey(i)) {
                    cips = (CustomItemProperties) mapAutoProperties.get(i);
                }

                if (cips == null) {
                    ResourceLocation locFile = new ResourceLocation(i);
                    InputStream in = rp.getInputStream(locFile);

                    if (in == null) {
                        Config.warn("CustomItems file not found: " + i);
                        continue;
                    }

                    Properties props = new Properties();
                    props.load(in);
                    cips = new CustomItemProperties(props, i);
                }

                if (cips.isValid(i)) {
                    cips.updateIcons(textureMap);
                    addToItemList(cips, var14);
                    addToEnchantmentList(cips, var15);
                }
            } catch (FileNotFoundException var12) {
                Config.warn("CustomItems file not found: " + i);
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }

        itemProperties = propertyListToArray(var14);
        enchantmentProperties = propertyListToArray(var15);
        Comparator var16 = getPropertiesComparator();
        int var17;
        CustomItemProperties[] var18;

        for (var17 = 0; var17 < itemProperties.length; ++var17) {
            var18 = itemProperties[var17];

            if (var18 != null) {
                Arrays.sort(var18, var16);
            }
        }

        for (var17 = 0; var17 < enchantmentProperties.length; ++var17) {
            var18 = enchantmentProperties[var17];

            if (var18 != null) {
                Arrays.sort(var18, var16);
            }
        }
    }

    private static Comparator getPropertiesComparator() {
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                CustomItemProperties cip1 = (CustomItemProperties) o1;
                CustomItemProperties cip2 = (CustomItemProperties) o2;
                return cip1.layer != cip2.layer ? cip1.layer - cip2.layer : (cip1.weight != cip2.weight ? cip2.weight - cip1.weight : (!cip1.basePath.equals(cip2.basePath) ? cip1.basePath.compareTo(cip2.basePath) : cip1.name.compareTo(cip2.name)));
            }
        };
        return comp;
    }

    public static void updateModels() {
        if (itemProperties != null) {
            for (int id = 0; id < itemProperties.length; ++id) {
                CustomItemProperties[] cips = itemProperties[id];

                if (cips != null) {
                    for (int c = 0; c < cips.length; ++c) {
                        CustomItemProperties cip = cips[c];

                        if (cip != null && cip.type == 1) {
                            TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
                            cip.updateModel(textureMap, itemModelGenerator);
                        }
                    }
                }
            }
        }
    }

    private static Map makeAutoImageProperties(IResourcePack rp) {
        HashMap map = new HashMap();
        map.putAll(makePotionImageProperties(rp, false));
        map.putAll(makePotionImageProperties(rp, true));
        return map;
    }

    private static Map makePotionImageProperties(IResourcePack rp, boolean splash) {
        HashMap map = new HashMap();
        String type = splash ? "splash/" : "normal/";
        String[] prefixes = new String[]{"mcpatcher/cit/potion/" + type, "mcpatcher/cit/Potion/" + type};
        String[] suffixes = new String[]{".png"};
        String[] names = ResUtils.collectFiles(rp, prefixes, suffixes);

        for (int i = 0; i < names.length; ++i) {
            String path = names[i];
            String name = StrUtils.removePrefixSuffix(path, prefixes, suffixes);
            Properties props = makePotionProperties(name, splash, path);

            if (props != null) {
                String pathProp = StrUtils.removeSuffix(path, suffixes) + ".properties";
                CustomItemProperties cip = new CustomItemProperties(props, pathProp);
                map.put(pathProp, cip);
            }
        }

        return map;
    }

    private static Properties makePotionProperties(String name, boolean splash, String path) {
        if (StrUtils.endsWith(name, new String[]{"_n", "_s"})) {
            return null;
        } else {
            int potionItemId;

            if (name.equals("empty") && !splash) {
                potionItemId = Item.getIdFromItem(Items.glass_bottle);
                Properties var8 = new Properties();
                var8.put("type", "item");
                var8.put("items", "" + potionItemId);
                return var8;
            } else {
                potionItemId = Item.getIdFromItem(Items.potionitem);
                int[] damages = (int[]) ((int[]) getMapPotionIds().get(name));

                if (damages == null) {
                    Config.warn("Potion not found for image: " + path);
                    return null;
                } else {
                    StringBuffer bufDamage = new StringBuffer();

                    for (int damageMask = 0; damageMask < damages.length; ++damageMask) {
                        int props = damages[damageMask];

                        if (splash) {
                            props |= 16384;
                        }

                        if (damageMask > 0) {
                            bufDamage.append(" ");
                        }

                        bufDamage.append(props);
                    }

                    short var9 = 16447;
                    Properties var10 = new Properties();
                    var10.put("type", "item");
                    var10.put("items", "" + potionItemId);
                    var10.put("damage", "" + bufDamage.toString());
                    var10.put("damageMask", "" + var9);

                    if (splash) {
                        var10.put("texture.potion_bottle_splash", name);
                    } else {
                        var10.put("texture.potion_bottle_drinkable", name);
                    }

                    return var10;
                }
            }
        }
    }

    private static Map getMapPotionIds() {
        if (mapPotionIds == null) {
            mapPotionIds = new LinkedHashMap();
            mapPotionIds.put("water", new int[]{0});
            mapPotionIds.put("awkward", new int[]{16});
            mapPotionIds.put("thick", new int[]{32});
            mapPotionIds.put("potent", new int[]{48});
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

    private static int[] getPotionIds(int baseId) {
        return new int[]{baseId, baseId + 16, baseId + 32, baseId + 48};
    }

    private static int getPotionNameDamage(String name) {
        String fullName = "potion." + name;
        Potion[] effectPotions = Potion.potionTypes;

        for (int i = 0; i < effectPotions.length; ++i) {
            Potion potion = effectPotions[i];

            if (potion != null) {
                String potionName = potion.getName();

                if (fullName.equals(potionName)) {
                    return potion.getId();
                }
            }
        }

        return -1;
    }

    private static List makePropertyList(CustomItemProperties[][] propsArr) {
        ArrayList list = new ArrayList();

        if (propsArr != null) {
            for (int i = 0; i < propsArr.length; ++i) {
                CustomItemProperties[] props = propsArr[i];
                ArrayList propList = null;

                if (props != null) {
                    propList = new ArrayList(Arrays.asList(props));
                }

                list.add(propList);
            }
        }

        return list;
    }

    private static CustomItemProperties[][] propertyListToArray(List list) {
        CustomItemProperties[][] propArr = new CustomItemProperties[list.size()][];

        for (int i = 0; i < list.size(); ++i) {
            List subList = (List) list.get(i);

            if (subList != null) {
                CustomItemProperties[] subArr = (CustomItemProperties[]) ((CustomItemProperties[]) subList.toArray(new CustomItemProperties[subList.size()]));
                Arrays.sort(subArr, new CustomItemsComparator());
                propArr[i] = subArr;
            }
        }

        return propArr;
    }

    private static void addToItemList(CustomItemProperties cp, List itemList) {
        if (cp.items != null) {
            for (int i = 0; i < cp.items.length; ++i) {
                int itemId = cp.items[i];

                if (itemId <= 0) {
                    Config.warn("Invalid item ID: " + itemId);
                } else {
                    addToList(cp, itemList, itemId);
                }
            }
        }
    }

    private static void addToEnchantmentList(CustomItemProperties cp, List enchantmentList) {
        if (cp.type == 2) {
            if (cp.enchantmentIds != null) {
                for (int i = 0; i < 256; ++i) {
                    if (cp.enchantmentIds.isInRange(i)) {
                        addToList(cp, enchantmentList, i);
                    }
                }
            }
        }
    }

    private static void addToList(CustomItemProperties cp, List list, int id) {
        while (id >= list.size()) {
            list.add((Object) null);
        }

        Object subList = (List) list.get(id);

        if (subList == null) {
            subList = new ArrayList();
            list.set(id, subList);
        }

        ((List) subList).add(cp);
    }

    public static IBakedModel getCustomItemModel(ItemStack itemStack, IBakedModel model, ModelResourceLocation modelLocation) {
        if (model.isAmbientOcclusionEnabled()) {
            return model;
        } else if (itemProperties == null) {
            return model;
        } else {
            CustomItemProperties props = getCustomItemProperties(itemStack, 1);
            return props == null ? model : props.getModel(modelLocation);
        }
    }

    public static boolean bindCustomArmorTexture(ItemStack itemStack, int layer, String overlay) {
        if (itemProperties == null) {
            return false;
        } else {
            ResourceLocation loc = getCustomArmorLocation(itemStack, layer, overlay);

            if (loc == null) {
                return false;
            } else {
                Config.getTextureManager().bindTexture(loc);
                return true;
            }
        }
    }

    private static ResourceLocation getCustomArmorLocation(ItemStack itemStack, int layer, String overlay) {
        CustomItemProperties props = getCustomItemProperties(itemStack, 3);

        if (props == null) {
            return null;
        } else if (props.mapTextureLocations == null) {
            return null;
        } else {
            Item item = itemStack.getItem();

            if (!(item instanceof ItemArmor)) {
                return null;
            } else {
                ItemArmor itemArmor = (ItemArmor) item;
                String material = itemArmor.getArmorMaterial().func_179242_c();
                StringBuffer sb = new StringBuffer();
                sb.append("texture.");
                sb.append(material);
                sb.append("_layer_");
                sb.append(layer);

                if (overlay != null) {
                    sb.append("_");
                    sb.append(overlay);
                }

                String key = sb.toString();
                ResourceLocation loc = (ResourceLocation) props.mapTextureLocations.get(key);
                return loc;
            }
        }
    }

    private static CustomItemProperties getCustomItemProperties(ItemStack itemStack, int type) {
        if (itemProperties == null) {
            return null;
        } else if (itemStack == null) {
            return null;
        } else {
            Item item = itemStack.getItem();
            int itemId = Item.getIdFromItem(item);

            if (itemId >= 0 && itemId < itemProperties.length) {
                CustomItemProperties[] cips = itemProperties[itemId];

                if (cips != null) {
                    for (int i = 0; i < cips.length; ++i) {
                        CustomItemProperties cip = cips[i];

                        if (cip.type == type && matchesProperties(cip, itemStack, (int[][]) null)) {
                            return cip;
                        }
                    }
                }
            }

            return null;
        }
    }

    private static boolean matchesProperties(CustomItemProperties cip, ItemStack itemStack, int[][] enchantmentIdLevels) {
        Item item = itemStack.getItem();

        if (cip.damage != null) {
            int idLevels = itemStack.getItemDamage();

            if (cip.damageMask != 0) {
                idLevels &= cip.damageMask;
            }

            if (cip.damagePercent) {
                int nbt = item.getMaxDamage();
                idLevels = (int) ((double) (idLevels * 100) / (double) nbt);
            }

            if (!cip.damage.isInRange(idLevels)) {
                return false;
            }
        }

        if (cip.stackSize != null && !cip.stackSize.isInRange(itemStack.stackSize)) {
            return false;
        } else {
            int[][] var8 = enchantmentIdLevels;
            int i;
            int ntv;
            boolean var9;

            if (cip.enchantmentIds != null) {
                if (enchantmentIdLevels == null) {
                    var8 = getEnchantmentIdLevels(itemStack);
                }

                var9 = false;

                for (i = 0; i < var8.length; ++i) {
                    ntv = var8[i][0];

                    if (cip.enchantmentIds.isInRange(ntv)) {
                        var9 = true;
                        break;
                    }
                }

                if (!var9) {
                    return false;
                }
            }

            if (cip.enchantmentLevels != null) {
                if (var8 == null) {
                    var8 = getEnchantmentIdLevels(itemStack);
                }

                var9 = false;

                for (i = 0; i < var8.length; ++i) {
                    ntv = var8[i][1];

                    if (cip.enchantmentLevels.isInRange(ntv)) {
                        var9 = true;
                        break;
                    }
                }

                if (!var9) {
                    return false;
                }
            }

            if (cip.nbtTagValues != null) {
                NBTTagCompound var10 = itemStack.getTagCompound();

                for (i = 0; i < cip.nbtTagValues.length; ++i) {
                    NbtTagValue var11 = cip.nbtTagValues[i];

                    if (!var11.matches(var10)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private static int[][] getEnchantmentIdLevels(ItemStack itemStack) {
        Item item = itemStack.getItem();
        NBTTagList nbt = item == Items.enchanted_book ? Items.enchanted_book.func_92110_g(itemStack) : itemStack.getEnchantmentTagList();

        if (nbt != null && nbt.tagCount() > 0) {
            int[][] arr = new int[nbt.tagCount()][2];

            for (int i = 0; i < nbt.tagCount(); ++i) {
                NBTTagCompound tag = nbt.getCompoundTagAt(i);
                short id = tag.getShort("id");
                short lvl = tag.getShort("lvl");
                arr[i][0] = id;
                arr[i][1] = lvl;
            }

            return arr;
        } else {
            return EMPTY_INT2_ARRAY;
        }
    }

    public static boolean renderCustomEffect(RenderItem renderItem, ItemStack itemStack, IBakedModel model) {
        if (enchantmentProperties == null) {
            return false;
        } else if (itemStack == null) {
            return false;
        } else {
            int[][] idLevels = getEnchantmentIdLevels(itemStack);

            if (idLevels.length <= 0) {
                return false;
            } else {
                HashSet layersRendered = null;
                boolean rendered = false;
                TextureManager textureManager = Config.getTextureManager();

                for (int i = 0; i < idLevels.length; ++i) {
                    int id = idLevels[i][0];

                    if (id >= 0 && id < enchantmentProperties.length) {
                        CustomItemProperties[] cips = enchantmentProperties[id];

                        if (cips != null) {
                            for (int p = 0; p < cips.length; ++p) {
                                CustomItemProperties cip = cips[p];

                                if (layersRendered == null) {
                                    layersRendered = new HashSet();
                                }

                                if (layersRendered.add(Integer.valueOf(id)) && matchesProperties(cip, itemStack, idLevels) && cip.textureLocation != null) {
                                    textureManager.bindTexture(cip.textureLocation);
                                    float width = cip.getTextureWidth(textureManager);

                                    if (!rendered) {
                                        rendered = true;
                                        GlStateManager.depthMask(false);
                                        GlStateManager.depthFunc(514);
                                        GlStateManager.disableLighting();
                                        GlStateManager.matrixMode(5890);
                                    }

                                    Blender.setupBlend(cip.blend, 1.0F);
                                    GlStateManager.pushMatrix();
                                    GlStateManager.scale(width / 2.0F, width / 2.0F, width / 2.0F);
                                    float offset = cip.speed * (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                                    GlStateManager.translate(offset, 0.0F, 0.0F);
                                    GlStateManager.rotate(cip.rotation, 0.0F, 0.0F, 1.0F);
                                    renderItem.func_175035_a(model, -1);
                                    GlStateManager.popMatrix();
                                }
                            }
                        }
                    }
                }

                if (rendered) {
                    GlStateManager.enableAlpha();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.matrixMode(5888);
                    GlStateManager.enableLighting();
                    GlStateManager.depthFunc(515);
                    GlStateManager.depthMask(true);
                    textureManager.bindTexture(TextureMap.locationBlocksTexture);
                }

                return rendered;
            }
        }
    }

    public static boolean renderCustomArmorEffect(EntityLivingBase entity, ItemStack itemStack, ModelBase model, float limbSwing, float prevLimbSwing, float partialTicks, float timeLimbSwing, float yaw, float pitch, float scale) {
        if (enchantmentProperties == null) {
            return false;
        } else if (Config.isShaders() && Shaders.isShadowPass) {
            return false;
        } else if (itemStack == null) {
            return false;
        } else {
            int[][] idLevels = getEnchantmentIdLevels(itemStack);

            if (idLevels.length <= 0) {
                return false;
            } else {
                HashSet layersRendered = null;
                boolean rendered = false;
                TextureManager textureManager = Config.getTextureManager();

                for (int i = 0; i < idLevels.length; ++i) {
                    int id = idLevels[i][0];

                    if (id >= 0 && id < enchantmentProperties.length) {
                        CustomItemProperties[] cips = enchantmentProperties[id];

                        if (cips != null) {
                            for (int p = 0; p < cips.length; ++p) {
                                CustomItemProperties cip = cips[p];

                                if (layersRendered == null) {
                                    layersRendered = new HashSet();
                                }

                                if (layersRendered.add(Integer.valueOf(id)) && matchesProperties(cip, itemStack, idLevels) && cip.textureLocation != null) {
                                    textureManager.bindTexture(cip.textureLocation);
                                    float width = cip.getTextureWidth(textureManager);

                                    if (!rendered) {
                                        rendered = true;

                                        if (Config.isShaders()) {
                                            ShadersRender.layerArmorBaseDrawEnchantedGlintBegin();
                                        }

                                        GlStateManager.enableBlend();
                                        GlStateManager.depthFunc(514);
                                        GlStateManager.depthMask(false);
                                    }

                                    Blender.setupBlend(cip.blend, 1.0F);
                                    GlStateManager.disableLighting();
                                    GlStateManager.matrixMode(5890);
                                    GlStateManager.loadIdentity();
                                    GlStateManager.rotate(cip.rotation, 0.0F, 0.0F, 1.0F);
                                    float texScale = width / 8.0F;
                                    GlStateManager.scale(texScale, texScale / 2.0F, texScale);
                                    float offset = cip.speed * (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
                                    GlStateManager.translate(0.0F, offset, 0.0F);
                                    GlStateManager.matrixMode(5888);
                                    model.render(entity, limbSwing, prevLimbSwing, timeLimbSwing, yaw, pitch, scale);
                                }
                            }
                        }
                    }
                }

                if (rendered) {
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

                    if (Config.isShaders()) {
                        ShadersRender.layerArmorBaseDrawEnchantedGlintEnd();
                    }
                }

                return rendered;
            }
        }
    }

    public static boolean isUseGlint() {
        return useGlint;
    }
}
