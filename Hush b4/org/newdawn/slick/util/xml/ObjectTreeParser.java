// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.newdawn.slick.util.Log;
import java.io.InputStream;
import org.newdawn.slick.util.ResourceLoader;
import java.util.ArrayList;
import java.util.HashMap;

public class ObjectTreeParser
{
    private HashMap nameToClass;
    private String defaultPackage;
    private ArrayList ignored;
    private String addMethod;
    
    public ObjectTreeParser() {
        this.nameToClass = new HashMap();
        this.ignored = new ArrayList();
        this.addMethod = "add";
    }
    
    public ObjectTreeParser(final String defaultPackage) {
        this.nameToClass = new HashMap();
        this.ignored = new ArrayList();
        this.addMethod = "add";
        this.defaultPackage = defaultPackage;
    }
    
    public void addElementMapping(final String elementName, final Class elementClass) {
        this.nameToClass.put(elementName, elementClass);
    }
    
    public void addIgnoredElement(final String elementName) {
        this.ignored.add(elementName);
    }
    
    public void setAddMethodName(final String methodName) {
        this.addMethod = methodName;
    }
    
    public void setDefaultPackage(final String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }
    
    public Object parse(final String ref) throws SlickXMLException {
        return this.parse(ref, ResourceLoader.getResourceAsStream(ref));
    }
    
    public Object parse(final String name, final InputStream in) throws SlickXMLException {
        final XMLParser parser = new XMLParser();
        final XMLElement root = parser.parse(name, in);
        return this.traverse(root);
    }
    
    public Object parseOnto(final String ref, final Object target) throws SlickXMLException {
        return this.parseOnto(ref, ResourceLoader.getResourceAsStream(ref), target);
    }
    
    public Object parseOnto(final String name, final InputStream in, final Object target) throws SlickXMLException {
        final XMLParser parser = new XMLParser();
        final XMLElement root = parser.parse(name, in);
        return this.traverse(root, target);
    }
    
    private Class getClassForElementName(final String name) {
        final Class clazz = this.nameToClass.get(name);
        if (clazz != null) {
            return clazz;
        }
        if (this.defaultPackage != null) {
            try {
                return Class.forName(String.valueOf(this.defaultPackage) + "." + name);
            }
            catch (ClassNotFoundException ex) {}
        }
        return null;
    }
    
    private Object traverse(final XMLElement current) throws SlickXMLException {
        return this.traverse(current, null);
    }
    
    private Object traverse(final XMLElement current, Object instance) throws SlickXMLException {
        final String name = current.getName();
        if (this.ignored.contains(name)) {
            return null;
        }
        Class clazz;
        if (instance == null) {
            clazz = this.getClassForElementName(name);
        }
        else {
            clazz = instance.getClass();
        }
        if (clazz == null) {
            throw new SlickXMLException("Unable to map element " + name + " to a class, define the mapping");
        }
        try {
            if (instance == null) {
                instance = clazz.newInstance();
                final Method elementNameMethod = this.getMethod(clazz, "setXMLElementName", new Class[] { String.class });
                if (elementNameMethod != null) {
                    this.invoke(elementNameMethod, instance, new Object[] { name });
                }
                final Method contentMethod = this.getMethod(clazz, "setXMLElementContent", new Class[] { String.class });
                if (contentMethod != null) {
                    this.invoke(contentMethod, instance, new Object[] { current.getContent() });
                }
            }
            final String[] attrs = current.getAttributeNames();
            for (int i = 0; i < attrs.length; ++i) {
                final String methodName = "set" + attrs[i];
                final Method method = this.findMethod(clazz, methodName);
                if (method == null) {
                    final Field field = this.findField(clazz, attrs[i]);
                    if (field != null) {
                        final String value = current.getAttribute(attrs[i]);
                        final Object typedValue = this.typeValue(value, field.getType());
                        this.setField(field, instance, typedValue);
                    }
                    else {
                        Log.info("Unable to find property on: " + clazz + " for attribute: " + attrs[i]);
                    }
                }
                else {
                    final String value2 = current.getAttribute(attrs[i]);
                    final Object typedValue2 = this.typeValue(value2, method.getParameterTypes()[0]);
                    this.invoke(method, instance, new Object[] { typedValue2 });
                }
            }
            final XMLElementList children = current.getChildren();
            for (int j = 0; j < children.size(); ++j) {
                final XMLElement element = children.get(j);
                final Object child = this.traverse(element);
                if (child != null) {
                    final String methodName2 = this.addMethod;
                    final Method method2 = this.findMethod(clazz, methodName2, child.getClass());
                    if (method2 == null) {
                        Log.info("Unable to find method to add: " + child + " to " + clazz);
                    }
                    else {
                        this.invoke(method2, instance, new Object[] { child });
                    }
                }
            }
            return instance;
        }
        catch (InstantiationException e) {
            throw new SlickXMLException("Unable to instance " + clazz + " for element " + name + ", no zero parameter constructor?", e);
        }
        catch (IllegalAccessException e2) {
            throw new SlickXMLException("Unable to instance " + clazz + " for element " + name + ", no zero parameter constructor?", e2);
        }
    }
    
