// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.GUI;

import java.awt.Color;
import java.net.MalformedURLException;
import me.CheerioFX.FusionX.utils.BrowserUtil;
import java.net.URL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StringTranslate;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.client.gui.GuiScreen;

public class FusionX_Options extends GuiScreen
{
    protected String screenTitle;
    protected GuiScreen guiScreen;
    
    public FusionX_Options(final GuiScreen GS) {
        this.screenTitle = "Client Options";
        this.guiScreen = GS;
    }
    
    @Override
    public void initGui() {
        FusionX.renderTheGui = false;
        final StringTranslate st = StringTranslate.getInstance();
        this.screenTitle = st.translateKey("Client Options");
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 8, "Client Website"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 15, 99, 20, "YouTube"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 2, this.height / 6 + 15, 99, 20, "Twitter"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 15, "Free Minecraft Accounts"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 3 + 15, "ChangeLog"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 100, this.height / 6 + 150, "Discord"));
        this.buttonList.add(new GuiButton(10, this.width / 2 - 100, this.height / 6 + 180, "Done"));
    }
    
    @Override
    protected void actionPerformed(final GuiButton gb) {
        if (!gb.enabled) {
            return;
        }
        switch (gb.id) {
            case 1: {
                try {
                    BrowserUtil.openWebpage(new URL("http://fusionxclient.weebly.com/"));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {
                try {
                    BrowserUtil.openWebpage(new URL("https://www.youtube.com/channel/UCXQb3R_8gpUVxS-6CNhdHgA"));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 3: {
                try {
                    BrowserUtil.openWebpage(new URL("https://twitter.com/CheerioFX"));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 5: {
                try {
                    BrowserUtil.openWebpage(new URL("https://dailymcalts.weebly.com/"));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 6: {
                try {
                    BrowserUtil.openWebpage(new URL("https://fusionxclient.weebly.com/client-info--updates"));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 7: {
                try {
                    BrowserUtil.openWebpage(new URL("https://discord.gg/x3ZMqVW"));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 10: {
                this.mc.displayGuiScreen(this.guiScreen);
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 20, new Color(255, 0, 0).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
