package net.minecraft.util;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Iterator;
import java.util.Set;

public class JsonSerializableSet extends ForwardingSet implements IJsonSerializable {
   private final Set underlyingSet = Sets.newHashSet();

   public void fromJson(JsonElement json) {
      if (json.isJsonArray()) {
         Iterator var2 = json.getAsJsonArray().iterator();

         while(var2.hasNext()) {
            JsonElement jsonelement = (JsonElement)var2.next();
            this.add(jsonelement.getAsString());
         }
      }

   }

   public JsonElement getSerializableElement() {
      JsonArray jsonarray = new JsonArray();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         String s = (String)var2.next();
         jsonarray.add(new JsonPrimitive(s));
      }

      return jsonarray;
   }

   protected Set delegate() {
      return this.underlyingSet;
   }
}