    private Object typeValue(final String value, Class clazz) throws SlickXMLException {
        if (clazz == String.class) {
            return value;
        }
        try {
            clazz = this.mapPrimitive(clazz);
            return clazz.getConstructor(String.class).newInstance(value);
        }
        catch (Exception e) {
            throw new SlickXMLException("Failed to convert: " + value + " to the expected primitive type: " + clazz, e);
        }
    }
    
    private Class mapPrimitive(final Class clazz) {
        if (clazz == Integer.TYPE) {
            return Integer.class;
        }
        if (clazz == Double.TYPE) {
            return Double.class;
        }
        if (clazz == Float.TYPE) {
            return Float.class;
        }
        if (clazz == Boolean.TYPE) {
            return Boolean.class;
        }
        if (clazz == Long.TYPE) {
            return Long.class;
        }
        throw new RuntimeException("Unsupported primitive: " + clazz);
    }
    
    private Field findField(final Class clazz, final String name) {
        final Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].getName().equalsIgnoreCase(name)) {
                if (fields[i].getType().isPrimitive()) {
                    return fields[i];
                }
                if (fields[i].getType() == String.class) {
                    return fields[i];
                }
            }
        }
        return null;
    }
    
    private Method findMethod(final Class clazz, final String name) {
        final Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().equalsIgnoreCase(name)) {
                final Method method = methods[i];
                final Class[] params = method.getParameterTypes();
                if (params.length == 1) {
                    return method;
                }
            }
        }
        return null;
    }
    
    private Method findMethod(final Class clazz, final String name, final Class parameter) {
        final Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            if (methods[i].getName().equalsIgnoreCase(name)) {
                final Method method = methods[i];
                final Class[] params = method.getParameterTypes();
                if (params.length == 1 && method.getParameterTypes()[0].isAssignableFrom(parameter)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    private void setField(final Field field, final Object instance, final Object value) throws SlickXMLException {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        }
        catch (IllegalArgumentException e) {
            throw new SlickXMLException("Failed to set: " + field + " for an XML attribute, is it valid?", e);
        }
        catch (IllegalAccessException e2) {
            throw new SlickXMLException("Failed to set: " + field + " for an XML attribute, is it valid?", e2);
        }
        finally {
            field.setAccessible(false);
        }
        field.setAccessible(false);
    }
    
    private void invoke(final Method method, final Object instance, final Object[] params) throws SlickXMLException {
        try {
            method.setAccessible(true);
            method.invoke(instance, params);
        }
        catch (IllegalArgumentException e) {
            throw new SlickXMLException("Failed to invoke: " + method + " for an XML attribute, is it valid?", e);
        }
        catch (IllegalAccessException e2) {
            throw new SlickXMLException("Failed to invoke: " + method + " for an XML attribute, is it valid?", e2);
        }
        catch (InvocationTargetException e3) {
            throw new SlickXMLException("Failed to invoke: " + method + " for an XML attribute, is it valid?", e3);
        }
        finally {
            method.setAccessible(false);
        }
        method.setAccessible(false);
    }
    
    private Method getMethod(final Class clazz, final String name, final Class[] params) {
        try {
            return clazz.getMethod(name, (Class[])params);
        }
        catch (SecurityException e) {
            return null;
        }
        catch (NoSuchMethodException e2) {
            return null;
        }
    }
}
