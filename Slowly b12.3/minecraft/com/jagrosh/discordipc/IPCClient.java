package com.jagrosh.discordipc;

import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.UUID;
import org.apache.logging.log4j.core.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

public final class IPCClient implements Closeable {
   private static final Logger LOGGER = (Logger)LoggerFactory.getLogger(IPCClient.class);
   private final int version = 1;
   private final long clientId;
   private final HashMap callbacks = new HashMap();
   private IPCClient.Status status;
   private DiscordBuild build;
   private IPCListener listener;
   private RandomAccessFile pipe;
   private Thread readThread;
   private static final String[] paths = new String[]{"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event;

   public IPCClient(long clientId) {
      this.status = IPCClient.Status.CREATED;
      this.build = null;
      this.listener = null;
      this.pipe = null;
      this.readThread = null;
      this.clientId = clientId;
   }

   public void setListener(IPCListener listener) {
      this.listener = listener;
   }

   public void connect(DiscordBuild... preferredOrder) throws NoDiscordClientException {
      this.checkConnected(false);
      this.status = IPCClient.Status.CONNECTING;
      if (preferredOrder == null || preferredOrder.length == 0) {
         preferredOrder = new DiscordBuild[]{DiscordBuild.ANY};
      }

      this.callbacks.clear();
      this.pipe = null;
      this.build = null;
      RandomAccessFile[] open = new RandomAccessFile[DiscordBuild.values().length];

      int i;
      for(i = 0; i < 10; ++i) {
         try {
            String ipc = getIPC(i);
            LOGGER.debug(String.format("Searching for IPC: %s", ipc));
            this.pipe = new RandomAccessFile(ipc, "rw");
            this.send(Packet.OpCode.HANDSHAKE, (new JSONObject()).put("v", 1).put("client_id", Long.toString(this.clientId)), (Callback)null);
            Packet p = this.read();
            this.build = DiscordBuild.from(p.getJson().getJSONObject("data").getJSONObject("config").getString("api_endpoint"));
            LOGGER.debug(String.format("Found a valid client (%s) with packet: %s", this.build.name(), p.toString()));
            if (this.build == preferredOrder[0] || DiscordBuild.ANY == preferredOrder[0]) {
               LOGGER.info(String.format("Found preferred client: %s", this.build.name()));
               break;
            }

            open[this.build.ordinal()] = this.pipe;
            open[DiscordBuild.ANY.ordinal()] = this.pipe;
            this.build = null;
            this.pipe = null;
         } catch (JSONException | IOException var7) {
            this.pipe = null;
            this.build = null;
         }
      }

      if (this.pipe == null) {
         for(i = 1; i < preferredOrder.length; ++i) {
            DiscordBuild cb = preferredOrder[i];
            LOGGER.debug(String.format("Looking for client build: %s", cb.name()));
            if (open[cb.ordinal()] != null) {
               this.pipe = open[cb.ordinal()];
               open[cb.ordinal()] = null;
               if (cb == DiscordBuild.ANY) {
                  for(int k = 0; k < open.length; ++k) {
                     if (open[k] == this.pipe) {
                        this.build = DiscordBuild.values()[k];
                        open[k] = null;
                     }
                  }
               } else {
                  this.build = cb;
               }

               LOGGER.info(String.format("Found preferred client: %s", this.build.name()));
               break;
            }
         }

         if (this.pipe == null) {
            this.status = IPCClient.Status.DISCONNECTED;
            throw new NoDiscordClientException();
         }
      }

      for(i = 0; i < open.length; ++i) {
         if (i != DiscordBuild.ANY.ordinal() && open[i] != null) {
            try {
               open[i].close();
            } catch (IOException var6) {
               LOGGER.debug("Failed to close an open IPC Pipe!", var6);
            }
         }
      }

      this.status = IPCClient.Status.CONNECTED;
      LOGGER.debug("Client is now connected and ready!");
      if (this.listener != null) {
         this.listener.onReady(this);
      }

      this.startReading();
   }

   public void sendRichPresence(RichPresence presence) throws JSONException {
      this.sendRichPresence(presence, (Callback)null);
   }

   public void sendRichPresence(RichPresence presence, Callback callback) throws JSONException {
      this.checkConnected(true);
      LOGGER.debug("Sending RichPresence to discord: " + (presence == null ? null : presence.toJson().toString()));
      this.send(Packet.OpCode.FRAME, (new JSONObject()).put("cmd", "SET_ACTIVITY").put("args", (new JSONObject()).put("pid", getPID()).put("activity", presence == null ? null : presence.toJson())), callback);
   }

   public void subscribe(IPCClient.Event sub) throws JSONException {
      this.subscribe(sub, (Callback)null);
   }

   public void subscribe(IPCClient.Event sub, Callback callback) throws JSONException {
      this.checkConnected(true);
      if (!sub.isSubscribable()) {
         throw new IllegalStateException("Cannot subscribe to " + sub + " event!");
      } else {
         LOGGER.debug(String.format("Subscribing to Event: %s", sub.name()));
         this.send(Packet.OpCode.FRAME, (new JSONObject()).put("cmd", "SUBSCRIBE").put("evt", sub.name()), callback);
      }
   }

   public IPCClient.Status getStatus() {
      return this.status;
   }

   public void close() {
      this.checkConnected(true);
      LOGGER.debug("Closing IPC Pipe...");

      try {
         this.send(Packet.OpCode.CLOSE, new JSONObject(), (Callback)null);
      } catch (JSONException var2) {
         var2.printStackTrace();
      }

      this.status = IPCClient.Status.CLOSED;
   }

   public DiscordBuild getDiscordBuild() {
      return this.build;
   }

   private void checkConnected(boolean connected) {
      if (connected && this.status != IPCClient.Status.CONNECTED) {
         throw new IllegalStateException(String.format("IPCClient (ID: %d) is not connected!", this.clientId));
      } else if (!connected && this.status == IPCClient.Status.CONNECTED) {
         throw new IllegalStateException(String.format("IPCClient (ID: %d) is already connected!", this.clientId));
      }
   }

   private void startReading() {
      this.readThread = new Thread(() -> {
         while(true) {
            try {
               Packet p;
               if ((p = this.read()).getOp() != Packet.OpCode.CLOSE) {
                  JSONObject json = p.getJson();
                  IPCClient.Event event = IPCClient.Event.of(json.optString("evt", (String)null));
                  String nonce = json.optString("nonce", (String)null);
                  switch($SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event()[event.ordinal()]) {
                  case 1:
                     if (nonce != null && this.callbacks.containsKey(nonce)) {
                        ((Callback)this.callbacks.remove(nonce)).succeed();
                     }
                  case 2:
                  default:
                     break;
                  case 3:
                     if (nonce != null && this.callbacks.containsKey(nonce)) {
                        ((Callback)this.callbacks.remove(nonce)).fail(json.getJSONObject("data").optString("message", (String)null));
                     }
                     break;
                  case 4:
                     LOGGER.debug("Reading thread received a 'join' event.");
                     break;
                  case 5:
                     LOGGER.debug("Reading thread received a 'spectate' event.");
                     break;
                  case 6:
                     LOGGER.debug("Reading thread received a 'join request' event.");
                     break;
                  case 7:
                     LOGGER.debug("Reading thread encountered an event with an unknown type: " + json.getString("evt"));
                  }

                  if (this.listener == null || !json.has("cmd") || !json.getString("cmd").equals("DISPATCH")) {
                     continue;
                  }

                  try {
                     JSONObject data = json.getJSONObject("data");
                     switch($SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event()[IPCClient.Event.of(json.getString("evt")).ordinal()]) {
                     case 4:
                        this.listener.onActivityJoin(this, data.getString("secret"));
                        continue;
                     case 5:
                        this.listener.onActivitySpectate(this, data.getString("secret"));
                        continue;
                     case 6:
                        JSONObject u = data.getJSONObject("user");
                        User user = new User(u.getString("username"), u.getString("discriminator"), Long.parseLong(u.getString("id")), u.optString("avatar", (String)null));
                        this.listener.onActivityJoinRequest(this, data.optString("secret", (String)null), user);
                     }
                  } catch (Exception var8) {
                     LOGGER.error("Exception when handling event: ", var8);
                  }
                  continue;
               }

               this.status = IPCClient.Status.CLOSED;
               if (this.listener != null) {
                  this.listener.onClose(this, p.getJson());
               }
            } catch (JSONException | IOException var9) {
               if (var9 instanceof IOException) {
                  LOGGER.error("Reading thread encountered an IOException", var9);
               } else {
                  LOGGER.error("Reading thread encountered an JSONException", var9);
               }

               this.status = IPCClient.Status.DISCONNECTED;
               if (this.listener != null) {
                  this.listener.onDisconnect(this, var9);
               }
            }

            return;
         }
      });
      LOGGER.debug("Starting IPCClient reading thread!");
      this.readThread.start();
   }

   private void send(Packet.OpCode op, JSONObject data, Callback callback) throws JSONException {
      try {
         String nonce = generateNonce();
         Packet p = new Packet(op, data.put("nonce", nonce));
         if (callback != null && !callback.isEmpty()) {
            this.callbacks.put(nonce, callback);
         }

         this.pipe.write(p.toBytes());
         LOGGER.debug(String.format("Sent packet: %s", p.toString()));
         if (this.listener != null) {
            this.listener.onPacketSent(this, p);
         }
      } catch (IOException var6) {
         LOGGER.error("Encountered an IOException while sending a packet and disconnected!");
         this.status = IPCClient.Status.DISCONNECTED;
      }

   }

   private Packet read() throws IOException, JSONException {
      while(this.pipe.length() == 0L && this.status == IPCClient.Status.CONNECTED) {
         try {
            Thread.sleep(50L);
         } catch (InterruptedException var5) {
            ;
         }
      }

      if (this.status == IPCClient.Status.DISCONNECTED) {
         throw new IOException("Disconnected!");
      } else if (this.status == IPCClient.Status.CLOSED) {
         return new Packet(Packet.OpCode.CLOSE, (JSONObject)null);
      } else {
         Packet.OpCode op = Packet.OpCode.values()[Integer.reverseBytes(this.pipe.readInt())];
         int len = Integer.reverseBytes(this.pipe.readInt());
         byte[] d = new byte[len];
         this.pipe.readFully(d);
         Packet p = new Packet(op, new JSONObject(new String(d)));
         LOGGER.debug(String.format("Received packet: %s", p.toString()));
         if (this.listener != null) {
            this.listener.onPacketReceived(this, p);
         }

         return p;
      }
   }

   private static int getPID() {
      String pr = ManagementFactory.getRuntimeMXBean().getName();
      return Integer.parseInt(pr.substring(0, pr.indexOf(64)));
   }

   private static String getIPC(int i) {
      if (System.getProperty("os.name").contains("Win")) {
         return "\\\\?\\pipe\\discord-ipc-" + i;
      } else {
         String tmppath = null;
         String[] var5 = paths;
         int var4 = paths.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            String str = var5[var3];
            tmppath = System.getenv(str);
            if (tmppath != null) {
               break;
            }
         }

         if (tmppath == null) {
            tmppath = "/tmp";
         }

         return tmppath + "/discord-ipc-" + i;
      }
   }

