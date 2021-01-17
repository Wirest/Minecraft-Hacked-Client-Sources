// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock
{
    private int blockId;
    private int[] metadatas;
    
    public MatchBlock(final int p_i63_1_) {
        this.blockId = -1;
        this.metadatas = null;
        this.blockId = p_i63_1_;
    }
    
    public MatchBlock(final int p_i64_1_, final int p_i64_2_) {
        this.blockId = -1;
        this.metadatas = null;
        this.blockId = p_i64_1_;
        if (p_i64_2_ >= 0 && p_i64_2_ <= 15) {
            this.metadatas = new int[] { p_i64_2_ };
        }
    }
    
    public MatchBlock(final int p_i65_1_, final int[] p_i65_2_) {
        this.blockId = -1;
        this.metadatas = null;
        this.blockId = p_i65_1_;
        this.metadatas = p_i65_2_;
    }
    
    public int getBlockId() {
        return this.blockId;
    }
    
    public int[] getMetadatas() {
        return this.metadatas;
    }
    
    public boolean matches(final BlockStateBase p_matches_1_) {
        return p_matches_1_.getBlockId() == this.blockId && Matches.metadata(p_matches_1_.getMetadata(), this.metadatas);
    }
    
    public boolean matches(final int p_matches_1_, final int p_matches_2_) {
        return p_matches_1_ == this.blockId && Matches.metadata(p_matches_2_, this.metadatas);
    }
    
    public void addMetadata(final int p_addMetadata_1_) {
        if (this.metadatas != null && p_addMetadata_1_ >= 0 && p_addMetadata_1_ <= 15) {
            for (int i = 0; i < this.metadatas.length; ++i) {
                if (this.metadatas[i] == p_addMetadata_1_) {
                    return;
                }
            }
            this.metadatas = Config.addIntToArray(this.metadatas, p_addMetadata_1_);
        }
    }
    
    @Override
    public String toString() {
        return this.blockId + ":" + Config.arrayToString(this.metadatas);
    }
}
