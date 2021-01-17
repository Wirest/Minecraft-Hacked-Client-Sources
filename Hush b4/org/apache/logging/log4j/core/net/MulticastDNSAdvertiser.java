// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.helpers.Integers;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "multicastdns", category = "Core", elementType = "advertiser", printObject = false)
public class MulticastDNSAdvertiser implements Advertiser
{
    protected static final Logger LOGGER;
    private static Object jmDNS;
    private static Class<?> jmDNSClass;
    private static Class<?> serviceInfoClass;
    
    @Override
    public Object advertise(final Map<String, String> properties) {
        final Map<String, String> truncatedProperties = new HashMap<String, String>();
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            if (entry.getKey().length() <= 255 && entry.getValue().length() <= 255) {
                truncatedProperties.put(entry.getKey(), entry.getValue());
            }
        }
        final String protocol = truncatedProperties.get("protocol");
        final String zone = "._log4j._" + ((protocol != null) ? protocol : "tcp") + ".local.";
        final String portString = truncatedProperties.get("port");
        final int port = Integers.parseInt(portString, 4555);
        final String name = truncatedProperties.get("name");
        if (MulticastDNSAdvertiser.jmDNS != null) {
            boolean isVersion3 = false;
            try {
                MulticastDNSAdvertiser.jmDNSClass.getMethod("create", (Class<?>[])null);
                isVersion3 = true;
            }
            catch (NoSuchMethodException ex) {}
            Object serviceInfo;
            if (isVersion3) {
                serviceInfo = this.buildServiceInfoVersion3(zone, port, name, truncatedProperties);
            }
            else {
                serviceInfo = this.buildServiceInfoVersion1(zone, port, name, truncatedProperties);
            }
            try {
                final Method method = MulticastDNSAdvertiser.jmDNSClass.getMethod("registerService", MulticastDNSAdvertiser.serviceInfoClass);
                method.invoke(MulticastDNSAdvertiser.jmDNS, serviceInfo);
            }
            catch (IllegalAccessException e) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke registerService method", e);
            }
            catch (NoSuchMethodException e2) {
                MulticastDNSAdvertiser.LOGGER.warn("No registerService method", e2);
            }
            catch (InvocationTargetException e3) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke registerService method", e3);
            }
            return serviceInfo;
        }
        MulticastDNSAdvertiser.LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
        return null;
    }
    
    @Override
    public void unadvertise(final Object serviceInfo) {
        if (MulticastDNSAdvertiser.jmDNS != null) {
            try {
                final Method method = MulticastDNSAdvertiser.jmDNSClass.getMethod("unregisterService", MulticastDNSAdvertiser.serviceInfoClass);
                method.invoke(MulticastDNSAdvertiser.jmDNS, serviceInfo);
            }
            catch (IllegalAccessException e) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke unregisterService method", e);
            }
            catch (NoSuchMethodException e2) {
                MulticastDNSAdvertiser.LOGGER.warn("No unregisterService method", e2);
            }
            catch (InvocationTargetException e3) {
                MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke unregisterService method", e3);
            }
        }
    }
    
    private static Object createJmDNSVersion1() {
        try {
            return MulticastDNSAdvertiser.jmDNSClass.newInstance();
        }
        catch (InstantiationException e) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to instantiate JMDNS", e);
        }
        catch (IllegalAccessException e2) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to instantiate JMDNS", e2);
        }
        return null;
    }
    
    private static Object createJmDNSVersion3() {
        try {
            final Method jmDNSCreateMethod = MulticastDNSAdvertiser.jmDNSClass.getMethod("create", (Class<?>[])null);
            return jmDNSCreateMethod.invoke(null, (Object[])null);
        }
        catch (IllegalAccessException e) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to instantiate jmdns class", e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to access constructor", e2);
        }
        catch (InvocationTargetException e3) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to call constructor", e3);
        }
        return null;
    }
    
    private Object buildServiceInfoVersion1(final String zone, final int port, final String name, final Map<String, String> properties) {
        final Hashtable<String, String> hashtableProperties = new Hashtable<String, String>(properties);
        try {
            final Class<?>[] args = (Class<?>[])new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class };
            final Constructor<?> constructor = MulticastDNSAdvertiser.serviceInfoClass.getConstructor(args);
            final Object[] values = { zone, name, port, 0, 0, hashtableProperties };
            return constructor.newInstance(values);
        }
        catch (IllegalAccessException e) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to get ServiceInfo constructor", e2);
        }
        catch (InstantiationException e3) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", e3);
        }
        catch (InvocationTargetException e4) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", e4);
        }
        return null;
    }
    
    private Object buildServiceInfoVersion3(final String zone, final int port, final String name, final Map<String, String> properties) {
        try {
            final Class<?>[] args = (Class<?>[])new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class };
            final Method serviceInfoCreateMethod = MulticastDNSAdvertiser.serviceInfoClass.getMethod("create", args);
            final Object[] values = { zone, name, port, 0, 0, properties };
            return serviceInfoCreateMethod.invoke(null, values);
        }
        catch (IllegalAccessException e) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke create method", e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to find create method", e2);
        }
        catch (InvocationTargetException e3) {
            MulticastDNSAdvertiser.LOGGER.warn("Unable to invoke create method", e3);
        }
        return null;
    }
    
    private static Object initializeJMDNS() {
        try {
            MulticastDNSAdvertiser.jmDNSClass = Class.forName("javax.jmdns.JmDNS");
            MulticastDNSAdvertiser.serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
            boolean isVersion3 = false;
            try {
                MulticastDNSAdvertiser.jmDNSClass.getMethod("create", (Class<?>[])null);
                isVersion3 = true;
            }
            catch (NoSuchMethodException ex) {}
            if (isVersion3) {
                return createJmDNSVersion3();
            }
            return createJmDNSVersion1();
        }
        catch (ClassNotFoundException e) {
            MulticastDNSAdvertiser.LOGGER.warn("JmDNS or serviceInfo class not found", e);
        }
        catch (ExceptionInInitializerError e2) {
            MulticastDNSAdvertiser.LOGGER.warn("JmDNS or serviceInfo class not found", e2);
        }
        return null;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        MulticastDNSAdvertiser.jmDNS = initializeJMDNS();
    }
}
