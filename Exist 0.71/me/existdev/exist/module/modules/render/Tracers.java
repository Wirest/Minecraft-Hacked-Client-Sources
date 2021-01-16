package me.existdev.exist.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import me.existdev.exist.Exist;
import me.existdev.exist.events.EventRender3D;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.render.ESP;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
   // $FF: synthetic field
   public ArrayList ents = new ArrayList();

   // $FF: synthetic method
   public Tracers() {
      super("Tracers", 0, Module.Category.Render);
      Exist.settingManager.addSetting(new Setting(this, "Players", true));
      Exist.settingManager.addSetting(new Setting(this, "Monsters", true));
      Exist.settingManager.addSetting(new Setting(this, "Animals", true));
      Exist.settingManager.addSetting(new Setting(this, "Invisibles", true));
   }

   // $FF: synthetic method
   @EventTarget
   public void onRender3D(EventRender3D e) {
      if(this.isToggled()) {
         if(this.ents.size() > 50) {
            this.ents.clear();
         }

         Minecraft var10000 = mc;
         Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Object entity = var3.next();
            Entity posX = (Entity)entity;
            if(this.ents.size() > 50) {
               break;
            }

            if(Exist.settingManager.getSetting(this, "Animals").getBooleanValue() && posX instanceof EntityAnimal && !this.ents.contains(posX)) {
               this.ents.add(posX);
            }

            if(Exist.settingManager.getSetting(this, "Players").getBooleanValue() && posX instanceof EntityPlayer && !this.ents.contains(posX) && !(posX instanceof EntityPlayerSP)) {
               this.ents.add(posX);
            }

            if(Exist.settingManager.getSetting(this, "Monsters").getBooleanValue() && posX instanceof EntityMob && !this.ents.contains(posX)) {
               this.ents.add(posX);
            }

            if(!Exist.settingManager.getSetting(this, "Animals").getBooleanValue() && this.ents.contains(posX) && posX instanceof EntityAnimal) {
               this.ents.remove(posX);
            }

            if(!Exist.settingManager.getSetting(this, "Players").getBooleanValue() && this.ents.contains(posX) && posX instanceof EntityPlayer) {
               this.ents.remove(posX);
            }

            if(!Exist.settingManager.getSetting(this, "Monsters").getBooleanValue() && this.ents.contains(posX) && posX instanceof EntityMob) {
               this.ents.remove(posX);
            }

            if(this.ents.contains(posX) && posX.isInvisible() && !Exist.settingManager.getSetting(this, "Invisibles").getBooleanValue()) {
               this.ents.remove(posX);
            }
         }

         var10000 = mc;
         var3 = Minecraft.theWorld.loadedEntityList.iterator();

         while(var3.hasNext()) {
            Entity entity1 = (Entity)var3.next();
            if(entity1.isEntityAlive() && this.ents.contains(entity1)) {
               double posX1 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * (double)e.getPartialTicks() - RenderManager.renderPosX;
               double posY = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * (double)e.getPartialTicks() - RenderManager.renderPosY;
               double posZ = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * (double)e.getPartialTicks() - RenderManager.renderPosZ;
               boolean old = mc.gameSettings.viewBobbing;
               RenderHelper.startDrawing();
               mc.gameSettings.viewBobbing = false;
               mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
               mc.gameSettings.viewBobbing = old;
               this.renderTracer(entity1, posX1, posY, posZ);
               RenderHelper.stopDrawing();
            }
         }

      }
   }

   // $FF: synthetic method
   private void renderTracer(Entity entity, double x, double y, double z) {
      Minecraft var10000 = mc;
      float distance = Minecraft.thePlayer.getDistanceToEntity(entity);
      float xD = distance / 48.0F;
      if(xD >= 1.0F) {
         xD = 1.0F;
      }

      int color = ColorUtils.getClientColor().hashCode();
      boolean entityesp = Exist.moduleManager.getModule(ESP.class).isToggled();
      GL11.glPushMatrix();
      GL11.glEnable(2848);
      RenderHelper.glColor(ColorUtils.transparency(color, 1.0D));
      GL11.glLineWidth(1.5F);
      GL11.glBegin(1);
      Minecraft var10001 = mc;
      GL11.glVertex3d(0.0D, (double)Minecraft.thePlayer.getEyeHeight(), 0.0D);
      GL11.glVertex3d(x, y, z);
      GL11.glEnd();
      GL11.glBegin(1);
      GL11.glVertex3d(x, y, z);
      GL11.glVertex3d(x, y + (entityesp?0.0D:1.2D), z);
      GL11.glEnd();
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }
}
