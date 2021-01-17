// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

public class ListenerData
{
    public Vector3D position;
    public Vector3D lookAt;
    public Vector3D up;
    public Vector3D velocity;
    public float angle;
    
    public ListenerData() {
        this.angle = 0.0f;
        this.position = new Vector3D(0.0f, 0.0f, 0.0f);
        this.lookAt = new Vector3D(0.0f, 0.0f, -1.0f);
        this.up = new Vector3D(0.0f, 1.0f, 0.0f);
        this.velocity = new Vector3D(0.0f, 0.0f, 0.0f);
        this.angle = 0.0f;
    }
    
    public ListenerData(final float pX, final float pY, final float pZ, final float lX, final float lY, final float lZ, final float uX, final float uY, final float uZ, final float a) {
        this.angle = 0.0f;
        this.position = new Vector3D(pX, pY, pZ);
        this.lookAt = new Vector3D(lX, lY, lZ);
        this.up = new Vector3D(uX, uY, uZ);
        this.velocity = new Vector3D(0.0f, 0.0f, 0.0f);
        this.angle = a;
    }
    
    public ListenerData(final Vector3D p, final Vector3D l, final Vector3D u, final float a) {
        this.angle = 0.0f;
        this.position = p.clone();
        this.lookAt = l.clone();
        this.up = u.clone();
        this.velocity = new Vector3D(0.0f, 0.0f, 0.0f);
        this.angle = a;
    }
    
    public void setData(final float pX, final float pY, final float pZ, final float lX, final float lY, final float lZ, final float uX, final float uY, final float uZ, final float a) {
        this.position.x = pX;
        this.position.y = pY;
        this.position.z = pZ;
        this.lookAt.x = lX;
        this.lookAt.y = lY;
        this.lookAt.z = lZ;
        this.up.x = uX;
        this.up.y = uY;
        this.up.z = uZ;
        this.angle = a;
    }
    
    public void setData(final Vector3D p, final Vector3D l, final Vector3D u, final float a) {
        this.position.x = p.x;
        this.position.y = p.y;
        this.position.z = p.z;
        this.lookAt.x = l.x;
        this.lookAt.y = l.y;
        this.lookAt.z = l.z;
        this.up.x = u.x;
        this.up.y = u.y;
        this.up.z = u.z;
        this.angle = a;
    }
    
    public void setData(final ListenerData l) {
        this.position.x = l.position.x;
        this.position.y = l.position.y;
        this.position.z = l.position.z;
        this.lookAt.x = l.lookAt.x;
        this.lookAt.y = l.lookAt.y;
        this.lookAt.z = l.lookAt.z;
        this.up.x = l.up.x;
        this.up.y = l.up.y;
        this.up.z = l.up.z;
        this.angle = l.angle;
    }
    
    public void setPosition(final float x, final float y, final float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    
    public void setPosition(final Vector3D p) {
        this.position.x = p.x;
        this.position.y = p.y;
        this.position.z = p.z;
    }
    
    public void setOrientation(final float lX, final float lY, final float lZ, final float uX, final float uY, final float uZ) {
        this.lookAt.x = lX;
        this.lookAt.y = lY;
        this.lookAt.z = lZ;
        this.up.x = uX;
        this.up.y = uY;
        this.up.z = uZ;
    }
    
    public void setOrientation(final Vector3D l, final Vector3D u) {
        this.lookAt.x = l.x;
        this.lookAt.y = l.y;
        this.lookAt.z = l.z;
        this.up.x = u.x;
        this.up.y = u.y;
        this.up.z = u.z;
    }
    
    public void setVelocity(final Vector3D v) {
        this.velocity.x = v.x;
        this.velocity.y = v.y;
        this.velocity.z = v.z;
    }
    
    public void setVelocity(final float x, final float y, final float z) {
        this.velocity.x = x;
        this.velocity.y = y;
        this.velocity.z = z;
    }
    
    public void setAngle(final float a) {
        this.angle = a;
        this.lookAt.x = -1.0f * (float)Math.sin(this.angle);
        this.lookAt.z = -1.0f * (float)Math.cos(this.angle);
    }
}
