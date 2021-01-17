// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.lang.reflect.Field;
import java.util.List;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.io.IOException;

final class RawKeyboard extends Keyboard
{
    private final RawKeyboardEvent raw_event;
    private final RawDevice device;
    
    protected RawKeyboard(final String name, final RawDevice device, final Controller[] children, final Rumbler[] rumblers) throws IOException {
        super(name, createKeyboardComponents(device), children, rumblers);
        this.raw_event = new RawKeyboardEvent();
        this.device = device;
    }
    
    private static final Component[] createKeyboardComponents(final RawDevice device) {
        final List components = new ArrayList();
        final Field[] vkey_fields = RawIdentifierMap.class.getFields();
        for (int i = 0; i < vkey_fields.length; ++i) {
            final Field vkey_field = vkey_fields[i];
            try {
                if (Modifier.isStatic(vkey_field.getModifiers()) && vkey_field.getType() == Integer.TYPE) {
                    final int vkey_code = vkey_field.getInt(null);
                    final Component.Identifier.Key key_id = RawIdentifierMap.mapVKey(vkey_code);
                    if (key_id != Component.Identifier.Key.UNKNOWN) {
                        components.add(new Key(device, vkey_code, key_id));
                    }
                }
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return components.toArray(new Component[0]);
    }
    
    protected final synchronized boolean getNextDeviceEvent(final Event event) throws IOException {
        while (this.device.getNextKeyboardEvent(this.raw_event)) {
            final int vkey = this.raw_event.getVKey();
            final Component.Identifier.Key key_id = RawIdentifierMap.mapVKey(vkey);
            final Component key = this.getComponent(key_id);
            if (key == null) {
                continue;
            }
            final int message = this.raw_event.getMessage();
            if (message == 256 || message == 260) {
                event.set(key, 1.0f, this.raw_event.getNanos());
                return true;
            }
            if (message == 257 || message == 261) {
                event.set(key, 0.0f, this.raw_event.getNanos());
                return true;
            }
        }
        return false;
    }
    
    public final void pollDevice() throws IOException {
        this.device.pollKeyboard();
    }
    
    protected final void setDeviceEventQueueSize(final int size) throws IOException {
        this.device.setBufferSize(size);
    }
    
    static final class Key extends AbstractComponent
    {
        private final RawDevice device;
        private final int vkey_code;
        
        public Key(final RawDevice device, final int vkey_code, final Component.Identifier.Key key_id) {
            super(key_id.getName(), key_id);
            this.device = device;
            this.vkey_code = vkey_code;
        }
        
        protected final float poll() throws IOException {
            return this.device.isKeyDown(this.vkey_code) ? 1.0f : 0.0f;
        }
        
        public final boolean isAnalog() {
            return false;
        }
        
        public final boolean isRelative() {
            return false;
        }
    }
}
