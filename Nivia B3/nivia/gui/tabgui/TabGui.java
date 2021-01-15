package nivia.gui.tabgui;

import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.events.EventTarget;
import nivia.events.events.EventKeyPress;
import nivia.events.events.EventTick;
import nivia.gui.tabgui.TabGuiItem.GuiItem;
import nivia.managers.EventManager;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module.Category;
import nivia.modules.render.GUI;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils.R2DUtils;

import java.util.ArrayList;
import java.util.Objects;

public final class TabGui  {
    private final Minecraft mc = Minecraft.getMinecraft();

    private int guiHeight = 0;
    private boolean justSwitched = false;
    public boolean mainMenu = true;
    public boolean moduleMenu = false;
    public boolean valueMenu = false;
    private int selectedItem = 0;
    public int selectedTab = 0;
    public boolean vselected = false;
    public int selectedValue = 0;
    private int tabHeight = 12;
    private final ArrayList<TabGuiItem> tabs = new ArrayList<>();
    private int transition = 0;
    private float transition2 = 0;
    private int memes;
    private boolean visible = true;
    public GUI gui;

    public Property<Boolean> BIG = new Property<Boolean>(null, "Size", false, false);
    public Property<Boolean> tanimation = new Property<Boolean>(null, "Text Animation", true);

    public PropertyManager.DoubleProperty width = new PropertyManager.DoubleProperty(null, "Width", 60, 50 , 100);
    public TabGui() {
    	updateTabs(BIG.value);
        EventManager.register(this);
    }

