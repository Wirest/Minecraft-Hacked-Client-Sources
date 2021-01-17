package skyline.specc.mods.combat;

import java.util.ArrayList;
import java.util.Iterator;

import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.utils.AimUtils;
import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.util.MathHelper;

public class BowAim extends Module {
	private Entity bowtarget;
	private float velocity;
	public BowAim(ModData data, ModType type) {
		super(data, type);
	}

	   @EventListener
	   public void onUpdate(UpdateEvent event) {
	      bowtarget = null;
	      if (mc.thePlayer.inventory.getCurrentItem() != null) {
	         if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.getMinecraft().gameSettings.keyBindUseItem.pressed) {
	            bowtarget = getClosestEntity(true, true);
	            aimAtbowbowbowtarget();
	         }
	      }

	   }
	   public static synchronized void faceEntityClient(EntityLivingBase entity) {
		      float[] rotations = AimUtils.getRotations(entity);
		      if (rotations != null) {
		         mc.thePlayer.rotationYaw = limitAngleChange(mc.thePlayer.prevRotationYaw, rotations[0], 55.0F);
		         mc.getMinecraft();
		         mc.thePlayer.rotationPitch = rotations[1];
		      }

		   }	

	   private static final float limitAngleChange(float current, float intended, float maxChange) {
		      float change = intended - current;
		      if (change > maxChange) {
		         change = maxChange;
		      } else if (change < -maxChange) {
		         change = -maxChange;
		      }

		      return current + change;
		   }
	   
	   private void aimAtbowbowbowtarget() {
	      if (bowtarget != null) {
	         Mineman var10000 = mc;
	         int bowCharge = mc.thePlayer.getItemInUseDuration();
	         velocity = (float)(bowCharge / 20);
	         velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
	         velocity = 1.0F;
	         if ((double)velocity < 0.1D) {
	            if (bowtarget instanceof EntityLivingBase) {
	               faceEntityClient((EntityLivingBase)bowtarget);
	            }

	         } else {
	            if (velocity > 1.0F) {
	               velocity = 1.0F;
	            }

	            double var14 = bowtarget.posX + (bowtarget.posX - bowtarget.prevPosX) * 5.0D;
	            Mineman var10001 = mc;
	            double posX = var14 - mc.thePlayer.posX;
	            var14 = bowtarget.posY + (bowtarget.posY - bowtarget.prevPosY) * 5.0D + (double)bowtarget.getEyeHeight() - 0.15D;
	            var10001 = mc;
	            var14 -= mc.thePlayer.posY;
	            var10001 = mc;
	            double posY = var14 - (double)mc.thePlayer.getEyeHeight();
	            var14 = bowtarget.posZ + (bowtarget.posZ - bowtarget.prevPosZ) * 5.0D;
	            var10001 = mc;
	            double posZ = var14 - mc.thePlayer.posZ;
	            float yaw = (float)(Math.atan2(posZ, posX) * 180.0D / 3.141592653589793D) - 90.0F;
	            double y2 = Math.sqrt(posX * posX + posZ * posZ);
	            float g = 0.006F;
	            float tmp = (float)((double)(velocity * velocity * velocity * velocity) - (double)g * ((double)g * y2 * y2 + 2.0D * posY * (double)(velocity * velocity)));
	            float pitch = (float)(-Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt((double)tmp)) / ((double)g * y2))));
	            var10000 = mc;
	            mc.thePlayer.rotationPitch = pitch;
	            var10000 = mc;
	            mc.thePlayer.rotationYaw = yaw;
	         }
	      }
	   }
	      public static EntityLivingBase getClosestEntity(boolean ignoreFriends, boolean useFOV) {
	         EntityLivingBase closestEntity = null;
	         mc.getMinecraft();
	         Iterator var4 = mc.theWorld.loadedEntityList.iterator();

	         while(true) {
	            EntityLivingBase en;
	            float var6;
	            do {
	               String var10000;
	               do {
	                  do {
	                     do {
	                        Object o;
	                        do {
	                           do {
	                              do {
	                                 do {
	                                    if (!var4.hasNext()) {
	                                       return closestEntity;
	                                    }

	                                    o = var4.next();
	                                 } while(!(o instanceof EntityLivingBase));
	                              } while(getDistanceFromMouse((Entity)o) > 180);

	                              en = (EntityLivingBase)o;
	                           } while(o instanceof EntityPlayerSP);
	                        } while(en.isDead);
	                     } while(en.getHealth() <= 0.0F);

	                     mc.getMinecraft();
	                  } while(!mc.thePlayer.canEntityBeSeen(en));

	                  var10000 = en.getName();
	                  mc.getMinecraft();
	               } while(var10000.equals(mc.thePlayer.getName()));

	               if (closestEntity == null) {
	                  break;
	               }

	               mc.getMinecraft();
	               var6 = mc.thePlayer.getDistanceToEntity(en);
	               mc.getMinecraft();
	            } while(var6 >= mc.thePlayer.getDistanceToEntity(closestEntity));

	            closestEntity = en;
	         }
	      }
	      
	      public static int getDistanceFromMouse(Entity entity) {
	          float[] neededRotations = AimUtils.getRotations(entity);
	          if (neededRotations != null) {
	             float neededYaw = mc.thePlayer.rotationYaw - neededRotations[0];
	             float neededPitch = mc.thePlayer.rotationPitch - neededRotations[1];
	             float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw + neededPitch * neededPitch);
	             return (int)distanceFromMouse;
	          } else {
	             return -1;
	          }
	       }
	      public static ArrayList getCloseEntities(float range) {
	         ArrayList closeEntities = new ArrayList();
	         mc.getMinecraft();
	         Iterator var3 = mc.theWorld.loadedEntityList.iterator();

	         while(var3.hasNext()) {
	            Object o = var3.next();
	            EntityLivingBase en2 = (EntityLivingBase)o;
	            if (!(o instanceof EntityPlayerSP) && !en2.isDead && en2.getHealth() > 0.0F) {
	               mc.getMinecraft();
	               if (mc.thePlayer.canEntityBeSeen(en2)) {
	                  String var10000 = en2.getName();
	                  mc.getMinecraft();
	                  if (!var10000.equals(mc.thePlayer.getName())) {
	                     mc.getMinecraft();
	                     if (mc.thePlayer.getDistanceToEntity(en2) <= range) {
	                        closeEntities.add(en2);
	                     }
	                  }
	               }
	            }
	         }

	         return closeEntities;
	      }

}
