package com.thealtening.api.retriever;

import com.google.gson.JsonObject;
import com.thealtening.api.TheAlteningException;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;

public class BasicDataRetriever implements DataRetriever {
   private String apiKey;

   public BasicDataRetriever(String apiKey) {
      this.apiKey = apiKey;
   }

   public void updateKey(String newApiKey) {
      this.apiKey = newApiKey;
   }

   public License getLicense() throws TheAlteningException {
      JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v1/license?token=" + this.apiKey);
      return (License)gson.fromJson(jsonObject, License.class);
   }

   public Account getAccount() throws TheAlteningException {
      JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v1/generate?info=true&token=" + this.apiKey);
      return (Account)gson.fromJson(jsonObject, Account.class);
   }

   public boolean isPrivate(String token) throws TheAlteningException {
      JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v1/private?acctoken=" + token + "&token=" + this.apiKey);
      return this.isSuccess(jsonObject);
   }

   public boolean isFavorite(String token) throws TheAlteningException {
      JsonObject jsonObject = this.retrieveData("http://api.thealtening.com/v1/favorite?acctoken=" + token + "&token=" + this.apiKey);
      return this.isSuccess(jsonObject);
   }

   public AsynchronousDataRetriever toAsync() {
      return new AsynchronousDataRetriever(this.apiKey);
   }
}
