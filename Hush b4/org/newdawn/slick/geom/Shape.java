// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.io.Serializable;

public abstract class Shape implements Serializable
{
    protected float[] points;
    protected float[] center;
    protected float x;
    protected float y;
    protected float maxX;
    protected float maxY;
    protected float minX;
    protected float minY;
    protected float boundingCircleRadius;
    protected boolean pointsDirty;
    protected transient Triangulator tris;
    protected boolean trianglesDirty;
    
    public Shape() {
        this.pointsDirty = true;
    }
    
    public void setLocation(final float x, final float y) {
        this.setX(x);
        this.setY(y);
    }
    
    public abstract Shape transform(final Transform p0);
    
    protected abstract void createPoints();
    
    public float getX() {
        return this.x;
    }
    
    public void setX(float x) {
        if (x != this.x) {
            final float dx = x - this.x;
            this.x = x;
            if (this.points == null || this.center == null) {
                this.checkPoints();
            }
            for (int i = 0; i < this.points.length / 2; ++i) {
                final float[] points = this.points;
                final int n = i * 2;
                points[n] += dx;
            }
            final float[] center = this.center;
            final int n2 = 0;
            center[n2] += dx;
            x += dx;
            this.maxX += dx;
            this.minX += dx;
            this.trianglesDirty = true;
        }
    }
    
    public void setY(float y) {
        if (y != this.y) {
            final float dy = y - this.y;
            this.y = y;
            if (this.points == null || this.center == null) {
                this.checkPoints();
            }
            for (int i = 0; i < this.points.length / 2; ++i) {
                final float[] points = this.points;
                final int n = i * 2 + 1;
                points[n] += dy;
            }
            final float[] center = this.center;
            final int n2 = 1;
            center[n2] += dy;
            y += dy;
            this.maxY += dy;
            this.minY += dy;
            this.trianglesDirty = true;
        }
    }
    
    public float getY() {
        return this.y;
    }
    
    public Vector2f getLocation() {
        return new Vector2f(this.getX(), this.getY());
    }
    
    public void setLocation(final Vector2f loc) {
        this.setX(loc.x);
        this.setY(loc.y);
    }
    
    public float getCenterX() {
        this.checkPoints();
        return this.center[0];
    }
    
    public void setCenterX(final float centerX) {
        if (this.points == null || this.center == null) {
            this.checkPoints();
        }
        final float xDiff = centerX - this.getCenterX();
        this.setX(this.x + xDiff);
    }
    
    public float getCenterY() {
        this.checkPoints();
        return this.center[1];
    }
    
    public void setCenterY(final float centerY) {
        if (this.points == null || this.center == null) {
            this.checkPoints();
        }
        final float yDiff = centerY - this.getCenterY();
        this.setY(this.y + yDiff);
    }
    
    public float getMaxX() {
        this.checkPoints();
        return this.maxX;
    }
    
    public float getMaxY() {
        this.checkPoints();
        return this.maxY;
    }
    
    public float getMinX() {
        this.checkPoints();
        return this.minX;
    }
    
    public float getMinY() {
        this.checkPoints();
        return this.minY;
    }
    
    public float getBoundingCircleRadius() {
        this.checkPoints();
        return this.boundingCircleRadius;
    }
    
    public float[] getCenter() {
        this.checkPoints();
        return this.center;
    }
    
    public float[] getPoints() {
        this.checkPoints();
        return this.points;
    }
    
    public int getPointCount() {
        this.checkPoints();
        return this.points.length / 2;
    }
    
    public float[] getPoint(final int index) {
        this.checkPoints();
        final float[] result = { this.points[index * 2], this.points[index * 2 + 1] };
        return result;
    }
    
    public float[] getNormal(final int index) {
        final float[] current = this.getPoint(index);
        final float[] prev = this.getPoint((index - 1 < 0) ? (this.getPointCount() - 1) : (index - 1));
        final float[] next = this.getPoint((index + 1 >= this.getPointCount()) ? 0 : (index + 1));
        final float[] t1 = this.getNormal(prev, current);
        final float[] t2 = this.getNormal(current, next);
        if (index == 0 && !this.closed()) {
            return t2;
        }
        if (index == this.getPointCount() - 1 && !this.closed()) {
            return t1;
        }
        final float tx = (t1[0] + t2[0]) / 2.0f;
        final float ty = (t1[1] + t2[1]) / 2.0f;
        final float len = (float)Math.sqrt(tx * tx + ty * ty);
        return new float[] { tx / len, ty / len };
    }
    
