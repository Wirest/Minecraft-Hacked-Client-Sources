// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.management.ThreadInfo;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;

public class ThreadDumpMessage implements Message
{
    private static final long serialVersionUID = -1103400781608841088L;
    private static final ThreadInfoFactory FACTORY;
    private volatile Map<ThreadInformation, StackTraceElement[]> threads;
    private final String title;
    private String formattedMessage;
    
    public ThreadDumpMessage(final String title) {
        this.title = ((title == null) ? "" : title);
        this.threads = ThreadDumpMessage.FACTORY.createThreadInfo();
    }
    
    private ThreadDumpMessage(final String formattedMsg, final String title) {
        this.formattedMessage = formattedMsg;
        this.title = ((title == null) ? "" : title);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ThreadDumpMessage[");
        if (this.title.length() > 0) {
            sb.append("Title=\"").append(this.title).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        final StringBuilder sb = new StringBuilder(this.title);
        if (this.title.length() > 0) {
            sb.append("\n");
        }
        for (final Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
            final ThreadInformation info = entry.getKey();
            info.printThreadInfo(sb);
            info.printStack(sb, entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String getFormat() {
        return (this.title == null) ? "" : this.title;
    }
    
    @Override
    public Object[] getParameters() {
        return null;
    }
    
    protected Object writeReplace() {
        return new ThreadDumpMessageProxy(this);
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    static {
        final Method[] methods = ThreadInfo.class.getMethods();
        boolean basic = true;
        for (final Method method : methods) {
            if (method.getName().equals("getLockInfo")) {
                basic = false;
                break;
            }
        }
        FACTORY = (basic ? new BasicThreadInfoFactory() : new ExtendedThreadInfoFactory());
    }
    
    private static class ThreadDumpMessageProxy implements Serializable
    {
        private static final long serialVersionUID = -3476620450287648269L;
        private final String formattedMsg;
        private final String title;
        
        public ThreadDumpMessageProxy(final ThreadDumpMessage msg) {
            this.formattedMsg = msg.getFormattedMessage();
            this.title = msg.title;
        }
        
        protected Object readResolve() {
            return new ThreadDumpMessage(this.formattedMsg, this.title, null);
        }
    }
    
    private static class BasicThreadInfoFactory implements ThreadInfoFactory
    {
        @Override
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            final Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            final Map<ThreadInformation, StackTraceElement[]> threads = new HashMap<ThreadInformation, StackTraceElement[]>(map.size());
            for (final Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                threads.put(new BasicThreadInformation(entry.getKey()), entry.getValue());
            }
            return threads;
        }
    }
    
    private static class ExtendedThreadInfoFactory implements ThreadInfoFactory
    {
        @Override
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            final ThreadInfo[] array = bean.dumpAllThreads(true, true);
            final Map<ThreadInformation, StackTraceElement[]> threads = new HashMap<ThreadInformation, StackTraceElement[]>(array.length);
            for (final ThreadInfo info : array) {
                threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
            }
            return threads;
        }
    }
    
    private interface ThreadInfoFactory
    {
        Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
    }
}
