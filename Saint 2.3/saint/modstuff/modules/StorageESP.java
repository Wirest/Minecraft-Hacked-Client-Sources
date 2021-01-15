package saint.modstuff.modules;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.RenderHelper;

public class StorageESP extends Module {
   public StorageESP() {
      super("StorageESP", -12621684, ModManager.Category.RENDER);
      this.setTag("Storage ESP");
   }

   public void draw(Block block, double x, double y, double z, double xo, double yo, double zo) {
      GL11.glDisable(2896);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2929);
      GL11.glEnable(2848);
      GL11.glDepthMask(false);
      GL11.glLineWidth(0.75F);
      if (block == Blocks.ender_chest) {
         GL11.glColor4f(0.4F, 0.2F, 1.0F, 1.0F);
         x += 0.0650000000745058D;
         y += 0.0D;
         z += 0.06000000074505806D;
         xo -= 0.13000000149011612D;
         yo -= 0.1200000149011612D;
         zo -= 0.12000000149011612D;
      } else if (block == Blocks.chest) {
         GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
         x += 0.0650000000745058D;
         y += 0.0D;
         z += 0.06000000074505806D;
         xo -= 0.13000000149011612D;
         yo -= 0.1200000149011612D;
         zo -= 0.12000000149011612D;
      } else if (block == Blocks.trapped_chest) {
         GL11.glColor4f(1.0F, 0.6F, 0.0F, 1.0F);
         x += 0.0650000000745058D;
         y += 0.0D;
         z += 0.06000000074505806D;
         xo -= 0.13000000149011612D;
         yo -= 0.1200000149011612D;
         zo -= 0.12000000149011612D;
      } else if (block == Blocks.brewing_stand) {
         GL11.glColor4f(1.0F, 0.3F, 0.3F, 1.0F);
         x += 0.0650000000745058D;
         y += 0.0D;
         z += 0.06000000074505806D;
         xo -= 0.13000000149011612D;
         yo -= 0.1200000149011612D;
         zo -= 0.12000000149011612D;
      } else if (block == Blocks.furnace) {
         GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
      } else if (block == Blocks.lit_furnace) {
         GL11.glColor4f(1.0F, 0.4F, 0.0F, 1.0F);
      } else if (block == Blocks.hopper || block == Blocks.dispenser || block == Blocks.dropper) {
         GL11.glColor4f(0.3F, 0.3F, 0.3F, 1.0F);
      }

      RenderHelper.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
      if (block == Blocks.ender_chest) {
         GL11.glColor4f(0.4F, 0.2F, 1.0F, 0.21F);
      } else if (block == Blocks.chest) {
         GL11.glColor4f(1.0F, 1.0F, 0.0F, 0.11F);
      } else if (block == Blocks.trapped_chest) {
         GL11.glColor4f(1.0F, 0.6F, 0.0F, 0.11F);
      } else if (block == Blocks.brewing_stand) {
         GL11.glColor4f(1.0F, 0.3F, 0.3F, 0.11F);
      } else if (block == Blocks.furnace) {
         GL11.glColor4f(0.6F, 0.6F, 0.6F, 0.11F);
      } else if (block == Blocks.lit_furnace) {
         GL11.glColor4f(1.0F, 0.4F, 0.0F, 0.11F);
      } else if (block == Blocks.hopper || block == Blocks.dispenser || block == Blocks.dropper) {
         GL11.glColor4f(0.3F, 0.3F, 0.3F, 0.11F);
      }

      RenderHelper.drawFilledBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
      if (block == Blocks.ender_chest) {
         GL11.glColor4f(0.4F, 0.2F, 1.0F, 1.0F);
      } else if (block == Blocks.chest) {
         GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
      } else if (block == Blocks.trapped_chest) {
         GL11.glColor4f(1.0F, 0.6F, 0.0F, 1.0F);
      } else if (block == Blocks.brewing_stand) {
         GL11.glColor4f(1.0F, 0.3F, 0.3F, 1.0F);
      } else if (block == Blocks.furnace) {
         GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
      } else if (block == Blocks.lit_furnace) {
         GL11.glColor4f(1.0F, 0.4F, 0.0F, 1.0F);
      } else if (block == Blocks.hopper || block == Blocks.dispenser || block == Blocks.dropper) {
         GL11.glColor4f(0.3F, 0.3F, 0.3F, 1.0F);
      }

