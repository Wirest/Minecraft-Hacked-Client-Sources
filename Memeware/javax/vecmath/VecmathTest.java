
package javax.vecmath;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.GMatrix;
import javax.vecmath.GVector;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple4d;
import javax.vecmath.Tuple4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;

public class VecmathTest {
    public static String NL = System.getProperty("line.separator");
    public static float epsilon = 1.0E-5f;

    public static boolean equals(double m1, double m2) {
        return Math.abs(m1 - m2) < (double) epsilon;
    }

    public static boolean equals(Matrix3d m1, Matrix3d m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(Matrix4d m1, Matrix4d m2) {
        return m1.epsilonEquals(m2, (double) epsilon);
    }

    public static boolean equals(Tuple4d m1, Tuple4d m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(Tuple3d m1, Tuple3d m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(Matrix3f m1, Matrix3f m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(Matrix4f m1, Matrix4f m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(GMatrix m1, GMatrix m2) {
        return m1.epsilonEquals(m2, (double) epsilon);
    }

    public static boolean equals(GVector v1, GVector v2) {
        return v1.epsilonEquals(v2, epsilon);
    }

    public static boolean equals(Tuple4f m1, Tuple4f m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(Tuple3f m1, Tuple3f m2) {
        return m1.epsilonEquals(m2, epsilon);
    }

    public static boolean equals(AxisAngle4d a1, AxisAngle4d a2) {
        if (0.0 < a1.x * a2.x + a1.y * a2.y + a1.z * a2.z) {
            return VecmathTest.equals(a1.y * a2.z - a1.z * a2.y, 0.0) && VecmathTest.equals(a1.z * a2.x - a1.x * a2.z, 0.0) && VecmathTest.equals(a1.x * a2.y - a1.y * a2.x, 0.0) && VecmathTest.equals(a1.angle, a2.angle);
        }
        return VecmathTest.equals(a1.y * a2.z - a1.z * a2.y, 0.0) && VecmathTest.equals(a1.z * a2.x - a1.x * a2.z, 0.0) && VecmathTest.equals(a1.x * a2.y - a1.y * a2.x, 0.0) && (VecmathTest.equals(a1.angle, -a2.angle) || VecmathTest.equals(a1.angle + a2.angle, 6.283185307179586) || VecmathTest.equals(a1.angle + a2.angle, -6.283185307179586));
    }

    public static boolean equals(AxisAngle4f a1, AxisAngle4f a2) {
        if (0.0f < a1.x * a2.x + a1.y * a2.y + a1.z * a2.z) {
            return VecmathTest.equals(a1.y * a2.z - a1.z * a2.y, 0.0) && VecmathTest.equals(a1.z * a2.x - a1.x * a2.z, 0.0) && VecmathTest.equals(a1.x * a2.y - a1.y * a2.x, 0.0) && VecmathTest.equals(a1.angle, a2.angle);
        }
        return VecmathTest.equals(a1.y * a2.z - a1.z * a2.y, 0.0) && VecmathTest.equals(a1.z * a2.x - a1.x * a2.z, 0.0) && VecmathTest.equals(a1.x * a2.y - a1.y * a2.x, 0.0) && (VecmathTest.equals(a1.angle, -a2.angle) || VecmathTest.equals(a1.angle + a2.angle, 6.283185307179586) || VecmathTest.equals(a1.angle + a2.angle, -6.283185307179586));
    }

    public static void ASSERT(boolean condition) {
        if (!condition) {
            throw new InternalError("Vecmath Test Failed!");
        }
    }

    public static void ASSERT(boolean condition, String comment) {
        if (!condition) {
            throw new InternalError("Vecmath Test Failed!: " + comment);
        }
    }

    public static void exit() {
        System.out.println("java.vecmath all test passed successfully.");
        System.out.print("Quit ?");
        try {
            System.in.read();
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    public static void main(String[] v) {
        System.out.print("Vector3d ...");
        VecmathTest.Vector3dTest();
        System.out.println("ok.");
        System.out.print("Vector3f ...");
        VecmathTest.Vector3fTest();
        System.out.println("ok.");
        System.out.print("Matrix3d with Quat4d, AxisAngle4d, Point/Vector3d interaction ...");
        VecmathTest.Matrix3dTest();
        System.out.println("ok.");
        System.out.print("Matrix3f with Quat4f, AxisAngle4f, Point/Vector3f interaction ...");
        VecmathTest.Matrix3fTest();
        System.out.println("ok.");
        System.out.print("Matrix4d with Quat4d, AxisAngle4d, Point/Vector3d interaction ...");
        VecmathTest.Matrix4dTest();
        System.out.println("ok.");
        System.out.print("Matrix4f with Quat4f, AxisAngle4f, Point/Vector3f interaction ...");
        VecmathTest.Matrix4fTest();
        System.out.println("ok.");
        System.out.print("GMatrix with GVector interaction ...");
        VecmathTest.GMatrixTest();
        System.out.println("ok.");
        System.out.print("SVD test ...");
        VecmathTest.SVDTest();
        System.out.println("ok.");
        VecmathTest.exit();
    }

    public static void Vector3dTest() {
        Vector3d zeroVector = new Vector3d();
        Vector3d v1 = new Vector3d(2.0, 3.0, 4.0);
        Vector3d v2 = new Vector3d(2.0, 5.0, -8.0);
        Vector3d v3 = new Vector3d();
        v3.cross(v1, v2);
        VecmathTest.ASSERT(VecmathTest.equals(v3.dot(v1), 0.0));
        VecmathTest.ASSERT(VecmathTest.equals(v3.dot(v2), 0.0));
        v1.cross(v1, v2);
        VecmathTest.ASSERT(VecmathTest.equals(v1, new Vector3d(-44.0, 24.0, 4.0)));
        VecmathTest.ASSERT(VecmathTest.equals(v2.lengthSquared(), 93.0));
        VecmathTest.ASSERT(VecmathTest.equals(v2.length(), Math.sqrt(93.0)));
        v1.set(v2);
        v2.normalize();
        VecmathTest.ASSERT(VecmathTest.equals(v2.length(), 1.0));
        v1.cross(v2, v1);
        VecmathTest.ASSERT(VecmathTest.equals(v1, zeroVector));
        v1.set(1.0, 2.0, 3.0);
        v2.set(-1.0, -6.0, -3.0);
        double ang = v1.angle(v2);
        VecmathTest.ASSERT(VecmathTest.equals(v1.length() * v2.length() * Math.cos(ang), v1.dot(v2)));
        v1.set(v2);
        ang = v1.angle(v2);
        VecmathTest.ASSERT(VecmathTest.equals(ang, 0.0));
        VecmathTest.ASSERT(VecmathTest.equals(v1.length() * v2.length() * Math.cos(ang), v1.dot(v2)));
        v1.set(1.0, 2.0, 3.0);
        v2.set(1.0, 2.0, 3.00001);
        ang = v1.angle(v2);
        VecmathTest.ASSERT(VecmathTest.equals(v1.length() * v2.length() * Math.cos(ang), v1.dot(v2)));
        v1.set(1.0, 2.0, 3.0);
        v2.set(-1.0, -2.0, -3.00001);
        ang = v1.angle(v2);
        VecmathTest.ASSERT(VecmathTest.equals(v1.length() * v2.length() * Math.cos(ang), v1.dot(v2)));
    }

    public static void Vector3fTest() {
        Vector3f zeroVector = new Vector3f();
        Vector3f v1 = new Vector3f(2.0f, 3.0f, 4.0f);
        Vector3f v2 = new Vector3f(2.0f, 5.0f, -8.0f);
        Vector3f v3 = new Vector3f();
        v3.cross(v1, v2);
        VecmathTest.ASSERT(VecmathTest.equals(v3.dot(v1), 0.0));
        VecmathTest.ASSERT(VecmathTest.equals(v3.dot(v2), 0.0));
        v1.cross(v1, v2);
        VecmathTest.ASSERT(VecmathTest.equals(v1, new Vector3f(-44.0f, 24.0f, 4.0f)));
        VecmathTest.ASSERT(VecmathTest.equals(v2.lengthSquared(), 93.0));
        VecmathTest.ASSERT(VecmathTest.equals(v2.length(), Math.sqrt(93.0)));
        v1.set(v2);
        v2.normalize();
        VecmathTest.ASSERT(VecmathTest.equals(v2.length(), 1.0));
        v1.cross(v2, v1);
        VecmathTest.ASSERT(VecmathTest.equals(v1, zeroVector));
        v1.set(1.0f, 2.0f, 3.0f);
        v2.set(-1.0f, -6.0f, -3.0f);
        double ang = v1.angle(v2);
        VecmathTest.ASSERT(VecmathTest.equals((double) (v1.length() * v2.length()) * Math.cos(ang), v1.dot(v2)));
        v1.set(v2);
        ang = v1.angle(v2);
        VecmathTest.ASSERT(VecmathTest.equals(ang, 0.0));
        VecmathTest.ASSERT(VecmathTest.equals((double) (v1.length() * v2.length()) * Math.cos(ang), v1.dot(v2)));
    }

    public static void Matrix3dTest() {
        int i;
        int j;
        Matrix3d O = new Matrix3d();
        Matrix3d I = new Matrix3d();
        I.setIdentity();
        Matrix3d m1 = new Matrix3d();
        Matrix3d m2 = new Matrix3d();
        double[] v = new double[]{2.0, 1.0, 4.0, 1.0, -2.0, 3.0, -3.0, -1.0, 1.0};
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                m1.setElement(i, j, i * 2 * j + 3);
            }
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                VecmathTest.ASSERT(VecmathTest.equals(m1.getElement(i, j), i * 2 * j + 3));
            }
        }
        m1.set(v);
        m2 = new Matrix3d(m1);
        m2.mul(O);
        VecmathTest.ASSERT(VecmathTest.equals(m2, O));
        m2.mul(m1, I);
        VecmathTest.ASSERT(VecmathTest.equals(m2, m1));
        VecmathTest.ASSERT(VecmathTest.equals(m1.determinant(), -36.0));
        m2.negate(m1);
        m2.add(m1);
        VecmathTest.ASSERT(VecmathTest.equals(m2, O));
        m2.negate(m1);
        Matrix3d m3 = new Matrix3d(m1);
        m3.sub(m2);
        m3.mul(0.5);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m3));
        m3.invert(m2);
        m3.mul(m2);
        VecmathTest.ASSERT(VecmathTest.equals(m3, I));
        Point3d p1 = new Point3d(1.0, 2.0, 3.0);
        Vector3d v2 = new Vector3d(2.0, -1.0, -4.0);
        p1.set(1.0, 0.0, 0.0);
        m1.rotZ(0.5235987755982988);
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(0.5235987755982988), Math.sin(0.5235987755982988), 0.0)));
        p1.set(1.0, 0.0, 0.0);
        m1.rotY(1.0471975511965976);
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(1.0471975511965976), 0.0, -Math.sin(1.0471975511965976))));
        AxisAngle4d a1 = new AxisAngle4d(0.0, 1.0, 0.0, 1.0471975511965976);
        p1.set(1.0, 0.0, 0.0);
        m1.set(a1);
        m1.transform(p1, p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(1.0471975511965976), 0.0, -Math.sin(1.0471975511965976))));
        Quat4d q1 = new Quat4d();
        p1.set(1.0, 0.0, 0.0);
        q1.set(a1);
        m2.set(q1);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        m2.transform(p1, p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(1.0471975511965976), 0.0, -Math.sin(1.0471975511965976))));
        a1.set(1.0, 2.0, -3.0, 1.0471975511965976);
        VecmathTest.Mat3dQuatAxisAngle(a1);
        a1.set(1.0, 2.0, 3.0, 3.141592653589793);
        VecmathTest.Mat3dQuatAxisAngle(a1);
        a1.set(1.0, 0.1, 0.1, 3.141592653589793);
        VecmathTest.Mat3dQuatAxisAngle(a1);
        a1.set(0.1, 1.0, 0.1, 3.141592653589793);
        VecmathTest.Mat3dQuatAxisAngle(a1);
        a1.set(0.1, 0.1, 1.0, 3.141592653589793);
        VecmathTest.Mat3dQuatAxisAngle(a1);
        a1.set(1.0, 1.0, 1.0, 2.0943951023931953);
        m1.set(a1);
        p1.set(1.0, 0.0, 0.0);
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(0.0, 1.0, 0.0)));
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(0.0, 0.0, 1.0)));
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(1.0, 0.0, 0.0)));
        m1.set(a1);
        VecmathTest.ASSERT(VecmathTest.equals(m1.determinant(), 1.0));
        VecmathTest.ASSERT(VecmathTest.equals(m1.getScale(), 1.0));
        m2.set(a1);
        m2.normalize();
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        m2.set(a1);
        m2.normalizeCP();
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        double scale = 3.0;
        m2.rotZ(-0.7853981633974483);
        m2.mul(3.0);
        VecmathTest.ASSERT(VecmathTest.equals(m2.determinant(), 27.0));
        VecmathTest.ASSERT(VecmathTest.equals(m2.getScale(), 3.0));
        m2.normalize();
        VecmathTest.ASSERT(VecmathTest.equals(m2.determinant(), 1.0));
        VecmathTest.ASSERT(VecmathTest.equals(m2.getScale(), 1.0));
        m2.rotX(1.0471975511965976);
        m2.mul(3.0);
        VecmathTest.ASSERT(VecmathTest.equals(m2.determinant(), 27.0));
        VecmathTest.ASSERT(VecmathTest.equals(m2.getScale(), 3.0));
        m2.normalizeCP();
        VecmathTest.ASSERT(VecmathTest.equals(m2.determinant(), 1.0));
        VecmathTest.ASSERT(VecmathTest.equals(m2.getScale(), 1.0));
        m1.set(a1);
        m2.invert(m1);
        m1.transpose();
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
    }

    static void Mat3dQuatAxisAngle(AxisAngle4d a1) {
        Matrix3d m1 = new Matrix3d();
        Matrix3d m2 = new Matrix3d();
        AxisAngle4d a2 = new AxisAngle4d();
        Quat4d q1 = new Quat4d();
        Quat4d q2 = new Quat4d();
        q1.set(a1);
        a2.set(q1);
        VecmathTest.ASSERT(VecmathTest.equals(a1, a2));
        q2 = new Quat4d();
        q2.set(a2);
        VecmathTest.ASSERT(VecmathTest.equals(q1, q2));
        q1.set(a1);
        m1.set(q1);
        q2.set(m1);
        VecmathTest.ASSERT(VecmathTest.equals(q1, q2));
        m2.set(q2);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        m1.set(a1);
        a2.set(m1);
        VecmathTest.ASSERT(VecmathTest.equals(a1, a2));
        m2.set(a1);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        a1.x *= 2.0;
        a1.y *= 2.0;
        a1.z *= 2.0;
        m2.set(a1);
        a1.x = -a1.x;
        a1.y = -a1.y;
        a1.z = -a1.z;
        a1.angle = -a1.angle;
        m2.set(a1);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
    }

    public static void Matrix3fTest() {
    }

    static void Mat4dQuatAxisAngle(AxisAngle4d a1) {
        Matrix4d m1 = new Matrix4d();
        Matrix4d m2 = new Matrix4d();
        AxisAngle4d a2 = new AxisAngle4d();
        Quat4d q1 = new Quat4d();
        Quat4d q2 = new Quat4d();
        q1.set(a1);
        a2.set(q1);
        VecmathTest.ASSERT(VecmathTest.equals(a1, a2));
        q2 = new Quat4d();
        q2.set(a2);
        VecmathTest.ASSERT(VecmathTest.equals(q1, q2));
        q1.set(a1);
        m1.set(q1);
        q2.set(m1);
        VecmathTest.ASSERT(VecmathTest.equals(q1, q2));
        m2.set(q2);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        m1.set(a1);
        a2.set(m1);
        VecmathTest.ASSERT(VecmathTest.equals(a1, a2));
        m2.set(a1);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        a1.x *= 2.0;
        a1.y *= 2.0;
        a1.z *= 2.0;
        m2.set(a1);
        a1.x = -a1.x;
        a1.y = -a1.y;
        a1.z = -a1.z;
        a1.angle = -a1.angle;
        m2.set(a1);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
    }

    public static void Matrix4dTest() {
        int i;
        int j;
        Matrix4d O = new Matrix4d();
        Matrix4d I = new Matrix4d();
        I.setIdentity();
        Matrix4d m1 = new Matrix4d();
        Matrix4d m2 = new Matrix4d();
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 4; ++j) {
                m1.setElement(i, j, i * 2 * j + 3);
            }
        }
        for (i = 0; i < 4; ++i) {
            for (j = 0; j < 4; ++j) {
                VecmathTest.ASSERT(VecmathTest.equals(m1.getElement(i, j), i * 2 * j + 3));
            }
        }
        m1 = new Matrix4d(2.0, 1.0, 4.0, 1.0, -2.0, 3.0, -3.0, 1.0, -1.0, 1.0, 2.0, 2.0, 0.0, 8.0, 1.0, -10.0);
        m2 = new Matrix4d(m1);
        m2.mul(O);
        VecmathTest.ASSERT(VecmathTest.equals(m2, O), "O = m2 x O");
        m2.mul(m1, I);
        VecmathTest.ASSERT(VecmathTest.equals(m2, m1), "m2 = m1 x I");
        m2.negate(m1);
        m2.add(m1);
        VecmathTest.ASSERT(VecmathTest.equals(m2, O));
        double[] v = new double[]{5.0, 1.0, 4.0, 0.0, 2.0, 3.0, -4.0, -1.0, 2.0, 3.0, -4.0, -1.0, 1.0, 1.0, 1.0, 1.0};
        m2.set(v);
        m2.negate(m1);
        Matrix4d m3 = new Matrix4d(m1);
        m3.sub(m2);
        m3.mul(0.5);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m3));
        m2 = new Matrix4d(0.5, 1.0, 4.0, 1.0, -2.0, 3.0, -4.0, -1.0, 1.0, 9.0, 100.0, 2.0, -20.0, 2.0, 1.0, 9.0);
        m3.invert(m2);
        m3.mul(m2);
        VecmathTest.ASSERT(VecmathTest.equals(m3, I));
        m1 = new Matrix4d(-1.0, 2.0, 0.0, 3.0, -1.0, 1.0, -3.0, -1.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0);
        Point3d p1 = new Point3d(1.0, 2.0, 3.0);
        Vector3d v2 = new Vector3d();
        Vector3d v3 = new Vector3d(1.0, 2.0, 3.0);
        Vector4d V2 = new Vector4d(2.0, -1.0, -4.0, 1.0);
        VecmathTest.ASSERT(m1.toString().equals("[" + NL + "  [-1.0\t2.0\t0.0\t3.0]" + NL + "  [-1.0\t1.0\t-3.0\t-1.0]" + NL + "  [1.0\t2.0\t1.0\t1.0]" + NL + "  [0.0\t0.0\t0.0\t1.0] ]"));
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(6.0, -9.0, 9.0)));
        m1.transform(V2, V2);
        VecmathTest.ASSERT(VecmathTest.equals(V2, new Vector4d(-1.0, 8.0, -3.0, 1.0)));
        p1.set(1.0, 0.0, 0.0);
        m1.rotZ(0.5235987755982988);
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(0.5235987755982988), Math.sin(0.5235987755982988), 0.0)));
        p1.set(1.0, 0.0, 0.0);
        m1.rotY(1.0471975511965976);
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(1.0471975511965976), 0.0, -Math.sin(1.0471975511965976))));
        AxisAngle4d a1 = new AxisAngle4d(0.0, 1.0, 0.0, 1.0471975511965976);
        p1.set(1.0, 0.0, 0.0);
        m1.set(a1);
        m1.transform(p1, p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(1.0471975511965976), 0.0, -Math.sin(1.0471975511965976))));
        Quat4d q1 = new Quat4d();
        p1.set(1.0, 0.0, 0.0);
        q1.set(a1);
        m2.set(q1);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        m2.transform(p1, p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(Math.cos(1.0471975511965976), 0.0, -Math.sin(1.0471975511965976))));
        a1.set(1.0, 2.0, -3.0, 1.0471975511965976);
        VecmathTest.Mat4dQuatAxisAngle(a1);
        a1.set(1.0, 2.0, 3.0, 3.141592653589793);
        VecmathTest.Mat4dQuatAxisAngle(a1);
        a1.set(1.0, 0.1, 0.1, 3.141592653589793);
        VecmathTest.Mat4dQuatAxisAngle(a1);
        a1.set(0.1, 1.0, 0.1, 3.141592653589793);
        VecmathTest.Mat4dQuatAxisAngle(a1);
        a1.set(0.1, 0.1, 1.0, 3.141592653589793);
        VecmathTest.Mat4dQuatAxisAngle(a1);
        a1.set(1.0, 1.0, 1.0, 2.0943951023931953);
        m1.set(a1);
        p1.set(1.0, 0.0, 0.0);
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(0.0, 1.0, 0.0)));
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(0.0, 0.0, 1.0)));
        m1.transform(p1);
        VecmathTest.ASSERT(VecmathTest.equals(p1, new Point3d(1.0, 0.0, 0.0)));
        m1.set(a1);
        VecmathTest.ASSERT(VecmathTest.equals(m1.determinant(), 1.0));
        VecmathTest.ASSERT(VecmathTest.equals(m1.getScale(), 1.0));
        m2.set(a1);
        m1.set(a1);
        m2.invert(m1);
        m1.transpose();
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        Matrix3d n1 = new Matrix3d();
        n1.set(a1);
        Matrix3d n2 = new Matrix3d();
        v3.set(2.0, -1.0, -1.0);
        m1.set(n1, v3, 0.4);
        m2.set(n1, v3, 0.4);
        Vector3d v4 = new Vector3d();
        double s = m1.get(n2, v4);
        VecmathTest.ASSERT(VecmathTest.equals(n1, n2));
        VecmathTest.ASSERT(VecmathTest.equals(s, 0.4));
        VecmathTest.ASSERT(VecmathTest.equals(v3, v4));
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
    }

    public static void Matrix4fTest() {
    }

    public static void GMatrixTest() {
        int j;
        int i;
        GMatrix I44 = new GMatrix(4, 4);
        GMatrix O44 = new GMatrix(4, 4);
        O44.setZero();
        GMatrix O45 = new GMatrix(3, 4);
        O45.setZero();
        GMatrix m1 = new GMatrix(3, 4);
        GMatrix m2 = new GMatrix(3, 4);
        Matrix3d mm1 = new Matrix3d();
        Matrix3d mm2 = new Matrix3d();
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 4; ++j) {
                m1.setElement(i, j, (i + 1) * (j + 2));
                if (j >= 3) continue;
                mm1.setElement(i, j, (i + 1) * (j + 2));
            }
        }
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 4; ++j) {
                VecmathTest.ASSERT(VecmathTest.equals(m1.getElement(i, j), (i + 1) * (j + 2)));
            }
        }
        m1.get(mm2);
        VecmathTest.ASSERT(VecmathTest.equals(mm1, mm2));
        m2.mul(m1, I44);
        VecmathTest.ASSERT(VecmathTest.equals(m1, m2));
        m2.mul(m1, O44);
        VecmathTest.ASSERT(VecmathTest.equals(O45, m2));
        Matrix4d mm3 = new Matrix4d(1.0, 2.0, 3.0, 4.0, -2.0, 3.0, -1.0, 3.0, -1.0, -2.0, -4.0, 1.0, 1.0, 1.0, -1.0, -2.0);
        Matrix4d mm4 = new Matrix4d();
        Matrix4d mm5 = new Matrix4d();
        mm5.set(mm3);
        m1.setSize(4, 4);
        m2.setSize(4, 4);
        m1.set(mm3);
        VecmathTest.ASSERT(m1.toString().equals("[" + NL + "  [1.0\t2.0\t3.0\t4.0]" + NL + "  [-2.0\t3.0\t-1.0\t3.0]" + NL + "  [-1.0\t-2.0\t-4.0\t1.0]" + NL + "  [1.0\t1.0\t-1.0\t-2.0] ]"));
        m2.set(m1);
        m1.invert();
        mm3.invert();
        mm5.mul(mm3);
        VecmathTest.ASSERT(VecmathTest.equals(mm5, new Matrix4d(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)));
        m1.get(mm4);
        VecmathTest.ASSERT(VecmathTest.equals(mm3, mm4));
        m1.mul(m2);
        VecmathTest.ASSERT(VecmathTest.equals(m1, I44));
        Matrix4d mm6 = new Matrix4d(1.0, 2.0, 3.0, 4.0, -2.0, 3.0, -1.0, 3.0, -1.0, -2.0, -4.0, 1.0, 1.0, 1.0, -1.0, -2.0);
        Vector4d vv1 = new Vector4d(1.0, -1.0, -1.0, 2.0);
        Vector4d vv2 = new Vector4d();
        Vector4d vv3 = new Vector4d(4.0, 2.0, 7.0, -3.0);
        mm6.transform(vv1, vv2);
        VecmathTest.ASSERT(VecmathTest.equals(vv2, vv3));
        m1.set(mm6);
        GVector x = new GVector(4);
        GVector v2 = new GVector(4);
        GVector b = new GVector(4);
        x.set(vv1);
        b.set(vv3);
        GVector mx = new GVector(4);
        mx.mul(m1, x);
        VecmathTest.ASSERT(VecmathTest.equals(mx, b));
        GVector p2 = new GVector(4);
        m1.LUD(m2, p2);
        VecmathTest.ASSERT(VecmathTest.checkLUD(m1, m2, p2));
        GVector xx = new GVector(4);
        xx.LUDBackSolve(m2, b, p2);
        VecmathTest.ASSERT(VecmathTest.equals(xx, x));
        GMatrix u = new GMatrix(m1.getNumRow(), m1.getNumRow());
        GMatrix w = new GMatrix(m1.getNumRow(), m1.getNumCol());
        GMatrix v3 = new GMatrix(m1.getNumCol(), m1.getNumCol());
        int rank = m1.SVD(u, w, v3);
        VecmathTest.ASSERT(rank == 4);
        VecmathTest.ASSERT(VecmathTest.checkSVD(m1, u, w, v3));
        xx.SVDBackSolve(u, w, v3, b);
        VecmathTest.ASSERT(VecmathTest.equals(xx, x));
    }

    static boolean checkLUD(GMatrix m, GMatrix LU, GVector permutation) {
        int n = m.getNumCol();
        boolean ok = true;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                double aij = 0.0;
                int min = i < j ? i : j;
                for (int k = 0; k <= min; ++k) {
                    if (i != k) {
                        aij += LU.getElement(i, k) * LU.getElement(k, j);
                        continue;
                    }
                    aij += LU.getElement(k, j);
                }
                if (!(Math.abs(aij - m.getElement((int) permutation.getElement(i), j)) > (double) epsilon)) continue;
                System.out.println("a[" + i + "," + j + "] = " + aij + "(LU)ij ! = " + m.getElement((int) permutation.getElement(i), j));
                ok = false;
            }
        }
        return ok;
    }

    static boolean checkSVD(GMatrix m, GMatrix u, GMatrix w, GMatrix v) {
        boolean ok = true;
        int wsize = w.getNumRow() < w.getNumRow() ? w.getNumRow() : w.getNumCol();
        for (int i = 0; i < m.getNumRow(); ++i) {
            for (int j = 0; j < m.getNumCol(); ++j) {
                double sum = 0.0;
                for (int k = 0; k < m.getNumCol(); ++k) {
                    sum += u.getElement(i, k) * w.getElement(k, k) * v.getElement(j, k);
                }
                if (!((double) epsilon < Math.abs(m.getElement(i, j) - sum))) continue;
                System.out.println("(SVD)ij = " + sum + " != a[" + i + "," + j + "] = " + m.getElement(i, j));
                ok = false;
            }
        }
        if (!ok) {
            System.out.print("[W] = ");
            System.out.println(w);
            System.out.print("[U] = ");
            System.out.println(u);
            System.out.print("[V] = ");
            System.out.println(v);
        }
        return ok;
    }

    public static void SVDTest() {
        double[] val = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0, 1.0};
        int m = 5;
        int n = 4;
        GMatrix matA = new GMatrix(5, 4, val);
        GMatrix matU = new GMatrix(5, 5);
        GMatrix matW = new GMatrix(5, 4);
        GMatrix matV = new GMatrix(4, 4);
        int rank = matA.SVD(matU, matW, matV);
        GMatrix matTEMP = new GMatrix(5, 4);
        matTEMP.mul(matU, matW);
        matV.transpose();
        matTEMP.mul(matV);
        if (!VecmathTest.equals(matTEMP, matA)) {
            System.out.println("matU=" + matU);
            System.out.println("matW=" + matW);
            System.out.println("matV=" + matV);
            System.out.println("matA=" + matA);
            System.out.println("UWV=" + matTEMP);
        }
        VecmathTest.ASSERT(VecmathTest.equals(matTEMP, matA));
    }
}

