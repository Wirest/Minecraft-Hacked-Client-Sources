package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ModeUtils;
import org.m0jang.crystal.Utils.RenderUtils;

public class NoWeather extends Module {
   private boolean Tags;

   public NoWeather() {
      super("EntityESP", Category.Render, false);
   }

   public void onEnable() {
      if (Crystal.INSTANCE.getMods().get(NameTags.class).isEnabled()) {
         Crystal.INSTANCE.getMods().get(NameTags.class).setEnabled(false);
         this.Tags = true;
      }

      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      this.Tags = false;
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      if (this.Tags && !Crystal.INSTANCE.getMods().get(NameTags.class).isEnabled()) {
         Crystal.INSTANCE.getMods().get(NameTags.class).setEnabled(true);
      }

      Iterator var3 = Minecraft.theWorld.loadedEntityList.iterator();

      while(true) {
         while(true) {
            EntityLivingBase entity;
            do {
               do {
                  Object obj;
                  do {
                     if (!var3.hasNext()) {
                        return;
                     }

                     obj = var3.next();
                  } while(!(obj instanceof EntityLivingBase));

                  entity = (EntityLivingBase)obj;
               } while(entity == Minecraft.thePlayer);
            } while(!ModeUtils.isValidForESP(entity));

            GL11.glLoadIdentity();
            this.mc.entityRenderer.orientCamera(event.partialTicks);
            double posX = entity.posX - RenderManager.renderPosX;
            double posY = entity.posY - RenderManager.renderPosY;
            double posZ = entity.posZ - RenderManager.renderPosZ;
            if (entity.hurtTime != 0) {
               RenderUtils.drawEntityESP(posX, posY, posZ, (double)(entity.width / 2.0F) + 0.15D, (double)entity.height + 0.1D, 1.0F, 0.0F, 0.0F, 0.15F, 1.0F, 0.0F, 0.0F, 0.0F, 1.5F);
            } else if (entity instanceof EntityPlayer && !Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawEntityESP(posX, posY, posZ, (double)(entity.width / 2.0F) + 0.15D, (double)entity.height + 0.1D, 0.0F, 0.6F, 1.0F, 0.25F, 0.0F, 1.0F, 0.0F, 0.0F, 1.5F);
            } else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && !Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawEntityESP(posX, posY, posZ, (double)(entity.width / 2.0F), (double)entity.height + 0.1D, 1.0F, 0.0F, 0.0F, 0.2F, 1.0F, 0.0F, 0.0F, 0.5F, 1.0F);
            } else if ((entity instanceof EntityCreature || entity instanceof EntityBat || entity instanceof EntitySquid || entity instanceof EntityVillager) && !Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawEntityESP(posX, posY, posZ, (double)(entity.width / 2.0F), (double)entity.height + 0.1D, 0.0F, 1.0F, 0.3F, 0.2F, 0.0F, 1.0F, 0.0F, 0.5F, 1.0F);
            } else if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawEntityESP(posX, posY, posZ, (double)(entity.width / 2.0F), (double)entity.height + 0.1D, 0.7F, 0.3F, 0.5F, 0.2F, 1.0F, 0.0F, 0.0F, 0.5F, 1.0F);
            }
         }
      }
   }
}
