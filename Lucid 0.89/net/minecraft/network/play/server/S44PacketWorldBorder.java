package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class S44PacketWorldBorder implements Packet
{
    private S44PacketWorldBorder.Action field_179795_a;
    private int field_179793_b;
    private double field_179794_c;
    private double field_179791_d;
    private double field_179792_e;
    private double field_179789_f;
    private long field_179790_g;
    private int field_179796_h;
    private int field_179797_i;

    public S44PacketWorldBorder() {}

    public S44PacketWorldBorder(WorldBorder border, S44PacketWorldBorder.Action p_i45962_2_)
    {
        this.field_179795_a = p_i45962_2_;
        this.field_179794_c = border.getCenterX();
        this.field_179791_d = border.getCenterZ();
        this.field_179789_f = border.getDiameter();
        this.field_179792_e = border.getTargetSize();
        this.field_179790_g = border.getTimeUntilTarget();
        this.field_179793_b = border.getSize();
        this.field_179797_i = border.getWarningDistance();
        this.field_179796_h = border.getWarningTime();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179795_a = (S44PacketWorldBorder.Action)buf.readEnumValue(S44PacketWorldBorder.Action.class);

        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()])
        {
            case 1:
                this.field_179792_e = buf.readDouble();
                break;

            case 2:
                this.field_179789_f = buf.readDouble();
                this.field_179792_e = buf.readDouble();
                this.field_179790_g = buf.readVarLong();
                break;

            case 3:
                this.field_179794_c = buf.readDouble();
                this.field_179791_d = buf.readDouble();
                break;

            case 4:
                this.field_179797_i = buf.readVarIntFromBuffer();
                break;

            case 5:
                this.field_179796_h = buf.readVarIntFromBuffer();
                break;

            case 6:
                this.field_179794_c = buf.readDouble();
                this.field_179791_d = buf.readDouble();
                this.field_179789_f = buf.readDouble();
                this.field_179792_e = buf.readDouble();
                this.field_179790_g = buf.readVarLong();
                this.field_179793_b = buf.readVarIntFromBuffer();
                this.field_179797_i = buf.readVarIntFromBuffer();
                this.field_179796_h = buf.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.field_179795_a);

        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()])
        {
            case 1:
                buf.writeDouble(this.field_179792_e);
                break;

            case 2:
                buf.writeDouble(this.field_179789_f);
                buf.writeDouble(this.field_179792_e);
                buf.writeVarLong(this.field_179790_g);
                break;

            case 3:
                buf.writeDouble(this.field_179794_c);
                buf.writeDouble(this.field_179791_d);
                break;

            case 4:
                buf.writeVarIntToBuffer(this.field_179797_i);
                break;

            case 5:
                buf.writeVarIntToBuffer(this.field_179796_h);
                break;

            case 6:
                buf.writeDouble(this.field_179794_c);
                buf.writeDouble(this.field_179791_d);
                buf.writeDouble(this.field_179789_f);
                buf.writeDouble(this.field_179792_e);
                buf.writeVarLong(this.field_179790_g);
                buf.writeVarIntToBuffer(this.field_179793_b);
                buf.writeVarIntToBuffer(this.field_179797_i);
                buf.writeVarIntToBuffer(this.field_179796_h);
        }
    }

    public void func_179787_a(INetHandlerPlayClient handler)
    {
        handler.handleWorldBorder(this);
    }

    public void func_179788_a(WorldBorder border)
    {
        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()])
        {
            case 1:
                border.setTransition(this.field_179792_e);
                break;

            case 2:
                border.setTransition(this.field_179789_f, this.field_179792_e, this.field_179790_g);
                break;

            case 3:
                border.setCenter(this.field_179794_c, this.field_179791_d);
                break;

            case 4:
                border.setWarningDistance(this.field_179797_i);
                break;

            case 5:
                border.setWarningTime(this.field_179796_h);
                break;

            case 6:
                border.setCenter(this.field_179794_c, this.field_179791_d);

                if (this.field_179790_g > 0L)
                {
                    border.setTransition(this.field_179789_f, this.field_179792_e, this.field_179790_g);
                }
                else
                {
                    border.setTransition(this.field_179792_e);
                }

                border.setSize(this.field_179793_b);
                border.setWarningDistance(this.field_179797_i);
                border.setWarningTime(this.field_179796_h);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_179787_a((INetHandlerPlayClient)handler);
    }

    public static enum Action
    {
        SET_SIZE("SET_SIZE", 0),
        LERP_SIZE("LERP_SIZE", 1),
        SET_CENTER("SET_CENTER", 2),
        INITIALIZE("INITIALIZE", 3),
        SET_WARNING_TIME("SET_WARNING_TIME", 4),
        SET_WARNING_BLOCKS("SET_WARNING_BLOCKS", 5); 

        private Action(String p_i45961_1_, int p_i45961_2_) {}
    }

    static final class SwitchAction
    {
        static final int[] field_179947_a = new int[S44PacketWorldBorder.Action.values().length];

        static
        {
            try
            {
                field_179947_a[S44PacketWorldBorder.Action.SET_SIZE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
                ;
            }

            try
            {
                field_179947_a[S44PacketWorldBorder.Action.LERP_SIZE.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
                ;
            }

            try
            {
                field_179947_a[S44PacketWorldBorder.Action.SET_CENTER.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_179947_a[S44PacketWorldBorder.Action.SET_WARNING_BLOCKS.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_179947_a[S44PacketWorldBorder.Action.SET_WARNING_TIME.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_179947_a[S44PacketWorldBorder.Action.INITIALIZE.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
