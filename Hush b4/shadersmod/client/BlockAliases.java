// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.MatchBlock;
import java.util.Iterator;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import optifine.StrUtils;
import optifine.ConnectedParser;
import java.util.List;
import java.util.ArrayList;
import optifine.Config;
import optifine.PropertiesOrdered;

public class BlockAliases
{
    private static BlockAlias[][] blockAliases;
    
    static {
        BlockAliases.blockAliases = null;
    }
    
    public static int getMappedBlockId(final int blockId, final int metadata) {
        if (BlockAliases.blockAliases == null) {
            return blockId;
        }
        if (blockId < 0 || blockId >= BlockAliases.blockAliases.length) {
            return blockId;
        }
        final BlockAlias[] ablockalias = BlockAliases.blockAliases[blockId];
        if (ablockalias == null) {
            return blockId;
        }
        for (int i = 0; i < ablockalias.length; ++i) {
            final BlockAlias blockalias = ablockalias[i];
            if (blockalias.matches(blockId, metadata)) {
                return blockalias.getBlockId();
            }
        }
        return blockId;
    }
    
    public static void update(final IShaderPack shaderPack) {
        reset();
        final String s = "/shaders/block.properties";
        try {
            final InputStream inputstream = shaderPack.getResourceAsStream(s);
            if (inputstream == null) {
                return;
            }
            final Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            Config.dbg("[Shaders] Parsing block mappings: " + s);
            final List<List<BlockAlias>> list = new ArrayList<List<BlockAlias>>();
            final ConnectedParser connectedparser = new ConnectedParser("Shaders");
            for (final Object s2 : properties.keySet()) {
                final String s3 = (String)s2;
                final String s4 = properties.getProperty(s3);
                final String s5 = "block.";
                if (!s3.startsWith(s5)) {
                    Config.warn("[Shaders] Invalid block ID: " + s3);
                }
                else {
                    final String s6 = StrUtils.removePrefix(s3, s5);
                    final int i = Config.parseInt(s6, -1);
                    if (i < 0) {
                        Config.warn("[Shaders] Invalid block ID: " + s3);
                    }
                    else {
                        final MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s4);
                        if (amatchblock != null && amatchblock.length >= 1) {
                            final BlockAlias blockalias = new BlockAlias(i, amatchblock);
                            addToList(list, blockalias);
                        }
                        else {
                            Config.warn("[Shaders] Invalid block ID mapping: " + s3 + "=" + s4);
                        }
                    }
                }
            }
            if (list.size() <= 0) {
                return;
            }
            BlockAliases.blockAliases = toArrays(list);
        }
        catch (IOException var15) {
            Config.warn("[Shaders] Error reading: " + s);
        }
    }
    
    private static void addToList(final List<List<BlockAlias>> blocksAliases, final BlockAlias ba) {
        final int[] aint = ba.getMatchBlockIds();
        for (int i = 0; i < aint.length; ++i) {
            final int j = aint[i];
            while (j >= blocksAliases.size()) {
                blocksAliases.add(null);
            }
            List<BlockAlias> list = blocksAliases.get(j);
            if (list == null) {
                list = new ArrayList<BlockAlias>();
                blocksAliases.set(j, list);
            }
            list.add(ba);
        }
    }
    
    private static BlockAlias[][] toArrays(final List<List<BlockAlias>> listBlocksAliases) {
        final BlockAlias[][] ablockalias = new BlockAlias[listBlocksAliases.size()][];
        for (int i = 0; i < ablockalias.length; ++i) {
            final List<BlockAlias> list = listBlocksAliases.get(i);
            if (list != null) {
                ablockalias[i] = list.toArray(new BlockAlias[list.size()]);
            }
        }
        return ablockalias;
    }
    
    public static void reset() {
        BlockAliases.blockAliases = null;
    }
}
