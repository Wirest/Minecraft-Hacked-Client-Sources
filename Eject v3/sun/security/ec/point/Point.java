package sun.security.ec.point;

import sun.security.util.math.IntegerFieldModuloP;

public abstract interface Point {
    public abstract IntegerFieldModuloP getField();

    public abstract AffinePoint asAffine();

    public abstract ImmutablePoint fixed();

    public abstract MutablePoint mutable();
}




