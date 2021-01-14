package me.memewaredevs.client.module.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.Memeware;
import me.memewaredevs.client.event.events.PacketOutEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.ChatUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.function.Consumer;

public class Commands extends Module {

    private boolean say = false;

    public static String clientName = "Memeware";
    public static String editionName = "Value Patch Edition";
    private String prefix = ".";

    public Commands() {
        super("Commands", 0, Module.Category.MISC);
    }

    @Handler
    public Consumer<PacketOutEvent> eventConsumer0 = (event) -> {
        Packet p = event.getPacket();
        if (p instanceof C01PacketChatMessage) {
            C01PacketChatMessage packet = (C01PacketChatMessage) p;
            String msg = packet.getMessage();
            String[] args = msg.split(" +");
            String cmd = args[0].replace(this.prefix, "").toLowerCase();
            if (!say) {
                if (msg.startsWith(this.prefix)) {
                    event.cancel();
                    switch (cmd) {
                        case "bind": {
                            if (args[2] != null) {
                                Module m = Memeware.INSTANCE.getModuleManager().getModule(args[1]);
                                if (m != null) {
                                    int key = Keyboard.getKeyIndex(args[2].toUpperCase());
                                    if (key != -1) {
                                        m.setKeyBind(key);
                                        ChatUtil.printChat("Successfully set bind to "
                                                + Keyboard.getKeyName(key) + ".");
                                    } else {
                                        ChatUtil.printChat("Invalid command usage! Correct usage: .bind [module] [key]");
                                    }
                                } else {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .bind [module] [key]");
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .bind [module] [key]");
                            }
                            break;
                        }
                        case "help": {
                            String[] cmds = new String[] {".help", ".prefix [key]", ".bind [module] [key]", ".vclip [amount]", ".hclip [amount]", ".name", ".say [message]", ".t [module]", ".toggle [module]", ".clientname [name]", ".clientedition [edition]"};
                            for (String s : cmds) {
                                ChatUtil.printChat(s);
                            }
                            break;
                        }
                        case "vclip": {
                            if (args[1] != null) {
                                try {
                                    mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY + Double.parseDouble(args[1]), mc.thePlayer.posZ);
                                    ChatUtil.printChat("VClip'd " + Double.parseDouble(args[1]) + " blocks.");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .vclip [amount]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .vclip [amount]");
                            }
                            break;
                        }
                        case "hclip": {
                            if (args[1] != null) {
                                try {
                                    double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                                    double distance = Double.parseDouble(args[1]);
                                    mc.thePlayer.setPosition(mc.thePlayer.posX + -Math.sin(yaw) * distance, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(yaw) * distance);
                                    ChatUtil.printChat("HClip'd " + Double.parseDouble(args[1]) + " blocks.");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .hclip [amount]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .hclip [amount]");
                            }
                            break;
                        }
                        case "name": {
                            String name = mc.thePlayer.getName();
                            StringSelection stringSelection = new StringSelection(name);
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(stringSelection, null);
                            ChatUtil.printChat("Copied your username to the clipboard!");
                            break;
                        }
                        case "say": {
                            if (args[1] != null) {
                                try {
                                    String[] bruh = Arrays.copyOfRange(args, 1, args.length);
                                    String msg1 = String.join(" ", bruh);
                                    mc.thePlayer.sendChatMessage("\uF705" + msg1);
                                    ChatUtil.printChat("Said your chat message!");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .say [message]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .say [message]");
                            }
                            break;
                        }
                        case "t": {
                            if (args[1] != null) {
                                try {
                                    String[] bruh = Arrays.copyOfRange(args, 1, args.length);
                                    String module = String.join(" ", bruh);
                                    Memeware.INSTANCE.getModuleManager().getModule(module).toggle();
                                    ChatUtil.printChat("Toggled " + module.toUpperCase() + ".");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .t [module]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .t [module]");
                            }
                            break;
                        }
                        case "toggle": {
                            if (args[1] != null) {
                                try {
                                    String[] bruh = Arrays.copyOfRange(args, 1, args.length);
                                    String module = String.join(" ", bruh);
                                    Memeware.INSTANCE.getModuleManager().getModule(module).toggle();
                                    ChatUtil.printChat("Toggled " + module.toUpperCase() + ".");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .toggle [module]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .toggle [module]");
                            }
                            break;
                        }
                        case "clientname": {
                            if (args[1] != null) {
                                try {
                                    String[] bruh = Arrays.copyOfRange(args, 1, args.length);
                                    String aeiofhneaon = String.join(" ", bruh);
                                    this.clientName = aeiofhneaon;
                                    ChatUtil.printChat("Client name set to " + aeiofhneaon + ".");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .clientname [name]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .clientname [name]");
                            }
                            break;
                        }
                        case "clientedition": {
                            if (args[1] != null) {
                                try {
                                    String[] bruh = Arrays.copyOfRange(args, 1, args.length);
                                    String aeiofhneaon = String.join(" ", bruh);
                                    this.editionName = aeiofhneaon;
                                    ChatUtil.printChat("Client Edition set to " + aeiofhneaon + ".");
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .clientedition [edition]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .clientedition [edition]");
                            }
                            break;
                        }
                        case "prefix": {
                            if (args[1] != null) {
                                try {
                                    String[] bruh = Arrays.copyOfRange(args, 1, args.length);
                                    String aeiofhneaon = String.join(" ", bruh);
                                    this.prefix = aeiofhneaon;
                                    ChatUtil.printChat("Prefix set to " + aeiofhneaon);
                                } catch (Exception e) {
                                    ChatUtil.printChat("Invalid command usage! Correct usage: .prefix [key]");
                                    e.printStackTrace();
                                }
                            } else {
                                ChatUtil.printChat("Invalid command usage! Correct usage: .prefix [key]");
                            }
                            break;
                        }
                    }
                }
            }
        }
    };
}
