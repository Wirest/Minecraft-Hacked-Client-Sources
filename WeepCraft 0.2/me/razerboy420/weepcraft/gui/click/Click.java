package me.razerboy420.weepcraft.gui.click;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.commands.CommandManager;
import me.razerboy420.weepcraft.files.GuiFile;
import me.razerboy420.weepcraft.files.ValuesFile;
import me.razerboy420.weepcraft.gui.click.items.ClickItem;
import me.razerboy420.weepcraft.gui.click.window.WindowAuto;
import me.razerboy420.weepcraft.gui.click.window.WindowCombat;
import me.razerboy420.weepcraft.gui.click.window.WindowMisc;
import me.razerboy420.weepcraft.gui.click.window.WindowModHub;
import me.razerboy420.weepcraft.gui.click.window.WindowMovement;
import me.razerboy420.weepcraft.gui.click.window.WindowPlayer;
import me.razerboy420.weepcraft.gui.click.window.WindowRadar;
import me.razerboy420.weepcraft.gui.click.window.WindowRender;
import me.razerboy420.weepcraft.gui.click.window.WindowWorld;
import me.razerboy420.weepcraft.gui.tab.TabGUI;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.gui.GuiScreen;

public class Click extends GuiScreen {

   public static ArrayList windows = new ArrayList();
   public static ArrayList unFocusedWindows = new ArrayList();
   public Window combat = (new WindowCombat()).init();
   public Window player = (new WindowPlayer()).init();
   public Window render = (new WindowRender()).init();
   public Window world = (new WindowWorld()).init();
   public Window misc = (new WindowMisc()).init();
   public Window movement = (new WindowMovement()).init();
   public Window auto = (new WindowAuto()).init();
   public Window modhub = (new WindowModHub()).init();
   public Window radar = (new WindowRadar()).init();
   public GuiConsoleField console;
   public ClickHub hub;
   public TabGUI tabgui;


   public void initGui() {
      GuiFile.load();
      this.console = new GuiConsoleField(500, Wrapper.fr(), this.width / 2 - 250, 0, 500, 15);
      this.console.setFocused(true);
      this.console.setCanLoseFocus(false);
      if(this.tabgui == null) {
         this.tabgui = new TabGUI();
      }

      if(this.hub == null) {
         this.hub = new ClickHub(0, this.height / 2 - 70, 100, 140);
         int count = 0;

         for(Iterator var3 = windows.iterator(); var3.hasNext(); ++count) {
            Window w = (Window)var3.next();
            int y = this.height / 2 - 70 + 2 + 10 * count;
            this.hub.clickitems.add(new ClickItem(2, y, w));
         }
      }

   }

   public void onGuiClosed() {
      GuiFile.save();
      ValuesFile.save();
   }

   public static void sendPanelToFront(Window window) {
      if(windows.contains(window)) {
         int panelIndex = windows.indexOf(window);
         windows.remove(panelIndex);
         windows.add(windows.size(), window);
      }

   }

