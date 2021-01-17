package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Mineman;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;

public class EventSystem {

    private static ArrayList<MethodListener> methods = new ArrayList<MethodListener>();

    private static String bla = "";

    public static void register(Listener listener) {
	Listener boo = new Listener() {
	    @Override
	    public void onEvent(Object event) {

	    }
	};

	bla = boo.getClass().getDeclaredMethods()[0].getName();

	for (Method method : listener.getClass().getDeclaredMethods()) {
	    if (isMethodValid(method, listener) && method.getName().equalsIgnoreCase(bla)) {
		Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];

		MethodListener methodListener = new MethodListener(listener, eventClass, method, getPriority(method));
		methods.add(methodListener);
	    }
	}
	sortListeners();
    }

    public static void register(Object object) {
	for (Method method : object.getClass().getDeclaredMethods()) {
	    if (isMethodValid(method, object)) {
		Class<? extends Event> eventClass = (Class<? extends Event>) method.getParameterTypes()[0];

		MethodListener methodListener = new MethodListener(object, eventClass, method, getPriority(method));

		methods.add(methodListener);
	    }
	}
	sortListeners();
    }

    public static boolean isRegistered(Object object) {
	for (int i = 0; i <= methods.size() - 1; i++) {
	    MethodListener methodListener = methods.get(i);
	    if (methodListener.getParent().getClass() == object.getClass())
		return true;
	}
	return false;
    }

    public static void unregister(Object object) {
	List<MethodListener> remove = new ArrayList<MethodListener>();
	for (MethodListener methodListener : methods) {
	    if (methodListener.getParent().getClass() == object.getClass())
		remove.add(methodListener);
	}

	methods.removeAll(remove);

	sortListeners();
    }

    public static class MethodListenerComparator implements Comparator<MethodListener> {

	@Override
	public int compare(MethodListener o1, MethodListener o2) {
	    if (o1.getPriority() > o2.getPriority()) {
		return 1;
	    } else if (o1.getPriority() < o2.getPriority()) {
		return -1;
	    } else {
		return 0;
	    }
	}

    }

    private static void sortListeners() {
	Collections.sort(methods, new MethodListenerComparator());
    }

    private static int getPriority(Method method) {
	if (!method.isAnnotationPresent(EventPriorityListener.class)) {
	    return EventPriority.NORMAL.getPriority();
	}

	EventPriorityListener listener = method.getAnnotation(EventPriorityListener.class);
	return listener.priority().getPriority();
    }

    private static boolean isMethodValid(Method method, Object object) {
	return (method.isAnnotationPresent(EventListener.class)
		|| method.isAnnotationPresent(EventPriorityListener.class) || object instanceof Listener)
		&& method.getParameterTypes().length == 1;
    }

    public static Event call(Event event) {
	for (Module module : SkyLine.getManagers().getModuleManager().getContents()) {
	    module.mc = Mineman.getMinecraft();
	    module.p = Mineman.getMinecraft().thePlayer;
	    for (ModMode mode : module.getModes()) {
		mode.mc = Mineman.getMinecraft();
		mode.p = Mineman.getMinecraft().thePlayer;
	    }
	}

	for (MethodListener methodListener : methods) {
	    if (methodListener.getPriority() < 2)
		System.out.println("CALL1 " + methodListener.getParent().getClass().getSimpleName() + ", "
			+ methodListener.getPriority() + ", " + methodListener.getMethod().getName());
	}

	for (int i = 0; i <= methods.size() - 1; i++) {
	    MethodListener methodListener = methods.get(i);
	    Class<? extends Event> eventClass = event.getClass();

	    if (event.isCancelled())
		break;

	    if (event.hasEnded())
		break;

	    if (eventClass.getName().equalsIgnoreCase(methodListener.getEventClass().getName())) {
		invokeMethodListener(methodListener, event);
	    }
	}
	return event;
    }

    private static void invokeMethodListener(MethodListener methodListener, Event event) {
	if (methodListener == null)
	    return;

	try {
	    methodListener.getMethod().setAccessible(true);
	    methodListener.getMethod().invoke(methodListener.getParent(), event);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
