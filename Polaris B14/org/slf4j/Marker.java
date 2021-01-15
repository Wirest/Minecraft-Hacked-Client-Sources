package org.slf4j;

import java.io.Serializable;
import java.util.Iterator;

public abstract interface Marker
  extends Serializable
{
  public static final String ANY_MARKER = "*";
  public static final String ANY_NON_NULL_MARKER = "+";
  
  public abstract String getName();
  
  public abstract void add(Marker paramMarker);
  
  public abstract boolean remove(Marker paramMarker);
  
  /**
   * @deprecated
   */
  public abstract boolean hasChildren();
  
  public abstract boolean hasReferences();
  
  public abstract Iterator<Marker> iterator();
  
  public abstract boolean contains(Marker paramMarker);
  
  public abstract boolean contains(String paramString);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\Marker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */