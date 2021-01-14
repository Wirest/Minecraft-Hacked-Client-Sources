package rip.autumn.module.impl.visuals.hud.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import rip.autumn.core.Autumn;
import rip.autumn.module.Module;
import rip.autumn.module.impl.visuals.hud.Component;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.utils.render.Palette;
import rip.autumn.utils.render.Translate;

public final class ModList extends Component {
   private float hue = 1.0F;

   public ModList(HUDMod parent) {
      super(parent);
   }

   public void draw(ScaledResolution sr) {
      HUDMod hud = this.getParent();
      int width = sr.getScaledWidth();
      int height = sr.getScaledHeight();
      FontRenderer fr = hud.defaultFont.getValue() ? mc.fontRendererObj : mc.fontRenderer;
      boolean bottom = hud.arrayListPosition.getValue() == HUDMod.ArrayListPosition.BOTTOM;
      List sortedList = this.getSortedModules(fr);
      Color modListColor = (Color)hud.color.getValue();
      float translationFactor = 14.4F / (float)Minecraft.getDebugFPS();
      int listOffset = 10;
      int y = bottom ? height - listOffset : 0;
      this.hue += translationFactor / 100.0F;
      if (this.hue > 1.0F) {
         this.hue = 0.0F;
      }

      float h = this.hue;
      GL11.glEnable(3042);
      int i = 0;

      for(int sortedListSize = sortedList.size(); i < sortedListSize; ++i) {
         Module module = (Module)sortedList.get(i);
         Translate translate = module.getTranslate();
         String moduleLabel = module.getDisplayLabel();
         float length = (float)fr.getStringWidth(moduleLabel);
         float featureX = (float)width - length - 2.0F;
         boolean enable = module.isEnabled();
         if (bottom) {
            if (enable) {
               translate.interpolate((double)featureX, (double)(y + 1), (double)translationFactor);
            } else {
               translate.interpolate((double)width, (double)(height + 1), (double)translationFactor);
            }
         } else if (enable) {
            translate.interpolate((double)featureX, (double)(y + 1), (double)translationFactor);
         } else {
            translate.interpolate((double)width, (double)(-listOffset - 1), (double)translationFactor);
         }

         double translateX = translate.getX();
         double translateY = translate.getY();
         boolean visible = bottom ? translateY < (double)height : translateY > (double)(-listOffset);
         if (visible) {
            int color;
            switch((HUDMod.ArrayListColor)hud.modListColorMode.getValue()) {
            case PULSING:
               color = Palette.fade(modListColor, 100, sortedList.indexOf(module) * 2 + 10).getRGB();
               break;
            case RAINBOW:
               color = Color.HSBtoRGB(h, 0.7F, 1.0F);
               break;
            default:
               color = modListColor.getRGB();
            }

            int nextIndex = sortedList.indexOf(module) + 1;
            Module nextModule = null;
            if (sortedList.size() > nextIndex) {
               nextModule = this.getNextEnabledModule(sortedList, nextIndex);
            }

            if (hud.modListBackground.getValue()) {
               Gui.drawRect(translateX - 2.0D, translateY - 1.0D, (double)width, translateY + (double)listOffset - 1.0D, (new Color(13, 13, 13, (int)(255.0F * ((Double)this.parent.modListBackgroundAlpha.getValue()).floatValue()))).getRGB());
            }

            if (hud.modListSideBar.getValue()) {
               Gui.drawRect(translateX + (double)length + 1.0D, translateY - 1.0D, (double)width, translateY + (double)listOffset - 1.0D, color);
            }

            if (hud.modListOutline.getValue()) {
               Gui.drawRect(translateX - 2.5D, translateY - 1.0D, translateX - 2.0D, translateY + (double)listOffset - 1.0D, color);
               double offsetY = bottom ? -0.5D : (double)listOffset;
               if (nextModule != null) {
                  double dif = (double)(length - (float)fr.getStringWidth(nextModule.getDisplayLabel()));
                  Gui.drawRect(translateX - 2.5D, translateY + offsetY - 1.0D, translateX - 2.5D + dif, translateY + offsetY - 0.5D, color);
               } else {
                  Gui.drawRect(translateX - 2.5D, translateY + offsetY - 1.0D, (double)width, translateY + offsetY - 0.5D, color);
               }
            }

            fr.drawStringWithShadow(moduleLabel, (float)translateX, (float)translateY, color);
            if (module.isEnabled()) {
               y += bottom ? -listOffset : listOffset;
            }

            h += translationFactor / 6.0F;
         }
      }

   }

   private Module getNextEnabledModule(List modules, int startingIndex) {
      int i = startingIndex;

      for(int modulesSize = modules.size(); i < modulesSize; ++i) {
         Module module = (Module)modules.get(i);
         if (module.isEnabled()) {
            return module;
         }
      }

      return null;
   }

   private List getSortedModules(FontRenderer fr) {
      List sortedList = new ArrayList(Autumn.MANAGER_REGISTRY.moduleManager.getModules());
      sortedList.removeIf(Module::isHidden);
      sortedList.sort(Comparator.comparingDouble((e) -> {
         return (double)(-fr.getStringWidth(e.getDisplayLabel()));
      }));
      return sortedList;
   }
}
