package saint.modstuff.modules;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.BlockBreaking;
import saint.eventstuff.events.PostMotion;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.utilities.RenderHelper;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Nuker extends Module {
   private BlockPos blockBreaking;
   private TimeHelper time = new TimeHelper();
   private Block selectedBlock;
   private Block globalBlock;
   private final Value selective = new Value("nuker_selective", true);
   EntityZombie AI;

   public Nuker() {
      super("Nuker", -7077677, ModManager.Category.WORLD);
      Saint.getCommandManager().getContentList().add(new Command("nukerselective", "none", new String[]{"nukersel", "ns"}) {
         public void run(String message) {
            Nuker.this.selective.setValueState(!(Boolean)Nuker.this.selective.getValueState());
            Logger.writeChat("Nuker will " + ((Boolean)Nuker.this.selective.getValueState() ? "now" : "no longer") + " only attack the last selected block.");
         }
      });
   }

   private double getDistance(double x, double y, double z) {
      float xDiff = (float)(mc.thePlayer.posX - x);
      float yDiff = (float)(mc.thePlayer.posY - y);
      float zDiff = (float)(mc.thePlayer.posZ - z);
      return (double)MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
   }

   public void onEvent(Event event) {
      if (event instanceof BlockBreaking) {
         BlockBreaking bb = (BlockBreaking)event;
         if (bb.getState() == BlockBreaking.EnumBlock.CLICK && bb.getPos() != null) {
            this.selectedBlock = BlockHelper.getBlockAtPos(bb.getPos());
         }
      } else if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;

         for(int y = 6; y >= -1; --y) {
            for(int x = -6; x <= 6; ++x) {
               for(int z = -6; z <= 6; ++z) {
                  if (x != 0 || z != 0) {
                     BlockPos pos = new BlockPos(mc.thePlayer.posX + (double)x, mc.thePlayer.posY + (double)y, mc.thePlayer.posZ + (double)z);
                     if (pos != null && this.getFacingDirection(pos) != null && this.blockChecks(BlockHelper.getBlockAtPos(pos)) && mc.thePlayer.getDistance(mc.thePlayer.posX + (double)x, mc.thePlayer.posY + (double)y, mc.thePlayer.posZ + (double)z) < (double)mc.playerController.getBlockReachDistance() - 0.5D) {
                        float[] rotations = BlockHelper.getBlockRotations((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
                        pre.setYaw(rotations[0]);
                        pre.setPitch(rotations[1]);
                        BlockHelper.bestTool(pos.getX(), pos.getY(), pos.getZ());
                        this.blockBreaking = pos;
                        return;
                     }
                  }
               }
            }
         }

         this.blockBreaking = null;
      } else if (event instanceof PostMotion) {
         if (this.blockBreaking != null) {
            mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
            if (mc.playerController.blockHitDelay > 0) {
               mc.playerController.blockHitDelay = 0;
            }

            EnumFacing direction = this.getFacingDirection(this.blockBreaking);
            if (direction != null) {
               mc.playerController.func_180512_c(this.blockBreaking, direction);
            }
         }
      } else if (event instanceof RenderIn3D && this.blockBreaking != null) {
         GL11.glDisable(2896);
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(0.75F);
         GL11.glColor4f(0.2F, 1.2F, 1.2F, 1.0F);
         double var10000 = (double)this.blockBreaking.getX();
         mc.getRenderManager();
         double x = var10000 - RenderManager.renderPosX;
         var10000 = (double)this.blockBreaking.getY();
         mc.getRenderManager();
         double y = var10000 - RenderManager.renderPosY;
         var10000 = (double)this.blockBreaking.getZ();
         mc.getRenderManager();
         double z = var10000 - RenderManager.renderPosZ;
         double xo = 1.0D;
         double yo = 1.0D;
         double zo = 1.0D;
         AxisAlignedBB mask = new AxisAlignedBB(x, y, z, x + xo - xo * (double)mc.playerController.curBlockDamageMP, y + yo - yo * (double)mc.playerController.curBlockDamageMP, z + zo - zo * (double)mc.playerController.curBlockDamageMP);
         RenderHelper.drawOutlinedBoundingBox(mask);
         GL11.glColor4f(0.2F, 1.2F, 1.2F, 0.11F);
         RenderHelper.drawFilledBox(mask);
         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glEnable(2929);
         GL11.glDisable(3042);
         GL11.glEnable(2896);
         GL11.glEnable(3553);
      }

   }

   private EnumFacing getFacingDirection(BlockPos pos) {
      EnumFacing direction = null;
      if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.UP;
      } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.DOWN;
      } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.EAST;
      } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.WEST;
      } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.SOUTH;
      } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isSolidFullCube()) {
         direction = EnumFacing.NORTH;
      }

      MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D));
      return rayResult != null && rayResult.func_178782_a() == pos ? rayResult.field_178784_b : direction;
   }

   private boolean blockChecks(Block block) {
      if ((Boolean)this.selective.getValueState()) {
         if (block != this.selectedBlock) {
            return false;
         }
      } else if (block instanceof BlockAir) {
         return false;
      }

      if (block.getBlockHardness(mc.theWorld, BlockPos.ORIGIN) > -1.0F || mc.playerController.isInCreativeMode()) {
         return true;
      } else {
         return false;
      }
   }

   private void renderESP(double x, double y, double z) {
      double xPos = x - RenderManager.renderPosX;
      double yPos = y - RenderManager.renderPosY;
      double zPos = z - RenderManager.renderPosZ;
      int color = true;
      float red = 1.0F;
      float green = 1.0F;
      float blue = 0.5019608F;
      GL11.glColor4f(1.0F, 1.0F, 0.5019608F, 0.2F);
      RenderHelper.drawFilledBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glColor4f(1.0F, 1.0F, 0.5019608F, 1.0F);
      RenderHelper.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
      GL11.glColor4f(1.0F, 1.0F, 0.5019608F, 0.5F);
      RenderHelper.drawLines(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
   }
}
