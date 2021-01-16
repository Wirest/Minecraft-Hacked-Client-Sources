package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.AntiBots;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;
import org.m0jang.crystal.Utils.FriendManager;
import org.m0jang.crystal.Utils.ModeUtils;
import org.m0jang.crystal.Utils.RenderUtils;

public class Tracers extends Module {
   public Tracers() {
      super("Tracers", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   private double interpolate(double lastI, double i, float ticks, double ownI) {
      return lastI + (i - lastI) * (double)ticks - ownI;
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      boolean wasBobbing = Minecraft.gameSettings.viewBobbing;
      Iterator var4 = Minecraft.theWorld.loadedEntityList.iterator();

      while(true) {
         EntityLivingBase entity;
         double x;
         double y;
         double z;
         do {
            do {
               do {
                  do {
                     Object obj;
                     do {
                        if (!var4.hasNext()) {
                           Minecraft.gameSettings.viewBobbing = wasBobbing;
                           return;
                        }

                        obj = var4.next();
                     } while(!(obj instanceof EntityLivingBase));

                     entity = (EntityLivingBase)obj;
                  } while(entity == Minecraft.thePlayer);
               } while(!ModeUtils.isValidForTracers(entity));

               double var10001 = entity.lastTickPosX;
               double var10002 = entity.posX;
               float var10003 = event.getPartialTicks();
               this.mc.getRenderManager();
               x = this.interpolate(var10001, var10002, var10003, RenderManager.renderPosX);
               var10001 = entity.lastTickPosY;
               var10002 = entity.posY;
               var10003 = event.getPartialTicks();
               this.mc.getRenderManager();
               y = this.interpolate(var10001, var10002, var10003, RenderManager.renderPosY);
               var10001 = entity.lastTickPosZ;
               var10002 = entity.posZ;
               var10003 = event.getPartialTicks();
               this.mc.getRenderManager();
               z = this.interpolate(var10001, var10002, var10003, RenderManager.renderPosZ);
            } while(!(entity instanceof EntityPlayer));
         } while(Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() && ((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC(entity));

         GlStateManager.pushMatrix();
         GlStateManager.loadIdentity();
         Minecraft.gameSettings.viewBobbing = false;
         this.mc.entityRenderer.orientCamera(event.partialTicks);
         RenderUtils.enableGL3D(2.1F);
         float distance = Minecraft.thePlayer.getDistanceToEntity(entity);
         if (Crystal.INSTANCE.friendManager.isFriend(entity.getName()) || Aura.team.getBooleanValue() && FriendManager.isTeam(entity)) {
            GlStateManager.color(0.0F, 1.0F, 0.0F, 0.7F);
         } else if (distance <= 32.0F) {
            GlStateManager.color(2.0F, distance / 32.0F, 0.0F, 0.7F);
         } else {
            GlStateManager.color(0.0F, 0.9F, 0.0F, 0.7F);
         }

         GL11.glBegin(1);
         Minecraft.getMinecraft();
         GL11.glVertex3d(0.0D, (double)Minecraft.thePlayer.getEyeHeight(), 0.0D);
         GL11.glVertex3d(x, y + 1.0D, z);
         GL11.glEnd();
         RenderUtils.disableGL3D();
         GlStateManager.popMatrix();
      }
   }
}
