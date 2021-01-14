package paulscode.sound;

public class CommandObject {
    public static final int INITIALIZE = 1;
    public static final int LOAD_SOUND = 2;
    public static final int LOAD_DATA = 3;
    public static final int UNLOAD_SOUND = 4;
    public static final int QUEUE_SOUND = 5;
    public static final int DEQUEUE_SOUND = 6;
    public static final int FADE_OUT = 7;
    public static final int FADE_OUT_IN = 8;
    public static final int CHECK_FADE_VOLUMES = 9;
    public static final int NEW_SOURCE = 10;
    public static final int RAW_DATA_STREAM = 11;
    public static final int QUICK_PLAY = 12;
    public static final int SET_POSITION = 13;
    public static final int SET_VOLUME = 14;
    public static final int SET_PITCH = 15;
    public static final int SET_PRIORITY = 16;
    public static final int SET_LOOPING = 17;
    public static final int SET_ATTENUATION = 18;
    public static final int SET_DIST_OR_ROLL = 19;
    public static final int CHANGE_DOPPLER_FACTOR = 20;
    public static final int CHANGE_DOPPLER_VELOCITY = 21;
    public static final int SET_VELOCITY = 22;
    public static final int SET_LISTENER_VELOCITY = 23;
    public static final int PLAY = 24;
    public static final int FEED_RAW_AUDIO_DATA = 25;
    public static final int PAUSE = 26;
    public static final int STOP = 27;
    public static final int REWIND = 28;
    public static final int FLUSH = 29;
    public static final int CULL = 30;
    public static final int ACTIVATE = 31;
    public static final int SET_TEMPORARY = 32;
    public static final int REMOVE_SOURCE = 33;
    public static final int MOVE_LISTENER = 34;
    public static final int SET_LISTENER_POSITION = 35;
    public static final int TURN_LISTENER = 36;
    public static final int SET_LISTENER_ANGLE = 37;
    public static final int SET_LISTENER_ORIENTATION = 38;
    public static final int SET_MASTER_VOLUME = 39;
    public static final int NEW_LIBRARY = 40;
    public byte[] buffer;
    public int[] intArgs;
    public float[] floatArgs;
    public long[] longArgs;
    public boolean[] boolArgs;
    public String[] stringArgs;
    public Class[] classArgs;
    public Object[] objectArgs;
    public int Command;

    public CommandObject(int paramInt) {
        this.Command = paramInt;
    }

    public CommandObject(int paramInt1, int paramInt2) {
        this.Command = paramInt1;
        this.intArgs = new int[1];
        this.intArgs[0] = paramInt2;
    }

    public CommandObject(int paramInt, Class paramClass) {
        this.Command = paramInt;
        this.classArgs = new Class[1];
        this.classArgs[0] = paramClass;
    }

    public CommandObject(int paramInt, float paramFloat) {
        this.Command = paramInt;
        this.floatArgs = new float[1];
        this.floatArgs[0] = paramFloat;
    }

    public CommandObject(int paramInt, String paramString) {
        this.Command = paramInt;
        this.stringArgs = new String[1];
        this.stringArgs[0] = paramString;
    }

    public CommandObject(int paramInt, Object paramObject) {
        this.Command = paramInt;
        this.objectArgs = new Object[1];
        this.objectArgs[0] = paramObject;
    }

    public CommandObject(int paramInt, String paramString, Object paramObject) {
        this.Command = paramInt;
        this.stringArgs = new String[1];
        this.stringArgs[0] = paramString;
        this.objectArgs = new Object[1];
        this.objectArgs[0] = paramObject;
    }

    public CommandObject(int paramInt, String paramString, byte[] paramArrayOfByte) {
        this.Command = paramInt;
        this.stringArgs = new String[1];
        this.stringArgs[0] = paramString;
        this.buffer = paramArrayOfByte;
    }

    public CommandObject(int paramInt, String paramString, Object paramObject, long paramLong) {
        this.Command = paramInt;
        this.stringArgs = new String[1];
        this.stringArgs[0] = paramString;
        this.objectArgs = new Object[1];
        this.objectArgs[0] = paramObject;
        this.longArgs = new long[1];
        this.longArgs[0] = paramLong;
    }

    public CommandObject(int paramInt, String paramString, Object paramObject, long paramLong1, long paramLong2) {
        this.Command = paramInt;
        this.stringArgs = new String[1];
        this.stringArgs[0] = paramString;
        this.objectArgs = new Object[1];
        this.objectArgs[0] = paramObject;
        this.longArgs = new long[2];
        this.longArgs[0] = paramLong1;
        this.longArgs[1] = paramLong2;
    }

