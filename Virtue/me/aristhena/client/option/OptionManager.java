// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.option;

import java.util.List;
import java.lang.reflect.Field;
import java.util.Iterator;
import me.aristhena.client.command.CommandManager;
import java.util.ArrayList;
import me.aristhena.client.option.types.NumberOption;
import me.aristhena.client.option.types.StringOption;
import me.aristhena.client.option.types.BooleanOption;
import org.apache.commons.lang3.StringUtils;
import java.lang.annotation.Annotation;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.ModuleManager;
import me.aristhena.utils.FileUtils;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.File;

public final class OptionManager
{
    private static final File OPTION_DIR;
    private static CopyOnWriteArrayList<Option> optionList;
    
    static {
        OPTION_DIR = FileUtils.getConfigFile("Options");
        OptionManager.optionList = new CopyOnWriteArrayList<Option>();
    }
    
    public static void start() {
        try {
            for (final Module mod : ModuleManager.getModules()) {
                mod.preInitialize();
                Field[] declaredFields;
                for (int length = (declaredFields = mod.getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
                    final Field field = declaredFields[i];
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Option.Op.class)) {
                        if (!field.getType().isAssignableFrom(Float.TYPE) && !field.getType().isAssignableFrom(Double.TYPE) && !field.getType().isAssignableFrom(Integer.TYPE) && !field.getType().isAssignableFrom(Long.TYPE) && !field.getType().isAssignableFrom(Short.TYPE)) {
                            if (!field.getType().isAssignableFrom(Byte.TYPE)) {
                                if (field.getType().isAssignableFrom(Boolean.TYPE)) {
                                    boolean value;
                                    try {
                                        value = field.getBoolean(mod);
                                    }
                                    catch (IllegalArgumentException e2) {
                                        value = false;
                                    }
                                    final Option.Op opAnnotation = field.getAnnotation(Option.Op.class);
                                    final String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize(field.getName()) : StringUtils.capitalize(opAnnotation.name());
                                    OptionManager.optionList.add(new BooleanOption(field.getName(), StringUtils.capitalize(name), value, mod, false));
                                    continue;
                                }
                                if (field.getType().isAssignableFrom(String.class)) {
                                    String value2;
                                    try {
                                        value2 = (String)field.get(mod);
                                    }
                                    catch (IllegalArgumentException e2) {
                                        value2 = "";
                                    }
                                    final Option.Op opAnnotation = field.getAnnotation(Option.Op.class);
                                    final String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize(field.getName()) : StringUtils.capitalize(opAnnotation.name());
                                    OptionManager.optionList.add(new StringOption(field.getName(), StringUtils.capitalize(name), value2, mod));
                                }
                                continue;
                            }
                        }
                        Number value3;
                        try {
                            value3 = (Number)field.get(mod);
                        }
                        catch (IllegalArgumentException e2) {
                            value3 = 0;
                        }
                        final Option.Op opAnnotation = field.getAnnotation(Option.Op.class);
                        final String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize(field.getName()) : StringUtils.capitalize(opAnnotation.name());
                        final NumberOption option = new NumberOption(field.getName(), StringUtils.capitalize(name), value3, mod);
                        option.setMin(opAnnotation.min());
                        option.setMax(opAnnotation.max());
                        option.setIncrement(opAnnotation.increment());
                        OptionManager.optionList.add(option);
                    }
                }
                final List<String> nameList = new ArrayList<String>();
                if (CommandManager.optionCommand.getNames() != null) {
                    String[] names;
                    for (int length2 = (names = CommandManager.optionCommand.getNames()).length, j = 0; j < length2; ++j) {
                        final String name2 = names[j];
                        nameList.add(name2);
                    }
                }
                nameList.add(mod.getId());
                nameList.add(mod.getDisplayName());
                CommandManager.optionCommand.setNames(nameList.toArray(new String[0]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        load();
        save();
    }
    
    public static void load() {
        final List<String> fileContent = FileUtils.read(OptionManager.OPTION_DIR);
        for (final String line : fileContent) {
            try {
                final String[] split = line.split(":");
                final String optionId = split[0];
                final String optionValue = split[1];
                final String modId = split[2];
                final Option option = getOption(optionId, split[2]);
                if (option == null) {
                    continue;
                }
                if (option instanceof NumberOption) {
                    ((NumberOption)option).setValue(optionValue);
                }
                else if (option instanceof BooleanOption) {
                    ((BooleanOption)option).setValueHard(Boolean.parseBoolean(optionValue));
                }
                else {
                    if (!(option instanceof StringOption)) {
                        continue;
                    }
                    option.setValue(optionValue);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Option option : OptionManager.optionList) {
            final String optionId = option.getId();
            final String optionVal = option.getValue().toString();
            final Module mod = option.getModule();
            fileContent.add(String.format("%s:%s:%s", optionId, optionVal, mod.getId()));
        }
        FileUtils.write(OptionManager.OPTION_DIR, fileContent, true);
    }
    
    public static Option getOption(final String optionName, final String modId) {
        for (final Option option : OptionManager.optionList) {
            if ((option.getId().equalsIgnoreCase(optionName) || option.getDisplayName().equalsIgnoreCase(optionName)) && option.getModule().getId().equalsIgnoreCase(modId)) {
                return option;
            }
        }
        return null;
    }
    
    public static List<Option> getOptionList() {
        return OptionManager.optionList;
    }
}
