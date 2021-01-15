package nivia.events.events;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import nivia.events.Event;

public class EventBoundingBox implements Event {

	private final Block block;
	private AxisAlignedBB boundingBox;
    private final double x;
    private final double y;
    private final double z;
       public EventBoundingBox(AxisAlignedBB bb, Block block, double x, double y, double z) {
    	   this.block = block;
           this.boundingBox = bb;
           this.x = x;
           this.y = y;
           this.z = z;
       }
    public AxisAlignedBB getBoundingBox() {
   		return boundingBox;
   	}
   	public void setBoundingBox(AxisAlignedBB boundingBox) {
   		this.boundingBox = boundingBox;
   	}
   	public Block getBlock() {
   		return block;
   	}
   	public double getX() {
   		return x;
   	}
   	public double getY() {
   		return y;
   	}
   	public double getZ() {
   		return z;
   	}
}
