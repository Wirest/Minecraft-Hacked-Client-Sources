package org.m0jang.crystal.GUI.click;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.component.Checkbox;
import org.m0jang.crystal.GUI.click.component.ComboBox;
import org.m0jang.crystal.GUI.click.component.Info;
import org.m0jang.crystal.GUI.click.component.Label;
import org.m0jang.crystal.GUI.click.component.ModuleButton;
import org.m0jang.crystal.GUI.click.component.Seplator;
import org.m0jang.crystal.GUI.click.component.Slider;
import org.m0jang.crystal.GUI.click.component.WindowButton;
import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.GUI.click.window.WindowPreset;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Mod.Collection.Render.ClickGUI;
import org.m0jang.crystal.Utils.MathUtils;
import org.m0jang.crystal.Values.Value;
import org.m0jang.crystal.Values.ValueManager;

public class WolframGui extends GuiScreen {
   public List windows = new ArrayList();
   public List staticWindows = new ArrayList();
   public WindowPreset defaultPreset = new WindowPreset("Default");
   public List presets = new ArrayList();
   private int mouseX;
   private int mouseY;
   public boolean isOpened = false;
   public static int defaultWidth = 80;
   public static int settingsWidth = 100;
   public static int defaultHeight = 18;
   public static int buttonHeight = 15;
   public static int scrollbarWidth = 2;
   public static int scrollbarHeight = 50;
   public static int maxWindowHeight;
   public static int backgroundColor;
   public static int mainColor;

   static {
      maxWindowHeight = Minecraft.displayHeight - 100;
   }

   public WolframGui() {
      this.loadStaticWindows();
      this.allowUserInput = true;
      this.setupDefaultPreset();
      this.loadPresets();
      if (this.windows.size() <= 0) {
         this.loadPreset(this.defaultPreset);
      }

      Iterator var2 = Crystal.INSTANCE.getMods().getModList().iterator();

      while(true) {
         Module mod;
         Window settingsWindow;
         do {
            if (!var2.hasNext()) {
               var2 = Crystal.INSTANCE.getMods().getModList().iterator();

               while(true) {
                  do {
                     if (!var2.hasNext()) {
                        return;
                     }

                     mod = (Module)var2.next();
                     settingsWindow = this.getWindowByID(Crystal.INSTANCE.getMods().getModList().indexOf(mod));
                  } while(settingsWindow == null);

                  if (mod.getMode() != null) {
                     settingsWindow.children.add(new ComboBox(mod, settingsWindow, 0, 0, defaultHeight));
                  }

                  int i = 0;

                  for(Iterator killaura = ValueManager.getValueByModName(mod.getName()).iterator(); killaura.hasNext(); ++i) {
                     Value var21 = (Value)killaura.next();
                     if (var21.getType() == Float.TYPE) {
                        settingsWindow.children.add(new Slider(var21, settingsWindow, i, 0, defaultHeight * (i + 1), var21.getName(), var21.getName(), var21.getMinFloatValue(), var21.getMaxFloat(), var21.getIncrement()));
                     } else if (var21.getType() == Boolean.TYPE) {
                        settingsWindow.children.add(new Checkbox(var21, settingsWindow, i, 0, defaultHeight * (i + 1), var21.getName(), var21.getValType()));
                     }
                  }

                  settingsWindow.repositionComponents();
               }
            }

            mod = (Module)var2.next();
         } while(mod.getMode() == null && ValueManager.getValueByModName(mod.getName()).isEmpty());

         settingsWindow = new Window(mod.getName() + " Settings", 5, 5);
         settingsWindow.repositionComponents();
         settingsWindow.id = Crystal.INSTANCE.getMods().getModList().indexOf(mod);
         this.staticWindows.add(settingsWindow);
      }
   }

   private void loadStaticWindows() {
      this.staticWindows.clear();
      Window windowHub = new Window("Hub", 5, 5);
      windowHub.repositionComponents();
      windowHub.isEnabled = true;
      windowHub.isOpened = true;
      windowHub.y += 10;
      windowHub.id = -1;
      this.staticWindows.add(windowHub);
   }

   public void reloadStaticWindows() {
      Window windowHub = this.getWindowByID(-1);
      windowHub.children.clear();
      int i = 0;

      for(Iterator var4 = this.windows.iterator(); var4.hasNext(); ++i) {
         Window w = (Window)var4.next();
         windowHub.children.add(new WindowButton(windowHub, i, 0, 0, w.title, "", w.id));
      }

      windowHub.repositionComponents();
   }

   public void handleInput() throws IOException {
      if (Mouse.isCreated()) {
         while(Mouse.next()) {
            this.handleMouseInput();
         }
      }

   }

   public void update() {
      this.mouseX = (int)MathUtils.map((float)Mouse.getX(), 0.0F, (float)Display.getWidth(), 0.0F, (float)RenderUtils.getDisplayWidth());
      this.mouseY = (int)MathUtils.map((float)(Display.getHeight() - Mouse.getY()), 0.0F, (float)Display.getHeight(), 0.0F, (float)RenderUtils.getDisplayHeight());
      this.isOpened = Minecraft.getMinecraft().currentScreen instanceof WolframGui;
      backgroundColor = this.isOpened ? -803200992 : -1340071904;
      mainColor = this.isOpened ? GuiManager.getHexMainColor() : GuiManager.getHexMainColor() + -1342177280;
      Iterator var2 = this.staticWindows.iterator();

      Window window;
      while(var2.hasNext()) {
         window = (Window)var2.next();
         window.update(this.mouseX, this.mouseY);
         if (this.isOpened) {
            window.handleMouseUpdates(this.mouseX, this.mouseY, Mouse.isButtonDown(0));
         } else {
            window.noMouseUpdates();
         }
      }

      var2 = this.windows.iterator();

      while(var2.hasNext()) {
         window = (Window)var2.next();
         window.update(this.mouseX, this.mouseY);
         if (this.isOpened) {
            window.handleMouseUpdates(this.mouseX, this.mouseY, Mouse.isButtonDown(0));
         } else {
            window.noMouseUpdates();
         }
      }

   }

