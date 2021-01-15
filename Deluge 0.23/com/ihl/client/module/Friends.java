package com.ihl.client.module;

import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerPickBlock;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import com.ihl.client.module.option.ValueList;
import com.ihl.client.module.option.ValueString;
import com.ihl.client.util.ChatUtil;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.List;

@EventHandler(events = {EventPlayerPickBlock.class})
public class Friends extends Module {

    public static boolean toggleFriend(String name) {
        Friends friends = (Friends) Module.get("friends");
        List<String> list = new ArrayList();
        list.addAll(Option.get(friends.options, "friends").LIST());

        boolean added;
        name = name.toLowerCase();
        String message;
        if (list.contains(name)) {
            list.remove(name);
            added = false;
            message = String.format("[v]%s [t]removed from [v]%s", name, Option.get(friends.options, "friends").name);
        } else {
            list.add(name);
            added = true;
            message = String.format("[v]%s [t]added to [v]%s", name, Option.get(friends.options, "friends").name);
        }

        ChatUtil.send(message);
        Option.get(friends.options, "friends").value.value = list;
        return added;
    }

    public static boolean isFriend(String name) {
        Friends friends = (Friends) Module.get("friends");
        List<String> list = Option.get(friends.options, "friends").LIST();

        name = name.toLowerCase();
        return friends.active && list.contains(name);
    }

    public Friends(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("friends", new Option("Friends", "List of friends", new ValueList(new String[]{}), Option.Type.LIST));
        options.put("middleclick", new Option("Middle Click", "Allow middle clicking players to toggle them", new ValueBoolean(true), Option.Type.BOOLEAN));
        options.put("color", new Option("Color", "Render color override", new ValueString("ffffffff"), Option.Type.STRING));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        if (event instanceof EventPlayerPickBlock) {
            EventPlayerPickBlock e = (EventPlayerPickBlock) event;
            if (e.type == Event.Type.PRE) {
                if (e.mop.entityHit != null && e.mop.entityHit instanceof EntityLivingBase && Option.get(options, "middleclick").BOOLEAN()) {
                    toggleFriend(e.mop.entityHit.getName());
                }
            }
        }
    }
}
