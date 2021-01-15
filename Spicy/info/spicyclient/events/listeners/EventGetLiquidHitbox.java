package info.spicyclient.events.listeners;

import info.spicyclient.events.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EventGetLiquidHitbox extends Event<EventGetLiquidHitbox> {
	
	public EventGetLiquidHitbox(World worldIn, BlockPos pos, IBlockState state, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		
		this.worldIn = worldIn;
		this.pos = pos;
		this.state = state;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		
	}
	
	public double minX, minY, minZ, maxX, maxY, maxZ;
	
	public World worldIn;
	public BlockPos pos;
	public IBlockState state;
	
	public AxisAlignedBB returnValue = null;
	
}
