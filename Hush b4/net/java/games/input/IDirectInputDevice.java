// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.Arrays;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

final class IDirectInputDevice
{
    public static final int GUID_XAxis = 1;
    public static final int GUID_YAxis = 2;
    public static final int GUID_ZAxis = 3;
    public static final int GUID_RxAxis = 4;
    public static final int GUID_RyAxis = 5;
    public static final int GUID_RzAxis = 6;
    public static final int GUID_Slider = 7;
    public static final int GUID_Button = 8;
    public static final int GUID_Key = 9;
    public static final int GUID_POV = 10;
    public static final int GUID_Unknown = 11;
    public static final int GUID_ConstantForce = 12;
    public static final int GUID_RampForce = 13;
    public static final int GUID_Square = 14;
    public static final int GUID_Sine = 15;
    public static final int GUID_Triangle = 16;
    public static final int GUID_SawtoothUp = 17;
    public static final int GUID_SawtoothDown = 18;
    public static final int GUID_Spring = 19;
    public static final int GUID_Damper = 20;
    public static final int GUID_Inertia = 21;
    public static final int GUID_Friction = 22;
    public static final int GUID_CustomForce = 23;
    public static final int DI8DEVTYPE_DEVICE = 17;
    public static final int DI8DEVTYPE_MOUSE = 18;
    public static final int DI8DEVTYPE_KEYBOARD = 19;
    public static final int DI8DEVTYPE_JOYSTICK = 20;
    public static final int DI8DEVTYPE_GAMEPAD = 21;
    public static final int DI8DEVTYPE_DRIVING = 22;
    public static final int DI8DEVTYPE_FLIGHT = 23;
    public static final int DI8DEVTYPE_1STPERSON = 24;
    public static final int DI8DEVTYPE_DEVICECTRL = 25;
    public static final int DI8DEVTYPE_SCREENPOINTER = 26;
    public static final int DI8DEVTYPE_REMOTE = 27;
    public static final int DI8DEVTYPE_SUPPLEMENTAL = 28;
    public static final int DISCL_EXCLUSIVE = 1;
    public static final int DISCL_NONEXCLUSIVE = 2;
    public static final int DISCL_FOREGROUND = 4;
    public static final int DISCL_BACKGROUND = 8;
    public static final int DISCL_NOWINKEY = 16;
    public static final int DIDFT_ALL = 0;
    public static final int DIDFT_RELAXIS = 1;
    public static final int DIDFT_ABSAXIS = 2;
    public static final int DIDFT_AXIS = 3;
    public static final int DIDFT_PSHBUTTON = 4;
    public static final int DIDFT_TGLBUTTON = 8;
    public static final int DIDFT_BUTTON = 12;
    public static final int DIDFT_POV = 16;
    public static final int DIDFT_COLLECTION = 64;
    public static final int DIDFT_NODATA = 128;
    public static final int DIDFT_FFACTUATOR = 16777216;
    public static final int DIDFT_FFEFFECTTRIGGER = 33554432;
    public static final int DIDFT_OUTPUT = 268435456;
    public static final int DIDFT_VENDORDEFINED = 67108864;
    public static final int DIDFT_ALIAS = 134217728;
    public static final int DIDFT_OPTIONAL = Integer.MIN_VALUE;
    public static final int DIDFT_NOCOLLECTION = 16776960;
    public static final int DIDF_ABSAXIS = 1;
    public static final int DIDF_RELAXIS = 2;
    public static final int DI_OK = 0;
    public static final int DI_NOEFFECT = 1;
    public static final int DI_PROPNOEFFECT = 1;
    public static final int DI_POLLEDDEVICE = 2;
    public static final int DI_DOWNLOADSKIPPED = 3;
    public static final int DI_EFFECTRESTARTED = 4;
    public static final int DI_TRUNCATED = 8;
    public static final int DI_SETTINGSNOTSAVED = 11;
    public static final int DI_TRUNCATEDANDRESTARTED = 12;
    public static final int DI_BUFFEROVERFLOW = 1;
    public static final int DIERR_INPUTLOST = -2147024866;
    public static final int DIERR_NOTACQUIRED = -2147024868;
    public static final int DIERR_OTHERAPPHASPRIO = -2147024891;
    public static final int DIDOI_FFACTUATOR = 1;
    public static final int DIDOI_FFEFFECTTRIGGER = 2;
    public static final int DIDOI_POLLED = 32768;
    public static final int DIDOI_ASPECTPOSITION = 256;
    public static final int DIDOI_ASPECTVELOCITY = 512;
    public static final int DIDOI_ASPECTACCEL = 768;
    public static final int DIDOI_ASPECTFORCE = 1024;
    public static final int DIDOI_ASPECTMASK = 3840;
    public static final int DIDOI_GUIDISUSAGE = 65536;
    public static final int DIEFT_ALL = 0;
    public static final int DIEFT_CONSTANTFORCE = 1;
    public static final int DIEFT_RAMPFORCE = 2;
    public static final int DIEFT_PERIODIC = 3;
    public static final int DIEFT_CONDITION = 4;
    public static final int DIEFT_CUSTOMFORCE = 5;
    public static final int DIEFT_HARDWARE = 255;
    public static final int DIEFT_FFATTACK = 512;
    public static final int DIEFT_FFFADE = 1024;
    public static final int DIEFT_SATURATION = 2048;
    public static final int DIEFT_POSNEGCOEFFICIENTS = 4096;
    public static final int DIEFT_POSNEGSATURATION = 8192;
    public static final int DIEFT_DEADBAND = 16384;
    public static final int DIEFT_STARTDELAY = 32768;
    public static final int DIEFF_OBJECTIDS = 1;
    public static final int DIEFF_OBJECTOFFSETS = 2;
    public static final int DIEFF_CARTESIAN = 16;
    public static final int DIEFF_POLAR = 32;
    public static final int DIEFF_SPHERICAL = 64;
    public static final int DIEP_DURATION = 1;
    public static final int DIEP_SAMPLEPERIOD = 2;
    public static final int DIEP_GAIN = 4;
    public static final int DIEP_TRIGGERBUTTON = 8;
    public static final int DIEP_TRIGGERREPEATINTERVAL = 16;
    public static final int DIEP_AXES = 32;
    public static final int DIEP_DIRECTION = 64;
    public static final int DIEP_ENVELOPE = 128;
    public static final int DIEP_TYPESPECIFICPARAMS = 256;
    public static final int DIEP_STARTDELAY = 512;
    public static final int DIEP_ALLPARAMS_DX5 = 511;
    public static final int DIEP_ALLPARAMS = 1023;
    public static final int DIEP_START = 536870912;
    public static final int DIEP_NORESTART = 1073741824;
    public static final int DIEP_NODOWNLOAD = Integer.MIN_VALUE;
    public static final int DIEB_NOTRIGGER = -1;
    public static final int INFINITE = -1;
    public static final int DI_DEGREES = 100;
    public static final int DI_FFNOMINALMAX = 10000;
    public static final int DI_SECONDS = 1000000;
    public static final int DIPROPRANGE_NOMIN = Integer.MIN_VALUE;
    public static final int DIPROPRANGE_NOMAX = Integer.MAX_VALUE;
    private final DummyWindow window;
    private final long address;
    private final int dev_type;
    private final int dev_subtype;
    private final String instance_name;
    private final String product_name;
    private final List objects;
    private final List effects;
    private final List rumblers;
    private final int[] device_state;
    private final Map object_to_component;
    private final boolean axes_in_relative_mode;
    private boolean released;
    private DataQueue queue;
    private int button_counter;
    private int current_format_offset;
    
