package awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.HEfbhs;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.ZAhbhjefb;
import awfdd.wefsd.gsdbhfb.Zabehjfb;
import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.Fina1doesntstopcapping;

/**
 * This bus expander can expand any limited event bus that extends {@link Suge}
 * and thus removes the listener limit while still keeping all the functionality of the limited bus.
 * Any properties of the expanded bus have to be set before {@link Zabef#bind()} is called.
 * 
 * @author TCB
 *
 */
public class Zabef<B extends Suge> implements HEfbhs, JAJbef {
	private final ArrayList<B> busCollection = new ArrayList<B>();
	private final HashMap<Class<? extends Event>, List<B>> busMap = new HashMap<Class<? extends Event>, List<B>>();
	private final List<ZAhbhjefb> registeredMethodEntries = new ArrayList<ZAhbhjefb>();
	private Suge currentBus = null;
	private final int maxMethodEntriesPerBus;
	private boolean singleBus = true;
	private B busInstance;
	private Class<B> busType;

	/**
	 * Initializes a new {@link Zabef} with a maximum method context limit per bus of 50 method contexts (recommended).
	 */
	@SuppressWarnings("unchecked")
	public Zabef(B busInstance) {
		this.maxMethodEntriesPerBus = 50;
		this.busInstance = busInstance;
		this.busType = (Class<B>) busInstance.getClass();
	}

	/**
	 * Initializes a new {@link Zabef} with a specified maximum method context limit per bus.
	 * Maximum limit of method contexts per bus is {@link Suge#MAX_METHODS}
	 * @param maxMethodsPerBus Integer
	 */
	@SuppressWarnings("unchecked")
	public Zabef(B busInstance, int maxMethodsPerBus) {
		if(maxMethodsPerBus > Suge.MAX_METHODS) {
			maxMethodsPerBus = Suge.MAX_METHODS;
		} else if(maxMethodsPerBus < 1) {
			maxMethodsPerBus = 1;
		}
		this.maxMethodEntriesPerBus = maxMethodsPerBus;
		this.busInstance = busInstance;
		this.busType = (Class<B>) busInstance.getClass();
	}

	/**
	 * Returns the bus type this {@link Zabef} uses.
	 * @return
	 */
	public Class<B> getBusType() {
		return this.busType;
	}

	/**
	 * Registers a listener to the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for the new listener to take effect.
	 * <p>
	 * A {@link Fina1doesntstopcapping} is thrown if an invalid method has been found.
	 * @param listener {@link JAHfb} to register
	 * @throws Fina1doesntstopcapping
	 * @return {@link List} read-only list of all found valid method contexts
	 */
	public final List<ZAhbhjefb> registerAndAnalyze(JAHfb listener) throws Fina1doesntstopcapping {
		List<ZAhbhjefb> entries = ZAhbhjefb.getMethodContexts(listener);
		this.registeredMethodEntries.addAll(entries);
		return Collections.unmodifiableList(entries);
	}

	/**
	 * Registers a listener to the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for the new listener to take effect.
	 * <p>
	 * A {@link Fina1doesntstopcapping} is thrown if an invalid method has been found.
	 * @param listener {@link JAHfb} to register
	 * @throws Fina1doesntstopcapping
	 */
	@Override
	public final void register(JAHfb listener) throws Fina1doesntstopcapping {
		this.registerAndAnalyze(listener);
	}

	/**
	 * Unregisters an {@link JAHfb} from the {@link Zabef}. The event bus has to be updated with {@link Zabef#bind()} for this to take effect.
	 * Only unregisters the first occurrence of the specified listener.
	 * @param listener {@link JAHfb} to unregister
	 */
	@Override
	public final void unregister(JAHfb listener) {
		for(Method method : listener.getClass().getDeclaredMethods()) {
			Iterator<ZAhbhjefb> it = this.registeredMethodEntries.iterator();
			while(it.hasNext()) {
				ZAhbhjefb context = it.next();
				if(context.getMethod().equals(method) && context.getListener() == listener) {
					it.remove();
					break;
				}
			}
		}
	}

	/**
	 * Registers a single {@link ZAhbhjefb} to the {@link Zabef}. The event bus has to be updated with {@link Zabef#bind()} for the new {@link ZAhbhjefb} to take effect.
	 * @param context {@link ZAhbhjefb} to register
	 */
	public final void register(ZAhbhjefb context) {
		this.registeredMethodEntries.add(context);
	}

