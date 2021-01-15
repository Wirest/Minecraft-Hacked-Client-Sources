// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.UI;

import org.lwjgl.input.Keyboard;
import java.io.IOException;
import cf.euphoria.euphorical.Euphoria;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import cf.euphoria.euphorical.Mod.Mod;
import net.minecraft.client.gui.GuiScreen;

public class GuiKeybindChanger extends GuiScreen
{
    private Mod theMod;
    
    public GuiKeybindChanger(final Mod theMod) {
        this.theMod = theMod;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 - 16, I18n.format("menu.returnToMenu", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(Euphoria.getEuphoria().getKeybindGui());
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode != 57 && keyCode != 1) {
            this.theMod.setBind(keyCode);
        }
        if (keyCode == 57) {
            this.theMod.setBind(0);
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(Euphoria.getEuphoria().getKeybindGui());
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Change the keybind for: " + this.theMod.getModName(), this.width / 2, 40, -1);
        this.drawCenteredString(this.fontRendererObj, "Current Bind: " + Keyboard.getKeyName(this.theMod.getBind()), this.width / 2, 50, -1);
        this.drawCenteredString(this.fontRendererObj, "Use Spacebar to set it to none and escape or the bottom to close out of this", this.width / 2, 60, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
