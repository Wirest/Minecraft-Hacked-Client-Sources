package modification.commands;

import modification.enummerates.Category;
import modification.extenders.Command;
import modification.extenders.Module;
import modification.extenders.Value;
import modification.files.ModuleFile;
import modification.files.ValueFile;
import modification.main.Modification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public final class Config
        extends Command {
    public Config(String paramString1, String paramString2) {
        super(paramString1, paramString2);
    }

    public void execute(String paramString, String[] paramArrayOfString) {
        Object localObject;
        if (paramArrayOfString.length == 3) {
            if (paramArrayOfString[1].toLowerCase().equals("load")) {
                if (paramArrayOfString[2].isEmpty()) {
                    Modification.LOG_UTIL.sendChatMessage("§4I'm sorry, but you are too silly to use this command...");
                    return;
                }
                int i = 0;
                try {
                    localObject = new BufferedReader(new InputStreamReader(new URL("https://clientshop.eu/config/".concat(paramArrayOfString[2].toLowerCase()).concat(".html")).openStream()));
                    String str;
                    while ((str = ((BufferedReader) localObject).readLine()) != null) {
                        String[] arrayOfString = str.split("~");
                        switch (arrayOfString.length) {
                            case 2:
                                Module localModule = Modification.MODULE_MANAGER.checkModuleForName(arrayOfString[0]);
                                if ((localModule != null) && (localModule.category != Category.VISUALS) && (localModule.category != Category.MISC)) {
                                    localModule.enabled = Boolean.parseBoolean(arrayOfString[1]);
                                }
                                break;
                            case 3:
                                Value localValue = Modification.VALUE_MANAGER.checkValueForName(arrayOfString[1]);
                                if ((localValue != null) && (localValue.module.category != Category.VISUALS) && (localValue.module.category != Category.MISC)) {
                                    switch (localValue.mode) {
                                        case 0:
                                            localValue.value = Boolean.valueOf(Boolean.parseBoolean(arrayOfString[2]));
                                            break;
                                        case 1:
                                            localValue.value = Float.valueOf(Float.parseFloat(arrayOfString[2]));
                                            break;
                                        case 2:
                                            localValue.value = arrayOfString[2];
                                    }
                                }
                                break;
                        }
                        i = 1;
                    }
                    ((BufferedReader) localObject).close();
                } catch (MalformedURLException localMalformedURLException) {
                    Modification.LOG_UTIL.sendChatMessage("§4Error: Couldn't connect to config servers");
                } catch (IOException localIOException) {
                    Modification.LOG_UTIL.sendChatMessage("§4Error: This config does not exist");
                }
                if (i != 0) {
                    Modification.FILE_MANAGER.update(ModuleFile.class);
                    Modification.FILE_MANAGER.update(ValueFile.class);
                    Modification.LOG_UTIL.sendChatMessage("Loaded online config §8\"§f".concat(paramArrayOfString[2].toLowerCase()).concat("§8\" §7successfully"));
                }
            }
            return;
        }
        if (paramArrayOfString.length == 2) {
            if (paramArrayOfString[1].toLowerCase().equals("list")) {
                localObject = Modification.LOG_UTIL.readFromWebsite("https://clientshop.eu/config/online.html", true);
                if (((String) localObject).isEmpty()) {
                    Modification.LOG_UTIL.sendChatMessage("§4There are no online configs!");
                    return;
                }
                Modification.LOG_UTIL.sendChatMessage("Current online configs: ".concat(((String) localObject).substring(0, ((String) localObject).length() - 2)).concat("§f"));
                return;
            }
            return;
        }
        sendUsage();
    }
}




