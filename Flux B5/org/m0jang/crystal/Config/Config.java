package org.m0jang.crystal.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.UI.AltGenerator.AltGeneratorLogin;
import org.m0jang.crystal.UI.AltGenerator.GeneratorType;
import org.m0jang.crystal.UI.AltManager.Alt;
import org.m0jang.crystal.UI.AltManager.GuiAltList;
import org.m0jang.crystal.Values.Value;
import org.m0jang.crystal.Values.ValueManager;

public class Config {
   public static String dir = "Flux";

   public Config(int killswitch) {
      try {
         for(int i = 0; i < killswitch; ++i) {
            this.createNewDir(dir);
         }

         this.createNewDir(dir);
         this.createNewFile("/Mods.txt");
         this.createNewFile("/Friends.txt");
         this.createNewFile("/Keys.txt");
         this.createNewFile("/Config.txt");
         this.createNewFile("/GUI.txt");
         this.createNewFile("/ALTS.txt");
         this.loadMods();
         this.loadBinds();
         this.loadAlts(killswitch);
         this.loadConfig();
         this.loadIdentities();
      } catch (IOException var3) {
         System.out.println("Error loading from file " + dir);
      }

   }

   public void createNewDir(String name) throws IOException {
      File file = new File(name);
      if (!file.exists()) {
         file.mkdirs();
      }

   }

   public void createNewFile(String name) throws IOException {
      File file = new File(String.valueOf(dir) + name);
      System.out.println(file.getAbsolutePath());
      if (!file.exists()) {
         file.createNewFile();
      }

   }

   public void loadConfig() {
      try {
         String line = null;
         FileReader fileReader = new FileReader(String.valueOf(dir) + "/Config.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         while((line = bufferedReader.readLine()) != null) {
            String s = line.substring(0, line.indexOf(":"));
            Iterator var6 = ValueManager.values.iterator();

            while(var6.hasNext()) {
               Value value = (Value)var6.next();
               String val = String.valueOf(value.getValType()) + "-" + value.getName();
               if (s.equalsIgnoreCase(val)) {
                  if (value.getType() == Boolean.TYPE) {
                     value.setBooleanValue(Boolean.parseBoolean(line.substring(line.lastIndexOf(":") + 1)));
                  }

                  if (value.getType() == Float.TYPE) {
                     value.setFloatValue(Float.parseFloat(line.substring(line.lastIndexOf(":") + 1)));
                  }

                  if (value.getType() == Integer.TYPE) {
                     value.setIntValue(Integer.parseInt(line.substring(line.lastIndexOf(":") + 1)));
                  }

                  if (value.getType() == String.class && Arrays.asList(value.getOptions()).contains(line.substring(line.lastIndexOf(":") + 1))) {
                     value.setSelectedOption(line.substring(line.lastIndexOf(":") + 1));
                  }
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void saveConfig() {
      try {
         FileWriter fileWriter = new FileWriter(String.valueOf(dir) + "/Config.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

         for(Iterator var4 = ValueManager.values.iterator(); var4.hasNext(); bufferedWriter.newLine()) {
            Value value = (Value)var4.next();
            if (value.getType() == Boolean.TYPE) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getBooleanValue());
            }

            if (value.getType() == Float.TYPE) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getFloatValue());
            }

            if (value.getType() == Integer.TYPE) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getIntValue());
            }

            if (value.getType() == String.class) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getSelectedOption());
            }
         }

         bufferedWriter.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void loadPresetConfig(String fileName) {
      try {
         String line = null;
         FileReader fileReader = new FileReader(String.valueOf(dir) + "/" + fileName + ".txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         while((line = bufferedReader.readLine()) != null) {
            String s = line.substring(0, line.indexOf(":"));
            Iterator var7 = ValueManager.values.iterator();

            while(var7.hasNext()) {
               Value value = (Value)var7.next();
               String val = String.valueOf(value.getValType()) + "-" + value.getName();
               if (s.equalsIgnoreCase(val)) {
                  if (value.getType() == Boolean.TYPE) {
                     value.setBooleanValue(Boolean.parseBoolean(line.substring(line.lastIndexOf(":") + 1)));
                  }

                  if (value.getType() == Float.TYPE) {
                     value.setFloatValue(Float.parseFloat(line.substring(line.lastIndexOf(":") + 1)));
                  }

                  if (value.getType() == Integer.TYPE) {
                     value.setIntValue(Integer.parseInt(line.substring(line.lastIndexOf(":") + 1)));
                  }

                  if (value.getType() == String.class) {
                     value.setSelectedOption(line.substring(line.lastIndexOf(":") + 1));
                  }
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var9) {
         ;
      }

   }

   public void savePresetConfig(String fileName) {
      try {
         FileWriter fileWriter = new FileWriter(String.valueOf(dir) + "/" + fileName + ".txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

         for(Iterator var5 = ValueManager.values.iterator(); var5.hasNext(); bufferedWriter.newLine()) {
            Value value = (Value)var5.next();
            if (value.getType() == Boolean.TYPE) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getBooleanValue());
            }

            if (value.getType() == Float.TYPE) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getFloatValue());
            }

            if (value.getType() == Integer.TYPE) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getIntValue());
            }

            if (value.getType() == String.class) {
               bufferedWriter.write(String.valueOf(value.getValType()) + "-" + value.getName() + ":" + value.getSelectedOption());
            }
         }

