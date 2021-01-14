package net.minecraft.client.gui;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.components.Slider;
import info.sigmaclient.gui.screen.MusicScreen;
import info.sigmaclient.gui.screen.impl.mainmenu.ClientMainMenu;
import info.sigmaclient.gui.screen.impl.mainmenu.GuiUpgradesMenu;
import info.sigmaclient.management.MusicManager;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.management.users.impl.Staff;
import info.sigmaclient.management.users.impl.Upgraded;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.lwjgl.opengl.GL11.*;

public class GuiIngameMenu extends GuiScreen {
    private int field_146445_a;
    private int field_146444_f;
    private static final String __OBFID = "CL_00000703";
    private Expand expand = new Expand(0, 0, 0, 0);
    private MusicScreen musicScreen = new MusicScreen();

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        expand.setExpandY(0);

        this.field_146445_a = 0;
        this.buttonList.clear();
        byte var1 = -16;
        boolean var2 = true;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + var1, I18n.format("menu.returnToMenu")));

        if (!this.mc.isIntegratedServerRunning()) {
            ((GuiButton) this.buttonList.get(0)).displayString = I18n.format("menu.disconnect");
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + var1, I18n.format("menu.returnToGame")));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + var1, 98, 20, I18n.format("menu.options")));

        GuiButton var4;
        this.buttonList.add(var4 = new GuiButton(8, this.width / 2 - 100, this.height / 4 + 72 + var1, 98, 20, I18n.format("Upgrades")));
        var4.enabled = Client.um.getUser() instanceof Upgraded || Client.um.getUser() instanceof Staff;
        GuiButton var3;
        this.buttonList.add(new GuiButton(9, this.width / 2 + 2, this.height / 4 + 72 + var1, 98, 20, I18n.format("Toggle Blur")));
        this.buttonList.add(var3 = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + var1, 98, 20, I18n.format("menu.shareToLan")));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + var1, 98, 20, I18n.format("gui.achievements")));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + var1, 98, 20, I18n.format("gui.stats")));
        var3.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();

        /*List<MusicManager.Track> tr = MusicManager.getInstance().search("Monstercat muzzy endgame");
        MusicManager.getInstance().play(tr.get(0));*/
        //Minecraft.getMinecraft().displayGuiScreen(new MusicScreen());
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new ClientMainMenu());
            case 2:

            case 3:
            default:
                break;

            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;

            case 5:
                this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 6:
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;

            case 8:
                this.mc.displayGuiScreen(new GuiUpgradesMenu(this));
                break;
            case 9:
                Client.isLowEndPC = !Client.isLowEndPC;
                ClientMainMenu.save();
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        ++this.field_146444_f;

        expand.interpolate(0, 70, 3, 1);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game"), this.width / 2, 40, 16777215);

        Minecraft m = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(m, m.displayWidth, m.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        int s = res.getScaleFactor();
        glPushMatrix();
        glScissor(0 * s, (int) (height - expand.getExpandY()) * s, width * s, 100 * s);
        glEnable(GL_SCISSOR_TEST);

        musicScreen.renderMusicPlayer(width - 200, 5);

        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int clicX, int clicY, int button) {
        try {
            super.mouseClicked(clicX, clicY, button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicScreen.clickMusicPlayer(width - 200, 5, clicX, clicY);
    }
}
