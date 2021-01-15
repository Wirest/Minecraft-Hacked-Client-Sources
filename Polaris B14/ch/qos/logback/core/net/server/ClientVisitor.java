package ch.qos.logback.core.net.server;

public abstract interface ClientVisitor<T extends Client>
{
  public abstract void visit(T paramT);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\ClientVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */