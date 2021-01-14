package com.thealtening.api.retriever;

import com.thealtening.api.TheAlteningException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class AsynchronousDataRetriever extends BasicDataRetriever {
   public AsynchronousDataRetriever(String apiKey) {
      super(apiKey);
   }

   public CompletableFuture getLicenseDataAsync() {
      return this.completeTask(BasicDataRetriever::getLicense);
   }

   public CompletableFuture getAccountDataAsync() {
      return this.completeTask(BasicDataRetriever::getAccount);
   }

   public CompletableFuture isPrivateAsync(String token) {
      return this.completeTask((dr) -> {
         return dr.isPrivate(token);
      });
   }

   public CompletableFuture isFavoriteAsync(String token) {
      return this.completeTask((dr) -> {
         return dr.isFavorite(token);
      });
   }

   private CompletableFuture completeTask(Function function) {
      CompletableFuture returnValue = new CompletableFuture();

      try {
         returnValue.complete(function.apply(this));
      } catch (TheAlteningException var4) {
         returnValue.completeExceptionally(var4);
      }

      return returnValue;
   }
}
