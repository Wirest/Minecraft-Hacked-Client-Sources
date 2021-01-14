package sun.security.ec.point;

import sun.security.util.math.ImmutableIntegerModuloP;

import java.util.Objects;

public class AffinePoint {
    private final ImmutableIntegerModuloP x;
    private final ImmutableIntegerModuloP y;

    public AffinePoint(ImmutableIntegerModuloP paramImmutableIntegerModuloP1, ImmutableIntegerModuloP paramImmutableIntegerModuloP2) {
        this.x = paramImmutableIntegerModuloP1;
        this.y = paramImmutableIntegerModuloP2;
    }

    public ImmutableIntegerModuloP getX() {
        return this.x;
    }

    public ImmutableIntegerModuloP getY() {
        return this.y;
    }

    public boolean equals(Object paramObject) {
        if (!(paramObject instanceof AffinePoint)) {
            return false;
        }
        AffinePoint localAffinePoint = (AffinePoint) paramObject;
        boolean bool1 = this.x.asBigInteger().equals(localAffinePoint.x.asBigInteger());
        boolean bool2 = this.y.asBigInteger().equals(localAffinePoint.y.asBigInteger());
        return (bool1) && (bool2);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.x, this.y});
    }

    public String toString() {
        return "(" + this.x.asBigInteger().toString() + "," + this.y.asBigInteger().toString() + ")";
    }
}




