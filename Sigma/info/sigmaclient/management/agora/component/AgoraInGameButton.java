package info.sigmaclient.management.agora.component;

import info.sigmaclient.Client;
import info.sigmaclient.management.agora.GuiAgora;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;

public class AgoraInGameButton extends GuiButton {

    boolean hovered;

    public AgoraInGameButton(int buttonId, int x, int y, int width, int height, String name) {
        super(buttonId, x, y, name);
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.displayString = name;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        hovered = mouseX >= xPosition && mouseX <= xPosition + width && mouseY > yPosition && mouseY <= yPosition + height;
        int s = res.getScaleFactor();

        TTFFontRenderer font = Client.fm.getFont("SFB 8");
        String str = displayString;
      	int inside = Colors.getColor(101, 32, 10, 125);
      	int strC = Colors.getColor(230);
      	if(hovered){
      		inside = Colors.getColor(151, 32, 10, 125);
      		strC =Colors.getColor(-1);
      	}
    	RenderingUtil.drawRoundedRect(xPosition, yPosition,width, height, inside);
    	font.drawString(str, xPosition +3, yPosition + 4, strC);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return visible && hovered;
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
    }


}
