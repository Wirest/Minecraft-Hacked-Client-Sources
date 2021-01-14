package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event2D;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;

import me.Corbis.Execution.ui.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class HUD extends Module {
    public static UnicodeFontRenderer ufr;
    public static UnicodeFontRenderer ufr2;
    public Setting fps;
    public Setting logo;
    public Setting keystrokes;
    public Setting mode;
    public HUD(){
        super("HUD", Keyboard.KEY_L, Category.RENDER);
        Execution.instance.settingsManager.rSetting(fps = new Setting("Fps Boost", this, 1, 0, 10, false));
        Execution.instance.settingsManager.rSetting(logo = new Setting("Logo", this, false));
        Execution.instance.settingsManager.rSetting(keystrokes = new Setting("Keystrokes", this, false));
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Execution");
        arrayList.add("ExecutionClean");
        Execution.instance.settingsManager.rSetting(mode = new Setting("HUD Mode", this, "Execution", arrayList));



    }
    public static class ModuleComparator implements Comparator<Module> {
        @Override
        public int compare(Module o1, Module o2) {
            if(ufr.getStringWidth(o1.getDisplayName()) > ufr.getStringWidth(o2.getDisplayName())) {
                return -1;
            }
            if(ufr.getStringWidth(o1.getDisplayName()) < ufr.getStringWidth(o2.getDisplayName())) {
                return 1;
            }
            return 0;
        }

    }


    @EventTarget
    public void onRenderGUi(Event2D event){


        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 1, 1);
        }
        if(ufr2 == null){
            ufr2 = UnicodeFontRenderer.getFontFromAssets("GravityLight", 20, 0, 1, 1);
        }


        ScaledResolution sr = new ScaledResolution(mc);
        if(!logo.getValBoolean()) {
            try {
                if(mode.getValString().equalsIgnoreCase("Execution"))
                    ufr.drawStringWithShadow(EnumChatFormatting.RED + "E" + EnumChatFormatting.WHITE + "xecution", 2, 2, rainbow(0));
			    else {
                    GlStateManager.scale(2.0, 2.0, 2.0);
                    ufr.drawStringWithShadow("Execution", 2, 2, new Color(185, 230, 255).getRGB());
                    GlStateManager.scale(0.5, 0.5, 0.5);
                }

            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else {

            GlStateManager.enableAlpha();
            mc.getTextureManager().bindTexture(new ResourceLocation("Execution/Logo.png"));
            Gui.drawModalRectWithCustomSizedTexture(10, 10, 0, 0, 60, 60, 60, 60);
        }



        ArrayList<Module> modules = Execution.instance.moduleManager.getEnabledModules();
        modules.sort(new ModuleComparator());

        int count = 0;
       // modules.clear();
        if(mode.getValString().equalsIgnoreCase("Execution")) {
            for (Module m : modules) {
                float diff = m.mSize - m.lastSize;
                m.lastSize += diff / 4;
                if (m.lastSize != m.ufr.getStringWidth(m.getDisplayName()) || m.isEnabled) {

                    GlStateManager.enableBlend();


                    Module m2 = modules.get(modules.indexOf(m) + 1 < modules.size() ? modules.indexOf(m) + 1 : modules.indexOf(m));

                    Gui.drawRect(sr.getScaledWidth() - ufr.getStringWidth(m.getDisplayName()) - 5 + m.lastSize, (count * (ufr.FONT_HEIGHT + 2)), sr.getScaledWidth() + 10 + m.lastSize, count * (ufr.FONT_HEIGHT + 2) + ufr.FONT_HEIGHT + 2, new Color(0, 0, 0, 150).getRGB());

                    Gui.drawRect(sr.getScaledWidth() - ufr.getStringWidth(m.getDisplayName()) - 5 + m.lastSize, (count * (ufr.FONT_HEIGHT + 2)), sr.getScaledWidth() - ufr.getStringWidth(m.getDisplayName()) - 4 + m.lastSize, count * (ufr.FONT_HEIGHT + 2) + ufr.FONT_HEIGHT + 2, rainbow(count * 200));

                    Gui.drawRect(sr.getScaledWidth() - ufr.getStringWidth(m.getDisplayName()) - 5 + m.lastSize, count * (ufr.FONT_HEIGHT + 2) + ufr.FONT_HEIGHT + 2, modules.indexOf(m2) == modules.indexOf(m) ? sr.getScaledWidth() : sr.getScaledWidth() - ufr.getStringWidth(m2.getDisplayName()) - 5 + 1, count * (ufr.FONT_HEIGHT + 2) + ufr.FONT_HEIGHT + 3, rainbow(count * 200));

                    ufr.drawString(m.getDisplayName(), sr.getScaledWidth() - ufr.getStringWidth(m.getDisplayName()) - 2 + m.lastSize, count * (ufr.FONT_HEIGHT + 2) - 2, rainbow1(count * 200).getRGB());
                    count++;


                }

            }
          //  Execution.instance.addChatMessage("" + getAlpha(0));

        }else {
            for (Module m : modules) {
                float diff = m.mSize - m.lastSize;
                m.lastSize += diff / 4;
                if (m.lastSize != m.ufr.getStringWidth(m.getDisplayName()) || m.isEnabled) {

                    GlStateManager.enableBlend();
                    ufr.drawString(m.getDisplayName(), sr.getScaledWidth() - ufr2.getStringWidth(m.getDisplayName()) - 2 + m.lastSize, count * (ufr2.FONT_HEIGHT + 3) - 2, new Color(185, 230, 230, 255).getRGB());
                    count++;


                }

            }
        }
        Execution.instance.notificationManager.render();

    }
    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 100), 0.8f, 0.7f).getRGB();
    }
    public static Color rainbow1(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / (100)), 0.8f, 0.7f);
    }

    public int getAlpha(int delay){
        double state = Math.ceil((System.currentTimeMillis() + delay) / 10);
        state %= 255 * 2;
        return (int) ((int) state > 255 ? (255 * 2 - state) : state) > 0 ? (int) ((int) state > 255 ? (255 * 2 - state) : state) : 1;
    }



}
