package cedo.ui;

import cedo.Fan;
import cedo.command.commands.NameCommand;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.modules.customize.Arraylist;
import cedo.modules.customize.HudModule;
import cedo.modules.customize.TabGUI;
import cedo.util.ColorManager;
import cedo.util.font.MinecraftFontRenderer;
import cedo.util.random.MathUtils;
import com.ibm.icu.text.SimpleDateFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;


public class HUD {
    public static Minecraft mc = Minecraft.getMinecraft();
    int colorr;

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:a");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String years() {
        SimpleDateFormat sdfDate = new SimpleDateFormat(" " + "yyyy");//dd/MM/yyyy
        Date now = new Date();
        return sdfDate.format(now);
    }

    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);
        MinecraftFontRenderer theFont2 = Fan.getClientFont("Regular", false);
        MinecraftFontRenderer theFont = Fan.getClientFont("Medium", false);


        Arraylist arraylist = Fan.arraylist;
        HudModule hud = Fan.hudMod;
        float backgroundAlpha = (float) arraylist.backgroundAlpha.getValue() * .01f;
        String logo = hud.logo.getSelected();
        String arraylistColor = arraylist.color.getSelected();
        float hue = (float) (arraylist.hue.getValue() / 100);
        float red = (float) (arraylist.red.getValue() / 255);
        float green = (float) (arraylist.green.getValue() / 255);
        float blue = (float) (arraylist.blue.getValue() / 255);
        float rainbowSpeed = (float) (arraylist.rainbowSpeed.getValue() + .00001);
        float offset = (float) (arraylist.offset.getValue());

        Fan.modules.sort(Comparator.comparingDouble(
                m -> {
                    String modulesText = ((Module) m).getDisplayName();

                    if (hud.text.isEnabled())
                        modulesText = ((Module) m).getDisplayName().toLowerCase();

                    return theFont.getStringWidth(modulesText);
                })
                .reversed()
        );

        //WaterMarking
        String commandWatermark = NameCommand.namenameOfWatermark;
        String watermark;


        if (commandWatermark != null) {
            watermark = commandWatermark;
        } else {
            watermark = Fan.fullname;
        }


        if (hud.text.isEnabled()) {
            watermark = watermark.toLowerCase();
        }


        String text = watermark.charAt(0) + "\247f" + watermark.substring(1);

        int rainbowColor = ColorManager.rainbow(100, 20, 0.5f, 1, 1f).getRGB();

        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        //changing logo
        switch (logo) {

            case "Medium":
                mc.getTextureManager().bindTexture(new ResourceLocation("Fan/Fan2.png"));
                Gui.drawModalRectWithCustomSizedTexture(4, 4, 0, 0, 196 / 2f, 132 / 2f, 196 / 2f, 132 / 2f);
                break;


            case "ZeroTwo":
                mc.getTextureManager().bindTexture(new ResourceLocation("Fan/zero2.png"));
                Gui.drawModalRectWithCustomSizedTexture(4, 4, 0, 0, 190 / 2f, 200 / 2f, 190 / 2f, 200 / 2f);
                break;


            case "Text":
                //drawing watermark
                theFont2.drawStringWithShadow(text, 6, 6, rainbowColor);

                if (hud.info.isEnabled()) {
                    //TODO: Make it regular time not military time for now its commented out
                    //drawing time
                    // theFont2.drawString("\247" + " (" + getCurrentTimeStamp() + "\247r" + years() + "\247 )",
                    //       moveText + 10, 6, new Color(250, 90, 100).getRGB());
                }
                break;
        }
        GlStateManager.translate(-1, -1, -1);


        int count = 0;


        TabGUI tabgui = Fan.tabgui;

        int movementInfo = 95;
        int fpsY = 0;
        int bpsY = 0;
        int xyzYvalue = 0;
        int tabguiY = (int) tabgui.yValue.getValue();
        int infoFPSY = 0;
        int infoBPSY = 0;
        int infoxyzY = 0;


        if (hud.infoPos.is("Clip")) {
            infoFPSY = tabguiY;
            infoBPSY = tabguiY;
            infoxyzY = tabguiY;
        }


        switch (logo) {

            case "Text":
                if (tabguiY > 30) {
                    infoFPSY = 0;
                    fpsY = -76;
                    bpsY = -16;
                    xyzYvalue = -16;
                }
                if (tabguiY > 42) {
                    infoBPSY = 0;
                    bpsY = -76;
                    xyzYvalue = -32;
                }
                if (tabguiY > 58) {
                    infoxyzY = 0;
                    xyzYvalue = -76;
                }
                if (tabguiY > 63) {
                    fpsY = -140;
                    bpsY = -140;
                    xyzYvalue = -140;
                    infoFPSY = tabguiY;
                    infoBPSY = tabguiY;
                    infoxyzY = tabguiY;
                }
                break;

            case "ZeroTwo":
                if (tabguiY > 122) {
                    fpsY = 16;
                    infoFPSY = 0;
                    bpsY = -16;
                    xyzYvalue = -16;
                }
                if (tabguiY > 137) {
                    bpsY = 16;
                    infoBPSY = 0;
                    xyzYvalue = -32;
                }
                if (tabguiY > 147) {
                    xyzYvalue = 14;
                    infoxyzY = 0;
                }
                break;

            case "Medium":
                if (tabguiY > 85) {
                    fpsY = -20;
                    infoFPSY = 0;
                    bpsY = -16;
                    xyzYvalue = -16;
                }
                if (tabguiY > 98) {
                    bpsY = -20;
                    infoBPSY = 0;
                    xyzYvalue = -32;
                }
                if (tabguiY > 114) {
                    xyzYvalue = -21;
                    infoxyzY = 0;
                }
                break;
        }
        if (!tabgui.isEnabled()) {
            fpsY = 0;
            bpsY = 0;
            xyzYvalue = 0;
            infoFPSY = 0;
            infoBPSY = 0;
            infoxyzY = 0;
            movementInfo = 19;
        }
        int fpsXValue = 0;
        if (hud.infoPos.is("Bottom")) {
            infoFPSY = sr.getScaledHeight() - 128;
            infoBPSY = sr.getScaledHeight() - 132;
            infoxyzY = sr.getScaledHeight() - 135;
            fpsY = 0;
            bpsY = 0;
            xyzYvalue = 0;

            if (mc.currentScreen instanceof GuiChat) {
                infoFPSY += 11;
                fpsXValue = 40;
            }
        }

        



        /*HOW TO MAKE PING 
          NetworkPlayerInfo you = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
          String ping = "Ping: \247f" + (you == null ? "0" : you.responseTime);*/

        double bruh = (MathUtils.square(mc.thePlayer.motionX) + MathUtils.square(mc.thePlayer.motionZ));
        double bps = MathUtils.round((Math.sqrt(bruh) * 20) * mc.timer.timerSpeed, 2);

        String bpsText = "BPS: \247f";
        String fps = "FPS: \247f" + Minecraft.getDebugFPS();
        String xyz = "X: \247f" + Math.round(mc.thePlayer.posX) + " \247rY: \247f" + Math.round(mc.thePlayer.posY) + "\247r Z: \247f" + Math.round(mc.thePlayer.posZ);

        if (hud.text.isEnabled()) {
            fps = fps.toLowerCase();
            xyz = xyz.toLowerCase();
            bpsText = bpsText.toLowerCase();
        }


        int xValue = 6;
        if (hud.info.isEnabled()) {
            //Where the info is drawn
            Fan.getClientFont("Medium", true).drawString(fps, xValue + fpsXValue, infoFPSY + movementInfo + fpsY, rainbowColor);
            Fan.getClientFont("Medium", true).drawString(bpsText + bps, xValue, infoBPSY + 15 + movementInfo + bpsY, rainbowColor);
            Fan.getClientFont("Medium", true).drawString(xyz, xValue, infoxyzY + 30 + movementInfo + xyzYvalue, rainbowColor);


        }

        // ArrayList
        for (Module m : Fan.modules) {
            if (!m.isToggled() || m.getName().equals("TabGUI") || m.getName().equals("HUD") || m.getName().equals("ArrayList")) {
                continue;
            }


            switch (arraylistColor) {
                case "Custom Color":
                    colorr = new Color(red, green, blue, 1).getRGB();
                    break;
                case "Chill Rainbow":
                    colorr = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed, 0.5f, 1, 1f).getRGB();
                    break;
                case "Rainbow":
                    colorr = ColorManager.rainbow(((int) offset * count), (int) rainbowSpeed).getRGB();
                    break;
                case "Astolfo":
                    colorr = ColorManager.astolfo(((int) offset * -count), (int) rainbowSpeed, 0.6f, -hue, 1f).getRGB();
                    break;
                case "Astolfo2":
                    colorr = ColorManager.getAstoGay(3000, count * (offset * 4));
                    break;
            }


            double theOffset = count * (theFont.getHeight() + 2.5);

            String modulesText = m.getDisplayName();
            if (hud.text.isEnabled())
                modulesText = modulesText.toLowerCase();


            // The box behind the arraylist text
            Gui.drawRect(sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 8, theOffset, sr.getScaledWidth() + 2, 2.5 + theFont.getHeight() + theOffset, new Color(0, 0, 0, backgroundAlpha).getRGB());


            //Modes of outlining
            switch (arraylist.mode.getSelected()) {

                case "Sidebar":
                    GlStateManager.color(1, 1, 1, 1);
                    Gui.drawRect(sr.getScaledWidth() - 1.5, theOffset, sr.getScaledWidth() + 1, theFont.getHeight() + theOffset + 2.5, colorr);
                    break;


                case "Outline":
                    ArrayList<Module> toggledModules = new ArrayList<>(Fan.modules);
                    toggledModules.removeIf(module -> !module.isToggled() || module.getName().equals("TabGUI") || module.getName().equals("HUD") || module.getName().equals("ArrayList"));
                    int toggledIndex = toggledModules.indexOf(m);
                    int m1Offset = -1;
                    if (toggledIndex != toggledModules.size() - 1) {
                        m1Offset += !hud.text.isEnabled() ? theFont.getStringWidth(toggledModules.get(toggledIndex + 1).getDisplayName()) : theFont.getStringWidth(toggledModules.get(toggledIndex + 1).getDisplayName().toLowerCase());
                        m1Offset += 10;
                    }

                    GlStateManager.color(1, 1, 1, 1);
                    Gui.drawRect(
                            sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 9,
                            theOffset,
                            sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 8,
                            theFont.getHeight() + theOffset + 3,
                            colorr);
                    Gui.drawRect(
                            sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 9,
                            theOffset + theFont.getHeight() + 3,
                            sr.getScaledWidth() - m1Offset,
                            theOffset + theFont.getHeight() + 2.5,
                            colorr);
                    break;


                case "LeftBars":
                    GlStateManager.color(1, 1, 1, 1);
                    Gui.drawRect(sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 10, theOffset, sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 8, theFont.getHeight() + theOffset - 2.5, colorr);
                    break;
            }


            // The arraylist text
            GlStateManager.color(1, 1, 1, 1);
            if (arraylist.color.is("Astolfo")) {
                theFont.drawStringWithShadow(modulesText, sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 4, (float) (0.25 + theOffset) + 2.5f, colorr);
            } else {
                theFont.drawString(modulesText, sr.getScaledWidth() - theFont.getStringWidth(modulesText) - 4, (float) (theOffset + 1.8), colorr);
            }

            count++;

        }
        Fan.onEvent(new EventRenderGUI(sr));
    }
}
