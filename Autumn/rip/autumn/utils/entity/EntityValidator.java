package rip.autumn.utils.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.entity.Entity;

public final class EntityValidator {
   private final Set checks = new HashSet();

   public final boolean validate(Entity entity) {
      Iterator var2 = this.checks.iterator();

      ICheck check;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         check = (ICheck)var2.next();
      } while(check.validate(entity));

      return false;
   }

   public void add(ICheck check) {
      this.checks.add(check);
   }
}
