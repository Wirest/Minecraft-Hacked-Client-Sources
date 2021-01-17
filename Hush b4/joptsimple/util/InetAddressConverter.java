// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.util;

import java.net.UnknownHostException;
import joptsimple.ValueConversionException;
import java.net.InetAddress;
import joptsimple.ValueConverter;

public class InetAddressConverter implements ValueConverter<InetAddress>
{
    public InetAddress convert(final String value) {
        try {
            return InetAddress.getByName(value);
        }
        catch (UnknownHostException e) {
            throw new ValueConversionException("Cannot convert value [" + value + " into an InetAddress", e);
        }
    }
    
    public Class<InetAddress> valueType() {
        return InetAddress.class;
    }
    
    public String valuePattern() {
        return null;
    }
}
