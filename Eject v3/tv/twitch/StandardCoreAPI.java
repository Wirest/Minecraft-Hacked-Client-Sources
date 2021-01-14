package tv.twitch;

public class StandardCoreAPI
        extends CoreAPI {
    public StandardCoreAPI() {
        try {
            System.loadLibrary("twitchsdk");
        } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
            System.out.println("If on Windows, make sure to provide all of the necessary dll's as specified in the twitchsdk README. Also, make sure to set the PATH environment variable to point to the directory containing the dll's.");
            throw localUnsatisfiedLinkError;
        }
    }

    private static native ErrorCode TTV_Java_Init(String paramString1, String paramString2);

    private static native ErrorCode TTV_Java_Shutdown();

    private static native ErrorCode TTV_Java_SetTraceLevel(int paramInt);

    private static native ErrorCode TTV_Java_SetTraceOutput(String paramString);

    private static native String TTV_Java_ErrorToString(ErrorCode paramErrorCode);

    protected void finalize() {
    }

    public ErrorCode init(String paramString1, String paramString2) {
        return TTV_Java_Init(paramString1, paramString2);
    }

    public ErrorCode shutdown() {
        return TTV_Java_Shutdown();
    }

    public ErrorCode setTraceLevel(MessageLevel paramMessageLevel) {
        if (paramMessageLevel == null) {
            paramMessageLevel = MessageLevel.TTV_ML_NONE;
        }
        return TTV_Java_SetTraceLevel(paramMessageLevel.getValue());
    }

    public ErrorCode setTraceOutput(String paramString) {
        return TTV_Java_SetTraceOutput(paramString);
    }

    public String errorToString(ErrorCode paramErrorCode) {
        if (paramErrorCode == null) {
            return null;
        }
        return TTV_Java_ErrorToString(paramErrorCode);
    }
}




