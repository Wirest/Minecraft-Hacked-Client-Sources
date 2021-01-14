package me.Corbis.Execution.ui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.Corbis.Execution.ui.AltManager.Alt;
import me.Corbis.Execution.ui.AltManager.AltLoginThread;
import me.Corbis.Execution.ui.AltManager.AltManager;
import me.Corbis.Execution.ui.Buttons.GuiAnimatedButton;
import me.Corbis.Execution.utils.ServerUtil;
import me.Corbis.Execution.utils.TimeHelper;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.newdawn.slick.UnicodeFont;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BanChecker extends GuiScreen {
    UnicodeFontRenderer ufr;
    private AltLoginThread loginThread;
    public boolean start = false;
    GuiTextField textField;
    ServerData serverData;
    TimeHelper timer = new TimeHelper();
    int altsindex = 0;
    int currentCount = 0;
    boolean changeIP = false;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //this.textField.drawTextBox();
        if(ufr == null){
            ufr = UnicodeFontRenderer.getFontFromAssets("Roboto-Light", 20, 0, 10, 5);
        }
     //   mc.getTextureManager().bindTexture(new ResourceLocation("Execution/MainBackground.jpg"));
      //  ScaledResolution sr = new ScaledResolution(mc);
      //  Gui.drawModalRectWithCustomSizedTexture(0,0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
      //  ufr.drawCenteredString(mc.session.getUsername(), sr.getScaledWidth() / 2, sr.getScaledHeight() - sr.getScaledHeight() / 2 - 20, 0xFFFFFFFF);
      //  if(changeIP){
      //      ufr.drawCenteredString("Weve tried more than 4 attempts without an unbanned alt, please change your IP.", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 100, 0xFFFFFFFF);
      //  }

        ufr.drawCenteredString("Ban Checker not released yet!", 0, 0, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        if(start) {

            serverData = new ServerData("", "hypixel.net" + ":" + 25565, false);
            if(timer.hasReached(10000)){
                System.out.println(timer.getCurrentTime() + "Started Checking" + this.currentCount + " " + this.altsindex);
                if (getAlts().isEmpty()) return;

                Alt minecraftAccount = getAlts().get(altsindex);
                (this.loginThread = new AltLoginThread(minecraftAccount)).start();
                final Session auth = this.createSession(minecraftAccount.getUsername(), minecraftAccount.getPassword());
                if(auth == null){
                    return;
                }
                connectToLastServer();
                currentCount ++;
                altsindex++;
                if(currentCount >= 4){
                    currentCount = 0;
                    changeIP = true;
                    this.start = false;
                }
                timer.reset();

            }
        }
        super.updateScreen();
    }
    public void connectToLastServer() {
        if(serverData == null)
            return;

        mc.displayGuiScreen(new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), mc, serverData));
    }
    private Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }
    private List<Alt> getAlts() {
        List<Alt> altList = new ArrayList<>();
        for (final Alt alt : AltManager.registry) {

            altList.add(alt);

        }
        return altList;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.textField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void initGui() {
        changeIP = false;
        timer.reset();
        ScaledResolution sr = new ScaledResolution(mc);
        textField = new GuiTextField(2, mc.fontRendererObj, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 + 60, 200, 20);
        this.buttonList.add(new GuiAnimatedButton(0,sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2,"Start Checking Alts!"));
        this.buttonList.add(new GuiAnimatedButton(1,sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 + 30,"Stop Checking Alts!"));
        this.buttonList.add(new GuiAnimatedButton(2,sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 + 60,"Reset Index!"));

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id){
            case 0:
                changeIP = false;
                this.start = true;
                timer.reset();;
                break;
            case 1:
                this.start = false;
                break;
            case 2:
                this.currentCount = 0;
                this.altsindex = 0;
                break;
        }
        super.actionPerformed(button);
    }
}
