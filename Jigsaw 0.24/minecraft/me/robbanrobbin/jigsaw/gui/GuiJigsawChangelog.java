package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import org.jsoup.nodes.Element;
import org.lwjgl.input.Mouse;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawChangelog extends GuiScreen {

	private int scroll = 0;
	private GuiScreen before;

	public GuiJigsawChangelog(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(1, width / 2 - 100, 50, 200, 20, "Cool cool!"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		if (!Jigsaw.changelogFailed) {
			if (Jigsaw.changeLineElmts != null) {
				GlStateManager.pushMatrix();
				GlStateManager.scale(1.8, 1.8, 1);
				drawString(fontRendererObj, "Changelog: ", 2, (int) Math.round(30 - scroll / 1.8), 0xffffff);
				GlStateManager.popMatrix();
				int sub = 80;
				for (Element elmt : Jigsaw.changeLineElmts) {
					if (elmt.ownText().trim().equals(Jigsaw.getClientVersion())) {
						drawString(fontRendererObj, "§c§l" + elmt.ownText() + "§6 - Your version", 5, sub - scroll,
								0xffffff);
					} else if (elmt.ownText().trim().equals(Jigsaw.serverVersion)) {
						drawString(fontRendererObj, "§e§l" + elmt.ownText() + "§6 - Latest version", 5, sub - scroll,
								0xffffff);
					} else {
						drawString(fontRendererObj, "§l" + elmt.ownText(), 5, sub - scroll, 0xffffff);
					}

					sub += 10;
					for (Element elmtChild : elmt.child(0).children()) {
						drawString(fontRendererObj,
								elmtChild.text()
										.replaceAll("Removed", "§cRemoved§r")
										.replaceAll("removed", "§cremoved§r").replaceAll("Fixed", "§bFixed§r")
										.replaceAll("fixed", "§bfixed§r").replaceAll("Added", "§aAdded§r")
										.replaceAll("added", "§aadded§r").replaceAll("bug", "§7bug§r")
										.replaceAll("§7bug§rs", "§7bugs§r").replaceAll("Bug", "§7Bug§r")
										.replaceAll("§7Bug§rs", "§7Bugs§r").replaceAll("Buggy", "§7Buggy§r"),
								10, sub - scroll, 0xcfcfcf);
						sub += 10;
					}
				}
				drawRect(width - 10, 0, width, height, 0xff000000);
				drawRect(width - 10, 0 + scroll, width, 145 + scroll, 0xffffffff);
				drawRect(width - 8, 0 + scroll, width, 145 + scroll, 0xffcccccc);
				drawRect(width - 8, 0 + scroll, width - 2, 145 + scroll, 0xffdddddd);
			} else {
				drawString(fontRendererObj, "§7Loading changelog...", 4, height / 2 - 30 - scroll, 0xffffff);
			}
		} else {
			drawString(fontRendererObj, "§cCould not get changelog!", 4, height / 2 - 30 - scroll, 0xffffff);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}
			i *= 20;
			if (scroll - i < 0) {
				return;
			}
			this.scroll -= i;
			for (int ii = 0; ii < this.buttonList.size(); ++ii) {
				((GuiButton) this.buttonList.get(ii)).yPosition += i;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if (id == 1) {
			mc.displayGuiScreen(before);
		}
		super.actionPerformed(button);
	}

	// No escape here
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

	}

}
