package sun.security.ec.point;

import sun.security.util.math.ImmutableIntegerModuloP;
import sun.security.util.math.IntegerFieldModuloP;
import sun.security.util.math.IntegerModuloP;
import sun.security.util.math.MutableIntegerModuloP;

public abstract class ProjectivePoint<T extends IntegerModuloP>
        implements Point {
    protected final T x;
    protected final T y;
    protected final T z;

    protected ProjectivePoint(T paramT1, T paramT2, T paramT3) {
        this.x = paramT1;
        this.y = paramT2;
        this.z = paramT3;
    }

    public IntegerFieldModuloP getField() {
        return this.x.getField();
    }

    public Immutable fixed() {
        return new Immutable(this.x.fixed(), this.y.fixed(), this.z.fixed());
    }

    public Mutable mutable() {
        return new Mutable(this.x.mutable(), this.y.mutable(), this.z.mutable());
    }

    public T getX() {
        return this.x;
    }

    public T getY() {
        return this.y;
    }

    public T getZ() {
        return this.z;
    }

    public AffinePoint asAffine() {
        ImmutableIntegerModuloP localImmutableIntegerModuloP = this.z.multiplicativeInverse();
        return new AffinePoint(this.x.multiply(localImmutableIntegerModuloP), this.y.multiply(localImmutableIntegerModuloP));
    }

    public static class Mutable
            extends ProjectivePoint<MutableIntegerModuloP>
            implements MutablePoint {
        public Mutable(MutableIntegerModuloP paramMutableIntegerModuloP1, MutableIntegerModuloP paramMutableIntegerModuloP2, MutableIntegerModuloP paramMutableIntegerModuloP3) {
            super(paramMutableIntegerModuloP2, paramMutableIntegerModuloP3);
        }

        public Mutable(IntegerFieldModuloP paramIntegerFieldModuloP) {
            super(paramIntegerFieldModuloP.get0().mutable(), paramIntegerFieldModuloP.get0().mutable());
        }

        public Mutable conditionalSet(Point paramPoint, int paramInt) {
            if (!(paramPoint instanceof ProjectivePoint)) {
                throw new RuntimeException("Incompatible point");
            }
            ProjectivePoint localProjectivePoint = (ProjectivePoint) paramPoint;
            return conditionalSet(localProjectivePoint, paramInt);
        }

        private <T extends IntegerModuloP> Mutable conditionalSet(ProjectivePoint<T> paramProjectivePoint, int paramInt) {
            ((MutableIntegerModuloP) this.x).conditionalSet(paramProjectivePoint.x, paramInt);
            ((MutableIntegerModuloP) this.y).conditionalSet(paramProjectivePoint.y, paramInt);
            ((MutableIntegerModuloP) this.z).conditionalSet(paramProjectivePoint.z, paramInt);
            return this;
        }

        public Mutable setValue(AffinePoint paramAffinePoint) {
            ((MutableIntegerModuloP) this.x).setValue(paramAffinePoint.getX());
            ((MutableIntegerModuloP) this.y).setValue(paramAffinePoint.getY());
            ((MutableIntegerModuloP) this.z).setValue(paramAffinePoint.getX().getField().get1());
            return this;
        }

        public Mutable setValue(Point paramPoint) {
            if (!(paramPoint instanceof ProjectivePoint)) {
                throw new RuntimeException("Incompatible point");
            }
            ProjectivePoint localProjectivePoint = (ProjectivePoint) paramPoint;
            return setValue(localProjectivePoint);
        }

        private <T extends IntegerModuloP> Mutable setValue(ProjectivePoint<T> paramProjectivePoint) {
            ((MutableIntegerModuloP) this.x).setValue(paramProjectivePoint.x);
            ((MutableIntegerModuloP) this.y).setValue(paramProjectivePoint.y);
            ((MutableIntegerModuloP) this.z).setValue(paramProjectivePoint.z);
            return this;
        }
    }

    public static class Immutable
            extends ProjectivePoint<ImmutableIntegerModuloP>
            implements ImmutablePoint {
        public Immutable(ImmutableIntegerModuloP paramImmutableIntegerModuloP1, ImmutableIntegerModuloP paramImmutableIntegerModuloP2, ImmutableIntegerModuloP paramImmutableIntegerModuloP3) {
            super(paramImmutableIntegerModuloP2, paramImmutableIntegerModuloP3);
        }
    }
}




