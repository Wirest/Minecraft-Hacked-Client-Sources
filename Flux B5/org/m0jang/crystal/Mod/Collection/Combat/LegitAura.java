package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.EntityUtils;
import org.m0jang.crystal.Utils.RotationUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class LegitAura extends Module {
   private EntityLivingBase target;
   private boolean clickready;
   int delay = 70 + (new Random()).nextInt(30);
   private TimeHelper timeHelper = new TimeHelper();

   public LegitAura() {
      super("LegitAura", Category.Combat, false);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (event.state == EventState.PRE) {
         this.target = EntityUtils.findClosestEntity();
         if (this.target == null || Minecraft.thePlayer.getDistanceToEntity(this.target) > 1000.0F || this.target.isInvisible() || this.target.isOnSameTeam(Minecraft.thePlayer) || !Minecraft.thePlayer.canEntityBeSeen(this.target)) {
            this.target = null;
            return;
         }

         if (Minecraft.thePlayer.isEntityAlive()) {
            float distance = Minecraft.thePlayer.getDistanceToEntity(this.target);
            if (distance >= 8.0F || Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() && ((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC(this.target)) {
               return;
            }

            event.yaw = RotationUtils.getRotations(this.target)[0];
            event.pitch = RotationUtils.getRotations(this.target)[1];
            Minecraft.thePlayer.rotationYaw = RotationUtils.getRotations(this.target)[0];
            Minecraft.thePlayer.rotationPitch = RotationUtils.getRotations(this.target)[1];
            if (this.timeHelper.hasPassed((double)this.delay)) {
               this.delay = 70 + (new Random()).nextInt(30);
               this.timeHelper.reset();
               this.clickready = true;
            }
         }
      }

   }

   @EventTarget
   public void onTick(EventTick event) {
      if (this.clickready && this.target != null && Minecraft.thePlayer.getDistanceToEntity(this.mc.objectMouseOver.entityHit) <= 4.0F) {
         Minecraft.thePlayer.swingItem();
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
         this.clickready = false;
      }

   }
}
