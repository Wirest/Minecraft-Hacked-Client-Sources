package org.slf4j.spi;

import org.slf4j.ILoggerFactory;

public abstract interface LoggerFactoryBinder
{
  public abstract ILoggerFactory getLoggerFactory();
  
  public abstract String getLoggerFactoryClassStr();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\spi\LoggerFactoryBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */