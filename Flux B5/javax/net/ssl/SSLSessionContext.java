package javax.net.ssl;

import java.util.Enumeration;

public interface SSLSessionContext {
   Enumeration getIds();

   SSLSession getSession(byte[] var1);
}
