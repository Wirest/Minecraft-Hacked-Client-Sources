package me.Corbis.Execution.Security;

import me.Corbis.Execution.ui.UnicodeFontRenderer;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class CleanTextField {

    int x, y;
    String text;
    String name;
    boolean isFocused = false;
    int length = 0;
    TimeHelper timer = new TimeHelper();

    public CleanTextField(int x, int y, String text, String name) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.name = name;
    }

    public void draw(UnicodeFontRenderer ufr){
        GlStateManager.color(1.0f, 1.0f,1.0f,1.0f);
        float diff = ufr.getStringWidth(text) - length;
        length += diff / 4;
        if(ufr.getStringWidth(text) < 50){
            Gui.drawRect(x, y + ufr.FONT_HEIGHT + 3, x + 50, y + ufr.FONT_HEIGHT + 4, new Color(0, 231, 255, 255).getRGB());
        }else {
            Gui.drawRect(x, y + ufr.FONT_HEIGHT + 3, x + length, y + ufr.FONT_HEIGHT + 4, new Color(0, 231, 255, 255).getRGB());
        }
        if(text.equalsIgnoreCase("") && !isFocused){
            ufr.drawString(name, x, y + 1, new Color(160, 160, 160, 255).getRGB());
        }
        if(!timer.hasReached(1000)) {
            if(timer.hasReached(500) && isFocused) {
                ufr.drawString(text + "|", x, y + 1, new Color(160, 160, 160, 255).getRGB());
            }else {
                ufr.drawString(text, x, y + 1, new Color(160, 160, 160, 255).getRGB());
            }
        }

        if(timer.hasReached(950)){
            timer.reset();
        }
        GlStateManager.color(1.0f, 1.0f,1.0f,1.0f);


    }

    public void mouseClicked(int mouseX, int mouseY, UnicodeFontRenderer ufr){
        System.out.println(mouseX + "," + mouseY + " asdasd" + x + "," + y);
        if(mouseX > x && mouseX < (length > 50 ? x + length : x + 50) && mouseY > y && mouseY < y + ufr.FONT_HEIGHT){
            this.isFocused = true;
        }else {
            this.isFocused = false;
        }
    }

    public void keyTyped(char keyTyped, int key){
        if(isFocused){
            switch(key){
                case 14:
                    text = text.substring(0, text.length() > 0 ? text.length() - 1 : text.length());
                    break;
                default:
                    if (GuiScreen.isKeyComboCtrlV(key))
                    {

                        this.text = this.text + (GuiScreen.getClipboardString());
                        return;


                    }
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        text += Character.toUpperCase(keyTyped);
                    }else {
                        text += keyTyped;
                    }
                    break;
            }


        }
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
