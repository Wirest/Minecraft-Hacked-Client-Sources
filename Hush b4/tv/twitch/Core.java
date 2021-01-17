// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch;

public class Core
{
    private static Core s_Instance;
    private CoreAPI m_CoreAPI;
    private String m_ClientId;
    private int m_NumInitializations;
    
    public static Core getInstance() {
        return Core.s_Instance;
    }
    
    public Core(final CoreAPI coreAPI) {
        this.m_CoreAPI = null;
        this.m_ClientId = null;
        this.m_NumInitializations = 0;
        this.m_CoreAPI = coreAPI;
        if (Core.s_Instance == null) {
            Core.s_Instance = this;
        }
    }
    
    public boolean getIsInitialized() {
        return this.m_NumInitializations > 0;
    }
    
    public ErrorCode initialize(final String clientId, final String s) {
        if (this.m_NumInitializations == 0) {
            this.m_ClientId = clientId;
        }
        else if (clientId != this.m_ClientId) {
            return ErrorCode.TTV_EC_INVALID_CLIENTID;
        }
        ++this.m_NumInitializations;
        if (this.m_NumInitializations > 1) {
            return ErrorCode.TTV_EC_SUCCESS;
        }
        final ErrorCode init = this.m_CoreAPI.init(clientId, s);
        if (ErrorCode.failed(init)) {
            --this.m_NumInitializations;
            this.m_ClientId = null;
        }
        return init;
    }
    
    public ErrorCode shutdown() {
        if (this.m_NumInitializations == 0) {
            return ErrorCode.TTV_EC_NOT_INITIALIZED;
        }
        --this.m_NumInitializations;
        ErrorCode errorCode = ErrorCode.TTV_EC_SUCCESS;
        if (this.m_NumInitializations == 0) {
            errorCode = this.m_CoreAPI.shutdown();
            if (ErrorCode.failed(errorCode)) {
                ++this.m_NumInitializations;
            }
            else if (Core.s_Instance == this) {
                Core.s_Instance = null;
            }
        }
        return errorCode;
    }
    
    public ErrorCode setTraceLevel(final MessageLevel traceLevel) {
        return this.m_CoreAPI.setTraceLevel(traceLevel);
    }
    
    public String errorToString(final ErrorCode errorCode) {
        return this.m_CoreAPI.errorToString(errorCode);
    }
    
    static {
        Core.s_Instance = null;
    }
}
