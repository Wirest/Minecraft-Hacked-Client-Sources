// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;

public class CommandResultStats
{
    private static final int NUM_RESULT_TYPES;
    private static final String[] STRING_RESULT_TYPES;
    private String[] field_179675_c;
    private String[] field_179673_d;
    
    static {
        NUM_RESULT_TYPES = Type.values().length;
        STRING_RESULT_TYPES = new String[CommandResultStats.NUM_RESULT_TYPES];
    }
    
    public CommandResultStats() {
        this.field_179675_c = CommandResultStats.STRING_RESULT_TYPES;
        this.field_179673_d = CommandResultStats.STRING_RESULT_TYPES;
    }
    
    public void func_179672_a(final ICommandSender sender, final Type resultTypeIn, final int p_179672_3_) {
        final String s = this.field_179675_c[resultTypeIn.getTypeID()];
        if (s != null) {
            final ICommandSender icommandsender = new ICommandSender() {
                @Override
                public String getName() {
                    return sender.getName();
                }
                
                @Override
                public IChatComponent getDisplayName() {
                    return sender.getDisplayName();
                }
                
                @Override
                public void addChatMessage(final IChatComponent component) {
                    sender.addChatMessage(component);
                }
                
                @Override
                public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
                    return true;
                }
                
                @Override
                public BlockPos getPosition() {
                    return sender.getPosition();
                }
                
                @Override
                public Vec3 getPositionVector() {
                    return sender.getPositionVector();
                }
                
                @Override
                public World getEntityWorld() {
                    return sender.getEntityWorld();
                }
                
                @Override
                public Entity getCommandSenderEntity() {
                    return sender.getCommandSenderEntity();
                }
                
                @Override
                public boolean sendCommandFeedback() {
                    return sender.sendCommandFeedback();
                }
                
                @Override
                public void setCommandStat(final Type type, final int amount) {
                    sender.setCommandStat(type, amount);
                }
            };
            String s2;
            try {
                s2 = CommandBase.getEntityName(icommandsender, s);
            }
            catch (EntityNotFoundException var11) {
                return;
            }
            final String s3 = this.field_179673_d[resultTypeIn.getTypeID()];
            if (s3 != null) {
                final Scoreboard scoreboard = sender.getEntityWorld().getScoreboard();
                final ScoreObjective scoreobjective = scoreboard.getObjective(s3);
                if (scoreobjective != null && scoreboard.entityHasObjective(s2, scoreobjective)) {
                    final Score score = scoreboard.getValueFromObjective(s2, scoreobjective);
                    score.setScorePoints(p_179672_3_);
                }
            }
        }
    }
    
    public void readStatsFromNBT(final NBTTagCompound tagcompound) {
        if (tagcompound.hasKey("CommandStats", 10)) {
            final NBTTagCompound nbttagcompound = tagcompound.getCompoundTag("CommandStats");
            Type[] values;
            for (int length = (values = Type.values()).length, i = 0; i < length; ++i) {
                final Type commandresultstats$type = values[i];
                final String s = String.valueOf(commandresultstats$type.getTypeName()) + "Name";
                final String s2 = String.valueOf(commandresultstats$type.getTypeName()) + "Objective";
                if (nbttagcompound.hasKey(s, 8) && nbttagcompound.hasKey(s2, 8)) {
                    final String s3 = nbttagcompound.getString(s);
                    final String s4 = nbttagcompound.getString(s2);
                    func_179667_a(this, commandresultstats$type, s3, s4);
                }
            }
        }
    }
    
    public void writeStatsToNBT(final NBTTagCompound tagcompound) {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        Type[] values;
        for (int length = (values = Type.values()).length, i = 0; i < length; ++i) {
            final Type commandresultstats$type = values[i];
            final String s = this.field_179675_c[commandresultstats$type.getTypeID()];
            final String s2 = this.field_179673_d[commandresultstats$type.getTypeID()];
            if (s != null && s2 != null) {
                nbttagcompound.setString(String.valueOf(commandresultstats$type.getTypeName()) + "Name", s);
                nbttagcompound.setString(String.valueOf(commandresultstats$type.getTypeName()) + "Objective", s2);
            }
        }
        if (!nbttagcompound.hasNoTags()) {
            tagcompound.setTag("CommandStats", nbttagcompound);
        }
    }
    
    public static void func_179667_a(final CommandResultStats stats, final Type resultType, final String p_179667_2_, final String p_179667_3_) {
        if (p_179667_2_ != null && p_179667_2_.length() != 0 && p_179667_3_ != null && p_179667_3_.length() != 0) {
            if (stats.field_179675_c == CommandResultStats.STRING_RESULT_TYPES || stats.field_179673_d == CommandResultStats.STRING_RESULT_TYPES) {
                stats.field_179675_c = new String[CommandResultStats.NUM_RESULT_TYPES];
                stats.field_179673_d = new String[CommandResultStats.NUM_RESULT_TYPES];
            }
            stats.field_179675_c[resultType.getTypeID()] = p_179667_2_;
            stats.field_179673_d[resultType.getTypeID()] = p_179667_3_;
        }
        else {
            func_179669_a(stats, resultType);
        }
    }
    
    private static void func_179669_a(final CommandResultStats resultStatsIn, final Type resultTypeIn) {
        if (resultStatsIn.field_179675_c != CommandResultStats.STRING_RESULT_TYPES && resultStatsIn.field_179673_d != CommandResultStats.STRING_RESULT_TYPES) {
            resultStatsIn.field_179675_c[resultTypeIn.getTypeID()] = null;
            resultStatsIn.field_179673_d[resultTypeIn.getTypeID()] = null;
            boolean flag = true;
            Type[] values;
            for (int length = (values = Type.values()).length, i = 0; i < length; ++i) {
                final Type commandresultstats$type = values[i];
                if (resultStatsIn.field_179675_c[commandresultstats$type.getTypeID()] != null && resultStatsIn.field_179673_d[commandresultstats$type.getTypeID()] != null) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultStatsIn.field_179675_c = CommandResultStats.STRING_RESULT_TYPES;
                resultStatsIn.field_179673_d = CommandResultStats.STRING_RESULT_TYPES;
            }
        }
    }
    
    public void func_179671_a(final CommandResultStats resultStatsIn) {
        Type[] values;
        for (int length = (values = Type.values()).length, i = 0; i < length; ++i) {
            final Type commandresultstats$type = values[i];
            func_179667_a(this, commandresultstats$type, resultStatsIn.field_179675_c[commandresultstats$type.getTypeID()], resultStatsIn.field_179673_d[commandresultstats$type.getTypeID()]);
        }
    }
    
    public enum Type
    {
        SUCCESS_COUNT("SUCCESS_COUNT", 0, 0, "SuccessCount"), 
        AFFECTED_BLOCKS("AFFECTED_BLOCKS", 1, 1, "AffectedBlocks"), 
        AFFECTED_ENTITIES("AFFECTED_ENTITIES", 2, 2, "AffectedEntities"), 
        AFFECTED_ITEMS("AFFECTED_ITEMS", 3, 3, "AffectedItems"), 
        QUERY_RESULT("QUERY_RESULT", 4, 4, "QueryResult");
        
        final int typeID;
        final String typeName;
        
        private Type(final String name2, final int ordinal, final int id, final String name) {
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
            final String[] astring = new String[values().length];
            int i = 0;
            Type[] values;
            for (int length = (values = values()).length, j = 0; j < length; ++j) {
                final Type commandresultstats$type = values[j];
                astring[i++] = commandresultstats$type.getTypeName();
            }
            return astring;
        }
        
        public static Type getTypeByName(final String name) {
            Type[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Type commandresultstats$type = values[i];
                if (commandresultstats$type.getTypeName().equals(name)) {
                    return commandresultstats$type;
                }
            }
            return null;
        }
    }
}
