package awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import awfdd.wefsd.Abjwef;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.HEfbhs;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.rtdgf.ZAhbhjefb;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import awfdd.ksksk.dabehfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zAfbehjvf;
import awfdd.wefsd.gsdbhfb.Zabehjfb;
import awfdd.wefsd.gsdbhfb.Ahhhhhhh;
import awfdd.wefsd.gsdbhfb.AKjkef;
import awfdd.ksksk.ap.zajkb.rgds.Event;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventCancellable;
import awfdd.ksksk.ap.zajkb.ZAhbhjer;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.ZAhbhjerCancellable;
import awfdd.ksksk.zabejhf.rgsd.JAHfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.Fina1doesntstopcapping;
import awfdd.ksksk.zabejhf.ihae.ehgewgr;

/**
 * This event bus should only be used for a low amount of listening methods. The 
 * maximum amount of possible listening methods is {@link Suge#MAX_METHODS}.
 * Use {@link Zabef} for high amounts of listening methods.
 * <p>
 * This event bus supports listener priorities and an event filter.
 * The filter allows the user to filter and cancel any event before 
 * it's passed to its listener. The event itself will not be cancelled. 
 * Filters work on any type of listener or event, even
 * if the event is not a subclass of {@link EventCancellable}.
 * <p>
 * It is recommended to set a custom dispatcher to increase performance (for some reason
 * custom dispatchers seems to work a little bit faster). More information
 * about custom dispatchers at {@link Ahhhhhhh}, {@link Suge#setDispatcher(Class)} and
 *
 * 
 * @author TCB
 *
 */
public class Suge implements HEfbhs, Abjwef, JAJbef {
	/**
	 * The internal private dispatcher implementation
	 */
	private static final class AhhhhhhhImpl extends Ahhhhhhh {
		@Override
		public final <T extends Event> T dispatchEvent(T event) {
			return event;
		}

		@Override
		public void init(JAHfb[] listenerArray, ehgewgr[] filterArray) {
			this.listenerArray = listenerArray;
			this.filterArray = filterArray;
		}
	}

	/**
	 * Internal private data of a verified listening method
	 */
	private static final class InternalMethodContext {
		private final ZAhbhjefb context;
		private final JAHfb instance;
		private final Method method;
		private final String methodName;
		private final boolean forced;
		private final int priority;
		private final ehgewgr filter;
		private final Subscribe handlerAnnotation;
		private final Class<? extends Event> eventClass;
		public InternalMethodContext(ZAhbhjefb context, JAHfb instance, Method method, Subscribe handlerAnnotation, ehgewgr filter, Class<? extends Event> eventClass) {
			this.context = context;
			this.instance = instance;
			this.method = method;
			this.methodName = method.getName();
			this.handlerAnnotation = handlerAnnotation;
			this.forced = handlerAnnotation.forced();
			this.priority = handlerAnnotation.priority();
			this.filter = filter;
			this.eventClass = eventClass;
		}
	}

	/**
	 * The maximum amount of registered method contexts
	 */
	public static final int MAX_METHODS = 256;

	/**
	 * The method names used for reflection and bytecode construction
	 */
	private static final String DISPATCHER_DISPATCH_EVENT_INTERNAL = "dispatchEvent";
	private static final String DISPATCHER_LISTENER_ARRAY = "listenerArray";
	private static final String DISPATCHER_FILTER_ARRAY = "filterArray";
	private static final String IFILTER_FILTER = "filter";
	private static final String ILISTENER_IS_ENABLED = "isEnabled";
	private static final String IEVENTCANCELLABLE_IS_CANCELLED = "isCancelled";
	private static final String DISPATCHER_DISPATCH = "dispatch";

	/**
	 * Contains all registered listeners
	 */
	private final HashMap<Class<? extends Event>, List<InternalMethodContext>> registeredEntries = new HashMap<Class<? extends Event>, List<InternalMethodContext>>();

	/**
	 * Contains all registered listeners that accept subclasses of events (those listeners are also added to {@link Suge#registeredEntries})
	 */
	private final List<InternalMethodContext> subclassListeners = new ArrayList<InternalMethodContext>();

	/**
	 * Index lookup map, used for creating the bytecode
	 */
	private final HashMap<JAHfb, Integer> indexLookup = new HashMap<JAHfb, Integer>();

	/**
	 * Filter index lookup map, used for creating the bytecode
	 */
	private final HashMap<InternalMethodContext, Integer> filterIndexLookup = new HashMap<InternalMethodContext, Integer>();

	/**
	 * Contains all registered listeners
	 */
	private JAHfb[] listenerArray;

