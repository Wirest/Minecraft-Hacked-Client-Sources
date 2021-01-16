package me.existdev.exist.module.modules.combat.aura;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Random;
import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.modules.combat.Aura;
import me.existdev.exist.utils.RayTraceUtils;
import me.existdev.exist.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AACAura {
   // $FF: synthetic field
   Minecraft mc = Minecraft.getMinecraft();

   // $FF: synthetic method
   @EventTarget
   public void onPre(EventPreMotionUpdates event) {
      if(Exist.moduleManager.getModule(Aura.class).isToggled()) {
         if(Exist.settingManager.getSetting(Exist.moduleManager.getModule(Aura.class), "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
            Aura.curYaw = Minecraft.thePlayer.rotationYaw;
            Aura.curPitch = Minecraft.thePlayer.rotationPitch;
            if(this.mc.currentScreen != null) {
               Aura.lastMs = System.currentTimeMillis() + 1000L;
            } else {
               Aura.doBlock = false;
               Aura.clear();
               Aura.findTargets(event);
               Aura.setCurTarget();
               if(Aura.curTarget != null) {
                  RayTraceUtils rand1 = new RayTraceUtils(Aura.curTarget);
                  if(rand1.getEntity() != Aura.curTarget) {
                     Aura.curBot = rand1.getEntity();
                  }
               }

               if(Aura.curTarget != null) {
                  Aura.switchDelay();
                  Random rand11 = new Random();
                  if(Aura.tick == 0) {
                     Aura.doAttack();
                     Aura.lastTarget = Aura.curTarget;
                     RotationUtils.server_pitch = Aura.curPitch;
                     RotationUtils.server_yaw = Aura.curYaw + (float)rand11.nextInt(10) - 5.0F;
                  } else {
                     RotationUtils.server_yaw = Minecraft.thePlayer.rotationYaw + (Aura.curYaw + (float)rand11.nextInt(10) - 5.0F - Minecraft.thePlayer.rotationYaw) / 2.0F;
                  }
               } else {
                  Aura.targets.clear();
                  Aura.attackedTargets.clear();
                  Aura.lastMs = System.currentTimeMillis();
                  if(Aura.unBlock) {
                     this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                     Minecraft.thePlayer.itemInUseCount = 0;
                     Aura.unBlock = false;
                  }
               }
            }

         }
      }
   }
}
