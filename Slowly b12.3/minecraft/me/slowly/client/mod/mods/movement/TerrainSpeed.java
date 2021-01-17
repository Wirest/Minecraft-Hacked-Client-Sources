package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.value.Value;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class TerrainSpeed extends Mod {
   public static Value mode = new Value("TerrainSpeed", "Mode", 0);
   public Value stair = new Value("TerrainSpeed_Stair", true);
   public Value slab = new Value("TerrainSpeed_Slab", true);
   public Value wall = new Value("TerrainSpeed_Wall", true);
   public Value cover = new Value("TerrainSpeed_Cover", true);
   public Value ice = new Value("TerrainSpeed_Ice", true);
   private Value modeIce = new Value("TerrainSpeed", "Mode", 0);
   private double currentSpeed;
   private double maxspeed;

   public TerrainSpeed() {
      super("TerrainSpeed", Mod.Category.MOVEMENT, Colors.ORANGE.c);
      mode.mode.add("Normal");
      mode.mode.add("AAC3.3.8");
      this.modeIce.mode.add("OldAAC");
      this.modeIce.mode.add("NCP");
   }
   

   @EventTarget
   public void onPre(EventPreMotion event) {
      if (((Boolean)this.stair.getValueState()).booleanValue()) {
         BlockPos s = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.1D, this.mc.thePlayer.posZ);
         boolean onStairs = this.mc.theWorld.getBlockState(s).getBlock() instanceof BlockStairs;
         if (!PlayerUtil.MovementInput() || !this.mc.thePlayer.onGround || this.currentSpeed <= 0.0D && !onStairs) {
            this.currentSpeed = 0.0D;
         } else if (onStairs) {
            if (this.mc.thePlayer.onGround) {
               this.mc.thePlayer.motionY = -9.0D;
            }

            double maxSpeed = 3.0D;
            this.currentSpeed += 0.30000000000000004D;
            if (this.currentSpeed > maxSpeed) {
               this.currentSpeed = (double)((float)maxSpeed);
            }

            PlayerUtil.setSpeed(this.currentSpeed);
            this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
         } else {
            if (this.mc.thePlayer.onGround) {
               this.mc.thePlayer.motionY = -9.0D;
            }

            PlayerUtil.setSpeed(this.currentSpeed);
            this.mc.thePlayer.motionX /= 1.02D;
            this.mc.thePlayer.motionZ /= 1.02D;
            this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
         }

         if (((Boolean)this.slab.getValueState()).booleanValue()) {
            BlockPos slab = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.1D, this.mc.thePlayer.posZ);
            boolean onSlab = this.mc.theWorld.getBlockState(slab).getBlock() instanceof BlockSlab;
            if (!PlayerUtil.MovementInput() || !this.mc.thePlayer.onGround || this.currentSpeed <= 0.0D && !onSlab) {
               this.currentSpeed = 0.0D;
            } else if (onSlab) {
               if (this.mc.thePlayer.onGround) {
                  this.mc.thePlayer.motionY = -9.0D;
               }

               double maxSpeed = 3.0D;
               this.currentSpeed += 0.05D;
               if (this.currentSpeed > maxSpeed) {
                  this.currentSpeed = (double)((float)maxSpeed);
               }

               PlayerUtil.setSpeed(this.currentSpeed);
               this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
            } else {
               if (this.mc.thePlayer.onGround) {
                  this.mc.thePlayer.motionY = -9.0D;
               }

               PlayerUtil.setSpeed(this.currentSpeed);
               this.mc.thePlayer.motionX /= 1.02D;
               this.mc.thePlayer.motionZ /= 1.02D;
               this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
            }

            if (((Boolean)this.cover.getValueState()).booleanValue()) {
               BlockPos c = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 2.0D, this.mc.thePlayer.posZ);
               boolean underCorner = this.mc.theWorld.getBlockState(c).getBlock() != Blocks.air;
               if (PlayerUtil.MovementInput() && this.mc.thePlayer.onGround && (this.currentSpeed > 0.0D || underCorner)) {
                  if (underCorner) {
                     if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.motionY = -9.0D;
                     }

                     double maxSpeed = 3.0D;
                     this.currentSpeed += 0.30000000000000004D;
                     if (this.currentSpeed > maxSpeed) {
                        this.currentSpeed = (double)((float)maxSpeed);
                     }

                     PlayerUtil.setSpeed(this.currentSpeed);
                     this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
                  } else {
                     if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.motionY = -9.0D;
                     }

                     PlayerUtil.setSpeed(this.currentSpeed);
                     this.mc.thePlayer.motionX /= 1.02D;
                     this.mc.thePlayer.motionZ /= 1.02D;
                     this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
                  }
               } else {
                  this.currentSpeed = 0.0D;
               }

               if (((Boolean)this.wall.getValueState()).booleanValue()) {
                  BlockPos pos2;
                  BlockPos pos3;
                  BlockPos pos4;
                  BlockPos pos5;
                  BlockPos pos6;
                  BlockPos pos7;
                  BlockPos pos8;
                  Block block1;
                  Block block2;
                  Block block3;
                  Block block4;
                  Block block5;
                  Block block6;
                  Block block7;
                  Block block8;
                  boolean collide;
                  BlockPos pos1;
                  if (mode.isCurrentMode("Normal")) {
                     pos1 = new BlockPos(this.mc.thePlayer.posX + 0.5D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                     pos2 = new BlockPos(this.mc.thePlayer.posX - 0.5D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                     pos3 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + 0.5D);
                     pos4 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ - 0.5D);
                     pos5 = new BlockPos(this.mc.thePlayer.posX + 0.5D, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ);
                     pos6 = new BlockPos(this.mc.thePlayer.posX - 0.5D, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ);
                     pos7 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ + 0.5D);
                     pos8 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ - 0.5D);
                     block1 = this.mc.theWorld.getBlockState(pos1).getBlock();
                     block2 = this.mc.theWorld.getBlockState(pos2).getBlock();
                     block3 = this.mc.theWorld.getBlockState(pos3).getBlock();
                     block4 = this.mc.theWorld.getBlockState(pos4).getBlock();
                     block5 = this.mc.theWorld.getBlockState(pos5).getBlock();
                     block6 = this.mc.theWorld.getBlockState(pos6).getBlock();
                     block7 = this.mc.theWorld.getBlockState(pos7).getBlock();
                     block8 = this.mc.theWorld.getBlockState(pos8).getBlock();
                     collide = !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && (block1 != Blocks.air || block2 != Blocks.air || block3 != Blocks.air || block4 != Blocks.air) && (block5 != Blocks.air || block6 != Blocks.air || block7 != Blocks.air || block8 != Blocks.air);
                     if (PlayerUtil.MovementInput() && (this.currentSpeed > 0.0D || collide)) {
                        if (collide) {
                           double maxSpeed = 2.0D;
                           this.currentSpeed += 0.15000000000000002D;
                           if (this.currentSpeed > maxSpeed) {
                              this.currentSpeed = (double)((float)maxSpeed);
                           }

                           PlayerUtil.setSpeed(this.currentSpeed);
                           this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
                        } else {
                           if (this.mc.thePlayer.onGround) {
                              this.mc.thePlayer.motionY = -9.0D;
                           }

                           PlayerUtil.setSpeed(this.currentSpeed);
                           this.mc.thePlayer.motionX /= 1.02D;
                           this.mc.thePlayer.motionZ /= 1.02D;
                           this.currentSpeed = (double)((float)PlayerUtil.getSpeed());
                        }
                     } else {
                        this.currentSpeed = 0.0D;
                     }
                  } else if (mode.isCurrentMode("AAC3.3.8")) {
                     pos1 = new BlockPos(this.mc.thePlayer.posX + 0.5D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                     pos2 = new BlockPos(this.mc.thePlayer.posX - 0.5D, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
                     pos3 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + 0.5D);
                     pos4 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ - 0.5D);
                     pos5 = new BlockPos(this.mc.thePlayer.posX + 0.5D, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ);
                     pos6 = new BlockPos(this.mc.thePlayer.posX - 0.5D, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ);
                     pos7 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ + 0.5D);
                     pos8 = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0D, this.mc.thePlayer.posZ - 0.5D);
                     block1 = this.mc.theWorld.getBlockState(pos1).getBlock();
                     block2 = this.mc.theWorld.getBlockState(pos2).getBlock();
                     block3 = this.mc.theWorld.getBlockState(pos3).getBlock();
                     block4 = this.mc.theWorld.getBlockState(pos4).getBlock();
                     block5 = this.mc.theWorld.getBlockState(pos5).getBlock();
                     block6 = this.mc.theWorld.getBlockState(pos6).getBlock();
                     block7 = this.mc.theWorld.getBlockState(pos7).getBlock();
                     block8 = this.mc.theWorld.getBlockState(pos8).getBlock();
                     collide = !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && (block1 != Blocks.air || block2 != Blocks.air || block3 != Blocks.air || block4 != Blocks.air) && (block5 != Blocks.air || block6 != Blocks.air || block7 != Blocks.air || block8 != Blocks.air);
                     if (PlayerUtil.MovementInput() && collide && collide) {
                        BlockPos below = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.8D, this.mc.thePlayer.posZ);
                        if (!(this.mc.theWorld.getBlockState(below).getBlock() instanceof BlockAir) && !this.mc.thePlayer.isBlocking()) {
                           this.mc.thePlayer.posY = (double)(below.getY() + 1);
                        }

                        if (this.mc.thePlayer.onGround && PlayerUtil.MovementInput()) {
                           this.mc.thePlayer.jump();
                        } else {
                           this.mc.thePlayer.motionY = -0.21D;
                        }
                     }
                  }
               }
            }
         }
      }

   }

   @EventTarget
   public void onUpdate(UpdateEvent event) {
      BlockPos BlockPos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0D, this.mc.thePlayer.posZ);
      if (this.mc.theWorld.getBlockState(BlockPos).getBlock() == Blocks.ice || this.mc.theWorld.getBlockState(BlockPos).getBlock() == Blocks.packed_ice) {
         if (mode.isCurrentMode("OldAAC")) {
            this.mc.thePlayer.motionX *= 1.15D;
            this.mc.thePlayer.motionZ *= 1.15D;
            if (!PlayerUtil.MovementInput()) {
               this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = 0.0D;
            }
         } else if (mode.isCurrentMode("NCP")) {
            Blocks.ice.slipperiness = 0.39F;
            Blocks.packed_ice.slipperiness = 0.39F;
         }
      }

   }
   public void onEnable() {
   	super.isEnabled();
       ClientUtil.sendClientMessage("TerrainSpeed Enable", ClientNotification.Type.SUCCESS);
   }

   public void onDisable() {
      super.onDisable();
      Blocks.ice.slipperiness = 0.98F;
      Blocks.packed_ice.slipperiness = 0.98F;
      ClientUtil.sendClientMessage("TerrainSpeed Disable", ClientNotification.Type.ERROR);
   }
}

