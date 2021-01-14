package info.sigmaclient.gui.screen.component.particles;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ParticleManager {
    private List<Particle> particles = new CopyOnWriteArrayList<>();
    public Minecraft mc = Minecraft.getMinecraft();
    private boolean rightClicked;

    public ParticleManager() {
        getParticles().clear();
    }

    public void render(int x, int y) {
        int rand;
        if (getParticles().size() <= 5000) {
            for (int i = 0; i < 5; i++) {
                rand = random(0, 6);
                if (rand == 1) {
                    getParticles().add(new TopLeftParticle(centerWidth() + random(-getRes().getScaledWidth(), getRes().getScaledWidth()), centerHeight() + random(-getRes().getScaledHeight(), getRes().getScaledHeight()), random(1, 2), 0.35f, random(40, 100)));
                }
                if (rand == 2) {
                    getParticles().add(new GravityParticle(centerWidth() + random(-getRes().getScaledWidth(), getRes().getScaledWidth()), centerHeight() + random(-getRes().getScaledHeight(), getRes().getScaledHeight()), random(1, 3), 0.35f, random(40, 110)));
                }
                if (rand == 3) {
                    getParticles().add(new TopRightParticle(centerWidth() + random(-getRes().getScaledWidth(), getRes().getScaledWidth()), centerHeight() + random(-getRes().getScaledHeight(), getRes().getScaledHeight()), random(1, 3), 0.35f, random(40, 110)));
                }
                if (rand == 4) {
                    getParticles().add(new BottomLeftParticle(centerWidth() + random(-getRes().getScaledWidth(), getRes().getScaledWidth()), centerHeight() + random(-getRes().getScaledHeight(), getRes().getScaledHeight()), random(1, 3), 0.35f, random(40, 110)));
                }
                if (rand == 5) {
                    getParticles().add(new BottomRightParticle(centerWidth() + random(-getRes().getScaledWidth(), getRes().getScaledWidth()), centerHeight() + random(-getRes().getScaledHeight(), getRes().getScaledHeight()), random(1, 3), 0.35f, random(40, 110)));
                }
            }
        }
        for (Particle p : getParticles()) {
            if (p.getAlpha() <= 0.0F) {
                getParticles().remove(p);
            }
            p.render(this);
        }
    }

    public int random(int low, int high) {
        Random r = new Random();
        return r.nextInt(high - low) + low;
    }

    public ScaledResolution getRes() {
        return new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
    }

    public int centerWidth() {
        return getRes().getScaledWidth() / 2;
    }

    public int centerHeight() {
        return getRes().getScaledHeight() / 2;
    }

    public List<Particle> getParticles() {
        return particles;
    }

}