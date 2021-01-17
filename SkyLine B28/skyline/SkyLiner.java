package skyline;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import org.lwjgl.opengl.Display;

import skyline.altman.AltManager;
import skyline.altman.GuiAddAlt;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabTheme;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties.SimpleTabAlignment;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands.ThemeCommand;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventSystem;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer.FontObjectType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.Clarinet;
import skyline.specc.SkyLine;
import skyline.specc.extras.chat.ChatColor;
import skyline.specc.render.modules.tabgui.TabGuiOverlay;
import skyline.specc.render.modules.tabgui.main.TabGui;

public class SkyLiner extends Clarinet {

	public static AltManager altManager;
	private TabGui tabGui;
	public static boolean hide = false;
	private boolean hud = true;
	public static boolean useFont = true;
	public static boolean red = true;
	public static Value<String> tabTheme = new Value<String>("theme", "SkyLine");

	public SkyLiner() {
		super("SkyLine", 20, new String[] { "SkyLine" }, new ClientData(ChatColor.RED));
		addValue(tabTheme);
		Display.setTitle("SkyLine B28 | Public Release");
	}

	@Override
	public void start() {
		EventSystem.register(this);
		tabGui = new TabGui(new SimpleTabTheme(
				VitalFontRenderer.createFontRenderer(FontObjectType.CFONT, new Font("Arial", Font.PLAIN, 18)),
				new SimpleTabThemeProperties(new Color(135, 13, 55), Color.WHITE, new Color(35, 35, 35, 200), 0.6f,
						SimpleTabAlignment.CENTER)));
		SkyLine.getVital().setTabGui(tabGui);

		ThemeCommand.setTheme(tabTheme.getValue());


		SkyLine.getManagers().getOverlayManager().addContent(new TabGuiOverlay());
		SkyLine.getManagers().getModDataManager().load();
        GuiAddAlt.loadAlts();
	
	}

	public static File getDirectory() {
		return SkyLiner.getDirectory();
	}

}