   public static Window getFocusedPanel() {
      return (Window)windows.get(windows.size() - 1);
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      this.console.drawTextBox();
      this.console.updateCursorCounter();
      this.hub.draw();
      if(!this.console.getText().isEmpty()) {
         ArrayList window = new ArrayList();
         Iterator i = Weepcraft.getMods().iterator();

         while(i.hasNext()) {
            Module count = (Module)i.next();
            if(this.console.getText().toLowerCase().startsWith(count.getName().toLowerCase())) {
               window.add(count.getName() + " - " + count.getDescription());
               String s = this.console.getText();

               try {
                  s = this.console.getText().split(" ")[1];
               } catch (Exception var11) {
                  ;
               }

               Value y1;
               Iterator name;
               String p;
               if(!s.equalsIgnoreCase(this.console.getText()) && s.length() > 0) {
                  name = count.getValues().iterator();

                  while(name.hasNext()) {
                     y1 = (Value)name.next();
                     if(y1.name.replace(count.getName().toLowerCase() + "_", "").toLowerCase().startsWith(s.toLowerCase())) {
                        p = "";
                        if(y1.isafloat) {
                           p = " [Value]";
                        }

                        if(y1.isamode) {
                           p = " [Letters]";
                        }

                        if(y1.isaboolean) {
                           p = " [True/False]";
                        }

                        window.add(count.getName() + " " + y1.name.replace(count.getName().toLowerCase() + "_", "") + p);
                     }
                  }
               } else {
                  for(name = count.getValues().iterator(); name.hasNext(); window.add(count.getName() + " " + y1.name.replace(count.getName().toLowerCase() + "_", "") + p)) {
                     y1 = (Value)name.next();
                     p = "";
                     if(y1.isafloat) {
                        p = " [Value]";
                     }

                     if(y1.isamode) {
                        p = " [Letters]";
                     }

                     if(y1.isaboolean) {
                        p = " [True/False]";
                     }
                  }
               }
            } else if(count.getName().toLowerCase().startsWith(this.console.getText().toLowerCase())) {
               window.add(count.getName() + " - " + count.getDescription());
            }
         }

         i = CommandManager.cmds.iterator();

         while(i.hasNext()) {
            Command var13 = (Command)i.next();
            if(var13.names[0].toLowerCase().startsWith(this.console.getText().toLowerCase()) && !window.contains(var13.names[0] + " - " + var13.syntax + " - " + var13.desc)) {
               window.add(var13.names[0] + " - " + var13.syntax + " - " + var13.desc);
            }
         }

         if(window.size() > 0) {
            drawRect((float)this.console.xPosition, 16.0F, (float)(this.console.xPosition + 500), (float)(16 + 11 * window.size()), 1879048192);
            int var14 = 0;

            for(Iterator var18 = window.iterator(); var18.hasNext(); ++var14) {
               String var16 = (String)var18.next();
               int var19 = 17 + 11 * var14;
               String var20 = ColorUtil.getColor(Weepcraft.whiteColor) + var16;
               drawString(Wrapper.fr(), var20, (float)(this.console.xPosition + 4), (float)var19, -1);
            }
         }
      }

      Iterator var15 = windows.iterator();

      while(var15.hasNext()) {
         Window var12 = (Window)var15.next();
         var12.draw(x, y);
         if(var12.getTitle().equalsIgnoreCase("Mod Hub")) {
            var12.setPinned(var12.isExtended());
         }

         for(int var17 = 0; var17 < 400; ++var17) {
            if(var12.getX() + var12.dragX < 0) {
               ++var12.dragX;
            }

            if(var12.getY() + var12.dragY < 0) {
               ++var12.dragY;
            }

            if(var12.getY() + var12.dragY + 12 > this.height) {
               --var12.dragY;
            }

            if(var12.getX() + var12.dragX + 90 > this.width) {
               --var12.dragX;
            }
         }
      }

   }

   public void mouseClicked(int x, int y, int button) {
      try {
         Iterator var5 = windows.iterator();

         while(var5.hasNext()) {
            Window window = (Window)var5.next();
            window.mouseClicked(x, y, button);
         }
      } catch (Exception var6) {
         ;
      }

      this.hub.mouseClicked();
      GuiFile.save();
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      Iterator mod = windows.iterator();

      Window meme;
      while(mod.hasNext()) {
         meme = (Window)mod.next();
         if(meme.keyTyped(typedChar, keyCode)) {
            return;
         }
      }

      mod = windows.iterator();

      while(mod.hasNext()) {
         meme = (Window)mod.next();
         if(meme.getTitle().equalsIgnoreCase("Mod Hub") && meme.isExtended() && meme.isOpen()) {
            if(keyCode == 200) {
               this.tabgui.up();
            }

            if(keyCode == 208) {
               this.tabgui.down();
            }

            if(keyCode == 203) {
               this.tabgui.left();
            }

            if(keyCode == 205) {
               this.tabgui.right();
            }
         }
      }

      if(keyCode != 28) {
         this.console.textboxKeyTyped(typedChar, keyCode);
         super.keyTyped(typedChar, keyCode);
      } else {
         boolean meme1 = false;
         Iterator var5 = Weepcraft.getMods().iterator();

         while(var5.hasNext()) {
            Module mod1 = (Module)var5.next();
            if(this.console.getText().equalsIgnoreCase(mod1.getName())) {
               mod1.toggle();
               return;
            }

            if(this.console.getText().toLowerCase().startsWith(mod1.getName().toLowerCase())) {
               meme1 = true;
            }
         }

         if(meme1) {
            Wrapper.getPlayer().sendChatMessage(".mod " + this.console.getText());
         } else {
            Wrapper.getPlayer().sendChatMessage("." + this.console.getText());
         }
      }
   }

   public void mouseMovedOrUp(int x, int y, int button) {
      Iterator var5 = windows.iterator();

      while(var5.hasNext()) {
         Window window = (Window)var5.next();
         window.mouseMovedOrUp(x, y, button);
      }

   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
