package optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
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

public class CustomColors
{
    private static String paletteFormatDefault = "vanilla";
    private static CustomColormap waterColors = null;
    private static CustomColormap foliagePineColors = null;
    private static CustomColormap foliageBirchColors = null;
    private static CustomColormap swampFoliageColors = null;
    private static CustomColormap swampGrassColors = null;
    private static CustomColormap[] colorsBlockColormaps = null;
    private static CustomColormap[][] blockColormaps = (CustomColormap[][])null;
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
    private static float[][] wolfCollarColors = (float[][])null;
    private static float[][] sheepColors = (float[][])null;
    private static int[] textColors = null;
    private static int[] mapColorsOriginal = null;
    private static int[] potionColors = null;
    private static final IBlockState BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
    private static final IBlockState BLOCK_STATE_WATER = Blocks.water.getDefaultState();
    public static Random random = new Random();
    private static final CustomColors.IColorizer COLORIZER_GRASS = new CustomColors.IColorizer()
    {
        public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_)
        {
            BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
            return CustomColors.swampGrassColors != null && biomegenbase == BiomeGenBase.swampland ? CustomColors.swampGrassColors.getColor(biomegenbase, p_getColor_2_) : biomegenbase.getGrassColorAtPos(p_getColor_2_);
        }
        public boolean isColorConstant()
        {
            return false;
        }
    };
    private static final CustomColors.IColorizer COLORIZER_FOLIAGE = new CustomColors.IColorizer()
    {
        public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_)
        {
            BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
            return CustomColors.swampFoliageColors != null && biomegenbase == BiomeGenBase.swampland ? CustomColors.swampFoliageColors.getColor(biomegenbase, p_getColor_2_) : biomegenbase.getFoliageColorAtPos(p_getColor_2_);
        }
        public boolean isColorConstant()
        {
            return false;
        }
    };
    private static final CustomColors.IColorizer COLORIZER_FOLIAGE_PINE = new CustomColors.IColorizer()
    {
        public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_)
        {
            return CustomColors.foliagePineColors != null ? CustomColors.foliagePineColors.getColor(p_getColor_1_, p_getColor_2_) : ColorizerFoliage.getFoliageColorPine();
        }
        public boolean isColorConstant()
        {
            return CustomColors.foliagePineColors == null;
        }
    };
    private static final CustomColors.IColorizer COLORIZER_FOLIAGE_BIRCH = new CustomColors.IColorizer()
    {
        public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_)
        {
            return CustomColors.foliageBirchColors != null ? CustomColors.foliageBirchColors.getColor(p_getColor_1_, p_getColor_2_) : ColorizerFoliage.getFoliageColorBirch();
        }
        public boolean isColorConstant()
        {
            return CustomColors.foliageBirchColors == null;
        }
    };
    private static final CustomColors.IColorizer COLORIZER_WATER = new CustomColors.IColorizer()
    {
        public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_)
        {
            BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
            return CustomColors.waterColors != null ? CustomColors.waterColors.getColor(biomegenbase, p_getColor_2_) : (Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt(biomegenbase, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]) : biomegenbase.waterColorMultiplier);
        }
        public boolean isColorConstant()
        {
            return false;
        }
    };

    public static void update()
    {
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
        blockColormaps = (CustomColormap[][])null;
        useDefaultGrassFoliageColors = true;
        spawnEggPrimaryColors = null;
        spawnEggSecondaryColors = null;
        wolfCollarColors = (float[][])null;
        sheepColors = (float[][])null;
        textColors = null;
        setMapColors(mapColorsOriginal);
        potionColors = null;
        PotionHelper.clearPotionColorCache();
        paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
        String s = "mcpatcher/colormap/";
        String[] astring = new String[] {"water.png", "watercolorX.png"};
        waterColors = getCustomColors(s, astring, 256, 256);
        updateUseDefaultGrassFoliageColors();

        if (Config.isCustomColors())
        {
            String[] astring1 = new String[] {"pine.png", "pinecolor.png"};
            foliagePineColors = getCustomColors(s, astring1, 256, 256);
            String[] astring2 = new String[] {"birch.png", "birchcolor.png"};
            foliageBirchColors = getCustomColors(s, astring2, 256, 256);
            String[] astring3 = new String[] {"swampgrass.png", "swampgrasscolor.png"};
            swampGrassColors = getCustomColors(s, astring3, 256, 256);
            String[] astring4 = new String[] {"swampfoliage.png", "swampfoliagecolor.png"};
            swampFoliageColors = getCustomColors(s, astring4, 256, 256);
            String[] astring5 = new String[] {"sky0.png", "skycolor0.png"};
            skyColors = getCustomColors(s, astring5, 256, 256);
            String[] astring6 = new String[] {"fog0.png", "fogcolor0.png"};
            fogColors = getCustomColors(s, astring6, 256, 256);
            String[] astring7 = new String[] {"underwater.png", "underwatercolor.png"};
            underwaterColors = getCustomColors(s, astring7, 256, 256);
            String[] astring8 = new String[] {"redstone.png", "redstonecolor.png"};
            redstoneColors = getCustomColors(s, astring8, 16, 1);
            xpOrbColors = getCustomColors(s + "xporb.png", -1, -1);
            durabilityColors = getCustomColors(s + "durability.png", -1, -1);
            String[] astring9 = new String[] {"stem.png", "stemcolor.png"};
            stemColors = getCustomColors(s, astring9, 8, 1);
            stemPumpkinColors = getCustomColors(s + "pumpkinstem.png", 8, 1);
            stemMelonColors = getCustomColors(s + "melonstem.png", 8, 1);
            String[] astring10 = new String[] {"myceliumparticle.png", "myceliumparticlecolor.png"};
            myceliumParticleColors = getCustomColors(s, astring10, -1, -1);
            Pair<CustomColormap[], Integer> pair = parseLightmapsRgb();
            lightMapsColorsRgb = (CustomColormap[])pair.getLeft();
            lightmapMinDimensionId = ((Integer)pair.getRight()).intValue();
            readColorProperties("mcpatcher/color.properties");
            blockColormaps = readBlockColormaps(new String[] {s + "custom/", s + "blocks/"}, colorsBlockColormaps, 256, 256);
            updateUseDefaultGrassFoliageColors();
        }
    }

    private static String getValidProperty(String p_getValidProperty_0_, String p_getValidProperty_1_, String[] p_getValidProperty_2_, String p_getValidProperty_3_)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(p_getValidProperty_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return p_getValidProperty_3_;
            }
            else
            {
                Properties properties = new Properties();
                properties.load(inputstream);
                inputstream.close();
                String s = properties.getProperty(p_getValidProperty_1_);

                if (s == null)
                {
                    return p_getValidProperty_3_;
                }
                else
                {
                    List<String> list = Arrays.<String>asList(p_getValidProperty_2_);

                    if (!list.contains(s))
                    {
                        warn("Invalid value: " + p_getValidProperty_1_ + "=" + s);
                        warn("Expected values: " + Config.arrayToString((Object[])p_getValidProperty_2_));
                        return p_getValidProperty_3_;
                    }
                    else
                    {
                        dbg("" + p_getValidProperty_1_ + "=" + s);
                        return s;
                    }
                }
            }
        }
        catch (FileNotFoundException var9)
        {
            return p_getValidProperty_3_;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            return p_getValidProperty_3_;
        }
    }

    private static Pair<CustomColormap[], Integer> parseLightmapsRgb()
    {
        String s = "mcpatcher/lightmap/world";
        String s1 = ".png";
        String[] astring = ResUtils.collectFiles(s, s1);
        Map<Integer, String> map = new HashMap();

        for (int i = 0; i < astring.length; ++i)
        {
            String s2 = astring[i];
            String s3 = StrUtils.removePrefixSuffix(s2, s, s1);
            int j = Config.parseInt(s3, Integer.MIN_VALUE);

            if (j == Integer.MIN_VALUE)
            {
                warn("Invalid dimension ID: " + s3 + ", path: " + s2);
            }
            else
            {
                map.put(Integer.valueOf(j), s2);
            }
        }

        Set<Integer> set = map.keySet();
        Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
        Arrays.sort((Object[])ainteger);

        if (ainteger.length <= 0)
        {
            return new ImmutablePair((Object)null, Integer.valueOf(0));
        }
        else
        {
            int j1 = ainteger[0].intValue();
            int k1 = ainteger[ainteger.length - 1].intValue();
            int k = k1 - j1 + 1;
            CustomColormap[] acustomcolormap = new CustomColormap[k];

            for (int l = 0; l < ainteger.length; ++l)
            {
                Integer integer = ainteger[l];
                String s4 = (String)map.get(integer);
                CustomColormap customcolormap = getCustomColors(s4, -1, -1);

                if (customcolormap != null)
                {
                    if (customcolormap.getWidth() < 16)
                    {
                        warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s4);
                    }
                    else
                    {
                        int i1 = integer.intValue() - j1;
                        acustomcolormap[i1] = customcolormap;
                    }
                }
            }

            return new ImmutablePair(acustomcolormap, Integer.valueOf(j1));
        }
    }

    private static int getTextureHeight(String p_getTextureHeight_0_, int p_getTextureHeight_1_)
    {
        try
        {
            InputStream inputstream = Config.getResourceStream(new ResourceLocation(p_getTextureHeight_0_));

            if (inputstream == null)
            {
                return p_getTextureHeight_1_;
            }
            else
            {
                BufferedImage bufferedimage = ImageIO.read(inputstream);
                inputstream.close();
                return bufferedimage == null ? p_getTextureHeight_1_ : bufferedimage.getHeight();
            }
        }
        catch (IOException var4)
        {
            return p_getTextureHeight_1_;
        }
    }

    private static void readColorProperties(String p_readColorProperties_0_)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(p_readColorProperties_0_);
            InputStream inputstream = Config.getResourceStream(resourcelocation);

            if (inputstream == null)
            {
                return;
            }

            dbg("Loading " + p_readColorProperties_0_);
            Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            particleWaterColor = readColor(properties, new String[] {"particle.water", "drop.water"});
            particlePortalColor = readColor(properties, "particle.portal");
            lilyPadColor = readColor(properties, "lilypad");
            expBarTextColor = readColor(properties, "text.xpbar");
            bossTextColor = readColor(properties, "text.boss");
            signTextColor = readColor(properties, "text.sign");
            fogColorNether = readColorVec3(properties, "fog.nether");
            fogColorEnd = readColorVec3(properties, "fog.end");
            skyColorEnd = readColorVec3(properties, "sky.end");
            colorsBlockColormaps = readCustomColormaps(properties, p_readColorProperties_0_);
            spawnEggPrimaryColors = readSpawnEggColors(properties, p_readColorProperties_0_, "egg.shell.", "Spawn egg shell");
            spawnEggSecondaryColors = readSpawnEggColors(properties, p_readColorProperties_0_, "egg.spots.", "Spawn egg spot");
            wolfCollarColors = readDyeColors(properties, p_readColorProperties_0_, "collar.", "Wolf collar");
            sheepColors = readDyeColors(properties, p_readColorProperties_0_, "sheep.", "Sheep");
            textColors = readTextColors(properties, p_readColorProperties_0_, "text.code.", "Text");
            int[] aint = readMapColors(properties, p_readColorProperties_0_, "map.", "Map");

            if (aint != null)
            {
                if (mapColorsOriginal == null)
                {
                    mapColorsOriginal = getMapColors();
                }

                setMapColors(aint);
            }

            potionColors = readPotionColors(properties, p_readColorProperties_0_, "potion.", "Potion");
            xpOrbTime = Config.parseInt(properties.getProperty("xporb.time"), -1);
        }
        catch (FileNotFoundException var5)
        {
            return;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private static CustomColormap[] readCustomColormaps(Properties p_readCustomColormaps_0_, String p_readCustomColormaps_1_)
    {
        List list = new ArrayList();
        String s = "palette.block.";
        Map map = new HashMap();

        for (Object s1 : p_readCustomColormaps_0_.keySet())
        {
            String s2 = p_readCustomColormaps_0_.getProperty((String) s1);

            if (((String) s1).startsWith(s))
            {
                map.put(s1, s2);
            }
        }

        String[] astring = (String[])((String[])map.keySet().toArray(new String[map.size()]));

        for (int j = 0; j < astring.length; ++j)
        {
            String s6 = astring[j];
            String s3 = p_readCustomColormaps_0_.getProperty(s6);
            dbg("Block palette: " + s6 + " = " + s3);
            String s4 = s6.substring(s.length());
            String s5 = TextureUtils.getBasePath(p_readCustomColormaps_1_);
            s4 = TextureUtils.fixResourcePath(s4, s5);
            CustomColormap customcolormap = getCustomColors(s4, 256, 256);

            if (customcolormap == null)
            {
                warn("Colormap not found: " + s4);
            }
            else
            {
                ConnectedParser connectedparser = new ConnectedParser("CustomColors");
                MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s3);

                if (amatchblock != null && amatchblock.length > 0)
                {
                    for (int i = 0; i < amatchblock.length; ++i)
                    {
                        MatchBlock matchblock = amatchblock[i];
                        customcolormap.addMatchBlock(matchblock);
                    }

                    list.add(customcolormap);
                }
                else
                {
                    warn("Invalid match blocks: " + s3);
                }
            }
        }

        if (list.size() <= 0)
        {
            return null;
        }
        else
        {
            CustomColormap[] acustomcolormap = (CustomColormap[])((CustomColormap[])list.toArray(new CustomColormap[list.size()]));
            return acustomcolormap;
        }
    }

    private static CustomColormap[][] readBlockColormaps(String[] p_readBlockColormaps_0_, CustomColormap[] p_readBlockColormaps_1_, int p_readBlockColormaps_2_, int p_readBlockColormaps_3_)
    {
        String[] astring = ResUtils.collectFiles(p_readBlockColormaps_0_, new String[] {".properties"});
        Arrays.sort((Object[])astring);
        List list = new ArrayList();

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            dbg("Block colormap: " + s);

            try
            {
                ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                InputStream inputstream = Config.getResourceStream(resourcelocation);

                if (inputstream == null)
                {
                    warn("File not found: " + s);
                }
                else
                {
                    Properties properties = new Properties();
                    properties.load(inputstream);
                    CustomColormap customcolormap = new CustomColormap(properties, s, p_readBlockColormaps_2_, p_readBlockColormaps_3_, paletteFormatDefault);

                    if (customcolormap.isValid(s) && customcolormap.isValidMatchBlocks(s))
                    {
                        addToBlockList(customcolormap, list);
                    }
                }
            }
            catch (FileNotFoundException var12)
            {
                warn("File not found: " + s);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        if (p_readBlockColormaps_1_ != null)
        {
            for (int j = 0; j < p_readBlockColormaps_1_.length; ++j)
            {
                CustomColormap customcolormap1 = p_readBlockColormaps_1_[j];
                addToBlockList(customcolormap1, list);
            }
        }

        if (list.size() <= 0)
        {
            return (CustomColormap[][])null;
        }
        else
        {
            CustomColormap[][] acustomcolormap = blockListToArray(list);
            return acustomcolormap;
        }
    }

    private static void addToBlockList(CustomColormap p_addToBlockList_0_, List p_addToBlockList_1_)
    {
        int[] aint = p_addToBlockList_0_.getMatchBlockIds();

        if (aint != null && aint.length > 0)
        {
            for (int i = 0; i < aint.length; ++i)
            {
                int j = aint[i];

                if (j < 0)
                {
                    warn("Invalid block ID: " + j);
                }
                else
                {
                    addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
                }
            }
        }
        else
        {
            warn("No match blocks: " + Config.arrayToString(aint));
        }
    }

    private static void addToList(CustomColormap p_addToList_0_, List p_addToList_1_, int p_addToList_2_)
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

    private static CustomColormap[][] blockListToArray(List p_blockListToArray_0_)
    {
        CustomColormap[][] acustomcolormap = new CustomColormap[p_blockListToArray_0_.size()][];

        for (int i = 0; i < p_blockListToArray_0_.size(); ++i)
        {
            List list = (List)p_blockListToArray_0_.get(i);

            if (list != null)
            {
                CustomColormap[] acustomcolormap1 = (CustomColormap[])((CustomColormap[])list.toArray(new CustomColormap[list.size()]));
                acustomcolormap[i] = acustomcolormap1;
            }
        }

        return acustomcolormap;
    }

    private static int readColor(Properties p_readColor_0_, String[] p_readColor_1_)
    {
        for (int i = 0; i < p_readColor_1_.length; ++i)
        {
            String s = p_readColor_1_[i];
            int j = readColor(p_readColor_0_, s);

            if (j >= 0)
            {
                return j;
            }
        }

        return -1;
    }

    private static int readColor(Properties p_readColor_0_, String p_readColor_1_)
    {
        String s = p_readColor_0_.getProperty(p_readColor_1_);

        if (s == null)
        {
            return -1;
        }
        else
        {
            s = s.trim();
            int i = parseColor(s);

            if (i < 0)
            {
                warn("Invalid color: " + p_readColor_1_ + " = " + s);
                return i;
            }
            else
            {
                dbg(p_readColor_1_ + " = " + s);
                return i;
            }
        }
    }

    private static int parseColor(String p_parseColor_0_)
    {
        if (p_parseColor_0_ == null)
        {
            return -1;
        }
        else
        {
            p_parseColor_0_ = p_parseColor_0_.trim();

            try
            {
                int i = Integer.parseInt(p_parseColor_0_, 16) & 16777215;
                return i;
            }
            catch (NumberFormatException var2)
            {
                return -1;
            }
        }
    }

    private static Vec3 readColorVec3(Properties p_readColorVec3_0_, String p_readColorVec3_1_)
    {
        int i = readColor(p_readColorVec3_0_, p_readColorVec3_1_);

        if (i < 0)
        {
            return null;
        }
        else
        {
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            return new Vec3((double)f, (double)f1, (double)f2);
        }
    }

    private static CustomColormap getCustomColors(String p_getCustomColors_0_, String[] p_getCustomColors_1_, int p_getCustomColors_2_, int p_getCustomColors_3_)
    {
        for (int i = 0; i < p_getCustomColors_1_.length; ++i)
        {
            String s = p_getCustomColors_1_[i];
            s = p_getCustomColors_0_ + s;
            CustomColormap customcolormap = getCustomColors(s, p_getCustomColors_2_, p_getCustomColors_3_);

            if (customcolormap != null)
            {
                return customcolormap;
            }
        }

        return null;
    }

    public static CustomColormap getCustomColors(String p_getCustomColors_0_, int p_getCustomColors_1_, int p_getCustomColors_2_)
    {
        try
        {
            ResourceLocation resourcelocation = new ResourceLocation(p_getCustomColors_0_);

            if (!Config.hasResource(resourcelocation))
            {
                return null;
            }
            else
            {
                dbg("Colormap " + p_getCustomColors_0_);
                Properties properties = new Properties();
                String s = StrUtils.replaceSuffix(p_getCustomColors_0_, ".png", ".properties");
                ResourceLocation resourcelocation1 = new ResourceLocation(s);

                if (Config.hasResource(resourcelocation1))
                {
                    InputStream inputstream = Config.getResourceStream(resourcelocation1);
                    properties.load(inputstream);
                    inputstream.close();
                    dbg("Colormap properties: " + s);
                }
                else
                {
                    properties.put("format", paletteFormatDefault);
                    properties.put("source", p_getCustomColors_0_);
                    s = p_getCustomColors_0_;
                }

                CustomColormap customcolormap = new CustomColormap(properties, s, p_getCustomColors_1_, p_getCustomColors_2_, paletteFormatDefault);
                return !customcolormap.isValid(s) ? null : customcolormap;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    public static void updateUseDefaultGrassFoliageColors()
    {
        useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad p_getColorMultiplier_0_, Block p_getColorMultiplier_1_, IBlockAccess p_getColorMultiplier_2_, BlockPos p_getColorMultiplier_3_, RenderEnv p_getColorMultiplier_4_)
    {
        if (blockColormaps != null)
        {
            IBlockState iblockstate = p_getColorMultiplier_4_.getBlockState();

            if (!p_getColorMultiplier_0_.hasTintIndex())
            {
                if (p_getColorMultiplier_1_ == Blocks.grass)
                {
                    iblockstate = BLOCK_STATE_DIRT;
                }

                if (p_getColorMultiplier_1_ == Blocks.redstone_wire)
                {
                    return -1;
                }
            }

            if (p_getColorMultiplier_1_ == Blocks.double_plant && p_getColorMultiplier_4_.getMetadata() >= 8)
            {
                p_getColorMultiplier_3_ = p_getColorMultiplier_3_.down();
                iblockstate = p_getColorMultiplier_2_.getBlockState(p_getColorMultiplier_3_);
            }

            CustomColormap customcolormap = getBlockColormap(iblockstate);

            if (customcolormap != null)
            {
                if (Config.isSmoothBiomes() && !customcolormap.isColorConstant())
                {
                    return getSmoothColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolormap, p_getColorMultiplier_4_.getColorizerBlockPosM());
                }

                return customcolormap.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
            }
        }

        if (!p_getColorMultiplier_0_.hasTintIndex())
        {
            return -1;
        }
        else if (p_getColorMultiplier_1_ == Blocks.waterlily)
        {
            return getLilypadColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
        }
        else if (p_getColorMultiplier_1_ == Blocks.redstone_wire)
        {
            return getRedstoneColor(p_getColorMultiplier_4_.getBlockState());
        }
        else if (p_getColorMultiplier_1_ instanceof BlockStem)
        {
            return getStemColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, p_getColorMultiplier_4_);
        }
        else if (useDefaultGrassFoliageColors)
        {
            return -1;
        }
        else
        {
            int i = p_getColorMultiplier_4_.getMetadata();
            CustomColors.IColorizer customcolors$icolorizer;

            if (p_getColorMultiplier_1_ != Blocks.grass && p_getColorMultiplier_1_ != Blocks.tallgrass && p_getColorMultiplier_1_ != Blocks.double_plant)
            {
                if (p_getColorMultiplier_1_ == Blocks.double_plant)
                {
                    customcolors$icolorizer = COLORIZER_GRASS;

                    if (i >= 8)
                    {
                        p_getColorMultiplier_3_ = p_getColorMultiplier_3_.down();
                    }
                }
                else if (p_getColorMultiplier_1_ == Blocks.leaves)
                {
                    switch (i & 3)
                    {
                        case 0:
                            customcolors$icolorizer = COLORIZER_FOLIAGE;
                            break;

                        case 1:
                            customcolors$icolorizer = COLORIZER_FOLIAGE_PINE;
                            break;

                        case 2:
                            customcolors$icolorizer = COLORIZER_FOLIAGE_BIRCH;
                            break;

                        default:
                            customcolors$icolorizer = COLORIZER_FOLIAGE;
                    }
                }
                else if (p_getColorMultiplier_1_ == Blocks.leaves2)
                {
                    customcolors$icolorizer = COLORIZER_FOLIAGE;
                }
                else
                {
                    if (p_getColorMultiplier_1_ != Blocks.vine)
                    {
                        return -1;
                    }

                    customcolors$icolorizer = COLORIZER_FOLIAGE;
                }
            }
            else
            {
                customcolors$icolorizer = COLORIZER_GRASS;
            }

            return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? getSmoothColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolors$icolorizer, p_getColorMultiplier_4_.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
        }
    }

    protected static BiomeGenBase getColorBiome(IBlockAccess p_getColorBiome_0_, BlockPos p_getColorBiome_1_)
    {
        BiomeGenBase biomegenbase = p_getColorBiome_0_.getBiomeGenForCoords(p_getColorBiome_1_);

        if (biomegenbase == BiomeGenBase.swampland && !Config.isSwampColors())
        {
            biomegenbase = BiomeGenBase.plains;
        }

        return biomegenbase;
    }

    private static CustomColormap getBlockColormap(IBlockState p_getBlockColormap_0_)
    {
        if (blockColormaps == null)
        {
            return null;
        }
        else if (!(p_getBlockColormap_0_ instanceof BlockStateBase))
        {
            return null;
        }
        else
        {
            BlockStateBase blockstatebase = (BlockStateBase)p_getBlockColormap_0_;
            int i = blockstatebase.getBlockId();

            if (i >= 0 && i < blockColormaps.length)
            {
                CustomColormap[] acustomcolormap = blockColormaps[i];

                if (acustomcolormap == null)
                {
                    return null;
                }
                else
                {
                    for (int j = 0; j < acustomcolormap.length; ++j)
                    {
                        CustomColormap customcolormap = acustomcolormap[j];

                        if (customcolormap.matchesBlock(blockstatebase))
                        {
                            return customcolormap;
                        }
                    }

                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }

    private static int getSmoothColorMultiplier(IBlockAccess p_getSmoothColorMultiplier_0_, BlockPos p_getSmoothColorMultiplier_1_, CustomColors.IColorizer p_getSmoothColorMultiplier_2_, BlockPosM p_getSmoothColorMultiplier_3_)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = p_getSmoothColorMultiplier_1_.getX();
        int i1 = p_getSmoothColorMultiplier_1_.getY();
        int j1 = p_getSmoothColorMultiplier_1_.getZ();
        BlockPosM blockposm = p_getSmoothColorMultiplier_3_;

        for (int k1 = l - 1; k1 <= l + 1; ++k1)
        {
            for (int l1 = j1 - 1; l1 <= j1 + 1; ++l1)
            {
                blockposm.setXyz(k1, i1, l1);
                int i2 = p_getSmoothColorMultiplier_2_.getColor(p_getSmoothColorMultiplier_0_, blockposm);
                i += i2 >> 16 & 255;
                j += i2 >> 8 & 255;
                k += i2 & 255;
            }
        }

        int j2 = i / 9;
        int k2 = j / 9;
        int l2 = k / 9;
        return j2 << 16 | k2 << 8 | l2;
    }

    public static int getFluidColor(IBlockAccess p_getFluidColor_0_, IBlockState p_getFluidColor_1_, BlockPos p_getFluidColor_2_, RenderEnv p_getFluidColor_3_)
    {
        Block block = p_getFluidColor_1_.getBlock();
        CustomColors.IColorizer customcolors$icolorizer = getBlockColormap(p_getFluidColor_1_);

        if (customcolors$icolorizer == null && block.getMaterial() == Material.water)
        {
            customcolors$icolorizer = COLORIZER_WATER;
        }

        return customcolors$icolorizer == null ? block.colorMultiplier(p_getFluidColor_0_, p_getFluidColor_2_) : (Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant() ? getSmoothColorMultiplier(p_getFluidColor_0_, p_getFluidColor_2_, customcolors$icolorizer, p_getFluidColor_3_.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(p_getFluidColor_0_, p_getFluidColor_2_));
    }

    public static void updatePortalFX(EntityFX p_updatePortalFX_0_)
    {
        if (particlePortalColor >= 0)
        {
            int i = particlePortalColor;
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            p_updatePortalFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    public static void updateMyceliumFX(EntityFX p_updateMyceliumFX_0_)
    {
        if (myceliumParticleColors != null)
        {
            int i = myceliumParticleColors.getColorRandom();
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            p_updateMyceliumFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneColor(IBlockState p_getRedstoneColor_0_)
    {
        if (redstoneColors == null)
        {
            return -1;
        }
        else
        {
            int i = getRedstoneLevel(p_getRedstoneColor_0_, 15);
            int j = redstoneColors.getColor(i);
            return j;
        }
    }

    public static void updateReddustFX(EntityFX p_updateReddustFX_0_, IBlockAccess p_updateReddustFX_1_, double p_updateReddustFX_2_, double p_updateReddustFX_4_, double p_updateReddustFX_6_)
    {
        if (redstoneColors != null)
        {
            IBlockState iblockstate = p_updateReddustFX_1_.getBlockState(new BlockPos(p_updateReddustFX_2_, p_updateReddustFX_4_, p_updateReddustFX_6_));
            int i = getRedstoneLevel(iblockstate, 15);
            int j = redstoneColors.getColor(i);
            int k = j >> 16 & 255;
            int l = j >> 8 & 255;
            int i1 = j & 255;
            float f = (float)k / 255.0F;
            float f1 = (float)l / 255.0F;
            float f2 = (float)i1 / 255.0F;
            p_updateReddustFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    private static int getRedstoneLevel(IBlockState p_getRedstoneLevel_0_, int p_getRedstoneLevel_1_)
    {
        Block block = p_getRedstoneLevel_0_.getBlock();

        if (!(block instanceof BlockRedstoneWire))
        {
            return p_getRedstoneLevel_1_;
        }
        else
        {
            Object object = p_getRedstoneLevel_0_.getValue(BlockRedstoneWire.POWER);

            if (!(object instanceof Integer))
            {
                return p_getRedstoneLevel_1_;
            }
            else
            {
                Integer integer = (Integer)object;
                return integer.intValue();
            }
        }
    }

    public static float getXpOrbTimer(float p_getXpOrbTimer_0_)
    {
        if (xpOrbTime <= 0)
        {
            return p_getXpOrbTimer_0_;
        }
        else
        {
            float f = 628.0F / (float)xpOrbTime;
            return p_getXpOrbTimer_0_ * f;
        }
    }

    public static int getXpOrbColor(float p_getXpOrbColor_0_)
    {
        if (xpOrbColors == null)
        {
            return -1;
        }
        else
        {
            int i = (int)Math.round((double)((MathHelper.sin(p_getXpOrbColor_0_) + 1.0F) * (float)(xpOrbColors.getLength() - 1)) / 2.0D);
            int j = xpOrbColors.getColor(i);
            return j;
        }
    }

    public static int getDurabilityColor(int p_getDurabilityColor_0_)
    {
        if (durabilityColors == null)
        {
            return -1;
        }
        else
        {
            int i = p_getDurabilityColor_0_ * durabilityColors.getLength() / 255;
            int j = durabilityColors.getColor(i);
            return j;
        }
    }

    public static void updateWaterFX(EntityFX p_updateWaterFX_0_, IBlockAccess p_updateWaterFX_1_, double p_updateWaterFX_2_, double p_updateWaterFX_4_, double p_updateWaterFX_6_)
    {
        if (waterColors != null || blockColormaps != null)
        {
            BlockPos blockpos = new BlockPos(p_updateWaterFX_2_, p_updateWaterFX_4_, p_updateWaterFX_6_);
            RenderEnv renderenv = RenderEnv.getInstance(p_updateWaterFX_1_, BLOCK_STATE_WATER, blockpos);
            int i = getFluidColor(p_updateWaterFX_1_, BLOCK_STATE_WATER, blockpos, renderenv);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;

            if (particleWaterColor >= 0)
            {
                int i1 = particleWaterColor >> 16 & 255;
                int j1 = particleWaterColor >> 8 & 255;
                int k1 = particleWaterColor & 255;
                f *= (float)i1 / 255.0F;
                f1 *= (float)j1 / 255.0F;
                f2 *= (float)k1 / 255.0F;
            }

            p_updateWaterFX_0_.setRBGColorF(f, f1, f2);
        }
    }

    private static int getLilypadColorMultiplier(IBlockAccess p_getLilypadColorMultiplier_0_, BlockPos p_getLilypadColorMultiplier_1_)
    {
        return lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(p_getLilypadColorMultiplier_0_, p_getLilypadColorMultiplier_1_) : lilyPadColor;
    }

    private static Vec3 getFogColorNether(Vec3 p_getFogColorNether_0_)
    {
        return fogColorNether == null ? p_getFogColorNether_0_ : fogColorNether;
    }

    private static Vec3 getFogColorEnd(Vec3 p_getFogColorEnd_0_)
    {
        return fogColorEnd == null ? p_getFogColorEnd_0_ : fogColorEnd;
    }

    private static Vec3 getSkyColorEnd(Vec3 p_getSkyColorEnd_0_)
    {
        return skyColorEnd == null ? p_getSkyColorEnd_0_ : skyColorEnd;
    }

    public static Vec3 getSkyColor(Vec3 p_getSkyColor_0_, IBlockAccess p_getSkyColor_1_, double p_getSkyColor_2_, double p_getSkyColor_4_, double p_getSkyColor_6_)
    {
        if (skyColors == null)
        {
            return p_getSkyColor_0_;
        }
        else
        {
            int i = skyColors.getColorSmooth(p_getSkyColor_1_, p_getSkyColor_2_, p_getSkyColor_4_, p_getSkyColor_6_, 3);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            float f3 = (float)p_getSkyColor_0_.xCoord / 0.5F;
            float f4 = (float)p_getSkyColor_0_.yCoord / 0.66275F;
            float f5 = (float)p_getSkyColor_0_.zCoord;
            f = f * f3;
            f1 = f1 * f4;
            f2 = f2 * f5;
            Vec3 vec3 = skyColorFader.getColor((double)f, (double)f1, (double)f2);
            return vec3;
        }
    }

    private static Vec3 getFogColor(Vec3 p_getFogColor_0_, IBlockAccess p_getFogColor_1_, double p_getFogColor_2_, double p_getFogColor_4_, double p_getFogColor_6_)
    {
        if (fogColors == null)
        {
            return p_getFogColor_0_;
        }
        else
        {
            int i = fogColors.getColorSmooth(p_getFogColor_1_, p_getFogColor_2_, p_getFogColor_4_, p_getFogColor_6_, 3);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            float f3 = (float)p_getFogColor_0_.xCoord / 0.753F;
            float f4 = (float)p_getFogColor_0_.yCoord / 0.8471F;
            float f5 = (float)p_getFogColor_0_.zCoord;
            f = f * f3;
            f1 = f1 * f4;
            f2 = f2 * f5;
            Vec3 vec3 = fogColorFader.getColor((double)f, (double)f1, (double)f2);
            return vec3;
        }
    }

    public static Vec3 getUnderwaterColor(IBlockAccess p_getUnderwaterColor_0_, double p_getUnderwaterColor_1_, double p_getUnderwaterColor_3_, double p_getUnderwaterColor_5_)
    {
        if (underwaterColors == null)
        {
            return null;
        }
        else
        {
            int i = underwaterColors.getColorSmooth(p_getUnderwaterColor_0_, p_getUnderwaterColor_1_, p_getUnderwaterColor_3_, p_getUnderwaterColor_5_, 3);
            int j = i >> 16 & 255;
            int k = i >> 8 & 255;
            int l = i & 255;
            float f = (float)j / 255.0F;
            float f1 = (float)k / 255.0F;
            float f2 = (float)l / 255.0F;
            Vec3 vec3 = underwaterColorFader.getColor((double)f, (double)f1, (double)f2);
            return vec3;
        }
    }

    private static int getStemColorMultiplier(Block p_getStemColorMultiplier_0_, IBlockAccess p_getStemColorMultiplier_1_, BlockPos p_getStemColorMultiplier_2_, RenderEnv p_getStemColorMultiplier_3_)
    {
        CustomColormap customcolormap = stemColors;

        if (p_getStemColorMultiplier_0_ == Blocks.pumpkin_stem && stemPumpkinColors != null)
        {
            customcolormap = stemPumpkinColors;
        }

        if (p_getStemColorMultiplier_0_ == Blocks.melon_stem && stemMelonColors != null)
        {
            customcolormap = stemMelonColors;
        }

        if (customcolormap == null)
        {
            return -1;
        }
        else
        {
            int i = p_getStemColorMultiplier_3_.getMetadata();
            return customcolormap.getColor(i);
        }
    }

    public static boolean updateLightmap(World p_updateLightmap_0_, float p_updateLightmap_1_, int[] p_updateLightmap_2_, boolean p_updateLightmap_3_)
    {
        if (p_updateLightmap_0_ == null)
        {
            return false;
        }
        else if (lightMapsColorsRgb == null)
        {
            return false;
        }
        else
        {
            int i = p_updateLightmap_0_.provider.getDimensionId();
            int j = i - lightmapMinDimensionId;

            if (j >= 0 && j < lightMapsColorsRgb.length)
            {
                CustomColormap customcolormap = lightMapsColorsRgb[j];

                if (customcolormap == null)
                {
                    return false;
                }
                else
                {
                    int k = customcolormap.getHeight();

                    if (p_updateLightmap_3_ && k < 64)
                    {
                        return false;
                    }
                    else
                    {
                        int l = customcolormap.getWidth();

                        if (l < 16)
                        {
                            warn("Invalid lightmap width: " + l + " for dimension: " + i);
                            lightMapsColorsRgb[j] = null;
                            return false;
                        }
                        else
                        {
                            int i1 = 0;

                            if (p_updateLightmap_3_)
                            {
                                i1 = l * 16 * 2;
                            }

                            float f = 1.1666666F * (p_updateLightmap_0_.getSunBrightness(1.0F) - 0.2F);

                            if (p_updateLightmap_0_.getLastLightningBolt() > 0)
                            {
                                f = 1.0F;
                            }

                            f = Config.limitTo1(f);
                            float f1 = f * (float)(l - 1);
                            float f2 = Config.limitTo1(p_updateLightmap_1_ + 0.5F) * (float)(l - 1);
                            float f3 = Config.limitTo1(Config.getGameSettings().gammaSetting);
                            boolean flag = f3 > 1.0E-4F;
                            float[][] afloat = customcolormap.getColorsRgb();
                            getLightMapColumn(afloat, f1, i1, l, sunRgbs);
                            getLightMapColumn(afloat, f2, i1 + 16 * l, l, torchRgbs);
                            float[] afloat1 = new float[3];

                            for (int j1 = 0; j1 < 16; ++j1)
                            {
                                for (int k1 = 0; k1 < 16; ++k1)
                                {
                                    for (int l1 = 0; l1 < 3; ++l1)
                                    {
                                        float f4 = Config.limitTo1(sunRgbs[j1][l1] + torchRgbs[k1][l1]);

                                        if (flag)
                                        {
                                            float f5 = 1.0F - f4;
                                            f5 = 1.0F - f5 * f5 * f5 * f5;
                                            f4 = f3 * f5 + (1.0F - f3) * f4;
                                        }

                                        afloat1[l1] = f4;
                                    }

                                    int i2 = (int)(afloat1[0] * 255.0F);
                                    int j2 = (int)(afloat1[1] * 255.0F);
                                    int k2 = (int)(afloat1[2] * 255.0F);
                                    p_updateLightmap_2_[j1 * 16 + k1] = -16777216 | i2 << 16 | j2 << 8 | k2;
                                }
                            }

                            return true;
                        }
                    }
                }
            }
            else
            {
                return false;
            }
        }
    }

    private static void getLightMapColumn(float[][] p_getLightMapColumn_0_, float p_getLightMapColumn_1_, int p_getLightMapColumn_2_, int p_getLightMapColumn_3_, float[][] p_getLightMapColumn_4_)
    {
        int i = (int)Math.floor((double)p_getLightMapColumn_1_);
        int j = (int)Math.ceil((double)p_getLightMapColumn_1_);

        if (i == j)
        {
            for (int i1 = 0; i1 < 16; ++i1)
            {
                float[] afloat3 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + i1 * p_getLightMapColumn_3_ + i];
                float[] afloat4 = p_getLightMapColumn_4_[i1];

                for (int j1 = 0; j1 < 3; ++j1)
                {
                    afloat4[j1] = afloat3[j1];
                }
            }
        }
        else
        {
            float f = 1.0F - (p_getLightMapColumn_1_ - (float)i);
            float f1 = 1.0F - ((float)j - p_getLightMapColumn_1_);

            for (int k = 0; k < 16; ++k)
            {
                float[] afloat = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + i];
                float[] afloat1 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + j];
                float[] afloat2 = p_getLightMapColumn_4_[k];

                for (int l = 0; l < 3; ++l)
                {
                    afloat2[l] = afloat[l] * f + afloat1[l] * f1;
                }
            }
        }
    }

    public static Vec3 getWorldFogColor(Vec3 p_getWorldFogColor_0_, WorldClient p_getWorldFogColor_1_, Entity p_getWorldFogColor_2_, float p_getWorldFogColor_3_)
    {
        int i = p_getWorldFogColor_1_.provider.getDimensionId();

        switch (i)
        {
            case -1:
                p_getWorldFogColor_0_ = getFogColorNether(p_getWorldFogColor_0_);
                break;

            case 0:
                Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldFogColor_0_ = getFogColor(p_getWorldFogColor_0_, minecraft.theWorld, p_getWorldFogColor_2_.posX, p_getWorldFogColor_2_.posY + 1.0D, p_getWorldFogColor_2_.posZ);
                break;

            case 1:
                p_getWorldFogColor_0_ = getFogColorEnd(p_getWorldFogColor_0_);
        }

        return p_getWorldFogColor_0_;
    }

    public static Vec3 getWorldSkyColor(Vec3 p_getWorldSkyColor_0_, World p_getWorldSkyColor_1_, Entity p_getWorldSkyColor_2_, float p_getWorldSkyColor_3_)
    {
        int i = p_getWorldSkyColor_1_.provider.getDimensionId();

        switch (i)
        {
            case 0:
                Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldSkyColor_0_ = getSkyColor(p_getWorldSkyColor_0_, minecraft.theWorld, p_getWorldSkyColor_2_.posX, p_getWorldSkyColor_2_.posY + 1.0D, p_getWorldSkyColor_2_.posZ);
                break;

            case 1:
                p_getWorldSkyColor_0_ = getSkyColorEnd(p_getWorldSkyColor_0_);
        }

        return p_getWorldSkyColor_0_;
    }

    private static int[] readSpawnEggColors(Properties p_readSpawnEggColors_0_, String p_readSpawnEggColors_1_, String p_readSpawnEggColors_2_, String p_readSpawnEggColors_3_)
    {
        List<Integer> list = new ArrayList();
        Set set = p_readSpawnEggColors_0_.keySet();
        int i = 0;

        for (Object s : set)
        {
            String s1 = p_readSpawnEggColors_0_.getProperty((String) s);

            if (((String) s).startsWith(p_readSpawnEggColors_2_))
            {
                String s2 = StrUtils.removePrefix((String) s, p_readSpawnEggColors_2_);
                int j = getEntityId(s2);
                int k = parseColor(s1);

                if (j >= 0 && k >= 0)
                {
                    while (((List)list).size() <= j)
                    {
                        list.add(Integer.valueOf(-1));
                    }

                    list.set(j, Integer.valueOf(k));
                    ++i;
                }
                else
                {
                    warn("Invalid spawn egg color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(p_readSpawnEggColors_3_ + " colors: " + i);
            int[] aint = new int[list.size()];

            for (int l = 0; l < aint.length; ++l)
            {
                aint[l] = ((Integer)list.get(l)).intValue();
            }

            return aint;
        }
    }

    private static int getSpawnEggColor(ItemMonsterPlacer p_getSpawnEggColor_0_, ItemStack p_getSpawnEggColor_1_, int p_getSpawnEggColor_2_, int p_getSpawnEggColor_3_)
    {
        int i = p_getSpawnEggColor_1_.getMetadata();
        int[] aint = p_getSpawnEggColor_2_ == 0 ? spawnEggPrimaryColors : spawnEggSecondaryColors;

        if (aint == null)
        {
            return p_getSpawnEggColor_3_;
        }
        else if (i >= 0 && i < aint.length)
        {
            int j = aint[i];
            return j < 0 ? p_getSpawnEggColor_3_ : j;
        }
        else
        {
            return p_getSpawnEggColor_3_;
        }
    }

    public static int getColorFromItemStack(ItemStack p_getColorFromItemStack_0_, int p_getColorFromItemStack_1_, int p_getColorFromItemStack_2_)
    {
        if (p_getColorFromItemStack_0_ == null)
        {
            return p_getColorFromItemStack_2_;
        }
        else
        {
            Item item = p_getColorFromItemStack_0_.getItem();
            return item == null ? p_getColorFromItemStack_2_ : (item instanceof ItemMonsterPlacer ? getSpawnEggColor((ItemMonsterPlacer)item, p_getColorFromItemStack_0_, p_getColorFromItemStack_1_, p_getColorFromItemStack_2_) : p_getColorFromItemStack_2_);
        }
    }

    private static float[][] readDyeColors(Properties p_readDyeColors_0_, String p_readDyeColors_1_, String p_readDyeColors_2_, String p_readDyeColors_3_)
    {
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        Map<String, EnumDyeColor> map = new HashMap();

        for (int i = 0; i < aenumdyecolor.length; ++i)
        {
            EnumDyeColor enumdyecolor = aenumdyecolor[i];
            map.put(enumdyecolor.getName(), enumdyecolor);
        }

        float[][] afloat1 = new float[aenumdyecolor.length][];
        int k = 0;

        for (Object s : p_readDyeColors_0_.keySet())
        {
            String s1 = p_readDyeColors_0_.getProperty((String) s);

            if (((String) s).startsWith(p_readDyeColors_2_))
            {
                String s2 = StrUtils.removePrefix((String) s, p_readDyeColors_2_);

                if (s2.equals("lightBlue"))
                {
                    s2 = "light_blue";
                }

                EnumDyeColor enumdyecolor1 = (EnumDyeColor)map.get(s2);
                int j = parseColor(s1);

                if (enumdyecolor1 != null && j >= 0)
                {
                    float[] afloat = new float[] {(float)(j >> 16 & 255) / 255.0F, (float)(j >> 8 & 255) / 255.0F, (float)(j & 255) / 255.0F};
                    afloat1[enumdyecolor1.ordinal()] = afloat;
                    ++k;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (k <= 0)
        {
            return (float[][])null;
        }
        else
        {
            dbg(p_readDyeColors_3_ + " colors: " + k);
            return afloat1;
        }
    }

    private static float[] getDyeColors(EnumDyeColor p_getDyeColors_0_, float[][] p_getDyeColors_1_, float[] p_getDyeColors_2_)
    {
        if (p_getDyeColors_1_ == null)
        {
            return p_getDyeColors_2_;
        }
        else if (p_getDyeColors_0_ == null)
        {
            return p_getDyeColors_2_;
        }
        else
        {
            float[] afloat = p_getDyeColors_1_[p_getDyeColors_0_.ordinal()];
            return afloat == null ? p_getDyeColors_2_ : afloat;
        }
    }

    public static float[] getWolfCollarColors(EnumDyeColor p_getWolfCollarColors_0_, float[] p_getWolfCollarColors_1_)
    {
        return getDyeColors(p_getWolfCollarColors_0_, wolfCollarColors, p_getWolfCollarColors_1_);
    }

    public static float[] getSheepColors(EnumDyeColor p_getSheepColors_0_, float[] p_getSheepColors_1_)
    {
        return getDyeColors(p_getSheepColors_0_, sheepColors, p_getSheepColors_1_);
    }

    private static int[] readTextColors(Properties p_readTextColors_0_, String p_readTextColors_1_, String p_readTextColors_2_, String p_readTextColors_3_)
    {
        int[] aint = new int[32];
        Arrays.fill((int[])aint, (int) - 1);
        int i = 0;

        for (Object s : p_readTextColors_0_.keySet())
        {
            String s1 = p_readTextColors_0_.getProperty((String) s);

            if (((String) s).startsWith(p_readTextColors_2_))
            {
                String s2 = StrUtils.removePrefix((String) s, p_readTextColors_2_);
                int j = Config.parseInt(s2, -1);
                int k = parseColor(s1);

                if (j >= 0 && j < aint.length && k >= 0)
                {
                    aint[j] = k;
                    ++i;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(p_readTextColors_3_ + " colors: " + i);
            return aint;
        }
    }

    public static int getTextColor(int p_getTextColor_0_, int p_getTextColor_1_)
    {
        if (textColors == null)
        {
            return p_getTextColor_1_;
        }
        else if (p_getTextColor_0_ >= 0 && p_getTextColor_0_ < textColors.length)
        {
            int i = textColors[p_getTextColor_0_];
            return i < 0 ? p_getTextColor_1_ : i;
        }
        else
        {
            return p_getTextColor_1_;
        }
    }

    private static int[] readMapColors(Properties p_readMapColors_0_, String p_readMapColors_1_, String p_readMapColors_2_, String p_readMapColors_3_)
    {
        int[] aint = new int[MapColor.mapColorArray.length];
        Arrays.fill((int[])aint, (int) - 1);
        int i = 0;

        for (Object s : p_readMapColors_0_.keySet())
        {
            String s1 = p_readMapColors_0_.getProperty((String) s);

            if (((String) s).startsWith(p_readMapColors_2_))
            {
                String s2 = StrUtils.removePrefix((String) s, p_readMapColors_2_);
                int j = getMapColorIndex(s2);
                int k = parseColor(s1);

                if (j >= 0 && j < aint.length && k >= 0)
                {
                    aint[j] = k;
                    ++i;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(p_readMapColors_3_ + " colors: " + i);
            return aint;
        }
    }

    private static int[] readPotionColors(Properties p_readPotionColors_0_, String p_readPotionColors_1_, String p_readPotionColors_2_, String p_readPotionColors_3_)
    {
        int[] aint = new int[Potion.potionTypes.length];
        Arrays.fill((int[])aint, (int) - 1);
        int i = 0;

        for (Object s : p_readPotionColors_0_.keySet())
        {
            String s1 = p_readPotionColors_0_.getProperty((String) s);

            if (((String) s).startsWith(p_readPotionColors_2_))
            {
                int j = getPotionId((String) s);
                int k = parseColor(s1);

                if (j >= 0 && j < aint.length && k >= 0)
                {
                    aint[j] = k;
                    ++i;
                }
                else
                {
                    warn("Invalid color: " + s + " = " + s1);
                }
            }
        }

        if (i <= 0)
        {
            return null;
        }
        else
        {
            dbg(p_readPotionColors_3_ + " colors: " + i);
            return aint;
        }
    }

    private static int getPotionId(String p_getPotionId_0_)
    {
        if (p_getPotionId_0_.equals("potion.water"))
        {
            return 0;
        }
        else
        {
            Potion[] apotion = Potion.potionTypes;

            for (int i = 0; i < apotion.length; ++i)
            {
                Potion potion = apotion[i];

                if (potion != null && potion.getName().equals(p_getPotionId_0_))
                {
                    return potion.getId();
                }
            }

            return -1;
        }
    }

    public static int getPotionColor(int p_getPotionColor_0_, int p_getPotionColor_1_)
    {
        if (potionColors == null)
        {
            return p_getPotionColor_1_;
        }
        else if (p_getPotionColor_0_ >= 0 && p_getPotionColor_0_ < potionColors.length)
        {
            int i = potionColors[p_getPotionColor_0_];
            return i < 0 ? p_getPotionColor_1_ : i;
        }
        else
        {
            return p_getPotionColor_1_;
        }
    }

    private static int getMapColorIndex(String p_getMapColorIndex_0_)
    {
        return p_getMapColorIndex_0_ == null ? -1 : (p_getMapColorIndex_0_.equals("air") ? MapColor.airColor.colorIndex : (p_getMapColorIndex_0_.equals("grass") ? MapColor.grassColor.colorIndex : (p_getMapColorIndex_0_.equals("sand") ? MapColor.sandColor.colorIndex : (p_getMapColorIndex_0_.equals("cloth") ? MapColor.clothColor.colorIndex : (p_getMapColorIndex_0_.equals("tnt") ? MapColor.tntColor.colorIndex : (p_getMapColorIndex_0_.equals("ice") ? MapColor.iceColor.colorIndex : (p_getMapColorIndex_0_.equals("iron") ? MapColor.ironColor.colorIndex : (p_getMapColorIndex_0_.equals("foliage") ? MapColor.foliageColor.colorIndex : (p_getMapColorIndex_0_.equals("clay") ? MapColor.clayColor.colorIndex : (p_getMapColorIndex_0_.equals("dirt") ? MapColor.dirtColor.colorIndex : (p_getMapColorIndex_0_.equals("stone") ? MapColor.stoneColor.colorIndex : (p_getMapColorIndex_0_.equals("water") ? MapColor.waterColor.colorIndex : (p_getMapColorIndex_0_.equals("wood") ? MapColor.woodColor.colorIndex : (p_getMapColorIndex_0_.equals("quartz") ? MapColor.quartzColor.colorIndex : (p_getMapColorIndex_0_.equals("gold") ? MapColor.goldColor.colorIndex : (p_getMapColorIndex_0_.equals("diamond") ? MapColor.diamondColor.colorIndex : (p_getMapColorIndex_0_.equals("lapis") ? MapColor.lapisColor.colorIndex : (p_getMapColorIndex_0_.equals("emerald") ? MapColor.emeraldColor.colorIndex : (p_getMapColorIndex_0_.equals("podzol") ? MapColor.obsidianColor.colorIndex : (p_getMapColorIndex_0_.equals("netherrack") ? MapColor.netherrackColor.colorIndex : (!p_getMapColorIndex_0_.equals("snow") && !p_getMapColorIndex_0_.equals("white") ? (!p_getMapColorIndex_0_.equals("adobe") && !p_getMapColorIndex_0_.equals("orange") ? (p_getMapColorIndex_0_.equals("magenta") ? MapColor.magentaColor.colorIndex : (!p_getMapColorIndex_0_.equals("light_blue") && !p_getMapColorIndex_0_.equals("lightBlue") ? (p_getMapColorIndex_0_.equals("yellow") ? MapColor.yellowColor.colorIndex : (p_getMapColorIndex_0_.equals("lime") ? MapColor.limeColor.colorIndex : (p_getMapColorIndex_0_.equals("pink") ? MapColor.pinkColor.colorIndex : (p_getMapColorIndex_0_.equals("gray") ? MapColor.grayColor.colorIndex : (p_getMapColorIndex_0_.equals("silver") ? MapColor.silverColor.colorIndex : (p_getMapColorIndex_0_.equals("cyan") ? MapColor.cyanColor.colorIndex : (p_getMapColorIndex_0_.equals("purple") ? MapColor.purpleColor.colorIndex : (p_getMapColorIndex_0_.equals("blue") ? MapColor.blueColor.colorIndex : (p_getMapColorIndex_0_.equals("brown") ? MapColor.brownColor.colorIndex : (p_getMapColorIndex_0_.equals("green") ? MapColor.greenColor.colorIndex : (p_getMapColorIndex_0_.equals("red") ? MapColor.redColor.colorIndex : (p_getMapColorIndex_0_.equals("black") ? MapColor.blackColor.colorIndex : -1)))))))))))) : MapColor.lightBlueColor.colorIndex)) : MapColor.adobeColor.colorIndex) : MapColor.snowColor.colorIndex)))))))))))))))))))));
    }

    private static int[] getMapColors()
    {
        MapColor[] amapcolor = MapColor.mapColorArray;
        int[] aint = new int[amapcolor.length];
        Arrays.fill((int[])aint, (int) - 1);

        for (int i = 0; i < amapcolor.length && i < aint.length; ++i)
        {
            MapColor mapcolor = amapcolor[i];

            if (mapcolor != null)
            {
                aint[i] = mapcolor.colorValue;
            }
        }

        return aint;
    }

    private static void setMapColors(int[] p_setMapColors_0_)
    {
        if (p_setMapColors_0_ != null)
        {
            MapColor[] amapcolor = MapColor.mapColorArray;
            boolean flag = false;

            for (int i = 0; i < amapcolor.length && i < p_setMapColors_0_.length; ++i)
            {
                MapColor mapcolor = amapcolor[i];

                if (mapcolor != null)
                {
                    int j = p_setMapColors_0_[i];

                    if (j >= 0 && mapcolor.colorValue != j)
                    {
                        mapcolor.colorValue = j;
                        flag = true;
                    }
                }
            }

            if (flag)
            {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
    }

    private static int getEntityId(String p_getEntityId_0_)
    {
        if (p_getEntityId_0_ == null)
        {
            return -1;
        }
        else
        {
            int i = EntityList.getIDFromString(p_getEntityId_0_);

            if (i < 0)
            {
                return -1;
            }
            else
            {
                String s = EntityList.getStringFromID(i);
                return !Config.equals(p_getEntityId_0_, s) ? -1 : i;
            }
        }
    }

    private static void dbg(String p_dbg_0_)
    {
        Config.dbg("CustomColors: " + p_dbg_0_);
    }

    private static void warn(String p_warn_0_)
    {
        Config.warn("CustomColors: " + p_warn_0_);
    }

    public static int getExpBarTextColor(int p_getExpBarTextColor_0_)
    {
        return expBarTextColor < 0 ? p_getExpBarTextColor_0_ : expBarTextColor;
    }

    public static int getBossTextColor(int p_getBossTextColor_0_)
    {
        return bossTextColor < 0 ? p_getBossTextColor_0_ : bossTextColor;
    }

    public static int getSignTextColor(int p_getSignTextColor_0_)
    {
        return signTextColor < 0 ? p_getSignTextColor_0_ : signTextColor;
    }

    public interface IColorizer
    {
        int getColor(IBlockAccess var1, BlockPos var2);

        boolean isColorConstant();
    }
}
