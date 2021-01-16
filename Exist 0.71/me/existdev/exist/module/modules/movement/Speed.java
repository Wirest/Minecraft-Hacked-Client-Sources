package me.existdev.exist.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import me.existdev.exist.Exist;
import me.existdev.exist.events.EventMove;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;

public class Speed extends Module {
   // $FF: synthetic field
   TimeHelper time;
   // $FF: synthetic field
   ArrayList Modes = new ArrayList();
   // $FF: synthetic field
   private double moveSpeed;
   // $FF: synthetic field
   private double lastDist;
   // $FF: synthetic field
   private int stage;

   // $FF: synthetic method
   public Speed() {
      super("Speed", 0, Module.Category.Movement);
      this.Modes.add("AAC3.3.9FastHop");
      this.Modes.add("AAC3.3.9BHop");
      this.Modes.add("AAC3.3.9SlowHop");
      this.Modes.add("NCPBhop");
      this.Modes.add("NCPBhop2");
      this.Modes.add("NCPSmoothHop");
      this.Modes.add("NCPY-Port");
      this.Modes.add("NCPLowHop");
      this.Modes.add("Mineplex");
      this.Modes.add("GommeHD");
      this.time = new TimeHelper();
      Exist.settingManager.addSetting(new Setting(this, "Mode", "NCPLowHop", this.Modes));
   }

   // $FF: synthetic method
   public void onUpdate() {
      if(this.isToggled()) {
         String options = Exist.settingManager.getSetting(this, "Mode").getCurrentOption();
         if(!mc.gameSettings.keyBindJump.pressed) {
            if(options.equalsIgnoreCase("AAC3.3.9FastHop")) {
               this.AAC339FastHop();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AAC3.3.9FastHop");
            }

            if(options.equalsIgnoreCase("AAC3.3.9BHop")) {
               this.AAC339FastHop();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AAC3.3.9BHop");
            }

            if(options.equalsIgnoreCase("AAC3.3.9SlowHop")) {
               this.AAC339SlowHop();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AAC3.3.9SlowHop");
            }

            if(options.equalsIgnoreCase("NCPY-Port")) {
               this.NCPYPort();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCPY-Port");
            }

            if(options.equalsIgnoreCase("NCPLowHop")) {
               this.NCPLowHop();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCPLowHop");
            }

            if(options.equalsIgnoreCase("NCPBhop")) {
               this.NCPBhop();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCPBhop");
            }

            if(options.equalsIgnoreCase("NCPBhop2")) {
               this.NCPBhop2();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCPBhop2");
            }

            if(options.equalsIgnoreCase("Mineplex")) {
               this.Mineplex();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Mineplex");
            }

            if(options.equalsIgnoreCase("GommeHD")) {
               this.GommeHD();
               this.setDisplayName(this.getName() + ChatFormatting.WHITE + " GommeHD");
            }

            super.onUpdate();
         }
      }
   }

