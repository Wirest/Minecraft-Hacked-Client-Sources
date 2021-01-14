package shadersmod.client;

import optifine.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlockAliases {
    private static BlockAlias[][] blockAliases = (BlockAlias[][]) null;

    public static int getMappedBlockId(int paramInt1, int paramInt2) {
        if (blockAliases == null) {
            return paramInt1;
        }
        if ((paramInt1 >= 0) && (paramInt1 < blockAliases.length)) {
            BlockAlias[] arrayOfBlockAlias = blockAliases[paramInt1];
            if (arrayOfBlockAlias == null) {
                return paramInt1;
            }
            for (int i = 0; i < arrayOfBlockAlias.length; i++) {
                BlockAlias localBlockAlias = arrayOfBlockAlias[i];
                if (localBlockAlias.matches(paramInt1, paramInt2)) {
                    return localBlockAlias.getBlockId();
                }
            }
            return paramInt1;
        }
        return paramInt1;
    }

    public static void update(IShaderPack paramIShaderPack) {
        reset();
        String str1 = "/shaders/block.properties";
        try {
            InputStream localInputStream = paramIShaderPack.getResourceAsStream(str1);
            if (localInputStream == null) {
                return;
            }
            PropertiesOrdered localPropertiesOrdered = new PropertiesOrdered();
            localPropertiesOrdered.load(localInputStream);
            localInputStream.close();
            Config.dbg("[Shaders] Parsing block mappings: " + str1);
            ArrayList localArrayList = new ArrayList();
            ConnectedParser localConnectedParser = new ConnectedParser("Shaders");
            Iterator localIterator = localPropertiesOrdered.keySet().iterator();
            while (localIterator.hasNext()) {
                Object localObject = localIterator.next();
                String str2 = (String) localObject;
                String str3 = localPropertiesOrdered.getProperty(str2);
                String str4 = "block.";
                if (!str2.startsWith(str4)) {
                    Config.warn("[Shaders] Invalid block ID: " + str2);
                } else {
                    String str5 = StrUtils.removePrefix(str2, str4);
                    int i = Config.parseInt(str5, -1);
                    if (i < 0) {
                        Config.warn("[Shaders] Invalid block ID: " + str2);
                    } else {
                        MatchBlock[] arrayOfMatchBlock = localConnectedParser.parseMatchBlocks(str3);
                        if ((arrayOfMatchBlock != null) && (arrayOfMatchBlock.length >= 1)) {
                            BlockAlias localBlockAlias = new BlockAlias(i, arrayOfMatchBlock);
                            addToList(localArrayList, localBlockAlias);
                        } else {
                            Config.warn("[Shaders] Invalid block ID mapping: " + str2 + "=" + str3);
                        }
                    }
                }
            }
            if (localArrayList.size() <= 0) {
                return;
            }
            blockAliases = toArrays(localArrayList);
        } catch (IOException localIOException) {
            Config.warn("[Shaders] Error reading: " + str1);
        }
    }

    private static void addToList(List<List<BlockAlias>> paramList, BlockAlias paramBlockAlias) {
        int[] arrayOfInt = paramBlockAlias.getMatchBlockIds();
        for (int i = 0; i < arrayOfInt.length; i++) {
            int j = arrayOfInt[i];
            while (j >= paramList.size()) {
                paramList.add(null);
            }
            Object localObject = (List) paramList.get(j);
            if (localObject == null) {
                localObject = new ArrayList();
                paramList.set(j, localObject);
            }
            ((List) localObject).add(paramBlockAlias);
        }
    }

    private static BlockAlias[][] toArrays(List<List<BlockAlias>> paramList) {
        BlockAlias[][] arrayOfBlockAlias = new BlockAlias[paramList.size()][];
        for (int i = 0; i < arrayOfBlockAlias.length; i++) {
            List localList = (List) paramList.get(i);
            if (localList != null) {
                arrayOfBlockAlias[i] = ((BlockAlias[]) (BlockAlias[]) (BlockAlias[]) localList.toArray(new BlockAlias[localList.size()]));
            }
        }
        return arrayOfBlockAlias;
    }

    public static void reset() {
        blockAliases = (BlockAlias[][]) null;
    }
}




