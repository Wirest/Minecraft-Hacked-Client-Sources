package skyline.specc.helper.loc;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import skyline.specc.utils.Wrapper;

public class Loc {

	private double x, y, z;
	private float yaw, pitch;

	public Loc(double x, double y, double z, float yaw, float pitch){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Loc(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0;
		this.pitch = 0;
	}

	public Loc(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0;
		this.pitch = 0;
	}

	public Loc add(int x, int y, int z){
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Loc add(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Loc subtract(int x, int y, int z){
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	public Loc subtract(double x, double y, double z){
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	public Block getBlock(){
		return Wrapper.getWorld().getBlockState(this.toBlockPos()).getBlock();
	}

	public double getX(){
		return x;
	}

	public Loc setX(double x){
		this.x = x;
        return this;
	}

	public double getY(){
		return y;
	}

	public Loc setY(double y){
		this.y = y;
        return this;
	}

	public double getZ(){
		return z;
	}

	public Loc setZ(double z){
		this.z = z;
        return this;
	}

	public float getYaw(){
		return yaw;
	}

	public Loc setYaw(float yaw){
		this.yaw = yaw;
        return this;
	}

	public float getPitch(){
		return pitch;
	}

	public Loc setPitch(float pitch){
		this.pitch = pitch;
        return this;
	}

	public static Loc fromBlockPos(BlockPos blockPos){
		return new Loc(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public BlockPos toBlockPos(){
		return new BlockPos(getX(), getY(), getZ());
	}
	
}
