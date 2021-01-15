package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public abstract interface LoggerContextListener
{
  public abstract boolean isResetResistant();
  
  public abstract void onStart(LoggerContext paramLoggerContext);
  
  public abstract void onReset(LoggerContext paramLoggerContext);
  
  public abstract void onStop(LoggerContext paramLoggerContext);
  
  public abstract void onLevelChange(Logger paramLogger, Level paramLevel);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggerContextListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */