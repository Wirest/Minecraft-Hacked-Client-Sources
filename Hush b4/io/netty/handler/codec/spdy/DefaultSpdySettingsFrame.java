// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.Iterator;
import io.netty.util.internal.StringUtil;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

public class DefaultSpdySettingsFrame implements SpdySettingsFrame
{
    private boolean clear;
    private final Map<Integer, Setting> settingsMap;
    
    public DefaultSpdySettingsFrame() {
        this.settingsMap = new TreeMap<Integer, Setting>();
    }
    
    @Override
    public Set<Integer> ids() {
        return this.settingsMap.keySet();
    }
    
    @Override
    public boolean isSet(final int id) {
        final Integer key = id;
        return this.settingsMap.containsKey(key);
    }
    
    @Override
    public int getValue(final int id) {
        final Integer key = id;
        if (this.settingsMap.containsKey(key)) {
            return this.settingsMap.get(key).getValue();
        }
        return -1;
    }
    
    @Override
    public SpdySettingsFrame setValue(final int id, final int value) {
        return this.setValue(id, value, false, false);
    }
    
    @Override
    public SpdySettingsFrame setValue(final int id, final int value, final boolean persistValue, final boolean persisted) {
        if (id < 0 || id > 16777215) {
            throw new IllegalArgumentException("Setting ID is not valid: " + id);
        }
        final Integer key = id;
        if (this.settingsMap.containsKey(key)) {
            final Setting setting = this.settingsMap.get(key);
            setting.setValue(value);
            setting.setPersist(persistValue);
            setting.setPersisted(persisted);
        }
        else {
            this.settingsMap.put(key, new Setting(value, persistValue, persisted));
        }
        return this;
    }
    
    @Override
    public SpdySettingsFrame removeValue(final int id) {
        final Integer key = id;
        if (this.settingsMap.containsKey(key)) {
            this.settingsMap.remove(key);
        }
        return this;
    }
    
    @Override
    public boolean isPersistValue(final int id) {
        final Integer key = id;
        return this.settingsMap.containsKey(key) && this.settingsMap.get(key).isPersist();
    }
    
    @Override
    public SpdySettingsFrame setPersistValue(final int id, final boolean persistValue) {
        final Integer key = id;
        if (this.settingsMap.containsKey(key)) {
            this.settingsMap.get(key).setPersist(persistValue);
        }
        return this;
    }
    
    @Override
    public boolean isPersisted(final int id) {
        final Integer key = id;
        return this.settingsMap.containsKey(key) && this.settingsMap.get(key).isPersisted();
    }
    
    @Override
    public SpdySettingsFrame setPersisted(final int id, final boolean persisted) {
        final Integer key = id;
        if (this.settingsMap.containsKey(key)) {
            this.settingsMap.get(key).setPersisted(persisted);
        }
        return this;
    }
    
    @Override
    public boolean clearPreviouslyPersistedSettings() {
        return this.clear;
    }
    
    @Override
    public SpdySettingsFrame setClearPreviouslyPersistedSettings(final boolean clear) {
        this.clear = clear;
        return this;
    }
    
    private Set<Map.Entry<Integer, Setting>> getSettings() {
        return this.settingsMap.entrySet();
    }
    
    private void appendSettings(final StringBuilder buf) {
        for (final Map.Entry<Integer, Setting> e : this.getSettings()) {
            final Setting setting = e.getValue();
            buf.append("--> ");
            buf.append(e.getKey());
            buf.append(':');
            buf.append(setting.getValue());
            buf.append(" (persist value: ");
            buf.append(setting.isPersist());
            buf.append("; persisted: ");
            buf.append(setting.isPersisted());
            buf.append(')');
            buf.append(StringUtil.NEWLINE);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append(StringUtil.NEWLINE);
        this.appendSettings(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
    
    private static final class Setting
    {
        private int value;
        private boolean persist;
        private boolean persisted;
        
        Setting(final int value, final boolean persist, final boolean persisted) {
            this.value = value;
            this.persist = persist;
            this.persisted = persisted;
        }
        
        int getValue() {
            return this.value;
        }
        
        void setValue(final int value) {
            this.value = value;
        }
        
        boolean isPersist() {
            return this.persist;
        }
        
        void setPersist(final boolean persist) {
            this.persist = persist;
        }
        
        boolean isPersisted() {
            return this.persisted;
        }
        
        void setPersisted(final boolean persisted) {
            this.persisted = persisted;
        }
    }
}
