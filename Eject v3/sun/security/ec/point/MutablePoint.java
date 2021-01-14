package sun.security.ec.point;

public abstract interface MutablePoint
        extends Point {
    public abstract MutablePoint setValue(AffinePoint paramAffinePoint);

    public abstract MutablePoint setValue(Point paramPoint);

    public abstract MutablePoint conditionalSet(Point paramPoint, int paramInt);
}




