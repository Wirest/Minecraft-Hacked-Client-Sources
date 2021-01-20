package cedo.util.render;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircleManager {

    static Minecraft mc = Minecraft.getMinecraft();
    public List<Circle> circles = new ArrayList<Circle>();
    public boolean wasHeld;

    public void addCircle(double x, double y, double rad, double speed, int key) {
        circles.add(new Circle(x, y, rad, speed, key));
    }

    public void runCircle(Circle c) {
        //if(c.progress < 2){


        c.lastProgress = c.progress;
        if (c.keyCode != mc.gameSettings.keyBindAttack.getKeyCode() && c.keyCode != mc.gameSettings.keyBindUseItem.getKeyCode() && c.progress > c.topRadius * 0.67 && Keyboard.isKeyDown(c.keyCode))
            return;

        c.progress += (c.topRadius - c.progress) / (c.speed) + 0.01;
        if (c.progress >= c.topRadius) {
            c.complete = true;
        }
        //}
    }

    public void runCircles() {
        List<Circle> completes = new ArrayList<Circle>();
        for (Circle c : circles) {
            if (!c.complete) {
                runCircle(c);
            } else {
                completes.add(c);
            }
        }
        synchronized (circles) {
            circles.removeAll(completes);
        }
    }

    public void drawCircles() {
        for (Circle c : circles) {
            if (!c.complete)
                drawCircle(c);
        }
    }

    public void drawCircle(Circle c) {
        float progress = (float) (c.progress * mc.timer.renderPartialTicks + (c.lastProgress * (1.0f - mc.timer.renderPartialTicks)));
        if (!c.complete)

            RenderUtil.drawBorderedCircle((int) c.x, (int) c.y, progress, new Color(1f, 1f, 1f, (1 - Math.min(1f, Math.max(0f, (float) (progress / c.topRadius)))) / 2).getRGB(), new Color(1f, 1f, 1f, (1 - Math.min(1f, Math.max(0f, (float) (progress / c.topRadius)))) / 2).getRGB());
        //RenderingUtil.drawCircle((int)c.x, (int)c.y, progress, new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB(), new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB());
    }

}

