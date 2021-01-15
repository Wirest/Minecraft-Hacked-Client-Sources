package dev.astroclient.client.ui.menu.click.panel;

import dev.astroclient.client.Client;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.ui.menu.click.GuiMenu;
import dev.astroclient.client.ui.menu.click.component.components.GroupBox;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * made by oHare for Ayyware
 *
 * @since 4/10/2019
 **/
public abstract class MainPanel {
    private float posX, posY, lastPosX, lastPosY, width, height, scrollY;
    public boolean dragging;
    private static Category selectedTab = Category.COMBAT;
    public static List<GroupBox> groups = new ArrayList<>();
    private GuiMenu parent;

    public MainPanel(GuiMenu parent, float posX, float posY, float width, float height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.lastPosX = posX;
        this.lastPosY = posY;
        this.parent = parent;
    }

    public abstract void init();

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (dragging) {
            setPosX((int) (mouseX + this.lastPosX));
            setPosY((int) (mouseY + this.lastPosY));
        }
        if (getPosX() < 0) {
            setPosX(0);
        }
        if (getPosX() + getWidth() > scaledResolution.getScaledWidth()) {
            setPosX((int) (scaledResolution.getScaledWidth() - getWidth()));
        }
        if (getPosY() < 0) {
            setPosY(0);
        }
        if (getPosY() + getHeight() > scaledResolution.getScaledHeight()) {
            setPosY((int) (scaledResolution.getScaledHeight() - getHeight()));
        }

