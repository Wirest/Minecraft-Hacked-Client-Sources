package awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.ZAhbhjerCancellable;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

/**
 * This event bus implementation uses a {@link HashMap} to map the registered listeners to the event classes.
 * Using listeners that accept subclasses could slow down the event dispatching because the event types 
 * have to be compared during the dispatching.
 * 
 * @author TCB
 *
 */
public class JNkl implements HEfbhs {
	private final Map<Class<? extends Event>, List<ZAhbhjefb>> eventListenerMap = new HashMap<Class<? extends Event>, List<ZAhbhjefb>>();
	private final List<ZAhbhjefb> subclassListeners = new ArrayList<ZAhbhjefb>();

	@Override
	public void register(JAHfb listener) {
		List<ZAhbhjefb> methodEntries = ZAhbhjefb.getMethodContexts(listener);
		List<List<ZAhbhjefb>> modifiedLists = new ArrayList<List<ZAhbhjefb>>();
		boolean subclassListenersModified = false;
		for(ZAhbhjefb me : methodEntries) {
			List<ZAhbhjefb> listeners = this.eventListenerMap.get(me.getEventClass());
			if(listeners == null) {
				listeners = new ArrayList<ZAhbhjefb>();
				this.eventListenerMap.put(me.getEventClass(), listeners);
			}
			listeners.add(me);
			if(!modifiedLists.contains(listeners)) {
				modifiedLists.add(listeners);
			}
			if(me.getHandlerAnnotation().acceptSubclasses()) {
				//Calculate the correct maximum amount of subclass listeners
				int maxContainedEntriesSubclassList = 0;
				for(Entry<Class<? extends Event>, List<ZAhbhjefb>> regEntry : this.eventListenerMap.entrySet()) {
					for(ZAhbhjefb regMethodEntry : regEntry.getValue()) {
						if(regMethodEntry.getEventClass().equals(regEntry.getKey()) && regMethodEntry.getMethod().equals(me.getMethod()) && regMethodEntry.getListener() == me.getListener()) {
							maxContainedEntriesSubclassList++;
						}
					}
				}

				//Add to subclass listeners list
				int containedEntries = 0;
				for(ZAhbhjefb scl : this.subclassListeners) {
					if(scl.getMethod().equals(me.getMethod()) && scl.getListener() == me.getListener()) {
						containedEntries++;
					}
				}
				if(containedEntries < maxContainedEntriesSubclassList) {
					this.subclassListeners.add(me);
					subclassListenersModified = true;
				}
			}
		}
		if(subclassListenersModified) {
			modifiedLists.add(this.subclassListeners);
		}
		//Check for any registered subclasses of the event types and add listeners to that list
		Iterator<Entry<Class<? extends Event>, List<ZAhbhjefb>>> entryIterator = this.eventListenerMap.entrySet().iterator();
		while(entryIterator.hasNext()) {
			Entry<Class<? extends Event>, List<ZAhbhjefb>> entry = entryIterator.next();
			Class<? extends Event> eventClass = entry.getKey();
			List<ZAhbhjefb> methodEntryList = entry.getValue();
			for(ZAhbhjefb me : methodEntries) {
				if(me.getHandlerAnnotation().acceptSubclasses() && me.getEventClass().isAssignableFrom(eventClass) && !me.getEventClass().equals(eventClass)) {
					methodEntryList.add(me);
					if(!modifiedLists.contains(methodEntryList)) {
						modifiedLists.add(methodEntryList);
					}
				}
			}
		}
		//Update priorities
		Comparator<ZAhbhjefb> prioritySorter = new Comparator<ZAhbhjefb>() {
			@Override
			public int compare(ZAhbhjefb e1, ZAhbhjefb e2) {
				return e2.getHandlerAnnotation().priority() - e1.getHandlerAnnotation().priority();
			}
		};
		for(List<ZAhbhjefb> methodEntryList : modifiedLists) {
			Collections.sort(methodEntryList, prioritySorter);
		}
	}

	@Override
	public void unregister(JAHfb listener) {
		Method[] listenerMethods = listener.getClass().getDeclaredMethods();
		for(Method method : listenerMethods) {
			if(method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) continue;
			Subscribe handlerAnnotation = method.getAnnotation(Subscribe.class);
			if(handlerAnnotation != null) {
				int methodModifiers = method.getModifiers();
				if((methodModifiers & Modifier.STATIC) != 0 ||
						(methodModifiers & Modifier.ABSTRACT) != 0 ||
						(methodModifiers & Modifier.PRIVATE) != 0 ||
						(methodModifiers & Modifier.PROTECTED) != 0) {
					continue;
				}
				if(method.getReturnType() != void.class) {
					continue;
				}

				//Remove from regular map
				Iterator<Entry<Class<? extends Event>, List<ZAhbhjefb>>> mapEntryIT = this.eventListenerMap.entrySet().iterator();
				while(mapEntryIT.hasNext()) {
					Entry<Class<? extends Event>, List<ZAhbhjefb>> mapEntry = mapEntryIT.next();
					List<ZAhbhjefb> methodEntries = mapEntry.getValue();
					Iterator<ZAhbhjefb> methodEntryIterator = methodEntries.iterator();
					while(methodEntryIterator.hasNext()) {
						ZAhbhjefb me = methodEntryIterator.next();
						if(me.getMethod().equals(method) && me.getListener() == listener) {
							methodEntryIterator.remove();
							break;
						}
					}
					if(methodEntries.size() == 0) {
						mapEntryIT.remove();
					}
				}

				//Remove from subclass list
				Iterator<ZAhbhjefb> subclassListenersIT = this.subclassListeners.iterator();
				while(subclassListenersIT.hasNext()) {
					ZAhbhjefb entry = subclassListenersIT.next();
					if(entry.getMethod().equals(method) && entry.getListener() == listener) {
						subclassListenersIT.remove();
					}
				}
			}
		}
	}

	@Override
	public <T extends Event> T post(T event) {
		Class<?> eventClass = event.getClass();
		List<ZAhbhjefb> methodEntries = this.eventListenerMap.get(eventClass);
		ZAhbhjerCancellable eventCancellable = null;
		if(event instanceof ZAhbhjerCancellable) {
			eventCancellable = (ZAhbhjerCancellable) event;
		}
		boolean contained = false;
		if(methodEntries != null) {
			contained = true;
			for(ZAhbhjefb me : methodEntries) {
				if((me.getHandlerAnnotation().forced() || me.getListener().isEnabled()) && (me.getFilter() == null || me.getFilter().filter(event))) {
					try {
						me.invoke(event);
						if(eventCancellable != null && eventCancellable.isCancelled()) {
							return event;
						}
					} catch(Exception ex){
						throw new RuntimeException(ex);
					}
				}
			}
		}
		if(!contained) {
			for(ZAhbhjefb me : this.subclassListeners) {
				if((me.getHandlerAnnotation().forced() || me.getListener().isEnabled()) && (me.getFilter() == null || me.getFilter().filter(event))) {
					if(me.getEventClass() != eventClass) {
						try {
							me.invoke(event);
							if(eventCancellable != null && eventCancellable.isCancelled()) {
								return event;
							}
						} catch(Exception ex){
							throw new RuntimeException(ex);
						}
					}
				}
			}
		}
		return event;
	}
}
