package saint.modstuff;

import java.util.Random;
import net.minecraft.client.Minecraft;
import saint.Saint;
import saint.eventstuff.Listener;

public abstract class Module implements Listener {
   protected static final Minecraft mc = Minecraft.getMinecraft();
   protected int keybind;
   protected int color;
   protected boolean enabled;
   protected boolean visible;
   protected ModManager.Category category;
   protected String name;
   protected String tag;
   private static Random random;

   public static final Random getRandom() {
      return random == null ? (random = new Random()) : random;
   }

   public Module(String name, int keybind, int color, ModManager.Category category) {
      this.category = category;
      this.name = name;
      this.tag = name;
      this.keybind = keybind;
      this.color = color;
      this.enabled = false;
      this.visible = true;
   }

   public Module(String name) {
      this(name, 0, -1, ModManager.Category.INVISIBLE);
      this.visible = false;
   }

   public Module(String name, int color, ModManager.Category category) {
      this(name, 0, color, category);
      this.category = category;
   }

   public final ModManager.Category getCategory() {
      return this.category;
   }

   public void setCategory(ModManager.Category category) {
      this.category = category;
   }

   public int getColor() {
      return this.color;
   }

   public int getKeybind() {
      return this.keybind;
   }

   public String getName() {
      return this.name;
   }

   public String getTag() {
      return this.tag;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isVisible() {
      return this.visible;
   }

   public void onDisabled() {
      Saint.getEventManager().removeListener(this);
   }

   public void onEnabled() {
      Saint.getEventManager().addListener(this);
   }

   public void setColor(int color) {
      this.color = color;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
      if (Saint.getFileManager().getFileUsingName("moduleconfiguration") != null) {
         Saint.getFileManager().getFileUsingName("moduleconfiguration").saveFile();
      }

      if (this.enabled) {
         this.onEnabled();
      } else {
         this.onDisabled();
      }

   }

   public void setKeybind(int keybind) {
      this.keybind = keybind;
      if (Saint.getFileManager().getFileUsingName("moduleconfiguration") != null) {
         Saint.getFileManager().getFileUsingName("moduleconfiguration").saveFile();
      }

   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void toggle() {
      this.enabled = !this.enabled;
      if (Saint.getFileManager().getFileUsingName("moduleconfiguration") != null) {
         Saint.getFileManager().getFileUsingName("moduleconfiguration").saveFile();
      }

      if (this.enabled) {
         this.onEnabled();
      } else {
         this.onDisabled();
      }

   }
}