    public IDirectInputDevice(final DummyWindow window, final long address, final byte[] instance_guid, final byte[] product_guid, final int dev_type, final int dev_subtype, final String instance_name, final String product_name) throws IOException {
        this.objects = new ArrayList();
        this.effects = new ArrayList();
        this.rumblers = new ArrayList();
        this.object_to_component = new HashMap();
        this.window = window;
        this.address = address;
        this.product_name = product_name;
        this.instance_name = instance_name;
        this.dev_type = dev_type;
        this.dev_subtype = dev_subtype;
        this.enumObjects();
        try {
            this.enumEffects();
            this.createRumblers();
        }
        catch (IOException e) {
            ControllerEnvironment.logln("Failed to create rumblers: " + e.getMessage());
        }
        boolean all_relative = true;
        boolean has_axis = false;
        for (int i = 0; i < this.objects.size(); ++i) {
            final DIDeviceObject obj = this.objects.get(i);
            if (obj.isAxis()) {
                has_axis = true;
                if (!obj.isRelative()) {
                    all_relative = false;
                    break;
                }
            }
        }
        this.axes_in_relative_mode = (all_relative && has_axis);
        final int axis_mode = all_relative ? 2 : 1;
        this.setDataFormat(axis_mode);
        if (this.rumblers.size() > 0) {
            try {
                this.setCooperativeLevel(9);
            }
            catch (IOException e2) {
                this.setCooperativeLevel(10);
            }
        }
        else {
            this.setCooperativeLevel(10);
        }
        this.setBufferSize(32);
        this.acquire();
        this.device_state = new int[this.objects.size()];
    }
    