    public void updateTabs(boolean big){
    	tabs.clear();
    	selectedTab = selectedItem = 0;
    	if(!big)
    		guiHeight -= 5;
    	 for (final Category category : Category.values()) {
             if (Objects.equals(category, Category.NONE)) 
                 continue;
             if(!big){
            	 if (Objects.equals(category, Category.MISCELLANEOUS)) 
                    continue;
                 if (Objects.equals(category, Category.GHOST))
                     continue;
             }
             String name = StringUtils.capitalize(category.name().toLowerCase());
             final TabGuiItem tab = new TabGuiItem(this, name);
             Pandora.getModManager().mods.stream().filter(m -> m.getCategory().equals(category)).forEach(m -> tab.getModules().add(new GuiItem(m)));
             /*for(Module m : Pandora.getModManager().mods){
             	if(m.getCategory().equals(category))
             		tab.getModules().add(new GuiItem(m));
             }*/
             this.tabs.add(tab);
         }       
    }
    public void drawGui(int x, int y) {
    	this.guiHeight = this.tabs.size() * this.tabHeight;
        //Made it like this to edit it on debug ;)
    	//(GUI) Pandora.getModManager().getModule(GUI.class);
        int s = BIG.value ? 60 : 50;
        int sb = BIG.value ? 76 : 60;
        int w = (int) width.getValue();
        if(w < (gui.font.value ? s : sb)) w = (gui.font.value ? s : sb);
        int guiWidth = w;
        switch (gui.mode.value.getName()){
            case "Pandora":
                Helper.get2DUtils().drawBorderedRect(x, y - 1, x + guiWidth - 2, y + this.guiHeight + (BIG.value ? 7 : 5), Helper.colorUtils().RGBtoHEX(30,30,30, 165), 0x80000000);
                break;
            case "Nivia":
                Helper.get2DUtils().drawBorderedRect(x, y, x + guiWidth - 2, y + this.guiHeight, Helper.colorUtils().transparency(Helper.colorUtils().RGBtoHEX(30,30,30, 255), 0.75), 0x80000000);
                Helper.get2DUtils().drawRect(x, y, x + 1, y + this.guiHeight, gui.getTabGUIColor());
                break;
            default:
                Helper.get2DUtils().drawBorderedRect(x, y, x + guiWidth - 2, y + this.guiHeight, Helper.colorUtils().transparency(Helper.colorUtils().RGBtoHEX(30,30,30, 255), 0.75), 0x80000000);
        }
        for (int i = 0; i < tabs.size(); i++) {
            int transitionTop = !this.mainMenu ? 0 : this.transition + (Objects.equals(this.selectedTab, 0) && this.transition < 0 ? -this.transition : 0);
            int transitionBottom = !this.mainMenu ? 0 : this.transition + (Objects.equals(this.selectedTab, this.tabs.size() - 1) && this.transition > 0 ? -this.transition : 0);
            if (Objects.equals(this.selectedTab, i)) {
            	int hexColor = 0;
            	switch (this.selectedTab){
            	case 0: hexColor = Helper.colorUtils().RGBtoHEX(210, 90, 0, 255); break;
            	case 1: hexColor = Helper.colorUtils().RGBtoHEX(210, 140, 0, 255); break;
            	case 2: hexColor = Helper.colorUtils().RGBtoHEX(200, 25, 255, 255); break;
            	case 3: hexColor = Helper.colorUtils().RGBtoHEX(40, 200, 135, 255); break;
            	case 4: hexColor = Helper.colorUtils().RGBtoHEX(0, 150, 210, 255); break;
            	case 5: hexColor = Helper.colorUtils().RGBtoHEX(150, 150, 150, 255); break;
            	default: hexColor = Helper.colorUtils().RGBtoHEX(97, 126, 255, 255); break;
            	}

                switch (gui.mode.value.getName()){                    
                    case "Pandora":
                        R2DUtils.drawBorderedRect(x, i * 13 + y + transitionTop - 1, x + guiWidth - 2, i + (y + 13 + (i * 12)) + transitionBottom, GUI.getTabGUIColor(), 0);
                        break;
                    case "Nivia":
                        R2DUtils.drawBorderedRect(x + 1, i * 12 + y + transitionTop, x + guiWidth - 2, i + (y + 12 + (i * 11)) + transitionBottom, 0xEE000000, 0x80000000);
                        break;
                    default:
                        R2DUtils.drawBorderedRect(x, i * 12 + y + transitionTop, x + guiWidth - 2, i + (y + 12 + (i * 11)) + transitionBottom, -1878999041, 0x80000000);
                }
            }
        }
        int yOff = y + 2;
        for (int i = 0; i < this.tabs.size(); i++) {
            final TabGuiItem tab = this.tabs.get(i);
            if(!tanimation.value)
                transition2 = 0;
            switch (gui.mode.value.getName()){
                case "Pandora":
                    Helper.get2DUtils().drawStringWithShadow(tab.getTabName(), x + 2 + (selectedTab == i ? transition2 : 0), yOff, isBigger() ? 0xFFDDDDDD : 0xFFC7C7C7);
                    break;
                case "Nivia":
                    Helper.get2DUtils().drawStringWithShadow(tab.getTabName(), x + 3 + (selectedTab == i ? transition2 : 0), yOff, (selectedTab == i ) ? gui.getTabGUIColor() : 0xFFC7C7C7);
                    break;
                default:
                    Helper.get2DUtils().drawStringWithShadow(tab.getTabName(), x + 2 + (selectedTab == i ? transition2 : 0), yOff, isBigger() ? 0xFFDDDDDD : 0xFFC7C7C7);
            }

            if (Objects.equals(this.selectedTab, i) && !this.mainMenu) {
                tab.drawTabMenu(this.mc, x + guiWidth, yOff - (isBigger() ? 2 : 1));
            }
            if (Objects.equals(this.selectedTab, i) && valueMenu && this.tabs.get(this.selectedTab).getModules().get(selectedItem).getModule().equals(getSelectedTab().getValueDrawer().getModule())) {
                getSelectedTab().getValueDrawer().drawValues(mc, getSelectedTab().x + getSelectedTab().menuWidth + 3, getSelectedTab().y );
            }
            if(getSelectedTab().getValueDrawer() == null){
                if(valueMenu) {
                    valueMenu = false;
                    moduleMenu = true;
                }
            }
            yOff += this.tabHeight + (isBigger() ? 1 : 0);
        }
        if (this.transition > 0) {
            this.transition -= 1;
        } else if (this.transition < 0) {
            this.transition += 1;
        }

        if (this.transition2 < 0) {
            this.transition2 += 0.1F;
        }
    }
    public boolean isBigger(){
        return gui.mode.value.equals(gui.pandorahud);
    }
   @EventTarget
   public void onkeyPress(EventKeyPress e) {
       GUI sallosGui = (GUI) Pandora.getModManager().getModule(GUI.class);
       if (!sallosGui.tabGui.value || !sallosGui.getState())
           return;
       final EventKeyPress input = (EventKeyPress) e;
       switch (input.getEventKey()) {
           case Keyboard.KEY_UP: {
               if (this.mainMenu) {
                   this.selectedTab--;
                   if (this.selectedTab < 0) {
                       this.selectedTab = this.tabs.size() - 1;
                   }
                   this.transition2 = 2.3f;
                   this.transition = 11;
               }  if (moduleMenu) {
                   this.selectedItem--;
                   if (this.selectedItem < 0) {
                       this.selectedItem = (this.tabs.get(this.selectedTab)).getModules().size() - 1;
                   }

                   if (this.tabs.get(this.selectedTab).getModules().size() > 1) {
                       this.transition = 11;
                       this.transition2 = 2.3f;
                   }
               } if (valueMenu) {
                   if (vselected) {
                	   vselected = false;
                   }
                   this.selectedValue--;
                   if (this.selectedValue < 0) {
                       this.selectedValue = getSelectedTab().getValueDrawer().getProperties().size() - 1;
                   }

                   if (getSelectedTab().getValueDrawer().getProperties().size() > 1) {
                       this.transition = 11;
                       this.transition2 = 2.3f;
                   }
               }
               break;
           }

           case Keyboard.KEY_DOWN: {

               if (this.mainMenu) {
                   this.selectedTab++;
                   if (this.selectedTab > this.tabs.size() - 1) {
                       this.selectedTab = 0;
                   }

                   this.transition = -11;
                   this.transition2 = 2.3f;
               }  if (moduleMenu) {
                   this.selectedItem++;
                   if (this.selectedItem > (this.tabs.get(this.selectedTab)).getModules().size() - 1) {
                       this.selectedItem = 0;
                   }

                   if (this.tabs.get(this.selectedTab).getModules().size() > 1) {
                       this.transition = -11;
                       this.transition2 = 2.3f;
                   }
               }  if (valueMenu) {
                   if (vselected) {
                	   vselected = false;
                   }
                   this.selectedValue++;
                   if (this.selectedValue > (getSelectedTab().getValueDrawer().getProperties().size() - 1)) {
                       this.selectedValue = 0;
                   }

                   if (getSelectedTab().getValueDrawer().getProperties().size() > 1) {
                       this.transition = -11;
                       this.transition2 = 2.3f;
                   }
               }
           }
           break;
           case Keyboard.KEY_LEFT: {
               if (moduleMenu) {
                   selectedItem = 0;
                   mainMenu = true;
                   moduleMenu = false;
                   valueMenu = false;
               }
               if (valueMenu) {
            	   if (vselected)
            		   getSelectedTab().getValueDrawer().decreaseSelected();
                   if (!vselected) {
                       mainMenu = false;
                       moduleMenu = true;
                       valueMenu = false;
                   }
               }
               break;
           }

           case Keyboard.KEY_RIGHT: {
               if (this.mainMenu) {
                   moduleMenu = true;
                   this.mainMenu = false;
                   valueMenu = false;
                   this.selectedItem = 0;
               } else if (moduleMenu) {
                   try {
                       getSelectedTab().selectedItem().getModule().Toggle();
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
               } else if (valueMenu) {
                   if (getSelectedTab().getValueDrawer().getSelectedValue() instanceof PropertyManager.DoubleProperty) {
                       if (vselected)
                		   getSelectedTab().getValueDrawer().increaseSelected();
                       vselected = true;
                   } else {
                       getSelectedTab().getValueDrawer().changeSelected();
                   }
               }
               break;
           }

           case Keyboard.KEY_RETURN: {
               if (moduleMenu) {
                   if (getSelectedTab().getValueDrawer().getProperties().size() > 0) {
                       valueMenu = true;
                       moduleMenu = false;
                   } else ((this.tabs.get(this.selectedTab)).getModules().get(this.selectedItem)).getModule().Toggle();
               } else if (valueMenu) {
                   if (getSelectedTab().getValueDrawer().getSelectedValue() instanceof PropertyManager.DoubleProperty) {
                       vselected = !vselected;
                   } else {
                       getSelectedTab().getValueDrawer().changeSelected();
                   }
               }
               break;
           }
       }
   }

    @EventTarget
    public void onTick(EventTick e) {
    	if (!vselected)
    		this.justSwitched = false;
        if (valueMenu && Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if (vselected) {
                memes++;
                if(memes >= 20)
                    getSelectedTab().getValueDrawer().decreaseSelected();
            }
        }  if (valueMenu && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (vselected) {
                memes++;
                if(memes >= 20)
                    getSelectedTab().getValueDrawer().increaseSelected();
            }
        }
        if((!Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) || !valueMenu )memes = 0;
    }
    public TabGuiItem getSelectedTab() {
        return  this.tabs.get(this.selectedTab);
    }
    public int getSelectedItem() {
        return selectedItem;
    }
    public int getSelectedValue() {
        return selectedValue;
    }

    public int getTabHeight() {
        return tabHeight;
    }

    public int getTransition() {
        return transition;
    }

    public float getTransition2() {
        return transition2;
    }
}
