package nivia.gui.tabgui;


import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.modules.ModuleMode;
import nivia.modules.combat.KillAura;
import nivia.modules.render.GUI;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.RenderUtils;

import java.util.ArrayList;

public class TabGuiValue {

    private Module mod;
    private TabGui gui;
    public int menuHeight = 0;
    public int menuWidth = 0;
    public boolean selected;


    public TabGuiValue(Module selectedMod, TabGui gui) {
        mod = selectedMod;
        this.gui = gui;
    }

    public void drawValues(Minecraft mc, int x , int y) {
        x += 1;
        int boxY = y;
        int yOff = gui.getTabHeight() + (gui.isBigger() ? 1 : 0);
        countMenuSize(mc);
        switch(gui.gui.mode.value.getName()){
            case "Nivia":
                RenderUtils.R2DUtils.drawBorderedRect(x, y - 1, x + this.menuWidth + 2, y + this.menuHeight, Helper.colorUtils().RGBtoHEX(30,30,30, 165), 0xFF000000);
                break;
            case "Pandora":
                RenderUtils.R2DUtils.drawBorderedRect(x, y - 1, x + this.menuWidth + 2, y + this.menuHeight, Helper.colorUtils().RGBtoHEX(30,30,30, 165), 0xFF000000);
                break;
         
            default:
                RenderUtils.R2DUtils.drawBorderedRect(x, y, x + this.menuWidth + 2, y + this.menuHeight, 0x99000000, 0x99000000);
        }
        for(int i = 0; i < getProperties().size(); i++) {
            PropertyManager.Property p = getProperties().get(i);
            if (p.value instanceof Boolean || p.value instanceof Enum || p.value instanceof ModuleMode) {
                drawBooleanProperties(i, x, y, boxY, yOff, p);
            } else if (p instanceof PropertyManager.DoubleProperty) {
                drawSliderProperties(i, x, y, yOff, boxY, p);
            }
            boxY += gui.isBigger() ? 13 : 12;
        }
    }
    private void countMenuSize(final Minecraft mc) {
        int maxWidth = 0;
        for (PropertyManager.Property property : getProperties()) {
            String w;
            if(property instanceof PropertyManager.DoubleProperty) w = property.getName() +": " + String.valueOf(((PropertyManager.DoubleProperty) property).getValue());
            else if(property.value instanceof Enum) w = "Mode: " + ((Enum) property.value).name();
            else w =  property.getName() + " ";
            if (Helper.get2DUtils().getStringWidth(w) > maxWidth) {
                maxWidth = Helper.get2DUtils().getStringWidth(w, 9) + (gui.tanimation.value ? 4 : 2);

            }
        }
        this.menuWidth = maxWidth;
        this.menuHeight = getProperties().size() * (this.gui.getTabHeight() + (gui.isBigger()? 1 : 0));
    }
    public void drawSliderProperties(int i, int x, int y, int yOff, int boxY, PropertyManager.Property pr) {
        int hexColor = 0;
        PropertyManager.DoubleProperty p = (PropertyManager.DoubleProperty) pr;
        switch (this.gui.selectedTab){
            case 0: hexColor = Helper.colorUtils().RGBtoHEX(210, 90, 0, 255); break;
            case 1: hexColor = Helper.colorUtils().RGBtoHEX(210, 140, 0, 255); break;
            case 2: hexColor = Helper.colorUtils().RGBtoHEX(200, 25, 255, 255); break;
            case 3: hexColor = Helper.colorUtils().RGBtoHEX(40, 200, 135, 255); break;
            case 4: hexColor = Helper.colorUtils().RGBtoHEX(0, 150, 210, 255); break;
            case 5: hexColor = Helper.colorUtils().RGBtoHEX(150, 150, 150, 255); break;
            default: hexColor = Helper.colorUtils().RGBtoHEX(97, 126, 255, 255); break;
        }
       // if (!gui.vselected)
       //  drawSelectedRect(i, x, boxY, hexColor);
         //if (gui.vselected)
        	// drawSelectedRect(i,x,boxY,Helper.colorUtils().RGBtoHEX(30,30,30, 255));
        if (gui.vselected)
        	drawSliderRect(i,x,boxY,hexColor);
        else
        	drawSelectedRect(i,x,boxY,hexColor);
        int memes = hexColor;
        if(memes == Helper.colorUtils().RGBtoHEX(150, 150, 150, 255)) memes = Helper.colorUtils().RGBtoHEX(100, 100, 100, 255);
        float transitions = (gui.getSelectedValue() == i ? gui.getTransition2() : 0);
        if(!gui.tanimation.value)
            transitions = 0;
        double percent = (-2 + menuWidth * ((p.getValue() - p.min) / (p.max - p.min)));
        String meme = String.format("%s: %s%s", p.getName(), EnumChatFormatting.GRAY, p.getValue());
        switch (gui.gui.mode.value.getName()) {
            case "Nivia":
            	RenderUtils.R2DUtils.drawBorderedRect(x, boxY + 8, (float) (x + menuWidth - menuWidth + 3.5f + percent), boxY + 12, Helper.colorUtils().darker(GUI.getTabGUIColor()),0);
                Helper.get2DUtils().drawStringWithShadow(meme, x + 2 + transitions, y + yOff * i , Helper.colorUtils().brighter(GUI.getTabGUIColor()));
                break;
            case "Pandora": {
            	RenderUtils.R2DUtils.drawBorderedRect(x, boxY + 7, (float) (x + menuWidth - menuWidth + 3.5f + percent), boxY + 12, GUI.getTabGUIColor(),0);
                Helper.get2DUtils().drawStringWithShadow(meme, x + 2 + transitions, y + yOff * i - 1, GUI.getTabGUIColor());
            }
                break;
           
        }
    }

