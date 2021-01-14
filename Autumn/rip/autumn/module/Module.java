package rip.autumn.module;

import net.minecraft.client.Minecraft;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.keybinds.KeyboardKey;
import rip.autumn.module.option.Configurable;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.render.Translate;

public class Module extends Configurable {
   protected static final Minecraft mc = Minecraft.getMinecraft();
   private final String label = ((Label)this.getClass().getAnnotation(Label.class)).value();
   private final ModuleCategory category = ((Category)this.getClass().getAnnotation(Category.class)).value();
   private final Translate translate = new Translate(0.0F, 0.0F);
   private final KeyboardKey keyBind = new KeyboardKey("");
   private String[] aliases;
   private boolean enabled;
   private boolean hidden;

   public Module() {
      this.aliases = new String[]{this.label};
   }

   private static String capitalizeThenLowercase(String str) {
      return Character.toTitleCase(str.charAt(0)) + str.substring(1).toLowerCase();
   }

   public String[] getAliases() {
      return this.aliases;
   }

   public void setAliases(String[] aliases) {
      this.aliases = aliases;
   }

   public KeyboardKey getKeyBind() {
      return this.keyBind;
   }

   public Translate getTranslate() {
      return this.translate;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public void setHidden(boolean hidden) {
      this.hidden = hidden;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      if (this.enabled != enabled) {
         this.enabled = enabled;
         if (enabled) {
            this.onEnabled();
            Autumn.EVENT_BUS_REGISTRY.eventBus.subscribe(this);
         } else {
            Autumn.EVENT_BUS_REGISTRY.eventBus.unsubscribe(this);
            this.onDisabled();
         }
      }

   }

   public final String getLabel() {
      return this.label;
   }

   public final String getDisplayLabel() {
      EnumOption mode = this.getMode();
      if (mode != null && mode.getValue() != null) {
         String modeValue = ((Enum)mode.getValue()).name();
         String formattedMode = capitalizeThenLowercase(modeValue);
         return this.label + "§7 " + formattedMode;
      } else {
         return this.label;
      }
   }

   public final ModuleCategory getCategory() {
      return this.category;
   }

   public final void toggle() {
      this.setEnabled(!this.enabled);
   }

   public void onEnabled() {
   }

   public void onDisabled() {
   }
}
