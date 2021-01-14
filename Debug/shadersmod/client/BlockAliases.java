package shadersmod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.MatchBlock;
import optifine.PropertiesOrdered;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.StrUtils;

public class BlockAliases
{
    private static BlockAlias[][] blockAliases = (BlockAlias[][])null;
    private static boolean updateOnResourcesReloaded;

    public static int getMappedBlockId(int blockId, int metadata)
    {
        if (blockAliases == null)
        {
            return blockId;
        }
        else if (blockId >= 0 && blockId < blockAliases.length)
        {
            BlockAlias[] ablockalias = blockAliases[blockId];

            if (ablockalias == null)
            {
                return blockId;
            }
            else
            {
                for (int i = 0; i < ablockalias.length; ++i)
                {
                    BlockAlias blockalias = ablockalias[i];

                    if (blockalias.matches(blockId, metadata))
                    {
                        return blockalias.getBlockId();
                    }
                }

                return blockId;
            }
        }
        else
        {
            return blockId;
        }
    }

    public static void resourcesReloaded()
    {
        if (updateOnResourcesReloaded)
        {
            updateOnResourcesReloaded = false;
            update(Shaders.shaderPack);
        }
    }

    public static void update(IShaderPack shaderPack)
    {
        reset();

        if (shaderPack != null)
        {
            if (Reflector.Loader_getActiveModList.exists() && Minecraft.getMinecraft().getResourcePackRepository() == null)
            {
                Config.dbg("[Shaders] Delayed loading of block mappings after resources are loaded");
                updateOnResourcesReloaded = true;
            }
            else
            {
                List<List<BlockAlias>> list = new ArrayList();
                String s = "/shaders/block.properties";
                InputStream inputstream = shaderPack.getResourceAsStream(s);

                if (inputstream != null)
                {
                    loadBlockAliases(inputstream, s, list);
                }

                loadModBlockAliases(list);

                if (((List)list).size() > 0)
                {
                    blockAliases = toArrays(list);
                }
            }
        }
    }

    private static void loadModBlockAliases(List<List<BlockAlias>> listBlockAliases)
    {
        String[] astring = ReflectorForge.getForgeModIds();

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];

            try
            {
                ResourceLocation resourcelocation = new ResourceLocation(s, "shaders/block.properties");
                InputStream inputstream = Config.getResourceStream(resourcelocation);
                loadBlockAliases(inputstream, resourcelocation.toString(), listBlockAliases);
            }
            catch (IOException var6)
            {
                ;
            }
        }
    }

    public static void loadBlockAliases(InputStream in, String path, List<List<BlockAlias>> listBlockAliases) {
        if(in != null) {
           try {
              PropertiesOrdered e = new PropertiesOrdered();
              e.load(in);
              in.close();
              Config.dbg("[Shaders] Parsing block mappings: " + path);
              ConnectedParser cp = new ConnectedParser("Shaders");
              Set keys = e.keySet();
              Iterator it = keys.iterator();

              while(it.hasNext()) {
                 String key = (String)it.next();
                 String val = e.getProperty(key);
                 String prefix = "block.";
                 if(!key.startsWith(prefix)) {
                    Config.warn("[Shaders] Invalid block ID: " + key);
                 } else {
                    String blockIdStr = StrUtils.removePrefix(key, prefix);
                    int blockId = Config.parseInt(blockIdStr, -1);
                    if(blockId < 0) {
                       Config.warn("[Shaders] Invalid block ID: " + key);
                    } else {
                       MatchBlock[] matchBlocks = cp.parseMatchBlocks(val);
                       if(matchBlocks != null && matchBlocks.length >= 1) {
                          BlockAlias ba = new BlockAlias(blockId, matchBlocks);
                          addToList(listBlockAliases, ba);
                       } else {
                          Config.warn("[Shaders] Invalid block ID mapping: " + key + "=" + val);
                       }
                    }
                 }
              }
           } catch (IOException var14) {
              Config.warn("[Shaders] Error reading: " + path);
           }

        }
     }

    private static void addToList(List<List<BlockAlias>> blocksAliases, BlockAlias ba)
    {
        int[] aint = ba.getMatchBlockIds();

        for (int i = 0; i < aint.length; ++i)
        {
            int j = aint[i];

            while (j >= blocksAliases.size())
            {
                blocksAliases.add(null);
            }

            List<BlockAlias> list = (List)blocksAliases.get(j);

            if (list == null)
            {
                list = new ArrayList();
                blocksAliases.set(j, list);
            }

            BlockAlias blockalias = new BlockAlias(ba.getBlockId(), ba.getMatchBlocks(j));
            list.add(blockalias);
        }
    }

    private static BlockAlias[][] toArrays(List<List<BlockAlias>> listBlocksAliases)
    {
        BlockAlias[][] ablockalias = new BlockAlias[listBlocksAliases.size()][];

        for (int i = 0; i < ablockalias.length; ++i)
        {
            List<BlockAlias> list = (List)listBlocksAliases.get(i);

            if (list != null)
            {
                ablockalias[i] = (BlockAlias[])((BlockAlias[])list.toArray(new BlockAlias[list.size()]));
            }
        }

        return ablockalias;
    }

    public static void reset()
    {
        blockAliases = (BlockAlias[][])null;
    }
}
