// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import me.aristhena.event.EventTarget;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import me.aristhena.client.friend.FriendManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import me.aristhena.event.events.Render3DEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(shown = false)
public class Tracers extends Module
{
    private int state;
    private float r;
    private float g;
    private float b;
    @Option.Op
    public boolean players;
    @Option.Op
    public boolean monsters;
    @Option.Op
    public boolean farmHunt;
    @Option.Op
    public boolean rainbow;
    
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
        for (Object o : ClientUtils.world().loadedEntityList) {
        	Entity ent = (Entity)o;
            if (ent == ClientUtils.mc().thePlayer) {
                continue;
            }
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
            final double x = this.getDiff(ent.lastTickPosX, ent.posX, event.getPartialTicks(), RenderManager.renderPosX);
            final double y = this.getDiff(ent.lastTickPosY, ent.posY, event.getPartialTicks(), RenderManager.renderPosY);
            final double z = this.getDiff(ent.lastTickPosZ, ent.posZ, event.getPartialTicks(), RenderManager.renderPosZ);
            if (FriendManager.isFriend(ent.getName())) {
                if (this.rainbow) {
                    GL11.glColor3f(rainbowArray[0], rainbowArray[1], rainbowArray[2]);
                }
            }
            else {
                final float distance = ClientUtils.mc().thePlayer.getDistanceToEntity(ent);
                if (distance <= 32.0f) {
                    GL11.glColor3f(distance / 32.0f, 0.0f, 0.0f);
                }
                else {
                    GL11.glColor3f(0.9f, 0.0f, 0.0f);
                }
            }
            GL11.glLoadIdentity();
            final boolean bobbing = ClientUtils.mc().gameSettings.viewBobbing;
            ClientUtils.mc().gameSettings.viewBobbing = false;
            ClientUtils.mc().entityRenderer.orientCamera(event.getPartialTicks());
            GL11.glLineWidth(1.2f);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, (double)ClientUtils.mc().thePlayer.getEyeHeight(), 0.0);
            GL11.glVertex3d(x, y, z);
            GL11.glVertex3d(x, y + ent.getEyeHeight(), z);
            GL11.glEnd();
            ClientUtils.mc().gameSettings.viewBobbing = bobbing;
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
