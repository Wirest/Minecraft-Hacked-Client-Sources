// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Set;
import java.util.HashSet;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.BlockStateBase;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Properties;

public class CustomColormap implements CustomColors.IColorizer
{
    public String name;
    public String basePath;
    private int format;
    private MatchBlock[] matchBlocks;
    private String source;
    private int color;
    private int yVariance;
    private int yOffset;
    private int width;
    private int height;
    private int[] colors;
    private float[][] colorsRgb;
    private static final int FORMAT_UNKNOWN = -1;
    private static final int FORMAT_VANILLA = 0;
    private static final int FORMAT_GRID = 1;
    private static final int FORMAT_FIXED = 2;
    public static final String FORMAT_VANILLA_STRING = "vanilla";
    public static final String FORMAT_GRID_STRING = "grid";
    public static final String FORMAT_FIXED_STRING = "fixed";
    public static final String[] FORMAT_STRINGS;
    public static final String KEY_FORMAT = "format";
    public static final String KEY_BLOCKS = "blocks";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_COLOR = "color";
    public static final String KEY_Y_VARIANCE = "yVariance";
    public static final String KEY_Y_OFFSET = "yOffset";
    
    static {
        FORMAT_STRINGS = new String[] { "vanilla", "grid", "fixed" };
    }
    
    public CustomColormap(final Properties p_i33_1_, final String p_i33_2_, final int p_i33_3_, final int p_i33_4_, final String p_i33_5_) {
        this.name = null;
        this.basePath = null;
        this.format = -1;
        this.matchBlocks = null;
        this.source = null;
        this.color = -1;
        this.yVariance = 0;
        this.yOffset = 0;
        this.width = 0;
        this.height = 0;
        this.colors = null;
        this.colorsRgb = null;
        final ConnectedParser connectedparser = new ConnectedParser("Colormap");
        this.name = connectedparser.parseName(p_i33_2_);
        this.basePath = connectedparser.parseBasePath(p_i33_2_);
        this.format = this.parseFormat(p_i33_1_.getProperty("format", p_i33_5_));
        this.matchBlocks = connectedparser.parseMatchBlocks(p_i33_1_.getProperty("blocks"));
        this.source = parseTexture(p_i33_1_.getProperty("source"), p_i33_2_, this.basePath);
        this.color = ConnectedParser.parseColor(p_i33_1_.getProperty("color"), -1);
        this.yVariance = connectedparser.parseInt(p_i33_1_.getProperty("yVariance"), 0);
        this.yOffset = connectedparser.parseInt(p_i33_1_.getProperty("yOffset"), 0);
        this.width = p_i33_3_;
        this.height = p_i33_4_;
    }
    
