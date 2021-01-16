package net.minecraft.world.storage;

import net.minecraft.util.math.MathHelper;

public class MapDecoration
{
    private final MapDecoration.Type field_191181_a;
    private byte x;
    private byte y;
    private byte rotation;

    public MapDecoration(MapDecoration.Type p_i47236_1_, byte p_i47236_2_, byte p_i47236_3_, byte p_i47236_4_)
    {
        this.field_191181_a = p_i47236_1_;
        this.x = p_i47236_2_;
        this.y = p_i47236_3_;
        this.rotation = p_i47236_4_;
    }

    public byte getType()
    {
        return this.field_191181_a.func_191163_a();
    }

    public MapDecoration.Type func_191179_b()
    {
        return this.field_191181_a;
    }

    public byte getX()
    {
        return this.x;
    }

    public byte getY()
    {
        return this.y;
    }

    public byte getRotation()
    {
        return this.rotation;
    }

    public boolean func_191180_f()
    {
        return this.field_191181_a.func_191160_b();
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof MapDecoration))
        {
            return false;
        }
        else
        {
            MapDecoration mapdecoration = (MapDecoration)p_equals_1_;

            if (this.field_191181_a != mapdecoration.field_191181_a)
            {
                return false;
            }
            else if (this.rotation != mapdecoration.rotation)
            {
                return false;
            }
            else if (this.x != mapdecoration.x)
            {
                return false;
            }
            else
            {
                return this.y == mapdecoration.y;
            }
        }
    }

    public int hashCode()
    {
        int i = this.field_191181_a.func_191163_a();
        i = 31 * i + this.x;
        i = 31 * i + this.y;
        i = 31 * i + this.rotation;
        return i;
    }

    public static enum Type
    {
        PLAYER(false),
        FRAME(true),
        RED_MARKER(false),
        BLUE_MARKER(false),
        TARGET_X(true),
        TARGET_POINT(true),
        PLAYER_OFF_MAP(false),
        PLAYER_OFF_LIMITS(false),
        MANSION(true, 5393476),
        MONUMENT(true, 3830373);

        private final byte field_191175_k;
        private final boolean field_191176_l;
        private final int field_191177_m;

        private Type(boolean p_i47343_3_)
        {
            this(p_i47343_3_, -1);
        }

        private Type(boolean p_i47344_3_, int p_i47344_4_)
        {
            this.field_191175_k = (byte)this.ordinal();
            this.field_191176_l = p_i47344_3_;
            this.field_191177_m = p_i47344_4_;
        }

        public byte func_191163_a()
        {
            return this.field_191175_k;
        }

        public boolean func_191160_b()
        {
            return this.field_191176_l;
        }

        public boolean func_191162_c()
        {
            return this.field_191177_m >= 0;
        }

        public int func_191161_d()
        {
            return this.field_191177_m;
        }

        public static MapDecoration.Type func_191159_a(byte p_191159_0_)
        {
            return values()[MathHelper.clamp(p_191159_0_, 0, values().length - 1)];
        }
    }
}
