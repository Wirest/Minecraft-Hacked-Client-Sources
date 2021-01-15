package io.netty.handler.ssl;

import java.util.List;
import java.util.Set;

public abstract interface CipherSuiteFilter
{
  public abstract String[] filterCipherSuites(Iterable<String> paramIterable, List<String> paramList, Set<String> paramSet);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\CipherSuiteFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */