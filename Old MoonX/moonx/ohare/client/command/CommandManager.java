package moonx.ohare.client.command;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.impl.*;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.ChatEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.font.MCFontRenderer;
import moonx.ohare.client.utils.value.Value;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.FontValue;
import moonx.ohare.client.utils.value.impl.StringValue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public Map<String, Command> map = new HashMap<>();

    public void initialize() {
        register(Bind.class);
        register(Friend.class);
        register(Vclip.class);
        register(Teleport.class);
        register(ConfigCMD.class);
        register(Help.class);
        register(Macros.class);
        register(Waypoint.class);
        register(Toggle.class);
        register(Modules.class);
        Moonx.INSTANCE.getEventBus().bind(this);
    }

    @Handler
    public void onPacket(ChatEvent event) {
        final String message = event.getMsg();
        if (message.startsWith(".")) {
            event.setCanceled(true);
            dispatch(message.substring(1));
        }
    }

    private void register(Class<? extends Command> commandClass) {
        try {
            Command createdCommand = commandClass.newInstance();
            map.put(createdCommand.getLabel().toLowerCase(), createdCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dispatch(final String s) {
        final String[] command = s.split(" ");
        if (command.length > 1) {
            Module m = Moonx.INSTANCE.getModuleManager().getModule(command[0]);
            if (m != null) {
                if (command[1].equalsIgnoreCase("help")) {
                    if (!m.getValues().isEmpty()) {
                        Printer.print(m.getLabel() + "'s available properties are:");
                        for (Value value : m.getValues()) {
                            if (value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))
                                continue;
                            if (value instanceof FontValue) {
                                final FontValue fontValue = (FontValue) value;
                                Printer.print(fontValue.getLabel() + ": " + fontValue.getValue().getFont().getName());
                            } else
                                Printer.print(value.getLabel() + ": " + value.getValue());
                            if (value instanceof EnumValue) {
                                Printer.print(value.getLabel() + "'s available modes are:");
                                StringBuilder modes = new StringBuilder();
                                for (Enum vals : ((EnumValue) value).getConstants()) {
                                    modes.append(vals).append(" ");
                                }
                                Printer.print(modes.toString());
                            }
                        }
                    } else {
                        Printer.print("This cheat has no properties.");
                    }
                    Printer.print(m.getLabel() + " is a " + (m.isHidden() ? "hidden " : "shown ") + "module.");
                    Printer.print(m.getLabel() + " is bound to " + Keyboard.getKeyName(m.getKeybind()) + ".");
                    return;
                }
                if (command.length > 2) {
                    if (command[1].equalsIgnoreCase("hidden")) {
                        m.setHidden(Boolean.parseBoolean(command[2].toLowerCase()));
                        Printer.print("Set " + m.getLabel() + " to " + m.isHidden() + ".");
                        return;
                    }
                    for (Value value : m.getValues()) {
                        if (value.getParentValueObject() != null && !value.getParentValueObject().getValueAsString().equalsIgnoreCase(value.getParentValue()))
                            continue;
                        if (value.getLabel().replace(" ", "").toLowerCase().equals(command[1].toLowerCase())) {
                            if (value instanceof StringValue) {
                                final StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 2; i < command.length; i++) {
                                    stringBuilder.append(command[i]);
                                    if (i != command.length - 1) stringBuilder.append(" ");
                                }
                                value.setValue(stringBuilder.toString());
                                Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + value.getValue() + ".");
                            } else if (value instanceof FontValue) {
                                if (command.length >= 7) {
                                    final ArrayList<String> fonts = new ArrayList<>();
                                    for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
                                        fonts.add(font.toLowerCase());
                                    }
                                    File fontDirectory = new File(System.getenv("LOCALAPPDATA") + "\\Microsoft\\Windows\\Fonts");
                                    for (File file : FileUtils.listFiles(fontDirectory, new WildcardFileFilter("*.ttf", IOCase.INSENSITIVE), null)) {
                                        try {
                                            final Font font = Font.createFont(Font.TRUETYPE_FONT, file);
                                            if (!fonts.contains(font.getName().toLowerCase())) {
                                                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                                                fonts.add(font.getName().toLowerCase());
                                                Printer.print("Detected and added font " + font.getName());
                                            }
                                        } catch (FontFormatException | IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (fonts.contains(command[2].replace("_", " ").toLowerCase())) {
                                        try {
                                            final FontValue fontValue = (FontValue) value;
                                            final int style = (int) MathUtils.clamp(Integer.parseInt(command[3]), 2, 0);
                                            final int size = Integer.parseInt(command[4]);
                                            final boolean aa = Boolean.parseBoolean(command[5]);
                                            final boolean fractionalmetrics = Boolean.parseBoolean(command[6]);
                                            final MCFontRenderer mcFontRenderer = new MCFontRenderer(new Font(command[2].replace("_", " "), style, size), aa, fractionalmetrics);
                                            fontValue.setValue(mcFontRenderer);
                                            Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + fontValue.getValue().getFont().getName() + ", style " + fontValue.getValue().getFont().getStyle() + ", size " + fontValue.getValue().getFont().getSize() + ", antialiasing " + aa + ", fractionalmetrics " + fractionalmetrics + ".");
                                        } catch (Exception e) {
                                            Printer.print("Invalid Font 1!");
                                        }
                                    } else {
                                        Printer.print("Invalid Font 2!");
                                    }
                                } else {
                                    Printer.print("Arguments are (Name, Style (0 for plain, 1 for bold, 2 for italics), size, AntiAliasing (true or false), FractionalMetrics (true or false))");
                                }
                            } else {
                                value.setValue(command[2]);
                                Printer.print("Set " + command[0] + " " + value.getLabel() + " to " + value.getValue() + ".");
                            }
                        }
                    }
                    ;
                }
            }
        }
        Moonx.INSTANCE.getCommandManager().getCommandMap().values().forEach(c -> {
            for (String handle : c.getHandles()) {
                if (handle.toLowerCase().equals(command[0])) c.onRun(command);
            }
        });
    }

    public Map<String, Command> getCommandMap() {
        return map;
    }
}

