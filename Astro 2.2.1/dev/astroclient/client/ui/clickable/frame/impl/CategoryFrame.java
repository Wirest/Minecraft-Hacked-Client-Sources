package dev.astroclient.client.ui.clickable.frame.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.Feature;
import dev.astroclient.client.ui.clickable.frame.Frame;
import dev.astroclient.client.ui.clickable.frame.component.impl.ModuleComponent;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.OutlineUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import dev.astroclient.client.util.render.Render3DUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class CategoryFrame extends Frame {
    private Category category;
    private ArrayList<ModuleComponent> components = new ArrayList<>();
    private int scrollY;

    private final int darkBgColor = new Color(25, 25, 25).getRGB();

    public CategoryFrame(Category category, int posX, int posY, int width, int height) {
        super(StringUtils.capitalize(category.name().toLowerCase()), posX, posY, width, height, false);
        this.category = category;
        this.scrollY = 0;
    }

    @Override
    public void close() {
        components.forEach(ModuleComponent::close);
    }

    @Override
    public void init() {
        int offset = getHeight();
        for (Feature module : Client.INSTANCE.featureManager.getFeaturesForCategory(category)) {
            ModuleComponent moduleComponent = new ModuleComponent(this, module, getPosX(), getPosY() + 2, 2, offset, getWidth() - 4, 16);
            components.add(moduleComponent);
            offset += 16;
        }
        components.forEach(ModuleComponent::init);
    }

    @Override
    public void updatePosition(int posX, int posY) {
        if (isVisible()) {
            components.forEach(component -> component.updatePosition(posX, posY));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, ScaledResolution scaledResolution) {
        super.drawScreen(mouseX, mouseY, partialTicks, scaledResolution);
        if (isVisible()) {
            Render2DUtil.drawRectWH(getPosX(), getPosY() + 2, getWidth(), getHeight(), darkBgColor);
            Client.INSTANCE.hudFontRenderer.drawStringWithShadow(getLabel(), getPosX() + 6, getPosY() + getHeight() / 2 - Client.INSTANCE.smallFontRenderer.getHeight() / 2 + 2, isPinned() ? 0xffff0000 : -1);
            GlStateManager.pushMatrix();
            OutlineUtil.setColor(Client.INSTANCE.featureManager.clickGUI.guiColor.getColor());
            Client.INSTANCE.iconRenderer.drawString(String.valueOf(category.getCharacter()), getPosX() + getWidth() - Client.INSTANCE.iconRenderer.getStringWidth(String.valueOf(category.getCharacter())) - 2, getPosY() + Client.INSTANCE.fontRenderer.getHeight() - 1, -1);
            GlStateManager.popMatrix();
            int defaultOffset = 0;
            for (Feature module : Client.INSTANCE.featureManager.getFeaturesForCategory(category)) {
                defaultOffset += 16;
            }
            int offset = getHeight();
            if (isExtended()) {
                for (ModuleComponent moduleComponent : components) {
                    moduleComponent.setOffsetY(offset + getScrollY());
                    offset += moduleComponent.getAdditionHeight();
                }
                Render2DUtil.drawRectWH(getPosX(), getPosY() + getHeight() + 2, getWidth(), defaultOffset + 4, darkBgColor);
                components.forEach(component -> component.updatePosition(getPosX(), getPosY()));
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                Render2DUtil.prepareScissorBox(scaledResolution, getPosX() + 2, getPosY() + getHeight() + 4, getWidth() - 4, defaultOffset > 120 ? defaultOffset : 120);
                components.forEach(component -> component.drawScreen(mouseX, mouseY, partialTicks, scaledResolution));
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GL11.glPopMatrix();
            }
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 2, getPosY() + 16, getWidth() - 4, defaultOffset > 120 ? defaultOffset : 120) && offset > 120 && Mouse.hasWheel()) {
                int wheel = Mouse.getDWheel();
                if (wheel < 0) {
                    if (getScrollY() - 10 < -((offset - 20) - (defaultOffset > 120 ? defaultOffset : 120)))
                        setScrollY(-((offset - 20) - (defaultOffset > 120 ? defaultOffset : 120)));
                    else
                        setScrollY(getScrollY() - 10);
                } else if (wheel > 0) {
                    setScrollY(getScrollY() + 10);
                }
            }
            if (getScrollY() > 0) setScrollY(0);
            if (getScrollY() < -((offset - 20) - defaultOffset)) setScrollY(-((offset - 20) - defaultOffset));
        }

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isVisible()) {
            float defaultOffset = 0;
            for (Feature module : Client.INSTANCE.featureManager.getFeaturesForCategory(category)) {
                defaultOffset += 16;
            }
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 2, getPosY() + 16, getWidth() - 4, defaultOffset > 120 ? defaultOffset : 120) && isExtended()) {
                components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (isVisible()) {
            components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        if (isVisible()) {
            if (isExtended()) components.forEach(component -> component.keyTyped(typedChar, keyCode));
        }
    }

    public Category getCategory() {
        return category;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }
}
