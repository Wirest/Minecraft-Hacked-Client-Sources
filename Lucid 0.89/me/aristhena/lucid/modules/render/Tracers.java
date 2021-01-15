package me.aristhena.lucid.modules.render;

import me.aristhena.lucid.management.module.*;
import me.aristhena.lucid.management.option.*;
import me.aristhena.lucid.eventapi.events.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.client.renderer.entity.*;
import me.aristhena.lucid.management.friend.*;
import org.lwjgl.opengl.*;
import java.util.*;
import me.aristhena.lucid.eventapi.*;

@Mod
public class Tracers extends Module
{
    private int state;
    private float r;
    private float g;
    private float b;
    @Op
    public boolean rainbow;
    @Op
    public boolean players;
    @Op
    public boolean monsters;
    @Op
    public boolean farmHunt;
    
    public Tracers() {
        this.r = 0.33f;
        this.g = 0.34f;
        this.b = 0.33f;
        this.players = true;
    }
    
    @EventTarget
    private void onRender(final Render3DEvent event) {
        GlStateManager.pushMatrix();
        final float[] rainbowArray = this.getRainbow();
        for (Object o : this.mc.theWorld.loadedEntityList) {
        	Entity ent = (Entity)o;
            if (!this.farmHunt) {
                if (!(ent instanceof EntityPlayer) && !(ent instanceof EntityMob)) {
                    continue;
                }
                if (ent instanceof EntityPlayer && !this.players) {
                    continue;
                }
                if (ent instanceof EntityMob && !this.monsters) {
                    continue;
                }
            }
            if ((!(ent instanceof EntityLivingBase) || ((EntityLivingBase)ent).getMaxHealth() <= 20.0f || ((EntityLivingBase)ent).isInvisible() || ent instanceof EntityHorse) && this.farmHunt) {
                continue;
            }
            if (!ent.isEntityAlive()) {
                continue;
            }
            final double x = this.getDiff(ent.lastTickPosX, ent.posX, event.partialTicks, RenderManager.renderPosX);
            final double y = this.getDiff(ent.lastTickPosY, ent.posY, event.partialTicks, RenderManager.renderPosY);
            final double z = this.getDiff(ent.lastTickPosZ, ent.posZ, event.partialTicks, RenderManager.renderPosZ);
            if (FriendManager.isFriend(ent.getCommandSenderName())) {
                GL11.glColor3f(0.27f, 0.7f, 0.92f);
                if (this.rainbow) {
                    GL11.glColor3f(rainbowArray[0], rainbowArray[1], rainbowArray[2]);
                }
            }
            else {
                final float distance = this.mc.thePlayer.getDistanceToEntity(ent);
                if (distance <= 32.0f) {
                    GL11.glColor3f(distance / 32.0f, 0.0f, 0.0f);
                }
                else {
                    GL11.glColor3f(0.9f, 0.0f, 0.0f);
                }
            }
            GL11.glLoadIdentity();
            final boolean bobbing = this.mc.gameSettings.viewBobbing;
            this.mc.gameSettings.viewBobbing = false;
            this.mc.entityRenderer.orientCamera(event.partialTicks);
            GL11.glLineWidth(1.2f);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, (double)this.mc.thePlayer.getEyeHeight(), 0.0);
            GL11.glVertex3d(x, y, z);
            GL11.glVertex3d(x, y + ent.getEyeHeight(), z);
            GL11.glEnd();
            this.mc.gameSettings.viewBobbing = bobbing;
        }
        GlStateManager.popMatrix();
    }
    
    private float[] getRainbow() {
        if (this.state == 0) {
            this.r += 0.01;
            this.b -= 0.01;
            if (this.r >= 0.9) {
                ++this.state;
            }
        }
        else if (this.state == 1) {
            this.g += 0.01;
            this.r -= 0.01;
            if (this.g >= 0.9) {
                ++this.state;
            }
        }
        else {
            this.b += 0.01;
            this.g -= 0.01;
            if (this.b >= 0.9) {
                this.state = 0;
            }
        }
        return new float[] { this.r, this.g, this.b };
    }
    
    private double getDiff(final double lastI, final double i, final float ticks, final double ownI) {
        return lastI + (i - lastI) * ticks - ownI;
    }
}
