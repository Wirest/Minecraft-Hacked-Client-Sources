package skyline.specc;

import java.io.File;

import net.minecraft.client.Mineman;
import skyline.SkyLiner;
import skyline.altman.AltManager;
import skyline.altman.GuiAddAlt;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventKeyPress;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventSystem;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Listener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.Value;
import skyline.specc.managers.CommandManager;
import skyline.specc.managers.FriendManager;
import skyline.specc.managers.ModuleManager;
import skyline.specc.managers.File.Manager;
import skyline.specc.managers.File.Managers;
import skyline.specc.render.main.Renderer;
import skyline.specc.render.modules.tabgui.main.TabGui;
import skyline.specc.render.renderevnts.EventRenderChatMessage;
import skyline.specc.render.renderevnts.EventRenderScreen;
import skyline.specc.utils.Wrapper;

public class SkyLine {
	private static SkyLine skyLine;
	private static Managers managers;
	private Clarinet clarinet;
	private TabGui tabGui;
	public static File clientDir;
	private static AltManager altManager;
	
	static {
		SkyLine.skyLine = new SkyLine();
	}

	public void onLaunch() {
		this.setClient(new SkyLiner());
		System.out.println("Launching SkyLine b28");
		(SkyLine.managers = new Managers()).setCommandManager(new CommandManager());
		SkyLine.managers.setModuleManager(new ModuleManager());
		for (final Module module : getManagers().getModuleManager().getContents()) {
			module.mc = Mineman.getMinecraft();
			module.p = Mineman.getMinecraft().thePlayer;
		}
		EventSystem.register(new Listener<EventKeyPress>() {
			@Override
            public void onEvent(EventKeyPress event) {
                for(Module module : SkyLine.getManagers().getModuleManager().getContents()){

                    if(event.getKey() == module.getData().getKeyBind().getKey()){
                        module.toggle();
                    }

                }
            }

        });
		for (Value value : clarinet.getVals()) {
			getManagers().getOptionManager().addContent(value);
		}
		EventSystem.register(new Listener<EventRenderScreen>() {
			@Override
			public void onEvent(final EventRenderScreen event) {
				if (Wrapper.getMinecraft().gameSettings.showDebugInfo) {
					return;
				}
				for (final Renderer overlay : SkyLine.getManagers().getOverlayManager().getContents()) {
					overlay.render();
				}
			}
		});
		final Manager[] managersToLoad = { getManagers().getFriendManager(),
				getManagers().getOptionManager() };
		Manager[] array;
		for (int length = (array = managersToLoad).length, i = 0; i < length; ++i) {
			final Manager manager = array[i];
			manager.load();
		}
		this.clarinet.start();
		EventSystem.register(new Listener<EventRenderChatMessage>() {
			@Override
			public void onEvent(final EventRenderChatMessage event) {
				for (final FriendManager.Friend friend : SkyLine.managers.getFriendManager().getContents()) {
					event.setMessage(event.getMessage().replaceAll("(?i)" + friend.getName(),
							"§r§a" + friend.getNickname() + "§r"));
				}
			}
		});
	}

	public static SkyLine getVital() {
		return SkyLine.skyLine;
	}

	public static void start() {
		SkyLine.skyLine.onLaunch();
	}

	public void setClient(final Clarinet clarinet) {
		this.clarinet = clarinet;
	}

	public Clarinet getClient() {
		return this.clarinet;
	}

	public static Managers getManagers() {
		return SkyLine.managers;

	}

	public void setTabGui(final TabGui tabGui) {
		this.tabGui = tabGui;
	}

	public TabGui getTabGui() {
		return this.tabGui;
	}

	public static AltManager getAltManager() {
		return skyLine.altManager;
	}

}