	/**
	 * Contains all filters
	 */
	private ehgewgr[] filterArray;

	/**
	 * The instance of the recompiled {@link AhhhhhhhImpl}
	 */
	private Ahhhhhhh ahhhhhhhImplInstance = null;

	/**
	 * The dispatcher class
	 */
	private Class<? extends Ahhhhhhh> dispatcherClass = AhhhhhhhImpl.class;

	/**
	 * The amount of registered MethodEntries
	 */
	private int methodCount = 0;

	/**
	 * The compilation data
	 */
	private Zabehjfb zabehjfb = new Zabehjfb("Compilation");

	/**
	 * The compiled dispatcher class bytes
	 */
	private byte[] dispatcherClassBytes;

	/**
	 * The default EventBus has a limit of {@link Suge#MAX_METHODS} listening methods.
	 * If you want to add more listening methods use {@link Zabef} instead.
	 * More information at {@link Suge}.
	 */
	public Suge() {
			}

	/**
	 * Private constructor for copy method
	 */
	private Suge(Class<? extends Ahhhhhhh> dispatcherClass, Zabehjfb zabehjfb) {
		this.dispatcherClass = dispatcherClass;
		this.zabehjfb = zabehjfb;
	}

	/**
	 * Registers a list of {@link ZAhbhjefb} to the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for the new {@link JAHfb}s to take effect.
	 * <p>
	 * The default event bus has a limit of {@link Suge#MAX_METHODS} method contexts. If more than {@link Suge#MAX_METHODS} method contexts are registered an {@link IndexOutOfBoundsException} is thrown.
	 * @param context {@link List} of {@link ZAhbhjefb} to register
	 * @throws IndexOutOfBoundsException
	 */
	public final void register(List<ZAhbhjefb> methodList) throws IndexOutOfBoundsException {
		//Check for array bounds
		if(this.methodCount > MAX_METHODS) {
			throw new IndexOutOfBoundsException("Too many registered methods. Max: " + MAX_METHODS);
		} else if(this.methodCount + methodList.size() > MAX_METHODS) {
			throw new IndexOutOfBoundsException("Registering this listener exceeds the maximum " +
					"amount of registered methods. " +
					"Current: " + this.methodCount +
					" Max: " + MAX_METHODS);
		}

		//Add all method contexts
		for(ZAhbhjefb context : methodList) {
			Class<? extends Event> paramType = (Class<? extends Event>) context.getEventClass();
			List<InternalMethodContext> lle = this.registeredEntries.get(paramType);
			if(lle == null) {
				lle = new ArrayList<InternalMethodContext>();
				this.registeredEntries.put(paramType, lle);
			}
			lle.add(new InternalMethodContext(context, context.getListener(), context.getMethod(), context.getHandlerAnnotation(), context.getFilter(), context.getEventClass()));
			this.methodCount++;
		}

		this.updateArrays();
	}

	/**
	 * Registers a listener to the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for the new listener to take effect.
	 * <p>
	 * The default EventBus has a limit of {@link Suge#MAX_METHODS} listening methods. If more than {@link Suge#MAX_METHODS} listening methods
	 * are registered an IndexOutOfBoundsException is thrown.
	 * <p>
	 * A {@link Fina1doesntstopcapping} is thrown if an invalid method has been found.
	 * @param listener {@link JAHfb} to register
	 * @throws Fina1doesntstopcapping
	 * @throws IndexOutOfBoundsException
	 * @return {@link List} read-only list of all found valid method contexts
	 */
	public final List<ZAhbhjefb> registerAndAnalyze(JAHfb listener) throws Fina1doesntstopcapping, IndexOutOfBoundsException {
		List<ZAhbhjefb> contextList = ZAhbhjefb.getMethodContexts(listener);
		this.register(contextList);
		return Collections.unmodifiableList(contextList);
	}

	/**
	 * Registers a single {@link ZAhbhjefb} to the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for the new {@link JAHfb} to take effect.
	 * <p>
	 * The default event bus has a limit of {@link Suge#MAX_METHODS} method contexts. If more than {@link Suge#MAX_METHODS} method contexts are registered an {@link IndexOutOfBoundsException} is thrown.
	 * @param context {@link ZAhbhjefb} to register
	 * @throws IndexOutOfBoundsException
	 */
	public final void register(ZAhbhjefb context) throws IndexOutOfBoundsException {
		List<ZAhbhjefb> contextList = new ArrayList<ZAhbhjefb>();
		contextList.add(context);
		this.register(contextList);
	}

