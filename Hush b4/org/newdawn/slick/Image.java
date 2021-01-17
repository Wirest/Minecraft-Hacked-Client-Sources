// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.opengl.pbuffer.GraphicsFactory;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.TextureImpl;
import java.io.InputStream;
import java.io.IOException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.renderer.SGL;

public class Image implements Renderable
{
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_RIGHT = 2;
    public static final int BOTTOM_LEFT = 3;
    protected static SGL GL;
    protected static Image inUse;
    public static final int FILTER_LINEAR = 1;
    public static final int FILTER_NEAREST = 2;
    protected Texture texture;
    protected int width;
    protected int height;
    protected float textureWidth;
    protected float textureHeight;
    protected float textureOffsetX;
    protected float textureOffsetY;
    protected float angle;
    protected float alpha;
    protected String ref;
    protected boolean inited;
    protected byte[] pixelData;
    protected boolean destroyed;
    protected float centerX;
    protected float centerY;
    protected String name;
    protected Color[] corners;
    private int filter;
    private boolean flipped;
    private Color transparent;
    
    static {
        Image.GL = Renderer.get();
    }
    
    protected Image(final Image other) {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
        this.width = other.getWidth();
        this.height = other.getHeight();
        this.texture = other.texture;
        this.textureWidth = other.textureWidth;
        this.textureHeight = other.textureHeight;
        this.ref = other.ref;
        this.textureOffsetX = other.textureOffsetX;
        this.textureOffsetY = other.textureOffsetY;
        this.centerX = (float)(this.width / 2);
        this.centerY = (float)(this.height / 2);
        this.inited = true;
    }
    
    protected Image() {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
    }
    
    public Image(final Texture texture) {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
        this.texture = texture;
        this.ref = texture.toString();
        this.clampTexture();
    }
    
    public Image(final String ref) throws SlickException {
        this(ref, false);
    }
    
    public Image(final String ref, final Color trans) throws SlickException {
        this(ref, false, 1, trans);
    }
    
    public Image(final String ref, final boolean flipped) throws SlickException {
        this(ref, flipped, 1);
    }
    
    public Image(final String ref, final boolean flipped, final int filter) throws SlickException {
        this(ref, flipped, filter, null);
    }
    
    public Image(final String ref, final boolean flipped, final int f, final Color transparent) throws SlickException {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
        this.filter = ((f == 1) ? 9729 : 9728);
        this.transparent = transparent;
        this.flipped = flipped;
        try {
            this.ref = ref;
            int[] trans = null;
            if (transparent != null) {
                trans = new int[] { (int)(transparent.r * 255.0f), (int)(transparent.g * 255.0f), (int)(transparent.b * 255.0f) };
            }
            this.texture = InternalTextureLoader.get().getTexture(ref, flipped, this.filter, trans);
        }
        catch (IOException e) {
            Log.error(e);
            throw new SlickException("Failed to load image from: " + ref, e);
        }
    }
    
    public void setFilter(final int f) {
        this.filter = ((f == 1) ? 9729 : 9728);
        this.texture.bind();
        Image.GL.glTexParameteri(3553, 10241, this.filter);
        Image.GL.glTexParameteri(3553, 10240, this.filter);
    }
    
    public Image(final int width, final int height) throws SlickException {
        this(width, height, 2);
    }
    
    public Image(final int width, final int height, final int f) throws SlickException {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
        this.ref = super.toString();
        this.filter = ((f == 1) ? 9729 : 9728);
        try {
            this.texture = InternalTextureLoader.get().createTexture(width, height, this.filter);
        }
        catch (IOException e) {
            Log.error(e);
            throw new SlickException("Failed to create empty image " + width + "x" + height);
        }
        this.init();
    }
    
    public Image(final InputStream in, final String ref, final boolean flipped) throws SlickException {
        this(in, ref, flipped, 1);
    }
    
    public Image(final InputStream in, final String ref, final boolean flipped, final int filter) throws SlickException {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
        this.load(in, ref, flipped, filter, null);
    }
    
    Image(final ImageBuffer buffer) {
        this(buffer, 1);
        TextureImpl.bindNone();
    }
    
    Image(final ImageBuffer buffer, final int filter) {
        this((ImageData)buffer, filter);
        TextureImpl.bindNone();
    }
    
    public Image(final ImageData data) {
        this(data, 1);
    }
    
