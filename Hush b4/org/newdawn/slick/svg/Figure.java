// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Shape;

public class Figure
{
    public static final int ELLIPSE = 1;
    public static final int LINE = 2;
    public static final int RECTANGLE = 3;
    public static final int PATH = 4;
    public static final int POLYGON = 5;
    private int type;
    private Shape shape;
    private NonGeometricData data;
    private Transform transform;
    
    public Figure(final int type, final Shape shape, final NonGeometricData data, final Transform transform) {
        this.shape = shape;
        this.data = data;
        this.type = type;
        this.transform = transform;
    }
    
    public Transform getTransform() {
        return this.transform;
    }
    
    public int getType() {
        return this.type;
    }
    
    public Shape getShape() {
        return this.shape;
    }
    
    public NonGeometricData getData() {
        return this.data;
    }
}
