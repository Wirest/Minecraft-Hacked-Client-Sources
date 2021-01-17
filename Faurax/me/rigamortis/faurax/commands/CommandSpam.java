package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import java.util.*;
import net.minecraft.client.*;

public class CommandSpam extends Command
{
    public static String msg;
    public static boolean running;
    private String[] letters;
    
    static {
        CommandSpam.msg = "";
    }
    
    public CommandSpam() {
        super("Spam", new String[0]);
        this.letters = new String[] { "  ", "     ", "   ", "    ", "       ", "    ", "                  ", "        ", "      ", "      " };
    }
    
    @Argument
    protected String spamHelper() {
        return "Start, Stop, Set §a<§fString using _ as spaces.§a>§f";
    }
    
    @Argument(handles = { "set" })
    protected String spamSet(final String msg) {
        CommandSpam.msg = msg.replaceAll("_", " ");
        return "Set message to " + CommandSpam.msg;
    }
    
    public String getRandom(final String[] array) {
        final int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
    
    @Argument(handles = { "start" })
    protected String spamStart() {
        if (CommandSpam.msg == "") {
            return "Set the message.";
        }
        CommandSpam.running = true;
        new Thread() {
            @Override
            public void run() {
                while (CommandSpam.running) {
                    try {
                        if (CommandSpam.msg.startsWith("/") || CommandSpam.msg.startsWith(".")) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage(CommandSpam.msg);
                            Thread.sleep(2300L);
                        }
                        else {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage(String.valueOf(CommandSpam.this.getRandom(CommandSpam.this.letters)) + CommandSpam.msg + CommandSpam.this.getRandom(CommandSpam.this.letters));
                            Thread.sleep(2300L);
                        }
                    }
                    catch (Exception ex) {}
                }
            }
        }.start();
        return "Starting.";
    }
    
    @Argument(handles = { "stop" })
    protected String spamStop() {
        CommandSpam.running = false;
        return "Finished.";
    }
}
