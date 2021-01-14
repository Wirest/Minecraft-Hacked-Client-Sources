package de.iotacb.client.gui.alt;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.base.CaseFormat;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.AltManagerFile;
import de.iotacb.client.file.files.PrixGenFile;
import de.iotacb.client.file.files.VultureGenFile;
import de.iotacb.client.gui.alt.sub.GuiAddAlt;
import de.iotacb.client.gui.alt.sub.GuiDirectLogin;
import de.iotacb.client.gui.alt.util.AltLogin;
import de.iotacb.client.gui.elements.textfields.GuiTextBox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import wriva.core.Reader;
import wriva.core.Writer;

public class GuiAltManager extends GuiScreen {

	public final List<AltSlot> altSlots = new ArrayList<AltSlot>();

	private final GuiScreen prevScreen;
	
	public int selectedSlot = -1;
	
	private boolean slotsHovered;

	public GuiAltManager(GuiScreen prev) {
		this.prevScreen = prev;
	}
	
	private GuiTextBox prixGenUsername, prixGenPassword;
	private GuiTextBox vultureGenUsername, vultureGenPassword;

	@Override
	public void initGui() {
		final double heightOffset = 30;

		buttonList.add(new GuiButton(0, width / 2 - (((width / 8) / 2) * 3) - 5, height - heightOffset - 25, width / 8, 20, "Add"));
		buttonList.add(new GuiButton(1, width / 2 - (width / 8) / 2, height - heightOffset - 25, width / 8, 20, "Import"));
		buttonList.add(new GuiButton(2, width / 2 + ((width / 8) / 2) + 5, height - heightOffset - 25, width / 8, 20, "Remove"));

		buttonList.add(new GuiButton(3, width / 2 - (((width / 8) / 2) * 3) - 5, height - heightOffset, width / 8, 20, "Login"));
		buttonList.add(new GuiButton(4, width / 2 - (width / 8) / 2, height - heightOffset, width / 8, 20, "Direct login"));
		buttonList.add(new GuiButton(5, width / 2 + ((width / 8) / 2) + 5, height - heightOffset, width / 8, 20, "Cancel"));
		
		int y = 50;
		
		// Prix
		{
			prixGenUsername = new GuiTextBox(0, fontRendererObj, "PrixGen username", 0, height / 2 - 17 + y, width / 8, 20);
			prixGenPassword = new GuiTextBox(1, fontRendererObj, "PrixGen password", 0, height / 2 + 7 + y, width / 8, 20, true);
			
			final String[] prixGenDetails = ((PrixGenFile) Client.INSTANCE.getFileManager().getFileByClass(PrixGenFile.class)).readPrix();
			
			if (prixGenDetails != null) {
				prixGenUsername.setText(prixGenDetails[0]);
				prixGenPassword.setText(prixGenDetails[1]);
				Client.INSTANCE.setPrixGenLoggedIn(true);
			}
			
			buttonList.add(new GuiButton(6, 0, height / 2 + 30 + y, width / 8, 20, "Connect"));
			buttonList.add(new GuiButton(7, 0, height / 2 + 53 + y, width / 8, 20, "Buy"));
		}
		
		// Vulture
		{
			vultureGenUsername = new GuiTextBox(2, fontRendererObj, "Discord Id", 0, height / 2 - 17 - y, width / 8, 20);
			vultureGenPassword = new GuiTextBox(3, fontRendererObj, "VultureGen password", 0, height / 2 + 7 - y, width / 8, 20, true);
			
			final String[] vultureGenDetails = ((VultureGenFile) Client.INSTANCE.getFileManager().getFileByClass(VultureGenFile.class)).readVulture();
			
			if (vultureGenDetails != null) {
				vultureGenUsername.setText(vultureGenDetails[0]);
				vultureGenPassword.setText(vultureGenDetails[1]);
				Client.INSTANCE.setVultureGenLoggedIn(true);
			}
			
			buttonList.add(new GuiButton(8, 0, height / 2 + 30 - y, width / 8, 20, "Connect"));
		}
		
		init();
		
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			mc.displayGuiScreen(new GuiAddAlt(this));
			break;
		case 1:
			if (mc.isFullScreen()) mc.toggleFullscreen();
			
			final JFileChooser fileChooser = new JFileChooser();
			final JFrame frame = new JFrame();
			
			frame.setVisible(true);
			frame.toFront();
			frame.setVisible(false);
			
			fileChooser.setDialogTitle("Select alt list");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			String path = "";
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				path = fileChooser.getSelectedFile().getAbsolutePath();
			}
			frame.dispose();
			
			if (path.isEmpty()) return;
			
			final List<String> alts = Reader.INSTANCE.readAsStringList(path);
			Writer.INSTANCE.appendStringList(new File("./vulture/files/alts.txt"), alts, -1);
			init();
			
