package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.RegistrySimple;

public class SoundRegistry extends RegistrySimple {
   private Map soundRegistry;

   protected Map createUnderlyingMap() {
      this.soundRegistry = Maps.newHashMap();
      return this.soundRegistry;
   }

   public void registerSound(SoundEventAccessorComposite p_148762_1_) {
      this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
   }

   public void clearMap() {
      this.soundRegistry.clear();
   }
}
