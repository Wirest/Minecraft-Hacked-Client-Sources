package awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.Fina1doesntstopcapping;
import awfdd.ksksk.zabejhf.ihae.ehgewgr;

/**
 * The {@link ZAhbhjefb} holds information about a registered listener
 * and it's listening {@link Method}.
 */
public final class ZAhbhjefb {
	private static final String IFILTER_INIT = "init";

	private final Class<? extends Event> eventClass;
	private final JAHfb listener;
	private final Method method;
	private final Subscribe handlerAnnotation;
	private ehgewgr filter;

	/**
	 * The {@link ZAhbhjefb} holds information about a registered listener
	 * and it's listening {@link Method}.
	 * @param eventClass {@link Class}
	 * @param listener {@link JAHfb}
	 * @param method {@link Method}
	 * @param handlerAnnotation {@link Subscribe}
	 */
	private ZAhbhjefb(Class<? extends Event> eventClass, JAHfb listener, Method method, Subscribe handlerAnnotation) {
		this.eventClass = eventClass;
		this.listener = listener;
		this.method = method;
		this.handlerAnnotation = handlerAnnotation;
	}

	/**
	 * Returns the event type of this {@link ZAhbhjefb}.
	 * @return {@link Event}
	 */
	public Class<? extends Event> getEventClass() {
		return this.eventClass;
	}

	/**
	 * Returns the {@link JAHfb} instance.
	 * @return {@link JAHfb}
	 */
	public JAHfb getListener() {
		return this.listener;
	}

	/**
	 * Returns the {@link Method} this {@link ZAhbhjefb} belongs to.
	 * @return {@link Method}
	 */
	public Method getMethod() {
		return this.method;
	}

	/**
	 * Returns the {@link Subscribe} annotation that belongs to this
	 * {@link ZAhbhjefb}.
	 * @return {@link Subscribe}
	 */
	public Subscribe getHandlerAnnotation() {
		return this.handlerAnnotation;
	}

	/**
	 * Returns the {@link ehgewgr} that has been assigned to this {@link ZAhbhjefb}.
	 * @return {@link ehgewgr}
	 */
	public ehgewgr getFilter() {
		return this.filter;
	}

	/**
	 * Used to set a custom filter. If this method is used to set
	 * the filter instead of the {@link Subscribe#filter()} annotation member, a custom constructor
	 * can be used in the filter class. 
	 * <p>
	 * The method {@link ehgewgr#init(JAHfb)} will not be called if
	 * the filter was set via this method.
	 * @param filter {@link ehgewgr}
	 * @return {@link ZAhbhjefb}
	 */
	public ZAhbhjefb setFilter(ehgewgr filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * Invokes the listening method by reflection if the filter test is passed.
	 * @param event {@link Event}
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void invoke(Event event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(this.filter != null && !this.filter.filter(event)) return;
		this.method.invoke(this.listener, event);
	}

	/**
	 * This method sets the {@link ehgewgr} of the {@link ZAhbhjefb} that was specified
	 * with the {@link Subscribe#filter()} annotation member.
	 * <p>
	 * Throws a {@link Fina1doesntstopcapping} if the filter class is abstract or interface
	 * or doesn't have a no-arg constructor.
	 * @param context {@link ZAhbhjefb} to add the filter to
	 * @throws Fina1doesntstopcapping
	 * @return {@link ZAhbhjefb}
	 */
	public final ZAhbhjefb initFilter() throws Fina1doesntstopcapping {
		if(this.getFilter() != null) {
			return this;
		}
		Class<? extends ehgewgr> filterClass = this.handlerAnnotation.filter();
		if(filterClass == ehgewgr.class) {
			return this;
		}
		int classModifiers = filterClass.getModifiers();
		if((classModifiers & Modifier.ABSTRACT) != 0 ||
				(classModifiers & Modifier.INTERFACE) != 0) {
			throw new Fina1doesntstopcapping("Filter class must not be abstract or interface: " + filterClass.getName());
		}
		if((classModifiers & Modifier.PUBLIC) == 0) {
			throw new Fina1doesntstopcapping("Filter class must be public: " + filterClass.getName());
		}
		try {
			ehgewgr instance = null;
			Constructor<? extends ehgewgr> ctor = filterClass.getDeclaredConstructor();
			boolean accessible = ctor.isAccessible();
			ctor.setAccessible(true);
			instance = ctor.newInstance();
			if(!accessible) ctor.setAccessible(false);
			this.setFilter(instance);
			Method initMethod = filterClass.getDeclaredMethod(IFILTER_INIT, new Class[]{ZAhbhjefb.class});
			initMethod.invoke(instance, this);
		} catch(Exception ex) {
			throw new Fina1doesntstopcapping("Failed to initialize filter: " + filterClass.getName(), ex);
		}
		return this;
	}
	
	/**
	 * Verifies the specified listener and returns a list of all found valid method contexts.
	 * A SubscriptionException is thrown if an invalid method has been found.
	 * @param listener {@link JAHfb}
	 * @throws Fina1doesntstopcapping
	 * @return {@link List} of all found valid method contexts
	 */
	public static final List<ZAhbhjefb> getMethodContexts(JAHfb listener) throws Fina1doesntstopcapping {
		List<ZAhbhjefb> entryList = new ArrayList<ZAhbhjefb>();
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
					throw new Fina1doesntstopcapping("Invalid method modifiers for method " + listener.getClass().getName() + "#" + method.getName());
				}
				if(method.getReturnType() != void.class) {
					throw new Fina1doesntstopcapping("Return type is not void for method " + listener.getClass().getName() + "#" + method.getName());
				}
				@SuppressWarnings("unchecked")
				Class<? extends Event> paramType = (Class<? extends Event>) method.getParameterTypes()[0];
				if(paramType.isInterface()) {
					throw new Fina1doesntstopcapping("Parameter for method cannot be an interface: " + listener.getClass().getName() + "#" + method.getName());
				}
				entryList.add(new ZAhbhjefb(paramType, listener, method, handlerAnnotation).initFilter());
			}
		}
		return entryList;
	}
}
