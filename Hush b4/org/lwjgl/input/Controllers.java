// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

import java.util.Iterator;
import org.lwjgl.LWJGLException;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import java.util.ArrayList;

public class Controllers
{
    private static ArrayList<JInputController> controllers;
    private static int controllerCount;
    private static ArrayList<ControllerEvent> events;
    private static ControllerEvent event;
    private static boolean created;
    
    public static void create() throws LWJGLException {
        if (Controllers.created) {
            return;
        }
        try {
            final ControllerEnvironment env = ControllerEnvironment.getDefaultEnvironment();
            final Controller[] found = env.getControllers();
            final ArrayList<Controller> lollers = new ArrayList<Controller>();
            for (final Controller c : found) {
                if (!c.getType().equals(Controller.Type.KEYBOARD) && !c.getType().equals(Controller.Type.MOUSE)) {
                    lollers.add(c);
                }
            }
            for (final Controller c2 : lollers) {
                createController(c2);
            }
            Controllers.created = true;
        }
        catch (Throwable e) {
            throw new LWJGLException("Failed to initialise controllers", e);
        }
    }
    
    private static void createController(final Controller c) {
        final Controller[] subControllers = c.getControllers();
        if (subControllers.length == 0) {
            final JInputController controller = new JInputController(Controllers.controllerCount, c);
            Controllers.controllers.add(controller);
            ++Controllers.controllerCount;
        }
        else {
            for (final Controller sub : subControllers) {
                createController(sub);
            }
        }
    }
    
    public static org.lwjgl.input.Controller getController(final int index) {
        return Controllers.controllers.get(index);
    }
    
    public static int getControllerCount() {
        return Controllers.controllers.size();
    }
    
    public static void poll() {
        for (int i = 0; i < Controllers.controllers.size(); ++i) {
            getController(i).poll();
        }
    }
    
    public static void clearEvents() {
        Controllers.events.clear();
    }
    
    public static boolean next() {
        if (Controllers.events.size() == 0) {
            Controllers.event = null;
            return false;
        }
        Controllers.event = Controllers.events.remove(0);
        return Controllers.event != null;
    }
    
    public static boolean isCreated() {
        return Controllers.created;
    }
    
    public static void destroy() {
    }
    
    public static org.lwjgl.input.Controller getEventSource() {
        return Controllers.event.getSource();
    }
    
    public static int getEventControlIndex() {
        return Controllers.event.getControlIndex();
    }
    
    public static boolean isEventButton() {
        return Controllers.event.isButton();
    }
    
    public static boolean isEventAxis() {
        return Controllers.event.isAxis();
    }
    
    public static boolean isEventXAxis() {
        return Controllers.event.isXAxis();
    }
    
    public static boolean isEventYAxis() {
        return Controllers.event.isYAxis();
    }
    
    public static boolean isEventPovX() {
        return Controllers.event.isPovX();
    }
    
    public static boolean isEventPovY() {
        return Controllers.event.isPovY();
    }
    
    public static long getEventNanoseconds() {
        return Controllers.event.getTimeStamp();
    }
    
    public static boolean getEventButtonState() {
        return Controllers.event.getButtonState();
    }
    
    public static float getEventXAxisValue() {
        return Controllers.event.getXAxisValue();
    }
    
    public static float getEventYAxisValue() {
        return Controllers.event.getYAxisValue();
    }
    
    static void addEvent(final ControllerEvent event) {
        if (event != null) {
            Controllers.events.add(event);
        }
    }
    
    static {
        Controllers.controllers = new ArrayList<JInputController>();
        Controllers.events = new ArrayList<ControllerEvent>();
    }
}
