package nivia.gui.firsttime;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import nivia.utils.Helper;
import nivia.utils.TimeHelper;

import java.awt.*;

public class FirstTimeScreen extends GuiScreen {
	private TimeHelper time = new TimeHelper();
	private int i = 0;
	
	@Override
	public void initGui() {
		time.setLastMS();
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		//GlStateManager.disableBlend();
		GL11.glColor4f(1, 1, 1, 1);
		Helper.get2DUtils().drawRect(0, 0, width, height, new Color(120, 120, 120).getRGB());
		if (!time.isDelayComplete(1200)) {
			GL11.glScaled(1.5, 1.5, 1.5);
		Helper.mc().fontRendererObj.drawStringWithFagShadow("Welcome", (width / 3) - (Helper.get2DUtils().getStringWidth("Welcome") / 2), height / 3, new Color(200, 25, 255).getRGB());
		}
		if (time.isDelayComplete(1200) && i == 0) {
			i = 1;
		}
		if (!time.isDelayComplete(2200) && i == 1) {
			GL11.glScaled(1.5, 1.5, 1.5);
			Helper.mc().fontRendererObj.drawStringWithFagShadow("Welcome to Nivia", (width / 3) - (Helper.get2DUtils().getStringWidth("Welcome to Nivia") / 2), height / 3, new Color(200, 25, 255).getRGB());
		}
		if (time.isDelayComplete(2500) && i == 1) {
			i = 0;
			time.setLastMS();
		}
		GlStateManager.enableLighting();
		//GlStateManager.enableBlend();
		GL11.glPopMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
	
	int alpha;
	public void alpha() {
		if (alpha < 255)
			alpha++;
	}
	int p1, p2, p3, a1, a2, r1, r2, d1, d2, o1, x1, us;
	public void animate() {
		if (p1 < 60) {
			p1++;
		}
		if (p2 < 15) {
			p2++;
		}
		if (p3 < 180) {
			p3 += 3;
		}
		if (a1 < 30) {
			a1++;
		}
		if (a2 < 280) {
			a2 += 4;
		}
		if (r1 < 30) {
			r1++;
		}
		if (r2 < 90) {
			r2++;
		}
		if (d1 < 60) {
			d1++;
		}
		if (d2 < 320) {
			d2 += 5;
		}
		if (o1 < 360) {
			o1 += 6;
		}
		if (x1 < 40) {
			x1++;
		}
		if (us < 160) {
			us += 2;
		}
	}

}