    public CommandObject(int paramInt, String paramString1, String paramString2) {
        this.Command = paramInt;
        this.stringArgs = new String[2];
        this.stringArgs[0] = paramString1;
        this.stringArgs[1] = paramString2;
    }

    public CommandObject(int paramInt1, String paramString, int paramInt2) {
        this.Command = paramInt1;
        this.intArgs = new int[1];
        this.stringArgs = new String[1];
        this.intArgs[0] = paramInt2;
        this.stringArgs[0] = paramString;
    }

    public CommandObject(int paramInt, String paramString, float paramFloat) {
        this.Command = paramInt;
        this.floatArgs = new float[1];
        this.stringArgs = new String[1];
        this.floatArgs[0] = paramFloat;
        this.stringArgs[0] = paramString;
    }

    public CommandObject(int paramInt, String paramString, boolean paramBoolean) {
        this.Command = paramInt;
        this.boolArgs = new boolean[1];
        this.stringArgs = new String[1];
        this.boolArgs[0] = paramBoolean;
        this.stringArgs[0] = paramString;
    }

    public CommandObject(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
        this.Command = paramInt;
        this.floatArgs = new float[3];
        this.floatArgs[0] = paramFloat1;
        this.floatArgs[1] = paramFloat2;
        this.floatArgs[2] = paramFloat3;
    }

    public CommandObject(int paramInt, String paramString, float paramFloat1, float paramFloat2, float paramFloat3) {
        this.Command = paramInt;
        this.floatArgs = new float[3];
        this.stringArgs = new String[1];
        this.floatArgs[0] = paramFloat1;
        this.floatArgs[1] = paramFloat2;
        this.floatArgs[2] = paramFloat3;
        this.stringArgs[0] = paramString;
    }

    public CommandObject(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
        this.Command = paramInt;
        this.floatArgs = new float[6];
        this.floatArgs[0] = paramFloat1;
        this.floatArgs[1] = paramFloat2;
        this.floatArgs[2] = paramFloat3;
        this.floatArgs[3] = paramFloat4;
        this.floatArgs[4] = paramFloat5;
        this.floatArgs[5] = paramFloat6;
    }

    public CommandObject(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt2, float paramFloat4) {
        this.Command = paramInt1;
        this.intArgs = new int[1];
        this.floatArgs = new float[4];
        this.boolArgs = new boolean[3];
        this.stringArgs = new String[1];
        this.objectArgs = new Object[1];
        this.intArgs[0] = paramInt2;
        this.floatArgs[0] = paramFloat1;
        this.floatArgs[1] = paramFloat2;
        this.floatArgs[2] = paramFloat3;
        this.floatArgs[3] = paramFloat4;
        this.boolArgs[0] = paramBoolean1;
        this.boolArgs[1] = paramBoolean2;
        this.boolArgs[2] = paramBoolean3;
        this.stringArgs[0] = paramString;
        this.objectArgs[0] = paramObject;
    }

    public CommandObject(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt2, float paramFloat4, boolean paramBoolean4) {
        this.Command = paramInt1;
        this.intArgs = new int[1];
        this.floatArgs = new float[4];
        this.boolArgs = new boolean[4];
        this.stringArgs = new String[1];
        this.objectArgs = new Object[1];
        this.intArgs[0] = paramInt2;
        this.floatArgs[0] = paramFloat1;
        this.floatArgs[1] = paramFloat2;
        this.floatArgs[2] = paramFloat3;
        this.floatArgs[3] = paramFloat4;
        this.boolArgs[0] = paramBoolean1;
        this.boolArgs[1] = paramBoolean2;
        this.boolArgs[2] = paramBoolean3;
        this.boolArgs[3] = paramBoolean4;
        this.stringArgs[0] = paramString;
        this.objectArgs[0] = paramObject;
    }

    public CommandObject(int paramInt1, Object paramObject, boolean paramBoolean, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt2, float paramFloat4) {
        this.Command = paramInt1;
        this.intArgs = new int[1];
        this.floatArgs = new float[4];
        this.boolArgs = new boolean[1];
        this.stringArgs = new String[1];
        this.objectArgs = new Object[1];
        this.intArgs[0] = paramInt2;
        this.floatArgs[0] = paramFloat1;
        this.floatArgs[1] = paramFloat2;
        this.floatArgs[2] = paramFloat3;
        this.floatArgs[3] = paramFloat4;
        this.boolArgs[0] = paramBoolean;
        this.stringArgs[0] = paramString;
        this.objectArgs[0] = paramObject;
    }
}




