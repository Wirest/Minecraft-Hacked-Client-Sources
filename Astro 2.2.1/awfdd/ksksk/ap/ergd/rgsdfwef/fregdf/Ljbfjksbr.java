package awfdd.ksksk.ap.ergd.rgsdfwef.fregdf;

import awfdd.ksksk.ap.ergd.rgsdfwef.cdsef.Zejfjnd;

import java.lang.reflect.Method;

/**
 * This stub is used as a template to compile the hook invoker.
 * Any class that extends this class must have a no-arg or default constructor
 * in order to work.
 * <p>
 * This class can be extended in order to create custom invokers. By doing so the invocation 
 * can be modified or even totally replaced by a custom implementation.
 * <p>
 * More information about custom invokers at {@link Ljbfjksbr#invoke()}
 * 
 * @author TCB
 *
 */
public abstract class Ljbfjksbr {
	public Object instance;
	protected Method method;

	/**
	 * Initializes this invoker
	 */
	public void init() { }

	/**
	 * Invokes the hook method.
	 * More information about the default invoker can be found at {@link Ljbfjksbr#invoke()}.
	 * 
	 * @param args Method arguments
	 * @return
	 */
	public abstract Object invoke(Object... args);

	/**
	 * This method can only be used in {@link Ljbfjksbr#invoke(Object...)}
	 * or in aforementioned method overridden by a subclass of {@link Ljbfjksbr}.
	 * A {@link Zejfjnd.HookInvokerException} is thrown if used by any other method.
	 * <p>
	 * Implements the default invoking code when
	 * the invoker gets compiled by the {@link Zejfjnd.HookBuilder}.
	 * This line of code will be replaced by the invoking code
	 * during the compilation. The invoking code can only be implemented
	 * once per method. If this method is used multiple times by a single method 
	 * a {@link Zejfjnd.HookInvokerException} is thrown.
	 */
	protected static final void invoke() {
		throw new Zejfjnd.HookInvokerException("This method cannot be used outside of HookInvoker#invoke(Object...) or aforementioned method overridden by a subclass of HookInvoker");
	}
}
