package cedo.ui.screens;

import cedo.ui.elements.Draw;
import cedo.util.font.FontUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class ChangeLog extends GuiScreen {


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        mc.getTextureManager().bindTexture(new ResourceLocation("Fan/changelog.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);

        Draw.color(0x9f000000);
        Draw.drawRoundedRect(this.width / 2 - 450, this.height / 2 - 240, 900, 480, 3);


        FontUtil.cleanlarge.drawString("Changelog:", this.width - 920, this.height - 485, -1);


    }

}
