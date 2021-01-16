package me.existdev.exist.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.BlockUtils;
import me.existdev.exist.utils.RotationUtils;
import me.existdev.exist.utils.TimerUtils2;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

public class ScaffoldWalk extends Module {
   // $FF: synthetic field
   ArrayList Modes = new ArrayList();
   // $FF: synthetic field
   private List invalid;
   // $FF: synthetic field
   private TimerUtils2 time;
   // $FF: synthetic field
   private BlockUtils blockData;
   // $FF: synthetic field
   public EnumFacing enumFacing;
   // $FF: synthetic field
   public BlockPos pos;
   // $FF: synthetic field
   int delay;
   // $FF: synthetic field
   ScaffoldWalk.BlockData scaffoldHelper;
   // $FF: synthetic field
   public static int placedblocks = 0;

   // $FF: synthetic method
   public ScaffoldWalk() {
      super("ScaffoldWalk", 0, Module.Category.World);
      this.invalid = Arrays.asList(new Block[]{Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.gravel});
      this.time = new TimerUtils2();
      this.Modes.add("AACFast");
      this.Modes.add("AAC");
      this.Modes.add("NCP");
      this.Modes.add("Mineplex");
      Exist.settingManager.addSetting(new Setting(this, "Mode", "AACFast", this.Modes));
      Exist.settingManager.addSetting(new Setting(this, "Switch", true));
      Exist.settingManager.addSetting(new Setting(this, "Swing", true));
   }

