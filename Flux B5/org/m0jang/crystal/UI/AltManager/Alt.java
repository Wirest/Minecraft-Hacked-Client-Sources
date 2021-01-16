package org.m0jang.crystal.UI.AltManager;

public class Alt {
   private String email;
   private String name;
   private String password;
   private boolean cracked;
   private boolean unchecked;
   private boolean starred;

   public Alt(String email, String password, String name) {
      this(email, password, name, false);
   }

   public Alt(String email, String password, String name, boolean starred) {
      this.email = email;
      this.starred = starred;
      if (password != null && !password.isEmpty()) {
         this.cracked = false;
         this.unchecked = name == null || name.isEmpty();
         this.name = name;
         this.password = password;
      } else {
         this.cracked = true;
         this.unchecked = false;
         this.name = email;
         this.password = null;
      }

   }

   public String getEmail() {
      return this.email;
   }

   public String getName() {
      return this.name;
   }

   public String getNameOrEmail() {
      return this.unchecked ? this.email : this.name;
   }

   public String getPassword() {
      if (this.password != null && !this.password.isEmpty()) {
         return this.password;
      } else {
         this.cracked = true;
         return "";
      }
   }

   public boolean isCracked() {
      return this.cracked;
   }

   public boolean isStarred() {
      return this.starred;
   }

   public void setStarred(boolean starred) {
      this.starred = starred;
   }

   public boolean isUnchecked() {
      return this.unchecked;
   }

   public void setChecked(String name) {
      this.name = name;
      this.unchecked = false;
   }
}
