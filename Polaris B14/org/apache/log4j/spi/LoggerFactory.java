package org.apache.log4j.spi;

import org.apache.log4j.Logger;

public abstract interface LoggerFactory
{
  public abstract Logger makeNewLoggerInstance(String paramString);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\spi\LoggerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */