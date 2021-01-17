// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class RangeInt
{
    private int min;
    private int max;
    
    public RangeInt(final int p_i80_1_, final int p_i80_2_) {
        this.min = Math.min(p_i80_1_, p_i80_2_);
        this.max = Math.max(p_i80_1_, p_i80_2_);
    }
    
    public boolean isInRange(final int p_isInRange_1_) {
        return p_isInRange_1_ >= this.min && p_isInRange_1_ <= this.max;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getMax() {
        return this.max;
    }
    
    @Override
    public String toString() {
        return "min: " + this.min + ", max: " + this.max;
    }
}
