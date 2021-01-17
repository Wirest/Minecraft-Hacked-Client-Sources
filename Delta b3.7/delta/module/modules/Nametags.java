/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.render.EventRender3D
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package delta.module.modules;

import delta.Class47;
import delta.Class69;
import java.util.regex.Pattern;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.render.EventRender3D;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Nametags
extends Module {
    private Pattern pseudoPattern = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");

    private void renderNameTag(EntityPlayer entityPlayer, String string, float f) {
        String string2 = string;
        GL11.glPushMatrix();
        GL11.glTranslated((double)(entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)f - RenderManager.renderPosX), (double)(entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)f - RenderManager.renderPosY + (double)entityPlayer.getEyeHeight() + 0.55), (double)(entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)f - RenderManager.renderPosZ));
        GL11.glRotatef((float)(-RenderManager.instance.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)RenderManager.instance.playerViewX, (float)1.0f, (float)0.0f, (float)0.0f);
        float f2 = this.mc.thePlayer.getDistanceToEntity((Entity)entityPlayer) / 4.0f;
        if (f2 < 1.0f) {
            f2 = 1.0f;
        }
        float f3 = f2 / 100.0f;
        GL11.glScalef((float)(-f3), (float)(-f3), (float)f3);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        int n = Class69.develops$._commit(string2) / 2;
        Class47._divided()._paris((float)(-n) - 2.0f, -2.0, (float)n + 4.0f, (float)Class69.develops$._rwanda() + 2.0f, Integer.MIN_VALUE);
        Class69.develops$._college(string2, 1.0f + (float)(-n), 1.5, -1);
        GL11.glEnable((int)3008);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    @EventTarget
    public void onRender3D(EventRender3D eventRender3D) {
        for (Object e : this.mc.theWorld.loadedEntityList) {
            if (!(e instanceof EntityPlayer) || e instanceof EntityClientPlayerMP) continue;
            this.renderNameTag((EntityPlayer)e, ((EntityPlayer)e).getCommandSenderName(), eventRender3D.getPartialTicks());
        }
    }

    public Nametags() {
        super("Nametags", Category.Render);
        this.setDescription("Permet de voir le pseudo des autres joueurs clairement");
    }
}

