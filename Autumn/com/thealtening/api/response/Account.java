package com.thealtening.api.response;

public class Account {
   private String username;
   private String password;
   private String token;
   private boolean limit;
   private AccountDetails info;

   public String getToken() {
      return this.token;
   }

   public String getPassword() {
      return this.password;
   }

   public String getUsername() {
      return this.username;
   }

   public boolean isLimit() {
      return this.limit;
   }

   public AccountDetails getInfo() {
      return this.info;
   }

   public String toString() {
      return String.format("Account[%s:%s:%s:%s]", this.token, this.username, this.password, this.limit);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Account)) {
         return false;
      } else {
         Account other = (Account)obj;
         return other.getUsername().equals(this.username) && other.getToken().equals(this.token);
      }
   }
}
