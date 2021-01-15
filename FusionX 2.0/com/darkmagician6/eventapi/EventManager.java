// 
// Decompiled by Procyon v0.5.30
// 

package com.darkmagician6.eventapi;

import java.lang.reflect.InvocationTargetException;
import com.darkmagician6.eventapi.events.EventStoppable;
import java.lang.annotation.Annotation;
import com.darkmagician6.eventapi.types.Priority;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import com.darkmagician6.eventapi.events.Event;
import java.util.Map;

public final class EventManager
{
    private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP;
    
    static {
        REGISTRY_MAP = new HashMap<Class<? extends Event>, List<MethodData>>();
    }
    
    public static void register(final Object object) {
        Method[] declaredMethods;
        for (int length = (declaredMethods = object.getClass().getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (!isMethodBad(method)) {
                register(method, object);
            }
        }
    }
    
    public static void register(final Object object, final Class<? extends Event> eventClass) {
        Method[] declaredMethods;
        for (int length = (declaredMethods = object.getClass().getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (!isMethodBad(method, eventClass)) {
                register(method, object);
            }
        }
    }
    
    public static void unregister(final Object object) {
        for (final List<MethodData> dataList : EventManager.REGISTRY_MAP.values()) {
            for (final MethodData data : dataList) {
                if (data.getSource().equals(object)) {
                    dataList.remove(data);
                }
            }
        }
        cleanMap(true);
    }
    
    public static void unregister(final Object object, final Class<? extends Event> eventClass) {
        if (EventManager.REGISTRY_MAP.containsKey(eventClass)) {
            for (final MethodData data : EventManager.REGISTRY_MAP.get(eventClass)) {
                if (data.getSource().equals(object)) {
                    EventManager.REGISTRY_MAP.get(eventClass).remove(data);
                }
            }
            cleanMap(true);
        }
    }
    
    private static void register(Method method, Object object) {
    	Class<? extends Event> indexClass = (Class<? extends Event>) method.getParameterTypes()[0];
    	//New MethodData from the Method we are registering.
    	final MethodData data = new MethodData(object, method, method.getAnnotation(EventTarget.class).value());
    	
    	//Set's the method to accessible so that we can also invoke it if it's protected or private.
    	if (!data.getTarget().isAccessible()) {
    		data.getTarget().setAccessible(true);
    	}
	
    	if (REGISTRY_MAP.containsKey(indexClass)) {
    		if (!REGISTRY_MAP.get(indexClass).contains(data)) {
    			REGISTRY_MAP.get(indexClass).add(data);
    			sortListValue(indexClass);
    		}
    	} else {
    		REGISTRY_MAP.put(indexClass, new CopyOnWriteArrayList<MethodData>() {
    			//Eclipse was bitching about a serialVersionUID.
    			private static final long serialVersionUID = 666L; {
    				add(data);
    			}
    		});
    	}
    }
    
    public static void removeEntry(final Class<? extends Event> indexClass) {
        final Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = EventManager.REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (mapIterator.next().getKey().equals(indexClass)) {
                mapIterator.remove();
                break;
            }
        }
    }
    
    public static void cleanMap(final boolean onlyEmptyEntries) {
        final Iterator<Map.Entry<Class<? extends Event>, List<MethodData>>> mapIterator = EventManager.REGISTRY_MAP.entrySet().iterator();
        while (mapIterator.hasNext()) {
            if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
                mapIterator.remove();
            }
        }
    }
    
    private static void sortListValue(final Class<? extends Event> indexClass) {
        final List<MethodData> sortedList = new CopyOnWriteArrayList<MethodData>();
        byte[] value_ARRAY;
        for (int length = (value_ARRAY = Priority.VALUE_ARRAY).length, i = 0; i < length; ++i) {
            final byte priority = value_ARRAY[i];
            for (final MethodData data : EventManager.REGISTRY_MAP.get(indexClass)) {
                if (data.getPriority() == priority) {
                    sortedList.add(data);
                }
            }
        }
        EventManager.REGISTRY_MAP.put(indexClass, sortedList);
    }
    
    private static boolean isMethodBad(final Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }
    
    private static boolean isMethodBad(final Method method, final Class<? extends Event> eventClass) {
        return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
    }
    
    public static final Event call(final Event event) {
        final List<MethodData> dataList = EventManager.REGISTRY_MAP.get(event.getClass());
        if (dataList != null) {
            if (event instanceof EventStoppable) {
                final EventStoppable stoppable = (EventStoppable)event;
                for (final MethodData data : dataList) {
                    invoke(data, event);
                    if (stoppable.isStopped()) {
                        break;
                    }
                }
            }
            else {
                for (final MethodData data2 : dataList) {
                    invoke(data2, event);
                }
            }
        }
        return event;
    }
    
    private static void invoke(final MethodData data, final Event argument) {
        try {
            data.getTarget().invoke(data.getSource(), argument);
        }
        catch (IllegalAccessException ex) {}
        catch (IllegalArgumentException ex2) {}
        catch (InvocationTargetException ex3) {}
    }
    
    private static final class MethodData
    {
        private final Object source;
        private final Method target;
        private final byte priority;
        
        public MethodData(final Object source, final Method target, final byte priority) {
            this.source = source;
            this.target = target;
            this.priority = priority;
        }
        
        public Object getSource() {
            return this.source;
        }
        
        public Method getTarget() {
            return this.target;
        }
        
        public byte getPriority() {
            return this.priority;
        }
    }
}
