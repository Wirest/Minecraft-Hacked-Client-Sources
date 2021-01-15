package me.aristhena.lucid.modules.render;

import me.aristhena.lucid.management.module.*;
import me.aristhena.lucid.management.option.*;
import me.aristhena.lucid.eventapi.*;
import me.aristhena.lucid.eventapi.events.*;
import me.aristhena.lucid.util.*;
import java.util.*;
import net.minecraft.entity.*;
import org.lwjgl.opengl.*;
import me.aristhena.lucid.modules.combat.*;
import me.aristhena.lucid.management.friend.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

@Mod
public class ESP extends Module
{
    @Op
    private static boolean players;
    @Op
    private static boolean monsters;
    @Op
    private static boolean animals;
    @Op
    private boolean outline;
    private int state;
    private float r;
    private float g;
    private float b;
    private static float[] rainbowArray;
    
    static {
        ESP.players = true;
    }
    
    public ESP() {
        this.r = 0.33f;
        this.g = 0.34f;
        this.b = 0.33f;
    }
    
    @EventTarget
    private void onPreUpdate(final UpdateEvent event) {
        if (event.state == Event.State.PRE) {
            ESP.rainbowArray = this.getRainbow();
        }
    }
    
    @EventTarget(4)
    private void onRender3D(final Render3DEvent event) {
        if (this.outline) {
            return;
        }
        for (final Object o : this.mc.theWorld.loadedEntityList) {
            if (o instanceof EntityLivingBase && o != this.mc.thePlayer) {
                final EntityLivingBase entity = (EntityLivingBase)o;
                int color = -16777216;
                int thingyt = 1174405120;
                if (entity.hurtTime != 0) {
                    color = -6750208;
                    thingyt = 1184432128;
                }
                if (!checkValidity(entity)) {
                    continue;
                }
                RenderUtils.drawEsp(entity, event.partialTicks, color, thingyt);
            }
        }
    }
    
    public static void renderOne(final Entity ent) {
        if (!checkValidity((EntityLivingBase)ent)) {
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
        colorLines(ent);
        GL11.glLineWidth(1.5f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }
    
    private static void colorLines(final Entity ent) {
        int color = 13762557;
        if (Aura.pseudoTarget == ent) {
            GL11.glColor4f(ESP.rainbowArray[0], ESP.rainbowArray[1], ESP.rainbowArray[2], 1.0f);
        }
        else {
            if (FriendManager.isFriend(ent.getCommandSenderName())) {
                color = 3394815;
            }
            color(color, 1.0f);
        }
    }
    
    private static void color(final int color, final float alpha) {
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void renderTwo(final Entity ent) {
        if (!checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }
    
    public static void renderThree(final Entity ent) {
        if (!checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderFour(final Entity ent) {
        if (!checkValidity((EntityLivingBase)ent)) {
            return;
        }
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }
    
    public static void renderFive(final Entity ent) {
        if (!checkValidity((EntityLivingBase)ent)) {
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
    
    private static boolean checkValidity(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return ESP.players;
        }
        return (ESP.monsters && entity instanceof EntityMob) || (ESP.animals && entity instanceof EntityAnimal) || (ESP.animals && entity instanceof EntityBat);
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
}
