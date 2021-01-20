package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventRightClick;
import cedo.modules.Module;
import cedo.modules.movement.Scaffold.BlockData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class ChestAssist extends Module {

    public ChestAssist() {
        super("ChestAssist", Keyboard.KEY_NONE, Category.PLAYER);
    }

    public static EnumFacing getFacing(BlockPos pos) {
        EnumFacing[] orderedValues;
        EnumFacing[] arrenumFacing = orderedValues = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN};
        int n = arrenumFacing.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing facing = arrenumFacing[n2];
            EntitySnowball temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
            temp.posX = (double) pos.getX() + 0.5;
            temp.posY = (double) pos.getY() + 0.5;
            temp.posZ = (double) pos.getZ() + 0.5;
            temp.posX += (double) facing.getDirectionVec().getX() * 0.5;
            temp.posY += (double) facing.getDirectionVec().getY() * 0.5;
            temp.posZ += (double) facing.getDirectionVec().getZ() * 0.5;
            if (Minecraft.getMinecraft().thePlayer.canEntityBeSeen(temp)) {
                return facing;
            }
            ++n2;
        }
        return null;
    }

    public void onEvent(Event e) { //TODO add a mode to manipulate packets rather than spam new ones
        if (e instanceof EventRightClick && e.isPre()) {
            if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.chest) {
                BlockPos pointedPos = mc.objectMouseOver != null ? mc.objectMouseOver.getBlockPos() : new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

                if (pointedPos == null)
                    return;

                int radius = 3;
                BlockPos closest = null;
                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            BlockPos pos = new BlockPos(pointedPos.getX() + x, pointedPos.getY() + y, pointedPos.getZ() + z);
                            Block block = mc.theWorld.getBlockState(pos).getBlock();

                            if (block == Blocks.chest) {
                                MovingObjectPosition hit = mc.theWorld.rayTraceBlocks(mc.thePlayer.getPositionVector(), new Vec3(pos.getX(), pos.getY(), pos.getZ()));
                                boolean rayTraced = true;//hit != null && hit.typeOfHit == MovingObjectType.BLOCK && mc.theWorld.getBlockState(hit.getBlockPos()).getBlock() == Blocks.chest;
                                if (closest == null && mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 4 && rayTraced) {
                                    closest = pos;
                                } else if (closest != null) {
                                    if (mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) < mc.thePlayer.getDistance(closest.getX(), closest.getY(), closest.getZ()) && rayTraced) {
                                        closest = pos;
                                    }
                                }
                            }
                        }
                    }
                }

                if (closest != null && !mc.thePlayer.isSneaking()) {

                    double deltaX = closest.getX() + 0.5 - mc.thePlayer.posX,
                            deltaY = closest.getY() + 0.5 - 3.5 + 0.5 - mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                            deltaZ = closest.getZ() + 0.5 - mc.thePlayer.posZ,
                            distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
                    float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ)) + (float) (Math.random() * 2) - 1,
                            pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance)) + (float) (Math.random() * 2) - 1;

                    final double v = Math.toDegrees(Math.atan(deltaZ / deltaX));
                    if (deltaX < 0 && deltaZ < 0) {
                        yaw = (float) (90 + v);
                    } else if (deltaX > 0 && deltaZ < 0) {
                        yaw = (float) (-90 + v);
                    }

                    BlockData blockData = new BlockData(closest, getFacing(closest));
                    //Logger.ingameInfo("attempted to click chest");
                    mc.thePlayer.sendQueue.addToSendQueue(new C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround));
                    e.setCancelled(true);
                    mc.thePlayer.swingItem();
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(blockData.position, mc.getRenderViewEntity().getHorizontalFacing().getIndex(), mc.thePlayer.getHeldItem(), (float) mc.thePlayer.posX, (float) mc.thePlayer.posY, (float) mc.thePlayer.posZ));
                }
            }
        }
    }
}
	