        Render2DUtil.drawRect(posX, posY, posX + width, posY + height, new Color(5, 5, 5, Math.min(parent.getAlpha(), 255)).getRGB());
        Render2DUtil.drawBorderedRect(posX + .5, posY + .5, posX + width - .5, posY + height - .5, 0.5, new Color(40, 40, 40, Math.min(parent.getAlpha(), 255)).getRGB(), new Color(60, 60, 60, Math.min(parent.getAlpha(), 255)).getRGB(), true);
        Render2DUtil.drawBorderedRect(posX + 2, posY + 2, posX + width - 2, posY + height - 2, 0.5, new Color(22, 22, 22, Math.min(parent.getAlpha(), 255)).getRGB(), new Color(60, 60, 60, Math.min(parent.getAlpha(), 255)).getRGB(), true);
    //    Render2DUtil.drawImg(new ResourceLocation("client/images/skeetchainmail.png"), posX + 2.5, posY + 2.5, (width - 4f) / 2, (height - 5f) / 2);
    //    Render2DUtil.drawImg(new ResourceLocation("client/images/skeetchainmail.png"), posX + 1.5 + ((width - 4f) / 2), posY + 2.5, (width - 4f) / 2, (height - 5f) / 2);
    //    Render2DUtil.drawImg(new ResourceLocation("client/images/skeetchainmail.png"), posX + 2.5, posY + 2 + (height - 5f) / 2, (width - 4f) / 2, (height - 5f) / 2);
    //    Render2DUtil.drawImg(new ResourceLocation("client/images/skeetchainmail.png"), posX + 1.5 + ((width - 4f) / 2), posY + 2 + (height - 5f) / 2, (width - 4f) / 2, (height - 5f) / 2);
        Render2DUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - 2.5, posY + 4.5, new Color(9, 9, 9, Math.min(parent.getAlpha(), 255)).getRGB());
        Render2DUtil.drawGradientRect(posX + 3, posY + 3, posX + width - 2.5, posY + 4, Client.INSTANCE.featureManager.clickGUI.guiColor.getColor().getRGB(), Client.INSTANCE.featureManager.clickGUI.secondaryGuiColor.getColor().getRGB());
    //    Render2DUtil.drawGradientSideways(posX + 3, posY + 3, posX + (width / 3) - 2.5, posY + 4, new Color(81, 149, 219, Math.max(parent.getAlpha(), 255)).getRGB(), new Color(180, 49, 218, Math.max(parent.getAlpha(), 255)).getRGB());
    //    Render2DUtil.drawGradientSideways(posX + (width / 3) - 2.5, posY + 3, posX + ((width / 3) * 2) - 2.5, posY + 4, new Color(180, 49, 218, Math.max(parent.getAlpha(), 255)).getRGB(), new Color(236, 93, 128, Math.max(parent.getAlpha(), 255)).getRGB());
    //    Render2DUtil.drawGradientSideways(posX + ((width / 3) * 2) - 2.5, posY + 3, posX + ((width / 3) * 3) - 3, posY + 4, new Color(236, 93, 128, Math.max(parent.getAlpha(), 255)).getRGB(), new Color(167, 171, 90, Math.max(parent.getAlpha(), 255)).getRGB());
        double y = posY + 24;
        for (Category tab : Category.values()) {
            if (selectedTab == tab) {
                //top
                Render2DUtil.drawRect(posX + 2.5, posY + 4.5, posX + 48, y - 8, new Color(48, 48, 48, Math.min(parent.getAlpha(), 255)).getRGB());
                Render2DUtil.drawRect(posX + 2.5, posY + 4.5, posX + 47.5, y - 8.5, new Color(0, 0, 0, Math.min(parent.getAlpha(), 255)).getRGB());
                Render2DUtil.drawRect(posX + 2.5, posY + 4.5, posX + 47, y - 9, new Color(12, 12, 12, Math.min(parent.getAlpha(), 255)).getRGB());
                //bottom
                Render2DUtil.drawRect(posX + 2.5, y + 36, posX + 48, posY + height - 2.5, new Color(48, 48, 48, Math.min(parent.getAlpha(), 255)).getRGB());
                Render2DUtil.drawRect(posX + 2.5, y + 36.5, posX + 47.5, posY + height - 2.5, new Color(0, 0, 0, Math.min(parent.getAlpha(), 255)).getRGB());
                Render2DUtil.drawRect(posX + 2.5, y + 37, posX + 47, posY + height - 2.5, new Color(12, 12, 12, Math.min(parent.getAlpha(), 255)).getRGB());
            }
            y += 48;
        }
        float y2 = posY + 24;
        for (Category tab : Category.values()) {
            Client.INSTANCE.iconRenderer.drawStringWithShadow("" + tab.getCharacter(), posX + 24 - Client.INSTANCE.iconRenderer.getStringWidth("" + tab.getCharacter()) / 2, y2, (selectedTab == tab || MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 2.5f, y2 - 8, posX + 48f, y2 + 37)) ? new Color(210, 210, 210, Math.min(parent.getAlpha(), 255)).getRGB() : new Color(90, 90, 90, Math.min(parent.getAlpha(), 255)).getRGB());
            y2 += 49;
        }
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        Render2DUtil.prepareScissorBox(new ScaledResolution(Minecraft.getMinecraft()), getPosX() + 48, getPosY() + 5, getWidth() - 50, getHeight() - 7);
        groups.stream().filter(groupBox -> groupBox.getTab() == getSelectedTab()).forEach(cat -> cat.drawScreen(mouseX, mouseY, partialTicks));
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX() + 48, getPosY() + 3, getPosX() + getWidth(), getPosY() + getHeight()) && getTotalHeight() > getHeight()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0)
                setScrollY(Math.max(getScrollY() - 3, -(getTotalHeight() - getHeight() - 4)));
            else if (wheel > 0)
                setScrollY(getScrollY() + 3);

        }
        if (getScrollY() - 3 < -(getTotalHeight() - getHeight() - 4)) setScrollY(-(getTotalHeight() - getHeight() - 4));
        if (getScrollY() > 0) setScrollY(0);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX, posY, posX + width, posY + height)) {
            if (mouseButton == 0) {
                dragging = true;
                this.lastPosX = (posX - mouseX);
                this.lastPosY = (posY - mouseY);
            }
        }
        groups.stream().filter(groupBox -> groupBox.getTab() == getSelectedTab()).forEach(cat -> cat.mouseClicked(mouseX, mouseY, mouseButton));

        float y = posY + 24;
        for (Category tab : Category.values()) {
            if (MouseUtil.mouseWithinBounds(mouseX, mouseY, posX + 2.5f, y - 8, posX + 48f, y + 37)) {
                dragging = false;
                selectedTab = tab;
                setScrollY(0);
            }
            y += 48;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        groups.stream().filter(groupBox -> groupBox.getTab() == getSelectedTab()).forEach(cat -> cat.mouseReleased(mouseX, mouseY, mouseButton));
        if (dragging && mouseButton == 0) dragging = false;
    }

    public void keyTyped(char typedChar, int key) {
        groups.stream().filter(groupBox -> groupBox.getTab() == getSelectedTab()).forEach(cat -> cat.keyTyped(typedChar, key));
        if (key == Keyboard.KEY_ESCAPE) {
            dragging = false;
        }
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<GroupBox> getGroups() {
        return groups;
    }

    public static Category getSelectedTab() {
        return selectedTab;
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = scrollY;
    }

    private float getTotalHeight() {
        float height = 20;
        final ArrayList<GroupBox> validGroups = getGroups().stream().filter(groupBox -> groupBox.getTab() == getSelectedTab()).collect(Collectors.toCollection(ArrayList::new));
        for (GroupBox groupBox : validGroups) {
            if (getGroups().indexOf(groupBox) % 2 != 0) continue;
            if (getGroups().indexOf(groupBox) != getGroups().size() - 1)
                height += (getGroups().get(getGroups().indexOf(groupBox) + 1).getHeight() > groupBox.getHeight() ? getGroups().get(getGroups().indexOf(groupBox) + 1).getHeight() : groupBox.getHeight()) + 10;
            else height += groupBox.getHeight() + 10;
        }
        return height;
    }

    public GuiMenu getParent() {
        return parent;
    }
}
