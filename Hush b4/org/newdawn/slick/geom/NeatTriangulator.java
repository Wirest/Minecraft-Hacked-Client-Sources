// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public class NeatTriangulator implements Triangulator
{
    static final float EPSILON = 1.0E-6f;
    private float[] pointsX;
    private float[] pointsY;
    private int numPoints;
    private Edge[] edges;
    private int[] V;
    private int numEdges;
    private Triangle[] triangles;
    private int numTriangles;
    private float offset;
    
    public NeatTriangulator() {
        this.offset = 1.0E-6f;
        this.pointsX = new float[100];
        this.pointsY = new float[100];
        this.numPoints = 0;
        this.edges = new Edge[100];
        this.numEdges = 0;
        this.triangles = new Triangle[100];
        this.numTriangles = 0;
    }
    
    public void clear() {
        this.numPoints = 0;
        this.numEdges = 0;
        this.numTriangles = 0;
    }
    
    private int findEdge(final int i, final int j) {
        int k;
        int l;
        if (i < j) {
            k = i;
            l = j;
        }
        else {
            k = j;
            l = i;
        }
        for (int i2 = 0; i2 < this.numEdges; ++i2) {
            if (this.edges[i2].v0 == k && this.edges[i2].v1 == l) {
                return i2;
            }
        }
        return -1;
    }
    
    private void addEdge(final int i, final int j, final int k) {
        int l1 = this.findEdge(i, j);
        int j2;
        int k2;
        Edge edge;
        if (l1 < 0) {
            if (this.numEdges == this.edges.length) {
                final Edge[] aedge = new Edge[this.edges.length * 2];
                System.arraycopy(this.edges, 0, aedge, 0, this.numEdges);
                this.edges = aedge;
            }
            j2 = -1;
            k2 = -1;
            l1 = this.numEdges++;
            final Edge[] edges = this.edges;
            final int n = l1;
            final Edge edge2 = new Edge();
            edges[n] = edge2;
            edge = edge2;
        }
        else {
            edge = this.edges[l1];
            j2 = edge.t0;
            k2 = edge.t1;
        }
        int m;
        int i2;
        if (i < j) {
            m = i;
            i2 = j;
            j2 = k;
        }
        else {
            m = j;
            i2 = i;
            k2 = k;
        }
        edge.v0 = m;
        edge.v1 = i2;
        edge.t0 = j2;
        edge.t1 = k2;
        edge.suspect = true;
    }
    
    private void deleteEdge(final int i, final int j) throws InternalException {
        final int k;
        if ((k = this.findEdge(i, j)) < 0) {
            throw new InternalException("Attempt to delete unknown edge");
        }
        final Edge[] edges = this.edges;
        final int n = k;
        final Edge[] edges2 = this.edges;
        final int numEdges = this.numEdges - 1;
        this.numEdges = numEdges;
        edges[n] = edges2[numEdges];
    }
    
    void markSuspect(final int i, final int j, final boolean flag) throws InternalException {
        final int k;
        if ((k = this.findEdge(i, j)) < 0) {
            throw new InternalException("Attempt to mark unknown edge");
        }
        this.edges[k].suspect = flag;
    }
    
    private Edge chooseSuspect() {
        for (int i = 0; i < this.numEdges; ++i) {
            final Edge edge = this.edges[i];
            if (edge.suspect) {
                edge.suspect = false;
                if (edge.t0 >= 0 && edge.t1 >= 0) {
                    return edge;
                }
            }
        }
        return null;
    }
    
    private static float rho(final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
        final float f6 = f4 - f2;
        final float f7 = f5 - f3;
        final float f8 = f - f4;
        final float f9 = f1 - f5;
        float f10 = f6 * f9 - f7 * f8;
        if (f10 > 0.0f) {
            if (f10 < 1.0E-6f) {
                f10 = 1.0E-6f;
            }
            final float f11 = f6 * f6;
            final float f12 = f7 * f7;
            final float f13 = f8 * f8;
            final float f14 = f9 * f9;
            final float f15 = f2 - f;
            final float f16 = f3 - f1;
            final float f17 = f15 * f15;
            final float f18 = f16 * f16;
            return (f11 + f12) * (f13 + f14) * (f17 + f18) / (f10 * f10);
        }
        return -1.0f;
    }
    
    private static boolean insideTriangle(final float f, final float f1, final float f2, final float f3, final float f4, final float f5, final float f6, final float f7) {
        final float f8 = f4 - f2;
        final float f9 = f5 - f3;
        final float f10 = f - f4;
        final float f11 = f1 - f5;
        final float f12 = f2 - f;
        final float f13 = f3 - f1;
        final float f14 = f6 - f;
        final float f15 = f7 - f1;
        final float f16 = f6 - f2;
        final float f17 = f7 - f3;
        final float f18 = f6 - f4;
        final float f19 = f7 - f5;
        final float f20 = f8 * f17 - f9 * f16;
        final float f21 = f12 * f15 - f13 * f14;
        final float f22 = f10 * f19 - f11 * f18;
        return f20 >= 0.0 && f22 >= 0.0 && f21 >= 0.0;
    }
    
    private boolean snip(final int i, final int j, final int k, final int l) {
        final float f = this.pointsX[this.V[i]];
        final float f2 = this.pointsY[this.V[i]];
        final float f3 = this.pointsX[this.V[j]];
        final float f4 = this.pointsY[this.V[j]];
        final float f5 = this.pointsX[this.V[k]];
        final float f6 = this.pointsY[this.V[k]];
        if (1.0E-6f > (f3 - f) * (f6 - f2) - (f4 - f2) * (f5 - f)) {
            return false;
        }
        for (int i2 = 0; i2 < l; ++i2) {
            if (i2 != i && i2 != j && i2 != k) {
                final float f7 = this.pointsX[this.V[i2]];
                final float f8 = this.pointsY[this.V[i2]];
                if (insideTriangle(f, f2, f3, f4, f5, f6, f7, f8)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private float area() {
        float f = 0.0f;
        int i = this.numPoints - 1;
        for (int j = 0; j < this.numPoints; i = j++) {
            f += this.pointsX[i] * this.pointsY[j] - this.pointsY[i] * this.pointsX[j];
        }
        return f * 0.5f;
    }
    
    public void basicTriangulation() throws InternalException {
        int i = this.numPoints;
        if (i < 3) {
            return;
        }
        this.numEdges = 0;
        this.numTriangles = 0;
        this.V = new int[i];
        if (0.0 < this.area()) {
            for (int k = 0; k < i; ++k) {
                this.V[k] = k;
            }
        }
        else {
            for (int l = 0; l < i; ++l) {
                this.V[l] = this.numPoints - 1 - l;
            }
        }
        int k2 = 2 * i;
        int i2 = i - 1;
        while (i > 2) {
            if (k2-- <= 0) {
                throw new InternalException("Bad polygon");
            }
            int j = i2;
            if (i <= j) {
                j = 0;
            }
            i2 = j + 1;
            if (i <= i2) {
                i2 = 0;
            }
            int j2 = i2 + 1;
            if (i <= j2) {
                j2 = 0;
            }
            if (!this.snip(j, i2, j2, i)) {
                continue;
            }
            final int l2 = this.V[j];
            final int i3 = this.V[i2];
            final int j3 = this.V[j2];
            if (this.numTriangles == this.triangles.length) {
                final Triangle[] atriangle = new Triangle[this.triangles.length * 2];
                System.arraycopy(this.triangles, 0, atriangle, 0, this.numTriangles);
                this.triangles = atriangle;
            }
            this.triangles[this.numTriangles] = new Triangle(l2, i3, j3);
            this.addEdge(l2, i3, this.numTriangles);
            this.addEdge(i3, j3, this.numTriangles);
            this.addEdge(j3, l2, this.numTriangles);
            ++this.numTriangles;
            int k3 = i2;
            for (int l3 = i2 + 1; l3 < i; ++l3) {
                this.V[k3] = this.V[l3];
                ++k3;
            }
            --i;
            k2 = 2 * i;
        }
        this.V = null;
    }
    
    private void optimize() throws InternalException {
        Edge edge;
        while ((edge = this.chooseSuspect()) != null) {
            final int i1 = edge.v0;
            final int k1 = edge.v1;
            final int j = edge.t0;
            final int l = edge.t1;
            int j2 = -1;
            int l2 = -1;
            for (int m = 0; m < 3; ++m) {
                final int i2 = this.triangles[j].v[m];
                if (i1 != i2 && k1 != i2) {
                    l2 = i2;
                    break;
                }
            }
            for (int l3 = 0; l3 < 3; ++l3) {
                final int j3 = this.triangles[l].v[l3];
                if (i1 != j3 && k1 != j3) {
                    j2 = j3;
                    break;
                }
            }
            if (-1 == j2 || -1 == l2) {
                throw new InternalException("can't find quad");
            }
            final float f = this.pointsX[i1];
            final float f2 = this.pointsY[i1];
            final float f3 = this.pointsX[j2];
            final float f4 = this.pointsY[j2];
            final float f5 = this.pointsX[k1];
            final float f6 = this.pointsY[k1];
            final float f7 = this.pointsX[l2];
            final float f8 = this.pointsY[l2];
            float f9 = rho(f, f2, f3, f4, f5, f6);
            final float f10 = rho(f, f2, f5, f6, f7, f8);
            float f11 = rho(f3, f4, f5, f6, f7, f8);
            final float f12 = rho(f3, f4, f7, f8, f, f2);
            if (0.0f > f9 || 0.0f > f10) {
                throw new InternalException("original triangles backwards");
            }
            if (0.0f > f11 || 0.0f > f12) {
                continue;
            }
            if (f9 > f10) {
                f9 = f10;
            }
            if (f11 > f12) {
                f11 = f12;
            }
            if (f9 <= f11) {
                continue;
            }
            this.deleteEdge(i1, k1);
            this.triangles[j].v[0] = j2;
            this.triangles[j].v[1] = k1;
            this.triangles[j].v[2] = l2;
            this.triangles[l].v[0] = j2;
            this.triangles[l].v[1] = l2;
            this.triangles[l].v[2] = i1;
            this.addEdge(j2, k1, j);
            this.addEdge(k1, l2, j);
            this.addEdge(l2, j2, j);
            this.addEdge(l2, i1, l);
            this.addEdge(i1, j2, l);
            this.addEdge(j2, l2, l);
            this.markSuspect(j2, l2, false);
        }
    }
    
    @Override
    public boolean triangulate() {
        try {
            this.basicTriangulation();
            return true;
        }
        catch (InternalException e) {
            this.numEdges = 0;
            return false;
        }
    }
    
    @Override
    public void addPolyPoint(final float x, float y) {
        for (int i = 0; i < this.numPoints; ++i) {
            if (this.pointsX[i] == x && this.pointsY[i] == y) {
                y += this.offset;
                this.offset += 1.0E-6f;
            }
        }
        if (this.numPoints == this.pointsX.length) {
            float[] af = new float[this.numPoints * 2];
            System.arraycopy(this.pointsX, 0, af, 0, this.numPoints);
            this.pointsX = af;
            af = new float[this.numPoints * 2];
            System.arraycopy(this.pointsY, 0, af, 0, this.numPoints);
            this.pointsY = af;
        }
        this.pointsX[this.numPoints] = x;
        this.pointsY[this.numPoints] = y;
        ++this.numPoints;
    }
    
    @Override
    public int getTriangleCount() {
        return this.numTriangles;
    }
    
    @Override
    public float[] getTrianglePoint(final int tri, final int i) {
        final float xp = this.pointsX[this.triangles[tri].v[i]];
        final float yp = this.pointsY[this.triangles[tri].v[i]];
        return new float[] { xp, yp };
    }
    
    @Override
    public void startHole() {
    }
    
    class Triangle
    {
        int[] v;
        
        Triangle(final int i, final int j, final int k) {
            (this.v = new int[3])[0] = i;
            this.v[1] = j;
            this.v[2] = k;
        }
    }
    
    class Edge
    {
        int v0;
        int v1;
        int t0;
        int t1;
        boolean suspect;
        
        Edge() {
            this.v0 = -1;
            this.v1 = -1;
            this.t0 = -1;
            this.t1 = -1;
        }
    }
    
    class InternalException extends Exception
    {
        public InternalException(final String msg) {
            super(msg);
        }
    }
}
