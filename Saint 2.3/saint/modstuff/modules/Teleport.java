package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import saint.eventstuff.Event;
import saint.eventstuff.events.DrawScreen;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class Teleport extends Module {
   private int delay = 0;
   private int wait = 0;
   private boolean shouldTeleport = false;

   public Teleport() {
      super("Teleport", -16776961, ModManager.Category.MOVEMENT);
   }

   public void onEvent(Event event) {
      if (event instanceof saint.eventstuff.events.Reach) {
         if (this.delay == 0 && Mouse.isButtonDown(2)) {
            this.shouldTeleport = !this.shouldTeleport;
            this.delay = 10;
         }

         if (this.shouldTeleport) {
            int teleportDistance = 30;
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
            mc.thePlayer.motionY = 0.0D;
            ((saint.eventstuff.events.Reach)event).setReach((float)teleportDistance);
         }
      } else {
         BlockPos toPos;
         if (event instanceof PreMotion) {
            if (this.shouldTeleport) {
               ((PreMotion)event).setCancelled(true);
            }

            if (this.shouldTeleport) {
               ++this.wait;
            } else {
               this.wait = 0;
            }

            if (this.delay > 0) {
               --this.delay;
            }

            if (this.delay == 0 && Mouse.isButtonDown(1) && this.shouldTeleport) {
               try {
                  if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                  }

                  toPos = mc.objectMouseOver.func_178782_a();
                  double x = mc.thePlayer.posX;
                  double y = mc.thePlayer.posY;
                  double z = mc.thePlayer.posZ;
                  double toX = (double)toPos.getX() + 0.5D;
                  double toY = (double)toPos.getY();
                  double toZ = (double)toPos.getZ() + 0.5D;

                  for(Block getBlockAtPos = BlockHelper.getBlockAtPos(new BlockPos(toX, toY, toZ)); !getBlockAtPos.getMaterial().equals(Material.air); getBlockAtPos = BlockHelper.getBlockAtPos(new BlockPos(toX, toY, toZ))) {
                     ++toY;
                  }

                  double allDifference = Math.abs(x - toX) + Math.abs(y - toY) + Math.abs(z - toZ);
                  int loops = 0;
                  boolean error = false;
                  String goUpOrDown = "Neutral";
                  if (y - toY < 0.0D) {
                     goUpOrDown = "Up";
                  }

                  if (y - toY > 0.0D) {
                     goUpOrDown = "Down";
                  }

                  while(allDifference > 0.0D) {
                     allDifference = Math.abs(x - toX) + Math.abs(y - toY) + Math.abs(z - toZ);
                     Block gbap = BlockHelper.getBlockAtPos(new BlockPos(x, y + 0.1D, z));
                     if (!gbap.isPassable(mc.theWorld, new BlockPos(x, y + 0.1D, z))) {
                        error = true;
                        break;
                     }

                     if (loops > 10000) {
                        error = true;
                        break;
                     }

                     double differenceX = x - toX;
                     double differenceY = y - toY;
                     double differenceZ = z - toZ;
                     double differenceXZ = Math.abs(differenceX) + Math.abs(differenceZ);
                     boolean handleYFirst = goUpOrDown.equals("Up") && Math.abs(differenceY) > 0.0D;
                     boolean handleYLast = goUpOrDown.equals("Down") && Math.abs(differenceY) > 0.0D && differenceXZ == 0.0D;
                     if (handleYFirst) {
                        if (Math.abs(differenceY) > 0.2D) {
                           y += 0.2D;
                        } else {
                           y += Math.abs(differenceY);
                        }
                     } else {
                        if (differenceX < 0.0D) {
                           if (Math.abs(differenceX) > 0.2D) {
                              x += 0.2D;
                           } else {
                              x += Math.abs(differenceX);
                           }
                        }

                        if (differenceX > 0.0D) {
                           if (Math.abs(differenceX) > 0.2D) {
                              x -= 0.2D;
                           } else {
                              x -= Math.abs(differenceX);
                           }
                        }

                        if (differenceZ < 0.0D) {
                           if (Math.abs(differenceZ) > 0.2D) {
                              z += 0.2D;
                           } else {
                              z += Math.abs(differenceZ);
                           }
                        }

                        if (differenceZ > 0.0D) {
                           if (Math.abs(differenceZ) > 0.2D) {
                              z -= 0.2D;
                           } else {
                              z -= Math.abs(differenceZ);
                           }
                        }

                        if (handleYLast) {
                           if (Math.abs(differenceY) > 0.2D) {
                              y -= 0.2D;
                           } else {
                              y -= Math.abs(differenceY);
                           }
                        }
                     }

                     mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, mc.thePlayer.onGround));
                     ++loops;
                  }

                  if (error) {
                     Logger.writeChat("Could not teleport to that location.");
                     mc.thePlayer.playSound("mob.endermen.hit", 100.0F, 1.0F);
                  } else {
                     mc.thePlayer.setPosition(toX, toY, toZ);
                     mc.thePlayer.playSound("mob.endermen.portal", 100.0F, 1.0F);
                  }

                  this.delay += 5;
                  this.shouldTeleport = false;
               } catch (Exception var32) {
               }
            }
         } else {
            Block block;
            if (event instanceof RenderIn3D) {
               if (mc.objectMouseOver != null) {
                  toPos = mc.objectMouseOver.func_178782_a();
                  if (toPos != null && this.shouldTeleport) {
                     block = BlockHelper.getBlockAtPos(toPos);
                     GL11.glDisable(2896);
                     GL11.glDisable(3553);
                     GL11.glEnable(3042);
                     GL11.glBlendFunc(770, 771);
                     GL11.glDisable(2929);
                     GL11.glEnable(2848);
                     GL11.glDepthMask(false);
                     GL11.glLineWidth(1.25F);
                     double x = (double)toPos.getX() - RenderManager.renderPosX;
                     double y = (double)toPos.getY() - RenderManager.renderPosY;
                     double z = (double)toPos.getZ() - RenderManager.renderPosZ;
                     new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
                     AxisAlignedBB mask;
                     if (block instanceof BlockSlab) {
                        mask = new AxisAlignedBB(x, y, z, x + 1.0D - (double)(1.0F * mc.playerController.curBlockDamageMP), y + 0.5D - 0.5D * (double)mc.playerController.curBlockDamageMP, z + 1.0D - (double)(1.0F * mc.playerController.curBlockDamageMP));
                     } else {
                        mask = new AxisAlignedBB(x, y, z, x + 1.0D - (double)(1.0F * mc.playerController.curBlockDamageMP), y + 1.0D - (double)(1.0F * mc.playerController.curBlockDamageMP), z + 1.0D - (double)(1.0F * mc.playerController.curBlockDamageMP));
                     }

                     if (!(block instanceof BlockAir)) {
                        GL11.glColor4f(0.0F, 2.55F, 2.55F, 0.25F);
                        RenderHelper.drawFilledBox(mask);
                        GL11.glColor4f(0.0F, 2.55F, 2.55F, 0.85F);
                        RenderHelper.drawOutlinedBoundingBox(mask);
                     }

                     GL11.glDepthMask(true);
                     GL11.glDisable(2848);
                     GL11.glEnable(2929);
                     GL11.glDisable(3042);
                     GL11.glEnable(2896);
                     GL11.glEnable(3553);
                  }
               }
            } else if (event instanceof DrawScreen && mc.objectMouseOver != null) {
               toPos = mc.objectMouseOver.func_178782_a();
               if (toPos != null) {
                  block = BlockHelper.getBlockAtPos(toPos);
                  ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                  if (!(block instanceof BlockAir) && this.shouldTeleport) {
                     RenderHelper.drawRect((float)(scaledRes.getScaledWidth() / 2 + 9), (float)(scaledRes.getScaledHeight() / 2 - 4), (float)(scaledRes.getScaledWidth() / 2 + 11) + RenderHelper.getNahrFont().getStringWidth(block.getLocalizedName()), (float)(scaledRes.getScaledHeight() / 2 + 7), Integer.MIN_VALUE);
                     RenderHelper.getNahrFont().drawString(block.getLocalizedName(), (float)(scaledRes.getScaledWidth() / 2 + 10), (float)(scaledRes.getScaledHeight() / 2 - 7), NahrFont.FontType.OUTLINE_THIN, -1, -16777216);
                     RenderHelper.drawRect((float)(scaledRes.getScaledWidth() / 2 + 9), (float)(scaledRes.getScaledHeight() / 2 + 7), (float)(scaledRes.getScaledWidth() / 2 + 11) + RenderHelper.getNahrFont().getStringWidth("Distance: " + Math.round(mc.thePlayer.getDistance((double)toPos.getX(), (double)toPos.getY(), (double)toPos.getZ()))), (float)(scaledRes.getScaledHeight() / 2 + 17), Integer.MIN_VALUE);
                     RenderHelper.getNahrFont().drawString("Distance: " + Math.round(mc.thePlayer.getDistance((double)toPos.getX(), (double)toPos.getY(), (double)toPos.getZ())), (float)(scaledRes.getScaledWidth() / 2 + 10), (float)(scaledRes.getScaledHeight() / 2 + 3), NahrFont.FontType.OUTLINE_THIN, -1, -16777216);
                  }
               }
            }
         }
      }

   }
}