			break;
		case 2:
			if (selectedSlot != -1) {
				Client.INSTANCE.getAltManager().removeAlt(altSlots.get(selectedSlot).getEmail());
				altSlots.remove(selectedSlot);
				updateIds();
				selectedSlot = -1;
				((AltManagerFile) Client.INSTANCE.getFileManager().getFileByClass(AltManagerFile.class)).saveAlts();
				Client.INSTANCE.getNotificationManager().addNotification("Alt Manager", "The alt has been removed");
			}
			break;
		case 3:
			if (selectedSlot != -1) {
				AltLogin.login(getSlotById(selectedSlot), this);
			}
			break;
		case 4:
			mc.displayGuiScreen(new GuiDirectLogin(this));
			break;
		case 5:
			mc.displayGuiScreen(prevScreen);
			break;
		case 6:
			if (prixGenUsername.getText().isEmpty() || prixGenPassword.getText().isEmpty()) {
				Client.INSTANCE.getNotificationManager().addNotification("PrixGen", "Please fill all fields!");
				return;
			}
			((PrixGenFile) Client.INSTANCE.getFileManager().getFileByClass(PrixGenFile.class)).savePrix(prixGenUsername.getText(), prixGenPassword.getText());
			Client.INSTANCE.getNotificationManager().addNotification("PrixGen", "Connected to PrixGen");
			
			final String[] prixGenDetails = ((PrixGenFile) Client.INSTANCE.getFileManager().getFileByClass(PrixGenFile.class)).readPrix();
			
