// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.opengl.TextureImpl;
import java.nio.FloatBuffer;
import java.security.AccessController;
import org.newdawn.slick.util.Log;
import java.security.PrivilegedAction;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.renderer.Renderer;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.renderer.LineStripRenderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class Graphics
{
    protected static SGL GL;
    private static LineStripRenderer LSR;
    public static int MODE_NORMAL;
    public static int MODE_ALPHA_MAP;
    public static int MODE_ALPHA_BLEND;
    public static int MODE_COLOR_MULTIPLY;
    public static int MODE_ADD;
    public static int MODE_SCREEN;
    private static final int DEFAULT_SEGMENTS = 50;
    protected static Graphics currentGraphics;
    protected static Font DEFAULT_FONT;
    private float sx;
    private float sy;
    private Font font;
    private Color currentColor;
    protected int screenWidth;
    protected int screenHeight;
    private boolean pushed;
    private Rectangle clip;
    private DoubleBuffer worldClip;
    private ByteBuffer readBuffer;
    private boolean antialias;
    private Rectangle worldClipRecord;
    private int currentDrawingMode;
    private float lineWidth;
    private ArrayList stack;
    private int stackIndex;
    
    static {
        Graphics.GL = Renderer.get();
        Graphics.LSR = Renderer.getLineStripRenderer();
        Graphics.MODE_NORMAL = 1;
        Graphics.MODE_ALPHA_MAP = 2;
        Graphics.MODE_ALPHA_BLEND = 3;
        Graphics.MODE_COLOR_MULTIPLY = 4;
        Graphics.MODE_ADD = 5;
        Graphics.MODE_SCREEN = 6;
        Graphics.currentGraphics = null;
    }
    
    public static void setCurrent(final Graphics current) {
        if (Graphics.currentGraphics != current) {
            if (Graphics.currentGraphics != null) {
                Graphics.currentGraphics.disable();
            }
            (Graphics.currentGraphics = current).enable();
        }
    }
    
    public Graphics() {
        this.sx = 1.0f;
        this.sy = 1.0f;
        this.currentColor = Color.white;
        this.worldClip = BufferUtils.createDoubleBuffer(4);
        this.readBuffer = BufferUtils.createByteBuffer(4);
        this.currentDrawingMode = Graphics.MODE_NORMAL;
        this.lineWidth = 1.0f;
        this.stack = new ArrayList();
    }
    
    public Graphics(final int width, final int height) {
        this.sx = 1.0f;
        this.sy = 1.0f;
        this.currentColor = Color.white;
        this.worldClip = BufferUtils.createDoubleBuffer(4);
        this.readBuffer = BufferUtils.createByteBuffer(4);
        this.currentDrawingMode = Graphics.MODE_NORMAL;
        this.lineWidth = 1.0f;
        this.stack = new ArrayList();
        if (Graphics.DEFAULT_FONT == null) {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        Graphics.DEFAULT_FONT = new AngelCodeFont("org/newdawn/slick/data/defaultfont.fnt", "org/newdawn/slick/data/defaultfont.png");
                    }
                    catch (SlickException e) {
                        Log.error(e);
                    }
                    return null;
                }
            });
        }
        this.font = Graphics.DEFAULT_FONT;
        this.screenWidth = width;
        this.screenHeight = height;
    }
    
    void setDimensions(final int width, final int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }
    
    public void setDrawMode(final int mode) {
        this.predraw();
        this.currentDrawingMode = mode;
        if (this.currentDrawingMode == Graphics.MODE_NORMAL) {
            Graphics.GL.glEnable(3042);
            Graphics.GL.glColorMask(true, true, true, true);
            Graphics.GL.glBlendFunc(770, 771);
        }
        if (this.currentDrawingMode == Graphics.MODE_ALPHA_MAP) {
            Graphics.GL.glDisable(3042);
            Graphics.GL.glColorMask(false, false, false, true);
        }
        if (this.currentDrawingMode == Graphics.MODE_ALPHA_BLEND) {
            Graphics.GL.glEnable(3042);
            Graphics.GL.glColorMask(true, true, true, false);
            Graphics.GL.glBlendFunc(772, 773);
        }
        if (this.currentDrawingMode == Graphics.MODE_COLOR_MULTIPLY) {
            Graphics.GL.glEnable(3042);
            Graphics.GL.glColorMask(true, true, true, true);
            Graphics.GL.glBlendFunc(769, 768);
        }
        if (this.currentDrawingMode == Graphics.MODE_ADD) {
            Graphics.GL.glEnable(3042);
            Graphics.GL.glColorMask(true, true, true, true);
            Graphics.GL.glBlendFunc(1, 1);
        }
        if (this.currentDrawingMode == Graphics.MODE_SCREEN) {
            Graphics.GL.glEnable(3042);
            Graphics.GL.glColorMask(true, true, true, true);
            Graphics.GL.glBlendFunc(1, 769);
        }
        this.postdraw();
    }
    
    public void clearAlphaMap() {
        this.pushTransform();
        Graphics.GL.glLoadIdentity();
        final int originalMode = this.currentDrawingMode;
        this.setDrawMode(Graphics.MODE_ALPHA_MAP);
        this.setColor(new Color(0, 0, 0, 0));
        this.fillRect(0.0f, 0.0f, (float)this.screenWidth, (float)this.screenHeight);
        this.setColor(this.currentColor);
        this.setDrawMode(originalMode);
        this.popTransform();
    }
    
    private void predraw() {
        setCurrent(this);
    }
    
    private void postdraw() {
    }
    
    protected void enable() {
    }
    
    public void flush() {
        if (Graphics.currentGraphics == this) {
            Graphics.currentGraphics.disable();
            Graphics.currentGraphics = null;
        }
    }
    
    protected void disable() {
    }
    
    public Font getFont() {
        return this.font;
    }
    
    public void setBackground(final Color color) {
        this.predraw();
        Graphics.GL.glClearColor(color.r, color.g, color.b, color.a);
        this.postdraw();
    }
    
    public Color getBackground() {
        this.predraw();
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        Graphics.GL.glGetFloat(3106, buffer);
        this.postdraw();
        return new Color(buffer);
    }
    
    public void clear() {
        this.predraw();
        Graphics.GL.glClear(16384);
        this.postdraw();
    }
    
    public void resetTransform() {
        this.sx = 1.0f;
        this.sy = 1.0f;
        if (this.pushed) {
            this.predraw();
            Graphics.GL.glPopMatrix();
            this.pushed = false;
            this.postdraw();
        }
    }
    
    private void checkPush() {
        if (!this.pushed) {
            this.predraw();
            Graphics.GL.glPushMatrix();
            this.pushed = true;
            this.postdraw();
        }
    }
    
    public void scale(final float sx, final float sy) {
        this.sx *= sx;
        this.sy *= sy;
        this.checkPush();
        this.predraw();
        Graphics.GL.glScalef(sx, sy, 1.0f);
        this.postdraw();
    }
    
    public void rotate(final float rx, final float ry, final float ang) {
        this.checkPush();
        this.predraw();
        this.translate(rx, ry);
        Graphics.GL.glRotatef(ang, 0.0f, 0.0f, 1.0f);
        this.translate(-rx, -ry);
        this.postdraw();
    }
    
    public void translate(final float x, final float y) {
        this.checkPush();
        this.predraw();
        Graphics.GL.glTranslatef(x, y, 0.0f);
        this.postdraw();
    }
    
    public void setFont(final Font font) {
        this.font = font;
    }
    
    public void resetFont() {
        this.font = Graphics.DEFAULT_FONT;
    }
    
    public void setColor(final Color color) {
        if (color == null) {
            return;
        }
        this.currentColor = new Color(color);
        this.predraw();
        this.currentColor.bind();
        this.postdraw();
    }
    
    public Color getColor() {
        return new Color(this.currentColor);
    }
    
    public void drawLine(float x1, float y1, float x2, float y2) {
        float lineWidth = this.lineWidth - 1.0f;
        if (Graphics.LSR.applyGLLineFixes()) {
            if (x1 == x2) {
                if (y1 > y2) {
                    final float temp = y2;
                    y2 = y1;
                    y1 = temp;
                }
                final float step = 1.0f / this.sy;
                lineWidth /= this.sy;
                this.fillRect(x1 - lineWidth / 2.0f, y1 - lineWidth / 2.0f, lineWidth + step, y2 - y1 + lineWidth + step);
                return;
            }
            if (y1 == y2) {
                if (x1 > x2) {
                    final float temp = x2;
                    x2 = x1;
                    x1 = temp;
                }
                final float step = 1.0f / this.sx;
                lineWidth /= this.sx;
                this.fillRect(x1 - lineWidth / 2.0f, y1 - lineWidth / 2.0f, x2 - x1 + lineWidth + step, lineWidth + step);
                return;
            }
        }
        this.predraw();
        this.currentColor.bind();
        TextureImpl.bindNone();
        Graphics.LSR.start();
        Graphics.LSR.vertex(x1, y1);
        Graphics.LSR.vertex(x2, y2);
        Graphics.LSR.end();
        this.postdraw();
    }
    
    public void draw(final Shape shape, final ShapeFill fill) {
        this.predraw();
        TextureImpl.bindNone();
        ShapeRenderer.draw(shape, fill);
        this.currentColor.bind();
        this.postdraw();
    }
    
    public void fill(final Shape shape, final ShapeFill fill) {
        this.predraw();
        TextureImpl.bindNone();
        ShapeRenderer.fill(shape, fill);
        this.currentColor.bind();
        this.postdraw();
    }
    
    public void draw(final Shape shape) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        ShapeRenderer.draw(shape);
        this.postdraw();
    }
    
    public void fill(final Shape shape) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        ShapeRenderer.fill(shape);
        this.postdraw();
    }
    
    public void texture(final Shape shape, final Image image) {
        this.texture(shape, image, 0.01f, 0.01f, false);
    }
    
    public void texture(final Shape shape, final Image image, final ShapeFill fill) {
        this.texture(shape, image, 0.01f, 0.01f, fill);
    }
    
    public void texture(final Shape shape, final Image image, final boolean fit) {
        if (fit) {
            this.texture(shape, image, 1.0f, 1.0f, true);
        }
        else {
            this.texture(shape, image, 0.01f, 0.01f, false);
        }
    }
    
    public void texture(final Shape shape, final Image image, final float scaleX, final float scaleY) {
        this.texture(shape, image, scaleX, scaleY, false);
    }
    
    public void texture(final Shape shape, final Image image, final float scaleX, final float scaleY, final boolean fit) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        if (fit) {
            ShapeRenderer.textureFit(shape, image, scaleX, scaleY);
        }
        else {
            ShapeRenderer.texture(shape, image, scaleX, scaleY);
        }
        this.postdraw();
    }
    
    public void texture(final Shape shape, final Image image, final float scaleX, final float scaleY, final ShapeFill fill) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        ShapeRenderer.texture(shape, image, scaleX, scaleY, fill);
        this.postdraw();
    }
    
    public void drawRect(final float x1, final float y1, final float width, final float height) {
        final float lineWidth = this.getLineWidth();
        this.drawLine(x1, y1, x1 + width, y1);
        this.drawLine(x1 + width, y1, x1 + width, y1 + height);
        this.drawLine(x1 + width, y1 + height, x1, y1 + height);
        this.drawLine(x1, y1 + height, x1, y1);
    }
    
    public void clearClip() {
        this.clip = null;
        this.predraw();
        Graphics.GL.glDisable(3089);
        this.postdraw();
    }
    
    public void setWorldClip(final float x, final float y, final float width, final float height) {
        this.predraw();
        this.worldClipRecord = new Rectangle(x, y, width, height);
        Graphics.GL.glEnable(12288);
        this.worldClip.put(1.0).put(0.0).put(0.0).put(-x).flip();
        Graphics.GL.glClipPlane(12288, this.worldClip);
        Graphics.GL.glEnable(12289);
        this.worldClip.put(-1.0).put(0.0).put(0.0).put(x + width).flip();
        Graphics.GL.glClipPlane(12289, this.worldClip);
        Graphics.GL.glEnable(12290);
        this.worldClip.put(0.0).put(1.0).put(0.0).put(-y).flip();
        Graphics.GL.glClipPlane(12290, this.worldClip);
        Graphics.GL.glEnable(12291);
        this.worldClip.put(0.0).put(-1.0).put(0.0).put(y + height).flip();
        Graphics.GL.glClipPlane(12291, this.worldClip);
        this.postdraw();
    }
    
    public void clearWorldClip() {
        this.predraw();
        this.worldClipRecord = null;
        Graphics.GL.glDisable(12288);
        Graphics.GL.glDisable(12289);
        Graphics.GL.glDisable(12290);
        Graphics.GL.glDisable(12291);
        this.postdraw();
    }
    
    public void setWorldClip(final Rectangle clip) {
        if (clip == null) {
            this.clearWorldClip();
        }
        else {
            this.setWorldClip(clip.getX(), clip.getY(), clip.getWidth(), clip.getHeight());
        }
    }
    
    public Rectangle getWorldClip() {
        return this.worldClipRecord;
    }
    
    public void setClip(final int x, final int y, final int width, final int height) {
        this.predraw();
        if (this.clip == null) {
            Graphics.GL.glEnable(3089);
            this.clip = new Rectangle((float)x, (float)y, (float)width, (float)height);
        }
        else {
            this.clip.setBounds((float)x, (float)y, (float)width, (float)height);
        }
        Graphics.GL.glScissor(x, this.screenHeight - y - height, width, height);
        this.postdraw();
    }
    
    public void setClip(final Rectangle rect) {
        if (rect == null) {
            this.clearClip();
            return;
        }
        this.setClip((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
    }
    
    public Rectangle getClip() {
        return this.clip;
    }
    
    public void fillRect(final float x, final float y, final float width, final float height, final Image pattern, final float offX, final float offY) {
        final int cols = (int)Math.ceil(width / pattern.getWidth()) + 2;
        final int rows = (int)Math.ceil(height / pattern.getHeight()) + 2;
        final Rectangle preClip = this.getWorldClip();
        this.setWorldClip(x, y, width, height);
        this.predraw();
        for (int c = 0; c < cols; ++c) {
            for (int r = 0; r < rows; ++r) {
                pattern.draw(c * pattern.getWidth() + x - offX, r * pattern.getHeight() + y - offY);
            }
        }
        this.postdraw();
        this.setWorldClip(preClip);
    }
    
    public void fillRect(final float x1, final float y1, final float width, final float height) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        Graphics.GL.glBegin(7);
        Graphics.GL.glVertex2f(x1, y1);
        Graphics.GL.glVertex2f(x1 + width, y1);
        Graphics.GL.glVertex2f(x1 + width, y1 + height);
        Graphics.GL.glVertex2f(x1, y1 + height);
        Graphics.GL.glEnd();
        this.postdraw();
    }
    
    public void drawOval(final float x1, final float y1, final float width, final float height) {
        this.drawOval(x1, y1, width, height, 50);
    }
    
    public void drawOval(final float x1, final float y1, final float width, final float height, final int segments) {
        this.drawArc(x1, y1, width, height, segments, 0.0f, 360.0f);
    }
    
    public void drawArc(final float x1, final float y1, final float width, final float height, final float start, final float end) {
        this.drawArc(x1, y1, width, height, 50, start, end);
    }
    
    public void drawArc(final float x1, final float y1, final float width, final float height, final int segments, final float start, float end) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        while (end < start) {
            end += 360.0f;
        }
        final float cx = x1 + width / 2.0f;
        final float cy = y1 + height / 2.0f;
        Graphics.LSR.start();
        for (int step = 360 / segments, a = (int)start; a < (int)(end + step); a += step) {
            float ang = (float)a;
            if (ang > end) {
                ang = end;
            }
            final float x2 = (float)(cx + FastTrig.cos(Math.toRadians(ang)) * width / 2.0);
            final float y2 = (float)(cy + FastTrig.sin(Math.toRadians(ang)) * height / 2.0);
            Graphics.LSR.vertex(x2, y2);
        }
        Graphics.LSR.end();
        this.postdraw();
    }
    
    public void fillOval(final float x1, final float y1, final float width, final float height) {
        this.fillOval(x1, y1, width, height, 50);
    }
    
    public void fillOval(final float x1, final float y1, final float width, final float height, final int segments) {
        this.fillArc(x1, y1, width, height, segments, 0.0f, 360.0f);
    }
    
    public void fillArc(final float x1, final float y1, final float width, final float height, final float start, final float end) {
        this.fillArc(x1, y1, width, height, 50, start, end);
    }
    
    public void fillArc(final float x1, final float y1, final float width, final float height, final int segments, final float start, float end) {
        this.predraw();
        TextureImpl.bindNone();
        this.currentColor.bind();
        while (end < start) {
            end += 360.0f;
        }
        final float cx = x1 + width / 2.0f;
        final float cy = y1 + height / 2.0f;
        Graphics.GL.glBegin(6);
        final int step = 360 / segments;
        Graphics.GL.glVertex2f(cx, cy);
        for (int a = (int)start; a < (int)(end + step); a += step) {
            float ang = (float)a;
            if (ang > end) {
                ang = end;
            }
            final float x2 = (float)(cx + FastTrig.cos(Math.toRadians(ang)) * width / 2.0);
            final float y2 = (float)(cy + FastTrig.sin(Math.toRadians(ang)) * height / 2.0);
            Graphics.GL.glVertex2f(x2, y2);
        }
        Graphics.GL.glEnd();
        if (this.antialias) {
            Graphics.GL.glBegin(6);
            Graphics.GL.glVertex2f(cx, cy);
            if (end != 360.0f) {
                end -= 10.0f;
            }
            for (int a = (int)start; a < (int)(end + step); a += step) {
                float ang = (float)a;
                if (ang > end) {
                    ang = end;
                }
                final float x2 = (float)(cx + FastTrig.cos(Math.toRadians(ang + 10.0f)) * width / 2.0);
                final float y2 = (float)(cy + FastTrig.sin(Math.toRadians(ang + 10.0f)) * height / 2.0);
                Graphics.GL.glVertex2f(x2, y2);
            }
            Graphics.GL.glEnd();
        }
        this.postdraw();
    }
    
    public void drawRoundRect(final float x, final float y, final float width, final float height, final int cornerRadius) {
        this.drawRoundRect(x, y, width, height, cornerRadius, 50);
    }
    
    public void drawRoundRect(final float x, final float y, final float width, final float height, int cornerRadius, final int segs) {
        if (cornerRadius < 0) {
            throw new IllegalArgumentException("corner radius must be > 0");
        }
        if (cornerRadius == 0) {
            this.drawRect(x, y, width, height);
            return;
        }
        final int mr = (int)Math.min(width, height) / 2;
        if (cornerRadius > mr) {
            cornerRadius = mr;
        }
        this.drawLine(x + cornerRadius, y, x + width - cornerRadius, y);
        this.drawLine(x, y + cornerRadius, x, y + height - cornerRadius);
        this.drawLine(x + width, y + cornerRadius, x + width, y + height - cornerRadius);
        this.drawLine(x + cornerRadius, y + height, x + width - cornerRadius, y + height);
        final float d = (float)(cornerRadius * 2);
        this.drawArc(x + width - d, y + height - d, d, d, segs, 0.0f, 90.0f);
        this.drawArc(x, y + height - d, d, d, segs, 90.0f, 180.0f);
        this.drawArc(x + width - d, y, d, d, segs, 270.0f, 360.0f);
        this.drawArc(x, y, d, d, segs, 180.0f, 270.0f);
    }
    
    public void fillRoundRect(final float x, final float y, final float width, final float height, final int cornerRadius) {
        this.fillRoundRect(x, y, width, height, cornerRadius, 50);
    }
    
    public void fillRoundRect(final float x, final float y, final float width, final float height, int cornerRadius, final int segs) {
        if (cornerRadius < 0) {
            throw new IllegalArgumentException("corner radius must be > 0");
        }
        if (cornerRadius == 0) {
            this.fillRect(x, y, width, height);
            return;
        }
        final int mr = (int)Math.min(width, height) / 2;
        if (cornerRadius > mr) {
            cornerRadius = mr;
        }
        final float d = (float)(cornerRadius * 2);
        this.fillRect(x + cornerRadius, y, width - d, (float)cornerRadius);
        this.fillRect(x, y + cornerRadius, (float)cornerRadius, height - d);
        this.fillRect(x + width - cornerRadius, y + cornerRadius, (float)cornerRadius, height - d);
        this.fillRect(x + cornerRadius, y + height - cornerRadius, width - d, (float)cornerRadius);
        this.fillRect(x + cornerRadius, y + cornerRadius, width - d, height - d);
        this.fillArc(x + width - d, y + height - d, d, d, segs, 0.0f, 90.0f);
        this.fillArc(x, y + height - d, d, d, segs, 90.0f, 180.0f);
        this.fillArc(x + width - d, y, d, d, segs, 270.0f, 360.0f);
        this.fillArc(x, y, d, d, segs, 180.0f, 270.0f);
    }
    
    public void setLineWidth(final float width) {
        this.predraw();
        this.lineWidth = width;
        Graphics.LSR.setWidth(width);
        Graphics.GL.glPointSize(width);
        this.postdraw();
    }
    
    public float getLineWidth() {
        return this.lineWidth;
    }
    
    public void resetLineWidth() {
        this.predraw();
        Renderer.getLineStripRenderer().setWidth(1.0f);
        Graphics.GL.glLineWidth(1.0f);
        Graphics.GL.glPointSize(1.0f);
        this.postdraw();
    }
    
    public void setAntiAlias(final boolean anti) {
        this.predraw();
        this.antialias = anti;
        Graphics.LSR.setAntiAlias(anti);
        if (anti) {
            Graphics.GL.glEnable(2881);
        }
        else {
            Graphics.GL.glDisable(2881);
        }
        this.postdraw();
    }
    
    public boolean isAntiAlias() {
        return this.antialias;
    }
    
    public void drawString(final String str, final float x, final float y) {
        this.predraw();
        this.font.drawString(x, y, str, this.currentColor);
        this.postdraw();
    }
    
    public void drawImage(final Image image, final float x, final float y, final Color col) {
        this.predraw();
        image.draw(x, y, col);
        this.currentColor.bind();
        this.postdraw();
    }
    
    public void drawAnimation(final Animation anim, final float x, final float y) {
        this.drawAnimation(anim, x, y, Color.white);
    }
    
    public void drawAnimation(final Animation anim, final float x, final float y, final Color col) {
        this.predraw();
        anim.draw(x, y, col);
        this.currentColor.bind();
        this.postdraw();
    }
    
    public void drawImage(final Image image, final float x, final float y) {
        this.drawImage(image, x, y, Color.white);
    }
    
    public void drawImage(final Image image, final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        this.predraw();
        image.draw(x, y, x2, y2, srcx, srcy, srcx2, srcy2);
        this.currentColor.bind();
        this.postdraw();
    }
    
    public void drawImage(final Image image, final float x, final float y, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        this.drawImage(image, x, y, x + image.getWidth(), y + image.getHeight(), srcx, srcy, srcx2, srcy2);
    }
    
    public void copyArea(final Image target, final int x, final int y) {
        final int format = target.getTexture().hasAlpha() ? 6408 : 6407;
        target.bind();
        Graphics.GL.glCopyTexImage2D(3553, 0, format, x, this.screenHeight - (y + target.getHeight()), target.getTexture().getTextureWidth(), target.getTexture().getTextureHeight(), 0);
        target.ensureInverted();
    }
    
    private int translate(final byte b) {
        if (b < 0) {
            return 256 + b;
        }
        return b;
    }
    
    public Color getPixel(final int x, final int y) {
        this.predraw();
        Graphics.GL.glReadPixels(x, this.screenHeight - y, 1, 1, 6408, 5121, this.readBuffer);
        this.postdraw();
        return new Color(this.translate(this.readBuffer.get(0)), this.translate(this.readBuffer.get(1)), this.translate(this.readBuffer.get(2)), this.translate(this.readBuffer.get(3)));
    }
    
    public void getArea(final int x, final int y, final int width, final int height, final ByteBuffer target) {
        if (target.capacity() < width * height * 4) {
            throw new IllegalArgumentException("Byte buffer provided to get area is not big enough");
        }
        this.predraw();
        Graphics.GL.glReadPixels(x, this.screenHeight - y - height, width, height, 6408, 5121, target);
        this.postdraw();
    }
    
    public void drawImage(final Image image, final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2, final Color col) {
        this.predraw();
        image.draw(x, y, x2, y2, srcx, srcy, srcx2, srcy2, col);
        this.currentColor.bind();
        this.postdraw();
    }
    
    public void drawImage(final Image image, final float x, final float y, final float srcx, final float srcy, final float srcx2, final float srcy2, final Color col) {
        this.drawImage(image, x, y, x + image.getWidth(), y + image.getHeight(), srcx, srcy, srcx2, srcy2, col);
    }
    
    public void drawGradientLine(final float x1, final float y1, final float red1, final float green1, final float blue1, final float alpha1, final float x2, final float y2, final float red2, final float green2, final float blue2, final float alpha2) {
        this.predraw();
        TextureImpl.bindNone();
        Graphics.GL.glBegin(1);
        Graphics.GL.glColor4f(red1, green1, blue1, alpha1);
        Graphics.GL.glVertex2f(x1, y1);
        Graphics.GL.glColor4f(red2, green2, blue2, alpha2);
        Graphics.GL.glVertex2f(x2, y2);
        Graphics.GL.glEnd();
        this.postdraw();
    }
    
    public void drawGradientLine(final float x1, final float y1, final Color Color1, final float x2, final float y2, final Color Color2) {
        this.predraw();
        TextureImpl.bindNone();
        Graphics.GL.glBegin(1);
        Color1.bind();
        Graphics.GL.glVertex2f(x1, y1);
        Color2.bind();
        Graphics.GL.glVertex2f(x2, y2);
        Graphics.GL.glEnd();
        this.postdraw();
    }
    
    public void pushTransform() {
        this.predraw();
        FloatBuffer buffer;
        if (this.stackIndex >= this.stack.size()) {
            buffer = BufferUtils.createFloatBuffer(18);
            this.stack.add(buffer);
        }
        else {
            buffer = this.stack.get(this.stackIndex);
        }
        Graphics.GL.glGetFloat(2982, buffer);
        buffer.put(16, this.sx);
        buffer.put(17, this.sy);
        ++this.stackIndex;
        this.postdraw();
    }
    
    public void popTransform() {
        if (this.stackIndex == 0) {
            throw new RuntimeException("Attempt to pop a transform that hasn't be pushed");
        }
        this.predraw();
        --this.stackIndex;
        final FloatBuffer oldBuffer = this.stack.get(this.stackIndex);
        Graphics.GL.glLoadMatrix(oldBuffer);
        this.sx = oldBuffer.get(16);
        this.sy = oldBuffer.get(17);
        this.postdraw();
    }
    
    public void destroy() {
    }
}
