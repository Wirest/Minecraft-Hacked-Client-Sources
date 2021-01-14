package com.etb.client.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Everyone and their mama has this method.
 * Added by Kix
 *
 * @author TheCyberBrick
 */
public final class GLUProjection {
    private static GLUProjection instance;
    private final FloatBuffer coords;
    private IntBuffer viewport;
    private FloatBuffer modelview;
    private FloatBuffer projection;
    private Vector3D frustumPos;
    private Vector3D[] frustum;
    private Vector3D[] invFrustum;
    private Vector3D viewVec;
    private double displayWidth;
    private double displayHeight;
    private double widthScale;
    private double heightScale;
    private double bra;
    private double bla;
    private double tra;
    private double tla;
    private Line tb;
    private Line bb;
    private Line lb;
    private Line rb;
    private float fovY;
    private float fovX;
    private Vector3D lookVec;

    private GLUProjection() {
        this.coords = BufferUtils.createFloatBuffer(3);
    }

    public static GLUProjection getInstance() {
        if (GLUProjection.instance == null) {
            GLUProjection.instance = new GLUProjection();
        }
        return GLUProjection.instance;
    }

    public void updateMatrices(final IntBuffer viewport, final FloatBuffer modelview, final FloatBuffer projection, final double widthScale, final double heightScale) {
        this.viewport = viewport;
        this.modelview = modelview;
        this.projection = projection;
        this.widthScale = widthScale;
        this.heightScale = heightScale;
        final float fov = (float) Math.toDegrees(Math.atan(1.0 / this.projection.get(5)) * 2.0);
        this.fovY = fov;
        this.displayWidth = this.viewport.get(2);
        this.displayHeight = this.viewport.get(3);
        this.fovX = (float) Math.toDegrees(2.0 * Math.atan(this.displayWidth / this.displayHeight * Math.tan(Math.toRadians(this.fovY) / 2.0)));
        final Vector3D ft = new Vector3D(this.modelview.get(12), this.modelview.get(13), this.modelview.get(14));
        final Vector3D lv = new Vector3D(this.modelview.get(0), this.modelview.get(1), this.modelview.get(2));
        final Vector3D uv = new Vector3D(this.modelview.get(4), this.modelview.get(5), this.modelview.get(6));
        final Vector3D fv = new Vector3D(this.modelview.get(8), this.modelview.get(9), this.modelview.get(10));
        final Vector3D nuv = new Vector3D(0.0, 1.0, 0.0);
        final Vector3D nlv = new Vector3D(1.0, 0.0, 0.0);
        final Vector3D nfv = new Vector3D(0.0, 0.0, 1.0);
        double yaw = Math.toDegrees(Math.atan2(nlv.cross(lv).length(), nlv.dot(lv))) + 180.0;
        if (fv.x < 0.0) {
            yaw = 360.0 - yaw;
        }
        double pitch;
        if ((-fv.y > 0.0 && yaw >= 90.0 && yaw < 270.0) || (fv.y > 0.0 && (yaw < 90.0 || yaw >= 270.0))) {
            pitch = Math.toDegrees(Math.atan2(nuv.cross(uv).length(), nuv.dot(uv)));
        } else {
            pitch = -Math.toDegrees(Math.atan2(nuv.cross(uv).length(), nuv.dot(uv)));
        }
        this.lookVec = this.getRotationVector(yaw, pitch);
        final Matrix4f modelviewMatrix = new Matrix4f();
        modelviewMatrix.load(this.modelview.asReadOnlyBuffer());
        modelviewMatrix.invert();
        this.frustumPos = new Vector3D(modelviewMatrix.m30, modelviewMatrix.m31, modelviewMatrix.m32);
        this.frustum = this.getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, yaw, pitch, fov, 1.0, this.displayWidth / this.displayHeight);
        this.invFrustum = this.getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, yaw - 180.0, -pitch, fov, 1.0, this.displayWidth / this.displayHeight);
        this.viewVec = this.getRotationVector(yaw, pitch).normalized();
        this.bra = Math.toDegrees(Math.acos(this.displayHeight * heightScale / Math.sqrt(this.displayWidth * widthScale * this.displayWidth * widthScale + this.displayHeight * heightScale * this.displayHeight * heightScale)));
        this.bla = 360.0 - this.bra;
        this.tra = this.bla - 180.0;
        this.tla = this.bra + 180.0;
        this.rb = new Line(this.displayWidth * this.widthScale, 0.0, 0.0, 0.0, 1.0, 0.0);
        this.tb = new Line(0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
        this.lb = new Line(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        this.bb = new Line(0.0, this.displayHeight * this.heightScale, 0.0, 1.0, 0.0, 0.0);
    }

    public Projection project(final double x, final double y, final double z, final ClampMode clampModeOutside, final boolean extrudeInverted) {
        if (this.viewport != null && this.modelview != null && this.projection != null) {
            final Vector3D posVec = new Vector3D(x, y, z);
            final boolean[] frustum = this.doFrustumCheck(this.frustum, this.frustumPos, x, y, z);
            final boolean outsideFrustum = frustum[0] || frustum[1] || frustum[2] || frustum[3];
            if (outsideFrustum) {
                final boolean opposite = posVec.sub(this.frustumPos).dot(this.viewVec) <= 0.0;
                final boolean[] invFrustum = this.doFrustumCheck(this.invFrustum, this.frustumPos, x, y, z);
                final boolean outsideInvertedFrustum = invFrustum[0] || invFrustum[1] || invFrustum[2] || invFrustum[3];
                if ((extrudeInverted && !outsideInvertedFrustum) || (outsideInvertedFrustum && clampModeOutside != ClampMode.NONE)) {
                    if (extrudeInverted && !outsideInvertedFrustum || clampModeOutside == ClampMode.DIRECT) {
                        double vecX;
                        double vecY;
                        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.coords)) {
                            if (opposite) {
                                vecX = this.displayWidth * this.widthScale - this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0;
                                vecY = this.displayHeight * this.heightScale - (this.displayHeight - this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0;
                            } else {
                                vecX = this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0;
                                vecY = (this.displayHeight - this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0;
                            }
                            final Vector3D vec = new Vector3D(vecX, vecY, 0.0).snormalize();
                            vecX = vec.x;
                            vecY = vec.y;
                            final Line vectorLine = new Line(this.displayWidth * this.widthScale / 2.0, this.displayHeight * this.heightScale / 2.0, 0.0, vecX, vecY, 0.0);
                            double angle = Math.toDegrees(Math.acos(vec.y / Math.sqrt(vec.x * vec.x + vec.y * vec.y)));
                            if (vecX < 0.0) {
                                angle = 360.0 - angle;
                            }
                            Vector3D intersect;
                            if (angle >= this.bra && angle < this.tra) {
                                intersect = this.rb.intersect(vectorLine);
                            } else if (angle >= this.tra && angle < this.tla) {
                                intersect = this.tb.intersect(vectorLine);
                            } else if (angle >= this.tla && angle < this.bla) {
                                intersect = this.lb.intersect(vectorLine);
                            } else {
                                intersect = this.bb.intersect(vectorLine);
                            }
                            return new Projection(intersect.x, intersect.y, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                        }
                        return new Projection(0.0, 0.0, Projection.Type.FAIL);
                    } else if (clampModeOutside == ClampMode.ORTHOGONAL) {
                        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.coords)) {
                            double guiX = this.coords.get(0) * this.widthScale;
                            double guiY = (this.displayHeight - this.coords.get(1)) * this.heightScale;
                            if (opposite) {
                                guiX = this.displayWidth * this.widthScale - guiX;
                                guiY = this.displayHeight * this.heightScale - guiY;
                            }
                            if (guiX < 0.0) {
                                guiX = 0.0;
                            } else if (guiX > this.displayWidth * this.widthScale) {
                                guiX = this.displayWidth * this.widthScale;
                            }
                            if (guiY < 0.0) {
                                guiY = 0.0;
                            } else if (guiY > this.displayHeight * this.heightScale) {
                                guiY = this.displayHeight * this.heightScale;
                            }
                            return new Projection(guiX, guiY, Projection.Type.OUTSIDE);
                        }
                        return new Projection(0.0, 0.0, Projection.Type.FAIL);
                    }
                } else {
                    if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.coords)) {
                        double guiX = this.coords.get(0) * this.widthScale;
                        double guiY = (this.displayHeight - this.coords.get(1)) * this.heightScale;
                        if (opposite) {
                            guiX = this.displayWidth * this.widthScale - guiX;
                            guiY = this.displayHeight * this.heightScale - guiY;
                        }
                        return new Projection(guiX, guiY, outsideInvertedFrustum ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                    }
                    return new Projection(0.0, 0.0, Projection.Type.FAIL);
                }
            } else {
                if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.coords)) {
                    final double guiX2 = this.coords.get(0) * this.widthScale;
                    final double guiY2 = (this.displayHeight - this.coords.get(1)) * this.heightScale;
                    return new Projection(guiX2, guiY2, Projection.Type.INSIDE);
                }
                return new Projection(0.0, 0.0, Projection.Type.FAIL);
            }
        }
        return new Projection(0.0, 0.0, Projection.Type.FAIL);
    }

    private boolean[] doFrustumCheck(final Vector3D[] frustumCorners, final Vector3D frustumPos, final double x, final double y, final double z) {
        final Vector3D point = new Vector3D(x, y, z);
        final boolean c1 = this.crossPlane(new Vector3D[]{frustumPos, frustumCorners[3], frustumCorners[0]}, point);
        final boolean c2 = this.crossPlane(new Vector3D[]{frustumPos, frustumCorners[0], frustumCorners[1]}, point);
        final boolean c3 = this.crossPlane(new Vector3D[]{frustumPos, frustumCorners[1], frustumCorners[2]}, point);
        final boolean c4 = this.crossPlane(new Vector3D[]{frustumPos, frustumCorners[2], frustumCorners[3]}, point);
        return new boolean[]{c1, c2, c3, c4};
    }

    private boolean crossPlane(final Vector3D[] plane, final Vector3D point) {
        final Vector3D z = new Vector3D(0.0, 0.0, 0.0);
        final Vector3D e0 = plane[1].sub(plane[0]);
        final Vector3D e2 = plane[2].sub(plane[0]);
        final Vector3D normal = e0.cross(e2).snormalize();
        final double D = z.sub(normal).dot(plane[2]);
        final double dist = normal.dot(point) + D;
        return dist >= 0.0;
    }

    private Vector3D[] getFrustum(final double x, final double y, final double z, final double rotationYaw, final double rotationPitch, final double fov, final double farDistance, final double aspectRatio) {
        final Vector3D viewVec = this.getRotationVector(rotationYaw, rotationPitch).snormalize();
        final double hFar = 2.0 * Math.tan(Math.toRadians(fov / 2.0)) * farDistance;
        final double wFar = hFar * aspectRatio;
        final Vector3D view = this.getRotationVector(rotationYaw, rotationPitch).snormalize();
        final Vector3D up = this.getRotationVector(rotationYaw, rotationPitch - 90.0).snormalize();
        final Vector3D right = this.getRotationVector(rotationYaw + 90.0, 0.0).snormalize();
        final Vector3D camPos = new Vector3D(x, y, z);
        final Vector3D view_camPos_product = view.add(camPos);
        final Vector3D fc = new Vector3D(view_camPos_product.x * farDistance, view_camPos_product.y * farDistance, view_camPos_product.z * farDistance);
        final Vector3D topLeftfrustum = new Vector3D(fc.x + up.x * hFar / 2.0 - right.x * wFar / 2.0, fc.y + up.y * hFar / 2.0 - right.y * wFar / 2.0, fc.z + up.z * hFar / 2.0 - right.z * wFar / 2.0);
        final Vector3D downLeftfrustum = new Vector3D(fc.x - up.x * hFar / 2.0 - right.x * wFar / 2.0, fc.y - up.y * hFar / 2.0 - right.y * wFar / 2.0, fc.z - up.z * hFar / 2.0 - right.z * wFar / 2.0);
        final Vector3D topRightfrustum = new Vector3D(fc.x + up.x * hFar / 2.0 + right.x * wFar / 2.0, fc.y + up.y * hFar / 2.0 + right.y * wFar / 2.0, fc.z + up.z * hFar / 2.0 + right.z * wFar / 2.0);
        final Vector3D downRightfrustum = new Vector3D(fc.x - up.x * hFar / 2.0 + right.x * wFar / 2.0, fc.y - up.y * hFar / 2.0 + right.y * wFar / 2.0, fc.z - up.z * hFar / 2.0 + right.z * wFar / 2.0);
        return new Vector3D[]{topLeftfrustum, downLeftfrustum, downRightfrustum, topRightfrustum};
    }

    public Vector3D[] getFrustum() {
        return this.frustum;
    }

    public float getFovX() {
        return this.fovX;
    }

    public float getFovY() {
        return this.fovY;
    }

    public Vector3D getLookVector() {
        return this.lookVec;
    }

    private Vector3D getRotationVector(final double rotYaw, final double rotPitch) {
        final double c = Math.cos(-rotYaw * 0.01745329238474369 - 3.141592653589793);
        final double s = Math.sin(-rotYaw * 0.01745329238474369 - 3.141592653589793);
        final double nc = -Math.cos(-rotPitch * 0.01745329238474369);
        final double ns = Math.sin(-rotPitch * 0.01745329238474369);
        return new Vector3D(s * nc, ns, c * nc);
    }

    public enum ClampMode {
        ORTHOGONAL,
        DIRECT,
        NONE
    }

    public static class Line {
        Vector3D sourcePoint;
        Vector3D direction;

        Line(final double sx, final double sy, final double sz, final double dx, final double dy, final double dz) {
            this.sourcePoint = new Vector3D(0.0, 0.0, 0.0);
            this.direction = new Vector3D(0.0, 0.0, 0.0);
            this.sourcePoint.x = sx;
            this.sourcePoint.y = sy;
            this.sourcePoint.z = sz;
            this.direction.x = dx;
            this.direction.y = dy;
            this.direction.z = dz;
        }

        Vector3D intersect(final Line line) {
            final double a = this.sourcePoint.x;
            final double b = this.direction.x;
            final double c = line.sourcePoint.x;
            final double d = line.direction.x;
            final double e = this.sourcePoint.y;
            final double f = this.direction.y;
            final double g = line.sourcePoint.y;
            final double h = line.direction.y;
            final double te = -(a * h - c * h - d * (e - g));
            final double be = b * h - d * f;
            if (be == 0.0) {
                return this.intersectXZ(line);
            }
            return getResult(te, be);
        }

        private Vector3D getResult(double te, double be) {
            final double t = te / be;
            final Vector3D result = new Vector3D(0.0, 0.0, 0.0);
            result.x = this.sourcePoint.x + this.direction.x * t;
            result.y = this.sourcePoint.y + this.direction.y * t;
            result.z = this.sourcePoint.z + this.direction.z * t;
            return result;
        }

        private Vector3D intersectXZ(final Line line) {
            final double a = this.sourcePoint.x;
            final double b = this.direction.x;
            final double c = line.sourcePoint.x;
            final double d = line.direction.x;
            final double e = this.sourcePoint.z;
            final double f = this.direction.z;
            final double g = line.sourcePoint.z;
            final double h = line.direction.z;
            final double te = -(a * h - c * h - d * (e - g));
            final double be = b * h - d * f;
            if (be == 0.0) {
                return this.intersectYZ(line);
            }
            return getResult(te, be);
        }

        private Vector3D intersectYZ(final Line line) {
            final double a = this.sourcePoint.y;
            final double b = this.direction.y;
            final double c = line.sourcePoint.y;
            final double d = line.direction.y;
            final double e = this.sourcePoint.z;
            final double f = this.direction.z;
            final double g = line.sourcePoint.z;
            final double h = line.direction.z;
            final double te = -(a * h - c * h - d * (e - g));
            final double be = b * h - d * f;
            if (be == 0.0) {
                return null;
            }
            return getResult(te, be);
        }

        public Vector3D intersectPlane(final Vector3D pointOnPlane, final Vector3D planeNormal) {
            final Vector3D result = new Vector3D(this.sourcePoint.x, this.sourcePoint.y, this.sourcePoint.z);
            final double d = pointOnPlane.sub(this.sourcePoint).dot(planeNormal) / this.direction.dot(planeNormal);
            result.sadd(this.direction.mul(d));
            if (this.direction.dot(planeNormal) == 0.0) {
                return null;
            }
            return result;
        }
    }

    public static class Vector3D {
        public double x;
        public double y;
        public double z;

        Vector3D(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3D add(final Vector3D v) {
            return new Vector3D(this.x + v.x, this.y + v.y, this.z + v.z);
        }

        public Vector3D add(final double x, final double y, final double z) {
            return new Vector3D(this.x + x, this.y + y, this.z + z);
        }

        Vector3D sub(final Vector3D v) {
            return new Vector3D(this.x - v.x, this.y - v.y, this.z - v.z);
        }

        public Vector3D sub(final double x, final double y, final double z) {
            return new Vector3D(this.x - x, this.y - y, this.z - z);
        }

        Vector3D normalized() {
            final double len = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            return new Vector3D(this.x / len, this.y / len, this.z / len);
        }

        double dot(final Vector3D v) {
            return this.x * v.x + this.y * v.y + this.z * v.z;
        }

        Vector3D cross(final Vector3D v) {
            return new Vector3D(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
        }

        Vector3D mul(final double m) {
            return new Vector3D(this.x * m, this.y * m, this.z * m);
        }

        public Vector3D div(final double d) {
            return new Vector3D(this.x / d, this.y / d, this.z / d);
        }

        public double length() {
            return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        }

        void sadd(final Vector3D v) {
            this.x += v.x;
            this.y += v.y;
            this.z += v.z;
        }

        public Vector3D sadd(final double x, final double y, final double z) {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        }

        public Vector3D ssub(final Vector3D v) {
            this.x -= v.x;
            this.y -= v.y;
            this.z -= v.z;
            return this;
        }

        public Vector3D ssub(final double x, final double y, final double z) {
            this.x -= x;
            this.y -= y;
            this.z -= z;
            return this;
        }

        Vector3D snormalize() {
            final double len = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            this.x /= len;
            this.y /= len;
            this.z /= len;
            return this;
        }

        public Vector3D scross(final Vector3D v) {
            this.x = this.y * v.z - this.z * v.y;
            this.y = this.z * v.x - this.x * v.z;
            this.z = this.x * v.y - this.y * v.x;
            return this;
        }

        public Vector3D smul(final double m) {
            this.x *= m;
            this.y *= m;
            this.z *= m;
            return this;
        }

        public Vector3D sdiv(final double d) {
            this.x /= d;
            this.y /= d;
            this.z /= d;
            return this;
        }

        @Override
        public String toString() {
            return "(X: " + this.x + " Y: " + this.y + " Z: " + this.z + ")";
        }
    }

    public static class Projection {
        private final double x;
        private final double y;
        private final Type t;

        Projection(final double x, final double y, final Type t) {
            this.x = x;
            this.y = y;
            this.t = t;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public Type getType() {
            return this.t;
        }

        public enum Type {
            INSIDE,
            OUTSIDE,
            INVERTED,
            FAIL
        }
    }
}