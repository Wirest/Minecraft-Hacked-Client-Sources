package rip.autumn.utils.entity.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.utils.PlayerUtils;
import rip.autumn.utils.entity.ICheck;

public final class TeamsCheck implements ICheck {
   private final BoolOption teams;

   public TeamsCheck(BoolOption teams) {
      this.teams = teams;
   }

   public boolean validate(Entity entity) {
      return !(entity instanceof EntityPlayer) || !PlayerUtils.isOnSameTeam((EntityPlayer)entity) || !this.teams.getValue();
   }
}
