package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.AntiBots;
import org.m0jang.crystal.Utils.RenderUtils;
import org.m0jang.crystal.Values.Value;

public class ESP extends Module {
   private static boolean players = true;
   public static Value mode = new Value("ESP", String.class, "Mode", "Outline", new String[]{"Outline", "Cylinder"});

   public ESP() {
      super("ESP", Category.Render, mode);
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(this.getName() + " \247f" + mode.getSelectedOption());
   }

   @EventTarget
   private void onRender3D(EventRender3D event) {
      Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Object o = var3.next();
         if (o instanceof EntityLivingBase && o != Minecraft.thePlayer) {
            EntityLivingBase entity = (EntityLivingBase)o;
            if (checkValidity(entity)) {
               double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks() - RenderManager.renderPosX;
               double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks() - RenderManager.renderPosY;
               double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks() - RenderManager.renderPosZ;
               boolean draw = true;
               if (Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() && ((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC(entity)) {
                  draw = false;
               }

               if (draw) {
                  if (entity.hurtTime > 0) {
                     if (mode.getStringValue().equals("Cylinder")) {
                        RenderUtils.drawWolframEntityESP(entity, (new Color(255, 102, 113)).getRGB(), posX, posY, posZ);
                     }
                  } else if (mode.getStringValue().equals("Cylinder")) {
                     RenderUtils.drawWolframEntityESP(entity, (new Color(186, 100, 200)).getRGB(), posX, posY, posZ);
                  }
               }
            }
         }
      }

   }

   private static boolean checkValidity(EntityLivingBase entity) {
      return entity instanceof EntityPlayer ? players : false;
   }

   public void renderOne() {
      GL11.glPushAttrib(1048575);
      GL11.glDisable(3008);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(3.6F);
      GL11.glEnable(2848);
      GL11.glEnable(2960);
      GL11.glClear(1024);
      GL11.glClearStencil(15);
      GL11.glStencilFunc(512, 1, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1028, 6913);
   }

   public void renderTwo() {
      GL11.glStencilFunc(512, 0, 15);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glPolygonMode(1028, 6914);
   }

   public void renderThree() {
      GL11.glStencilFunc(514, 1, 15);
      GL11.glStencilOp(7680, 7680, 7680);
      GL11.glPolygonMode(1028, 6913);
   }

   public void renderFour(Entity renderEntity) {
      float[] color;
      if (renderEntity instanceof EntityLivingBase) {
         EntityLivingBase distance = (EntityLivingBase)renderEntity;
         if (Crystal.INSTANCE.friendManager.isFriend(renderEntity.getName())) {
            color = new float[]{0.27F, 0.7F, 0.92F};
         } else {
            float distance1 = Minecraft.thePlayer.getDistanceToEntity(distance);
            if (distance1 <= 32.0F) {
               color = new float[]{1.0F, distance1 / 32.0F, 0.0F};
            } else {
               color = new float[]{0.0F, 0.9F, 0.0F};
            }
         }
      } else {
         float distance2 = Minecraft.thePlayer.getDistanceToEntity(renderEntity);
         if (distance2 <= 32.0F) {
            color = new float[]{1.0F, distance2 / 32.0F, 0.0F};
         } else {
            color = new float[]{0.0F, 0.9F, 0.0F};
         }
      }

      GlStateManager.color(color[0], color[1], color[2], 0.85F);
      this.renderFour();
   }

   private void renderFour() {
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(10754);
      GL11.glPolygonOffset(1.0F, -2000000.0F);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
   }

   public void renderFive() {
      GL11.glPolygonOffset(1.0F, 2000000.0F);
      GL11.glDisable(10754);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(2960);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glEnable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
      GL11.glEnable(3008);
      GL11.glPopAttrib();
   }
}
