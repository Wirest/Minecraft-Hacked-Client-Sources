package com.ihl.client.module;

import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventMouse;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.HelperUtil;
import com.ihl.client.util.TimerUtil;

import java.util.ArrayList;
import java.util.List;

@EventHandler(events = {EventMouse.class})
public class CPS extends Module {

    private int cps;
    private List<Integer> cpsList = new ArrayList();
    private TimerUtil timer = new TimerUtil();

    public CPS(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        cpsList.clear();
    }

    public void disable() {
        super.disable();
        if (!cpsList.isEmpty()) {
            double average = 0;
            for (int i : cpsList) {
                average += i;
            }
            average /= cpsList.size();
            ChatUtil.send(String.format("Average CPS: [v]%s", average));
        }
        cps = 0;
        cpsList.clear();
    }

    protected void tick() {
        if (active && timer.isTime(1)) {
            if (cps > 0) {
                ChatUtil.send(String.format("Current CPS: [v]%s", cps));
                cpsList.add(cps);
                cps = 0;
            }
            timer.reset();
        }
    }

    protected void onEvent(Event event) {
        if (event instanceof EventMouse) {
            EventMouse e = (EventMouse) event;
            if (e.type == Event.Type.CLICKL) {
                cps++;
            }
        }
    }
}
