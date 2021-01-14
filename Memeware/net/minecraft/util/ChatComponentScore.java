package net.minecraft.util;

import java.util.Iterator;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;

public class ChatComponentScore extends ChatComponentStyle {
    private final String field_179999_b;
    private final String field_180000_c;
    private String field_179998_d = "";
    private static final String __OBFID = "CL_00002309";

    public ChatComponentScore(String p_i45997_1_, String p_i45997_2_) {
        this.field_179999_b = p_i45997_1_;
        this.field_180000_c = p_i45997_2_;
    }

    public String func_179995_g() {
        return this.field_179999_b;
    }

    public String func_179994_h() {
        return this.field_180000_c;
    }

    public void func_179997_b(String p_179997_1_) {
        this.field_179998_d = p_179997_1_;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.  TODO: why is this two
     * different methods?
     */
    public String getUnformattedTextForChat() {
        MinecraftServer var1 = MinecraftServer.getServer();

        if (var1 != null && var1.func_175578_N() && StringUtils.isNullOrEmpty(this.field_179998_d)) {
            Scoreboard var2 = var1.worldServerForDimension(0).getScoreboard();
            ScoreObjective var3 = var2.getObjective(this.field_180000_c);

            if (var2.func_178819_b(this.field_179999_b, var3)) {
                Score var4 = var2.getValueFromObjective(this.field_179999_b, var3);
                this.func_179997_b(String.format("%d", new Object[]{Integer.valueOf(var4.getScorePoints())}));
            } else {
                this.field_179998_d = "";
            }
        }

        return this.field_179998_d;
    }

    public ChatComponentScore func_179996_i() {
        ChatComponentScore var1 = new ChatComponentScore(this.field_179999_b, this.field_180000_c);
        var1.func_179997_b(this.field_179998_d);
        var1.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator var2 = this.getSiblings().iterator();

        while (var2.hasNext()) {
            IChatComponent var3 = (IChatComponent) var2.next();
            var1.appendSibling(var3.createCopy());
        }

        return var1;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof ChatComponentScore)) {
            return false;
        } else {
            ChatComponentScore var2 = (ChatComponentScore) p_equals_1_;
            return this.field_179999_b.equals(var2.field_179999_b) && this.field_180000_c.equals(var2.field_180000_c) && super.equals(p_equals_1_);
        }
    }

    public String toString() {
        return "ScoreComponent{name=\'" + this.field_179999_b + '\'' + "objective=\'" + this.field_180000_c + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public IChatComponent createCopy() {
        return this.func_179996_i();
    }
}
