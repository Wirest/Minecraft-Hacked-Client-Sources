package awfdd.ksksk.ap.ergd.rgsdfwef.cdsef;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import awfdd.ksksk.ap.ergd.rgsdfwef.fregdf.Ljbfjksbr;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import awfdd.ksksk.dabehfb;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zAfbehjvf;

/**
 * The Hook class is used to create hooks that can dynamically invoke methods
 * without using the Reflection API for the invocation. It is slightly faster than
 * using the Reflection API.
 * 
 * @author TCB
 *
 */
public final class Zejfjnd {
	private Zejfjnd() { }

	private static final String METHOD_NAME_INVOKE = "invoke";
	private static final String FIELD_INSTANCE = "instance";

	public static class HookInvokerException extends RuntimeException {
		private static final long serialVersionUID = 2694201723810916611L;

		public HookInvokerException(String msg) {
			super(msg);
		}
		public HookInvokerException(String msg, Exception cause) {
			super(msg, cause);
		}
	}

	private static final class LjbfjksbrImpl extends Ljbfjksbr {
		@Override
		public Object invoke(Object... args) {
			return null;
		}
	}

	private Ljbfjksbr invoker;

	public static final class HookBuilder {
		private static final String HOOK_INVOKER_INVOKE = "invoke";

		private final Class<?> clazz;
		private final Method method;
		private boolean staticHook = false;
		private Class<? extends Ljbfjksbr> hookInvoker = LjbfjksbrImpl.class;

		private HookBuilder(Class<?> clazz, Method method) {
			this.clazz = clazz;
			this.method = method;
		}

		/**
		 * Sets the hook invoker class
		 * 
		 * @param invoker Hook invoker class
		 * @return
		 */
		public HookBuilder setHookInvoker(Class<? extends Ljbfjksbr> invoker) {
			if(invoker == null) {
				this.hookInvoker = LjbfjksbrImpl.class;
				return this;
			}
			this.hookInvoker = invoker;
			return this;
		}

		/**
		 * Sets the hook method to static
		 * 
		 * @return
		 */
		public HookBuilder setStatic() {
			this.staticHook = true;
			return this;
		}

		/**
		 * Sets the hook method to static modifier
		 * 
		 * @return
		 */
		public HookBuilder setStatic(boolean staticModifier) {
			this.staticHook = staticModifier;
			return this;
		}

