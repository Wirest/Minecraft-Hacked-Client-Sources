package com.ihl.client.util;

import com.ihl.client.Client;
import com.ihl.client.commands.exceptions.ArgumentException;
import com.ihl.client.module.Module;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.module.option.ValueList;
import com.ihl.client.util.part.ChatColor;
import com.ihl.client.util.part.Settings;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    public static BufferedReader readInternal(String file) {
        return new BufferedReader(new InputStreamReader(Client.class.getResourceAsStream(file)));
    }

    public static BufferedReader readExternal(String file) throws IOException {
        return new BufferedReader(new FileReader(file));
    }

    public static BufferedWriter writeExternal(String file) throws IOException {
        File f = new File(file);
        File dir = f.getParentFile();
        if (!dir.exists()) {
        	dir.mkdirs();
        }
        if (!f.exists()) {
            f.createNewFile();
        }
        return new BufferedWriter(new FileWriter(file));
    }

    public static void readData() {
        BufferedReader reader;
        String line;
        try {
            reader = readInternal("/assets/minecraft/client/data.deluge");
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 3) {
                    ColorUtil.colors.put(split[0], new ChatColor(split[1], split[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSettings() {
        BufferedReader reader = null;
        String line;
        try {
            File f = new File("./"+Client.NAME+"/settings.deluge");
            if (!f.exists()) {
                writeSettings();
                return;
            }
            reader = readExternal(f.getPath());
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 2) {
                    Option option = Option.get(Settings.options, split[0]);
                    if (option != null) {
                        try {
                            Option.setOptionValue(option, split[1]);
                        } catch (Exception e) {
                        }
                    }
                } else if (split.length == 3) {
                    Option option = Option.get(Settings.options, split[0], split[1]);
                    if (option != null) {
                        try {
                            Option.setOptionValue(option, split[2]);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeSettings() {
        try {
            BufferedWriter writer = writeExternal("./" + Client.NAME + "/settings.deluge");
            for(String key : Settings.options.keySet()) {
                Option option = Option.get(Settings.options, key);
                writer.write(key + ":" + option.STRING());
                writer.write('\n');
                for(String key2 : option.options.keySet()) {
                    Option option2 = Option.get(Settings.options, key, key2);
                    writer.write(key+":"+key2+":"+option2.STRING());
                    writer.write('\n');
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readModules() {
        BufferedReader reader = null;
        String line;
        try {
            File f = new File("./"+Client.NAME+"/modules.deluge");
            if (!f.exists()) {
                writeModules();
                return;
            }
            reader = readExternal(f.getPath());
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 2) {
                    Module module = Module.get(split[0]);
                    if (module != null && (split[1].equalsIgnoreCase("true") || split[1].equalsIgnoreCase("false"))) {
                        boolean bool = Boolean.parseBoolean(split[1]);
                        if (bool) {
                            module.enable();
                        } else {
                            module.disable();
                        }
                    }
                } else if (split.length == 3) {
                    Module module = Module.get(split[0]);
                    if (module != null) {
                        Option option = Option.get(module.options, split[1]);
                        if (option != null) {
                            if (option.type == Option.Type.LIST) {
                                String[] splitter = split[2].split(",");
                                if (splitter.length > 0) {
                                    ((ValueList)option.value).value = Arrays.asList(splitter);
                                }
                            } else {
                                try {
                                    Option.setOptionValue(option, split[2]);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                } else if (split.length == 4) {
                    Module module = Module.get(split[0]);
                    if (module != null) {
                        Option option = Option.get(module.options, split[1], split[2]);
                        if (option != null) {
                            if (option.type == Option.Type.LIST) {
                                String[] splitter = split[2].split(",");
                                if (splitter.length > 0) {
                                    ((ValueList)option.value).value = Arrays.asList(splitter);
                                }
                            } else {
                                try {
                                    Option.setOptionValue(option, split[3]);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeModules() {
        try {
            BufferedWriter writer = writeExternal("./" + Client.NAME + "/modules.deluge");
            for(String key : Module.modules.keySet()) {
                Module module = Module.get(key);
                writer.write(key + ":" + module.active);
                writer.write('\n');
                for(String key2 : module.options.keySet()) {
                    Option option = Option.get(module.options, key2);
                    writer.write(key+":"+key2+":"+option.STRING());
                    writer.write('\n');
                    for(String key3 : option.options.keySet()) {
                        Option option2 = Option.get(module.options, key2, key3);
                        writer.write(key+":"+key2+":"+key3+":"+option2.STRING());
                        writer.write('\n');
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