			if (prixGenDetails != null) {
				System.out.println("Logged in PrixGen");
				Client.INSTANCE.setPrixGenLoggedIn(true);
			}
			break;
		case 7:
			try {
				Desktop.getDesktop().browse(new URL("https://clientshop.eu/shop-de.html").toURI());
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
			break;
		case 8:
			if (vultureGenUsername.getText().isEmpty() || vultureGenPassword.getText().isEmpty()) {
				Client.INSTANCE.getNotificationManager().addNotification("VultureGen", "Please fill all fields!");
				return;
			}
			if (vultureGenUsername.getText().length() != 18 || vultureGenPassword.getText().length() != 40) {
				Client.INSTANCE.getNotificationManager().addNotification("VultureGen", "Please check your input!");
			}
			((VultureGenFile) Client.INSTANCE.getFileManager().getFileByClass(VultureGenFile.class)).saveVulture(vultureGenUsername.getText(), vultureGenPassword.getText());
			Client.INSTANCE.getNotificationManager().addNotification("VultureGen", "Connected to VultureGen");
			
			final String[] vultureGenDetails = ((VultureGenFile) Client.INSTANCE.getFileManager().getFileByClass(VultureGenFile.class)).readVulture();
			if (vultureGenDetails != null) {
				System.out.println("Logged in VultureGen");
				Client.INSTANCE.setVultureGenLoggedIn(true);
			}
			break;
		}
		super.actionPerformed(button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prevScreen);
		} else if (keyCode == Keyboard.KEY_V) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
				final String[] vultureGenDetails = ((VultureGenFile) Client.INSTANCE.getFileManager().getFileByClass(VultureGenFile.class)).readVulture();
				if (vultureGenDetails != null) {
					final String alt = AltLogin.gerateTokenVultureGen(vultureGenDetails[0], vultureGenDetails[1]);
					if (Client.LOGIN_UTIL.loginVultureAlts(alt)) {
						addAlt(alt, "");
						mc.displayGuiScreen(prevScreen);
					}
				}
			}
		} else if (keyCode == Keyboard.KEY_DELETE) {
			if (selectedSlot != -1) {
				Client.INSTANCE.getAltManager().removeAlt(altSlots.get(selectedSlot).getEmail());
				altSlots.remove(selectedSlot);
				updateIds();
				selectedSlot = -1;
				((AltManagerFile) Client.INSTANCE.getFileManager().getFileByClass(AltManagerFile.class)).saveAlts();
				Client.INSTANCE.getNotificationManager().addNotification("Alt Manager", "The alt has been removed");
			}
		}
		
		prixGenUsername.textboxKeyTyped(typedChar, keyCode);
		prixGenPassword.textboxKeyTyped(typedChar, keyCode);
		vultureGenUsername.textboxKeyTyped(typedChar, keyCode);
		vultureGenPassword.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		slotsHovered = (mouseX > width / 2 - (((width / 8) / 2) * 3) - 10 && mouseX < (width / 2 - (((width / 8) / 2) * 3) - 10) + (((width / 8) + 5) * 3) + 5) && (mouseY > 0 && mouseY < height - 60);
		
		if (slotsHovered && (40 * altSlots.size()) > height - 60) {
			updateScrolling(new ArrayList<AltSlot>(altSlots));
		}
		
		drawBackground(mouseX, mouseY);
		Client.BLUR_UTIL.blur(width / 2 - (((width / 8) / 2) * 3) - 10, height - 60, (((width / 8) + 5) * 3) + 5, 60, 5);
		Client.RENDER2D.rect(width / 2 - (((width / 8) / 2) * 3) - 10, height - 60, (((width / 8) + 5) * 3) + 5, 60, new Color(20, 20, 20));
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		Client.RENDER2D.scissor(0, 0, width, height - 60);
		
		drawSlots(mouseX, mouseY, new ArrayList<AltSlot>(altSlots));
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		drawGens(mouseX, mouseY);
		
		Client.INSTANCE.getNotificationManager().draw(0);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		double y = scroll;
		for (AltSlot slot : new ArrayList<AltSlot>(altSlots)) {
			slot.setY(y);
			final boolean hovered = (mouseX > slot.getX() && mouseX < slot.getX() + slot.getWidth()) && (mouseY > slot.getY() && mouseY < slot.getY() + slot.getHeight());
			if (mouseButton == 0 && hovered) {
				selectedSlot = slot.getSlotId();
			}
			y += slot.getHeight();
		}
		
		prixGenUsername.mouseClicked(mouseX, mouseY, mouseButton);
		prixGenPassword.mouseClicked(mouseX, mouseY, mouseButton);
		vultureGenUsername.mouseClicked(mouseX, mouseY, mouseButton);
		vultureGenPassword.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		prixGenUsername.updateCursorCounter();
		prixGenPassword.updateCursorCounter();
		vultureGenUsername.updateCursorCounter();
		vultureGenPassword.updateCursorCounter();
		super.updateScreen();
	}
	
	private void drawSlots(int mouseX, int mouseY, List<AltSlot> slots) {
		double y = scroll;
		for (AltSlot slot : slots) {
			slot.setY(y);
			slot.draw(mouseX, mouseY);
			y += slot.getHeight();
		}
	}
	
	private double scroll;
	
	private void updateScrolling(List<AltSlot> slots) {
		scroll += Mouse.getDWheel() * .1;
		for (AltSlot slot : slots) {
			scroll = MathHelper.clamp_double(scroll, -(slot.getHeight() * altSlots.size()) + height - 60, 0);
			slot.setY(slot.getY() + scroll);
		}
	}
	
	private void updateIds() {
		int id = 0;
		for (AltSlot slot : altSlots) {
			slot.setSlotId(id);
			id++;
		}
	}
	
	private AltSlot getSlotById(int id) {
		for (AltSlot slot : altSlots) {
			if (slot.getSlotId() == id) {
				return slot;
			}
		}
		return null;
	}
	
	private void drawPrixGen(int mouseX, int mouseY) {
		prixGenUsername.drawTextBox(mouseX, mouseY);
		prixGenPassword.drawTextBox(mouseX, mouseY);
	}
	
	private void drawVultureGen(int mouseX, int mouseY) {
		vultureGenUsername.drawTextBox(mouseX, mouseY);
		vultureGenPassword.drawTextBox(mouseX, mouseY);
	}
	
	private void drawGens(int mouseX, int mouseY) {
		final double width = this.width / 8;
		Client.RENDER2D.rect(0, 0, width, height, new Color(20, 20, 20));
		drawPrixGen(mouseX, mouseY);
		drawVultureGen(mouseX, mouseY);
	}
	
	private void init() {
		altSlots.clear();
		
		((AltManagerFile) Client.INSTANCE.getFileManager().getFileByClass(AltManagerFile.class)).readAlts();
		
		for (int i = 0; i < Client.INSTANCE.getAltManager().getAlts().size(); i++) {
			final Alt alt = Client.INSTANCE.getAltManager().getAlts().get(i);
			altSlots.add(new AltSlot(width / 2 - (((width / 8) / 2) * 3) - 10, 0, (((width / 8) + 5) * 3) + 5, 40, i, alt.getEmail(), alt.getPassword(), this));
		}
		updateIds();
	}
	
	private void addAlt(String email, String password) {
		String type = AltLogin.getType(email, password);
		if (type == "M") {
			type = "MOJANG";
		} else if (type == "TA") {
			type = "THEALTENING";
		} else if (type == "V") {
			type = "VULTURE";
		} else {
			type = "CRACKED";
		}
		Writer.INSTANCE.appendString(Client.INSTANCE.getFileManager().getFileByClass(AltManagerFile.class).getPath(), email.concat(":").concat(password), -1);
		Client.INSTANCE.getNotificationManager().addNotification("Alt Manager", "The alt has been added");
	}

}
