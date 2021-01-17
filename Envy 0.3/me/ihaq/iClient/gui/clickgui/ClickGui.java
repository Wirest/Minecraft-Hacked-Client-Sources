package me.ihaq.iClient.gui.clickgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.Module.Category;
import me.ihaq.iClient.utils.values.BooleanValue;
import me.ihaq.iClient.utils.values.Value;
import me.ihaq.iClient.utils.values.ValueManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGui extends GuiScreen {

	public static ClickGui INSTANCE = new ClickGui();
	public ArrayList<Frame> Frames = new ArrayList<Frame>();

	public ClickGui() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		for (Category cat : Category.values()) {
			Frame modFrame = new Frame(cat.name());
			ArrayList<Module> mods = getModsInCategory(cat);
			for (final Module mod : mods) {
				Button button = new Button(mod.getName(), null, modFrame) {
					@Override
					public void onPressed() {
						mod.toggle();
						this.enabled = !enabled;
					}

					@Override
					public void onUpdate() {
						this.enabled = mod.isToggled();
					}
				};
				for (Value val : ValueManager.INSTANCE.getValues(mod)) {
					/*
					 * Create Button Of Boolean Value
					 */
					if (val instanceof BooleanValue) {
						final BooleanValue bV;
						bV = (BooleanValue) val;
						SpecialButton valButton = new SpecialButton(bV.getName(), null, button) {

							@Override
							public void onPressed() {
								/*
								 * Toggle value of true or false
								 */
								bV.setValue(!bV.getValue());
							}

							@Override
							public void onUpdate() {
								/*
								 * Toggle value of off and on/true & false
								 */
								this.enabled = bV.getValue();
							}
						};
						button.elements.add(valButton);
					}
				}
				button.elements.add(new KeyBinder(mod, button));
				modFrame.addItem(button);
			}
			Frames.add(modFrame);
		}
		layoutFrames(sr);
		File f = new File(Minecraft.getMinecraft().mcDataDir + "\\" + Envy.Client_Name + "\\" + "Gui" + ".txt");
		if (f.exists() && !f.isDirectory()) {
			loadSettings();
		}
	}

	public void loadSettings() {
		Scanner inFile;
		try {
			inFile = new Scanner(new BufferedReader(new FileReader(
					Minecraft.getMinecraft().mcDataDir + "\\" + Envy.Client_Name + "\\" + "Gui" + ".txt")));
			while (inFile.hasNextLine()) {
				String f = inFile.nextLine();
				String[] parts = f.split(":");
				String title = parts[0];
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				boolean open = Boolean.valueOf(parts[3]);
				for (Frame frame : getFrames()) {
					if (frame.getTitle().equals(title)) {
						frame.setX(x);
						frame.setY(y);
						frame.setExpanded(open);
					}
				}
			}
			inFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(
					new File(Minecraft.getMinecraft().mcDataDir + "\\" + Envy.Client_Name + "\\" + "gui" + ".txt")));
			for (Frame panel : getFrames()) {
				printWriter.println(String.valueOf(panel.getTitle()) + ":" + panel.getX() + ":" + panel.getY() + ":"
						+ panel.isExpanded());
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		for (Frame rFrame : Frames) {
			rFrame.draw(mouseX, mouseY, partialTicks, 0, 0, this);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Frame rFrame : Frames) {
			rFrame.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame rFrame : Frames) {
			rFrame.mouseReleased(mouseX, mouseY, state);
		}

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for (Frame rFrame : Frames) {
			rFrame.keyTyped(typedChar, keyCode);
		}

	};

	public ArrayList<Module> getModsInCategory(Category cat) {
		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module mod : Envy.MODULE_MANAGER.getModules()) {
			if (mod.getModCategory(cat)) {
				mods.add(mod);
			}
		}
		return mods;
	}

	private void layoutFrames(ScaledResolution sr) {
		int x = 10;
		int y = 10;
		int h = 15;
		int w = 130;
		for (Frame frame : Frames) {
			if (frame.getX() == 0 || frame.getY() == 0) {
				if (x + w > sr.getScaledWidth()) {
					x = 10;
					y += h + 20;
				}
				frame.setX(x);
				frame.setY(y);
				x += w + 10;
			}
		}
	}

	public ArrayList<Frame> getFrames() {
		return Frames;
	}

	@Override
	public void onGuiClosed() {
		saveSettings();
	}

}
