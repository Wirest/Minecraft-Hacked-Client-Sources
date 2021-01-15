package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockRender;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.RenderHelper;
import saint.valuestuff.Value;

public class BlockESP extends Module {
   public final List blocks = new ArrayList();
   private final List cache = new CopyOnWriteArrayList();
   private final Value limit = new Value("blockesp_limit", 5000);
   private final Value range = new Value("blockesp_range", 256);
   private boolean zrue = true;

   public BlockESP() {
      super("BlockESP", -5247250, ModManager.Category.WORLD);
      this.setTag("Block ESP");
      Saint.getCommandManager().getContentList().add(new Command("blockesplimit", "<block limit>", new String[]{"besplimit", "bel"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               BlockESP.this.limit.setValueState((Integer)BlockESP.this.limit.getDefaultValue());
            } else {
               BlockESP.this.limit.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)BlockESP.this.limit.getValueState() > 100000) {
               BlockESP.this.limit.setValueState(100000);
            } else if ((Integer)BlockESP.this.limit.getValueState() < 10) {
               BlockESP.this.limit.setValueState(10);
            }

            Logger.writeChat("BlockESP Limit set to: " + BlockESP.this.limit.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("blockesprange", "<block range>", new String[]{"besprange", "ber"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               BlockESP.this.range.setValueState((Integer)BlockESP.this.range.getDefaultValue());
            } else {
               BlockESP.this.range.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)BlockESP.this.range.getValueState() > 512) {
               BlockESP.this.range.setValueState(512);
            } else if ((Integer)BlockESP.this.range.getValueState() < 10) {
               BlockESP.this.range.setValueState(10);
            }

            Logger.writeChat("BlockESP Range set to: " + BlockESP.this.range.getValueState());
         }
      });
      Saint.getCommandManager().getContentList().add(new Command("blockesp", "<block name/id>", new String[]{"besp", "be"}) {
         public boolean isInteger(String string) {
            try {
               Integer.parseInt(string);
               return true;
            } catch (Exception var3) {
               return false;
            }
         }

         public void run(String message) {
            String[] arguments = message.split(" ");
            if (arguments.length == 1) {
               BlockESP.this.blocks.clear();
               BlockESP.this.cache.clear();
               Logger.writeChat("Block list cleared.");
            } else {
               String input = message.substring((arguments[0] + " ").length());
               Block block = null;
               if (this.isInteger(input)) {
                  block = Block.getBlockById(Integer.parseInt(input));
               } else {
                  Iterator var6 = Block.blockRegistry.iterator();

                  while(var6.hasNext()) {
                     Object o = var6.next();
                     Block blockk = (Block)o;
                     String name = blockk.getLocalizedName().replaceAll("tile.", "").replaceAll(".name", "");
                     if (name.toLowerCase().startsWith(input.toLowerCase())) {
                        block = blockk;
                        break;
                     }
                  }
               }

               if (block == null) {
                  Logger.writeChat("Invalid block.");
               } else {
                  if (BlockESP.this.blocks.contains(block)) {
                     Logger.writeChat("No longer searching for [" + block.getLocalizedName() + "]");
                     BlockESP.this.blocks.remove(BlockESP.this.blocks.indexOf(block));
                     BlockESP.this.cache.clear();
                  } else {
                     Logger.writeChat("Searching for [" + block.getLocalizedName() + "]");
                     BlockESP.this.blocks.add(block);
                     BlockESP.this.cache.clear();
                  }

                  mc.renderGlobal.loadRenderers();
                  Saint.getFileManager().getFileUsingName("blockespconfiguration").saveFile();
               }
            }
         }
      });
   }

   private int getBlockColor(Block block) {
      int color = block.getMaterial().getMaterialMapColor().colorValue;
      switch(Block.getIdFromBlock(block)) {
      case 14:
      case 41:
         color = 16576075;
         break;
      case 15:
         color = this.zrue ? -576083543 : -574570304;
         break;
      case 16:
      case 173:
         color = 3618615;
         break;
      case 21:
      case 22:
         color = 1525445;
         break;
      case 49:
         color = 3944534;
         break;
      case 54:
         color = 16760576;
         break;
      case 56:
      case 57:
      case 116:
         color = 6155509;
         break;
      case 61:
      case 62:
         color = 16658167;
         break;
      case 73:
      case 74:
      case 152:
         color = 16711680;
         break;
      case 129:
      case 133:
         color = 1564002;
         break;
      case 130:
         color = 14614999;
         break;
      case 146:
         color = 13474867;
      }

      return color == 0 ? 1216104 : color;
   }

   private boolean isCached(double x, double y, double z) {
      Iterator var8 = this.cache.iterator();

      Integer[] block;
      do {
         if (!var8.hasNext()) {
            return false;
         }

         block = (Integer[])var8.next();
      } while((double)block[0] != x || (double)block[1] != y || (double)block[2] != z);

      return true;
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(1.0F);
         Iterator var3 = this.cache.iterator();

         while(true) {
            while(var3.hasNext()) {
               Integer[] block = (Integer[])var3.next();
               Block block1 = mc.theWorld.getBlockState(new BlockPos(block[0], block[1], block[2])).getBlock();
               if (mc.thePlayer.getDistance((double)block[0], (double)block[1], (double)block[2]) <= (double)(Integer)this.range.getValueState() && this.blocks.contains(block1)) {
                  this.renderESP(block, block1);
               } else {
                  this.cache.remove(block);
               }
            }

            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            break;
         }
      } else if (event instanceof BlockRender) {
         if (this.blocks.isEmpty()) {
            return;
         }

         BlockRender blockRender = (BlockRender)event;
         int x = blockRender.getX();
         int y = blockRender.getY();
         int z = blockRender.getZ();
         if (mc.theWorld != null) {
            Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
            if (!this.isCached((double)x, (double)y, (double)z) && this.blocks.contains(block) && this.cache.size() < (Integer)this.limit.getValueState()) {
               this.cache.add(new Integer[]{x, y, z});
            }
         }
      }

   }

   public List getBlocks() {
      return this.blocks;
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.theWorld != null) {
         mc.renderGlobal.loadRenderers();
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.theWorld != null) {
         mc.renderGlobal.loadRenderers();
      }

   }

   private void renderESP(Integer[] cache, Block block) {
      if (this.blocks.contains(block)) {
         double x = (double)cache[0] - RenderManager.renderPosX;
         double y = (double)cache[1] - RenderManager.renderPosY;
         double z = (double)cache[2] - RenderManager.renderPosZ;
         int color = this.getBlockColor(block);
         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         GL11.glColor4f(red, green, blue, 0.2F);
         RenderHelper.drawFilledBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
         GL11.glColor4f(red, green, blue, 1.0F);
         RenderHelper.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
         GL11.glColor4f(red, green, blue, 0.5F);
         RenderHelper.drawLines(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      }
   }
}