	/**
	 * Registers a listener to the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for the new listener to take effect.
	 * <p>
	 * The default EventBus has a limit of {@link Suge#MAX_METHODS} listening methods. If more than {@link Suge#MAX_METHODS} listening methods
	 * are registered an {@link IndexOutOfBoundsException} is thrown.
	 * <p>
	 * A {@link Fina1doesntstopcapping} is thrown if an invalid method has been found.
	 * @param listener {@link JAHfb} to register
	 * @throws Fina1doesntstopcapping
	 * @throws IndexOutOfBoundsException
	 */
	@Override
	public final void register(JAHfb listener) throws Fina1doesntstopcapping, IndexOutOfBoundsException {
		this.registerAndAnalyze(listener);
	}

	/**
	 * Returns a read-only list of all registered method contexts.
	 * @return {@link List} read-only list of all registered method contexts
	 */
	public final List<ZAhbhjefb> getMethodEntries() {
		List<ZAhbhjefb> result = new ArrayList<ZAhbhjefb>();
		for(Entry<Class<? extends Event>, List<InternalMethodContext>> e : this.registeredEntries.entrySet()) {
			for(InternalMethodContext lme : e.getValue()) {
				result.add(lme.context);
			}
		}
		return Collections.unmodifiableList(result);
	}


