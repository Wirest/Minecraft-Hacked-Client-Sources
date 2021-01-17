package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Mineman;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventSystem;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.SkyLine;
import skyline.specc.extras.chat.ChatBuilder;
import skyline.specc.extras.chat.ChatColor;
import skyline.specc.helper.Player;
import skyline.specc.utils.Wrapper;

public class Module {

	private Value<String> modeValue;

	private String name;
	private String displayName;
	private ModData data;
	private ModType type;

	private boolean madeCommand = false;

	private ModMode mode;
	private List<ModMode> modes = new ArrayList<ModMode>();

	public static Mineman mc;
	public static Player p;
	
	public Module(ModData data, ModType type) {
		this.name = data.getName();
		this.displayName = name;
		this.data = data;
		this.type = type;
	}

	public void setData(ModData data) {

		this.data.setColor(data.getColor());
		this.data.setKeybind(data.getKeyBind());
		this.data.setVisible(data.isVisible());
		this.data.setValues(data.getValues());
		setState(data.getState());

	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean getModCategory2(ModType cat) {
		return type == cat;
	}

	public ModData getData() {
		return data;
	}

	public ModType getType() {
		return type;
	}

	public ModType getCategory() {
		return this.type;
	}

	public void addValue(Value... value) {
		for (Value v : value) {
			this.getData().getValues().add(v);

			if (!madeCommand) {
				SkyLine.getManagers().getCommandManager().addContent(new ModuleValueCommand(this));
				madeCommand = true;
			}
		}
	}

	public void onEnable() {
	}

	public void onDisable() {
	}
	
	
	public void toggle() {
		this.setState(!this.getState());
		SkyLine.getManagers().getModDataManager().save();
	}

	public void setState(boolean state) {
		this.data.setState(state);

		if (state) {
			this.onEnable();
			EventSystem.register(this);

			if (this.usesModes()) {
				this.getMode().p = Wrapper.getMinecraft().thePlayer;
				this.getMode().mc = Wrapper.getMinecraft();
				this.getMode().onEnable();
				this.setDisplayName(this.getName() + " §f" + this.getMode().getName());
				EventSystem.register(this.getMode());
			}
		} else {
			EventSystem.unregister(this);
			if (this.usesModes()) {
				EventSystem.unregister(this.getMode());
				this.getMode().onDisable();
			}
			this.onDisable();
		}
	}

	public boolean getState() {
		return this.data.getState();
	}

	public boolean usesModes() {
		return !this.modes.isEmpty();
	}

	public void addMode(ModMode... mode) {
		for (ModMode m : mode) {
			if (this.getMode() == null) {
				if (!usesModes()) {
					SkyLine.getManagers().getCommandManager().addContent(new ModuleModeCommand(this));
					modeValue = new Value<String>("mode", m.getName());
					addValue(modeValue);
				}
				this.setMode(m);
			}

			this.modes.add(m);
		}
	}

	public ModMode getMode() {
		return mode;
	}

	public void setMode(ModMode mode) {

		if (getMode() != null) {
			EventSystem.unregister(getMode());
			getMode().onDisable();
		}

		mode.p = Mineman.getMinecraft().thePlayer;
		mode.mc = Mineman.getMinecraft();
		this.mode = mode;

		if (getState()) {
			mode.onEnable();
			this.setDisplayName(this.getName() + " §f" + mode.getName());
			EventSystem.register(mode);
		}

		modeValue.setValue(mode.getName());

	}

	public List<ModMode> getModes() {
		return modes;
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

	public static class ModuleModeCommand extends Command {

		public Module module;

		public ModuleModeCommand(Module module) {
			super(module.getData().getName(), new String[] {}, "NO_SHOW");
			this.module = module;
		}

		@Override
		public void onCommand(List<String> args) {
			if (args.size() > 0 && module.usesModes()) {
				if (args.get(0).equalsIgnoreCase("mode")) {
					if (args.size() == 1) {
						String s = "";

						for (ModMode mode : module.getModes())
							s += mode.getName() + ", ";

						error("Mode not found! Modes : " + Module.replaceLast(s, ", ", "") + ".");
					} else if (args.size() == 2) {
						String s = args.get(1);
						ModMode mode = null;

						for (ModMode m : module.getModes()) {
							if (m.getName().equalsIgnoreCase(s)) {
								mode = m;
								break;
							}
						}

						if (mode == null) {
							String bla = "";

							for (ModMode m : module.getModes())
								bla += m.getName() + ", ";

							error("Not found - Modes : " + Module.replaceLast(bla, ", ", "") + ".");
							return;
						}

						if (module.getMode() != mode) {
							module.setMode(mode);
							mode.p = Wrapper.getPlayer();
							mode.mc = Wrapper.getMinecraft();

							addChat("Set mode to " + mode.getName() + ".");
							SkyLine.getManagers().getModDataManager().save();
						}

					} else {
						error("Usage : '" + module.getName() + " mode <mode>'.");
					}

				}
			}
		}

	}

	public void addChat(String message) {
		new ChatBuilder().appendText(SkyLine.getVital().getClient().getName(),
				SkyLine.getVital().getClient().getData().getDisplayColor())
				.appendText(" >>", ChatColor.GOLD).appendText(" " + message, ChatColor.GRAY).send();
	}

	public void addChat(ChatBuilder chatBuilder) {
		new ChatBuilder().appendText(SkyLine.getVital().getClient().getName(),
						SkyLine.getVital().getClient().getData().getDisplayColor())
				.appendText(">>", ChatColor.GOLD).appendText(" ", ChatColor.GRAY)
				.appendMessage(chatBuilder.getMessage()).send();
	}

	public void error(String message) {
		new ChatBuilder().appendText(SkyLine.getVital().getClient().getName(),
						SkyLine.getVital().getClient().getData().getDisplayColor())
				.appendText(">>", ChatColor.GOLD).appendText(" " + message, ChatColor.RED).send();
	}

	public void error(ChatBuilder chatBuilder) {
		new ChatBuilder().appendText("", ChatColor.RED)
				.appendText(SkyLine.getVital().getClient().getName(),
						SkyLine.getVital().getClient().getData().getDisplayColor())
				.appendText(">>", ChatColor.GOLD).appendText(" ", ChatColor.RED)
				.appendMessage(chatBuilder.getMessage()).send();
	}

}
