package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S39PacketPlayerAbilities implements Packet
{
    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;
    private static final String __OBFID = "CL_00001317";

    public S39PacketPlayerAbilities() {}

    public S39PacketPlayerAbilities(PlayerCapabilities capabilities)
    {
        this.setInvulnerable(capabilities.disableDamage);
        this.setFlying(capabilities.isFlying);
        this.setAllowFlying(capabilities.allowFlying);
        this.setCreativeMode(capabilities.isCreativeMode);
        this.setFlySpeed(capabilities.getFlySpeed());
        this.setWalkSpeed(capabilities.getWalkSpeed());
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        byte var2 = data.readByte();
        this.setInvulnerable((var2 & 1) > 0);
        this.setFlying((var2 & 2) > 0);
        this.setAllowFlying((var2 & 4) > 0);
        this.setCreativeMode((var2 & 8) > 0);
        this.setFlySpeed(data.readFloat());
        this.setWalkSpeed(data.readFloat());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        byte var2 = 0;

        if (this.isInvulnerable())
        {
            var2 = (byte)(var2 | 1);
        }

        if (this.isFlying())
        {
            var2 = (byte)(var2 | 2);
        }

        if (this.isAllowFlying())
        {
            var2 = (byte)(var2 | 4);
        }

        if (this.isCreativeMode())
        {
            var2 = (byte)(var2 | 8);
        }

        data.writeByte(var2);
        data.writeFloat(this.flySpeed);
        data.writeFloat(this.walkSpeed);
    }

    public void func_180742_a(INetHandlerPlayClient p_180742_1_)
    {
        p_180742_1_.handlePlayerAbilities(this);
    }

    public boolean isInvulnerable()
    {
        return this.invulnerable;
    }

    public void setInvulnerable(boolean isInvulnerable)
    {
        this.invulnerable = isInvulnerable;
    }

    public boolean isFlying()
    {
        return this.flying;
    }

    public void setFlying(boolean isFlying)
    {
        this.flying = isFlying;
    }

    public boolean isAllowFlying()
    {
        return this.allowFlying;
    }

    public void setAllowFlying(boolean isAllowFlying)
    {
        this.allowFlying = isAllowFlying;
    }

    public boolean isCreativeMode()
    {
        return this.creativeMode;
    }

    public void setCreativeMode(boolean isCreativeMode)
    {
        this.creativeMode = isCreativeMode;
    }

    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    public void setFlySpeed(float flySpeedIn)
    {
        this.flySpeed = flySpeedIn;
    }

    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    public void setWalkSpeed(float walkSpeedIn)
    {
        this.walkSpeed = walkSpeedIn;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180742_a((INetHandlerPlayClient)handler);
    }
}
