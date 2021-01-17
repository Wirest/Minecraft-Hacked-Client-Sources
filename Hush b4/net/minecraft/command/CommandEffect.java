// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import java.util.Collection;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;

public class CommandEffect extends CommandBase
{
    @Override
    public String getCommandName() {
        return "effect";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getCommandUsage(final ICommandSender sender) {
        return "commands.effect.usage";
    }
    
    @Override
    public void processCommand(final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        final EntityLivingBase entitylivingbase = CommandBase.getEntity(sender, args[0], (Class<? extends EntityLivingBase>)EntityLivingBase.class);
        if (args[1].equals("clear")) {
            if (entitylivingbase.getActivePotionEffects().isEmpty()) {
                throw new CommandException("commands.effect.failure.notActive.all", new Object[] { entitylivingbase.getName() });
            }
            entitylivingbase.clearActivePotions();
            CommandBase.notifyOperators(sender, this, "commands.effect.success.removed.all", entitylivingbase.getName());
        }
        else {
            int i;
            try {
                i = CommandBase.parseInt(args[1], 1);
            }
            catch (NumberInvalidException numberinvalidexception) {
                final Potion potion = Potion.getPotionFromResourceLocation(args[1]);
                if (potion == null) {
                    throw numberinvalidexception;
                }
                i = potion.id;
            }
            int j = 600;
            int l = 30;
            int k = 0;
            if (i < 0 || i >= Potion.potionTypes.length || Potion.potionTypes[i] == null) {
                throw new NumberInvalidException("commands.effect.notFound", new Object[] { i });
            }
            final Potion potion2 = Potion.potionTypes[i];
            if (args.length >= 3) {
                l = CommandBase.parseInt(args[2], 0, 1000000);
                if (potion2.isInstant()) {
                    j = l;
                }
                else {
                    j = l * 20;
                }
            }
            else if (potion2.isInstant()) {
                j = 1;
            }
            if (args.length >= 4) {
                k = CommandBase.parseInt(args[3], 0, 255);
            }
            boolean flag = true;
            if (args.length >= 5 && "true".equalsIgnoreCase(args[4])) {
                flag = false;
            }
            if (l > 0) {
                final PotionEffect potioneffect = new PotionEffect(i, j, k, false, flag);
                entitylivingbase.addPotionEffect(potioneffect);
                CommandBase.notifyOperators(sender, this, "commands.effect.success", new ChatComponentTranslation(potioneffect.getEffectName(), new Object[0]), i, k, entitylivingbase.getName(), l);
            }
            else {
                if (!entitylivingbase.isPotionActive(i)) {
                    throw new CommandException("commands.effect.failure.notActive", new Object[] { new ChatComponentTranslation(potion2.getName(), new Object[0]), entitylivingbase.getName() });
                }
                entitylivingbase.removePotionEffect(i);
                CommandBase.notifyOperators(sender, this, "commands.effect.success.removed", new ChatComponentTranslation(potion2.getName(), new Object[0]), entitylivingbase.getName());
            }
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return (args.length == 1) ? CommandBase.getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : ((args.length == 2) ? CommandBase.getListOfStringsMatchingLastWord(args, Potion.func_181168_c()) : ((args.length == 5) ? CommandBase.getListOfStringsMatchingLastWord(args, "true", "false") : null));
    }
    
    protected String[] getAllUsernames() {
        return MinecraftServer.getServer().getAllUsernames();
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return index == 0;
    }
}
