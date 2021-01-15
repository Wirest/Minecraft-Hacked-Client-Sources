// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.events.MoveEvent;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.events.EventPacketSent;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.renderer.entity.RenderManager;
import me.CheerioFX.FusionX.utils.R2DUtils;
import org.lwjgl.opengl.GL11;
import me.CheerioFX.FusionX.utils.Wrapper;
import me.CheerioFX.FusionX.events.Event3D;
import java.util.concurrent.CopyOnWriteArrayList;
import me.CheerioFX.FusionX.module.Category;
import java.util.ArrayList;
import net.minecraft.network.Packet;
import java.util.List;
import me.CheerioFX.FusionX.module.Module;

public class Blink extends Module
{
    Timer timer;
    static int count;
    private final List<Packet> packets;
    private final List<double[]> positions;
    public static ArrayList<double[]> positionsList;
    private double[] startingPosition;
    
    static {
        Blink.count = 0;
        Blink.positionsList = new ArrayList<double[]>();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    public Blink() {
        super("Blink", 48, Category.PLAYER);
        this.timer = new Timer();
        this.packets = new CopyOnWriteArrayList<Packet>();
        this.positions = new ArrayList<double[]>();
    }
    
    @EventTarget
    public void onRender(final Event3D e) {
        this.positions.add(new double[] { Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ });
        GL11.glPushMatrix();
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        R2DUtils.glColor(-1862314667);
        GL11.glBegin(3);
        GL11.glVertex3d(this.startingPosition[0] - RenderManager.renderPosX, this.startingPosition[1] - RenderManager.renderPosY, this.startingPosition[2] - RenderManager.renderPosZ);
        GL11.glVertex3d(this.startingPosition[0] - RenderManager.renderPosX, this.startingPosition[1] + Wrapper.mc.thePlayer.height - RenderManager.renderPosY, this.startingPosition[2] - RenderManager.renderPosZ);
        R2DUtils.glColor(-1711276033);
        for (final double[] position : this.positions) {
            final double x = position[0] - RenderManager.renderPosX;
            final double y = position[1] - RenderManager.renderPosY;
            final double z = position[2] - RenderManager.renderPosZ;
            GL11.glVertex3d(x, y, z);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    @EventTarget
    public void onPacketSent(final EventPacketSent event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            if (Wrapper.getPlayer().movementInput.moveForward != 0.0f || Wrapper.getPlayer().movementInput.moveStrafe != 0.0f) {
                this.packets.add(event.getPacket());
            }
            event.setCancelled(true);
        }
        else if (event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C09PacketHeldItemChange || event.getPacket() instanceof C02PacketUseEntity) {
            this.packets.add(event.getPacket());
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onMove(final MoveEvent event) {
        ++Blink.count;
        if (Blink.count >= 50) {
            Blink.count = 0;
            if (Blink.positionsList.size() > 5) {
                Blink.positionsList.remove(0);
            }
        }
        for (final Object o : Wrapper.getWorld().playerEntities) {
            if (o instanceof EntityPlayer) {
                final EntityPlayer player1 = (EntityPlayer)o;
                final boolean shouldBreadCrumb = player1 == Wrapper.getPlayer() && (Wrapper.getPlayer().movementInput.moveForward != 0.0f || Wrapper.getPlayer().movementInput.moveStrafe != 0.0f);
                if (!shouldBreadCrumb) {
                    continue;
                }
                final double x = RenderManager.renderPosX;
                final double y = RenderManager.renderPosY;
                final double z = RenderManager.renderPosZ;
                Blink.positionsList.add(new double[] { x, y - player1.height, z });
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        for (final Packet packet : this.packets) {
            Wrapper.getMinecraft().getNetHandler().addToSendQueue(packet);
        }
        this.packets.clear();
        this.positions.clear();
        Blink.positionsList.clear();
    }
    
    public static double posit(final double val) {
        return (val < 0.0) ? (val * -1.0) : ((val == 0.0) ? val : val);
    }
    
    @Override
    public void toggleModule() {
        super.toggleModule();
        if (this.getState()) {
            this.startingPosition = new double[] { Wrapper.mc.thePlayer.posX, Wrapper.mc.thePlayer.posY, Wrapper.mc.thePlayer.posZ };
        }
    }
}
