package cedo.command;

import cedo.command.commands.*;
import cedo.events.Event;
import cedo.events.listeners.EventChat;
import cedo.events.listeners.EventKey;
import cedo.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    public String prefix = ".";
    public int prefixInt = Keyboard.KEY_PERIOD;
    Map<String, Command> commands = new HashMap<>();
    boolean init = false;

    public void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void registerCommands() {
        addCommand(new HelpCommand());
        addCommand(new BindCommand());
        addCommand(new ToggleCommand());
        addCommand(new NameProtectCommand());
        addCommand(new NameCommand());
        addCommand(new ConfigCommand());
        addCommand(new ClearCommand());
        addCommand(new YanchopCommand());
        init = true;
    }

    public boolean isInitialized() {
        return init;
    }

    public void onEvent(Event e) {
        if (e instanceof EventChat) {
            EventChat event = (EventChat) e;
            String message = event.getMessage();

            if (!isInitialized()) {
                registerCommands();
            }
            if (message.startsWith(prefix)) {
                String commandName = message.substring(1);
                String[] m = commandName.split(" ");
                String[] args = Arrays.copyOfRange(m, 1, m.length);
                boolean executed = false;

                for (Command command : getCommands().values()) {
                    if (command.getName().equalsIgnoreCase(m[0])) {
                        command.execute(args);
                        executed = true;
                    }
                }

                if (!executed) {
                    Logger.ingameWarn("Command \"" + commandName.toLowerCase() + "\" not found!");
                }

                event.setCancelled(true);
            }
        } else if (e instanceof EventKey) {
            EventKey event = (EventKey) e;
            if (event.getCode() == prefixInt && Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiChat(prefix));
            }
        }
    }
}
