package saint.eventstuff.events;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import saint.eventstuff.Event;

public class BlockBB extends Event {
   private int x;
   private int y;
   private int z;
   private final Block block;
   private AxisAlignedBB bb;

   public BlockBB(int x, int y, int z, Block block, AxisAlignedBB bb) {
      this.bb = bb;
      this.x = x;
      this.y = y;
      this.z = z;
      this.block = block;
   }

   public AxisAlignedBB getBoundingBox() {
      return this.bb;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public Block getBlock() {
      return this.block;
   }

   public void setBoundingBox(AxisAlignedBB bb) {
      this.bb = bb;
   }
}
