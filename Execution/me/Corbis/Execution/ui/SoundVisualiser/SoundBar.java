package me.Corbis.Execution.ui.SoundVisualiser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class SoundBar {

    int index;
    float volume;

    public SoundBar(int index, float volume) {
        this.index = index;
        this.volume = volume;
    }

    public void draw(){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Gui.drawRect(5 * index + 1, sr.getScaledHeight() + volume * 80, 5 * index + 5, sr.getScaledHeight(), new Color(200, 200, 200, 150).getRGB());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
