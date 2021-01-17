package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.events.EventAttackEntity;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class SuperHit extends Mod {
   private EntityLivingBase entity;
   private Value modes = new Value("SuperHit", "TargetMode", 0);
   private Value mode = new Value("SuperHit", "Mode", 0);
   TimeHelper time = new TimeHelper();
   private boolean hit;

   public SuperHit() {
      super("SuperHit", Mod.Category.COMBAT, Colors.DARKRED.c);
      this.modes.mode.add("FOV");
      this.modes.mode.add("Distance");
      this.mode.addValue("Old");
      this.mode.addValue("New");
   }
   @Override
   public void onDisable() {
       super.onDisable();
       ClientUtil.sendClientMessage("SuperHit Disable", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("SuperHit Enable", ClientNotification.Type.SUCCESS);
   }

   @EventTarget
   public void onPreMotion(EventPreMotion event) {
      Vec3 vec;
      double x;
      double z;
      double y;
      if (this.mode.isCurrentMode("New")) {
         if (!this.hit) {
            this.entity = null;
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
               this.entity = (EntityPlayer)this.mc.objectMouseOver.entityHit;
            } else {
               this.setTarget();
            }
         }

         if (this.entity != null && Mouse.isButtonDown(0)) {
            if (!this.hit) {
               this.hit = true;
               return;
            }

            if (this.hit && this.mc.thePlayer.onGround) {
               this.mc.thePlayer.jump();
            }

            if (this.mc.thePlayer.fallDistance > 0.0F) {
               event.yaw = CombatUtil.getRotations(this.entity)[0];
               event.pitch = CombatUtil.getRotations(this.entity)[1];
               if ((double)this.mc.thePlayer.getDistanceToEntity(this.entity) > 3.5D) {
                  vec = this.mc.thePlayer.getVectorForRotation(0.0F, this.mc.thePlayer.rotationYaw);
                  x = this.mc.thePlayer.posX + vec.xCoord * (double)(this.mc.thePlayer.getDistanceToEntity(this.entity) - 1.0F);
                  z = this.mc.thePlayer.posZ + vec.zCoord * (double)(this.mc.thePlayer.getDistanceToEntity(this.entity) - 1.0F);
                  y = (double)this.entity.getPosition().getY() + 0.25D;
                  ArrayList positions = PlayerUtil.vanillaTeleportPositions(x, y + 1.0D, z, 4.0D);

                  for(int j = 0; j < 1; ++j) {
                     for(int i = 0; i < positions.size(); ++i) {
                        Vector3f pos = (Vector3f)positions.get(i);
                        if (i == 0) {
                           new Vector3f((float)this.mc.thePlayer.posX, (float)this.mc.thePlayer.posY, (float)this.mc.thePlayer.posZ);
                        } else {
                           Vector3f var10000 = (Vector3f)positions.get(i - 1);
                        }

                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), false));
                     }
                  }

                  this.mc.thePlayer.onCriticalHit(this.entity);
                  this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.entity, C02PacketUseEntity.Action.ATTACK));
                  this.hit = false;
               }
            }
         }
      } else if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null) {
         vec = this.mc.objectMouseOver.entityHit.getVectorForRotation(0.0F, this.mc.objectMouseOver.entityHit.rotationYaw);
         x = this.mc.objectMouseOver.entityHit.posX - vec.xCoord * 0.5D;
         z = this.mc.objectMouseOver.entityHit.posZ - vec.zCoord * 0.5D;
         y = (double)this.mc.objectMouseOver.entityHit.getPosition().getY();

         Vector3f var14;
         for(Iterator var15 = PlayerUtil.vanillaTeleportPositions(x, y, z, 1.0D).iterator(); var15.hasNext(); var14 = (Vector3f)var15.next()) {
            ;
         }
      }

   }

   private void setTarget() {
      double closest = 2.147483647E9D;
      EntityLivingBase target = null;
      Iterator var5 = this.mc.theWorld.loadedEntityList.iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         if (o instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase)o;
            if (this.isValidTarget(e)) {
               if (this.modes.isCurrentMode("FOV")) {
                  float yaw = CombatUtil.getRotations(e)[0];
                  float yawDiff = CombatUtil.getYawDifference(CombatUtil.getNewAngle(this.mc.thePlayer.rotationYaw), CombatUtil.getNewAngle(yaw));
                  if ((double)yawDiff < closest) {
                     closest = (double)yawDiff;
                     target = e;
                  }
               } else {
                  double dist = (double)this.mc.thePlayer.getDistanceToEntity(e);
                  if (dist < closest) {
                     closest = dist;
                     target = e;
                  }
               }
            }
         }
      }

      this.entity = target;
   }

   @EventTarget
   public void onEvent(EventReceivePacket event) {
      if (event.getPacket() instanceof S18PacketEntityTeleport) {
         S18PacketEntityTeleport packet = (S18PacketEntityTeleport)event.getPacket();
         if (packet.getEntityId() == 80085) {
            event.cancel = true;
         }
      }

   }

   @EventTarget
   public void doAttack(EventAttackEntity event) {
      if (this.mode.isCurrentMode("Old")) {
         if (!this.time.isDelayComplete(500L)) {
            return;
         }

         if (event.getPlayerIn() == this.mc.thePlayer && (double)this.mc.thePlayer.getDistanceToEntity(event.getTarget()) > 3.5D) {
            Vec3 vec = this.mc.thePlayer.getVectorForRotation(0.0F, this.mc.thePlayer.rotationYaw);
            double x = this.mc.thePlayer.posX + vec.xCoord * (double)(this.mc.thePlayer.getDistanceToEntity(event.getTarget()) - 1.0F);
            double z = this.mc.thePlayer.posZ + vec.zCoord * (double)(this.mc.thePlayer.getDistanceToEntity(event.getTarget()) - 1.0F);
            double y = (double)event.getTarget().getPosition().getY() + 0.25D;
            ArrayList positions = PlayerUtil.vanillaTeleportPositions(x, y, z, 3.0D);

            for(int j = 0; j < 1; ++j) {
               for(int i = 0; i < positions.size() - 1; ++i) {
                  Vector3f pos = (Vector3f)positions.get(i);
                  if (i == 0) {
                     new Vector3f((float)this.mc.thePlayer.posX, (float)this.mc.thePlayer.posY, (float)this.mc.thePlayer.posZ);
                  } else {
                     Vector3f var10000 = (Vector3f)positions.get(i - 1);
                  }

                  this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), true));
                  positions.size();
               }
            }

            this.time.reset();
         }
      }

   }

   private boolean isValidTarget(EntityLivingBase entity) {
      if (entity == this.mc.thePlayer) {
         return false;
      } else if (entity.isInvisible()) {
         return false;
      } else {
         return entity instanceof EntityPlayer;
      }
   }
}

