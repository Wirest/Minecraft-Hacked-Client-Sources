package ch.qos.logback.core.spi;

import java.io.Serializable;

public abstract interface PreSerializationTransformer<E>
{
  public abstract Serializable transform(E paramE);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\PreSerializationTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */