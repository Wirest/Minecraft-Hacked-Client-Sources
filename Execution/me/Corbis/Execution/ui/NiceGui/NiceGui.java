package me.Corbis.Execution.ui.NiceGui;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class NiceGui extends GuiScreen {
    Category currentCategory;
    ArrayList<CategoryButton> buttons = new ArrayList<>();
    ArrayList<Button> mods = new ArrayList<>();
    static UnicodeFontRenderer ufr;
    int offset = 0;
    int lastOffset = 0;
    public NiceGui(){}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 1, 1);
        }
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }

        if(lastOffset != offset){
            int diff = offset - lastOffset;
            lastOffset += diff / 4;
        }
        int left = width / 6;
        int top = height / 7;
        int right = width - left;
        int bottom = height - top;
        GlStateManager.pushMatrix();
        this.prepareScissorBox(left, top, right, bottom);
        GL11.glEnable(3089);
        Gui.drawRect(left, top, right, bottom, new Color(230, 230, 230, 255).getRGB());
        Gui.drawRect(left, top, left + 80, bottom, 0xFFFFFFFF);
        for(CategoryButton button : buttons){
            button.draw(mouseX, mouseY);
        }
        if(currentCategory != null){

            for(Button b : mods){
                b.setY(b.start - lastOffset);
                b.draw(mouseX, mouseY);
            }
            for(Button b : mods){
                b.setY(b.start - lastOffset);
                b.drawString(ufr);
            }

        }
        for(Button b : mods){
            if(b.settingsWindow != null){
                b.settingsWindow.draw(ufr, mouseX, mouseY);
            }
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }



    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int left = width / 6;
        int top = height / 7;
        for(Button b : mods){
            if(b.settingsWindow != null){
                b.settingsWindow.mouseClicked(mouseX, mouseY);
                return;
            }
        }
        for(CategoryButton button : buttons){
            button.mouseClicked(mouseX, mouseY);
            if(button.isWithinButton(mouseX, mouseY)){
                mods.clear();
                int count = 0;
                for(Module m : Execution.instance.moduleManager.getModulesInCategory(currentCategory)){
                    Button b = new Button(currentCategory, left + 86, top + 5 + count * 30, m);
                    b.setStart(top + 5 + count * 30);
                    this.mods.add(b);

                    count++;
                }
                offset = 0;

            }
        }

        for(Button b : mods){

            b.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(Button b : mods){
            if(b.settingsWindow != null){
                b.settingsWindow.mouseReleased();
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void initGui() {
        int left = width / 6;
        int top = height / 7;
        int count = 0;
        for(Category category : Category.values()){
            buttons.add(new CategoryButton(left + 15, top + 5 + 44 * count, category, this));
            count++;
        }
        super.initGui();
    }

    public void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scale = new ScaledResolution(this.mc);
        final int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for(Button b : mods){
            if(b.settingsWindow != null){
                if(keyCode == 1){
                    b.settingsWindow = null;
                }
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}
