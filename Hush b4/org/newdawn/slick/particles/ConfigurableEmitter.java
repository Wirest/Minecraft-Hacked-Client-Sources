// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.particles;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.newdawn.slick.util.FastTrig;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import java.util.ArrayList;

public class ConfigurableEmitter implements ParticleEmitter
{
    private static String relativePath;
    public Range spawnInterval;
    public Range spawnCount;
    public Range initialLife;
    public Range initialSize;
    public Range xOffset;
    public Range yOffset;
    public RandomValue spread;
    public SimpleValue angularOffset;
    public Range initialDistance;
    public Range speed;
    public SimpleValue growthFactor;
    public SimpleValue gravityFactor;
    public SimpleValue windFactor;
    public Range length;
    public ArrayList colors;
    public SimpleValue startAlpha;
    public SimpleValue endAlpha;
    public LinearInterpolator alpha;
    public LinearInterpolator size;
    public LinearInterpolator velocity;
    public LinearInterpolator scaleY;
    public Range emitCount;
    public int usePoints;
    public boolean useOriented;
    public boolean useAdditive;
    public String name;
    public String imageName;
    private Image image;
    private boolean updateImage;
    private boolean enabled;
    private float x;
    private float y;
    private int nextSpawn;
    private int timeout;
    private int particleCount;
    private ParticleSystem engine;
    private int leftToEmit;
    protected boolean wrapUp;
    protected boolean completed;
    protected boolean adjust;
    protected float adjustx;
    protected float adjusty;
    
    static {
        ConfigurableEmitter.relativePath = "";
    }
    
    public static void setRelativePath(String path) {
        if (!path.endsWith("/")) {
            path = String.valueOf(path) + "/";
        }
        ConfigurableEmitter.relativePath = path;
    }
    
