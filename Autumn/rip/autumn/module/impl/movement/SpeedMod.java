package rip.autumn.module.impl.movement;

import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.events.player.UpdateActionEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.MathUtils;
import rip.autumn.utils.MovementUtils;
import rip.autumn.utils.PlayerUtils;

@Label("Speed")
@Bind("V")
@Category(ModuleCategory.MOVEMENT)
public final class SpeedMod extends Module {
   public final EnumOption mode;
   public final DoubleOption bhopSpeed;
   private double moveSpeed;
   private double lastDist;
   private double y;
   private int stage;
   private int hops;
   private TargetStrafeMod targetStrafe;

   public SpeedMod() {
      this.mode = new EnumOption("Mode", SpeedMod.Mode.HYPIXEL);
      this.bhopSpeed = new DoubleOption("Bhop Speed", 0.5D, () -> {
         return this.mode.getValue() == SpeedMod.Mode.BHOP;
      }, 0.1D, 3.0D, 0.05D);
      this.setMode(this.mode);
      this.addOptions(new Option[]{this.mode, this.bhopSpeed});
   }

   public void onEnabled() {
      this.y = 0.0D;
      this.hops = 1;
      this.moveSpeed = MovementUtils.getBaseMoveSpeed();
      this.lastDist = 0.0D;
      this.stage = 0;
      if (this.targetStrafe == null) {
         this.targetStrafe = (TargetStrafeMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(TargetStrafeMod.class);
      }

   }

   public void onDisabled() {
      mc.thePlayer.stepHeight = 0.625F;
      mc.timer.timerSpeed = 1.0F;
   }

   @Listener(UpdateActionEvent.class)
   public final void onActionUpdate(UpdateActionEvent event) {
      if (event.isSneakState()) {
         event.setSneakState(false);
      }

   }

   @Listener(MoveEvent.class)
   public final void onMove(MoveEvent event) {
      EntityPlayerSP player = mc.thePlayer;
      if (!PlayerUtils.isInLiquid()) {
         if (player.isMoving()) {
            double rounded;
            double difference;
            label184:
            switch((SpeedMod.Mode)this.mode.getValue()) {
            case NCP:
               switch(this.stage) {
               case 2:
                  if (player.onGround && player.isCollidedVertically) {
                     event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.41999998688697815D);
                     this.moveSpeed *= 2.149D;
                     mc.timer.timerSpeed = 1.4F;
                  }
                  break label184;
               case 3:
                  rounded = 0.86D * (this.moveSpeed - MovementUtils.getBaseMoveSpeed());
                  this.moveSpeed = this.lastDist - rounded;
                  break label184;
               }

               if (mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0D, player.motionY, 0.0D)).size() > 0 || player.isCollidedVertically && player.onGround) {
                  this.stage = 1;
               }

               mc.timer.timerSpeed = 1.0F;
               break;
            case HYPIXEL:
               switch(this.stage) {
               case 2:
                  if (player.onGround && player.isCollidedVertically) {
                     mc.timer.timerSpeed = 1.0F;
                     event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.41999998688697815D);
                     this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 1.5D;
                  }
                  break;
               case 3:
                  rounded = 0.6200000047683716D * (this.lastDist - MovementUtils.getBaseMoveSpeed());
                  this.moveSpeed = this.lastDist + rounded;
                  break;
               default:
                  if (mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0D, player.motionY, 0.0D)).size() > 0 || player.isCollidedVertically && player.onGround) {
                     this.stage = 1;
                     mc.timer.timerSpeed = 1.085F;
                     ++this.hops;
                  }

