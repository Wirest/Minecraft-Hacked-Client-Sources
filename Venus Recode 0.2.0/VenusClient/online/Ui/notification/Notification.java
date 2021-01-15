package VenusClient.online.Ui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

import VenusClient.online.Client;
import VenusClient.online.Utils.RenderUtil;
import VenusClient.online.Utils.TimeHelper;

public class Notification {
    private TimeHelper timer;
    private static Minecraft mc = Minecraft.getMinecraft();
    private float x, oldX, y, oldY, width;
    private String text;
    private int stayTime;
    private boolean done;
    private float stayBar;

    Notification(String text, int stayTime) {
        this.x = new ScaledResolution(mc).getScaledWidth() - 2;
        this.y = new ScaledResolution(mc).getScaledHeight() - 2;
        this.text = text;
        this.stayTime = stayTime;
        timer = new TimeHelper();
        timer.reset();
        stayBar = stayTime;
        done = false;
    }

    public void draw(float prevY) {
        final float xSpeed = width / (Minecraft.getDebugFPS() / 8);
        final float ySpeed = (new ScaledResolution(mc).getScaledHeight() - prevY) / (Minecraft.getDebugFPS() / 8);{
            if (width != mc.fontRendererObj.getStringWidth(text) + 8) {
                width = mc.fontRendererObj.getStringWidth(text) + 8;
            }
        }
        if (done) {
            oldX = x;
            x += xSpeed;
            y += ySpeed;
        }
        if (!done() && !done) {
            timer.reset();
            oldX = x;
            if (x <= new ScaledResolution(mc).getScaledWidth() - 2 - width + xSpeed)
                x = new ScaledResolution(mc).getScaledWidth() - 2 - width;
            else x -= xSpeed;
        } else if (timer.hasReached(stayTime)) done = true;
        if (x < new ScaledResolution(mc).getScaledWidth() - 2 - width) {
            oldX = x;
            x += xSpeed;
        }
        if (y != prevY) {
            if (y != prevY) {
                if (y > prevY + ySpeed) {
                    y -= ySpeed;
                } else {
                    y = prevY;
                }
            } else if (y < prevY) {
                oldY = y;
                y += ySpeed;
            }
        }
        if (done() && !done) {
            stayBar = timer.getDelay();
        }
        final float finishedX = oldX + (x - oldX);
        final float finishedY = oldY + (y - oldY);
        RenderUtil.drawBorderedRect(finishedX, finishedY, width, 24, 0.5, new Color(0, 0, 0, 180).getRGB(), new Color(0, 0, 0, 255).getRGB());
            mc.fontRendererObj.drawStringWithShadow(text, finishedX + 5, finishedY + 9, -1);
        if (done())
            RenderUtil.drawRect(finishedX, finishedY, ((width - 1) / stayTime) * stayBar, 1, 0);
        if (delete()) Client.instance.notificationManager.getNotifications().remove(this);
    }

    public boolean done() {
        return x <= new ScaledResolution(mc).getScaledWidth() - 2 - width;
    }

    public boolean delete() {
        return x >= new ScaledResolution(mc).getScaledWidth() - 2 && done;
    }
}
