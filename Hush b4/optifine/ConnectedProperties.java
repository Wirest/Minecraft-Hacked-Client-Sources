// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedProperties
{
    public String name;
    public String basePath;
    public MatchBlock[] matchBlocks;
    public int[] metadatas;
    public String[] matchTiles;
    public int method;
    public String[] tiles;
    public int connect;
    public int faces;
    public BiomeGenBase[] biomes;
    public int minHeight;
    public int maxHeight;
    public int renderPass;
    public boolean innerSeams;
    public int width;
    public int height;
    public int[] weights;
    public int symmetry;
    public int[] sumWeights;
    public int sumAllWeights;
    public TextureAtlasSprite[] matchTileIcons;
    public TextureAtlasSprite[] tileIcons;
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
    
    public ConnectedProperties(final Properties p_i32_1_, final String p_i32_2_) {
        this.name = null;
        this.basePath = null;
        this.matchBlocks = null;
        this.metadatas = null;
        this.matchTiles = null;
        this.method = 0;
        this.tiles = null;
        this.connect = 0;
        this.faces = 63;
        this.biomes = null;
        this.minHeight = 0;
        this.maxHeight = 1024;
        this.renderPass = 0;
        this.innerSeams = false;
        this.width = 0;
        this.height = 0;
        this.weights = null;
        this.symmetry = 1;
        this.sumWeights = null;
        this.sumAllWeights = 1;
        this.matchTileIcons = null;
        this.tileIcons = null;
        final ConnectedParser connectedparser = new ConnectedParser("ConnectedTextures");
        this.name = connectedparser.parseName(p_i32_2_);
        this.basePath = connectedparser.parseBasePath(p_i32_2_);
        this.matchBlocks = connectedparser.parseMatchBlocks(p_i32_1_.getProperty("matchBlocks"));
        this.metadatas = connectedparser.parseIntList(p_i32_1_.getProperty("metadata"));
        this.matchTiles = this.parseMatchTiles(p_i32_1_.getProperty("matchTiles"));
        this.method = parseMethod(p_i32_1_.getProperty("method"));
        this.tiles = this.parseTileNames(p_i32_1_.getProperty("tiles"));
        this.connect = parseConnect(p_i32_1_.getProperty("connect"));
        this.faces = parseFaces(p_i32_1_.getProperty("faces"));
        this.biomes = connectedparser.parseBiomes(p_i32_1_.getProperty("biomes"));
        this.minHeight = connectedparser.parseInt(p_i32_1_.getProperty("minHeight"), -1);
        this.maxHeight = connectedparser.parseInt(p_i32_1_.getProperty("maxHeight"), 1024);
        this.renderPass = connectedparser.parseInt(p_i32_1_.getProperty("renderPass"));
        this.innerSeams = ConnectedParser.parseBoolean(p_i32_1_.getProperty("innerSeams"));
        this.width = connectedparser.parseInt(p_i32_1_.getProperty("width"));
        this.height = connectedparser.parseInt(p_i32_1_.getProperty("height"));
        this.weights = connectedparser.parseIntList(p_i32_1_.getProperty("weights"));
        this.symmetry = parseSymmetry(p_i32_1_.getProperty("symmetry"));
    }
    
    private String[] parseMatchTiles(final String p_parseMatchTiles_1_) {
        if (p_parseMatchTiles_1_ == null) {
            return null;
        }
        final String[] astring = Config.tokenize(p_parseMatchTiles_1_, " ");
        for (int i = 0; i < astring.length; ++i) {
            String s = astring[i];
            if (s.endsWith(".png")) {
                s = s.substring(0, s.length() - 4);
            }
            s = TextureUtils.fixResourcePath(s, this.basePath);
            astring[i] = s;
        }
        return astring;
    }
    
    private static String parseName(final String p_parseName_0_) {
        String s = p_parseName_0_;
        final int i = p_parseName_0_.lastIndexOf(47);
        if (i >= 0) {
            s = p_parseName_0_.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    private static String parseBasePath(final String p_parseBasePath_0_) {
        final int i = p_parseBasePath_0_.lastIndexOf(47);
        return (i < 0) ? "" : p_parseBasePath_0_.substring(0, i);
    }
    
    private String[] parseTileNames(final String p_parseTileNames_1_) {
        if (p_parseTileNames_1_ == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(p_parseTileNames_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.contains("-")) {
                final String[] astring2 = Config.tokenize(s, "-");
                if (astring2.length == 2) {
                    final int j = Config.parseInt(astring2[0], -1);
                    final int k = Config.parseInt(astring2[1], -1);
                    if (j >= 0 && k >= 0) {
                        if (j > k) {
                            Config.warn("Invalid interval: " + s + ", when parsing: " + p_parseTileNames_1_);
                            continue;
                        }
                        for (int l = j; l <= k; ++l) {
                            list.add(String.valueOf(l));
                        }
                        continue;
                    }
                }
            }
            list.add(s);
        }
        final String[] astring3 = list.toArray(new String[list.size()]);
        for (int i2 = 0; i2 < astring3.length; ++i2) {
            String s2 = astring3[i2];
            s2 = TextureUtils.fixResourcePath(s2, this.basePath);
            if (!s2.startsWith(this.basePath) && !s2.startsWith("textures/") && !s2.startsWith("mcpatcher/")) {
                s2 = String.valueOf(this.basePath) + "/" + s2;
            }
            if (s2.endsWith(".png")) {
                s2 = s2.substring(0, s2.length() - 4);
            }
            final String s3 = "textures/blocks/";
            if (s2.startsWith(s3)) {
                s2 = s2.substring(s3.length());
            }
            if (s2.startsWith("/")) {
                s2 = s2.substring(1);
            }
            astring3[i2] = s2;
        }
        return astring3;
    }
    
    private static int parseSymmetry(final String p_parseSymmetry_0_) {
        if (p_parseSymmetry_0_ == null) {
            return 1;
        }
        if (p_parseSymmetry_0_.equals("opposite")) {
            return 2;
        }
        if (p_parseSymmetry_0_.equals("all")) {
            return 6;
        }
        Config.warn("Unknown symmetry: " + p_parseSymmetry_0_);
        return 1;
    }
    
    private static int parseFaces(final String p_parseFaces_0_) {
        if (p_parseFaces_0_ == null) {
            return 63;
        }
        final String[] astring = Config.tokenize(p_parseFaces_0_, " ,");
        int i = 0;
        for (int j = 0; j < astring.length; ++j) {
            final String s = astring[j];
            final int k = parseFace(s);
            i |= k;
        }
        return i;
    }
    
    private static int parseFace(String p_parseFace_0_) {
        p_parseFace_0_ = p_parseFace_0_.toLowerCase();
        if (p_parseFace_0_.equals("bottom") || p_parseFace_0_.equals("down")) {
            return 1;
        }
        if (p_parseFace_0_.equals("top") || p_parseFace_0_.equals("up")) {
            return 2;
        }
        if (p_parseFace_0_.equals("north")) {
            return 4;
        }
        if (p_parseFace_0_.equals("south")) {
            return 8;
        }
        if (p_parseFace_0_.equals("east")) {
            return 32;
        }
        if (p_parseFace_0_.equals("west")) {
            return 16;
        }
        if (p_parseFace_0_.equals("sides")) {
            return 60;
        }
        if (p_parseFace_0_.equals("all")) {
            return 63;
        }
        Config.warn("Unknown face: " + p_parseFace_0_);
        return 128;
    }
    
    private static int parseConnect(final String p_parseConnect_0_) {
        if (p_parseConnect_0_ == null) {
            return 0;
        }
        if (p_parseConnect_0_.equals("block")) {
            return 1;
        }
        if (p_parseConnect_0_.equals("tile")) {
            return 2;
        }
        if (p_parseConnect_0_.equals("material")) {
            return 3;
        }
        Config.warn("Unknown connect: " + p_parseConnect_0_);
        return 128;
    }
    
    public static IProperty getProperty(final String p_getProperty_0_, final Collection p_getProperty_1_) {
        for (final Object iproperty : p_getProperty_1_) {
            if (p_getProperty_0_.equals(((IProperty)iproperty).getName())) {
                return (IProperty)iproperty;
            }
        }
        return null;
    }
    
    private static int parseMethod(final String p_parseMethod_0_) {
        if (p_parseMethod_0_ == null) {
            return 1;
        }
        if (p_parseMethod_0_.equals("ctm") || p_parseMethod_0_.equals("glass")) {
            return 1;
        }
        if (p_parseMethod_0_.equals("horizontal") || p_parseMethod_0_.equals("bookshelf")) {
            return 2;
        }
        if (p_parseMethod_0_.equals("vertical")) {
            return 6;
        }
        if (p_parseMethod_0_.equals("top")) {
            return 3;
        }
        if (p_parseMethod_0_.equals("random")) {
            return 4;
        }
        if (p_parseMethod_0_.equals("repeat")) {
            return 5;
        }
        if (p_parseMethod_0_.equals("fixed")) {
            return 7;
        }
        if (p_parseMethod_0_.equals("horizontal+vertical") || p_parseMethod_0_.equals("h+v")) {
            return 8;
        }
        if (!p_parseMethod_0_.equals("vertical+horizontal") && !p_parseMethod_0_.equals("v+h")) {
            Config.warn("Unknown method: " + p_parseMethod_0_);
            return 0;
        }
        return 9;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.name == null || this.name.length() <= 0) {
            Config.warn("No name found: " + p_isValid_1_);
            return false;
        }
        if (this.basePath == null) {
            Config.warn("No base path found: " + p_isValid_1_);
            return false;
        }
        if (this.matchBlocks == null) {
            this.matchBlocks = this.detectMatchBlocks();
        }
        if (this.matchTiles == null && this.matchBlocks == null) {
            this.matchTiles = this.detectMatchTiles();
        }
        if (this.matchBlocks == null && this.matchTiles == null) {
            Config.warn("No matchBlocks or matchTiles specified: " + p_isValid_1_);
            return false;
        }
        if (this.method == 0) {
            Config.warn("No method: " + p_isValid_1_);
            return false;
        }
        if (this.tiles == null || this.tiles.length <= 0) {
            Config.warn("No tiles specified: " + p_isValid_1_);
            return false;
        }
        if (this.connect == 0) {
            this.connect = this.detectConnect();
        }
        if (this.connect == 128) {
            Config.warn("Invalid connect in: " + p_isValid_1_);
            return false;
        }
        if (this.renderPass > 0) {
            Config.warn("Render pass not supported: " + this.renderPass);
            return false;
        }
        if ((this.faces & 0x80) != 0x0) {
            Config.warn("Invalid faces in: " + p_isValid_1_);
            return false;
        }
        if ((this.symmetry & 0x80) != 0x0) {
            Config.warn("Invalid symmetry in: " + p_isValid_1_);
            return false;
        }
        switch (this.method) {
            case 1: {
                return this.isValidCtm(p_isValid_1_);
            }
            case 2: {
                return this.isValidHorizontal(p_isValid_1_);
            }
            case 3: {
                return this.isValidTop(p_isValid_1_);
            }
            case 4: {
                return this.isValidRandom(p_isValid_1_);
            }
            case 5: {
                return this.isValidRepeat(p_isValid_1_);
            }
            case 6: {
                return this.isValidVertical(p_isValid_1_);
            }
            case 7: {
                return this.isValidFixed(p_isValid_1_);
            }
            case 8: {
                return this.isValidHorizontalVertical(p_isValid_1_);
            }
            case 9: {
                return this.isValidVerticalHorizontal(p_isValid_1_);
            }
            default: {
                Config.warn("Unknown method: " + p_isValid_1_);
                return false;
            }
        }
    }
    
    private int detectConnect() {
        return (this.matchBlocks != null) ? 1 : ((this.matchTiles != null) ? 2 : 128);
    }
    
    private MatchBlock[] detectMatchBlocks() {
        final int[] aint = this.detectMatchBlockIds();
        if (aint == null) {
            return null;
        }
        final MatchBlock[] amatchblock = new MatchBlock[aint.length];
        for (int i = 0; i < amatchblock.length; ++i) {
            amatchblock[i] = new MatchBlock(aint[i]);
        }
        return amatchblock;
    }
    
    private int[] detectMatchBlockIds() {
        if (!this.name.startsWith("block")) {
            return null;
        }
        int j;
        int i;
        for (i = (j = "block".length()); j < this.name.length(); ++j) {
            final char c0 = this.name.charAt(j);
            if (c0 < '0') {
                break;
            }
            if (c0 > '9') {
                break;
            }
        }
        if (j == i) {
            return null;
        }
        final String s = this.name.substring(i, j);
        final int k = Config.parseInt(s, -1);
        return (int[])((k < 0) ? null : new int[] { k });
    }
    
    private String[] detectMatchTiles() {
        final TextureAtlasSprite textureatlassprite = getIcon(this.name);
        return (String[])((textureatlassprite == null) ? null : new String[] { this.name });
    }
    
    private static TextureAtlasSprite getIcon(final String p_getIcon_0_) {
        final TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(p_getIcon_0_);
        if (textureatlassprite != null) {
            return textureatlassprite;
        }
        textureatlassprite = texturemap.getSpriteSafe("blocks/" + p_getIcon_0_);
        return textureatlassprite;
    }
    
    private boolean isValidCtm(final String p_isValidCtm_1_) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("0-11 16-27 32-43 48-58");
        }
        if (this.tiles.length < 47) {
            Config.warn("Invalid tiles, must be at least 47: " + p_isValidCtm_1_);
            return false;
        }
        return true;
    }
    
    private boolean isValidHorizontal(final String p_isValidHorizontal_1_) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("12-15");
        }
        if (this.tiles.length != 4) {
            Config.warn("Invalid tiles, must be exactly 4: " + p_isValidHorizontal_1_);
            return false;
        }
        return true;
    }
    
    private boolean isValidVertical(final String p_isValidVertical_1_) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for vertical: " + p_isValidVertical_1_);
            return false;
        }
        if (this.tiles.length != 4) {
            Config.warn("Invalid tiles, must be exactly 4: " + p_isValidVertical_1_);
            return false;
        }
        return true;
    }
    
    private boolean isValidHorizontalVertical(final String p_isValidHorizontalVertical_1_) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for horizontal+vertical: " + p_isValidHorizontalVertical_1_);
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn("Invalid tiles, must be exactly 7: " + p_isValidHorizontalVertical_1_);
            return false;
        }
        return true;
    }
    
    private boolean isValidVerticalHorizontal(final String p_isValidVerticalHorizontal_1_) {
        if (this.tiles == null) {
            Config.warn("No tiles defined for vertical+horizontal: " + p_isValidVerticalHorizontal_1_);
            return false;
        }
        if (this.tiles.length != 7) {
            Config.warn("Invalid tiles, must be exactly 7: " + p_isValidVerticalHorizontal_1_);
            return false;
        }
        return true;
    }
    
    private boolean isValidRandom(final String p_isValidRandom_1_) {
        if (this.tiles != null && this.tiles.length > 0) {
            if (this.weights != null) {
                if (this.weights.length > this.tiles.length) {
                    Config.warn("More weights defined than tiles, trimming weights: " + p_isValidRandom_1_);
                    final int[] aint = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, aint, 0, aint.length);
                    this.weights = aint;
                }
                if (this.weights.length < this.tiles.length) {
                    Config.warn("Less weights defined than tiles, expanding weights: " + p_isValidRandom_1_);
                    final int[] aint2 = new int[this.tiles.length];
                    System.arraycopy(this.weights, 0, aint2, 0, this.weights.length);
                    final int i = MathUtils.getAverage(this.weights);
                    for (int j = this.weights.length; j < aint2.length; ++j) {
                        aint2[j] = i;
                    }
                    this.weights = aint2;
                }
                this.sumWeights = new int[this.weights.length];
                int k = 0;
                for (int l = 0; l < this.weights.length; ++l) {
                    k += this.weights[l];
                    this.sumWeights[l] = k;
                }
                this.sumAllWeights = k;
                if (this.sumAllWeights <= 0) {
                    Config.warn("Invalid sum of all weights: " + k);
                    this.sumAllWeights = 1;
                }
            }
            return true;
        }
        Config.warn("Tiles not defined: " + p_isValidRandom_1_);
        return false;
    }
    
    private boolean isValidRepeat(final String p_isValidRepeat_1_) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + p_isValidRepeat_1_);
            return false;
        }
        if (this.width <= 0 || this.width > 16) {
            Config.warn("Invalid width: " + p_isValidRepeat_1_);
            return false;
        }
        if (this.height <= 0 || this.height > 16) {
            Config.warn("Invalid height: " + p_isValidRepeat_1_);
            return false;
        }
        if (this.tiles.length != this.width * this.height) {
            Config.warn("Number of tiles does not equal width x height: " + p_isValidRepeat_1_);
            return false;
        }
        return true;
    }
    
    private boolean isValidFixed(final String p_isValidFixed_1_) {
        if (this.tiles == null) {
            Config.warn("Tiles not defined: " + p_isValidFixed_1_);
            return false;
        }
        if (this.tiles.length != 1) {
            Config.warn("Number of tiles should be 1 for method: fixed.");
            return false;
        }
        return true;
    }
    
    private boolean isValidTop(final String p_isValidTop_1_) {
        if (this.tiles == null) {
            this.tiles = this.parseTileNames("66");
        }
        if (this.tiles.length != 1) {
            Config.warn("Invalid tiles, must be exactly 1: " + p_isValidTop_1_);
            return false;
        }
        return true;
    }
    
    public void updateIcons(final TextureMap p_updateIcons_1_) {
        if (this.matchTiles != null) {
            this.matchTileIcons = registerIcons(this.matchTiles, p_updateIcons_1_);
        }
        if (this.tiles != null) {
            this.tileIcons = registerIcons(this.tiles, p_updateIcons_1_);
        }
    }
    
    private static TextureAtlasSprite[] registerIcons(final String[] p_registerIcons_0_, final TextureMap p_registerIcons_1_) {
        if (p_registerIcons_0_ == null) {
            return null;
        }
        final List list = new ArrayList();
        for (int i = 0; i < p_registerIcons_0_.length; ++i) {
            final String s = p_registerIcons_0_[i];
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final String s2 = resourcelocation.getResourceDomain();
            String s3 = resourcelocation.getResourcePath();
            if (!s3.contains("/")) {
                s3 = "textures/blocks/" + s3;
            }
            final String s4 = String.valueOf(s3) + ".png";
            final ResourceLocation resourcelocation2 = new ResourceLocation(s2, s4);
            final boolean flag = Config.hasResource(resourcelocation2);
            if (!flag) {
                Config.warn("File not found: " + s4);
            }
            final String s5 = "textures/";
            String s6 = s3;
            if (s3.startsWith(s5)) {
                s6 = s3.substring(s5.length());
            }
            final ResourceLocation resourcelocation3 = new ResourceLocation(s2, s6);
            final TextureAtlasSprite textureatlassprite = p_registerIcons_1_.registerSprite(resourcelocation3);
            list.add(textureatlassprite);
        }
        final TextureAtlasSprite[] atextureatlassprite = list.toArray(new TextureAtlasSprite[list.size()]);
        return atextureatlassprite;
    }
    
    public boolean matchesBlockId(final int p_matchesBlockId_1_) {
        return Matches.blockId(p_matchesBlockId_1_, this.matchBlocks);
    }
    
    public boolean matchesBlock(final int p_matchesBlock_1_, final int p_matchesBlock_2_) {
        return Matches.block(p_matchesBlock_1_, p_matchesBlock_2_, this.matchBlocks) && Matches.metadata(p_matchesBlock_2_, this.metadatas);
    }
    
    public boolean matchesIcon(final TextureAtlasSprite p_matchesIcon_1_) {
        return Matches.sprite(p_matchesIcon_1_, this.matchTileIcons);
    }
    
    @Override
    public String toString() {
        return "CTM name: " + this.name + ", basePath: " + this.basePath + ", matchBlocks: " + Config.arrayToString(this.matchBlocks) + ", matchTiles: " + Config.arrayToString(this.matchTiles);
    }
    
    public boolean matchesBiome(final BiomeGenBase p_matchesBiome_1_) {
        return Matches.biome(p_matchesBiome_1_, this.biomes);
    }
    
    public int getMetadataMax() {
        int i = -1;
        i = this.getMax(this.metadatas, i);
        if (this.matchBlocks != null) {
            for (int j = 0; j < this.matchBlocks.length; ++j) {
                final MatchBlock matchblock = this.matchBlocks[j];
                i = this.getMax(matchblock.getMetadatas(), i);
            }
        }
        return i;
    }
    
    private int getMax(final int[] p_getMax_1_, int p_getMax_2_) {
        if (p_getMax_1_ == null) {
            return p_getMax_2_;
        }
        for (int i = 0; i < p_getMax_1_.length; ++i) {
            final int j = p_getMax_1_[i];
            if (j > p_getMax_2_) {
                p_getMax_2_ = j;
            }
        }
        return p_getMax_2_;
    }
}
