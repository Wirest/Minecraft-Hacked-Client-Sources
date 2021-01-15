package net.minecraft.dispenser;

public class PositionImpl implements IPosition
{
    protected final double x;
    protected final double y;
    protected final double z;
    private static final String __OBFID = "CL_00001208";

    public PositionImpl(double xCoord, double yCoord, double zCoord)
    {
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }
}