    public ConfigurableEmitter(final String name) {
        this.spawnInterval = new Range(100.0f, 100.0f, (Range)null);
        this.spawnCount = new Range(5.0f, 5.0f, (Range)null);
        this.initialLife = new Range(1000.0f, 1000.0f, (Range)null);
        this.initialSize = new Range(10.0f, 10.0f, (Range)null);
        this.xOffset = new Range(0.0f, 0.0f, (Range)null);
        this.yOffset = new Range(0.0f, 0.0f, (Range)null);
        this.spread = new RandomValue(360.0f, (RandomValue)null);
        this.angularOffset = new SimpleValue(0.0f, (SimpleValue)null);
        this.initialDistance = new Range(0.0f, 0.0f, (Range)null);
        this.speed = new Range(50.0f, 50.0f, (Range)null);
        this.growthFactor = new SimpleValue(0.0f, (SimpleValue)null);
        this.gravityFactor = new SimpleValue(0.0f, (SimpleValue)null);
        this.windFactor = new SimpleValue(0.0f, (SimpleValue)null);
        this.length = new Range(1000.0f, 1000.0f, (Range)null);
        this.colors = new ArrayList();
        this.startAlpha = new SimpleValue(255.0f, (SimpleValue)null);
        this.endAlpha = new SimpleValue(0.0f, (SimpleValue)null);
        this.emitCount = new Range(1000.0f, 1000.0f, (Range)null);
        this.usePoints = 1;
        this.useOriented = false;
        this.useAdditive = false;
        this.imageName = "";
        this.enabled = true;
        this.nextSpawn = 0;
        this.wrapUp = false;
        this.completed = false;
        this.name = name;
        this.leftToEmit = (int)this.emitCount.random();
        this.timeout = (int)this.length.random();
        this.colors.add(new ColorRecord(0.0f, Color.white));
        this.colors.add(new ColorRecord(1.0f, Color.red));
        ArrayList curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 255.0f));
        this.alpha = new LinearInterpolator(curve, 0, 255);
        curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 255.0f));
        this.size = new LinearInterpolator(curve, 0, 255);
        curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 1.0f));
        this.velocity = new LinearInterpolator(curve, 0, 1);
        curve = new ArrayList();
        curve.add(new Vector2f(0.0f, 0.0f));
        curve.add(new Vector2f(1.0f, 1.0f));
        this.scaleY = new LinearInterpolator(curve, 0, 1);
    }
    
    public void setImageName(String imageName) {
        if (imageName.length() == 0) {
            imageName = null;
        }
        if ((this.imageName = imageName) == null) {
            this.image = null;
        }
        else {
            this.updateImage = true;
        }
    }
    
    public String getImageName() {
        return this.imageName;
    }
    
    @Override
    public String toString() {
        return "[" + this.name + "]";
    }
    
    public void setPosition(final float x, final float y) {
        this.setPosition(x, y, true);
    }
    
    public void setPosition(final float x, final float y, final boolean moveParticles) {
        if (moveParticles) {
            this.adjust = true;
            this.adjustx -= this.x - x;
            this.adjusty -= this.y - y;
        }
        this.x = x;
        this.y = y;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void update(final ParticleSystem system, final int delta) {
        this.engine = system;
        if (!this.adjust) {
            this.adjustx = 0.0f;
            this.adjusty = 0.0f;
        }
        else {
            this.adjust = false;
        }
        if (this.updateImage) {
            this.updateImage = false;
            try {
                this.image = new Image(String.valueOf(ConfigurableEmitter.relativePath) + this.imageName);
            }
            catch (SlickException e) {
                this.image = null;
                Log.error(e);
            }
        }
        if ((this.wrapUp || (this.length.isEnabled() && this.timeout < 0) || (this.emitCount.isEnabled() && this.leftToEmit <= 0)) && this.particleCount == 0) {
            this.completed = true;
        }
        this.particleCount = 0;
        if (this.wrapUp) {
            return;
        }
        if (this.length.isEnabled()) {
            if (this.timeout < 0) {
                return;
            }
            this.timeout -= delta;
        }
        if (this.emitCount.isEnabled() && this.leftToEmit <= 0) {
            return;
        }
        this.nextSpawn -= delta;
        if (this.nextSpawn < 0) {
            this.nextSpawn = (int)this.spawnInterval.random();
            for (int count = (int)this.spawnCount.random(), i = 0; i < count; ++i) {
                final Particle p = system.getNewParticle(this, this.initialLife.random());
                p.setSize(this.initialSize.random());
                p.setPosition(this.x + this.xOffset.random(), this.y + this.yOffset.random());
                p.setVelocity(0.0f, 0.0f, 0.0f);
                final float dist = this.initialDistance.random();
                final float power = this.speed.random();
                if (dist != 0.0f || power != 0.0f) {
                    final float s = this.spread.getValue(0.0f);
                    final float ang = s + this.angularOffset.getValue(0.0f) - this.spread.getValue() / 2.0f - 90.0f;
                    final float xa = (float)FastTrig.cos(Math.toRadians(ang)) * dist;
                    final float ya = (float)FastTrig.sin(Math.toRadians(ang)) * dist;
                    p.adjustPosition(xa, ya);
                    final float xv = (float)FastTrig.cos(Math.toRadians(ang));
                    final float yv = (float)FastTrig.sin(Math.toRadians(ang));
                    p.setVelocity(xv, yv, power * 0.001f);
                }
                if (this.image != null) {
                    p.setImage(this.image);
                }
                final ColorRecord start = this.colors.get(0);
                p.setColor(start.col.r, start.col.g, start.col.b, this.startAlpha.getValue(0.0f) / 255.0f);
                p.setUsePoint(this.usePoints);
                p.setOriented(this.useOriented);
                if (this.emitCount.isEnabled()) {
                    --this.leftToEmit;
                    if (this.leftToEmit <= 0) {
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public void updateParticle(final Particle particle, final int delta) {
        ++this.particleCount;
        particle.x += this.adjustx;
        particle.y += this.adjusty;
        particle.adjustVelocity(this.windFactor.getValue(0.0f) * 5.0E-5f * delta, this.gravityFactor.getValue(0.0f) * 5.0E-5f * delta);
        final float offset = particle.getLife() / particle.getOriginalLife();
        final float inv = 1.0f - offset;
        float colOffset = 0.0f;
        float colInv = 1.0f;
        Color startColor = null;
        Color endColor = null;
        for (int i = 0; i < this.colors.size() - 1; ++i) {
            final ColorRecord rec1 = this.colors.get(i);
            final ColorRecord rec2 = this.colors.get(i + 1);
            if (inv >= rec1.pos && inv <= rec2.pos) {
                startColor = rec1.col;
                endColor = rec2.col;
                final float step = rec2.pos - rec1.pos;
                colOffset = inv - rec1.pos;
                colOffset /= step;
                colOffset = 1.0f - colOffset;
                colInv = 1.0f - colOffset;
            }
        }
        if (startColor != null) {
            final float r = startColor.r * colOffset + endColor.r * colInv;
            final float g = startColor.g * colOffset + endColor.g * colInv;
            final float b = startColor.b * colOffset + endColor.b * colInv;
            float a;
            if (this.alpha.isActive()) {
                a = this.alpha.getValue(inv) / 255.0f;
            }
            else {
                a = this.startAlpha.getValue(0.0f) / 255.0f * offset + this.endAlpha.getValue(0.0f) / 255.0f * inv;
            }
            particle.setColor(r, g, b, a);
        }
        if (this.size.isActive()) {
            final float s = this.size.getValue(inv);
            particle.setSize(s);
        }
        else {
            particle.adjustSize(delta * this.growthFactor.getValue(0.0f) * 0.001f);
        }
        if (this.velocity.isActive()) {
            particle.setSpeed(this.velocity.getValue(inv));
        }
        if (this.scaleY.isActive()) {
            particle.setScaleY(this.scaleY.getValue(inv));
        }
    }
    
    @Override
    public boolean completed() {
        if (this.engine == null) {
            return false;
        }
        if (this.length.isEnabled()) {
            return this.timeout <= 0 && this.completed;
        }
        if (this.emitCount.isEnabled()) {
            return this.leftToEmit <= 0 && this.completed;
        }
        return this.wrapUp && this.completed;
    }
    
    public void replay() {
        this.reset();
        this.nextSpawn = 0;
        this.leftToEmit = (int)this.emitCount.random();
        this.timeout = (int)this.length.random();
    }
    
    public void reset() {
        this.completed = false;
        if (this.engine != null) {
            this.engine.releaseAll(this);
        }
    }
    
    public void replayCheck() {
        if (this.completed() && this.engine != null && this.engine.getParticleCount() == 0) {
            this.replay();
        }
    }
    
    public ConfigurableEmitter duplicate() {
        ConfigurableEmitter theCopy = null;
        try {
            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ParticleIO.saveEmitter(bout, this);
            final ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            theCopy = ParticleIO.loadEmitter(bin);
        }
        catch (IOException e) {
            Log.error("Slick: ConfigurableEmitter.duplicate(): caught exception " + e.toString());
            return null;
        }
        return theCopy;
    }
    
    public void addColorPoint(final float pos, final Color col) {
        this.colors.add(new ColorRecord(pos, col));
    }
    
    @Override
    public boolean useAdditive() {
        return this.useAdditive;
    }
    
    @Override
    public boolean isOriented() {
        return this.useOriented;
    }
    
    @Override
    public boolean usePoints(final ParticleSystem system) {
        return (this.usePoints == 1 && system.usePoints()) || this.usePoints == 2;
    }
    
    @Override
    public Image getImage() {
        return this.image;
    }
    
    @Override
    public void wrapUp() {
        this.wrapUp = true;
    }
    
    @Override
    public void resetState() {
        this.wrapUp = false;
        this.replay();
    }
    
    public class SimpleValue implements Value
    {
        private float value;
        private float next;
        
        private SimpleValue(final float value) {
            this.value = value;
        }
        
        @Override
        public float getValue(final float time) {
            return this.value;
        }
        
        public void setValue(final float value) {
            this.value = value;
        }
    }
    
    public class RandomValue implements Value
    {
        private float value;
        
        private RandomValue(final float value) {
            this.value = value;
        }
        
        @Override
        public float getValue(final float time) {
            return (float)(Math.random() * this.value);
        }
        
        public void setValue(final float value) {
            this.value = value;
        }
        
        public float getValue() {
            return this.value;
        }
    }
    
    public class LinearInterpolator implements Value
    {
        private ArrayList curve;
        private boolean active;
        private int min;
        private int max;
        
        public LinearInterpolator(final ArrayList curve, final int min, final int max) {
            this.curve = curve;
            this.min = min;
            this.max = max;
            this.active = false;
        }
        
        public void setCurve(final ArrayList curve) {
            this.curve = curve;
        }
        
        public ArrayList getCurve() {
            return this.curve;
        }
        
        @Override
        public float getValue(final float t) {
            Vector2f p0 = this.curve.get(0);
            for (int i = 1; i < this.curve.size(); ++i) {
                final Vector2f p2 = this.curve.get(i);
                if (t >= p0.getX() && t <= p2.getX()) {
                    final float st = (t - p0.getX()) / (p2.getX() - p0.getX());
                    final float r = p0.getY() + st * (p2.getY() - p0.getY());
                    return r;
                }
                p0 = p2;
            }
            return 0.0f;
        }
        
        public boolean isActive() {
            return this.active;
        }
        
        public void setActive(final boolean active) {
            this.active = active;
        }
        
        public int getMax() {
            return this.max;
        }
        
        public void setMax(final int max) {
            this.max = max;
        }
        
        public int getMin() {
            return this.min;
        }
        
        public void setMin(final int min) {
            this.min = min;
        }
    }
    
    public class ColorRecord
    {
        public float pos;
        public Color col;
        
        public ColorRecord(final float pos, final Color col) {
            this.pos = pos;
            this.col = col;
        }
    }
    
    public class Range
    {
        private float max;
        private float min;
        private boolean enabled;
        
        private Range(final float min, final float max) {
            this.enabled = false;
            this.min = min;
            this.max = max;
        }
        
        public float random() {
            return (float)(this.min + Math.random() * (this.max - this.min));
        }
        
        public boolean isEnabled() {
            return this.enabled;
        }
        
        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }
        
        public float getMax() {
            return this.max;
        }
        
        public void setMax(final float max) {
            this.max = max;
        }
        
        public float getMin() {
            return this.min;
        }
        
        public void setMin(final float min) {
            this.min = min;
        }
    }
    
    public interface Value
    {
        float getValue(final float p0);
    }
}
