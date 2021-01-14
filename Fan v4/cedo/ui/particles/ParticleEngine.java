package cedo.ui.particles;

import cedo.Fan;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleEngine {
    public CopyOnWriteArrayList<Particle> particles = Lists.newCopyOnWriteArrayList();
    public float lastMouseX;
    public float lastMouseY;

    public static void drawCircle(double x, double y, float radius, int color) {
        float alpha = (color >> 24 & 255) / 255F,
                red = (color >> 16 & 255) / 255F,
                green = (color >> 8 & 255) / 255F,
                blue = (color & 255) / 255F;

        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d((x + Math.sin(i * 3.141526 / 180.0) * radius), (y + Math.cos(i * 3.141526 / 180.0) * radius));
            ++i;
        }
        GL11.glEnd();
    }

    public static void disableRender2D() {
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void enableRender2D() {
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
    }

    public static void setColor(int colorHex) {
        float alpha = (colorHex >> 24 & 0xFF) / 255F,
                red = (colorHex >> 16 & 0xFF) / 255F,
                green = (colorHex >> 8 & 0xFF) / 255F,
                blue = (colorHex & 0xFF) / 255F;

        GL11.glColor4f(red, green, blue, (alpha == 0.0f) ? 1.0f : alpha);
    }

    public static void drawLine(double startX, double startY, double endX, double endY, float thickness, int color) {
        enableRender2D();
        setColor(color);
        GL11.glLineWidth(thickness);
        GL11.glBegin(1);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, endY);
        GL11.glEnd();
        disableRender2D();
    }

    public void render(float mouseX, float mouseY) {
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float xOffset = 0;//sr.getScaledWidth()/2-mouseX;
        float yOffset = 0;//sr.getScaledHeight()/2-mouseY;
        for (; particles.size() < (sr.getScaledWidth() / 8f); particles.add(new Particle(sr, new Random().nextFloat() + 1, new Random().nextFloat() * 5 + 5)))
            ;
        List<Particle> toRemove = Lists.newArrayList();
        int maxOpacity = 52;
        int color = 0xddffffff, line_color; //0xffEEB9FA, line_color;
        int mouseRadius = 100;
        for (Particle particle : particles) {
            double particleX = particle.x + Math.sin(particle.ticks / 2) * 50 + -xOffset / 5;
            double particleY = (particle.ticks * particle.speed) * particle.ticks / 10 + -yOffset / 5;
            if (particleY < sr.getScaledHeight()) {

                if (particle.opacity < maxOpacity) {
                    particle.opacity += 2;
                }
                if (particle.opacity > maxOpacity) {
                    particle.opacity = maxOpacity;
                }
                Color c = new Color(255, 255, 255, (int) particle.opacity);
                //System.out.println(particleY + " "+ sr.getScaledHeight());

                float particle_thickness = 1.0f;
                line_color = new Color(1f, (1 - (float) (particleY / (sr.getScaledHeight()))) / 2f, 1f, 1f).getRGB();
                //drawLine(particleX, particleY, mouseX, mouseY, particle_thickness, line_color);
                GlStateManager.enableBlend();
                drawBorderedCircle(particleX, particleY, particle.radius * (particle.opacity / maxOpacity), color, color);
            }
            particle.ticks += 0.05;
            if ((particleY) > sr.getScaledHeight() || (particleY) < 0 || (particleX) > sr.getScaledWidth() || (particleX) < 0) {
                toRemove.add(particle);
            }
        }

        particles.removeAll(toRemove);
        GlStateManager.color(1, 1, 1, 1);
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        lastMouseX = GLUtils.getMouseX();
        lastMouseY = GLUtils.getMouseY();
    }

    public void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glScalef(0.1f, 0.1f, 0.1f);
        drawCircle(x * 10, y * 10, radius * 10.0f, insideC);
        GL11.glScalef(10.0f, 10.0f, 10.0f);
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
    }

}
