package net.minecraft.src;

public class RangeInt
{
    private int min = -1;
    private int max = -1;

    public RangeInt(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(int val)
    {
        return this.min >= 0 && val < this.min ? false : this.max < 0 || val <= this.max;
    }

    public String toString()
    {
        return "min: " + this.min + ", max: " + this.max;
    }
}
