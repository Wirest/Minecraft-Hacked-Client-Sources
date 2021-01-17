package me.slowly.client.util.command.cmds;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.note.Note;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.command.Command;
import net.minecraft.block.BlockNote;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class AdvNoteBotCommand extends Command {
   private boolean playing;
   private boolean isTuning;
   private int delay;
   private int count;
   private float speed;
   private boolean testing;
   private int time;
   private Note tested;
   public static HashMap cmds = new HashMap();
   public static ArrayList notes = new ArrayList();

   public AdvNoteBotCommand(String[] commands) {
      super(commands);
   }

   public void onCmd(String[] args) {
      if (!this.playing && !this.isTuning && !this.testing) {
         if (args.length == 2) {
            this.startPlaying(Boolean.valueOf(args[0]).booleanValue(), args[1]);
         }
      } else {
         this.playing = false;
         this.isTuning = false;
         this.testing = false;
      }

      super.onCmd(args);
   }

   public void startPlaying(boolean tuning, String file) {
      cmds.clear();
      notes.clear();
      this.count = 0;

      int i;
      int z;
      for(i = 2; i >= -2; --i) {
         for(z = 2; z >= -2; --z) {
            notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, -1, z), 3, 0));
         }
      }

      for(i = 2; i >= -2; --i) {
         for(z = 2; z >= -2; --z) {
            notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, 4, z), 0, 0));
         }
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, 3, 3), 4, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(-3, 3, i), 4, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, 3, -3), 4, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(3, 3, i), 1, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(3, -1, i), 2, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(-3, -1, i), 2, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, -1, 3), 2, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, -1, -3), 2, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(4, -1, i), 4, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(-4, -1, i), 4, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, -1, 4), 4, 0));
      }

      for(i = -2; i < 3; ++i) {
         notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(i, -1, -4), 4, 0));
      }

      notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(-2, 2, -2), 2, 0));
      notes.add(new Note(Minecraft.getMinecraft().thePlayer.getPosition().add(2, 2, -2), 2, 0));

      try {
         @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(new File(Minecraft.getMinecraft().mcDataDir + "/Slowly/sounds/" + file + ".sound")));
         this.count = 0;

         String line;
         while((line = br.readLine()) != null) {
            if (!line.equalsIgnoreCase("")) {
               cmds.put(this.count, line);
               ++this.count;
            }
         }

         this.count = 0;
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      EventManager.unregister(this);
      EventManager.register(this);
      if (tuning) {
         this.playing = false;
         this.isTuning = true;
         this.testing = true;
      } else {
         for(i = 0; i < notes.size(); ++i) {
            ((Note)notes.get(i)).setNote(i % 25);
         }

         this.playing = true;
         this.isTuning = false;
         this.testing = false;
      }

      this.count = 0;
      this.delay = 0;
      this.time = 1;
   }

   public void tune(BlockPos note) {
      if (Minecraft.getMinecraft().playerController.onPlayerRightClick(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem(), note, EnumFacing.DOWN, new Vec3((double)note.getX(), (double)note.getY(), (double)note.getZ()))) {
         Minecraft.getMinecraft().thePlayer.swingItem();
      }

   }

   public void play(BlockPos pos) {
      if (pos != null) {
         Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
         Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.DOWN));
         Minecraft.getMinecraft().thePlayer.swingItem();
      }

   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
      if (this.delay > 0) {
         --this.delay;
      }

      if (this.delay <= 0) {
         if (this.playing) {
            if (this.isTuning) {
               return;
            }

            for(; ((String)cmds.get(this.count)).startsWith("play"); ++this.count) {
               String[] splitted = ((String)cmds.get(this.count)).replaceAll("play ", "").split(":");
               if (splitted.length == 2) {
                  float[] rot = CombatUtil.getRotationsNeededBlock((double)((Note)notes.get(this.count)).getPos().getX(), (double)((Note)notes.get(this.count)).getPos().getY(), (double)((Note)notes.get(this.count)).getPos().getZ());
                  event.yaw = rot[0];
                  event.pitch = rot[1];
                  Minecraft.getMinecraft().thePlayer.rotationYawHead = event.yaw;
                  this.play(this.getNote(Integer.valueOf(splitted[0]).intValue(), Integer.valueOf(splitted[1]).intValue()));
               }
            }

            if (((String)cmds.get(this.count)).startsWith("rest")) {
               this.delay = Integer.valueOf(((String)cmds.get(this.count)).replaceAll("rest ", "")).intValue() * 2;
               ++this.count;
            }

            if (this.count > cmds.size()) {
               EventManager.unregister(this);
               this.playing = false;
            }
         }

         if (this.testing) {
            if (this.count > 0) {
               this.tested.setNote(BlockNote.note);
            }

            float[] rot = CombatUtil.getRotationsNeededBlock((double)((Note)notes.get(this.count)).getPos().getX(), (double)((Note)notes.get(this.count)).getPos().getY(), (double)((Note)notes.get(this.count)).getPos().getZ());
            event.yaw = rot[0];
            event.pitch = rot[1];
            Minecraft.getMinecraft().thePlayer.rotationYawHead = event.yaw;
            this.play(((Note)notes.get(this.count)).getPos());
            this.tested = (Note)notes.get(this.count);
            ++this.count;
            if (this.count >= notes.size()) {
               this.count = 0;
               this.time = 1;
               this.testing = false;

               for(int i = 0; i < notes.size(); ++i) {
                  if (((Note)notes.get(i)).getNote() == i % 25) {
                     ++this.count;
                  }
               }

               if (this.count >= notes.size() - 1) {
                  this.isTuning = false;
                  this.playing = true;
                  this.testing = false;
               }

               this.count = 0;
            }

            this.delay = 8;
         }

         if (this.isTuning) {
            if (this.testing) {
               return;
            }

            this.count = 0;
            if (this.time > notes.size()) {
               this.testing = true;
               this.count = 0;
            }

            for(int i = this.time - 1; i < this.time; ++i) {
               if (((Note)notes.get(i)).getNote() == i % 25) {
                  ++this.count;
               } else {
                  this.tune(((Note)notes.get(i)).getPos());
                  ((Note)notes.get(i)).setNote((((Note)notes.get(i)).getNote() + 1) % 25);
                  this.delay = 4;
               }
            }

            if (this.count >= 1) {
               ++this.time;
            }
         }

      }
   }

   private BlockPos getNote(int instrument, int note) {
      Iterator var4 = notes.iterator();

      Note all;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         all = (Note)var4.next();
      } while(all.getInstrument() != instrument || all.getNote() != note);

      return all.getPos();
   }
}
