package optifine;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

public class ConnectedProperties {
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

    public ConnectedProperties(Properties paramProperties, String paramString) {
        ConnectedParser localConnectedParser = new ConnectedParser("ConnectedTextures");
        this.name = localConnectedParser.parseName(paramString);
        this.basePath = localConnectedParser.parseBasePath(paramString);
        this.matchBlocks = localConnectedParser.parseMatchBlocks(paramProperties.getProperty("matchBlocks"));
        this.metadatas = localConnectedParser.parseIntList(paramProperties.getProperty("metadata"));
        this.matchTiles = parseMatchTiles(paramProperties.getProperty("matchTiles"));
        this.method = parseMethod(paramProperties.getProperty("method"));
        this.tiles = parseTileNames(paramProperties.getProperty("tiles"));
        this.connect = parseConnect(paramProperties.getProperty("connect"));
        this.faces = parseFaces(paramProperties.getProperty("faces"));
        this.biomes = localConnectedParser.parseBiomes(paramProperties.getProperty("biomes"));
        this.minHeight = localConnectedParser.parseInt(paramProperties.getProperty("minHeight"), -1);
        this.maxHeight = localConnectedParser.parseInt(paramProperties.getProperty("maxHeight"), 1024);
        this.renderPass = localConnectedParser.parseInt(paramProperties.getProperty("renderPass"));
        this.innerSeams = ConnectedParser.parseBoolean(paramProperties.getProperty("innerSeams"));
        this.width = localConnectedParser.parseInt(paramProperties.getProperty("width"));
        this.height = localConnectedParser.parseInt(paramProperties.getProperty("height"));
        this.weights = localConnectedParser.parseIntList(paramProperties.getProperty("weights"));
        this.symmetry = parseSymmetry(paramProperties.getProperty("symmetry"));
    }

    private static String parseName(String paramString) {
        String str = paramString;
        int i = paramString.lastIndexOf('/');
        if (i >= 0) {
            str = paramString.substring(i | 0x1);
        }
        int j = str.lastIndexOf('.');
        if (j >= 0) {
            str = str.substring(0, j);
        }
        return str;
    }

    private static String parseBasePath(String paramString) {
        int i = paramString.lastIndexOf('/');
        return i < 0 ? "" : paramString.substring(0, i);
    }

    private static int parseSymmetry(String paramString) {
        if (paramString == null) {
            return 1;
        }
        if (paramString.equals("opposite")) {
            return 2;
        }
        if (paramString.equals("all")) {
            return 6;
        }
        Config.warn("Unknown symmetry: " + paramString);
        return 1;
    }

    private static int parseFaces(String paramString) {
        if (paramString == null) {
            return 63;
        }
        String[] arrayOfString = Config.tokenize(paramString, " ,");
        int i = 0;
        for (int j = 0; j < arrayOfString.length; j++) {
            String str = arrayOfString[j];
            int k = parseFace(str);
            i ^= k;
        }
        return i;
    }

    private static int parseFace(String paramString) {
        paramString = paramString.toLowerCase();
        if ((!paramString.equals("bottom")) && (!paramString.equals("down"))) {
            if ((!paramString.equals("top")) && (!paramString.equals("up"))) {
                if (paramString.equals("north")) {
                    return 4;
                }
                if (paramString.equals("south")) {
                    return 8;
                }
                if (paramString.equals("east")) {
                    return 32;
                }
                if (paramString.equals("west")) {
                    return 16;
                }
                if (paramString.equals("sides")) {
                    return 60;
                }
                if (paramString.equals("all")) {
                    return 63;
                }
                Config.warn("Unknown face: " + paramString);
                return 128;
            }
            return 2;
        }
        return 1;
    }

    private static int parseConnect(String paramString) {
        if (paramString == null) {
            return 0;
        }
        if (paramString.equals("block")) {
            return 1;
        }
        if (paramString.equals("tile")) {
            return 2;
        }
        if (paramString.equals("material")) {
            return 3;
        }
        Config.warn("Unknown connect: " + paramString);
        return 128;
    }

