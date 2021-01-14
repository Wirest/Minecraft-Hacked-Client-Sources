package me.Corbis.Execution.ui.TabGui;


import me.Corbis.Execution.Execution;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.ui.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.world.storage.MapData;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class TabGui {

    static Minecraft mc = Minecraft.getMinecraft();
    static UnicodeFontRenderer ufr;
    int index;
    boolean open;
    float lastY = ufr == null ? index : index * ufr.FONT_HEIGHT;
    int moduleIndex;
    float lastY2 = moduleIndex;

    public void draw(){
        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 18, 0, 20, 8);
        }
        ScaledResolution sr = new ScaledResolution(mc);
        Gui.drawRect(0, ufr.FONT_HEIGHT + 11, ufr.getStringWidth("MovementMo"), ((ufr.FONT_HEIGHT + 3) * Category.values().length) - 2.5, new Color(0, 0, 0, 180).getRGB());
        int count = 0;
        float targetY = (index * ufr.FONT_HEIGHT);
        float diff = targetY - lastY;
        targetY = lastY;

        lastY += diff / 4;



        Gui.drawRect(0,ufr.FONT_HEIGHT + 10 + targetY + 1.5, ufr.getStringWidth("MovementMo"), ufr.FONT_HEIGHT + 10 + targetY + ufr.FONT_HEIGHT + 2, rainbow(0, 200));

        for(Category category : Category.values()){

            String name = "Null";
            if(category.name().equalsIgnoreCase("Combat")){
                name = "Combat";
            }else if(category.name().equalsIgnoreCase("Movement")){
                name = "Movement";
            }else if(category.name().equalsIgnoreCase("Player")){
                name = "Player";
            }else if(category.name().equalsIgnoreCase("Render")){
                name = "Render";
            }else if(category.name().equalsIgnoreCase("Exploit")){
                name = "Exploit";
            }else if(category.name().equalsIgnoreCase("Misc")){
                name = "Misc";
            }else if(category.name().equalsIgnoreCase("World")){
                name = "World";
            }else if(category.name().equalsIgnoreCase("Targets")){
                name = "Targets";
            }

            ufr.drawString(name, 2, ufr.FONT_HEIGHT + 10 + (count * ufr.FONT_HEIGHT) + 1, 0xFFFFFFFF);

            count++;
        }
        if(open){
            float targetY2 = (moduleIndex * ufr.FONT_HEIGHT);
            float diff2 = targetY2 - lastY2;
            targetY2 = lastY2;

            lastY2 += diff2 / 4;
            int modulesCount = Execution.instance.moduleManager.getModulesInCategory(Category.values()[index]).size() + index;
            Gui.drawRect(ufr.getStringWidth("MovementMo"), ufr.FONT_HEIGHT + 10 + targetY + 1.5, ufr.getStringWidth("MovementMo") + ufr.getStringWidth("BlockAnimation"), ufr.FONT_HEIGHT + 10 + (modulesCount * ufr.FONT_HEIGHT + 2) + 1, new Color(0, 0, 0, 180).getRGB());
            int countForModule = 0;
            Gui.drawRect(ufr.getStringWidth("MovementMo"),ufr.FONT_HEIGHT + 10 + targetY2 + 2 + targetY, ufr.getStringWidth("MovementMo") + ufr.getStringWidth("BlockAnimation"), ufr.FONT_HEIGHT + 10 + targetY2 + ufr.FONT_HEIGHT + 2 + targetY, rainbow(0, 200));


            for(Module m : Execution.instance.moduleManager.getModulesInCategory(Category.values()[index])){
                ufr.drawString(m.getName(), ufr.getStringWidth("MovementMo") + 2, (float) (ufr.FONT_HEIGHT + 8 + targetY + 1 + (countForModule * ufr.FONT_HEIGHT + 2)), m.isEnabled ? 0xFFFFFFFF : new Color(200, 200, 200, 200).getRGB());
                countForModule++;
            }

        }
    }

    public void onKeyPress(int key){
        if(key == Keyboard.KEY_UP){
            if(index > 0 && !open) {
                index--;
            }
            if(open && moduleIndex > 0){
                moduleIndex--;
            }
        }
        if(key == Keyboard.KEY_DOWN){
            if(index <= Category.values().length - 2 && !open) {
                index++;
            }
            if(open && moduleIndex <= Execution.instance.moduleManager.getModulesInCategory(Category.values()[index]).size() + index - 2){
                moduleIndex++;
            }
        }
        if(key == Keyboard.KEY_RIGHT){
            if(!open) {
                open = true;
                moduleIndex = 0;
            }else {
                Execution.instance.moduleManager.getModulesInCategory(Category.values()[index]).get(moduleIndex).toggle();
            }
        }
        if(key == Keyboard.KEY_LEFT){
            open = false;
        }
    }

    public static int rainbow(int delay, int alpha) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
    }
}
