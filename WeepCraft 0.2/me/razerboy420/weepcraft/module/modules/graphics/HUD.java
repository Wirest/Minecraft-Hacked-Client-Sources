package me.razerboy420.weepcraft.module.modules.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import darkmagician6.EventTarget;
import darkmagician6.events.EventRender2D;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Click;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

@Module.Mod(
   category = Module.Category.RENDER,
   description = "Renders the Heads Up Display",
   key = 0,
   name = "HUD"
)
public class HUD extends Module {

   public static Value arraylist = new Value("hud_Arraylist", Boolean.valueOf(true));
   public static ArrayList toggled = new ArrayList();


   @EventTarget(4)
   public void onHud(EventRender2D event) {
      if(!Wrapper.getSettings().showDebugInfo) {
         String weepcraftString = ColorUtil.getColor(Weepcraft.primaryColor) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft" + ColorUtil.getColor(Weepcraft.normalColor) + " v" + Weepcraft.version;
         Gui.drawString(Wrapper.fr(), weepcraftString, 1.0F, 1.0F, -1);
         if(toggled.isEmpty()) {
            Iterator w = Weepcraft.getMods().iterator();

            while(w.hasNext()) {
               Module var25 = (Module)w.next();
               toggled.add(var25);
            }
         }

         reorderMods();
         Iterator var5;
         int meme;
         int var9;
         if(arraylist.boolvalue) {
            var9 = 0;
            var5 = toggled.iterator();

            while(var5.hasNext()) {
               Module var10 = (Module)var5.next();
               if(var10.isToggled() && var10.isVisible()) {
                  meme = Wrapper.getPlayer().getActivePotionEffects().isEmpty()?1:27;
                  int y = meme + 10 * var9;
                  String name = ColorUtil.getColor(Weepcraft.enabledColor) + var10.getDisplayName().replace("[", "[" + ColorUtil.getColor(Weepcraft.normalColor)).replace("]", ColorUtil.getColor(Weepcraft.enabledColor) + "]");
                  Gui.drawString(Wrapper.fr(), name, (float)(RenderUtils2D.newScaledResolution().getScaledWidth() - Wrapper.fr().getStringWidth(var10.getDisplayName())), (float)y, -1);
                  ++var9;
               }
            }
         }


         Weepcraft.getClick();
         var5 = Click.windows.iterator();

         while(var5.hasNext()) {
            Window var12 = (Window)var5.next();
            if(Wrapper.mc().currentScreen != Weepcraft.getClick() && (Wrapper.mc().currentScreen == null || Wrapper.mc().currentScreen instanceof GuiChat) && (var12.isPinned() || var12.isExtended() && (var12.getTitle().equalsIgnoreCase("Mod Hub") || var12.getTitle().equalsIgnoreCase("Radar")))) {
               var12.draw(0, 0);
            }
         }

      }
   }

   public static void reorderMods() {
      ArrayList newList = toggled;
      Collections.sort(newList, new Comparator() {
         public int compare(Module mod, Module mod1) {
            String name1 = mod.getDisplayName().replace("[", "§7[").replace("]", "]");
            String name2 = mod1.getDisplayName().replace("[", "§7[").replace("]", "]");
            return Wrapper.clientFont().getStringWidth(name1) > Wrapper.clientFont().getStringWidth(name2)?-1:(Wrapper.clientFont().getStringWidth(name1) < Wrapper.clientFont().getStringWidth(name2)?1:0);
         }

		@Override
		public int compare(Object arg0, Object arg1) {
			// TODO Auto-generated method stub
			return 0;
		}
      });
      toggled = newList;
   }
}
