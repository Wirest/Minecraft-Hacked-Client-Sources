package com.darkmagician6.eventapi;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.EventStoppable;
import com.darkmagician6.eventapi.types.Priority;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {
   private static final Map REGISTRY_MAP = new HashMap();

   public static void register(Object object) {
      Method[] var4;
      int var3 = (var4 = object.getClass().getDeclaredMethods()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Method method = var4[var2];
         if(!isMethodBad(method)) {
            register(method, object);
         }
      }

   }

   public static void register(Object object, Class eventClass) {
      Method[] var5;
      int var4 = (var5 = object.getClass().getDeclaredMethods()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Method method = var5[var3];
         if(!isMethodBad(method, eventClass)) {
            register(method, object);
         }
      }

   }

   public static void unregister(Object object) {
      Iterator var2 = REGISTRY_MAP.values().iterator();

      while(var2.hasNext()) {
         List dataList = (List)var2.next();
         Iterator var4 = dataList.iterator();

         while(var4.hasNext()) {
            EventManager.MethodData data = (EventManager.MethodData)var4.next();
            if(data.getSource().equals(object)) {
               dataList.remove(data);
            }
         }
      }

      cleanMap(true);
   }

   public static void unregister(Object object, Class eventClass) {
      if(REGISTRY_MAP.containsKey(eventClass)) {
         Iterator var3 = ((List)REGISTRY_MAP.get(eventClass)).iterator();

         while(var3.hasNext()) {
            EventManager.MethodData data = (EventManager.MethodData)var3.next();
            if(data.getSource().equals(object)) {
               ((List)REGISTRY_MAP.get(eventClass)).remove(data);
            }
         }

         cleanMap(true);
      }

   }

   private static void register(Method method, Object object) {
      Class indexClass = method.getParameterTypes()[0];
      final EventManager.MethodData data = new EventManager.MethodData(object, method, ((EventTarget)method.getAnnotation(EventTarget.class)).value());
      if(!data.getTarget().isAccessible()) {
         data.getTarget().setAccessible(true);
      }

      if(REGISTRY_MAP.containsKey(indexClass)) {
         if(!((List)REGISTRY_MAP.get(indexClass)).contains(data)) {
            ((List)REGISTRY_MAP.get(indexClass)).add(data);
            sortListValue(indexClass);
         }
      } else {
         REGISTRY_MAP.put(indexClass, new CopyOnWriteArrayList() {
            private static final long serialVersionUID = 666L;

            {
               this.add(data);
            }
         });
      }

   }

   public static void removeEntry(Class indexClass) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(mapIterator.hasNext()) {
         if(((Class)((Entry)mapIterator.next()).getKey()).equals(indexClass)) {
            mapIterator.remove();
            break;
         }
      }

   }

   public static void cleanMap(boolean onlyEmptyEntries) {
      Iterator mapIterator = REGISTRY_MAP.entrySet().iterator();

      while(true) {
         do {
            if(!mapIterator.hasNext()) {
               return;
            }
         } while(onlyEmptyEntries && !((List)((Entry)mapIterator.next()).getValue()).isEmpty());

         mapIterator.remove();
      }
   }

   private static void sortListValue(Class indexClass) {
      CopyOnWriteArrayList sortedList = new CopyOnWriteArrayList();
      byte[] var5 = Priority.VALUE_ARRAY;
      int var4 = Priority.VALUE_ARRAY.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         byte priority = var5[var3];
         Iterator var7 = ((List)REGISTRY_MAP.get(indexClass)).iterator();

         while(var7.hasNext()) {
            EventManager.MethodData data = (EventManager.MethodData)var7.next();
            if(data.getPriority() == priority) {
               sortedList.add(data);
            }
         }
      }

      REGISTRY_MAP.put(indexClass, sortedList);
   }

   private static boolean isMethodBad(Method method) {
      return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
   }

   private static boolean isMethodBad(Method method, Class eventClass) {
      return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
   }

   public static final Event call(Event event) {
      List dataList = (List)REGISTRY_MAP.get(event.getClass());
      if(dataList != null) {
         if(event instanceof EventStoppable) {
            EventStoppable data = (EventStoppable)event;
            Iterator var4 = dataList.iterator();

            while(var4.hasNext()) {
               EventManager.MethodData data1 = (EventManager.MethodData)var4.next();
               invoke(data1, event);
               if(data.isStopped()) {
                  break;
               }
            }
         } else {
            Iterator data3 = dataList.iterator();

            while(data3.hasNext()) {
               EventManager.MethodData data2 = (EventManager.MethodData)data3.next();
               invoke(data2, event);
            }
         }
      }

      return event;
   }

   private static void invoke(EventManager.MethodData data, Event argument) {
      try {
         data.getTarget().invoke(data.getSource(), new Object[]{argument});
      } catch (IllegalAccessException var3) {
         ;
      } catch (IllegalArgumentException var4) {
         ;
      } catch (InvocationTargetException var5) {
         ;
      }

   }

   private static final class MethodData {
      private final Object source;
      private final Method target;
      private final byte priority;

      public MethodData(Object source, Method target, byte priority) {
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
