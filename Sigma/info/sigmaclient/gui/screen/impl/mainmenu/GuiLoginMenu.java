package info.sigmaclient.gui.screen.impl.mainmenu;

import info.sigmaclient.Client;
import info.sigmaclient.gui.altmanager.Alt;
import info.sigmaclient.gui.altmanager.AltManager;
import info.sigmaclient.gui.screen.GuiLogin;
import info.sigmaclient.gui.screen.GuiPremiumAdvantages;
import info.sigmaclient.gui.screen.component.particles.ParticleManager;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuiLoginMenu extends GuiScreen {

    private ResourceLocation background = new ResourceLocation("textures/menu/mainmenubackground.png");
    private ResourceLocation text = new ResourceLocation("textures/menu/big.png");
    private ParticleManager particles = new ParticleManager();
    private String[] status = {"", "§6[Processing]", "§a[Success]", "§c[Failed]"};
    private ArrayList<Step> steps = new ArrayList<>();
    private GuiButton loginButton, retryButton, cancelButton;
    private String loginLink = null;
    private Step computeOldHwid, computeNewHwid, update, login;

    @Override
    public void initGui() {
        this.buttonList.add(loginButton = new GuiButton(1, this.width / 2 - 150 / 2, -100, 150, 20, "Login"));
        this.buttonList.add(retryButton = new GuiButton(2, this.width / 2 - 150 / 2, -100, 150, 20, "Retry"));
        this.buttonList.add(cancelButton = new GuiButton(3, this.width / 2 - 150 / 2, -100, 150, 20, "Continue without login"));
        if (steps.isEmpty()) {
            computeOldHwid = new Step("Compute old hardware id");
            computeNewHwid = new Step("Compute new hardware id");
            update = new Step("Check for updates");
            login = new Step("Login");
            steps.add(computeOldHwid);
            steps.add(computeNewHwid);
            steps.add(update);
            steps.add(login);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    computeOldHwid.processing();
                    Client.um.computeOldHwid();
                    computeOldHwid.success();

                    computeNewHwid.processing();
                    Client.um.computeNewHwid();
                    computeNewHwid.success();

                    update.processing();
                    Client.um.checkVersion();
                    update.success();

                    if (!Client.um.isUpdateNeeded()) {
                        login.processing();
                        loginLink = Client.um.getLoginLink();
                        if (loginLink == null) {
                            login.success();
                        } else {
                            login.failure();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int w = res.getScaledWidth();
        int h = res.getScaledHeight();
        mc.getTextureManager().bindTexture(background);
        drawScaledCustomSizeModalRect(0, 0, 0, 0, w + 2, h, w + 2, h, w + 2, h);

        GlStateManager.bindTexture(0);

        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, 50));
        particles.render(par1, par2);

        //this.drawCenteredString(this.fontRendererObj, "???", this.width / 2, 20, -1);

        int boxWidth = 200;
        RenderingUtil.rectangleBordered(this.width / 2 - boxWidth / 2, 60, this.width / 2 + boxWidth / 2, this.height - 50, 1.0f, Colors.getColor(225, 50), Colors.getColor(160, 150));
        GL11.glPushMatrix();
        //this.prepareScissorBox(0.0f, 33.0f, this.width, this.height - 50);
        GL11.glEnable(3089);
        int y = 38;

        GL11.glDisable(3089);
        GL11.glPopMatrix();

        TTFFontRenderer font = Client.fm.getFont("Verdana");

        int i = 0;
        for (Step step : steps) {
            font.drawStringWithShadow("- " + step.name + " " + status[step.status], this.width / 2 - boxWidth / 2 + 10, 70 + i * 15, -1);
            i++;
        }

        if (Client.um.isUpdateNeeded()) {
            RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, 230));
            boxWidth = 300;
            RenderingUtil.rectangleBordered(this.width / 2 - boxWidth / 2, 80, this.width / 2 + boxWidth / 2, 300, 1.0f, Colors.getColor(225, 50), Colors.getColor(160, 150));
            font.drawCenteredString("Updating " + Client.um.getUpdateProgress() + "%", this.width / 2, 85, -1);
            
            if (Client.um.getUpdateProgress() < 100) {
                font.drawStringWithShadow("Version " + Client.um.getNewVersionName(), this.width / 2 - boxWidth / 2 + 10, 100, -1);
                i = 0;
                String add = "\247a\247l+ \247r";
                String improved = "\247b\247l+ \247r";
                String change = "\2476\247l~ \247r";
                String fix = "\247b\247l> \247r";
                String rem = "\247c\247l- \247r";
                List<String> changeLog = Arrays.asList(improved + "Improved Arraylist",
                		improved + "Improved Hypixel Antibot",
                		add + "Added watchdog killer option",
                		add + "Added fast Hypixel fly",
                		improved + "Improved a lot hypixel speed",
                		rem + "Removed render stuff",
                		change + "New ESP Organisation",
                		improved + "Improved a lot AAC speed",
                		rem + "Removed yPort speed"
                		
                		
                );
                changeLog.sort(Comparator.comparingDouble(font::getWidth));
                Collections.reverse(changeLog);
                for (String changeL : changeLog) {
                    //font.drawStringWithShadow(changeL, this.width / 2 - boxWidth / 2 + 10, 115 + i * 15, Colors.getColor(255, 255, 255, 255));
                    i++;
                }
            } else {
                font.drawCenteredString("§bPlease restart the client", this.width / 2, 100, -1);
            }
        } else if (Client.um.isLoginNeeded()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiLogin());
            /*RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0, 230));
            boxWidth = 300;
            RenderingUtil.rectangleBordered(this.width / 2 - boxWidth / 2, 80, this.width / 2 + boxWidth / 2, 230, 1.0f, Colors.getColor(225, 50), Colors.getColor(160, 150));
            font.drawCenteredString("Login", this.width / 2, 85, -1);
            font.drawStringWithShadow("In order to use Sigma, please login or register", this.width / 2 - boxWidth / 2 + 10, 100, -1);
            font.drawStringWithShadow("Once you logged in please click retry", this.width / 2 - boxWidth / 2 + 10, 115, -1);
            loginButton.yPosition = 150;
            retryButton.yPosition = 175;
            cancelButton.yPosition = 200;*/
        } else if (login.status == 2) {
            Client.um.setFinishedLoginSequence();
            Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
            //Minecraft.getMinecraft().displayGuiScreen(new GuiPremiumAdvantages());
        } else {
            loginButton.yPosition = -100;
            retryButton.yPosition = -100;
            cancelButton.yPosition = -100;
        }

        //Ajout du logo
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(text);
        drawModalRectWithCustomSizedTexture(w / 2 - 75, 10, 0, 0, 150, 195 / 4, 150, 195 / 4);
        GlStateManager.disableBlend();


        /*TTFFontRenderer font = Client.fm.getFont("Verdana");
        float strWidth = font.getWidth("Need some alts?");
        font.drawCenteredString("Need some alts?", width - strWidth - 12, 6, -1);
        font.drawCenteredString("\247eVisit \247bsuperalts.com \247efor the most", width - strWidth - 12, 14, -1);
        font.drawCenteredString("\247areliable\247e, \247aaffordable\247e, and \247ahigh quality\247e alts.", width - strWidth - 12, 22, -1);*/
        super.drawScreen(par1, par2, par3);
    }

    private class Step {
        private String name;
        private int status = 0;

        public Step(String name) {
            this.name = name;
        }

        public void processing() {
            status = 1;
        }

        public void success() {
            status = 2;
        }

        public void failure() {
            status = 3;
        }
    }

    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1:
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URL(loginLink).toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                loginLink = Client.um.getLoginLink();
                if (loginLink == null) {
                    login.success();
                    Client.um.setFinishedLoginSequence();
                    Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
                }
                break;
            case 3:
                Minecraft.getMinecraft().displayGuiScreen(new GuiModdedMainMenu());
                Client.um.setFinishedLoginSequence();
                break;
        }
    }
}
