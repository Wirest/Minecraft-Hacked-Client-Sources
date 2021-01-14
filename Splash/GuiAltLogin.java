package splash.client.login;

import java.awt.*;
import java.io.IOException;

import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import splash.Splash;
import splash.client.login.thread.AuthThread;
import splash.utilities.clipboard.ClipboardUtils;
import splash.utilities.visual.RenderUtilities;

public final class GuiAltLogin
extends GuiScreen {
    private PasswordField password;
    private final GuiScreen previousScreen;
    private AuthThread mojangThread;
    private GuiTextField username;

    private LoginType auth = LoginType.MOJANG;
    private TheAlteningAuthentication accountAuth = new TheAlteningAuthentication(AlteningServiceType.MOJANG);


    public enum LoginType {
        THEALTENING, MOJANG;
    }

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                    switch(auth) {
                        case MOJANG: {
                            this.mojangThread = new AuthThread(this.username.getText(), this.password.getText());
                            this.mojangThread.start();
                            break;
                        }
                        case THEALTENING: {
                            this.mojangThread = new AuthThread(this.username.getText(), "gay");
                            this.mojangThread.start();
                            break;
                        }
                    }
                    break;
            }
            case 3: {
               switch(auth) {
                   case MOJANG: {
                       accountAuth.updateService(AlteningServiceType.THEALTENING);
                       auth = LoginType.THEALTENING;
                       break;
                   }
                   case THEALTENING: {
                       accountAuth.updateService(AlteningServiceType.MOJANG);
                       auth = LoginType.MOJANG;
                       break;
                   }
               }
                break;
            }
            case 2: {
            	if (ClipboardUtils.getLatestPaste() != null) {
	                String result = ClipboardUtils.getLatestPaste();
	                if(result.split(":") != null) {
	                	if (result.split(":")[0] != null && result.split(":")[1] != null) {
	                		this.username.setText(result.split(":")[0]);
	                		this.password.setText(result.split(":")[1]);
	                	}
	                	
	                } else if(result.split(":") == null) {
	                    this.username.setText("ERROR");
	                }
            	}
            	try {
                	if (ClipboardUtils.getLatestPaste() != null) {
    	                String result = ClipboardUtils.getLatestPaste();
    	                if(result.split(":") != null) {
    	                    this.username.setText(result.split(":")[0]);
    	                    this.password.setText(result.split(":")[1]);
    	                } else if(result.split(":") == null) {
    	                    this.username.setText("ERROR");
    	                }
                	}
            	} catch (Exception e) {}
                break;
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
        RenderUtilities.INSTANCE.drawBackground(new ResourceLocation("splash/bg.jpg"));
        this.username.drawTextBox();
        this.password.drawTextBox();
        switch(auth) {
            case THEALTENING: {
                Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(EnumChatFormatting.BOLD + auth.name(), width / 2, 20, Color.green.getRGB());
                break;
            }
            case MOJANG: {
                Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(EnumChatFormatting.BOLD + auth.name(), width / 2, 20, -1);
                break;
            }
        }
        Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(this.mojangThread == null ? (Object)((Object)EnumChatFormatting.GRAY) + "Idle..." : this.mojangThread.getStatus(), width / 2, 29, -1);
        if (this.username.getText().isEmpty()) {
            Splash.getInstance().getFontRenderer().drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            Splash.getInstance().getFontRenderer().drawStringWithShadow( "Password", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 94, "Back"));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, var3 + 72 + 34, "Import User:Pass"));
        this.buttonList.add(new GuiButton(3, width / 2 - 100, var3 + 72 + 64, "Authentication"));
        this.username = new GuiTextField(var3, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }



    @Override
    protected void keyTyped(char character, int key) {
        if(key == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(this.previousScreen);
        }
        try {
            super.keyTyped(character, key);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}

