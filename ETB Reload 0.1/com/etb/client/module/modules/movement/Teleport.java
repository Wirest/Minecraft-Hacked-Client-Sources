package com.etb.client.module.modules.movement;

import com.etb.client.event.events.input.MouseEvent;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.event.events.render.Render3DEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.RenderUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * made by oHare for ETB Reloaded
 *
 * @since 6/30/2019
 **/
public class Teleport extends Module {
    private boolean dispatchTeleport;

    public Teleport() {
        super("Teleport", Category.MOVEMENT, new Color(0xAA76FF).getRGB());
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            MovingObjectPosition ray = rayTrace(500.0D);
            if (ray == null) {
                return;
            }
            if (dispatchTeleport) {
                double x_new = ray.getBlockPos().getX() + 0.5D;
                double y_new = ray.getBlockPos().getY() + 1;
                double z_new = ray.getBlockPos().getZ() + 0.5D;
                double distance = mc.thePlayer.getDistance(x_new, y_new, z_new);
                for (double d = 0.0D; d < distance; d += 2.0D) {
                    setPos(mc.thePlayer.posX + (x_new - mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - mc.thePlayer.posX) * d / distance, mc.thePlayer.posY + (y_new - mc.thePlayer.posY) * d / distance, mc.thePlayer.posZ + (z_new - mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - mc.thePlayer.posZ) * d / distance);
                }
                setPos(x_new, y_new, z_new);
                mc.renderGlobal.loadRenderers();
                dispatchTeleport = false;
            }
        }
    }

    @Subscribe
    public void onMouse(MouseEvent event) {
        if (event.getButton() == 1) dispatchTeleport = true;
    }

    @Override
    public void onEnable() {
        dispatchTeleport = false;
    }

    public MovingObjectPosition rayTrace(double blockReachDistance) {
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 vec4 = mc.thePlayer.getLookVec();
        Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return mc.theWorld.rayTraceBlocks(vec3, vec5, !mc.thePlayer.isInWater(), false, false);
    }

    public void setPos(double x, double y, double z) {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        mc.thePlayer.setPosition(x, y, z);
    }
}
