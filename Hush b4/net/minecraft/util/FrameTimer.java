// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class FrameTimer
{
    private final long[] field_181752_a;
    private int field_181753_b;
    private int field_181754_c;
    private int field_181755_d;
    
    public FrameTimer() {
        this.field_181752_a = new long[240];
    }
    
    public void func_181747_a(final long p_181747_1_) {
        this.field_181752_a[this.field_181755_d] = p_181747_1_;
        ++this.field_181755_d;
        if (this.field_181755_d == 240) {
            this.field_181755_d = 0;
        }
        if (this.field_181754_c < 240) {
            this.field_181753_b = 0;
            ++this.field_181754_c;
        }
        else {
            this.field_181753_b = this.func_181751_b(this.field_181755_d + 1);
        }
    }
    
    public int func_181748_a(final long p_181748_1_, final int p_181748_3_) {
        final double d0 = p_181748_1_ / 1.6666666E7;
        return (int)(d0 * p_181748_3_);
    }
    
    public int func_181749_a() {
        return this.field_181753_b;
    }
    
    public int func_181750_b() {
        return this.field_181755_d;
    }
    
    public int func_181751_b(final int p_181751_1_) {
        return p_181751_1_ % 240;
    }
    
    public long[] func_181746_c() {
        return this.field_181752_a;
    }
}
