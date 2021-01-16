package javax.net.ssl;

import java.util.EventListener;

public interface SSLSessionBindingListener extends EventListener {
   void valueBound(SSLSessionBindingEvent var1);

   void valueUnbound(SSLSessionBindingEvent var1);
}
