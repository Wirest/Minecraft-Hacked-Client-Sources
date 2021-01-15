package org.slf4j.spi;

import java.util.Map;

public abstract interface MDCAdapter
{
  public abstract void put(String paramString1, String paramString2);
  
  public abstract String get(String paramString);
  
  public abstract void remove(String paramString);
  
  public abstract void clear();
  
  public abstract Map<String, String> getCopyOfContextMap();
  
  public abstract void setContextMap(Map<String, String> paramMap);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\spi\MDCAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */