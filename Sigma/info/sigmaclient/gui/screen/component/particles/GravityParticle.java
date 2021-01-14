package info.sigmaclient.gui.screen.component.particles;

import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;

/**
 * Created by Arithmo on 5/10/2017 at 9:03 PM.
 */
public class GravityParticle extends Particle {

    public GravityParticle(float posX, float posY, float size, float speed, float alpha) {
        super(posX, posY, size, speed, alpha);
    }

    public void render(ParticleManager p) {
        super.render(p);
        setPosY(getPosY() + getSpeed());
        RenderingUtil.drawFullCircle(getPosX(), getPosY(), getSize(), Colors.getColor(255, 255, 255, (int) getAlpha()));
    }

}
