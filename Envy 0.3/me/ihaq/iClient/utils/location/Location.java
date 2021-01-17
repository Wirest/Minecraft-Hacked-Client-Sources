package me.ihaq.iClient.utils.location;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;

public class Location
{
  private double x;
  private double y;
  private double z;
  private float yaw;
  private float pitch;
  
  public Location(double x, double y, double z, float yaw, float pitch)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }
  
  public Location(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = 0.0F;
    this.pitch = 0.0F;
  }
  
  public Location(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = 0.0F;
    this.pitch = 0.0F;
  }
  
  public Location add(int x, int y, int z)
  {
    this.x += x;
    this.y += y;
    this.z += z;
    
    return this;
  }
  
  public Location add(double x, double y, double z)
  {
    this.x += x;
    this.y += y;
    this.z += z;
    
    return this;
  }
  
  public Location subtract(int x, int y, int z)
  {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    
    return this;
  }
  
  public Location subtract(double x, double y, double z)
  {
    this.x -= x;
    this.y -= y;
    this.z -= z;
    
    return this;
  }
  
  public Block getBlock()
  {
    Minecraft.getMinecraft();
    return Minecraft.theWorld.getBlockState(toBlockPos()).getBlock();
  }
  
  public double getX()
  {
    return this.x;
  }
  
  public void setX(double x)
  {
    this.x = x;
  }
  
  public double getY()
  {
    return this.y;
  }
  
  public void setY(double y)
  {
    this.y = y;
  }
  
  public double getZ()
  {
    return this.z;
  }
  
  public void setZ(double z)
  {
    this.z = z;
  }
  
  public float getYaw()
  {
    return this.yaw;
  }
  
  public void setYaw(float yaw)
  {
    this.yaw = yaw;
  }
  
  public float getPitch()
  {
    return this.pitch;
  }
  
  public void setPitch(float pitch)
  {
    this.pitch = pitch;
  }
  
  public static Location fromBlockPos(BlockPos blockPos)
  {
    return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }
  
  public BlockPos toBlockPos()
  {
    return new BlockPos(getX(), getY(), getZ());
  }
}
