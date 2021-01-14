package optifine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedParser
{
    private String context = null;
    public static final VillagerProfession[] PROFESSIONS_INVALID = new VillagerProfession[0];

    public ConnectedParser(String p_i27_1_)
    {
        this.context = p_i27_1_;
    }

    public String parseName(String p_parseName_1_)
    {
        String s = p_parseName_1_;
        int i = p_parseName_1_.lastIndexOf(47);

        if (i >= 0)
        {
            s = p_parseName_1_.substring(i + 1);
        }

        int j = s.lastIndexOf(46);

        if (j >= 0)
        {
            s = s.substring(0, j);
        }

        return s;
    }

    public String parseBasePath(String p_parseBasePath_1_)
    {
        int i = p_parseBasePath_1_.lastIndexOf(47);
        return i < 0 ? "" : p_parseBasePath_1_.substring(0, i);
    }

    public MatchBlock[] parseMatchBlocks(String p_parseMatchBlocks_1_)
    {
        if (p_parseMatchBlocks_1_ == null)
        {
            return null;
        }
        else
        {
            List<MatchBlock> list = new ArrayList<MatchBlock>();
            String[] astring = Config.tokenize(p_parseMatchBlocks_1_, " ");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                MatchBlock[] amatchblock = this.parseMatchBlock(s);

                if (amatchblock != null)
                {
                    list.addAll(Arrays.<MatchBlock>asList(amatchblock));
                }
            }

            MatchBlock[] amatchblock1 = (MatchBlock[])((MatchBlock[])list.toArray(new MatchBlock[list.size()]));
            return amatchblock1;
        }
    }

    public IBlockState parseBlockState(String p_parseBlockState_1_, IBlockState p_parseBlockState_2_)
    {
        MatchBlock[] amatchblock = this.parseMatchBlock(p_parseBlockState_1_);

        if (amatchblock == null)
        {
            return p_parseBlockState_2_;
        }
        else if (amatchblock.length != 1)
        {
            return p_parseBlockState_2_;
        }
        else
        {
            MatchBlock matchblock = amatchblock[0];
            int i = matchblock.getBlockId();
            Block block = Block.getBlockById(i);
            return block.getDefaultState();
        }
    }

    public MatchBlock[] parseMatchBlock(String p_parseMatchBlock_1_)
    {
        if (p_parseMatchBlock_1_ == null)
        {
            return null;
        }
        else
        {
            p_parseMatchBlock_1_ = p_parseMatchBlock_1_.trim();

            if (p_parseMatchBlock_1_.length() <= 0)
            {
                return null;
            }
            else
            {
                String[] astring = Config.tokenize(p_parseMatchBlock_1_, ":");
                String s = "minecraft";
                int i = 0;

                if (astring.length > 1 && this.isFullBlockName(astring))
                {
                    s = astring[0];
                    i = 1;
                }
                else
                {
                    s = "minecraft";
                    i = 0;
                }

                String s1 = astring[i];
                String[] astring1 = (String[])Arrays.copyOfRange(astring, i + 1, astring.length);
                Block[] ablock = this.parseBlockPart(s, s1);

                if (ablock == null)
                {
                    return null;
                }
                else
                {
                    MatchBlock[] amatchblock = new MatchBlock[ablock.length];

                    for (int j = 0; j < ablock.length; ++j)
                    {
                        Block block = ablock[j];
                        int k = Block.getIdFromBlock(block);
                        int[] aint = null;

                        if (astring1.length > 0)
                        {
                            aint = this.parseBlockMetadatas(block, astring1);

                            if (aint == null)
                            {
                                return null;
                            }
                        }

                        MatchBlock matchblock = new MatchBlock(k, aint);
                        amatchblock[j] = matchblock;
                    }

                    return amatchblock;
                }
            }
        }
    }

    public boolean isFullBlockName(String[] p_isFullBlockName_1_)
    {
        if (p_isFullBlockName_1_.length < 2)
        {
            return false;
        }
        else
        {
            String s = p_isFullBlockName_1_[1];
            return s.length() < 1 ? false : (this.startsWithDigit(s) ? false : !s.contains("="));
        }
    }

    public boolean startsWithDigit(String p_startsWithDigit_1_)
    {
        if (p_startsWithDigit_1_ == null)
        {
            return false;
        }
        else if (p_startsWithDigit_1_.length() < 1)
        {
            return false;
        }
        else
        {
            char c0 = p_startsWithDigit_1_.charAt(0);
            return Character.isDigit(c0);
        }
    }

    public Block[] parseBlockPart(String p_parseBlockPart_1_, String p_parseBlockPart_2_)
    {
        if (this.startsWithDigit(p_parseBlockPart_2_))
        {
            int[] aint = this.parseIntList(p_parseBlockPart_2_);

            if (aint == null)
            {
                return null;
            }
            else
            {
                Block[] ablock1 = new Block[aint.length];

                for (int j = 0; j < aint.length; ++j)
                {
                    int i = aint[j];
                    Block block1 = Block.getBlockById(i);

                    if (block1 == null)
                    {
                        this.warn("Block not found for id: " + i);
                        return null;
                    }

                    ablock1[j] = block1;
                }

                return ablock1;
            }
        }
        else
        {
            String s = p_parseBlockPart_1_ + ":" + p_parseBlockPart_2_;
            Block block = Block.getBlockFromName(s);

            if (block == null)
            {
                this.warn("Block not found for name: " + s);
                return null;
            }
            else
            {
                Block[] ablock = new Block[] {block};
                return ablock;
            }
        }
    }

    public int[] parseBlockMetadatas(Block p_parseBlockMetadatas_1_, String[] p_parseBlockMetadatas_2_)
    {
        if (p_parseBlockMetadatas_2_.length <= 0)
        {
            return null;
        }
        else
        {
            String s = p_parseBlockMetadatas_2_[0];

            if (this.startsWithDigit(s))
            {
                int[] aint = this.parseIntList(s);
                return aint;
            }
            else
            {
                IBlockState iblockstate = p_parseBlockMetadatas_1_.getDefaultState();
                Collection collection = iblockstate.getPropertyNames();
                Map<IProperty, List<Comparable>> map = new HashMap();

                for (int i = 0; i < p_parseBlockMetadatas_2_.length; ++i)
                {
                    String s1 = p_parseBlockMetadatas_2_[i];

                    if (s1.length() > 0)
                    {
                        String[] astring = Config.tokenize(s1, "=");

                        if (astring.length != 2)
                        {
                            this.warn("Invalid block property: " + s1);
                            return null;
                        }

                        String s2 = astring[0];
                        String s3 = astring[1];
                        IProperty iproperty = ConnectedProperties.getProperty(s2, collection);

                        if (iproperty == null)
                        {
                            this.warn("Property not found: " + s2 + ", block: " + p_parseBlockMetadatas_1_);
                            return null;
                        }

                        List<Comparable> list = (List)map.get(s2);

                        if (list == null)
                        {
                            list = new ArrayList();
                            map.put(iproperty, list);
                        }

                        String[] astring1 = Config.tokenize(s3, ",");

                        for (int j = 0; j < astring1.length; ++j)
                        {
                            String s4 = astring1[j];
                            Comparable comparable = parsePropertyValue(iproperty, s4);

                            if (comparable == null)
                            {
                                this.warn("Property value not found: " + s4 + ", property: " + s2 + ", block: " + p_parseBlockMetadatas_1_);
                                return null;
                            }

                            list.add(comparable);
                        }
                    }
                }

                if (map.isEmpty())
                {
                    return null;
                }
                else
                {
                    List list1 = new ArrayList();

                    for (int k = 0; k < 16; ++k)
                    {
                        int l = k;

                        try
                        {
                            IBlockState iblockstate1 = this.getStateFromMeta(p_parseBlockMetadatas_1_, l);

                            if (this.matchState(iblockstate1, map))
                            {
                                list1.add(Integer.valueOf(l));
                            }
                        }
                        catch (IllegalArgumentException var18)
                        {
                            ;
                        }
                    }

                    if (list1.size() == 16)
                    {
                        return null;
                    }
                    else
                    {
                        int[] aint1 = new int[list1.size()];

                        for (int i1 = 0; i1 < aint1.length; ++i1)
                        {
                            aint1[i1] = ((Integer)list1.get(i1)).intValue();
                        }

                        return aint1;
                    }
                }
            }
        }
    }

    private IBlockState getStateFromMeta(Block p_getStateFromMeta_1_, int p_getStateFromMeta_2_)
    {
        try
        {
            IBlockState iblockstate = p_getStateFromMeta_1_.getStateFromMeta(p_getStateFromMeta_2_);

            if (p_getStateFromMeta_1_ == Blocks.double_plant && p_getStateFromMeta_2_ > 7)
            {
                IBlockState iblockstate1 = p_getStateFromMeta_1_.getStateFromMeta(p_getStateFromMeta_2_ & 7);
                iblockstate = iblockstate.withProperty(BlockDoublePlant.VARIANT, iblockstate1.getValue(BlockDoublePlant.VARIANT));
            }

            return iblockstate;
        }
        catch (IllegalArgumentException var5)
        {
            return p_getStateFromMeta_1_.getDefaultState();
        }
    }

    public static Comparable parsePropertyValue(IProperty p_parsePropertyValue_0_, String p_parsePropertyValue_1_)
    {
        Class oclass = p_parsePropertyValue_0_.getValueClass();
        Comparable comparable = parseValue(p_parsePropertyValue_1_, oclass);

        if (comparable == null)
        {
            Collection collection = p_parsePropertyValue_0_.getAllowedValues();
            comparable = getPropertyValue(p_parsePropertyValue_1_, collection);
        }

        return comparable;
    }

    public static Comparable getPropertyValue(String p_getPropertyValue_0_, Collection<Comparable> p_getPropertyValue_1_)
    {
        for (Comparable comparable : p_getPropertyValue_1_)
        {
            if (getValueName(comparable).equals(p_getPropertyValue_0_))
            {
                return comparable;
            }
        }

        return null;
    }

    private static Object getValueName(Comparable p_getValueName_0_)
    {
        if (p_getValueName_0_ instanceof IStringSerializable)
        {
            IStringSerializable istringserializable = (IStringSerializable)p_getValueName_0_;
            return istringserializable.getName();
        }
        else
        {
            return p_getValueName_0_.toString();
        }
    }

    public static Comparable parseValue(String p_parseValue_0_, Class p_parseValue_1_)
    {
        return (Comparable)(p_parseValue_1_ == String.class ? p_parseValue_0_ : (p_parseValue_1_ == Boolean.class ? Boolean.valueOf(p_parseValue_0_) : (p_parseValue_1_ == Float.class ? Float.valueOf(p_parseValue_0_) : (p_parseValue_1_ == Double.class ? Double.valueOf(p_parseValue_0_) : (p_parseValue_1_ == Integer.class ? Integer.valueOf(p_parseValue_0_) : (p_parseValue_1_ == Long.class ? Long.valueOf(p_parseValue_0_) : null))))));
    }

    public boolean matchState(IBlockState p_matchState_1_, Map<IProperty, List<Comparable>> p_matchState_2_)
    {
        for (IProperty iproperty : p_matchState_2_.keySet())
        {
            List<Comparable> list = (List)p_matchState_2_.get(iproperty);
            Comparable comparable = p_matchState_1_.getValue(iproperty);

            if (comparable == null)
            {
                return false;
            }

            if (!list.contains(comparable))
            {
                return false;
            }
        }

        return true;
    }

    public BiomeGenBase[] parseBiomes(String p_parseBiomes_1_)
    {
        if (p_parseBiomes_1_ == null)
        {
            return null;
        }
        else
        {
            p_parseBiomes_1_ = p_parseBiomes_1_.trim();
            boolean flag = false;

            if (p_parseBiomes_1_.startsWith("!"))
            {
                flag = true;
                p_parseBiomes_1_ = p_parseBiomes_1_.substring(1);
            }

            String[] astring = Config.tokenize(p_parseBiomes_1_, " ");
            List list = new ArrayList();

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                BiomeGenBase biomegenbase = this.findBiome(s);

                if (biomegenbase == null)
                {
                    this.warn("Biome not found: " + s);
                }
                else
                {
                    list.add(biomegenbase);
                }
            }

            if (flag)
            {
                List<BiomeGenBase> list1 = new ArrayList(Arrays.asList(BiomeGenBase.getBiomeGenArray()));
                list1.removeAll(list);
                list = list1;
            }

            BiomeGenBase[] abiomegenbase = (BiomeGenBase[])((BiomeGenBase[])list.toArray(new BiomeGenBase[list.size()]));
            return abiomegenbase;
        }
    }

    public BiomeGenBase findBiome(String p_findBiome_1_)
    {
        p_findBiome_1_ = p_findBiome_1_.toLowerCase();

        if (p_findBiome_1_.equals("nether"))
        {
            return BiomeGenBase.hell;
        }
        else
        {
            BiomeGenBase[] abiomegenbase = BiomeGenBase.getBiomeGenArray();

            for (int i = 0; i < abiomegenbase.length; ++i)
            {
                BiomeGenBase biomegenbase = abiomegenbase[i];

                if (biomegenbase != null)
                {
                    String s = biomegenbase.biomeName.replace(" ", "").toLowerCase();

                    if (s.equals(p_findBiome_1_))
                    {
                        return biomegenbase;
                    }
                }
            }

            return null;
        }
    }

    public int parseInt(String p_parseInt_1_)
    {
        if (p_parseInt_1_ == null)
        {
            return -1;
        }
        else
        {
            p_parseInt_1_ = p_parseInt_1_.trim();
            int i = Config.parseInt(p_parseInt_1_, -1);

            if (i < 0)
            {
                this.warn("Invalid number: " + p_parseInt_1_);
            }

            return i;
        }
    }

    public int parseInt(String p_parseInt_1_, int p_parseInt_2_)
    {
        if (p_parseInt_1_ == null)
        {
            return p_parseInt_2_;
        }
        else
        {
            p_parseInt_1_ = p_parseInt_1_.trim();
            int i = Config.parseInt(p_parseInt_1_, -1);

            if (i < 0)
            {
                this.warn("Invalid number: " + p_parseInt_1_);
                return p_parseInt_2_;
            }
            else
            {
                return i;
            }
        }
    }

    public int[] parseIntList(String p_parseIntList_1_)
    {
        if (p_parseIntList_1_ == null)
        {
            return null;
        }
        else
        {
            List list = new ArrayList();
            String[] astring = Config.tokenize(p_parseIntList_1_, " ,");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (s.contains("-"))
                {
                    String[] astring1 = Config.tokenize(s, "-");

                    if (astring1.length != 2)
                    {
                        this.warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
                    }
                    else
                    {
                        int k = Config.parseInt(astring1[0], -1);
                        int l = Config.parseInt(astring1[1], -1);

                        if (k >= 0 && l >= 0 && k <= l)
                        {
                            for (int i1 = k; i1 <= l; ++i1)
                            {
                                list.add(Integer.valueOf(i1));
                            }
                        }
                        else
                        {
                            this.warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
                        }
                    }
                }
                else
                {
                    int j = Config.parseInt(s, -1);

                    if (j < 0)
                    {
                        this.warn("Invalid number: " + s + ", when parsing: " + p_parseIntList_1_);
                    }
                    else
                    {
                        list.add(Integer.valueOf(j));
                    }
                }
            }

            int[] aint = new int[list.size()];

            for (int j1 = 0; j1 < aint.length; ++j1)
            {
                aint[j1] = ((Integer)list.get(j1)).intValue();
            }

            return aint;
        }
    }

    public boolean[] parseFaces(String p_parseFaces_1_, boolean[] p_parseFaces_2_)
    {
        if (p_parseFaces_1_ == null)
        {
            return p_parseFaces_2_;
        }
        else
        {
            EnumSet enumset = EnumSet.allOf(EnumFacing.class);
            String[] astring = Config.tokenize(p_parseFaces_1_, " ,");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];

                if (s.equals("sides"))
                {
                    enumset.add(EnumFacing.NORTH);
                    enumset.add(EnumFacing.SOUTH);
                    enumset.add(EnumFacing.WEST);
                    enumset.add(EnumFacing.EAST);
                }
                else if (s.equals("all"))
                {
                    enumset.addAll(Arrays.asList(EnumFacing.VALUES));
                }
                else
                {
                    EnumFacing enumfacing = this.parseFace(s);

                    if (enumfacing != null)
                    {
                        enumset.add(enumfacing);
                    }
                }
            }

            boolean[] aboolean = new boolean[EnumFacing.VALUES.length];

            for (int j = 0; j < aboolean.length; ++j)
            {
                aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
            }

            return aboolean;
        }
    }

    public EnumFacing parseFace(String p_parseFace_1_)
    {
        p_parseFace_1_ = p_parseFace_1_.toLowerCase();

        if (!p_parseFace_1_.equals("bottom") && !p_parseFace_1_.equals("down"))
        {
            if (!p_parseFace_1_.equals("top") && !p_parseFace_1_.equals("up"))
            {
                if (p_parseFace_1_.equals("north"))
                {
                    return EnumFacing.NORTH;
                }
                else if (p_parseFace_1_.equals("south"))
                {
                    return EnumFacing.SOUTH;
                }
                else if (p_parseFace_1_.equals("east"))
                {
                    return EnumFacing.EAST;
                }
                else if (p_parseFace_1_.equals("west"))
                {
                    return EnumFacing.WEST;
                }
                else
                {
                    Config.warn("Unknown face: " + p_parseFace_1_);
                    return null;
                }
            }
            else
            {
                return EnumFacing.UP;
            }
        }
        else
        {
            return EnumFacing.DOWN;
        }
    }

    public void dbg(String p_dbg_1_)
    {
        Config.dbg("" + this.context + ": " + p_dbg_1_);
    }

    public void warn(String p_warn_1_)
    {
        Config.warn("" + this.context + ": " + p_warn_1_);
    }

    public RangeListInt parseRangeListInt(String p_parseRangeListInt_1_)
    {
        if (p_parseRangeListInt_1_ == null)
        {
            return null;
        }
        else
        {
            RangeListInt rangelistint = new RangeListInt();
            String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ,");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                RangeInt rangeint = this.parseRangeInt(s);

                if (rangeint == null)
                {
                    return null;
                }

                rangelistint.addRange(rangeint);
            }

            return rangelistint;
        }
    }

    private RangeInt parseRangeInt(String p_parseRangeInt_1_)
    {
        if (p_parseRangeInt_1_ == null)
        {
            return null;
        }
        else if (p_parseRangeInt_1_.indexOf(45) >= 0)
        {
            String[] astring = Config.tokenize(p_parseRangeInt_1_, "-");

            if (astring.length != 2)
            {
                this.warn("Invalid range: " + p_parseRangeInt_1_);
                return null;
            }
            else
            {
                int j = Config.parseInt(astring[0], -1);
                int k = Config.parseInt(astring[1], -1);

                if (j >= 0 && k >= 0)
                {
                    return new RangeInt(j, k);
                }
                else
                {
                    this.warn("Invalid range: " + p_parseRangeInt_1_);
                    return null;
                }
            }
        }
        else
        {
            int i = Config.parseInt(p_parseRangeInt_1_, -1);

            if (i < 0)
            {
                this.warn("Invalid integer: " + p_parseRangeInt_1_);
                return null;
            }
            else
            {
                return new RangeInt(i, i);
            }
        }
    }

    public boolean parseBoolean(String p_parseBoolean_1_)
    {
        if (p_parseBoolean_1_ == null)
        {
            return false;
        }
        else
        {
            String s = p_parseBoolean_1_.toLowerCase().trim();
            return s.equals("true");
        }
    }

    public Boolean parseBooleanObject(String p_parseBooleanObject_1_)
    {
        if (p_parseBooleanObject_1_ == null)
        {
            return null;
        }
        else
        {
            String s = p_parseBooleanObject_1_.toLowerCase().trim();

            if (s.equals("true"))
            {
                return Boolean.TRUE;
            }
            else if (s.equals("false"))
            {
                return Boolean.FALSE;
            }
            else
            {
                this.warn("Invalid boolean: " + p_parseBooleanObject_1_);
                return null;
            }
        }
    }

    public static int parseColor(String p_parseColor_0_, int p_parseColor_1_)
    {
        if (p_parseColor_0_ == null)
        {
            return p_parseColor_1_;
        }
        else
        {
            p_parseColor_0_ = p_parseColor_0_.trim();

            try
            {
                int i = Integer.parseInt(p_parseColor_0_, 16) & 16777215;
                return i;
            }
            catch (NumberFormatException var3)
            {
                return p_parseColor_1_;
            }
        }
    }

    public static int parseColor4(String p_parseColor4_0_, int p_parseColor4_1_)
    {
        if (p_parseColor4_0_ == null)
        {
            return p_parseColor4_1_;
        }
        else
        {
            p_parseColor4_0_ = p_parseColor4_0_.trim();

            try
            {
                int i = (int)(Long.parseLong(p_parseColor4_0_, 16) & -1L);
                return i;
            }
            catch (NumberFormatException var3)
            {
                return p_parseColor4_1_;
            }
        }
    }

    public EnumWorldBlockLayer parseBlockRenderLayer(String p_parseBlockRenderLayer_1_, EnumWorldBlockLayer p_parseBlockRenderLayer_2_)
    {
        if (p_parseBlockRenderLayer_1_ == null)
        {
            return p_parseBlockRenderLayer_2_;
        }
        else
        {
            p_parseBlockRenderLayer_1_ = p_parseBlockRenderLayer_1_.toLowerCase().trim();
            EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();

            for (int i = 0; i < aenumworldblocklayer.length; ++i)
            {
                EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];

                if (p_parseBlockRenderLayer_1_.equals(enumworldblocklayer.name().toLowerCase()))
                {
                    return enumworldblocklayer;
                }
            }

            return p_parseBlockRenderLayer_2_;
        }
    }

    public Enum parseEnum(String p_parseEnum_1_, Enum[] p_parseEnum_2_, String p_parseEnum_3_)
    {
        if (p_parseEnum_1_ == null)
        {
            return null;
        }
        else
        {
            String s = p_parseEnum_1_.toLowerCase().trim();

            for (int i = 0; i < p_parseEnum_2_.length; ++i)
            {
                Enum oenum = p_parseEnum_2_[i];

                if (oenum.name().toLowerCase().equals(s))
                {
                    return oenum;
                }
            }

            this.warn("Invalid " + p_parseEnum_3_ + ": " + p_parseEnum_1_);
            return null;
        }
    }

    public Enum[] parseEnums(String p_parseEnums_1_, Enum[] p_parseEnums_2_, String p_parseEnums_3_, Enum[] p_parseEnums_4_)
    {
        if (p_parseEnums_1_ == null)
        {
            return null;
        }
        else
        {
            p_parseEnums_1_ = p_parseEnums_1_.toLowerCase().trim();
            String[] astring = Config.tokenize(p_parseEnums_1_, " ");
            Enum[] aenum = (Enum[])((Enum[])Array.newInstance(p_parseEnums_2_.getClass().getComponentType(), astring.length));

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                Enum oenum = this.parseEnum(s, p_parseEnums_2_, p_parseEnums_3_);

                if (oenum == null)
                {
                    return p_parseEnums_4_;
                }

                aenum[i] = oenum;
            }

            return aenum;
        }
    }

    public NbtTagValue parseNbtTagValue(String p_parseNbtTagValue_1_, String p_parseNbtTagValue_2_)
    {
        return p_parseNbtTagValue_1_ != null && p_parseNbtTagValue_2_ != null ? new NbtTagValue(p_parseNbtTagValue_1_, p_parseNbtTagValue_2_) : null;
    }

    public VillagerProfession[] parseProfessions(String p_parseProfessions_1_)
    {
        if (p_parseProfessions_1_ == null)
        {
            return null;
        }
        else
        {
            List<VillagerProfession> list = new ArrayList();
            String[] astring = Config.tokenize(p_parseProfessions_1_, " ");

            for (int i = 0; i < astring.length; ++i)
            {
                String s = astring[i];
                VillagerProfession villagerprofession = this.parseProfession(s);

                if (villagerprofession == null)
                {
                    this.warn("Invalid profession: " + s);
                    return PROFESSIONS_INVALID;
                }

                list.add(villagerprofession);
            }

            if (list.isEmpty())
            {
                return null;
            }
            else
            {
                VillagerProfession[] avillagerprofession = (VillagerProfession[])((VillagerProfession[])list.toArray(new VillagerProfession[list.size()]));
                return avillagerprofession;
            }
        }
    }

    private VillagerProfession parseProfession(String p_parseProfession_1_)
    {
        p_parseProfession_1_ = p_parseProfession_1_.toLowerCase();
        String[] astring = Config.tokenize(p_parseProfession_1_, ":");

        if (astring.length > 2)
        {
            return null;
        }
        else
        {
            String s = astring[0];
            String s1 = null;

            if (astring.length > 1)
            {
                s1 = astring[1];
            }

            int i = parseProfessionId(s);

            if (i < 0)
            {
                return null;
            }
            else
            {
                int[] aint = null;

                if (s1 != null)
                {
                    aint = parseCareerIds(i, s1);

                    if (aint == null)
                    {
                        return null;
                    }
                }

                return new VillagerProfession(i, aint);
            }
        }
    }

    private static int parseProfessionId(String p_parseProfessionId_0_)
    {
        int i = Config.parseInt(p_parseProfessionId_0_, -1);
        return i >= 0 ? i : (p_parseProfessionId_0_.equals("farmer") ? 0 : (p_parseProfessionId_0_.equals("librarian") ? 1 : (p_parseProfessionId_0_.equals("priest") ? 2 : (p_parseProfessionId_0_.equals("blacksmith") ? 3 : (p_parseProfessionId_0_.equals("butcher") ? 4 : (p_parseProfessionId_0_.equals("nitwit") ? 5 : -1))))));
    }

    private static int[] parseCareerIds(int p_parseCareerIds_0_, String p_parseCareerIds_1_)
    {
        Set<Integer> set = new HashSet();
        String[] astring = Config.tokenize(p_parseCareerIds_1_, ",");

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            int j = parseCareerId(p_parseCareerIds_0_, s);

            if (j < 0)
            {
                return null;
            }

            set.add(Integer.valueOf(j));
        }

        Integer[] ainteger = (Integer[])((Integer[])set.toArray(new Integer[set.size()]));
        int[] aint = new int[ainteger.length];

        for (int k = 0; k < aint.length; ++k)
        {
            aint[k] = ainteger[k].intValue();
        }

        return aint;
    }

    private static int parseCareerId(int p_parseCareerId_0_, String p_parseCareerId_1_)
    {
        int i = Config.parseInt(p_parseCareerId_1_, -1);

        if (i >= 0)
        {
            return i;
        }
        else
        {
            if (p_parseCareerId_0_ == 0)
            {
                if (p_parseCareerId_1_.equals("farmer"))
                {
                    return 1;
                }

                if (p_parseCareerId_1_.equals("fisherman"))
                {
                    return 2;
                }

                if (p_parseCareerId_1_.equals("shepherd"))
                {
                    return 3;
                }

                if (p_parseCareerId_1_.equals("fletcher"))
                {
                    return 4;
                }
            }

            if (p_parseCareerId_0_ == 1)
            {
                if (p_parseCareerId_1_.equals("librarian"))
                {
                    return 1;
                }

                if (p_parseCareerId_1_.equals("cartographer"))
                {
                    return 2;
                }
            }

            if (p_parseCareerId_0_ == 2 && p_parseCareerId_1_.equals("cleric"))
            {
                return 1;
            }
            else
            {
                if (p_parseCareerId_0_ == 3)
                {
                    if (p_parseCareerId_1_.equals("armor"))
                    {
                        return 1;
                    }

                    if (p_parseCareerId_1_.equals("weapon"))
                    {
                        return 2;
                    }

                    if (p_parseCareerId_1_.equals("tool"))
                    {
                        return 3;
                    }
                }

                if (p_parseCareerId_0_ == 4)
                {
                    if (p_parseCareerId_1_.equals("butcher"))
                    {
                        return 1;
                    }

                    if (p_parseCareerId_1_.equals("leather"))
                    {
                        return 2;
                    }
                }

                return p_parseCareerId_0_ == 5 && p_parseCareerId_1_.equals("nitwit") ? 1 : -1;
            }
        }
    }
}
