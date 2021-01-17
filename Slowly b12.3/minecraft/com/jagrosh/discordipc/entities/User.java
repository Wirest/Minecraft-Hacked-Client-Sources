package com.jagrosh.discordipc.entities;

public class User {
   private final String name;
   private final String discriminator;
   private final long id;
   private final String avatar;

   public User(String name, String discriminator, long id, String avatar) {
      this.name = name;
      this.discriminator = discriminator;
      this.id = id;
      this.avatar = avatar;
   }

   public String getName() {
      return this.name;
   }

   public String getDiscriminator() {
      return this.discriminator;
   }

   public long getIdLong() {
      return this.id;
   }

   public String getId() {
      return Long.toString(this.id);
   }

   public String getAvatarId() {
      return this.avatar;
   }

   public String getAvatarUrl() {
      return this.getAvatarId() == null ? null : "https://cdn.discordapp.com/avatars/" + this.getId() + "/" + this.getAvatarId() + (this.getAvatarId().startsWith("a_") ? ".gif" : ".png");
   }

   public String getDefaultAvatarId() {
      return User.DefaultAvatar.values()[Integer.parseInt(this.getDiscriminator()) % User.DefaultAvatar.values().length].toString();
   }

   public String getDefaultAvatarUrl() {
      return "https://discordapp.com/assets/" + this.getDefaultAvatarId() + ".png";
   }

   public String getEffectiveAvatarUrl() {
      return this.getAvatarUrl() == null ? this.getDefaultAvatarUrl() : this.getAvatarUrl();
   }

   public boolean isBot() {
      return false;
   }

   public String getAsMention() {
      return "<@" + this.id + '>';
   }

   public boolean equals(Object o) {
      if (!(o instanceof User)) {
         return false;
      } else {
         User oUser = (User)o;
         return this == oUser || this.id == oUser.id;
      }
   }

   public int hashCode() {
      return Long.hashCode(this.id);
   }

   public String toString() {
      return "U:" + this.getName() + '(' + this.id + ')';
   }

   public static enum DefaultAvatar {
      BLURPLE("6debd47ed13483642cf09e832ed0bc1b"),
      GREY("322c936a8c8be1b803cd94861bdfa868"),
      GREEN("dd4dbc0016779df1378e7812eabaa04d"),
      ORANGE("0e291f67c9274a1abdddeb3fd919cbaa"),
      RED("1cbd08c76f8af6dddce02c5138971129");

      private final String text;

      private DefaultAvatar(String text) {
         this.text = text;
      }

      public String toString() {
         return this.text;
      }
   }
}
