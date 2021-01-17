// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.Vec3;

public class CustomColorFader
{
    private Vec3 color;
    private long timeUpdate;
    
    public CustomColorFader() {
        this.color = null;
        this.timeUpdate = System.currentTimeMillis();
    }
    
    public Vec3 getColor(final double p_getColor_1_, final double p_getColor_3_, final double p_getColor_5_) {
        if (this.color == null) {
            return this.color = new Vec3(p_getColor_1_, p_getColor_3_, p_getColor_5_);
        }
        final long i = System.currentTimeMillis();
        final long j = i - this.timeUpdate;
        if (j == 0L) {
            return this.color;
        }
        this.timeUpdate = i;
        if (Math.abs(p_getColor_1_ - this.color.xCoord) < 0.004 && Math.abs(p_getColor_3_ - this.color.yCoord) < 0.004 && Math.abs(p_getColor_5_ - this.color.zCoord) < 0.004) {
            return this.color;
        }
        double d0 = j * 0.001;
        d0 = Config.limit(d0, 0.0, 1.0);
        final double d2 = p_getColor_1_ - this.color.xCoord;
        final double d3 = p_getColor_3_ - this.color.yCoord;
        final double d4 = p_getColor_5_ - this.color.zCoord;
        final double d5 = this.color.xCoord + d2 * d0;
        final double d6 = this.color.yCoord + d3 * d0;
        final double d7 = this.color.zCoord + d4 * d0;
        return this.color = new Vec3(d5, d6, d7);
    }
}
