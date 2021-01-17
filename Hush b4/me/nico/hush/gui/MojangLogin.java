// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.gui;

import net.minecraft.client.gui.FontRenderer;
import de.Hero.clickgui.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class MojangLogin extends GuiScreen
{
    private GuiScreen parent;
    private String status;
    private GuiTextField login;
    private String format;
    
    public MojangLogin(final GuiScreen guiMainMenu) {
        this.parent = guiMainMenu;
    }
    
    @Override
    public void initGui() {
        this.status = "Waiting...";
        this.format = "(email:password)";
        final int width = 100;
        final int height = 20;
        final int offset = 2;
        this.buttonList.add(new Button(0, this.width / 2 - width / 2, this.height - (height + offset) * 2, width, height, "Login"));
        this.buttonList.add(new Button(1, this.width / 2 - width / 2, this.height - height - offset, width, height, I18n.format("gui.back", new Object[0])));
        (this.login = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4, 200, 20)).setFocused(true);
        this.login.setText("");
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.login.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.actionPerformed(this.buttonList.get(1));
        }
        if (keyCode == 28) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            if (this.login.getText() != null && !this.login.getText().isEmpty()) {
                if (!this.login.getText().contains(" ")) {
                    try {
                        final String[] args = this.login.getText().split(":");
                        if (args[0].contains("@")) {
                            final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                            authentication.setUsername(args[0]);
                            authentication.setPassword(args[1]);
                            try {
                                authentication.logIn();
                                this.mc.session = new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang");
                                System.out.println(this.status = "§aLogged in successfully as: §7" + this.mc.session.getUsername());
                            }
                            catch (Exception e) {
                                System.out.println(this.status = "ERROR: §cACCOUNT HAS INVALID INITIALIS");
                            }
                        }
                        else {
                            System.out.println(this.status = "ERROR: §cACCOUNT IS NOT PREMIUM");
                        }
                    }
                    catch (Exception e2) {
                        System.out.println(this.status = "ERROR: §cINVALID ACCOUNT");
                    }
                }
                else {
                    System.out.println(this.status = "ERROR: §cINVALID ACCOUNT");
                }
            }
            else {
                System.out.println(this.status = "ERROR: §cNO ACCOUNT IN FIELD");
            }
        }
        else if (button.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        final ScaledResolution s1 = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("Zodiac/MainBackGround.jpg"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, s1.getScaledWidth(), s1.getScaledHeight(), (float)s1.getScaledWidth(), (float)s1.getScaledHeight());
        Gui.drawRect(0.0, 0.0, this.width, this.height, 1073741824);
        this.drawCenteredString(this.fontRendererObj, "Account Login", this.width / 2, 10, ColorUtil.getClickGUIColor().getRGB());
        this.drawCenteredString(this.fontRendererObj, this.status, this.width / 2, 20, ColorUtil.getClickGUIColor().getRGB());
        this.drawCenteredString(this.fontRendererObj, this.format, this.width / 2, 100, ColorUtil.getClickGUIColor().getRGB());
        this.login.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
