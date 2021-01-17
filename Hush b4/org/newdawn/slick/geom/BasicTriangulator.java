// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.util.ArrayList;

public class BasicTriangulator implements Triangulator
{
    private static final float EPSILON = 1.0E-10f;
    private PointList poly;
    private PointList tris;
    private boolean tried;
    
    public BasicTriangulator() {
        this.poly = new PointList();
        this.tris = new PointList();
    }
    
    @Override
    public void addPolyPoint(final float x, final float y) {
        final Point p = new Point(x, y);
        if (!this.poly.contains(p)) {
            this.poly.add(p);
        }
    }
    
    public int getPolyPointCount() {
        return this.poly.size();
    }
    
    public float[] getPolyPoint(final int index) {
        return new float[] { this.poly.get(index).x, this.poly.get(index).y };
    }
    
    @Override
    public boolean triangulate() {
        this.tried = true;
        final boolean worked = this.process(this.poly, this.tris);
        return worked;
    }
    
    @Override
    public int getTriangleCount() {
        if (!this.tried) {
            throw new RuntimeException("Call triangulate() before accessing triangles");
        }
        return this.tris.size() / 3;
    }
    
    @Override
    public float[] getTrianglePoint(final int tri, final int i) {
        if (!this.tried) {
            throw new RuntimeException("Call triangulate() before accessing triangles");
        }
        return this.tris.get(tri * 3 + i).toArray();
    }
    
    private float area(final PointList contour) {
        final int n = contour.size();
        float A = 0.0f;
        int p = n - 1;
        for (int q = 0; q < n; p = q++) {
            final Point contourP = contour.get(p);
            final Point contourQ = contour.get(q);
            A += contourP.getX() * contourQ.getY() - contourQ.getX() * contourP.getY();
        }
        return A * 0.5f;
    }
    
    private boolean insideTriangle(final float Ax, final float Ay, final float Bx, final float By, final float Cx, final float Cy, final float Px, final float Py) {
        final float ax = Cx - Bx;
        final float ay = Cy - By;
        final float bx = Ax - Cx;
        final float by = Ay - Cy;
        final float cx = Bx - Ax;
        final float cy = By - Ay;
        final float apx = Px - Ax;
        final float apy = Py - Ay;
        final float bpx = Px - Bx;
        final float bpy = Py - By;
        final float cpx = Px - Cx;
        final float cpy = Py - Cy;
        final float aCROSSbp = ax * bpy - ay * bpx;
        final float cCROSSap = cx * apy - cy * apx;
        final float bCROSScp = bx * cpy - by * cpx;
        return aCROSSbp >= 0.0f && bCROSScp >= 0.0f && cCROSSap >= 0.0f;
    }
    
    private boolean snip(final PointList contour, final int u, final int v, final int w, final int n, final int[] V) {
        final float Ax = contour.get(V[u]).getX();
        final float Ay = contour.get(V[u]).getY();
        final float Bx = contour.get(V[v]).getX();
        final float By = contour.get(V[v]).getY();
        final float Cx = contour.get(V[w]).getX();
        final float Cy = contour.get(V[w]).getY();
        if (1.0E-10f > (Bx - Ax) * (Cy - Ay) - (By - Ay) * (Cx - Ax)) {
            return false;
        }
        for (int p = 0; p < n; ++p) {
            if (p != u && p != v) {
                if (p != w) {
                    final float Px = contour.get(V[p]).getX();
                    final float Py = contour.get(V[p]).getY();
                    if (this.insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean process(final PointList contour, final PointList result) {
        result.clear();
        final int n = contour.size();
        if (n < 3) {
            return false;
        }
        final int[] V = new int[n];
        if (0.0f < this.area(contour)) {
            for (int v = 0; v < n; ++v) {
                V[v] = v;
            }
        }
        else {
            for (int v = 0; v < n; ++v) {
                V[v] = n - 1 - v;
            }
        }
        int nv = n;
        int count = 2 * nv;
        int m = 0;
        int v2 = nv - 1;
        while (nv > 2) {
            if (count-- <= 0) {
                return false;
            }
            int u = v2;
            if (nv <= u) {
                u = 0;
            }
            v2 = u + 1;
            if (nv <= v2) {
                v2 = 0;
            }
            int w = v2 + 1;
            if (nv <= w) {
                w = 0;
            }
            if (!this.snip(contour, u, v2, w, nv, V)) {
                continue;
            }
            final int a = V[u];
            final int b = V[v2];
            final int c = V[w];
            result.add(contour.get(a));
            result.add(contour.get(b));
            result.add(contour.get(c));
            ++m;
            int s = v2;
            for (int t = v2 + 1; t < nv; ++t) {
                V[s] = V[t];
                ++s;
            }
            --nv;
            count = 2 * nv;
        }
        return true;
    }
    
    @Override
    public void startHole() {
    }
    
    private class Point
    {
        private float x;
        private float y;
        private float[] array;
        
        public Point(final float x, final float y) {
            this.x = x;
            this.y = y;
            this.array = new float[] { x, y };
        }
        
        public float getX() {
            return this.x;
        }
        
        public float getY() {
            return this.y;
        }
        
        public float[] toArray() {
            return this.array;
        }
        
        @Override
        public int hashCode() {
            return (int)(this.x * this.y * 31.0f);
        }
        
        @Override
        public boolean equals(final Object other) {
            if (other instanceof Point) {
                final Point p = (Point)other;
                return p.x == this.x && p.y == this.y;
            }
            return false;
        }
    }
    
    private class PointList
    {
        private ArrayList points;
        
        public PointList() {
            this.points = new ArrayList();
        }
        
        public boolean contains(final Point p) {
            return this.points.contains(p);
        }
        
        public void add(final Point point) {
            this.points.add(point);
        }
        
        public void remove(final Point point) {
            this.points.remove(point);
        }
        
        public int size() {
            return this.points.size();
        }
        
        public Point get(final int i) {
            return this.points.get(i);
        }
        
        public void clear() {
            this.points.clear();
        }
    }
}
