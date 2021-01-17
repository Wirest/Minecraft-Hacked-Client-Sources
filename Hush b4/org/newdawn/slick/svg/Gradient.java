// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.Color;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.Image;
import java.util.ArrayList;

public class Gradient
{
    private String name;
    private ArrayList steps;
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private float r;
    private Image image;
    private boolean radial;
    private Transform transform;
    private String ref;
    
    public Gradient(final String name, final boolean radial) {
        this.steps = new ArrayList();
        this.name = name;
        this.radial = radial;
    }
    
    public boolean isRadial() {
        return this.radial;
    }
    
    public void setTransform(final Transform trans) {
        this.transform = trans;
    }
    
    public Transform getTransform() {
        return this.transform;
    }
    
    public void reference(final String ref) {
        this.ref = ref;
    }
    
    public void resolve(final Diagram diagram) {
        if (this.ref == null) {
            return;
        }
        final Gradient other = diagram.getGradient(this.ref);
        for (int i = 0; i < other.steps.size(); ++i) {
            this.steps.add(other.steps.get(i));
        }
    }
    
    public void genImage() {
        if (this.image == null) {
            final ImageBuffer buffer = new ImageBuffer(128, 16);
            for (int i = 0; i < 128; ++i) {
                final Color col = this.getColorAt(i / 128.0f);
                for (int j = 0; j < 16; ++j) {
                    buffer.setRGBA(i, j, col.getRedByte(), col.getGreenByte(), col.getBlueByte(), col.getAlphaByte());
                }
            }
            this.image = buffer.getImage();
        }
    }
    
    public Image getImage() {
        this.genImage();
        return this.image;
    }
    
    public void setR(final float r) {
        this.r = r;
    }
    
    public void setX1(final float x1) {
        this.x1 = x1;
    }
    
    public void setX2(final float x2) {
        this.x2 = x2;
    }
    
    public void setY1(final float y1) {
        this.y1 = y1;
    }
    
    public void setY2(final float y2) {
        this.y2 = y2;
    }
    
    public float getR() {
        return this.r;
    }
    
    public float getX1() {
        return this.x1;
    }
    
    public float getX2() {
        return this.x2;
    }
    
    public float getY1() {
        return this.y1;
    }
    
    public float getY2() {
        return this.y2;
    }
    
    public void addStep(final float location, final Color c) {
        this.steps.add(new Step(location, c));
    }
    
    public Color getColorAt(float p) {
        if (p <= 0.0f) {
            return this.steps.get(0).col;
        }
        if (p > 1.0f) {
            return this.steps.get(this.steps.size() - 1).col;
        }
        for (int i = 1; i < this.steps.size(); ++i) {
            final Step prev = this.steps.get(i - 1);
            final Step current = this.steps.get(i);
            if (p <= current.location) {
                final float dis = current.location - prev.location;
                p -= prev.location;
                final float v = p / dis;
                final Color c = new Color(1, 1, 1, 1);
                c.a = prev.col.a * (1.0f - v) + current.col.a * v;
                c.r = prev.col.r * (1.0f - v) + current.col.r * v;
                c.g = prev.col.g * (1.0f - v) + current.col.g * v;
                c.b = prev.col.b * (1.0f - v) + current.col.b * v;
                return c;
            }
        }
        return Color.black;
    }
    
    private class Step
    {
        float location;
        Color col;
        
        public Step(final float location, final Color c) {
            this.location = location;
            this.col = c;
        }
    }
}
