package net.optifine.shaders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.src.Config;
import net.optifine.config.MatchBlock;

public class BlockAlias
{
    private int blockAliasId;
    private MatchBlock[] matchBlocks;

    public BlockAlias(int blockAliasId, MatchBlock[] matchBlocks)
    {
        this.blockAliasId = blockAliasId;
        this.matchBlocks = matchBlocks;
    }

    public int getBlockAliasId()
    {
        return this.blockAliasId;
    }

    public boolean matches(int id, int metadata)
    {
        for (MatchBlock matchblock : this.matchBlocks) {
            if (matchblock.matches(id, metadata)) {
                return true;
            }
        }

        return false;
    }

    public int[] getMatchBlockIds()
    {
        Set<Integer> set = new HashSet();

        for (MatchBlock matchblock : this.matchBlocks) {
            int j = matchblock.getBlockId();
            set.add(j);
        }

        Integer[] ainteger = set.toArray(new Integer[0]);
        int[] aint = Config.toPrimitive(ainteger);
        return aint;
    }

    public MatchBlock[] getMatchBlocks(int matchBlockId)
    {
        List<MatchBlock> list = new ArrayList();

        for (MatchBlock matchblock : this.matchBlocks) {
            if (matchblock.getBlockId() == matchBlockId) {
                list.add(matchblock);
            }
        }

        MatchBlock[] amatchblock = list.toArray(new MatchBlock[0]);
        return amatchblock;
    }

    public String toString()
    {
        return "block." + this.blockAliasId + "=" + Config.arrayToString(this.matchBlocks);
    }
}