      RenderHelper.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
      GL11.glDepthMask(true);
      GL11.glDisable(2848);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      GL11.glEnable(3553);
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D) {
         Iterator var3 = mc.theWorld.loadedTileEntityList.iterator();

         while(var3.hasNext()) {
            Object o = var3.next();
            if (o instanceof TileEntityChest) {
               TileEntityChest entity = (TileEntityChest)o;
               Block chest = mc.theWorld.getBlockState(entity.getPos()).getBlock();
               Block border = mc.theWorld.getBlockState(new BlockPos(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ() - 1)).getBlock();
               Block border2 = mc.theWorld.getBlockState(new BlockPos(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ() + 1)).getBlock();
               Block border3 = mc.theWorld.getBlockState(new BlockPos(entity.getPos().getX() - 1, entity.getPos().getY(), entity.getPos().getZ())).getBlock();
               Block border4 = mc.theWorld.getBlockState(new BlockPos(entity.getPos().getX() + 1, entity.getPos().getY(), entity.getPos().getZ())).getBlock();
               double x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
               double y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
               double z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
               GL11.glPushMatrix();
               net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
               if (chest == Blocks.chest) {
                  if (border != Blocks.chest) {
                     if (border2 == Blocks.chest) {
                        this.draw(Blocks.chest, x, y, z, 1.0D, 1.0D, 2.0D);
                     } else if (border4 == Blocks.chest) {
                        this.draw(Blocks.chest, x, y, z, 2.0D, 1.0D, 1.0D);
                     } else if (border4 == Blocks.chest) {
                        this.draw(Blocks.chest, x, y, z, 1.0D, 1.0D, 1.0D);
                     } else if (border != Blocks.chest && border2 != Blocks.chest && border3 != Blocks.chest && border4 != Blocks.chest) {
                        this.draw(Blocks.chest, x, y, z, 1.0D, 1.0D, 1.0D);
                     }
                  }
               } else if (chest == Blocks.trapped_chest && border != Blocks.trapped_chest) {
                  if (border2 == Blocks.trapped_chest) {
                     this.draw(Blocks.trapped_chest, x, y, z, 1.0D, 1.0D, 2.0D);
                  } else if (border4 == Blocks.trapped_chest) {
                     this.draw(Blocks.trapped_chest, x, y, z, 2.0D, 1.0D, 1.0D);
                  } else if (border4 == Blocks.trapped_chest) {
                     this.draw(Blocks.trapped_chest, x, y, z, 1.0D, 1.0D, 1.0D);
                  } else if (border != Blocks.trapped_chest && border2 != Blocks.trapped_chest && border3 != Blocks.trapped_chest && border4 != Blocks.trapped_chest) {
                     this.draw(Blocks.trapped_chest, x, y, z, 1.0D, 1.0D, 1.0D);
                  }
               }

               net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
               GL11.glPopMatrix();
            } else {
               double x;
               double y;
               double z;
               if (o instanceof TileEntityEnderChest) {
                  TileEntityEnderChest entity = (TileEntityEnderChest)o;
                  x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
                  y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
                  z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  GL11.glPushMatrix();
                  net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                  this.draw(Blocks.ender_chest, x, y, z, 1.0D, 1.0D, 1.0D);
                  net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               } else if (o instanceof TileEntityDropper) {
                  TileEntityDropper entity = (TileEntityDropper)o;
                  x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
                  y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
                  z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  GL11.glPushMatrix();
                  net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                  this.draw(Blocks.dropper, x, y, z, 1.0D, 1.0D, 1.0D);
                  net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               } else if (o instanceof TileEntityDispenser) {
                  TileEntityDispenser entity = (TileEntityDispenser)o;
                  x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
                  y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
                  z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  GL11.glPushMatrix();
                  net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                  this.draw(Blocks.dispenser, x, y, z, 1.0D, 1.0D, 1.0D);
                  net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               } else if (o instanceof TileEntityHopper) {
                  TileEntityHopper entity = (TileEntityHopper)o;
                  x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
                  y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
                  z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  GL11.glPushMatrix();
                  net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                  this.draw(Blocks.hopper, x, y, z, 1.0D, 1.0D, 1.0D);
                  net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               } else if (o instanceof TileEntityFurnace) {
                  TileEntityFurnace entity = (TileEntityFurnace)o;
                  x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
                  y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
                  z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  GL11.glPushMatrix();
                  net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                  if (entity.getBlockType() == Blocks.lit_furnace) {
                     this.draw(Blocks.lit_furnace, x, y, z, 1.0D, 1.0D, 1.0D);
                  } else {
                     this.draw(Blocks.furnace, x, y, z, 1.0D, 1.0D, 1.0D);
                  }

                  net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               } else if (o instanceof TileEntityBrewingStand) {
                  TileEntityBrewingStand entity = (TileEntityBrewingStand)o;
                  x = (double)entity.getPos().getX() - mc.getRenderManager().viewerPosX;
                  y = (double)entity.getPos().getY() - mc.getRenderManager().viewerPosY;
                  z = (double)entity.getPos().getZ() - mc.getRenderManager().viewerPosZ;
                  GL11.glPushMatrix();
                  net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                  this.draw(Blocks.brewing_stand, x, y, z, 1.0D, 1.0D, 1.0D);
                  net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
                  GL11.glPopMatrix();
               }
            }
         }
      }

   }
}
