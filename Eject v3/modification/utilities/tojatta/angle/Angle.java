package modification.utilities.tojatta.angle;

import modification.utilities.tojatta.vector.impl.Vector2;

public class Angle
        extends Vector2<Float> {
    public Angle(Float paramFloat1, Float paramFloat2) {
        super(paramFloat1, paramFloat2);
    }

    public Float getYaw() {
        return Float.valueOf(getX().floatValue());
    }

    public Angle setYaw(Float paramFloat) {
        setX(paramFloat);
        return this;
    }

    public Float getPitch() {
        return Float.valueOf(getY().floatValue());
    }

    public Angle setPitch(Float paramFloat) {
        setY(paramFloat);
        return this;
    }

    public Angle constrantAngle() {
        setYaw(Float.valueOf(getYaw().floatValue() % 360.0F));
        setPitch(Float.valueOf(getPitch().floatValue() % 360.0F));
        while (getYaw().floatValue() <= -180.0F) {
            setYaw(Float.valueOf(getYaw().floatValue() + 360.0F));
        }
        while (getPitch().floatValue() <= -180.0F) {
            setPitch(Float.valueOf(getPitch().floatValue() + 360.0F));
        }
        while (getYaw().floatValue() > 180.0F) {
            setYaw(Float.valueOf(getYaw().floatValue() - 360.0F));
        }
        while (getPitch().floatValue() > 180.0F) {
            setPitch(Float.valueOf(getPitch().floatValue() - 360.0F));
        }
        return this;
    }
}




