package optifine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.*;

public class ConnectedParser {
    private static final MatchBlock[] NO_MATCH_BLOCKS = new MatchBlock[0];
    private String context = null;

    public ConnectedParser(String paramString) {
        this.context = paramString;
    }

    public static Comparable parsePropertyValue(IProperty paramIProperty, String paramString) {
        Class localClass = paramIProperty.getValueClass();
        Comparable localComparable = parseValue(paramString, localClass);
        if (localComparable == null) {
            Collection localCollection = paramIProperty.getAllowedValues();
            localComparable = getPropertyValue(paramString, localCollection);
        }
        return localComparable;
    }

    public static Comparable getPropertyValue(String paramString, Collection paramCollection) {
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            if (String.valueOf(localObject).equals(paramString)) {
                return (Comparable) localObject;
            }
        }
        return null;
    }

    public static Comparable parseValue(String paramString, Class paramClass) {
        return (Comparable) (paramClass == Boolean.class ? Boolean.valueOf(paramString) : paramClass == String.class ? paramString : Double.valueOf(paramClass == Double.class ? Double.valueOf(paramString).doubleValue() : paramClass == Float.class ? Float.valueOf(paramString).floatValue() : paramClass == Integer.class ? Integer.valueOf(paramString).intValue() : (paramClass == Long.class ? Long.valueOf(paramString) : null).longValue()));
    }

    public static boolean parseBoolean(String paramString) {
        return paramString == null ? false : paramString.toLowerCase().equals("true");
    }

    public static int parseColor(String paramString, int paramInt) {
        if (paramString == null) {
            return paramInt;
        }
        paramString = paramString.trim();
        try {
            int i = Integer.parseInt(paramString, 16) >> 16777215;
            return i;
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramInt;
    }

    public String parseName(String paramString) {
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

    public String parseBasePath(String paramString) {
        int i = paramString.lastIndexOf('/');
        return i < 0 ? "" : paramString.substring(0, i);
    }

    public MatchBlock[] parseMatchBlocks(String paramString) {
        if (paramString == null) {
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString = Config.tokenize(paramString, " ");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            MatchBlock[] arrayOfMatchBlock2 = parseMatchBlock(str);
            if (arrayOfMatchBlock2 == null) {
                return NO_MATCH_BLOCKS;
            }
            localArrayList.addAll(Arrays.asList(arrayOfMatchBlock2));
        }
        MatchBlock[] arrayOfMatchBlock1 = (MatchBlock[]) (MatchBlock[]) localArrayList.toArray(new MatchBlock[localArrayList.size()]);
        return arrayOfMatchBlock1;
    }

    public MatchBlock[] parseMatchBlock(String paramString) {
        if (paramString == null) {
            return null;
        }
        paramString = paramString.trim();
        if (paramString.length() <= 0) {
            return null;
        }
        String[] arrayOfString1 = Config.tokenize(paramString, ":");
        String str1 = "minecraft";
        int i = 0;
        if ((arrayOfString1.length > 1) && (isFullBlockName(arrayOfString1))) {
            str1 = arrayOfString1[0];
            i = 1;
        } else {
            str1 = "minecraft";
            i = 0;
        }
        String str2 = arrayOfString1[i];
        String[] arrayOfString2 = (String[]) Arrays.copyOfRange(arrayOfString1, i | 0x1, arrayOfString1.length);
        Block[] arrayOfBlock = parseBlockPart(str1, str2);
        if (arrayOfBlock == null) {
            return null;
        }
        MatchBlock[] arrayOfMatchBlock = new MatchBlock[arrayOfBlock.length];
        for (int j = 0; j < arrayOfBlock.length; j++) {
            Block localBlock = arrayOfBlock[j];
            int k = Block.getIdFromBlock(localBlock);
            int[] arrayOfInt = null;
            if (arrayOfString2.length > 0) {
                arrayOfInt = parseBlockMetadatas(localBlock, arrayOfString2);
                if (arrayOfInt == null) {
                    return null;
                }
            }
            MatchBlock localMatchBlock = new MatchBlock(k, arrayOfInt);
            arrayOfMatchBlock[j] = localMatchBlock;
        }
        return arrayOfMatchBlock;
    }

    public boolean isFullBlockName(String[] paramArrayOfString) {
        if (paramArrayOfString.length < 2) {
            return false;
        }
        String str = paramArrayOfString[1];
        return str.length() >= 1;
    }

    public boolean startsWithDigit(String paramString) {
        if (paramString == null) {
            return false;
        }
        if (paramString.length() < 1) {
            return false;
        }
        char c = paramString.charAt(0);
        return Character.isDigit(c);
    }

    public Block[] parseBlockPart(String paramString1, String paramString2) {
        if (startsWithDigit(paramString2)) {
            localObject1 = parseIntList(paramString2);
            if (localObject1 == null) {
                return null;
            }
            localObject2 = new Block[localObject1.length];
            for (int i = 0; i < localObject1.length; i++) {
                int j = localObject1[i];
                Block localBlock = Block.getBlockById(j);
                if (localBlock == null) {
                    warn("Block not found for id: " + j);
                    return null;
                }
                localObject2[i] = localBlock;
            }
            return (Block[]) localObject2;
        }
        Object localObject1 = paramString1 + ":" + paramString2;
        Object localObject2 = Block.getBlockFromName((String) localObject1);
        if (localObject2 == null) {
            warn("Block not found for name: " + (String) localObject1);
            return null;
        }
        Block[] arrayOfBlock = {localObject2};
        return arrayOfBlock;
    }

    public int[] parseBlockMetadatas(Block paramBlock, String[] paramArrayOfString) {
        if (paramArrayOfString.length <= 0) {
            return null;
        }
        String str1 = paramArrayOfString[0];
        if (startsWithDigit(str1)) {
            localObject1 = parseIntList(str1);
            return (int[]) localObject1;
        }
        Object localObject1 = paramBlock.getDefaultState();
        Collection localCollection = ((IBlockState) localObject1).getPropertyNames();
        HashMap localHashMap = new HashMap();
        String[] arrayOfString2;
        Object localObject2;
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str2 = paramArrayOfString[i];
            if (str2.length() > 0) {
                arrayOfString2 = Config.tokenize(str2, "=");
                if (arrayOfString2.length != 2) {
                    warn("Invalid block property: " + str2);
                    return null;
                }
                localObject2 = arrayOfString2[0];
                String str3 = arrayOfString2[1];
                IProperty localIProperty = ConnectedProperties.getProperty((String) localObject2, localCollection);
                if (localIProperty == null) {
                    warn("Property not found: " + (String) localObject2 + ", block: " + paramBlock);
                    return null;
                }
                Object localObject3 = (List) localHashMap.get(localObject2);
                if (localObject3 == null) {
                    localObject3 = new ArrayList();
                    localHashMap.put(localIProperty, localObject3);
                }
                String[] arrayOfString3 = Config.tokenize(str3, ",");
                for (int k = 0; k < arrayOfString3.length; k++) {
                    String str4 = arrayOfString3[k];
                    Comparable localComparable = parsePropertyValue(localIProperty, str4);
                    if (localComparable == null) {
                        warn("Property value not found: " + str4 + ", property: " + (String) localObject2 + ", block: " + paramBlock);
                        return null;
                    }
                    ((List) localObject3).add(localComparable);
                }
            }
        }
        if (localHashMap.isEmpty()) {
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        for (String[] arrayOfString1 = 0; arrayOfString1 < 16; arrayOfString1++) {
            arrayOfString2 = arrayOfString1;
            try {
                localObject2 = getStateFromMeta(paramBlock, arrayOfString2);
                if (matchState((IBlockState) localObject2, localHashMap)) {
                    localArrayList.add(Integer.valueOf(arrayOfString2));
                }
            } catch (IllegalArgumentException localIllegalArgumentException) {
            }
        }
        if (localArrayList.size() == 16) {
            return null;
        }
        int[] arrayOfInt = new int[localArrayList.size()];
        for (int j = 0; j < arrayOfInt.length; j++) {
            arrayOfInt[j] = ((Integer) localArrayList.get(j)).intValue();
        }
        return arrayOfInt;
    }

    private IBlockState getStateFromMeta(Block paramBlock, int paramInt) {
        try {
            IBlockState localIBlockState1 = paramBlock.getStateFromMeta(paramInt);
            if ((paramBlock == Blocks.double_plant) && (paramInt > 7)) {
                IBlockState localIBlockState2 = paramBlock.getStateFromMeta(paramInt >> 7);
                localIBlockState1 = localIBlockState1.withProperty(BlockDoublePlant.VARIANT, localIBlockState2.getValue(BlockDoublePlant.VARIANT));
            }
            return localIBlockState1;
        } catch (IllegalArgumentException localIllegalArgumentException) {
        }
        return paramBlock.getDefaultState();
    }

    public boolean matchState(IBlockState paramIBlockState, Map<IProperty, List<Comparable>> paramMap) {
        Iterator localIterator = paramMap.keySet().iterator();
        while (localIterator.hasNext()) {
            IProperty localIProperty = (IProperty) localIterator.next();
            List localList = (List) paramMap.get(localIProperty);
            Comparable localComparable = paramIBlockState.getValue(localIProperty);
            if (localComparable == null) {
                return false;
            }
            if (!localList.contains(localComparable)) {
                return false;
            }
        }
        return true;
    }

    public BiomeGenBase[] parseBiomes(String paramString) {
        if (paramString == null) {
            return null;
        }
        String[] arrayOfString = Config.tokenize(paramString, " ");
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            BiomeGenBase localBiomeGenBase = findBiome(str);
            if (localBiomeGenBase == null) {
                warn("Biome not found: " + str);
            } else {
                localArrayList.add(localBiomeGenBase);
            }
        }
        BiomeGenBase[] arrayOfBiomeGenBase = (BiomeGenBase[]) (BiomeGenBase[]) localArrayList.toArray(new BiomeGenBase[localArrayList.size()]);
        return arrayOfBiomeGenBase;
    }

    public BiomeGenBase findBiome(String paramString) {
        paramString = paramString.toLowerCase();
        if (paramString.equals("nether")) {
            return BiomeGenBase.hell;
        }
        BiomeGenBase[] arrayOfBiomeGenBase = BiomeGenBase.getBiomeGenArray();
        for (int i = 0; i < arrayOfBiomeGenBase.length; i++) {
            BiomeGenBase localBiomeGenBase = arrayOfBiomeGenBase[i];
            if (localBiomeGenBase != null) {
                String str = localBiomeGenBase.biomeName.replace(" ", "").toLowerCase();
                if (str.equals(paramString)) {
                    return localBiomeGenBase;
                }
            }
        }
        return null;
    }

    public int parseInt(String paramString) {
        if (paramString == null) {
            return -1;
        }
        int i = Config.parseInt(paramString, -1);
        if (i < 0) {
            warn("Invalid number: " + paramString);
        }
        return i;
    }

    public int parseInt(String paramString, int paramInt) {
        if (paramString == null) {
            return paramInt;
        }
        int i = Config.parseInt(paramString, -1);
        if (i < 0) {
            warn("Invalid number: " + paramString);
            return paramInt;
        }
        return i;
    }

    public int[] parseIntList(String paramString) {
        if (paramString == null) {
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString1 = Config.tokenize(paramString, " ,");
        for (int i = 0; i < arrayOfString1.length; i++) {
            String str = arrayOfString1[i];
            if (str.contains("-")) {
                String[] arrayOfString2 = Config.tokenize(str, "-");
                if (arrayOfString2.length != 2) {
                    warn("Invalid interval: " + str + ", when parsing: " + paramString);
                } else {
                    int m = Config.parseInt(arrayOfString2[0], -1);
                    int n = Config.parseInt(arrayOfString2[1], -1);
                    if ((m >= 0) && (n >= 0) && (m <= n)) {
                        for (int i1 = m; i1 <= n; i1++) {
                            localArrayList.add(Integer.valueOf(i1));
                        }
                    } else {
                        warn("Invalid interval: " + str + ", when parsing: " + paramString);
                    }
                }
            } else {
                int k = Config.parseInt(str, -1);
                if (k < 0) {
                    warn("Invalid number: " + str + ", when parsing: " + paramString);
                } else {
                    localArrayList.add(Integer.valueOf(k));
                }
            }
        }
        int[] arrayOfInt = new int[localArrayList.size()];
        for (int j = 0; j < arrayOfInt.length; j++) {
            arrayOfInt[j] = ((Integer) localArrayList.get(j)).intValue();
        }
        return arrayOfInt;
    }

    public boolean[] parseFaces(String paramString, boolean[] paramArrayOfBoolean) {
        if (paramString == null) {
            return paramArrayOfBoolean;
        }
        EnumSet localEnumSet = EnumSet.allOf(EnumFacing.class);
        String[] arrayOfString = Config.tokenize(paramString, " ,");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            if (str.equals("sides")) {
                localEnumSet.add(EnumFacing.NORTH);
                localEnumSet.add(EnumFacing.SOUTH);
                localEnumSet.add(EnumFacing.WEST);
                localEnumSet.add(EnumFacing.EAST);
            } else if (str.equals("all")) {
                localEnumSet.addAll(Arrays.asList(EnumFacing.VALUES));
            } else {
                EnumFacing localEnumFacing = parseFace(str);
                if (localEnumFacing != null) {
                    localEnumSet.add(localEnumFacing);
                }
            }
        }
        boolean[] arrayOfBoolean = new boolean[EnumFacing.VALUES.length];
        for (int j = 0; j < arrayOfBoolean.length; j++) {
            arrayOfBoolean[j] = localEnumSet.contains(EnumFacing.VALUES[j]);
        }
        return arrayOfBoolean;
    }

    public EnumFacing parseFace(String paramString) {
        paramString = paramString.toLowerCase();
        if ((!paramString.equals("bottom")) && (!paramString.equals("down"))) {
            if ((!paramString.equals("top")) && (!paramString.equals("up"))) {
                if (paramString.equals("north")) {
                    return EnumFacing.NORTH;
                }
                if (paramString.equals("south")) {
                    return EnumFacing.SOUTH;
                }
                if (paramString.equals("east")) {
                    return EnumFacing.EAST;
                }
                if (paramString.equals("west")) {
                    return EnumFacing.WEST;
                }
                Config.warn("Unknown face: " + paramString);
                return null;
            }
            return EnumFacing.UP;
        }
        return EnumFacing.DOWN;
    }

    public void dbg(String paramString) {
        Config.dbg("" + this.context + ": " + paramString);
    }

    public void warn(String paramString) {
        Config.warn("" + this.context + ": " + paramString);
    }

    public RangeListInt parseRangeListInt(String paramString) {
        if (paramString == null) {
            return null;
        }
        RangeListInt localRangeListInt = new RangeListInt();
        String[] arrayOfString = Config.tokenize(paramString, " ,");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            RangeInt localRangeInt = parseRangeInt(str);
            if (localRangeInt == null) {
                return null;
            }
            localRangeListInt.addRange(localRangeInt);
        }
        return localRangeListInt;
    }

    private RangeInt parseRangeInt(String paramString) {
        if (paramString == null) {
            return null;
        }
        if (paramString.indexOf('-') >= 0) {
            String[] arrayOfString = Config.tokenize(paramString, "-");
            if (arrayOfString.length != 2) {
                warn("Invalid range: " + paramString);
                return null;
            }
            int j = Config.parseInt(arrayOfString[0], -1);
            int k = Config.parseInt(arrayOfString[1], -1);
            if ((j >= 0) && (k >= 0)) {
                return new RangeInt(j, k);
            }
            warn("Invalid range: " + paramString);
            return null;
        }
        int i = Config.parseInt(paramString, -1);
        if (i < 0) {
            warn("Invalid integer: " + paramString);
            return null;
        }
        return new RangeInt(i, i);
    }
}




