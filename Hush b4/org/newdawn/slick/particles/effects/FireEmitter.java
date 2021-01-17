// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.particles.effects;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.ParticleEmitter;

public class FireEmitter implements ParticleEmitter
{
    private int x;
    private int y;
    private int interval;
    private int timer;
    private float size;
    
    public FireEmitter() {
        this.interval = 50;
        this.size = 40.0f;
    }
    
    public FireEmitter(final int x, final int y) {
        this.interval = 50;
        this.size = 40.0f;
        this.x = x;
        this.y = y;
    }
    
    public FireEmitter(final int x, final int y, final float size) {
        this.interval = 50;
        this.size = 40.0f;
        this.x = x;
        this.y = y;
        this.size = size;
    }
    
    @Override
    public void update(final ParticleSystem system, final int delta) {
        this.timer -= delta;
        if (this.timer <= 0) {
            this.timer = this.interval;
            final Particle p = system.getNewParticle(this, 1000.0f);
            p.setColor(1.0f, 1.0f, 1.0f, 0.5f);
            p.setPosition((float)this.x, (float)this.y);
            p.setSize(this.size);
            final float vx = (float)(-0.019999999552965164 + Math.random() * 0.03999999910593033);
            final float vy = (float)(-(Math.random() * 0.15000000596046448));
            p.setVelocity(vx, vy, 1.1f);
        }
    }
    
    @Override
    public void updateParticle(final Particle particle, final int delta) {
        if (particle.getLife() > 600.0f) {
            particle.adjustSize(0.07f * delta);
        }
        else {
            particle.adjustSize(-0.04f * delta * (this.size / 40.0f));
        }
        final float c = 0.002f * delta;
        particle.adjustColor(0.0f, -c / 2.0f, -c * 2.0f, -c / 4.0f);
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
    }
    
    @Override
    public boolean completed() {
        return false;
    }
    
    @Override
    public boolean useAdditive() {
        return false;
    }
    
    @Override
    public Image getImage() {
        return null;
    }
    
    @Override
    public boolean usePoints(final ParticleSystem system) {
        return false;
    }
    
    @Override
    public boolean isOriented() {
        return false;
    }
    
    @Override
    public void wrapUp() {
    }
    
    @Override
    public void resetState() {
    }
}
