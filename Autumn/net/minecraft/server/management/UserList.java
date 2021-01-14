package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList {
   protected static final Logger logger = LogManager.getLogger();
   protected final Gson gson;
   private final File saveFile;
   private final Map values = Maps.newHashMap();
   private boolean lanServer = true;
   private static final ParameterizedType saveFileFormat = new ParameterizedType() {
      public Type[] getActualTypeArguments() {
         return new Type[]{UserListEntry.class};
      }

      public Type getRawType() {
         return List.class;
      }

      public Type getOwnerType() {
         return null;
      }
   };

   public UserList(File saveFile) {
      this.saveFile = saveFile;
      GsonBuilder gsonbuilder = (new GsonBuilder()).setPrettyPrinting();
      gsonbuilder.registerTypeHierarchyAdapter(UserListEntry.class, new UserList.Serializer());
      this.gson = gsonbuilder.create();
   }

   public boolean isLanServer() {
      return this.lanServer;
   }

   public void setLanServer(boolean state) {
      this.lanServer = state;
   }

   public void addEntry(UserListEntry entry) {
      this.values.put(this.getObjectKey(entry.getValue()), entry);

      try {
         this.writeChanges();
      } catch (IOException var3) {
         logger.warn("Could not save the list after adding a user.", var3);
      }

   }

   public UserListEntry getEntry(Object obj) {
      this.removeExpired();
      return (UserListEntry)this.values.get(this.getObjectKey(obj));
   }

   public void removeEntry(Object p_152684_1_) {
      this.values.remove(this.getObjectKey(p_152684_1_));

      try {
         this.writeChanges();
      } catch (IOException var3) {
         logger.warn("Could not save the list after removing a user.", var3);
      }

   }

   public String[] getKeys() {
      return (String[])((String[])this.values.keySet().toArray(new String[this.values.size()]));
   }

   protected String getObjectKey(Object obj) {
      return obj.toString();
   }

   protected boolean hasEntry(Object entry) {
      return this.values.containsKey(this.getObjectKey(entry));
   }

   private void removeExpired() {
      List list = Lists.newArrayList();
      Iterator var2 = this.values.values().iterator();

      while(var2.hasNext()) {
         UserListEntry v = (UserListEntry)var2.next();
         if (v.hasBanExpired()) {
            list.add(v.getValue());
         }
      }

      var2 = list.iterator();

      while(var2.hasNext()) {
         Object k = var2.next();
         this.values.remove(k);
      }

   }

   protected UserListEntry createEntry(JsonObject entryData) {
      return new UserListEntry((Object)null, entryData);
   }

   protected Map getValues() {
      return this.values;
   }

   public void writeChanges() throws IOException {
      Collection collection = this.values.values();
      String s = this.gson.toJson(collection);
      BufferedWriter bufferedwriter = null;

      try {
         bufferedwriter = Files.newWriter(this.saveFile, Charsets.UTF_8);
         bufferedwriter.write(s);
      } finally {
         IOUtils.closeQuietly(bufferedwriter);
      }

   }

   class Serializer implements JsonDeserializer, JsonSerializer {
      private Serializer() {
      }

      public JsonElement serialize(UserListEntry p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_) {
         JsonObject jsonobject = new JsonObject();
         p_serialize_1_.onSerialization(jsonobject);
         return jsonobject;
      }

      public UserListEntry deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
         if (p_deserialize_1_.isJsonObject()) {
            JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            UserListEntry userlistentry = UserList.this.createEntry(jsonobject);
            return userlistentry;
         } else {
            return null;
         }
      }
   }
}
