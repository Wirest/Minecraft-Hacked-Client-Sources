/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.setting.ISetting
 */
package delta;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import delta.Class170;
import delta.client.DeltaClient;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.setting.ISetting;

public class Class40
extends Class170 {
    private static boolean _markets(JsonElement jsonElement, ISetting iSetting) {
        return jsonElement.getAsJsonObject().has(iSetting.getDisplayName());
    }

    private void _guinea(IModule iModule) throws IOException {
        if (!this._total(iModule).exists()) {
            this._total(iModule).createNewFile();
        }
    }

    private static void _alleged(JsonElement jsonElement, ISetting iSetting) {
        if (iSetting.isCheck()) {
            iSetting.setCheckValue(jsonElement.getAsJsonObject().get(iSetting.getDisplayName()).getAsBoolean());
        } else if (iSetting.isSlider()) {
            iSetting.setSliderValue(jsonElement.getAsJsonObject().get(iSetting.getDisplayName()).getAsDouble());
        } else if (iSetting.isCombo()) {
            iSetting.setComboValue(jsonElement.getAsJsonObject().get(iSetting.getDisplayName()).getAsString());
        }
    }

    private static void _clubs(List list, JsonElement jsonElement) {
        list.stream().filter(arg_0 -> Class40._markets(jsonElement, arg_0)).forEach(arg_0 -> Class40._alleged(jsonElement, arg_0));
    }

    @Override
    public void _segment() throws IOException {
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            this._guinea(iModule);
            FileWriter fileWriter = new FileWriter(this._total(iModule));
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", iModule.getName());
            jsonObject.addProperty("state", Boolean.valueOf(iModule.isEnabled()));
            jsonObject.addProperty("keybind", (Number)iModule.getKey());
            List list = DeltaClient.instance.managers.settingsManager.getSettingsForModule(iModule);
            int n = 169 - 312 + 303 - 34 + -126;
            if (list != null) {
                for (ISetting iSetting : list) {
                    if (!iSetting.isCheck() && !iSetting.isSlider() && !iSetting.isCombo()) continue;
                    ++n;
                }
            }
            if (n > 0) {
                ISetting iSetting;
                JsonArray jsonArray = new JsonArray();
                iSetting = new JsonObject();
                for (ISetting iSetting2 : list) {
                    if (iSetting2.isCheck()) {
                        iSetting.addProperty(iSetting2.getDisplayName(), Boolean.valueOf(iSetting2.getCheckValue()));
                        continue;
                    }
                    if (iSetting2.isSlider()) {
                        iSetting.addProperty(iSetting2.getDisplayName(), (Number)iSetting2.getSliderValue());
                        continue;
                    }
                    if (!iSetting2.isCombo()) continue;
                    iSetting.addProperty(iSetting2.getDisplayName(), iSetting2.getComboValue());
                }
                jsonArray.add((JsonElement)iSetting);
                jsonObject.add("settings", (JsonElement)jsonArray);
            }
            fileWriter.write(this._tracy().toJson((JsonElement)jsonObject));
            fileWriter.close();
        }
    }

    public Class40(Gson gson, File file) {
        super(gson, file);
    }

    private File _total(IModule iModule) {
        return new File(this._duncan(), iModule.getName() + ".json");
    }

    @Override
    public void _redhead() throws IOException {
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            Object object;
            if (iModule.getName().contains("TestModule") || !this._total(iModule).exists()) continue;
            FileReader fileReader = new FileReader(this._total(iModule));
            JsonObject jsonObject = (JsonObject)this._tracy().fromJson((Reader)fileReader, JsonObject.class);
            if (jsonObject == null) {
                fileReader.close();
                continue;
            }
            if (jsonObject.has("state") && !((String)(object = iModule.getName())).equalsIgnoreCase("Spammer") && !((String)object).equalsIgnoreCase("Freecam") && jsonObject.get("state").getAsBoolean() && !iModule.isEnabled()) {
                iModule.toggle();
            }
            if (jsonObject.has("keybind")) {
                iModule.setKey(jsonObject.get("keybind").getAsInt());
            }
            object = DeltaClient.instance.managers.settingsManager.getSettingsForModule(iModule);
            if (jsonObject.has("settings")) {
                JsonArray jsonArray = (JsonArray)jsonObject.get("settings");
                try {
                    jsonArray.forEach(arg_0 -> Class40._clubs((List)object, arg_0));
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            fileReader.close();
        }
    }
}

