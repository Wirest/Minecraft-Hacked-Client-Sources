package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.value.Value;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;
import optifine.BlockPosM;

public class FastStairs extends Mod {
   private Value mode = new Value("FastStairs", "Mode", 0);

   public FastStairs() {
      super("FastStairs", Mod.Category.MOVEMENT, Colors.ORANGE.c);
      this.mode.mode.add("NCP");
      this.mode.mode.add("AAC");
      this.showValue = this.mode;
   }

   @EventTarget
   public void onUpdate(UpdateEvent event) {
      this.setColor(-9868801);
      BlockPos bp = new BlockPosM(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.3D, this.mc.thePlayer.posZ);
      if (this.mc.theWorld.getBlockState(bp).getBlock() instanceof BlockStairs) {
         if (this.mode.isCurrentMode("NCP")) {
            PlayerUtil.toFwd(0.12D);
         } else {
            this.mc.thePlayer.motionX *= 1.53D;
            this.mc.thePlayer.motionZ *= 1.53D;
         }
      } else {
         this.mc.timer.timerSpeed = 1.0F;
      }

   }
   @Override
   public void onDisable() {
       super.onDisable();
       ClientUtil.sendClientMessage("FastStairs Disable", ClientNotification.Type.ERROR);
   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("FastStairs Enable", ClientNotification.Type.SUCCESS);
   }
}
