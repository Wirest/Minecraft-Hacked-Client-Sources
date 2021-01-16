package org.m0jang.crystal.Mod.Collection.World;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Events.EventMove;
import org.m0jang.crystal.Events.EventRender2D;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Combat.Aura;
import org.m0jang.crystal.Utils.BlockUtils;
import org.m0jang.crystal.Utils.Location;
import org.m0jang.crystal.Utils.RenderUtils;
import org.m0jang.crystal.Utils.TimeHelper;

public class Scaffold extends Module {
   private Scaffold.BlockData blockData = null;
   private TimeHelper time = new TimeHelper();
   private List blacklist;
   public static double expand = 1.0D;
   private boolean rotated;
   public static Location fuck;
   private TimeHelper toUpCoolDown = new TimeHelper();

   public Scaffold() {
      super("Scaffold", Category.Movement, false);
      this.blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
      Minecraft.thePlayer.speedInAir = 0.02F;
      this.mc.rightClickDelayTimer = 6;
      Minecraft.thePlayer.onGround = true;
   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      ScaledResolution scaledResolution = new ScaledResolution(Wrapper.mc, Minecraft.displayWidth, Minecraft.displayHeight);
      String text = String.valueOf(this.getBlocks());
      int height = scaledResolution.getScaledHeight() / 2 - 5;
      int width = scaledResolution.getScaledWidth() / 2 - 5 - Fonts.segoe18.getStringWidth(text);
      if (text.equals("0")) {
         Fonts.segoe18.drawStringWithShadow("\247c" + text, (float)width, (float)height, -1);
      } else {
         Fonts.segoe18.drawStringWithShadow("\247e" + text, (float)width, (float)height, -1);
      }

   }

