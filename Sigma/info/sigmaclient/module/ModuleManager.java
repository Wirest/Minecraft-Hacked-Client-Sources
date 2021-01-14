package info.sigmaclient.module;

import java.util.ArrayList;

import info.sigmaclient.Client;
import info.sigmaclient.management.AbstractManager;
import info.sigmaclient.management.agora.AgoraModule;
import info.sigmaclient.management.agora.AgoraSwitchModule;
import info.sigmaclient.management.keybinding.KeyMask;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.impl.combat.*;
import info.sigmaclient.module.impl.movement.*;
import info.sigmaclient.module.impl.player.*;
import info.sigmaclient.module.impl.other.*;
import info.sigmaclient.module.impl.minigames.*;
import info.sigmaclient.module.impl.render.*;
import info.sigmaclient.module.impl.hud.*;

import org.lwjgl.input.Keyboard;

public class ModuleManager<E extends Module> extends AbstractManager<Module> {
    private boolean setup;

    public ModuleManager(Class<Module> clazz) {
        super(clazz, 0);
    }

    /**
     * Sets up the ModuleManager.
     * <hr>
     * Two ways to initiate modules internally:<br>
     * Modify constructor: so that it looks like: <i>super(clazz, numOfMods)</i>
     * ; Initiate modules by array index like so:
     *
     * <pre>
     * array[0] = new ModuleExample(...);
     * array[1] = new ModuleExample(...);
     * array[2] = new ModuleExample(...);
     * </pre>
     * <p>
     * or use the add method (Slight addition to startup time)
     *
     * <pre>
     *
     * add(new ModuleExample(...);
     * add(new ModuleExample(...);
     * add(new ModuleExample(...);
     * </pre>
     */
    @Override
    public void setup() {
        // Load modules from jars
        // loadLocalPlugins();
        //add(new PacketSniff(new ModuleData(ModuleData.Type.MiniGames, "Sniff", "Siff.")));
    	//add(new Test(new ModuleData(ModuleData.Type.Other, "Test", "Dev.")));
    	add(new AgoraSwitchModule(new ModuleData(ModuleData.Type.Other, "Agora", "AgoraSwitch.")));
    	add(new AgoraModule(new ModuleData(ModuleData.Type.Other, "AgoraR", "Rendering Agora.")));
        add(new Disabler(new ModuleData(ModuleData.Type.Other, "Disabler", "Disable an anticheat.")));
        add(new AutoArmor(new ModuleData(ModuleData.Type.Player, "AutoArmor", "Automatically equips best armor.")));
    	add(new Spider(new ModuleData(ModuleData.Type.Movement, "Spider", "Allows you to climb walls.")));
    	add(new Catch(new ModuleData(ModuleData.Type.Movement, "AntiFall", "Prevents you from getting crippled.")));
    	add(new FastLadder(new ModuleData(ModuleData.Type.Movement, "FastLadder", "Go up faster.")));
        add(new Timer(new ModuleData(ModuleData.Type.Other, "Gamespeed", "Modifies game speed. Can be used to \'bow fly\'.")));
        add(new Step(new ModuleData(ModuleData.Type.Movement, "Step", "Moves you up one block.")));
        add(new InventoryManager(new ModuleData(ModuleData.Type.Player, "InvManager", "Manages your inventory.")));
        add(new Teleport(new ModuleData(ModuleData.Type.Player, "Teleport", "Teleports you to the selected block.")));
        add(new Animations(new ModuleData(ModuleData.Type.Visuals, "Animations", "Block & Hit animations.")));
        add(new ESP(new ModuleData(ModuleData.Type.Visuals, "ESP", "Draws stuff arround players.")));
        add(new Highjump(new ModuleData(ModuleData.Type.Movement, "Highjump", "Jump higher")));
        add(new PingSpoof(new ModuleData(ModuleData.Type.Other, "PingSpoof", "Chokes KeepAlive packets to fake high ping.")));
        add(new Bhop(new ModuleData(ModuleData.Type.Movement, "Speed", "Zoom zoom!")));
        add(new Criticals(new ModuleData(ModuleData.Type.Combat, "Criticals", "Forces critical attack each hit.")));
        add(new SpeedMine(new ModuleData(ModuleData.Type.Player, "SpeedMine", "Mine blocks faster")));
        add(new Brightness(new ModuleData(ModuleData.Type.Visuals, "Brightness", "Applies night vision")));
        add(new BowAimbot(new ModuleData(ModuleData.Type.Combat, "BowAimbot", "Aims at players & predicts movement.")));
        add(new FriendAlert(new ModuleData(ModuleData.Type.Other, "FriendAlert", "Event specific alerts for friends. (Joining/Dying)")));
        add(new Lines(new ModuleData(ModuleData.Type.Visuals, "Tracers", "Draws lines at entities.")));
        add(new FastBow(new ModuleData(ModuleData.Type.Combat, "FastBow", "Allows you to shot arrows faster.")));
        add(new DeathClip(new ModuleData(ModuleData.Type.Other, "DeathClip", "Teleports you on death.")));
        add(new AutoClicker(new ModuleData(ModuleData.Type.Combat, "AutoClicker", "Automatically clicks for you.")));
        add(new AimAssist(new ModuleData(ModuleData.Type.Combat, "AimAssist", "Aims for you.")));
        add(new DONOTFUCKINGDIEYOURETARD(new ModuleData(ModuleData.Type.Visuals, "Health", "Shows your health in the middle of the screen.")));
        add(new AutoTool(new ModuleData(ModuleData.Type.Player, "AutoTool", "Switches to best tool.")));
        add(new Scaffold(new ModuleData(ModuleData.Type.Movement, "Scaffold", "Silently places blocks.")));
        add(new AntiVanish(new ModuleData(ModuleData.Type.Other, "AntiVanish", "Alerts you of vanished staff members.")));
        add(new NoFall(new ModuleData(ModuleData.Type.Player, "NoFall", "Take no fall damage.")));
        add(new Freecam(new ModuleData(ModuleData.Type.Visuals, "FreeCam", "Allows you to view around in noclip.")));
        add(new AutoGG(new ModuleData(ModuleData.Type.Other, "AutoGG", "GoodGame.")));
        add(new LongJump(new ModuleData(ModuleData.Type.Movement, "LongJump", "Jump, but longly.")));
        add(new AntiBot(new ModuleData(ModuleData.Type.Combat, "AntiBot", "Ignores/Removes bots.")));
        add(new DepthStrider(new ModuleData(ModuleData.Type.Movement, "DepthStrider", "Swim faster in water.")));
        add(new Jesus(new ModuleData(ModuleData.Type.Movement, "Jesus", "Walk on water.")));
        add(new FastUse(new ModuleData(ModuleData.Type.Player, "FastUse", "Consume items faster.")));
        add(new ChestStealer(new ModuleData(ModuleData.Type.Player, "ChestStealer", "Steal items from chests.")));
        add(new Fly(new ModuleData(ModuleData.Type.Movement, "Fly", "Become a bird.")));
        add(new AutoSoup(new ModuleData(ModuleData.Type.Combat, "AutoSoup", "Consumes soups to heal for you.")));
        add(new ChestESP(new ModuleData(ModuleData.Type.Visuals, "ChestESP", "Draws a box around chests.")));
        add(new Sprint(new ModuleData(ModuleData.Type.Movement, "Sprint", "Automatically sprints for you.")));
        add(new Xray(new ModuleData(ModuleData.Type.Visuals, "Xray", "Sends brain waves to blocks.", Keyboard.KEY_X, KeyMask.None)));
        add(new AntiVelocity(new ModuleData(ModuleData.Type.Combat, "AntiVelocity", "Reduce/Remove velocity.")));
        add(new FastPlace(new ModuleData(ModuleData.Type.Player, "FastPlace", "Place blocks, but fast.")));
        add(new ChatCommands(new ModuleData(ModuleData.Type.Other, "Commands", "Commands, but for chat.")));
        add(new Enabled(new ModuleData(ModuleData.Type.Visuals, "HUD", "Your hud.")));
        add(new Nametags(new ModuleData(ModuleData.Type.Visuals, "2DTags", "Nametags that are rendered in the 2D view.")));
        add(new ArmorStatus(new ModuleData(ModuleData.Type.Visuals, "ArmorHUD", "Shows you your armor stats.")));   
        add(new InventoryWalk(new ModuleData(ModuleData.Type.Player, "InvMove", "Walk in inventory + carry extra items.")));
        add(new Crosshair(new ModuleData(ModuleData.Type.Visuals, "Crosshair", "Draws a custom crosshair.")));
        add(new Killaura(new ModuleData(ModuleData.Type.Combat, "KillAura", "Attacks entities for you.")));
        add(new NoRotate(new ModuleData(ModuleData.Type.Player, "NoRotate", "Prevents the server from forcing head rotations.")));
        add(new NoSlowdown(new ModuleData(ModuleData.Type.Movement, "NoSlowdown", "Movement isn't reduced when using an item.")));
        add(new AutoPot(new ModuleData(ModuleData.Type.Combat, "AutoPot", "Throws potions to heal for you.")));
        add(new Radar(new ModuleData(ModuleData.Type.Visuals, "Radar", "Shows you all the players around you.")));
        add(new AutoSay(new ModuleData(ModuleData.Type.Other, "AutoSay", "Says what ever you set the string to for you.")));
        add(new AutoRespawn(new ModuleData(ModuleData.Type.Player, "Respawn", "Respawns you after you've died.")));
        add(new Chams(new ModuleData(ModuleData.Type.Visuals, "Chams", "Allows you to see entities throught blocks.")));
        add(new TabGUI(new ModuleData(ModuleData.Type.Visuals, "TabGUI", "TabGUI.")));
        add(new Phase(new ModuleData(ModuleData.Type.Movement, "Phase", "Clip through blocks.")));
        add(new BedFucker(new ModuleData(ModuleData.Type.MiniGames, "BedNuker", "Breaks beds around you.")));
        add(new AutoTPA(new ModuleData(ModuleData.Type.Other, "AutoAccept", "Auto accepts invites/requests for you.")));
        add(new Effects(new ModuleData(ModuleData.Type.Player, "Zoot", "Removes harmful potion effects & fire.")));
        add(new KeepSprint(new ModuleData(ModuleData.Type.Movement, "KeepSprint", "Prevents server from setting sprint.")));
        add(new AntiFreeze(new ModuleData(ModuleData.Type.Movement, "Unstuck", "Toggle this when NCP/AntiCheats freezes you.")));
        add(new Murder(new ModuleData(ModuleData.Type.MiniGames, "Murder", "Renders the murderer in murder mystery.")));
        add(new Blink(new ModuleData(ModuleData.Type.Movement, "Blink", "Holds packets and sends them on disable.")));
        add(new StreamerMode(new ModuleData(ModuleData.Type.Other, "Streaming", "Protection for streaming.")));
        add(new Waypoints(new ModuleData(ModuleData.Type.Visuals, "Waypoints", "Renders waypoints, server specific.")));
        add(new ClickGui(new ModuleData(ModuleData.Type.Other, "ClickGUI", "Opens the ClickGUI.", Keyboard.KEY_RSHIFT, KeyMask.None)));
        add(new Regen(new ModuleData(ModuleData.Type.Combat, "Regen", "Regenerates your health. Requires food.")));
        add(new AutoEat(new ModuleData(ModuleData.Type.Player, "AutoEat", "Consumes food for you instantly.")));
        add(new NoHurtCam(new ModuleData(ModuleData.Type.Visuals, "NoHurtCam", "Removes hurt cam effect.")));
        add(new MCF(new ModuleData(ModuleData.Type.Other, "MCF", "Middle click friends.")));
        //add(new Jump(new ModuleData(ModuleData.Type.Movement, "AutoJump", "Help with jumps.")));
        add(new AutoGApple(new ModuleData(ModuleData.Type.Combat, "AutoGApple", "Eat golden apple when you are low.")));
        add(new CakeEater(new ModuleData(ModuleData.Type.MiniGames, "CakeEater", "Eat cakes around you.")));
        //add(new AntiAim(new ModuleData(ModuleData.Type.MSGO, "AntiAim", "Derp, essentially.")));
        //add(new Aimbot(new ModuleData(ModuleData.Type.MSGO, "Ragebot", "Minestrike/Cops & Crims ragebot.")));
        add(new AutoMLG(new ModuleData(ModuleData.Type.Other, "AutoMLG", "Place water when you are about to hit the ground too hard.")));
        add(new Ranking(new ModuleData(ModuleData.Type.Other, "", "")));
       // add(new FakeHacker(new ModuleData(ModuleData.Type.Other, "FakeHacker", "Fakes hacks on an other player.")));
        add(new InfiniteAura(new ModuleData(ModuleData.Type.Combat, "InfiniteAura", "Killaura, but with infinite reach.")));
        setup = true;
        if (!get(TabGUI.class).isEnabled()) {
            get(TabGUI.class).toggle();
        }
        if (!get(ChatCommands.class).isEnabled()) {
            get(ChatCommands.class).toggle();
        }
        if (!get(Enabled.class).isEnabled()) {
            get(Enabled.class).toggle();
        }
        if (!get(AgoraModule.class).isEnabled()) {
            get(AgoraModule.class).toggle();
        }
        Client.um.getUser().applyModules();
        Module.loadStatus();
        Module.loadSettings();

        if (get(Blink.class).isEnabled()) {
            get(Blink.class).toggle();
        }
        if (get(Phase.class).isEnabled()) {
            get(Phase.class).toggle();
        }
        if (get(Freecam.class).isEnabled()) {
            get(Freecam.class).toggle();
        }
        if (get(Teleport.class).isEnabled()) {
            get(Teleport.class).toggle();
        }

    }


/*
	private void loadLocalPlugins() {
		// Get the directory of the jars
		String basePath = Client.getDataDir().getAbsolutePath();
		String newPath = basePath + ((basePath.endsWith(File.separator)) ? SubFolder.ModuleJars.getFolderName()
				: File.separator + SubFolder.ModuleJars.getFolderName());
		File test = new File(newPath);
		// Make the directory if it does not exist
		if (!test.exists()) {
			test.mkdirs();
		}
		// Loop through files in the directory
		for (File file : test.listFiles()) {
			// Load jars
			if (file.getAbsolutePath().endsWith(".jar")) {
				try {
					loadJar(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
*/


