package net.minecraft.client.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Wrapper;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.UI.AltGenerator.AltGeneratorLogin;
import org.m0jang.crystal.UI.AltGenerator.Generate;
import org.m0jang.crystal.UI.AltGenerator.GeneratorType;
import org.m0jang.crystal.Utils.LoginUtils;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    private static final String __OBFID = "CL_00000693";
    boolean doReconnect;
    
    public GuiDisconnected(GuiScreen p_i45020_1_, String p_i45020_2_, IChatComponent p_i45020_3_)
    {
        this.parentScreen = p_i45020_1_;
        this.reason = I18n.format(p_i45020_2_, new Object[0]);
        this.message = p_i45020_3_;
        this.doReconnect = false;
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = Fonts.segoe18.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.field_175353_i = this.multilineMessage.size() * Fonts.segoe18.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + Fonts.segoe18.FONT_HEIGHT, I18n.format("gui.toMenu")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 25 + Fonts.segoe18.FONT_HEIGHT, "Reconnect with a Generated Alt"));
     }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
           this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 1) {
           ((GuiButton)this.buttonList.get(1)).enabled = false;
           (new Thread(new Runnable() {
              public void run() {
                 try {
                    String[] alt = Generate.GetAltFromFastAlts((String[])AltGeneratorLogin.Idents.get(GeneratorType.FASTALTS));
                    if (alt == null) {
                       ((GuiButton)GuiDisconnected.this.buttonList.get(1)).enabled = true;
                       return;
                    }

                    String reply = LoginUtils.login(alt[0], alt[1]);
                    if (reply != null) {
                       ((GuiButton)GuiDisconnected.this.buttonList.get(1)).enabled = true;
                       return;
                    }
                 } catch (Exception var3) {
                    ;
                 }

                 ((GuiButton)GuiDisconnected.this.buttonList.get(1)).enabled = true;
                 GuiDisconnected.this.doReconnect = true;
              }
           })).start();
        }

     }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.doReconnect) {
           Wrapper.mc.displayGuiScreen(new GuiConnecting((GuiScreen)null, Wrapper.mc, Crystal.INSTANCE.getLastServer()));
        } else {
           this.drawDefaultBackground();
           this.drawCenteredString(Fonts.segoe18, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - Fonts.segoe18.FONT_HEIGHT * 2, 11184810);
           int var4 = this.height / 2 - this.field_175353_i / 2;
           if (this.multilineMessage != null) {
              for(Iterator var5 = this.multilineMessage.iterator(); var5.hasNext(); var4 += Fonts.segoe18.FONT_HEIGHT) {
                 String var6 = (String)var5.next();
                 this.drawCenteredString(Fonts.segoe18, var6, this.width / 2, var4, 16777215);
              }
           }

        super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
