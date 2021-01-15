package optifine;

public class RangeListInt
{
    private RangeInt[] ranges = new RangeInt[0];

    public void addRange(RangeInt ri)
    {
        this.ranges = (RangeInt[])((RangeInt[])Config.addObjectToArray(this.ranges, ri));
    }

    public boolean isInRange(int val)
    {
        for (int i = 0; i < this.ranges.length; ++i)
        {
            RangeInt ri = this.ranges[i];

            if (ri.isInRange(val))
            {
                return true;
            }
        }

        return false;
    }

    public int getCountRanges()
    {
        return this.ranges.length;
    }

    public RangeInt getRange(int i)
    {
        return this.ranges[i];
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");

        for (int i = 0; i < this.ranges.length; ++i)
        {
            RangeInt ri = this.ranges[i];

            if (i > 0)
            {
                sb.append(", ");
            }

            sb.append(ri.toString());
        }

        sb.append("]");
        return sb.toString();
    }
}