		/**
		 * Compiles the hook
		 * 
		 * @return
		 * @throws Exception
		 */
		public Zejfjnd compile() throws Exception {
			//Instrumentation classloader
			zAfbehjvf<Ljbfjksbr> instrumentationClassLoader = new zAfbehjvf<Ljbfjksbr>(this.getClass().getClassLoader(), this.hookInvoker) {
				@Override
				protected byte[] instrument(byte[] bytecode) {
					ClassReader classReader = new ClassReader(bytecode);
					ClassNode classNode = new ClassNode();
					classReader.accept(classNode, ClassReader.SKIP_FRAMES);
					methodNodeLoop:
						for(MethodNode methodNode : (List<MethodNode>) classNode.methods) {
							if(methodNode.name.equals(METHOD_NAME_INVOKE)) {
								InsnList methodInstructionSet = methodNode.instructions;
								Iterator<AbstractInsnNode> it = methodInstructionSet.iterator();
								AbstractInsnNode insn;

								AbstractInsnNode implementationNode = null;
								boolean isNodeInvoker = false;
								while((insn = it.next()) != null && it.hasNext()) {
									boolean isReturn = HookBuilder.this.hookInvoker == LjbfjksbrImpl.class &&
											(insn.getOpcode() == Opcodes.IRETURN || 
											insn.getOpcode() == Opcodes.LRETURN ||
											insn.getOpcode() == Opcodes.RETURN ||
											insn.getOpcode() == Opcodes.ARETURN ||
											insn.getOpcode() == Opcodes.DRETURN ||
											insn.getOpcode() == Opcodes.FRETURN);
									boolean isInvokerMethod = HookBuilder.this.hookInvoker != LjbfjksbrImpl.class && insn.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode)insn).name.equals(HOOK_INVOKER_INVOKE) && ((MethodInsnNode)insn).owner.equals(Ljbfjksbr.class.getName().replace(".", "/"));
									//Only implement first invoker, throw an error if there are multiple implementations
									if(isInvokerMethod && implementationNode != null) {
										throw new HookInvokerException("The invoking implementation HookInvoker#invoke() can only be used once per method");
									}
									if(isReturn || isInvokerMethod) {
										isNodeInvoker = isInvokerMethod;
										implementationNode = insn;
									}
								}

								//No implementation or return node
								if(implementationNode == null) {
									return bytecode;
								}

								String className = dabehfb.getClassType(HookBuilder.this.clazz);

								if(!HookBuilder.this.staticHook) {
									//Load instance
									methodInstructionSet.insertBefore(implementationNode, new IntInsnNode(Opcodes.ALOAD, 0));
									methodInstructionSet.insertBefore(implementationNode, new FieldInsnNode(Opcodes.GETFIELD, dabehfb.getClassType(Ljbfjksbr.class), FIELD_INSTANCE, "Ljava/lang/Object;"));
									methodInstructionSet.insertBefore(implementationNode, new TypeInsnNode(Opcodes.CHECKCAST, className));
								}

								Method method = HookBuilder.this.method;
								Class<?>[] methodParams = method.getParameterTypes();
								for(int i = 0; i < methodParams.length; i++) {
									//Push argument onto stack
									methodInstructionSet.insertBefore(implementationNode, new IntInsnNode(Opcodes.ALOAD, 1));
									methodInstructionSet.insertBefore(implementationNode, dabehfb.getOptimizedIndex(i));
									methodInstructionSet.insertBefore(implementationNode, new InsnNode(Opcodes.AALOAD));
									Class<?> type = methodParams[i];
									//Cast argument or convert primitives
									if(!type.isPrimitive()) {
										methodInstructionSet.insertBefore(implementationNode, new TypeInsnNode(Opcodes.CHECKCAST, dabehfb.getClassType(methodParams[i])));
									} else {
										AbstractInsnNode[] converter = dabehfb.getObject2PrimitiveConverter(type);
										methodInstructionSet.insertBefore(implementationNode, converter[0]);
										methodInstructionSet.insertBefore(implementationNode, converter[1]);
									}
								}

								//Invoke
								methodInstructionSet.insertBefore(implementationNode, new MethodInsnNode(HookBuilder.this.staticHook ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL, className, HookBuilder.this.method.getName(), dabehfb.getMethodType(method)));

								//Convert and return if required
								if(method.getReturnType() != void.class) {
									if(method.getReturnType().isPrimitive()) {
										methodInstructionSet.insertBefore(implementationNode, dabehfb.getPrimitive2ObjectConverter(method.getReturnType()));
									}

									methodInstructionSet.insertBefore(implementationNode, new InsnNode(Opcodes.ARETURN));
									it.remove();
								}

								if(isNodeInvoker) {
									methodInstructionSet.remove(implementationNode);
								}

								break methodNodeLoop;
							}
						}
					ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
					classNode.accept(classWriter);
					return classWriter.toByteArray();
				}
			};
			Zejfjnd zejfjnd = new Zejfjnd();
			Ljbfjksbr invoker = instrumentationClassLoader.createInstance(null);
			invoker.init();
			zejfjnd.invoker = invoker;
			return zejfjnd;
		}

		/**
		 * Compiles the hook and catches any errors
		 * 
		 * @return
		 */
		public Zejfjnd compileSafe() {
			try {
				return this.compile();
			} catch(Throwable ex) { }
			return null;
		}
	}

	/**
	 * Creates a hook builder
	 * 
	 * @param clazz Class
	 * @param method Method
	 * @return
	 */
	public static HookBuilder create(Class<?> clazz, Method method) {
		return new HookBuilder(clazz, method);
	}

	/**
	 * Creates a hook builder
	 * 
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static HookBuilder create(Class<?> clazz, String methodName, Class<?>... params) throws NoSuchMethodException, SecurityException {
		return new HookBuilder(clazz, clazz.getDeclaredMethod(methodName, params));
	}

	/**
	 * Creates a hook builder and catches any errors
	 * 
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static HookBuilder createSafe(Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params);
		} catch(Throwable ex) { }
		return null;
	}

	/**
	 * Creates and compiles the hook and catches any errors
	 * 
	 * @param staticModifier True for static hooks
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static Zejfjnd createAndCompileSafe(boolean staticModifier, Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params).setStatic(staticModifier).compile();
		} catch (Throwable e) { }
		return null;
	}

	/**
	 * Creates and compiles the hook and catches any errors
	 * 
	 * @param instance Instance (use null for a static hook)
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static Zejfjnd createAndCompileSafe(Object instance, Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params).setStatic(instance == null).compile().setInstance(instance);
		} catch (Throwable e) { }
		return null;
	}

	/**
	 * Creates and compiles the static hook and catches any errors
	 * 
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static Zejfjnd createAndCompileSafe(Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params).setStatic(true).compile();
		} catch (Throwable e) { }
		return null;
	}

	/**
	 * Creates and compiles the hook and catches any errors
	 * 
	 * @param invoker Hook invoker
	 * @param staticModifier True for static hooks
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static Zejfjnd createAndCompileInvokerSafe(Class<? extends Ljbfjksbr> invoker, boolean staticModifier, Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params).setHookInvoker(invoker).setStatic(staticModifier).compile();
		} catch (Throwable e) { }
		return null;
	}

	/**
	 * Creates and compiles the hook and catches any errors
	 * 
	 * @param invoker Hook invoker
	 * @param instance Instance (use null for a static hook)
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static Zejfjnd createAndCompileInvokerSafe(Class<? extends Ljbfjksbr> invoker, Object instance, Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params).setHookInvoker(invoker).setStatic(instance == null).compile().setInstance(instance);
		} catch (Throwable e) { }
		return null;
	}

	/**
	 * Creates and compiles the static hook and catches any errors
	 * 
	 * @param invoker Hook invoker
	 * @param clazz Class
	 * @param methodName Method name
	 * @param params Method parameters
	 * @return Returns null if failed
	 */
	public static Zejfjnd createAndCompileInvokerSafe(Class<? extends Ljbfjksbr> invoker, Class<?> clazz, String methodName, Class<?>... params) {
		try {
			return create(clazz, methodName, params).setHookInvoker(invoker).setStatic(true).compile();
		} catch (Throwable e) { }
		return null;
	}

	/**
	 * Invokes the method. Make sure you explicitly cast all arguments to
	 * avoid any ambiguities
	 * 
	 * @param args Method arguments
	 */
	public Object invoke(Object... args) {
		return this.invoker.invoke(args);
	}

	/**
	 * Invokes the method and catches any errors. Make sure you explicitly
	 * cast all arguments to avoid any ambiguities
	 * 
	 * @param args Method arguments
	 * @return Returns true if successful
	 */
	public Object invokeSafe(Object... args) {
		try {
			return this.invoke(args);
		} catch(Throwable ex) { }
		return null;
	}

	/**
	 * Invokes the method of the hook and catches any errors. Make sure
	 * you explicitly cast all arguments to avoid any ambiguities
	 * 
	 * @param zejfjnd Hook
	 * @param args Method arguments
	 * @return Returns true if successful
	 */
	public static Object invokeSafe(Zejfjnd zejfjnd, Object... args) {
		try {
			return zejfjnd.invoke(args);
		} catch(Exception ex) { }
		return null;
	}

	/**
	 * Sets the instance
	 * 
	 * @param obj Instace
	 * @return
	 */
	public Zejfjnd setInstance(Object obj) {
		this.invoker.instance = obj;
		return this;
	}
}