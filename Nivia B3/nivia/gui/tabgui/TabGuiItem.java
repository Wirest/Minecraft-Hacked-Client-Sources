package nivia.gui.tabgui;

import net.minecraft.client.Minecraft;
import nivia.Pandora;
import nivia.managers.PropertyManager;
import nivia.modules.Module;
import nivia.modules.render.GUI;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils.R2DUtils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Locale;


public final class TabGuiItem {
    public final TabGui gui;
    private final ArrayList<GuiItem> modules = new ArrayList<>();
    public int menuHeight = 0;
    public int menuWidth = 0;
    public int x = 0;
    public int y = 0;
    private String tabName;

    public TabGuiItem(final TabGui gui, final String tabName) {
        this.gui = gui;
        this.tabName = tabName;
    }

    public GuiItem selectedItem() {
        return getModules().get(gui.getSelectedItem());
    }

    public TabGuiValue getValueDrawer() {
        if(selectedItem() == null) return null;
        return new TabGuiValue(selectedItem().getModule(), gui);
    }

    public void drawTabMenu(final Minecraft mc, final int x, final int y) {
        this.countMenuSize(mc);
        this.x = x;
        this.y = y;
        int boxY = y;
        int yOff = gui.getTabHeight() + (gui.isBigger() ? 1 : 0);
        switch(gui.gui.mode.value.getName()){
            case "Nivia":
            	 R2DUtils.drawBorderedRect(x, y - 1, x + this.menuWidth + 2, y + this.menuHeight, Helper.colorUtils().RGBtoHEX(30,30,30, 165), 0xFF000000);
            	 break;
            case "Pandora":
                R2DUtils.drawBorderedRect(x, y - 1, x + this.menuWidth + 2, y + this.menuHeight, Helper.colorUtils().RGBtoHEX(30,30,30, 165), 0xFF000000);
                break;
            default:
                R2DUtils.drawBorderedRect(x, y, x + this.menuWidth + 2, y + this.menuHeight, 0x99000000, 0x99000000);
        }
        for (int i = 0; i < this.modules.size(); i++) {
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
            if (this.gui.getSelectedItem() == i) {
                int transitionTop = !gui.moduleMenu ? 0 :(this.gui.getTransition() + (this.gui.getSelectedItem() == 0 && this.gui.getTransition() < 0 ? -this.gui.getTransition() : 0));
                int transitionBottom = !gui.moduleMenu ? 0 : (this.gui.getTransition() + (this.gui.getSelectedItem() == this.modules.size() - 1 && this.gui.getTransition() > 0 ? -this.gui.getTransition() : 0));

                switch(gui.gui.mode.value.getName()){
                    case "Nivia":
                    	R2DUtils.drawBorderedRect(x, boxY + transitionTop - 1, x + this.menuWidth + 2, boxY + 12 + transitionBottom, 0xEE000000, 0);
                    	break;
                    case "Pandora":
                        R2DUtils.drawBorderedRect(x, boxY + transitionTop - 1, x + this.menuWidth + 2, boxY + 12 + transitionBottom + 1, GUI.getTabGUIColor(), 0);
                        break;
       
                    default:
                        R2DUtils.drawBorderedRect(x, boxY + transitionTop, x + this.menuWidth + 2, boxY + 12 + transitionBottom, -1878999041, 0x80000000);
                }
            }
            Collator collator = Collator.getInstance(Locale.US);
            this.modules.sort((mod1, mod2) -> collator.compare(mod1.getModule().getName(), mod2.getModule().getName()));
            int memes = hexColor;
            if(memes == Helper.colorUtils().RGBtoHEX(150, 150, 150, 255)) memes = Helper.colorUtils().RGBtoHEX(100, 100, 100, 255);
            Module mod = modules.get(i).getModule();

            float transitions = (gui.getSelectedItem() == i ? gui.getTransition2() : 0);
            if(!gui.tanimation.value)
                transitions = 0;
            boolean p = !Pandora.getPropertyManager().getPropertiesFromModule(mod).isEmpty();
            switch(gui.gui.mode.value.getName()){
                case "Nivia":
                	Helper.get2DUtils().drawStringWithShadow(mod.getTag(), x + 2 + transitions, y + yOff * i + 1.5F, (mod.getState() ? GUI.getTabGUIColor() : 0xFFDDDDDD));
                    if(p)R2DUtils.drawBorderedRect(x + menuWidth, boxY - 1, x + this.menuWidth + 2, boxY + 12,GUI.getTabGUIColor(), 0);
                    break;
                case "Pandora":
                    Helper.get2DUtils().drawStringWithShadow(mod.getTag(), x + 2 + transitions, y + yOff * i + 2, (mod.getState() ? GUI.getTabGUIColor() : 0xFFDDDDDD));
                    if(p)R2DUtils.drawBorderedRect(x + menuWidth, boxY - 1, x + this.menuWidth + 2, boxY + 12  + 1, GUI.getTabGUIColor(), 0);
                	break;
                

            }
          //  yOff += (gui.theme.value.equals(Theme.Spirit) ? 1 : 0);
            boxY += (gui.isBigger()? 13 : 12);
            
        }
    }

    private void countMenuSize(final Minecraft mc) {
        int maxWidth = 0;
        for (GuiItem module : this.modules) {
            if (Helper.get2DUtils().getStringWidth(module.getModule().getName()) > maxWidth) {
                maxWidth = Helper.get2DUtils().getStringWidth(module.getModule().getName(), 10) + 6;
            }
        }

        this.menuWidth = maxWidth;
        this.menuHeight = this.modules.size() * (this.gui.getTabHeight() + (gui.isBigger() ? 1 : 0));
    }

    public String getTabName() {
        return tabName;
    }

    public ArrayList<GuiItem> getModules() {
        return modules;
    }
    
    //TODO: GuiItem
    public static class GuiItem {
        private final Module mod;

        public GuiItem(final Module mod) {
            this.mod = mod;
        }
        public Module getModule() {
            return mod;
        }
    }
    public static class GuiValue {
        private final Module mod;
        private Module.Category category;
        private GuiItem parent;

        public GuiValue(final Module mod, Module.Category category, GuiItem item) {
            this.mod = mod;
            this.category = category;
            parent = item;
        }
        public Module getModule() {
            return mod;
        }
        public Module.Category getCategory() { return category; }
        public ArrayList<PropertyManager.Property> getModuleProperties() { return mod.getModuleProperties(); }
    }
}
