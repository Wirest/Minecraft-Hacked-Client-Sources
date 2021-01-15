package saint.modstuff.modules;

import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
import saint.valuestuff.Value;

public class CivBreak extends Module {
   private BlockPos block;
   private EnumFacing side;
   private final Value speed = new Value("civbreak_speed", 1);

   public CivBreak() {
      super("CivBreak", -6165654, ModManager.Category.EXPLOITS);
      Saint.getCommandManager().getContentList().add(new Command("civbreakspeed", "<speed>", new String[]{"civspeed", "cvs"}) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("-d")) {
               CivBreak.this.speed.setValueState((Integer)CivBreak.this.speed.getDefaultValue());
            } else {
               CivBreak.this.speed.setValueState(Integer.parseInt(message.split(" ")[1]));
            }

            if ((Integer)CivBreak.this.speed.getValueState() > 3) {
               CivBreak.this.speed.setValueState(3);
            } else if ((Integer)CivBreak.this.speed.getValueState() < 1) {
               CivBreak.this.speed.setValueState(1);
            }

            Logger.writeChat("CivBreak Speed set to: " + CivBreak.this.speed.getValueState());
         }
      });
   }

   public void onDisabled() {
      super.onDisabled();

      try {
         this.block = null;
         this.side = null;
      } catch (Exception var2) {
      }

   }

   public void onEvent(Event event) {
      if (event instanceof BlockBreaking) {
         try {
            BlockBreaking bb = (BlockBreaking)event;
            if (bb.getState() == BlockBreaking.EnumBlock.CLICK) {
               this.block = bb.getPos();
               this.side = bb.getSide();
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.block, this.side));
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.block, this.side));
               if (!(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                  mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.block, -1, mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
               }
            }
         } catch (Exception var14) {
         }
      } else if (event instanceof PreMotion) {
         PreMotion pre = (PreMotion)event;
         if (this.block != null) {
            float[] rotations = BlockHelper.getBlockRotations((double)this.block.getX(), (double)this.block.getY(), (double)this.block.getZ());
            pre.setYaw(rotations[0]);
            pre.setPitch(rotations[1]);
         }
      } else if (event instanceof PostMotion) {
         if (this.block != null && mc.thePlayer.getDistanceSq(this.block) < 22.399999618530273D) {
            for(int i = 0; i < (Integer)this.speed.getValueState(); ++i) {
               mc.thePlayer.swingItem();
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.block, this.side));
               mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.block, this.side));
               if (mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                  mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(this.block, -1, mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
               }

               mc.playerController.func_180512_c(this.block, this.side);
            }
         }
      } else if (event instanceof RenderIn3D && this.block != null) {
         GL11.glDisable(2896);
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(0.75F);
         if (this.block != null && mc.thePlayer.getDistanceSq(this.block) >= 22.399999618530273D) {
            GL11.glColor4f(1.0F, 0.2F, 0.0F, 1.0F);
         } else if (mc.theWorld.getBlockState(this.block).getBlock() instanceof BlockAir) {
            GL11.glColor4f(1.0F, 0.7F, 0.0F, 1.0F);
         } else {
            GL11.glColor4f(0.2F, 0.9F, 0.0F, 1.0F);
         }

         double var10000 = (double)this.block.getX();
         mc.getRenderManager();
         double x = var10000 - RenderManager.renderPosX;
         var10000 = (double)this.block.getY();
         mc.getRenderManager();
         double y = var10000 - RenderManager.renderPosY;
         var10000 = (double)this.block.getZ();
         mc.getRenderManager();
         double z = var10000 - RenderManager.renderPosZ;
         double xo = 1.0D;
         double yo = 1.0D;
         double zo = 1.0D;
         RenderHelper.drawLines(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
         RenderHelper.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
         if (this.block != null && mc.thePlayer.getDistanceSq(this.block) >= 22.399999618530273D) {
            GL11.glColor4f(1.0F, 0.2F, 0.0F, 0.11F);
         } else if (mc.theWorld.getBlockState(this.block).getBlock() instanceof BlockAir) {
            GL11.glColor4f(1.0F, 0.7F, 0.0F, 0.11F);
         } else {
            GL11.glColor4f(0.2F, 0.9F, 0.0F, 0.11F);
         }

         RenderHelper.drawFilledBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glEnable(2929);
         GL11.glDisable(3042);
         GL11.glEnable(2896);
         GL11.glEnable(3553);
      }

   }
}