    public boolean contains(final Shape other) {
        if (other.intersects(this)) {
            return false;
        }
        for (int i = 0; i < other.getPointCount(); ++i) {
            final float[] pt = other.getPoint(i);
            if (!this.contains(pt[0], pt[1])) {
                return false;
            }
        }
        return true;
    }
    
    private float[] getNormal(final float[] start, final float[] end) {
        float dx = start[0] - end[0];
        float dy = start[1] - end[1];
        final float len = (float)Math.sqrt(dx * dx + dy * dy);
        dx /= len;
        dy /= len;
        return new float[] { -dy, dx };
    }
    
    public boolean includes(final float x, final float y) {
        if (this.points.length == 0) {
            return false;
        }
        this.checkPoints();
        final Line testLine = new Line(0.0f, 0.0f, 0.0f, 0.0f);
        final Vector2f pt = new Vector2f(x, y);
        for (int i = 0; i < this.points.length; i += 2) {
            int n = i + 2;
            if (n >= this.points.length) {
                n = 0;
            }
            testLine.set(this.points[i], this.points[i + 1], this.points[n], this.points[n + 1]);
            if (testLine.on(pt)) {
                return true;
            }
        }
        return false;
    }
    
    public int indexOf(final float x, final float y) {
        for (int i = 0; i < this.points.length; i += 2) {
            if (this.points[i] == x && this.points[i + 1] == y) {
                return i / 2;
            }
        }
        return -1;
    }
    
    public boolean contains(final float x, final float y) {
        this.checkPoints();
        if (this.points.length == 0) {
            return false;
        }
        boolean result = false;
        final int npoints = this.points.length;
        float xold = this.points[npoints - 2];
        float yold = this.points[npoints - 1];
        for (int i = 0; i < npoints; i += 2) {
            final float xnew = this.points[i];
            final float ynew = this.points[i + 1];
            float x2;
            float x3;
            float y2;
            float y3;
            if (xnew > xold) {
                x2 = xold;
                x3 = xnew;
                y2 = yold;
                y3 = ynew;
            }
            else {
                x2 = xnew;
                x3 = xold;
                y2 = ynew;
                y3 = yold;
            }
            if (xnew < x == x <= xold && (y - (double)y2) * (x3 - x2) < (y3 - (double)y2) * (x - x2)) {
                result = !result;
            }
            xold = xnew;
            yold = ynew;
        }
        return result;
    }
    
    public boolean intersects(final Shape shape) {
        this.checkPoints();
        boolean result = false;
        final float[] points = this.getPoints();
        final float[] thatPoints = shape.getPoints();
        int length = points.length;
        int thatLength = thatPoints.length;
        if (!this.closed()) {
            length -= 2;
        }
        if (!shape.closed()) {
            thatLength -= 2;
        }
        for (int i = 0; i < length; i += 2) {
            int iNext = i + 2;
            if (iNext >= points.length) {
                iNext = 0;
            }
            for (int j = 0; j < thatLength; j += 2) {
                int jNext = j + 2;
                if (jNext >= thatPoints.length) {
                    jNext = 0;
                }
                final double unknownA = ((points[iNext] - points[i]) * (double)(thatPoints[j + 1] - points[i + 1]) - (points[iNext + 1] - points[i + 1]) * (thatPoints[j] - points[i])) / ((points[iNext + 1] - points[i + 1]) * (thatPoints[jNext] - thatPoints[j]) - (points[iNext] - points[i]) * (thatPoints[jNext + 1] - thatPoints[j + 1]));
                final double unknownB = ((thatPoints[jNext] - thatPoints[j]) * (double)(thatPoints[j + 1] - points[i + 1]) - (thatPoints[jNext + 1] - thatPoints[j + 1]) * (thatPoints[j] - points[i])) / ((points[iNext + 1] - points[i + 1]) * (thatPoints[jNext] - thatPoints[j]) - (points[iNext] - points[i]) * (thatPoints[jNext + 1] - thatPoints[j + 1]));
                if (unknownA >= 0.0 && unknownA <= 1.0 && unknownB >= 0.0 && unknownB <= 1.0) {
                    result = true;
                    break;
                }
            }
            if (result) {
                break;
            }
        }
        return result;
    }
    
    public boolean hasVertex(final float x, final float y) {
        if (this.points.length == 0) {
            return false;
        }
        this.checkPoints();
        for (int i = 0; i < this.points.length; i += 2) {
            if (this.points[i] == x && this.points[i + 1] == y) {
                return true;
            }
        }
        return false;
    }
    
