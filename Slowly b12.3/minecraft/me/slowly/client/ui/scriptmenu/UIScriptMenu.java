package me.slowly.client.ui.scriptmenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import me.slowly.client.Client;
import me.slowly.client.ui.scriptmenu.elements.UIElementButton;
import me.slowly.client.ui.scriptmenu.elements.UIElementComboBox;
import me.slowly.client.ui.scriptmenu.elements.UIElementComboList;
import me.slowly.client.ui.scriptmenu.elements.UIElementComboListScript;
import me.slowly.client.ui.scriptmenu.elements.UIElementDownloadManager;
import me.slowly.client.ui.scriptmenu.elements.UIElementTextField;
import me.slowly.client.ui.scriptmenu.elements.UIElementTreeViewScript;
import me.slowly.client.ui.scriptmenu.system.Script;
import me.slowly.client.ui.scriptmenu.system.ScriptManager;
import me.slowly.client.ui.scriptmenu.system.object.ScriptMod;
import me.slowly.client.ui.scriptmenu.system.object.ScriptValue;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class UIScriptMenu {
   private float width = 480.0F;
   private float height = 250.0F;
   private UIElementComboListScript scriptList = new UIElementComboListScript();
   private UIElementComboList downloadList = new UIElementComboList();
   private UIElementButton addButton = new UIElementButton("Add");
   private UIElementButton removeButton = new UIElementButton(new ResourceLocation("slowly/icon/garbage.png"));
   private UIElementButton moveUpButton = new UIElementButton(new ResourceLocation("slowly/icon/up-arrow.png"));
   private UIElementButton moveDownButton = new UIElementButton(new ResourceLocation("slowly/icon/down-arrow.png"));
   private UIElementButton ignoreAllButton = new UIElementButton("Ignore All");
   private UIElementButton unignoreAllButton = new UIElementButton("Unignore All");
   private UIElementButton applyButton = new UIElementButton("Apply");
   private UIElementButton loadScriptButton = new UIElementButton("Load");
   private UIElementButton exportButton = new UIElementButton("Export");
   private UIElementButton saveButton = new UIElementButton("Save");
   private UIElementButton changeNameButton = new UIElementButton("Change Name");
   private UIElementButton downloadButton = new UIElementButton(new ResourceLocation("slowly/icon/download-button.png"));
   private UIElementComboBox scriptModeComboBox;
   private ScriptManager scriptManager;
   private UIElementTreeViewScript treeView;
   private UIElementTextField textField = new UIElementTextField();
   private JFrame mainFrame;
   private UIElementDownloadManager downloadManager;
   private String changeName;

   public UIScriptMenu() {
      ArrayList scriptModes = new ArrayList();
      scriptModes.add("Module Settings");
      scriptModes.add("TabGui Theme");
      scriptModes.add("HUD Theme");
      this.scriptModeComboBox = new UIElementComboBox(scriptModes);
      this.scriptManager = new ScriptManager();
      this.treeView = new UIElementTreeViewScript((Script)null);
      this.mainFrame = new JFrame("Slowly Client - b1.2.3");
      this.downloadManager = new UIElementDownloadManager();
      this.loadScripts();

      try {
         this.loadDownloadScripts();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private void loadDownloadScripts() throws IOException {
      String url = "http://alphaantileak.cn/scripts/";
      URL oracle = new URL(url);
      BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
      this.downloadList.getOptions().clear();

      String inputLine;
      while((inputLine = in.readLine()) != null) {
         if (inputLine.length() > 0) {
            this.downloadList.add(inputLine);
         }
      }

      in.close();
   }

   private void loadScripts() {
      Minecraft mc = Minecraft.getMinecraft();
      String fileDir = mc.mcDataDir.getAbsolutePath() + "/" + "Slowly" + "/scripts";
      File fileFolder = new File(fileDir);
      File[] files = fileFolder.listFiles();
      if (files != null) {
         ArrayList names = new ArrayList();
         int count = 0;

         for(int i = 0; i < files.length; ++i) {
            if (files[i].getAbsolutePath().endsWith(".slowlyscript")) {
               ++count;

               try {
                  Script sc = this.scriptManager.createScript(files[i].getAbsolutePath());
                  if (names.contains(sc.getName())) {
                     sc.setName(sc.getName() + " (" + count + ")");
                  }

                  names.add(sc.getName());
                  this.scriptList.add(sc);
               } catch (IOException var9) {
                  var9.printStackTrace();
               }
            }
         }

      }
   }

   public void draw(int mouseX, int mouseY) {
	      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
	      float x1 = ((float)res.getScaledWidth() - this.width) / 2.0F;
	      float x2 = x1 + this.width;
	      float y1 = ((float)res.getScaledHeight() - this.height) / 2.0F;
	      float y2 = y1 + this.height;
	      float minWidth = 400.0F;
	      float minHeight = 230.0F;
	      float maxWidth = 600.0F;
	      float maxHeight = 330.0F;
	      RenderUtil.drawRect(x1, y1, x2, y2, Colors.DARKGREY.c);
	      int newY1 = (int)(y1 + this.height / 6.0F);
	      Gui.drawRect(x1 + this.width / 5.0F, (float)newY1, x1 + this.width / 5.0F + 0.35F, y2 - 20.0F, Colors.GREY.c);
	      Gui.drawRect(x1 + this.width / 5.0F, (float)newY1, x1 + this.width / 5.0F + 0.35F, y2 - 20.0F, ClientUtil.reAlpha(Colors.DARKGREY.c, 0.5F));
	      Gui.drawRect(x2 - this.width / 2.0F, (float)newY1, x2 - this.width / 2.0F + 0.35F, y2 - 20.0F, Colors.GREY.c);
	      Gui.drawRect(x2 - this.width / 2.0F, (float)newY1, x2 - this.width / 2.0F + 0.35F, y2 - 20.0F, ClientUtil.reAlpha(Colors.DARKGREY.c, 0.5F));
	      Gui.drawRect(x2 - 10.0F, (float)newY1, x2 - 10.0F - 0.35F, y2 - 20.0F, Colors.GREY.c);
	      Gui.drawRect(x2 - 10.0F, (float)newY1, x2 - 10.0F - 0.35F, y2 - 20.0F, ClientUtil.reAlpha(Colors.DARKGREY.c, 0.5F));
	      Gui.drawRect(x2 - this.width / 2.0F, (float)newY1, x2 - 10.0F, (float)(newY1 + 20), Colors.DARKGREY.c);
	      Gui.drawRect(x2 - this.width / 2.0F, (float)newY1, x2 - 10.0F, (float)(newY1 + 20), ClientUtil.reAlpha(Colors.WHITE.c, 0.1F));
	      Gui.drawRect(x2 - this.width / 2.0F, y2 - 30.0F, x2 - 10.0F, y2 - 20.0F, Colors.DARKGREY.c);
	      Gui.drawRect(x2 - this.width / 2.0F, y2 - 30.0F, x2 - 10.0F, y2 - 20.0F, ClientUtil.reAlpha(Colors.WHITE.c, 0.1F));
	      int color = -3092272;
	      UnicodeFontRenderer font = Client.getInstance().getFontManager().consolas14;
	      font.drawString("Download: ", x1 + 5.0F, (float)newY1, color);
	      font.drawString("Scripts: ", x1 + this.width / 5.0F + 10.0F, (float)newY1, color);
	      font.drawString("Properties: ", x2 - this.width / 2.0F, (float)(newY1 - 10), color);
	      UnicodeFontRenderer fontBold = Client.getInstance().getFontManager().consolasbold14;
	      if (this.scriptList.getSelected() != null) {
	         fontBold.drawString(this.scriptList.getSelected().getScriptMode(), x2 - this.width / 2.0F + 2.0F, (float)(newY1 + 2), -2894893);
	      }

	      this.changeNameButton.disabled = this.applyButton.disabled = this.saveButton.disabled = this.loadScriptButton.disabled = this.exportButton.disabled = this.ignoreAllButton.disabled = this.unignoreAllButton.disabled = this.scriptList.getSelected() == null;
	      this.treeView.setScript(this.scriptList.getSelected());
	      this.removeButton.disabled = !this.scriptList.canDelete();
	      this.moveDownButton.disabled = !this.scriptList.canMoveDown();
	      this.moveUpButton.disabled = !this.scriptList.canMoveUp();
	      if (this.addButton.clicked() && this.scriptList.getOptions().size() < 15) {
	         this.scriptList.add(new Script("Script " + (this.scriptList.getOptions().size() + 1), this.scriptManager.getCurrentValues(false), this.scriptModeComboBox.getSelected()));
	      }

	      String url;
	      if (this.removeButton.clicked()) {
	         Minecraft mc = Minecraft.getMinecraft();
	         url = mc.mcDataDir.getAbsolutePath() + "/" + "Slowly" + "/scripts";
	         File f = new File(url + "/" + this.scriptList.getSelected().getName() + ".slowlyscript");
	         if (f.exists()) {
	            f.delete();
	         }

	         this.scriptList.deleteSelected();
	      }

	      if (this.moveUpButton.clicked()) {
	         this.scriptList.moveSelectedUp();
	      }

	      if (this.moveDownButton.clicked()) {
	         this.scriptList.moveSelectedDown();
	      }

	      if (this.ignoreAllButton.clicked()) {
	         this.ignoreAll(false);
	      }

	      if (this.unignoreAllButton.clicked()) {
	         this.ignoreAll(true);
	      }

	      if (this.applyButton.clicked()) {
	         this.scriptManager.loadValues(this.scriptList.getSelected());
	      }

	      Runnable run;
	      if (this.exportButton.clicked()) {
	         this.scriptManager.exportValues(System.getProperty("user.home") + "/Desktop", this.scriptList.getSelected());
	         run = () -> {
	            JOptionPane.showMessageDialog(this.mainFrame, "Saved \"" + this.scriptList.getSelected().getName() + "\" to your Desktop!");
	         };
	         (new Thread(run)).start();
	      }

	      if (this.saveButton.clicked()) {
	         this.scriptManager.saveValues(this.scriptList.getSelected());
	      }

	      if (this.loadScriptButton.clicked()) {
	         run = () -> {
	            JFileChooser chooser = new JFileChooser();
	            FileNameExtensionFilter filter = new FileNameExtensionFilter("Slowly Script", new String[]{"slowlyscript"});
	            chooser.setFileFilter(filter);
	            int returnVal = chooser.showOpenDialog(this.mainFrame);
	            if (returnVal == 0) {
	               Script sc = this.loadScript(chooser.getSelectedFile().getAbsolutePath());
	               if (sc != null) {
	                  this.scriptManager.saveValues(sc);
	               }
	            }

	         };
	         (new Thread(run)).start();
	      }

	      String newName;
	      String oldName;
	      if (this.downloadButton.clicked() && this.scriptList.getOptions().size() < 14) {
	          String name = this.setName(this.downloadList.getSelected());
	          String url1 = "http://theslowly-dev.bplaced.net/scripts/download/" + this.downloadList.getSelected() + ".slowlyscript";
	          String path = String.valueOf(Minecraft.getMinecraft().mcDataDir.getAbsolutePath()) + "\\" + "Slowly" + "\\scripts\\";
	          File dir = new File(path);
	          if (!dir.exists()) {
	              dir.mkdirs();
	          }

	         Runnable run2 = () -> {
	             try {
	                 this.downloadManager.download(url1, String.valueOf(path) + name + ".slowlyscript");
	                 Script sc = this.scriptManager.createScript(String.valueOf(path) + name + ".slowlyscript");
	                 this.scriptList.add(sc);
	             }
	             catch (IOException e) {
	                 e.printStackTrace();
	             }
	         };
	         new Thread(run2).start();
	     }

	      if (this.changeNameButton.clicked()) {
	         newName = this.textField.getText();
	         boolean change = true;
	         if (!this.textField.getText().equals("")) {
	            Iterator var27 = this.scriptList.getOptions().iterator();

	            while(var27.hasNext()) {
	               Script sc = (Script)var27.next();
	               if (sc != this.scriptList.getSelected() && sc.getName().equalsIgnoreCase(newName)) {
	                  change = false;
	               }
	            }

	            if (change) {
	               oldName = this.scriptList.getSelected().getName();
	               this.scriptList.getSelected().setName(newName);
	               this.scriptManager.saveValues(this.scriptList.getSelected());
	               Minecraft mc = Minecraft.getMinecraft();
	               String fileDir = mc.mcDataDir.getAbsolutePath() + "/" + "Slowly" + "/scripts";
	               File f = new File(fileDir + "/" + oldName + ".slowlyscript");
	               if (f.exists()) {
	                  f.delete();
	               }
	            }
	         }
	      }

	      this.applyButton.draw(x1 + this.width / 2.0F, y2 - 19.5F, mouseX, mouseY);
	      this.saveButton.draw(x1 + this.width / 2.0F + 27.5F, y2 - 19.5F, mouseX, mouseY);
	      this.loadScriptButton.draw(x1 + this.width / 2.0F + 51.0F, y2 - 19.5F, mouseX, mouseY);
	      this.exportButton.draw(x1 + this.width / 2.0F + 74.5F, y2 - 19.5F, mouseX, mouseY);
	      this.ignoreAllButton.draw(x2 - 110.0F + 0.5F, (float)newY1 - 11.5F, mouseX, mouseY);
	      this.unignoreAllButton.draw(x2 - 63.0F, (float)newY1 - 11.5F, mouseX, mouseY);
	      this.addButton.draw(x1 + this.width / 5.0F + 85.5F, (float)(newY1 + 10), mouseX, mouseY);
	      this.changeNameButton.draw((float)((int)x2) - 60.5F, (float)(newY1 + 4), mouseX, mouseY);
	      this.textField.draw((int)x2 - 161, newY1 + 4, mouseX, mouseY);
	      this.downloadList.setWidth(90);
	      this.downloadList.draw(x1 + 5.0F, (float)(newY1 + 25), mouseX, mouseY);
	      this.downloadButton.draw(x1 + 83.0F, (float)(newY1 + 10), mouseX, mouseY);
	      this.downloadManager.draw((int)x1 + 5, newY1 + 10, mouseX, mouseY, 75);
	      this.scriptList.draw(x1 + this.width / 5.0F + 10.0F, (float)(newY1 + 25), mouseX, mouseY);
	      this.removeButton.draw(x1 + this.width / 5.0F + 110.0F, (float)(newY1 + 20), mouseX, mouseY);
	      this.moveUpButton.draw(x1 + this.width / 5.0F + 110.0F, (float)(newY1 + 35), mouseX, mouseY);
	      this.moveDownButton.draw(x1 + this.width / 5.0F + 110.0F, (float)(newY1 + 50), mouseX, mouseY);
	      this.scriptModeComboBox.draw(x1 + this.width / 5.0F + 10.0F, (float)(newY1 + 10), mouseX, mouseY);
	      this.treeView.draw(x2 - this.width / 2.0F + 1.0F, (float)(newY1 + 20 - 1), x2 - 10.0F - (x2 - this.width / 2.0F) - 1.0F, y2 - 30.0F - (float)(newY1 + 20), mouseX, mouseY);
	      this.scriptModeComboBox.drawbox(mouseX, mouseY);
	   }


   private boolean nameExist(String name) {
      Iterator var3 = this.scriptList.getOptions().iterator();

      while(var3.hasNext()) {
         Script sc = (Script)var3.next();
         if (name.equalsIgnoreCase(sc.getName())) {
            return true;
         }
      }

      return false;
   }

   private String setName(String name) {
      this.changeName = name;
      if (this.nameExist(this.changeName)) {
         this.changeName = this.changeName + " New";
      }

      if (this.nameExist(this.changeName)) {
         this.setName(this.changeName);
      }

      return this.changeName;
   }

   private void ignoreAll(boolean ignore) {
      if (this.scriptList.getSelected() != null) {
         Script sc = this.scriptList.getSelected();
         Iterator var4 = sc.getPropertyList().keySet().iterator();

         while(var4.hasNext()) {
            ScriptMod scm = (ScriptMod)var4.next();
            scm.setEditable(ignore);
            Iterator var6 = ((ArrayList)sc.getPropertyList().get(scm)).iterator();

            while(var6.hasNext()) {
               ScriptValue scv = (ScriptValue)var6.next();
               scv.setEditable(ignore);
            }
         }

      }
   }

   public Script loadScript(String path) {
      try {
         Script sc = this.scriptManager.createScript(path);
         this.scriptList.add(sc);
         return sc;
      } catch (IOException var3) {
         return null;
      }
   }

   public void mouseClicked(int mouseX, int mouseY) {
      this.scriptModeComboBox.mouseClick(mouseX, mouseY);
      this.treeView.mouseClicked(mouseX, mouseY);
   }

   public void mouseReleased(int mouseX, int mouseY) {
   }
}