                  this.moveSpeed = this.lastDist - this.lastDist / 149.0D;
               }

               this.moveSpeed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed());
               break;
            case PACKETHOP:
               switch(this.stage) {
               case 2:
                  mc.timer.timerSpeed = 2.0F;
                  this.moveSpeed = 0.0D;
                  break label184;
               case 3:
                  mc.timer.timerSpeed = 1.0F;
                  if (player.onGround && player.isCollidedVertically) {
                     event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.41999998688697815D);
                     this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 2.149D;
                  }
                  break label184;
               case 4:
                  rounded = 0.5600000023841858D * (this.lastDist - MovementUtils.getBaseMoveSpeed());
                  this.moveSpeed = this.lastDist - rounded;
                  break label184;
               }

               if (mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0D, player.motionY, 0.0D)).size() > 0 || player.isCollidedVertically && player.onGround) {
                  this.stage = 1;
                  mc.timer.timerSpeed = 1.085F;
               }

               if (player.motionY < 0.0D) {
                  player.motionY *= 1.1D;
               }

               this.moveSpeed = this.lastDist - this.lastDist / 50.0D;
               break;
            case MINEPLEX:
               switch(this.stage) {
               case 2:
                  if (player.onGround && player.isCollidedVertically) {
                     this.moveSpeed = 0.0D;
                  }

                  mc.timer.timerSpeed = 2.5F;
                  break;
               case 3:
                  mc.timer.timerSpeed = 1.0F;
                  this.moveSpeed = Math.min(0.3D * (double)this.hops, 0.97D);
                  break;
               default:
                  if (mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0D, player.motionY, 0.0D)).size() > 0 || player.isCollidedVertically && player.onGround) {
                     this.stage = 1;
                     ++this.hops;
                  }

                  this.moveSpeed -= 0.01D;
               }

               if (this.stage != 2) {
                  this.moveSpeed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed());
               }
               break;
            case VHOP:
               rounded = MathUtils.round(player.posY - (double)((int)player.posY), 3.0D);
               if (rounded == MathUtils.round(0.4D, 3.0D)) {
                  event.y = player.motionY = 0.31D;
               } else if (rounded == MathUtils.round(0.71D, 3.0D)) {
                  event.y = player.motionY = 0.04D;
               } else if (rounded == MathUtils.round(0.75D, 3.0D)) {
                  event.y = player.motionY = -0.2D;
               } else if (rounded == MathUtils.round(0.55D, 3.0D)) {
                  event.y = player.motionY = -0.14D;
               } else if (rounded == MathUtils.round(0.41D, 3.0D)) {
                  event.y = player.motionY = -0.2D;
               }

               switch(this.stage) {
               case 0:
                  if (player.onGround && player.isCollidedVertically) {
                     this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 1.3D;
                  }
                  break;
               case 1:
               default:
                  if (mc.theWorld.getCollidingBoundingBoxes(player, player.getEntityBoundingBox().offset(0.0D, player.motionY, 0.0D)).size() > 0 || player.isCollidedVertically && player.onGround) {
                     this.stage = 1;
                  }

                  this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                  break;
               case 2:
                  if (player.onGround && player.isCollidedVertically) {
                     event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.40001D);
                     this.moveSpeed *= 1.8D;
                  }

                  mc.timer.timerSpeed = 1.2F;
                  break;
               case 3:
                  mc.timer.timerSpeed = 1.0F;
                  difference = 0.72D * (this.lastDist - MovementUtils.getBaseMoveSpeed());
                  this.moveSpeed = this.lastDist - difference;
               }

               this.moveSpeed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed());
               break;
            case GROUND:
               if (this.canSpeed()) {
                  switch(this.stage) {
                  case 1:
                     mc.timer.timerSpeed = 1.0F;
                     this.y = MovementUtils.getJumpBoostModifier(0.40001D);
                     this.moveSpeed = MovementUtils.getBaseMoveSpeed() * 2.149D;
                     break label184;
                  case 2:
                     this.y = MovementUtils.getJumpBoostModifier(0.381D);
                     difference = 0.66D * (this.lastDist - MovementUtils.getBaseMoveSpeed());
                     this.moveSpeed = this.lastDist - difference;
                     break label184;
                  case 3:
                     this.y = MovementUtils.getJumpBoostModifier(0.22D);
                     this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                     break label184;
                  case 4:
                     this.y = MovementUtils.getJumpBoostModifier(0.11D);
                     this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                     break label184;
                  case 5:
                     mc.timer.timerSpeed = 2.0F;
                     this.y = 0.0D;
                     this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                     this.stage = 0;
                  }
               } else {
                  this.y = 0.0D;
                  this.moveSpeed = MovementUtils.getBaseMoveSpeed();
                  this.stage = 0;
                  mc.timer.timerSpeed = 1.0F;
               }
               break;
            case BHOP:
               if (player.onGround && player.isCollidedVertically) {
                  event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.41999998688697815D);
                  this.moveSpeed = (Double)this.bhopSpeed.getValue() * MovementUtils.getBaseMoveSpeed();
               } else {
                  this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
               }

               this.moveSpeed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed());
            }

            if (this.targetStrafe.canStrafe()) {
               this.targetStrafe.strafe(event, this.moveSpeed);
            } else {
               MovementUtils.setSpeed(event, this.moveSpeed);
            }

            ++this.stage;
         }

      }
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      if (event.isPre()) {
         if (PlayerUtils.isInLiquid()) {
            return;
         }

         EntityPlayerSP player = mc.thePlayer;
         if (player.isMoving()) {
            if (this.mode.getValue() == SpeedMod.Mode.HYPIXEL) {
               if (player.isCollidedVertically) {
                  event.setPosY(mc.thePlayer.posY + 7.435E-4D);
               }
            } else if (this.mode.getValue() == SpeedMod.Mode.MINEPLEX) {
               mc.thePlayer.stepHeight = 0.0F;
               if (mc.thePlayer.isCollidedHorizontally) {
                  this.moveSpeed = 0.0D;
                  this.hops = 1;
               }

               if (mc.thePlayer.fallDistance < 8.0F) {
                  mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.4D, mc.thePlayer.posZ);
                  EntityPlayerSP var10000 = mc.thePlayer;
                  var10000.motionY += 0.02D;
               } else {
                  this.moveSpeed = 0.0D;
                  this.hops = 1;
                  mc.thePlayer.motionY = -1.0D;
               }
            } else if (this.mode.getValue() == SpeedMod.Mode.GROUND && this.canSpeed()) {
               event.setOnGround(this.stage == 5);
               event.setPosY(mc.thePlayer.posY + this.y + 7.435E-4D);
            }
         }

         double xDist = player.posX - player.prevPosX;
         double zDist = player.posZ - player.prevPosZ;
         this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      }

   }

   private boolean canSpeed() {
      Block blockBelow = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)).getBlock();
      return mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isPressed() && blockBelow != Blocks.stone_stairs && blockBelow != Blocks.oak_stairs && blockBelow != Blocks.sandstone_stairs && blockBelow != Blocks.nether_brick_stairs && blockBelow != Blocks.spruce_stairs && blockBelow != Blocks.stone_brick_stairs && blockBelow != Blocks.birch_stairs && blockBelow != Blocks.jungle_stairs && blockBelow != Blocks.acacia_stairs && blockBelow != Blocks.brick_stairs && blockBelow != Blocks.dark_oak_stairs && blockBelow != Blocks.quartz_stairs && blockBelow != Blocks.red_sandstone_stairs && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 2.0D, mc.thePlayer.posZ)).getBlock() == Blocks.air;
   }

   private static enum Mode {
      HYPIXEL,
      MINEPLEX,
      VHOP,
      NCP,
      BHOP,
      PACKETHOP,
      GROUND;
   }
}
