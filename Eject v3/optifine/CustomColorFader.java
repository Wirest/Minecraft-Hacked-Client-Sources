package optifine;

import net.minecraft.util.Vec3;

public class CustomColorFader {
    private Vec3 color = null;
    private long timeUpdate = System.currentTimeMillis();

    public Vec3 getColor(double paramDouble1, double paramDouble2, double paramDouble3) {
        if (this.color == null) {
            this.color = new Vec3(paramDouble1, paramDouble2, paramDouble3);
            return this.color;
        }
        long l1 = System.currentTimeMillis();
        long l2 = l1 - this.timeUpdate;
        if (l2 == 0L) {
            return this.color;
        }
        this.timeUpdate = l1;
        if ((Math.abs(paramDouble1 - this.color.xCoord) < 0.004D) && (Math.abs(paramDouble2 - this.color.yCoord) < 0.004D) && (Math.abs(paramDouble3 - this.color.zCoord) < 0.004D)) {
            return this.color;
        }
        double d1 = l2 * 0.001D;
        d1 = Config.limit(d1, 0.0D, 1.0D);
        double d2 = paramDouble1 - this.color.xCoord;
        double d3 = paramDouble2 - this.color.yCoord;
        double d4 = paramDouble3 - this.color.zCoord;
        double d5 = this.color.xCoord + d2 * d1;
        double d6 = this.color.yCoord + d3 * d1;
        double d7 = this.color.zCoord + d4 * d1;
        this.color = new Vec3(d5, d6, d7);
        return this.color;
    }
}




