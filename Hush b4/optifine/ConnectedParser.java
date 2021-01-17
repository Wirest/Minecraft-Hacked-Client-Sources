// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.EnumSet;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.Iterator;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import java.util.HashMap;
import net.minecraft.block.Block;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class ConnectedParser
{
    private String context;
    private static final MatchBlock[] NO_MATCH_BLOCKS;
    
    static {
        NO_MATCH_BLOCKS = new MatchBlock[0];
    }
    
    public ConnectedParser(final String p_i31_1_) {
        this.context = null;
        this.context = p_i31_1_;
    }
    
    public String parseName(final String p_parseName_1_) {
        String s = p_parseName_1_;
        final int i = p_parseName_1_.lastIndexOf(47);
        if (i >= 0) {
            s = p_parseName_1_.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    public String parseBasePath(final String p_parseBasePath_1_) {
        final int i = p_parseBasePath_1_.lastIndexOf(47);
        return (i < 0) ? "" : p_parseBasePath_1_.substring(0, i);
    }
    
    public MatchBlock[] parseMatchBlocks(final String p_parseMatchBlocks_1_) {
        if (p_parseMatchBlocks_1_ == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(p_parseMatchBlocks_1_, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final MatchBlock[] amatchblock = this.parseMatchBlock(s);
            if (amatchblock == null) {
                return ConnectedParser.NO_MATCH_BLOCKS;
            }
            list.addAll(Arrays.asList(amatchblock));
        }
        final MatchBlock[] amatchblock2 = list.toArray(new MatchBlock[list.size()]);
        return amatchblock2;
    }
    
    public MatchBlock[] parseMatchBlock(String p_parseMatchBlock_1_) {
        if (p_parseMatchBlock_1_ == null) {
            return null;
        }
        p_parseMatchBlock_1_ = p_parseMatchBlock_1_.trim();
        if (p_parseMatchBlock_1_.length() <= 0) {
            return null;
        }
        final String[] astring = Config.tokenize(p_parseMatchBlock_1_, ":");
        String s = "minecraft";
        int i = 0;
        if (astring.length > 1 && this.isFullBlockName(astring)) {
            s = astring[0];
            i = 1;
        }
        else {
            s = "minecraft";
            i = 0;
        }
        final String s2 = astring[i];
        final String[] astring2 = Arrays.copyOfRange(astring, i + 1, astring.length);
        final Block[] ablock = this.parseBlockPart(s, s2);
        if (ablock == null) {
            return null;
        }
        final MatchBlock[] amatchblock = new MatchBlock[ablock.length];
        for (int j = 0; j < ablock.length; ++j) {
            final Block block = ablock[j];
            final int k = Block.getIdFromBlock(block);
            int[] aint = null;
            if (astring2.length > 0) {
                aint = this.parseBlockMetadatas(block, astring2);
                if (aint == null) {
                    return null;
                }
            }
            final MatchBlock matchblock = new MatchBlock(k, aint);
            amatchblock[j] = matchblock;
        }
        return amatchblock;
    }
    
    public boolean isFullBlockName(final String[] p_isFullBlockName_1_) {
        if (p_isFullBlockName_1_.length < 2) {
            return false;
        }
        final String s = p_isFullBlockName_1_[1];
        return s.length() >= 1 && !this.startsWithDigit(s) && !s.contains("=");
    }
    
    public boolean startsWithDigit(final String p_startsWithDigit_1_) {
        if (p_startsWithDigit_1_ == null) {
            return false;
        }
        if (p_startsWithDigit_1_.length() < 1) {
            return false;
        }
        final char c0 = p_startsWithDigit_1_.charAt(0);
        return Character.isDigit(c0);
    }
    
    public Block[] parseBlockPart(final String p_parseBlockPart_1_, final String p_parseBlockPart_2_) {
        if (this.startsWithDigit(p_parseBlockPart_2_)) {
            final int[] aint = this.parseIntList(p_parseBlockPart_2_);
            if (aint == null) {
                return null;
            }
            final Block[] ablock1 = new Block[aint.length];
            for (int j = 0; j < aint.length; ++j) {
                final int i = aint[j];
                final Block block1 = Block.getBlockById(i);
                if (block1 == null) {
                    this.warn("Block not found for id: " + i);
                    return null;
                }
                ablock1[j] = block1;
            }
            return ablock1;
        }
        else {
            final String s = String.valueOf(p_parseBlockPart_1_) + ":" + p_parseBlockPart_2_;
            final Block block2 = Block.getBlockFromName(s);
            if (block2 == null) {
                this.warn("Block not found for name: " + s);
                return null;
            }
            final Block[] ablock2 = { block2 };
            return ablock2;
        }
    }
    
    public int[] parseBlockMetadatas(final Block p_parseBlockMetadatas_1_, final String[] p_parseBlockMetadatas_2_) {
        if (p_parseBlockMetadatas_2_.length <= 0) {
            return null;
        }
        final String s = p_parseBlockMetadatas_2_[0];
        if (this.startsWithDigit(s)) {
            final int[] aint = this.parseIntList(s);
            return aint;
        }
        final IBlockState iblockstate = p_parseBlockMetadatas_1_.getDefaultState();
        final Collection collection = iblockstate.getPropertyNames();
        final Map<IProperty, List<Comparable>> map = new HashMap<IProperty, List<Comparable>>();
        for (int i = 0; i < p_parseBlockMetadatas_2_.length; ++i) {
            final String s2 = p_parseBlockMetadatas_2_[i];
            if (s2.length() > 0) {
                final String[] astring = Config.tokenize(s2, "=");
                if (astring.length != 2) {
                    this.warn("Invalid block property: " + s2);
                    return null;
                }
                final String s3 = astring[0];
                final String s4 = astring[1];
                final IProperty iproperty = ConnectedProperties.getProperty(s3, collection);
                if (iproperty == null) {
                    this.warn("Property not found: " + s3 + ", block: " + p_parseBlockMetadatas_1_);
                    return null;
                }
                List<Comparable> list = map.get(s3);
                if (list == null) {
                    list = new ArrayList<Comparable>();
                    map.put(iproperty, list);
                }
                final String[] astring2 = Config.tokenize(s4, ",");
                for (int j = 0; j < astring2.length; ++j) {
                    final String s5 = astring2[j];
                    final Comparable comparable = parsePropertyValue(iproperty, s5);
                    if (comparable == null) {
                        this.warn("Property value not found: " + s5 + ", property: " + s3 + ", block: " + p_parseBlockMetadatas_1_);
                        return null;
                    }
                    list.add(comparable);
                }
            }
        }
        if (map.isEmpty()) {
            return null;
        }
        final List list2 = new ArrayList();
        for (int k = 0; k < 16; ++k) {
            final int l = k;
            try {
                final IBlockState iblockstate2 = this.getStateFromMeta(p_parseBlockMetadatas_1_, l);
                if (this.matchState(iblockstate2, map)) {
                    list2.add(l);
                }
            }
            catch (IllegalArgumentException ex) {}
        }
        if (list2.size() == 16) {
            return null;
        }
        final int[] aint2 = new int[list2.size()];
        for (int i2 = 0; i2 < aint2.length; ++i2) {
            aint2[i2] = list2.get(i2);
        }
        return aint2;
    }
    
    private IBlockState getStateFromMeta(final Block p_getStateFromMeta_1_, final int p_getStateFromMeta_2_) {
        try {
            IBlockState iblockstate = p_getStateFromMeta_1_.getStateFromMeta(p_getStateFromMeta_2_);
            if (p_getStateFromMeta_1_ == Blocks.double_plant && p_getStateFromMeta_2_ > 7) {
                final IBlockState iblockstate2 = p_getStateFromMeta_1_.getStateFromMeta(p_getStateFromMeta_2_ & 0x7);
                iblockstate = iblockstate.withProperty(BlockDoublePlant.VARIANT, (BlockDoublePlant.EnumPlantType)iblockstate2.getValue((IProperty<V>)BlockDoublePlant.VARIANT));
            }
            return iblockstate;
        }
        catch (IllegalArgumentException var5) {
            return p_getStateFromMeta_1_.getDefaultState();
        }
    }
    
    public static Comparable parsePropertyValue(final IProperty p_parsePropertyValue_0_, final String p_parsePropertyValue_1_) {
        final Class oclass = p_parsePropertyValue_0_.getValueClass();
        Comparable comparable = parseValue(p_parsePropertyValue_1_, oclass);
        if (comparable == null) {
            final Collection collection = p_parsePropertyValue_0_.getAllowedValues();
            comparable = getPropertyValue(p_parsePropertyValue_1_, collection);
        }
        return comparable;
    }
    
    public static Comparable getPropertyValue(final String p_getPropertyValue_0_, final Collection p_getPropertyValue_1_) {
        for (final Object comparable : p_getPropertyValue_1_) {
            if (String.valueOf(comparable).equals(p_getPropertyValue_0_)) {
                return (Comparable)comparable;
            }
        }
        return null;
    }
    
    public static Comparable parseValue(final String p_parseValue_0_, final Class p_parseValue_1_) {
        return (Comparable)((p_parseValue_1_ == String.class) ? p_parseValue_0_ : ((p_parseValue_1_ == Boolean.class) ? Boolean.valueOf(p_parseValue_0_) : ((Double)((p_parseValue_1_ == Float.class) ? Float.valueOf(p_parseValue_0_) : ((p_parseValue_1_ == Double.class) ? Double.valueOf(p_parseValue_0_) : ((double)((p_parseValue_1_ == Integer.class) ? Integer.valueOf(p_parseValue_0_) : ((long)((p_parseValue_1_ == Long.class) ? Long.valueOf(p_parseValue_0_) : null)))))))));
    }
    
    public boolean matchState(final IBlockState p_matchState_1_, final Map<IProperty, List<Comparable>> p_matchState_2_) {
        for (final IProperty iproperty : p_matchState_2_.keySet()) {
            final List<Comparable> list = p_matchState_2_.get(iproperty);
            final Comparable comparable = p_matchState_1_.getValue((IProperty<Comparable>)iproperty);
            if (comparable == null) {
                return false;
            }
            if (!list.contains(comparable)) {
                return false;
            }
        }
        return true;
    }
    
    public BiomeGenBase[] parseBiomes(final String p_parseBiomes_1_) {
        if (p_parseBiomes_1_ == null) {
            return null;
        }
        final String[] astring = Config.tokenize(p_parseBiomes_1_, " ");
        final List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final BiomeGenBase biomegenbase = this.findBiome(s);
            if (biomegenbase == null) {
                this.warn("Biome not found: " + s);
            }
            else {
                list.add(biomegenbase);
            }
        }
        final BiomeGenBase[] abiomegenbase = list.toArray(new BiomeGenBase[list.size()]);
        return abiomegenbase;
    }
    
    public BiomeGenBase findBiome(String p_findBiome_1_) {
        p_findBiome_1_ = p_findBiome_1_.toLowerCase();
        if (p_findBiome_1_.equals("nether")) {
            return BiomeGenBase.hell;
        }
        final BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();
        for (int i = 0; i < abiomegenbase.length; ++i) {
            final BiomeGenBase biomegenbase = abiomegenbase[i];
            if (biomegenbase != null) {
                final String s = biomegenbase.biomeName.replace(" ", "").toLowerCase();
                if (s.equals(p_findBiome_1_)) {
                    return biomegenbase;
                }
            }
        }
        return null;
    }
    
    public int parseInt(final String p_parseInt_1_) {
        if (p_parseInt_1_ == null) {
            return -1;
        }
        final int i = Config.parseInt(p_parseInt_1_, -1);
        if (i < 0) {
            this.warn("Invalid number: " + p_parseInt_1_);
        }
        return i;
    }
    
    public int parseInt(final String p_parseInt_1_, final int p_parseInt_2_) {
        if (p_parseInt_1_ == null) {
            return p_parseInt_2_;
        }
        final int i = Config.parseInt(p_parseInt_1_, -1);
        if (i < 0) {
            this.warn("Invalid number: " + p_parseInt_1_);
            return p_parseInt_2_;
        }
        return i;
    }
    
    public int[] parseIntList(final String p_parseIntList_1_) {
        if (p_parseIntList_1_ == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(p_parseIntList_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.contains("-")) {
                final String[] astring2 = Config.tokenize(s, "-");
                if (astring2.length != 2) {
                    this.warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
                }
                else {
                    final int k = Config.parseInt(astring2[0], -1);
                    final int l = Config.parseInt(astring2[1], -1);
                    if (k >= 0 && l >= 0 && k <= l) {
                        for (int i2 = k; i2 <= l; ++i2) {
                            list.add(i2);
                        }
                    }
                    else {
                        this.warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
                    }
                }
            }
            else {
                final int j = Config.parseInt(s, -1);
                if (j < 0) {
                    this.warn("Invalid number: " + s + ", when parsing: " + p_parseIntList_1_);
                }
                else {
                    list.add(j);
                }
            }
        }
        final int[] aint = new int[list.size()];
        for (int j2 = 0; j2 < aint.length; ++j2) {
            aint[j2] = list.get(j2);
        }
        return aint;
    }
    
    public boolean[] parseFaces(final String p_parseFaces_1_, final boolean[] p_parseFaces_2_) {
        if (p_parseFaces_1_ == null) {
            return p_parseFaces_2_;
        }
        final EnumSet enumset = EnumSet.allOf(EnumFacing.class);
        final String[] astring = Config.tokenize(p_parseFaces_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.equals("sides")) {
                enumset.add(EnumFacing.NORTH);
                enumset.add(EnumFacing.SOUTH);
                enumset.add(EnumFacing.WEST);
                enumset.add(EnumFacing.EAST);
            }
            else if (s.equals("all")) {
                enumset.addAll(Arrays.asList(EnumFacing.VALUES));
            }
            else {
                final EnumFacing enumfacing = this.parseFace(s);
                if (enumfacing != null) {
                    enumset.add(enumfacing);
                }
            }
        }
        final boolean[] aboolean = new boolean[EnumFacing.VALUES.length];
        for (int j = 0; j < aboolean.length; ++j) {
            aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
        }
        return aboolean;
    }
    
    public EnumFacing parseFace(String p_parseFace_1_) {
        p_parseFace_1_ = p_parseFace_1_.toLowerCase();
        if (p_parseFace_1_.equals("bottom") || p_parseFace_1_.equals("down")) {
            return EnumFacing.DOWN;
        }
        if (p_parseFace_1_.equals("top") || p_parseFace_1_.equals("up")) {
            return EnumFacing.UP;
        }
        if (p_parseFace_1_.equals("north")) {
            return EnumFacing.NORTH;
        }
        if (p_parseFace_1_.equals("south")) {
            return EnumFacing.SOUTH;
        }
        if (p_parseFace_1_.equals("east")) {
            return EnumFacing.EAST;
        }
        if (p_parseFace_1_.equals("west")) {
            return EnumFacing.WEST;
        }
        Config.warn("Unknown face: " + p_parseFace_1_);
        return null;
    }
    
    public void dbg(final String p_dbg_1_) {
        Config.dbg(this.context + ": " + p_dbg_1_);
    }
    
    public void warn(final String p_warn_1_) {
        Config.warn(this.context + ": " + p_warn_1_);
    }
    
    public RangeListInt parseRangeListInt(final String p_parseRangeListInt_1_) {
        if (p_parseRangeListInt_1_ == null) {
            return null;
        }
        final RangeListInt rangelistint = new RangeListInt();
        final String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }
    
    private RangeInt parseRangeInt(final String p_parseRangeInt_1_) {
        if (p_parseRangeInt_1_ == null) {
            return null;
        }
        if (p_parseRangeInt_1_.indexOf(45) >= 0) {
            final String[] astring = Config.tokenize(p_parseRangeInt_1_, "-");
            if (astring.length != 2) {
                this.warn("Invalid range: " + p_parseRangeInt_1_);
                return null;
            }
            final int j = Config.parseInt(astring[0], -1);
            final int k = Config.parseInt(astring[1], -1);
            if (j >= 0 && k >= 0) {
                return new RangeInt(j, k);
            }
            this.warn("Invalid range: " + p_parseRangeInt_1_);
            return null;
        }
        else {
            final int i = Config.parseInt(p_parseRangeInt_1_, -1);
            if (i < 0) {
                this.warn("Invalid integer: " + p_parseRangeInt_1_);
                return null;
            }
            return new RangeInt(i, i);
        }
    }
    
    public static boolean parseBoolean(final String p_parseBoolean_0_) {
        return p_parseBoolean_0_ != null && p_parseBoolean_0_.toLowerCase().equals("true");
    }
    
    public static int parseColor(String p_parseColor_0_, final int p_parseColor_1_) {
        if (p_parseColor_0_ == null) {
            return p_parseColor_1_;
        }
        p_parseColor_0_ = p_parseColor_0_.trim();
        try {
            final int i = Integer.parseInt(p_parseColor_0_, 16) & 0xFFFFFF;
            return i;
        }
        catch (NumberFormatException var3) {
            return p_parseColor_1_;
        }
    }
}
