package modification.gui.screens;

import modification.gui.GuiPictureButton;
import net.minecraft.client.gui.*;

public final class GuiShowOptions
        extends GuiScreen {
    private final GuiMainMenu guiScreen;

    public GuiShowOptions(GuiMainMenu paramGuiMainMenu) {
        this.guiScreen = paramGuiMainMenu;
    }

    public void initGui() {
        int i = 48;
        String[] arrayOfString = {"Customize skin", "Sounds", "Video settings", "Controls", "Resource pack", "Chat", "Snooper"};
        int j = -2;
        int k = j;
        int m = this.guiScreen.startY | 0x7D;
        int n = 0;
        this.buttonList.add(new GuiPictureButton(n, k, m, 48, 48, n, 0, 7, 1, 48, arrayOfString[n], "controls"));
        k += 52;
        if (n == 3) {
            k = j | 0x1A;
            m += 52;
        }
        n++;
    }

    protected void actionPerformed(GuiButton paramGuiButton) {
        switch (paramGuiButton.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiCustomizeSkin(this.guiScreen));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this.guiScreen, this.mc.gameSettings));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiVideoSettings(this.guiScreen, this.mc.gameSettings));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiControls(this.guiScreen, this.mc.gameSettings));
                break;
            case 4:
                this.mc.displayGuiScreen(new GuiScreenResourcePacks(this.guiScreen));
                break;
            case 5:
                this.mc.displayGuiScreen(new ScreenChatOptions(this.guiScreen, this.mc.gameSettings));
                break;
            case 6:
                this.mc.displayGuiScreen(new GuiSnooper(this.guiScreen, this.mc.gameSettings));
        }
    }
}




