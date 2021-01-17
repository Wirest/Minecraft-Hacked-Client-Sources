// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public interface Controller
{
    Controller[] getControllers();
    
    Type getType();
    
    Component[] getComponents();
    
    Component getComponent(final Component.Identifier p0);
    
    Rumbler[] getRumblers();
    
    boolean poll();
    
    void setEventQueueSize(final int p0);
    
    EventQueue getEventQueue();
    
    PortType getPortType();
    
    int getPortNumber();
    
    String getName();
    
    public static class Type
    {
        private final String name;
        public static final Type UNKNOWN;
        public static final Type MOUSE;
        public static final Type KEYBOARD;
        public static final Type FINGERSTICK;
        public static final Type GAMEPAD;
        public static final Type HEADTRACKER;
        public static final Type RUDDER;
        public static final Type STICK;
        public static final Type TRACKBALL;
        public static final Type TRACKPAD;
        public static final Type WHEEL;
        
        protected Type(final String name) {
            this.name = name;
        }
        
        public String toString() {
            return this.name;
        }
        
        static {
            UNKNOWN = new Type("Unknown");
            MOUSE = new Type("Mouse");
            KEYBOARD = new Type("Keyboard");
            FINGERSTICK = new Type("Fingerstick");
            GAMEPAD = new Type("Gamepad");
            HEADTRACKER = new Type("Headtracker");
            RUDDER = new Type("Rudder");
            STICK = new Type("Stick");
            TRACKBALL = new Type("Trackball");
            TRACKPAD = new Type("Trackpad");
            WHEEL = new Type("Wheel");
        }
    }
    
    public static final class PortType
    {
        private final String name;
        public static final PortType UNKNOWN;
        public static final PortType USB;
        public static final PortType GAME;
        public static final PortType NETWORK;
        public static final PortType SERIAL;
        public static final PortType I8042;
        public static final PortType PARALLEL;
        
        protected PortType(final String name) {
            this.name = name;
        }
        
        public String toString() {
            return this.name;
        }
        
        static {
            UNKNOWN = new PortType("Unknown");
            USB = new PortType("USB port");
            GAME = new PortType("Game port");
            NETWORK = new PortType("Network port");
            SERIAL = new PortType("Serial port");
            I8042 = new PortType("i8042 (PS/2)");
            PARALLEL = new PortType("Parallel port");
        }
    }
}
