// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.jinput;

import net.java.games.input.AbstractComponent;
import net.java.games.input.Event;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Rumbler;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;

final class LWJGLKeyboard extends Keyboard
{
    LWJGLKeyboard() {
        super("LWJGLKeyboard", createComponents(), new Controller[0], new Rumbler[0]);
    }
    
    private static Component[] createComponents() {
        final List<Key> components = new ArrayList<Key>();
        final Field[] arr$;
        final Field[] vkey_fields = arr$ = org.lwjgl.input.Keyboard.class.getFields();
        for (final Field vkey_field : arr$) {
            try {
                if (Modifier.isStatic(vkey_field.getModifiers()) && vkey_field.getType() == Integer.TYPE && vkey_field.getName().startsWith("KEY_")) {
                    final int vkey_code = vkey_field.getInt(null);
                    final Component.Identifier.Key key_id = KeyMap.map(vkey_code);
                    if (key_id != Component.Identifier.Key.UNKNOWN) {
                        components.add(new Key(key_id, vkey_code));
                    }
                }
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return components.toArray(new Component[components.size()]);
    }
    
    public synchronized void pollDevice() throws IOException {
        if (!org.lwjgl.input.Keyboard.isCreated()) {
            return;
        }
        org.lwjgl.input.Keyboard.poll();
        for (final Component component : this.getComponents()) {
            final Key key = (Key)component;
            key.update();
        }
    }
    
    @Override
    protected synchronized boolean getNextDeviceEvent(final Event event) throws IOException {
        if (!org.lwjgl.input.Keyboard.isCreated()) {
            return false;
        }
        if (!org.lwjgl.input.Keyboard.next()) {
            return false;
        }
        final int lwjgl_key = org.lwjgl.input.Keyboard.getEventKey();
        if (lwjgl_key == 0) {
            return false;
        }
        final Component.Identifier.Key key_id = KeyMap.map(lwjgl_key);
        if (key_id == null) {
            return false;
        }
        final Component key = this.getComponent(key_id);
        if (key == null) {
            return false;
        }
        final float value = org.lwjgl.input.Keyboard.getEventKeyState() ? 1.0f : 0.0f;
        event.set(key, value, org.lwjgl.input.Keyboard.getEventNanoseconds());
        return true;
    }
    
    private static final class Key extends AbstractComponent
    {
        private final int lwjgl_key;
        private float value;
        
        Key(final Component.Identifier.Key key_id, final int lwjgl_key) {
            super(key_id.getName(), key_id);
            this.lwjgl_key = lwjgl_key;
        }
        
        public void update() {
            this.value = (org.lwjgl.input.Keyboard.isKeyDown(this.lwjgl_key) ? 1.0f : 0.0f);
        }
        
        @Override
        protected float poll() {
            return this.value;
        }
        
        public boolean isRelative() {
            return false;
        }
        
        @Override
        public boolean isAnalog() {
            return false;
        }
    }
}
