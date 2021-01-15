package dev.astroclient.client.feature.impl.hud.hud;

import dev.astroclient.client.Client;

import dev.astroclient.client.feature.impl.hud.hud.component.Component;
import dev.astroclient.client.feature.impl.hud.hud.settings.HudSettings;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;


/**
 * @author Xen for Astro
 * @since 11/10/2019
 **/
public class HudEditor extends GuiScreen {
    private boolean dragging;
    private Component selectedComp;
    private int lastX,lastY;

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        dragging = false;
        selectedComp = null;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        Render2DUtil.drawRect(0.0f, scaledResolution.getScaledHeight() / 2.0f - 0.5f, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight() / 2.0f + 0.5f, new Color(0x545454).getRGB());
        Render2DUtil.drawRect(scaledResolution.getScaledWidth() / 2.0f - 0.5f, 0.0f, scaledResolution.getScaledWidth() / 2.0f + 0.5f, scaledResolution.getScaledHeight(),  new Color(0x545454).getRGB());

        for (Component component : Client.INSTANCE.hudManager.getComponents()) {
            component.renderPreview();
            if (component.getWidth() != 0) {
                if (component.getHeight() == 0) {
                    continue;
                }
                GL11.glPushMatrix();
                final boolean mouseOver = isMouseOver(component, mouseX, mouseY);
                final int borderColor = mouseOver ? new Color(60, 52, 60, 200).getRGB() : new Color(169, 169, 169, 200).getRGB();
                Client.INSTANCE.fontRenderer.drawString(component.getName(), component.getX(), component.getY() - 9.0f, -1);
                Render2DUtil.drawBorderedRect(component.getX(), component.getY(), component.getX() + component.getWidth(), component.getY() + component.getHeight(), 0.5f, new Color(93, 93, 93,40).getRGB(), borderColor,false);
                GL11.glPopMatrix();

                if(dragging && selectedComp == component) {
                    float setX = (float)(mouseX - lastX);
                    float setY = (float)(mouseY - lastY);

                    final int height = component.getHeight();
                    final int width = component.getWidth();
                    if (setX < 2.0f) {
                        setX = 2.0f;
                    } else if (setX + width > scaledResolution.getScaledWidth() - 2) {
                        setX = (float)(scaledResolution.getScaledWidth() - width - 2);
                    }
                    if (setY < 2.0f) {
                        setY = 2.0f;
                    } else if (setY + height > scaledResolution.getScaledHeight() - 2) {
                        setY = (float)(scaledResolution.getScaledHeight() - height - 2);
                    }
                    component.setX(setX);
                    component.setY(setY);
                }
            }
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Component component : Client.INSTANCE.hudManager.getComponents()) {
            if (!isMouseOver(component, mouseX, mouseY)) {
                continue;
            }
            switch (mouseButton) {
                case 0:
                    lastX = (int)(mouseX - component.getX());
                    lastY = (int)(mouseY - component.getY());

                    dragging = true;
                    selectedComp = component;
                    break;
                case 1:
                    mc.displayGuiScreen(new HudSettings(component));
                    break;
                case 2:
                    component.setActive(!component.isActive());
                    break;
            }
            break;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = false;
    }

    private boolean isMouseOver(Component component, int mouseX, int mouseY) {
        return mouseX > component.getX() && mouseY > component.getY() && mouseX < component.getX() + component.getWidth() && mouseY < component.getY() + component.getHeight();
    }
}
