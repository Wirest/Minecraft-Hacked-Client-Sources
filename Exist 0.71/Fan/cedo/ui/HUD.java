package cedo.ui;

import cedo.Fan;
import cedo.command.commands.NameCommand;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.modules.customize.Arraylist;
import cedo.modules.customize.HudModule;
import cedo.util.ColorManager;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import cedo.util.random.MathUtils;
import com.ibm.icu.text.SimpleDateFormat;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;


public class HUD {
    public static Minecraft mc = Minecraft.getMinecraft();
    int intarraylist;
    int arraylistbig;
    int colorenableint;
    int colorr;
    @Getter
    @Setter
    int randomsetting = 10;

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
        MinecraftFontRenderer fr = FontUtil.regular;
        MinecraftFontRenderer fr2 = FontUtil.regular2;
        MinecraftFontRenderer clean = FontUtil.clean;
        MinecraftFontRenderer thefont = null;

        // Debug Notification
        //Notification notify = new Notification();
        //notify.showNotification("Title", "First Line", "Second Line");

        int generalColor = ColorManager.fade(ColorManager.WHITE.getColor()).getRGB();
        Arraylist arraylist = Fan.arraylist;
        HudModule hud = Fan.hudMod;
        // boolean colorenable = hud.colorenable.isEnabled();
        // boolean sidebarsetting = hud.sidebarsetting.isEnabled();
        String mode = arraylist.mode.getSelected();
        boolean background = arraylist.background.isEnabled();
        // int blur = (int) arraylist.blur.getValue();
        String logo = hud.logo.getSelected();
        String color = arraylist.color.getSelected();
        String font = arraylist.font.getSelected();
        float hue = (float) (arraylist.hue.getValue() / 100);
        float red = (float) (arraylist.red.getValue() / 255);
        float green = (float) (arraylist.green.getValue() / 255);
        float blue = (float) (arraylist.blue.getValue() / 255);
        float rainbowSpeed = (float) (arraylist.rainbowSpeed.getValue() + .00001);
        float theoffset = (float) (arraylist.offset.getValue());


        //int betterOffset =  theoffset -


        if (font.equals("Clean")) {
            thefont = FontUtil.cleanmedium;
            intarraylist = 2;
            arraylistbig = 1;
        }
        if (font.equals("Thicc")) {
            thefont = FontUtil.mediumfont;
            intarraylist = 2;
            arraylistbig = 1;
        }


