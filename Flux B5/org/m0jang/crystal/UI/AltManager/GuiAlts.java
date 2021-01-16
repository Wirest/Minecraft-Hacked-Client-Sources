package org.m0jang.crystal.UI.AltManager;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.UI.GUIMCLeaks;
import org.m0jang.crystal.UI.AltGenerator.AltGeneratorLogin;
import org.m0jang.crystal.UI.AltGenerator.Generate;
import org.m0jang.crystal.UI.AltGenerator.GeneratorType;
import org.m0jang.crystal.Utils.LoginUtils;

public class GuiAlts extends GuiScreen {
   private GuiScreen prevMenu;
   public static GuiAltList altList;

   public GuiAlts(GuiScreen par1GuiScreen) {
      this.prevMenu = par1GuiScreen;
   }

   public void initGui() {
      altList = new GuiAltList(this.mc, this);
      altList.registerScrollButtons(7, 8);
      altList.elementClicked(0, false, 0, 0);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 154, this.height - 52, 100, 20, "Use"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height - 52, 100, 20, "Direct Login"));
      this.buttonList.add(new GuiButton(2, this.width / 2 + 54, this.height - 52, 100, 20, "Add Alt"));
      this.buttonList.add(new GuiButton(3, this.width / 2 - 154, this.height - 28, 57, 20, "Star"));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 91, this.height - 28, 57, 20, "Gen"));
      this.buttonList.add(new GuiButton(5, this.width / 2 - 28, this.height - 28, 57, 20, "Edit"));
      this.buttonList.add(new GuiButton(6, this.width / 2 + 35, this.height - 28, 57, 20, "Delete"));
      this.buttonList.add(new GuiButton(7, this.width / 2 + 97, this.height - 28, 57, 20, "Cancel"));
      this.buttonList.add(new GuiButton(8, 8, 8, 100, 20, "Import Alts"));
      this.buttonList.add(new GuiButton(9, this.width - 108, 8, 100, 20, "MCLeaks"));
   }

   public void updateScreen() {
      ((GuiButton)this.buttonList.get(0)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
      ((GuiButton)this.buttonList.get(3)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
      ((GuiButton)this.buttonList.get(4)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
      ((GuiButton)this.buttonList.get(5)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
   }

   protected void actionPerformed(GuiButton clickedButton) {
      if (clickedButton.enabled) {
         Alt alt;
         String deleteQuestion;
         if (clickedButton.id == 0) {
            alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
            if (alt.isCracked()) {
               LoginUtils.changeCrackedName(alt.getEmail());
               this.mc.displayGuiScreen(this.prevMenu);
            } else {
               deleteQuestion = LoginUtils.login(alt.getEmail(), alt.getPassword());
               System.out.println("Result: " + deleteQuestion);
               if (deleteQuestion == null) {
                  this.mc.displayGuiScreen(this.prevMenu);
                  alt.setChecked(this.mc.session.getUsername());
                  Crystal.INSTANCE.getConfig().saveAlts();
               }
            }
         } else if (clickedButton.id == 1) {
            this.mc.displayGuiScreen(new GuiAltLogin(this));
         } else if (clickedButton.id == 4) {
            if (AltGeneratorLogin.Idents.containsKey(GeneratorType.FASTALTS)) {
               ((GuiButton)this.buttonList.get(2)).enabled = false;
               (new Thread(new Runnable() {
                  public void run() {
                     try {
                        String[] alt = Generate.GetAltFromFastAlts((String[])AltGeneratorLogin.Idents.get(GeneratorType.FASTALTS));
                        if (alt == null) {
                           GuiAlts.this.mc.displayGuiScreen(new AltGeneratorLogin(GuiAlts.this));
                        } else {
                           String reply = LoginUtils.login(alt[0], alt[1]);
                           System.out.println("Result: " + reply);
                           if (reply == null) {
                              Crystal.INSTANCE.getConfig().saveAlts();
                           }
                        }
                     } catch (Exception var3) {
                        ;
                     }

                     ((GuiButton)GuiAlts.this.buttonList.get(2)).enabled = true;
                  }
               })).start();
            } else {
               this.mc.displayGuiScreen(new AltGeneratorLogin(this));
            }
         } else if (clickedButton.id == 3) {
            alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
            alt.setStarred(!alt.isStarred());
            GuiAltList.sortAlts();
            Crystal.INSTANCE.getConfig().saveAlts();
         } else if (clickedButton.id == 2) {
            this.mc.displayGuiScreen(new GuiAltAdd(this));
         } else if (clickedButton.id == 5) {
            alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
            this.mc.displayGuiScreen(new GuiAltEdit(this, alt));
         } else if (clickedButton.id == 6) {
            alt = (Alt)GuiAltList.alts.get(altList.getSelectedSlot());
            deleteQuestion = "Are you sure you want to remove this alt?";
            String deleteWarning = "\"" + alt.getNameOrEmail() + "\" will be lost forever.";
            this.mc.displayGuiScreen(new GuiYesNo(this, deleteQuestion, deleteWarning, "Delete", "Cancel", 1));
         } else if (clickedButton.id == 7) {
            this.mc.displayGuiScreen(this.prevMenu);
         } else if (clickedButton.id == 8) {
            (new Thread(new Runnable() {
               public void run() {
                  JFileChooser fileChooser = new JFileChooser() {
                     protected JDialog createDialog(Component parent) throws HeadlessException {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setAlwaysOnTop(true);
                        return dialog;
                     }
                  };
                  fileChooser.setFileSelectionMode(0);
                  fileChooser.setAcceptAllFileFilterUsed(false);
                  fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Username:Password format (TXT)", new String[]{"txt"}));
                  int action = fileChooser.showOpenDialog(FrameHook.getFrame());
                  if (action == 0) {
                     try {
                        File file = fileChooser.getSelectedFile();
                        BufferedReader load = new BufferedReader(new FileReader(file));
                        String line = "";

                        while((line = load.readLine()) != null) {
                           String[] data = line.split(":");
                           if (data.length == 2) {
                              GuiAltList.alts.add(new Alt(data[0], data[1], (String)null));
                           }
                        }

                        load.close();
                        GuiAltList.sortAlts();
                        Crystal.INSTANCE.getConfig().saveAlts();
                     } catch (IOException var7) {
                        var7.printStackTrace();
                     }
                  }

               }
            })).start();
         } else if (clickedButton.id == 9) {
            this.mc.displayGuiScreen(new GUIMCLeaks(this));
         }
      }

   }

   public void confirmClicked(boolean par1, int par2) {
      if (par2 == 1 && par1) {
         GuiAltList.alts.remove(altList.getSelectedSlot());
         GuiAltList.sortAlts();
         Crystal.INSTANCE.getConfig().saveAlts();
      }

      this.mc.displayGuiScreen(this);
   }

   protected void keyTyped(char par1, int par2) {
      if (par2 == 28 || par2 == 156) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

   }

   protected void mouseClicked(int par1, int par2, int par3) throws IOException {
      if (par2 >= 36 && par2 <= this.height - 57 && (par1 >= this.width / 2 + 140 || par1 <= this.width / 2 - 126)) {
         altList.elementClicked(-1, false, 0, 0);
      }

      super.mouseClicked(par1, par2, par3);
   }

   public void drawScreen(int par1, int par2, float par3) {
      this.drawDefaultBackground();
      altList.drawScreen(par1, par2, par3);
      this.drawCenteredString(this.fontRendererObj, "Current name: \247a" + Minecraft.getMinecraft().getSession().getUsername(), this.width / 2, 4, 16777215);
      this.drawCenteredString(this.fontRendererObj, "Alts: " + GuiAltList.alts.size(), this.width / 2, 14, 10526880);
      this.drawCenteredString(this.fontRendererObj, "Premium: " + GuiAltList.premiumAlts + ", Cracked: " + GuiAltList.crackedAlts, this.width / 2, 24, 10526880);
      super.drawScreen(par1, par2, par3);
   }
}
