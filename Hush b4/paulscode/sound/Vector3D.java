// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

public class Vector3D
{
    public float x;
    public float y;
    public float z;
    
    public Vector3D() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    public Vector3D(final float nx, final float ny, final float nz) {
        this.x = nx;
        this.y = ny;
        this.z = nz;
    }
    
    public Vector3D clone() {
        return new Vector3D(this.x, this.y, this.z);
    }
    
    public Vector3D cross(final Vector3D A, final Vector3D B) {
        return new Vector3D(A.y * B.z - B.y * A.z, A.z * B.x - B.z * A.x, A.x * B.y - B.x * A.y);
    }
    
    public Vector3D cross(final Vector3D B) {
        return new Vector3D(this.y * B.z - B.y * this.z, this.z * B.x - B.z * this.x, this.x * B.y - B.x * this.y);
    }
    
    public float dot(final Vector3D A, final Vector3D B) {
        return A.x * B.x + A.y * B.y + A.z * B.z;
    }
    
    public float dot(final Vector3D B) {
        return this.x * B.x + this.y * B.y + this.z * B.z;
    }
    
    public Vector3D add(final Vector3D A, final Vector3D B) {
        return new Vector3D(A.x + B.x, A.y + B.y, A.z + B.z);
    }
    
    public Vector3D add(final Vector3D B) {
        return new Vector3D(this.x + B.x, this.y + B.y, this.z + B.z);
    }
    
    public Vector3D subtract(final Vector3D A, final Vector3D B) {
        return new Vector3D(A.x - B.x, A.y - B.y, A.z - B.z);
    }
    
    public Vector3D subtract(final Vector3D B) {
        return new Vector3D(this.x - B.x, this.y - B.y, this.z - B.z);
    }
    
    public float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public void normalize() {
        final double t = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x /= (float)t;
        this.y /= (float)t;
        this.z /= (float)t;
    }
    
    @Override
    public String toString() {
        return "Vector3D (" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
