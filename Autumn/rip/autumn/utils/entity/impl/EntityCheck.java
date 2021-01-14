package rip.autumn.utils.entity.impl;

import java.util.function.Supplier;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import rip.autumn.friend.FriendManager;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.utils.entity.ICheck;

public final class EntityCheck implements ICheck {
   private final BoolOption players;
   private final BoolOption animals;
   private final BoolOption monsters;
   private final BoolOption invisibles;
   private final Supplier friend;

   public EntityCheck(BoolOption players, BoolOption animals, BoolOption monsters, BoolOption invisibles, Supplier friend) {
      this.players = players;
      this.animals = animals;
      this.monsters = monsters;
      this.invisibles = invisibles;
      this.friend = friend;
   }

   public boolean validate(Entity entity) {
      if (entity instanceof EntityPlayerSP) {
         return false;
      } else if (!this.invisibles.getValue() && entity.isInvisible()) {
         return false;
      } else if (this.animals.getValue() && entity instanceof EntityAnimal) {
         return true;
      } else if (this.players.getValue() && entity instanceof EntityPlayer) {
         return !FriendManager.isFriend(entity.getName()) || !(Boolean)this.friend.get();
      } else {
         return this.monsters.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem);
      }
   }
}
