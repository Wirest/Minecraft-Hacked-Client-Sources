package me.rigamortis.faurax.hooks;

import net.minecraft.client.entity.*;
import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.commands.*;
import net.minecraft.client.*;
import net.minecraft.world.*;
import net.minecraft.client.network.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import me.cupboard.command.exception.*;
import me.cupboard.command.exception.argument.*;
import java.lang.reflect.*;
import me.cupboard.command.exception.parse.*;

public class EntityPlayerSPHook extends EntityPlayerSP implements PlayerHelper
{
    private final CommandManager executor;
    
    public EntityPlayerSPHook(final Minecraft mcIn, final World worldIn, final NetHandlerPlayClient p_i46278_3_, final StatFileWriter p_i46278_4_) {
        super(mcIn, worldIn, p_i46278_3_, p_i46278_4_);
        this.executor = new CommandManager();
    }
    
    @Override
    public void func_175161_p() {
    }
    
    @Override
    public void sendChatMessage(final String message) {
        if (message.startsWith("0")) {
            try {
                final Object feedback = this.executor.execute(message.substring(1));
                if (feedback instanceof String && !feedback.toString().isEmpty()) {
                    this.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§f " + feedback.toString()));
                }
            }
            catch (InvocationTargetException | IllegalAccessException | CommandNotFoundException | ArrayIndexOutOfBoundsException | NoSuchArgumentException | ParseException e) {
                if (e instanceof CommandNotFoundException) {
                    this.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§c Command not found."));
                }
                if (e instanceof NoSuchArgumentException) {
                    this.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§c No such argument found."));
                }
                if (e instanceof ArrayIndexOutOfBoundsException) {
                    this.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§c Not enough input."));
                }
                e.printStackTrace();
            }
        }
        else {
            super.sendChatMessage(message);
        }
    }
}
