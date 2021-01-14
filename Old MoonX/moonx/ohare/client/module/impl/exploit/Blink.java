package moonx.ohare.client.module.impl.exploit;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.ArrayList;

public class Blink extends Module {
    private ArrayList<Packet> packets = new ArrayList<>();
    private ArrayList<Vector3d> locations = new ArrayList<>();
    private Vector3d startVector;
    public Blink() {
        super("Blink", Category.EXPLOITS, new Color(246, 255, 146).getRGB());
        setDescription("Cancel all packets and then send them on disable");
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (getMc().thePlayer == null || !event.isSending()) return;
        if (hasMoved()) {
            locations.add(new Vector3d(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ));
        }
        packets.add(event.getPacket());
        event.setCanceled(true);
    }

    @Handler
    public void onRender(Render3DEvent event) {
        if (getMc().thePlayer == null) return;
        if (!locations.isEmpty()) {
            GL11.glPushMatrix();
            GL11.glLineWidth(3);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor3d(1, 1, 1);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            locations.forEach(vector -> {
				GL11.glVertex3d(vector.x - getMc().getRenderManager().getRenderPosX(),
                        vector.y - getMc().getRenderManager().getRenderPosY(),
                        vector.z - getMc().getRenderManager().getRenderPosZ());
            });
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void onEnable() {
        if (getMc().theWorld == null) return;
        startVector = new Vector3d(getMc().thePlayer.posX, getMc().thePlayer.posY, getMc().thePlayer.posZ);
        final EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(getMc().theWorld, getMc().thePlayer.getGameProfile());
        entityOtherPlayerMP.inventory = getMc().thePlayer.inventory;
        entityOtherPlayerMP.inventoryContainer = getMc().thePlayer.inventoryContainer;
        entityOtherPlayerMP.setPositionAndRotation(getMc().thePlayer.posX, getMc().thePlayer.getEntityBoundingBox().minY, getMc().thePlayer.posZ, getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch);
        entityOtherPlayerMP.rotationYawHead = getMc().thePlayer.rotationYawHead;
        entityOtherPlayerMP.setSneaking(getMc().thePlayer.isSneaking());
        getMc().theWorld.addEntityToWorld(-13376969, entityOtherPlayerMP);
        packets.clear();
    }

    @Override
    public void onDisable() {
        if (getMc().theWorld == null) return;
        getMc().theWorld.removeEntityFromWorld(-13376969);
        packets.forEach(getMc().thePlayer.sendQueue.getNetworkManager()::sendPacket);
        packets.clear();
        locations.clear();
    }

    private boolean hasMoved() {
        return getMc().thePlayer.posX != getMc().thePlayer.prevPosX || getMc().thePlayer.posY != getMc().thePlayer.prevPosY || getMc().thePlayer.posZ != getMc().thePlayer.prevPosZ;
    }
}