package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityDaylightDetector extends TileEntity implements IUpdatePlayerListBox {
    private static final String __OBFID = "CL_00000350";

    /**
     * Updates the JList with a new model.
     */
    @Override
    public void update() {
        if (worldObj != null && !worldObj.isRemote && worldObj.getTotalWorldTime() % 20L == 0L) {
            blockType = getBlockType();

            if (blockType instanceof BlockDaylightDetector) {
                ((BlockDaylightDetector) blockType).func_180677_d(worldObj, pos);
            }
        }
    }
}
