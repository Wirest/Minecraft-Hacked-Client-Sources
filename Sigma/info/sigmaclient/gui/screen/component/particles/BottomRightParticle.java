package info.sigmaclient.gui.screen.component.particles;

import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;

/**
 * Created by Arithmo on 5/10/2017 at 9:06 PM.
 */
public class BottomRightParticle extends Particle {

    BottomRightParticle(float posX, float posY, float size, float speed, float alpha) {
        super(posX, posY, size, speed, alpha);
    }

    public void render(ParticleManager p) {
        super.render(p);
        setPosY(getPosY() - getSpeed());
        setPosX(getPosX() + getSpeed());
        RenderingUtil.drawFullCircle(getPosX(), getPosY(), getSize(), Colors.getColor(255, 255, 255, (int) getAlpha()));
    }

}
