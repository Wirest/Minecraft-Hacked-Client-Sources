package javassist.tools.web;

import java.io.IOException;
import java.net.Socket;

class ServiceThread extends Thread {
   Webserver web;
   Socket sock;

   public ServiceThread(Webserver w, Socket s) {
      this.web = w;
      this.sock = s;
   }

   public void run() {
      try {
         this.web.process(this.sock);
      } catch (IOException var2) {
      }

   }
}