   // $FF: synthetic method
   public void onEnable() {
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer != null) {
         var10000 = mc;
         if(Minecraft.theWorld != null) {
            this.scaffoldHelper = null;
            this.delay = 0;
            placedblocks = 0;
            super.onEnable();
         }
      }

   }

   // $FF: synthetic method
   public void onDisable() {
      this.scaffoldHelper = null;
      placedblocks = 0;
      Minecraft var10000 = mc;
      Minecraft var10003 = mc;
      Minecraft var10004 = mc;
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, false));
      super.onDisable();
   }

   // $FF: synthetic method
   @EventTarget
   public void onPreMotion(EventPreMotionUpdates event) {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACFast")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AACFast");
         } else if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " Mineplex");
         }

         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACFast") || Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) {
            this.blockData = null;
            Minecraft var10000 = mc;
            if(!Minecraft.thePlayer.isSneaking()) {
               Minecraft var10002 = mc;
               Minecraft var10003 = mc;
               double var5 = Minecraft.thePlayer.posY - 1.0D;
               Minecraft var10004 = mc;
               BlockPos blockBelow1 = new BlockPos(Minecraft.thePlayer.posX, var5, Minecraft.thePlayer.posZ);
               this.blockData = this.getBlockData(blockBelow1, this.invalid);
               if(this.blockData != null) {
                  float yaw = RotationUtils.aimAtLocation((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ(), this.blockData.face)[0];
                  float pitch = RotationUtils.aimAtLocation((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ(), this.blockData.face)[1];
                  RotationUtils.server_yaw = yaw;
                  RotationUtils.server_pitch = pitch;
               }
            }
         }

      }
   }

   // $FF: synthetic method
   @EventTarget
   public void onPost(EventPreMotionUpdates e) {
      if(this.isToggled()) {
         if((Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AACFast") || Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) && this.blockData != null) {
            Minecraft var10000;
            if(Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
               if(this.getBlockAmount() == 0) {
                  return;
               }
            } else {
               var10000 = mc;
               if(Minecraft.thePlayer.getCurrentEquippedItem() == null) {
                  return;
               }

               var10000 = mc;
               if(!(Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                  return;
               }
            }

            if(this.time.hasTimeElapsed(1000L)) {
               mc.rightClickDelayTimer = 0;
               Minecraft var10002 = mc;
               String playerPos = String.valueOf(Minecraft.thePlayer.posY);
               var10000 = mc;
               int heldItem = Minecraft.thePlayer.inventory.currentItem;
               var10002 = mc;
               Minecraft var10003 = mc;
               double var6 = Minecraft.thePlayer.posY - 1.0D;
               Minecraft var10004 = mc;
               new BlockPos(Minecraft.thePlayer.posX, var6, Minecraft.thePlayer.posZ);

               for(int i = 0; i < 9; ++i) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.inventory.getStackInSlot(i) != null) {
                     var10000 = mc;
                     if(Minecraft.thePlayer.inventory.getStackInSlot(i).stackSize != 0) {
                        var10000 = mc;
                        if(Minecraft.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
                           var10000 = mc;
                           var10003 = mc;
                           Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem = i));
                           break;
                        }
                     }
                  }
               }

               this.setSpeed(0.15000000596046448D);
               Minecraft var10001 = mc;
               var10002 = mc;
               var10003 = mc;
               if(this.shouldPlace(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ)) {
                  var10001 = mc;
                  var10002 = mc;
                  var10003 = mc;
                  if(mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX() + (double)rando05(0L) + (double)((float)this.blockData.face.getDirectionVec().getX() * rando05(1L)), (double)this.blockData.position.getY() + (double)rando05(2L) + (double)((float)this.blockData.face.getDirectionVec().getY() * rando05(3L)), (double)this.blockData.position.getZ() + (double)rando05(4L) + (double)((float)this.blockData.face.getDirectionVec().getZ() * rando05(5L))))) {
                     if(Exist.settingManager.getSetting(this, "Swing").getBooleanValue()) {
                        var10000 = mc;
                        Minecraft.thePlayer.swingItem();
                     } else {
                        var10000 = mc;
                        Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                     }

                     mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                  }
               }

               if(Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
                  var10000 = mc;
                  var10003 = mc;
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem = heldItem));
               }
            }
         }

      }
   }

   // $FF: synthetic method
   @EventTarget
   public void onPreMotionUpdate() {
      if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
         this.setDisplayName(this.getName() + ChatFormatting.WHITE + " AAC");
         if(this.isToggled()) {
            float nextYaw = 0.0F;
            float nextPitch = 0.0F;
            String var3;
            switch((var3 = mc.func_175606_aa().func_174811_aO().name()).hashCode()) {
            case 2120701:
               if(var3.equals("EAST")) {
                  nextYaw = 90.0F;
               }
               break;
            case 2660783:
               if(var3.equals("WEST")) {
                  nextYaw = -90.0F;
               }
               break;
            case 74469605:
               if(var3.equals("NORTH")) {
                  nextYaw = 0.0F;
               }
               break;
            case 79090093:
               if(var3.equals("SOUTH")) {
                  nextYaw = -180.0F;
               }
            }

            nextPitch = 82.8F;
            Minecraft var10000 = mc;
            if(Minecraft.thePlayer.isSneaking()) {
               nextPitch = 82.5F;
            }

            C03PacketPlayer.yaw = nextYaw;
            C03PacketPlayer.pitch = nextPitch;
         }

      }
   }

   // $FF: synthetic method
   @EventTarget
   public void onUpdate(EventPreMotionUpdates e) {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
            Minecraft var10000 = mc;
            Minecraft.thePlayer.setSprinting(false);
            this.setSpeed(0.06D);
            Minecraft var10002 = mc;
            Minecraft var10003 = mc;
            double var3 = Minecraft.thePlayer.posY - 1.0D;
            Minecraft var10004 = mc;
            BlockPos pos = new BlockPos(Minecraft.thePlayer.posX, var3, Minecraft.thePlayer.posZ);
            var10000 = mc;
            if(Minecraft.thePlayer.getCurrentEquippedItem() != null) {
               var10000 = mc;
               if(Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                  var10000 = mc;
                  if(Minecraft.theWorld.getBlockState(pos) != null) {
                     var10000 = mc;
                     if(Minecraft.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
                        this.scaffoldHelper = this.getBlock(pos);
                        if(this.scaffoldHelper != null) {
                           ++this.delay;
                           if((double)this.delay == 5.0D) {
                              Minecraft var10001 = mc;
                              var10002 = mc;
                              var10003 = mc;
                              mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), this.scaffoldHelper.blockPos, this.scaffoldHelper.enumFacing, new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()));
                              var10000 = mc;
                              Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                              if(Exist.settingManager.getSetting(this, "Swing").getBooleanValue()) {
                                 var10000 = mc;
                                 Minecraft.thePlayer.swingItem();
                              } else {
                                 var10000 = mc;
                                 Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                              }

                              this.delay = 0;
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
   @EventTarget
   public void onPacket(EventPacket event) {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
            String direction = mc.func_175606_aa().func_174811_aO().name();
            if(event instanceof EventPacket) {
               Packet packet = event.getPacket();
               if(packet instanceof C03PacketPlayer) {
                  C03PacketPlayer p = (C03PacketPlayer)packet;
                  C03PacketPlayer.pitch = 82.0F;
                  if(direction.equalsIgnoreCase("NORTH")) {
                     C03PacketPlayer.yaw = 360.0F;
                  } else if(direction.equalsIgnoreCase("SOUTH")) {
                     C03PacketPlayer.yaw = 180.0F;
                  } else if(direction.equalsIgnoreCase("WEST")) {
                     C03PacketPlayer.yaw = 270.0F;
                  } else if(direction.equalsIgnoreCase("EAST")) {
                     C03PacketPlayer.yaw = 90.0F;
                  }
               }
            }

         }
      }
   }

   // $FF: synthetic method
   @EventTarget
   public void onPre(EventPreMotionUpdates event) {
      if(this.isToggled()) {
         if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
            this.setDisplayName(this.getName() + ChatFormatting.WHITE + " NCP");
            this.blockData = null;
            Minecraft var10000 = mc;
            if(!Minecraft.thePlayer.isSneaking()) {
               Minecraft var10002 = mc;
               Minecraft var10003 = mc;
               double var5 = Minecraft.thePlayer.posY - 1.0D;
               Minecraft var10004 = mc;
               BlockPos blockBelow1 = new BlockPos(Minecraft.thePlayer.posX, var5, Minecraft.thePlayer.posZ);
               var10000 = mc;
               if(Minecraft.theWorld.getBlockState(blockBelow1).getBlock() == Blocks.air) {
                  this.blockData = this.getBlockData(blockBelow1, this.invalid);
                  if(this.blockData != null) {
                     float yaw = RotationUtils.aimAtLocation((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ(), this.blockData.face)[0];
                     float pitch = RotationUtils.aimAtLocation((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ(), this.blockData.face)[1];
                     event.setYaw(yaw);
                     event.setPitch(pitch);
                  }
               }
            }

         }
      }
   }

   // $FF: synthetic method
   @EventTarget
   public void NCP(EventPreMotionUpdates post) {
      if(Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NCP")) {
         if(this.blockData != null) {
            Minecraft var10000 = mc;
            if(Minecraft.thePlayer.getCurrentEquippedItem() != null) {
               var10000 = mc;
               if(Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                  return;
               }
            }

            if(this.time.hasTimeElapsed(200L)) {
               mc.rightClickDelayTimer = 0;
               Minecraft var10002 = mc;
               String playerPos = String.valueOf(Minecraft.thePlayer.posY);
               var10000 = mc;
               MovementInput var6 = Minecraft.thePlayer.movementInput;
               if(MovementInput.jump) {
                  var10000 = mc;
                  Minecraft.thePlayer.motionY = 0.42D;
                  var10000 = mc;
                  Minecraft.thePlayer.motionX = 0.0D;
                  var10000 = mc;
                  Minecraft.thePlayer.motionZ = 0.0D;
                  if(this.time.hasTimeElapsed(200L)) {
                     var10000 = mc;
                     Minecraft.thePlayer.motionY = -0.28D;
                     this.time.reset();
                  }
               }

               var10000 = mc;
               int heldItem = Minecraft.thePlayer.inventory.currentItem;
               var10002 = mc;
               Minecraft var10003 = mc;
               double var7 = Minecraft.thePlayer.posY - 1.0D;
               Minecraft var10004 = mc;
               new BlockPos(Minecraft.thePlayer.posX, var7, Minecraft.thePlayer.posZ);

               for(int i = 0; i < 9; ++i) {
                  var10000 = mc;
                  if(Minecraft.thePlayer.inventory.getStackInSlot(i) != null) {
                     var10000 = mc;
                     if(Minecraft.thePlayer.inventory.getStackInSlot(i).stackSize != 0) {
                        var10000 = mc;
                        if(Minecraft.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
                           var10000 = mc;
                           var10003 = mc;
                           Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem = i));
                           break;
                        }
                     }
                  }
               }

               Minecraft var10001 = mc;
               var10002 = mc;
               var10003 = mc;
               if(mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, Minecraft.thePlayer.getHeldItem(), this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX() + (double)rando05(0L) + (double)((float)this.blockData.face.getDirectionVec().getX() * rando05(1L)), (double)this.blockData.position.getY() + (double)rando05(2L) + (double)((float)this.blockData.face.getDirectionVec().getY() * rando05(3L)), (double)this.blockData.position.getZ() + (double)rando05(4L) + (double)((float)this.blockData.face.getDirectionVec().getZ() * rando05(5L))))) {
                  var10000 = mc;
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
               }

               if(Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
                  var10000 = mc;
                  var10003 = mc;
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem = heldItem));
               }

               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
            }
         }

      }
   }

   // $FF: synthetic method
   private int getBlockAmount() {
      int n = 0;

      for(int i = 9; i < 45; ++i) {
         Minecraft var10000 = mc;
         ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if(stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock().isCollidable()) {
            n += stack.stackSize;
         }
      }

      return n;
   }

   // $FF: synthetic method
   public static float rando05(long seed) {
      seed += System.currentTimeMillis();
      return 0.3F + (float)(new Random(seed)).nextInt(70000000) / 1.0E8F + 1.458745E-8F;
   }

   // $FF: synthetic method
   private BlockUtils getBlockData(BlockPos pos, List list) {
      Minecraft var10001 = mc;
      BlockUtils var10000;
      if(!list.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
         var10000 = new BlockUtils(pos.add(0, -1, 0), EnumFacing.UP);
      } else {
         var10001 = mc;
         if(!list.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            var10000 = new BlockUtils(pos.add(-1, 0, 0), EnumFacing.EAST);
         } else {
            var10001 = mc;
            if(!list.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
               var10000 = new BlockUtils(pos.add(1, 0, 0), EnumFacing.WEST);
            } else {
               var10001 = mc;
               if(!list.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
                  var10000 = new BlockUtils(pos.add(0, 0, -1), EnumFacing.SOUTH);
               } else {
                  var10001 = mc;
                  var10000 = !list.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())?new BlockUtils(pos.add(0, 0, 1), EnumFacing.NORTH):null;
               }
            }
         }
      }

      return var10000;
   }

   // $FF: synthetic method
   public boolean shouldPlace(double x, double y, double z) {
      BlockPos p1 = new BlockPos(x - 0.23999999463558197D, y - 0.5D, z - 0.23999999463558197D);
      BlockPos p2 = new BlockPos(x - 0.23999999463558197D, y - 0.5D, z + 0.23999999463558197D);
      BlockPos p3 = new BlockPos(x + 0.23999999463558197D, y - 0.5D, z + 0.23999999463558197D);
      BlockPos p4 = new BlockPos(x + 0.23999999463558197D, y - 0.5D, z - 0.23999999463558197D);
      Minecraft var10000 = mc;
      if(Minecraft.thePlayer.worldObj.getBlockState(p1).getBlock() == Blocks.air) {
         var10000 = mc;
         if(Minecraft.thePlayer.worldObj.getBlockState(p2).getBlock() == Blocks.air) {
            var10000 = mc;
            if(Minecraft.thePlayer.worldObj.getBlockState(p3).getBlock() == Blocks.air) {
               var10000 = mc;
               if(Minecraft.thePlayer.worldObj.getBlockState(p4).getBlock() == Blocks.air) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   // $FF: synthetic method
   public ScaffoldWalk.BlockData getBlock(BlockPos pos) {
      Minecraft var10000 = mc;
      if(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
         return new ScaffoldWalk.BlockData(pos.add(0, -1, 0), EnumFacing.UP);
      } else {
         var10000 = mc;
         if(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
            return new ScaffoldWalk.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
         } else {
            var10000 = mc;
            if(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
               return new ScaffoldWalk.BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
            } else {
               var10000 = mc;
               if(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
                  return new ScaffoldWalk.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
               } else {
                  var10000 = mc;
                  return Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air?new ScaffoldWalk.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH):null;
               }
            }
         }
      }
   }

   public class BlockData {
      // $FF: synthetic field
      public BlockPos blockPos;
      // $FF: synthetic field
      public EnumFacing enumFacing;

      // $FF: synthetic method
      public BlockData(BlockPos blockPos, EnumFacing enumFacing) {
         this.blockPos = blockPos;
         this.enumFacing = enumFacing;
      }
   }
}
