package org.lwjgl.input;

import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.LWJGLException;

import java.util.ArrayList;
import java.util.Iterator;

public class Controllers {
    private static ArrayList<JInputController> controllers = new ArrayList();
    private static int controllerCount;
    private static ArrayList<ControllerEvent> events = new ArrayList();
    private static ControllerEvent event;
    private static boolean created;

    public static void create()
            throws LWJGLException {
        if (created) {
            return;
        }
        try {
            ControllerEnvironment localControllerEnvironment = ControllerEnvironment.getDefaultEnvironment();
            net.java.games.input.Controller[] arrayOfController = localControllerEnvironment.getControllers();
            ArrayList localArrayList = new ArrayList();
            for (Object localObject2 : arrayOfController) {
                if ((!((net.java.games.input.Controller) localObject2).getType().equals(Controller.Type.KEYBOARD)) && (!((net.java.games.input.Controller) localObject2).getType().equals(Controller.Type.MOUSE))) {
                    localArrayList.add(localObject2);
                }
            }
      ??? =localArrayList.iterator();
            while (((Iterator) ? ??).hasNext())
            {
                net.java.games.input.Controller localController = (net.java.games.input.Controller) ((Iterator) ? ??).
                next();
                createController(localController);
            }
            created = true;
        } catch (Throwable localThrowable) {
            throw new LWJGLException("Failed to initialise controllers", localThrowable);
        }
    }

    private static void createController(net.java.games.input.Controller paramController) {
        net.java.games.input.Controller[] arrayOfController = paramController.getControllers();
        Object localObject;
        if (arrayOfController.length == 0) {
            localObject = new JInputController(controllerCount, paramController);
            controllers.add(localObject);
            controllerCount |= 0x1;
        } else {
            for (net.java.games.input.Controller localController : arrayOfController) {
                createController(localController);
            }
        }
    }

    public static Controller getController(int paramInt) {
        return (Controller) controllers.get(paramInt);
    }

    public static int getControllerCount() {
        return controllers.size();
    }

    public static void poll() {
        for (int i = 0; i < controllers.size(); i++) {
            getController(i).poll();
        }
    }

    public static void clearEvents() {
        events.clear();
    }

    public static boolean next() {
        if (events.size() == 0) {
            event = null;
            return false;
        }
        event = (ControllerEvent) events.remove(0);
        return event != null;
    }

    public static boolean isCreated() {
        return created;
    }

    public static void destroy() {
    }

    public static Controller getEventSource() {
        return event.getSource();
    }

    public static int getEventControlIndex() {
        return event.getControlIndex();
    }

    public static boolean isEventButton() {
        return event.isButton();
    }

    public static boolean isEventAxis() {
        return event.isAxis();
    }

    public static boolean isEventXAxis() {
        return event.isXAxis();
    }

    public static boolean isEventYAxis() {
        return event.isYAxis();
    }

    public static boolean isEventPovX() {
        return event.isPovX();
    }

    public static boolean isEventPovY() {
        return event.isPovY();
    }

    public static long getEventNanoseconds() {
        return event.getTimeStamp();
    }

    public static boolean getEventButtonState() {
        return event.getButtonState();
    }

    public static float getEventXAxisValue() {
        return event.getXAxisValue();
    }

    public static float getEventYAxisValue() {
        return event.getYAxisValue();
    }

    static void addEvent(ControllerEvent paramControllerEvent) {
        if (paramControllerEvent != null) {
            events.add(paramControllerEvent);
        }
    }
}




