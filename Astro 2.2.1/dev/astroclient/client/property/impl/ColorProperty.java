package dev.astroclient.client.property.impl;

import dev.astroclient.client.property.Property;

import java.awt.*;

/**
* @author Zane for PublicBase
* @since 10/27/19
*/

public class ColorProperty extends Property {

    private String name;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float[] getHSB() {
        return Color.RGBtoHSB(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), null);
    }

    public int[] getRGB() {
        return new int[]{getColor().getRed(), getColor().getGreen(), getColor().getRed()};
    }

    private Color color;

    public ColorProperty(String name, boolean dependency, Color color) {
        super(name, dependency);
        this.color = color;
    }

    public ColorProperty(String name, Color color) {
        super(name);
        this.color = color;
    }
}