    public static IProperty getProperty(String paramString, Collection paramCollection) {
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            if (paramString.equals(((IProperty) localObject).getName())) {
                return (IProperty) localObject;
            }
        }
        return null;
    }

    private static int parseMethod(String paramString) {
        if (paramString == null) {
            return 1;
        }
        if ((!paramString.equals("ctm")) && (!paramString.equals("glass"))) {
            if ((!paramString.equals("horizontal")) && (!paramString.equals("bookshelf"))) {
                if (paramString.equals("vertical")) {
                    return 6;
                }
                if (paramString.equals("top")) {
                    return 3;
                }
                if (paramString.equals("random")) {
                    return 4;
                }
                if (paramString.equals("repeat")) {
                    return 5;
                }
                if (paramString.equals("fixed")) {
                    return 7;
                }
                if ((!paramString.equals("horizontal+vertical")) && (!paramString.equals("h+v"))) {
                    if ((!paramString.equals("vertical+horizontal")) && (!paramString.equals("v+h"))) {
                        Config.warn("Unknown method: " + paramString);
                        return 0;
                    }
                    return 9;
                }
                return 8;
            }
            return 2;
        }
        return 1;
    }

    private static TextureAtlasSprite getIcon(String paramString) {
        TextureMap localTextureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite localTextureAtlasSprite = localTextureMap.getSpriteSafe(paramString);
        if (localTextureAtlasSprite != null) {
            return localTextureAtlasSprite;
        }
        localTextureAtlasSprite = localTextureMap.getSpriteSafe("blocks/" + paramString);
        return localTextureAtlasSprite;
    }

    private static TextureAtlasSprite[] registerIcons(String[] paramArrayOfString, TextureMap paramTextureMap) {
        if (paramArrayOfString == null) {
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str1 = paramArrayOfString[i];
            ResourceLocation localResourceLocation1 = new ResourceLocation(str1);
            String str2 = localResourceLocation1.getResourceDomain();
            String str3 = localResourceLocation1.getResourcePath();
            if (!str3.contains("/")) {
                str3 = "textures/blocks/" + str3;
            }
            String str4 = str3 + ".png";
            ResourceLocation localResourceLocation2 = new ResourceLocation(str2, str4);
            boolean bool = Config.hasResource(localResourceLocation2);
            if (!bool) {
                Config.warn("File not found: " + str4);
            }
            String str5 = "textures/";
            String str6 = str3;
            if (str3.startsWith(str5)) {
                str6 = str3.substring(str5.length());
            }
            ResourceLocation localResourceLocation3 = new ResourceLocation(str2, str6);
            TextureAtlasSprite localTextureAtlasSprite = paramTextureMap.registerSprite(localResourceLocation3);
            localArrayList.add(localTextureAtlasSprite);
        }
        TextureAtlasSprite[] arrayOfTextureAtlasSprite = (TextureAtlasSprite[]) (TextureAtlasSprite[]) localArrayList.toArray(new TextureAtlasSprite[localArrayList.size()]);
        return arrayOfTextureAtlasSprite;
    }

    private String[] parseMatchTiles(String paramString) {
        if (paramString == null) {
            return null;
        }
        String[] arrayOfString = Config.tokenize(paramString, " ");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            if (str.endsWith(".png")) {
                str = str.substring(0, str.length() - 4);
            }
            str = TextureUtils.fixResourcePath(str, this.basePath);
            arrayOfString[i] = str;
        }
        return arrayOfString;
    }

    private String[] parseTileNames(String paramString) {
        if (paramString == null) {
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString1 = Config.tokenize(paramString, " ,");
        Object localObject;
        for (int i = 0; i < arrayOfString1.length; i++) {
            String str1 = arrayOfString1[i];
            if (str1.contains("-")) {
                localObject = Config.tokenize(str1, "-");
                if (localObject.length == 2) {
                    int k = Config.parseInt(localObject[0], -1);
                    int m = Config.parseInt(localObject[1], -1);
                    if ((k >= 0) && (m >= 0)) {
                        if (k > m) {
                            Config.warn("Invalid interval: " + str1 + ", when parsing: " + paramString);
                            continue;
                        }
                        for (int n = k; n <= m; n++) {
                            localArrayList.add(String.valueOf(n));
                        }
                    }
                }
            }
            localArrayList.add(str1);
        }
        String[] arrayOfString2 = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        for (int j = 0; j < arrayOfString2.length; j++) {
            localObject = arrayOfString2[j];
            localObject = TextureUtils.fixResourcePath((String) localObject, this.basePath);
            if ((!((String) localObject).startsWith(this.basePath)) && (!((String) localObject).startsWith("textures/")) && (!((String) localObject).startsWith("mcpatcher/"))) {
                localObject = this.basePath + "/" + (String) localObject;
            }
            if (((String) localObject).endsWith(".png")) {
                localObject = ((String) localObject).substring(0, ((String) localObject).length() - 4);
            }
            String str2 = "textures/blocks/";
            if (((String) localObject).startsWith(str2)) {
                localObject = ((String) localObject).substring(str2.length());
            }
            if (((String) localObject).startsWith("/")) {
                localObject = ((String) localObject).substring(1);
            }
            arrayOfString2[j] = localObject;
        }
        return arrayOfString2;
    }

    public boolean isValid(String paramString) {
        if ((this.name != null) && (this.name.length() > 0)) {
            if (this.basePath == null) {
                Config.warn("No base path found: " + paramString);
                return false;
            }
            if (this.matchBlocks == null) {
                this.matchBlocks = detectMatchBlocks();
            }
            if ((this.matchTiles == null) && (this.matchBlocks == null)) {
                this.matchTiles = detectMatchTiles();
            }
            if ((this.matchBlocks == null) && (this.matchTiles == null)) {
                Config.warn("No matchBlocks or matchTiles specified: " + paramString);
                return false;
            }
            if (this.method == 0) {
                Config.warn("No method: " + paramString);
                return false;
            }
            if ((this.tiles != null) && (this.tiles.length > 0)) {
                if (this.connect == 0) {
                    this.connect = detectConnect();
                }
                if (this.connect == 128) {
                    Config.warn("Invalid connect in: " + paramString);
                    return false;
                }
                if (this.renderPass > 0) {
                    Config.warn("Render pass not supported: " + this.renderPass);
                    return false;
                }
                if (this.faces >> 128 != 0) {
                    Config.warn("Invalid faces in: " + paramString);
                    return false;
                }
                if (this.symmetry >> 128 != 0) {
                    Config.warn("Invalid symmetry in: " + paramString);
                    return false;
                }
                switch (this.method) {
                    case 1:
                        return isValidCtm(paramString);
                    case 2:
                        return isValidHorizontal(paramString);
                    case 3:
                        return isValidTop(paramString);
                    case 4:
                        return isValidRandom(paramString);
                    case 5:
                        return isValidRepeat(paramString);
                    case 6:
                        return isValidVertical(paramString);
                    case 7:
                        return isValidFixed(paramString);
                    case 8:
                        return isValidHorizontalVertical(paramString);
                    case 9:
                        return isValidVerticalHorizontal(paramString);
                }
                Config.warn("Unknown method: " + paramString);
                return false;
            }
            Config.warn("No tiles specified: " + paramString);
            return false;
        }
        Config.warn("No name found: " + paramString);
        return false;
    }

    private int detectConnect() {
        return this.matchTiles != null ? 2 : this.matchBlocks != null ? 1 : 128;
    }

    private MatchBlock[] detectMatchBlocks() {
        int[] arrayOfInt = detectMatchBlockIds();
        if (arrayOfInt == null) {
            return null;
        }
        MatchBlock[] arrayOfMatchBlock = new MatchBlock[arrayOfInt.length];
        for (int i = 0; i < arrayOfMatchBlock.length; i++) {
            arrayOfMatchBlock[i] = new MatchBlock(arrayOfInt[i]);
        }
        return arrayOfMatchBlock;
    }

    private int[] detectMatchBlockIds() {
        if (!this.name.startsWith("block")) {
            return null;
        }
        int i = "block".length();
        for (int j = i; j < this.name.length(); j++) {
            int k = this.name.charAt(j);
            if ((k < 48) || (k > 57)) {
                break;
            }
        }
        if (j == i) {
            return null;
        }
        String str = this.name.substring(i, j);
        int m = Config.parseInt(str, -1);
        return new int[]{m < 0 ? null : m};
    }

    private String[] detectMatchTiles() {
        TextureAtlasSprite localTextureAtlasSprite = getIcon(this.name);
        return new String[]{localTextureAtlasSprite == null ? null : this.name};
    }

    private boolean isValidCtm(String paramString) {
        if (this.tiles == null) {
            this.tiles = parseTileNames("0-11 16-27 32-43 48-58");
        }
        if (this.tiles.length < 47) {
            Config.warn("Invalid tiles, must be at least 47: " + paramString);
            return false;
        }
        return true;
    }

    private boolean isValidHorizontal(String paramString) {
        if (this.tiles == null) {
            this.tiles = parseTileNames("12-15");
        }
        if (this.tiles.length != 4) {
            Config.warn("Invalid tiles, must be exactly 4: " + paramString);
            return false;
        }
        return true;
    }

    private boolean isValidVertical(String paramString) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for vertical: " + paramString);
            return false;
        }
        if (this.tiles.length != 4) {
            Config.warn("Invalid tiles, must be exactly 4: " + paramString);
            return false;
        }
        return true;
    }

    private boolean isValidHorizontalVertical(String paramString) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for horizontal+vertical: " + paramString);
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn("Invalid tiles, must be exactly 7: " + paramString);
            return false;
        }
        return true;
    }

    private boolean isValidVerticalHorizontal(String paramString) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for vertical+horizontal: " + paramString);
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn("Invalid tiles, must be exactly 7: " + paramString);
            return false;
        }
        return true;
    }

    private boolean isValidRandom(String paramString) {
        if ((this.tiles != null) && (this.tiles.length > 0)) {
            if (this.weights != null) {
                int[] arrayOfInt;
                if (this.weights.length > this.tiles.length) {
                    Config.warn("More weights defined than tiles, trimming weights: " + paramString);
                    arrayOfInt = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, arrayOfInt, 0, arrayOfInt.length);
                    this.weights = arrayOfInt;
                }
                if (this.weights.length < this.tiles.length) {
                    Config.warn("Less weights defined than tiles, expanding weights: " + paramString);
                    arrayOfInt = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, arrayOfInt, 0, this.weights.length);
                    j = MathUtils.getAverage(this.weights);
                    for (int k = this.weights.length; k < arrayOfInt.length; k++) {
                        arrayOfInt[k] = j;
                    }
                    this.weights = arrayOfInt;
                }
                this.sumWeights = new int[this.weights.length];
                int i = 0;
                for (int j = 0; j < this.weights.length; j++) {
                    i |= this.weights[j];
                    this.sumWeights[j] = i;
                }
                this.sumAllWeights = i;
                if (this.sumAllWeights <= 0) {
                    Config.warn("Invalid sum of all weights: " + i);
                    this.sumAllWeights = 1;
                }
            }
            return true;
        }
        Config.warn("Tiles not defined: " + paramString);
        return false;
    }

    private boolean isValidRepeat(String paramString) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + paramString);
            return false;
        }
        if ((this.width > 0) && (this.width <= 16)) {
            if ((this.height > 0) && (this.height <= 16)) {
                if (this.tiles.length != this.width * this.height) {
                    Config.warn("Number of tiles does not equal width x height: " + paramString);
                    return false;
                }
                return true;
            }
            Config.warn("Invalid height: " + paramString);
            return false;
        }
        Config.warn("Invalid width: " + paramString);
        return false;
    }

    private boolean isValidFixed(String paramString) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + paramString);
            return false;
        }
        if (this.tiles.length != 1) {
            Config.warn("Number of tiles should be 1 for method: fixed.");
            return false;
        }
        return true;
    }

    private boolean isValidTop(String paramString) {
        if (this.tiles == null) {
            this.tiles = parseTileNames("66");
        }
        if (this.tiles.length != 1) {
            Config.warn("Invalid tiles, must be exactly 1: " + paramString);
            return false;
        }
        return true;
    }

    public void updateIcons(TextureMap paramTextureMap) {
        if (this.matchTiles != null) {
            this.matchTileIcons = registerIcons(this.matchTiles, paramTextureMap);
        }
        if (this.tiles != null) {
            this.tileIcons = registerIcons(this.tiles, paramTextureMap);
        }
    }

    public boolean matchesBlockId(int paramInt) {
        return Matches.blockId(paramInt, this.matchBlocks);
    }

    public boolean matchesBlock(int paramInt1, int paramInt2) {
        return !Matches.block(paramInt1, paramInt2, this.matchBlocks) ? false : Matches.metadata(paramInt2, this.metadatas);
    }

    public boolean matchesIcon(TextureAtlasSprite paramTextureAtlasSprite) {
        return Matches.sprite(paramTextureAtlasSprite, this.matchTileIcons);
    }

    public String toString() {
        return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString((Object[]) this.matchBlocks) + ", matchTiles: " + Config.arrayToString((Object[]) this.matchTiles);
    }

    public boolean matchesBiome(BiomeGenBase paramBiomeGenBase) {
        return Matches.biome(paramBiomeGenBase, this.biomes);
    }

    public int getMetadataMax() {
        int i = -1;
        i = getMax(this.metadatas, i);
        if (this.matchBlocks != null) {
            for (int j = 0; j < this.matchBlocks.length; j++) {
                MatchBlock localMatchBlock = this.matchBlocks[j];
                i = getMax(localMatchBlock.getMetadatas(), i);
            }
        }
        return i;
    }

    private int getMax(int[] paramArrayOfInt, int paramInt) {
        if (paramArrayOfInt == null) {
            return paramInt;
        }
        for (int i = 0; i < paramArrayOfInt.length; i++) {
            int j = paramArrayOfInt[i];
            if (j > paramInt) {
                paramInt = j;
            }
        }
        return paramInt;
    }
}




