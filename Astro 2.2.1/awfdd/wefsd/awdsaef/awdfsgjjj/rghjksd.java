package awfdd.wefsd.awdsaef.awdfsgjjj;

import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.Suge;
import awfdd.ksksk.ap.zajkb.rgds.Event;

/**
 * This dispatcher thread dispatches the events of a {@link ZAJKBehkjbf}.
 * The dispatcher thread automatically goes to sleep after {@link rghjksd#THREAD_SLEEP_DELAY}
 * if no event has been posted through the {@link ZAJKBehkjbf} if management was not set
 * to manual.
 * 
 * @author TCB
 *
 */
public class rghjksd extends Thread {
	private final Suge dispatcherBus;
	private final ZAJKBehkjbf bus;
	private boolean runDispatcher = true;
	private boolean sleeping = false;
	private long startTime;

	/**
	 * The delay until the thread goes to sleep automatically.
	 */
	public static final int THREAD_SLEEP_DELAY = 1000;

	/**
	 * Creates a new dispatcher.
	 * @param bus AsyncEventBus
	 * @param dispatcherBus EventBus
	 */
	protected rghjksd(ZAJKBehkjbf bus, Suge dispatcherBus) {
		this.bus = bus;
		this.dispatcherBus = dispatcherBus;
	}

	/**
	 * Returns the dispatcher bus for this dispatcher.
	 * @return EventBus
	 */
	protected final Suge getDispatcherBus() {
		return this.dispatcherBus;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Dispatcher Thread");
		this.startTime = System.currentTimeMillis();
		while(this.runDispatcher) {
			synchronized(this.bus.eventLock) {
				if(this.bus.getEventQueue().size() > 0) {
					try {
						Event event = this.bus.eventQueue.take();
						event = this.dispatcherBus.post(event);
						if(this.bus.feedbackHandler != null) {
							synchronized(this.bus.feedbackHandler) {
								this.bus.feedbackHandler.handleFeedback(event);
							}
						}
					} catch (InterruptedException e) { }
					this.startTime = System.currentTimeMillis();
				}
			}
			if(!this.bus.hasManualManagement() && this.runDispatcher &&
					this.bus.getEventQueue().size() == 0 &&
					this.bus.getEventQueueCancellable().size() == 0) {
				if(System.currentTimeMillis() - this.startTime >= THREAD_SLEEP_DELAY) {
					this.setSleeping(true);
				}
			}
		}
	}

	/**
	 * Returns whether this thread is currently sleeping.
	 * @return
	 */
	public synchronized boolean isSleeping() {
		return this.sleeping;
	}

	/**
	 * Sets the thread sleeping state.
	 * @param sleep
	 */
	public synchronized void setSleeping(boolean sleep) {
		if(sleep && !this.isSleeping()) {
			try {
				this.sleeping = true;
				this.bus.addToSleepers(this);
				synchronized(this) {
					this.wait();
				}
				if(this.sleeping) {
					this.bus.removeFromSleepers(this);
					this.sleeping = false;
				}
			} catch(Exception ex) { 
				ex.printStackTrace();
			}
		} else if(!sleep && this.isSleeping()) {
			synchronized(this) {
				this.notify();
			}
		}
	}

	/**
	 * Returns whether this thread is currently running or if it has terminated already.
	 * @return Boolean
	 */
	public synchronized boolean isRunning() {
		return this.isAlive() && this.runDispatcher && this.getState() != State.TERMINATED;
	}

	/**
	 * Starts the dispatcher.
	 */
	public synchronized void startDispatcher() {
		if(this.runDispatcher) {
			this.runDispatcher = true;
			this.start();
			this.setSleeping(false);
		}
	}

	/**
	 * Stops the dispatcher.
	 */
	public synchronized void stopDispatcher() {
		this.runDispatcher = false;
		this.setSleeping(false);
	}
}
