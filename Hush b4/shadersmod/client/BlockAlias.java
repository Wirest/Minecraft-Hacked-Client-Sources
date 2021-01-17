// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.MatchBlock;

public class BlockAlias
{
    private int blockId;
    private MatchBlock[] matchBlocks;
    
    public BlockAlias(final int blockId, final MatchBlock[] matchBlocks) {
        this.blockId = blockId;
        this.matchBlocks = matchBlocks;
    }
    
    public int getBlockId() {
        return this.blockId;
    }
    
    public boolean matches(final int id, final int metadata) {
        for (int i = 0; i < this.matchBlocks.length; ++i) {
            final MatchBlock matchblock = this.matchBlocks[i];
            if (matchblock.matches(id, metadata)) {
                return true;
            }
        }
        return false;
    }
    
    public int[] getMatchBlockIds() {
        final int[] aint = new int[this.matchBlocks.length];
        for (int i = 0; i < aint.length; ++i) {
            aint[i] = this.matchBlocks[i].getBlockId();
        }
        return aint;
    }
}