   // $FF: synthetic method
   private void AAC339FastHop() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.onGround) {
         var10000 = mc;
         Minecraft.thePlayer.jump();
      }

      var10000 = mc;
      if(!Minecraft.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed && (mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindLeft.pressed)) {
         this.setSpeed(0.25D);
         mc.timer.timerSpeed = 1.0F;
      }

      var10000 = mc;
      if(!Minecraft.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed && mc.gameSettings.keyBindForward.pressed) {
         var10000 = mc;
         if(Minecraft.thePlayer.hurtTime <= 0) {
            var10000 = mc;
            if(Minecraft.thePlayer.onGround) {
               var10000 = mc;
               Minecraft.thePlayer.motionX *= 1.0099999904632568D;
               var10000 = mc;
               Minecraft.thePlayer.motionZ *= 1.0099999904632568D;
            } else {
               var10000 = mc;
               if(Minecraft.thePlayer.motionY > 0.0D) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY -= 0.014999999664723873D;
               } else {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY -= 0.01489999983459711D;
                  mc.timer.timerSpeed = 1.08F;
                  Minecraft var10002 = mc;
                  Minecraft var10003 = mc;
                  double var2 = Minecraft.thePlayer.posY - 0.6D;
                  Minecraft var10004 = mc;
                  BlockPos willBe = new BlockPos(Minecraft.thePlayer.posX, var2, Minecraft.thePlayer.posZ);
                  var10000 = mc;
                  double var3 = Minecraft.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMaxY();
                  Minecraft var10001 = mc;
                  if(var3 == Minecraft.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMinY() + 1.0D) {
                     var10000 = mc;
                     if(!Minecraft.theWorld.getBlockState(willBe).getBlock().isTranslucent()) {
                        var10000 = mc;
                        if(Minecraft.theWorld.getBlockState(willBe).getBlock() != Blocks.water) {
                           var10000 = mc;
                           if(!(Minecraft.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab)) {
                              var10000 = mc;
                              Minecraft.thePlayer.motionY = -1.0D;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   private void AAC339BHop() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.onGround) {
         var10000 = mc;
         Minecraft.thePlayer.jump();
      }

      var10000 = mc;
      if(!Minecraft.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed && (mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindLeft.pressed)) {
         this.setSpeed(0.25D);
         mc.timer.timerSpeed = 1.0F;
      }

      var10000 = mc;
      if(!Minecraft.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed && mc.gameSettings.keyBindForward.pressed) {
         var10000 = mc;
         if(Minecraft.thePlayer.hurtTime <= 0) {
            var10000 = mc;
            if(Minecraft.thePlayer.onGround) {
               var10000 = mc;
               Minecraft.thePlayer.motionX *= 1.0099999904632568D;
               var10000 = mc;
               Minecraft.thePlayer.motionZ *= 1.0099999904632568D;
            } else {
               var10000 = mc;
               if(Minecraft.thePlayer.motionY > 0.0D) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY -= 0.014999999664723873D;
               } else {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY -= 0.01489999983459711D;
                  mc.timer.timerSpeed = 1.04F;
                  Minecraft var10002 = mc;
                  Minecraft var10003 = mc;
                  double var2 = Minecraft.thePlayer.posY - 0.6D;
                  Minecraft var10004 = mc;
                  BlockPos willBe = new BlockPos(Minecraft.thePlayer.posX, var2, Minecraft.thePlayer.posZ);
                  var10000 = mc;
                  double var3 = Minecraft.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMaxY();
                  Minecraft var10001 = mc;
                  if(var3 == Minecraft.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMinY() + 1.0D) {
                     var10000 = mc;
                     if(!Minecraft.theWorld.getBlockState(willBe).getBlock().isTranslucent()) {
                        var10000 = mc;
                        if(Minecraft.theWorld.getBlockState(willBe).getBlock() != Blocks.water) {
                           var10000 = mc;
                           if(!(Minecraft.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab)) {
                              var10000 = mc;
                              Minecraft.thePlayer.motionY = -1.0D;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   private void AAC339SlowHop() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.onGround) {
         var10000 = mc;
         Minecraft.thePlayer.jump();
      }

      var10000 = mc;
      if(!Minecraft.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed && (mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindLeft.pressed)) {
         this.setSpeed(0.25D);
         mc.timer.timerSpeed = 1.0F;
      }

      var10000 = mc;
      if(!Minecraft.thePlayer.isCollidedHorizontally && !mc.gameSettings.keyBindJump.pressed && mc.gameSettings.keyBindForward.pressed) {
         var10000 = mc;
         if(Minecraft.thePlayer.hurtTime <= 0) {
            var10000 = mc;
            if(Minecraft.thePlayer.onGround) {
               var10000 = mc;
               Minecraft.thePlayer.motionX *= 1.0099999904632568D;
               var10000 = mc;
               Minecraft.thePlayer.motionZ *= 1.0099999904632568D;
            } else {
               var10000 = mc;
               if(Minecraft.thePlayer.motionY > 0.0D) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY -= 0.014999999664723873D;
               } else {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY -= 0.01489999983459711D;
                  Minecraft var10002 = mc;
                  Minecraft var10003 = mc;
                  double var2 = Minecraft.thePlayer.posY - 0.6D;
                  Minecraft var10004 = mc;
                  BlockPos willBe = new BlockPos(Minecraft.thePlayer.posX, var2, Minecraft.thePlayer.posZ);
                  var10000 = mc;
                  double var3 = Minecraft.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMaxY();
                  Minecraft var10001 = mc;
                  if(var3 == Minecraft.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMinY() + 1.0D) {
                     var10000 = mc;
                     if(!Minecraft.theWorld.getBlockState(willBe).getBlock().isTranslucent()) {
                        var10000 = mc;
                        if(Minecraft.theWorld.getBlockState(willBe).getBlock() != Blocks.water) {
                           var10000 = mc;
                           if(!(Minecraft.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab)) {
                              var10000 = mc;
                              Minecraft.thePlayer.motionY = -0.7D;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   private void GommeHD() {
      Minecraft var10002 = mc;
      Minecraft var10003 = mc;
      double var2 = Minecraft.thePlayer.posY - 1.0D;
      Minecraft var10004 = mc;
      BlockPos bp = new BlockPos(Minecraft.thePlayer.posX, var2, Minecraft.thePlayer.posZ);
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isMoving()) {
         if(!MovementInput.jump) {
            var10000 = mc;
            if(!Minecraft.thePlayer.isCollidedHorizontally) {
               var10000 = mc;
               if(Minecraft.theWorld.getBlockState(bp).getBlock() == Blocks.air) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY = -9.899999618530273D;
               }

               var10000 = mc;
               if(Minecraft.thePlayer.onGround) {
                  var10000 = mc;
                  Minecraft.thePlayer.capabilities.allowFlying = true;
                  var10000 = mc;
                  Minecraft.thePlayer.capabilities.isFlying = true;
               }

            }
         }
      }
   }

   // $FF: synthetic method
   private void Mineplex() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isMoving()) {
         if(!mc.gameSettings.keyBindJump.pressed) {
            var10000 = mc;
            if(Minecraft.thePlayer.onGround) {
               var10000 = mc;
               Minecraft.thePlayer.motionY = 0.4000000059604645D;
            }

            this.setSpeed(0.41999998688697815D);
         }
      }
   }

   // $FF: synthetic method
   private void NCPBhop() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isMoving()) {
         this.setSpeed(0.27D);
         var10000 = mc;
         if(Minecraft.thePlayer.onGround) {
            var10000 = mc;
            Minecraft.thePlayer.motionY = 0.4000000059604645D;
         }

      }
   }

   // $FF: synthetic method
   private void NCPBhop2() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isMoving()) {
         this.setSpeed(0.27D);
         var10000 = mc;
         if(Minecraft.thePlayer.onGround) {
            var10000 = mc;
            Minecraft.thePlayer.motionY = 0.4000000059604645D;
         }

         mc.timer.timerSpeed = 1.09F;
      }
   }

   // $FF: synthetic method
   private void NCPYPort() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isMoving()) {
         var10000 = mc;
         if(!Minecraft.thePlayer.isCollidedHorizontally) {
            var10000 = mc;
            if(Minecraft.thePlayer.onGround) {
               var10000 = mc;
               Minecraft.thePlayer.jump();
            } else {
               var10000 = mc;
               Minecraft.thePlayer.motionY = -0.41999998688697815D;
            }

         }
      }
   }

   // $FF: synthetic method
   public void NCPLowHop() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.isMoving()) {
         var10000 = mc;
         if(Minecraft.thePlayer.onGround) {
            this.setSpeed(0.27D);
            mc.timer.timerSpeed = 1.2F;
            var10000 = mc;
            Minecraft.thePlayer.addVelocity(0.0D, 0.3D, 0.0D);
            var10000 = mc;
            Minecraft.thePlayer.onGround = false;
         } else {
            this.setSpeed(0.15D);
            var10000 = mc;
            Minecraft.thePlayer.addVelocity(0.0D, -0.05D, 0.0D);
            var10000 = mc;
            Minecraft.thePlayer.onGround = true;
         }

      }
   }

   // $FF: synthetic method
   @EventTarget
   public void onMove(EventMove event) {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCPSmoothHop")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCPSmoothHop");
            Minecraft var10000 = mc;
            if(!Minecraft.thePlayer.isCollidedHorizontally) {
               var10000 = mc;
               if(Minecraft.thePlayer.moveForward == 0.0F) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.moveStrafing == 0.0F) {
                     this.moveSpeed = 0.2873D;
                  }
               }

               Minecraft var10001 = mc;
               Minecraft var10002 = mc;
               if(this.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == this.round(0.4D, 3)) {
                  var10001 = mc;
                  event.setY(Minecraft.thePlayer.motionY = 0.31D);
               } else {
                  var10001 = mc;
                  var10002 = mc;
                  if(this.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == this.round(0.71D, 3)) {
                     var10001 = mc;
                     event.setY(Minecraft.thePlayer.motionY = 0.04D);
                  } else {
                     var10001 = mc;
                     var10002 = mc;
                     if(this.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == this.round(0.75D, 3)) {
                        var10001 = mc;
                        event.setY(Minecraft.thePlayer.motionY = -0.2D);
                     }
                  }
               }

               var10000 = mc;
               var10001 = mc;
               var10002 = mc;
               List collidingList = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0D, -0.56D, 0.0D));
               if(collidingList.size() > 0) {
                  var10001 = mc;
                  var10002 = mc;
                  if(this.round(Minecraft.thePlayer.posY - (double)((int)Minecraft.thePlayer.posY), 3) == this.round(0.55D, 3)) {
                     event.setY(-0.14D);
                  }
               }

               if(this.stage == 1) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.isCollidedVertically) {
                     label124: {
                        var10000 = mc;
                        if(Minecraft.thePlayer.moveForward == 0.0F) {
                           var10000 = mc;
                           if(Minecraft.thePlayer.moveStrafing == 0.0F) {
                              break label124;
                           }
                        }

                        this.moveSpeed = 0.6048220000000001D;
                     }
                  }
               }

               double forward;
               label119: {
                  label118: {
                     if(this.stage == 2) {
                        var10000 = mc;
                        if(Minecraft.thePlayer.isCollidedVertically) {
                           var10000 = mc;
                           if(Minecraft.thePlayer.moveForward != 0.0F) {
                              break label118;
                           }

                           var10000 = mc;
                           if(Minecraft.thePlayer.moveStrafing != 0.0F) {
                              break label118;
                           }
                        }
                     }

                     if(this.stage == 3) {
                        forward = 0.66D * (this.lastDist - 0.2873D);
                        this.moveSpeed = this.lastDist - forward;
                     } else {
                        label136: {
                           var10000 = mc;
                           var10001 = mc;
                           var10002 = mc;
                           Minecraft var10004 = mc;
                           List var8 = Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.boundingBox.offset(0.0D, Minecraft.thePlayer.motionY, 0.0D));
                           if(var8.size() <= 0) {
                              var10000 = mc;
                              if(!Minecraft.thePlayer.isCollidedVertically) {
                                 break label136;
                              }
                           }

                           if(this.stage > 0) {
                              byte var9;
                              label104: {
                                 var10001 = mc;
                                 if(Minecraft.thePlayer.moveForward == 0.0F) {
                                    var10001 = mc;
                                    if(Minecraft.thePlayer.moveStrafing == 0.0F) {
                                       var9 = 0;
                                       break label104;
                                    }
                                 }

                                 var9 = 1;
                              }

                              this.stage = var9;
                           }
                        }

                        this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                     }
                     break label119;
                  }

                  var10001 = mc;
                  event.setY(Minecraft.thePlayer.motionY = 0.4D);
                  this.moveSpeed *= 1.5563D;
               }

               this.moveSpeed = Math.max(this.moveSpeed, 0.2873D);
               if(this.stage > 0) {
                  forward = (double)MovementInput.moveForward;
                  double strafe = (double)MovementInput.moveStrafe;
                  var10000 = mc;
                  float yaw = Minecraft.thePlayer.rotationYaw;
                  if(forward == 0.0D && strafe == 0.0D) {
                     event.setX(0.0D);
                     event.setZ(0.0D);
                  } else {
                     if(forward != 0.0D) {
                        if(strafe > 0.0D) {
                           yaw += (float)(forward > 0.0D?-45:45);
                        } else if(strafe < 0.0D) {
                           yaw += (float)(forward > 0.0D?45:-45);
                        }

                        strafe = 0.0D;
                        if(forward > 0.0D) {
                           forward = 1.0D;
                        } else if(forward < 0.0D) {
                           forward = -1.0D;
                        }
                     }

                     event.setX(forward * this.moveSpeed * Math.cos(Math.toRadians((double)(yaw + 90.0F))) + strafe * this.moveSpeed * Math.sin(Math.toRadians((double)(yaw + 90.0F))));
                     event.setZ(forward * this.moveSpeed * Math.sin(Math.toRadians((double)(yaw + 90.0F))) - strafe * this.moveSpeed * Math.cos(Math.toRadians((double)(yaw + 90.0F))));
                  }
               }

               var10000 = mc;
               if(Minecraft.thePlayer.moveForward == 0.0F) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.moveStrafing == 0.0F) {
                     return;
                  }
               }

               ++this.stage;
            }
         }
      }
   }

   // $FF: synthetic method
   @EventTarget
   public void onUpdate(EventPreMotionUpdates event) {
      if(event.getType() == EventPreMotionUpdates.EventType.PRE) {
         Minecraft var10000 = mc;
         Minecraft var10001 = mc;
         double xDist = Minecraft.thePlayer.posX - Minecraft.thePlayer.prevPosX;
         var10000 = mc;
         var10001 = mc;
         double zDist = Minecraft.thePlayer.posZ - Minecraft.thePlayer.prevPosZ;
         this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
      }

   }

   // $FF: synthetic method
   public double round(double value, int places) {
      if(places < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal bd = new BigDecimal(value);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         return bd.doubleValue();
      }
   }

   // $FF: synthetic method
   public void onEnable() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         this.moveSpeed = 0.2873D;
      }

      this.lastDist = 0.0D;
      this.stage = 2;
      mc.timer.timerSpeed = 1.0F;
      this.time.reset();
      super.onEnable();
   }

   // $FF: synthetic method
   public void onDisable() {
      mc.timer.timerSpeed = 1.0F;
      Minecraft var10000 = mc;
      Minecraft.thePlayer.capabilities.allowFlying = false;
      var10000 = mc;
      Minecraft.thePlayer.capabilities.isFlying = false;
      this.time.reset();
      super.onDisable();
   }
}
