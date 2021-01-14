package com.etb.client.command;

import java.util.HashMap;
import java.util.Map;

import com.etb.client.command.impl.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.input.Keyboard;

import com.etb.client.Client;
import com.etb.client.event.events.game.ChatEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.Printer;
import com.etb.client.utils.value.Value;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;

public class CommandManager {
    public Map<String, Command> map = new HashMap<>();

    public void initialize() {
        register(Bind.class);
        register(Friend.class);
        register(Vclip.class);
        register(ConfigCMD.class);
        register(Help.class);
        register(NoStrike.class);
        register(BanWaveCMD.class);
        register(XrayCMD.class);
        register(Teleport.class);
        register(TeleportPacket.class);
        register(Toggle.class);
        register(Cheats.class);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onChat(ChatEvent event) {
        final String message = event.getMsg();
        if (message.startsWith(".")) {
            event.setCanceled(true);
            dispatch(message.substring(1).toLowerCase());
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
            Module m = Client.INSTANCE.getModuleManager().getModule(command[0]);
            if (m != null) {
                if (command[1].equalsIgnoreCase("help")) {
                    if (!m.getValues().isEmpty()) {
                        Printer.print(m.getLabel() + "'s available properties are:");
                        m.getValues().forEach(prop -> {
                            Printer.print(prop.getLabel() + ": " + prop.getValue());
                            if (prop instanceof EnumValue) {
                                Printer.print(prop.getLabel() + "'s available modes are:");
                                StringBuilder modes = new StringBuilder();
                                for (Enum vals : ((EnumValue) prop).getConstants()) {
                                    modes.append(vals + " ");
                                }
                                Printer.print(modes.toString());
                            }
                        });
                    } else {
                        Printer.print("This cheat has no properties.");
                    }
                    Printer.print(m.getLabel() + " is a " + (m.isHidden() ? "hidden " : "shown ") + "module.");
                    Printer.print(m.getLabel() + " is bound to " + Keyboard.getKeyName(m.getKeybind()) + ".");
                    return;
                }
                if (command.length > 2) {
                    if (command[1].equalsIgnoreCase("hidden")) {
                        if (command[2].equalsIgnoreCase("true")) {
                            m.setHidden(true);
                            Printer.print("Set " + m.getLabel() + " to hidden.");
                        }
                        if (command[2].equalsIgnoreCase("false")) {
                            m.setHidden(false);
                            Printer.print("Set " + m.getLabel() + " to shown.");
                        }
                        return;
                    }
                    for (Value v : m.getValues()) {
                        if (v.getLabel().toLowerCase().equals(command[1])) {
                            if (v instanceof BooleanValue) {
                                v.setValue(command[2]);
                                Printer.print("Set " + command[0] + " " + command[1] + " to " + v.getValue() + ".");
                                return;
                            }
                            if (v instanceof NumberValue) {
                                v.setValue(command[2]);
                                Printer.print("Set " + command[0] + " " + command[1] + " to " + v.getValue() + ".");
                                return;
                            }
                            if (v instanceof EnumValue) {
                                v.setValue(command[2]);
                                Printer.print("Set " + command[0] + " " + command[1] + " to " + v.getValue() + ".");
                                return;
                            }
                            v.setValue(command[2]);
                            Printer.print("Set " + command[0] + " " + command[1] + " to " + v.getValue() + ".");
                            return;
                        }
                    }
                } else {
                    Printer.print("Property not found! Do ." + command[0] + " help for list of properties.");
                }
            }
        }
        for (Command c : Client.INSTANCE.getCommandManager().getCommandMap().values()) {
            for (String handle : c.getHandles()) {
                if (handle.toLowerCase().equals(command[0])) c.onRun(command);
            }
        }
    }

    public Map<String, Command> getCommandMap() {
        return map;
    }
}

