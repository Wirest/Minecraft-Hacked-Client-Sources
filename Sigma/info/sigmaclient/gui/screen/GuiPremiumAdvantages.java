package info.sigmaclient.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiPremiumAdvantages extends GuiScreen {

    @Override
    public void initGui() {
        buttonList.clear();

        buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, "Go premium"));
        buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 80, "Continue using the free version"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        int i = 0;
        drawString(this.mc.fontRendererObj, "Premium version:", this.width / 2 - 100, this.height / 2 - 80 + (i += 15), -1);
        drawString(this.mc.fontRendererObj, "- Hypixel, Cubecraft, Mineplex and more bypasses", this.width / 2 - 100, this.height / 2 - 80 + (i += 20), -1);
        drawString(this.mc.fontRendererObj, "- Premium modules", this.width / 2 - 100, this.height / 2 - 80 + (i += 15), -1);
        drawString(this.mc.fontRendererObj, "- All futures updates", this.width / 2 - 100, this.height / 2 - 80 + (i += 15), -1);
        drawString(this.mc.fontRendererObj, "9.99€ /lifetime", this.width / 2 - 100, this.height / 2 - 80 + (i += 20), -1);
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0:
                Minecraft.getMinecraft().displayGuiScreen(new GuiRedeemPremiumKey());
                break;
            case 1:
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
        }
    }
}
