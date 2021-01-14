package modification.utilities.tojatta.vector;

public class Vector<T extends Number> {
    private T x;
    private T y;
    private T z;

    public Vector(T paramT1, T paramT2, T paramT3) {
        this.x = paramT1;
        this.y = paramT2;
        this.z = paramT3;
    }

    public T getX() {
        return this.x;
    }

    public Vector setX(T paramT) {
        this.x = paramT;
        return this;
    }

    public T getY() {
        return this.y;
    }

    public Vector setY(T paramT) {
        this.y = paramT;
        return this;
    }

    public T getZ() {
        return this.z;
    }

    public Vector setZ(T paramT) {
        this.z = paramT;
        return this;
    }
}




