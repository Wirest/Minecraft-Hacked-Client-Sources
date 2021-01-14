package shadersmod.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import optifine.Config;
import optifine.MatchBlock;

public class BlockAlias
{
    private int blockId;
    private MatchBlock[] matchBlocks;

    public BlockAlias(int blockId, MatchBlock[] matchBlocks)
    {
        this.blockId = blockId;
        this.matchBlocks = matchBlocks;
    }

    public int getBlockId()
    {
        return this.blockId;
    }

    public boolean matches(int id, int metadata)
    {
        for (int i = 0; i < this.matchBlocks.length; ++i)
        {
            MatchBlock matchblock = this.matchBlocks[i];

            if (matchblock.matches(id, metadata))
            {
                return true;
            }
        }

        return false;
    }

    public int[] getMatchBlockIds()
    {
        Set<Integer> set = new HashSet();

        for (int i = 0; i < this.matchBlocks.length; ++i)
        {
            MatchBlock matchblock = this.matchBlocks[i];
            int j = matchblock.getBlockId();
            set.add(Integer.valueOf(j));
        }

        Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
        int[] aint = Config.toPrimitive(ainteger);
        return aint;
    }

    public MatchBlock[] getMatchBlocks(int matchBlockId)
    {
        List<MatchBlock> list = new ArrayList();

        for (int i = 0; i < this.matchBlocks.length; ++i)
        {
            MatchBlock matchblock = this.matchBlocks[i];

            if (matchblock.getBlockId() == matchBlockId)
            {
                list.add(matchblock);
            }
        }

        MatchBlock[] amatchblock = (MatchBlock[])((MatchBlock[])list.toArray(new MatchBlock[list.size()]));
        return amatchblock;
    }

    public String toString()
    {
        return "block." + this.blockId + "=" + Config.arrayToString((Object[])this.matchBlocks);
    }
}
