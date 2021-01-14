package optifine;

public class RangeInt
{
    private int min;
    private int max;

    public RangeInt(int p_i90_1_, int p_i90_2_)
    {
        this.min = Math.min(p_i90_1_, p_i90_2_);
        this.max = Math.max(p_i90_1_, p_i90_2_);
    }

    public boolean isInRange(int p_isInRange_1_)
    {
        return p_isInRange_1_ < this.min ? false : p_isInRange_1_ <= this.max;
    }

    public int getMin()
    {
        return this.min;
    }

    public int getMax()
    {
        return this.max;
    }

    public String toString()
    {
        return "min: " + this.min + ", max: " + this.max;
    }
}
