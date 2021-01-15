package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.joran.action.Action;
import java.util.List;

public abstract interface RuleStore
{
  public abstract void addRule(ElementSelector paramElementSelector, String paramString)
    throws ClassNotFoundException;
  
  public abstract void addRule(ElementSelector paramElementSelector, Action paramAction);
  
  public abstract List<Action> matchActions(ElementPath paramElementPath);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\RuleStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */