package awfdd.ksksk.zabejhf.rgsd.xasnfkj;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Used to load and instrument classes.
 * Note that once the class has been instrumented it can no longer be casted
 * to that class, unless it's done in the same class loader context. However, it 
 * can still be casted to it's superclass.
 * 
 * @author TCB
 *
 * @param <T> Superclass of the class to be instrumentated
 */
public abstract class zAfbehjvf<T> extends ClassLoader {
	private final ClassLoader parent;
	public final Class<? extends T> instrumentedClass;

	private Exception loadingException = null;

	/**
	 * Used to load and instrument classes.
	 * Note that once the class has been instrumented it can no longer be casted
	 * to that class, unless it's done in the same class loader context. However, it 
	 * can still be casted to it's superclass.
	 * 
	 * @param parent Parent class loader to delegate to
	 * @param instrumentedClass Class to be instrumented
	 */
	public zAfbehjvf(ClassLoader parent, Class<? extends T> instrumentedClass) {
		super(parent);
		this.parent = parent;
		this.instrumentedClass = instrumentedClass;
	}

	/**
	 * Loads and instrumentates the class and creates an instance
	 * 
	 * @param paramTypes Parameter types
	 * @param params Parameters
	 * @return
	 * @throws Exception 
	 */
	public T createInstance(Class<?>[] paramTypes, Object... params) throws Exception {
		Class<? extends T> dispatcherClass = this.loadClass();
		if(this.loadingException != null) {
			throw this.loadingException;
		}
		Constructor<? extends T> ctor = dispatcherClass.getDeclaredConstructor(paramTypes);
		ctor.setAccessible(true);
		T instance = ctor.newInstance(params);
		return instance;
	}

	/**
	 * Loads and instrumentates the class
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<? extends T> loadClass() throws ClassNotFoundException {
		return this.loadClass(this.instrumentedClass.getName());
	}

	/**
	 * Loads and instrumentates the class
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends T> loadClass(String name) throws ClassNotFoundException {
		if(name.equals(this.instrumentedClass.getName())) {
			return (Class<? extends T>) super.loadClass(name);
		}
		return (Class<? extends T>) this.parent.loadClass(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected final Class<?extends T> loadClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
		if(paramString.equals(this.instrumentedClass.getName())) {
			try {
				InputStream is = this.instrumentedClass.getResourceAsStream("/" + paramString.replace('.', '/') + ".class");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int readBytes = 0;
				byte[] buffer = new byte[1024];
				while((readBytes = is.read(buffer)) >= 0) {
					baos.write(buffer, 0, readBytes);
				}
				byte[] bytecode = baos.toByteArray();
				bytecode = this.instrument(bytecode);
				return (Class<T>) this.defineClass(paramString, bytecode, 0, bytecode.length);
			} catch(Exception ex) {
				this.loadingException = ex;
			}
		}
		return (Class<? extends T>) super.loadClass(paramString, paramBoolean);
	}

	/**
	 * Returns the loading exception
	 * 
	 * @return
	 */
	public Exception getLoadingException() {
		return this.loadingException;
	}

	/**
	 * Instrumentates the bytecode of the class
	 * 
	 * @param bytecode
	 * @return
	 */
	protected abstract byte[] instrument(byte[] bytecode);
}
