
package me.memewaredevs.client.module.player;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.EventCollide;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.movement.MovementUtils;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.function.Consumer;

public class Phase extends Module {
    public Phase(final String name, final int key, final Module.Category category) {
        super(name, key, category);
    }

    @Handler
    public Consumer<UpdateEvent> event = (event) -> {
        double multi = 1.139;
        mc.thePlayer.noClip = true;
        mc.thePlayer.motionY = 0;
        if (isInsideBlock()) {
            event.setGround(true);
            mc.thePlayer.motionY = mc.thePlayer.movementInput.isJumping() ? multi : mc.thePlayer.movementInput.isSneaking() ? -multi : 0;
            MovementUtils.setMoveSpeed(multi);
        }
        else if (mc.thePlayer.movementInput.isSneaking()) {
            PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 120, mc.thePlayer.posZ, true));
            PacketUtil.sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }
    };

    @Handler
    public Consumer<EventCollide> eventCollideConsumer = (event) -> {
        event.setBoundingBox(null);
    };

    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper)
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
