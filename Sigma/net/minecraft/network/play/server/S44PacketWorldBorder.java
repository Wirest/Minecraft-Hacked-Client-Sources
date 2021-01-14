package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;

public class S44PacketWorldBorder implements Packet {
    private S44PacketWorldBorder.Action field_179795_a;
    private int field_179793_b;
    private double field_179794_c;
    private double field_179791_d;
    private double field_179792_e;
    private double field_179789_f;
    private long field_179790_g;
    private int field_179796_h;
    private int field_179797_i;
    private static final String __OBFID = "CL_00002292";

    public S44PacketWorldBorder() {
    }

    public S44PacketWorldBorder(WorldBorder p_i45962_1_, S44PacketWorldBorder.Action p_i45962_2_) {
        this.field_179795_a = p_i45962_2_;
        this.field_179794_c = p_i45962_1_.getCenterX();
        this.field_179791_d = p_i45962_1_.getCenterZ();
        this.field_179789_f = p_i45962_1_.getDiameter();
        this.field_179792_e = p_i45962_1_.getTargetSize();
        this.field_179790_g = p_i45962_1_.getTimeUntilTarget();
        this.field_179793_b = p_i45962_1_.getSize();
        this.field_179797_i = p_i45962_1_.getWarningDistance();
        this.field_179796_h = p_i45962_1_.getWarningTime();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_179795_a = (S44PacketWorldBorder.Action) data.readEnumValue(S44PacketWorldBorder.Action.class);

        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()]) {
            case 1:
                this.field_179792_e = data.readDouble();
                break;

            case 2:
                this.field_179789_f = data.readDouble();
                this.field_179792_e = data.readDouble();
                this.field_179790_g = data.readVarLong();
                break;

            case 3:
                this.field_179794_c = data.readDouble();
                this.field_179791_d = data.readDouble();
                break;

            case 4:
                this.field_179797_i = data.readVarIntFromBuffer();
                break;

            case 5:
                this.field_179796_h = data.readVarIntFromBuffer();
                break;

            case 6:
                this.field_179794_c = data.readDouble();
                this.field_179791_d = data.readDouble();
                this.field_179789_f = data.readDouble();
                this.field_179792_e = data.readDouble();
                this.field_179790_g = data.readVarLong();
                this.field_179793_b = data.readVarIntFromBuffer();
                this.field_179797_i = data.readVarIntFromBuffer();
                this.field_179796_h = data.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeEnumValue(this.field_179795_a);

        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()]) {
            case 1:
                data.writeDouble(this.field_179792_e);
                break;

            case 2:
                data.writeDouble(this.field_179789_f);
                data.writeDouble(this.field_179792_e);
                data.writeVarLong(this.field_179790_g);
                break;

            case 3:
                data.writeDouble(this.field_179794_c);
                data.writeDouble(this.field_179791_d);
                break;

            case 4:
                data.writeVarIntToBuffer(this.field_179797_i);
                break;

            case 5:
                data.writeVarIntToBuffer(this.field_179796_h);
                break;

            case 6:
                data.writeDouble(this.field_179794_c);
                data.writeDouble(this.field_179791_d);
                data.writeDouble(this.field_179789_f);
                data.writeDouble(this.field_179792_e);
                data.writeVarLong(this.field_179790_g);
                data.writeVarIntToBuffer(this.field_179793_b);
                data.writeVarIntToBuffer(this.field_179797_i);
                data.writeVarIntToBuffer(this.field_179796_h);
        }
    }

    public void func_179787_a(INetHandlerPlayClient p_179787_1_) {
        p_179787_1_.func_175093_a(this);
    }

    public void func_179788_a(WorldBorder p_179788_1_) {
        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()]) {
            case 1:
                p_179788_1_.setTransition(this.field_179792_e);
                break;

            case 2:
                p_179788_1_.setTransition(this.field_179789_f, this.field_179792_e, this.field_179790_g);
                break;

            case 3:
                p_179788_1_.setCenter(this.field_179794_c, this.field_179791_d);
                break;

            case 4:
                p_179788_1_.setWarningDistance(this.field_179797_i);
                break;

            case 5:
                p_179788_1_.setWarningTime(this.field_179796_h);
                break;

            case 6:
                p_179788_1_.setCenter(this.field_179794_c, this.field_179791_d);

                if (this.field_179790_g > 0L) {
                    p_179788_1_.setTransition(this.field_179789_f, this.field_179792_e, this.field_179790_g);
                } else {
                    p_179788_1_.setTransition(this.field_179792_e);
                }

                p_179788_1_.setSize(this.field_179793_b);
                p_179788_1_.setWarningDistance(this.field_179797_i);
                p_179788_1_.setWarningTime(this.field_179796_h);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.func_179787_a((INetHandlerPlayClient) handler);
    }

    public static enum Action {
        SET_SIZE("SET_SIZE", 0),
        LERP_SIZE("LERP_SIZE", 1),
        SET_CENTER("SET_CENTER", 2),
        INITIALIZE("INITIALIZE", 3),
        SET_WARNING_TIME("SET_WARNING_TIME", 4),
        SET_WARNING_BLOCKS("SET_WARNING_BLOCKS", 5);

        private static final S44PacketWorldBorder.Action[] $VALUES = new S44PacketWorldBorder.Action[]{SET_SIZE, LERP_SIZE, SET_CENTER, INITIALIZE, SET_WARNING_TIME, SET_WARNING_BLOCKS};
        private static final String __OBFID = "CL_00002290";

        private Action(String p_i45961_1_, int p_i45961_2_) {
        }
    }

    static final class SwitchAction {
        static final int[] field_179947_a = new int[S44PacketWorldBorder.Action.values().length];
        private static final String __OBFID = "CL_00002291";

        static {
            try {
                field_179947_a[S44PacketWorldBorder.Action.SET_SIZE.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
                ;
            }

            try {
                field_179947_a[S44PacketWorldBorder.Action.LERP_SIZE.ordinal()] = 2;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_179947_a[S44PacketWorldBorder.Action.SET_CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_179947_a[S44PacketWorldBorder.Action.SET_WARNING_BLOCKS.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_179947_a[S44PacketWorldBorder.Action.SET_WARNING_TIME.ordinal()] = 5;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_179947_a[S44PacketWorldBorder.Action.INITIALIZE.ordinal()] = 6;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