    public final boolean areAxesRelative() {
        return this.axes_in_relative_mode;
    }
    
    public final Rumbler[] getRumblers() {
        return this.rumblers.toArray(new Rumbler[0]);
    }
    
    private final List createRumblers() throws IOException {
        final DIDeviceObject x_axis = this.lookupObjectByGUID(1);
        if (x_axis == null) {
            return this.rumblers;
        }
        final DIDeviceObject[] axes = { x_axis };
        final long[] directions = { 0L };
        for (int i = 0; i < this.effects.size(); ++i) {
            final DIEffectInfo info = this.effects.get(i);
            if ((info.getEffectType() & 0xFF) == 0x3 && (info.getDynamicParams() & 0x4) != 0x0) {
                this.rumblers.add(this.createPeriodicRumbler(axes, directions, info));
            }
        }
        return this.rumblers;
    }
    
    private final Rumbler createPeriodicRumbler(final DIDeviceObject[] axes, final long[] directions, final DIEffectInfo info) throws IOException {
        final int[] axis_ids = new int[axes.length];
        for (int i = 0; i < axis_ids.length; ++i) {
            axis_ids[i] = axes[i].getDIIdentifier();
        }
        final long effect_address = nCreatePeriodicEffect(this.address, info.getGUID(), 17, -1, 0, 10000, -1, 0, axis_ids, directions, 0, 0, 0, 0, 10000, 0, 0, 50000, 0);
        return new IDirectInputEffect(effect_address, info);
    }
    
    private static final native long nCreatePeriodicEffect(final long p0, final byte[] p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int[] p8, final long[] p9, final int p10, final int p11, final int p12, final int p13, final int p14, final int p15, final int p16, final int p17, final int p18) throws IOException;
    
    private final DIDeviceObject lookupObjectByGUID(final int guid_id) {
        for (int i = 0; i < this.objects.size(); ++i) {
            final DIDeviceObject object = this.objects.get(i);
            if (guid_id == object.getGUIDType()) {
                return object;
            }
        }
        return null;
    }
    
    public final int getPollData(final DIDeviceObject object) {
        return this.device_state[object.getFormatOffset()];
    }
    
    public final DIDeviceObject mapEvent(final DIDeviceObjectData event) {
        final int format_offset = event.getFormatOffset() / 4;
        return this.objects.get(format_offset);
    }
    
