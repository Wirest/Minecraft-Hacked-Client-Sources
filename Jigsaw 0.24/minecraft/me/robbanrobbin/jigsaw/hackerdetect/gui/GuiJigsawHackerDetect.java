package me.robbanrobbin.jigsaw.hackerdetect.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.HackerDetect;
import me.robbanrobbin.jigsaw.client.modules.NameProtect;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.hackerdetect.Hacker;
import me.robbanrobbin.jigsaw.hackerdetect.checks.Check;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawHackerDetect extends GuiScreen {
	private GuiScreen before;
	public Hacker selected;
	public int scroll;

	public GuiJigsawHackerDetect(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(1, 205 - 200, height - 25, 70, 20, "Reset All"));
		this.buttonList.add(new GuiButton(3, 280 - 200, height - 25, 70, 20, "Auto Notify"));
		this.buttonList.add(new GuiButton(4, 355 - 200, height - 25, 80, 20, "Denotify All"));
		this.buttonList.add(new GuiButton(2, 440 - 200, height - 25, 100, 20, "Notify Selected"));
		this.buttonList.add(new GuiButton(5, 545 - 200, height - 25, 77, 20, "More Info"));
		this.buttonList.add(new GuiButton(6, 627 - 200, height - 25, 50, 20, "Back"));
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		{
			this.buttonList.get(3).enabled = selected != null;
			ArrayList<Hacker> list = new ArrayList<Hacker>();
			list.addAll(HackerDetect.players.values());
			if (Jigsaw.java8) {
				list.sort(new Comparator<Hacker>() {
					public int compare(Hacker o1, Hacker o2) {
						if (o1.getViolations() > o2.getViolations()) {
							return -1;
						}
						if (o1.getViolations() < o2.getViolations()) {
							return 1;
						}
						return 0;
					};
				});
			}
			this.drawDefaultBackground();
			drawRect(0, 0, 200, height, 0x2f000000);
			int i = 0;
			int index = 0;
			for (Hacker hacker : list) {
				drawRect(0, i - scroll, 200, i + 20 - scroll, 0x2f000000);

				drawHorizontalLine(0, 200, i + 20 - scroll - 1, 0xffffffff);

				if (selected != null && selected.equals(hacker)) {
					drawRect(0, i - scroll, 200, i + 20 - scroll - 1, 0xaf707070);
				}
				int s = hacker.getViolations();
				if (NameProtect.replacements.containsKey(hacker.player.getName())) {
					drawString(fontRendererObj,
							NameProtect.replacements.get(hacker.player.getName()) + " " + (s == 0 ? "§r" : "§c") + (s),
							6, i + 7 - scroll, 0xffffffff);
				} else {
					drawString(fontRendererObj, hacker.player.getName() + " " + (s == 0 ? "§r" : "§c") + (s), 6,
							i + 7 - scroll, 0xffffffff);
				}
				if (!HackerDetect.muted.contains(hacker.player.getName())) {
					drawString(fontRendererObj, "§6Notifying", 200 - fontRendererObj.getStringWidth("§6Notifying") - 5,
							i + 7 - scroll, 0xffffffff);
				}
				i += 20;
				index++;
				if (i - scroll >= height - 30) {
					break;
				}
			}
			if (selected != null) {
				GlStateManager.pushMatrix();
				GlStateManager.scale(2, 2, 0);
				String name = null;
				if (NameProtect.replacements.containsKey(selected.player.getName())) {
					name = NameProtect.replacements.get(selected.player.getName());
				} else {
					name = selected.player.getName();
				}
				drawString(fontRendererObj, name, 210 / 2, 10 / 2, 0xffffffff);
				drawHorizontalLine(216 / 2, (fontRendererObj.getStringWidth(name) * 2 + 199) / 2, 30 / 2 + 2,
						0xffffffff);
				GlStateManager.popMatrix();
				int y = 45;
				for (Check check : selected.checks) {
					boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
					GlStateManager.enableBlend();
					int enabledColor = 0xffffffff;
					int disabledColor = 0x5affffff;
					if (ClientSettings.hackerDetectMoreInfo) {
						drawString(fontRendererObj,
								(check.isEnabled() ? "" : "§m") + check.getName() + ": §c" + check.getViolations()
										+ "§7 : §2" + check.tempViolations + "§7/§2" + check.getMaxViolations()
										+ ((check.timer.getTime() < check.getDecayTime())
												? "§r Decay: §c" + check.timer.getTime() : ""),
								216, y, check.isEnabled() ? enabledColor : disabledColor);
					} else {
						drawString(fontRendererObj,
								(check.isEnabled() ? "" : "§m") + check.getName() + ": §c" + check.getViolations(), 216,
								y, check.isEnabled() ? enabledColor : disabledColor);
					}
					if (!blend) {
						GlStateManager.disableBlend();
					}
					y += 10;
				}
				drawHorizontalLine(216, fontRendererObj.getStringWidth(name) * 2 + 199, y + 30, 0xffffffff);

				GlStateManager.pushMatrix();
				GlStateManager.scale(2, 2, 0);
				drawCenteredString(fontRendererObj, "Info",
						210 / 2 + fontRendererObj.getStringWidth(selected.player.getName()) / 2, (y + 10) / 2,
						0xffffffff);
				GlStateManager.popMatrix();
				drawString(fontRendererObj, "Max APS" + ": §c" + (int) Math.floor((selected.maxAps * 0.8)) + " §7- §4"
						+ (int) Math.ceil((selected.maxAps * 2)) + " §7(Not perfect)", 216, y + 40, 0xffffffff);
				drawString(fontRendererObj, "Current APS" + ": §c" + (int) Math.floor((selected.player.aps * 0.8))
						+ " §7- §4" + (int) Math.ceil((selected.player.aps * 2)), 216, y + 50, 0xffffffff);
				drawString(fontRendererObj, "Max Yawrate" + ": §c" + (float) selected.maxYawrate, 216, y + 60,
						0xffffffff);
				drawString(fontRendererObj,
						"Current Yawrate" + ": §c"
								+ (float) Math.abs(selected.player.rotationYaw - selected.player.prevRotationYaw),
						216, y + 70, 0xffffffff);
			}
			drawVerticalLine(200, 0, height - 30, 0xffffafaf);
			drawHorizontalLine(0, width, height - 30, 0xffffafaf);
			if (ClientSettings.hackerDetectAutoNotify) {
				drawString(fontRendererObj, "§6AutoNotifying...", 205, height - 40, 0xffffffff);
			}
		}
		for (int i = 0; i < this.buttonList.size(); ++i) {
			((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
		}

		for (int j = 0; j < this.labelList.size(); ++j) {
			((GuiLabel) this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
		}
		drawCenteredString(fontRendererObj, "Jigsaw HackerDetect GUI BETA", width / 2, 5, 0xffdddddd);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1) {
			HackerDetect.players.clear();
			selected = null;
			HackerDetect.muted.clear();
		}
		if (button.id == 2) {
			if (selected == null) {
				return;
			}
			if (HackerDetect.muted.contains(selected.player.getName())) {
				HackerDetect.muted.remove(selected.player.getName());
			} else {
				HackerDetect.muted.add(selected.player.getName());
			}

		}
		if (button.id == 3) {
			ClientSettings.hackerDetectAutoNotify = !ClientSettings.hackerDetectAutoNotify;
			if (ClientSettings.hackerDetectAutoNotify) {
				HackerDetect.muted.clear();
			}
		}
		if (button.id == 4) {
			// ClientSettings.hackerDetectAutoNotify = false;
			HackerDetect.muted.addAll(HackerDetect.players.keySet());
		}
		if (button.id == 5) {
			ClientSettings.hackerDetectMoreInfo = !ClientSettings.hackerDetectMoreInfo;
		}
		if (button.id == 6) {
			mc.displayGuiScreen(before);
		}
		super.actionPerformed(button);
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
			// for (int ii = 0; ii < this.buttonList.size(); ++ii)
			// {
			// ((GuiButton)this.buttonList.get(ii)).yPosition += i;
			// }
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		ArrayList<Hacker> list = new ArrayList<Hacker>();
		list.addAll(HackerDetect.players.values());
		if (Jigsaw.java8) {
			list.sort(new Comparator<Hacker>() {
				public int compare(Hacker o1, Hacker o2) {
					if (o1.getViolations() > o2.getViolations()) {
						return -1;
					}
					if (o1.getViolations() < o2.getViolations()) {
						return 1;
					}
					return 0;
				};
			});
		}
		int i = 0;
		for (Hacker hacker : list) {
			if (mouseX > 0 && mouseX < 200 && mouseY > i - scroll && mouseY < i + 20 - scroll) {
				selected = hacker;
				return;
			}
			i += 20;
			if (i - scroll >= height - 30) {
				break;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {

		super.mouseReleased(mouseX, mouseY, state);
	}
}
