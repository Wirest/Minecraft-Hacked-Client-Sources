package nivia.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event2D;
import nivia.files.modulefiles.Colors;
import nivia.gui.tabgui.TabGui;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.modules.ModuleMode;
import nivia.modules.render.hud.NiviaHUD;
import nivia.modules.render.hud.PandoraHUD;
import nivia.utils.Logger;
import nivia.utils.utils.RenderUtils;
import nivia.utils.utils.Timer;

import java.io.IOException;

public class GUI extends Module {
	private Property<Boolean> arrayList = new Property<Boolean>(this, "ArrayList", true);
	public Property<Boolean> tabGui = new Property<Boolean>(this, "TabGui", true);
	public Property<Boolean> watermark = new Property<Boolean>(this, "Watermark", true);
	public Property<Boolean> status = new Property<Boolean>(this, "Armor Status", true);
	public Property<Boolean> coords = new Property<Boolean>(this, "Coordinates", true);
	public Property<Boolean> potStatus = new Property<Boolean>(this, "Potion Status", true);
	public Property<Boolean> rainbow = new Property<Boolean>(this, "Rainbow ArrayList", false);
	public Property<Boolean> values = new Property<Boolean>(this, "Values", true);
	public Property<Boolean> scoreboard = new Property<Boolean>(this, "Scoreboard", true);
	public Property<Boolean> font = new Property<Boolean>(this, "CustomFont", true);
	public PropertyManager.DoubleProperty tabGuiColor = new PropertyManager.DoubleProperty(this, "Tab Color", -16727809, -999999999, 999999999, false, true);
	public PropertyManager.DoubleProperty clickGuiColor = new PropertyManager.DoubleProperty(this, "Click Color", -16727809, -999999999, 999999999, false, true);
	private Timer timer = new Timer();
	public TabGui gui = new TabGui();
 
	public PandoraHUD pandorahud = new PandoraHUD(this);
	public NiviaHUD niviahud = new NiviaHUD(this);
	
	public Property<HUDMode> mode = new Property<>(this, "Mode", niviahud);
    
	public GUI() {
		super("GUI", 0, 0xd3290f, Category.RENDER, "Toggles the clients main GUI", new String[] { "HUD" }, false);
		addMode(niviahud, pandorahud);
		gui.tanimation.setModule(this);
		gui.width.setModule(this);
		gui.BIG.setModule(this);
		gui.BIG.value = true;
		gui.gui = this;
	}
	public static int getClickGuiColor(){
		GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
		return (int) gui.clickGuiColor.getValue();
	}
	public static int getTabGUIColor(){
		GUI gui = (GUI) Pandora.getModManager().getModule(GUI.class);
		return (int) gui.tabGuiColor.getValue();
	}
	public int y = 1;
	public float hue = 0;

	/**
	 * Thanks to zeb / Valkyrie for the rainbow shit for arraylist.
	 */
	@Override
	public void onEnable(){
		super.onEnable();
		gui.updateTabs(gui.BIG.value = true);
	}
	
	public void drawTabGui(ScaledResolution sr) {
		if (!tabGui.value)
			return;
		gui.drawGui(mode.value.equals(pandorahud) ? 2 : 5, y + 1);
	}
	
	@EventTarget(Priority.HIGHEST)
	public void onScreenDraw(Event2D e) {
		if (mc.gameSettings.showDebugInfo)
			return;
		drawTabGui(e.getScaledRes());
		drawArrayList(e.getScaledRes());
		drawValues(e.getScaledRes());
		drawWaterMark(e.getScaledRes());
		drawShitStatus(e.getScaledRes());
		drawCoordinates(e.getScaledRes());
		drawPotionStatus(e.getScaledRes());
		hue += 0.2;
		if (hue > 255)
			hue = 0;
	}

	private void drawArrayList(ScaledResolution sr) {
		if (!arrayList.value)
			return;
		mode.value.renderArraylist();		
	}

	private void drawWaterMark(ScaledResolution sr) {
		if (!watermark.value) {
			y = 2;
			return;
		}
		mode.value.renderWatermark();	
	}

	private void drawShitStatus(ScaledResolution sr) {
		if (!status.value || mc.playerController.isInCreativeMode())
			return;
		mode.value.renderArmorStatus();
	}

	private void drawValues(ScaledResolution sr) {
		if (!values.value)
			return;
		mode.value.renderValues();
	}

	private void drawCoordinates(ScaledResolution sr) {
		if (!coords.value)
			return;
		mode.value.renderCoordinates();
	}

	private void drawPotionStatus(ScaledResolution sr) {
		if (!potStatus.value)
			return;
		mode.value.renderPotionStatus();
	}
	
	

    public static abstract class HUDMode extends ModuleMode {
        private String name;
        protected static GUI gui = null;
        protected final Minecraft mc = Minecraft.getMinecraft();

        public HUDMode(String name, GUI hud) {
        	super(hud, name);
            this.name = name;
            gui = hud;
        }

        public String getName() {
            return name;
        }
        
        public Property<HUDMode> getMode() {        	
			return gui.mode;
        }

