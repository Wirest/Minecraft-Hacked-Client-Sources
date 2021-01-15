package io.netty.handler.codec.spdy;

import java.util.Set;

public abstract interface SpdySettingsFrame
  extends SpdyFrame
{
  public static final int SETTINGS_MINOR_VERSION = 0;
  public static final int SETTINGS_UPLOAD_BANDWIDTH = 1;
  public static final int SETTINGS_DOWNLOAD_BANDWIDTH = 2;
  public static final int SETTINGS_ROUND_TRIP_TIME = 3;
  public static final int SETTINGS_MAX_CONCURRENT_STREAMS = 4;
  public static final int SETTINGS_CURRENT_CWND = 5;
  public static final int SETTINGS_DOWNLOAD_RETRANS_RATE = 6;
  public static final int SETTINGS_INITIAL_WINDOW_SIZE = 7;
  public static final int SETTINGS_CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
  
  public abstract Set<Integer> ids();
  
  public abstract boolean isSet(int paramInt);
  
  public abstract int getValue(int paramInt);
  
  public abstract SpdySettingsFrame setValue(int paramInt1, int paramInt2);
  
  public abstract SpdySettingsFrame setValue(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract SpdySettingsFrame removeValue(int paramInt);
  
  public abstract boolean isPersistValue(int paramInt);
  
  public abstract SpdySettingsFrame setPersistValue(int paramInt, boolean paramBoolean);
  
  public abstract boolean isPersisted(int paramInt);
  
  public abstract SpdySettingsFrame setPersisted(int paramInt, boolean paramBoolean);
  
  public abstract boolean clearPreviouslyPersistedSettings();
  
  public abstract SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean paramBoolean);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdySettingsFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */