package com.thealtening.api.response;

import com.google.gson.annotations.SerializedName;

public class License {
   private String username;
   private boolean premium;
   @SerializedName("premium_name")
   private String premiumName;
   @SerializedName("expires")
   private String expiryDate;

   public String getUsername() {
      return this.username;
   }

   public boolean isPremium() {
      return this.premium;
   }

   public String getPremiumName() {
      return this.premiumName;
   }

   public String getExpiryDate() {
      return this.expiryDate;
   }

   public String toString() {
      return String.format("License[%s:%s:%s:%s]", this.username, this.premium, this.premiumName, this.expiryDate);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof License)) {
         return false;
      } else {
         License other = (License)obj;
         return other.getExpiryDate().equals(this.getExpiryDate()) && other.getPremiumName().equals(this.getPremiumName()) && other.isPremium() == this.isPremium() && other.getUsername().equals(this.getUsername());
      }
   }
}
