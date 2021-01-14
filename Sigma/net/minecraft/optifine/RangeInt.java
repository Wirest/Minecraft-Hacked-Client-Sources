package net.minecraft.optifine;

public class RangeInt {
    private int min = -1;
    private int max = -1;

    public RangeInt(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(int val) {
        return min >= 0 && val < min ? false : max < 0 || val <= max;
    }

    @Override
    public String toString() {
        return "min: " + min + ", max: " + max;
    }
}
