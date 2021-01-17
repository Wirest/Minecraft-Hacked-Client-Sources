// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.particles;

import org.newdawn.slick.Image;

public interface ParticleEmitter
{
    void update(final ParticleSystem p0, final int p1);
    
    boolean completed();
    
    void wrapUp();
    
    void updateParticle(final Particle p0, final int p1);
    
    boolean isEnabled();
    
    void setEnabled(final boolean p0);
    
    boolean useAdditive();
    
    Image getImage();
    
    boolean isOriented();
    
    boolean usePoints(final ParticleSystem p0);
    
    void resetState();
}
