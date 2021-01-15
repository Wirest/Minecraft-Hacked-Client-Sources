package awfdd.ksksk.ap.zajkb;

import awfdd.ksksk.ap.zajkb.rgds.zajg123;

/**
 * Any event implements this interface
 * 
 * @author TCB
 *
 */
public interface ZAhbhjer {
	/**
	 * Sets the context of this event
	 * @param context {@link zajg123} the context
	 * @return the instance this was called on
	 */
	public ZAhbhjer setContext(Aefbe context);

	/**
	 * Returns the casted context of this event if the type matches.
	 * Use null as parameter to get any context.
	 * @param type class of the context
	 * @return {@link zajg123} the context
	 */
	public <T extends Aefbe> T getContext(Class<T> type);
}
