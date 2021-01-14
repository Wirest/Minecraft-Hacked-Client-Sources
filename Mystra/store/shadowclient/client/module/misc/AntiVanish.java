package store.shadowclient.client.module.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventReceivePacket;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

public final class AntiVanish extends Module {
   public AntiVanish() {
		super("AntiVanish", 0, Category.RENDER);
	}

private List vanished = new ArrayList();

   @EventTarget
   public void onReceivePacket(EventReceivePacket event) {
      if (event.getPacket() instanceof S38PacketPlayerListItem) {
         S38PacketPlayerListItem listItem = (S38PacketPlayerListItem)event.getPacket();
         if (listItem.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
            Iterator var3 = listItem.func_179767_a().iterator();

            while(var3.hasNext()) {
               Object o = var3.next();
               S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
               UUID uuid = data.getProfile().getId();
               if (mc.getNetHandler().getPlayerInfo(uuid) == null && !this.checkList(uuid)) {
            	   Shadow.addChatMessage(this.getName(uuid) + " has gone invisible.");
               }
            }
         }
      }

   }
   
   @EventTarget
   public final void onUpdate(EventUpdate event) {
         Iterator var2 = this.vanished.iterator();

         while(var2.hasNext()) {
            UUID uuid = (UUID)var2.next();
            if (mc.getNetHandler().getPlayerInfo(uuid) != null) {
            	Shadow.addChatMessage(this.getName(uuid) + " is now visible.");
               this.vanished.remove(uuid);
         }
      }
   }

   private String getName(UUID uuid) {
      try {
         URL url = new URL("https://namemc.com/profile/" + uuid.toString());
         URLConnection connection = url.openConnection();
         connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         String name = null;

         String line;
         while((line = reader.readLine()) != null) {
            if (line.contains("<title>")) {
               name = line.split("§")[0].trim().replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("“ Minecraft Profile “ NameMC", "").replaceAll(", Minecraft Profile, NameMC", "");
            }
         }

         reader.close();
         return name;
      } catch (Exception var7) {
         var7.printStackTrace();
         return "(Failed) " + uuid;
      }
   }

   private boolean checkList(UUID uuid) {
      if (this.vanished.contains(uuid)) {
         this.vanished.remove(uuid);
         return true;
      } else {
         this.vanished.add(uuid);
         return false;
      }
   }
}
