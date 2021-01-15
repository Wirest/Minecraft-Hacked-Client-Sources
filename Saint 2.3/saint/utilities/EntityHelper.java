package saint.utilities;

import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public final class EntityHelper {
   private static Minecraft mc = Minecraft.getMinecraft();

   public static int getBestWeapon(Entity target) {
      int originalSlot = mc.thePlayer.inventory.currentItem;
      int weaponSlot = -1;
      float weaponDamage = 1.0F;

      for(byte slot = 0; slot < 9; ++slot) {
         mc.thePlayer.inventory.currentItem = slot;
         ItemStack itemStack = mc.thePlayer.getHeldItem();
         if (itemStack != null) {
            float damage = getItemDamage(itemStack);
            damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);
            if (damage > weaponDamage) {
               weaponDamage = damage;
               weaponSlot = slot;
            }
         }
      }

      if (weaponSlot != -1) {
         return weaponSlot;
      } else {
         return originalSlot;
      }
   }

   public static float[] getFacingRotations(int x, int y, int z, EnumFacing facing) {
      Entity temp = new EntitySnowball(mc.theWorld);
      temp.posX = (double)x + 0.5D;
      temp.posY = (double)y + 0.5D;
      temp.posZ = (double)z + 0.5D;
      temp.posX += (double)facing.getDirectionVec().getX() * 0.25D;
      temp.posY += (double)facing.getDirectionVec().getY() * 0.25D;
      temp.posZ += (double)facing.getDirectionVec().getZ() * 0.25D;
      return getAngles(temp);
   }

   public static Location predictEntityLocation(Entity e, double milliseconds) {
      if (e != null) {
         if (e.posX == e.lastTickPosX && e.posY == e.lastTickPosY && e.posZ == e.lastTickPosZ) {
            return new Location(e.posX, e.posY, e.posZ);
         } else {
            double ticks = milliseconds / 1000.0D;
            ticks *= 20.0D;
            return interp(new Location(e.lastTickPosX, e.lastTickPosY, e.lastTickPosZ), new Location(e.posX + e.motionX, e.posY + e.motionY, e.posZ + e.motionZ), ticks);
         }
      } else {
         return null;
      }
   }

   public static Location interp(Location from, Location to, double pct) {
      double x = from.x + (to.x - from.x) * pct;
      double y = from.y + (to.y - from.y) * pct;
      double z = from.z + (to.z - from.z) * pct;
      return new Location(x, y, z);
   }

   public static float getYawChangeToEntity(Entity entity) {
      double deltaX = entity.posX - mc.thePlayer.posX;
      double deltaZ = entity.posZ - mc.thePlayer.posZ;
      double yawToEntity;
      if (deltaZ < 0.0D && deltaX < 0.0D) {
         yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else if (deltaZ < 0.0D && deltaX > 0.0D) {
         yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
      } else {
         yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
      }

      return MathHelper.wrapAngleTo180_float(-(mc.thePlayer.rotationYaw - (float)yawToEntity));
   }

   public static float getPitchChangeToEntity(Entity entity) {
      double deltaX = entity.posX - mc.thePlayer.posX;
      double deltaZ = entity.posZ - mc.thePlayer.posZ;
      double deltaY = entity.posY - 1.6D + (double)entity.getEyeHeight() - mc.thePlayer.posY;
      double distanceXZ = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
      double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
      return -MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch - (float)pitchToEntity);
   }

   public static float[] getAngles(Entity e) {
      return new float[]{getYawChangeToEntity(e) + mc.thePlayer.rotationYaw, getPitchChangeToEntity(e) + mc.thePlayer.rotationPitch};
   }

   public static float[] getEntityRotations(EntityPlayer player, Entity target) {
      double posX = target.posX - player.posX;
      double posY = target.posY + (double)target.getEyeHeight() - (player.posY + (double)player.getEyeHeight());
      double posZ = target.posZ - player.posZ;
      double var14 = (double)MathHelper.sqrt_double(posX * posX + posZ * posZ);
      float yaw = (float)(Math.atan2(posZ, posX) * 180.0D / 3.141592653589793D) - 90.0F;
      float pitch = (float)(-(Math.atan2(posY, var14) * 180.0D / 3.141592653589793D));
      return new float[]{yaw, pitch};
   }

   private float[] getBlockRotations(int x, int y, int z) {
      double var4 = (double)x - mc.thePlayer.posX + 0.5D;
      double var6 = (double)z - mc.thePlayer.posZ + 0.5D;
      double var8 = (double)y - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - 1.0D);
      double var14 = (double)MathHelper.sqrt_double(var4 * var4 + var6 * var6);
      float var12 = (float)(Math.atan2(var6, var4) * 180.0D / 3.141592653589793D) - 90.0F;
      return new float[]{var12, (float)(-(Math.atan2(var8, var14) * 180.0D / 3.141592653589793D))};
   }

   private static float getItemDamage(ItemStack itemStack) {
      Multimap multimap = itemStack.getAttributeModifiers();
      if (!multimap.isEmpty()) {
         Iterator iterator = multimap.entries().iterator();
         if (iterator.hasNext()) {
            Entry entry = (Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double damage;
            if (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {
               damage = attributeModifier.getAmount();
            } else {
               damage = attributeModifier.getAmount() * 100.0D;
            }

            if (attributeModifier.getAmount() > 1.0D) {
               return 1.0F + (float)damage;
            }

            return 1.0F;
         }
      }

      return 1.0F;
   }
}
