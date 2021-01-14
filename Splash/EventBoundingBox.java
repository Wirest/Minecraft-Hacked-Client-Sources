package splash.client.events.player;

import me.hippo.systems.lwjeb.event.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBoundingBox extends Cancelable {
    private Block block;
    private BlockPos blockPos;
    private AxisAlignedBB boundingBox;
    private boolean isCollided;

    public EventBoundingBox(Block block, BlockPos pos, AxisAlignedBB boundingBox, boolean b) {
        this.block = block;
        this.blockPos = pos;
        this.boundingBox = boundingBox;
        this.isCollided = b;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    public boolean isCollided() {
        return isCollided;
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }
}
