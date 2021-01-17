package me.slowly.client.ui.hotbaritems;

import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.CustomValue;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class HotbarItem {
   public FontRenderer font;
   public CustomValue enabled;
   public CustomValue fontName;
   public CustomValue position;
   public CustomValue fontSize;
   public CustomValue useShadow;
   public CustomValue shadowSize;
   public CustomValue fontColor;

   public HotbarItem(CustomValue enabled, CustomValue position, CustomValue font, CustomValue fontSize, CustomValue useShadow, CustomValue shadowSize, CustomValue colorPicker) {
      this.enabled = enabled;
      this.position = position;
      this.fontName = font;
      this.fontSize = fontSize;
      this.useShadow = useShadow;
      this.shadowSize = shadowSize;
      this.fontColor = colorPicker;
   }

   public abstract String getStr();

   public FontRenderer getFont() {
      return this.font;
   }

   public void setFont(UnicodeFontRenderer font) {
      this.font = font;
   }

   public void updateFont() {
      String name = CustomHUDHotbar.getFontName(this.fontName.getCurrentModeStr());
      if (name.contains("minecraft")) {
         this.font = Minecraft.getMinecraft().fontRendererObj;
      } else if (name.contains("simpleton")) {
         this.font = Client.getInstance().getFontManager().getFont(name, ((Double)this.fontSize.getValueState()).floatValue(), true);
      } else {
         this.font = Client.getInstance().getFontManager().getFont(name, ((Double)this.fontSize.getValueState()).floatValue());
      }

   }
}