	/**
	 * Unregisters a {@link ZAhbhjefb} from the {@link Zabef}. The event bus has to be updated with {@link Zabef#bind()} for this to take effect.
	 * Only unregisters the first occurrence of the specified method context.
	 * @param methodEntry {@link ZAhbhjefb} to unregister
	 */
	public final void unregister(ZAhbhjefb context) {
		this.registeredMethodEntries.remove(context);
	}

	/**
	 * Compiles the internal event dispatcher and binds all registered listeners. Required for new method contexts or dispatcher to take effect.
	 * For optimal performance this method should be called after all listeners have been
	 * registered.
	 */
	@Override
	public final void bind() {
		this.busCollection.clear();
		this.busMap.clear();
		this.singleBus = true;
		for(List<ZAhbhjefb> mel : this.getSortedMethodEntries()) {
			@SuppressWarnings("unchecked")
			B bus = (B) this.busInstance.copyBus();
			this.currentBus = bus;
			for(ZAhbhjefb me : mel) {
				bus.register(me);
				List<B> busList = this.busMap.get(me.getEventClass());
				if(busList == null) {
					busList = new ArrayList<B>();
					this.busMap.put(me.getEventClass(), busList);
				}
				if(!busList.contains(bus)) busList.add(bus);
			}
			if(!this.busCollection.contains(bus)) this.busCollection.add(bus);
		}
		if(this.busCollection.size() > 0) {
			for(B bus : this.busCollection) {
				bus.bind();
			}
		} else {
			//Create empty bus
			@SuppressWarnings("unchecked")
			B emptyBus = (B) this.busInstance.copyBus();
			emptyBus.bind();
			this.busCollection.add(emptyBus);
			this.currentBus = emptyBus;
		}
	}

	/**
	 * Returns the read-only bus map. 
	 * The bus lists are grouped by event class.
	 * @return read-only bus map
	 */
	public final Map<Class<? extends Event>, List<B>> getBusMap() {
		return Collections.unmodifiableMap(this.busMap);
	}

	/**
	 * Returns the read-only bus list.
	 * @return read-only bus list
	 */
	public final List<Suge> getBusList() {
		return Collections.<Suge>unmodifiableList(this.busCollection);
	}

	/**
	 * Returns a list of all registered method contexts grouped by event type.
	 * @return List<List<MethodEntry>>
	 */
	private final List<List<ZAhbhjefb>> getSortedMethodEntries() {
		HashMap<Class<? extends Event>, List<ZAhbhjefb>> eventListenerMap = new HashMap<Class<? extends Event>, List<ZAhbhjefb>>();
		for(ZAhbhjefb me : this.registeredMethodEntries) {
			List<ZAhbhjefb> mel = eventListenerMap.get(me.getEventClass());
			if(mel == null) {
				mel = new ArrayList<ZAhbhjefb>();
				eventListenerMap.put(me.getEventClass(), mel);
			}
			mel.add(me);
		}
		int index = 0;
		Iterator<Entry<Class<? extends Event>, List<ZAhbhjefb>>> it = eventListenerMap.entrySet().iterator();
		List<List<ZAhbhjefb>> methodEntryList = new ArrayList<List<ZAhbhjefb>>();
		List<ZAhbhjefb> currentList = new ArrayList<ZAhbhjefb>();
		while(it.hasNext()) {
			List<ZAhbhjefb> mel = it.next().getValue();
			for(ZAhbhjefb me : mel) {
				if(index >= this.maxMethodEntriesPerBus) {
					methodEntryList.add(currentList);
					currentList = new ArrayList<ZAhbhjefb>();
					this.singleBus = false;
					index = 0;
				}
				currentList.add(me);
				index++;
			}
		}
		if(index != 0) methodEntryList.add(currentList);
		return methodEntryList;
	}

	/**
	 * The compilation node contains a tree structure that describes the compiled dispatcher methods.
	 * @return {@link Zabehjfb}
	 */
	public Zabehjfb getCompilationNode() {
		return this.busInstance.getZabehjfb();
	}

	@Override
	public final <T extends Event> T post(T event) {
		if(this.currentBus == null) {
			throw new NullPointerException("Bus has not been compiled");
		}
		if(this.singleBus) {
			event = this.currentBus.post(event);
		} else {
			/*List<B> busList = this.busMap.get(event.getClass());
			if(busList == null) {
				return event;
			}
			for(DRCEventBus bus : busList) {
				event = bus.post(event);
			}*/
			for(Suge bus : this.busCollection) {
				bus.post(event);
			}
		}
		return event;
	}
}
