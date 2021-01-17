// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.TexCoordGenerator;

public class RadialGradientFill implements TexCoordGenerator
{
    private Vector2f centre;
    private float radius;
    private Gradient gradient;
    private Shape shape;
    
    public RadialGradientFill(final Shape shape, final Transform trans, final Gradient gradient) {
        this.gradient = gradient;
        this.radius = gradient.getR();
        final float x = gradient.getX1();
        final float y = gradient.getY1();
        final float[] c = { x, y };
        gradient.getTransform().transform(c, 0, c, 0, 1);
        trans.transform(c, 0, c, 0, 1);
        final float[] rt = { x, y - this.radius };
        gradient.getTransform().transform(rt, 0, rt, 0, 1);
        trans.transform(rt, 0, rt, 0, 1);
        this.centre = new Vector2f(c[0], c[1]);
        final Vector2f dis = new Vector2f(rt[0], rt[1]);
        this.radius = dis.distance(this.centre);
    }
    
    @Override
    public Vector2f getCoordFor(final float x, final float y) {
        float u = this.centre.distance(new Vector2f(x, y));
        u /= this.radius;
        if (u > 0.99f) {
            u = 0.99f;
        }
        return new Vector2f(u, 0.0f);
    }
}
