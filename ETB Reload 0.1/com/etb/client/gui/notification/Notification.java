package com.etb.client.gui.notification;

import java.awt.Color;

import com.etb.client.utils.RenderUtil;
import com.etb.client.utils.TimerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class Notification {
    private String message;
    private TimerUtil timer;
    private double lastY, posY, width, height, animationX;
    private int color = new Color(38, 46, 52, 255).getRGB(), imageWidth;
    private ResourceLocation image;
    private long stayTime;

    public Notification(String message, Type type) {
        this.message = message;
        timer = new TimerUtil();
        timer.reset();
        width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(message) + 35;
        height = 20;
        animationX = width;
        stayTime = 2000;
        imageWidth = 13;
        posY = -1;
        image = new ResourceLocation("textures/client/icon/" + type.name().toLowerCase() + ".png");
    }

    public void draw(double getY, double lastY) {
        this.lastY = lastY;
        animationX = getAnimationState(animationX, isFinished() ? width : 0, Math.max(isFinished() ? 200 : 30, Math.abs(animationX - (isFinished() ? width : 0)) * 5));
        if(posY == -1)
            posY = getY;
        else
            posY = getAnimationState(posY, getY, 200);
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int x1 = (int) (res.getScaledWidth() - width + animationX), x2 = (int) (res.getScaledWidth() + animationX), y1 = (int) posY, y2 = (int) (y1 + height);
        Gui.drawRect(x1, y1, x2, y2, color);
        Gui.drawRect(x1, y2, x2, y2 + 0.5F,  new Color(38, 46, 52, 255).getRGB());
        Gui.drawRect(x1, y2, x2, y2 + 0.5F,  new Color(26, 31, 41, 255).getRGB());

        Gui.drawRect(x1, y1, (int) (x1 + height), y2, RenderUtil.reAlpha(-1, 0.1F));
        RenderUtil.drawImage(image, (int)(x1 + (height - imageWidth) / 2F), y1 + (int)((height - imageWidth) / 2F), imageWidth, imageWidth);

        Minecraft.getMinecraft().fontRendererObj.drawCenteredString(message, (float)(x1 + width / 2F) + 10, (float)(y1 + height / 3.5F), -1);
    }

    private double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        if (animation < finalState) {
            if (animation + add < finalState)
                animation += add;
            else
                animation = finalState;
        } else {
            if (animation - add > finalState)
                animation -= add;
            else
                animation = finalState;
        }
        return animation;
    }


    public boolean shouldDelete() {
        return isFinished() && animationX >= width;
    }

    private boolean isFinished() {
        return timer.reach(stayTime) && posY == lastY;
    }

    public double getHeight() {
        return height;
    }

    public enum Type {
        SUCCESS, INFO, WARNING, ERROR
    }
}