    public void drawBooleanProperties(int i, int x, int y, int boxY, int yOff, PropertyManager.Property p) {
        int hexColor = 0;
        switch (this.gui.selectedTab){
            case 0: hexColor = Helper.colorUtils().RGBtoHEX(210, 90, 0, 255); break;
            case 1: hexColor = Helper.colorUtils().RGBtoHEX(210, 140, 0, 255); break;
            case 2: hexColor = Helper.colorUtils().RGBtoHEX(200, 25, 255, 255); break;
            case 3: hexColor = Helper.colorUtils().RGBtoHEX(40, 200, 135, 255); break;
            case 4: hexColor = Helper.colorUtils().RGBtoHEX(0, 150, 210, 255); break;
            case 5: hexColor = Helper.colorUtils().RGBtoHEX(150, 150, 150, 255); break;
            default: hexColor = Helper.colorUtils().RGBtoHEX(97, 126, 255, 255); break;
        }
        drawSelectedRect(i, x, boxY, hexColor);
        int memes = hexColor;
        if(memes == Helper.colorUtils().RGBtoHEX(150, 150, 150, 255)) memes = Helper.colorUtils().RGBtoHEX(100, 100, 100, 255);
        float transitions = (gui.getSelectedValue() == i ? gui.getTransition2() : 0);
        if(!gui.tanimation.value)
            transitions = 0;
        if(p.value instanceof Boolean) {
            switch (gui.gui.mode.value.getName()) {
                case "Nivia":
                    Helper.get2DUtils().drawStringWithShadow(p.getName(), x + 2 + transitions, y + yOff * i + 2, ((boolean) p.value ?
                            GUI.getTabGUIColor() : 0xFFDDDDDD));
                    break;
                case "Pandora":
                    Helper.get2DUtils().drawStringWithShadow(p.getName(), x + 2 + transitions, y + yOff * i + 2, (((boolean) p.value ?
                            GUI.getTabGUIColor() : 0xFFDDDDDD)));
                    break;
               
                default:
                    Helper.get2DUtils().drawStringWithShadow(p.getName(), x + 2 + transitions, y + yOff * i + 2, ((boolean) p.value ?
                            0xFFFFFFFF : 0xFFAAAAAA));
            }
        }
        else if(p.value instanceof Enum || p.value instanceof ModuleMode) {
            String name = "";
            if(p.value instanceof Enum) name = ((Enum) p.value).name();
            if(p.value instanceof ModuleMode) name = ((ModuleMode)p.value).getName();
            String meme = p.getName() + ": "+ EnumChatFormatting.GRAY + name;
            //String meme = String.format("Mode: %s%s", EnumChatFormatting.GRAY, name);
            switch (gui.gui.mode.value.getName()) {
                case "Nivia":
                    Helper.get2DUtils().drawStringWithShadow(meme, x + 2 + transitions, y + yOff * i + 1    , Helper.colorUtils().brighter(GUI.getTabGUIColor()));
                    break;
                case "Pandora":
                    Helper.get2DUtils().drawStringWithShadow(meme, x + 2 + transitions, y + yOff * i + 2, Helper.colorUtils().brighter(GUI.getTabGUIColor()));
                    break;
                
            }
        }
        //  yOff += (gui.gui.mode.value.getName().equals(Theme.Spirit) ? 1 : 0);
    }
    public void drawSelectedRect(int i, int x, int boxY, int hexColor){
        if (this.gui.getSelectedValue() == i) {
            int transitionTop = this.gui.getTransition() + (this.gui.getSelectedValue() == 0 && this.gui.getTransition() < 0 ? -this.gui.getTransition() : 0);
            int transitionBottom = this.gui.getTransition() + (this.gui.getSelectedValue() == this.getProperties().size() - 1 && this.gui.getTransition() > 0 ? -this.gui.getTransition() : 0);
            switch(gui.gui.mode.value.getName()){
                case "Nivia":
                    RenderUtils.R2DUtils.drawBorderedRect(x, boxY + transitionTop - 1, x + this.menuWidth + 2, boxY + 12 + transitionBottom , 0xEE000000, 0);
                    break;
                case "Pandora":
                    RenderUtils.R2DUtils.drawBorderedRect(x, boxY + transitionTop - 1, x + this.menuWidth + 2, boxY + 12 + transitionBottom + 1, GUI.getTabGUIColor(), 0);
                    break;
              
                default:
                    RenderUtils.R2DUtils.drawBorderedRect(x, boxY + transitionTop, x + this.menuWidth + 2, boxY + 12 + transitionBottom, -1878999041, 0x80000000);
            }
        }
    }
    
