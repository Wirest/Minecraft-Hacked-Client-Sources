package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.Module;
import saint.utilities.ListenerUtil;
import saint.utilities.RenderHelper;

public class Trajectories extends Module {
   private boolean esping = false;

   public Trajectories() {
      super("Trajectories");
      this.setEnabled(true);
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D) {
         boolean bow = false;
         boolean potion = false;
         if (mc.thePlayer.getHeldItem() == null) {
            return;
         }

         if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSnowball) && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemEnderPearl) && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemEgg) && (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) || !ItemPotion.isSplash(mc.thePlayer.getHeldItem().getItemDamage()))) {
            return;
         }

         bow = mc.thePlayer.getHeldItem().getItem() instanceof ItemBow;
         potion = mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion;
         float throwingYaw = ListenerUtil.serverPos.rotationYaw;
         float throwingPitch = ListenerUtil.serverPos.rotationPitch;
         mc.getRenderManager();
         double posX = RenderManager.renderPosX - (double)(MathHelper.cos(throwingYaw / 180.0F * 3.141593F) * 0.16F);
         mc.getRenderManager();
         double posY = RenderManager.renderPosY + (double)mc.thePlayer.getEyeHeight() - 0.1000000014901161D;
         mc.getRenderManager();
         double posZ = RenderManager.renderPosZ - (double)(MathHelper.sin(throwingYaw / 180.0F * 3.141593F) * 0.16F);
         double motionX = (double)(-MathHelper.sin(throwingYaw / 180.0F * 3.141593F) * MathHelper.cos(throwingPitch / 180.0F * 3.141593F)) * (bow ? 1.0D : 0.4D);
         double motionY = (double)(-MathHelper.sin((throwingPitch - (float)(potion ? 20 : 0)) / 180.0F * 3.141593F)) * (bow ? 1.0D : 0.4D);
         double motionZ = (double)(MathHelper.cos(throwingYaw / 180.0F * 3.141593F) * MathHelper.cos(throwingPitch / 180.0F * 3.141593F)) * (bow ? 1.0D : 0.4D);
         int var6 = 72000 - mc.thePlayer.getItemInUseCount();
         float power = (float)var6 / 20.0F;
         power = (power * power + power * 2.0F) / 3.0F;
         if ((double)power < 0.1D) {
            return;
         }

         if (power > 1.0F) {
            power = 1.0F;
         }

         float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
         motionX /= (double)distance;
         motionY /= (double)distance;
         motionZ /= (double)distance;
         motionX *= (double)(bow ? power * 2.0F : 1.0F) * (potion ? 0.5D : 1.5D);
         motionY *= (double)(bow ? power * 2.0F : 1.0F) * (potion ? 0.5D : 1.5D);
         motionZ *= (double)(bow ? power * 2.0F : 1.0F) * (potion ? 0.5D : 1.5D);
         OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glEnable(3553);
         OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0F, 0.0F);
         GL11.glDisable(2896);
         GL11.glEnable(2848);
         GL11.glDisable(2929);
         GL11.glPushMatrix();
         GL11.glColor4f(0.5F, 1.0F, 1.0F, 0.5F);
         GL11.glDisable(3553);
         GL11.glDepthMask(false);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3042);
         GL11.glLineWidth(2.0F);
         GL11.glBegin(3);
         boolean hasLanded = false;
         Entity hitEntity = null;
         MovingObjectPosition landingPosition = null;

         double var10000;
         double var10001;
         while(!hasLanded && posY > 0.0D) {
            Vec3 present = new Vec3(posX, posY, posZ);
            Vec3 future = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition possibleLandingStrip = mc.theWorld.rayTraceBlocks(present, future, false, true, false);
            if (possibleLandingStrip != null) {
               if (possibleLandingStrip.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                  landingPosition = possibleLandingStrip;
                  hasLanded = true;
               }
            } else {
               Entity entityHit = this.getEntityHit(bow, present, future);
               if (entityHit != null) {
                  landingPosition = new MovingObjectPosition(entityHit);
                  hasLanded = true;
               }
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float motionAdjustment = 0.99F;
            motionX *= (double)motionAdjustment;
            motionY *= (double)motionAdjustment;
            motionZ *= (double)motionAdjustment;
            motionY -= potion ? 0.05D : (bow ? 0.05D : 0.03D);
            mc.getRenderManager();
            var10000 = posX - RenderManager.renderPosX;
            mc.getRenderManager();
            var10001 = posY - RenderManager.renderPosY;
            mc.getRenderManager();
            GL11.glVertex3d(var10000, var10001, posZ - RenderManager.renderPosZ);
         }

         GL11.glEnd();
         GL11.glPushMatrix();
         mc.getRenderManager();
         var10000 = posX - RenderManager.renderPosX;
         mc.getRenderManager();
         var10001 = posY - RenderManager.renderPosY;
         mc.getRenderManager();
         GL11.glTranslated(var10000, var10001, posZ - RenderManager.renderPosZ);
         if (landingPosition != null && landingPosition.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY && landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int landingIndex = landingPosition.field_178784_b.getIndex();
            if (landingIndex == 1) {
               GL11.glRotatef(270.0F, 1.0F, 0.0F, 0.0F);
            } else if (landingIndex == 2) {
               GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            } else if (landingIndex == 3) {
               GL11.glRotatef(360.0F, 1.0F, 0.0F, 0.0F);
            } else if (landingIndex == 4) {
               GL11.glRotatef(270.0F, 0.0F, 90.0F, 1.0F);
            } else if (landingIndex == 5) {
               GL11.glRotatef(90.0F, 0.0F, 90.0F, 1.0F);
            }

            RenderHelper.drawBorderedRect(-0.5F, 0.5F, 0.5F, -0.5F, 1.0F, -16777216, -2135891738);
            GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
         }

         GL11.glPopMatrix();
         if (landingPosition != null) {
            if (landingPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
               this.esping = true;
               mc.getRenderManager();
               var10000 = -RenderManager.renderPosX;
               mc.getRenderManager();
               var10001 = -RenderManager.renderPosY;
               mc.getRenderManager();
               GL11.glTranslated(var10000, var10001, -RenderManager.renderPosZ);
               GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.55F);
               RenderHelper.drawOutlinedBoundingBox(landingPosition.entityHit.getEntityBoundingBox().expand(0.225D, 0.125D, 0.225D).offset(0.0D, 0.1D, 0.0D));
               GL11.glColor4f(0.5F, 1.0F, 1.0F, 0.11F);
               RenderHelper.drawFilledBox(landingPosition.entityHit.getEntityBoundingBox().expand(0.225D, 0.125D, 0.225D).offset(0.0D, 0.1D, 0.0D));
               mc.getRenderManager();
               var10000 = RenderManager.renderPosX;
               mc.getRenderManager();
               var10001 = RenderManager.renderPosY;
               mc.getRenderManager();
               GL11.glTranslated(var10000, var10001, RenderManager.renderPosZ);
            } else {
               this.esping = false;
            }
         }

         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(2929);
         GL11.glDisable(2848);
         GL11.glPopMatrix();
      }

   }

   public boolean isEsping() {
      return this.esping;
   }

   private ArrayList getEntities() {
      ArrayList ret = new ArrayList();
      Iterator var3 = mc.theWorld.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Object e = var3.next();
         if (e != mc.thePlayer && e instanceof EntityLivingBase) {
            ret.add((EntityLivingBase)e);
         }
      }

      return ret;
   }

   private Entity getEntityHit(boolean bow, Vec3 vecOrig, Vec3 vecNew) {
      Iterator var5 = this.getEntities().iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         EntityLivingBase entity = (EntityLivingBase)o;
         if (entity != mc.thePlayer) {
            float expander = 0.2F;
            AxisAlignedBB bounding2 = entity.boundingBox.expand((double)expander, (double)expander, (double)expander);
            MovingObjectPosition possibleEntityLanding = bounding2.calculateIntercept(vecOrig, vecNew);
            if (possibleEntityLanding != null) {
               return entity;
            }
         }
      }

      return null;
   }
}
