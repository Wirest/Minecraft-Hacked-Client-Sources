// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render;

import net.minecraft.client.renderer.OpenGlHelper;
import me.aristhena.client.friend.FriendManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import me.aristhena.utils.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.Render3DEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(shown = false)
public class ESP extends Module
{
    @Option.Op
    private boolean players;
    @Option.Op
    private boolean monsters;
    @Option.Op
    private boolean animals;
    @Option.Op
    private boolean outline;
    private int state;
    private float r;
    private float g;
    private float b;
    private static float[] rainbowArray;
    
    public ESP() {
        this.players = true;
        this.r = 0.33f;
        this.g = 0.34f;
        this.b = 0.33f;
    }
    
    @EventTarget
    private void onPreUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            ESP.rainbowArray = this.getRainbow();
        }
    }
    
    @EventTarget(0)
    private void onRender3D(final Render3DEvent event) {
        if (this.outline) {
            return;
        }
        for (final Object o : ClientUtils.world().loadedEntityList) {
            if (o instanceof EntityLivingBase && o != ClientUtils.mc().thePlayer) {
                final EntityLivingBase entity = (EntityLivingBase)o;
                int color = -13816531;
                int thingyt = 1177365805;
                if (entity.hurtTime != 0) {
                    color = -5054084;
                    thingyt = 1186128252;
                }
                if (!this.checkValidity(entity)) {
                    continue;
                }
                RenderUtils.drawEsp(entity, event.getPartialTicks(), color, thingyt);
            }
        }
    }
    
    public void renderOne(final Entity ent) {
        if (!this.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(0.2f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        this.colorLines(ent);
        GL11.glLineWidth(4.0f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }
    
    private void colorLines(final Entity ent) {
        int color = 13762557;
        if (FriendManager.isFriend(ent.getName())) {
            color = 3394815;
        }
        this.color(color, 1.0f);
    }
    
    private void color(final int color, final float alpha) {
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public void renderTwo(final Entity ent) {
        if (!this.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }
    
    public void renderThree(final Entity ent) {
        if (!this.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public void renderFour(final Entity ent) {
        if (!this.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }
    
    public void renderFive(final Entity ent) {
        if (!this.checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }
    
    private boolean checkValidity(final EntityLivingBase entity) {
        return true;
    }
    
    private float[] getRainbow() {
        if (this.state == 0) {
            this.r += 0.02;
            this.b -= 0.02;
            if (this.r >= 0.85) {
                ++this.state;
            }
        }
        else if (this.state == 1) {
            this.g += 0.02;
            this.r -= 0.02;
            if (this.g >= 0.85) {
                ++this.state;
            }
        }
        else {
            this.b += 0.02;
            this.g -= 0.02;
            if (this.b >= 0.85) {
                this.state = 0;
            }
        }
        return new float[] { this.r, this.g, this.b };
    }
    
    public boolean isOutline() {
        return this.outline;
    }
}
