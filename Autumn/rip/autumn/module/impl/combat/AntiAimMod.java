package rip.autumn.module.impl.combat;

import me.zane.basicbus.api.annotations.Listener;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.EnumOption;

@Label("Anti Aim")
@Category(ModuleCategory.COMBAT)
@Aliases({"antiaim", "aa"})
public final class AntiAimMod extends Module {
   public final EnumOption yawMode;
   public final EnumOption pitchMode;
   private float yaw;
   private float pitch;

   public AntiAimMod() {
      this.yawMode = new EnumOption("Yaw mode", AntiAimMod.YawMode.JITTER);
      this.pitchMode = new EnumOption("Pitch mode", AntiAimMod.PitchMode.DOWN);
      this.addOptions(new Option[]{this.pitchMode, this.yawMode});
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      if (!mc.thePlayer.isSwingInProgress) {
         switch((AntiAimMod.YawMode)this.yawMode.getValue()) {
         case SPIN:
            this.yaw += 20.0F;
            if (this.yaw > 180.0F) {
               this.yaw = -180.0F;
            } else if (this.yaw < -180.0F) {
               this.yaw = 180.0F;
            }
            break;
         case JITTER:
            this.yaw = (float)(mc.thePlayer.ticksExisted % 2 == 0 ? 90 : -90);
         }

         event.setYaw(this.yaw);
         switch((AntiAimMod.PitchMode)this.pitchMode.getValue()) {
         case UP:
            this.pitch = -90.0F;
            break;
         case DOWN:
            this.pitch = 90.0F;
            break;
         case JITTER:
            this.pitch += 30.0F;
            if (this.pitch > 90.0F) {
               this.pitch = -90.0F;
            } else if (this.pitch < -90.0F) {
               this.pitch = 90.0F;
            }
         }

         event.setPitch(this.pitch);
      }

   }

   private static enum PitchMode {
      DOWN,
      UP,
      JITTER;
   }

   private static enum YawMode {
      JITTER,
      SPIN;
   }
}
