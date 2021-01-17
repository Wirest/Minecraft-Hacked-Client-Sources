/**
 * 
 */
package skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main;

import java.lang.reflect.Method;

/**
 * @author Zeb
 * 
 */
public class MethodListener {

	private Object parent;
	private int priority = 2;
	private Class<? extends Event> eventClass;
	private Method method;

	/**
	 * @param parent
	 * @param eventClass
	 * @param method
	 * @param priority
	 */
	public MethodListener(Object parent, Class<? extends Event> eventClass, Method method, int priority){
		this.parent = parent;
		this.eventClass = eventClass;
		this.method = method;
		this.priority = priority;
	}
	
	/**
	 * @return the priority
	 */
	public int getPriority(){
		return this.priority;
	}

	/**
	 * @return the parent
	 */
	public Object getParent(){
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Object parent){
		this.parent = parent;
	}

	/**
	 * @return the eventClass
	 */
	public Class<? extends Event> getEventClass(){
		return eventClass;
	}

	/**
	 * @param eventClass
	 *            the eventClass to set
	 */
	public void setEventClass(Class<? extends Event> eventClass){
		this.eventClass = eventClass;
	}

	/**
	 * @return the method
	 */
	public Method getMethod(){
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(Method method){
		this.method = method;
	}
	
}
