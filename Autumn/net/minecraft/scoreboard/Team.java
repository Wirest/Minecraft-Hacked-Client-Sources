package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public abstract class Team {
   public boolean isSameTeam(Team other) {
      return other == null ? false : this == other;
   }

   public abstract String getRegisteredName();

   public abstract String formatString(String var1);

   public abstract boolean getSeeFriendlyInvisiblesEnabled();

   public abstract boolean getAllowFriendlyFire();

   public abstract Team.EnumVisible getNameTagVisibility();

   public abstract Collection getMembershipCollection();

   public abstract Team.EnumVisible getDeathMessageVisibility();

   public static enum EnumVisible {
      ALWAYS("always", 0),
      NEVER("never", 1),
      HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
      HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

      private static Map field_178828_g = Maps.newHashMap();
      public final String field_178830_e;
      public final int field_178827_f;

      public static String[] func_178825_a() {
         return (String[])((String[])field_178828_g.keySet().toArray(new String[field_178828_g.size()]));
      }

      public static Team.EnumVisible func_178824_a(String p_178824_0_) {
         return (Team.EnumVisible)field_178828_g.get(p_178824_0_);
      }

      private EnumVisible(String p_i45550_3_, int p_i45550_4_) {
         this.field_178830_e = p_i45550_3_;
         this.field_178827_f = p_i45550_4_;
      }

      static {
         Team.EnumVisible[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            Team.EnumVisible team$enumvisible = var0[var2];
            field_178828_g.put(team$enumvisible.field_178830_e, team$enumvisible);
         }

      }
   }
}
