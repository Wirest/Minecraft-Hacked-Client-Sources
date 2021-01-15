package awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg;

import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.HEfbhs;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.JNkl;
import awfdd.wefsd.awdsaef.awdfsgjjj.ZAJKBehkjbf;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.Ljkdbn;
import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;

/**
 * The bus factory class lets the user easily create a simple event bus
 * 
 * @author TCB
 *
 */
public class KSJjihfe {
	public static final class WrappedEventBus implements HEfbhs, JAJbef {
		private final HEfbhs wrappedBus;

		private WrappedEventBus(HEfbhs wrappedBus) {
			this.wrappedBus = wrappedBus;
		}

		@Override
		public void register(JAHfb listener) {
			this.wrappedBus.register(listener);
		}

		@Override
		public void unregister(JAHfb listener) {
			this.wrappedBus.unregister(listener);
		}

		@Override
		public <T extends Event> T post(T event) {
			return this.wrappedBus.post(event);
		}

		@Override
		public final void bind() {
			if(this.wrappedBus instanceof JAJbef) {
				((JAJbef)this.wrappedBus).bind();
			}
		}
	}

	/**
	 * Creates a new DRC event bus
	 * 
	 * @return
	 */
	public static WrappedEventBus createDRCEventBus() {
		return new WrappedEventBus(new Zabef<>(new ZAJKBehkjbf(0)));
	}

	/**
	 * Creates a new mapped event bus
	 * 
	 * @return
	 */
	public static WrappedEventBus createMapEventBus() {
		return new WrappedEventBus(new JNkl());
	}

	/**
	 * Creates a new asynchronous event bus
	 * 
	 * @param threads Dispatcher threads
	 * @param feedbackHandler Feedback handler (can be null if not required)
	 * @return
	 */
	public static WrappedEventBus createAsyncEventBus(int threads, Ljkdbn feedbackHandler) {
		return new WrappedEventBus(new ZAJKBehkjbf(threads, false).setFeedbackHandler(feedbackHandler));
	}
}
