package me.existdev.exist.module.modules.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.combat.AntiBot;
import me.existdev.exist.module.modules.combat.Aura;
import me.existdev.exist.module.modules.combat.AutoArmor;
import me.existdev.exist.module.modules.combat.ChestSteal;
import me.existdev.exist.module.modules.combat.Velocity;
import me.existdev.exist.module.modules.movement.FastLadder;
import me.existdev.exist.module.modules.movement.FastStair;
import me.existdev.exist.module.modules.movement.Fly;
import me.existdev.exist.module.modules.movement.InvMove;
import me.existdev.exist.module.modules.movement.Jesus;
import me.existdev.exist.module.modules.movement.NoSlowdown;
import me.existdev.exist.module.modules.movement.Speed;
import me.existdev.exist.module.modules.movement.Sprint;
import me.existdev.exist.module.modules.movement.Step;
import me.existdev.exist.module.modules.movement.WaterSpeed;
import me.existdev.exist.module.modules.movement.WebWalk;
import me.existdev.exist.module.modules.player.AntiCactus;
import me.existdev.exist.module.modules.player.FastEat;
import me.existdev.exist.module.modules.player.InvCleaner;
import me.existdev.exist.module.modules.player.NoFall;
import me.existdev.exist.module.modules.settings.CubeCraft;
import me.existdev.exist.module.modules.settings.Hypixel;
import me.existdev.exist.module.modules.settings.Mineplex;
import me.existdev.exist.module.modules.settings.NCP;
import me.existdev.exist.module.modules.world.ScaffoldWalk;
import me.existdev.exist.utils.ChatUtils;

public class AAC extends Module {
   public AAC() {
      super("AAC", 0, Module.Category.Settings);
   }

   public void onEnable() {
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(Aura.class), "Mode").setCurrentOption("AAC");
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(Velocity.class), "Mode").setCurrentOption("AAC");
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(Speed.class), "Mode").setCurrentOption("AAC3.3.9BHop");
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(NoSlowdown.class), "Mode").setCurrentOption("AAC");
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(Step.class), "Mode").setCurrentOption("AAC");
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(NoFall.class), "Mode").setCurrentOption("AAC");
      Exist.settingManager.getSetting(Exist.moduleManager.getModule(ScaffoldWalk.class), "Mode").setCurrentOption("AACFast");
      Exist.moduleManager.getModule(AntiBot.class).setToggled(false);
      Exist.moduleManager.getModule(AutoArmor.class).setToggled(true);
      Exist.moduleManager.getModule(ChestSteal.class).setToggled(false);
      Exist.moduleManager.getModule(Velocity.class).setToggled(true);
      Exist.moduleManager.getModule(FastLadder.class).setToggled(true);
      Exist.moduleManager.getModule(FastStair.class).setToggled(true);
      Exist.moduleManager.getModule(Fly.class).setToggled(false);
      Exist.moduleManager.getModule(Jesus.class).setToggled(false);
      Exist.moduleManager.getModule(Speed.class).setToggled(false);
      Exist.moduleManager.getModule(WaterSpeed.class).setToggled(true);
      Exist.moduleManager.getModule(WebWalk.class).setToggled(true);
      Exist.moduleManager.getModule(Step.class).setToggled(true);
      Exist.moduleManager.getModule(NoSlowdown.class).setToggled(true);
      Exist.moduleManager.getModule(InvMove.class).setToggled(true);
      Exist.moduleManager.getModule(AntiCactus.class).setToggled(true);
      Exist.moduleManager.getModule(NoFall.class).setToggled(true);
      Exist.moduleManager.getModule(Sprint.class).setToggled(true);
      Exist.moduleManager.getModule(InvCleaner.class).setToggled(true);
      Exist.moduleManager.getModule(Hypixel.class).setToggled(false);
      Exist.moduleManager.getModule(NCP.class).setToggled(false);
      Exist.moduleManager.getModule(Mineplex.class).setToggled(false);
      Exist.moduleManager.getModule(FastEat.class).setToggled(false);
      Exist.moduleManager.getModule(CubeCraft.class).setToggled(false);
      Exist.moduleManager.getModule(AntiBot.class).onDisable();
      Exist.moduleManager.getModule(AutoArmor.class).onEnable();
      Exist.moduleManager.getModule(ChestSteal.class).onDisable();
      Exist.moduleManager.getModule(Velocity.class).onEnable();
      Exist.moduleManager.getModule(FastStair.class).onEnable();
      Exist.moduleManager.getModule(FastLadder.class).onEnable();
      Exist.moduleManager.getModule(Fly.class).onDisable();
      Exist.moduleManager.getModule(Jesus.class).onDisable();
      Exist.moduleManager.getModule(Speed.class).onDisable();
      Exist.moduleManager.getModule(WaterSpeed.class).onEnable();
      Exist.moduleManager.getModule(WebWalk.class).onEnable();
      Exist.moduleManager.getModule(Step.class).onEnable();
      Exist.moduleManager.getModule(NoSlowdown.class).onEnable();
      Exist.moduleManager.getModule(InvMove.class).onEnable();
      Exist.moduleManager.getModule(AntiCactus.class).onEnable();
      Exist.moduleManager.getModule(NoFall.class).onEnable();
      Exist.moduleManager.getModule(Sprint.class).onEnable();
      Exist.moduleManager.getModule(InvCleaner.class).onEnable();
      Exist.moduleManager.getModule(Hypixel.class).onDisable();
      Exist.moduleManager.getModule(NCP.class).onDisable();
      Exist.moduleManager.getModule(Mineplex.class).onDisable();
      Exist.moduleManager.getModule(FastEat.class).onDisable();
      Exist.moduleManager.getModule(CubeCraft.class).onDisable();
      ChatUtils.tellPlayer("Load Setting! " + ChatFormatting.GREEN + "[AAC]");
      this.toggle();
      super.onEnable();
   }
}