    private int parseFormat(final String p_parseFormat_1_) {
        if (p_parseFormat_1_ == null) {
            return 0;
        }
        if (p_parseFormat_1_.equals("vanilla")) {
            return 0;
        }
        if (p_parseFormat_1_.equals("grid")) {
            return 1;
        }
        if (p_parseFormat_1_.equals("fixed")) {
            return 2;
        }
        warn("Unknown format: " + p_parseFormat_1_);
        return -1;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.format != 0 && this.format != 1) {
            if (this.format != 2) {
                return false;
            }
            if (this.color < 0) {
                this.color = 16777215;
            }
        }
        else {
            if (this.source == null) {
                warn("Source not defined: " + p_isValid_1_);
                return false;
            }
            this.readColors();
            if (this.colors == null) {
                return false;
            }
            if (this.color < 0) {
                if (this.format == 0) {
                    this.color = this.getColor(127, 127);
                }
                if (this.format == 1) {
                    this.color = this.getColorGrid(BiomeGenBase.plains, new BlockPos(0, 64, 0));
                }
            }
        }
        return true;
    }
    
    public boolean isValidMatchBlocks(final String p_isValidMatchBlocks_1_) {
        if (this.matchBlocks == null) {
            this.matchBlocks = this.detectMatchBlocks();
            if (this.matchBlocks == null) {
                warn("Match blocks not defined: " + p_isValidMatchBlocks_1_);
                return false;
            }
        }
        return true;
    }
    
    private MatchBlock[] detectMatchBlocks() {
        final Block block = Block.getBlockFromName(this.name);
        if (block != null) {
            return new MatchBlock[] { new MatchBlock(Block.getIdFromBlock(block)) };
        }
        final Pattern pattern = Pattern.compile("^block([0-9]+).*$");
        final Matcher matcher = pattern.matcher(this.name);
        if (matcher.matches()) {
            final String s = matcher.group(1);
            final int i = Config.parseInt(s, -1);
            if (i >= 0) {
                return new MatchBlock[] { new MatchBlock(i) };
            }
        }
        final ConnectedParser connectedparser = new ConnectedParser("Colormap");
        final MatchBlock[] amatchblock = connectedparser.parseMatchBlock(this.name);
        return (MatchBlock[])((amatchblock != null) ? amatchblock : null);
    }
    
    private void readColors() {
        try {
            this.colors = null;
            if (this.source == null) {
                return;
            }
            final String s = String.valueOf(this.source) + ".png";
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            final BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
            if (bufferedimage == null) {
                return;
            }
            final int i = bufferedimage.getWidth();
            final int j = bufferedimage.getHeight();
            final boolean flag = this.width < 0 || this.width == i;
            final boolean flag2 = this.height < 0 || this.height == j;
            if (!flag || !flag2) {
                dbg("Non-standard palette size: " + i + "x" + j + ", should be: " + this.width + "x" + this.height + ", path: " + s);
            }
            this.width = i;
            this.height = j;
            if (this.width <= 0 || this.height <= 0) {
                warn("Invalid palette size: " + i + "x" + j + ", path: " + s);
                return;
            }
            bufferedimage.getRGB(0, 0, i, j, this.colors = new int[i * j], 0, i);
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private static void dbg(final String p_dbg_0_) {
        Config.dbg("CustomColors: " + p_dbg_0_);
    }
    
    private static void warn(final String p_warn_0_) {
        Config.warn("CustomColors: " + p_warn_0_);
    }
    
    private static String parseTexture(String p_parseTexture_0_, final String p_parseTexture_1_, final String p_parseTexture_2_) {
        if (p_parseTexture_0_ != null) {
            final String s1 = ".png";
            if (p_parseTexture_0_.endsWith(s1)) {
                p_parseTexture_0_ = p_parseTexture_0_.substring(0, p_parseTexture_0_.length() - s1.length());
            }
            p_parseTexture_0_ = fixTextureName(p_parseTexture_0_, p_parseTexture_2_);
            return p_parseTexture_0_;
        }
        String s2 = p_parseTexture_1_;
        final int i = p_parseTexture_1_.lastIndexOf(47);
        if (i >= 0) {
            s2 = p_parseTexture_1_.substring(i + 1);
        }
        final int j = s2.lastIndexOf(46);
        if (j >= 0) {
            s2 = s2.substring(0, j);
        }
        s2 = fixTextureName(s2, p_parseTexture_2_);
        return s2;
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
    
    public boolean matchesBlock(final BlockStateBase p_matchesBlock_1_) {
        return Matches.block(p_matchesBlock_1_, this.matchBlocks);
    }
    
    public int getColorRandom() {
        if (this.format == 2) {
            return this.color;
        }
        final int i = CustomColors.random.nextInt(this.colors.length);
        return this.colors[i];
    }
    
    public int getColor(int p_getColor_1_) {
        p_getColor_1_ = Config.limit(p_getColor_1_, 0, this.colors.length - 1);
        return this.colors[p_getColor_1_] & 0xFFFFFF;
    }
    
    public int getColor(int p_getColor_1_, int p_getColor_2_) {
        p_getColor_1_ = Config.limit(p_getColor_1_, 0, this.width - 1);
        p_getColor_2_ = Config.limit(p_getColor_2_, 0, this.height - 1);
        return this.colors[p_getColor_2_ * this.width + p_getColor_1_] & 0xFFFFFF;
    }
    
    public float[][] getColorsRgb() {
        if (this.colorsRgb == null) {
            this.colorsRgb = toRgb(this.colors);
        }
        return this.colorsRgb;
    }
    
    @Override
    public int getColor(final IBlockAccess p_getColor_1_, final BlockPos p_getColor_2_) {
        final BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
        return this.getColor(biomegenbase, p_getColor_2_);
    }
    
    @Override
    public boolean isColorConstant() {
        return this.format == 2;
    }
    
    public int getColor(final BiomeGenBase p_getColor_1_, final BlockPos p_getColor_2_) {
        return (this.format == 0) ? this.getColorVanilla(p_getColor_1_, p_getColor_2_) : ((this.format == 1) ? this.getColorGrid(p_getColor_1_, p_getColor_2_) : this.color);
    }
    
    public int getColorSmooth(final IBlockAccess p_getColorSmooth_1_, final double p_getColorSmooth_2_, final double p_getColorSmooth_4_, final double p_getColorSmooth_6_, final int p_getColorSmooth_8_) {
        if (this.format == 2) {
            return this.color;
        }
        final int i = MathHelper.floor_double(p_getColorSmooth_2_);
        final int j = MathHelper.floor_double(p_getColorSmooth_4_);
        final int k = MathHelper.floor_double(p_getColorSmooth_6_);
        int l = 0;
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        final BlockPosM blockposm = new BlockPosM(0, 0, 0);
        for (int l2 = i - p_getColorSmooth_8_; l2 <= i + p_getColorSmooth_8_; ++l2) {
            for (int i3 = k - p_getColorSmooth_8_; i3 <= k + p_getColorSmooth_8_; ++i3) {
                blockposm.setXyz(l2, j, i3);
                final int j3 = this.getColor(p_getColorSmooth_1_, blockposm);
                l += (j3 >> 16 & 0xFF);
                i2 += (j3 >> 8 & 0xFF);
                j2 += (j3 & 0xFF);
                ++k2;
            }
        }
        final int k3 = l / k2;
        final int l3 = i2 / k2;
        final int i4 = j2 / k2;
        return k3 << 16 | l3 << 8 | i4;
    }
    
    private int getColorVanilla(final BiomeGenBase p_getColorVanilla_1_, final BlockPos p_getColorVanilla_2_) {
        final double d0 = MathHelper.clamp_float(p_getColorVanilla_1_.getFloatTemperature(p_getColorVanilla_2_), 0.0f, 1.0f);
        double d2 = MathHelper.clamp_float(p_getColorVanilla_1_.getFloatRainfall(), 0.0f, 1.0f);
        d2 *= d0;
        final int i = (int)((1.0 - d0) * (this.width - 1));
        final int j = (int)((1.0 - d2) * (this.height - 1));
        return this.getColor(i, j);
    }
    
    private int getColorGrid(final BiomeGenBase p_getColorGrid_1_, final BlockPos p_getColorGrid_2_) {
        final int i = p_getColorGrid_1_.biomeID;
        int j = p_getColorGrid_2_.getY() - this.yOffset;
        if (this.yVariance > 0) {
            final int k = p_getColorGrid_2_.getX() << 16 + p_getColorGrid_2_.getZ();
            final int l = Config.intHash(k);
            final int i2 = this.yVariance * 2 + 1;
            final int j2 = (l & 0xFF) % i2 - this.yVariance;
            j += j2;
        }
        return this.getColor(i, j);
    }
    
    public int getLength() {
        return (this.format == 2) ? 1 : this.colors.length;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    private static float[][] toRgb(final int[] p_toRgb_0_) {
        final float[][] afloat = new float[p_toRgb_0_.length][3];
        for (int i = 0; i < p_toRgb_0_.length; ++i) {
            final int j = p_toRgb_0_[i];
            final float f = (j >> 16 & 0xFF) / 255.0f;
            final float f2 = (j >> 8 & 0xFF) / 255.0f;
            final float f3 = (j & 0xFF) / 255.0f;
            final float[] afloat2 = afloat[i];
            afloat2[0] = f;
            afloat2[1] = f2;
            afloat2[2] = f3;
        }
        return afloat;
    }
    
    public void addMatchBlock(final MatchBlock p_addMatchBlock_1_) {
        if (this.matchBlocks == null) {
            this.matchBlocks = new MatchBlock[0];
        }
        this.matchBlocks = (MatchBlock[])Config.addObjectToArray(this.matchBlocks, p_addMatchBlock_1_);
    }
    
    public void addMatchBlock(final int p_addMatchBlock_1_, final int p_addMatchBlock_2_) {
        final MatchBlock matchblock = this.getMatchBlock(p_addMatchBlock_1_);
        if (matchblock != null) {
            if (p_addMatchBlock_2_ >= 0) {
                matchblock.addMetadata(p_addMatchBlock_2_);
            }
        }
        else {
            this.addMatchBlock(new MatchBlock(p_addMatchBlock_1_, p_addMatchBlock_2_));
        }
    }
    
    private MatchBlock getMatchBlock(final int p_getMatchBlock_1_) {
        if (this.matchBlocks == null) {
            return null;
        }
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            final MatchBlock matchblock = this.matchBlocks[i];
            if (matchblock.getBlockId() == p_getMatchBlock_1_) {
                return matchblock;
            }
        }
        return null;
    }
    
    public int[] getMatchBlockIds() {
        if (this.matchBlocks == null) {
            return null;
        }
        final Set set = new HashSet();
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            final MatchBlock matchblock = this.matchBlocks[i];
            if (matchblock.getBlockId() >= 0) {
                set.add(matchblock.getBlockId());
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = new int[ainteger.length];
        for (int j = 0; j < ainteger.length; ++j) {
            aint[j] = ainteger[j];
        }
        return aint;
    }
    
    @Override
    public String toString() {
        return this.basePath + "/" + this.name + ", blocks: " + Config.arrayToString(this.matchBlocks) + ", source: " + this.source;
    }
}
