package saint.modstuff.modules;

import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.BlockHelper;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class Tower extends Module {
   private final Value slowmode = new Value("tower_slowmode", false);
   private final TimeHelper time = new TimeHelper();

   public Tower() {
      super("Tower", -7650029, ModManager.Category.WORLD);
      Saint.getCommandManager().getContentList().add(new Command("towerslowmode", "none", new String[]{"towerslow", "tsm"}) {
         public void run(String message) {
            Tower.this.slowmode.setValueState(!(Boolean)Tower.this.slowmode.getValueState());
            Logger.writeChat("Fly will " + ((Boolean)Tower.this.slowmode.getValueState() ? "now" : "no longer") + " slowly place the blocks.");
         }
      });
   }

   public void onEvent(Event event) {
      float[] rotations;
      if (event instanceof PreMotion) {
         BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
         EnumFacing face = this.getFacingDirection(pos);

         try {
            if (this.time.hasReached((long)((Boolean)this.slowmode.getValueState() ? 150 : 75)) && mc.thePlayer.getCurrentEquippedItem().getItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
               mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.1D, mc.thePlayer.posZ);
               rotations = BlockHelper.getBlockRotations(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
               ((PreMotion)event).setYaw(rotations[0]);
               ((PreMotion)event).setPitch(rotations[1]);
               if (!mc.thePlayer.onGround) {
                  mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, face, new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ));
                  mc.thePlayer.swingItem();
               }

               this.time.reset();
            }
         } catch (Exception var5) {
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            rotations = BlockHelper.getBlockRotations(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
            player.yaw = rotations[0];
            player.pitch = rotations[1];
         }
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

      return direction;
   }
}
