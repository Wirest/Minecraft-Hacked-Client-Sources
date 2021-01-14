package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandResultStats {
   private static final int NUM_RESULT_TYPES = CommandResultStats.Type.values().length;
   private static final String[] STRING_RESULT_TYPES;
   private String[] field_179675_c;
   private String[] field_179673_d;

   public CommandResultStats() {
      this.field_179675_c = STRING_RESULT_TYPES;
      this.field_179673_d = STRING_RESULT_TYPES;
   }

   public void func_179672_a(final ICommandSender sender, CommandResultStats.Type resultTypeIn, int p_179672_3_) {
      String s = this.field_179675_c[resultTypeIn.getTypeID()];
      if (s != null) {
         ICommandSender icommandsender = new ICommandSender() {
            public String getName() {
               return sender.getName();
            }

            public IChatComponent getDisplayName() {
               return sender.getDisplayName();
            }

            public void addChatMessage(IChatComponent component) {
               sender.addChatMessage(component);
            }

            public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
               return true;
            }

            public BlockPos getPosition() {
               return sender.getPosition();
            }

            public Vec3 getPositionVector() {
               return sender.getPositionVector();
            }

            public World getEntityWorld() {
               return sender.getEntityWorld();
            }

            public Entity getCommandSenderEntity() {
               return sender.getCommandSenderEntity();
            }

            public boolean sendCommandFeedback() {
               return sender.sendCommandFeedback();
            }

            public void setCommandStat(CommandResultStats.Type type, int amount) {
               sender.setCommandStat(type, amount);
            }
         };

         String s1;
         try {
            s1 = CommandBase.getEntityName(icommandsender, s);
         } catch (EntityNotFoundException var11) {
            return;
         }

         String s2 = this.field_179673_d[resultTypeIn.getTypeID()];
         if (s2 != null) {
            Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
            ScoreObjective scoreobjective = scoreboard.getObjective(s2);
            if (scoreobjective != null && scoreboard.entityHasObjective(s1, scoreobjective)) {
               Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
               score.setScorePoints(p_179672_3_);
            }
         }
      }

   }

   public void readStatsFromNBT(NBTTagCompound tagcompound) {
      if (tagcompound.hasKey("CommandStats", 10)) {
         NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");
         CommandResultStats.Type[] var3 = CommandResultStats.Type.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CommandResultStats.Type commandresultstats$type = var3[var5];
            String s = commandresultstats$type.getTypeName() + "Name";
            String s1 = commandresultstats$type.getTypeName() + "Objective";
            if (nbttagcompound.hasKey(s, 8) && nbttagcompound.hasKey(s1, 8)) {
               String s2 = nbttagcompound.getString(s);
               String s3 = nbttagcompound.getString(s1);
               func_179667_a(this, commandresultstats$type, s2, s3);
            }
         }
      }

   }

   public void writeStatsToNBT(NBTTagCompound tagcompound) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      CommandResultStats.Type[] var3 = CommandResultStats.Type.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CommandResultStats.Type commandresultstats$type = var3[var5];
         String s = this.field_179675_c[commandresultstats$type.getTypeID()];
         String s1 = this.field_179673_d[commandresultstats$type.getTypeID()];
         if (s != null && s1 != null) {
            nbttagcompound.setString(commandresultstats$type.getTypeName() + "Name", s);
            nbttagcompound.setString(commandresultstats$type.getTypeName() + "Objective", s1);
         }
      }

      if (!nbttagcompound.hasNoTags()) {
         tagcompound.setTag("CommandStats", nbttagcompound);
      }

   }

   public static void func_179667_a(CommandResultStats stats, CommandResultStats.Type resultType, String p_179667_2_, String p_179667_3_) {
      if (p_179667_2_ != null && p_179667_2_.length() != 0 && p_179667_3_ != null && p_179667_3_.length() != 0) {
         if (stats.field_179675_c == STRING_RESULT_TYPES || stats.field_179673_d == STRING_RESULT_TYPES) {
            stats.field_179675_c = new String[NUM_RESULT_TYPES];
            stats.field_179673_d = new String[NUM_RESULT_TYPES];
         }

         stats.field_179675_c[resultType.getTypeID()] = p_179667_2_;
         stats.field_179673_d[resultType.getTypeID()] = p_179667_3_;
      } else {
         func_179669_a(stats, resultType);
      }

   }

   private static void func_179669_a(CommandResultStats resultStatsIn, CommandResultStats.Type resultTypeIn) {
      if (resultStatsIn.field_179675_c != STRING_RESULT_TYPES && resultStatsIn.field_179673_d != STRING_RESULT_TYPES) {
         resultStatsIn.field_179675_c[resultTypeIn.getTypeID()] = null;
         resultStatsIn.field_179673_d[resultTypeIn.getTypeID()] = null;
         boolean flag = true;
         CommandResultStats.Type[] var3 = CommandResultStats.Type.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CommandResultStats.Type commandresultstats$type = var3[var5];
            if (resultStatsIn.field_179675_c[commandresultstats$type.getTypeID()] != null && resultStatsIn.field_179673_d[commandresultstats$type.getTypeID()] != null) {
               flag = false;
               break;
            }
         }

         if (flag) {
            resultStatsIn.field_179675_c = STRING_RESULT_TYPES;
            resultStatsIn.field_179673_d = STRING_RESULT_TYPES;
         }
      }

   }

   public void func_179671_a(CommandResultStats resultStatsIn) {
      CommandResultStats.Type[] var2 = CommandResultStats.Type.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CommandResultStats.Type commandresultstats$type = var2[var4];
         func_179667_a(this, commandresultstats$type, resultStatsIn.field_179675_c[commandresultstats$type.getTypeID()], resultStatsIn.field_179673_d[commandresultstats$type.getTypeID()]);
      }

   }

   static {
      STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
   }

   public static enum Type {
      SUCCESS_COUNT(0, "SuccessCount"),
      AFFECTED_BLOCKS(1, "AffectedBlocks"),
      AFFECTED_ENTITIES(2, "AffectedEntities"),
      AFFECTED_ITEMS(3, "AffectedItems"),
      QUERY_RESULT(4, "QueryResult");

      final int typeID;
      final String typeName;

      private Type(int id, String name) {
         this.typeID = id;
         this.typeName = name;
      }

      public int getTypeID() {
         return this.typeID;
      }

      public String getTypeName() {
         return this.typeName;
      }

      public static String[] getTypeNames() {
         String[] astring = new String[values().length];
         int i = 0;
         CommandResultStats.Type[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CommandResultStats.Type commandresultstats$type = var2[var4];
            astring[i++] = commandresultstats$type.getTypeName();
         }

         return astring;
      }

      public static CommandResultStats.Type getTypeByName(String name) {
         CommandResultStats.Type[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CommandResultStats.Type commandresultstats$type = var1[var3];
            if (commandresultstats$type.getTypeName().equals(name)) {
               return commandresultstats$type;
            }
         }

         return null;
      }
   }
}
