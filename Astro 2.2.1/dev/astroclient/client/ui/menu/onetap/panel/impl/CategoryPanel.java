package dev.astroclient.client.ui.menu.onetap.panel.impl;

import dev.astroclient.client.feature.Category;
import dev.astroclient.client.ui.menu.onetap.panel.Panel;
import dev.astroclient.client.util.ChatUtil;
import dev.astroclient.client.util.render.Render2DUtil;

import java.awt.*;
import java.io.IOException;

public class CategoryPanel implements Panel {

    private Category category;

    private final int color = 0x575757;

    private int posX, posY, width, height;

    public CategoryPanel(Category category, int posX, int posY, int width, int height) {
        this.category = category;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Render2DUtil.drawRoundedRect(posX, posY, width, height, 10, color);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int key) {

    }
}
