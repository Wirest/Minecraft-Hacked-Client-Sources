// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.svg;

import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.TexCoordGenerator;

public class LinearGradientFill implements TexCoordGenerator
{
    private Vector2f start;
    private Vector2f end;
    private Gradient gradient;
    private Line line;
    private Shape shape;
    
    public LinearGradientFill(final Shape shape, final Transform trans, final Gradient gradient) {
        this.gradient = gradient;
        final float x = gradient.getX1();
        final float y = gradient.getY1();
        final float mx = gradient.getX2();
        final float my = gradient.getY2();
        final float h = my - y;
        final float w = mx - x;
        final float[] s = { x, y + h / 2.0f };
        gradient.getTransform().transform(s, 0, s, 0, 1);
        trans.transform(s, 0, s, 0, 1);
        final float[] e = { x + w, y + h / 2.0f };
        gradient.getTransform().transform(e, 0, e, 0, 1);
        trans.transform(e, 0, e, 0, 1);
        this.start = new Vector2f(s[0], s[1]);
        this.end = new Vector2f(e[0], e[1]);
        this.line = new Line(this.start, this.end);
    }
    
    @Override
    public Vector2f getCoordFor(final float x, final float y) {
        final Vector2f result = new Vector2f();
        this.line.getClosestPoint(new Vector2f(x, y), result);
        float u = result.distance(this.start);
        u /= this.line.length();
        return new Vector2f(u, 0.0f);
    }
}
