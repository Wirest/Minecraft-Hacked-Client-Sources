package dev.astroclient.client.ui.clickable.frame.component.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.property.Property;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.ColorProperty;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.ui.clickable.frame.component.Component;
import dev.astroclient.client.ui.clickable.frame.impl.CategoryFrame;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class ModuleComponent extends Component {
    private Feature module;
    private boolean binding, extended;
    private int defaultHeight;
    private ArrayList<Component> components = new ArrayList<>();
    private CategoryFrame parentFrame;

    private final int backgroundColor = new Color(47, 47, 47).getRGB();

    private final int darkerColor = new Color(47, 47, 47).darker().getRGB();

    public ModuleComponent(CategoryFrame parentFrame, Feature module, int parentX, int parentY, int offsetX, int offsetY, int width, int height) {
        super(module.getLabel(), parentX, parentY, offsetX, offsetY, width, height);
        this.module = module;
        this.defaultHeight = height;
        this.parentFrame = parentFrame;
    }

    @Override
    public void close() {
        components.forEach(component -> component.close());
    }

    @Override
    public void init() {
        int offset = getHeight();
        components.add(new HiddenComponent(module, getParentX(), getParentY(), 0, offset, getWidth(), 12));
        if (Client.INSTANCE.propertyManager.getPropertiesForFeature(module) != null) {
            for (Property value : Client.INSTANCE.propertyManager.getPropertiesForFeature(module)) {
                if (value instanceof BooleanProperty) {
                    components.add(new BooleanComponent((BooleanProperty) value, getParentX(), getParentY(), 0, offset, getWidth(), 12));
                    offset += 12;
                } else if (value instanceof NumberProperty) {
                    components.add(new SliderComponent((NumberProperty) value, getParentX(), getParentY(), 0, offset, getWidth(), 20));
                    offset += 20;
                } else if (value instanceof StringProperty) {
                    components.add(new StringComponent((StringProperty) value, getParentX(), getParentY(), 0, offset, getWidth(), 10));
                    offset += 10;
                } else if (value instanceof ColorProperty) {
                    components.add(new ColorComponent((ColorProperty) value, getParentX(), getParentY(), 0, offset, getWidth(), 10));
                    offset += 10;
                } else if (value instanceof MultiSelectableProperty) {
                    components.add(new StringMultiSelectable((MultiSelectableProperty) value, getParentX(), getParentY(), 0, offset, getWidth(), 10));
                    offset += 10;
                }
            }
        }
    }

    public void updatePosition(int posX, int posY) {
        setParentX(posX);
        setParentY(posY);
        setFinishedX(posX + getOffsetX());
        setFinishedY(posY + getOffsetY());
        components.forEach(component -> component.updatePosition(getFinishedX(), getFinishedY()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
        final boolean hoveredmain = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX(), getFinishedY(), getWidth() - (components.isEmpty() ? 0 : 20), getHeight());
        final boolean hoveredbutton = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + getWidth() - 20, getFinishedY(), 20, getHeight());
        Render2DUtil.drawRectWH(getFinishedX(), getFinishedY(), getWidth() - (components.isEmpty() ? 0 : 20), getHeight(), hoveredmain ? darkerColor : backgroundColor);

        ToggleableFeature toggleableFeature = (ToggleableFeature) getModule();
        if (!components.isEmpty())
            Render2DUtil.drawRectWH(getFinishedX() + getWidth() - 20, getFinishedY(), 20, getHeight(), hoveredbutton ? darkerColor : backgroundColor);
        Client.INSTANCE.smallFontRenderer.drawStringWithShadow(isBinding() ? "Press a key..." : getLabel(), getFinishedX() + 2, (getFinishedY() + getHeight() / 2 - Client.INSTANCE.smallFontRenderer.getHeight() / 2), toggleableFeature.getState() ? getColor().getRGB() : -1);
        if (!components.isEmpty()) {
            GlStateManager.pushMatrix();
            Client.INSTANCE.smallFontRenderer.drawStringWithShadow(isExtended() ? "-" : "+", (int) (getFinishedX() + getWidth() - 6.5f) - Client.INSTANCE.smallFontRenderer.getStringWidth(isExtended() ? "-" : "+"), (getFinishedY() + getHeight() / 2 - Client.INSTANCE.smallFontRenderer.getHeight() / 2), -1);
            GlStateManager.popMatrix();
        }
        if (isExtended()) {
            int offset = getHeight();
            for (Component component : components) {
                component.setOffsetY(offset);
                offset += component.getHeight();
            }
            components.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks, scaledResolution));
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final boolean hoveredmain = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX(), getFinishedY(), getWidth() - (components.isEmpty() ? 0 : 20), getHeight());
        final boolean hoveredbutton = MouseUtil.mouseWithinBounds(mouseX, mouseY, getFinishedX() + getWidth() - 20, getFinishedY(), 20, getHeight());
        switch (mouseButton) {
            case 0:
                if (hoveredmain)
                    if (getModule() instanceof ToggleableFeature)
                        ((ToggleableFeature) getModule()).setState(!((ToggleableFeature) getModule()).getState());
                if (hoveredbutton && !components.isEmpty()) setExtended(!isExtended());
                break;
            case 2:
                if (hoveredmain) setBinding(!isBinding());
                break;
            default:
                break;
        }
        if (isExtended()) components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (isExtended()) components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (isBinding()) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (getModule() instanceof ToggleableFeature)
                    ((ToggleableFeature) getModule()).setBind(Keyboard.KEY_NONE);

            } else {
                if (getModule() instanceof ToggleableFeature) ((ToggleableFeature) getModule()).setBind(keyCode);
            }
            setBinding(false);
        }
        if (isExtended()) components.forEach(component -> component.keyTyped(typedChar, keyCode));

    }

    public Feature getModule() {
        return module;
    }

    private boolean isBinding() {
        return binding;
    }

    private void setBinding(boolean binding) {
        this.binding = binding;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public float getAdditionHeight() {
        float additionHeight = defaultHeight;
        if (isExtended()) {
            for (Component comp : components) {
                additionHeight += comp.getHeight();
            }
        }
        return additionHeight;
    }
}