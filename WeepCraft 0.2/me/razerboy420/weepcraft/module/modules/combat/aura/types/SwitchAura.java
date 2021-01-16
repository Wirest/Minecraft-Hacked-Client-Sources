package me.razerboy420.weepcraft.module.modules.combat.aura.types;

import java.util.Iterator;
import java.util.Random;

import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.module.modules.combat.aura.Aura;
import me.razerboy420.weepcraft.module.modules.combat.aura.AuraType;
import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;

public class SwitchAura extends AuraType {

   public void onUpdate(EventPreMotionUpdates event) {
      Object o;
      Iterator var3;
      EntityLivingBase nig;
      if(this.gasChamber.isEmpty()) {
         var3 = Wrapper.getWorld().loadedEntityList.iterator();

         while(var3.hasNext()) {
            o = var3.next();
            if(o instanceof EntityLivingBase) {
               nig = (EntityLivingBase)o;
               if(this.isAttackable(nig) && this.isInAttackRange(nig)) {
                  this.gasChamber.add(nig);
               }
            }
         }
      }

      var3 = this.gasChamber.iterator();

      while(var3.hasNext()) {
         EntityLivingBase o1 = (EntityLivingBase)var3.next();
         if(!this.isAttackable(o1) || !this.isInAttackRange(o1)) {
            this.gasChamber.remove(o1);
         }
      }

      if(jew != null && (!this.isAttackable(jew) || !this.isInAttackRange(jew) || !this.gasChamber.contains(jew))) {
         jew = null;
      }

      if(jew == null) {
         jew = (EntityLivingBase)this.gasChamber.get(0);
      }

      if(jew != null && this.isInAttackRange(this.getClosestNigger()) && this.isAttackable(jew)) {
         float o2 = 2.14748365E9F;
         Aura var10000 = ModuleManager.aura;
         if(Aura.lockview.boolvalue) {
            Wrapper.getPlayer().rotationYaw = this.getRots(jew, o2, o2)[0];
            Wrapper.getPlayer().rotationPitch = this.getRots(jew, o2, o2)[1];
         }

         this.face(jew);
         yaw = this.getRots(jew, o2, o2)[0];
         pitch = this.getRots(jew, o2, o2)[1];
      }

      if(Aura.block.boolvalue) {
         var3 = Wrapper.getWorld().loadedEntityList.iterator();

         while(var3.hasNext()) {
            o = var3.next();
            if(o instanceof EntityLivingBase) {
               nig = (EntityLivingBase)o;
               if(this.isInBlockRange(nig) && this.isAttackable(nig)) {
                  this.block();
                  break;
               }
            }
         }
      }

      if(MathUtils.isAuraBlocking()) {
         this.unBlock();
      }

   }

   public boolean canIFuckingSwing() {
      if(Aura.autoaps.boolvalue) {
         return Wrapper.getPlayer().getCooledAttackStrength(0.0F) == 1.0F;
      } else {
         Random r = new Random();
         boolean neg = r.nextBoolean();
         double var10000 = (double)this.tTicks;
         Aura var10002 = ModuleManager.aura;
         return var10000 >= 20.0D / ((double)Aura.delay.value.floatValue() + (neg?this.random:-this.random));
      }
   }

   public void afterUpdate(EventPostMotionUpdates event) {
      if(!this.isHealing()) {
         Object o;
         Iterator neg;
         EntityLivingBase nig;
         if(Aura.block.boolvalue) {
            neg = Wrapper.getWorld().loadedEntityList.iterator();

            while(neg.hasNext()) {
               o = neg.next();
               if(o instanceof EntityLivingBase) {
                  nig = (EntityLivingBase)o;
                  if(this.isInBlockRange(nig) && this.isAttackable(nig)) {
                     this.block();
                     break;
                  }
               }
            }
         }

         if(jew != null && this.isInAttackRange(jew) && this.isAttackable(jew)) {
            ++this.tTicks;
            ModuleManager.aura.timer.update();
            if(ModuleManager.aura.timer.hasPassed(120L)) {
               Random o1 = new Random();
               boolean neg1 = o1.nextBoolean();
               double var10001 = Math.random();
               Aura var10002 = ModuleManager.aura;
               this.random = var10001 * (double)Aura.random.value.floatValue();
               if(this.canIFuckingSwing()) {
                  this.gasThe(jew);
                  this.tTicks = 0;
               }

               ModuleManager.aura.timer.reset();
            }
         }

         if(Aura.block.boolvalue) {
            neg = Wrapper.getWorld().loadedEntityList.iterator();

            while(neg.hasNext()) {
               o = neg.next();
               if(o instanceof EntityLivingBase) {
                  nig = (EntityLivingBase)o;
                  if(this.isInBlockRange(nig) && this.isAttackable(nig)) {
                     this.block();
                     return;
                  }
               }
            }
         }

      }
   }

   public void onPacketOut(EventPacketSent event) {
      if(!this.isHealing() && jew != null && this.isInAttackRange(jew) && this.isAttackable(jew)) {
         CPacketPlayer p;
         if(event.getPacket() instanceof CPacketPlayer) {
            p = (CPacketPlayer)event.getPacket();
            p.yaw = yaw;
            p.pitch = pitch;
         }

         if(event.getPacket() instanceof CPacketPlayer) {
            p = (CPacketPlayer)event.getPacket();
            p.yaw = yaw;
            p.pitch = pitch;
         }

         if(event.getPacket() instanceof CPacketPlayer) {
            p = (CPacketPlayer)event.getPacket();
            p.yaw = yaw;
            p.pitch = pitch;
         }

         if(event.getPacket() instanceof CPacketPlayer) {
            p = (CPacketPlayer)event.getPacket();
            p.yaw = yaw;
            p.pitch = pitch;
         }

         if(event.getPacket() instanceof CPacketPlayer) {
            p = (CPacketPlayer)event.getPacket();
            p.yaw = yaw;
            p.pitch = pitch;
         }
      }

   }

   public void gasThe(EntityLivingBase jew) {
      Aura var10000 = ModuleManager.aura;
      if(Aura.crits.boolvalue) {
         this.crittheshit();
      }

      boolean justunblockedtheguyneededtobeattackedbutnocheatisgay = false;
      if(MathUtils.isAuraBlocking()) {
         this.unBlock();
         justunblockedtheguyneededtobeattackedbutnocheatisgay = true;
      }

      Wrapper.getPlayerController().attackEntity(Wrapper.getPlayer(), jew);
      this.swing();
      if(justunblockedtheguyneededtobeattackedbutnocheatisgay) {
         this.block();
      }

      this.gasChamber.remove(jew);
   }

   public EntityLivingBase getClosestNigger() {
      EntityLivingBase close = null;
      Iterator var3 = Wrapper.getWorld().loadedEntityList.iterator();

      while(var3.hasNext()) {
         Object o = var3.next();
         if(o instanceof EntityLivingBase) {
            EntityLivingBase negro = (EntityLivingBase)o;
            if(this.isAttackable(negro) && this.isInAttackRange(negro) && (close == null || close.getDistanceToEntity(Wrapper.getPlayer()) > negro.getDistanceToEntity(Wrapper.getPlayer()))) {
               close = negro;
            }
         }
      }

      return close;
   }
}
