package javassist.tools.rmi;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import javassist.tools.web.Webserver;

public class AppletServer extends Webserver {
   private StubGenerator stubGen;
   private Hashtable exportedNames;
   private Vector exportedObjects;
   private static final byte[] okHeader = "HTTP/1.0 200 OK\r\n\r\n".getBytes();

   public AppletServer(String port) throws IOException, NotFoundException, CannotCompileException {
      this(Integer.parseInt(port));
   }

   public AppletServer(int port) throws IOException, NotFoundException, CannotCompileException {
      this(ClassPool.getDefault(), new StubGenerator(), port);
   }

   public AppletServer(int port, ClassPool src) throws IOException, NotFoundException, CannotCompileException {
      this(new ClassPool(src), new StubGenerator(), port);
   }

   private AppletServer(ClassPool loader, StubGenerator gen, int port) throws IOException, NotFoundException, CannotCompileException {
      super(port);
      this.exportedNames = new Hashtable();
      this.exportedObjects = new Vector();
      this.stubGen = gen;
      this.addTranslator(loader, gen);
   }

   public void run() {
      super.run();
   }

   public synchronized int exportObject(String name, Object obj) throws CannotCompileException {
      Class clazz = obj.getClass();
      ExportedObject eo = new ExportedObject();
      eo.object = obj;
      eo.methods = clazz.getMethods();
      this.exportedObjects.addElement(eo);
      eo.identifier = this.exportedObjects.size() - 1;
      if (name != null) {
         this.exportedNames.put(name, eo);
      }

      try {
         this.stubGen.makeProxyClass(clazz);
      } catch (NotFoundException var6) {
         throw new CannotCompileException(var6);
      }

      return eo.identifier;
   }

   public void doReply(InputStream in, OutputStream out, String cmd) throws IOException, BadHttpRequest {
      if (cmd.startsWith("POST /rmi ")) {
         this.processRMI(in, out);
      } else if (cmd.startsWith("POST /lookup ")) {
         this.lookupName(cmd, in, out);
      } else {
         super.doReply(in, out, cmd);
      }

   }

   private void processRMI(InputStream ins, OutputStream outs) throws IOException {
      ObjectInputStream in = new ObjectInputStream(ins);
      int objectId = in.readInt();
      int methodId = in.readInt();
      Exception err = null;
      Object rvalue = null;

      try {
         ExportedObject eo = (ExportedObject)this.exportedObjects.elementAt(objectId);
         Object[] args = this.readParameters(in);
         rvalue = this.convertRvalue(eo.methods[methodId].invoke(eo.object, args));
      } catch (Exception var12) {
         err = var12;
         this.logging2(var12.toString());
      }

      outs.write(okHeader);
      ObjectOutputStream out = new ObjectOutputStream(outs);
      if (err != null) {
         out.writeBoolean(false);
         out.writeUTF(err.toString());
      } else {
         try {
            out.writeBoolean(true);
            out.writeObject(rvalue);
         } catch (NotSerializableException var10) {
            this.logging2(var10.toString());
         } catch (InvalidClassException var11) {
            this.logging2(var11.toString());
         }
      }

      out.flush();
      out.close();
      in.close();
   }

   private Object[] readParameters(ObjectInputStream in) throws IOException, ClassNotFoundException {
      int n = in.readInt();
      Object[] args = new Object[n];

      for(int i = 0; i < n; ++i) {
         Object a = in.readObject();
         if (a instanceof RemoteRef) {
            RemoteRef ref = (RemoteRef)a;
            ExportedObject eo = (ExportedObject)this.exportedObjects.elementAt(ref.oid);
            a = eo.object;
         }

         args[i] = a;
      }

      return args;
   }

   private Object convertRvalue(Object rvalue) throws CannotCompileException {
      if (rvalue == null) {
         return null;
      } else {
         String classname = rvalue.getClass().getName();
         return this.stubGen.isProxyClass(classname) ? new RemoteRef(this.exportObject((String)null, rvalue), classname) : rvalue;
      }
   }

   private void lookupName(String cmd, InputStream ins, OutputStream outs) throws IOException {
      ObjectInputStream in = new ObjectInputStream(ins);
      String name = DataInputStream.readUTF(in);
      ExportedObject found = (ExportedObject)this.exportedNames.get(name);
      outs.write(okHeader);
      ObjectOutputStream out = new ObjectOutputStream(outs);
      if (found == null) {
         this.logging2(name + "not found.");
         out.writeInt(-1);
         out.writeUTF("error");
      } else {
         this.logging2(name);
         out.writeInt(found.identifier);
         out.writeUTF(found.object.getClass().getName());
      }

      out.flush();
      out.close();
      in.close();
   }
}
