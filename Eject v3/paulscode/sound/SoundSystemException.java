package paulscode.sound;

public class SoundSystemException
        extends Exception {
    public static final int ERROR_NONE = 0;
    public static final int UNKNOWN_ERROR = 1;
    public static final int NULL_PARAMETER = 2;
    public static final int CLASS_TYPE_MISMATCH = 3;
    public static final int LIBRARY_NULL = 4;
    public static final int LIBRARY_TYPE = 5;
    private int myType = 1;

    public SoundSystemException(String paramString) {
        super(paramString);
    }

    public SoundSystemException(String paramString, int paramInt) {
        super(paramString);
        this.myType = paramInt;
    }

    public int getType() {
        return this.myType;
    }
}




