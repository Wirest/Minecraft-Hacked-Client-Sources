package net.minecraft.optifine;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class BlockUtils {
    private static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
    private static ReflectorMethod ForgeBlock_setLightOpacity = new ReflectorMethod(BlockUtils.ForgeBlock, "setLightOpacity");
    private static boolean directAccessValid = true;

    public static void setLightOpacity(Block block, int opacity) {
        if (BlockUtils.directAccessValid) {
            try {
                block.setLightOpacity(opacity);
                return;
            } catch (IllegalAccessError var3) {
                BlockUtils.directAccessValid = false;

                if (!BlockUtils.ForgeBlock_setLightOpacity.exists()) {
                    throw var3;
                }
            }
        }

        Reflector.callVoid(block, BlockUtils.ForgeBlock_setLightOpacity, new Object[]{Integer.valueOf(opacity)});
    }

    public static void place(BlockPos pos, EnumFacing face) {
        if(face == EnumFacing.UP){
            pos = pos.add(0, -1, 0);
        }else if(face == EnumFacing.NORTH){
            pos = pos.add(0, 0, 1);
        }else if(face == EnumFacing.EAST){
            pos = pos.add(-1, 0, 0);
        }else if(face == EnumFacing.SOUTH){
            pos = pos.add(0, 0, -1);
        }else if(face == EnumFacing.WEST){
            pos = pos.add(1, 0, 0);
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.inventoryContainer.getSlot(36 + mc.thePlayer.inventory.currentItem).getStack(), pos, face, new Vec3(0.5, 0.5, 0.5))) {
            mc.thePlayer.swingItem();
        }
    }
}