   public void onGuiClosed() {
      if (this.mc.entityRenderer.getShaderGroup() != null) {
         this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
         this.mc.entityRenderer.setTheShaderGroup((ShaderGroup)null);
      }

   }

   public void initGui() {
      if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
         }

         if (ClickGUI.blur.getBooleanValue()) {
            this.mc.entityRenderer.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
         }
      }

   }

   public void handleMouseInput() throws IOException {
      Window window;
      Iterator var2;
      if (Mouse.getEventDWheel() != 0) {
         var2 = this.windows.iterator();

         while(var2.hasNext()) {
            window = (Window)var2.next();
            if (this.isOpened) {
               window.handleWheelUpdates(this.mouseX, this.mouseY, Mouse.isButtonDown(0));
            } else {
               window.noWheelUpdates();
            }
         }

         var2 = this.staticWindows.iterator();

         while(var2.hasNext()) {
            window = (Window)var2.next();
            if (this.isOpened) {
               window.handleWheelUpdates(this.mouseX, this.mouseY, Mouse.isButtonDown(0));
            } else {
               window.noWheelUpdates();
            }
         }
      } else {
         var2 = this.windows.iterator();

         while(var2.hasNext()) {
            window = (Window)var2.next();
            window.noWheelUpdates();
         }

         var2 = this.staticWindows.iterator();

         while(var2.hasNext()) {
            window = (Window)var2.next();
            window.noWheelUpdates();
         }
      }

      super.handleMouseInput();
   }

   public void render() {
      int i;
      Window w;
      for(i = this.windows.size(); i > 0; --i) {
         w = (Window)this.windows.get(i - 1);
         if (w.isEnabled && this.isOpened) {
            w.render(this.mouseX, this.mouseY);
         }
      }

      for(i = this.staticWindows.size(); i > 0; --i) {
         w = (Window)this.staticWindows.get(i - 1);
         if (w.isEnabled && this.isOpened) {
            w.render(this.mouseX, this.mouseY);
         }
      }

   }

   public void loadPreset(WindowPreset wp) {
      this.windows.clear();
      Iterator var3 = wp.iterator();

      while(var3.hasNext()) {
         Window w = (Window)var3.next();
         w.isOpened = true;
         this.windows.add(w);
      }

      this.reloadStaticWindows();
   }

   public void setupDefaultPreset() {
      this.defaultPreset.clear();
      byte x = 110;
      int y = 5;
      int i = 0;
      Category[] info;
      int arrayList = (info = Category.values()).length;

      int var14;
      Window moduleWindow;
      for(var14 = 0; var14 < arrayList; ++var14) {
         Category radar = info[var14];
         moduleWindow = new Window(radar.name(), x, y);
         Iterator preferences = Crystal.INSTANCE.getMods().getModList().iterator();

         while(preferences.hasNext()) {
            Module module = (Module)preferences.next();
            if (module.getCategory() == radar) {
               moduleWindow.children.add(new ModuleButton(moduleWindow, i, 0, 0, module.getName(), "", module));
               ++i;
            }
         }

         moduleWindow.repositionComponents();
         this.defaultPreset.add(moduleWindow);
         y += 20;
      }

      var14 = x + 105;
      byte var15 = 5;
      moduleWindow = new Window("Info", var14, var15 + 60);
      moduleWindow.children.add(new Info(moduleWindow, 0, 0, defaultHeight, "Info"));
      moduleWindow.repositionComponents();
      this.defaultPreset.add(moduleWindow);
      Window var20 = new Window("Other Settings", var14, var15 + 80);
      i = 0;

      for(Iterator killaura = ValueManager.getValueByModName("Other").iterator(); killaura.hasNext(); ++i) {
         Value var21 = (Value)killaura.next();
         if (var21.getType() == Float.TYPE) {
            var20.children.add(new Slider(var21, var20, i, 0, defaultHeight * (i + 1), var21.getValType() + " " + var21.getName(), var21.getName(), var21.getMinFloatValue(), var21.getMaxFloat(), var21.getIncrement()));
         } else if (var21.getType() == Boolean.TYPE) {
            var20.children.add(new Checkbox(var21, var20, i, 0, defaultHeight * (i + 1), var21.getName(), var21.getValType()));
         } else if (var21.getOptions() != null && var21.getOptions().length > 0) {
            var20.children.add(new Label(var20, 0, 0, defaultHeight, var21.getName()));
            var20.children.add(new ComboBox(var21, var20, 0, 0, defaultHeight));
            var20.children.add(new Seplator(var20, 0, 0, 0));
         }
      }

      var20.repositionComponents();
      this.defaultPreset.add(var20);
      this.defaultPreset.id = -1;
   }

   public Window getWindowByID(int id) {
      Iterator var3 = this.windows.iterator();

      Window w;
      while(var3.hasNext()) {
         w = (Window)var3.next();
         if (w.id == id) {
            return w;
         }
      }

      var3 = this.staticWindows.iterator();

      while(var3.hasNext()) {
         w = (Window)var3.next();
         if (w.id == id) {
            return w;
         }
      }

      return null;
   }

   public void loadPresets() {
      this.presets.clear();
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
