// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font.effects;

import java.awt.geom.PathIterator;
import java.awt.BasicStroke;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.Shape;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;
import java.awt.Stroke;

public class OutlineZigzagEffect extends OutlineEffect
{
    private float amplitude;
    private float wavelength;
    
    public OutlineZigzagEffect() {
        this.amplitude = 1.0f;
        this.wavelength = 3.0f;
        this.setStroke(new ZigzagStroke((ZigzagStroke)null));
    }
    
    public float getWavelength() {
        return this.wavelength;
    }
    
    public void setWavelength(final float wavelength) {
        this.wavelength = wavelength;
    }
    
    public float getAmplitude() {
        return this.amplitude;
    }
    
    public void setAmplitude(final float amplitude) {
        this.amplitude = amplitude;
    }
    
    public OutlineZigzagEffect(final int width, final Color color) {
        super(width, color);
        this.amplitude = 1.0f;
        this.wavelength = 3.0f;
    }
    
    @Override
    public String toString() {
        return "Outline (Zigzag)";
    }
    
    @Override
    public List getValues() {
        final List values = super.getValues();
        values.add(EffectUtil.floatValue("Wavelength", this.wavelength, 1.0f, 100.0f, "This setting controls the wavelength of the outline. The smaller the value, the more segments will be used to draw the outline."));
        values.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5f, 50.0f, "This setting controls the amplitude of the outline. The bigger the value, the more the zigzags will vary."));
        return values;
    }
    
    @Override
    public void setValues(final List values) {
        super.setValues(values);
        for (final ConfigurableEffect.Value value : values) {
            if (value.getName().equals("Wavelength")) {
                this.wavelength = (float)value.getObject();
            }
            else {
                if (!value.getName().equals("Amplitude")) {
                    continue;
                }
                this.amplitude = (float)value.getObject();
            }
        }
    }
    
    private class ZigzagStroke implements Stroke
    {
        private static final float FLATNESS = 1.0f;
        
        @Override
        public Shape createStrokedShape(final Shape shape) {
            final GeneralPath result = new GeneralPath();
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
            int phase = 0;
            while (!it.isDone()) {
                type = it.currentSegment(points);
                switch (type) {
                    case 0: {
                        lastX = (moveX = points[0]);
                        lastY = (moveY = points[1]);
                        result.moveTo(moveX, moveY);
                        next = OutlineZigzagEffect.this.wavelength / 2.0f;
                        break;
                    }
                    case 4: {
                        points[0] = moveX;
                        points[1] = moveY;
                    }
                    case 1: {
                        thisX = points[0];
                        thisY = points[1];
                        final float dx = thisX - lastX;
                        final float dy = thisY - lastY;
                        final float distance = (float)Math.sqrt(dx * dx + dy * dy);
                        if (distance >= next) {
                            final float r = 1.0f / distance;
                            while (distance >= next) {
                                final float x = lastX + next * dx * r;
                                final float y = lastY + next * dy * r;
                                if ((phase & 0x1) == 0x0) {
                                    result.lineTo(x + OutlineZigzagEffect.this.amplitude * dy * r, y - OutlineZigzagEffect.this.amplitude * dx * r);
                                }
                                else {
                                    result.lineTo(x - OutlineZigzagEffect.this.amplitude * dy * r, y + OutlineZigzagEffect.this.amplitude * dx * r);
                                }
                                next += OutlineZigzagEffect.this.wavelength;
                                ++phase;
                            }
                        }
                        next -= distance;
                        lastX = thisX;
                        lastY = thisY;
                        if (type == 4) {
                            result.closePath();
                            break;
                        }
                        break;
                    }
                }
                it.next();
            }
            return new BasicStroke(OutlineZigzagEffect.this.getWidth(), 2, OutlineZigzagEffect.this.getJoin()).createStrokedShape(result);
        }
    }
}