    public Image(final ImageData data, final int f) {
        this.alpha = 1.0f;
        this.inited = false;
        this.filter = 9729;
        try {
            this.filter = ((f == 1) ? 9729 : 9728);
            this.texture = InternalTextureLoader.get().getTexture(data, this.filter);
            this.ref = this.texture.toString();
        }
        catch (IOException e) {
            Log.error(e);
        }
    }
    
    public int getFilter() {
        return this.filter;
    }
    
    public String getResourceReference() {
        return this.ref;
    }
    
    public void setImageColor(final float r, final float g, final float b, final float a) {
        this.setColor(0, r, g, b, a);
        this.setColor(1, r, g, b, a);
        this.setColor(3, r, g, b, a);
        this.setColor(2, r, g, b, a);
    }
    
    public void setImageColor(final float r, final float g, final float b) {
        this.setColor(0, r, g, b);
        this.setColor(1, r, g, b);
        this.setColor(3, r, g, b);
        this.setColor(2, r, g, b);
    }
    
    public void setColor(final int corner, final float r, final float g, final float b, final float a) {
        if (this.corners == null) {
            this.corners = new Color[] { new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f) };
        }
        this.corners[corner].r = r;
        this.corners[corner].g = g;
        this.corners[corner].b = b;
        this.corners[corner].a = a;
    }
    
    public void setColor(final int corner, final float r, final float g, final float b) {
        if (this.corners == null) {
            this.corners = new Color[] { new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f), new Color(1.0f, 1.0f, 1.0f, 1.0f) };
        }
        this.corners[corner].r = r;
        this.corners[corner].g = g;
        this.corners[corner].b = b;
    }
    
    public void clampTexture() {
        if (Image.GL.canTextureMirrorClamp()) {
            Image.GL.glTexParameteri(3553, 10242, 34627);
            Image.GL.glTexParameteri(3553, 10243, 34627);
        }
        else {
            Image.GL.glTexParameteri(3553, 10242, 10496);
            Image.GL.glTexParameteri(3553, 10243, 10496);
        }
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Graphics getGraphics() throws SlickException {
        return GraphicsFactory.getGraphicsForImage(this);
    }
    
    private void load(final InputStream in, final String ref, final boolean flipped, final int f, final Color transparent) throws SlickException {
        this.filter = ((f == 1) ? 9729 : 9728);
        try {
            this.ref = ref;
            int[] trans = null;
            if (transparent != null) {
                trans = new int[] { (int)(transparent.r * 255.0f), (int)(transparent.g * 255.0f), (int)(transparent.b * 255.0f) };
            }
            this.texture = InternalTextureLoader.get().getTexture(in, ref, flipped, this.filter, trans);
        }
        catch (IOException e) {
            Log.error(e);
            throw new SlickException("Failed to load image from: " + ref, e);
        }
    }
    
    public void bind() {
        this.texture.bind();
    }
    
    protected void reinit() {
        this.inited = false;
        this.init();
    }
    
    protected final void init() {
        if (this.inited) {
            return;
        }
        this.inited = true;
        if (this.texture != null) {
            this.width = this.texture.getImageWidth();
            this.height = this.texture.getImageHeight();
            this.textureOffsetX = 0.0f;
            this.textureOffsetY = 0.0f;
            this.textureWidth = this.texture.getWidth();
            this.textureHeight = this.texture.getHeight();
        }
        this.initImpl();
        this.centerX = (float)(this.width / 2);
        this.centerY = (float)(this.height / 2);
    }
    
    protected void initImpl() {
    }
    
    public void draw() {
        this.draw(0.0f, 0.0f);
    }
    
    public void drawCentered(final float x, final float y) {
        this.draw(x - this.getWidth() / 2, y - this.getHeight() / 2);
    }
    
    @Override
    public void draw(final float x, final float y) {
        this.init();
        this.draw(x, y, (float)this.width, (float)this.height);
    }
    
    public void draw(final float x, final float y, final Color filter) {
        this.init();
        this.draw(x, y, (float)this.width, (float)this.height, filter);
    }
    
    public void drawEmbedded(final float x, final float y, final float width, final float height) {
        this.init();
        if (this.corners == null) {
            Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
            Image.GL.glVertex3f(x, y, 0.0f);
            Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
            Image.GL.glVertex3f(x, y + height, 0.0f);
            Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
            Image.GL.glVertex3f(x + width, y + height, 0.0f);
            Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
            Image.GL.glVertex3f(x + width, y, 0.0f);
        }
        else {
            this.corners[0].bind();
            Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
            Image.GL.glVertex3f(x, y, 0.0f);
            this.corners[3].bind();
            Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
            Image.GL.glVertex3f(x, y + height, 0.0f);
            this.corners[2].bind();
            Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
            Image.GL.glVertex3f(x + width, y + height, 0.0f);
            this.corners[1].bind();
            Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
            Image.GL.glVertex3f(x + width, y, 0.0f);
        }
    }
    
    public float getTextureOffsetX() {
        this.init();
        return this.textureOffsetX;
    }
    
    public float getTextureOffsetY() {
        this.init();
        return this.textureOffsetY;
    }
    
    public float getTextureWidth() {
        this.init();
        return this.textureWidth;
    }
    
    public float getTextureHeight() {
        this.init();
        return this.textureHeight;
    }
    
    public void draw(final float x, final float y, final float scale) {
        this.init();
        this.draw(x, y, this.width * scale, this.height * scale, Color.white);
    }
    
    public void draw(final float x, final float y, final float scale, final Color filter) {
        this.init();
        this.draw(x, y, this.width * scale, this.height * scale, filter);
    }
    
    public void draw(final float x, final float y, final float width, final float height) {
        this.init();
        this.draw(x, y, width, height, Color.white);
    }
    
    public void drawSheared(final float x, final float y, final float hshear, final float vshear) {
        this.drawSheared(x, y, hshear, vshear, Color.white);
    }
    
    public void drawSheared(final float x, final float y, final float hshear, final float vshear, Color filter) {
        if (this.alpha != 1.0f) {
            if (filter == null) {
                filter = Color.white;
            }
            final Color color;
            filter = (color = new Color(filter));
            color.a *= this.alpha;
        }
        if (filter != null) {
            filter.bind();
        }
        this.texture.bind();
        Image.GL.glTranslatef(x, y, 0.0f);
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glBegin(7);
        this.init();
        Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
        Image.GL.glVertex3f(0.0f, 0.0f, 0.0f);
        Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
        Image.GL.glVertex3f(hshear, (float)this.height, 0.0f);
        Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
        Image.GL.glVertex3f(this.width + hshear, this.height + vshear, 0.0f);
        Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
        Image.GL.glVertex3f((float)this.width, vshear, 0.0f);
        Image.GL.glEnd();
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(-this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glTranslatef(-x, -y, 0.0f);
    }
    
    public void draw(final float x, final float y, final float width, final float height, Color filter) {
        if (this.alpha != 1.0f) {
            if (filter == null) {
                filter = Color.white;
            }
            final Color color;
            filter = (color = new Color(filter));
            color.a *= this.alpha;
        }
        if (filter != null) {
            filter.bind();
        }
        this.texture.bind();
        Image.GL.glTranslatef(x, y, 0.0f);
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glBegin(7);
        this.drawEmbedded(0.0f, 0.0f, width, height);
        Image.GL.glEnd();
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(-this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glTranslatef(-x, -y, 0.0f);
    }
    
    public void drawFlash(final float x, final float y, final float width, final float height) {
        this.drawFlash(x, y, width, height, Color.white);
    }
    
    public void setCenterOfRotation(final float x, final float y) {
        this.centerX = x;
        this.centerY = y;
    }
    
    public float getCenterOfRotationX() {
        this.init();
        return this.centerX;
    }
    
    public float getCenterOfRotationY() {
        this.init();
        return this.centerY;
    }
    
    public void drawFlash(final float x, final float y, final float width, final float height, final Color col) {
        this.init();
        col.bind();
        this.texture.bind();
        if (Image.GL.canSecondaryColor()) {
            Image.GL.glEnable(33880);
            Image.GL.glSecondaryColor3ubEXT((byte)(col.r * 255.0f), (byte)(col.g * 255.0f), (byte)(col.b * 255.0f));
        }
        Image.GL.glTexEnvi(8960, 8704, 8448);
        Image.GL.glTranslatef(x, y, 0.0f);
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glBegin(7);
        this.drawEmbedded(0.0f, 0.0f, width, height);
        Image.GL.glEnd();
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(-this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glTranslatef(-x, -y, 0.0f);
        if (Image.GL.canSecondaryColor()) {
            Image.GL.glDisable(33880);
        }
    }
    
    public void drawFlash(final float x, final float y) {
        this.drawFlash(x, y, (float)this.getWidth(), (float)this.getHeight());
    }
    
    public void setRotation(final float angle) {
        this.angle = angle % 360.0f;
    }
    
    public float getRotation() {
        return this.angle;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
    
    public void rotate(final float angle) {
        this.angle += angle;
        this.angle %= 360.0f;
    }
    
    public Image getSubImage(final int x, final int y, final int width, final int height) {
        this.init();
        final float newTextureOffsetX = x / (float)this.width * this.textureWidth + this.textureOffsetX;
        final float newTextureOffsetY = y / (float)this.height * this.textureHeight + this.textureOffsetY;
        final float newTextureWidth = width / (float)this.width * this.textureWidth;
        final float newTextureHeight = height / (float)this.height * this.textureHeight;
        final Image sub = new Image();
        sub.inited = true;
        sub.texture = this.texture;
        sub.textureOffsetX = newTextureOffsetX;
        sub.textureOffsetY = newTextureOffsetY;
        sub.textureWidth = newTextureWidth;
        sub.textureHeight = newTextureHeight;
        sub.width = width;
        sub.height = height;
        sub.ref = this.ref;
        sub.centerX = (float)(width / 2);
        sub.centerY = (float)(height / 2);
        return sub;
    }
    
    public void draw(final float x, final float y, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        this.draw(x, y, x + this.width, y + this.height, srcx, srcy, srcx2, srcy2);
    }
    
    public void draw(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        this.draw(x, y, x2, y2, srcx, srcy, srcx2, srcy2, Color.white);
    }
    
    public void draw(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2, Color filter) {
        this.init();
        if (this.alpha != 1.0f) {
            if (filter == null) {
                filter = Color.white;
            }
            final Color color;
            filter = (color = new Color(filter));
            color.a *= this.alpha;
        }
        filter.bind();
        this.texture.bind();
        Image.GL.glTranslatef(x, y, 0.0f);
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glBegin(7);
        this.drawEmbedded(0.0f, 0.0f, x2 - x, y2 - y, srcx, srcy, srcx2, srcy2);
        Image.GL.glEnd();
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(-this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glTranslatef(-x, -y, 0.0f);
    }
    
    public void drawEmbedded(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2) {
        this.drawEmbedded(x, y, x2, y2, srcx, srcy, srcx2, srcy2, null);
    }
    
    public void drawEmbedded(final float x, final float y, final float x2, final float y2, final float srcx, final float srcy, final float srcx2, final float srcy2, final Color filter) {
        if (filter != null) {
            filter.bind();
        }
        final float mywidth = x2 - x;
        final float myheight = y2 - y;
        final float texwidth = srcx2 - srcx;
        final float texheight = srcy2 - srcy;
        final float newTextureOffsetX = srcx / this.width * this.textureWidth + this.textureOffsetX;
        final float newTextureOffsetY = srcy / this.height * this.textureHeight + this.textureOffsetY;
        final float newTextureWidth = texwidth / this.width * this.textureWidth;
        final float newTextureHeight = texheight / this.height * this.textureHeight;
        Image.GL.glTexCoord2f(newTextureOffsetX, newTextureOffsetY);
        Image.GL.glVertex3f(x, y, 0.0f);
        Image.GL.glTexCoord2f(newTextureOffsetX, newTextureOffsetY + newTextureHeight);
        Image.GL.glVertex3f(x, y + myheight, 0.0f);
        Image.GL.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY + newTextureHeight);
        Image.GL.glVertex3f(x + mywidth, y + myheight, 0.0f);
        Image.GL.glTexCoord2f(newTextureOffsetX + newTextureWidth, newTextureOffsetY);
        Image.GL.glVertex3f(x + mywidth, y, 0.0f);
    }
    
    public void drawWarped(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3, final float x4, final float y4) {
        Color.white.bind();
        this.texture.bind();
        Image.GL.glTranslatef(x1, y1, 0.0f);
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glBegin(7);
        this.init();
        Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY);
        Image.GL.glVertex3f(0.0f, 0.0f, 0.0f);
        Image.GL.glTexCoord2f(this.textureOffsetX, this.textureOffsetY + this.textureHeight);
        Image.GL.glVertex3f(x2 - x1, y2 - y1, 0.0f);
        Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY + this.textureHeight);
        Image.GL.glVertex3f(x3 - x1, y3 - y1, 0.0f);
        Image.GL.glTexCoord2f(this.textureOffsetX + this.textureWidth, this.textureOffsetY);
        Image.GL.glVertex3f(x4 - x1, y4 - y1, 0.0f);
        Image.GL.glEnd();
        if (this.angle != 0.0f) {
            Image.GL.glTranslatef(this.centerX, this.centerY, 0.0f);
            Image.GL.glRotatef(-this.angle, 0.0f, 0.0f, 1.0f);
            Image.GL.glTranslatef(-this.centerX, -this.centerY, 0.0f);
        }
        Image.GL.glTranslatef(-x1, -y1, 0.0f);
    }
    
    public int getWidth() {
        this.init();
        return this.width;
    }
    
    public int getHeight() {
        this.init();
        return this.height;
    }
    
    public Image copy() {
        this.init();
        return this.getSubImage(0, 0, this.width, this.height);
    }
    
    public Image getScaledCopy(final float scale) {
        this.init();
        return this.getScaledCopy((int)(this.width * scale), (int)(this.height * scale));
    }
    
    public Image getScaledCopy(final int width, final int height) {
        this.init();
        final Image image = this.copy();
        image.width = width;
        image.height = height;
        image.centerX = (float)(width / 2);
        image.centerY = (float)(height / 2);
        return image;
    }
    
    public void ensureInverted() {
        if (this.textureHeight > 0.0f) {
            this.textureOffsetY += this.textureHeight;
            this.textureHeight = -this.textureHeight;
        }
    }
    
    public Image getFlippedCopy(final boolean flipHorizontal, final boolean flipVertical) {
        this.init();
        final Image image = this.copy();
        if (flipHorizontal) {
            image.textureOffsetX = this.textureOffsetX + this.textureWidth;
            image.textureWidth = -this.textureWidth;
        }
        if (flipVertical) {
            image.textureOffsetY = this.textureOffsetY + this.textureHeight;
            image.textureHeight = -this.textureHeight;
        }
        return image;
    }
    
    public void endUse() {
        if (Image.inUse != this) {
            throw new RuntimeException("The sprite sheet is not currently in use");
        }
        Image.inUse = null;
        Image.GL.glEnd();
    }
    
    public void startUse() {
        if (Image.inUse != null) {
            throw new RuntimeException("Attempt to start use of a sprite sheet before ending use with another - see endUse()");
        }
        (Image.inUse = this).init();
        Color.white.bind();
        this.texture.bind();
        Image.GL.glBegin(7);
    }
    
    @Override
    public String toString() {
        this.init();
        return "[Image " + this.ref + " " + this.width + "x" + this.height + "  " + this.textureOffsetX + "," + this.textureOffsetY + "," + this.textureWidth + "," + this.textureHeight + "]";
    }
    
    public Texture getTexture() {
        return this.texture;
    }
    
    public void setTexture(final Texture texture) {
        this.texture = texture;
        this.reinit();
    }
    
    private int translate(final byte b) {
        if (b < 0) {
            return 256 + b;
        }
        return b;
    }
    
    public Color getColor(int x, int y) {
        if (this.pixelData == null) {
            this.pixelData = this.texture.getTextureData();
        }
        final int xo = (int)(this.textureOffsetX * this.texture.getTextureWidth());
        final int yo = (int)(this.textureOffsetY * this.texture.getTextureHeight());
        if (this.textureWidth < 0.0f) {
            x = xo - x;
        }
        else {
            x += xo;
        }
        if (this.textureHeight < 0.0f) {
            y = yo - y;
        }
        else {
            y += yo;
        }
        int offset = x + y * this.texture.getTextureWidth();
        offset *= (this.texture.hasAlpha() ? 4 : 3);
        if (this.texture.hasAlpha()) {
            return new Color(this.translate(this.pixelData[offset]), this.translate(this.pixelData[offset + 1]), this.translate(this.pixelData[offset + 2]), this.translate(this.pixelData[offset + 3]));
        }
        return new Color(this.translate(this.pixelData[offset]), this.translate(this.pixelData[offset + 1]), this.translate(this.pixelData[offset + 2]));
    }
    
    public boolean isDestroyed() {
        return this.destroyed;
    }
    
    public void destroy() throws SlickException {
        if (this.isDestroyed()) {
            return;
        }
        this.destroyed = true;
        this.texture.release();
        GraphicsFactory.releaseGraphicsForImage(this);
    }
    
    public void flushPixelData() {
        this.pixelData = null;
    }
}
