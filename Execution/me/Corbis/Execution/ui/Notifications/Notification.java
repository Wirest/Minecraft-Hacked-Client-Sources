package me.Corbis.Execution.ui.Notifications;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.RenderingUtil;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Notification {
    String message;
    int duration;
    NotificationType type;
    long currentTime = 0;
    static UnicodeFontRenderer ufr;
    int currentLocation = GuiScreen.width;
    int currentLastLocation = GuiScreen.width;
    float lastYloc = indexOf(this) * 42;

    public Notification(String message, int duration, NotificationType type) {
        this.message = message;
        this.duration = duration;
        this.type = type;
    }

    public void draw(){
        int index = indexOf(this);
        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 2,  2);
        }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        currentTime++;

        currentLocation = currentTime > (duration * 50) - 50 ? sr.getScaledWidth() + 10 : sr.getScaledWidth() - (ufr.getStringWidth(message) + 40);

        if(currentLastLocation != currentLocation){
            float diff = currentLocation - currentLastLocation;
            currentLocation = currentLastLocation;
            currentLastLocation += diff / 8;
        }

        if(currentTime > (duration * 50)){
            Execution.instance.notificationManager.notifications.remove(this);
        }

        float yloc = 42 * index;
        if(lastYloc != yloc){
            float diff = yloc - lastYloc;

            lastYloc += diff / 3;
        }

        int progress = (int) (currentTime / duration * 50);
        int hue = 360 * progress;
        int color = Color.HSBtoRGB(hue, 1, 1);


        Gui.drawRect(currentLocation + 20, sr.getScaledHeight() - 42 - (lastYloc), sr.getScaledWidth(), sr.getScaledHeight() - 10- (lastYloc),new Color(255, 255, 255, 255).getRGB());
        ufr.drawString(message,currentLocation + 25, sr.getScaledHeight() - 33 - (lastYloc), new Color(0, 0,0, 255).getRGB());
    //    Gui.drawRect(currentLocation + 20, sr.getScaledHeight() - 10- (lastYloc) - 2, (currentLocation + 20) + (sr.getScaledWidth() / progress), sr.getScaledHeight() - 10- (lastYloc), color);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public int indexOf(Object o){
        if (o == null) {
            for (int i = 0; i < Execution.instance.notificationManager.notifications.size(); i++)
                if (Execution.instance.notificationManager.notifications.toArray()[i]==null)
                    return i;
        } else {
            for (int i = 0; i < Execution.instance.notificationManager.notifications.size(); i++)
                if (o.equals(Execution.instance.notificationManager.notifications.toArray()[i]))
                    return i;
        }
        return -1;
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
