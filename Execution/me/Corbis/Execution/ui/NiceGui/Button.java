package me.Corbis.Execution.ui.NiceGui;

import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.RenderingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class Button {
    Minecraft mc = Minecraft.getMinecraft();
    Category category;
    int x, y;
    Module mod;
    public int start = 0;
    public boolean open = false;
    int slide;
    int lastSlide = 0;
    public SettingsWindow settingsWindow = null;

    public Button(Category category, int x, int y, Module mod) {
        this.category = category;
        this.x = x;
        this.y = y;
        start = y;
        this.mod = mod;
    }
    int mouseTicks = 0;
    int last = 500;

    public Minecraft getMc() {
        return mc;
    }

    public void setMc(Minecraft mc) {
        this.mc = mc;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getMouseTicks() {
        return mouseTicks;
    }

    public void setMouseTicks(int mouseTicks) {
        this.mouseTicks = mouseTicks;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public void draw(int mouseX, int mouseY){
        if(isWithinButton(mouseX, mouseY)){
            if(mouseTicks < 2){
                mouseTicks++;
            }
        }else {
            if(mouseTicks > 0){
                mouseTicks--;
            }
        }
        mc.getTextureManager().bindTexture(new ResourceLocation("Execution/moduleshader.png"));

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        Gui.drawModalRectWithCustomSizedTexture(x - mouseTicks, y - mouseTicks, 0, 0, 550 + mouseTicks, 35 + mouseTicks, 550 + mouseTicks    , 35 + mouseTicks);

        GlStateManager.color(1.0f, 1.0f, 1.0f ,1.0f);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        int target = mod.isEnabled ? 518 : 500;
        float diff  = target - last;
        last += diff / 4;
        for(int i = 0; i < 15; i++){
            RenderingUtil.drawFilledCircle(x + 500 +  i, y + 17, 4, mod.isEnabled ? new Color(0, 255, 0, 255) : new Color(150, 150, 150, 255));
        }
        RenderingUtil.drawFilledCircle(x + last, y + 17, 6, new Color(255, 255, 255, 255));
    }

    public boolean isWithinButton(int mouseX, int mouseY){
        return mouseX > x && mouseX < x + 550 && mouseY > y && mouseY < y + 35;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(isWithinButton(mouseX, mouseY)){
            if(mouseButton == 0){
                if(!open) {
                    mod.toggle();
                }
            }else {
                settingsWindow = new SettingsWindow(this);
            }
        }
    }

    public void drawString(UnicodeFontRenderer ufr){
        ufr.drawString(mod.getName(), x + 20, y + 35 / 2 - ufr.FONT_HEIGHT / 2 - 2, 0xFF000000);

    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Module getMod() {
        return mod;
    }

    public void setMod(Module mod) {
        this.mod = mod;
    }
}
