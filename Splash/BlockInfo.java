package splash.utilities.player;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockInfo {
    public BlockInfo(BlockPos position, EnumFacing face) {
        this.position = position;
        this.face = face;
    }

    public EnumFacing face;
    public BlockPos position;
}
