package moonx.ohare.client.event.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Listener {
    private Object parent;
    private Method method;
    private Class<?> eventClass;
    private Handler handler;

    public Listener(Object parent, Method method) {
        this.parent = parent;
        this.method = method;
        this.eventClass = method.getParameterTypes()[0];
        this.handler = method.getAnnotation(Handler.class);
    }

    public Object getParent() {
        return parent;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEventClass() {
        return eventClass;
    }

    public Handler getHandler() {
        return handler;
    }

    public <E> void run(E event) {
        try {
            method.invoke(parent, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
