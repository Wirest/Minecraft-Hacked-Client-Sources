// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl.renderer;

public class QuadBasedLineStripRenderer implements LineStripRenderer
{
    private SGL GL;
    public static int MAX_POINTS;
    private boolean antialias;
    private float width;
    private float[] points;
    private float[] colours;
    private int pts;
    private int cpt;
    private DefaultLineStripRenderer def;
    private boolean renderHalf;
    private boolean lineCaps;
    
    static {
        QuadBasedLineStripRenderer.MAX_POINTS = 10000;
    }
    
    public QuadBasedLineStripRenderer() {
        this.GL = Renderer.get();
        this.width = 1.0f;
        this.def = new DefaultLineStripRenderer();
        this.lineCaps = false;
        this.points = new float[QuadBasedLineStripRenderer.MAX_POINTS * 2];
        this.colours = new float[QuadBasedLineStripRenderer.MAX_POINTS * 4];
    }
    
    @Override
    public void setLineCaps(final boolean caps) {
        this.lineCaps = caps;
    }
    
    @Override
    public void start() {
        if (this.width == 1.0f) {
            this.def.start();
            return;
        }
        this.pts = 0;
        this.cpt = 0;
        this.GL.flush();
        final float[] col = this.GL.getCurrentColor();
        this.color(col[0], col[1], col[2], col[3]);
    }
    
    @Override
    public void end() {
        if (this.width == 1.0f) {
            this.def.end();
            return;
        }
        this.renderLines(this.points, this.pts);
    }
    
    @Override
    public void vertex(final float x, final float y) {
        if (this.width == 1.0f) {
            this.def.vertex(x, y);
            return;
        }
        this.points[this.pts * 2] = x;
        this.points[this.pts * 2 + 1] = y;
        ++this.pts;
        final int index = this.pts - 1;
        this.color(this.colours[index * 4], this.colours[index * 4 + 1], this.colours[index * 4 + 2], this.colours[index * 4 + 3]);
    }
    
    @Override
    public void setWidth(final float width) {
        this.width = width;
    }
    
    @Override
    public void setAntiAlias(final boolean antialias) {
        this.def.setAntiAlias(antialias);
        this.antialias = antialias;
    }
    
    public void renderLines(final float[] points, final int count) {
        if (this.antialias) {
            this.GL.glEnable(2881);
            this.renderLinesImpl(points, count, this.width + 1.0f);
        }
        this.GL.glDisable(2881);
        this.renderLinesImpl(points, count, this.width);
        if (this.antialias) {
            this.GL.glEnable(2881);
        }
    }
    
    public void renderLinesImpl(final float[] points, final int count, final float w) {
        final float width = w / 2.0f;
        float lastx1 = 0.0f;
        float lasty1 = 0.0f;
        float lastx2 = 0.0f;
        float lasty2 = 0.0f;
        this.GL.glBegin(7);
        for (int i = 0; i < count + 1; ++i) {
            int current = i;
            int next = i + 1;
            int prev = i - 1;
            if (prev < 0) {
                prev += count;
            }
            if (next >= count) {
                next -= count;
            }
            if (current >= count) {
                current -= count;
            }
            final float x1 = points[current * 2];
            final float y1 = points[current * 2 + 1];
            final float x2 = points[next * 2];
            final float y2 = points[next * 2 + 1];
            float dx = x2 - x1;
            float dy = y2 - y1;
            if (dx != 0.0f || dy != 0.0f) {
                final float d2 = dx * dx + dy * dy;
                final float d3 = (float)Math.sqrt(d2);
                dx *= width;
                dy *= width;
                dx /= d3;
                final float tx;
                dy = (tx = dy / d3);
                final float ty = -dx;
                if (i != 0) {
                    this.bindColor(prev);
                    this.GL.glVertex3f(lastx1, lasty1, 0.0f);
                    this.GL.glVertex3f(lastx2, lasty2, 0.0f);
                    this.bindColor(current);
                    this.GL.glVertex3f(x1 + tx, y1 + ty, 0.0f);
                    this.GL.glVertex3f(x1 - tx, y1 - ty, 0.0f);
                }
                lastx1 = x2 - tx;
                lasty1 = y2 - ty;
                lastx2 = x2 + tx;
                lasty2 = y2 + ty;
                if (i < count - 1) {
                    this.bindColor(current);
                    this.GL.glVertex3f(x1 + tx, y1 + ty, 0.0f);
                    this.GL.glVertex3f(x1 - tx, y1 - ty, 0.0f);
                    this.bindColor(next);
                    this.GL.glVertex3f(x2 - tx, y2 - ty, 0.0f);
                    this.GL.glVertex3f(x2 + tx, y2 + ty, 0.0f);
                }
            }
        }
        this.GL.glEnd();
        final float step = (width <= 12.5f) ? 5.0f : (180.0f / (float)Math.ceil(width / 2.5));
        if (this.lineCaps) {
            final float dx2 = points[2] - points[0];
            final float dy2 = points[3] - points[1];
            final float fang = (float)Math.toDegrees(Math.atan2(dy2, dx2)) + 90.0f;
            if (dx2 != 0.0f || dy2 != 0.0f) {
                this.GL.glBegin(6);
                this.bindColor(0);
                this.GL.glVertex2f(points[0], points[1]);
                for (int j = 0; j < 180.0f + step; j += (int)step) {
                    final float ang = (float)Math.toRadians(fang + j);
                    this.GL.glVertex2f(points[0] + (float)(Math.cos(ang) * width), points[1] + (float)(Math.sin(ang) * width));
                }
                this.GL.glEnd();
            }
        }
        if (this.lineCaps) {
            final float dx2 = points[count * 2 - 2] - points[count * 2 - 4];
            final float dy2 = points[count * 2 - 1] - points[count * 2 - 3];
            final float fang = (float)Math.toDegrees(Math.atan2(dy2, dx2)) - 90.0f;
            if (dx2 != 0.0f || dy2 != 0.0f) {
                this.GL.glBegin(6);
                this.bindColor(count - 1);
                this.GL.glVertex2f(points[count * 2 - 2], points[count * 2 - 1]);
                for (int j = 0; j < 180.0f + step; j += (int)step) {
                    final float ang = (float)Math.toRadians(fang + j);
                    this.GL.glVertex2f(points[count * 2 - 2] + (float)(Math.cos(ang) * width), points[count * 2 - 1] + (float)(Math.sin(ang) * width));
                }
                this.GL.glEnd();
            }
        }
    }
    
    private void bindColor(final int index) {
        if (index < this.cpt) {
            if (this.renderHalf) {
                this.GL.glColor4f(this.colours[index * 4] * 0.5f, this.colours[index * 4 + 1] * 0.5f, this.colours[index * 4 + 2] * 0.5f, this.colours[index * 4 + 3] * 0.5f);
            }
            else {
                this.GL.glColor4f(this.colours[index * 4], this.colours[index * 4 + 1], this.colours[index * 4 + 2], this.colours[index * 4 + 3]);
            }
        }
    }
    
    @Override
    public void color(final float r, final float g, final float b, final float a) {
        if (this.width == 1.0f) {
            this.def.color(r, g, b, a);
            return;
        }
        this.colours[this.pts * 4] = r;
        this.colours[this.pts * 4 + 1] = g;
        this.colours[this.pts * 4 + 2] = b;
        this.colours[this.pts * 4 + 3] = a;
        ++this.cpt;
    }
    
    @Override
    public boolean applyGLLineFixes() {
        if (this.width == 1.0f) {
            return this.def.applyGLLineFixes();
        }
        return this.def.applyGLLineFixes();
    }
}
