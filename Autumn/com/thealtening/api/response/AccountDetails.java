package com.thealtening.api.response;

import com.google.gson.annotations.SerializedName;

public class AccountDetails {
   @SerializedName("hypixel.lvl")
   private int hypixelLevel;
   @SerializedName("hypixel.rank")
   private String hypixelRank;
   @SerializedName("mineplex.lvl")
   private int mineplexLevel;
   @SerializedName("mineplex.rank")
   private String mineplexRank;
   @SerializedName("labymod.cape")
   private boolean labymodCape;
   @SerializedName("5zig.cape")
   private boolean fiveZigCape;

   public int getHypixelLevel() {
      return this.hypixelLevel;
   }

   public String getHypixelRank() {
      return this.hypixelRank;
   }

   public int getMineplexLevel() {
      return this.mineplexLevel;
   }

   public String getMineplexRank() {
      return this.mineplexRank;
   }

   public boolean hasLabyModCape() {
      return this.labymodCape;
   }

   public boolean hasFiveZigCape() {
      return this.fiveZigCape;
   }

   public String toString() {
      return String.format("AccountDetails[%s:%s:%s:%s:%s:%s]", this.hypixelLevel, this.hypixelRank, this.mineplexLevel, this.mineplexRank, this.labymodCape, this.fiveZigCape);
   }
}
