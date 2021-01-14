package net.minecraft.optifine;

public class RangeListInt {
    private RangeInt[] ranges = new RangeInt[0];

    public void addRange(RangeInt ri) {
        ranges = ((RangeInt[]) Config.addObjectToArray(ranges, ri));
    }

    public boolean isInRange(int val) {
        for (RangeInt ri : ranges) {
            if (ri.isInRange(val)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");

        for (int i = 0; i < ranges.length; ++i) {
            RangeInt ri = ranges[i];

            if (i > 0) {
                sb.append(", ");
            }

            sb.append(ri.toString());
        }

        sb.append("]");
        return sb.toString();
    }
}
