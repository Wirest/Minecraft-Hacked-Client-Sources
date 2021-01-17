// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

public interface Library
{
    public static final String OPTION_TYPE_MAPPER = "type-mapper";
    public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
    public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
    public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
    public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
    public static final String OPTION_CALLING_CONVENTION = "calling-convention";
    
    public static class Handler implements InvocationHandler
    {
        static final Method OBJECT_TOSTRING;
        static final Method OBJECT_HASHCODE;
        static final Method OBJECT_EQUALS;
        private final NativeLibrary nativeLibrary;
        private final Class interfaceClass;
        private final Map options;
        private FunctionMapper functionMapper;
        private final InvocationMapper invocationMapper;
        private final Map functions;
        
        public Handler(final String libname, final Class interfaceClass, Map options) {
            this.functions = new WeakHashMap();
            if (libname != null && "".equals(libname.trim())) {
                throw new IllegalArgumentException("Invalid library name \"" + libname + "\"");
            }
            this.interfaceClass = interfaceClass;
            options = new HashMap<Object, FunctionMapper>((Map<? extends K, ? extends FunctionMapper>)options);
            final int callingConvention = ((Library$1.class$com$sun$jna$AltCallingConvention == null) ? (Library$1.class$com$sun$jna$AltCallingConvention = Library$1.class$("com.sun.jna.AltCallingConvention")) : Library$1.class$com$sun$jna$AltCallingConvention).isAssignableFrom(interfaceClass) ? 1 : 0;
            if (options.get("calling-convention") == null) {
                options.put("calling-convention", (FunctionMapper)new Integer(callingConvention));
            }
            this.options = options;
            this.nativeLibrary = NativeLibrary.getInstance(libname, options);
            this.functionMapper = (FunctionMapper)options.get("function-mapper");
            if (this.functionMapper == null) {
                this.functionMapper = new FunctionNameMap(options);
            }
            this.invocationMapper = (InvocationMapper)options.get("invocation-mapper");
        }
        
        public NativeLibrary getNativeLibrary() {
            return this.nativeLibrary;
        }
        
        public String getLibraryName() {
            return this.nativeLibrary.getName();
        }
        
        public Class getInterfaceClass() {
            return this.interfaceClass;
        }
        
        public Object invoke(final Object proxy, final Method method, Object[] inArgs) throws Throwable {
            if (Handler.OBJECT_TOSTRING.equals(method)) {
                return "Proxy interface to " + this.nativeLibrary;
            }
            if (Handler.OBJECT_HASHCODE.equals(method)) {
                return new Integer(this.hashCode());
            }
            if (Handler.OBJECT_EQUALS.equals(method)) {
                final Object o = inArgs[0];
                if (o != null && Proxy.isProxyClass(o.getClass())) {
                    return Function.valueOf(Proxy.getInvocationHandler(o) == this);
                }
                return Boolean.FALSE;
            }
            else {
                FunctionInfo f = null;
                synchronized (this.functions) {
                    f = this.functions.get(method);
                    if (f == null) {
                        f = new FunctionInfo();
                        f.isVarArgs = Function.isVarArgs(method);
                        if (this.invocationMapper != null) {
                            f.handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
                        }
                        if (f.handler == null) {
                            String methodName = this.functionMapper.getFunctionName(this.nativeLibrary, method);
                            if (methodName == null) {
                                methodName = method.getName();
                            }
                            f.function = this.nativeLibrary.getFunction(methodName, method);
                            (f.options = new HashMap(this.options)).put("invoking-method", method);
                        }
                        this.functions.put(method, f);
                    }
                }
                if (f.isVarArgs) {
                    inArgs = Function.concatenateVarArgs(inArgs);
                }
                if (f.handler != null) {
                    return f.handler.invoke(proxy, method, inArgs);
                }
                return f.function.invoke(method.getReturnType(), inArgs, f.options);
            }
        }
        
        static {
            try {
                OBJECT_TOSTRING = ((Library$1.class$java$lang$Object == null) ? (Library$1.class$java$lang$Object = Library$1.class$("java.lang.Object")) : Library$1.class$java$lang$Object).getMethod("toString", (Class[])new Class[0]);
                OBJECT_HASHCODE = ((Library$1.class$java$lang$Object == null) ? (Library$1.class$java$lang$Object = Library$1.class$("java.lang.Object")) : Library$1.class$java$lang$Object).getMethod("hashCode", (Class[])new Class[0]);
                OBJECT_EQUALS = ((Library$1.class$java$lang$Object == null) ? (Library$1.class$java$lang$Object = Library$1.class$("java.lang.Object")) : Library$1.class$java$lang$Object).getMethod("equals", (Library$1.class$java$lang$Object == null) ? (Library$1.class$java$lang$Object = Library$1.class$("java.lang.Object")) : Library$1.class$java$lang$Object);
            }
            catch (Exception e) {
                throw new Error("Error retrieving Object.toString() method");
            }
        }
        
        private static class FunctionNameMap implements FunctionMapper
        {
            private final Map map;
            
            public FunctionNameMap(final Map map) {
                this.map = new HashMap(map);
            }
            
            public String getFunctionName(final NativeLibrary library, final Method method) {
                final String name = method.getName();
                if (this.map.containsKey(name)) {
                    return this.map.get(name);
                }
                return name;
            }
        }
        
        private static class FunctionInfo
        {
            InvocationHandler handler;
            Function function;
            boolean isVarArgs;
            Map options;
        }
    }
}
