package net.minecraft.world.border;

public enum EnumBorderStatus
{
    GROWING("GROWING", 0, 4259712),
    SHRINKING("SHRINKING", 1, 16724016),
    STATIONARY("STATIONARY", 2, 2138367);
    private final int id; 

    private EnumBorderStatus(String p_i45647_1_, int p_i45647_2_, int id)
    {
        this.id = id;
    }

    /**
     * Returns an integer that represents the state of the world border. Growing, Shrinking and Stationary all have
     * unique values.
     */
    public int getID()
    {
        return this.id;
    }
}
