package cedo.ui;

import cedo.Fan;
import cedo.modules.Module;
import cedo.modules.Module.Category;
import cedo.modules.customize.HudModule;
import cedo.settings.Setting;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.KeybindSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.animations.Direction;
import cedo.ui.animations.impl.SmoothStepAnimation;
import cedo.ui.elements.Draw;
import cedo.util.ColorManager;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import cedo.util.render.BlurUtil;
import cedo.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;

import static cedo.ui.ClickGUI.Handle.*;

public class ClickGUI extends GuiScreen {
    public Category dragging;
    public int startX, startY;
    public Module binding;
    public NumberSetting draggingNumber;
    public int accent, modBackground, settingsBackground, bindingBackground;
    public int sliderColor, categoryColor, enabledColor, textColor;
    SmoothStepAnimation openingAnimation = new SmoothStepAnimation(335, 1, Direction.FORWARDS);

    MinecraftFontRenderer fr;
    MinecraftFontRenderer small;
    double categoryPos;

    public static int applyOpacity(int color, float opacity) {
        Color old = new Color(color);
        return new Color(old.getRed(), old.getGreen(), old.getBlue(), (int) (opacity * (color >> 24 & 255))).getRGB();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        handle(mouseX, mouseY, -1, DRAW);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        handle(mouseX, mouseY, button, CLICK);
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        handle(mouseX, mouseY, button, RELEASE);
    }

    public void keyTyped(char character, int key) {
        if (key == Fan.clickGui.getKey() || key == 1) {
            //openingAnimation.changeDirection();
            //openingAnimation.setDuration(200);
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }

        if (binding != null) {
            if (key == Keyboard.KEY_SPACE || key == Keyboard.KEY_ESCAPE || key == Keyboard.KEY_DELETE)
                binding.getKeyBind().setCode(Keyboard.KEY_NONE);
            else
                binding.getKeyBind().setCode(key);

            binding = null;
        }
    }

    public void handle(int mouseX, int mouseY, int button, Handle type) {

        if (Fan.clickGui.blur.isEnabled())
            BlurUtil.blur(15);

        if (!openingAnimation.isDone()) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.scissor(width / 2d - (openingAnimation.getOutput() * width / 2d), height / 2d - (openingAnimation.getOutput() * height / 2d), (openingAnimation.getOutput() * width), (openingAnimation.getOutput() * width));
        } /*else {
            if (openingAnimation.getDirection() == Direction.BACKWARDS){
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null) {
                    this.mc.setIngameFocus();
                }
            }
        }*/

        fr = FontUtil.clean;
        Fan.modules.sort(Comparator.comparingDouble(m -> {
                    String modulesText = ((Module) m).getName();

                    return fr.getStringWidth(modulesText);

                })
                        .reversed()
        );
        if (type == RELEASE && button == 0) {
            dragging = null;
            draggingNumber = null;
            if (binding != null)
                binding = null;
        }

        if (dragging != null) {
            dragging.posX = mouseX - startX;
            dragging.posY = mouseY - startY;
        }

        //importing all of the settings

        cedo.modules.customize.ClickGUI clickGui = Fan.clickGui;
        String theme = clickGui.theme.getSelected();
        float offset = (float) (clickGui.offset.getValue());
        float corners = (float) (clickGui.corners.getValue());
        float red = (float) (clickGui.red.getValue() / 255);
        float green = (float) (clickGui.green.getValue() / 255);
        float blue = (float) (clickGui.blue.getValue() / 255);
        float rainbowSpeed = (float) (clickGui.rainbowSpeed.getValue());
        boolean custom = clickGui.custom.isEnabled();


        //TODO: Do Small ClickGUI

        //this changes both the font and the size of the clickgui

        small = FontUtil.cleanSmall;
        categoryPos = 4.5;


        // this makes the text for the category always readable
        if (theme.equals("Custom Color")) {
            if ((red * 255) + (green * 255) >= 445) {
                categoryColor = 0xff000000;
            }
        }
        if (theme.equals("Discord")) {
            categoryColor = new Color(214, 215, 220).getRGB();
        } else {
            categoryColor = -1;
        }

