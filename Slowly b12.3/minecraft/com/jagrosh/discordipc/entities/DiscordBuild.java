package com.jagrosh.discordipc.entities;

public enum DiscordBuild {
   CANARY("//canary.discordapp.com/api"),
   PTB("//ptb.discordapp.com/api"),
   STABLE("//discordapp.com/api"),
   ANY;

   private final String endpoint;

   private DiscordBuild(String endpoint) {
      this.endpoint = endpoint;
   }

   private DiscordBuild() {
      this((String)null);
   }

   public static DiscordBuild from(String endpoint) {
      DiscordBuild[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         DiscordBuild value = var4[var2];
         if (value.endpoint != null && value.endpoint.equals(endpoint)) {
            return value;
         }
      }

      return ANY;
   }
}
