package info.sigmaclient.gui.screen.component.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Random;

/**
 * Created by Arithmo on 5/10/2017 at 9:01 PM.
 */
public class Particle {
    private float posX;
    private float posY;
    private float alpha;
    private float size;
    private float speed;
    public Minecraft mc = Minecraft.getMinecraft();

    public Particle(float posX, float posY, float size, float speed, float alpha) {
        setPosX(posX);
        setPosY(posY);
        setSize(size);
        setSpeed(speed);
        setAlpha(alpha);
    }

    public int random(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public ScaledResolution getRes() {
        return new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    }

    public void render(ParticleManager p) {
    	setAlpha(getAlpha() - 0.3F);
    	if(getAlpha() < 0)
    		setAlpha(0);
    }

    public float getPosX() {
        return this.posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return this.posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getSize() {
        return this.size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
