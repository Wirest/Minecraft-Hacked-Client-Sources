package moonx.ohare.client.command.impl;

import java.io.IOException;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.utils.Printer;
import org.lwjgl.input.Keyboard;


public class Macros extends Command {

    public Macros() {
        super("Macros", new String[]{"macros", "mac", "macro"});
    }

    @Override
    public void onRun(final String[] args) {
        switch (args[1].toLowerCase()) {
            case "list":
                if (Moonx.INSTANCE.getMacroManager().getMacros().isEmpty()) {
                    Printer.print("Your macro list is empty.");
                    return;
                }
                Printer.print("Your macros are:");
                Moonx.INSTANCE.getMacroManager().getMacros().values().forEach(macro -> Printer.print("Label: " + macro.getLabel() + ", Keybind: " + Keyboard.getKeyName(macro.getKey()) + ", Text: " + macro.getText() + "."));
                break;
            case "reload":
                Moonx.INSTANCE.getMacroManager().clearMacros();
                Moonx.INSTANCE.getMacroManager().load();
                Printer.print("Reloaded macros.");
                break;
            case "remove":
            case "delete":
                if (args.length < 3) {
                    Printer.print("Invalid args.");
                    return;
                }
                if (Moonx.INSTANCE.getMacroManager().isMacro(args[2])) {
                    Moonx.INSTANCE.getMacroManager().removeMacroByLabel(args[2]);
                    Printer.print("Removed a macro named " + args[2] + ".");
                    if (Moonx.INSTANCE.getMacroManager().getMacroFile().exists()) {
                        Moonx.INSTANCE.getMacroManager().save();
                    } else {
                        try {
                            Moonx.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                            Moonx.INSTANCE.getMacroManager().save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Printer.print(args[2] + " is not a macro.");
                }
                break;
            case "clear":
                if (Moonx.INSTANCE.getMacroManager().getMacros().isEmpty()) {
                    Printer.print("Your macro list is empty.");
                    return;
                }
                Printer.print("Cleared all macros.");
                Moonx.INSTANCE.getMacroManager().clearMacros();
                if (Moonx.INSTANCE.getMacroManager().getMacroFile().exists()) {
                    Moonx.INSTANCE.getMacroManager().save();
                } else {
                    try {
                        Moonx.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                        Moonx.INSTANCE.getMacroManager().save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "add":
            case "create":
                if (args.length < 5) {
                    Printer.print("Invalid args.");
                    return;
                }
                int keyCode = Keyboard.getKeyIndex(args[3].toUpperCase());
                if (keyCode != -1 && !Keyboard.getKeyName(keyCode).equals("NONE")) {
                    if (Moonx.INSTANCE.getMacroManager().getMacroByKey(keyCode) != null) {
                        Printer.print("There is already a macro bound to that key.");
                        return;
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 4; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        if (i != args.length - 1) stringBuilder.append(" ");
                    }
                    Moonx.INSTANCE.getMacroManager().addMacro(args[2], keyCode, stringBuilder.toString());
                    Printer.print("Bound a macro named " + args[2] + " to the key " + Keyboard.getKeyName(keyCode) + ".");
                    if (Moonx.INSTANCE.getMacroManager().getMacroFile().exists()) {
                        Moonx.INSTANCE.getMacroManager().save();
                    } else {
                        try {
                            Moonx.INSTANCE.getMacroManager().getMacroFile().createNewFile();
                            Moonx.INSTANCE.getMacroManager().save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Printer.print("That is not a valid key code.");
                }
                break;
        }
    }
}