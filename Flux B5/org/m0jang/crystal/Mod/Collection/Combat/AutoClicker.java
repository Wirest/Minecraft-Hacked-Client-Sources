package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.TimeHelper;

public class AutoClicker extends Module {
   private TimeHelper timer = new TimeHelper();

   public AutoClicker() {
      super("AutoClicker", Category.Combat, false);
   }

   @EventTarget
   public void onUpdate(EventTick event) {
      if (Minecraft.gameSettings.keyBindAttack.getIsKeyPressed()) {
         Random random = new Random();
         int randomD = random.nextInt(25);
         int randomInc = random.nextInt(15);
         if (this.timer.hasPassed((double)(100 - randomD + randomInc))) {
            if (Minecraft.thePlayer.isBlocking()) {
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.UP));
               Minecraft.thePlayer.swingItem();
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
               this.timer.reset();
            } else {
               Minecraft.thePlayer.swingItem();
               Minecraft.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, C02PacketUseEntity.Action.ATTACK));
               this.timer.reset();
            }
         }
      }

   }
}
