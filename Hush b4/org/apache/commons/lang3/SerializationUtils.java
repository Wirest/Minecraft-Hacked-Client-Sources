// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

import java.util.HashMap;
import java.io.ObjectStreamClass;
import java.util.Map;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.Serializable;

public class SerializationUtils
{
    public static <T extends Serializable> T clone(final T object) {
        if (object == null) {
            return null;
        }
        final byte[] objectData = serialize(object);
        final ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        ClassLoaderAwareObjectInputStream in = null;
        try {
            in = new ClassLoaderAwareObjectInputStream(bais, object.getClass().getClassLoader());
            final T readObject = (T)in.readObject();
            return readObject;
        }
        catch (ClassNotFoundException ex) {
            throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
        }
        catch (IOException ex2) {
            throw new SerializationException("IOException while reading cloned object data", ex2);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex3) {
                throw new SerializationException("IOException on closing cloned object data InputStream.", ex3);
            }
        }
    }
    
    public static <T extends Serializable> T roundtrip(final T msg) {
        return deserialize(serialize(msg));
    }
    
    public static void serialize(final Serializable obj, final OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(obj);
        }
        catch (IOException ex) {
            throw new SerializationException(ex);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException ex2) {}
        }
    }
    
    public static byte[] serialize(final Serializable obj) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }
    
    public static <T> T deserialize(final InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(inputStream);
            final T obj = (T)in.readObject();
            return obj;
        }
        catch (ClassCastException ex) {
            throw new SerializationException(ex);
        }
        catch (ClassNotFoundException ex2) {
            throw new SerializationException(ex2);
        }
        catch (IOException ex3) {
            throw new SerializationException(ex3);
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex4) {}
        }
    }
    
    public static <T> T deserialize(final byte[] objectData) {
        if (objectData == null) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        return deserialize(new ByteArrayInputStream(objectData));
    }
    
    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream
    {
        private static final Map<String, Class<?>> primitiveTypes;
        private final ClassLoader classLoader;
        
        public ClassLoaderAwareObjectInputStream(final InputStream in, final ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("byte", Byte.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("short", Short.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("int", Integer.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("long", Long.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("float", Float.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("double", Double.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("boolean", Boolean.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("char", Character.TYPE);
            ClassLoaderAwareObjectInputStream.primitiveTypes.put("void", Void.TYPE);
        }
        
        @Override
        protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            final String name = desc.getName();
            try {
                return Class.forName(name, false, this.classLoader);
            }
            catch (ClassNotFoundException ex) {
                try {
                    return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
                }
                catch (ClassNotFoundException cnfe) {
                    final Class<?> cls = ClassLoaderAwareObjectInputStream.primitiveTypes.get(name);
                    if (cls != null) {
                        return cls;
                    }
                    throw cnfe;
                }
            }
        }
        
        static {
            primitiveTypes = new HashMap<String, Class<?>>();
        }
    }
}
