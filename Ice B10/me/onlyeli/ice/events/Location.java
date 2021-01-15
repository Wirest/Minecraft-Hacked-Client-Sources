package me.onlyeli.ice.events;

import me.onlyeli.ice.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class Location {

	private double x, y, z;
	private float yaw, pitch;

	public Location(double x, double y, double z, float yaw, float pitch){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Location(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0;
		this.pitch = 0;
	}

	public Location(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0;
		this.pitch = 0;
	}

	public Location add(int x, int y, int z){
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Location add(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	public Location subtract(int x, int y, int z){
		this.x -= x;
		this.y -= y;
		this.z -= z;

		return this;
	}

	public Location subtract(double x, double y, double z){
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

	public void setX(double x){
		this.x = x;
	}

	public double getY(){
		return y;
	}

	public void setY(double y){
		this.y = y;
	}

	public double getZ(){
		return z;
	}

	public void setZ(double z){
		this.z = z;
	}

	public float getYaw(){
		return yaw;
	}

	public void setYaw(float yaw){
		this.yaw = yaw;
	}

	public float getPitch(){
		return pitch;
	}

	public void setPitch(float pitch){
		this.pitch = pitch;
	}

	public static Location fromBlockPos(BlockPos blockPos){
		return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public BlockPos toBlockPos(){
		return new BlockPos(getX(), getY(), getZ());
	}
}
