package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer implements Packet
{
    protected double x;
    public double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    public boolean onGround;
    protected boolean moving;
    protected boolean rotating;
    
    public C03PacketPlayer()
    {
    }
    
    public C03PacketPlayer(boolean isOnGround)
    {
	this.onGround = isOnGround;
    }
    
    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
	handler.processPlayer(this);
    }
    
    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
    public void readPacketData(PacketBuffer buf) throws IOException
    {
	this.onGround = buf.readUnsignedByte() != 0;
    }
    
    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
    public void writePacketData(PacketBuffer buf) throws IOException
    {
	buf.writeByte(this.onGround ? 1 : 0);
    }
    
    public double getPositionX()
    {
	return this.x;
    }
    
    public double getPositionY()
    {
	return this.y;
    }
    
    public double getPositionZ()
    {
	return this.z;
    }
    
    public float getYaw()
    {
	return this.yaw;
    }
    
    public float getPitch()
    {
	return this.pitch;
    }
    
    public boolean isOnGround()
    {
	return this.onGround;
    }
    
    public boolean isMoving()
    {
	return this.moving;
    }
    
    public boolean getRotating()
    {
	return this.rotating;
    }
    
    public void setMoving(boolean isMoving)
    {
	this.moving = isMoving;
    }
    
    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
    public void processPacket(INetHandler handler)
    {
	this.processPacket((INetHandlerPlayServer) handler);
    }
    
    public static class C04PacketPlayerPosition extends C03PacketPlayer
    {
	
	public C04PacketPlayerPosition()
	{
	    this.moving = true;
	}
	
	public C04PacketPlayerPosition(double posX, double posY, double posZ, boolean isOnGround)
	{
	    this.x = posX;
	    this.y = posY;
	    this.z = posZ;
	    this.onGround = isOnGround;
	    this.moving = true;
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException
	{
	    this.x = buf.readDouble();
	    this.y = buf.readDouble();
	    this.z = buf.readDouble();
	    super.readPacketData(buf);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException
	{
	    buf.writeDouble(this.x);
	    buf.writeDouble(this.y);
	    buf.writeDouble(this.z);
	    super.writePacketData(buf);
	}
	
	@Override
	public void processPacket(INetHandler handler)
	{
	    super.processPacket((INetHandlerPlayServer) handler);
	}
    }
    
    public static class C05PacketPlayerLook extends C03PacketPlayer
    {
	
	public C05PacketPlayerLook()
	{
	    this.rotating = true;
	}
	
	public C05PacketPlayerLook(float playerYaw, float playerPitch, boolean isOnGround)
	{
	    this.yaw = playerYaw;
	    this.pitch = playerPitch;
	    this.onGround = isOnGround;
	    this.rotating = true;
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException
	{
	    this.yaw = buf.readFloat();
	    this.pitch = buf.readFloat();
	    super.readPacketData(buf);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException
	{
	    buf.writeFloat(this.yaw);
	    buf.writeFloat(this.pitch);
	    super.writePacketData(buf);
	}
	
	@Override
	public void processPacket(INetHandler handler)
	{
	    super.processPacket((INetHandlerPlayServer) handler);
	}
    }
    
    public static class C06PacketPlayerPosLook extends C03PacketPlayer
    {
	
	public C06PacketPlayerPosLook()
	{
	    this.moving = true;
	    this.rotating = true;
	}
	
	public C06PacketPlayerPosLook(double playerX, double playerY, double playerZ, float playerYaw, float playerPitch, boolean playerIsOnGround)
	{
	    this.x = playerX;
	    this.y = playerY;
	    this.z = playerZ;
	    this.yaw = playerYaw;
	    this.pitch = playerPitch;
	    this.onGround = playerIsOnGround;
	    this.rotating = true;
	    this.moving = true;
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException
	{
	    this.x = buf.readDouble();
	    this.y = buf.readDouble();
	    this.z = buf.readDouble();
	    this.yaw = buf.readFloat();
	    this.pitch = buf.readFloat();
	    super.readPacketData(buf);
	}
	
	@Override
	public void writePacketData(PacketBuffer buf) throws IOException
	{
	    buf.writeDouble(this.x);
	    buf.writeDouble(this.y);
	    buf.writeDouble(this.z);
	    buf.writeFloat(this.yaw);
	    buf.writeFloat(this.pitch);
	    super.writePacketData(buf);
	}
	
	@Override
	public void processPacket(INetHandler handler)
	{
	    super.processPacket((INetHandlerPlayServer) handler);
	}
    }
}
