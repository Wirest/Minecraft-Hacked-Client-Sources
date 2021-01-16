package javax.net.ssl;

import java.util.EventObject;

public class SSLSessionBindingEvent extends EventObject {
   private String a;

   public SSLSessionBindingEvent(SSLSession var1, String var2) {
      super(var1);
      this.a = var2;
   }

   public String getName() {
      return this.a;
   }

   public SSLSession getSession() {
      return (SSLSession)this.getSource();
   }
}
