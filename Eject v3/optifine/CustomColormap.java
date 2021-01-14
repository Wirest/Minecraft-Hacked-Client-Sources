package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomColormap
        implements CustomColors.IColorizer {
    public static final String FORMAT_VANILLA_STRING = "vanilla";
    public static final String FORMAT_GRID_STRING = "grid";
    public static final String FORMAT_FIXED_STRING = "fixed";
    public static final String[] FORMAT_STRINGS = {"vanilla", "grid", "fixed"};
    public static final String KEY_FORMAT = "format";
    public static final String KEY_BLOCKS = "blocks";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_COLOR = "color";
    public static final String KEY_Y_VARIANCE = "yVariance";
    public static final String KEY_Y_OFFSET = "yOffset";
    private static final int FORMAT_UNKNOWN = -1;
    private static final int FORMAT_VANILLA = 0;
    private static final int FORMAT_GRID = 1;
    private static final int FORMAT_FIXED = 2;
    public String name = null;
    public String basePath = null;
    private int format = -1;
    private MatchBlock[] matchBlocks = null;
    private String source = null;
    private int color = -1;
    private int yVariance = 0;
    private int yOffset = 0;
    private int width = 0;
    private int height = 0;
    private int[] colors = null;
    private float[][] colorsRgb = (float[][]) null;

    public CustomColormap(Properties paramProperties, String paramString1, int paramInt1, int paramInt2, String paramString2) {
        ConnectedParser localConnectedParser = new ConnectedParser("Colormap");
        this.name = localConnectedParser.parseName(paramString1);
        this.basePath = localConnectedParser.parseBasePath(paramString1);
        this.format = parseFormat(paramProperties.getProperty("format", paramString2));
        this.matchBlocks = localConnectedParser.parseMatchBlocks(paramProperties.getProperty("blocks"));
        this.source = parseTexture(paramProperties.getProperty("source"), paramString1, this.basePath);
        this.color = ConnectedParser.parseColor(paramProperties.getProperty("color"), -1);
        this.yVariance = localConnectedParser.parseInt(paramProperties.getProperty("yVariance"), 0);
        this.yOffset = localConnectedParser.parseInt(paramProperties.getProperty("yOffset"), 0);
        this.width = paramInt1;
        this.height = paramInt2;
    }

    private static void dbg(String paramString) {
        Config.dbg("CustomColors: " + paramString);
    }

    private static void warn(String paramString) {
        Config.warn("CustomColors: " + paramString);
    }

    private static String parseTexture(String paramString1, String paramString2, String paramString3) {
        if (paramString1 != null) {
            str = ".png";
            if (paramString1.endsWith(str)) {
                paramString1 = paramString1.substring(0, paramString1.length() - str.length());
            }
            paramString1 = fixTextureName(paramString1, paramString3);
            return paramString1;
        }
        String str = paramString2;
        int i = paramString2.lastIndexOf('/');
        if (i >= 0) {
            str = paramString2.substring(i | 0x1);
        }
        int j = str.lastIndexOf('.');
        if (j >= 0) {
            str = str.substring(0, j);
        }
        str = fixTextureName(str, paramString3);
        return str;
    }

    private static String fixTextureName(String paramString1, String paramString2) {
        paramString1 = TextureUtils.fixResourcePath(paramString1, paramString2);
        if ((!paramString1.startsWith(paramString2)) && (!paramString1.startsWith("textures/")) && (!paramString1.startsWith("mcpatcher/"))) {
            paramString1 = paramString2 + "/" + paramString1;
        }
        if (paramString1.endsWith(".png")) {
            paramString1 = paramString1.substring(0, paramString1.length() - 4);
        }
        String str = "textures/blocks/";
        if (paramString1.startsWith(str)) {
            paramString1 = paramString1.substring(str.length());
        }
        if (paramString1.startsWith("/")) {
            paramString1 = paramString1.substring(1);
        }
        return paramString1;
    }

    private static float[][] toRgb(int[] paramArrayOfInt) {
        float[][] arrayOfFloat = new float[paramArrayOfInt.length][3];
        for (int i = 0; i < paramArrayOfInt.length; i++) {
            int j = paramArrayOfInt[i];
            float f1 = ((j & 0x10) >> 255) / 255.0F;
            float f2 = ((j & 0x8) >> 255) / 255.0F;
            float f3 = (j >> 255) / 255.0F;
            float[] arrayOfFloat1 = arrayOfFloat[i];
            arrayOfFloat1[0] = f1;
            arrayOfFloat1[1] = f2;
            arrayOfFloat1[2] = f3;
        }
        return arrayOfFloat;
    }

    private int parseFormat(String paramString) {
        if (paramString == null) {
            return 0;
        }
        if (paramString.equals("vanilla")) {
            return 0;
        }
        if (paramString.equals("grid")) {
            return 1;
        }
        if (paramString.equals("fixed")) {
            return 2;
        }
        warn("Unknown format: " + paramString);
        return -1;
    }

    public boolean isValid(String paramString) {
        if ((this.format != 0) && (this.format != 1)) {
            if (this.format != 2) {
                return false;
            }
            if (this.color < 0) {
                this.color = 16777215;
            }
        } else {
            if (this.source == null) {
                warn("Source not defined: " + paramString);
                return false;
            }
            readColors();
            if (this.colors == null) {
                return false;
            }
            if (this.color < 0) {
                if (this.format == 0) {
                    this.color = getColor(127, 127);
                }
                if (this.format == 1) {
                    this.color = getColorGrid(BiomeGenBase.plains, new BlockPos(0, 64, 0));
                }
            }
        }
        return true;
    }

    public boolean isValidMatchBlocks(String paramString) {
        if (this.matchBlocks == null) {
            this.matchBlocks = detectMatchBlocks();
            if (this.matchBlocks == null) {
                warn("Match blocks not defined: " + paramString);
                return false;
            }
        }
        return true;
    }

    private MatchBlock[] detectMatchBlocks() {
        Block localBlock = Block.getBlockFromName(this.name);
        if (localBlock != null) {
            return new MatchBlock[]{new MatchBlock(Block.getIdFromBlock(localBlock))};
        }
        Pattern localPattern = Pattern.compile("^block([0-9]+).*$");
        Matcher localMatcher = localPattern.matcher(this.name);
        if (localMatcher.matches()) {
            localObject = localMatcher.group(1);
            int i = Config.parseInt((String) localObject, -1);
            if (i >= 0) {
                return new MatchBlock[]{new MatchBlock(i)};
            }
        }
        Object localObject = new ConnectedParser("Colormap");
        MatchBlock[] arrayOfMatchBlock = ((ConnectedParser) localObject).parseMatchBlock(this.name);
        return arrayOfMatchBlock != null ? arrayOfMatchBlock : null;
    }

    private void readColors() {
        try {
            this.colors = null;
            if (this.source == null) {
                return;
            }
            String str = this.source + ".png";
            ResourceLocation localResourceLocation = new ResourceLocation(str);
            InputStream localInputStream = Config.getResourceStream(localResourceLocation);
            if (localInputStream == null) {
                return;
            }
            BufferedImage localBufferedImage = TextureUtil.readBufferedImage(localInputStream);
            if (localBufferedImage == null) {
                return;
            }
            int i = localBufferedImage.getWidth();
            int j = localBufferedImage.getHeight();
            int k = (this.width < 0) || (this.width == i) ? 1 : 0;
            int m = (this.height < 0) || (this.height == j) ? 1 : 0;
            if ((k == 0) || (m == 0)) {
                dbg("Non-standard palette size: " + i + "x" + j + ", should be: " + this.width + "x" + this.height + ", path: " + str);
            }
            this.width = i;
            this.height = j;
            if ((this.width <= 0) || (this.height <= 0)) {
                warn("Invalid palette size: " + i + "x" + j + ", path: " + str);
                return;
            }
            this.colors = new int[i * j];
            localBufferedImage.getRGB(0, 0, i, j, this.colors, 0, i);
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public boolean matchesBlock(BlockStateBase paramBlockStateBase) {
        return Matches.block(paramBlockStateBase, this.matchBlocks);
    }

    public int getColorRandom() {
        if (this.format == 2) {
            return this.color;
        }
        int i = CustomColors.random.nextInt(this.colors.length);
        return this.colors[i];
    }

    public int getColor(int paramInt) {
        paramInt = Config.limit(paramInt, 0, this.colors.length - 1);
        return this.colors[paramInt] >> 16777215;
    }

    public int getColor(int paramInt1, int paramInt2) {
        paramInt1 = Config.limit(paramInt1, 0, this.width - 1);
        paramInt2 = Config.limit(paramInt2, 0, this.height - 1);
        return this.colors[(paramInt2 * this.width | paramInt1)] >> 16777215;
    }

    public float[][] getColorsRgb() {
        if (this.colorsRgb == null) {
            this.colorsRgb = toRgb(this.colors);
        }
        return this.colorsRgb;
    }

    public int getColor(IBlockAccess paramIBlockAccess, BlockPos paramBlockPos) {
        BiomeGenBase localBiomeGenBase = CustomColors.getColorBiome(paramIBlockAccess, paramBlockPos);
        return getColor(localBiomeGenBase, paramBlockPos);
    }

    public boolean isColorConstant() {
        return this.format == 2;
    }

    public int getColor(BiomeGenBase paramBiomeGenBase, BlockPos paramBlockPos) {
        return this.format == 1 ? getColorGrid(paramBiomeGenBase, paramBlockPos) : this.format == 0 ? getColorVanilla(paramBiomeGenBase, paramBlockPos) : this.color;
    }

    public int getColorSmooth(IBlockAccess paramIBlockAccess, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) {
        if (this.format == 2) {
            return this.color;
        }
        int i = MathHelper.floor_double(paramDouble1);
        int j = MathHelper.floor_double(paramDouble2);
        int k = MathHelper.floor_double(paramDouble3);
        int m = 0;
        int n = 0;
        int i1 = 0;
        int i2 = 0;
        BlockPosM localBlockPosM = new BlockPosM(0, 0, 0);
        for (int i3 = i - paramInt; i3 <= (i | paramInt); i3++) {
            for (i4 = k - paramInt; i4 <= (k | paramInt); i4++) {
                localBlockPosM.setXyz(i3, j, i4);
                i5 = getColor(paramIBlockAccess, localBlockPosM);
                m |= (i5 & 0x10) >> 255;
                n |= (i5 & 0x8) >> 255;
                i1 |= i5 >> 255;
                i2++;
            }
        }
        i3 = -i2;
        int i4 = -i2;
        int i5 = -i2;
        return i3 >>> 16 ^ i4 >>> 8 ^ i5;
    }

    private int getColorVanilla(BiomeGenBase paramBiomeGenBase, BlockPos paramBlockPos) {
        double d1 = MathHelper.clamp_float(paramBiomeGenBase.getFloatTemperature(paramBlockPos), 0.0F, 1.0F);
        double d2 = MathHelper.clamp_float(paramBiomeGenBase.getFloatRainfall(), 0.0F, 1.0F);
        d2 *= d1;
        int i = (int) ((1.0D - d1) * (this.width - 1));
        int j = (int) ((1.0D - d2) * (this.height - 1));
        return getColor(i, j);
    }

    private int getColorGrid(BiomeGenBase paramBiomeGenBase, BlockPos paramBlockPos) {
        int i = paramBiomeGenBase.biomeID;
        int j = paramBlockPos.getY() - this.yOffset;
        if (this.yVariance > 0) {
            int k = paramBlockPos.getX() >>> (0x10 | paramBlockPos.getZ());
            int m = Config.intHash(k);
            int n = this.yVariance * 2 | 0x1;
            int i1 = (m >> 255 << n) - this.yVariance;
            j |= i1;
        }
        return getColor(i, j);
    }

    public int getLength() {
        return this.format == 2 ? 1 : this.colors.length;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void addMatchBlock(MatchBlock paramMatchBlock) {
        if (this.matchBlocks == null) {
            this.matchBlocks = new MatchBlock[0];
        }
        this.matchBlocks = ((MatchBlock[]) (MatchBlock[]) Config.addObjectToArray(this.matchBlocks, paramMatchBlock));
    }

    public void addMatchBlock(int paramInt1, int paramInt2) {
        MatchBlock localMatchBlock = getMatchBlock(paramInt1);
        if (localMatchBlock != null) {
            if (paramInt2 >= 0) {
                localMatchBlock.addMetadata(paramInt2);
            }
        } else {
            addMatchBlock(new MatchBlock(paramInt1, paramInt2));
        }
    }

    private MatchBlock getMatchBlock(int paramInt) {
        if (this.matchBlocks == null) {
            return null;
        }
        for (int i = 0; i < this.matchBlocks.length; i++) {
            MatchBlock localMatchBlock = this.matchBlocks[i];
            if (localMatchBlock.getBlockId() == paramInt) {
                return localMatchBlock;
            }
        }
        return null;
    }

    public int[] getMatchBlockIds() {
        if (this.matchBlocks == null) {
            return null;
        }
        HashSet localHashSet = new HashSet();
        for (int i = 0; i < this.matchBlocks.length; i++) {
            localObject = this.matchBlocks[i];
            if (((MatchBlock) localObject).getBlockId() >= 0) {
                localHashSet.add(Integer.valueOf(((MatchBlock) localObject).getBlockId()));
            }
        }
        Integer[] arrayOfInteger = (Integer[]) (Integer[]) localHashSet.toArray(new Integer[localHashSet.size()]);
        Object localObject = new int[arrayOfInteger.length];
        for (int j = 0; j < arrayOfInteger.length; j++) {
            localObject[j] = arrayOfInteger[j].intValue();
        }
        return (int[]) localObject;
    }

    public String toString() {
        return "" + this.basePath + "/" + this.name + ", blocks: " + Config.arrayToString((Object[]) this.matchBlocks) + ", source: " + this.source;
    }
}