	/**
	 * Unregisters an {@link JAHfb} from the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for this to take effect.
	 * Only unregisters the first occurrence of the specified listener.
	 * @param listener {@link JAHfb} to unregister
	 */
	@Override
	public final void unregister(JAHfb listener) {
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
				Iterator<Entry<Class<? extends Event>, List<InternalMethodContext>>> entryIterator = this.registeredEntries.entrySet().iterator();
				while(entryIterator.hasNext()) {
					Entry<Class<? extends Event>, List<InternalMethodContext>> entry = entryIterator.next();
					Class<? extends Event> eventClassGroup = entry.getKey();
					List<InternalMethodContext> imeList = entry.getValue();
					Iterator<InternalMethodContext> imeIterator = imeList.iterator();
					boolean removed = false;
					while(imeIterator.hasNext()) {
						InternalMethodContext ime = imeIterator.next();
						if(ime.method.equals(method) && ime.instance == listener) {
							imeIterator.remove();
							if(ime.method.getParameterTypes()[0].equals(eventClassGroup)) {
								this.methodCount--;
								if(imeList.size() == 0) {
									entryIterator.remove();
									removed = true;
								}
								break;
							}
						}
					}
					if(!removed && imeList.size() == 0) {
						entryIterator.remove();
					}
				}
			}
		}
		this.updateArrays();
	}

	/**
	 * Unregisters a {@link ZAhbhjefb} from the {@link Suge}. The event bus has to be updated with {@link Suge#bind()} for this to take effect.
	 * Only unregisters the first occurrence of the specified method context.
	 * @param context {@link ZAhbhjefb} to unregister
	 */
	public final void unregister(ZAhbhjefb context) {
		Method method = context.getMethod();
		Iterator<Entry<Class<? extends Event>, List<InternalMethodContext>>> entryIterator = this.registeredEntries.entrySet().iterator();
		while(entryIterator.hasNext()) {
			Entry<Class<? extends Event>, List<InternalMethodContext>> entry = entryIterator.next();
			Class<? extends Event> eventClassGroup = entry.getKey();
			List<InternalMethodContext> imeList = entry.getValue();
			Iterator<InternalMethodContext> imeIterator = imeList.iterator();
			while(imeIterator.hasNext()) {
				InternalMethodContext ime = imeIterator.next();
				if(ime.method.equals(method) && ime.instance == context.getListener()) {
					imeIterator.remove();
					if(ime.method.getParameterTypes()[0].equals(eventClassGroup)) {
						this.methodCount--;
						if(imeList.size() == 0) {
							entryIterator.remove();
						}
						break;
					}
				}
			}
			if(imeList.size() == 0) {
				entryIterator.remove();
			}
		}
	}

	/**
	 * Removes all registered listeners from this {@link Suge}.
	 */
	public final void clear() {
		this.registeredEntries.clear();
		this.indexLookup.clear();
		this.filterIndexLookup.clear();
		this.updateArrays();
	}

	/**
	 * Returns a read-only list of all registered listeners.
	 * @return {@link List} read-only list of all registered listeners
	 */
	public final List<JAHfb> getListeners() {
		ArrayList<JAHfb> listeners = new ArrayList<JAHfb>();
		for(Entry<Class<? extends Event>, List<InternalMethodContext>> e : this.registeredEntries.entrySet()) {
			for(InternalMethodContext le : e.getValue()) {
				listeners.add(le.instance);
			}
		}
		return Collections.unmodifiableList(listeners);
	}

	/**
	 * Updates the listener array and index lookup map.
	 */
	private final void updateArrays() {
		this.indexLookup.clear();
		this.filterIndexLookup.clear();
		ArrayList<JAHfb> arrayListenerList = new ArrayList<JAHfb>();
		ArrayList<ehgewgr> arrayFilterList = new ArrayList<ehgewgr>();
		for(Entry<Class<? extends Event>, List<InternalMethodContext>> e : this.registeredEntries.entrySet()) {
			List<InternalMethodContext> listenerEntryList = e.getValue();
			for(InternalMethodContext listenerEntry : listenerEntryList) {
				arrayListenerList.add(listenerEntry.instance);
				this.indexLookup.put(listenerEntry.instance, arrayListenerList.size() - 1);
				if(listenerEntry.filter != null) {
					arrayFilterList.add(listenerEntry.filter);
					this.filterIndexLookup.put(listenerEntry, arrayFilterList.size() - 1);
				}
			}
		}
		this.listenerArray = arrayListenerList.toArray(new JAHfb[0]);
		this.filterArray = arrayFilterList.toArray(new ehgewgr[0]);
	}

	/**
	 * Returns the recompiled version of {@link AhhhhhhhImpl}
	 * @return {@link Ahhhhhhh}
	 */
	private final Ahhhhhhh getCompiledDispatcher() {
		if(this.ahhhhhhhImplInstance == null)  {
			this.ahhhhhhhImplInstance = (Ahhhhhhh)this.compileDispatcher();
		}
		return this.ahhhhhhhImplInstance;
	}

	/**
	 * Recompiles {@link AhhhhhhhImpl} with all registered listeners.
	 * @return {@link Ahhhhhhh}
	 */
	private final synchronized Ahhhhhhh compileDispatcher() {
		try {
			//Check for available events for method contexts that accept subclasses and add to the list if available
			this.subclassListeners.clear();
			for(Entry<Class<? extends Event>, List<InternalMethodContext>> mapEntry : this.registeredEntries.entrySet()) {
				List<InternalMethodContext> entryList = mapEntry.getValue();
				for(InternalMethodContext entry : entryList) {
					if(entry.handlerAnnotation.acceptSubclasses()) {
						//Calculate the correct maximum amount of subclass listeners
						int maxContainedEntriesSubclassList = 0;
						for(Entry<Class<? extends Event>, List<InternalMethodContext>> regEntry : this.registeredEntries.entrySet()) {
							for(InternalMethodContext regMethodEntry : regEntry.getValue()) {
								if(regMethodEntry.eventClass.equals(regEntry.getKey()) && regMethodEntry.method.equals(entry.method) && regMethodEntry.instance == entry.instance) {
									maxContainedEntriesSubclassList++;
								}
							}
						}

						//Add to subclass listeners list
						int containedEntries = 0;
						for(InternalMethodContext ime : this.subclassListeners) {
							if(ime.method.equals(entry.method) && ime.instance == entry.instance) {
								containedEntries++;
							}
						}
						if(containedEntries < maxContainedEntriesSubclassList) {
							this.subclassListeners.add(entry);
						}

						//Check if any event subclasses are already available and can be registered in the normal registeredEntries map
						for(Class<? extends Event> eventClass : this.registeredEntries.keySet()) {
							if(entry.eventClass.isAssignableFrom(eventClass) && !entry.eventClass.equals(eventClass)) {
								//Calculate the maximum correct amount of static subclass listeners
								int maxContainedEntries = 0;
								for(Entry<Class<? extends Event>, List<InternalMethodContext>> regEntry : this.registeredEntries.entrySet()) {
									for(InternalMethodContext regMethodEntry : regEntry.getValue()) {
										if(regEntry.getKey().equals(regMethodEntry.eventClass) && regMethodEntry.method.equals(entry.method) && regMethodEntry.instance == entry.instance) {
											maxContainedEntries++;
										}
									}
								}

								//Add to static lists
								List<InternalMethodContext> imeList = this.registeredEntries.get(eventClass);
								containedEntries = 0;
								for(InternalMethodContext ime : imeList) {
									if(!eventClass.equals(ime.eventClass) && ime.method.equals(entry.method) && ime.instance == entry.instance) {
										containedEntries++;
									}
								}
								if(containedEntries < maxContainedEntries) {
									if(imeList != entryList) {
										imeList.add(entry);
									}
								}
							}
						}
					}
				}
			}

			//Sort by priority
			Comparator<InternalMethodContext> prioritySorter = new Comparator<InternalMethodContext>() {
				@Override
				public int compare(InternalMethodContext e1, InternalMethodContext e2) {
					return e2.priority - e1.priority;
				}
			};
			for(List<InternalMethodContext> lle : this.registeredEntries.values()) {
				Collections.sort(lle, prioritySorter);
			}
			Collections.sort(this.subclassListeners, prioritySorter);

			//Instrumentation classloader
			zAfbehjvf<Ahhhhhhh> instrumentationClassLoader = new zAfbehjvf<Ahhhhhhh>(this.getClass().getClassLoader(), Suge.this.dispatcherClass) {
				@Override
				protected byte[] instrument(byte[] bytecode) {
					ClassReader classReader = new ClassReader(bytecode);
					ClassNode classNode = new ClassNode();
					classReader.accept(classNode, ClassReader.SKIP_FRAMES);
					Zabehjfb mainNode = new Zabehjfb(Suge.this.toString());
					Suge.this.zabehjfb.addChild(mainNode);
					for(MethodNode methodNode : (List<MethodNode>) classNode.methods) {
						if(methodNode.name.equals(DISPATCHER_DISPATCH_EVENT_INTERNAL)) {
							instrumentDispatcher(methodNode, true, mainNode);
						}
					}
					Suge.this.zabehjfb = mainNode;
					ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
					classNode.accept(classWriter);
					Suge.this.dispatcherClassBytes = classWriter.toByteArray();
					return Suge.this.dispatcherClassBytes;
				}
			};
			Ahhhhhhh ahhhhhhh = instrumentationClassLoader.createInstance(null);
			ahhhhhhh.init(this.listenerArray, this.filterArray);
			return ahhhhhhh;
		} catch(Exception ex) {
			throw new AKjkef("Could not initialize dispatcher", ex);
		}
	}

	/**
	 * Modifies the internal event dispatching method.
	 * @param methodNode {@link MethodNode}
	 */
	private final synchronized void instrumentDispatcher(MethodNode methodNode, boolean cancellable, Zabehjfb mainNode) {
		InsnList methodInstructionSet = methodNode.instructions;
		ArrayList<AbstractInsnNode> instructionSet = new ArrayList<AbstractInsnNode>();
		Iterator<AbstractInsnNode> it = methodInstructionSet.iterator();
		AbstractInsnNode insn;
		AbstractInsnNode implementationNode = null;
		boolean isNodeDispatcher = false;
		while((insn = it.next()) != null && it.hasNext()) {
			boolean isReturn = this.dispatcherClass == AhhhhhhhImpl.class &&
					(insn.getOpcode() == Opcodes.IRETURN || 
					insn.getOpcode() == Opcodes.LRETURN ||
					insn.getOpcode() == Opcodes.RETURN ||
					insn.getOpcode() == Opcodes.ARETURN ||
					insn.getOpcode() == Opcodes.DRETURN ||
					insn.getOpcode() == Opcodes.FRETURN);
			boolean isDispatcherMethod = this.dispatcherClass != AhhhhhhhImpl.class && insn.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode)insn).name.equals(DISPATCHER_DISPATCH) && ((MethodInsnNode)insn).owner.equals(Ahhhhhhh.class.getName().replace(".", "/"));
			//Only implement first dispatcher, throw an error if there are multiple implementations
			if(isDispatcherMethod && implementationNode != null) {
				throw new AKjkef("The dispatching implementation Dispatcher#dispatch() can only be used once per method");
			}
			if(isReturn || isDispatcherMethod) {
				isNodeDispatcher = isDispatcherMethod;
				implementationNode = insn;
			}
		}

		//No implementation or return node
		if(implementationNode == null) {
			return;
		}

		LabelNode exitNode = new LabelNode();

		Zabehjfb eventNode = new Zabehjfb((cancellable ? "Cancellable" : "Non cancellable") + " events");
		mainNode.addChild(eventNode);

		//Scope start for contained variable
		LabelNode containedVarStart = new LabelNode();
		instructionSet.add(containedVarStart);

		//Add the contained variable
		int containedVarID = methodNode.localVariables.size();
		LocalVariableNode containedVarNode = new LocalVariableNode("contained", "Z", null, containedVarStart, exitNode, containedVarID);
		methodNode.localVariables.add(containedVarNode);
		//Set contained variable to false
		instructionSet.add(new InsnNode(Opcodes.ICONST_0));
		instructionSet.add(new VarInsnNode(Opcodes.ISTORE, containedVarID));

		/*
		 * Pseudo code, runs for every listener method:
		 * 
		 * //Optional check, only present if subclasses are also accepted
		 * if(event instanceof listenerArray[n].eventType) {
		 * //Replacement for when subclasses are not accepted
		 * if(event.getClass() == listenerArray[n].eventType) {
		 * 
		 *   //Optional check, only present if filter is not default IFilter class
		 *   if(filterArray[p].filter(listenerArray[n], event) {
		 *   
		 *     //Optional check, only present if the compiled method with the cancellable code is used
		 *     if(event instanceof IEventCancellable == false ||
		 *        !((IEventCancellable)event).isCancelled()) {
		 *        
		 *       //Invokes the listener method
		 *       listenerArray[n].invokeMethod(event);
		 *       
		 *       //Optional check, only present if the compiled method with the cancellable code is used
		 *       if(event instanceof IEventCancellable) {
		 *       
		 *         //Optional check, only present if the compiled method with the cancellable code is used
		 *         if(((IEventCancellable)event).isCancelled()) return;
		 *         
		 *       }
		 *     }
		 *   }
		 *   
		 * }
		 * 
		 * ...
		 */

		for(Entry<Class<? extends Event>, List<InternalMethodContext>> e : this.registeredEntries.entrySet()) {
			String eventClassGroup = dabehfb.getClassType(e.getKey());

			Zabehjfb eventClassNode = new Zabehjfb(e.getKey().getName());
			eventNode.addChild(eventClassNode);

			Zabehjfb staticListenersNode = new Zabehjfb("Static Listeners");
			eventClassNode.addChild(staticListenersNode);

			//Fail label, jumped to if class comparison fails
			LabelNode classCompareFailLabelNode = new LabelNode();

			//if(event.getClass() != listenerArray[n].eventType) -> jump to classCompareFailLabelNode
			instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 1));
			instructionSet.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;"));
			String eventClassNameClass = "L" + eventClassGroup + ";";
			instructionSet.add(new LdcInsnNode(Type.getType(eventClassNameClass)));
			instructionSet.add(new JumpInsnNode(Opcodes.IF_ACMPNE, classCompareFailLabelNode));

			//Set contained variable to true
			instructionSet.add(new InsnNode(Opcodes.ICONST_1));
			instructionSet.add(new VarInsnNode(Opcodes.ISTORE, containedVarID));

			for(InternalMethodContext listenerEntry : e.getValue()) {
				//Instrument dispatcher for listener
				this.instrumentSingleDispatcher(instructionSet, listenerEntry, 
						listenerEntry.eventClass, cancellable, exitNode, staticListenersNode);
			}

			//Jumped to if class comparison fails
			instructionSet.add(classCompareFailLabelNode);
		}

		Zabehjfb dynamicListenersNode = new Zabehjfb("Dynamic Listeners");
		mainNode.addChild(dynamicListenersNode);

		//Jump to exit if contained == true
		instructionSet.add(new VarInsnNode(Opcodes.ILOAD, containedVarID));
		instructionSet.add(new JumpInsnNode(Opcodes.IFNE, exitNode));

		for(InternalMethodContext listenerEntry : this.subclassListeners) {
			//Instrument dispatcher for listener
			this.instrumentSingleDispatcher(instructionSet, listenerEntry,
					listenerEntry.eventClass, cancellable, exitNode, dynamicListenersNode);
		}


		//Jumped to if an event was cancelled or contained == true
		instructionSet.add(exitNode);

		//Instrumentation callback to let the user modify the dispatching bytecode
		if(this.instrumentDispatcher(instructionSet, methodNode)) {
			for(AbstractInsnNode insnNode : instructionSet) {
				methodInstructionSet.insertBefore(implementationNode, insnNode);
			}
			if(isNodeDispatcher) {
				methodInstructionSet.remove(implementationNode);
			}
		}
		methodNode.visitMaxs(0, 0);
	}

	/**
	 * Instruments the dispatcher for a listener entry
	 * @param instructionSet
	 * @param listenerEntry
	 * @param entryList
	 * @param eventClassName
	 * @param cancellable
	 * @param exitLabelNode
	 */
	private final synchronized void instrumentSingleDispatcher(ArrayList<AbstractInsnNode> instructionSet, InternalMethodContext listenerEntry,
															   Class<? extends ZAhbhjer> eventClass, boolean cancellable, LabelNode exitLabelNode, Zabehjfb zabehjfb) {
		String eventClassName = dabehfb.getClassType(eventClass);
		String dispatcherClassName = dabehfb.getClassType(this.dispatcherClass);
		String listenerArrayType = dabehfb.getArrayObjectType(JAHfb.class);
		String listenerClassName = dabehfb.getClassType(listenerEntry.instance.getClass());
		String listenerMethodName = listenerEntry.methodName;
		String listenerMethodType = dabehfb.getListenerMethodType(eventClassName);
		int listenerIndex = this.indexLookup.get(listenerEntry.instance);

		Zabehjfb listenerNode = new Zabehjfb(listenerEntry.instance.getClass().getName() + "#" + listenerEntry.methodName);
		zabehjfb.addChild(listenerNode);

		Zabehjfb indexNode = new Zabehjfb("Class index: " + listenerIndex);
		listenerNode.addChild(indexNode);

		Zabehjfb priorityNode = new Zabehjfb("Priority: " + listenerEntry.priority);
		listenerNode.addChild(priorityNode);

		//Used for filter fail jump or subclass check fail jump (only if subclasses are not accepted)
		LabelNode entryFailLabelNode = new LabelNode();

		//Only implement if filter is not default IFilter class
		//if(!filterArray[n].filter(listenerArray[p], event)) -> jump to entryFailLabelNode
		if(listenerEntry.filter != null) {
			int filterIndex = this.filterIndexLookup.get(listenerEntry);
			String filterClassName = dabehfb.getClassType(listenerEntry.filter.getClass());
			String filterFieldType = dabehfb.getArrayObjectType(ehgewgr.class);
			String filterMethodType = dabehfb.getFilterMethodType();

			//get filter array
			instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 0));
			instructionSet.add(new FieldInsnNode(Opcodes.GETFIELD, dispatcherClassName, DISPATCHER_FILTER_ARRAY, filterFieldType));
			//push index onto stack
			instructionSet.add(dabehfb.getOptimizedIndex(filterIndex));
			//load filter from array and cast filter type
			instructionSet.add(new InsnNode(Opcodes.AALOAD));
			instructionSet.add(new TypeInsnNode(Opcodes.CHECKCAST, filterClassName));

			//load event and invoke filter, return boolean
			instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 1));
			instructionSet.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, filterClassName, IFILTER_FILTER, filterMethodType));

			//jump if false was returned
			instructionSet.add(new JumpInsnNode(Opcodes.IFEQ, entryFailLabelNode));
		}

		if(cancellable && ZAhbhjerCancellable.class.isAssignableFrom(eventClass)) {
			//load and cast event to EventCancellable
			instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 1));
			instructionSet.add(new TypeInsnNode(Opcodes.CHECKCAST, eventClassName));

			//invoke EventCancellable#isCancelled
			instructionSet.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, eventClassName, IEVENTCANCELLABLE_IS_CANCELLED, "()Z"));

			//if(!EventCancellable#isCancelled()) -> jump to exitNode
			instructionSet.add(new JumpInsnNode(Opcodes.IFNE, exitLabelNode));
		}

		//Only implement IListener#isEnabled() check if Receiver#forced() is false
		if(!listenerEntry.forced) {
			////////////////////////////// Check if listener is enabled //////////////////////////////
			//get listener array
			instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 0));
			instructionSet.add(new FieldInsnNode(Opcodes.GETFIELD, dispatcherClassName, DISPATCHER_LISTENER_ARRAY, listenerArrayType));
			//push index onto stack
			instructionSet.add(dabehfb.getOptimizedIndex(listenerIndex));
			//load listener from array and cast listener type
			instructionSet.add(new InsnNode(Opcodes.AALOAD));
			instructionSet.add(new TypeInsnNode(Opcodes.CHECKCAST, listenerClassName));

			//invoke isEnabled, return boolean
			instructionSet.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, listenerClassName, ILISTENER_IS_ENABLED, "()Z"));

			//jump to failLabelNode if returned boolean is false
			instructionSet.add(new JumpInsnNode(Opcodes.IFEQ, entryFailLabelNode));
		}

		///////////////////////////////// Invoke listener method /////////////////////////////////
		//get listener array
		instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 0));
		instructionSet.add(new FieldInsnNode(Opcodes.GETFIELD, dispatcherClassName, DISPATCHER_LISTENER_ARRAY, listenerArrayType));
		//push index onto stack
		instructionSet.add(dabehfb.getOptimizedIndex(listenerIndex));
		//load listener from array and cast listener type
		instructionSet.add(new InsnNode(Opcodes.AALOAD));
		instructionSet.add(new TypeInsnNode(Opcodes.CHECKCAST, listenerClassName));

		//load parameter and cast parameter type (the event to post)
		instructionSet.add(new IntInsnNode(Opcodes.ALOAD, 1));
		instructionSet.add(new TypeInsnNode(Opcodes.CHECKCAST, eventClassName));

		//invoke method
		instructionSet.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, listenerClassName, listenerMethodName, listenerMethodType));

		Zabehjfb forcedNode = new Zabehjfb((!listenerEntry.forced ? "[ ]" : "[x]") + " Forced");
		listenerNode.addChild(forcedNode);

		if(listenerEntry.filter != null) {
			Zabehjfb filterNode = new Zabehjfb("[x] Filter");
			listenerNode.addChild(filterNode);
			Zabehjfb filterNameNode = new Zabehjfb(listenerEntry.filter.getClass().getName());
			filterNode.addChild(filterNameNode);
		} else {
			Zabehjfb filterNode = new Zabehjfb("[ ] Filter");
			listenerNode.addChild(filterNode);
		}

		instructionSet.add(entryFailLabelNode);
	}

	/**
	 * Posts an {@link Event} and returns the posted event.
	 * @param event {@link Event} to dispatch
	 * @return {@link Event} the posted event
	 */
	@Override
	public <T extends Event> T post(T event) {
		return this.getCompiledDispatcher().dispatchEvent(event);
	}

	/**
	 * Compiles the internal event dispatcher and binds all registered listeners. Required for new method contexts or dispatcher to take effect.
	 * For optimal performance this method should be called after all listeners have been
	 * registered.
	 */
	@Override
	public void bind() {
		this.ahhhhhhhImplInstance = (Ahhhhhhh)this.compileDispatcher();
	}

	/**
	 * Sets the event dispatcher class.
	 * Set to null to enable the internal dispatcher of {@link Suge}.
	 * <p>
	 * More information about custom dispatchers at {@link Ahhhhhhh} and {@link Ahhhhhhh#dispatch()}
	 * <p>
	 * The event bus has to be updated with {@link Suge#bind()} for the new dispatcher to take effect.
	 * @param dispatcherClass {@link Ahhhhhhh}
	 * @throws AKjkef
	 */
	public final void setDispatcher(Class<? extends Ahhhhhhh> dispatcherClass) throws AKjkef {
		try {
			if(dispatcherClass != null) {
				if(dispatcherClass.getDeclaredConstructor() != null) {
					this.dispatcherClass = dispatcherClass;
				}
			} else {
				this.dispatcherClass = AhhhhhhhImpl.class;
			}
		} catch(Exception ex) {
			throw new AKjkef("Could not set dispatcher", ex);
		}
	}

	/**
	 * Returns the compiled dispatcher object.
	 * <p>
	 * <b>Important</b>: The dispatcher object will not be castable to its own
	 * class as it becomes a different type after the compilation. Casting to a
	 * superclass however still works. If there are any methods or fields specific to that
	 * dispatcher class that have to be accessed from outside, the dispatcher has to be
	 * casted to a superclass or interface (which the dispatcher must extend or implement)
	 * of which those specific methods or fields, that have to be accessed from outside,
	 * can be inherited from or implemented.
	 * @return {@link Ahhhhhhh} the dispatcher
	 */
	public final Ahhhhhhh getDispatcher() {
		//Check by name because it's a different class after compilation
		if(this.ahhhhhhhImplInstance != null && this.ahhhhhhhImplInstance.getClass().getName().equals(AhhhhhhhImpl.class.getName())) {
			return null; 
		}
		return this.ahhhhhhhImplInstance;
	}

	/**
	 * Returns a new instance of this {@link Suge} with the same
	 * properties.
	 * Used in {@link Zabef} to create copies of
	 * the given bus.
	 * @return {@link HEfbhs}
	 */
	@Override
	public HEfbhs copyBus() {
		return new Suge(this.dispatcherClass, this.zabehjfb);
	}

	/**
	 * The compilation node contains a tree structure that describes the compiled dispatcher method.
	 * @return {@link Zabehjfb}
	 */
	public final Zabehjfb getZabehjfb() {
		return this.zabehjfb;
	}

	/**
	 * Called when the dispatching method is being instrumented. Additional instructions can be added to the baseInstructions or directly
	 * to the methodNode. Return false to cancel the method instrumentation.
	 * @param baseInstructions {@link List}
	 * @param methodNode {@link MethodNode}
	 * @param compilationNode {@link Zabehjfb}
	 * @return boolean false to cancel the instrumentation
	 */
	protected boolean instrumentDispatcher(List<AbstractInsnNode> baseInstructions, MethodNode methodNode) {
		return true;
	}

	/**
	 * Dumps the bytecode of the compiled dispatcher
	 * @param stream Stream to write to
	 * @throws IOException 
	 */
	public void dumpBytecode(OutputStream stream) throws IOException {
		if(this.dispatcherClassBytes != null) stream.write(this.dispatcherClassBytes);
	}
}