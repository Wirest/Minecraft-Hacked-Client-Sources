// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import com.google.gson.JsonParseException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;

public class SoundListSerializer implements JsonDeserializer<SoundList>
{
    @Override
    public SoundList deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
        final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "entry");
        final SoundList soundlist = new SoundList();
        soundlist.setReplaceExisting(JsonUtils.getBoolean(jsonobject, "replace", false));
        final SoundCategory soundcategory = SoundCategory.getCategory(JsonUtils.getString(jsonobject, "category", SoundCategory.MASTER.getCategoryName()));
        soundlist.setSoundCategory(soundcategory);
        Validate.notNull(soundcategory, "Invalid category", new Object[0]);
        if (jsonobject.has("sounds")) {
            final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sounds");
            for (int i = 0; i < jsonarray.size(); ++i) {
                final JsonElement jsonelement = jsonarray.get(i);
                final SoundList.SoundEntry soundlist$soundentry = new SoundList.SoundEntry();
                if (JsonUtils.isString(jsonelement)) {
                    soundlist$soundentry.setSoundEntryName(JsonUtils.getString(jsonelement, "sound"));
                }
                else {
                    final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonelement, "sound");
                    soundlist$soundentry.setSoundEntryName(JsonUtils.getString(jsonobject2, "name"));
                    if (jsonobject2.has("type")) {
                        final SoundList.SoundEntry.Type soundlist$soundentry$type = SoundList.SoundEntry.Type.getType(JsonUtils.getString(jsonobject2, "type"));
                        Validate.notNull(soundlist$soundentry$type, "Invalid type", new Object[0]);
                        soundlist$soundentry.setSoundEntryType(soundlist$soundentry$type);
                    }
                    if (jsonobject2.has("volume")) {
                        final float f = JsonUtils.getFloat(jsonobject2, "volume");
                        Validate.isTrue(f > 0.0f, "Invalid volume", new Object[0]);
                        soundlist$soundentry.setSoundEntryVolume(f);
                    }
                    if (jsonobject2.has("pitch")) {
                        final float f2 = JsonUtils.getFloat(jsonobject2, "pitch");
                        Validate.isTrue(f2 > 0.0f, "Invalid pitch", new Object[0]);
                        soundlist$soundentry.setSoundEntryPitch(f2);
                    }
                    if (jsonobject2.has("weight")) {
                        final int j = JsonUtils.getInt(jsonobject2, "weight");
                        Validate.isTrue(j > 0, "Invalid weight", new Object[0]);
                        soundlist$soundentry.setSoundEntryWeight(j);
                    }
                    if (jsonobject2.has("stream")) {
                        soundlist$soundentry.setStreaming(JsonUtils.getBoolean(jsonobject2, "stream"));
                    }
                }
                soundlist.getSoundList().add(soundlist$soundentry);
            }
        }
        return soundlist;
    }
}