    public final DIComponent mapObject(final DIDeviceObject object) {
        return this.object_to_component.get(object);
    }
    
    public final void registerComponent(final DIDeviceObject object, final DIComponent component) {
        this.object_to_component.put(object, component);
    }
    
    public final synchronized void pollAll() throws IOException {
        this.checkReleased();
        this.poll();
        this.getDeviceState(this.device_state);
        this.queue.compact();
        this.getDeviceData(this.queue);
        this.queue.flip();
    }
    
    public final synchronized boolean getNextEvent(final DIDeviceObjectData data) {
        final DIDeviceObjectData next_event = (DIDeviceObjectData)this.queue.get();
        if (next_event == null) {
            return false;
        }
        data.set(next_event);
        return true;
    }
    
    private final void poll() throws IOException {
        final int res = nPoll(this.address);
        if (res == 0 || res == 1) {
            return;
        }
        if (res == -2147024868) {
            this.acquire();
            return;
        }
        throw new IOException("Failed to poll device (" + Integer.toHexString(res) + ")");
    }
    
    private static final native int nPoll(final long p0) throws IOException;
    
    private final void acquire() throws IOException {
        final int res = nAcquire(this.address);
        if (res != 0 && res != -2147024891 && res != 1) {
            throw new IOException("Failed to acquire device (" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nAcquire(final long p0);
    
    private final void unacquire() throws IOException {
        final int res = nUnacquire(this.address);
        if (res != 0 && res != 1) {
            throw new IOException("Failed to unAcquire device (" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nUnacquire(final long p0);
    
    private final boolean getDeviceData(final DataQueue queue) throws IOException {
        final int res = nGetDeviceData(this.address, 0, queue, queue.getElements(), queue.position(), queue.remaining());
        if (res == 0 || res == 1) {
            return true;
        }
        if (res == -2147024868) {
            this.acquire();
            return false;
        }
        throw new IOException("Failed to get device data (" + Integer.toHexString(res) + ")");
    }
    
    private static final native int nGetDeviceData(final long p0, final int p1, final DataQueue p2, final Object[] p3, final int p4, final int p5);
    
    private final void getDeviceState(final int[] device_state) throws IOException {
        final int res = nGetDeviceState(this.address, device_state);
        if (res == 0) {
            return;
        }
        if (res == -2147024868) {
            Arrays.fill(device_state, 0);
            this.acquire();
            return;
        }
        throw new IOException("Failed to get device state (" + Integer.toHexString(res) + ")");
    }
    
    private static final native int nGetDeviceState(final long p0, final int[] p1);
    
    private final void setDataFormat(final int flags) throws IOException {
        final DIDeviceObject[] device_objects = new DIDeviceObject[this.objects.size()];
        this.objects.toArray(device_objects);
        final int res = nSetDataFormat(this.address, flags, device_objects);
        if (res != 0) {
            throw new IOException("Failed to set data format (" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nSetDataFormat(final long p0, final int p1, final DIDeviceObject[] p2);
    
    public final String getProductName() {
        return this.product_name;
    }
    
    public final int getType() {
        return this.dev_type;
    }
    
    public final List getObjects() {
        return this.objects;
    }
    
    private final void enumEffects() throws IOException {
        final int res = this.nEnumEffects(this.address, 0);
        if (res != 0) {
            throw new IOException("Failed to enumerate effects (" + Integer.toHexString(res) + ")");
        }
    }
    
    private final native int nEnumEffects(final long p0, final int p1);
    
    private final void addEffect(final byte[] guid, final int guid_id, final int effect_type, final int static_params, final int dynamic_params, final String name) {
        this.effects.add(new DIEffectInfo(this, guid, guid_id, effect_type, static_params, dynamic_params, name));
    }
    
    private final void enumObjects() throws IOException {
        final int res = this.nEnumObjects(this.address, 31);
        if (res != 0) {
            throw new IOException("Failed to enumerate objects (" + Integer.toHexString(res) + ")");
        }
    }
    
    private final native int nEnumObjects(final long p0, final int p1);
    
    public final synchronized long[] getRangeProperty(final int object_identifier) throws IOException {
        this.checkReleased();
        final long[] range = new long[2];
        final int res = nGetRangeProperty(this.address, object_identifier, range);
        if (res != 0) {
            throw new IOException("Failed to get object range (" + res + ")");
        }
        return range;
    }
    
    private static final native int nGetRangeProperty(final long p0, final int p1, final long[] p2);
    
    public final synchronized int getDeadzoneProperty(final int object_identifier) throws IOException {
        this.checkReleased();
        return nGetDeadzoneProperty(this.address, object_identifier);
    }
    
    private static final native int nGetDeadzoneProperty(final long p0, final int p1) throws IOException;
    
    private final void addObject(final byte[] guid, final int guid_type, final int identifier, final int type, final int instance, final int flags, final String name) throws IOException {
        final Component.Identifier id = this.getIdentifier(guid_type, type, instance);
        final int format_offset = this.current_format_offset++;
        final DIDeviceObject obj = new DIDeviceObject(this, id, guid, guid_type, identifier, type, instance, flags, name, format_offset);
        this.objects.add(obj);
    }
    
    private static final Component.Identifier.Key getKeyIdentifier(final int key_instance) {
        return DIIdentifierMap.getKeyIdentifier(key_instance);
    }
    
    private final Component.Identifier.Button getNextButtonIdentifier() {
        final int button_id = this.button_counter++;
        return DIIdentifierMap.getButtonIdentifier(button_id);
    }
    
    private final Component.Identifier getIdentifier(final int guid_type, final int type, final int instance) {
        switch (guid_type) {
            case 1: {
                return Component.Identifier.Axis.X;
            }
            case 2: {
                return Component.Identifier.Axis.Y;
            }
            case 3: {
                return Component.Identifier.Axis.Z;
            }
            case 4: {
                return Component.Identifier.Axis.RX;
            }
            case 5: {
                return Component.Identifier.Axis.RY;
            }
            case 6: {
                return Component.Identifier.Axis.RZ;
            }
            case 7: {
                return Component.Identifier.Axis.SLIDER;
            }
            case 10: {
                return Component.Identifier.Axis.POV;
            }
            case 9: {
                return getKeyIdentifier(instance);
            }
            case 8: {
                return this.getNextButtonIdentifier();
            }
            default: {
                return Component.Identifier.Axis.UNKNOWN;
            }
        }
    }
    
    public final synchronized void setBufferSize(final int size) throws IOException {
        this.checkReleased();
        this.unacquire();
        final int res = nSetBufferSize(this.address, size);
        if (res != 0 && res != 1 && res != 2) {
            throw new IOException("Failed to set buffer size (" + Integer.toHexString(res) + ")");
        }
        (this.queue = new DataQueue(size, DIDeviceObjectData.class)).position(this.queue.limit());
        this.acquire();
    }
    
    private static final native int nSetBufferSize(final long p0, final int p1);
    
    public final synchronized void setCooperativeLevel(final int flags) throws IOException {
        this.checkReleased();
        final int res = nSetCooperativeLevel(this.address, this.window.getHwnd(), flags);
        if (res != 0) {
            throw new IOException("Failed to set cooperative level (" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nSetCooperativeLevel(final long p0, final long p1, final int p2);
    
    public final synchronized void release() {
        if (!this.released) {
            this.released = true;
            for (int i = 0; i < this.rumblers.size(); ++i) {
                final IDirectInputEffect effect = this.rumblers.get(i);
                effect.release();
            }
            nRelease(this.address);
        }
    }
    
    private static final native void nRelease(final long p0);
    
    private final void checkReleased() throws IOException {
        if (this.released) {
            throw new IOException("Device is released");
        }
    }
    
    protected void finalize() {
        this.release();
    }
}