    public void drawSliderRect(int i, int x, int boxY, int hexColor){
        if (this.gui.getSelectedValue() == i) {
            int transitionTop = this.gui.getTransition() + (this.gui.getSelectedValue() == 0 && this.gui.getTransition() < 0 ? -this.gui.getTransition() : 0);
            int transitionBottom = this.gui.getTransition() + (this.gui.getSelectedValue() == this.getProperties().size() - 1 && this.gui.getTransition() > 0 ? -this.gui.getTransition() : 0);
            switch(gui.gui.mode.value.getName()){
                case "Nivia":
                    RenderUtils.R2DUtils.drawBorderedRect(x, boxY + transitionTop - 1, x + this.menuWidth + 2, boxY + 11 + transitionBottom + 1, 0, GUI.getTabGUIColor());
                    break;
                case "Pandora":
                    RenderUtils.R2DUtils.drawBorderedRect(x, boxY + transitionTop - 2, x + this.menuWidth + 2, boxY + 11 + transitionBottom + 1, 0, Helper.colorUtils().RGBtoHEX(200, 25, 255, 255));
                    break;
              
                default:
                    RenderUtils.R2DUtils.drawBorderedRect(x, boxY + transitionTop, x + this.menuWidth + 2, boxY + 12 + transitionBottom, -1878999041, 0x80000000);
            }
        }
    }
    public Module getModule(){
        return mod;
    }
    public ArrayList<PropertyManager.Property> getProperties() {
        if(mod == null) return null;
        return sort(Pandora.getPropertyManager().getPropertiesFromModule(mod));
    }
    public static ArrayList<PropertyManager.Property> sort(ArrayList<PropertyManager.Property> objects){
        ArrayList<PropertyManager.Property> temp = new ArrayList<>();
        for(PropertyManager.Property object : objects) {
            if(!object.onClickGui) continue;
            if (object.value instanceof Enum || object.value instanceof ModuleMode) { //
                temp.add(object);
            }
        }
        for(PropertyManager.Property object : objects){
            if(!object.onClickGui) continue;
            if(object.value instanceof Double || object.value instanceof Integer || object.value instanceof Float || object instanceof PropertyManager.DoubleProperty){ //
                temp.add(object);
            }
        }
        for(PropertyManager.Property object : objects){
            if(!object.onClickGui) continue;
            if(object.value instanceof Boolean){ //
                temp.add(object);
                if(temp.size() > (20 - Helper.get2DUtils().scaledRes().getScaleFactor())) break;
            }
        }
        return temp;
    }
    public PropertyManager.Property getSelectedValue() {
        return getProperties().get(gui.selectedValue);
    }
    public void increaseSelected() {
        if(getSelectedValue() instanceof PropertyManager.DoubleProperty) {
            PropertyManager.DoubleProperty p = (PropertyManager.DoubleProperty) getSelectedValue();
            double value = p.getValue() + (p.increase == 0 ? 1 : 0.1);
            p.setValue((double)Math.round(value * 10d) / 10d);
        }
        Pandora.getFileManager().saveFiles();
    }
    public void decreaseSelected() {
        if(getSelectedValue() instanceof PropertyManager.DoubleProperty) {
            PropertyManager.DoubleProperty p = (PropertyManager.DoubleProperty) getSelectedValue();
            double value = p.getValue() - (p.increase == 0 ? 1 : 0.1);
            p.setValue((double)Math.round(value * 10d) / 10d);
        }
        Pandora.getFileManager().saveFiles();
    }
    private static int index, i;

    public void changeSelected() {
        if (getSelectedValue().value instanceof Boolean) {
            PropertyManager.Property<Boolean> p = getSelectedValue();
            getSelectedValue().value = !p.value;
        }
        if (getSelectedValue().value instanceof Enum) {
            PropertyManager.Property<Enum> p = getSelectedValue();
            Enum[] modes = p.value.getClass().getEnumConstants();
            if (index < modes.length - 1)
                index++;
            else index = 0;
            p.value = modes[index];
        }
        
        if (getSelectedValue().value instanceof ModuleMode) {
            PropertyManager.Property<ModuleMode> p = getSelectedValue();
            ArrayList<String> modes = new ArrayList<>();
            p.getModule().getModes().forEach(mode -> modes.add(mode.getName()));
            if (i < modes.size() - 1)
                i++;
            else i = 0;
            p.value = p.getModule().getMode(modes.get(i));
            KillAura.getAura().switchMode.updateProperties();
        }
        Pandora.getFileManager().saveFiles();
    }
    private enum Type {
        ENUM, SLIDER, BOOLEAN;
    }
}
