package optifine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedParser
{
    private String context = null;
    private static final MatchBlock[] NO_MATCH_BLOCKS = new MatchBlock[0];

    public ConnectedParser(String context)
    {
        this.context = context;
    }

    public String parseName(String path)
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

    public String parseBasePath(String path)
    {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }

    public MatchBlock[] parseMatchBlocks(String propMatchBlocks)
    {
        if (propMatchBlocks == null)
        {
            return null;
        }
        else
        {
            ArrayList list = new ArrayList();
            String[] blockStrs = Config.tokenize(propMatchBlocks, " ");

            for (int mbs = 0; mbs < blockStrs.length; ++mbs)
            {
                String blockStr = blockStrs[mbs];
                MatchBlock[] mbs1 = this.parseMatchBlock(blockStr);

                if (mbs1 == null)
                {
                    return NO_MATCH_BLOCKS;
                }

                list.addAll(Arrays.asList(mbs1));
            }

            MatchBlock[] var7 = (MatchBlock[])((MatchBlock[])list.toArray(new MatchBlock[list.size()]));
            return var7;
        }
    }

    public MatchBlock[] parseMatchBlock(String blockStr)
    {
        if (blockStr == null)
        {
            return null;
        }
        else
        {
            blockStr = blockStr.trim();

            if (blockStr.length() <= 0)
            {
                return null;
            }
            else
            {
                String[] parts = Config.tokenize(blockStr, ":");
                String domain = "minecraft";
                boolean blockIndex = false;
                byte var14;

                if (parts.length > 1 && this.isFullBlockName(parts))
                {
                    domain = parts[0];
                    var14 = 1;
                }
                else
                {
                    domain = "minecraft";
                    var14 = 0;
                }

                String blockPart = parts[var14];
                String[] params = (String[])Arrays.copyOfRange(parts, var14 + 1, parts.length);
                Block[] blocks = this.parseBlockPart(domain, blockPart);

                if (blocks == null)
                {
                    return null;
                }
                else
                {
                    MatchBlock[] datas = new MatchBlock[blocks.length];

                    for (int i = 0; i < blocks.length; ++i)
                    {
                        Block block = blocks[i];
                        int blockId = Block.getIdFromBlock(block);
                        int[] metadatas = null;

                        if (params.length > 0)
                        {
                            metadatas = this.parseBlockMetadatas(block, params);

                            if (metadatas == null)
                            {
                                return null;
                            }
                        }

                        MatchBlock bd = new MatchBlock(blockId, metadatas);
                        datas[i] = bd;
                    }

                    return datas;
                }
            }
        }
    }

    public boolean isFullBlockName(String[] parts)
    {
        if (parts.length < 2)
        {
            return false;
        }
        else
        {
            String part1 = parts[1];
            return part1.length() < 1 ? false : (this.startsWithDigit(part1) ? false : !part1.contains("="));
        }
    }

    public boolean startsWithDigit(String str)
    {
        if (str == null)
        {
            return false;
        }
        else if (str.length() < 1)
        {
            return false;
        }
        else
        {
            char ch = str.charAt(0);
            return Character.isDigit(ch);
        }
    }

    public Block[] parseBlockPart(String domain, String blockPart)
    {
        if (this.startsWithDigit(blockPart))
        {
            int[] var8 = this.parseIntList(blockPart);

            if (var8 == null)
            {
                return null;
            }
            else
            {
                Block[] var9 = new Block[var8.length];

                for (int var10 = 0; var10 < var8.length; ++var10)
                {
                    int id = var8[var10];
                    Block block1 = Block.getBlockById(id);

                    if (block1 == null)
                    {
                        this.warn("Block not found for id: " + id);
                        return null;
                    }

                    var9[var10] = block1;
                }

                return var9;
            }
        }
        else
        {
            String fullName = domain + ":" + blockPart;
            Block block = Block.getBlockFromName(fullName);

            if (block == null)
            {
                this.warn("Block not found for name: " + fullName);
                return null;
            }
            else
            {
                Block[] blocks = new Block[] {block};
                return blocks;
            }
        }
    }

    public int[] parseBlockMetadatas(Block block, String[] params)
    {
        if (params.length <= 0)
        {
            return null;
        }
        else
        {
            String param0 = params[0];

            if (this.startsWithDigit(param0))
            {
                int[] var19 = this.parseIntList(param0);
                return var19;
            }
            else
            {
                IBlockState stateDefault = block.getDefaultState();
                Collection properties = stateDefault.getPropertyNames();
                HashMap mapPropValues = new HashMap();

                for (int listMetadatas = 0; listMetadatas < params.length; ++listMetadatas)
                {
                    String metadatas = params[listMetadatas];

                    if (metadatas.length() > 0)
                    {
                        String[] i = Config.tokenize(metadatas, "=");

                        if (i.length != 2)
                        {
                            this.warn("Invalid block property: " + metadatas);
                            return null;
                        }

                        String e = i[0];
                        String valStr = i[1];
                        IProperty prop = ConnectedProperties.getProperty(e, properties);

                        if (prop == null)
                        {
                            this.warn("Property not found: " + e + ", block: " + block);
                            return null;
                        }

                        Object list = (List)mapPropValues.get(e);

                        if (list == null)
                        {
                            list = new ArrayList();
                            mapPropValues.put(prop, list);
                        }

                        String[] vals = Config.tokenize(valStr, ",");

                        for (int v = 0; v < vals.length; ++v)
                        {
                            String val = vals[v];
                            Comparable propVal = parsePropertyValue(prop, val);

                            if (propVal == null)
                            {
                                this.warn("Property value not found: " + val + ", property: " + e + ", block: " + block);
                                return null;
                            }

                            ((List)list).add(propVal);
                        }
                    }
                }

                if (mapPropValues.isEmpty())
                {
                    return null;
                }
                else
                {
                    ArrayList var20 = new ArrayList();
                    int var23;

                    for (int var21 = 0; var21 < 16; ++var21)
                    {
                        var23 = var21;

                        try
                        {
                            IBlockState var24 = this.getStateFromMeta(block, var23);

                            if (this.matchState(var24, mapPropValues))
                            {
                                var20.add(Integer.valueOf(var23));
                            }
                        }
                        catch (IllegalArgumentException var18)
                        {
                            ;
                        }
                    }

                    if (var20.size() == 16)
                    {
                        return null;
                    }
                    else
                    {
                        int[] var22 = new int[var20.size()];

                        for (var23 = 0; var23 < var22.length; ++var23)
                        {
                            var22[var23] = ((Integer)var20.get(var23)).intValue();
                        }

                        return var22;
                    }
                }
            }
        }
    }

    private IBlockState getStateFromMeta(Block block, int md)
    {
        try
        {
            IBlockState e = block.getStateFromMeta(md);

            if (block == Blocks.double_plant && md > 7)
            {
                IBlockState bsLow = block.getStateFromMeta(md & 7);
                e = e.withProperty(BlockDoublePlant.VARIANT_PROP, bsLow.getValue(BlockDoublePlant.VARIANT_PROP));
            }

            return e;
        }
        catch (IllegalArgumentException var5)
        {
            return block.getDefaultState();
        }
    }

    public static Comparable parsePropertyValue(IProperty prop, String valStr)
    {
        Class valueClass = prop.getValueClass();
        Comparable valueObj = parseValue(valStr, valueClass);

        if (valueObj == null)
        {
            Collection propertyValues = prop.getAllowedValues();
            valueObj = getPropertyValue(valStr, propertyValues);
        }

        return valueObj;
    }

    public static Comparable getPropertyValue(String value, Collection propertyValues)
    {
        Iterator it = propertyValues.iterator();
        Comparable obj;

        do
        {
            if (!it.hasNext())
            {
                return null;
            }

            obj = (Comparable)it.next();
        }
        while (!String.valueOf(obj).equals(value));

        return obj;
    }

    public static Comparable parseValue(String str, Class cls)
    {
        return (Comparable)(cls == String.class ? str : (cls == Boolean.class ? Boolean.valueOf(str) : (cls == Float.class ? Float.valueOf(str) : (cls == Double.class ? Double.valueOf(str) : (cls == Integer.class ? Integer.valueOf(str) : (cls == Long.class ? Long.valueOf(str) : null))))));
    }

    public boolean matchState(IBlockState bs, Map<IProperty, List<Comparable>> mapPropValues)
    {
        Set keys = mapPropValues.keySet();
        Iterator it = keys.iterator();
        List vals;
        Comparable bsVal;

        do
        {
            if (!it.hasNext())
            {
                return true;
            }

            IProperty prop = (IProperty)it.next();
            vals = (List)mapPropValues.get(prop);
            bsVal = bs.getValue(prop);

            if (bsVal == null)
            {
                return false;
            }
        }
        while (vals.contains(bsVal));

        return false;
    }

    public BiomeGenBase[] parseBiomes(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            String[] biomeNames = Config.tokenize(str, " ");
            ArrayList list = new ArrayList();

            for (int biomeArr = 0; biomeArr < biomeNames.length; ++biomeArr)
            {
                String biomeName = biomeNames[biomeArr];
                BiomeGenBase biome = this.findBiome(biomeName);

                if (biome == null)
                {
                    this.warn("Biome not found: " + biomeName);
                }
                else
                {
                    list.add(biome);
                }
            }

            BiomeGenBase[] var7 = (BiomeGenBase[])((BiomeGenBase[])list.toArray(new BiomeGenBase[list.size()]));
            return var7;
        }
    }

    public BiomeGenBase findBiome(String biomeName)
    {
        biomeName = biomeName.toLowerCase();

        if (biomeName.equals("nether"))
        {
            return BiomeGenBase.hell;
        }
        else
        {
            BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();

            for (int i = 0; i < biomeList.length; ++i)
            {
                BiomeGenBase biome = biomeList[i];

                if (biome != null)
                {
                    String name = biome.biomeName.replace(" ", "").toLowerCase();

                    if (name.equals(biomeName))
                    {
                        return biome;
                    }
                }
            }

            return null;
        }
    }

    public int parseInt(String str)
    {
        if (str == null)
        {
            return -1;
        }
        else
        {
            int num = Config.parseInt(str, -1);

            if (num < 0)
            {
                this.warn("Invalid number: " + str);
            }

            return num;
        }
    }

    public int parseInt(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            int num = Config.parseInt(str, -1);

            if (num < 0)
            {
                this.warn("Invalid number: " + str);
                return defVal;
            }
            else
            {
                return num;
            }
        }
    }

    public int[] parseIntList(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            ArrayList list = new ArrayList();
            String[] intStrs = Config.tokenize(str, " ,");

            for (int ints = 0; ints < intStrs.length; ++ints)
            {
                String i = intStrs[ints];

                if (i.contains("-"))
                {
                    String[] val = Config.tokenize(i, "-");

                    if (val.length != 2)
                    {
                        this.warn("Invalid interval: " + i + ", when parsing: " + str);
                    }
                    else
                    {
                        int min = Config.parseInt(val[0], -1);
                        int max = Config.parseInt(val[1], -1);

                        if (min >= 0 && max >= 0 && min <= max)
                        {
                            for (int n = min; n <= max; ++n)
                            {
                                list.add(Integer.valueOf(n));
                            }
                        }
                        else
                        {
                            this.warn("Invalid interval: " + i + ", when parsing: " + str);
                        }
                    }
                }
                else
                {
                    int var12 = Config.parseInt(i, -1);

                    if (var12 < 0)
                    {
                        this.warn("Invalid number: " + i + ", when parsing: " + str);
                    }
                    else
                    {
                        list.add(Integer.valueOf(var12));
                    }
                }
            }

            int[] var10 = new int[list.size()];

            for (int var11 = 0; var11 < var10.length; ++var11)
            {
                var10[var11] = ((Integer)list.get(var11)).intValue();
            }

            return var10;
        }
    }

    public boolean[] parseFaces(String str, boolean[] defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            EnumSet setFaces = EnumSet.allOf(EnumFacing.class);
            String[] faceStrs = Config.tokenize(str, " ,");

            for (int faces = 0; faces < faceStrs.length; ++faces)
            {
                String i = faceStrs[faces];

                if (i.equals("sides"))
                {
                    setFaces.add(EnumFacing.NORTH);
                    setFaces.add(EnumFacing.SOUTH);
                    setFaces.add(EnumFacing.WEST);
                    setFaces.add(EnumFacing.EAST);
                }
                else if (i.equals("all"))
                {
                    setFaces.addAll(Arrays.asList(EnumFacing.VALUES));
                }
                else
                {
                    EnumFacing face = this.parseFace(i);

                    if (face != null)
                    {
                        setFaces.add(face);
                    }
                }
            }

            boolean[] var8 = new boolean[EnumFacing.VALUES.length];

            for (int var9 = 0; var9 < var8.length; ++var9)
            {
                var8[var9] = setFaces.contains(EnumFacing.VALUES[var9]);
            }

            return var8;
        }
    }

    public EnumFacing parseFace(String str)
    {
        str = str.toLowerCase();

        if (!str.equals("bottom") && !str.equals("down"))
        {
            if (!str.equals("top") && !str.equals("up"))
            {
                if (str.equals("north"))
                {
                    return EnumFacing.NORTH;
                }
                else if (str.equals("south"))
                {
                    return EnumFacing.SOUTH;
                }
                else if (str.equals("east"))
                {
                    return EnumFacing.EAST;
                }
                else if (str.equals("west"))
                {
                    return EnumFacing.WEST;
                }
                else
                {
                    Config.warn("Unknown face: " + str);
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

    public void dbg(String str)
    {
        Config.dbg("" + this.context + ": " + str);
    }

    public void warn(String str)
    {
        Config.warn("" + this.context + ": " + str);
    }

    public RangeListInt parseRangeListInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else
        {
            RangeListInt list = new RangeListInt();
            String[] parts = Config.tokenize(str, " ,");

            for (int i = 0; i < parts.length; ++i)
            {
                String part = parts[i];
                RangeInt ri = this.parseRangeInt(part);

                if (ri == null)
                {
                    return null;
                }

                list.addRange(ri);
            }

            return list;
        }
    }

    private RangeInt parseRangeInt(String str)
    {
        if (str == null)
        {
            return null;
        }
        else if (str.indexOf(45) >= 0)
        {
            String[] val1 = Config.tokenize(str, "-");

            if (val1.length != 2)
            {
                this.warn("Invalid range: " + str);
                return null;
            }
            else
            {
                int min = Config.parseInt(val1[0], -1);
                int max = Config.parseInt(val1[1], -1);

                if (min >= 0 && max >= 0)
                {
                    return new RangeInt(min, max);
                }
                else
                {
                    this.warn("Invalid range: " + str);
                    return null;
                }
            }
        }
        else
        {
            int val = Config.parseInt(str, -1);

            if (val < 0)
            {
                this.warn("Invalid integer: " + str);
                return null;
            }
            else
            {
                return new RangeInt(val, val);
            }
        }
    }

    public static boolean parseBoolean(String str)
    {
        return str == null ? false : str.toLowerCase().equals("true");
    }

    public static int parseColor(String str, int defVal)
    {
        if (str == null)
        {
            return defVal;
        }
        else
        {
            str = str.trim();

            try
            {
                int e = Integer.parseInt(str, 16) & 16777215;
                return e;
            }
            catch (NumberFormatException var3)
            {
                return defVal;
            }
        }
    }
}