   private static String generateNonce() {
      return UUID.randomUUID().toString();
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event() {
      int[] var10000 = $SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event;
      if ($SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event != null) {
         return var10000;
      } else {
         int[] var0 = new int[IPCClient.Event.values().length];

         try {
            var0[IPCClient.Event.ACTIVITY_JOIN.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[IPCClient.Event.ACTIVITY_JOIN_REQUEST.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[IPCClient.Event.ACTIVITY_SPECTATE.ordinal()] = 5;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[IPCClient.Event.ERROR.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[IPCClient.Event.NULL.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[IPCClient.Event.READY.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[IPCClient.Event.UNKNOWN.ordinal()] = 7;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$jagrosh$discordipc$IPCClient$Event = var0;
         return var0;
      }
   }

   public static enum Event {
      NULL(false),
      READY(false),
      ERROR(false),
      ACTIVITY_JOIN(true),
      ACTIVITY_SPECTATE(true),
      ACTIVITY_JOIN_REQUEST(true),
      UNKNOWN(false);

      private final boolean subscribable;

      private Event(boolean subscribable) {
         this.subscribable = subscribable;
      }

      public boolean isSubscribable() {
         return this.subscribable;
      }

      static IPCClient.Event of(String str) {
         if (str == null) {
            return NULL;
         } else {
            IPCClient.Event[] var4;
            int var3 = (var4 = values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
               IPCClient.Event s = var4[var2];
               if (s != UNKNOWN && s.name().equalsIgnoreCase(str)) {
                  return s;
               }
            }

            return UNKNOWN;
         }
      }
   }

   public static enum Status {
      CREATED,
      CONNECTING,
      CONNECTED,
      CLOSED,
      DISCONNECTED;
   }
}