        //the default text color excluding category
        textColor = -1;

        //light up text color
        // int moduleText = new Color(214, 215, 220).getRGB();

        //these are the themes
        if (theme.equals("Custom Color")) {
            accent = applyOpacity(new Color(red, green, blue, 1).getRGB(), 1);
            if (!custom) {
                modBackground = applyOpacity(new Color(red, green, blue, 0.8f).darker().getRGB(), 0.8f);
                settingsBackground = applyOpacity(new Color(red, green, blue, 0.75f).darker().getRGB(), 0.8f);
                bindingBackground = applyOpacity(new Color(red, green, blue, 1).getRGB(), 1f);
                sliderColor = applyOpacity(new Color(red, green, blue, 1).getRGB(), 1f);
            } else {
                modBackground = applyOpacity(new Color(40, 50, 50).darker().getRGB(), 0.8f);
                settingsBackground = applyOpacity(new Color(41, 43, 42).darker().getRGB(), 0.7f);
                bindingBackground = -1;
                sliderColor = -1;
            }
        }


        if (theme.equals("Discord")) {
            accent = new Color(32, 34, 37).getRGB(); //(32,34,37)
            modBackground = new Color(47, 49, 54).getRGB();//(47,49,54)
            settingsBackground = new Color(54, 57, 63).getRGB();//(54,57,63)
            bindingBackground = new Color(34, 36, 40).getRGB();
            sliderColor = new Color(114, 137, 218).getRGB();//(41,43,47)
            textColor = new Color(214, 215, 220).getRGB();


        }


