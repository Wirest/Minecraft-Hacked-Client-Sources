package dev.astroclient.client.ui.menu.click.component.components;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.property.Property;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.ui.menu.click.component.Category;
import dev.astroclient.client.ui.menu.click.component.Component;
import dev.astroclient.client.ui.menu.click.component.components.buttons.*;
import dev.astroclient.client.ui.menu.click.panel.MainPanel;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.StringProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public class GroupBox extends Category {
    public List<Component> components = new ArrayList<>();
    private Feature module;
    private float scrollY;

    public GroupBox(dev.astroclient.client.feature.Category tab, MainPanel parent, String label, float posX, float posY, float width, float height) {
        super(tab, parent, label, posX, posY, width, height);
    }

    public GroupBox(dev.astroclient.client.feature.Category tab, MainPanel parent, float posX, float posY, float width, float height, Feature module) {
        super(tab, parent, module.getLabel(), posX, posY, width, height);
        this.module = module;
    }

    public void init() {
        if (module instanceof ToggleableFeature) {
            getComponents().add(new EnabledButton(this, "Enabled", 8, 10, 100, 8, (ToggleableFeature) module));
            getComponents().add(new HiddenButton(this, "Hidden", 8, 18, 100, 8, (ToggleableFeature) module));
            getComponents().add(new KeybindButton(this, "Keybind", 115, 14, 100, 8, (ToggleableFeature) module));
        }

        float valY = getPosY() + 27;
        for (Property value : Client.INSTANCE.propertyManager.getPropertiesForFeature(module)) {
            Property previous = null;
            if (Client.INSTANCE.propertyManager.getPropertiesForFeature(module).get(Client.INSTANCE.propertyManager.getPropertiesForFeature(module).indexOf(value)) != null)
                previous = Client.INSTANCE.propertyManager.getPropertiesForFeature(module).get(Client.INSTANCE.propertyManager.getPropertiesForFeature(module).indexOf(value));
            if (value instanceof BooleanProperty) {
                getComponents().add(new BooleanButton(this, 8, valY, 100, 8, (BooleanProperty) value));
                valY += 9;
            }

            if (value instanceof StringProperty) {
                getComponents().add(new EnumButton(this, 8, valY, 100, 19, (StringProperty) value));
                valY += 20;
            }

            if (value instanceof MultiSelectableProperty) {
                getComponents().add(new MultiSelectableButton(this, 8, valY, 100, 19, (MultiSelectableProperty) value));
                valY += 20;
            }

            if (value instanceof NumberProperty) {
                getComponents().add(new SliderButton(this, 8, valY, 100, 16, (NumberProperty) value));
                valY += 15;
            }

            if (value instanceof ColorProperty) {
                getComponents().add(new ColorButton(this, 8, valY, 100, 8, (ColorProperty) value));
                valY += 8;
            }
        }
        setHeight(valY - getPosY() + 8);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);
        float posX = getParent().getPosX() + 50 + getPosX();
        float posY = getParent().getPosY() + 15 + getPosY();

        Render2DUtil.drawRect(posX, posY, posX + getWidth(), posY + getHeight(), new Color(10, 10, 10, Math.min(getParent().getParent().getAlpha(), 255)).getRGB());
        Render2DUtil.drawBorderedRect(posX + 0.5, posY + 0.5, posX + getWidth() - 0.5, posY + getHeight() - 0.5, 0.5, new Color(17, 17, 17, Math.min(getParent().getParent().getAlpha(), 255)).getRGB(), new Color(48, 48, 48, Math.min(getParent().getParent().getAlpha(), 255)).getRGB(), true);
        Render2DUtil.drawRect(posX + 6, posY, posX + 10 + Client.INSTANCE.buttonFontRenderer.getStringWidth(getLabel()), posY + 2, new Color(17, 17, 17, Math.min(getParent().getParent().getAlpha(), 255)).getRGB());
        Client.INSTANCE.buttonFontRenderer.drawStringWithOutline(getLabel(), posX + 8, posY - 1, new Color(210, 210, 210, Math.min(getParent().getParent().getAlpha(), 255)).getRGB());
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        Render2DUtil.prepareScissorBox(new ScaledResolution(Minecraft.getMinecraft()), posX, posY + 6, getWidth(), getHeight() - 8);
        components.forEach(comp -> comp.drawScreen(mouseX, mouseY, partialTicks));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight()) && getComponentHeight() > getHeight()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                setScrollY(Math.max(getScrollY() - 3, -(getComponentHeight() - getHeight() - 4)));
            } else if (wheel > 0) {
                setScrollY(getScrollY() + 3);
            }
        }
        if (getScrollY() - 3 < -(getComponentHeight() - getHeight() - 4))
            setScrollY(-(getComponentHeight() - getHeight() - 4));
        if (getScrollY() > 0) setScrollY(0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float posX = getParent().getPosX() + 50 + getPosX();
        float posY = getParent().getPosY() + 15 + getPosY();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + getWidth(), posY + getHeight()))
            components.forEach(comp -> comp.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        components.forEach(comp -> comp.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void keyTyped(final char typedChar, final int key) {
        components.forEach(comp -> comp.keyTyped(typedChar, key));
    }

    public List<dev.astroclient.client.ui.menu.click.component.Component> getComponents() {
        return components;
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }

    public float getComponentHeight() {
        float height = 8;
        for (Component component : getComponents()) {
            height += component.getHeight();
        }
        return height;
    }
}
