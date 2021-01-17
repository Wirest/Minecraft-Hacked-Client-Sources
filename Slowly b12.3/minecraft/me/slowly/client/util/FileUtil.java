package me.slowly.client.util;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.render.BlockESP;
import me.slowly.client.ui.clickgui.Komplexe.uis.KomplPanel;
import me.slowly.client.ui.clickgui.newclickgui.uis.Panel;
import me.slowly.client.ui.clickgui.solstice.uis.SolsticePanel;
import me.slowly.client.ui.clickgui.xave.uis.XPanel;
import me.slowly.client.ui.hudcustomizer.CustomHUD;
import me.slowly.client.ui.hudcustomizer.CustomHUDOptions;
import me.slowly.client.ui.hudcustomizer.CustomValue;
import me.slowly.client.ui.hudcustomizer.UIHUDCustomizer;
import me.slowly.client.util.friendmanager.Friend;
import me.slowly.client.util.friendmanager.FriendManager;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FileUtil {
   private Minecraft mc = Minecraft.getMinecraft();
   private String fileDir;

   public FileUtil() {
      this.fileDir = this.mc.mcDataDir.getAbsolutePath() + "/" + "Slowly";
      File fileFolder = new File(this.fileDir);
      if (!fileFolder.exists()) {
         fileFolder.mkdirs();
      }

      try {
         this.loadKeys();
         this.loadValues();
         this.loadMods();
         this.loadBlocks();
         this.loadFriends();
         this.loadGUI();
         this.loadXaveUI();
         this.loadOther();
         this.loadKomp();
         this.loadSolstice();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public void saveSolstice() {
      try {
         File f = new File(this.fileDir + "/solgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            PrintWriter writer = new PrintWriter(f);
            Iterator var4 = Client.getInstance().getSolstice().getPanels().iterator();

            while(var4.hasNext()) {
               SolsticePanel p = (SolsticePanel)var4.next();
               writer.write(String.valueOf(p.getCat().name()) + ":" + p.getX() + ":" + p.getY() + ":" + p.isOpen() + "\n");
            }

            writer.flush();
            writer.close();
         }
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void loadSolstice() {
      try {
         File f = new File(String.valueOf(this.fileDir) + "/solgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null) {
               String[] splitted = line.split(":");
               Iterator var6 = Client.getInstance().getSolstice().getPanels().iterator();

               while(var6.hasNext()) {
                  SolsticePanel p = (SolsticePanel)var6.next();
                  if (p.getCat().name().equals(splitted[0]) && splitted.length >= 3) {
                     int x = Integer.parseInt(splitted[1]);
                     int y = Integer.parseInt(splitted[2]);
                     boolean open = Boolean.parseBoolean(splitted[3]);
                     p.setX(x);
                     p.setY(y);
                     p.setOpen(open);
                  }
               }
            }

            reader.close();
         }
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void saveKompl() {
      try {
         File f = new File(this.fileDir + "/kgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            PrintWriter writer = new PrintWriter(f);
            Iterator var4 = Client.getInstance().getKomplexe().getPanels().iterator();

            while(var4.hasNext()) {
               KomplPanel p = (KomplPanel)var4.next();
               writer.write(String.valueOf(p.getCat().name()) + ":" + p.getX() + ":" + p.getY() + ":" + p.isOpen() + "\n");
            }

            writer.flush();
            writer.close();
         }
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void loadKomp() {
      try {
         File f = new File(String.valueOf(this.fileDir) + "/kgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null) {
               String[] splitted = line.split(":");
               Iterator var6 = Client.getInstance().getKomplexe().getPanels().iterator();

               while(var6.hasNext()) {
                  KomplPanel p = (KomplPanel)var6.next();
                  if (p.getCat().name().equals(splitted[0]) && splitted.length >= 3) {
                     int x = Integer.parseInt(splitted[1]);
                     int y = Integer.parseInt(splitted[2]);
                     boolean open = Boolean.parseBoolean(splitted[3]);
                     p.setX(x);
                     p.setY(y);
                     p.setOpen(open);
                  }
               }
            }

            reader.close();
         }
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void saveGUI() {
      try {
         File f = new File(this.fileDir + "/sgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            PrintWriter writer = new PrintWriter(f);
            Iterator var4 = Client.getInstance().getUIClick().getPanels().iterator();

            while(var4.hasNext()) {
               Panel p = (Panel)var4.next();
               writer.write(String.valueOf(p.getCat().name()) + ":" + p.getX() + ":" + p.getY() + ":" + p.isOpen() + "\n");
            }

            writer.flush();
            writer.close();
         }
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void loadGUI() {
      try {
         File f = new File(String.valueOf(this.fileDir) + "/sgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null) {
               String[] splitted = line.split(":");
               Iterator var6 = Client.getInstance().getUIClick().getPanels().iterator();

               while(var6.hasNext()) {
                  Panel p = (Panel)var6.next();
                  if (p.getCat().name().equals(splitted[0]) && splitted.length >= 3) {
                     int x = Integer.parseInt(splitted[1]);
                     int y = Integer.parseInt(splitted[2]);
                     boolean open = Boolean.parseBoolean(splitted[3]);
                     p.setX(x);
                     p.setY(y);
                     p.setOpen(open);
                  }
               }
            }

            reader.close();
         }
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void saveOther() {
      try {
         File f = new File(this.fileDir + "/ogui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            PrintWriter writer = new PrintWriter(f);
            Iterator var4 = Client.getInstance().getUIOther().getPanels().iterator();

            while(var4.hasNext()) {
               me.slowly.client.ui.clickgui.other.screens.Panel p = (me.slowly.client.ui.clickgui.other.screens.Panel)var4.next();
               writer.write(String.valueOf(p.getCat().name()) + ":" + p.getX() + ":" + p.getY() + ":" + p.isOpen() + "\n");
            }

            writer.flush();
            writer.close();
         }
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void loadOther() {
      try {
         File f = new File(String.valueOf(this.fileDir) + "/ogui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null) {
               String[] splitted = line.split(":");
               Iterator var6 = Client.getInstance().getUIOther().getPanels().iterator();

               while(var6.hasNext()) {
                  me.slowly.client.ui.clickgui.other.screens.Panel p = (me.slowly.client.ui.clickgui.other.screens.Panel)var6.next();
                  if (p.getCat().name().equals(splitted[0]) && splitted.length >= 3) {
                     int x = Integer.parseInt(splitted[1]);
                     int y = Integer.parseInt(splitted[2]);
                     boolean open = Boolean.parseBoolean(splitted[3]);
                     p.setX(x);
                     p.setY(y);
                     p.setOpen(open);
                  }
               }
            }

            reader.close();
         }
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void saveXaveUI() {
      try {
         File f = new File(this.fileDir + "/xgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            PrintWriter writer = new PrintWriter(f);
            Iterator var4 = Client.getInstance().getXave().getPanels().iterator();

            while(var4.hasNext()) {
               XPanel p = (XPanel)var4.next();
               writer.write(String.valueOf(p.getCat().name()) + ":" + p.getX() + ":" + p.getY() + ":" + p.isOpen() + "\n");
            }

            writer.flush();
            writer.close();
         }
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void loadXaveUI() {
      try {
         File f = new File(String.valueOf(this.fileDir) + "/xgui.txt");
         if (!f.exists()) {
            f.createNewFile();
         } else {
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line;
            while((line = reader.readLine()) != null) {
               String[] splitted = line.split(":");
               Iterator var6 = Client.getInstance().getXave().getPanels().iterator();

               while(var6.hasNext()) {
                  XPanel p = (XPanel)var6.next();
                  if (p.getCat().name().equals(splitted[0]) && splitted.length >= 3) {
                     int x = Integer.parseInt(splitted[1]);
                     int y = Integer.parseInt(splitted[2]);
                     boolean open = Boolean.parseBoolean(splitted[3]);
                     p.setX(x);
                     p.setY(y);
                     p.setOpen(open);
                  }
               }
            }

            reader.close();
         }
      } catch (FileNotFoundException var10) {
         var10.printStackTrace();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public void saveBlocks() {
      File f = new File(this.fileDir + "/blocks.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = BlockESP.getBlockIds().iterator();

         while(var4.hasNext()) {
            int id = ((Integer)var4.next()).intValue();
            pw.print(String.valueOf(id) + "\n");
         }

         pw.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void loadBlocks() throws IOException {
      File f = new File(this.fileDir + "/blocks.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               int id = Integer.valueOf(line).intValue();
               BlockESP.getBlockIds().add(id);
            } catch (Exception var5) {
               ;
            }
         }
      }

   }

   public void saveFriends() {
      File f = new File(this.fileDir + "/friends.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = FriendManager.getFriends().iterator();

         while(var4.hasNext()) {
            Friend friend = (Friend)var4.next();
            pw.print(friend.getName() + ":" + friend.getAlias() + "\n");
         }

         pw.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void loadFriends() throws IOException {
      File f = new File(this.fileDir + "/friends.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            if (line.contains(":")) {
               String[] split = line.split(":");
               if (line.length() >= 2) {
                  Friend friend = new Friend(split[0], split[1]);
                  FriendManager.getFriends().add(friend);
               }
            }
         }
      }

   }

   public void saveKeys() {
      File f = new File(this.fileDir + "/keys.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = ModManager.getModList().iterator();

         while(var4.hasNext()) {
            Mod m = (Mod)var4.next();
            String keyName = m.getKey() < 0 ? "None" : Keyboard.getKeyName(m.getKey());
            pw.write(m.getName() + ":" + keyName + "\n");
         }

         pw.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void loadKeys() throws IOException {
      File f = new File(this.fileDir + "/keys.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            if (line.contains(":")) {
               String[] split = line.split(":");
               Mod m = ModManager.getModByName(split[0]);
               int key = Keyboard.getKeyIndex(split[1]);
               if (m != null && key != -1) {
                  m.setKey(key);
               }
            }
         }
      }

   }

   public void saveValues() {
      File f = new File(this.fileDir + "/values.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = Value.list.iterator();

         while(var4.hasNext()) {
            Value value = (Value)var4.next();
            String valueName = value.getValueName();
            if (value.isValueBoolean) {
               pw.print(valueName + ":b:" + value.getValueState() + "\n");
            } else if (value.isValueDouble) {
               pw.print(valueName + ":d:" + value.getValueState() + "\n");
            } else if (value.isValueMode) {
               pw.print(valueName + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
         }

         pw.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void loadValues() throws IOException {
      File f = new File(this.fileDir + "/values.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         label51:
         while(true) {
            String line;
            do {
               if ((line = br.readLine()) == null) {
                  return;
               }
            } while(!line.contains(":"));

            String[] split = line.split(":");
            Iterator var6 = Value.list.iterator();

            while(true) {
               while(true) {
                  Value value;
                  do {
                     if (!var6.hasNext()) {
                        continue label51;
                     }

                     value = (Value)var6.next();
                  } while(!split[0].equalsIgnoreCase(value.getValueName()));

                  if (value.isValueBoolean && split[1].equalsIgnoreCase("b")) {
                     value.setValueState(Boolean.parseBoolean(split[2]));
                  } else if (value.isValueDouble && split[1].equalsIgnoreCase("d")) {
                     value.setValueState(Double.parseDouble(split[2]));
                  } else if (value.isValueMode && split[1].equalsIgnoreCase("s") && split[2].equalsIgnoreCase(value.getModeTitle())) {
                     value.setCurrentMode(Integer.parseInt(split[3]));
                  }
               }
            }
         }
      }
   }

   public void saveMods() {
      File f = new File(this.fileDir + "/mods.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = ModManager.getModList().iterator();

         while(var4.hasNext()) {
            Mod m = (Mod)var4.next();
            pw.print(m.getName() + ":" + m.isEnabled() + "\n");
         }

         pw.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void loadMods() throws IOException {
      File f = new File(this.fileDir + "/mods.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            if (line.contains(":")) {
               String[] split = line.split(":");
               Mod m = ModManager.getModByName(split[0]);
               boolean state = Boolean.parseBoolean(split[1]);
               if (m != null) {
                  m.set(state, false);
               }
            }
         }
      }

   }

   public void saveCustomValues() {
      File f = new File(this.fileDir + "/customvalues.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = UIHUDCustomizer.customs.iterator();

         while(var4.hasNext()) {
            CustomHUD custom = (CustomHUD)var4.next();
            Iterator var6 = custom.options.iterator();

            while(var6.hasNext()) {
               CustomHUDOptions option = (CustomHUDOptions)var6.next();
               Iterator var8 = option.values.iterator();

               while(var8.hasNext()) {
                  CustomValue val = (CustomValue)var8.next();
                  pw.print(custom.owner + "->" + option.name + "->" + val.getValueName() + "->" + (val.isValueMode ? val.getCurrentMode() : (val.isValueColor ? ((Color)val.getValueState()).getRGB() : val.getValueState())) + "\n");
               }
            }
         }

         pw.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void loadCustomValues() throws IOException {
      File f = new File(this.fileDir + "/customvalues.txt");
      if (!f.exists()) {
         f.createNewFile();
         InputStream in = this.getClass().getResourceAsStream("customvalues.txt");
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         label119:
         while(true) {
            String line;
            do {
               if ((line = br.readLine()) == null) {
                  this.saveCustomValues();
                  return;
               }
            } while(!line.contains("->"));

            String[] split = line.split("->");
            Iterator var7 = UIHUDCustomizer.customs.iterator();

            label117:
            while(true) {
               CustomHUD custom;
               do {
                  if (!var7.hasNext()) {
                     continue label119;
                  }

                  custom = (CustomHUD)var7.next();
               } while(!split[0].equalsIgnoreCase(custom.owner));

               Iterator var9 = custom.options.iterator();

               while(true) {
                  CustomHUDOptions option;
                  do {
                     if (!var9.hasNext()) {
                        continue label117;
                     }

                     option = (CustomHUDOptions)var9.next();
                  } while(!split[1].equalsIgnoreCase(option.name));

                  Iterator var11 = option.values.iterator();

                  while(var11.hasNext()) {
                     CustomValue val = (CustomValue)var11.next();
                     if (split[2].equalsIgnoreCase(val.getValueName())) {
                        if (val.isValueMode) {
                           val.setCurrentMode(Integer.parseInt(split[3]));
                        } else if (val.isValueBoolean) {
                           val.setValueState(Boolean.parseBoolean(split[3]));
                        } else if (val.isValueDouble) {
                           val.setValueState(Double.parseDouble(split[3]));
                        } else if (val.isValueColor) {
                           val.setValueState(new Color(Integer.parseInt(split[3])));
                        }
                     }
                  }
               }
            }
         }
      } else {
         BufferedReader br = new BufferedReader(new FileReader(f));

         label87:
         while(true) {
            String line;
            do {
               if ((line = br.readLine()) == null) {
                  return;
               }
            } while(!line.contains("->"));

            String[] split = line.split("->");
            Iterator var16 = UIHUDCustomizer.customs.iterator();

            label85:
            while(true) {
               CustomHUD custom;
               do {
                  if (!var16.hasNext()) {
                     continue label87;
                  }

                  custom = (CustomHUD)var16.next();
               } while(!split[0].equalsIgnoreCase(custom.owner));

               Iterator var18 = custom.options.iterator();

               while(true) {
                  CustomHUDOptions option;
                  do {
                     if (!var18.hasNext()) {
                        continue label85;
                     }

                     option = (CustomHUDOptions)var18.next();
                  } while(!split[1].equalsIgnoreCase(option.name));

                  Iterator var20 = option.values.iterator();

                  while(var20.hasNext()) {
                     CustomValue val = (CustomValue)var20.next();
                     if (split[2].equalsIgnoreCase(val.getValueName())) {
                        if (val.isValueMode) {
                           val.setCurrentMode(Integer.parseInt(split[3]));
                        } else if (val.isValueBoolean) {
                           val.setValueState(Boolean.parseBoolean(split[3]));
                        } else if (val.isValueDouble) {
                           val.setValueState(Double.parseDouble(split[3]));
                        } else if (val.isValueColor) {
                           val.setValueState(new Color(Integer.parseInt(split[3])));
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
