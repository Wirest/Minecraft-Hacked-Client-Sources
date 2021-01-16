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

public class Hats extends Module {
   public Hats() {
      super("Hats", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
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
            if (entity.hurtTime > 5) {
               RenderUtils.drawHat(posX, posY + (double)entity.height + 0.12D, posZ, (double)(entity.width / 2.0F) - 0.5D, (double)entity.height - 1.45D, 0.0F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
               RenderUtils.drawHat(posX, posY + (double)entity.height + 0.12D, posZ, (double)(entity.width / 2.0F) - 0.6D, (double)entity.height - 1.75D, 0.0F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
            } else if (entity instanceof EntityPlayer && !Crystal.INSTANCE.friendManager.isFriend(entity.getName()) && !entity.isSneaking()) {
               RenderUtils.drawHat(posX, posY + (double)entity.height + 0.12D, posZ, (double)(entity.width / 2.0F) - 0.5D, (double)entity.height - 1.45D, 0.0F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
               RenderUtils.drawHat(posX, posY + (double)entity.height + 0.12D, posZ, (double)(entity.width / 2.0F) - 0.6D, (double)entity.height - 1.75D, 0.0F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
            } else if (entity instanceof EntityPlayer && !Crystal.INSTANCE.friendManager.isFriend(entity.getName()) && entity.isSneaking()) {
               RenderUtils.drawHat(posX, posY + (double)entity.height - 0.25D, posZ, (double)(entity.width / 2.0F) - 0.5D, (double)entity.height - 1.45D, 0.0F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
               RenderUtils.drawHat(posX, posY + (double)entity.height - 0.25D, posZ, (double)(entity.width / 2.0F) - 0.6D, (double)entity.height - 1.75D, 0.0F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
            } else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && !Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawHat(posX, posY, posZ, (double)(entity.width / 2.0F), (double)entity.height, 1.0F, 0.0F, 0.0F, 0.2F, 1.0F, 0.0F, 0.0F, 0.5F, 1.0F);
            } else if ((entity instanceof EntityCreature || entity instanceof EntityBat || entity instanceof EntitySquid || entity instanceof EntityVillager) && !Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawHat(posX, posY, posZ, (double)(entity.width / 2.0F), (double)entity.height, 0.0F, 1.0F, 0.3F, 0.2F, 0.0F, 1.0F, 0.0F, 0.5F, 1.0F);
            } else if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
               RenderUtils.drawHat(posX, posY + (double)entity.height + 0.12D, posZ, (double)(entity.width / 2.0F) - 0.5D, (double)entity.height - 1.45D, 0.1F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
               RenderUtils.drawHat(posX, posY + (double)entity.height + 0.12D, posZ, (double)(entity.width / 2.0F) - 0.6D, (double)entity.height - 1.75D, 0.1F, 0.0F, 0.0F, 255.0F, 1.0F, 1.0F, 1.0F, 0.2F, 1.0F);
            }
         }
      }
   }
}
