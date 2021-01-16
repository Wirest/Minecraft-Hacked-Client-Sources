package me.razerboy420.weepcraft.gui.tab;

import java.util.ArrayList;
import java.util.Iterator;

import darkmagician6.EventManager;
import darkmagician6.EventTarget;
import darkmagician6.events.EventKeyPressed;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.gui.click.Click;
import me.razerboy420.weepcraft.gui.click.Window;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.gui.Gui;

public class TabGUI {

   private ArrayList category = new ArrayList();
   private int selectedTab;
   private int selectedMod;
   private int selectedOption;
   public int end;
   public static int screen = 0;
   boolean inSliderMode;
   int top = 2;
   int startx = 1;
   int endX = 65;
   int otherstartx;
   int sliderColor;
   int backgroundColor;
   int toggledColor;
   int normalColor;
   public static double var;
   public static TabGUI me;
   Timer timer;


   public TabGUI() {
      this.otherstartx = this.endX + 2;
      this.sliderColor = -16092487;
      this.backgroundColor = 1879048192;
      this.toggledColor = -5592406;
      this.normalColor = -11513776;
      this.timer = new Timer();
      EventManager.register(this);
      Module.Category[] arrayOfModCategory;
      int j = (arrayOfModCategory = Module.Category.values()).length;

      for(int i = 0; i < j; ++i) {
         Module.Category mc = arrayOfModCategory[i];
         if(!mc.name().equalsIgnoreCase("Other") && !mc.name().equalsIgnoreCase("Hidden")) {
            this.category.add(mc.toString().substring(0, 1) + mc.toString().substring(1, mc.toString().length()).toLowerCase());
         }
      }

   }

   public static TabGUI get() {
      return me;
   }

   public void draw() {
      Window w = null;
      Weepcraft.getClick();
      Iterator count = Click.windows.iterator();

      while(count.hasNext()) {
         Window border = (Window)count.next();
         if(border.getTitle().equalsIgnoreCase("Mod Hub")) {
            w = border;
         }
      }

      this.top = w.getY() + w.dragY + 12;
      this.startx = w.getX() + w.dragX;
      this.endX = this.startx + 90;
      this.otherstartx = this.endX;
      this.toggledColor = -7303024;
      this.backgroundColor = ColorUtil.getHexColor(Weepcraft.panlColor) & 1895825407;
      int var9 = ColorUtil.getHexColor(Weepcraft.borderColor);
      boolean var10 = false;
      RenderUtils2D.drawRect((float)this.startx, (float)this.top, (float)this.endX, (float)(this.top + 1 + this.category.size() * 11 + 7), this.backgroundColor);
      int categoryCount = 0;
      Iterator mod = this.category.iterator();

      while(mod.hasNext()) {
         String modCount = (String)mod.next();
         if(!modCount.equalsIgnoreCase("Hidden")) {
            if(categoryCount == this.selectedTab) {
               modCount = ColorUtil.getColor(Weepcraft.normalColor) + modCount;
            } else {
               modCount = ColorUtil.getColor(Weepcraft.whiteColor) + modCount;
            }

            Gui.drawString(Wrapper.fr(), modCount, (float)(this.startx + 2), (float)(this.top + 3 + categoryCount * 12), this.toggledColor);
            ++categoryCount;
         }
      }

      if(screen == 1) {
         RenderUtils2D.drawRect((float)this.otherstartx, (float)this.top, (float)this.otherstartx + this.getLongestModWidth() + 1.0F, (float)(this.getModsForCategory().size() * 12 + this.top + 1), this.backgroundColor);
         int var11 = 0;

         for(Iterator var7 = this.getModsForCategory().iterator(); var7.hasNext(); ++var11) {
            Module var12 = (Module)var7.next();
            String name = (this.getSelectedMod() == var12?ColorUtil.getColor(Weepcraft.normalColor):(var12.isToggled()?ColorUtil.getColor(Weepcraft.enabledColor):ColorUtil.getColor(Weepcraft.whiteColor))) + var12.getName();
            Gui.drawString(Wrapper.fr(), name, (float)(this.otherstartx + 2), (float)(this.top + 3 + var11 * 12), var12.isToggled()?this.toggledColor:this.normalColor);
         }
      }

   }

   public void down() {
      if(screen == 0) {
         if(this.selectedTab >= this.category.size() - 1) {
            this.selectedTab = -1;
            this.inSliderMode = true;
         } else {
            this.inSliderMode = false;
         }

         ++this.selectedTab;
      } else if(screen == 1) {
         if(this.selectedMod >= this.getModsForCategory().size() - 1) {
            this.selectedMod = -1;
         }

         ++this.selectedMod;
      }

   }

   public void up() {
      if(screen == 0) {
         if(this.selectedTab <= 0) {
            this.selectedTab = this.category.size();
            this.inSliderMode = true;
         } else {
            this.inSliderMode = false;
         }

         --this.selectedTab;
      } else if(screen == 1) {
         if(this.selectedMod <= 0) {
            this.selectedMod = this.getModsForCategory().size();
         }

         --this.selectedMod;
      }

   }

   public void left() {
      if(screen == 1) {
         screen = 0;
      }

   }

   public void right() {
      if(screen == 1) {
         this.enter();
      }

      if(screen == 0) {
         screen = 1;
         this.selectedMod = 0;
      }

   }

   public void enter() {
      if(screen == 1 && this.getSelectedMod() != null) {
         ((Module)this.getModsForCategory().get(this.selectedMod)).toggle();
      }

   }

   public Module getSelectedMod() {
      return (Module)this.getModsForCategory().get(this.selectedMod);
   }

   private ArrayList getModsForCategory() {
      ArrayList mods = new ArrayList();
      Iterator var3 = Weepcraft.getMods().iterator();

      while(var3.hasNext()) {
         Module mod = (Module)var3.next();
         if(mod.getCategory().name().equalsIgnoreCase((String)this.category.get(this.selectedTab))) {
            mods.add(mod);
         }
      }

      return mods;
   }

   private float getLongestModWidth() {
      float longest = 0.0F;
      Iterator var3 = this.getModsForCategory().iterator();

      while(var3.hasNext()) {
         Module mod = (Module)var3.next();
         if((float)Wrapper.fr().getStringWidth(mod.getName()) > longest) {
            longest = (float)Wrapper.fr().getStringWidth(" " + mod.getName());
         }
      }

      return longest;
   }

   @EventTarget
   public void onKey(EventKeyPressed event) {
      int keyCode = event.getEventKey();
      if(keyCode == 200) {
         this.up();
      }

      if(keyCode == 208) {
         this.down();
      }

      if(keyCode == 203) {
         this.left();
      }

      if(keyCode == 205) {
         this.right();
      }

      if(keyCode == 28) {
         this.enter();
      }

   }
}
