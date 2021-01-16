package me.razerboy420.weepcraft.module.modules.combat.aura;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.friend.FriendManager;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.module.modules.combat.aura.types.SwitchAura;
import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AuraType {

   public ArrayList types = new ArrayList();
   public List gasChamber = new ArrayList();
   public static EntityLivingBase jew;
   public String name;
   public AuraType current;
   public int tTicks;
   public int critTicks;
   public boolean killniggerpackets;
   public static float yaw;
   public static float pitch;
   public static int counter;
   public double random;


   public void addType(AuraType type) {
      this.types.add(type);
   }

   public AuraType getTypeByName(String name) {
      Iterator var3 = this.types.iterator();

      while(var3.hasNext()) {
         AuraType t = (AuraType)var3.next();
         if(t.getName().equalsIgnoreCase(name)) {
            return t;
         }
      }

      return null;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public AuraType getCurrent() {
      if(this.current == null) {
         this.current = new SwitchAura();
      }

      return this.current;
   }

   public void setCurrent(AuraType current) {
      this.current = current;
   }

   public void onUpdate(EventPreMotionUpdates event) {}

   public void onPacketOut(EventPacketSent event) {}

   public void afterUpdate(EventPostMotionUpdates event) {}

   public boolean isHealing() {
      return ModuleManager.autopot.isHealing();
   }

   public EntityLivingBase getClosestJiggaboo() {
      EntityLivingBase niggerthatstolemytv = null;
      Iterator var3 = Wrapper.getWorld().loadedEntityList.iterator();

      while(var3.hasNext()) {
         Object o = var3.next();
         if(o instanceof EntityLivingBase) {
            EntityLivingBase otherniggerwiththevcr = (EntityLivingBase)o;
            if((niggerthatstolemytv == null || niggerthatstolemytv.getDistanceToEntity(Wrapper.getPlayer()) > otherniggerwiththevcr.getDistanceToEntity(Wrapper.getPlayer())) && otherniggerwiththevcr != Wrapper.getPlayer() && otherniggerwiththevcr.getHealth() > 0.0F && (double)otherniggerwiththevcr.getDistanceToEntity(Wrapper.getPlayer()) < 4.2D) {
               niggerthatstolemytv = otherniggerwiththevcr;
            }
         }
      }

      return niggerthatstolemytv;
   }

   public void crittheshit() {
      if(Wrapper.getPlayer().onGround) {
         Wrapper.sendPacket(new CPacketPlayer.Position(Wrapper.mc().player.posX, Wrapper.mc().player.posY + 0.05000000074505806D, Wrapper.mc().player.posZ, false));
         Wrapper.sendPacket(new CPacketPlayer.Position(Wrapper.mc().player.posX, Wrapper.mc().player.posY, Wrapper.mc().player.posZ, false));
         Wrapper.sendPacket(new CPacketPlayer.Position(Wrapper.mc().player.posX, Wrapper.mc().player.posY + 0.012511000037193298D, Wrapper.mc().player.posZ, false));
         Wrapper.sendPacket(new CPacketPlayer.Position(Wrapper.mc().player.posX, Wrapper.mc().player.posY, Wrapper.mc().player.posZ, false));
         this.killniggerpackets = true;
      }

   }

   public void swing() {
      Aura var10000 = ModuleManager.aura;
      if(Aura.noswing.boolvalue) {
         Wrapper.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
      } else {
         Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
      }

   }

   public boolean timeToSwing() {
      Random r = new Random();
      boolean neg = r.nextBoolean();
      double var10001 = Math.random();
      Aura var10002 = ModuleManager.aura;
      this.random = var10001 * (double)Aura.random.value.floatValue();
      double var10000 = (double)this.tTicks;
      var10002 = ModuleManager.aura;
      return var10000 >= 20.0D / ((double)Aura.delay.value.floatValue() + (neg?this.random:-this.random));
   }

   public void face(EntityLivingBase ent) {
      EntityPlayerSP var10000 = Wrapper.getPlayer();
      var10000.rotationPitch += 9.0E-4F;
   }

   public float[] getRots(Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
      double d0 = entityIn.posX - Wrapper.getPlayer().posX;
      double d1 = entityIn.posZ - Wrapper.getPlayer().posZ;
      double d2;
      if(entityIn instanceof EntityLivingBase) {
         EntityLivingBase d3 = (EntityLivingBase)entityIn;
         d2 = d3.posY + (double)d3.getEyeHeight() - (Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight());
      } else {
         d2 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight());
      }

      double d31 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1);
      float f = (float)(MathHelper.atan2(d1, d0) * 57.29577951308232D) - 90.0F;
      float f1 = (float)(-(MathHelper.atan2(d2, d31) * 57.29577951308232D));
      float pitch = this.updateRotation(Wrapper.getPlayer().rotationPitch, f1, maxPitchIncrease);
      float yaw = this.updateRotation(Wrapper.getPlayer().rotationYaw, f, maxYawIncrease);
      return new float[]{yaw, pitch};
   }

   private float updateRotation(float angle, float targetAngle, float maxIncrease) {
      float f = MathHelper.wrapDegrees(targetAngle - angle);
      if(f > maxIncrease) {
         f = maxIncrease;
      }

      if(f < -maxIncrease) {
         f = -maxIncrease;
      }

      return angle + f;
   }

   public boolean isInAttackRange(EntityLivingBase ent) {
      float var10000 = ent.getDistanceToEntity(Wrapper.getPlayer());
      Aura var10001 = ModuleManager.aura;
      return var10000 <= Aura.range.value.floatValue();
   }

   public boolean isInBlockRange(EntityLivingBase ent) {
      return ent.getDistanceToEntity(Wrapper.getPlayer()) <= Aura.blockrange.value.floatValue();
   }

   public static float changeRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
      float var4 = MathHelper.wrapDegrees(p_70663_2_ - p_70663_1_);
      if(var4 > p_70663_3_) {
         var4 = p_70663_3_;
      }

      if(var4 < -p_70663_3_) {
         var4 = -p_70663_3_;
      }

      return p_70663_1_ + var4;
   }

   public boolean isAttackable(EntityLivingBase ent) {
      Aura var10000 = ModuleManager.aura;
      Aura var10001;
      float var6;
      if(Aura.player.boolvalue) {
         if(Aura.teams.boolvalue && MathUtils.isOnSameTeam(Wrapper.getPlayer(), ent)) {
            return false;
         }

         if(Aura.botcheck.boolvalue && ent instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer)ent;
            boolean bot = false;

            try {
               NetworkPlayerInfo e = Minecraft.getMinecraft().getConnection().getPlayerInfo(p.getUniqueID());
               e.getGameType().isAdventure();
            } catch (Exception var5) {
               bot = true;
            }

            if(bot) {
               return false;
            }
         }

         if(ent instanceof EntityPlayer && !ent.getUniqueID().equals(Integer.valueOf(0)) && !((EntityPlayer)ent).capabilities.isCreativeMode && !ent.isDead && ent != Wrapper.getPlayer()) {
            var6 = (float)ent.ticksExisted;
            var10001 = ModuleManager.aura;
            if(var6 > Aura.existed.value.floatValue() && ((EntityPlayer)ent).getHealth() > 0.0F && !ent.isDead && !FriendManager.isFriend(ent.getName()) && !ent.getName().startsWith("Body #")) {
               var10000 = ModuleManager.aura;
               if(!Aura.wallcheck.boolvalue) {
                  var10000 = ModuleManager.aura;
                  if(!Aura.invisible.boolvalue) {
                     if(!ent.isInvisible()) {
                        return true;
                     }

                     return false;
                  }

                  if(ent.canEntityBeSeen(Wrapper.getPlayer())) {
                     return true;
                  }

                  return false;
               }

               var10000 = ModuleManager.aura;
               if(!Aura.invisible.boolvalue) {
                  if(!ent.isInvisible()) {
                     return true;
                  }

                  return false;
               }

               return true;
            }
         }
      }

      var10000 = ModuleManager.aura;
      if(Aura.mob.boolvalue && ent instanceof EntityLivingBase && !(ent instanceof EntityPlayer) && !(ent instanceof EntityArmorStand) && ent.getHealth() > 0.0F && !ent.isDead) {
         var6 = (float)ent.ticksExisted;
         var10001 = ModuleManager.aura;
         if(var6 > Aura.existed.value.floatValue() && !ent.isDead && !FriendManager.isFriend(ent.getName()) && !ent.getName().startsWith("Body #")) {
            var10000 = ModuleManager.aura;
            if(!Aura.wallcheck.boolvalue) {
               var10000 = ModuleManager.aura;
               if(!Aura.invisible.boolvalue) {
                  if(!ent.isInvisible()) {
                     return true;
                  }

                  return false;
               }

               if(ent.canEntityBeSeen(Wrapper.getPlayer())) {
                  return true;
               }

               return false;
            }

            var10000 = ModuleManager.aura;
            if(!Aura.invisible.boolvalue) {
               if(!ent.isInvisible()) {
                  return true;
               }

               return false;
            }

            return true;
         }
      }

      return false;
   }

   public void block() {
      if(this.getShield() != null) {
         Wrapper.getPlayerController().processRightClick(Wrapper.getPlayer(), Wrapper.getWorld(), this.getShield());
      }

   }

   public void unBlock() {
      if(this.getShield() != null) {
         Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
      }

   }

   public EnumHand getShield() {
      return ModuleManager.flashback.isToggled()?EnumHand.MAIN_HAND:(Wrapper.getPlayer().getHeldItemMainhand() != null && Wrapper.getPlayer().getHeldItemMainhand().getItem() instanceof ItemShield?EnumHand.MAIN_HAND:(Wrapper.getPlayer().getHeldItemOffhand() != null && Wrapper.getPlayer().getHeldItemOffhand().getItem() instanceof ItemShield?EnumHand.OFF_HAND:null));
   }
}
