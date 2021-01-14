package moonx.ohare.client.gui.proxy;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ProxyGUI extends GuiScreen {
    private GuiScreen prevMenu;
    private GuiTextField proxyBox;
    private String error;

    public ProxyGUI(final GuiScreen prevMultiplayerMenu) {
        this.error = "";
        this.prevMenu = prevMultiplayerMenu;
    }

    @Override
    public void updateScreen() {
        this.proxyBox.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72 + 12, "Connect"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96 + 12, "Reset"));
        (this.proxyBox = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(final GuiButton clickedButton) {
        if (clickedButton.enabled) {
            if (clickedButton.id == 0) {
                mc.displayGuiScreen(this.prevMenu);
            } else if (clickedButton.id == 1) {
                if (!this.proxyBox.getText().contains(":") || this.proxyBox.getText().split(":").length != 2) {
                    this.error = "Not a proxy!";
                    return;
                }
                final String[] parts = this.proxyBox.getText().split(":");
                if (Integer.parseInt(parts[1]) > 65536
                        || Integer.parseInt(parts[1]) < 0) {
                    this.error = "Invalid port!";
                    return;
                }
                try {
                    System.setProperty("socksProxyHost", parts[0]);
                    System.setProperty("socksProxyPort", parts[1]);
                } catch (Exception e) {
                    this.error = e.toString();
                }
            } else if (clickedButton.id == 2) {
                System.setProperty("socksProxyHost", "");
                System.setProperty("socksProxyPort", "");
            }
        }
    }

    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.proxyBox.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        super.mouseClicked(par1, par2, par3);
        this.proxyBox.mouseClicked(par1, par2, par3);
        if (this.proxyBox.isFocused()) {
            this.error = "";
        }
    }

    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Use Proxy", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "IP:Port (must be a SOCKS proxy)", this.width / 2 - 100, 47, 10526880);
        this.drawCenteredString(this.fontRendererObj, this.error, this.width / 2, 87, 16711680);
        String currentProxy = String.valueOf(System.getProperty("socksProxyHost")) + ":"
                + System.getProperty("socksProxyPort");
        if (currentProxy.equals(":") || currentProxy.equals("null:null")) {
            currentProxy = "none";
        }
        this.drawString(this.fontRendererObj, "Current proxy: " + currentProxy, this.width / 2 - 100, 97, 10526880);
        this.proxyBox.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
}
