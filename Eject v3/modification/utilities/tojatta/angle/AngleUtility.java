package modification.utilities.tojatta.angle;

import modification.utilities.tojatta.vector.impl.Vector3;

import java.util.Random;

public class AngleUtility {
    private final Random random;
    public float minYawSmoothing;
    public float maxYawSmoothing;
    public float minPitchSmoothing;
    public float maxPitchSmoothing;
    private Vector3<Float> delta;
    private Angle smoothedAngle;

    public AngleUtility(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this.minYawSmoothing = paramFloat1;
        this.maxYawSmoothing = paramFloat2;
        this.minPitchSmoothing = paramFloat3;
        this.maxPitchSmoothing = paramFloat4;
        this.random = new Random();
        this.delta = new Vector3(Float.valueOf(0.0F), Float.valueOf(0.0F), Float.valueOf(0.0F));
        this.smoothedAngle = new Angle(Float.valueOf(0.0F), Float.valueOf(0.0F));
    }

    public float randomFloat(float paramFloat1, float paramFloat2) {
        return paramFloat1 + this.random.nextFloat() * (paramFloat2 - paramFloat1);
    }

    public Angle calculateAngle(Vector3<Double> paramVector31, Vector3<Double> paramVector32) {
        Angle localAngle = new Angle(Float.valueOf(0.0F), Float.valueOf(0.0F));
        float f1 = 1.5F;
        this.delta.setX(Float.valueOf(paramVector31.getX().floatValue() - paramVector32.getX().floatValue())).setY(Float.valueOf(paramVector31.getY().floatValue() + f1 - (paramVector32.getY().floatValue() + f1))).setZ(Float.valueOf(paramVector31.getZ().floatValue() - paramVector32.getZ().floatValue()));
        double d = Math.hypot(this.delta.getX().doubleValue(), this.delta.getZ().doubleValue());
        float f2 = (float) Math.atan2(this.delta.getZ().floatValue(), this.delta.getX().floatValue());
        float f3 = (float) Math.atan2(this.delta.getY().floatValue(), d);
        float f4 = 57.29578F;
        float f5 = f2 * f4 - 90.0F;
        float f6 = -(f3 * f4);
        return localAngle.setYaw(Float.valueOf(f5)).setPitch(Float.valueOf(f6 + 8.0F)).constrantAngle();
    }

    public Angle smoothAngle(Angle paramAngle1, Angle paramAngle2) {
        return this.smoothedAngle.setYaw(Float.valueOf(paramAngle2.getYaw().floatValue() - paramAngle1.getYaw().floatValue())).setPitch(Float.valueOf(paramAngle2.getPitch().floatValue() - paramAngle1.getPitch().floatValue())).constrantAngle().setYaw(Float.valueOf(paramAngle2.getYaw().floatValue() - this.smoothedAngle.getYaw().floatValue() / 100.0F * randomFloat(this.minYawSmoothing, this.maxYawSmoothing))).setPitch(Float.valueOf(paramAngle2.getPitch().floatValue() - this.smoothedAngle.getPitch().floatValue() / 100.0F * randomFloat(this.minPitchSmoothing, this.maxPitchSmoothing))).constrantAngle();
    }
}