	/*private void loadJar(File file) throws IOException {
		JarInputStream jis = new JarInputStream(new FileInputStream(file));
		URLClassLoader urlLoader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
		for (JarEntry jarEntry = jis.getNextJarEntry(); jarEntry != null; jarEntry = jis.getNextJarEntry()) {
			// Skip non-jar entries
			if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
				continue;
			}
			String className = jarEntry.getName().replace('/', '.').substring(0,
					jarEntry.getName().length() - ".class".length());
			// Skip internal classes / others
			if (className.contains("$")) {
				continue;
			}
			try {
				// Attempt to load the class and create an instance.
				Class<?> classs = urlLoader.loadClass(className);
				// If the class is a module, load it.
				if (Module.class.isAssignableFrom(classs)) {
					add((Module) classs.newInstance());
				}
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
		// Close resources when complete.
		jis.close();
		urlLoader.close();
	}*/

    public boolean isSetup() {
        return setup;
    }

    public boolean isEnabled(Class<? extends Module> clazz) {
        Module module = get(clazz);
        return module != null && module.isEnabled();
    }

    public Module get(String name) {
        for (Module module : getArray()) {
            if (module.getName().toLowerCase().equals(name.toLowerCase()) && !(module instanceof AgoraModule)) {
                return module;
            }
        }
        return null;
    }


    public ArrayList<Module> getModuleByCategory(ModuleData.Type cat) {
    	ArrayList<Module> mods = new ArrayList<>();
    	for (Module module : Client.getModuleManager().getArray()) {
    		if(module.getType() == cat && !module.isHidden() && !(module instanceof AgoraModule) && !(module instanceof AgoraSwitchModule)){
    			mods.add(module);
    		}
    	}
        return mods.isEmpty()?null:mods;
    }
}
