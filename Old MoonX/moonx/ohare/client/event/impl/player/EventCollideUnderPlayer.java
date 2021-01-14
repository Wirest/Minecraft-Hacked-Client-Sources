package moonx.ohare.client.event.impl.player;

import moonx.ohare.client.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.List;

public class EventCollideUnderPlayer extends Event
{
    private Block block;
    private BlockPos.MutableBlockPos blockPos;
    private List<AxisAlignedBB> list;

    public EventCollideUnderPlayer(final BlockPos.MutableBlockPos pos, Block block, final List<AxisAlignedBB> bList) {
        this.block = block;
        this.blockPos = pos;
        this.list = bList;
    }

    public Block getBlock() {
        return block;
    }

    public BlockPos.MutableBlockPos getBlockPos() {
        return blockPos;
    }

    public List<AxisAlignedBB> getList() {
        return list;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }

    public void setBlockPos(final BlockPos.MutableBlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setList(final List<AxisAlignedBB> boundingBox) {
        this.list = boundingBox;
    }
}
