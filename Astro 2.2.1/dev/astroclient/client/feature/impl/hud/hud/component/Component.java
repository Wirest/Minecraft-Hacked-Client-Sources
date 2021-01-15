package dev.astroclient.client.feature.impl.hud.hud.component;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.impl.hud.HUD;
import dev.astroclient.client.property.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Xen for Astro
 * @since 11/10/2019
 **/
public class Component {
    private String name;
    protected float x,y;
    private int width,height;
    private boolean enabled;

    protected Minecraft mc = Minecraft.getMinecraft();

    protected ScaledResolution scaledResolution = new ScaledResolution(mc);

    protected HUD hud = (HUD) Client.INSTANCE.featureManager.get(HUD.class);

    private ArrayList<Property> properties = new ArrayList<>();

    public Component(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void renderPreview() {}

    public void renderReal() {}

    public boolean isActive() {
        return enabled;
    }

    public void setActive(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void add(Property... props) {
        properties.addAll(Arrays.asList(props));
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }
}