        final MinecraftFontRenderer compareFont = thefont;
        Fan.modules.sort(Comparator.comparingDouble(m -> {
                    String modulesText = ((Module) m).getDisplayName();

                    if (hud.text.isEnabled())
                        modulesText = ((Module) m).getDisplayName().toLowerCase();

                    return compareFont.getStringWidth(modulesText);

                })
                        .reversed()
        );

        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1, 1, 1, 1);
        if (logo.equals("Small")) {
            mc.getTextureManager().bindTexture(new ResourceLocation("Fan/Fansmall.png"));
            Gui.drawModalRectWithCustomSizedTexture(8, 8, 0, 0, 88 / 2f, 59 / 2f, 88 / 2f, 59 / 2f);
        }
        if (logo.equals("Medium")) {
            mc.getTextureManager().bindTexture(new ResourceLocation("Fan/Fan2.png"));
            Gui.drawModalRectWithCustomSizedTexture(4, 4, 0, 0, 196 / 2f, 132 / 2f, 196 / 2f, 132 / 2f);
        }
        if (logo.equals("ZeroTwo")) {
            mc.getTextureManager().bindTexture(new ResourceLocation("Fan/zero2.png"));
            Gui.drawModalRectWithCustomSizedTexture(4, 4, 0, 0, 190 / 2f, 200 / 2f, 190 / 2f, 200 / 2f);
        }
        GlStateManager.translate(-1, -1, -1);


        String waterMark = NameCommand.namenameOfWatermark;


        String text;


        if (waterMark != null)
            text = waterMark;
        else
            text = Fan.fullname;

        if (hud.text.isEnabled()) {
            text = text.toLowerCase();
        }


        String tex2 = text.charAt(0) + "\247f" + text.substring(1);

        float moveText = (float) clean.getStringWidth(text);
        if (logo.equals("Text")) {
            clean.drawStringWithShadow(tex2, 6, 6, ColorManager.rainbow(100, 15, 0.5f, 1, 0.8f).getRGB());

            if (hud.info.isEnabled())
                clean.drawStringWithShadow("\247" + " " + "(" + getCurrentTimeStamp() + "\247r" + years() + "\247 )", moveText + 10, 6, new Color(250, 90, 100).getRGB());
        }

        boolean astolfoThing = color.equals("Astolfo");

        int count = 0;
        if (color.equals("Custom Color")) {
            colorr = new Color(red, green, blue, 1).getRGB();
        }

        if (color.equals("Chill Rainbow")) {
            colorr = ColorManager.rainbow((0), (int) rainbowSpeed, 0.5f, 1, 0.8f).getRGB();
        }
        if (color.equals("Rainbow")) {
            colorr = ColorManager.rainbow((0), (int) rainbowSpeed).getRGB();
        }

        if (color.equals("Custom Rainbow")) {
            colorr = ColorManager.rainbow((0), (int) rainbowSpeed, hue, 1, 0.87f).getRGB();
        }

        if (color.equals("Astolfo")) {
            colorr = ColorManager.astolfo((0), (int) rainbowSpeed, 0.6f, hue, 1f).getRGB();
        }

        cedo.modules.customize.TabGUI tabgui = Fan.tabgui;
        float size2 = (float) (tabgui.size.getValue());
        int realsize = (int) (size2 * 6) + 5;

        int size3 = 0;
        int size4 = 0;
        if (logo.equals("Medium")) {
            size3 = 35;
        }

        if (logo.equals("Text")) {
            size3 = -15;
        }
        if (logo.equals("Small")) {
            size3 = 3;
        }
        if (logo.equals("ZeroTwo")) {
            size3 = 70;
        }
        for (Module m : Fan.modules) {
            if (!m.isToggled() && m.getName().equals("TabGUI")) {
                size4 = -80;
                break;
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
        

        int moveMe = 0;
        int bottomPos1 = 0;
        int bottomPos2 = 0;
        int bottomPos3 = 0;
        int xPosBottom1 = 0;
        int xPosBottom2 = 0;
        int xPosBottom3 = 0;
        
        if (!(Fan.getModuleByName("TabGUI").isToggled())) {
            moveMe = 35;
        }
        
        
        if(hud.infoPos.is("Bottom")) {
        	bottomPos1 = 345 + 13;
        	bottomPos2 = 377 + 13;
        	bottomPos3 = 375 + 13;
        	xPosBottom1 = 60;
            xPosBottom2 = 0;
            xPosBottom3 = 0;
        	realsize = 0;
        	size3 =0;
        	size4 = 0;
        	moveMe = 0;
        }
        
        
        
        MinecraftFontRenderer fontSwitcher;
        if(hud.boldinfo.isEnabled()) 
        	fontSwitcher = FontUtil.regular2;
        else
        	fontSwitcher = FontUtil.clean;

        int heightInfo = 125;
        int xValue = 6;
        if (hud.info.isEnabled()) {
        	fontSwitcher.drawString(bpsText + bps, xPosBottom1 + xValue, bottomPos1 + (heightInfo + 32) + realsize + size3 + size4 - moveMe, colorr);
        	fontSwitcher.drawString(fps, xPosBottom2 + xValue, bottomPos2 + heightInfo + realsize + size3 + size4 - moveMe, colorr);
        	fontSwitcher.drawString(xyz,xPosBottom3 + xValue,bottomPos3 + (heightInfo + 16) + realsize + size3 + size4 - moveMe, colorr);
        }

        // ArrayList
        for (Module m : Fan.modules) {
            if (!m.isToggled() || m.getName().equals("TabGUI") || m.getName().equals("HUD") || m.getName().equals("ArrayList")) {
                continue;
            }

            if (color.equals("Custom Color")) {
                colorr = new Color(red, green, blue, 1).getRGB();
            }

            if (color.equals("Chill Rainbow")) {
                colorr = ColorManager.rainbow(((int) theoffset * count), (int) rainbowSpeed, 0.5f, 1, 0.8f).getRGB();
            }
            if (color.equals("Rainbow")) {
                colorr = ColorManager.rainbow(((int) theoffset * count), (int) rainbowSpeed).getRGB();
            }

            if (color.equals("Custom Rainbow")) {
                colorr = ColorManager.rainbow(((int) theoffset * count), (int) rainbowSpeed, hue, 1, 0.87f).getRGB();
            }

            if (color.equals("Astolfo")) {
                colorr = ColorManager.astolfo(((int) theoffset * -count), (int) rainbowSpeed, 0.6f, -hue, 1f).getRGB();
            }


            double offset = count * (fr2.getHeight() + intarraylist);

            String modulesText = m.getDisplayName();

            if (hud.text.isEnabled())
                modulesText = modulesText.toLowerCase();



            /*if (blur > 0){
                BlurUtil.blur(sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 8, offset, thefont.getStringWidth(modulesText) + 10, intarraylist + fr2.getHeight(), blur);
            }*/
            // The little rainbow thing on the left side of the module in the arraylist
            if (mode.equals("LeftBars")) {
                GlStateManager.color(1, 1, 1, 1);
                Gui.drawRect(sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 10, offset, sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 8, fr.getHeight() + offset - arraylistbig, colorr);
            }
            // The box behind the arraylist text
            if (background) {
                Gui.drawRect(sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 8, offset, sr.getScaledWidth() + 2, intarraylist + fr2.getHeight() + offset, 0x65000000);
            }
            if (mode.equals("Sidebar")) {
                GlStateManager.color(1, 1, 1, 1);
                Gui.drawRect(sr.getScaledWidth() - 1, offset, sr.getScaledWidth() + 1, fr.getHeight() + offset - arraylistbig, colorr);
            }
            if (mode.equals("Outline")) {
                ArrayList<Module> toggledModules = new ArrayList<Module>(Fan.modules);
                toggledModules.removeIf(module -> !module.isToggled() || module.getName().equals("TabGUI") || module.getName().equals("HUD") || module.getName().equals("ArrayList"));
                int toggledIndex = toggledModules.indexOf(m);
                int m1Offset = -1;
                if (toggledIndex != toggledModules.size() - 1) {
                    m1Offset += !hud.text.isEnabled() ? thefont.getStringWidth(toggledModules.get(toggledIndex + 1).getDisplayName()) : thefont.getStringWidth(toggledModules.get(toggledIndex + 1).getDisplayName().toLowerCase());
                    m1Offset += 10;
                }

                GlStateManager.color(1, 1, 1, 1);
                Gui.drawRect(
                        sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 9,
                        offset,
                        sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 8,
                        fr.getHeight() + offset - arraylistbig + 1,
                        colorr);
                Gui.drawRect(
                        sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 8,
                        offset + fr.getHeight() - 1,
                        sr.getScaledWidth() - m1Offset,
                        offset + fr.getHeight(),
                        colorr);

            }
            // The arraylist text
            GlStateManager.color(1, 1, 1, 1);


            if (astolfoThing) {
                thefont.drawStringWithShadow(modulesText, sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 4, (float) (0.25 + offset) + 2.5f, colorr);
            } else {
                thefont.drawString(modulesText, sr.getScaledWidth() - thefont.getStringWidth(modulesText) - 4, (float) (2.35 + offset), colorr);
            }

            count++;

        }
        Fan.onEvent(new EventRenderGUI(sr));
    }
}

