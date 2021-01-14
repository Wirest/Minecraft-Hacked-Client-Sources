package moonx.ohare.client.gui.materialui;

import moonx.ohare.client.gui.materialui.plane.impl.MainPlane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class MaterialUI extends GuiScreen {
    private MainPlane mainPlane = null;

    public void initializedUI() {
        if (mainPlane == null) {
            mainPlane = new MainPlane("Moon X",2,2,325,315);
            mainPlane.initializePlane();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mainPlane.onDrawScreen(mouseX,mouseY,partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        mainPlane.onKeyTyped(typedChar,keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mainPlane.onMouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        mainPlane.onMouseReleased(mouseX,mouseY,mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mainPlane.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onResize(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
        super.onResize(mcIn, p_175273_2_, p_175273_3_);
        if (mainPlane.getPosX() + mainPlane.getWidth() > p_175273_2_) mainPlane.setPosX(p_175273_2_ - mainPlane.getWidth());
        if (mainPlane.getPosY() + mainPlane.getHeight() > p_175273_3_) mainPlane.setPosY(p_175273_3_ - mainPlane.getHeight());
    }
}
