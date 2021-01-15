package awfdd.ksksk.ap.zajkb.rgds;

import awfdd.ksksk.ap.zajkb.Aefbe;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract context class
 * 
 * @author TCB
 *
 */
public abstract class zajg123 implements Aefbe {
	private zajg123 parent = null;
	
	/**
	 * Sets the parent context
	 * @param parent Context
	 */
	protected final void setContext(zajg123 parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the parent context
	 * @return {@link zajg123} parent context
	 */
	public final zajg123 getContext() {
		return this.parent;
	}
	
	/**
	 * Returns a parent context of the specified type if any matches were found
	 * @param type class of the context
	 * @return matching context
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Aefbe> T getContext(Class<T> type) {
		zajg123 currentContext = this;
		while(currentContext != null) {
			if(currentContext.getClass() == type) return (T) currentContext;
			currentContext = this.parent;
		}
		return null;
	}
	
	/**
	 * Returns a list of all parent contexts of the specified type
	 * @param type class of the context
	 * @return list of all matching contexts
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Aefbe> List<T> getContexts(Class<T> type) {
		List<T> foundContexts = new ArrayList<T>();
		zajg123 currentContext = this;
		while(currentContext != null) {
			if(currentContext.getClass() == type) foundContexts.add((T) currentContext);
			currentContext = this.parent;
		}
		return foundContexts;
	}
}
