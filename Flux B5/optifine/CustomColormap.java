package optifine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class CustomColormap implements CustomColors.IColorizer
{
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
    private float[][] colorsRgb = (float[][])null;
    private static final int FORMAT_UNKNOWN = -1;
    private static final int FORMAT_VANILLA = 0;
    private static final int FORMAT_GRID = 1;
    private static final int FORMAT_FIXED = 2;
    public static final String KEY_FORMAT = "format";
    public static final String KEY_BLOCKS = "blocks";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_COLOR = "color";
    public static final String KEY_Y_VARIANCE = "yVariance";
    public static final String KEY_Y_OFFSET = "yOffset";

    public CustomColormap(Properties props, String path, int width, int height)
    {
        ConnectedParser cp = new ConnectedParser("Colormap");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.format = this.parseFormat(props.getProperty("format"));
        this.matchBlocks = cp.parseMatchBlocks(props.getProperty("blocks"));
        this.source = parseTexture(props.getProperty("source"), path, this.basePath);
        this.color = ConnectedParser.parseColor(props.getProperty("color"), -1);
        this.yVariance = cp.parseInt(props.getProperty("yVariance"), 0);
        this.yOffset = cp.parseInt(props.getProperty("yOffset"), 0);
        this.width = width;
        this.height = height;
    }

    private int parseFormat(String str)
    {
        if (str == null)
        {
            return 0;
        }
        else if (str.equals("vanilla"))
        {
            return 0;
        }
        else if (str.equals("grid"))
        {
            return 1;
        }
        else if (str.equals("fixed"))
        {
            return 2;
        }
        else
        {
            warn("Unknown format: " + str);
            return -1;
        }
    }

    public boolean isValid(String path)
    {
        if (this.format != 0 && this.format != 1)
        {
            if (this.format != 2)
            {
                return false;
            }

            if (this.color < 0)
            {
                this.color = 16777215;
            }
        }
        else
        {
            if (this.source == null)
            {
                warn("Source not defined: " + path);
                return false;
            }

            this.readColors();

            if (this.colors == null)
            {
                return false;
            }

            if (this.color < 0)
            {
                if (this.format == 0)
                {
                    this.color = this.getColor(127, 127);
                }

                if (this.format == 1)
                {
                    this.color = this.getColorGrid(BiomeGenBase.plains, new BlockPos(0, 64, 0));
                }
            }
        }

        return true;
    }

    public boolean isValidMatchBlocks(String path)
    {
        if (this.matchBlocks == null)
        {
            this.matchBlocks = this.detectMatchBlocks();

            if (this.matchBlocks == null)
            {
                warn("Match blocks not defined: " + path);
                return false;
            }
        }

        return true;
    }

    private MatchBlock[] detectMatchBlocks()
    {
        Block block = Block.getBlockFromName(this.name);

        if (block != null)
        {
            return new MatchBlock[] {new MatchBlock(Block.getIdFromBlock(block))};
        }
        else
        {
            Pattern p = Pattern.compile("^block([0-9]+).*$");
            Matcher m = p.matcher(this.name);

            if (m.matches())
            {
                String cp = m.group(1);
                int mbs = Config.parseInt(cp, -1);

                if (mbs >= 0)
                {
                    return new MatchBlock[] {new MatchBlock(mbs)};
                }
            }

            ConnectedParser cp1 = new ConnectedParser("Colormap");
            MatchBlock[] mbs1 = cp1.parseMatchBlock(this.name);
            return mbs1 != null ? mbs1 : null;
        }
    }

    private void readColors()
    {
        try
        {
            this.colors = null;

            if (this.source == null)
            {
                return;
            }

            String e = this.source + ".png";
            ResourceLocation loc = new ResourceLocation(e);
            InputStream is = Config.getResourceStream(loc);

            if (is == null)
            {
                return;
            }

            BufferedImage img = TextureUtil.func_177053_a(is);

            if (img == null)
            {
                return;
            }

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();
            boolean widthOk = this.width < 0 || this.width == imgWidth;
            boolean heightOk = this.height < 0 || this.height == imgHeight;

            if (!widthOk || !heightOk)
            {
                dbg("Non-standard palette size: " + imgWidth + "x" + imgHeight + ", should be: " + this.width + "x" + this.height + ", path: " + e);
            }

            this.width = imgWidth;
            this.height = imgHeight;

            if (this.width <= 0 || this.height <= 0)
            {
                warn("Invalid palette size: " + imgWidth + "x" + imgHeight + ", path: " + e);
                return;
            }

            this.colors = new int[imgWidth * imgHeight];
            img.getRGB(0, 0, imgWidth, imgHeight, this.colors, 0, imgWidth);
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
        }
    }

    private static void dbg(String str)
    {
        Config.dbg("CustomColors: " + str);
    }

    private static void warn(String str)
    {
        Config.warn("CustomColors: " + str);
    }

    private static String parseTexture(String texStr, String path, String basePath)
    {
        String str;

        if (texStr != null)
        {
            str = ".png";

            if (texStr.endsWith(str))
            {
                texStr = texStr.substring(0, texStr.length() - str.length());
            }

            texStr = fixTextureName(texStr, basePath);
            return texStr;
        }
        else
        {
            str = path;
            int pos = path.lastIndexOf(47);

            if (pos >= 0)
            {
                str = path.substring(pos + 1);
            }

            int pos2 = str.lastIndexOf(46);

            if (pos2 >= 0)
            {
                str = str.substring(0, pos2);
            }

            str = fixTextureName(str, basePath);
            return str;
        }
    }

    private static String fixTextureName(String iconName, String basePath)
    {
        iconName = TextureUtils.fixResourcePath(iconName, basePath);

        if (!iconName.startsWith(basePath) && !iconName.startsWith("textures/") && !iconName.startsWith("mcpatcher/"))
        {
            iconName = basePath + "/" + iconName;
        }

        if (iconName.endsWith(".png"))
        {
            iconName = iconName.substring(0, iconName.length() - 4);
        }

        String pathBlocks = "textures/blocks/";

        if (iconName.startsWith(pathBlocks))
        {
            iconName = iconName.substring(pathBlocks.length());
        }

        if (iconName.startsWith("/"))
        {
            iconName = iconName.substring(1);
        }

        return iconName;
    }

    public boolean matchesBlock(BlockStateBase blockState)
    {
        return Matches.block(blockState, this.matchBlocks);
    }

    public int getColorRandom()
    {
        if (this.format == 2)
        {
            return this.color;
        }
        else
        {
            int index = CustomColors.random.nextInt(this.colors.length);
            return this.colors[index];
        }
    }

    public int getColor(int index)
    {
        index = Config.limit(index, 0, this.colors.length);
        return this.colors[index] & 16777215;
    }

    public int getColor(int cx, int cy)
    {
        cx = Config.limit(cx, 0, this.width - 1);
        cy = Config.limit(cy, 0, this.height - 1);
        return this.colors[cy * this.width + cx] & 16777215;
    }

    public float[][] getColorsRgb()
    {
        if (this.colorsRgb == null)
        {
            this.colorsRgb = toRgb(this.colors);
        }

        return this.colorsRgb;
    }

    public int getColor(IBlockAccess blockAccess, BlockPos blockPos)
    {
        BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
        return this.getColor(biome, blockPos);
    }

    public boolean isColorConstant()
    {
        return this.format == 2;
    }

    public int getColor(BiomeGenBase biome, BlockPos blockPos)
    {
        return this.format == 0 ? this.getColorVanilla(biome, blockPos) : (this.format == 1 ? this.getColorGrid(biome, blockPos) : this.color);
    }

    public int getColorSmooth(IBlockAccess blockAccess, double x, double y, double z, int radius)
    {
        if (this.format == 2)
        {
            return this.color;
        }
        else
        {
            int x0 = MathHelper.floor_double(x);
            int y0 = MathHelper.floor_double(y);
            int z0 = MathHelper.floor_double(z);
            int sumRed = 0;
            int sumGreen = 0;
            int sumBlue = 0;
            int count = 0;
            BlockPosM blockPosM = new BlockPosM(0, 0, 0);
            int r;
            int g;
            int b;

            for (r = x0 - radius; r <= x0 + radius; ++r)
            {
                for (g = z0 - radius; g <= z0 + radius; ++g)
                {
                    blockPosM.setXyz(r, y0, g);
                    b = this.getColor(blockAccess, blockPosM);
                    sumRed += b >> 16 & 255;
                    sumGreen += b >> 8 & 255;
                    sumBlue += b & 255;
                    ++count;
                }
            }

            r = sumRed / count;
            g = sumGreen / count;
            b = sumBlue / count;
            return r << 16 | g << 8 | b;
        }
    }

    private int getColorVanilla(BiomeGenBase biome, BlockPos blockPos)
    {
        double temperature = (double)MathHelper.clamp_float(biome.func_180626_a(blockPos), 0.0F, 1.0F);
        double rainfall = (double)MathHelper.clamp_float(biome.getFloatRainfall(), 0.0F, 1.0F);
        rainfall *= temperature;
        int cx = (int)((1.0D - temperature) * (double)(this.width - 1));
        int cy = (int)((1.0D - rainfall) * (double)(this.height - 1));
        return this.getColor(cx, cy);
    }

    private int getColorGrid(BiomeGenBase biome, BlockPos blockPos)
    {
        int cx = biome.biomeID;
        int cy = blockPos.getY() - this.yOffset;

        if (this.yVariance > 0)
        {
            int seed = blockPos.getX() << 16 + blockPos.getZ();
            int rand = Config.intHash(seed);
            int range = this.yVariance * 2 + 1;
            int diff = (rand & 255) % range - this.yVariance;
            cy += diff;
        }

        return this.getColor(cx, cy);
    }

    public int getLength()
    {
        return this.format == 2 ? 1 : this.colors.length;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    private static float[][] toRgb(int[] cols)
    {
        float[][] colsRgb = new float[cols.length][3];

        for (int i = 0; i < cols.length; ++i)
        {
            int col = cols[i];
            float rf = (float)(col >> 16 & 255) / 255.0F;
            float gf = (float)(col >> 8 & 255) / 255.0F;
            float bf = (float)(col & 255) / 255.0F;
            float[] colRgb = colsRgb[i];
            colRgb[0] = rf;
            colRgb[1] = gf;
            colRgb[2] = bf;
        }

        return colsRgb;
    }

    public void addMatchBlock(MatchBlock mb)
    {
        if (this.matchBlocks == null)
        {
            this.matchBlocks = new MatchBlock[0];
        }

        this.matchBlocks = (MatchBlock[])((MatchBlock[])Config.addObjectToArray(this.matchBlocks, mb));
    }

    public void addMatchBlock(int blockId, int metadata)
    {
        MatchBlock mb = this.getMatchBlock(blockId);

        if (mb != null)
        {
            if (metadata >= 0)
            {
                mb.addMetadata(metadata);
            }
        }
        else
        {
            this.addMatchBlock(new MatchBlock(blockId, metadata));
        }
    }

    private MatchBlock getMatchBlock(int blockId)
    {
        if (this.matchBlocks == null)
        {
            return null;
        }
        else
        {
            for (int i = 0; i < this.matchBlocks.length; ++i)
            {
                MatchBlock mb = this.matchBlocks[i];

                if (mb.getBlockId() == blockId)
                {
                    return mb;
                }
            }

            return null;
        }
    }

    public int[] getMatchBlockIds()
    {
        if (this.matchBlocks == null)
        {
            return null;
        }
        else
        {
            HashSet setIds = new HashSet();

            for (int ints = 0; ints < this.matchBlocks.length; ++ints)
            {
                MatchBlock ids = this.matchBlocks[ints];

                if (ids.getBlockId() >= 0)
                {
                    setIds.add(Integer.valueOf(ids.getBlockId()));
                }
            }

            Integer[] var5 = (Integer[])((Integer[])setIds.toArray(new Integer[setIds.size()]));
            int[] var6 = new int[var5.length];

            for (int i = 0; i < var5.length; ++i)
            {
                var6[i] = var5[i].intValue();
            }

            return var6;
        }
    }

    public String toString()
    {
        return "" + this.basePath + "/" + this.name + ", blocks: " + Config.arrayToString((Object[])this.matchBlocks) + ", source: " + this.source;
    }
}
