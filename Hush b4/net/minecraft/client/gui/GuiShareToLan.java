// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.resources.I18n;

public class GuiShareToLan extends GuiScreen
{
    private final GuiScreen field_146598_a;
    private GuiButton field_146596_f;
    private GuiButton field_146597_g;
    private String field_146599_h;
    private boolean field_146600_i;
    
    public GuiShareToLan(final GuiScreen p_i1055_1_) {
        this.field_146599_h = "survival";
        this.field_146598_a = p_i1055_1_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("lanServer.start", new Object[0])));
        this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(this.field_146597_g = new GuiButton(104, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
        this.buttonList.add(this.field_146596_f = new GuiButton(103, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
        this.func_146595_g();
    }
    
    private void func_146595_g() {
        this.field_146597_g.displayString = String.valueOf(I18n.format("selectWorld.gameMode", new Object[0])) + " " + I18n.format("selectWorld.gameMode." + this.field_146599_h, new Object[0]);
        this.field_146596_f.displayString = String.valueOf(I18n.format("selectWorld.allowCommands", new Object[0])) + " ";
        if (this.field_146600_i) {
            this.field_146596_f.displayString = String.valueOf(this.field_146596_f.displayString) + I18n.format("options.on", new Object[0]);
        }
        else {
            this.field_146596_f.displayString = String.valueOf(this.field_146596_f.displayString) + I18n.format("options.off", new Object[0]);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 102) {
            this.mc.displayGuiScreen(this.field_146598_a);
        }
        else if (button.id == 104) {
            if (this.field_146599_h.equals("spectator")) {
                this.field_146599_h = "creative";
            }
            else if (this.field_146599_h.equals("creative")) {
                this.field_146599_h = "adventure";
            }
            else if (this.field_146599_h.equals("adventure")) {
                this.field_146599_h = "survival";
            }
            else {
                this.field_146599_h = "spectator";
            }
            this.func_146595_g();
        }
        else if (button.id == 103) {
            this.field_146600_i = !this.field_146600_i;
            this.func_146595_g();
        }
        else if (button.id == 101) {
            this.mc.displayGuiScreen(null);
            final String s = this.mc.getIntegratedServer().shareToLAN(WorldSettings.GameType.getByName(this.field_146599_h), this.field_146600_i);
            IChatComponent ichatcomponent;
            if (s != null) {
                ichatcomponent = new ChatComponentTranslation("commands.publish.started", new Object[] { s });
            }
            else {
                ichatcomponent = new ChatComponentText("commands.publish.failed");
            }
            this.mc.ingameGUI.getChatGUI().printChatMessage(ichatcomponent);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("lanServer.title", new Object[0]), this.width / 2, 50, 16777215);
        this.drawCenteredString(this.fontRendererObj, I18n.format("lanServer.otherPlayers", new Object[0]), this.width / 2, 82, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
