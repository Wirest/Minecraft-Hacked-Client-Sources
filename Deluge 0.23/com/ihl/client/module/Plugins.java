package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPacket;
import com.ihl.client.gui.GuiHandle;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.HelperUtil;
import com.ihl.client.util.TimerUtil;
import joptsimple.internal.Strings;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.lwjgl.input.Keyboard;

import java.util.*;

@EventHandler(events = {EventPacket.class})
public class Plugins extends Module {

    private List<String> plugins = new ArrayList();
    private boolean scan;
    private TimerUtil timer = new TimerUtil();

    public Plugins(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("timeout", new Option("Timeout", "Period before timing out and cancelling request", new ValueDouble(5, new double[] {0, 10}, 0.1), Option.Type.NUMBER));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        if (!HelperUtil.inGame()) {
            return;
        }
        Helper.player().sendQueue.addToSendQueue(new C14PacketTabComplete("/"));
        scan = true;
        plugins.clear();
        timer.reset();
    }

    public void disable() {
        super.disable();
        scan = false;
    }

    protected void tick() {
        super.tick();
        double timeout = Option.get(options, "timeout").DOUBLE();
        if (active && timer.isTime(timeout)) {
            timer.reset();
            disable();
        }
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPacket) {
            EventPacket e = (EventPacket) event;
            if (e.type == Event.Type.RECEIVE) {
                if (e.packet instanceof S3APacketTabComplete && scan) {
                    S3APacketTabComplete packet = (S3APacketTabComplete) e.packet;
                    String[] commands = packet.func_149630_c();
                    for(String s : commands) {
                        String[] split = s.split(":");
                        if (split.length > 1) {
                            String in = split[0].replaceAll("/", "");
                            if (!plugins.contains(in)) {
                                plugins.add(in);
                            }
                        }
                    }
                    Collections.sort(plugins);
                    if (plugins.isEmpty()) {
                        ChatUtil.send("No plugins found");
                    } else {
                        ChatUtil.send("[n]Plugins [t]([v]"+plugins.size()+"[t]): [v]" + Strings.join(plugins.toArray(new String[0]), "[t], [v]"));
                    }
                    disable();
                }
            }
        }
    }
}
