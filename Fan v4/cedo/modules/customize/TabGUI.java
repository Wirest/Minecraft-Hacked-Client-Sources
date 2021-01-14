package cedo.modules.customize;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventKey;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.elements.Draw;
import cedo.util.ColorManager;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;


public class TabGUI extends Module {
    //NumberSettings
    public NumberSetting xValue = new NumberSetting("X", 7, 1, 75, 1),
            yValue = new NumberSetting("Y", 30, 20, 150, 1);
    MinecraftFontRenderer fontRenderer = FontUtil.cleanmedium;
    NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", 15, 1, 40, 1),
    //RGB
    red = new NumberSetting("Red", 0, 1, 255, 1),
            green = new NumberSetting("Green", 0, 1, 255, 1),
            blue = new NumberSetting("Blue", 0, 1, 255, 1);


    //ModeSettings
    ModeSetting colorTheme = new ModeSetting("Color", "Rainbow", "Chill Rainbow", "Rainbow", "Custom Color");


    //BooleanSettings
    BooleanSetting thinMode = new BooleanSetting("Thin", false);

    //Color int
    int color;

    //This is the selected Tab
    int currentTab;

    boolean expanded;

    public TabGUI() {
        super("TabGUI", Keyboard.KEY_NONE, Category.CUSTOMIZE);
        addSettings(xValue, yValue, thinMode, colorTheme, rainbowSpeed, red, green, blue);
        setToggled(true);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {

            MinecraftFontRenderer font = Fan.getClientFont("Medium", false);

            //Chnaging values for thin mode
            int widthChange = 0;
            byte xChange = 0;
            byte smallXChange = 0;
            byte yChange = 0;
            float heightChange = 0;
            float cornerValue = 0;

            if (thinMode.isEnabled()) {
                xChange = 2;
                widthChange = -62;
                yChange = 1;
                heightChange = -2.2f;
                cornerValue = 1.3f;
            }


            //Color Math
            float red = (float) (this.red.getValue() / 255);
            float green = (float) (this.green.getValue() / 255);
            float blue = (float) (this.blue.getValue() / 255);


            //Configuring the Color Settings
            switch (colorTheme.getSelected()) {
                case "Chill Rainbow":
                    color = ColorManager.rainbow(1, (int) rainbowSpeed.getValue(), 0.5f, 0.8f, 1f).getRGB();
                    break;
                case "Rainbow":
                    color = ColorManager.rainbow(1, (int) rainbowSpeed.getValue(), 1f, 0.8f, 1f).getRGB();
                    break;
                case "Custom Color":
                    color = new Color(red, green, blue, 1).getRGB();
                    break;
            }


            //Background Rect
            Draw.color(0x90000000);
            Draw.drawRoundedRect(xValue.getValue(), yValue.getValue(), 65, 48 + (Category.values().length * 7), 0);

            //Slider Rect
            Draw.colorRGBA(color);
            Draw.drawRoundedRect(xValue.getValue() + xChange, yValue.getValue() + yChange + (currentTab * 15),
                    65 + widthChange, 15 + heightChange, cornerValue);


            int count = 0;
            for (Category c : Module.Category.values()) {
                boolean selected = count == currentTab;
                //Category Names
                font.drawStringWithShadow(c.name, xValue.getValue() + 3 + (selected ? 8 : 0), yValue.getValue() + 4 + (count * 15), -1);

                count++;
            }

            //MODULES
            if (expanded) {
                Category category = Module.Category.values()[currentTab];
                List<Module> modules = Fan.getModulesByCategory(category);

                //Module Background rect
                Draw.color(0x95000000);
                Draw.drawRoundedRect(xValue.getValue() + 69, yValue.getValue(), 65, (modules.size() * 15), 0);

                //Slider Rect
                Draw.colorRGBA(color);
                Draw.drawRoundedRect(xValue.getValue() + 69, yValue.getValue() + yChange + category.moduleIndex * 15,
                        65 + widthChange, 15 + heightChange, cornerValue);


                count = 0;
                for (Module m : modules) {
                    boolean selected = count == currentTab;
                    //Category Names
                    font.drawString(m.getName(), xValue.getValue() + 75, yValue.getValue() + 4 + (count * 15), -1);

                    count++;
                }
            }
        }
        if (e instanceof EventKey) {
            Category category = Module.Category.values()[currentTab];
            List<Module> modules = Fan.getModulesByCategory(category);
            int code = ((EventKey) e).code;

            //Up Arrow Key Actions
            if (code == Keyboard.KEY_UP) {
                if (expanded) {
                    if (category.moduleIndex <= 0) {
                        category.moduleIndex = modules.size() - 1;
                    } else {
                        category.moduleIndex--;
                    }
                } else {
                    if (currentTab <= 0) {
                        currentTab = Category.values().length - 1;
                    } else
                        currentTab--;
                }
            }
            //Down ArrowKey Actions
            if (code == Keyboard.KEY_DOWN) {
                if (expanded) {
                    if (category.moduleIndex >= modules.size() - 1) {
                        category.moduleIndex = 0;
                    } else {
                        category.moduleIndex++;
                    }
                } else {
                    if (currentTab >= Category.values().length - 1) {
                        currentTab = 0;
                    } else
                        currentTab++;
                }
            }
            //Right ArrowKey Actions
            if (code == Keyboard.KEY_RIGHT) {
                if (expanded) {
                    modules.get(category.moduleIndex).toggle();
                } else {
                    expanded = true;
                }
            }
            //Left ArrowKey Actions
            if (code == Keyboard.KEY_LEFT) {
                expanded = false;
            }
        }
    }
}