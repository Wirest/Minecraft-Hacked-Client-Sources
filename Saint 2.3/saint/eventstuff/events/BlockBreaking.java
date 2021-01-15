package saint.eventstuff.events;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import saint.eventstuff.Event;

public class BlockBreaking extends Event {
   private BlockBreaking.EnumBlock state;
   private BlockPos pos;
   private EnumFacing side;

   public BlockBreaking(BlockBreaking.EnumBlock state, BlockPos pos, EnumFacing side) {
      this.side = side;
      this.state = state;
      this.pos = pos;
   }

   public void setState(BlockBreaking.EnumBlock state) {
      this.state = state;
   }

   public void setPos(BlockPos pos) {
      this.pos = pos;
   }

   public void setSide(EnumFacing side) {
      this.side = side;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public BlockBreaking.EnumBlock getState() {
      return this.state;
   }

   public EnumFacing getSide() {
      return this.side;
   }

   public static enum EnumBlock {
      CLICK,
      DAMAGE,
      DESTROY;
   }
}
