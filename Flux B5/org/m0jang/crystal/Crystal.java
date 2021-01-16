package org.m0jang.crystal;

import net.minecraft.client.multiplayer.ServerData;
import org.lwjgl.opengl.Display;
import org.m0jang.crystal.Config.Config;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.TabGUI;
import org.m0jang.crystal.Mod.CommandManager;
import org.m0jang.crystal.Mod.Modules;
import org.m0jang.crystal.Mod.Collection.Misc.Commands;
import org.m0jang.crystal.Mod.Collection.Render.Hud;
import org.m0jang.crystal.Utils.AntiRename;
import org.m0jang.crystal.Utils.FriendManager;
import org.m0jang.crystal.Values.Value;
import org.m0jang.crystal.Values.ValueManager;

public class Crystal {
   public static Crystal INSTANCE = new Crystal();
   public Modules mods;
   public FriendManager friendManager;
   public CommandManager commandManager;
   public ValueManager values;
   private Config config;
   public GuiManager guiManager;
   ServerData lastServer;
   public static Value HudTheme = new Value("Other", String.class, "Hud Theme", "Flux", new String[]{"OldFlux", "Flux"});
   public static Value SwordAnimation = new Value("Other", String.class, "Sword Animation", "Flux", new String[]{"Flux", "Slide", "Vanilla"});
   public static Value RotateItem;
   public static Value RealsticItem;
   public static Value CustomFont;

   static {
      RotateItem = new Value("Other", Boolean.TYPE, "360° Item", false);
      RealsticItem = new Value("Other", Boolean.TYPE, "Realstic Item", true);
      CustomFont = new Value("Other", Boolean.TYPE, "Custom Font", true);
   }

   public static String getName() {
      return AntiRename.getClientName();
   }

   public void start(int killswitch) {
      this.friendManager = new FriendManager();
      this.commandManager = new CommandManager();
      this.mods = new Modules();
      this.values = new ValueManager();
      this.config = new Config(killswitch);
      this.guiManager = new GuiManager();
      this.mods.get(Commands.class).setEnabled(true);
      TabGUI.init();
      INSTANCE.getMods().get(Hud.class).setEnabled(true);
      Display.setTitle(AntiRename.getClientName() + " b5 [Minecraft 1.8]");
      this.guiManager.loadGui();
   }

   public Modules getMods() {
      return this.mods;
   }

   public Config getConfig() {
      return this.config;
   }

   public GuiManager getGuiManager() {
      return this.guiManager;
   }

   public ServerData getLastServer() {
      return this.lastServer;
   }

   public void setLastServer(ServerData lastServer) {
      this.lastServer = lastServer;
   }
}
