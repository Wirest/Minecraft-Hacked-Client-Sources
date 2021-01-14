package rip.autumn.module.impl.visuals.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.render.RenderGuiEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.visuals.hud.impl.InfoComponent;
import rip.autumn.module.impl.visuals.hud.impl.ModList;
import rip.autumn.module.impl.visuals.hud.impl.TabComponent;
import rip.autumn.module.impl.visuals.hud.impl.Watermark;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.ColorOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;

@Label("HUD")
@Category(ModuleCategory.VISUALS)
public final class HUDMod extends Module {
   public static String clientName;
   private static final Minecraft mc;
   public final ColorOption color = new ColorOption("Color", new Color(255, 0, 0));
   public final EnumOption arrayListPosition;
   public final BoolOption tabGui;
   public final BoolOption watermark;
   public final BoolOption defaultFont;
   public final EnumOption infoDisplayMode;
   public final EnumOption modListColorMode;
   public final BoolOption modListSideBar;
   public final BoolOption modListOutline;
   public final BoolOption modListBackground;
   public final DoubleOption modListBackgroundAlpha;
   private final List components;

   public HUDMod() {
      this.arrayListPosition = new EnumOption("Mod List Position", HUDMod.ArrayListPosition.TOP);
      this.tabGui = new BoolOption("Tab Gui", true);
      this.watermark = new BoolOption("Watermark", true);
      this.defaultFont = new BoolOption("Default Font", false);
      this.infoDisplayMode = new EnumOption("Info Display Mode", HUDMod.InfoDisplayMode.LEFT);
      this.modListColorMode = new EnumOption("Mod List Color Mode", HUDMod.ArrayListColor.PULSING);
      this.modListSideBar = new BoolOption("Mod List Side Bar", false);
      this.modListOutline = new BoolOption("Mod List Outline", true);
      this.modListBackground = new BoolOption("Mod List Background", true);
      this.modListBackgroundAlpha = new DoubleOption("Mod List Background Alpha", 0.2D, 0.0D, 1.0D, 0.05D);
      this.components = new ArrayList();
      this.setEnabled(true);
      this.setHidden(true);
      this.components.add(new ModList(this));
      this.components.add(new Watermark(this));
      this.components.add(new InfoComponent(this));
      this.components.add(new TabComponent(this));
      this.addOptions(new Option[]{this.color, this.tabGui, this.defaultFont, this.infoDisplayMode, this.arrayListPosition, this.modListColorMode, this.modListSideBar, this.modListOutline, this.modListBackground, this.modListBackgroundAlpha, this.watermark});
   }

   @Listener(RenderGuiEvent.class)
   public final void onRenderGui(RenderGuiEvent event) {
      if (!mc.gameSettings.showDebugInfo) {
         ScaledResolution sr = event.getScaledResolution();
         int i = 0;

         for(int componentsSize = this.components.size(); i < componentsSize; ++i) {
            Component component = (Component)this.components.get(i);
            component.draw(sr);
         }

      }
   }

   static {
      clientName = Autumn.INSTANCE.getName() + " " + Autumn.INSTANCE.getVersion();
      mc = Minecraft.getMinecraft();
   }

   public static enum InfoDisplayMode {
      LEFT,
      RIGHT,
      OFF;
   }

   public static enum ArrayListColor {
      STATIC,
      PULSING,
      RAINBOW;
   }

   public static enum ArrayListPosition {
      BOTTOM,
      TOP;
   }
}
