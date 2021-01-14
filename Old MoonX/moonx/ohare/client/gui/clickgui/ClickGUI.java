package moonx.ohare.client.gui.clickgui;

import moonx.ohare.client.gui.clickgui.frames.Frame;
import moonx.ohare.client.gui.clickgui.frames.impl.CategoryFrame;
import moonx.ohare.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {
    private ArrayList<Frame> frames = new ArrayList<>();

    public void init() {
        int x = 2;
        int y = 2;
        for (Module.Category moduleCategory : Module.Category.values()) {
            frames.add(new CategoryFrame(moduleCategory, x, y, 100, 18));
            if (x + 225 >= new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
                x = 2;
                y += 20;
            } else x += 105;
        }
        frames.forEach(Frame::init);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        frames.forEach(frame -> frame.onDraw(mouseX,mouseY,partialTicks));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        frames.forEach(frame -> frame.onKeyTyped(keyCode,typedChar));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        frames.forEach(frame -> frame.onMouseClicked(mouseX,mouseY,mouseButton));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        frames.forEach(frame -> frame.onMouseReleased(mouseX,mouseY,mouseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
