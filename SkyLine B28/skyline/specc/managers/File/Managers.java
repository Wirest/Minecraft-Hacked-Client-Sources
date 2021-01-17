package skyline.specc.managers.File;

import skyline.specc.managers.CommandManager;
import skyline.specc.managers.FriendManager;
import skyline.specc.managers.ModDataManager;
import skyline.specc.managers.ModuleManager;
import skyline.specc.managers.OptionManager;
import skyline.specc.managers.OverlayManager;

public class Managers {

	private ModuleManager moduleManager;
	private ModDataManager modDataManager;
	private OverlayManager overlayManager;
	private CommandManager commandManager;
	private FriendManager friendManager;
	private OptionManager optionManager;

	public Managers() {
		friendManager = new FriendManager();
		overlayManager = new OverlayManager();
		optionManager = new OptionManager();
		modDataManager = new ModDataManager();
	}

	public ModDataManager getModDataManager() {
		return modDataManager;
	}

	public void setModDataManager(ModDataManager modDataManager) {
		this.modDataManager = modDataManager;
	}
	public ModuleManager mods() {
		return moduleManager;
	}

	public void setModuleManager(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	public OverlayManager getOverlayManager() {
		return overlayManager;
	}

	public void setOverlayManager(OverlayManager overlayManager) {
		this.overlayManager = overlayManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public FriendManager getFriendManager() {
		return friendManager;
	}

	public void setFriendManager(FriendManager friendManager) {
		this.friendManager = friendManager;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}
	

	public OptionManager getOptionManager() {
		return optionManager;
	}

	public void setOptionManager(OptionManager optionManager) {
		this.optionManager = optionManager;
	}

}
