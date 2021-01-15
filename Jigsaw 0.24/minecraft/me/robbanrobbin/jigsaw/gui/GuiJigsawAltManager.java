package me.robbanrobbin.jigsaw.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.robbanrobbin.jigsaw.client.alts.Alt;
import me.robbanrobbin.jigsaw.client.alts.Login;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.animations.Animation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiJigsawAltManager extends GuiScreen {

	private GuiScreen before = null;
	private GuiTextField searchField;

	private GuiTextField emailField;
	private GuiTextField passField;

	private boolean displayingLogin = false;
	
	private final JFileChooser fileChooser;

	private int selected = 0;
	
	private JFrame frame = new JFrame("FileChooser");
	
	private int scroll = 0;
	
	private boolean failed = false;
	private String failMessage = null;
	
	public GuiJigsawAltManager(GuiScreen before) {
		this.before = before;
		fileChooser = new JFileChooser();
	}

	Animation anim = new Animation() {

		public int getMaxTime() {
			return 5;
		};

		@Override
		public void render() {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.enableBlend();
			GlStateManager.disableTexture2D();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.color(0f, 0f, 0f, (float) time / 5.1f);
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos((double) 0, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) height + 1, 0.0D).endVertex();
			worldrenderer.pos((double) width, (double) 0, 0.0D).endVertex();
			worldrenderer.pos((double) 0, (double) 0, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	};

	@Override
	public void initGui() {
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		
		this.buttonList.add(new GuiButton(0, width / 2 - 90, height - 44 - 5 + 26, 88, 20, "Login Selected"));
		this.buttonList.add(new GuiButton(1, width / 2 + 0, height - 44 - 5 + 26, 88, 20, "Remove Selected"));
		this.buttonList.add(new GuiButton(2, 3, 3, 88, 20, "Direct Login"));
		this.buttonList.add(new GuiButton(3, width / 2 + 90, height - 44 - 5 + 26, 48, 20, "Back"));
		this.buttonList.add(new GuiButton(4, width / 2 - 140, height - 44 - 5 + 26, 48, 20, "Add Alt"));
		this.buttonList.add(new GuiButton(5, width - 135, 5, 133, 20, "Import from Accountlist"));
		this.buttonList.add(new GuiButton(6, width - 135, 28, 133, 20, "Export to Accountlist"));
		
		this.buttonList.add(new GuiButton(10, this.width / 2 + 1 + 100, this.height / 2, 48, 15, "Add"));
		
		this.buttonList.add(new GuiButton(11, this.width / 2 - 50, this.height - 80, 100, 20, "Okay"));
		
		this.buttonList.get(7).visible = false;
		this.buttonList.get(8).visible = false;

		searchField = new GuiTextField(7, this.fontRendererObj, width / 2 - 50, 50, 98, 15);
		searchField.setMaxStringLength(666);
		searchField.setFocused(true);

		emailField = new GuiTextField(8, this.fontRendererObj, this.width / 2 - 99 - 50, this.height / 2, 145, 15);
		emailField.setMaxStringLength(666);

		passField = new GuiTextField(9, this.fontRendererObj, this.width / 2 + 1, this.height / 2, 95, 15);
		passField.setMaxStringLength(666);

		if (displayingLogin) {
			displayLogin();
		} else {
			stopDisplayingLogin();
		}
		
		if (failed) {
			startDisplayingFailed();
		} else {
			stopDisplayingFailed();
		}
	}

	public void displayLogin() {
		searchField.setFocused(false);
		displayingLogin = true;
		anim.on();
		((GuiButton) this.buttonList.get(7)).visible = true;
		for (GuiButton btn : this.buttonList) {
			btn.enabled = btn.displayString.equals("Add") || btn.displayString.equals("Back");
		}
	}

	public void stopDisplayingLogin() {
		searchField.setFocused(true);
		displayingLogin = false;
		anim.off();
		((GuiButton) this.buttonList.get(7)).visible = false;
		for (GuiButton btn : this.buttonList) {
			btn.enabled = true;
		}
	}
	
	public void startDisplayingFailed() {
		searchField.setFocused(false);
		failed = true;
		anim.on();
		((GuiButton) this.buttonList.get(8)).visible = true;
		for (GuiButton btn : this.buttonList) {
			btn.enabled = btn.displayString.equals("Okay");
		}
	}
	
	public void stopDisplayingFailed() {
		searchField.setFocused(true);
		failed = false;
		anim.off();
		((GuiButton) this.buttonList.get(8)).visible = false;
		for (GuiButton btn : this.buttonList) {
			btn.enabled = true;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 2) {
			mc.displayGuiScreen(new GuiJigsawAltLogin(before));
		}
		if (button.id == 3) {
			if (displayingLogin) {
				stopDisplayingLogin();
			} else {
				mc.displayGuiScreen(before);
			}

		}
		if (button.id == 4) {
			displayLogin();
		}
		if (button.id == 10) {
			Alt alt = new Alt(emailField.getText(), passField.getText());
			Jigsaw.getAltManager().addAlt(alt);
			stopDisplayingLogin();
		}
		if (button.id == 11) {
			stopDisplayingFailed();
		}
		if(button.id == 1) {
			Jigsaw.getAltManager().getAlts().remove(Jigsaw.getAltManager().getAlts().get(selected));
		}
		if(button.id == 0) {
			Alt alt = Jigsaw.getAltManager().getAlts().get(selected);
			if(alt.cracked) {
				Login.changeName(alt.name);
				Jigsaw.loggedInName = Jigsaw.getAltManager().getAlts().get(selected).name;
			}
			else {
				try {
					boolean changeIndex = Login.login(alt.email, alt.password);
					if(changeIndex) {
						Jigsaw.loggedInName = Jigsaw.getAltManager().getAlts().get(selected).name;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Login failed!");
					failMessage = e.getMessage();
					startDisplayingFailed();
				}
			}
			
		}
		if(button.id == 5) {
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);
			int result = fileChooser.showOpenDialog(frame);
			frame.setVisible(false);
			if(result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if(file.getName().endsWith(".txt")) {
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						String line;
						while ((line = br.readLine()) != null) {
							String email = line.split(":")[0];
							String pass = null;
							if(line.split(":").length > 1) {
								pass = line.split(":")[1];
							}
							if(pass != null && pass.isEmpty()) {
								Alt alt = new Alt(email, null);
								Jigsaw.getAltManager().addAlt(alt);
							}
							else {
								Alt alt = new Alt(email, pass);
								Jigsaw.getAltManager().addAlt(alt);
							}
						}
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(button.id == 6) {
			frame.setVisible(true);
			frame.setAlwaysOnTop(true);
			int result = fileChooser.showSaveDialog(frame);
			frame.setVisible(false);
			if(result == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				if(file.getName().endsWith(".txt")) {
					file.delete();
					try {
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
						for (Alt alt : Jigsaw.getAltManager().getAlts()) {
							if(alt.cracked) {
								pw.println(alt.name);
							}
							else {
								pw.println(alt.email + ":" + alt.password);
							}
						}
						pw.flush();
						pw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		super.actionPerformed(button);
	}

	@Override
	public void updateScreen() {
		searchField.updateCursorCounter();
		emailField.updateCursorCounter();
		passField.updateCursorCounter();
		anim.update();
		if(selected >= Jigsaw.getAltManager().getAlts().size()) {
			selected = Jigsaw.getAltManager().getAlts().size() - 1;
		}
		if(!displayingLogin) {
			emailField.setText("");
			passField.setText("");
		}
		this.buttonList.get(0).enabled = !Jigsaw.getAltManager().getAlts().isEmpty();
		this.buttonList.get(1).enabled = !Jigsaw.getAltManager().getAlts().isEmpty();
		super.updateScreen();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		drawRect(0, 50, width, height - 25, 0xad000000);
		GlStateManager.scale(2, 2, 1);
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, width / 2 / 2, (10) / 4, 0xffffffff);
		drawCenteredString(fontRendererObj, "§7Alt Manager", this.width / 2 / 2, (49 - 25) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (50 - 5) / 1, 0xffaaaaaa);
		searchField.drawTextBox();

		int offsetY = 67;

		int alts = 0;
		for (Alt alt : Jigsaw.getAltManager().getAlts()) {
			if(alt.name.toLowerCase().indexOf(searchField.getText().toLowerCase()) == -1) {
				continue;
			}
			int left = width / 2 - 150;
			int right = width / 2 + 150;
			int top = offsetY + (alts * 21) + scroll;
			int bottom = offsetY + ((alts * 21) + 20) + scroll;
			if(top < 51) {
				alts++;
				continue;
			}
			boolean hover = mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
			drawRect(left, top, right, bottom, hover ? 0xaa302020 : 0xaa202020);
			drawHorizontalLine(left, right - 1, top, 0xffffffff);
			drawHorizontalLine(left, right - 1, bottom - 1, 0xffffffff);
			drawVerticalLine(left, top, bottom, 0xffffffff);
			drawVerticalLine(right - 1, top, bottom, 0xffffffff);
			String obfPass = "";
			if(!alt.cracked) {
				for(int i = 0; i < alt.password.length(); i++) {
					obfPass += "*";
				}
			}
			if(Jigsaw.loggedInName != null && Jigsaw.loggedInName.equals(alt.name)) {
				drawString(fontRendererObj, "§2Logged in", right - fontRendererObj.getStringWidth("§2Logged in") - 2, bottom - 18, 0xffffffff);
			}
			drawString(fontRendererObj, alts + 1 + ".", left - fontRendererObj.getStringWidth(alts + 1 + ".") - 2, top + 6, 0xffffffff);
			drawString(fontRendererObj, alt.cracked ? "§c" + alt.name : "§c" + alt.name + "§r:" + "§8" + obfPass, left + 6, top + 6, 0xffffffff);
			if (alt.cracked) {
				drawString(fontRendererObj, "§cCracked", right - fontRendererObj.getStringWidth("§cCracked") - 2, bottom - 9, 0xffffffff);
			} else {
				drawString(fontRendererObj, "§bPremium", right - fontRendererObj.getStringWidth("§bPremium") - 2, bottom - 9, 0xffffffff);
			}
			if(selected == alts) {
				drawRect(left, top, right, bottom, 0x1affffff);
			}
			alts++;
		}
		drawRect(0, height - 25, width, height, 0xffa02020);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		anim.render();
		if(failed) {
			GlStateManager.scale(4, 4, 1);
			drawCenteredString(fontRendererObj, "§cFailed to login!", this.width / 2 / 4, (60) / 4, 0xffffffff);
			GlStateManager.scale(0.25, 0.25, 1);
			drawCenteredString(fontRendererObj, "§7" + failMessage, this.width / 2, 100, 0xffffffff);
			
			((GuiButton) this.buttonList.get(8)).drawButton(this.mc, mouseX, mouseY);
		}
		if (displayingLogin) {
			GlStateManager.scale(4, 4, 1);
			drawCenteredString(fontRendererObj, "§cAdd §8alt", this.width / 2 / 4, (60) / 4, 0xffffffff);
			GlStateManager.scale(0.25, 0.25, 1);
			drawString(fontRendererObj, "Email/Username", this.width / 2 - 99 - 50, this.height / 2 - 11, 0xffffffff);
			drawString(fontRendererObj, "Password", this.width / 2 + 1, this.height / 2 - 11, 0xffffffff);

			emailField.drawTextBox();
			passField.drawTextBox();

			((GuiButton) this.buttonList.get(7)).drawButton(this.mc, mouseX, mouseY);
			((GuiButton) this.buttonList.get(3)).drawButton(this.mc, mouseX, mouseY);
		}
		
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (displayingLogin) {
			emailField.mouseClicked(mouseX, mouseY, mouseButton);
			passField.mouseClicked(mouseX, mouseY, mouseButton);
		}
		if(displayingLogin) {
			return;
		}
		searchField.mouseClicked(mouseX, mouseY, mouseButton);
		
		int offsetY = 67;

		int alts = 0;
		for (Alt alt : Jigsaw.getAltManager().getAlts()) {
			if(alt.name.toLowerCase().indexOf(searchField.getText().toLowerCase()) == -1) {
				continue;
			}
			int left = width / 2 - 150;
			int right = width / 2 + 150;
			int top = offsetY + (alts * 21) + scroll;
			int bottom = offsetY + ((alts * 21) + 20) + scroll;
			boolean hover = mouseX > left && mouseX < right && mouseY > top && mouseY < bottom;
			drawRect(left, top, right, bottom, hover ? 0xaaa04040 : 0xaa702020);
			if(hover) {
				selected = alts;
				break;
			}
			alts++;
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		searchField.textboxKeyTyped(typedChar, keyCode);
		if (displayingLogin) {
			emailField.textboxKeyTyped(typedChar, keyCode);
			passField.textboxKeyTyped(typedChar, keyCode);
		}
	}
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		if(displayingLogin) {
			return;
		}
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}
			if(isCtrlKeyDown()) {
				i *= 21 * 10;
			}
			else {
				i *= 21;
			}
			
			
			this.scroll += i;
			
			if(this.scroll > 0) {
				this.scroll = 0;
			}
			
			if(this.scroll < (-Jigsaw.getAltManager().getAlts().size() * 21) + 21 * 5) {
				this.scroll = (-Jigsaw.getAltManager().getAlts().size() * 21) + 21 * 5;
			}
		}
	}

}
