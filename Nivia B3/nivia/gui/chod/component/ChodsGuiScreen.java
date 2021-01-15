package nivia.gui.chod.component;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IntHashMap;
import nivia.Pandora;
import nivia.gui.chod.ChodsGui;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.modules.Module.Category;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ChodsGuiScreen {

    private int width, height;
    private Category category;
    private ChodsGui parent;

    private static final IntHashMap hash = new IntHashMap();
    public int selectedColor = RenderUtils.getIntFromColor(new Color(75, 177, 219));
    public int hoverColor    = RenderUtils.getIntFromColor(new Color(60, 141, 175));
    public int inline        = RenderUtils.getIntFromColor(new Color(57, 57, 57));
    public int background    = RenderUtils.getIntFromColor(new Color(90, 91, 91));

    public int insideColor = RenderUtils.getIntFromColor(new Color(60, 60, 60));

    int selectedIndex = 0;

    private Module selectedModule;

    private ArrayList<ChodsComponent> components = new ArrayList<>();

    public ChodsGuiScreen(int width, int height, Category category, ChodsGui parent) {
        this.width    = width;
        this.height   = height;
        this.category = category;
        this.parent = parent;
        Pandora.getModManager().getModulesInCategory(category).forEach(mod -> {
        	mod.getModuleProperties().stream().filter(mv -> mv instanceof DoubleProperty).forEach(mv -> {
        		if (((DoubleProperty)mv).colorSlider)
        			components.add(new ChodsHueSlider(mod, (DoubleProperty)mv));
        		 else
        		components.add(new ChodsSlider(mod, (DoubleProperty) mv));
        	});
        	mod.getModuleProperties().stream().filter(mv -> mv.value instanceof Boolean).forEach(mv -> {
        		ChodsCheckbox checkbox = new ChodsCheckbox(mod, mv);
        		components.add(checkbox);
        	});
        	mod.getModuleProperties().stream().filter(mv -> mv.value instanceof Enum).forEach(mv -> {
        		components.add(new ChodsMode(mod, mv));
        	});
        	components.add(new ChodsKeybind(mod));
        });
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
   
    	
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();

        int x1 = parent.posX;
        int x2 = parent.posX + width;
        int y1 = parent.posY;
        int y2 = parent.posY + height;
        RenderUtils.layeredRect(x1, y1, x2, y2, parent.outline, parent.inline, parent.background);
        int insideBuffer = 10;

        RenderUtils.drawBorderedRect(x1 + insideBuffer, y1 + insideBuffer * 3, x2 - insideBuffer, y2 - insideBuffer, 1, parent.insideColor1, parent.insideInline);
        String someText;
        if (!(this.category == null))  {
            someText = category.getName();
        }
        else {
            someText = "Config";
        }
        //RenderUtils.cgothicgui.drawStringWithShadow("�l" + someText, x1 + insideBuffer, y1 + 9, -1);
        //Wrapper.getFontRenderer().drawStringWithShadow(EnumChatFormatting.BOLD + someText, x1 + insideBuffer, y1 + 9, -1);

        // Module Button Start
        int modBuffer = 10;
        int y = y1 + insideBuffer * 3 + modBuffer;
        int modHeight = 24;
        int modWidth  = 200;

        RenderUtils.drawBorderedRect(x1 + insideBuffer + modBuffer, y1 + insideBuffer * 3 + modBuffer, x1 + modWidth - modBuffer, y2 - insideBuffer - modBuffer, 1, background, inline);
        for (int i = 0; i< Pandora.getModManager().getModulesInCategory(category).size(); i++) {
            Module mod = Pandora.getModManager().getModulesInCategory(category).get(i);
            if (selectedIndex == i) {
                this.selectedModule = mod;
                RenderUtils.drawRectWH(x1+insideBuffer+modBuffer + 1, y + 1, modWidth - 3*modBuffer - 2, modHeight, selectedColor);
            }
            else if (isInside(scaledMouseX, scaledMouseY, x1+insideBuffer+modBuffer + 1, y + 1, modWidth - 3*modBuffer - 2, modHeight)) {
                RenderUtils.drawRectWH(                   x1+insideBuffer+modBuffer + 1, y + 1, modWidth - 3*modBuffer - 2, modHeight, hoverColor);
            }
            if (mod.getState()) {
                RenderUtils.drawRectWH(x1 + modWidth - 3*modBuffer + 13, y + 1, 6, modHeight, selectedIndex == i ? 0xFF55D987 :0x9955D987);
            }
            else {
                RenderUtils.drawRectWH(x1 + modWidth - 3*modBuffer + 13, y + 1, 6, modHeight, selectedIndex == i ? 0xFFD95555 : 0x99D95555);
            }
            RenderUtils.cgothicgui.drawStringWithShadow(EnumChatFormatting.BOLD + mod.getTag(), isInside(scaledMouseX, scaledMouseY, x1+insideBuffer+modBuffer + 1, y + 1, modWidth - 3*modBuffer - 2, modHeight) ? x1 + insideBuffer + 10 + modBuffer : x1 + insideBuffer + 5 + modBuffer, y + 6, -1);
            y+= modHeight;
        }
        // Module Button End

        // Options Start
        int interval = 0;
        int optionsX = x1 + modWidth;
        int optionsY = y1 + insideBuffer * 3 + modBuffer;
        int optionsHeight = 24;
        int optionsWidth = 200;
        RenderUtils.drawBorderedRect(optionsX, y1 + insideBuffer * 3 + modBuffer, x2 - modBuffer - insideBuffer, y2 - insideBuffer - modBuffer, 1, insideColor, background);
 
        for (ChodsComponent component : components) {
            if (component.getMod().equals(selectedModule)) {
                component.onUpdate(scaledMouseX, scaledMouseY);
                if (interval < 15) {
                    component.setX(optionsX);
                    component.setY(optionsY);
                }
                else {
                    component.setX(optionsX + modWidth + modBuffer);
                    component.setY(optionsY - (optionsHeight*15));
                }
                component.setHeight(optionsHeight);
                component.setWidth(optionsWidth);
                component.drawElement(scaledMouseX, scaledMouseY, partialTicks);
                optionsY += optionsHeight;
                interval++;
            }
        }
        // Options End
    
    }
    public void mouseReleased(int mouseX, int mouseY, int state) {
    	int scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();
    	components.stream().filter(c -> c.getMod().equals(selectedModule)).forEach(c -> c.mouseReleased(scaledMouseX, scaledMouseY, state));
        /*for (ChodsComponent component : components) {
            if (component.getMod().equals(selectedModule)) {
                component.mouseReleased(mouseX, mouseY, state);
            }
        }*/
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        //The gui is scaled down by two, so we need to scale down the mouseX and mouseY by 2
        int scaledMouseX = mouseX * Helper.get2DUtils().scaledRes().getScaleFactor();
        int scaledMouseY = mouseY * Helper.get2DUtils().scaledRes().getScaleFactor();
        int x1 = parent.posX;
        int y1 = parent.posY;
        int insideBuffer = 10;
        int modBuffer = 10;
        int y = y1 + insideBuffer * 3 + modBuffer;
        int modHeight = 24;
        int modWidth  = 200;

        for (int i = 0; i< Pandora.getModManager().getModulesInCategory(category).size(); i++) {
            if (isInside(mouseX, mouseY, x1+insideBuffer+modBuffer + 1, y + 1, modWidth - 3*modBuffer - 2, modHeight)) {
                if (mouseButton != 0) {
                    Module mod = Pandora.getModManager().getModulesInCategory(category).get(i);
                    mod.setState(!mod.getState());
                }
                this.selectedIndex = i;
            }
            y+= modHeight;
        }

        for (ChodsComponent component : components) {
            if (component.getMod().equals(selectedModule)) {
                component.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
            }
        }
    }
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (ChodsComponent component : components) {
            if (component.getMod().equals(selectedModule)) {
            	component.keyTyped(typedChar, keyCode);
            }
        }
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(parent);
            parent.selectedScreen = null;
        }
    }
    public boolean isInside(int pointX, int pointY, int x, int y, int width, int height) {
        return (pointX >= x && pointX <= x + width) && (pointY >= y && pointY <= y + height);
    }
}
