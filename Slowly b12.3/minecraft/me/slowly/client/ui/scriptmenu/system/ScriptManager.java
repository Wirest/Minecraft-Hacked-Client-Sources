package me.slowly.client.ui.scriptmenu.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.scriptmenu.system.object.ScriptMod;
import me.slowly.client.ui.scriptmenu.system.object.ScriptValue;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;

public class ScriptManager {
   public static final String SUFFIX = ".slowlyscript";

   public Map getCurrentValues(boolean allIgnore) {
      Map propertyList = new LinkedHashMap();
      Iterator var4 = ModManager.getModList().iterator();

      label57:
      while(var4.hasNext()) {
         Mod m = (Mod)var4.next();
         ScriptMod scm = new ScriptMod(m);
         scm.setEditable(!allIgnore);
         ArrayList values = new ArrayList();
         Iterator var8 = Value.list.iterator();

         while(true) {
            Value value;
            String valueModName;
            do {
               if (!var8.hasNext()) {
                  if (values.size() > 0) {
                     propertyList.put(scm, values);
                  }
                  continue label57;
               }

               value = (Value)var8.next();
               valueModName = value.getValueName().split("_")[0];
               if (value.isValueMode) {
                  value.getModeTitle();
               } else {
                  String var10000 = value.getValueName().split("_")[1];
               }
            } while(!m.getName().equalsIgnoreCase(valueModName));

            ScriptValue scv = new ScriptValue(scm, value);
            if (value.isValueBoolean || value.isValueMode) {
               int mode = value.isValueBoolean ? (((Boolean)value.getValueState()).booleanValue() ? 0 : 1) : value.getCurrentMode();
               scv.getComboList().selected = mode;
            }

            scv.setEditable(!allIgnore);
            values.add(scv);
         }
      }

      return propertyList;
   }

   public void saveValues(Script script) {
      Minecraft mc = Minecraft.getMinecraft();
      String fileDir = mc.mcDataDir.getAbsolutePath() + "/" + "Slowly" + "/scripts";
      File f = new File(fileDir);
      if (!f.exists()) {
         f.mkdir();
      }

      this.exportValues(fileDir, script);
   }

   public void exportValues(String path, Script script) {
      File f = new File(path + "/" + script.getName() + ".slowlyscript");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var6 = script.getPropertyList().keySet().iterator();

         while(true) {
            ScriptMod scm;
            do {
               if (!var6.hasNext()) {
                  pw.close();
                  return;
               }

               scm = (ScriptMod)var6.next();
            } while(!scm.isEditable());

            Iterator var8 = ((ArrayList)script.getPropertyList().get(scm)).iterator();

            while(var8.hasNext()) {
               ScriptValue scv = (ScriptValue)var8.next();
               if (scv.isEditable()) {
                  String valueName = scv.getValue().isValueMode ? scv.getValue().getModeTitle() : scv.getValue().getValueName().split("_")[1];
                  int mode = scv.getValue().isValueBoolean ? 0 : (scv.getValue().isValueDouble ? 1 : 2);
                  String valueState = mode != 1 ? scv.getComboList().getSelected() : String.valueOf(scv.getNumeric().getCurrent());
                  String modeStr = mode == 0 ? "Boolean" : (mode == 1 ? "Double" : "Mode");
                  pw.write(scm.getMod().getName() + "->");
                  pw.write(modeStr + "Value" + "->" + valueName + "->" + valueState + "\n");
               }
            }
         }
      } catch (Exception var13) {
         var13.printStackTrace();
      }
   }

   private String getScriptNameByPath(String path) {
      path = path.replace("\\", "/");
      String[] split = path.split("/");
      return split[split.length - 1].replace(".slowlyscript", "");
   }

   public Script createScript(String path) throws IOException {
      Script script = new Script(this.getScriptNameByPath(path), this.getCurrentValues(true), "Module Settings");
      if (!path.endsWith(".slowlyscript")) {
         return script;
      } else {
         File f = new File(path);
         if (!f.exists()) {
            f.createNewFile();
            return script;
         } else {
            @SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(f));

            while(true) {
               String[] fields;
               do {
                  String line;
                  if ((line = br.readLine()) == null) {
                     return script;
                  }

                  fields = line.split("->");
               } while(fields.length < 4);

               String modName = fields[0];
               String valueType = fields[1];
               String valueName = fields[2];
               String valueState = fields[3];
               Iterator var12 = script.getPropertyList().keySet().iterator();

               label68:
               while(var12.hasNext()) {
                  ScriptMod scm = (ScriptMod)var12.next();
                  Iterator var14 = ((ArrayList)script.getPropertyList().get(scm)).iterator();

                  while(true) {
                     while(true) {
                        ScriptValue scv;
                        do {
                           do {
                              if (!var14.hasNext()) {
                                 continue label68;
                              }

                              scv = (ScriptValue)var14.next();
                           } while(!scm.getMod().getName().equalsIgnoreCase(modName));
                        } while(!scv.getValue().getDisplayTitle().equalsIgnoreCase(valueName));

                        int mode;
                        if (scv.getValue().isValueBoolean && valueType.equalsIgnoreCase("BooleanValue")) {
                           scv.setEditable(true);
                           mode = Boolean.valueOf(valueState).booleanValue() ? 0 : 1;
                           scv.getComboList().selected = mode;
                        } else if (scv.getValue().isValueDouble && valueType.equalsIgnoreCase("DoubleValue")) {
                           scv.setEditable(true);
                           double state = Double.valueOf(valueState).doubleValue();
                           scv.getNumeric().setCurrent(state);
                        } else if (scv.getValue().isValueMode && valueType.equalsIgnoreCase("ModeValue")) {
                           scv.setEditable(true);
                           mode = scv.getValue().getModeInt(valueState);
                           scv.getComboList().selected = mode;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void loadValues(Script script) {
      Iterator var3 = script.getPropertyList().keySet().iterator();

      while(var3.hasNext()) {
         ScriptMod scm = (ScriptMod)var3.next();
         Iterator var5 = ((ArrayList)script.getPropertyList().get(scm)).iterator();

         while(var5.hasNext()) {
            ScriptValue scv = (ScriptValue)var5.next();
            if (scv.isEditable()) {
               Value val;
               if (scv.getValue().isValueBoolean) {
                  val = Value.getBooleanValueByName(scv.getValue().getValueName());
                  val.setValueState(Boolean.parseBoolean(scv.getComboList().getSelected()));
               } else if (scv.getValue().isValueDouble) {
                  val = Value.getDoubleValueByName(scv.getValue().getValueName());
                  val.setValueState(scv.getNumeric().getCurrent());
               } else if (scv.getValue().isValueMode) {
                  val = Value.getModeValue(scv.getValue().getValueName(), scv.getValue().getModeTitle());
                  val.setCurrentMode(scv.getComboList().selected);
               }
            }
         }
      }

      Client.getInstance().getFileUtil().saveValues();
   }
}
