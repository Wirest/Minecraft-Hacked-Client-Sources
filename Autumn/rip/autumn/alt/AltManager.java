package rip.autumn.alt;

import java.util.ArrayList;
import java.util.List;

public final class AltManager {
   public static String apiKey;
   private final List alts = new ArrayList();

   public List getAlts() {
      return this.alts;
   }
}
