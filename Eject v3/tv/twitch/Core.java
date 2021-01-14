package tv.twitch;

public class Core {
    private static Core s_Instance = null;
    private CoreAPI m_CoreAPI = null;
    private String m_ClientId = null;
    private int m_NumInitializations = 0;

    public Core(CoreAPI paramCoreAPI) {
        this.m_CoreAPI = paramCoreAPI;
        if (s_Instance == null) {
            s_Instance = this;
        }
    }

    public static Core getInstance() {
        return s_Instance;
    }

    public boolean getIsInitialized() {
        return this.m_NumInitializations > 0;
    }

    public ErrorCode initialize(String paramString1, String paramString2) {
        if (this.m_NumInitializations == 0) {
            this.m_ClientId = paramString1;
        } else if (paramString1 != this.m_ClientId) {
            return ErrorCode.TTV_EC_INVALID_CLIENTID;
        }
        this.m_NumInitializations |= 0x1;
        if (this.m_NumInitializations > 1) {
            return ErrorCode.TTV_EC_SUCCESS;
        }
        ErrorCode localErrorCode = this.m_CoreAPI.init(paramString1, paramString2);
        if (ErrorCode.failed(localErrorCode)) {
            this.m_NumInitializations -= 1;
            this.m_ClientId = null;
        }
        return localErrorCode;
    }

    public ErrorCode shutdown() {
        if (this.m_NumInitializations == 0) {
            return ErrorCode.TTV_EC_NOT_INITIALIZED;
        }
        this.m_NumInitializations -= 1;
        ErrorCode localErrorCode = ErrorCode.TTV_EC_SUCCESS;
        if (this.m_NumInitializations == 0) {
            localErrorCode = this.m_CoreAPI.shutdown();
            if (ErrorCode.failed(localErrorCode)) {
                this.m_NumInitializations |= 0x1;
            } else if (s_Instance == this) {
                s_Instance = null;
            }
        }
        return localErrorCode;
    }

    public ErrorCode setTraceLevel(MessageLevel paramMessageLevel) {
        ErrorCode localErrorCode = this.m_CoreAPI.setTraceLevel(paramMessageLevel);
        return localErrorCode;
    }

    public String errorToString(ErrorCode paramErrorCode) {
        String str = this.m_CoreAPI.errorToString(paramErrorCode);
        return str;
    }
}




