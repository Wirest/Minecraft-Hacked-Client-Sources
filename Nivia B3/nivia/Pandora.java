package nivia;

import com.stringer.annotations.HideAccess;
import com.stringer.annotations.StringEncryption;
import org.lwjgl.opengl.Display;
import nivia.gui.aclickgui.GuiAPX;
import nivia.gui.altmanager.AltManager;
import nivia.gui.chod.ChodsGui;
import nivia.gui.mainmenu.PandoraMainMenu;
import nivia.managers.*;
import nivia.security.ConnectionUtils;
import nivia.security.HWIDTools;
import nivia.utils.MinecraftFontRenderer;
import nivia.utils.Wrapper;
import nivia.utils.font.TTFFontRenderer;
import nivia.utils.utils.RenderUtils;


public class Pandora {
	
	@StringEncryption
	public static String getClientName(){
		return "Nivia";
	}
	public static int getClientVersion(){
		return 3;
	}

	private static ModuleManager modManager;
	private static FriendManager friendManager;
	private static EventManager eventManager;
	private static CommandManager cmdManager;
	private static PropertyManager propManager;
	private static AltManager altManager;
	private static FileManager fileManager;
	private static StaffManager staffManager;
	private static GuiAPX clickGui;
	private static ChodsGui gui;

	public static ModuleManager getModManager(){
		return modManager;
	}
	public static CommandManager getCommandManager(){
		return cmdManager;
	}
	public static PropertyManager getPropertyManager(){
		return propManager;
	}
	public static FriendManager getFriendManager(){
		return friendManager;
	}
	public static EventManager getEventManager(){
		return eventManager;
	}
	public static AltManager getAltManager(){
		return altManager;
	}
	public static FileManager getFileManager(){
		return fileManager;
	}
	public static StaffManager getStaffManager(){
		return staffManager;
	}
	public static GuiAPX getAPXGui(){
		return clickGui;
	}
	

	public static MinecraftFontRenderer testFont;
	public static TTFFontRenderer tf;
	//TODO: false
	public static boolean isAuthedReal = true;
	


	@HideAccess
	public static void start() {
		/*try {
			ConnectionUtils.authorize(HWIDTools.a());
		} catch (Exception e) {
			System.out.println("hi");			
		}*/

		modManager = new ModuleManager();
		friendManager = new FriendManager();
		cmdManager = new CommandManager();
		propManager = new PropertyManager();
		fileManager = new FileManager();
		clickGui = new GuiAPX();
		gui = new ChodsGui();
		clickGui.getTheme().insert();
		fileManager.loadFiles();

		testFont = RenderUtils.helvetica;				

		Wrapper.getMinecraft().displayGuiScreen(new PandoraMainMenu());
		//new FirstTimeScreen();		
		
		Display.setTitle("Nivia Premium b" + Pandora.getClientVersion() + " - Minecraft 1.8");
		try {
			ConnectionUtils.authorize(HWIDTools.a());
		} catch (Exception eg) {}
	}
	
	public static String prefix = "-";
}