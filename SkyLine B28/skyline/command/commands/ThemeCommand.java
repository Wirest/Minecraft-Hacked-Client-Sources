package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.commands;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import skyline.SkyLiner;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabTheme;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.TabGui.Simple.SimpleTabThemeProperties.SimpleTabAlignment;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer.FontObjectType;
import skyline.specc.SkyLine;
import skyline.specc.extras.chat.ChatBuilder;
import skyline.specc.extras.chat.ChatColor;

public class ThemeCommand extends Command {

	public static int red = 255;
	public static int green = 10;
	public static int blue = 10;
	public static int opacity = 225;
	
	public ThemeCommand() {
		super("Theme", new String[] { "th" }, "Change the tabgui theme");
	}

	public static void setTheme(String theme) {
		String t = SkyLiner.tabTheme.getValue();
		SkyLiner.tabTheme.setValue(theme);
		if (theme.equalsIgnoreCase("SkyLine")) {
			SkyLine.getVital().getTabGui().setTabTheme(new SimpleTabTheme(
					VitalFontRenderer.createFontRenderer(FontObjectType.DEFAULT, new Font("CleanStephanieee", Font.PLAIN, 20)),
					new SimpleTabThemeProperties(
							new Color(red, green, blue, opacity),
							Color.WHITE, new Color(255, 0, 0, 0), 0.8f,
							SimpleTabAlignment.CENTER)));
		}  else {
			SkyLiner.tabTheme.setValue(t);
		}
	}

	@Override
	public void onCommand(List<String> args) {
		if (args.size() == 1) {
			String t = SkyLiner.tabTheme.getValue();
			SkyLiner.tabTheme.setValue(args.get(0));
			if (args.get(0).equalsIgnoreCase("SkyLine")) {
				SkyLine.getVital().getTabGui().setTabTheme(new SimpleTabTheme(
						VitalFontRenderer.createFontRenderer(FontObjectType.DEFAULT, new Font("CleanStephanieee", Font.PLAIN, 20)),
						new SimpleTabThemeProperties(
								new Color(red, green, blue, opacity),
								Color.WHITE, new Color(200, 0, 0, 180), 0.8f,
								SimpleTabAlignment.CENTER)));

			}  else {
				SkyLiner.tabTheme.setValue(t);
				error("Theme not found. Themes §8: ");

				String[] themes = new String[] { "SkyLine" };

				ChatBuilder chat = new ChatBuilder().appendText("   ");

				for (String string : themes) {
					chat.appendText(string + " ", ChatColor.GRAY);
				}

				chat.send();
			}
		} else {
			error("Invalid args! Usage : 'Theme [theme]'");
			String[] themes = new String[] { "SkyLine" };

			ChatBuilder chat = new ChatBuilder().appendText("   ");

			for (String string : themes) {
				chat.appendText(string + " ", ChatColor.GRAY);
			}

			chat.send();
		}
	}
}