        public abstract void renderArraylist();
        public abstract void renderValues();
        public abstract void renderWatermark();
        public abstract void renderArmorStatus();
        public abstract void renderCoordinates();
        public abstract void renderPotionStatus();

    	
    }
	protected void addCommand() {
		Pandora.getCommandManager().cmds.add(new Command(
				"Gui", "Manages what the GUI displays", Logger.LogExecutionFail("Option, Options:", new String[] {
						"ArrayList", "TabGui", "Watermark", "Status", "Coords", "PotionStatus", "Scoreboard"}),
				"hud") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
				case "arraylist":
				case "array":
				case "modules":
				case "list":
				case "al":
					arrayList.value = !arrayList.value;
					Logger.logToggleMessage("ArrayList", arrayList.value);
					break;
				case "tabgui":
				case "tg":
				case "tab":
					tabGui.value = !tabGui.value;
					Logger.logToggleMessage("TabGui", tabGui.value);
					break;

				case "watermark":
				case "wm":
				case "name":
				case "waterm":
				case "wmark":
					watermark.value = !watermark.value;
					Logger.logToggleMessage("WaterMark", watermark.value);
					break;
				case "values":
				case "fps":
				case "pots":
				case "soups":
					values.value = !values.value;
					Logger.logToggleMessage("Values", values.value);
					break;
				case "armorstatus":
				case "itemstatus":
				case "astatus":
				case "status":
				case "stats":
				case "st":
					status.value = !status.value;
					Logger.logToggleMessage("Armor Status", status.value);
					break;
				case "coordinates":
				case "cordinates":
				case "cords":
				case "coords":
					coords.value = !coords.value;
					Logger.logToggleMessage("Coordinates", coords.value);
					break;
				case "potstatus":
				case "pstatus":
				case "potionstatus":
				case "potions":
				case "pstat":
				case "pstats":
					potStatus.value = !potStatus.value;
					Logger.logToggleMessage("Potion Status", potStatus.value);
					break;
					case "sc":
					case "scoreb":
					case "sboard":
						scoreboard.value = !scoreboard.value;
						Logger.logToggleMessage("Scoreboard", scoreboard.value);
						break;
				case "rainbows":
				case "rainbow":
				case "rbow":
					rainbow.value = !rainbow.value;
					Logger.logToggleMessage("Rainbow Arraylist", rainbow.value);
					break;
				case "reload":
				case "r":
					try {
						Pandora.getFileManager().getFile(Colors.class).loadFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				default:
					Logger.logChat(this.getError());
				}
			}
		});
		Pandora.getCommandManager().cmds.add(new Command("Tabgui", "Manages tabgui theme", Logger.LogExecutionFail("Theme, Theme:", new String[] { "Width", "Big", "Small", "Nivia", "Pandora", "Text Animation" }), "tg", "tgui", "theme") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				switch (message.toLowerCase()) {
					// int mainColor, int outlineColor, int itemcolor, int modcolor,
					// int boxColor, int rectColor, int textColor
					case "width":
					case "w":
						try {
							String message2 = arguments[2];
							Integer sD = Integer.parseInt(message2);
							gui.width.setValue(sD);
							Logger.logSetMessage("Tabgui","width", gui.width);
						} catch (Exception e) {
							Logger.LogExecutionFail("Value!");
						}
						break;

					case "big":
					case "b":
						gui.BIG.value = true;
						gui.updateTabs(true);
						Logger.logChat("Changed Tabgui's Size to: " + (gui.BIG.value ? (EnumChatFormatting.AQUA + "Full")
								: (EnumChatFormatting.AQUA + "Small")));
						break;
					case "small":
					case "s":
						gui.BIG.value = false;
						gui.updateTabs(false);
						Logger.logChat("Changed Tabgui's Size to: " + (gui.BIG.value ? (EnumChatFormatting.AQUA + "Full")
								: (EnumChatFormatting.AQUA + "Small")));
						break;		
					case "nivia":
						mode.value = niviahud;
						Logger.logSetMessage("Theme", mode.value.getName());
						break;
					case "pandora": case "main":
						mode.value = pandorahud;
						Logger.logSetMessage("Theme", mode.value.getName());
						break;
					case "ta":
					case "textanimation":
					case "texta":
					case "tanimation":
						gui.tanimation.value = !gui.tanimation.value;
						Logger.logSetMessage("Text Animation", String.valueOf(gui.tanimation.value));
						break;
					default:
						Logger.logChat(this.getError());
				}
			}
		});
		Pandora.getCommandManager().cmds.add(new Command("Font", "Toggles the client's font.", Logger.LogExecutionFail("Mode", new String[]{ "FF", "Comfortaa", "Helvetica", "Raleway", "Century", "toggle" }), "fn", "text") {
					@Override
					public void execute(String commandName, String[] arguments) {
						String message = arguments[1];
						switch (message.toLowerCase()) {
							// NORMAL, EMBOSS_TOP, EMBOSS_BOTTOM, OUTLINE_THIN,
							// SHADOW_THICK, SHADOW_THIN
							case "comfortaa":
							case "c":
								Pandora.testFont = RenderUtils.comfortaa;
								break;
							case "forgotten":
							case "ff":
							case "forgottenfuturistic":
								Pandora.testFont = RenderUtils.futuristic;
								break;
							case "helvetica":
							case "h":
								Pandora.testFont = RenderUtils.helvetica;
								break;
							case "raleway":
							case "rw":
							case "r":
								Pandora.testFont = RenderUtils.raleway;
								break;
							case "century":
							case "cg":
								Pandora.testFont = RenderUtils.cgothic;
								break;
							case "t":
							case "toggle":
								font.value = !font.value;
								break;
							default:
								Logger.logChat(this.getError());
						}
					}
				});

	}

}