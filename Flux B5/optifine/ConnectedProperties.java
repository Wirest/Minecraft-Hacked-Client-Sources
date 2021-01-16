package optifine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedProperties
{
    public String name = null;
    public String basePath = null;
    public MatchBlock[] matchBlocks = null;
    public int[] metadatas = null;
    public String[] matchTiles = null;
    public int method = 0;
    public String[] tiles = null;
    public int connect = 0;
    public int faces = 63;
    public BiomeGenBase[] biomes = null;
    public int minHeight = 0;
    public int maxHeight = 1024;
    public int renderPass = 0;
    public boolean innerSeams = false;
    public int width = 0;
    public int height = 0;
    public int[] weights = null;
    public int symmetry = 1;
    public int[] sumWeights = null;
    public int sumAllWeights = 1;
    public TextureAtlasSprite[] matchTileIcons = null;
    public TextureAtlasSprite[] tileIcons = null;
    public static final int METHOD_NONE = 0;
    public static final int METHOD_CTM = 1;
    public static final int METHOD_HORIZONTAL = 2;
    public static final int METHOD_TOP = 3;
    public static final int METHOD_RANDOM = 4;
    public static final int METHOD_REPEAT = 5;
    public static final int METHOD_VERTICAL = 6;
    public static final int METHOD_FIXED = 7;
    public static final int METHOD_HORIZONTAL_VERTICAL = 8;
    public static final int METHOD_VERTICAL_HORIZONTAL = 9;
    public static final int CONNECT_NONE = 0;
    public static final int CONNECT_BLOCK = 1;
    public static final int CONNECT_TILE = 2;
    public static final int CONNECT_MATERIAL = 3;
    public static final int CONNECT_UNKNOWN = 128;
    public static final int FACE_BOTTOM = 1;
    public static final int FACE_TOP = 2;
    public static final int FACE_NORTH = 4;
    public static final int FACE_SOUTH = 8;
    public static final int FACE_WEST = 16;
    public static final int FACE_EAST = 32;
    public static final int FACE_SIDES = 60;
    public static final int FACE_ALL = 63;
    public static final int FACE_UNKNOWN = 128;
    public static final int SYMMETRY_NONE = 1;
    public static final int SYMMETRY_OPPOSITE = 2;
    public static final int SYMMETRY_ALL = 6;
    public static final int SYMMETRY_UNKNOWN = 128;

    public ConnectedProperties(Properties props, String path)
    {
        ConnectedParser cp = new ConnectedParser("ConnectedTextures");
        this.name = cp.parseName(path);
        this.basePath = cp.parseBasePath(path);
        this.matchBlocks = cp.parseMatchBlocks(props.getProperty("matchBlocks"));
        this.metadatas = cp.parseIntList(props.getProperty("metadata"));
        this.matchTiles = this.parseMatchTiles(props.getProperty("matchTiles"));
        this.method = parseMethod(props.getProperty("method"));
        this.tiles = this.parseTileNames(props.getProperty("tiles"));
        this.connect = parseConnect(props.getProperty("connect"));
        this.faces = parseFaces(props.getProperty("faces"));
        this.biomes = cp.parseBiomes(props.getProperty("biomes"));
        this.minHeight = cp.parseInt(props.getProperty("minHeight"), -1);
        this.maxHeight = cp.parseInt(props.getProperty("maxHeight"), 1024);
        this.renderPass = cp.parseInt(props.getProperty("renderPass"));
        this.innerSeams = ConnectedParser.parseBoolean(props.getProperty("innerSeams"));
        this.width = cp.parseInt(props.getProperty("width"));
        this.height = cp.parseInt(props.getProperty("height"));
        this.weights = cp.parseIntList(props.getProperty("weights"));
        this.symmetry = parseSymmetry(props.getProperty("symmetry"));
    }

    private String[] parseMatchTiles(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            String[] names = Config.tokenize(str, " ");

            for (int i = 0; i < names.length; ++i)
            {
                String iconName = names[i];

                if (iconName.endsWith(".png"))
                {
                    iconName = iconName.substring(0, iconName.length() - 4);
                }

                iconName = TextureUtils.fixResourcePath(iconName, this.basePath);
                names[i] = iconName;
            }

            return names;
        }
    }

    private static String parseName(String path)
    {
        String str = path;
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

        return str;
    }

    private static String parseBasePath(String path)
    {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }

    private String[] parseTileNames(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            ArrayList list = new ArrayList();
            String[] iconStrs = Config.tokenize(str, " ,");
            label67:

            for (int names = 0; names < iconStrs.length; ++names)
            {
                String i = iconStrs[names];

                if (i.contains("-"))
                {
                    String[] iconName = Config.tokenize(i, "-");

                    if (iconName.length == 2)
                    {
                        int pathBlocks = Config.parseInt(iconName[0], -1);
                        int max = Config.parseInt(iconName[1], -1);

                        if (pathBlocks >= 0 && max >= 0)
                        {
                            if (pathBlocks <= max)
                            {
                                int n = pathBlocks;

                                while (true)
                                {
                                    if (n > max)
                                    {
                                        continue label67;
                                    }

                                    list.add(String.valueOf(n));
                                    ++n;
                                }
                            }

                            Config.warn("Invalid interval: " + i + ", when parsing: " + str);
                            continue;
                        }
                    }
                }

                list.add(i);
            }

            String[] var10 = (String[])((String[])list.toArray(new String[list.size()]));

            for (int var11 = 0; var11 < var10.length; ++var11)
            {
                String var12 = var10[var11];
                var12 = TextureUtils.fixResourcePath(var12, this.basePath);

                if (!var12.startsWith(this.basePath) && !var12.startsWith("textures/") && !var12.startsWith("mcpatcher/"))
                {
                    var12 = this.basePath + "/" + var12;
                }

                if (var12.endsWith(".png"))
                {
                    var12 = var12.substring(0, var12.length() - 4);
                }

                String var13 = "textures/blocks/";

                if (var12.startsWith(var13))
                {
                    var12 = var12.substring(var13.length());
                }

                if (var12.startsWith("/"))
                {
                    var12 = var12.substring(1);
                }

                var10[var11] = var12;
            }

            return var10;
        }
    }

    private static int parseSymmetry(String str)
    {
        if (str == null)
        {
            return 1;
        }
        else if (str.equals("opposite"))
        {
            return 2;
        }
        else if (str.equals("all"))
        {
            return 6;
        }
        else
        {
            Config.warn("Unknown symmetry: " + str);
            return 1;
        }
    }

    private static int parseFaces(String str)
    {
        if (str == null)
        {
            return 63;
        }
        else
        {
            String[] faceStrs = Config.tokenize(str, " ,");
            int facesMask = 0;

            for (int i = 0; i < faceStrs.length; ++i)
            {
                String faceStr = faceStrs[i];
                int faceMask = parseFace(faceStr);
                facesMask |= faceMask;
            }

            return facesMask;
        }
    }

    private static int parseFace(String str)
    {
        str = str.toLowerCase();

        if (!str.equals("bottom") && !str.equals("down"))
        {
            if (!str.equals("top") && !str.equals("up"))
            {
                if (str.equals("north"))
                {
                    return 4;
                }
                else if (str.equals("south"))
                {
                    return 8;
                }
                else if (str.equals("east"))
                {
                    return 32;
                }
                else if (str.equals("west"))
                {
                    return 16;
                }
                else if (str.equals("sides"))
                {
                    return 60;
                }
                else if (str.equals("all"))
                {
                    return 63;
                }
                else
                {
                    Config.warn("Unknown face: " + str);
                    return 128;
                }
            }
            else
            {
                return 2;
            }
        }
        else
        {
            return 1;
        }
    }

    private static int parseConnect(String str)
    {
        if (str == null)
        {
            return 0;
        }
        else if (str.equals("block"))
        {
            return 1;
        }
        else if (str.equals("tile"))
        {
            return 2;
        }
        else if (str.equals("material"))
        {
            return 3;
        }
        else
        {
            Config.warn("Unknown connect: " + str);
            return 128;
        }
    }

    public static IProperty getProperty(String key, Collection properties)
    {
        Iterator it = properties.iterator();
        IProperty prop;

        do
        {
            if (!it.hasNext())
            {
                return null;
            }

            prop = (IProperty)it.next();
        }
        while (!key.equals(prop.getName()));

        return prop;
    }

    private static int parseMethod(String str)
    {
        if (str == null)
        {
            return 1;
        }
        else if (!str.equals("ctm") && !str.equals("glass"))
        {
            if (!str.equals("horizontal") && !str.equals("bookshelf"))
            {
                if (str.equals("vertical"))
                {
                    return 6;
                }
                else if (str.equals("top"))
                {
                    return 3;
                }
                else if (str.equals("random"))
                {
                    return 4;
                }
                else if (str.equals("repeat"))
                {
                    return 5;
                }
                else if (str.equals("fixed"))
                {
                    return 7;
                }
                else if (!str.equals("horizontal+vertical") && !str.equals("h+v"))
                {
                    if (!str.equals("vertical+horizontal") && !str.equals("v+h"))
                    {
                        Config.warn("Unknown method: " + str);
                        return 0;
                    }
                    else
                    {
                        return 9;
                    }
                }
                else
                {
                    return 8;
                }
            }
            else
            {
                return 2;
            }
        }
        else
        {
            return 1;
        }
    }

    public boolean isValid(String path)
    {
        if (this.name != null && this.name.length() > 0)
        {
            if (this.basePath == null)
            {
                Config.warn("No base path found: " + path);
                return false;
            }
            else
            {
                if (this.matchBlocks == null)
                {
                    this.matchBlocks = this.detectMatchBlocks();
                }

                if (this.matchTiles == null && this.matchBlocks == null)
                {
                    this.matchTiles = this.detectMatchTiles();
                }

                if (this.matchBlocks == null && this.matchTiles == null)
                {
                    Config.warn("No matchBlocks or matchTiles specified: " + path);
                    return false;
                }
                else if (this.method == 0)
                {
                    Config.warn("No method: " + path);
                    return false;
                }
                else if (this.tiles != null && this.tiles.length > 0)
                {
                    if (this.connect == 0)
                    {
                        this.connect = this.detectConnect();
                    }

                    if (this.connect == 128)
                    {
                        Config.warn("Invalid connect in: " + path);
                        return false;
                    }
                    else if (this.renderPass > 0)
                    {
                        Config.warn("Render pass not supported: " + this.renderPass);
                        return false;
                    }
                    else if ((this.faces & 128) != 0)
                    {
                        Config.warn("Invalid faces in: " + path);
                        return false;
                    }
                    else if ((this.symmetry & 128) != 0)
                    {
                        Config.warn("Invalid symmetry in: " + path);
                        return false;
                    }
                    else
                    {
                        switch (this.method)
                        {
                            case 1:
                                return this.isValidCtm(path);

                            case 2:
                                return this.isValidHorizontal(path);

                            case 3:
                                return this.isValidTop(path);

                            case 4:
                                return this.isValidRandom(path);

                            case 5:
                                return this.isValidRepeat(path);

                            case 6:
                                return this.isValidVertical(path);

                            case 7:
                                return this.isValidFixed(path);

                            case 8:
                                return this.isValidHorizontalVertical(path);

                            case 9:
                                return this.isValidVerticalHorizontal(path);

                            default:
                                Config.warn("Unknown method: " + path);
                                return false;
                        }
                    }
                }
                else
                {
                    Config.warn("No tiles specified: " + path);
                    return false;
                }
            }
        }
        else
        {
            Config.warn("No name found: " + path);
            return false;
        }
    }

    private int detectConnect()
    {
        return this.matchBlocks != null ? 1 : (this.matchTiles != null ? 2 : 128);
    }

    private MatchBlock[] detectMatchBlocks()
    {
        int[] ids = this.detectMatchBlockIds();

        if (ids == null)
        {
            return null;
        }
        else
        {
            MatchBlock[] mbs = new MatchBlock[ids.length];

            for (int i = 0; i < mbs.length; ++i)
            {
                mbs[i] = new MatchBlock(ids[i]);
            }

            return mbs;
        }
    }

    private int[] detectMatchBlockIds()
    {
        if (!this.name.startsWith("block"))
        {
            return null;
        }
        else
        {
            int startPos = "block".length();
            int pos;

            for (pos = startPos; pos < this.name.length(); ++pos)
            {
                char idStr = this.name.charAt(pos);

                if (idStr < 48 || idStr > 57)
                {
                    break;
                }
            }

            if (pos == startPos)
            {
                return null;
            }
            else
            {
                String var5 = this.name.substring(startPos, pos);
                int id = Config.parseInt(var5, -1);
                return id < 0 ? null : new int[] {id};
            }
        }
    }

    private String[] detectMatchTiles()
    {
        TextureAtlasSprite icon = getIcon(this.name);
        return icon == null ? null : new String[] {this.name};
    }

    private static TextureAtlasSprite getIcon(String iconName)
    {
        TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite icon = textureMapBlocks.getSpriteSafe(iconName);

        if (icon != null)
        {
            return icon;
        }
        else
        {
            icon = textureMapBlocks.getSpriteSafe("blocks/" + iconName);
            return icon;
        }
    }

    private boolean isValidCtm(String path)
    {
        if (this.tiles == null)
        {
            this.tiles = this.parseTileNames("0-11 16-27 32-43 48-58");
        }

        if (this.tiles.length < 47)
        {
            Config.warn("Invalid tiles, must be at least 47: " + path);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidHorizontal(String path)
    {
        if (this.tiles == null)
        {
            this.tiles = this.parseTileNames("12-15");
        }

        if (this.tiles.length != 4)
        {
            Config.warn("Invalid tiles, must be exactly 4: " + path);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidVertical(String path)
    {
        if (this.tiles == null)
        {
            Config.warn("No tiles defined for vertical: " + path);
            return false;
        }
        else if (this.tiles.length != 4)
        {
            Config.warn("Invalid tiles, must be exactly 4: " + path);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidHorizontalVertical(String path)
    {
        if (this.tiles == null)
        {
            Config.warn("No tiles defined for horizontal+vertical: " + path);
            return false;
        }
        else if (this.tiles.length != 7)
        {
            Config.warn("Invalid tiles, must be exactly 7: " + path);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidVerticalHorizontal(String path)
    {
        if (this.tiles == null)
        {
            Config.warn("No tiles defined for vertical+horizontal: " + path);
            return false;
        }
        else if (this.tiles.length != 7)
        {
            Config.warn("Invalid tiles, must be exactly 7: " + path);
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidRandom(String path)
    {
        if (this.tiles != null && this.tiles.length > 0)
        {
            if (this.weights != null)
            {
                int[] sum;

                if (this.weights.length > this.tiles.length)
                {
                    Config.warn("More weights defined than tiles, trimming weights: " + path);
                    sum = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, sum, 0, sum.length);
                    this.weights = sum;
                }

                int i;

                if (this.weights.length < this.tiles.length)
                {
                    Config.warn("Less weights defined than tiles, expanding weights: " + path);
                    sum = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, sum, 0, this.weights.length);
                    i = MathUtils.getAverage(this.weights);

                    for (int i1 = this.weights.length; i1 < sum.length; ++i1)
                    {
                        sum[i1] = i;
                    }

                    this.weights = sum;
                }

                this.sumWeights = new int[this.weights.length];
                int var5 = 0;

                for (i = 0; i < this.weights.length; ++i)
                {
                    var5 += this.weights[i];
                    this.sumWeights[i] = var5;
                }

                this.sumAllWeights = var5;

                if (this.sumAllWeights <= 0)
                {
                    Config.warn("Invalid sum of all weights: " + var5);
                    this.sumAllWeights = 1;
                }
            }

            return true;
        }
        else
        {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
    }

    private boolean isValidRepeat(String path)
    {
        if (this.tiles == null)
        {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
        else if (this.width > 0 && this.width <= 16)
        {
            if (this.height > 0 && this.height <= 16)
            {
                if (this.tiles.length != this.width * this.height)
                {
                    Config.warn("Number of tiles does not equal width x height: " + path);
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                Config.warn("Invalid height: " + path);
                return false;
            }
        }
        else
        {
            Config.warn("Invalid width: " + path);
            return false;
        }
    }

    private boolean isValidFixed(String path)
    {
        if (this.tiles == null)
        {
            Config.warn("Tiles not defined: " + path);
            return false;
        }
        else if (this.tiles.length != 1)
        {
            Config.warn("Number of tiles should be 1 for method: fixed.");
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isValidTop(String path)
    {
        if (this.tiles == null)
        {
            this.tiles = this.parseTileNames("66");
        }

        if (this.tiles.length != 1)
        {
            Config.warn("Invalid tiles, must be exactly 1: " + path);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void updateIcons(TextureMap textureMap)
    {
        if (this.matchTiles != null)
        {
            this.matchTileIcons = registerIcons(this.matchTiles, textureMap);
        }

        if (this.tiles != null)
        {
            this.tileIcons = registerIcons(this.tiles, textureMap);
        }
    }

    private static TextureAtlasSprite[] registerIcons(String[] tileNames, TextureMap textureMap)
    {
        if (tileNames == null)
        {
            return null;
        }
        else
        {
            ArrayList iconList = new ArrayList();

            for (int icons = 0; icons < tileNames.length; ++icons)
            {
                String iconName = tileNames[icons];
                ResourceLocation resLoc = new ResourceLocation(iconName);
                String domain = resLoc.getResourceDomain();
                String path = resLoc.getResourcePath();

                if (!path.contains("/"))
                {
                    path = "textures/blocks/" + path;
                }

                String filePath = path + ".png";
                ResourceLocation locFile = new ResourceLocation(domain, filePath);
                boolean exists = Config.hasResource(locFile);

                if (!exists)
                {
                    Config.warn("File not found: " + filePath);
                }

                String prefixTextures = "textures/";
                String pathSprite = path;

                if (path.startsWith(prefixTextures))
                {
                    pathSprite = path.substring(prefixTextures.length());
                }

                ResourceLocation locSprite = new ResourceLocation(domain, pathSprite);
                TextureAtlasSprite icon = textureMap.func_174942_a(locSprite);
                iconList.add(icon);
            }

            TextureAtlasSprite[] var15 = (TextureAtlasSprite[])((TextureAtlasSprite[])iconList.toArray(new TextureAtlasSprite[iconList.size()]));
            return var15;
        }
    }

    public boolean matchesBlockId(int blockId)
    {
        return Matches.blockId(blockId, this.matchBlocks);
    }

    public boolean matchesBlock(int blockId, int metadata)
    {
        return !Matches.block(blockId, metadata, this.matchBlocks) ? false : Matches.metadata(metadata, this.metadatas);
    }

    public boolean matchesIcon(TextureAtlasSprite icon)
    {
        return Matches.sprite(icon, this.matchTileIcons);
    }

    public String toString()
    {
        return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString((Object[])this.matchBlocks) + ", matchTiles: " + Config.arrayToString((Object[])this.matchTiles);
    }

    public boolean matchesBiome(BiomeGenBase biome)
    {
        return Matches.biome(biome, this.biomes);
    }

    public int getMetadataMax()
    {
        byte max = -1;
        int var4 = this.getMax(this.metadatas, max);

        if (this.matchBlocks != null)
        {
            for (int i = 0; i < this.matchBlocks.length; ++i)
            {
                MatchBlock mb = this.matchBlocks[i];
                var4 = this.getMax(mb.getMetadatas(), var4);
            }
        }

        return var4;
    }

    private int getMax(int[] mds, int max)
    {
        if (mds == null)
        {
            return max;
        }
        else
        {
            for (int i = 0; i < mds.length; ++i)
            {
                int md = mds[i];

                if (md > max)
                {
                    max = md;
                }
            }

            return max;
        }
    }
}
