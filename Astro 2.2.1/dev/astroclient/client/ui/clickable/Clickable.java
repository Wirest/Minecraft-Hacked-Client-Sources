package dev.astroclient.client.ui.clickable;

import dev.astroclient.client.feature.Category;
import dev.astroclient.client.ui.clickable.frame.Frame;
import dev.astroclient.client.ui.clickable.frame.impl.CategoryFrame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

/**
 * made by Xen for Astro
 * at 12/19/2019
 **/
public class Clickable extends GuiScreen {
    private ArrayList<Frame> frames = new ArrayList<>();

    public Clickable() {
        int posX = 114;
        for (Category category : Category.values()) {
            CategoryFrame categoryFrame = new CategoryFrame(category, posX,2,110,16);
            categoryFrame.setVisible(true);
            frames.add(categoryFrame);
            posX += 112;
        }
        frames.forEach(Frame::init);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
       final ScaledResolution scaledResolution = new ScaledResolution(mc);
//        GL11.glPushMatrix();
//        GL11.glScaled(1.6, 1.6, 1.6);
//        mc.fontRenderer.drawStringWithShadow("Lurking", 6, 8, -1);
//        mc.fontRenderer.drawStringWithShadow("v1.1", mc.fontRenderer.getStringWidth("Lurking") + 8, 8, Lurking.instance.getGlobalValues().find_color("color").getValue());
//        GL11.glPopMatrix();

        frames.forEach(frame -> frame.drawScreen(mouseX, mouseY, partialTicks, scaledResolution));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        frames.forEach(frame -> frame.keyTyped(typedChar, keyCode));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frames.forEach(frame -> frame.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        frames.forEach(frame -> frame.mouseReleased(mouseX, mouseY, state));
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        frames.forEach(frame -> frame.setDragging(false));
        frames.forEach(Frame::close);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
