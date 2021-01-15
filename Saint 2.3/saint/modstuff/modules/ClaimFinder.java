package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.StringUtils;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PreMotion;
import saint.eventstuff.events.RecievePacket;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class ClaimFinder extends Module {
   private final Value delay = new Value("claimfinder_delay", 1L);
   private final Value radius = new Value("claimfinder_radius", 10000);
   private List map = new CopyOnWriteArrayList();
   private final TimeHelper time = new TimeHelper();
   private int x;
   private int z;
   private int line;

   public ClaimFinder() {
      super("ClaimFinder", -5374161, ModManager.Category.PLAYER);
      this.setTag("Claim Finder");
      Saint.getCommandManager().getContentList().add(new Command("claimfinder", "<delay/radius> <value>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("delay")) {
               if (message.split(" ")[2].equalsIgnoreCase("-d")) {
                  ClaimFinder.this.delay.setValueState((Long)ClaimFinder.this.delay.getDefaultValue());
               } else {
                  ClaimFinder.this.delay.setValueState(Long.parseLong(message.split(" ")[2]));
               }

               if ((Long)ClaimFinder.this.delay.getValueState() > 10000L) {
                  ClaimFinder.this.delay.setValueState(10000L);
               } else if ((Long)ClaimFinder.this.delay.getValueState() < 1L) {
                  ClaimFinder.this.delay.setValueState(1L);
               }

               Logger.writeChat("Claim Finder Delay set to: " + ClaimFinder.this.delay.getValueState());
            } else if (message.split(" ")[1].equalsIgnoreCase("radius")) {
               if (message.split(" ")[2].equalsIgnoreCase("-d")) {
                  ClaimFinder.this.radius.setValueState((Integer)ClaimFinder.this.radius.getDefaultValue());
               } else {
                  ClaimFinder.this.radius.setValueState(Integer.parseInt(message.split(" ")[2]));
               }

               if ((Integer)ClaimFinder.this.radius.getValueState() > 50000) {
                  ClaimFinder.this.radius.setValueState(50000);
               } else if ((Integer)ClaimFinder.this.radius.getValueState() < 500) {
                  ClaimFinder.this.radius.setValueState(500);
               }

               ClaimFinder.this.x = -(Integer)ClaimFinder.this.radius.getValueState();
               ClaimFinder.this.z = -(Integer)ClaimFinder.this.radius.getValueState();
               Logger.writeChat("Claim Finder Radius set to: " + ClaimFinder.this.radius.getValueState());
            } else {
               Logger.writeChat("Option not valid! Available options: radius, delay.");
            }

         }
      });
   }

   public final List getMap() {
      return this.map;
   }

   public void onEnabled() {
      if (mc.theWorld == null) {
         this.toggle();
      } else {
         mc.thePlayer.sendChatMessage("/f map on");
         super.onEnabled();
      }
   }

   public void onDisabled() {
      if (mc.theWorld != null && mc.thePlayer.ridingEntity != null) {
         mc.thePlayer.sendChatMessage("/f map off");
      }

      this.setTag("Claim Finder");
      super.onDisabled();
   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         try {
            PreMotion pre = (PreMotion)event;
            if (mc.thePlayer.ridingEntity == null) {
               Logger.writeChat("Get in a boat/vehicle!");
               this.toggle();
               return;
            }

            pre.setCancelled(true);
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      } else if (event instanceof RecievePacket) {
         RecievePacket receive = (RecievePacket)event;
         if (receive.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = (S02PacketChat)receive.getPacket();
            String message = StringUtils.stripControlCodes(chat.func_148915_c().getFormattedText());
            Pattern pattern = Pattern.compile(".+\\((-?[0-9]+),(-?[0-9]+)\\).([^ ]+).+");
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
               (new Thread() {
                  public void run() {
                     if (ClaimFinder.this.time.hasReached((Long)ClaimFinder.this.delay.getValueState())) {
                        ClaimFinder var10000;
                        if (ClaimFinder.this.z <= (Integer)ClaimFinder.this.radius.getValueState()) {
                           var10000 = ClaimFinder.this;
                           var10000.z = var10000.z + 128;
                        } else {
                           if (ClaimFinder.this.x > (Integer)ClaimFinder.this.radius.getValueState()) {
                              Logger.writeChat("ClaimFinder finished!");
                              if (Saint.getFileManager().getFileUsingName("coords") != null) {
                                 Saint.getFileManager().getFileUsingName("coords").saveFile();
                              }

                              ClaimFinder.this.map = new ArrayList();
                              ClaimFinder.this.x = -(Integer)ClaimFinder.this.radius.getValueState();
                              ClaimFinder.this.z = -(Integer)ClaimFinder.this.radius.getValueState();
                              ClaimFinder.this.setEnabled(false);
                              return;
                           }

                           ClaimFinder.this.z = -(Integer)ClaimFinder.this.radius.getValueState();
                           var10000 = ClaimFinder.this;
                           var10000.x = var10000.x + 624;
                        }

                        ClaimFinder.this.setTag("Claim Finder (" + ClaimFinder.this.x + ", " + ClaimFinder.this.z + ")");
                        ClaimFinder.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition((double)ClaimFinder.this.x, 1.0D, (double)ClaimFinder.this.z, true));
                        ClaimFinder.this.time.reset();
                     }

                  }
               }).start();
               this.line = 1;
            }

            if (this.line > 0) {
               this.map.add(message);
               receive.setCancelled(true);
               ++this.line;
               if (this.line == 11) {
                  this.line = 0;
               }
            }
         }
      }

   }
}
