// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.particles;

import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.renderer.SGL;

public class Particle
{
    protected static SGL GL;
    public static final int INHERIT_POINTS = 1;
    public static final int USE_POINTS = 2;
    public static final int USE_QUADS = 3;
    protected float x;
    protected float y;
    protected float velx;
    protected float vely;
    protected float size;
    protected Color color;
    protected float life;
    protected float originalLife;
    private ParticleSystem engine;
    private ParticleEmitter emitter;
    protected Image image;
    protected int type;
    protected int usePoints;
    protected boolean oriented;
    protected float scaleY;
    
    static {
        Particle.GL = Renderer.get();
    }
    
    public Particle(final ParticleSystem engine) {
        this.size = 10.0f;
        this.color = Color.white;
        this.usePoints = 1;
        this.oriented = false;
        this.scaleY = 1.0f;
        this.engine = engine;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void move(final float x, final float y) {
        this.x += x;
        this.y += y;
    }
    
    public float getSize() {
        return this.size;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setImage(final Image image) {
        this.image = image;
    }
    
    public float getOriginalLife() {
        return this.originalLife;
    }
    
    public float getLife() {
        return this.life;
    }
    
    public boolean inUse() {
        return this.life > 0.0f;
    }
    
    public void render() {
        if ((this.engine.usePoints() && this.usePoints == 1) || this.usePoints == 2) {
            TextureImpl.bindNone();
            Particle.GL.glEnable(2832);
            Particle.GL.glPointSize(this.size / 2.0f);
            this.color.bind();
            Particle.GL.glBegin(0);
            Particle.GL.glVertex2f(this.x, this.y);
            Particle.GL.glEnd();
        }
        else if (this.oriented || this.scaleY != 1.0f) {
            Particle.GL.glPushMatrix();
            Particle.GL.glTranslatef(this.x, this.y, 0.0f);
            if (this.oriented) {
                final float angle = (float)(Math.atan2(this.y, this.x) * 180.0 / 3.141592653589793);
                Particle.GL.glRotatef(angle, 0.0f, 0.0f, 1.0f);
            }
            Particle.GL.glScalef(1.0f, this.scaleY, 1.0f);
            this.image.draw((float)(int)(-(this.size / 2.0f)), (float)(int)(-(this.size / 2.0f)), (float)(int)this.size, (float)(int)this.size, this.color);
            Particle.GL.glPopMatrix();
        }
        else {
            this.color.bind();
            this.image.drawEmbedded((float)(int)(this.x - this.size / 2.0f), (float)(int)(this.y - this.size / 2.0f), (float)(int)this.size, (float)(int)this.size);
        }
    }
    
    public void update(final int delta) {
        this.emitter.updateParticle(this, delta);
        this.life -= delta;
        if (this.life > 0.0f) {
            this.x += delta * this.velx;
            this.y += delta * this.vely;
        }
        else {
            this.engine.release(this);
        }
    }
    
    public void init(final ParticleEmitter emitter, final float life) {
        this.x = 0.0f;
        this.emitter = emitter;
        this.y = 0.0f;
        this.velx = 0.0f;
        this.vely = 0.0f;
        this.size = 10.0f;
        this.type = 0;
        this.life = life;
        this.originalLife = life;
        this.oriented = false;
        this.scaleY = 1.0f;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setUsePoint(final int usePoints) {
        this.usePoints = usePoints;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setSize(final float size) {
        this.size = size;
    }
    
    public void adjustSize(final float delta) {
        this.size += delta;
        this.size = Math.max(0.0f, this.size);
    }
    
    public void setLife(final float life) {
        this.life = life;
    }
    
    public void adjustLife(final float delta) {
        this.life += delta;
    }
    
    public void kill() {
        this.life = 1.0f;
    }
    
    public void setColor(final float r, final float g, final float b, final float a) {
        if (this.color == Color.white) {
            this.color = new Color(r, g, b, a);
        }
        else {
            this.color.r = r;
            this.color.g = g;
            this.color.b = b;
            this.color.a = a;
        }
    }
    
    public void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public void setVelocity(final float dirx, final float diry, final float speed) {
        this.velx = dirx * speed;
        this.vely = diry * speed;
    }
    
    public void setSpeed(final float speed) {
        final float currentSpeed = (float)Math.sqrt(this.velx * this.velx + this.vely * this.vely);
        this.velx *= speed;
        this.vely *= speed;
        this.velx /= currentSpeed;
        this.vely /= currentSpeed;
    }
    
    public void setVelocity(final float velx, final float vely) {
        this.setVelocity(velx, vely, 1.0f);
    }
    
    public void adjustPosition(final float dx, final float dy) {
        this.x += dx;
        this.y += dy;
    }
    
    public void adjustColor(final float r, final float g, final float b, final float a) {
        if (this.color == Color.white) {
            this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        final Color color = this.color;
        color.r += r;
        final Color color2 = this.color;
        color2.g += g;
        final Color color3 = this.color;
        color3.b += b;
        final Color color4 = this.color;
        color4.a += a;
    }
    
    public void adjustColor(final int r, final int g, final int b, final int a) {
        if (this.color == Color.white) {
            this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        }
        final Color color = this.color;
        color.r += r / 255.0f;
        final Color color2 = this.color;
        color2.g += g / 255.0f;
        final Color color3 = this.color;
        color3.b += b / 255.0f;
        final Color color4 = this.color;
        color4.a += a / 255.0f;
    }
    
    public void adjustVelocity(final float dx, final float dy) {
        this.velx += dx;
        this.vely += dy;
    }
    
    public ParticleEmitter getEmitter() {
        return this.emitter;
    }
    
    @Override
    public String toString() {
        return String.valueOf(super.toString()) + " : " + this.life;
    }
    
    public boolean isOriented() {
        return this.oriented;
    }
    
    public void setOriented(final boolean oriented) {
        this.oriented = oriented;
    }
    
    public float getScaleY() {
        return this.scaleY;
    }
    
    public void setScaleY(final float scaleY) {
        this.scaleY = scaleY;
    }
}