         bufferedWriter.close();
      } catch (Exception var6) {
         ;
      }

   }

   public void saveAlts() {
      try {
         FileWriter fileWriter = new FileWriter(String.valueOf(dir) + "/Alts.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Iterator var4 = GuiAltList.alts.iterator();

         while(var4.hasNext()) {
            Alt alt = (Alt)var4.next();
            bufferedWriter.write(String.valueOf(alt.getEmail() + ":" + alt.getPassword() + ":" + alt.getName() + ":" + alt.isStarred()));
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
      } catch (Exception var5) {
         ;
      }

   }

   public void loadAlts(int killswitch) {
      try {
         String line = null;
         FileReader fileReader = new FileReader(String.valueOf(dir) + "/Alts.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         while((line = bufferedReader.readLine()) != null) {
            if (line.contains("@") && line.split(":")[0].length() >= 4) {
               String name = line.split(":")[2];
               if (name.equals("null")) {
                  name = null;
               }

               if (killswitch == -1) {
                  GuiAltList.alts.add(new Alt(line.split(":")[0], line.split(":")[1], name, line.split(":")[3].equals("true")));
               }
            }
         }

         GuiAltList.sortAlts();
         bufferedReader.close();
         System.out.println("Loaded Alts.");
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   public void loadIdentities() {
      try {
         String line = null;
         FileReader fileReader = new FileReader(String.valueOf(dir) + "/Generator.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         while((line = bufferedReader.readLine()) != null) {
            if (line.contains("#") && line.split(":")[0].length() >= 2) {
               GeneratorType type = GeneratorType.valueOf(line.split("#")[0]);
               if (type != null) {
                  String username = line.split("#")[1].split(":")[0];
                  String password = line.split("#")[1].split(":")[1];
                  AltGeneratorLogin.Idents.put(type, new String[]{username, password});
               }
            }
         }

         GuiAltList.sortAlts();
         bufferedReader.close();
         System.out.println("Loaded Generator(s) Information.");
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public void saveIdentities() {
      try {
         FileWriter fileWriter = new FileWriter(String.valueOf(dir) + "/Generator.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Iterator var4 = AltGeneratorLogin.Idents.keySet().iterator();

         while(var4.hasNext()) {
            GeneratorType type = (GeneratorType)var4.next();
            bufferedWriter.write(String.valueOf(type + "#" + ((String[])AltGeneratorLogin.Idents.get(type))[0] + ":" + ((String[])AltGeneratorLogin.Idents.get(type))[1]));
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
      } catch (Exception var5) {
         ;
      }

   }

   public void loadBinds() {
      try {
         String line = null;
         FileReader fileReader = new FileReader(String.valueOf(dir) + "/Binds.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         while((line = bufferedReader.readLine()) != null) {
            Iterator var5 = Crystal.INSTANCE.getMods().getModList().iterator();

            while(var5.hasNext()) {
               Module mod = (Module)var5.next();
               if (mod.getName().equalsIgnoreCase(line.substring(0, line.indexOf(":")))) {
                  int key = Keyboard.getKeyIndex(line.substring(line.lastIndexOf(":") + 1).toUpperCase());
                  mod.setBind(key);
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var7) {
         ;
      }

   }

   public void saveBinds() {
      try {
         FileWriter fileWriter = new FileWriter(String.valueOf(dir) + "/Binds.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Iterator var4 = Crystal.INSTANCE.getMods().getModList().iterator();

         while(var4.hasNext()) {
            Module mod = (Module)var4.next();
            bufferedWriter.write(String.valueOf(mod.getName()) + ":" + Keyboard.getKeyName(mod.getBind()));
            bufferedWriter.newLine();
         }

         bufferedWriter.close();
      } catch (Exception var5) {
         ;
      }

   }

   public void loadMods() {
      try {
         String line = null;
         FileReader fileReader = new FileReader(String.valueOf(dir) + "/Mods.txt");
         BufferedReader bufferedReader = new BufferedReader(fileReader);

         while((line = bufferedReader.readLine()) != null) {
            Iterator var5 = Crystal.INSTANCE.getMods().getModList().iterator();

            while(var5.hasNext()) {
               Module mod = (Module)var5.next();
               if (mod.getName().equalsIgnoreCase(line)) {
                  try {
                     mod.setEnabled(true);
                  } catch (Exception var7) {
                     ;
                  }
               }
            }
         }

         bufferedReader.close();
      } catch (Exception var8) {
         ;
      }

   }

   public void saveMods() {
      try {
         FileWriter fileWriter = new FileWriter(String.valueOf(dir) + "/Mods.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         Iterator var4 = Crystal.INSTANCE.getMods().getModList().iterator();

         while(var4.hasNext()) {
            Module mod = (Module)var4.next();
            if (mod.isEnabled()) {
               bufferedWriter.write(mod.getName());
               bufferedWriter.newLine();
            }
         }

         bufferedWriter.close();
      } catch (Exception var5) {
         ;
      }

   }
}
