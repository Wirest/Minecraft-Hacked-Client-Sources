/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3f
 */
package delta;

import delta.utils.BoundingBox;
import javax.vecmath.Vector3f;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class Class38
extends Vector3f {
    public Class38 KzcX(float f, float f2, float f3) {
        this.x += f;
        this.y += f2;
        this.z += f3;
        return this;
    }

    public Class38 4uJ7(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        return this;
    }

    public Class38 V2r2() {
        return new Class38(this.x, this.y, this.z);
    }

    public Class38(float f, float f2, float f3) {
        super(f, f2, f3);
    }

    public Class38(BoundingBox boundingBox) {
        super((float)boundingBox._talented(), (float)boundingBox._adelaide(), (float)boundingBox._produce());
    }

    public Class38 kxe8(Class38 class38) {
        this.x += class38.x;
        this.y += class38.y;
        this.z += class38.z;
        return this;
    }

    public Object clone() {
        return this.V2r2();
    }

    public double mp8C(Class38 class38) {
        double d = class38.x - this.x;
        double d2 = class38.y - this.y;
        double d3 = class38.z - this.z;
        return d * d + d2 * d2 + d3 * d3;
    }
}

