package cedo.modules.customize;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventKey;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.Setting;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.KeybindSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.elements.Draw;
import cedo.util.ColorManager;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;


public class TabGUI extends Module {
    public NumberSetting size = new NumberSetting("Size", 4, 1, 10, 1);
    boolean expanded;
    int currentTab, randomInt, intdes, randomint, colorForBackground;
    double lengthTop, sliderColor, randomint2;
    MinecraftFontRenderer fontsize;
    ModeSetting theme = new ModeSetting("Mode", "Chill Rainbow", "Custom Color", "Chill Rainbow", "Rainbow", "Fan");
    NumberSetting rainbowSpeed = new NumberSetting("Rainbow Speed", 30, 1, 100, 1),
            red = new NumberSetting("Red", 1, 1, 255, 1),
            green = new NumberSetting("Green", 1, 1, 255, 1),
            blue = new NumberSetting("Blue", 1, 1, 255, 1),
            corners = new NumberSetting("Corners", 4, 1, 7, 1);
    BooleanSetting Selector = new BooleanSetting("Selector", true),
            chill = new BooleanSetting("Chill Mode", true);

    public TabGUI() {
        super("TabGUI", Keyboard.KEY_NONE, Category.CUSTOMIZE);
        addSettings(theme, Selector, chill, corners, rainbowSpeed, size, red, green, blue);
        setToggled(true);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {
            HudModule hud = Fan.hudMod;
            String logo = hud.logo.getSelected();

            int selectorColorUnfocused = new Color(0x9F787878, true).getRGB();
            int selectorColorFocused = new Color(0x9EC3C3C3, true).getRGB();

            switch (logo) {

                case "ZeroTwo":
                    randomint = 80;
                    intdes = 122;
                    break;
                case "Text":
                    randomint = 0;
                    intdes = 42;
                    break;
                case "Small":
                    randomint = 15;
                    randomint2 = 51.5;
                    intdes = 57;
                    break;
                case "Medium":
                    randomint = 50;
                    intdes = 92;
                    break;
            }


            fontsize = FontUtil.cleanmedium;


            switch (theme.getSelected()) {

                case "Custom Color":
                    sliderColor = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();
                    break;
                case "Chill Rainbow":
                    sliderColor = ColorManager.rainbow(100, (int) rainbowSpeed.getValue(), 0.5f, 1, 0.8f).getRGB();
                    break;
                case "Rainbow":
                    sliderColor = ColorManager.rainbow(100, (int) rainbowSpeed.getValue()).getRGB();
                    break;
                case "Fan":
                    colorForBackground = new Color(47, 49, 54).getRGB();
                    sliderColor = new Color(173, 121, 196).getRGB();
                    break;
            }

            if (!theme.is("Fan"))
                colorForBackground = 0x9f15151a;


            int size1 = (int) (size.getValue() + 12);

            int size2 = (int) (size.getValue() + 15);
            // TabGui

            Gui.drawRect(0, 0, 0, 0, colorForBackground);
            Draw.drawRoundedRect(4, 30 + randomint, 67, Category.values().length * size2 - size1 + 2, (float) corners.getValue());
            Gui.drawRect(7, 30 + randomint + 3.5f + currentTab * size1, 67,
                    30 + randomint + 3 + 6 + currentTab * size1 + 7, 0x9f787878); // SelectedCategoryBackground

            if (Selector.isEnabled()) {
                if (chill.isEnabled()) {
                    Draw.color((int) sliderColor);
                    Draw.drawRoundedRect(6, 33.5 + randomint + currentTab * size1, 3,
                            12.5, 1);
                } else {
                    Draw.color((int) sliderColor);
                    Draw.drawRoundedRect(6, 33.5 + randomint + currentTab * size1, 62,
                            12.5, 1);
                }

            }
            int count = 0;

            String texty;

            for (Category c : Module.Category.values()) {

                if (hud.text.isEnabled()) {
                    texty = c.name.toLowerCase();
                } else
                    texty = c.name;


                boolean selected = count == currentTab;
                fontsize.drawString(texty, 9 + (selected ? 9 : 1),
                        30 + randomint + 6.5f + count * size1, -1);

                count++;
            }

            if (expanded) {
                Category category = Module.Category.values()[currentTab];
                List<Module> modules = Fan.getModulesByCategory(category);

                if (modules.size() == 0) {
                    return;
                }
                GlStateManager.color(1, 1, 1, 1);
                Draw.color(colorForBackground);
                Draw.drawRoundedRect(randomInt * 4 + 98 - 17, 36.5 + randomint, 62, modules.size() * size1 - size1 + 18, (float) corners.getValue()); // ModuleBackground

                Gui.drawRect(randomInt * 4 + (98 + 5) - 17,
                        36.5 + randomint + 2.5f + category.moduleIndex * size1,
                        randomInt * 4 + (160 - 7) - 17 + 4,
                        36.5 + randomint + 15.5f + category.moduleIndex * size1, 0x9f787878); // SelectedModuleBackground

                if (Selector.isEnabled()) {

                    Gui.drawRect(randomInt * 4 + 86,
                            36.5 + randomint + 2.5f + category.moduleIndex * size1,
                            randomInt * 4 + 84.2, 36.5 + randomint + 15.5f + category.moduleIndex * size1, (int) sliderColor);
                }

                count = 0;

                for (Module m : modules) {


                    String text1;
                    if (hud.text.isEnabled())
                        text1 = m.getName().toLowerCase();
                    else
                        text1 = m.getName();


                    fontsize.drawString(text1, 88,
                            (float) (lengthTop + intdes + count * size1), -1);

                    if (count == category.moduleIndex && m.expanded) {
                        int index = 0;
                        double maxLength = 0;
                        for (Setting setting : m.settings) {


                            String settingText;
                            if (hud.text.isEnabled())
                                settingText = setting.name.toLowerCase();
                            else
                                settingText = setting.name;


                            if (setting instanceof BooleanSetting) {
                                BooleanSetting bool = (BooleanSetting) setting;
                                if (maxLength < fontsize.getStringWidth(settingText + ": " + (bool.isEnabled() ? "Enabled" : "Disabled"))) {
                                    maxLength = fontsize.getStringWidth(settingText + ": " + (bool.isEnabled() ? "Enabled" : "Disabled"));
                                }
                            }
                            if (setting instanceof NumberSetting) {
                                NumberSetting num = (NumberSetting) setting;
                                if (maxLength < fontsize.getStringWidth(settingText + ": " + num.getValue())) {
                                    maxLength = fontsize.getStringWidth(settingText + ": " + num.getValue());
                                }
                            }
                            if (setting instanceof ModeSetting) {
                                ModeSetting mode = (ModeSetting) setting;
                                if (maxLength < fontsize.getStringWidth(settingText + ": " + mode.getModes())) {
                                    maxLength = fontsize.getStringWidth(settingText + ": " + mode.getModes());
                                }
                            }
                            if (setting instanceof KeybindSetting) {
                                KeybindSetting keybind = (KeybindSetting) setting;
                                if (maxLength < fontsize.getStringWidth(settingText + ": " + Keyboard.getKeyName(keybind.getCode()))) {
                                    maxLength = fontsize.getStringWidth(settingText + ": " + Keyboard.getKeyName(keybind.getCode()));
                                }
                            }
                            index++;
                        }

                        //Gui.drawRect(randomInt * 4 + 155, 36.5 + randomint,
                        //      randomInt * 4 + 169 + maxLength, randomint + m.settings.size() * size1 - size1 + 54.5, (int) colorForBackground); // ModuleBackground

                        Draw.color(colorForBackground);
                        Draw.drawRoundedRect(randomInt * 4 + 155, 36.5 + randomint, maxLength + 14, randomint + m.settings.size() * size1 - size1 + 54.5 - 36.5 + randomint, (float) corners.getValue()); // ModuleBackground

                        Gui.drawRect(randomInt * 4 + 158, 39 + randomint + m.index * size1,
                                randomInt * 4 + 166 + maxLength, randomint + 52.5 + m.index * size1, m.settings.get(m.index).focused ? selectorColorFocused : selectorColorUnfocused); // SelectedModuleBackground

                        index = 0;
                        for (Setting setting : m.settings) {

                            String settingText;
                            if (hud.text.isEnabled())
                                settingText = setting.name.toLowerCase();
                            else
                                settingText = setting.name;

                            if (setting instanceof BooleanSetting) {
                                BooleanSetting bool = (BooleanSetting) setting;
                                fontsize.drawString(settingText + ": " + (bool.isEnabled() ? "Enabled" : "Disabled"), 162, (float) (lengthTop + intdes + index * size1), -1);
                            }
                            if (setting instanceof NumberSetting) {
                                NumberSetting num = (NumberSetting) setting;
                                fontsize.drawString(settingText + ": " + num.getValue(), 162, (float) (lengthTop + intdes + index * size1), -1);
                            }
                            if (setting instanceof ModeSetting) {
                                ModeSetting mode = (ModeSetting) setting;
                                fontsize.drawString(settingText + ": " + mode.getSelected(), 162, (float) (lengthTop + intdes + index * size1), -1);
                            }
                            if (setting instanceof KeybindSetting) {
                                KeybindSetting keybind = (KeybindSetting) setting;
                                fontsize.drawString(settingText + ": " + Keyboard.getKeyName(keybind.getCode()), 162, (float) (lengthTop + intdes + index * size1), -1);
                            }
                            index++;
                        }

                        if (Selector.isEnabled()) {

                            Gui.drawRect(randomInt * 4 + 158, 36.5 + randomint + 2.5 + m.index * size1, randomInt * 4 + 160,
                                    37 + randomint + m.index * size1 + 15.5, (int) sliderColor); // Left of
                            // letters
                        }
                    }

                    count++;
                }

            }
        }

        if (e instanceof EventKey) {
            int code = ((EventKey) e).code;
            Category category = Module.Category.values()[currentTab];
            List<Module> modules = Fan.getModulesByCategory(category);

            if (code == Keyboard.KEY_UP) {
                if (expanded) {
                    if (!modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
                        Module module = modules.get(category.moduleIndex);
                        if (module.settings.get(module.index).focused) {
                            Setting setting = module.settings.get(module.index);
                            if (setting instanceof NumberSetting) {
                                ((NumberSetting) setting).increment(true);
                            }
                        } else {
                            if (module.index <= 0) {
                                module.index = module.settings.size() - 1;
                            } else {
                                module.index--;
                            }
                        }
                    } else {
                        if (category.moduleIndex <= 0) {
                            category.moduleIndex = modules.size() - 1;
                        } else {
                            category.moduleIndex--;
                        }
                    }
                } else {
                    if (currentTab <= 0) {
                        currentTab = Module.Category.values().length - 1;
                    } else {
                        currentTab--;
                    }
                }
            }

            if (code == Keyboard.KEY_DOWN) {
                if (expanded) {
                    if (!modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
                        Module module = modules.get(category.moduleIndex);
                        if (module.settings.get(module.index).focused) {
                            Setting setting = module.settings.get(module.index);
                            if (setting instanceof NumberSetting) {
                                ((NumberSetting) setting).increment(false);
                            }
                        } else {
                            if (module.index >= module.settings.size() - 1) {
                                module.index = 0;
                            } else {
                                module.index++;
                            }
                        }
                    } else {
                        if (category.moduleIndex >= modules.size() - 1) {
                            category.moduleIndex = 0;
                        } else {
                            category.moduleIndex++;
                        }
                    }
                } else {
                    if (currentTab >= Module.Category.values().length - 1) {
                        currentTab = 0;
                    } else {
                        currentTab++;
                    }
                }
            }

            if (code == Keyboard.KEY_RETURN) {
                if (expanded && modules.size() != 0) {
                    Module module = modules.get(category.moduleIndex);
                    if (!module.expanded)
                        module.expanded = true;
                    else {
                        module.settings.get(module.index).focused = !module.settings.get(module.index).focused;
                    }
                }
            }

            if (code == Keyboard.KEY_RIGHT) {
                if (expanded && modules.size() != 0) {
                    Module module = modules.get(category.moduleIndex);

                    if (module.expanded) {
                        Setting setting = module.settings.get(module.index);
                        if (setting instanceof BooleanSetting) {
                            ((BooleanSetting) setting).toggle();
                        }
                        if (setting instanceof ModeSetting) {
                            ((ModeSetting) setting).cycle();
                        }
                        if (setting instanceof KeybindSetting) {
                            // TODO: make this cycle keyboard characters
                            //((KeybindSetting) setting).cycle;
                        }
                    } else {
                        if (!module.getName().equals("TabGUI")) {
                            module.toggle();
                        }
                    }
                } else {
                    expand();
                }
            }

            if (code == Keyboard.KEY_LEFT) {
                if (expanded && !modules.isEmpty() && modules.get(category.moduleIndex).expanded) {
                    Module module = modules.get(category.moduleIndex);
                    if (module.settings.get(module.index).focused) {

                    } else {
                        modules.get(category.moduleIndex).expanded = false;
                    }
                } else {
                    collapse();
                }
            }
        }
    }

    public void expand() {
        expanded = true;
    }

    public void collapse() {
        expanded = false;
    }


}
