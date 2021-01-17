package net.minecraft.optifine;

import net.minecraft.block.state.BlockStateBase;

public class MatchBlock
{
    private int blockId = -1;
    private int[] metadatas = null;

    public MatchBlock(int blockId)
    {
        this.blockId = blockId;
    }

    public MatchBlock(int blockId, int[] metadatas)
    {
        this.blockId = blockId;
        this.metadatas = metadatas;
    }

    public int getBlockId()
    {
        return this.blockId;
    }

    public int[] getMetadatas()
    {
        return this.metadatas;
    }

    public boolean matches(BlockStateBase blockState)
    {
        if (blockState.getBlockId() != this.blockId)
        {
            return false;
        }
        else
        {
            if (this.metadatas != null)
            {
                boolean matchMetadata = false;
                int metadata = blockState.getMetadata();

                for (int i = 0; i < this.metadatas.length; ++i)
                {
                    int md = this.metadatas[i];

                    if (md == metadata)
                    {
                        matchMetadata = true;
                        break;
                    }
                }

                if (!matchMetadata)
                {
                    return false;
                }
            }

            return true;
        }
    }
}
