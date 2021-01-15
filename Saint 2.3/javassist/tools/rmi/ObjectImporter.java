package javassist.tools.rmi;

import java.applet.Applet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.Socket;
import java.net.URL;

public class ObjectImporter implements Serializable {
   private final byte[] endofline = new byte[]{13, 10};
   private String servername;
   private String orgServername;
   private int port;
   private int orgPort;
   protected byte[] lookupCommand = "POST /lookup HTTP/1.0".getBytes();
   protected byte[] rmiCommand = "POST /rmi HTTP/1.0".getBytes();
   private static final Class[] proxyConstructorParamTypes;

   public ObjectImporter(Applet applet) {
      URL codebase = applet.getCodeBase();
      this.orgServername = this.servername = codebase.getHost();
      this.orgPort = this.port = codebase.getPort();
   }

   public ObjectImporter(String servername, int port) {
      this.orgServername = this.servername = servername;
      this.orgPort = this.port = port;
   }

   public Object getObject(String name) {
      try {
         return this.lookupObject(name);
      } catch (ObjectNotFoundException var3) {
         return null;
      }
   }

   public void setHttpProxy(String host, int port) {
      String proxyHeader = "POST http://" + this.orgServername + ":" + this.orgPort;
      String cmd = proxyHeader + "/lookup HTTP/1.0";
      this.lookupCommand = cmd.getBytes();
      cmd = proxyHeader + "/rmi HTTP/1.0";
      this.rmiCommand = cmd.getBytes();
      this.servername = host;
      this.port = port;
   }

   public Object lookupObject(String name) throws ObjectNotFoundException {
      try {
         Socket sock = new Socket(this.servername, this.port);
         OutputStream out = sock.getOutputStream();
         out.write(this.lookupCommand);
         out.write(this.endofline);
         out.write(this.endofline);
         ObjectOutputStream dout = new ObjectOutputStream(out);
         dout.writeUTF(name);
         dout.flush();
         InputStream in = new BufferedInputStream(sock.getInputStream());
         this.skipHeader(in);
         ObjectInputStream din = new ObjectInputStream(in);
         int n = din.readInt();
         String classname = din.readUTF();
         din.close();
         dout.close();
         sock.close();
         if (n >= 0) {
            return this.createProxy(n, classname);
         }
      } catch (Exception var9) {
         var9.printStackTrace();
         throw new ObjectNotFoundException(name, var9);
      }

      throw new ObjectNotFoundException(name);
   }

   private Object createProxy(int oid, String classname) throws Exception {
      Class c = Class.forName(classname);
      Constructor cons = c.getConstructor(proxyConstructorParamTypes);
      return cons.newInstance(this, new Integer(oid));
   }

   public Object call(int objectid, int methodid, Object[] args) throws RemoteException {
      boolean result;
      Object rvalue;
      String errmsg;
      try {
         Socket sock = new Socket(this.servername, this.port);
         OutputStream out = new BufferedOutputStream(sock.getOutputStream());
         out.write(this.rmiCommand);
         out.write(this.endofline);
         out.write(this.endofline);
         ObjectOutputStream dout = new ObjectOutputStream(out);
         dout.writeInt(objectid);
         dout.writeInt(methodid);
         this.writeParameters(dout, args);
         dout.flush();
         InputStream ins = new BufferedInputStream(sock.getInputStream());
         this.skipHeader(ins);
         ObjectInputStream din = new ObjectInputStream(ins);
         result = din.readBoolean();
         rvalue = null;
         errmsg = null;
         if (result) {
            rvalue = din.readObject();
         } else {
            errmsg = din.readUTF();
         }

         din.close();
         dout.close();
         sock.close();
         if (rvalue instanceof RemoteRef) {
            RemoteRef ref = (RemoteRef)rvalue;
            rvalue = this.createProxy(ref.oid, ref.classname);
         }
      } catch (ClassNotFoundException var13) {
         throw new RemoteException(var13);
      } catch (IOException var14) {
         throw new RemoteException(var14);
      } catch (Exception var15) {
         throw new RemoteException(var15);
      }

      if (result) {
         return rvalue;
      } else {
         throw new RemoteException(errmsg);
      }
   }

   private void skipHeader(InputStream in) throws IOException {
      int len;
      do {
         int c;
         for(len = 0; (c = in.read()) >= 0 && c != 13; ++len) {
         }

         in.read();
      } while(len > 0);

   }

   private void writeParameters(ObjectOutputStream dout, Object[] params) throws IOException {
      int n = params.length;
      dout.writeInt(n);

      for(int i = 0; i < n; ++i) {
         if (params[i] instanceof Proxy) {
            Proxy p = (Proxy)params[i];
            dout.writeObject(new RemoteRef(p._getObjectId()));
         } else {
            dout.writeObject(params[i]);
         }
      }

   }

   static {
      proxyConstructorParamTypes = new Class[]{ObjectImporter.class, Integer.TYPE};
   }
}
