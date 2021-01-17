// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.LogEvent;

@Plugin(name = "SerializedLayout", category = "Core", elementType = "layout", printObject = true)
public final class SerializedLayout extends AbstractLayout<LogEvent>
{
    private static byte[] header;
    
    private SerializedLayout() {
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream oos = new PrivateObjectOutputStream(baos);
            try {
                oos.writeObject(event);
                oos.reset();
            }
            finally {
                oos.close();
            }
        }
        catch (IOException ioe) {
            SerializedLayout.LOGGER.error("Serialization of LogEvent failed.", ioe);
        }
        return baos.toByteArray();
    }
    
    @Override
    public LogEvent toSerializable(final LogEvent event) {
        return event;
    }
    
    @PluginFactory
    public static SerializedLayout createLayout() {
        return new SerializedLayout();
    }
    
    @Override
    public byte[] getHeader() {
        return SerializedLayout.header;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        return new HashMap<String, String>();
    }
    
    @Override
    public String getContentType() {
        return "application/octet-stream";
    }
    
    static {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.close();
            SerializedLayout.header = baos.toByteArray();
        }
        catch (Exception ex) {
            SerializedLayout.LOGGER.error("Unable to generate Object stream header", ex);
        }
    }
    
    private class PrivateObjectOutputStream extends ObjectOutputStream
    {
        public PrivateObjectOutputStream(final OutputStream os) throws IOException {
            super(os);
        }
        
        @Override
        protected void writeStreamHeader() {
        }
    }
}
