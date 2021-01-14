package com.etb.client.module.modules.player;

import com.etb.client.event.events.render.Render3DEvent;
import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.ArrayList;

public class Blink extends Module {
    private ArrayList<Packet> packets = new ArrayList<>();
    private ArrayList<Vector3d> locations = new ArrayList<>();
    private Vector3d startVector;
    public Blink() {
        super("Blink", Category.PLAYER, new Color(246, 255, 146).getRGB());
        setDescription("Cancel all packets and then send them on disable");
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer == null || !event.isSending()) return;
        if (hasMoved()) {
            locations.add(new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }
        packets.add(event.getPacket());
        event.setCanceled(true);
    }

    @Subscribe
    public void onRender(Render3DEvent event) {
        if (mc.thePlayer == null) return;
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
            for (Vector3d vector : locations) {
                GL11.glVertex3d(vector.x - mc.getRenderManager().renderPosX,
                        vector.y - mc.getRenderManager().renderPosY,
                        vector.z - mc.getRenderManager().renderPosZ);
            }
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
        if (mc.theWorld == null) return;
        startVector = new Vector3d(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        final EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        entityOtherPlayerMP.inventory = mc.thePlayer.inventory;
        entityOtherPlayerMP.inventoryContainer = mc.thePlayer.inventoryContainer;
        entityOtherPlayerMP.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        entityOtherPlayerMP.rotationYawHead = mc.thePlayer.rotationYawHead;
        entityOtherPlayerMP.setSneaking(mc.thePlayer.isSneaking());
        mc.theWorld.addEntityToWorld(-13376969, entityOtherPlayerMP);
        packets.clear();
    }

    @Override
    public void onDisable() {
        if (mc.theWorld == null) return;
        mc.theWorld.removeEntityFromWorld(-13376969);
        packets.forEach(mc.thePlayer.sendQueue.getNetworkManager()::sendPacket);
        packets.clear();
        locations.clear();
    }

    private boolean hasMoved() {
        return mc.thePlayer.posX != mc.thePlayer.prevPosX || mc.thePlayer.posY != mc.thePlayer.prevPosY || mc.thePlayer.posZ != mc.thePlayer.prevPosZ;
    }
}