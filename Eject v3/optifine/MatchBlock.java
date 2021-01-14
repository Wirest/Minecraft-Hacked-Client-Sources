package optifine;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock {
    private int blockId = -1;
    private int[] metadatas = null;

    public MatchBlock(int paramInt) {
        this.blockId = paramInt;
    }

    public MatchBlock(int paramInt1, int paramInt2) {
        this.blockId = paramInt1;
        if ((paramInt2 >= 0) && (paramInt2 <= 15)) {
            this.metadatas = new int[]{paramInt2};
        }
    }

    public MatchBlock(int paramInt, int[] paramArrayOfInt) {
        this.blockId = paramInt;
        this.metadatas = paramArrayOfInt;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public int[] getMetadatas() {
        return this.metadatas;
    }

    public boolean matches(BlockStateBase paramBlockStateBase) {
        return paramBlockStateBase.getBlockId() != this.blockId ? false : Matches.metadata(paramBlockStateBase.getMetadata(), this.metadatas);
    }

    public boolean matches(int paramInt1, int paramInt2) {
        return paramInt1 != this.blockId ? false : Matches.metadata(paramInt2, this.metadatas);
    }

    public void addMetadata(int paramInt) {
        if ((this.metadatas != null) && (paramInt >= 0) && (paramInt <= 15)) {
            for (int i = 0; i < this.metadatas.length; i++) {
                if (this.metadatas[i] == paramInt) {
                    return;
                }
            }
            this.metadatas = Config.addIntToArray(this.metadatas, paramInt);
        }
    }

    public String toString() {
        return "" + this.blockId + ":" + Config.arrayToString(this.metadatas);
    }
}