        for (Category category : Category.values()) {
            float left = category.posX,
                    top = category.posY,
                    width = 90,
                    right = left + width;

            boolean hoveringCategory = isHovered(left, top, left + width, top + 20, mouseX, mouseY);

            if (type == CLICK && hoveringCategory && button == 0) {
                dragging = category;
                startX = mouseX - category.posX;
                startY = mouseY - category.posY;
                return;
            }

            if (type == CLICK && hoveringCategory && button == 1) {
                category.expanded = !category.expanded;
                return;
            }
            int count = 0;
            if (theme.equals("Chill Rainbow")) {
                accent = ColorManager.rainbow((0), (int) rainbowSpeed, 0.5f, 1, 1f).getRGB();
            }
            if (theme.equals("Rainbow")) {
                accent = ColorManager.rainbow((0), (int) rainbowSpeed, 1, 1, 1f).getRGB();
            }


            HudModule hud = Fan.hudMod;

            boolean textEnabled = hud.text.isEnabled();

            String categoryText;
            if (textEnabled)
                categoryText = category.name.toLowerCase();
            else
                categoryText = category.name;


            // Gui.drawRect(left - 1.5, top, left + width + 1.5, top + 20, accent);
            GlStateManager.color(1, 1, 1, 1);
            Gui.drawRect(0, 0, 0, 0, accent);

            Draw.drawRoundedRect(left - 1.5, top + 1, width + 3, width - 69, corners);
            fr.drawCenteredString(categoryText, left + (width / 2), (float) (top + categoryPos) + 1, categoryColor);


            int lastModBackground = -1;

            boolean isGradient = false;

            if (category.expanded)
                for (Module m : Fan.getModulesByCategory(category)) {
                    boolean hoveringModule = isHovered(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, mouseX, mouseY);

                    if (type == CLICK && hoveringModule && button == 0) {
                        m.toggle();
                        return;
                    }

                    if (type == CLICK && hoveringModule && button == 1) {
                        m.setExpanded(!m.isExpanded());
                        return;
                    }

                    if (theme.contains("Rainbow")) {
                        isGradient = true;
                        if (lastModBackground != -1)
                            lastModBackground = modBackground;

                        modBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, theme.equals("Chill Rainbow") ? 0.5f : 1, 0.8f, 1f).getRGB();

                        if (lastModBackground == -1)
                            lastModBackground = modBackground;


                    }
                    
                    /*if (theme.equals("Chill Rainbow")) {
                        accent = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1, 0.8f).getRGB();
                        modBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 0.8f, 0.8f).getRGB();
                        settingsBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 0.6f, 0.8f).getRGB();
                        bindingBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 0.5f, 0.8f).getRGB();
                        sliderColor = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1f, 1f).getRGB();

                    }

                    if (theme.equals("Rainbow")) {
                        accent = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 1, 1, 0.8f).getRGB();
                        modBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 1, 0.8f, 0.8f).getRGB();
                        settingsBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 1, 0.6f, 0.8f).getRGB();
                        bindingBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1, 0.8f).getRGB();
                        sliderColor = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1, 1f).getRGB();


                    }*/
                    if (isGradient)
                        drawGradientRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, lastModBackground, modBackground);
                    else
                        Gui.drawRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, modBackground);

                    Gui.drawRect(0, 0, 0, 0, accent);
                    Draw.drawRoundedRect(left - 1.5, top + 16, width + 3, width - 84, corners / 4);
                    //this makes the text light up WIP as it is only configured for discord mode
                    String textModule;
                    if (textEnabled)
                        textModule = m.getName().toLowerCase();
                    else
                        textModule = m.getName();

                    if (!m.isEnabled() || m.getName().equals("HUD") || m.getName().equals("ArrayList")) {
                        fr.drawString(textModule, left + 5, top + 25.5f + count * 20, textColor);
                    }

                    int textColorHighlight = -1;


                    if (theme.equals("Custom Color"))
                        textColorHighlight = applyOpacity(new Color(red, green, blue, 1).getRGB(), 1);
                    else if (theme.equals("Discord"))
                        textColorHighlight = new Color(114, 137, 218).getRGB();


                    if (m.isEnabled() && !m.getName().equals("HUD") && !m.getName().equals("ArrayList")) {
                        if (theme.equals("Rainbow") || theme.equals("Chill Rainbow"))
                            Gui.drawRect(left + 85, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, -1);


                        fr.drawString(textModule, left + 5, top + 25.5f + count * 20, textColorHighlight);

                    }


                    count++;

                    lastModBackground = modBackground;

                    if (m.isExpanded()) {
                        for (Setting setting : m.getSettings()) {
                            if (theme.equals("Rainbow")) {
                                if (lastModBackground != modBackground)
                                    lastModBackground = settingsBackground;

                                settingsBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 1, 0.6f, 1f).getRGB();
                                bindingBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1, 1f).getRGB();
                                sliderColor = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1, 1f).getRGB();

                                if (lastModBackground == modBackground)
                                    lastModBackground = settingsBackground;

                            }
                            if (theme.equals("Chill Rainbow")) {
                                if (lastModBackground != modBackground)
                                    lastModBackground = settingsBackground;

                                settingsBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 0.6f, 1f).getRGB();
                                bindingBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 0.5f, 1f).getRGB();
                                sliderColor = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1f, 1f).getRGB();

                                if (lastModBackground == modBackground)
                                    lastModBackground = settingsBackground;
                            }
                            boolean hoveringSetting = isHovered(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, mouseX, mouseY);

                            if (setting instanceof BooleanSetting) {
                                BooleanSetting bool = (BooleanSetting) setting;

                                if (isGradient)
                                    drawGradientRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, lastModBackground, settingsBackground);
                                else
                                    Gui.drawRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, settingsBackground);

                                String title = bool.isEnabled() ? "On" : "Off";

                                if (type == CLICK && hoveringSetting && button == 0) {
                                    bool.toggle();
                                }


                                if (textEnabled)
                                    title = title.toLowerCase();

                                small.drawString(title, right - 5 - small.getStringWidth(title), top + 31 + count * 20, -1);
                            }
                            if (setting instanceof NumberSetting) {
                                NumberSetting number = (NumberSetting) setting;
                                float percent = (float) ((number.getValue() - number.getMin()) / (number.getMax() - number.getMin())),
                                        numberWidth = percent * width;

                                if (draggingNumber != null && draggingNumber == number) {
                                    double mousePercent = Math.min(1, Math.max(0, (mouseX - left) / width)),
                                            newValue = (mousePercent * (number.getMax() - number.getMin())) + number.getMin();
                                    number.setValue(newValue);
                                }

                                if (isGradient)
                                    drawGradientRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, lastModBackground, settingsBackground);
                                else
                                    Gui.drawRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, settingsBackground);

                                //this is the slider and the numberwidth helps it move and the sliderColor changes the color of it. We need to implement new color for diffrent modes
                                Gui.drawRect(left, top + 35 + count * 20, left + numberWidth, top + 18 + count * 20 + 20, sliderColor);

                                //this int controls the color or the circle dot. II made a draw rect to override the color of the other drawrects
                                int colorslider = -1;
                                Gui.drawRect(0, 0, 0, 0, colorslider);

                                //this is the dot
                                GlStateManager.enableBlend();
                                //GlStateManager.disableAlpha();
                                mc.getTextureManager().bindTexture(new ResourceLocation("Fan/SliderDot.png"));
                                Gui.drawModalRectWithCustomSizedTexture(Math.min((int) (left + numberWidth), left + width - 5), (int) (top + 34 + count * 20), 0, 0, 10 / 2f, 10 / 2f, 10 / 2f, 10 / 2f);


                                String title = String.valueOf(number.getValue());

                                if (type == CLICK && hoveringSetting && button == 0) {
                                    draggingNumber = number;
                                }

                                if (textEnabled)
                                    title = title.toLowerCase();

                                small.drawString(title, right - 3 - small.getStringWidth(title), top + 28 + count * 20, -1);
                            }
                            if (setting instanceof ModeSetting) {
                                ModeSetting mode = (ModeSetting) setting;

                                if (isGradient)
                                    drawGradientRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, lastModBackground, settingsBackground);
                                else
                                    Gui.drawRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, settingsBackground);

                                String title = mode.getSelected();

                                if (type == CLICK && hoveringSetting && button == 0) {
                                    mode.cycle();
                                }

                                if (textEnabled)
                                    title = title.toLowerCase();

                                small.drawString(title, right - 5 - small.getStringWidth(title), top + 31 + count * 20, -1);
                            }
                            if (setting instanceof KeybindSetting) {
                                KeybindSetting bind = (KeybindSetting) setting;

                                if (isGradient)
                                    drawGradientRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, lastModBackground, settingsBackground);
                                else
                                    Gui.drawRect(left, top + 20 + count * 20, left + width, top + 20 + count * 20 + 20, binding == m ? bindingBackground : settingsBackground);

                                String title = Keyboard.getKeyName(bind.getCode());

                                if (type == CLICK && hoveringSetting && button == 0) {
                                    binding = m;
                                }

                                if (textEnabled)
                                    title = title.toLowerCase();

                                small.drawString(title, right - 5 - small.getStringWidth(title), top + 31 + count * 20, -1);

                            }
                            String settingText;
                            if (textEnabled)
                                settingText = setting.name.toLowerCase();
                            else
                                settingText = setting.name;

                            small.drawString(settingText, left + 5, top + 25 + count * 20, textColor);

                            count++;

                        }

                        if (isGradient)
                            modBackground = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, theme.equals("Chill Rainbow") ? 0.5f : 1, 0.8f, 1f).getRGB();

                    }
                }
            left += 105;
        }

        if (type == CLICK && button == 0) {
            dragging = null;
            // binding = null;
        }

        Fan.modules.sort(Comparator.comparingDouble(m -> {
                    String modulesText = ((Module) m).getDisplayName();

                    return fr.getStringWidth(modulesText);

                })
                        .reversed()
        );
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean isHovered(float left, float top, float right, float bottom, int mouseX, int mouseY) {
        return mouseX >= left && mouseY >= top && mouseX < right && mouseY < bottom;
    }

    enum Handle {
        DRAW,
        CLICK,
        RELEASE
    }

}
