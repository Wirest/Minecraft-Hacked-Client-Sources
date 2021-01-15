package saint.clickgui;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.clickgui.element.elements.ElementModuleButton;
import saint.clickgui.element.panel.Panel;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class ClickGui extends GuiScreen {
   private final List panels = new CopyOnWriteArrayList();

   public List getPanels() {
      return this.panels;
   }

   public ClickGui() {
      this.width = 100;
      this.height = 18;
      this.panels.add(new Panel(ModManager.Category.PLAYER, 120, 20, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.PLAYER) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.PLAYER));
               }
            }

         }
      });
      this.panels.add(new Panel(ModManager.Category.MOVEMENT, 120, 60, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.MOVEMENT) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.MOVEMENT));
               }
            }

         }
      });
      this.panels.add(new Panel(ModManager.Category.COMBAT, 120, 100, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.COMBAT) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.COMBAT));
               }
            }

         }
      });
      this.panels.add(new Panel(ModManager.Category.RENDER, 120, 140, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.RENDER) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.RENDER));
               }
            }

         }
      });
      this.panels.add(new Panel(ModManager.Category.WORLD, 120, 180, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.WORLD) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.WORLD));
               }
            }

         }
      });
      this.panels.add(new Panel(ModManager.Category.EXPLOITS, 120, 220, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.EXPLOITS) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.EXPLOITS));
               }
            }

         }
      });
      this.panels.add(new Panel(ModManager.Category.INVISIBLE, 120, 260, this.width, this.height, false) {
         public void setupItems() {
            Iterator var2 = Saint.getModuleManager().getContentList().iterator();

            while(var2.hasNext()) {
               Module module = (Module)var2.next();
               if (module.getCategory() == ModManager.Category.INVISIBLE) {
                  this.getElements().add(new ElementModuleButton(module, ModManager.Category.INVISIBLE));
               }
            }

         }
      });
   }

   public void drawScreen(int mouseX, int mouseY, float button) {
      GL11.glPushAttrib(1048575);
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         Panel panel = (Panel)var5.next();
         panel.drawScreen(mouseX, mouseY, button);
      }

      GL11.glPopAttrib();
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         Panel panel = (Panel)var5.next();
         panel.mouseClicked(mouseX, mouseY, mouseButton);
      }

   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      Iterator var5 = this.panels.iterator();

      while(var5.hasNext()) {
         Panel panel = (Panel)var5.next();
         panel.mouseReleased(mouseX, mouseY, state);
      }

   }

   public void onGuiClosed() {
      Saint.getFileManager().getFileUsingName("guiconfiguration").saveFile();
   }
}
