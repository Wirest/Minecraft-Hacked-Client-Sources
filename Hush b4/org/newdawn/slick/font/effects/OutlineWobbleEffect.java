// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.awt.geom.PathIterator;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.AffineTransform;
import java.awt.BasicStroke;
import java.awt.geom.GeneralPath;
import java.awt.Shape;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;
import java.awt.Stroke;

public class OutlineWobbleEffect extends OutlineEffect
{
    private float detail;
    private float amplitude;
    
    public OutlineWobbleEffect() {
        this.detail = 1.0f;
        this.amplitude = 1.0f;
        this.setStroke(new WobbleStroke((WobbleStroke)null));
    }
    
    public float getDetail() {
        return this.detail;
    }
    
    public void setDetail(final float detail) {
        this.detail = detail;
    }
    
    public float getAmplitude() {
        return this.amplitude;
    }
    
    public void setAmplitude(final float amplitude) {
        this.amplitude = amplitude;
    }
    
    public OutlineWobbleEffect(final int width, final Color color) {
        super(width, color);
        this.detail = 1.0f;
        this.amplitude = 1.0f;
    }
    
    @Override
    public String toString() {
        return "Outline (Wobble)";
    }
    
    @Override
    public List getValues() {
        final List values = super.getValues();
        values.remove(2);
        values.add(EffectUtil.floatValue("Detail", this.detail, 1.0f, 50.0f, "This setting controls how detailed the outline will be. Smaller numbers cause the outline to have more detail."));
        values.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5f, 50.0f, "This setting controls the amplitude of the outline."));
        return values;
    }
    
    @Override
    public void setValues(final List values) {
        super.setValues(values);
        for (final ConfigurableEffect.Value value : values) {
            if (value.getName().equals("Detail")) {
                this.detail = (float)value.getObject();
            }
            else {
                if (!value.getName().equals("Amplitude")) {
                    continue;
                }
                this.amplitude = (float)value.getObject();
            }
        }
    }
    
    private class WobbleStroke implements Stroke
    {
        private static final float FLATNESS = 1.0f;
        
        @Override
        public Shape createStrokedShape(Shape shape) {
            final GeneralPath result = new GeneralPath();
            shape = new BasicStroke(OutlineWobbleEffect.this.getWidth(), 2, OutlineWobbleEffect.this.getJoin()).createStrokedShape(shape);
            final PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0);
            final float[] points = new float[6];
            float moveX = 0.0f;
            float moveY = 0.0f;
            float lastX = 0.0f;
            float lastY = 0.0f;
            float thisX = 0.0f;
            float thisY = 0.0f;
            int type = 0;
            float next = 0.0f;
            while (!it.isDone()) {
                type = it.currentSegment(points);
                switch (type) {
                    case 0: {
                        lastX = (moveX = this.randomize(points[0]));
                        lastY = (moveY = this.randomize(points[1]));
                        result.moveTo(moveX, moveY);
                        next = 0.0f;
                        break;
                    }
                    case 4: {
                        points[0] = moveX;
                        points[1] = moveY;
                    }
                    case 1: {
                        thisX = this.randomize(points[0]);
                        thisY = this.randomize(points[1]);
                        final float dx = thisX - lastX;
                        final float dy = thisY - lastY;
                        final float distance = (float)Math.sqrt(dx * dx + dy * dy);
                        if (distance >= next) {
                            final float r = 1.0f / distance;
                            while (distance >= next) {
                                final float x = lastX + next * dx * r;
                                final float y = lastY + next * dy * r;
                                result.lineTo(this.randomize(x), this.randomize(y));
                                next += OutlineWobbleEffect.this.detail;
                            }
                        }
                        next -= distance;
                        lastX = thisX;
                        lastY = thisY;
                        break;
                    }
                }
                it.next();
            }
            return result;
        }
        
        private float randomize(final float x) {
            return x + (float)Math.random() * OutlineWobbleEffect.this.amplitude * 2.0f - 1.0f;
        }
    }
}