   public Scaffold.BlockData getBlockData(BlockPos input) {
      List var10000 = this.blacklist;
      Minecraft.getMinecraft();
      if (!var10000.contains(Minecraft.theWorld.getBlockState(input.add(0, -1, 0)).getBlock())) {
         return new Scaffold.BlockData(input.add(0, -1, 0), EnumFacing.UP);
      } else {
         var10000 = this.blacklist;
         Minecraft.getMinecraft();
         if (!var10000.contains(Minecraft.theWorld.getBlockState(input.add(-1, 0, 0)).getBlock())) {
            return new Scaffold.BlockData(input.add(-1, 0, 0), EnumFacing.EAST);
         } else {
            var10000 = this.blacklist;
            Minecraft.getMinecraft();
            if (!var10000.contains(Minecraft.theWorld.getBlockState(input.add(1, 0, 0)).getBlock())) {
               return new Scaffold.BlockData(input.add(1, 0, 0), EnumFacing.WEST);
            } else {
               var10000 = this.blacklist;
               Minecraft.getMinecraft();
               if (!var10000.contains(Minecraft.theWorld.getBlockState(input.add(0, 0, -1)).getBlock())) {
                  return new Scaffold.BlockData(input.add(0, 0, -1), EnumFacing.SOUTH);
               } else {
                  var10000 = this.blacklist;
                  Minecraft.getMinecraft();
                  if (!var10000.contains(Minecraft.theWorld.getBlockState(input.add(0, 0, 1)).getBlock())) {
                     return new Scaffold.BlockData(input.add(0, 0, 1), EnumFacing.NORTH);
                  } else {
                     BlockPos pos = input.add(-1, 0, 0);
                     var10000 = this.blacklist;
                     Minecraft.getMinecraft();
                     if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
                        return new Scaffold.BlockData(pos.add(0, -1, 0), EnumFacing.UP);
                     } else {
                        var10000 = this.blacklist;
                        Minecraft.getMinecraft();
                        if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
                           return new Scaffold.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
                        } else {
                           var10000 = this.blacklist;
                           Minecraft.getMinecraft();
                           if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
                              return new Scaffold.BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
                           } else {
                              var10000 = this.blacklist;
                              Minecraft.getMinecraft();
                              if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
                                 return new Scaffold.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
                              } else {
                                 var10000 = this.blacklist;
                                 Minecraft.getMinecraft();
                                 if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
                                    return new Scaffold.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
                                 } else {
                                    pos = input.add(1, 0, 0);
                                    var10000 = this.blacklist;
                                    Minecraft.getMinecraft();
                                    if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
                                       return new Scaffold.BlockData(pos.add(0, -1, 0), EnumFacing.UP);
                                    } else {
                                       var10000 = this.blacklist;
                                       Minecraft.getMinecraft();
                                       if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
                                          return new Scaffold.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
                                       } else {
                                          var10000 = this.blacklist;
                                          Minecraft.getMinecraft();
                                          if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
                                             return new Scaffold.BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
                                          } else {
                                             var10000 = this.blacklist;
                                             Minecraft.getMinecraft();
                                             if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
                                                return new Scaffold.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
                                             } else {
                                                var10000 = this.blacklist;
                                                Minecraft.getMinecraft();
                                                if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
                                                   return new Scaffold.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
                                                } else {
                                                   pos = input.add(0, 0, -1);
                                                   var10000 = this.blacklist;
                                                   Minecraft.getMinecraft();
                                                   if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
                                                      return new Scaffold.BlockData(pos.add(0, -1, 0), EnumFacing.UP);
                                                   } else {
                                                      var10000 = this.blacklist;
                                                      Minecraft.getMinecraft();
                                                      if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
                                                         return new Scaffold.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
                                                      } else {
                                                         var10000 = this.blacklist;
                                                         Minecraft.getMinecraft();
                                                         if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
                                                            return new Scaffold.BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
                                                         } else {
                                                            var10000 = this.blacklist;
                                                            Minecraft.getMinecraft();
                                                            if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
                                                               return new Scaffold.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
                                                            } else {
                                                               var10000 = this.blacklist;
                                                               Minecraft.getMinecraft();
                                                               if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
                                                                  return new Scaffold.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
                                                               } else {
                                                                  pos = input.add(0, 0, 1);
                                                                  var10000 = this.blacklist;
                                                                  Minecraft.getMinecraft();
                                                                  if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
                                                                     return new Scaffold.BlockData(pos.add(0, -1, 0), EnumFacing.UP);
                                                                  } else {
                                                                     var10000 = this.blacklist;
                                                                     Minecraft.getMinecraft();
                                                                     if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
                                                                        return new Scaffold.BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
                                                                     } else {
                                                                        var10000 = this.blacklist;
                                                                        Minecraft.getMinecraft();
                                                                        if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
                                                                           return new Scaffold.BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
                                                                        } else {
                                                                           var10000 = this.blacklist;
                                                                           Minecraft.getMinecraft();
                                                                           if (!var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
                                                                              return new Scaffold.BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
                                                                           } else {
                                                                              var10000 = this.blacklist;
                                                                              Minecraft.getMinecraft();
                                                                              return !var10000.contains(Minecraft.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock()) ? new Scaffold.BlockData(pos.add(0, 0, 1), EnumFacing.NORTH) : null;
                                                                           }
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventTarget(3)
   public void onUpdate(EventUpdate e) {
      if (e.state == EventState.PRE) {
         this.blockData = null;
         if (!Minecraft.thePlayer.isSneaking() && this.hasBlock()) {
            double x = Minecraft.thePlayer.posX;
            double z = Minecraft.thePlayer.posZ;
            BlockPos BelowAir = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY - 1.0D, this.mc.getRenderViewEntity().posZ);
            if (Minecraft.theWorld.getBlockState(BelowAir).getBlock() == Blocks.air) {
               this.blockData = this.getBlockData(BelowAir);
            }
         }

         if (this.blockData != null && !Aura.isEntityValid(Aura.currentTarget, true)) {
            float[] rots = BlockUtils.getFacingRotations(this.blockData.position, this.blockData.face);
            e.yaw = rots[0];
            e.pitch = rots[1];
            this.rotated = true;
         }
      }

   }

   private double interpolate(double lastI, double i, float ticks, double ownI) {
      return lastI + (i - lastI) * (double)ticks - ownI;
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      boolean wasBobbing = Minecraft.gameSettings.viewBobbing;
      if (fuck != null) {
         double var10001 = fuck.getX();
         double var10002 = fuck.getX();
         float var10003 = event.getPartialTicks();
         this.mc.getRenderManager();
         double x = this.interpolate(var10001, var10002, var10003, RenderManager.renderPosX);
         var10001 = fuck.getY();
         var10002 = fuck.getY();
         var10003 = event.getPartialTicks();
         this.mc.getRenderManager();
         double y = this.interpolate(var10001, var10002, var10003, RenderManager.renderPosY);
         var10001 = fuck.getZ();
         var10002 = fuck.getZ();
         var10003 = event.getPartialTicks();
         this.mc.getRenderManager();
         double z = this.interpolate(var10001, var10002, var10003, RenderManager.renderPosZ);
         GlStateManager.pushMatrix();
         GlStateManager.loadIdentity();
         Minecraft.gameSettings.viewBobbing = false;
         this.mc.entityRenderer.orientCamera(event.partialTicks);
         RenderUtils.enableGL3D(1.5F);
         GlStateManager.color(0.0F, 0.9F, 0.0F, 0.7F);
         GL11.glBegin(1);
         Minecraft.getMinecraft();
         GL11.glVertex3d(0.0D, (double)Minecraft.thePlayer.getEyeHeight(), 0.0D);
         GL11.glVertex3d(x, y + 1.0D, z);
         GL11.glEnd();
         RenderUtils.disableGL3D();
         GlStateManager.popMatrix();
         Minecraft.gameSettings.viewBobbing = wasBobbing;
      }
   }

   @EventTarget(3)
   public void onPostUpdate(EventUpdate e) {
      if (e.state == EventState.POST && this.rotated) {
         this.rotated = false;
         if (this.blockData != null && !Aura.isEntityValid(Aura.currentTarget, true) && this.time.hasPassed(80.0D)) {
            this.time.reset();
            int BlockInInventory = this.findBlock(9, 36);
            int BlockInHotbar = this.findBlock(36, 45);
            if (BlockInInventory == -1 && BlockInHotbar == -1) {
               return;
            }

            if (BlockInHotbar != -1) {
               if (Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock) {
                  Minecraft.thePlayer.swingItem();
                  this.PlaceBlock(this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ()));
               } else {
                  int oldSlot = Minecraft.thePlayer.inventory.currentItem;
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(BlockInHotbar - 36));
                  Minecraft.thePlayer.inventory.currentItem = BlockInHotbar - 36;
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                  this.PlaceBlock(this.blockData.position, this.blockData.face, new Vec3((double)this.blockData.position.getX(), (double)this.blockData.position.getY(), (double)this.blockData.position.getZ()));
                  Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
                  Minecraft.thePlayer.inventory.currentItem = oldSlot;
                  this.mc.playerController.updateController();
               }

               if (Minecraft.thePlayer.posY != (double)(this.blockData.position.getY() + 1) && Minecraft.gameSettings.keyBindJump.pressed) {
                  if (this.toUpCoolDown.hasPassed(200.0D)) {
                     Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, (double)(this.blockData.position.getY() + 2), Minecraft.thePlayer.posZ);
                     Minecraft.thePlayer.motionY = 0.0D;
                     Minecraft.thePlayer.motionX = 0.0D;
                     Minecraft.thePlayer.motionZ = 0.0D;
                     Minecraft.thePlayer.jump();
                  }
               } else {
                  this.toUpCoolDown.reset();
               }
            } else {
               this.mc.playerController.windowClick(0, BlockInInventory, 0, 1, Minecraft.thePlayer);
            }
         }
      }

   }

   private void PlaceBlock(BlockPos pos, EnumFacing facing, Vec3 vec) {
      ItemStack heldItem = Minecraft.thePlayer.inventory.getCurrentItem();
      if (heldItem != null) {
         int beforeStackSize = heldItem.stackSize--;
         if (heldItem.stackSize <= 0) {
            Minecraft.thePlayer.inventory.mainInventory[Minecraft.thePlayer.inventory.currentItem] = null;
         } else if (heldItem.stackSize != beforeStackSize || this.mc.playerController.isInCreativeMode()) {
            this.mc.entityRenderer.itemRenderer.resetEquippedProgress();
         }

         this.mc.playerController.func_178890_a(Minecraft.thePlayer, Minecraft.theWorld, heldItem, pos, facing, vec);
      }
   }

   public BlockPos getExpanded(BlockPos pos) {
      float plusexpand = 1.0F;
      float minusexpand = -1.0F;
      boolean same = Minecraft.thePlayer.motionX == Minecraft.thePlayer.motionZ;
      boolean x = !same && this.getPlus((float)Minecraft.thePlayer.motionX) > this.getPlus((float)Minecraft.thePlayer.motionZ);
      boolean z = !same && this.getPlus((float)Minecraft.thePlayer.motionZ) > this.getPlus((float)Minecraft.thePlayer.motionX);
      pos = pos.add((double)(x ? (Minecraft.thePlayer.motionX > 0.0D ? plusexpand : (Minecraft.thePlayer.motionX < 0.0D ? minusexpand : 0.0F)) : 0.0F), 0.0D, (double)(z ? (Minecraft.thePlayer.motionZ > 0.0D ? plusexpand : (Minecraft.thePlayer.motionZ < 0.0D ? minusexpand : 0.0F)) : 0.0F));
      return pos;
   }

   public float getPlus(float f) {
      if (f < 0.0F) {
         f = f - f - f;
      }

      return f;
   }

   @EventTarget
   public void onMove(EventMove e) {
      e.setSafeWalk(true);
      if (!Minecraft.thePlayer.onGround || !Minecraft.thePlayer.isCollidedVertically) {
         e.x = 0.0D;
         e.z = 0.0D;
      }

   }

   private boolean hasBlock() {
      int BlockInInventory = this.findBlock(9, 36);
      int BlockInHotbar = this.findBlock(36, 45);
      return BlockInInventory != -1 || BlockInHotbar != -1;
   }

   private int getBlocks() {
      int result = 0;

      for(int i = 9; i < 45; ++i) {
         ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock().isFullBlock()) {
            result += stack.stackSize;
         }
      }

      return result;
   }

   private int findBlock(int startSlot, int endSlot) {
      for(int i = startSlot; i < endSlot; ++i) {
         ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock().isFullBlock()) {
            return i;
         }
      }

      return -1;
   }

   private class BlockData {
      public BlockPos position;
      public EnumFacing face;

      public BlockData(BlockPos position, EnumFacing face) {
         this.position = position;
         this.face = face;
      }
   }
}
