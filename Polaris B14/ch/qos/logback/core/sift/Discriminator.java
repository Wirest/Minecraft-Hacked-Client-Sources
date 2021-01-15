package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.LifeCycle;

public abstract interface Discriminator<E>
  extends LifeCycle
{
  public abstract String getDiscriminatingValue(E paramE);
  
  public abstract String getKey();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\Discriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */