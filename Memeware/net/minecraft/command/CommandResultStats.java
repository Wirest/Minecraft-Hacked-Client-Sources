package net.minecraft.command;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class CommandResultStats {
    private static final int field_179676_a = CommandResultStats.Type.values().length;
    private static final String[] field_179674_b = new String[field_179676_a];
    private String[] field_179675_c;
    private String[] field_179673_d;
    private static final String __OBFID = "CL_00002364";

    public CommandResultStats() {
        this.field_179675_c = field_179674_b;
        this.field_179673_d = field_179674_b;
    }

    public void func_179672_a(ICommandSender p_179672_1_, CommandResultStats.Type p_179672_2_, int p_179672_3_) {
        String var4 = this.field_179675_c[p_179672_2_.func_179636_a()];

        if (var4 != null) {
            String var5;

            try {
                var5 = CommandBase.func_175758_e(p_179672_1_, var4);
            } catch (EntityNotFoundException var10) {
                return;
            }

            String var6 = this.field_179673_d[p_179672_2_.func_179636_a()];

            if (var6 != null) {
                Scoreboard var7 = p_179672_1_.getEntityWorld().getScoreboard();
                ScoreObjective var8 = var7.getObjective(var6);

                if (var8 != null) {
                    if (var7.func_178819_b(var5, var8)) {
                        Score var9 = var7.getValueFromObjective(var5, var8);
                        var9.setScorePoints(p_179672_3_);
                    }
                }
            }
        }
    }

    public void func_179668_a(NBTTagCompound p_179668_1_) {
        if (p_179668_1_.hasKey("CommandStats", 10)) {
            NBTTagCompound var2 = p_179668_1_.getCompoundTag("CommandStats");
            CommandResultStats.Type[] var3 = CommandResultStats.Type.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                CommandResultStats.Type var6 = var3[var5];
                String var7 = var6.func_179637_b() + "Name";
                String var8 = var6.func_179637_b() + "Objective";

                if (var2.hasKey(var7, 8) && var2.hasKey(var8, 8)) {
                    String var9 = var2.getString(var7);
                    String var10 = var2.getString(var8);
                    func_179667_a(this, var6, var9, var10);
                }
            }
        }
    }

    public void func_179670_b(NBTTagCompound p_179670_1_) {
        NBTTagCompound var2 = new NBTTagCompound();
        CommandResultStats.Type[] var3 = CommandResultStats.Type.values();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            CommandResultStats.Type var6 = var3[var5];
            String var7 = this.field_179675_c[var6.func_179636_a()];
            String var8 = this.field_179673_d[var6.func_179636_a()];

            if (var7 != null && var8 != null) {
                var2.setString(var6.func_179637_b() + "Name", var7);
                var2.setString(var6.func_179637_b() + "Objective", var8);
            }
        }

        if (!var2.hasNoTags()) {
            p_179670_1_.setTag("CommandStats", var2);
        }
    }

    public static void func_179667_a(CommandResultStats p_179667_0_, CommandResultStats.Type p_179667_1_, String p_179667_2_, String p_179667_3_) {
        if (p_179667_2_ != null && p_179667_2_.length() != 0 && p_179667_3_ != null && p_179667_3_.length() != 0) {
            if (p_179667_0_.field_179675_c == field_179674_b || p_179667_0_.field_179673_d == field_179674_b) {
                p_179667_0_.field_179675_c = new String[field_179676_a];
                p_179667_0_.field_179673_d = new String[field_179676_a];
            }

            p_179667_0_.field_179675_c[p_179667_1_.func_179636_a()] = p_179667_2_;
            p_179667_0_.field_179673_d[p_179667_1_.func_179636_a()] = p_179667_3_;
        } else {
            func_179669_a(p_179667_0_, p_179667_1_);
        }
    }

    private static void func_179669_a(CommandResultStats p_179669_0_, CommandResultStats.Type p_179669_1_) {
        if (p_179669_0_.field_179675_c != field_179674_b && p_179669_0_.field_179673_d != field_179674_b) {
            p_179669_0_.field_179675_c[p_179669_1_.func_179636_a()] = null;
            p_179669_0_.field_179673_d[p_179669_1_.func_179636_a()] = null;
            boolean var2 = true;
            CommandResultStats.Type[] var3 = CommandResultStats.Type.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                CommandResultStats.Type var6 = var3[var5];

                if (p_179669_0_.field_179675_c[var6.func_179636_a()] != null && p_179669_0_.field_179673_d[var6.func_179636_a()] != null) {
                    var2 = false;
                    break;
                }
            }

            if (var2) {
                p_179669_0_.field_179675_c = field_179674_b;
                p_179669_0_.field_179673_d = field_179674_b;
            }
        }
    }

    public void func_179671_a(CommandResultStats p_179671_1_) {
        CommandResultStats.Type[] var2 = CommandResultStats.Type.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            CommandResultStats.Type var5 = var2[var4];
            func_179667_a(this, var5, p_179671_1_.field_179675_c[var5.func_179636_a()], p_179671_1_.field_179673_d[var5.func_179636_a()]);
        }
    }

    public static enum Type {
        SUCCESS_COUNT("SUCCESS_COUNT", 0, 0, "SuccessCount"),
        AFFECTED_BLOCKS("AFFECTED_BLOCKS", 1, 1, "AffectedBlocks"),
        AFFECTED_ENTITIES("AFFECTED_ENTITIES", 2, 2, "AffectedEntities"),
        AFFECTED_ITEMS("AFFECTED_ITEMS", 3, 3, "AffectedItems"),
        QUERY_RESULT("QUERY_RESULT", 4, 4, "QueryResult");
        final int field_179639_f;
        final String field_179640_g;

        private static final CommandResultStats.Type[] $VALUES = new CommandResultStats.Type[]{SUCCESS_COUNT, AFFECTED_BLOCKS, AFFECTED_ENTITIES, AFFECTED_ITEMS, QUERY_RESULT};
        private static final String __OBFID = "CL_00002363";

        private Type(String p_i46050_1_, int p_i46050_2_, int p_i46050_3_, String p_i46050_4_) {
            this.field_179639_f = p_i46050_3_;
            this.field_179640_g = p_i46050_4_;
        }

        public int func_179636_a() {
            return this.field_179639_f;
        }

        public String func_179637_b() {
            return this.field_179640_g;
        }

        public static String[] func_179634_c() {
            String[] var0 = new String[values().length];
            int var1 = 0;
            CommandResultStats.Type[] var2 = values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                CommandResultStats.Type var5 = var2[var4];
                var0[var1++] = var5.func_179637_b();
            }

            return var0;
        }

        public static CommandResultStats.Type func_179635_a(String p_179635_0_) {
            CommandResultStats.Type[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                CommandResultStats.Type var4 = var1[var3];

                if (var4.func_179637_b().equals(p_179635_0_)) {
                    return var4;
                }
            }

            return null;
        }
    }
}
