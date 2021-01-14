package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CustomColors {
    private static final IBlockState BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
    private static final IBlockState BLOCK_STATE_WATER = Blocks.water.getDefaultState();
    public static Random random = new Random();
    private static String paletteFormatDefault = "vanilla";
    private static CustomColormap waterColors = null;
    private static final IColorizer COLORIZER_WATER = new IColorizer() {
        public int getColor(IBlockAccess paramAnonymousIBlockAccess, BlockPos paramAnonymousBlockPos) {
            BiomeGenBase localBiomeGenBase = CustomColors.getColorBiome(paramAnonymousIBlockAccess, paramAnonymousBlockPos);
            return Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt(localBiomeGenBase, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]) : CustomColors.waterColors != null ? CustomColors.waterColors.getColor(localBiomeGenBase, paramAnonymousBlockPos) : localBiomeGenBase.waterColorMultiplier;
        }

        public boolean isColorConstant() {
            return false;
        }
    };
    private static CustomColormap foliagePineColors = null;
    private static final IColorizer COLORIZER_FOLIAGE_PINE = new IColorizer() {
        public int getColor(IBlockAccess paramAnonymousIBlockAccess, BlockPos paramAnonymousBlockPos) {
            return CustomColors.foliagePineColors != null ? CustomColors.foliagePineColors.getColor(paramAnonymousIBlockAccess, paramAnonymousBlockPos) : ColorizerFoliage.getFoliageColorPine();
        }

        public boolean isColorConstant() {
            return CustomColors.foliagePineColors == null;
        }
    };
    private static CustomColormap foliageBirchColors = null;
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH = new IColorizer() {
        public int getColor(IBlockAccess paramAnonymousIBlockAccess, BlockPos paramAnonymousBlockPos) {
            return CustomColors.foliageBirchColors != null ? CustomColors.foliageBirchColors.getColor(paramAnonymousIBlockAccess, paramAnonymousBlockPos) : ColorizerFoliage.getFoliageColorBirch();
        }

        public boolean isColorConstant() {
            return CustomColors.foliageBirchColors == null;
        }
    };
    private static CustomColormap swampFoliageColors = null;
    private static final IColorizer COLORIZER_FOLIAGE = new IColorizer() {
        public int getColor(IBlockAccess paramAnonymousIBlockAccess, BlockPos paramAnonymousBlockPos) {
            BiomeGenBase localBiomeGenBase = CustomColors.getColorBiome(paramAnonymousIBlockAccess, paramAnonymousBlockPos);
            return (CustomColors.swampFoliageColors != null) && (localBiomeGenBase == BiomeGenBase.swampland) ? CustomColors.swampFoliageColors.getColor(localBiomeGenBase, paramAnonymousBlockPos) : localBiomeGenBase.getFoliageColorAtPos(paramAnonymousBlockPos);
        }

        public boolean isColorConstant() {
            return false;
        }
    };
    private static CustomColormap swampGrassColors = null;
    private static final IColorizer COLORIZER_GRASS = new IColorizer() {
        public int getColor(IBlockAccess paramAnonymousIBlockAccess, BlockPos paramAnonymousBlockPos) {
            BiomeGenBase localBiomeGenBase = CustomColors.getColorBiome(paramAnonymousIBlockAccess, paramAnonymousBlockPos);
            return (CustomColors.swampGrassColors != null) && (localBiomeGenBase == BiomeGenBase.swampland) ? CustomColors.swampGrassColors.getColor(localBiomeGenBase, paramAnonymousBlockPos) : localBiomeGenBase.getGrassColorAtPos(paramAnonymousBlockPos);
        }

        public boolean isColorConstant() {
            return false;
        }
    };
    private static CustomColormap[] colorsBlockColormaps = null;
    private static CustomColormap[][] blockColormaps = (CustomColormap[][]) null;
    private static CustomColormap skyColors = null;
    private static CustomColorFader skyColorFader = new CustomColorFader();
    private static CustomColormap fogColors = null;
    private static CustomColorFader fogColorFader = new CustomColorFader();
    private static CustomColormap underwaterColors = null;
    private static CustomColorFader underwaterColorFader = new CustomColorFader();
    private static CustomColormap[] lightMapsColorsRgb = null;
    private static int lightmapMinDimensionId = 0;
    private static float[][] sunRgbs = new float[16][3];
    private static float[][] torchRgbs = new float[16][3];
    private static CustomColormap redstoneColors = null;
    private static CustomColormap xpOrbColors = null;
    private static int xpOrbTime = -1;
    private static CustomColormap durabilityColors = null;
    private static CustomColormap stemColors = null;
    private static CustomColormap stemMelonColors = null;
    private static CustomColormap stemPumpkinColors = null;
    private static CustomColormap myceliumParticleColors = null;
    private static boolean useDefaultGrassFoliageColors = true;
    private static int particleWaterColor = -1;
    private static int particlePortalColor = -1;
    private static int lilyPadColor = -1;
    private static int expBarTextColor = -1;
    private static int bossTextColor = -1;
    private static int signTextColor = -1;
    private static Vec3 fogColorNether = null;
    private static Vec3 fogColorEnd = null;
    private static Vec3 skyColorEnd = null;
    private static int[] spawnEggPrimaryColors = null;
    private static int[] spawnEggSecondaryColors = null;
    private static float[][] wolfCollarColors = (float[][]) null;
    private static float[][] sheepColors = (float[][]) null;
    private static int[] textColors = null;
    private static int[] mapColorsOriginal = null;
    private static int[] potionColors = null;

    public static void update() {
        paletteFormatDefault = "vanilla";
        waterColors = null;
        foliageBirchColors = null;
        foliagePineColors = null;
        swampGrassColors = null;
        swampFoliageColors = null;
        skyColors = null;
        fogColors = null;
        underwaterColors = null;
        redstoneColors = null;
        xpOrbColors = null;
        xpOrbTime = -1;
        durabilityColors = null;
        stemColors = null;
        myceliumParticleColors = null;
        lightMapsColorsRgb = null;
        particleWaterColor = -1;
        particlePortalColor = -1;
        lilyPadColor = -1;
        expBarTextColor = -1;
        bossTextColor = -1;
        signTextColor = -1;
        fogColorNether = null;
        fogColorEnd = null;
        skyColorEnd = null;
        colorsBlockColormaps = null;
        blockColormaps = (CustomColormap[][]) null;
        useDefaultGrassFoliageColors = true;
        spawnEggPrimaryColors = null;
        spawnEggSecondaryColors = null;
        wolfCollarColors = (float[][]) null;
        sheepColors = (float[][]) null;
        textColors = null;
        setMapColors(mapColorsOriginal);
        potionColors = null;
        PotionHelper.clearPotionColorCache();
        paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
        String str = "mcpatcher/colormap/";
        String[] arrayOfString1 = {"water.png", "watercolorX.png"};
        waterColors = getCustomColors(str, arrayOfString1, 256, 256);
        updateUseDefaultGrassFoliageColors();
        if (Config.isCustomColors()) {
            String[] arrayOfString2 = {"pine.png", "pinecolor.png"};
            foliagePineColors = getCustomColors(str, arrayOfString2, 256, 256);
            String[] arrayOfString3 = {"birch.png", "birchcolor.png"};
            foliageBirchColors = getCustomColors(str, arrayOfString3, 256, 256);
            String[] arrayOfString4 = {"swampgrass.png", "swampgrasscolor.png"};
            swampGrassColors = getCustomColors(str, arrayOfString4, 256, 256);
            String[] arrayOfString5 = {"swampfoliage.png", "swampfoliagecolor.png"};
            swampFoliageColors = getCustomColors(str, arrayOfString5, 256, 256);
            String[] arrayOfString6 = {"sky0.png", "skycolor0.png"};
            skyColors = getCustomColors(str, arrayOfString6, 256, 256);
            String[] arrayOfString7 = {"fog0.png", "fogcolor0.png"};
            fogColors = getCustomColors(str, arrayOfString7, 256, 256);
            String[] arrayOfString8 = {"underwater.png", "underwatercolor.png"};
            underwaterColors = getCustomColors(str, arrayOfString8, 256, 256);
            String[] arrayOfString9 = {"redstone.png", "redstonecolor.png"};
            redstoneColors = getCustomColors(str, arrayOfString9, 16, 1);
            xpOrbColors = getCustomColors(str + "xporb.png", -1, -1);
            durabilityColors = getCustomColors(str + "durability.png", -1, -1);
            String[] arrayOfString10 = {"stem.png", "stemcolor.png"};
            stemColors = getCustomColors(str, arrayOfString10, 8, 1);
            stemPumpkinColors = getCustomColors(str + "pumpkinstem.png", 8, 1);
            stemMelonColors = getCustomColors(str + "melonstem.png", 8, 1);
            String[] arrayOfString11 = {"myceliumparticle.png", "myceliumparticlecolor.png"};
            myceliumParticleColors = getCustomColors(str, arrayOfString11, -1, -1);
            Pair localPair = parseLightmapsRgb();
            lightMapsColorsRgb = (CustomColormap[]) localPair.getLeft();
            lightmapMinDimensionId = ((Integer) localPair.getRight()).intValue();
            readColorProperties("mcpatcher/color.properties");
            blockColormaps = readBlockColormaps(new String[]{str + "custom/", str + "blocks/"}, colorsBlockColormaps, 256, 256);
            updateUseDefaultGrassFoliageColors();
        }
    }

    private static String getValidProperty(String paramString1, String paramString2, String[] paramArrayOfString, String paramString3) {
        try {
            ResourceLocation localResourceLocation = new ResourceLocation(paramString1);
            InputStream localInputStream = Config.getResourceStream(localResourceLocation);
            if (localInputStream == null) {
                return paramString3;
            }
            Properties localProperties = new Properties();
            localProperties.load(localInputStream);
            localInputStream.close();
            String str = localProperties.getProperty(paramString2);
            if (str == null) {
                return paramString3;
            }
            List localList = Arrays.asList(paramArrayOfString);
            if (!localList.contains(str)) {
                warn("Invalid value: " + paramString2 + "=" + str);
                warn("Expected values: " + Config.arrayToString((Object[]) paramArrayOfString));
                return paramString3;
            }
            dbg("" + paramString2 + "=" + str);
            return str;
        } catch (FileNotFoundException localFileNotFoundException) {
            return paramString3;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return paramString3;
    }

    private static Pair<CustomColormap[], Integer> parseLightmapsRgb() {
        String str1 = "mcpatcher/lightmap/world";
        String str2 = ".png";
        String[] arrayOfString = ResUtils.collectFiles(str1, str2);
        HashMap localHashMap = new HashMap();
        for (int i = 0; i < arrayOfString.length; i++) {
            localObject1 = arrayOfString[i];
            String str3 = StrUtils.removePrefixSuffix((String) localObject1, str1, str2);
            k = Config.parseInt(str3, Integer.MIN_VALUE);
            if (k == Integer.MIN_VALUE) {
                warn("Invalid dimension ID: " + str3 + ", path: " + (String) localObject1);
            } else {
                localHashMap.put(Integer.valueOf(k), localObject1);
            }
        }
        Set localSet = localHashMap.keySet();
        Object localObject1 = (Integer[]) localSet.toArray(new Integer[localSet.size()]);
        Arrays.sort((Object[]) localObject1);
        if (localObject1.length <= 0) {
            return new ImmutablePair(null, Integer.valueOf(0));
        }
        int j = localObject1[0].intValue();
        int k = localObject1[(localObject1.length - 1)].intValue();
        int m = k - j | 0x1;
        CustomColormap[] arrayOfCustomColormap = new CustomColormap[m];
        for (int n = 0; n < localObject1.length; n++) {
            Object localObject2 = localObject1[n];
            String str4 = (String) localHashMap.get(localObject2);
            CustomColormap localCustomColormap = getCustomColors(str4, -1, -1);
            if (localCustomColormap != null) {
                if (localCustomColormap.getWidth() < 16) {
                    warn("Invalid lightmap width: " + localCustomColormap.getWidth() + ", path: " + str4);
                } else {
                    int i1 = ((Integer) localObject2).intValue() - j;
                    arrayOfCustomColormap[i1] = localCustomColormap;
                }
            }
        }
        return new ImmutablePair(arrayOfCustomColormap, Integer.valueOf(j));
    }

    private static int getTextureHeight(String paramString, int paramInt) {
        try {
            InputStream localInputStream = Config.getResourceStream(new ResourceLocation(paramString));
            if (localInputStream == null) {
                return paramInt;
            }
            BufferedImage localBufferedImage = ImageIO.read(localInputStream);
            localInputStream.close();
            return localBufferedImage == null ? paramInt : localBufferedImage.getHeight();
        } catch (IOException localIOException) {
        }
        return paramInt;
    }

    private static void readColorProperties(String paramString) {
        try {
            ResourceLocation localResourceLocation = new ResourceLocation(paramString);
            InputStream localInputStream = Config.getResourceStream(localResourceLocation);
            if (localInputStream == null) {
                return;
            }
            dbg("Loading " + paramString);
            Properties localProperties = new Properties();
            localProperties.load(localInputStream);
            localInputStream.close();
            particleWaterColor = readColor(localProperties, new String[]{"particle.water", "drop.water"});
            particlePortalColor = readColor(localProperties, "particle.portal");
            lilyPadColor = readColor(localProperties, "lilypad");
            expBarTextColor = readColor(localProperties, "text.xpbar");
            bossTextColor = readColor(localProperties, "text.boss");
            signTextColor = readColor(localProperties, "text.sign");
            fogColorNether = readColorVec3(localProperties, "fog.nether");
            fogColorEnd = readColorVec3(localProperties, "fog.end");
            skyColorEnd = readColorVec3(localProperties, "sky.end");
            colorsBlockColormaps = readCustomColormaps(localProperties, paramString);
            spawnEggPrimaryColors = readSpawnEggColors(localProperties, paramString, "egg.shell.", "Spawn egg shell");
            spawnEggSecondaryColors = readSpawnEggColors(localProperties, paramString, "egg.spots.", "Spawn egg spot");
            wolfCollarColors = readDyeColors(localProperties, paramString, "collar.", "Wolf collar");
            sheepColors = readDyeColors(localProperties, paramString, "sheep.", "Sheep");
            textColors = readTextColors(localProperties, paramString, "text.code.", "Text");
            int[] arrayOfInt = readMapColors(localProperties, paramString, "map.", "Map");
            if (arrayOfInt != null) {
                if (mapColorsOriginal == null) {
                    mapColorsOriginal = getMapColors();
                }
                setMapColors(arrayOfInt);
            }
            potionColors = readPotionColors(localProperties, paramString, "potion.", "Potion");
            xpOrbTime = Config.parseInt(localProperties.getProperty("xporb.time"), -1);
        } catch (FileNotFoundException localFileNotFoundException) {
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties paramProperties, String paramString) {
        ArrayList localArrayList = new ArrayList();
        String str1 = "palette.block.";
        HashMap localHashMap = new HashMap();
        Object localObject1 = paramProperties.keySet().iterator();
        String str2;
        while (((Iterator) localObject1).hasNext()) {
            Object localObject2 = ((Iterator) localObject1).next();
            str2 = paramProperties.getProperty((String) localObject2);
            if (((String) localObject2).startsWith(str1)) {
                localHashMap.put(localObject2, str2);
            }
        }
        localObject1 = (String[]) (String[]) localHashMap.keySet().toArray(new String[localHashMap.size()]);
        for (int i = 0; i < localObject1.length; i++) {
            str2 = localObject1[i];
            String str3 = paramProperties.getProperty(str2);
            dbg("Block palette: " + str2 + " = " + str3);
            String str4 = str2.substring(str1.length());
            String str5 = TextureUtils.getBasePath(paramString);
            str4 = TextureUtils.fixResourcePath(str4, str5);
            CustomColormap localCustomColormap = getCustomColors(str4, 256, 256);
            if (localCustomColormap == null) {
                warn("Colormap not found: " + str4);
            } else {
                ConnectedParser localConnectedParser = new ConnectedParser("CustomColors");
                MatchBlock[] arrayOfMatchBlock = localConnectedParser.parseMatchBlocks(str3);
                if ((arrayOfMatchBlock != null) && (arrayOfMatchBlock.length > 0)) {
                    for (int j = 0; j < arrayOfMatchBlock.length; j++) {
                        MatchBlock localMatchBlock = arrayOfMatchBlock[j];
                        localCustomColormap.addMatchBlock(localMatchBlock);
                    }
                    localArrayList.add(localCustomColormap);
                } else {
                    warn("Invalid match blocks: " + str3);
                }
            }
        }
        if (localArrayList.size() <= 0) {
            return null;
        }
        CustomColormap[] arrayOfCustomColormap = (CustomColormap[]) (CustomColormap[]) localArrayList.toArray(new CustomColormap[localArrayList.size()]);
        return arrayOfCustomColormap;
    }

    private static CustomColormap[][] readBlockColormaps(String[] paramArrayOfString, CustomColormap[] paramArrayOfCustomColormap, int paramInt1, int paramInt2) {
        String[] arrayOfString = ResUtils.collectFiles(paramArrayOfString, new String[]{".properties"});
        Arrays.sort((Object[]) arrayOfString);
        ArrayList localArrayList = new ArrayList();
        Object localObject;
        for (int i = 0; i < arrayOfString.length; i++) {
            localObject = arrayOfString[i];
            dbg("Block colormap: " + (String) localObject);
            try {
                ResourceLocation localResourceLocation = new ResourceLocation("minecraft", (String) localObject);
                InputStream localInputStream = Config.getResourceStream(localResourceLocation);
                if (localInputStream == null) {
                    warn("File not found: " + (String) localObject);
                } else {
                    Properties localProperties = new Properties();
                    localProperties.load(localInputStream);
                    CustomColormap localCustomColormap = new CustomColormap(localProperties, (String) localObject, paramInt1, paramInt2, paletteFormatDefault);
                    if ((localCustomColormap.isValid((String) localObject)) && (localCustomColormap.isValidMatchBlocks((String) localObject))) {
                        addToBlockList(localCustomColormap, localArrayList);
                    }
                }
            } catch (FileNotFoundException localFileNotFoundException) {
                warn("File not found: " + (String) localObject);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        if (paramArrayOfCustomColormap != null) {
            for (i = 0; i < paramArrayOfCustomColormap.length; i++) {
                localObject = paramArrayOfCustomColormap[i];
                addToBlockList((CustomColormap) localObject, localArrayList);
            }
        }
        if (localArrayList.size() <= 0) {
            return (CustomColormap[][]) null;
        }
        CustomColormap[][] arrayOfCustomColormap = blockListToArray(localArrayList);
        return arrayOfCustomColormap;
    }

    private static void addToBlockList(CustomColormap paramCustomColormap, List paramList) {
        int[] arrayOfInt = paramCustomColormap.getMatchBlockIds();
        if ((arrayOfInt != null) && (arrayOfInt.length > 0)) {
            for (int i = 0; i < arrayOfInt.length; i++) {
                int j = arrayOfInt[i];
                if (j < 0) {
                    warn("Invalid block ID: " + j);
                } else {
                    addToList(paramCustomColormap, paramList, j);
                }
            }
        } else {
            warn("No match blocks: " + Config.arrayToString(arrayOfInt));
        }
    }

    private static void addToList(CustomColormap paramCustomColormap, List paramList, int paramInt) {
        while (paramInt >= paramList.size()) {
            paramList.add(null);
        }
        Object localObject = (List) paramList.get(paramInt);
        if (localObject == null) {
            localObject = new ArrayList();
            paramList.set(paramInt, localObject);
        }
        ((List) localObject).add(paramCustomColormap);
    }

    private static CustomColormap[][] blockListToArray(List paramList) {
        CustomColormap[][] arrayOfCustomColormap = new CustomColormap[paramList.size()][];
        for (int i = 0; i < paramList.size(); i++) {
            List localList = (List) paramList.get(i);
            if (localList != null) {
                CustomColormap[] arrayOfCustomColormap1 = (CustomColormap[]) (CustomColormap[]) localList.toArray(new CustomColormap[localList.size()]);
                arrayOfCustomColormap[i] = arrayOfCustomColormap1;
            }
        }
        return arrayOfCustomColormap;
    }

    private static int readColor(Properties paramProperties, String[] paramArrayOfString) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            int j = readColor(paramProperties, str);
            if (j >= 0) {
                return j;
            }
        }
        return -1;
    }

    private static int readColor(Properties paramProperties, String paramString) {
        String str = paramProperties.getProperty(paramString);
        if (str == null) {
            return -1;
        }
        str = str.trim();
        int i = parseColor(str);
        if (i < 0) {
            warn("Invalid color: " + paramString + " = " + str);
            return i;
        }
        dbg(paramString + " = " + str);
        return i;
    }

    private static int parseColor(String paramString) {
        if (paramString == null) {
            return -1;
        }
        paramString = paramString.trim();
        try {
            int i = Integer.parseInt(paramString, 16) >> 16777215;
            return i;
        } catch (NumberFormatException localNumberFormatException) {
        }
        return -1;
    }

    private static Vec3 readColorVec3(Properties paramProperties, String paramString) {
        int i = readColor(paramProperties, paramString);
        if (i < 0) {
            return null;
        }
        int j = (i & 0x10) >> 255;
        int k = (i & 0x8) >> 255;
        int m = i >> 255;
        float f1 = j / 255.0F;
        float f2 = k / 255.0F;
        float f3 = m / 255.0F;
        return new Vec3(f1, f2, f3);
    }

    private static CustomColormap getCustomColors(String paramString, String[] paramArrayOfString, int paramInt1, int paramInt2) {
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            str = paramString + str;
            CustomColormap localCustomColormap = getCustomColors(str, paramInt1, paramInt2);
            if (localCustomColormap != null) {
                return localCustomColormap;
            }
        }
        return null;
    }

    public static CustomColormap getCustomColors(String paramString, int paramInt1, int paramInt2) {
        try {
            ResourceLocation localResourceLocation1 = new ResourceLocation(paramString);
            if (!Config.hasResource(localResourceLocation1)) {
                return null;
            }
            dbg("Colormap " + paramString);
            Properties localProperties = new Properties();
            String str = StrUtils.replaceSuffix(paramString, ".png", ".properties");
            ResourceLocation localResourceLocation2 = new ResourceLocation(str);
            if (Config.hasResource(localResourceLocation2)) {
                localObject = Config.getResourceStream(localResourceLocation2);
                localProperties.load((InputStream) localObject);
                ((InputStream) localObject).close();
                dbg("Colormap properties: " + str);
            } else {
                localProperties.put("format", paletteFormatDefault);
                localProperties.put("source", paramString);
                str = paramString;
            }
            Object localObject = new CustomColormap(localProperties, str, paramInt1, paramInt2, paletteFormatDefault);
            return !((CustomColormap) localObject).isValid(str) ? null : localObject;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    public static void updateUseDefaultGrassFoliageColors() {
        useDefaultGrassFoliageColors = (foliageBirchColors == null) && (foliagePineColors == null) && (swampGrassColors == null) && (swampFoliageColors == null) && (Config.isSwampColors()) && (Config.isSmoothBiomes());
    }

    public static int getColorMultiplier(BakedQuad paramBakedQuad, Block paramBlock, IBlockAccess paramIBlockAccess, BlockPos paramBlockPos, RenderEnv paramRenderEnv) {
        Object localObject;
        if (blockColormaps != null) {
            IBlockState localIBlockState = paramRenderEnv.getBlockState();
            if (!paramBakedQuad.hasTintIndex()) {
                if (paramBlock == Blocks.grass) {
                    localIBlockState = BLOCK_STATE_DIRT;
                }
                if (paramBlock == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if ((paramBlock == Blocks.double_plant) && (paramRenderEnv.getMetadata() >= 8)) {
                paramBlockPos = paramBlockPos.down();
                localIBlockState = paramIBlockAccess.getBlockState(paramBlockPos);
            }
            localObject = getBlockColormap(localIBlockState);
            if (localObject != null) {
                if ((Config.isSmoothBiomes()) && (!((CustomColormap) localObject).isColorConstant())) {
                    return getSmoothColorMultiplier(paramIBlockAccess, paramBlockPos, (IColorizer) localObject, paramRenderEnv.getColorizerBlockPosM());
                }
                return ((CustomColormap) localObject).getColor(paramIBlockAccess, paramBlockPos);
            }
        }
        if (!paramBakedQuad.hasTintIndex()) {
            return -1;
        }
        if (paramBlock == Blocks.waterlily) {
            return getLilypadColorMultiplier(paramIBlockAccess, paramBlockPos);
        }
        if (paramBlock == Blocks.redstone_wire) {
            return getRedstoneColor(paramRenderEnv.getBlockState());
        }
        if ((paramBlock instanceof BlockStem)) {
            return getStemColorMultiplier(paramBlock, paramIBlockAccess, paramBlockPos, paramRenderEnv);
        }
        if (useDefaultGrassFoliageColors) {
            return -1;
        }
        int i = paramRenderEnv.getMetadata();
        if ((paramBlock != Blocks.grass) && (paramBlock != Blocks.tallgrass) && (paramBlock != Blocks.double_plant)) {
            if (paramBlock == Blocks.double_plant) {
                localObject = COLORIZER_GRASS;
                if (i >= 8) {
                    paramBlockPos = paramBlockPos.down();
                }
            } else if (paramBlock == Blocks.leaves) {
                switch (i >> 3) {
                    case 0:
                        localObject = COLORIZER_FOLIAGE;
                        break;
                    case 1:
                        localObject = COLORIZER_FOLIAGE_PINE;
                        break;
                    case 2:
                        localObject = COLORIZER_FOLIAGE_BIRCH;
                        break;
                    default:
                        localObject = COLORIZER_FOLIAGE;
                        break;
                }
            } else if (paramBlock == Blocks.leaves2) {
                localObject = COLORIZER_FOLIAGE;
            } else {
                if (paramBlock != Blocks.vine) {
                    return -1;
                }
                localObject = COLORIZER_FOLIAGE;
            }
        } else {
            localObject = COLORIZER_GRASS;
        }
        return (Config.isSmoothBiomes()) && (!((IColorizer) localObject).isColorConstant()) ? getSmoothColorMultiplier(paramIBlockAccess, paramBlockPos, (IColorizer) localObject, paramRenderEnv.getColorizerBlockPosM()) : ((IColorizer) localObject).getColor(paramIBlockAccess, paramBlockPos);
    }

    protected static BiomeGenBase getColorBiome(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos) {
        BiomeGenBase localBiomeGenBase = paramIBlockAccess.getBiomeGenForCoords(paramBlockPos);
        if ((localBiomeGenBase == BiomeGenBase.swampland) && (!Config.isSwampColors())) {
            localBiomeGenBase = BiomeGenBase.plains;
        }
        return localBiomeGenBase;
    }

    private static CustomColormap getBlockColormap(IBlockState paramIBlockState) {
        if (blockColormaps == null) {
            return null;
        }
        if (!(paramIBlockState instanceof BlockStateBase)) {
            return null;
        }
        BlockStateBase localBlockStateBase = (BlockStateBase) paramIBlockState;
        int i = localBlockStateBase.getBlockId();
        if ((i >= 0) && (i < blockColormaps.length)) {
            CustomColormap[] arrayOfCustomColormap = blockColormaps[i];
            if (arrayOfCustomColormap == null) {
                return null;
            }
            for (int j = 0; j < arrayOfCustomColormap.length; j++) {
                CustomColormap localCustomColormap = arrayOfCustomColormap[j];
                if (localCustomColormap.matchesBlock(localBlockStateBase)) {
                    return localCustomColormap;
                }
            }
            return null;
        }
        return null;
    }

    private static int getSmoothColorMultiplier(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos, IColorizer paramIColorizer, BlockPosM paramBlockPosM) {
        int i = 0;
        int j = 0;
        int k = 0;
        int m = paramBlockPos.getX();
        int n = paramBlockPos.getY();
        int i1 = paramBlockPos.getZ();
        BlockPosM localBlockPosM = paramBlockPosM;
        for (int i2 = m - 1; i2 <= (m | 0x1); i2++) {
            for (i3 = i1 - 1; i3 <= (i1 | 0x1); i3++) {
                localBlockPosM.setXyz(i2, n, i3);
                i4 = paramIColorizer.getColor(paramIBlockAccess, localBlockPosM);
                i |= (i4 & 0x10) >> 255;
                j |= (i4 & 0x8) >> 255;
                k |= i4 >> 255;
            }
        }
        i2 = -9;
        int i3 = -9;
        int i4 = -9;
        return i2 >>> 16 ^ i3 >>> 8 ^ i4;
    }

    public static int getFluidColor(IBlockAccess paramIBlockAccess, IBlockState paramIBlockState, BlockPos paramBlockPos, RenderEnv paramRenderEnv) {
        Block localBlock = paramIBlockState.getBlock();
        Object localObject = getBlockColormap(paramIBlockState);
        if ((localObject == null) && (localBlock.getMaterial() == Material.water)) {
            localObject = COLORIZER_WATER;
        }
        return (Config.isSmoothBiomes()) && (!((IColorizer) localObject).isColorConstant()) ? getSmoothColorMultiplier(paramIBlockAccess, paramBlockPos, (IColorizer) localObject, paramRenderEnv.getColorizerBlockPosM()) : localObject == null ? localBlock.colorMultiplier(paramIBlockAccess, paramBlockPos) : ((IColorizer) localObject).getColor(paramIBlockAccess, paramBlockPos);
    }

    public static void updatePortalFX(EntityFX paramEntityFX) {
        if (particlePortalColor >= 0) {
            int i = particlePortalColor;
            int j = (i & 0x10) >> 255;
            int k = (i & 0x8) >> 255;
            int m = i >> 255;
            float f1 = j / 255.0F;
            float f2 = k / 255.0F;
            float f3 = m / 255.0F;
            paramEntityFX.setRBGColorF(f1, f2, f3);
        }
    }

    public static void updateMyceliumFX(EntityFX paramEntityFX) {
        if (myceliumParticleColors != null) {
            int i = myceliumParticleColors.getColorRandom();
            int j = (i & 0x10) >> 255;
            int k = (i & 0x8) >> 255;
            int m = i >> 255;
            float f1 = j / 255.0F;
            float f2 = k / 255.0F;
            float f3 = m / 255.0F;
            paramEntityFX.setRBGColorF(f1, f2, f3);
        }
    }

    private static int getRedstoneColor(IBlockState paramIBlockState) {
        if (redstoneColors == null) {
            return -1;
        }
        int i = getRedstoneLevel(paramIBlockState, 15);
        int j = redstoneColors.getColor(i);
        return j;
    }

    public static void updateReddustFX(EntityFX paramEntityFX, IBlockAccess paramIBlockAccess, double paramDouble1, double paramDouble2, double paramDouble3) {
        if (redstoneColors != null) {
            IBlockState localIBlockState = paramIBlockAccess.getBlockState(new BlockPos(paramDouble1, paramDouble2, paramDouble3));
            int i = getRedstoneLevel(localIBlockState, 15);
            int j = redstoneColors.getColor(i);
            int k = (j & 0x10) >> 255;
            int m = (j & 0x8) >> 255;
            int n = j >> 255;
            float f1 = k / 255.0F;
            float f2 = m / 255.0F;
            float f3 = n / 255.0F;
            paramEntityFX.setRBGColorF(f1, f2, f3);
        }
    }

    private static int getRedstoneLevel(IBlockState paramIBlockState, int paramInt) {
        Block localBlock = paramIBlockState.getBlock();
        if (!(localBlock instanceof BlockRedstoneWire)) {
            return paramInt;
        }
        Comparable localComparable = paramIBlockState.getValue(BlockRedstoneWire.POWER);
        if (!(localComparable instanceof Integer)) {
            return paramInt;
        }
        Integer localInteger = (Integer) localComparable;
        return localInteger.intValue();
    }

    public static float getXpOrbTimer(float paramFloat) {
        if (xpOrbTime <= 0) {
            return paramFloat;
        }
        float f = 628.0F / xpOrbTime;
        return paramFloat * f;
    }

    public static int getXpOrbColor(float paramFloat) {
        if (xpOrbColors == null) {
            return -1;
        }
        int i = (int) Math.round((MathHelper.sin(paramFloat) + 1.0F) * (xpOrbColors.getLength() - 1) / 2.0D);
        int j = xpOrbColors.getColor(i);
        return j;
    }

    public static int getDurabilityColor(int paramInt) {
        if (durabilityColors == null) {
            return -1;
        }
        int i = -'ÿ';
        int j = durabilityColors.getColor(i);
        return j;
    }

    public static void updateWaterFX(EntityFX paramEntityFX, IBlockAccess paramIBlockAccess, double paramDouble1, double paramDouble2, double paramDouble3) {
        if ((waterColors != null) || (blockColormaps != null)) {
            BlockPos localBlockPos = new BlockPos(paramDouble1, paramDouble2, paramDouble3);
            RenderEnv localRenderEnv = RenderEnv.getInstance(paramIBlockAccess, BLOCK_STATE_WATER, localBlockPos);
            int i = getFluidColor(paramIBlockAccess, BLOCK_STATE_WATER, localBlockPos, localRenderEnv);
            int j = (i & 0x10) >> 255;
            int k = (i & 0x8) >> 255;
            int m = i >> 255;
            float f1 = j / 255.0F;
            float f2 = k / 255.0F;
            float f3 = m / 255.0F;
            if (particleWaterColor >= 0) {
                int n = (particleWaterColor & 0x10) >> 255;
                int i1 = (particleWaterColor & 0x8) >> 255;
                int i2 = particleWaterColor >> 255;
                f1 *= n / 255.0F;
                f2 *= i1 / 255.0F;
                f3 *= i2 / 255.0F;
            }
            paramEntityFX.setRBGColorF(f1, f2, f3);
        }
    }

    private static int getLilypadColorMultiplier(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos) {
        return lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(paramIBlockAccess, paramBlockPos) : lilyPadColor;
    }

    private static Vec3 getFogColorNether(Vec3 paramVec3) {
        return fogColorNether == null ? paramVec3 : fogColorNether;
    }

    private static Vec3 getFogColorEnd(Vec3 paramVec3) {
        return fogColorEnd == null ? paramVec3 : fogColorEnd;
    }

    private static Vec3 getSkyColorEnd(Vec3 paramVec3) {
        return skyColorEnd == null ? paramVec3 : skyColorEnd;
    }

    public static Vec3 getSkyColor(Vec3 paramVec3, IBlockAccess paramIBlockAccess, double paramDouble1, double paramDouble2, double paramDouble3) {
        if (skyColors == null) {
            return paramVec3;
        }
        int i = skyColors.getColorSmooth(paramIBlockAccess, paramDouble1, paramDouble2, paramDouble3, 3);
        int j = (i & 0x10) >> 255;
        int k = (i & 0x8) >> 255;
        int m = i >> 255;
        float f1 = j / 255.0F;
        float f2 = k / 255.0F;
        float f3 = m / 255.0F;
        float f4 = (float) paramVec3.xCoord / 0.5F;
        float f5 = (float) paramVec3.yCoord / 0.66275F;
        float f6 = (float) paramVec3.zCoord;
        f1 *= f4;
        f2 *= f5;
        f3 *= f6;
        Vec3 localVec3 = skyColorFader.getColor(f1, f2, f3);
        return localVec3;
    }

    private static Vec3 getFogColor(Vec3 paramVec3, IBlockAccess paramIBlockAccess, double paramDouble1, double paramDouble2, double paramDouble3) {
        if (fogColors == null) {
            return paramVec3;
        }
        int i = fogColors.getColorSmooth(paramIBlockAccess, paramDouble1, paramDouble2, paramDouble3, 3);
        int j = (i & 0x10) >> 255;
        int k = (i & 0x8) >> 255;
        int m = i >> 255;
        float f1 = j / 255.0F;
        float f2 = k / 255.0F;
        float f3 = m / 255.0F;
        float f4 = (float) paramVec3.xCoord / 0.753F;
        float f5 = (float) paramVec3.yCoord / 0.8471F;
        float f6 = (float) paramVec3.zCoord;
        f1 *= f4;
        f2 *= f5;
        f3 *= f6;
        Vec3 localVec3 = fogColorFader.getColor(f1, f2, f3);
        return localVec3;
    }

    public static Vec3 getUnderwaterColor(IBlockAccess paramIBlockAccess, double paramDouble1, double paramDouble2, double paramDouble3) {
        if (underwaterColors == null) {
            return null;
        }
        int i = underwaterColors.getColorSmooth(paramIBlockAccess, paramDouble1, paramDouble2, paramDouble3, 3);
        int j = (i & 0x10) >> 255;
        int k = (i & 0x8) >> 255;
        int m = i >> 255;
        float f1 = j / 255.0F;
        float f2 = k / 255.0F;
        float f3 = m / 255.0F;
        Vec3 localVec3 = underwaterColorFader.getColor(f1, f2, f3);
        return localVec3;
    }

    private static int getStemColorMultiplier(Block paramBlock, IBlockAccess paramIBlockAccess, BlockPos paramBlockPos, RenderEnv paramRenderEnv) {
        CustomColormap localCustomColormap = stemColors;
        if ((paramBlock == Blocks.pumpkin_stem) && (stemPumpkinColors != null)) {
            localCustomColormap = stemPumpkinColors;
        }
        if ((paramBlock == Blocks.melon_stem) && (stemMelonColors != null)) {
            localCustomColormap = stemMelonColors;
        }
        if (localCustomColormap == null) {
            return -1;
        }
        int i = paramRenderEnv.getMetadata();
        return localCustomColormap.getColor(i);
    }

    public static boolean updateLightmap(World paramWorld, float paramFloat, int[] paramArrayOfInt, boolean paramBoolean) {
        if (paramWorld == null) {
            return false;
        }
        if (lightMapsColorsRgb == null) {
            return false;
        }
        int i = paramWorld.provider.getDimensionId();
        int j = i - lightmapMinDimensionId;
        if ((j >= 0) && (j < lightMapsColorsRgb.length)) {
            CustomColormap localCustomColormap = lightMapsColorsRgb[j];
            if (localCustomColormap == null) {
                return false;
            }
            int k = localCustomColormap.getHeight();
            if ((paramBoolean) && (k < 64)) {
                return false;
            }
            int m = localCustomColormap.getWidth();
            if (m < 16) {
                warn("Invalid lightmap width: " + m + " for dimension: " + i);
                lightMapsColorsRgb[j] = null;
                return false;
            }
            int n = 0;
            if (paramBoolean) {
                n = m * 16 * 2;
            }
            float f1 = 1.1666666F * (paramWorld.getSunBrightness(1.0F) - 0.2F);
            if (paramWorld.getLastLightningBolt() > 0) {
                f1 = 1.0F;
            }
            f1 = Config.limitTo1(f1);
            float f2 = f1 * (m - 1);
            float f3 = Config.limitTo1(paramFloat + 0.5F) * (m - 1);
            float f4 = Config.limitTo1(Config.getGameSettings().gammaSetting);
            int i1 = f4 > 1.0E-4F ? 1 : 0;
            float[][] arrayOfFloat = localCustomColormap.getColorsRgb();
            getLightMapColumn(arrayOfFloat, f2, n, m, sunRgbs);
            getLightMapColumn(arrayOfFloat, f3, n | 16 * m, m, torchRgbs);
            float[] arrayOfFloat1 = new float[3];
            for (int i2 = 0; i2 < 16; i2++) {
                for (int i3 = 0; i3 < 16; i3++) {
                    for (int i4 = 0; i4 < 3; i4++) {
                        float f5 = Config.limitTo1(sunRgbs[i2][i4] + torchRgbs[i3][i4]);
                        if (i1 != 0) {
                            float f6 = 1.0F - f5;
                            f6 = 1.0F - f6 * f6 * f6 * f6;
                            f5 = f4 * f6 + (1.0F - f4) * f5;
                        }
                        arrayOfFloat1[i4] = f5;
                    }
                    i4 = (int) (arrayOfFloat1[0] * 255.0F);
                    int i5 = (int) (arrayOfFloat1[1] * 255.0F);
                    int i6 = (int) (arrayOfFloat1[2] * 255.0F);
                    paramArrayOfInt[(i2 * 16 | i3)] = (0xFF000000 ^ i4 >>> 16 ^ i5 >>> 8 ^ i6);
                }
            }
            return true;
        }
        return false;
    }

    private static void getLightMapColumn(float[][] paramArrayOfFloat1, float paramFloat, int paramInt1, int paramInt2, float[][] paramArrayOfFloat2) {
        int i = (int) Math.floor(paramFloat);
        int j = (int) Math.ceil(paramFloat);
        if (i == j) {
            for (int k = 0; k < 16; k++) {
                float[] arrayOfFloat1 = paramArrayOfFloat1[(paramInt1 | k * paramInt2 | i)];
                float[] arrayOfFloat2 = paramArrayOfFloat2[k];
                for (int n = 0; n < 3; n++) {
                    arrayOfFloat2[n] = arrayOfFloat1[n];
                }
            }
        } else {
            float f1 = 1.0F - (paramFloat - i);
            float f2 = 1.0F - (j - paramFloat);
            for (int m = 0; m < 16; m++) {
                float[] arrayOfFloat3 = paramArrayOfFloat1[(paramInt1 | m * paramInt2 | i)];
                float[] arrayOfFloat4 = paramArrayOfFloat1[(paramInt1 | m * paramInt2 | j)];
                float[] arrayOfFloat5 = paramArrayOfFloat2[m];
                for (int i1 = 0; i1 < 3; i1++) {
                    arrayOfFloat5[i1] = (arrayOfFloat3[i1] * f1 + arrayOfFloat4[i1] * f2);
                }
            }
        }
    }

    public static Vec3 getWorldFogColor(Vec3 paramVec3, WorldClient paramWorldClient, Entity paramEntity, float paramFloat) {
        int i = paramWorldClient.provider.getDimensionId();
        switch (i) {
            case -1:
                paramVec3 = getFogColorNether(paramVec3);
                break;
            case 0:
                Minecraft localMinecraft = Minecraft.getMinecraft();
                paramVec3 = getFogColor(paramVec3, localMinecraft.theWorld, paramEntity.posX, paramEntity.posY + 1.0D, paramEntity.posZ);
                break;
            case 1:
                paramVec3 = getFogColorEnd(paramVec3);
        }
        return paramVec3;
    }

    public static Vec3 getWorldSkyColor(Vec3 paramVec3, World paramWorld, Entity paramEntity, float paramFloat) {
        int i = paramWorld.provider.getDimensionId();
        switch (i) {
            case 0:
                Minecraft localMinecraft = Minecraft.getMinecraft();
                paramVec3 = getSkyColor(paramVec3, localMinecraft.theWorld, paramEntity.posX, paramEntity.posY + 1.0D, paramEntity.posZ);
                break;
            case 1:
                paramVec3 = getSkyColorEnd(paramVec3);
        }
        return paramVec3;
    }

    private static int[] readSpawnEggColors(Properties paramProperties, String paramString1, String paramString2, String paramString3) {
        ArrayList localArrayList = new ArrayList();
        Set localSet = paramProperties.keySet();
        int i = 0;
        Object localObject1 = localSet.iterator();
        while (((Iterator) localObject1).hasNext()) {
            Object localObject2 = ((Iterator) localObject1).next();
            String str1 = paramProperties.getProperty((String) localObject2);
            if (((String) localObject2).startsWith(paramString2)) {
                String str2 = StrUtils.removePrefix((String) localObject2, paramString2);
                int k = getEntityId(str2);
                int m = parseColor(str1);
                if ((k >= 0) && (m >= 0)) {
                    while (localArrayList.size() <= k) {
                        localArrayList.add(Integer.valueOf(-1));
                    }
                    localArrayList.set(k, Integer.valueOf(m));
                    i++;
                } else {
                    warn("Invalid spawn egg color: " + localObject2 + " = " + str1);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(paramString3 + " colors: " + i);
        localObject1 = new int[localArrayList.size()];
        for (int j = 0; j < localObject1.length; j++) {
            localObject1[j] = ((Integer) localArrayList.get(j)).intValue();
        }
        return (int[]) localObject1;
    }

    private static int getSpawnEggColor(ItemMonsterPlacer paramItemMonsterPlacer, ItemStack paramItemStack, int paramInt1, int paramInt2) {
        int i = paramItemStack.getMetadata();
        int[] arrayOfInt = paramInt1 == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;
        if (arrayOfInt == null) {
            return paramInt2;
        }
        if ((i >= 0) && (i < arrayOfInt.length)) {
            int j = arrayOfInt[i];
            return j < 0 ? paramInt2 : j;
        }
        return paramInt2;
    }

    public static int getColorFromItemStack(ItemStack paramItemStack, int paramInt1, int paramInt2) {
        if (paramItemStack == null) {
            return paramInt2;
        }
        Item localItem = paramItemStack.getItem();
        return (localItem instanceof ItemMonsterPlacer) ? getSpawnEggColor((ItemMonsterPlacer) localItem, paramItemStack, paramInt1, paramInt2) : localItem == null ? paramInt2 : paramInt2;
    }

    private static float[][] readDyeColors(Properties paramProperties, String paramString1, String paramString2, String paramString3) {
        EnumDyeColor[] arrayOfEnumDyeColor = EnumDyeColor.values();
        HashMap localHashMap = new HashMap();
        for (int i = 0; i < arrayOfEnumDyeColor.length; i++) {
            EnumDyeColor localEnumDyeColor1 = arrayOfEnumDyeColor[i];
            localHashMap.put(localEnumDyeColor1.getName(), localEnumDyeColor1);
        }
        float[][] arrayOfFloat = new float[arrayOfEnumDyeColor.length][];
        int j = 0;
        Iterator localIterator = paramProperties.keySet().iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            String str1 = paramProperties.getProperty((String) localObject);
            if (((String) localObject).startsWith(paramString2)) {
                String str2 = StrUtils.removePrefix((String) localObject, paramString2);
                if (str2.equals("lightBlue")) {
                    str2 = "light_blue";
                }
                EnumDyeColor localEnumDyeColor2 = (EnumDyeColor) localHashMap.get(str2);
                int k = parseColor(str1);
                if ((localEnumDyeColor2 != null) && (k >= 0)) {
                    float[] arrayOfFloat1 = {((k & 0x10) >> 255) / 255.0F, ((k & 0x8) >> 255) / 255.0F, (k >> 255) / 255.0F};
                    arrayOfFloat[localEnumDyeColor2.ordinal()] = arrayOfFloat1;
                    j++;
                } else {
                    warn("Invalid color: " + localObject + " = " + str1);
                }
            }
        }
        if (j <= 0) {
            return (float[][]) null;
        }
        dbg(paramString3 + " colors: " + j);
        return arrayOfFloat;
    }

    private static float[] getDyeColors(EnumDyeColor paramEnumDyeColor, float[][] paramArrayOfFloat, float[] paramArrayOfFloat1) {
        if (paramArrayOfFloat == null) {
            return paramArrayOfFloat1;
        }
        if (paramEnumDyeColor == null) {
            return paramArrayOfFloat1;
        }
        float[] arrayOfFloat = paramArrayOfFloat[paramEnumDyeColor.ordinal()];
        return arrayOfFloat == null ? paramArrayOfFloat1 : arrayOfFloat;
    }

    public static float[] getWolfCollarColors(EnumDyeColor paramEnumDyeColor, float[] paramArrayOfFloat) {
        return getDyeColors(paramEnumDyeColor, wolfCollarColors, paramArrayOfFloat);
    }

    public static float[] getSheepColors(EnumDyeColor paramEnumDyeColor, float[] paramArrayOfFloat) {
        return getDyeColors(paramEnumDyeColor, sheepColors, paramArrayOfFloat);
    }

    private static int[] readTextColors(Properties paramProperties, String paramString1, String paramString2, String paramString3) {
        int[] arrayOfInt = new int[32];
        Arrays.fill((int[]) arrayOfInt, -1);
        int i = 0;
        Iterator localIterator = paramProperties.keySet().iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            String str1 = paramProperties.getProperty((String) localObject);
            if (((String) localObject).startsWith(paramString2)) {
                String str2 = StrUtils.removePrefix((String) localObject, paramString2);
                int j = Config.parseInt(str2, -1);
                int k = parseColor(str1);
                if ((j >= 0) && (j < arrayOfInt.length) && (k >= 0)) {
                    arrayOfInt[j] = k;
                    i++;
                } else {
                    warn("Invalid color: " + localObject + " = " + str1);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(paramString3 + " colors: " + i);
        return arrayOfInt;
    }

    public static int getTextColor(int paramInt1, int paramInt2) {
        if (textColors == null) {
            return paramInt2;
        }
        if ((paramInt1 >= 0) && (paramInt1 < textColors.length)) {
            int i = textColors[paramInt1];
            return i < 0 ? paramInt2 : i;
        }
        return paramInt2;
    }

    private static int[] readMapColors(Properties paramProperties, String paramString1, String paramString2, String paramString3) {
        int[] arrayOfInt = new int[MapColor.mapColorArray.length];
        Arrays.fill((int[]) arrayOfInt, -1);
        int i = 0;
        Iterator localIterator = paramProperties.keySet().iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            String str1 = paramProperties.getProperty((String) localObject);
            if (((String) localObject).startsWith(paramString2)) {
                String str2 = StrUtils.removePrefix((String) localObject, paramString2);
                int j = getMapColorIndex(str2);
                int k = parseColor(str1);
                if ((j >= 0) && (j < arrayOfInt.length) && (k >= 0)) {
                    arrayOfInt[j] = k;
                    i++;
                } else {
                    warn("Invalid color: " + localObject + " = " + str1);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(paramString3 + " colors: " + i);
        return arrayOfInt;
    }

    private static int[] readPotionColors(Properties paramProperties, String paramString1, String paramString2, String paramString3) {
        int[] arrayOfInt = new int[Potion.potionTypes.length];
        Arrays.fill((int[]) arrayOfInt, -1);
        int i = 0;
        Iterator localIterator = paramProperties.keySet().iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            String str = paramProperties.getProperty((String) localObject);
            if (((String) localObject).startsWith(paramString2)) {
                int j = getPotionId((String) localObject);
                int k = parseColor(str);
                if ((j >= 0) && (j < arrayOfInt.length) && (k >= 0)) {
                    arrayOfInt[j] = k;
                    i++;
                } else {
                    warn("Invalid color: " + localObject + " = " + str);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(paramString3 + " colors: " + i);
        return arrayOfInt;
    }

    private static int getPotionId(String paramString) {
        if (paramString.equals("potion.water")) {
            return 0;
        }
        Potion[] arrayOfPotion = Potion.potionTypes;
        for (int i = 0; i < arrayOfPotion.length; i++) {
            Potion localPotion = arrayOfPotion[i];
            if ((localPotion != null) && (localPotion.getName().equals(paramString))) {
                return localPotion.getId();
            }
        }
        return -1;
    }

    public static int getPotionColor(int paramInt1, int paramInt2) {
        if (potionColors == null) {
            return paramInt2;
        }
        if ((paramInt1 >= 0) && (paramInt1 < potionColors.length)) {
            int i = potionColors[paramInt1];
            return i < 0 ? paramInt2 : i;
        }
        return paramInt2;
    }

    private static int getMapColorIndex(String paramString) {
        return (!paramString.equals("snow")) && (!paramString.equals("white")) ? MapColor.adobeColor.colorIndex : (!paramString.equals("adobe")) && (!paramString.equals("orange")) ? MapColor.lightBlueColor.colorIndex : (!paramString.equals("light_blue")) && (!paramString.equals("lightBlue")) ? -1 : paramString.equals("black") ? MapColor.blackColor.colorIndex : paramString.equals("red") ? MapColor.redColor.colorIndex : paramString.equals("green") ? MapColor.greenColor.colorIndex : paramString.equals("brown") ? MapColor.brownColor.colorIndex : paramString.equals("blue") ? MapColor.blueColor.colorIndex : paramString.equals("purple") ? MapColor.purpleColor.colorIndex : paramString.equals("cyan") ? MapColor.cyanColor.colorIndex : paramString.equals("silver") ? MapColor.silverColor.colorIndex : paramString.equals("gray") ? MapColor.grayColor.colorIndex : paramString.equals("pink") ? MapColor.pinkColor.colorIndex : paramString.equals("lime") ? MapColor.limeColor.colorIndex : paramString.equals("yellow") ? MapColor.yellowColor.colorIndex : paramString.equals("magenta") ? MapColor.magentaColor.colorIndex : paramString.equals("netherrack") ? MapColor.netherrackColor.colorIndex : paramString.equals("podzol") ? MapColor.obsidianColor.colorIndex : paramString.equals("emerald") ? MapColor.emeraldColor.colorIndex : paramString.equals("lapis") ? MapColor.lapisColor.colorIndex : paramString.equals("diamond") ? MapColor.diamondColor.colorIndex : paramString.equals("gold") ? MapColor.goldColor.colorIndex : paramString.equals("quartz") ? MapColor.quartzColor.colorIndex : paramString.equals("wood") ? MapColor.woodColor.colorIndex : paramString.equals("water") ? MapColor.waterColor.colorIndex : paramString.equals("stone") ? MapColor.stoneColor.colorIndex : paramString.equals("dirt") ? MapColor.dirtColor.colorIndex : paramString.equals("clay") ? MapColor.clayColor.colorIndex : paramString.equals("foliage") ? MapColor.foliageColor.colorIndex : paramString.equals("iron") ? MapColor.ironColor.colorIndex : paramString.equals("ice") ? MapColor.iceColor.colorIndex : paramString.equals("tnt") ? MapColor.tntColor.colorIndex : paramString.equals("cloth") ? MapColor.clothColor.colorIndex : paramString.equals("sand") ? MapColor.sandColor.colorIndex : paramString.equals("grass") ? MapColor.grassColor.colorIndex : paramString.equals("air") ? MapColor.airColor.colorIndex : paramString == null ? -1 : MapColor.snowColor.colorIndex;
    }

    private static int[] getMapColors() {
        MapColor[] arrayOfMapColor = MapColor.mapColorArray;
        int[] arrayOfInt = new int[arrayOfMapColor.length];
        Arrays.fill((int[]) arrayOfInt, -1);
        for (int i = 0; (i < arrayOfMapColor.length) && (i < arrayOfInt.length); i++) {
            MapColor localMapColor = arrayOfMapColor[i];
            if (localMapColor != null) {
                arrayOfInt[i] = localMapColor.colorValue;
            }
        }
        return arrayOfInt;
    }

    private static void setMapColors(int[] paramArrayOfInt) {
        if (paramArrayOfInt != null) {
            MapColor[] arrayOfMapColor = MapColor.mapColorArray;
            int i = 0;
            for (int j = 0; (j < arrayOfMapColor.length) && (j < paramArrayOfInt.length); j++) {
                MapColor localMapColor = arrayOfMapColor[j];
                if (localMapColor != null) {
                    int k = paramArrayOfInt[j];
                    if ((k >= 0) && (localMapColor.colorValue != k)) {
                        localMapColor.colorValue = k;
                        i = 1;
                    }
                }
            }
            if (i != 0) {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
    }

    private static int getEntityId(String paramString) {
        if (paramString == null) {
            return -1;
        }
        int i = EntityList.getIDFromString(paramString);
        if (i < 0) {
            return -1;
        }
        String str = EntityList.getStringFromID(i);
        return !Config.equals(paramString, str) ? -1 : i;
    }

    private static void dbg(String paramString) {
        Config.dbg("CustomColors: " + paramString);
    }

    private static void warn(String paramString) {
        Config.warn("CustomColors: " + paramString);
    }

    public static int getExpBarTextColor(int paramInt) {
        return expBarTextColor < 0 ? paramInt : expBarTextColor;
    }

    public static int getBossTextColor(int paramInt) {
        return bossTextColor < 0 ? paramInt : bossTextColor;
    }

    public static int getSignTextColor(int paramInt) {
        return signTextColor < 0 ? paramInt : signTextColor;
    }

    public static abstract interface IColorizer {
        public abstract int getColor(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos);

        public abstract boolean isColorConstant();
    }
}




