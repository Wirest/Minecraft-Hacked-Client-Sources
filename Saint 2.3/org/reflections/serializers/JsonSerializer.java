package org.reflections.serializers;

import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import org.reflections.Reflections;
import org.reflections.util.Utils;

public class JsonSerializer implements Serializer {
   private Gson gson;

   public Reflections read(InputStream inputStream) {
      return (Reflections)this.getGson().fromJson(new InputStreamReader(inputStream), Reflections.class);
   }

   public File save(Reflections reflections, String filename) {
      try {
         File file = Utils.prepareFile(filename);
         Files.write(this.toString(reflections), file, Charset.defaultCharset());
         return file;
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public String toString(Reflections reflections) {
      return this.getGson().toJson(reflections);
   }

   private Gson getGson() {
      if (this.gson == null) {
         this.gson = (new GsonBuilder()).registerTypeAdapter(Multimap.class, new com.google.gson.JsonSerializer() {
            public JsonElement serialize(Multimap multimap, Type type, JsonSerializationContext jsonSerializationContext) {
               return jsonSerializationContext.serialize(multimap.asMap());
            }
         }).registerTypeAdapter(Multimap.class, new JsonDeserializer() {
            public Multimap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
               SetMultimap map = Multimaps.newSetMultimap(new HashMap(), new Supplier() {
                  public Set get() {
                     return Sets.newHashSet();
                  }
               });
               Iterator i$x = ((JsonObject)jsonElement).entrySet().iterator();

               while(i$x.hasNext()) {
                  Entry entry = (Entry)i$x.next();
                  Iterator i$ = ((JsonArray)entry.getValue()).iterator();

                  while(i$.hasNext()) {
                     JsonElement element = (JsonElement)i$.next();
                     map.get(entry.getKey()).add(element.getAsString());
                  }
               }

               return map;
            }
         }).setPrettyPrinting().create();
      }

      return this.gson;
   }
}
