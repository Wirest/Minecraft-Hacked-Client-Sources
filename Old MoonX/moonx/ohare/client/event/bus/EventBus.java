package moonx.ohare.client.event.bus;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private Map<Class<?>, List<Listener>> registry = new HashMap<>();
    private Map<Class<?>, List<Listener>> cache = new HashMap<>();
    private Comparator<Listener> sorter = Comparator.comparingInt(listener -> listener.getHandler().value());

    public void bind(Object parent) {
        if (cache.containsKey(parent.getClass())) {
            cache.get(parent.getClass()).forEach(this::bind);
        } else {
            for (Method method : parent.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Handler.class) && method.getParameterCount() == 1) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    Listener listener = new Listener(parent, method);
                    bind(listener);
                    cache.computeIfAbsent(parent.getClass(), v -> new CopyOnWriteArrayList<>()).add(listener);
                }
            }
        }
    }

    public void bind(Listener listener) {
        List<Listener> list = registry.computeIfAbsent(listener.getEventClass(), v -> new CopyOnWriteArrayList<>());
        list.add(listener);
        list.sort(sorter);
    }

    public void unbind(Object parent) {
        if (cache.containsKey(parent.getClass())) {
            cache.get(parent.getClass()).forEach(this::unbind);
        }
    }

    public void unbind(Listener listener) {
        registry.values().forEach(list -> list.remove(listener));
    }

    public <E> E dispatch(E event) {
        List<Listener> list = registry.get(event.getClass());
        if (list != null) {
            list.forEach(listener -> listener.run(event));
        }
        return event;
    }
}