    protected void findCenter() {
        this.center = new float[] { 0.0f, 0.0f };
        final int length = this.points.length;
        for (int i = 0; i < length; i += 2) {
            final float[] center = this.center;
            final int n = 0;
            center[n] += this.points[i];
            final float[] center2 = this.center;
            final int n2 = 1;
            center2[n2] += this.points[i + 1];
        }
        final float[] center3 = this.center;
        final int n3 = 0;
        center3[n3] /= length / 2;
        final float[] center4 = this.center;
        final int n4 = 1;
        center4[n4] /= length / 2;
    }
    
    protected void calculateRadius() {
        this.boundingCircleRadius = 0.0f;
        for (int i = 0; i < this.points.length; i += 2) {
            final float temp = (this.points[i] - this.center[0]) * (this.points[i] - this.center[0]) + (this.points[i + 1] - this.center[1]) * (this.points[i + 1] - this.center[1]);
            this.boundingCircleRadius = ((this.boundingCircleRadius > temp) ? this.boundingCircleRadius : temp);
        }
        this.boundingCircleRadius = (float)Math.sqrt(this.boundingCircleRadius);
    }
    
    protected void calculateTriangles() {
        if (!this.trianglesDirty && this.tris != null) {
            return;
        }
        if (this.points.length >= 6) {
            boolean clockwise = true;
            float area = 0.0f;
            for (int i = 0; i < this.points.length / 2 - 1; ++i) {
                final float x1 = this.points[i * 2];
                final float y1 = this.points[i * 2 + 1];
                final float x2 = this.points[i * 2 + 2];
                final float y2 = this.points[i * 2 + 3];
                area += x1 * y2 - y1 * x2;
            }
            area /= 2.0f;
            clockwise = (area > 0.0f);
            this.tris = new NeatTriangulator();
            for (int i = 0; i < this.points.length; i += 2) {
                this.tris.addPolyPoint(this.points[i], this.points[i + 1]);
            }
            this.tris.triangulate();
        }
        this.trianglesDirty = false;
    }
    
    public void increaseTriangulation() {
        this.checkPoints();
        this.calculateTriangles();
        this.tris = new OverTriangulator(this.tris);
    }
    
    public Triangulator getTriangles() {
        this.checkPoints();
        this.calculateTriangles();
        return this.tris;
    }
    
    protected final void checkPoints() {
        if (this.pointsDirty) {
            this.createPoints();
            this.findCenter();
            this.calculateRadius();
            if (this.points.length > 0) {
                this.maxX = this.points[0];
                this.maxY = this.points[1];
                this.minX = this.points[0];
                this.minY = this.points[1];
                for (int i = 0; i < this.points.length / 2; ++i) {
                    this.maxX = Math.max(this.points[i * 2], this.maxX);
                    this.maxY = Math.max(this.points[i * 2 + 1], this.maxY);
                    this.minX = Math.min(this.points[i * 2], this.minX);
                    this.minY = Math.min(this.points[i * 2 + 1], this.minY);
                }
            }
            this.pointsDirty = false;
            this.trianglesDirty = true;
        }
    }
    
    public void preCache() {
        this.checkPoints();
        this.getTriangles();
    }
    
    public boolean closed() {
        return true;
    }
    
    public Shape prune() {
        final Polygon result = new Polygon();
        for (int i = 0; i < this.getPointCount(); ++i) {
            final int next = (i + 1 >= this.getPointCount()) ? 0 : (i + 1);
            final int prev = (i - 1 < 0) ? (this.getPointCount() - 1) : (i - 1);
            float dx1 = this.getPoint(i)[0] - this.getPoint(prev)[0];
            float dy1 = this.getPoint(i)[1] - this.getPoint(prev)[1];
            float dx2 = this.getPoint(next)[0] - this.getPoint(i)[0];
            float dy2 = this.getPoint(next)[1] - this.getPoint(i)[1];
            final float len1 = (float)Math.sqrt(dx1 * dx1 + dy1 * dy1);
            final float len2 = (float)Math.sqrt(dx2 * dx2 + dy2 * dy2);
            dx1 /= len1;
            dy1 /= len1;
            dx2 /= len2;
            dy2 /= len2;
            if (dx1 != dx2 || dy1 != dy2) {
                result.addPoint(this.getPoint(i)[0], this.getPoint(i)[1]);
            }
        }
        return result;
    }
    
    public Shape[] subtract(final Shape other) {
        return new GeomUtil().subtract(this, other);
    }
    
    public Shape[] union(final Shape other) {
        return new GeomUtil().union(this, other);
    }
    
    public float getWidth() {
        return this.maxX - this.minX;
    }
    
    public float getHeight() {
        return this.maxY - this.minY;
    }
}
