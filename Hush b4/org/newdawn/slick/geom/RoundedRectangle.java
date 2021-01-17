// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import org.newdawn.slick.util.FastTrig;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

public class RoundedRectangle extends Rectangle
{
    public static final int TOP_LEFT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int BOTTOM_RIGHT = 4;
    public static final int BOTTOM_LEFT = 8;
    public static final int ALL = 15;
    private static final int DEFAULT_SEGMENT_COUNT = 25;
    private float cornerRadius;
    private int segmentCount;
    private int cornerFlags;
    
    public RoundedRectangle(final float x, final float y, final float width, final float height, final float cornerRadius) {
        this(x, y, width, height, cornerRadius, 25);
    }
    
    public RoundedRectangle(final float x, final float y, final float width, final float height, final float cornerRadius, final int segmentCount) {
        this(x, y, width, height, cornerRadius, segmentCount, 15);
    }
    
    public RoundedRectangle(final float x, final float y, final float width, final float height, final float cornerRadius, final int segmentCount, final int cornerFlags) {
        super(x, y, width, height);
        if (cornerRadius < 0.0f) {
            throw new IllegalArgumentException("corner radius must be >= 0");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = cornerRadius;
        this.segmentCount = segmentCount;
        this.pointsDirty = true;
        this.cornerFlags = cornerFlags;
    }
    
    public float getCornerRadius() {
        return this.cornerRadius;
    }
    
    public void setCornerRadius(final float cornerRadius) {
        if (cornerRadius >= 0.0f && cornerRadius != this.cornerRadius) {
            this.cornerRadius = cornerRadius;
            this.pointsDirty = true;
        }
    }
    
    @Override
    public float getHeight() {
        return this.height;
    }
    
    @Override
    public void setHeight(final float height) {
        if (this.height != height) {
            this.height = height;
            this.pointsDirty = true;
        }
    }
    
    @Override
    public float getWidth() {
        return this.width;
    }
    
    @Override
    public void setWidth(final float width) {
        if (width != this.width) {
            this.width = width;
            this.pointsDirty = true;
        }
    }
    
    @Override
    protected void createPoints() {
        this.maxX = this.x + this.width;
        this.maxY = this.y + this.height;
        this.minX = this.x;
        this.minY = this.y;
        final float useWidth = this.width - 1.0f;
        final float useHeight = this.height - 1.0f;
        if (this.cornerRadius == 0.0f) {
            (this.points = new float[8])[0] = this.x;
            this.points[1] = this.y;
            this.points[2] = this.x + useWidth;
            this.points[3] = this.y;
            this.points[4] = this.x + useWidth;
            this.points[5] = this.y + useHeight;
            this.points[6] = this.x;
            this.points[7] = this.y + useHeight;
        }
        else {
            float doubleRadius = this.cornerRadius * 2.0f;
            if (doubleRadius > useWidth) {
                doubleRadius = useWidth;
                this.cornerRadius = doubleRadius / 2.0f;
            }
            if (doubleRadius > useHeight) {
                doubleRadius = useHeight;
                this.cornerRadius = doubleRadius / 2.0f;
            }
            final ArrayList tempPoints = new ArrayList();
            if ((this.cornerFlags & 0x1) != 0x0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + this.cornerRadius, 180.0f, 270.0f));
            }
            else {
                tempPoints.add(new Float(this.x));
                tempPoints.add(new Float(this.y));
            }
            if ((this.cornerFlags & 0x2) != 0x0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + useWidth - this.cornerRadius, this.y + this.cornerRadius, 270.0f, 360.0f));
            }
            else {
                tempPoints.add(new Float(this.x + useWidth));
                tempPoints.add(new Float(this.y));
            }
            if ((this.cornerFlags & 0x4) != 0x0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + useWidth - this.cornerRadius, this.y + useHeight - this.cornerRadius, 0.0f, 90.0f));
            }
            else {
                tempPoints.add(new Float(this.x + useWidth));
                tempPoints.add(new Float(this.y + useHeight));
            }
            if ((this.cornerFlags & 0x8) != 0x0) {
                tempPoints.addAll(this.createPoints(this.segmentCount, this.cornerRadius, this.x + this.cornerRadius, this.y + useHeight - this.cornerRadius, 90.0f, 180.0f));
            }
            else {
                tempPoints.add(new Float(this.x));
                tempPoints.add(new Float(this.y + useHeight));
            }
            this.points = new float[tempPoints.size()];
            for (int i = 0; i < tempPoints.size(); ++i) {
                this.points[i] = tempPoints.get(i);
            }
        }
        this.findCenter();
        this.calculateRadius();
    }
    
    private List createPoints(final int numberOfSegments, final float radius, final float cx, final float cy, final float start, final float end) {
        final ArrayList tempPoints = new ArrayList();
        final int step = 360 / numberOfSegments;
        for (float a = start; a <= end + step; a += step) {
            float ang = a;
            if (ang > end) {
                ang = end;
            }
            final float x = (float)(cx + FastTrig.cos(Math.toRadians(ang)) * radius);
            final float y = (float)(cy + FastTrig.sin(Math.toRadians(ang)) * radius);
            tempPoints.add(new Float(x));
            tempPoints.add(new Float(y));
        }
        return tempPoints;
    }
    
    @Override
    public Shape transform(final Transform transform) {
        this.checkPoints();
        final Polygon resultPolygon = new Polygon();
        final float[] result = new float[this.points.length];
        transform.transform(this.points, 0, result, 0, this.points.length / 2);
        resultPolygon.points = result;
        resultPolygon.findCenter();
        return resultPolygon;
    }
}
