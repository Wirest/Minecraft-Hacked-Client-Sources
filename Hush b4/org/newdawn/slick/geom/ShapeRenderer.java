// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.LineStripRenderer;
import org.newdawn.slick.opengl.renderer.SGL;

public final class ShapeRenderer
{
    private static SGL GL;
    private static LineStripRenderer LSR;
    
    static {
        ShapeRenderer.GL = Renderer.get();
        ShapeRenderer.LSR = Renderer.getLineStripRenderer();
    }
    
    public static final void draw(final Shape shape) {
        final Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        final float[] points = shape.getPoints();
        ShapeRenderer.LSR.start();
        for (int i = 0; i < points.length; i += 2) {
            ShapeRenderer.LSR.vertex(points[i], points[i + 1]);
        }
        if (shape.closed()) {
            ShapeRenderer.LSR.vertex(points[0], points[1]);
        }
        ShapeRenderer.LSR.end();
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    public static final void draw(final Shape shape, final ShapeFill fill) {
        final float[] points = shape.getPoints();
        final Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        final float[] center = shape.getCenter();
        ShapeRenderer.GL.glBegin(3);
        for (int i = 0; i < points.length; i += 2) {
            fill.colorAt(shape, points[i], points[i + 1]).bind();
            final Vector2f offset = fill.getOffsetAt(shape, points[i], points[i + 1]);
            ShapeRenderer.GL.glVertex2f(points[i] + offset.x, points[i + 1] + offset.y);
        }
        if (shape.closed()) {
            fill.colorAt(shape, points[0], points[1]).bind();
            final Vector2f offset2 = fill.getOffsetAt(shape, points[0], points[1]);
            ShapeRenderer.GL.glVertex2f(points[0] + offset2.x, points[1] + offset2.y);
        }
        ShapeRenderer.GL.glEnd();
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    public static boolean validFill(final Shape shape) {
        return shape.getTriangles() != null && shape.getTriangles().getTriangleCount() != 0;
    }
    
    public static final void fill(final Shape shape) {
        if (!validFill(shape)) {
            return;
        }
        final Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        fill(shape, new PointCallback() {
            @Override
            public float[] preRenderPoint(final Shape shape, final float x, final float y) {
                return null;
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    private static final void fill(final Shape shape, final PointCallback callback) {
        final Triangulator tris = shape.getTriangles();
        ShapeRenderer.GL.glBegin(4);
        for (int i = 0; i < tris.getTriangleCount(); ++i) {
            for (int p = 0; p < 3; ++p) {
                final float[] pt = tris.getTrianglePoint(i, p);
                final float[] np = callback.preRenderPoint(shape, pt[0], pt[1]);
                if (np == null) {
                    ShapeRenderer.GL.glVertex2f(pt[0], pt[1]);
                }
                else {
                    ShapeRenderer.GL.glVertex2f(np[0], np[1]);
                }
            }
        }
        ShapeRenderer.GL.glEnd();
    }
    
    public static final void texture(final Shape shape, final Image image) {
        texture(shape, image, 0.01f, 0.01f);
    }
    
    public static final void textureFit(final Shape shape, final Image image) {
        textureFit(shape, image, 1.0f, 1.0f);
    }
    
    public static final void texture(final Shape shape, final Image image, final float scaleX, final float scaleY) {
        if (!validFill(shape)) {
            return;
        }
        final Texture t = TextureImpl.getLastBind();
        image.getTexture().bind();
        fill(shape, new PointCallback() {
            @Override
            public float[] preRenderPoint(final Shape shape, final float x, final float y) {
                float tx = x * scaleX;
                float ty = y * scaleY;
                tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
                ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
                ShapeRenderer.GL.glTexCoord2f(tx, ty);
                return null;
            }
        });
        final float[] points = shape.getPoints();
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    public static final void textureFit(final Shape shape, final Image image, final float scaleX, final float scaleY) {
        if (!validFill(shape)) {
            return;
        }
        final float[] points = shape.getPoints();
        final Texture t = TextureImpl.getLastBind();
        image.getTexture().bind();
        final float minX = shape.getX();
        final float minY = shape.getY();
        final float maxX = shape.getMaxX() - minX;
        final float maxY = shape.getMaxY() - minY;
        fill(shape, new PointCallback() {
            @Override
            public float[] preRenderPoint(final Shape shape, float x, float y) {
                x -= shape.getMinX();
                y -= shape.getMinY();
                x /= shape.getMaxX() - shape.getMinX();
                y /= shape.getMaxY() - shape.getMinY();
                float tx = x * scaleX;
                float ty = y * scaleY;
                tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
                ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
                ShapeRenderer.GL.glTexCoord2f(tx, ty);
                return null;
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    public static final void fill(final Shape shape, final ShapeFill fill) {
        if (!validFill(shape)) {
            return;
        }
        final Texture t = TextureImpl.getLastBind();
        TextureImpl.bindNone();
        final float[] center = shape.getCenter();
        fill(shape, new PointCallback() {
            @Override
            public float[] preRenderPoint(final Shape shape, final float x, final float y) {
                fill.colorAt(shape, x, y).bind();
                final Vector2f offset = fill.getOffsetAt(shape, x, y);
                return new float[] { offset.x + x, offset.y + y };
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    public static final void texture(final Shape shape, final Image image, final float scaleX, final float scaleY, final ShapeFill fill) {
        if (!validFill(shape)) {
            return;
        }
        final Texture t = TextureImpl.getLastBind();
        image.getTexture().bind();
        final float[] center = shape.getCenter();
        fill(shape, new PointCallback() {
            @Override
            public float[] preRenderPoint(final Shape shape, float x, float y) {
                fill.colorAt(shape, x - center[0], y - center[1]).bind();
                final Vector2f offset = fill.getOffsetAt(shape, x, y);
                x += offset.x;
                y += offset.y;
                float tx = x * scaleX;
                float ty = y * scaleY;
                tx = image.getTextureOffsetX() + image.getTextureWidth() * tx;
                ty = image.getTextureOffsetY() + image.getTextureHeight() * ty;
                ShapeRenderer.GL.glTexCoord2f(tx, ty);
                return new float[] { offset.x + x, offset.y + y };
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    public static final void texture(final Shape shape, final Image image, final TexCoordGenerator gen) {
        final Texture t = TextureImpl.getLastBind();
        image.getTexture().bind();
        final float[] center = shape.getCenter();
        fill(shape, new PointCallback() {
            @Override
            public float[] preRenderPoint(final Shape shape, final float x, final float y) {
                final Vector2f tex = gen.getCoordFor(x, y);
                ShapeRenderer.GL.glTexCoord2f(tex.x, tex.y);
                return new float[] { x, y };
            }
        });
        if (t == null) {
            TextureImpl.bindNone();
        }
        else {
            t.bind();
        }
    }
    
    private interface PointCallback
    {
        float[] preRenderPoint(final Shape p0, final float p1, final float p2);
    }
}
