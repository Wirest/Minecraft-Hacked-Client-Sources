package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventRespawn;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.ClientUtils;
import org.m0jang.crystal.Utils.ModeUtils;
import org.m0jang.crystal.Utils.RotationUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class TPAura extends Module {
   private int attacked;
   private static int switchIndex;
   private TimeHelper switchTimer = new TimeHelper();
   public static EntityLivingBase currentTarget;
   private boolean AIMed;
   private boolean attackTick;

   public TPAura() {
      super("TPAura", Category.Combat, false);
   }

   public void onEnable() {
      super.onEnable();
      this.attacked = 0;
   }

   public void onDisable() {
      super.onDisable();
      this.attacked = 0;
   }

   @EventTarget
   public void onRespawn(EventRespawn eventRespawn) {
      this.setEnabled(false);
      ChatUtils.sendMessageToPlayer("Auto Disabled \247a" + this.getName() + "\247r by Respawn.");
   }

   @EventTarget
   private void aonTick(EventTick event) {
      if (this.getTargets().size() <= 0) {
         currentTarget = null;
      } else {
         int count = 0;
         int max = 50;
         Iterator var5 = this.getTargets().iterator();

         while(var5.hasNext()) {
            EntityLivingBase currentTarget = (EntityLivingBase)var5.next();
            ++count;
            if (count > max) {
               break;
            }

            if (!this.isNeedTeleport(currentTarget)) {
               float[] rot = RotationUtils.getRotations(currentTarget);
               Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C05PacketPlayerLook(rot[0], rot[1], Minecraft.thePlayer.onGround));
               if (isEntityValid(currentTarget, true)) {
                  this.attack(currentTarget, Crystal.INSTANCE.getMods().get(Criticals.class).isEnabled());
               }
            } else {
               List path = this.PathBlocks(new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ), new Vec3(currentTarget.posX, currentTarget.posY + (double)currentTarget.getEyeHeight(), currentTarget.posZ), false, true, true);
               if (path == null) {
                  return;
               }

               List pathLocs = new ArrayList();
               Vec3 lastPos = new Vec3(0.0D, 0.0D, 0.0D);

               for(int pointt = 0; pointt < path.size() - 1; ++pointt) {
                  BlockPos i = (BlockPos)path.get(pointt);
                  pathLocs.add(new Vec3((double)i.getX() + 0.5D, (double)i.getY(), (double)i.getZ() + 0.5D));
                  lastPos = new Vec3((double)i.getX() + 0.5D, (double)i.getY(), (double)i.getZ() + 0.5D);
               }

               Iterator var14 = pathLocs.iterator();

               while(var14.hasNext()) {
                  Vec3 item = (Vec3)var14.next();
                  Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(item.xCoord, item.yCoord, item.zCoord, true));
               }

               float[] rot = RotationUtils.getRotations(currentTarget, lastPos.xCoord, lastPos.yCoord, lastPos.zCoord);
               Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C05PacketPlayerLook(rot[0], rot[1], Minecraft.thePlayer.onGround));
               if (isEntityValid(currentTarget, true)) {
                  this.attack(currentTarget, Crystal.INSTANCE.getMods().get(Criticals.class).isEnabled());
               }
            }
         }

      }
   }

   public List<BlockPos> PathBlocks(Vec3 from, final Vec3 to, final boolean p_147447_3_, final boolean p_147447_4_, final boolean p_147447_5_) {
       final List<BlockPos> result = new ArrayList<BlockPos>();
       if (Double.isNaN(from.xCoord) || Double.isNaN(from.yCoord) || Double.isNaN(from.zCoord)) {
           return null;
       }
       if (!Double.isNaN(to.xCoord) && !Double.isNaN(to.yCoord) && !Double.isNaN(to.zCoord)) {
           final int var6 = MathHelper.floor_double(to.xCoord);
           final int var7 = MathHelper.floor_double(to.yCoord);
           final int var8 = MathHelper.floor_double(to.zCoord);
           int var9 = MathHelper.floor_double(from.xCoord);
           int var10 = MathHelper.floor_double(from.yCoord);
           int var11 = MathHelper.floor_double(from.zCoord);
           BlockPos var12 = new BlockPos(var9, var10, var11);
           new BlockPos(var6, var7, var8);
           final IBlockState var13 = Minecraft.theWorld.getBlockState(var12);
           final Block var14 = var13.getBlock();
           if ((!p_147447_4_ || var14.getCollisionBoundingBox((World)Minecraft.theWorld, var12, var13) != null) && var14.canCollideCheck(var13, p_147447_3_)) {
               final MovingObjectPosition var15 = var14.collisionRayTrace((World)Minecraft.theWorld, var12, from, to);
               if (var15 != null) {
                   return result;
               }
           }
           MovingObjectPosition var16 = null;
           int var17 = 200;
           while (var17-- >= 0) {
               if (Double.isNaN(from.xCoord) || Double.isNaN(from.yCoord) || Double.isNaN(from.zCoord)) {
                   return null;
               }
               if (var9 == var6 && var10 == var7 && var11 == var8) {
                   return p_147447_5_ ? result : null;
               }
               boolean var18 = true;
               boolean var19 = true;
               boolean var20 = true;
               double var21 = 999.0;
               double var22 = 999.0;
               double var23 = 999.0;
               if (var6 > var9) {
                   var21 = var9 + 1.0;
               }
               else if (var6 < var9) {
                   var21 = var9 + 0.0;
               }
               else {
                   var18 = false;
               }
               if (var7 > var10) {
                   var22 = var10 + 1.0;
               }
               else if (var7 < var10) {
                   var22 = var10 + 0.0;
               }
               else {
                   var19 = false;
               }
               if (var8 > var11) {
                   var23 = var11 + 1.0;
               }
               else if (var8 < var11) {
                   var23 = var11 + 0.0;
               }
               else {
                   var20 = false;
               }
               double var24 = 999.0;
               double var25 = 999.0;
               double var26 = 999.0;
               final double var27 = to.xCoord - from.xCoord;
               final double var28 = to.yCoord - from.yCoord;
               final double var29 = to.zCoord - from.zCoord;
               if (var18) {
                   var24 = (var21 - from.xCoord) / var27;
               }
               if (var19) {
                   var25 = (var22 - from.yCoord) / var28;
               }
               if (var20) {
                   var26 = (var23 - from.zCoord) / var29;
               }
               if (var24 == -0.0) {
                   var24 = -1.0E-4;
               }
               if (var25 == -0.0) {
                   var25 = -1.0E-4;
               }
               if (var26 == -0.0) {
                   var26 = -1.0E-4;
               }
               EnumFacing var30;
               if (var24 < var25 && var24 < var26) {
                   var30 = ((var6 > var9) ? EnumFacing.WEST : EnumFacing.EAST);
                   from = new Vec3(var21, from.yCoord + var28 * var24, from.zCoord + var29 * var24);
               }
               else if (var25 < var26) {
                   var30 = ((var7 > var10) ? EnumFacing.DOWN : EnumFacing.UP);
                   from = new Vec3(from.xCoord + var27 * var25, var22, from.zCoord + var29 * var25);
               }
               else {
                   var30 = ((var8 > var11) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                   from = new Vec3(from.xCoord + var27 * var26, from.yCoord + var28 * var26, var23);
               }
               var9 = MathHelper.floor_double(from.xCoord) - ((var30 == EnumFacing.EAST) ? 1 : 0);
               var10 = MathHelper.floor_double(from.yCoord) - ((var30 == EnumFacing.UP) ? 1 : 0);
               var11 = MathHelper.floor_double(from.zCoord) - ((var30 == EnumFacing.SOUTH) ? 1 : 0);
               var12 = new BlockPos(var9, var10, var11);
               result.add(new BlockPos(var9, var10, var11));
               final IBlockState var31 = Minecraft.theWorld.getBlockState(var12);
               final Block var32 = var31.getBlock();
               if (p_147447_4_ && var32.getCollisionBoundingBox((World)Minecraft.theWorld, var12, var31) == null) {
                   continue;
               }
               if (var32.canCollideCheck(var31, p_147447_3_)) {
                   final MovingObjectPosition var33 = var32.collisionRayTrace((World)Minecraft.theWorld, var12, from, to);
                   if (var33 != null) {
                       return result;
                   }
                   continue;
               }
               else {
                   var16 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, from, var30, var12);
               }
           }
           return p_147447_5_ ? result : null;
       }
       return null;
   }

   public static boolean isEntityValid(Entity entity, boolean attack) {
      if (!(entity instanceof EntityLivingBase)) {
         return false;
      } else {
         EntityLivingBase entityLiving = (EntityLivingBase)entity;
         if (ClientUtils.player().isEntityAlive() && entityLiving.isEntityAlive()) {
            if (!ModeUtils.isValidForAura(entity)) {
               return false;
            } else {
               return !Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() || !((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC((EntityLivingBase)entity);
            }
         } else {
            return false;
         }
      }
   }

   public boolean isNeedTeleport(Entity entity) {
      if (!Minecraft.thePlayer.canEntityBeSeen(entity)) {
         return true;
      } else {
         return Minecraft.thePlayer.getDistanceToEntity(entity) > 3.0F;
      }
   }

   private static boolean needBlock() {
      Iterator var1 = ClientUtils.loadedEntityList().iterator();

      while(var1.hasNext()) {
         Entity entity = (Entity)var1.next();
         if (isEntityValid(entity, false)) {
            return true;
         }
      }

      return false;
   }

   private List getTargets() {
      ArrayList targets = new ArrayList();
      Iterator var3 = ClientUtils.loadedEntityList().iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if (isEntityValid(entity, false)) {
            targets.add((EntityLivingBase)entity);
         }
      }

      targets.sort(new Comparator<EntityLivingBase>() {
         public int compare(EntityLivingBase target1, EntityLivingBase target2) {
            return target1.getName().compareTo(target2.getName());
         }
      });
      return targets;
   }

   private void attack(EntityLivingBase entity, boolean crit) {
      ItemStack currentItem = Minecraft.thePlayer.inventory.getCurrentItem();
      boolean var10000;
      if (currentItem != null && currentItem.getItem().getItemUseAction(currentItem) == EnumAction.BLOCK && Minecraft.thePlayer.isBlocking()) {
         var10000 = true;
      } else {
         var10000 = false;
      }

      if (ClientUtils.player().fallDistance > 0.0F && !ClientUtils.player().onGround && !ClientUtils.player().isOnLadder() && !ClientUtils.player().isInWater() && !ClientUtils.player().isPotionActive(Potion.blindness) && ClientUtils.player().ridingEntity == null) {
         var10000 = true;
      } else {
         var10000 = false;
      }

      ++this.attacked;
      Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
      Minecraft.thePlayer.swingItem();
      Minecraft.thePlayer.onCriticalHit(entity);
   }
}
