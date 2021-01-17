package com.jagrosh.discordipc.entities;

import java.time.OffsetDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RichPresence {
   private final String state;
   private final String details;
   private final OffsetDateTime startTimestamp;
   private final OffsetDateTime endTimestamp;
   private final String largeImageKey;
   private final String largeImageText;
   private final String smallImageKey;
   private final String smallImageText;
   private final String partyId;
   private final int partySize;
   private final int partyMax;
   private final String matchSecret;
   private final String joinSecret;
   private final String spectateSecret;
   private final boolean instance;

   public RichPresence(String state, String details, OffsetDateTime startTimestamp, OffsetDateTime endTimestamp, String largeImageKey, String largeImageText, String smallImageKey, String smallImageText, String partyId, int partySize, int partyMax, String matchSecret, String joinSecret, String spectateSecret, boolean instance) {
      this.state = state;
      this.details = details;
      this.startTimestamp = startTimestamp;
      this.endTimestamp = endTimestamp;
      this.largeImageKey = largeImageKey;
      this.largeImageText = largeImageText;
      this.smallImageKey = smallImageKey;
      this.smallImageText = smallImageText;
      this.partyId = partyId;
      this.partySize = partySize;
      this.partyMax = partyMax;
      this.matchSecret = matchSecret;
      this.joinSecret = joinSecret;
      this.spectateSecret = spectateSecret;
      this.instance = instance;
   }

   public JSONObject toJson() throws JSONException {
      return (new JSONObject()).put("state", this.state).put("details", this.details).put("timestamps", (new JSONObject()).put("start", this.startTimestamp == null ? null : this.startTimestamp.toEpochSecond()).put("end", this.endTimestamp == null ? null : this.endTimestamp.toEpochSecond())).put("assets", (new JSONObject()).put("large_image", this.largeImageKey).put("large_text", this.largeImageText).put("small_image", this.smallImageKey).put("small_text", this.smallImageText)).put("party", this.partyId == null ? null : (new JSONObject()).put("id", this.partyId).put("size", (new JSONArray()).put(this.partySize).put(this.partyMax))).put("secrets", (new JSONObject()).put("join", this.joinSecret).put("spectate", this.spectateSecret).put("match", this.matchSecret)).put("instance", this.instance);
   }

   public static class Builder {
      private String state;
      private String details;
      private OffsetDateTime startTimestamp;
      private OffsetDateTime endTimestamp;
      private String largeImageKey;
      private String largeImageText;
      private String smallImageKey;
      private String smallImageText;
      private String partyId;
      private int partySize;
      private int partyMax;
      private String matchSecret;
      private String joinSecret;
      private String spectateSecret;
      private boolean instance;

      public RichPresence build() {
         return new RichPresence(this.state, this.details, this.startTimestamp, this.endTimestamp, this.largeImageKey, this.largeImageText, this.smallImageKey, this.smallImageText, this.partyId, this.partySize, this.partyMax, this.matchSecret, this.joinSecret, this.spectateSecret, this.instance);
      }

      public RichPresence.Builder setState(String state) {
         this.state = state;
         return this;
      }

      public RichPresence.Builder setDetails(String details) {
         this.details = details;
         return this;
      }

      public RichPresence.Builder setStartTimestamp(OffsetDateTime startTimestamp) {
         this.startTimestamp = startTimestamp;
         return this;
      }

      public RichPresence.Builder setEndTimestamp(OffsetDateTime endTimestamp) {
         this.endTimestamp = endTimestamp;
         return this;
      }

      public RichPresence.Builder setLargeImage(String largeImageKey, String largeImageText) {
         this.largeImageKey = largeImageKey;
         this.largeImageText = largeImageText;
         return this;
      }

      public RichPresence.Builder setLargeImage(String largeImageKey) {
         return this.setLargeImage(largeImageKey, (String)null);
      }

      public RichPresence.Builder setSmallImage(String smallImageKey, String smallImageText) {
         this.smallImageKey = smallImageKey;
         this.smallImageText = smallImageText;
         return this;
      }

      public RichPresence.Builder setSmallImage(String smallImageKey) {
         return this.setSmallImage(smallImageKey, (String)null);
      }

      public RichPresence.Builder setParty(String partyId, int partySize, int partyMax) {
         this.partyId = partyId;
         this.partySize = partySize;
         this.partyMax = partyMax;
         return this;
      }

      public RichPresence.Builder setMatchSecret(String matchSecret) {
         this.matchSecret = matchSecret;
         return this;
      }

      public RichPresence.Builder setJoinSecret(String joinSecret) {
         this.joinSecret = joinSecret;
         return this;
      }

      public RichPresence.Builder setSpectateSecret(String spectateSecret) {
         this.spectateSecret = spectateSecret;
         return this;
      }

      public RichPresence.Builder setInstance(boolean instance) {
         this.instance = instance;
         return this;
      }
   }
}
