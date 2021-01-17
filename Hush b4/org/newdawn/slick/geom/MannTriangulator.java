// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MannTriangulator implements Triangulator
{
    private static final double EPSILON = 1.0E-5;
    protected PointBag contour;
    protected PointBag holes;
    private PointBag nextFreePointBag;
    private Point nextFreePoint;
    private List triangles;
    
    public MannTriangulator() {
        this.triangles = new ArrayList();
        this.contour = this.getPointBag();
    }
    
    @Override
    public void addPolyPoint(final float x, final float y) {
        this.addPoint(new Vector2f(x, y));
    }
    
    public void reset() {
        while (this.holes != null) {
            this.holes = this.freePointBag(this.holes);
        }
        this.contour.clear();
        this.holes = null;
    }
    
    @Override
    public void startHole() {
        final PointBag newHole = this.getPointBag();
        newHole.next = this.holes;
        this.holes = newHole;
    }
    
    private void addPoint(final Vector2f pt) {
        if (this.holes == null) {
            final Point p = this.getPoint(pt);
            this.contour.add(p);
        }
        else {
            final Point p = this.getPoint(pt);
            this.holes.add(p);
        }
    }
    
    private Vector2f[] triangulate(Vector2f[] result) {
        this.contour.computeAngles();
        for (PointBag hole = this.holes; hole != null; hole = hole.next) {
            hole.computeAngles();
        }
        while (this.holes != null) {
            Point pHole = this.holes.first;
        Label_0243:
            do {
                if (pHole.angle <= 0.0) {
                    Point pContour = this.contour.first;
                    do {
                        if (pHole.isInfront(pContour) && pContour.isInfront(pHole) && !this.contour.doesIntersectSegment(pHole.pt, pContour.pt)) {
                            PointBag hole2 = this.holes;
                            while (!hole2.doesIntersectSegment(pHole.pt, pContour.pt)) {
                                if ((hole2 = hole2.next) == null) {
                                    final Point newPtContour = this.getPoint(pContour.pt);
                                    pContour.insertAfter(newPtContour);
                                    final Point newPtHole = this.getPoint(pHole.pt);
                                    pHole.insertBefore(newPtHole);
                                    pContour.next = pHole;
                                    pHole.prev = pContour;
                                    newPtHole.next = newPtContour;
                                    newPtContour.prev = newPtHole;
                                    pContour.computeAngle();
                                    pHole.computeAngle();
                                    newPtContour.computeAngle();
                                    newPtHole.computeAngle();
                                    this.holes.first = null;
                                    break Label_0243;
                                }
                            }
                        }
                    } while ((pContour = pContour.next) != this.contour.first);
                }
            } while ((pHole = pHole.next) != this.holes.first);
            this.holes = this.freePointBag(this.holes);
        }
        final int numTriangles = this.contour.countPoints() - 2;
        final int neededSpace = numTriangles * 3 + 1;
        if (result.length < neededSpace) {
            result = (Vector2f[])Array.newInstance(result.getClass().getComponentType(), neededSpace);
        }
        int idx = 0;
        while (true) {
            Point pContour2 = this.contour.first;
            if (pContour2 == null) {
                break;
            }
            if (pContour2.next == pContour2.prev) {
                break;
            }
            do {
                if (pContour2.angle > 0.0) {
                    final Point prev = pContour2.prev;
                    final Point next = pContour2.next;
                    if ((next.next == prev || (prev.isInfront(next) && next.isInfront(prev))) && !this.contour.doesIntersectSegment(prev.pt, next.pt)) {
                        result[idx++] = pContour2.pt;
                        result[idx++] = next.pt;
                        result[idx++] = prev.pt;
                        break;
                    }
                    continue;
                }
            } while ((pContour2 = pContour2.next) != this.contour.first);
            final Point prev = pContour2.prev;
            final Point next = pContour2.next;
            this.contour.first = prev;
            pContour2.unlink();
            this.freePoint(pContour2);
            next.computeAngle();
            prev.computeAngle();
        }
        result[idx] = null;
        this.contour.clear();
        return result;
    }
    
    private PointBag getPointBag() {
        final PointBag pb = this.nextFreePointBag;
        if (pb != null) {
            this.nextFreePointBag = pb.next;
            pb.next = null;
            return pb;
        }
        return new PointBag();
    }
    
    private PointBag freePointBag(final PointBag pb) {
        final PointBag next = pb.next;
        pb.clear();
        pb.next = this.nextFreePointBag;
        this.nextFreePointBag = pb;
        return next;
    }
    
    private Point getPoint(final Vector2f pt) {
        final Point p = this.nextFreePoint;
        if (p != null) {
            this.nextFreePoint = p.next;
            p.next = null;
            p.prev = null;
            p.pt = pt;
            return p;
        }
        return new Point(pt);
    }
    
    private void freePoint(final Point p) {
        p.next = this.nextFreePoint;
        this.nextFreePoint = p;
    }
    
    private void freePoints(final Point head) {
        head.prev.next = this.nextFreePoint;
        head.prev = null;
        this.nextFreePoint = head;
    }
    
    @Override
    public boolean triangulate() {
        final Vector2f[] temp = this.triangulate(new Vector2f[0]);
        for (int i = 0; i < temp.length && temp[i] != null; ++i) {
            this.triangles.add(temp[i]);
        }
        return true;
    }
    
    @Override
    public int getTriangleCount() {
        return this.triangles.size() / 3;
    }
    
    @Override
    public float[] getTrianglePoint(final int tri, final int i) {
        final Vector2f pt = this.triangles.get(tri * 3 + i);
        return new float[] { pt.x, pt.y };
    }
    
    private static class Point implements Serializable
    {
        protected Vector2f pt;
        protected Point prev;
        protected Point next;
        protected double nx;
        protected double ny;
        protected double angle;
        protected double dist;
        
        public Point(final Vector2f pt) {
            this.pt = pt;
        }
        
        public void unlink() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            this.next = null;
            this.prev = null;
        }
        
        public void insertBefore(final Point p) {
            this.prev.next = p;
            p.prev = this.prev;
            p.next = this;
            this.prev = p;
        }
        
        public void insertAfter(final Point p) {
            this.next.prev = p;
            p.prev = this;
            p.next = this.next;
            this.next = p;
        }
        
        private double hypot(final double x, final double y) {
            return Math.sqrt(x * x + y * y);
        }
        
        public void computeAngle() {
            if (this.prev.pt.equals(this.pt)) {
                final Vector2f pt = this.pt;
                pt.x += 0.01f;
            }
            double dx1 = this.pt.x - this.prev.pt.x;
            double dy1 = this.pt.y - this.prev.pt.y;
            final double len1 = this.hypot(dx1, dy1);
            dx1 /= len1;
            dy1 /= len1;
            if (this.next.pt.equals(this.pt)) {
                final Vector2f pt2 = this.pt;
                pt2.y += 0.01f;
            }
            double dx2 = this.next.pt.x - this.pt.x;
            double dy2 = this.next.pt.y - this.pt.y;
            final double len2 = this.hypot(dx2, dy2);
            dx2 /= len2;
            dy2 /= len2;
            final double nx1 = -dy1;
            final double ny1 = dx1;
            this.nx = (nx1 - dy2) * 0.5;
            this.ny = (ny1 + dx2) * 0.5;
            if (this.nx * this.nx + this.ny * this.ny < 1.0E-5) {
                this.nx = dx1;
                this.ny = dy2;
                this.angle = 1.0;
                if (dx1 * dx2 + dy1 * dy2 > 0.0) {
                    this.nx = -dx1;
                    this.ny = -dy1;
                }
            }
            else {
                this.angle = this.nx * dx2 + this.ny * dy2;
            }
        }
        
        public double getAngle(final Point p) {
            final double dx = p.pt.x - this.pt.x;
            final double dy = p.pt.y - this.pt.y;
            final double dlen = this.hypot(dx, dy);
            return (this.nx * dx + this.ny * dy) / dlen;
        }
        
        public boolean isConcave() {
            return this.angle < 0.0;
        }
        
        public boolean isInfront(final double dx, final double dy) {
            final boolean sidePrev = (this.prev.pt.y - this.pt.y) * dx + (this.pt.x - this.prev.pt.x) * dy >= 0.0;
            final boolean sideNext = (this.pt.y - this.next.pt.y) * dx + (this.next.pt.x - this.pt.x) * dy >= 0.0;
            return (this.angle < 0.0) ? (sidePrev | sideNext) : (sidePrev & sideNext);
        }
        
        public boolean isInfront(final Point p) {
            return this.isInfront(p.pt.x - this.pt.x, p.pt.y - this.pt.y);
        }
    }
    
    protected class PointBag implements Serializable
    {
        protected Point first;
        protected PointBag next;
        
        public void clear() {
            if (this.first != null) {
                MannTriangulator.this.freePoints(this.first);
                this.first = null;
            }
        }
        
        public void add(final Point p) {
            if (this.first != null) {
                this.first.insertBefore(p);
            }
            else {
                this.first = p;
                p.next = p;
                p.prev = p;
            }
        }
        
        public void computeAngles() {
            if (this.first == null) {
                return;
            }
            Point p = this.first;
            do {
                p.computeAngle();
            } while ((p = p.next) != this.first);
        }
        
        public boolean doesIntersectSegment(final Vector2f v1, final Vector2f v2) {
            final double dxA = v2.x - v1.x;
            final double dyA = v2.y - v1.y;
            Point p = this.first;
            while (true) {
                final Point n = p.next;
                if (p.pt != v1 && n.pt != v1 && p.pt != v2 && n.pt != v2) {
                    final double dxB = n.pt.x - p.pt.x;
                    final double dyB = n.pt.y - p.pt.y;
                    final double d = dxA * dyB - dyA * dxB;
                    if (Math.abs(d) > 1.0E-5) {
                        final double tmp1 = p.pt.x - v1.x;
                        final double tmp2 = p.pt.y - v1.y;
                        final double tA = (dyB * tmp1 - dxB * tmp2) / d;
                        final double tB = (dyA * tmp1 - dxA * tmp2) / d;
                        if (tA >= 0.0 && tA <= 1.0 && tB >= 0.0 && tB <= 1.0) {
                            return true;
                        }
                    }
                }
                if (n == this.first) {
                    return false;
                }
                p = n;
            }
        }
        
        public int countPoints() {
            if (this.first == null) {
                return 0;
            }
            int count = 0;
            Point p = this.first;
            do {
                ++count;
            } while ((p = p.next) != this.first);
            return count;
        }
        
        public boolean contains(final Vector2f point) {
            return this.first != null && (this.first.prev.pt.equals(point) || this.first.pt.equals(point));
        }
    }
}
