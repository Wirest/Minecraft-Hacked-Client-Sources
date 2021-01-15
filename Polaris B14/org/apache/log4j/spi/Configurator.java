package org.apache.log4j.spi;

import java.net.URL;

public abstract interface Configurator
{
  public static final String INHERITED = "inherited";
  public static final String NULL = "null";
  
  public abstract void doConfigure(URL paramURL, LoggerRepository paramLoggerRepository);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\spi\Configurator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */