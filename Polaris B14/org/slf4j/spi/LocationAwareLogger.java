package org.slf4j.spi;

import org.slf4j.Logger;
import org.slf4j.Marker;

public abstract interface LocationAwareLogger
  extends Logger
{
  public static final int TRACE_INT = 0;
  public static final int DEBUG_INT = 10;
  public static final int INFO_INT = 20;
  public static final int WARN_INT = 30;
  public static final int ERROR_INT = 40;
  
  public abstract void log(Marker paramMarker, String paramString1, int paramInt, String paramString2, Object[] paramArrayOfObject, Throwable paramThrowable);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\spi\LocationAwareLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */