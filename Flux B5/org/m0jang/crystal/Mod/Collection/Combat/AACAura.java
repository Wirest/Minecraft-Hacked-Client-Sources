package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventPacketReceive;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Events.EventRespawn;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.ClientUtils;
import org.m0jang.crystal.Utils.EntityUtils;
import org.m0jang.crystal.Utils.ModeUtils;
import org.m0jang.crystal.Utils.RotationUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class AACAura extends Module {
   private TimeHelper delayTimer = new TimeHelper();
   private TimeHelper tickTimer = new TimeHelper();
   private int attacked;
   private double delay;
   public static EntityLivingBase currentTarget;
   private boolean doMiss;
   private final double range = 3.9D;
   private float rotatedYaw;
   private float rotatedPitch;
   int fakeAttackRange = 8;

   public AACAura() {
      super("AACAura", Category.Combat, false);
   }

   public void onEnable() {
      super.onEnable();
      this.attacked = 0;
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onRespawn(EventRespawn eventRespawn) {
      this.setEnabled(false);
      ChatUtils.sendMessageToPlayer("Auto Disabled \247a" + this.getName() + "\247r by Respawn.");
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         if (this.getTargets().size() > 0) {
            currentTarget = (EntityLivingBase)this.getTargets().get(0);
            float[] rot = RotationUtils.getRotations(currentTarget);
            if (this.doMiss && (double)currentTarget.getDistanceToEntity(Minecraft.thePlayer) > 1.8D) {
               event.yaw = rot[0] + -50.0F + (float)(new Random()).nextInt(100);
               event.pitch = rot[1] + -20.0F + (float)(new Random()).nextInt(40);
            } else {
               event.yaw = rot[0] + -5.0F + (float)(new Random()).nextInt(10);
               event.pitch = rot[1] + -5.0F + (float)(new Random()).nextInt(10);
            }

            this.rotatedYaw = event.yaw;
            this.rotatedPitch = event.pitch;
            this.tickTimer.reset();
         }
      }
   }

   @EventTarget
   public void onRender(EventRender3D event) {
      if (currentTarget != null && !currentTarget.isDead) {
         if (this.delayTimer.hasPassed(this.delay)) {
            this.delayTimer.reset();
            if (Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() && AntiBots.Mode.getSelectedOption().equals("AAC")) {
               this.delay = (double)(100 + (new Random()).nextInt(50));
            }

            if (currentTarget.getDistanceToEntity(Minecraft.thePlayer) < (float)this.fakeAttackRange && (double)currentTarget.getDistanceToEntity(Minecraft.thePlayer) > 3.9D) {
               Minecraft.thePlayer.swingItem();
               this.fakeAttackRange = 6 + (new Random()).nextInt(2);
            }

            float[] rot = RotationUtils.getRotations(currentTarget);
            float diffYaw = rot[0] - this.rotatedYaw;
            float diffPitch = rot[1] - this.rotatedPitch;
            if ((double)currentTarget.getDistanceToEntity(Minecraft.thePlayer) <= 1.8D || diffYaw <= 5.0F && diffYaw >= -5.0F && diffPitch <= 5.0F && diffPitch >= -5.0F && this.isEntityValid(currentTarget, true)) {
               this.attack(currentTarget, Crystal.INSTANCE.getMods().get(Criticals.class).isEnabled());
               if (this.attacked % 10 == 0) {
                  this.doMiss = true;
               }

            } else {
               this.doMiss = false;
            }
         }
      }
   }

   public boolean isEntityValid(Entity entity, boolean attack) {
      if (entity instanceof EntityLivingBase) {
         EntityLivingBase entityLiving = (EntityLivingBase)entity;
         double seenRange = attack ? 3.9D : 10.0D;
         boolean okRange = (double)entityLiving.getDistanceToEntity(Minecraft.thePlayer) < seenRange;
         if (Minecraft.thePlayer.isEntityAlive() && entityLiving.isEntityAlive() && okRange) {
            if (!ModeUtils.isValidForAura(entity)) {
               return false;
            } else {
               return !Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() || !((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC((EntityLivingBase)entity);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private List getTargets() {
      ArrayList targets = new ArrayList();
      Iterator var3 = ClientUtils.loadedEntityList().iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if (this.isEntityValid(entity, false)) {
            targets.add((EntityLivingBase)entity);
         }
      }

      targets.sort(new Comparator<EntityLivingBase>() {
         public int compare(EntityLivingBase target1, EntityLivingBase target2) {
            return AntiBots.isBindedAACBot(target1) ? -1 : 0;
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

      ++this.attacked;
      Minecraft.thePlayer.swingItem();
      this.mc.playerController.attackEntity(Minecraft.thePlayer, entity);
   }

   @EventTarget
   public void onMotionPacket(EventPacketReceive event) {
      if (event.getPacket() instanceof C03PacketPlayer) {
         C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
         if (packet.getRotating()) {
            if (currentTarget != null && this.isEntityValid(currentTarget, false)) {
               float[] rotations = EntityUtils.getEntityRotations(currentTarget);
               packet.setYaw(rotations[0]);
               packet.setPitch(rotations[1]);
            }

         }
      }
   }
}
