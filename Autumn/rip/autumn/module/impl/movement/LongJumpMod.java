package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import rip.autumn.annotations.Label;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.MovementUtils;

@Label("Long Jump")
@Bind("Z")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"longjump", "lj"})
public final class LongJumpMod extends Module {
   public final EnumOption mode;
   public final BoolOption hypixel;
   private double lastDif;
   private double moveSpeed;
   private int stage;
   private int groundTicks;

   public LongJumpMod() {
      this.mode = new EnumOption("Mode", LongJumpMod.Mode.NCP);
      this.hypixel = new BoolOption("Hypixel", true, () -> {
         return this.mode.getValue() == LongJumpMod.Mode.NCP;
      });
      this.setMode(this.mode);
      this.addOptions(new Option[]{this.mode, this.hypixel});
   }

   public void onEnabled() {
      this.lastDif = 0.0D;
      this.moveSpeed = 0.0D;
      this.stage = 0;
      this.groundTicks = 1;
   }

   public void onDisabled() {
   }

   @Listener(MoveEvent.class)
   public final void onMove(MoveEvent event) {
      if (this.isEnabled()) {
         EntityPlayerSP player = mc.thePlayer;
         boolean watchdog = this.hypixel.getValue();
         if (player.isMoving()) {
            switch(this.stage) {
            case 0:
            case 1:
               this.moveSpeed = 0.0D;
               break;
            case 2:
               if (player.onGround && player.isCollidedVertically) {
                  event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.3999999463558197D);
                  this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 2.0D;
               }
               break;
            case 3:
               this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 2.1489999294281006D;
               break;
            case 4:
               if (watchdog) {
                  this.moveSpeed *= 1.600000023841858D;
               }
               break;
            default:
               if (player.motionY < 0.0D) {
                  player.motionY *= 0.5D;
               }

               this.moveSpeed = this.lastDif - this.lastDif / 159.0D;
            }

            this.moveSpeed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed());
            ++this.stage;
         }

         MovementUtils.setSpeed(event, this.moveSpeed);
      }

   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      EntityPlayerSP player = mc.thePlayer;
      if (event.isPre()) {
         if (player.onGround && player.isCollidedVertically) {
            event.setPosY(event.getPosY() + 7.435E-4D);
         }

         double xDif = player.posX - player.prevPosX;
         double zDif = player.posZ - player.prevPosZ;
         this.lastDif = Math.sqrt(xDif * xDif + zDif * zDif);
         if (player.isMoving() && player.onGround && player.isCollidedVertically && this.stage > 2) {
            ++this.groundTicks;
         }

         if (this.groundTicks > 1) {
            this.toggle();
         }
      }

   }

   private static enum Mode {
      NCP;
   }
}
