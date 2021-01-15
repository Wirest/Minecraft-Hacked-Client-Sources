// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import cf.euphoria.euphorical.Utils.MathUtils;
import cf.euphoria.euphorical.Utils.ChatUtils;
import net.minecraft.client.Minecraft;

public abstract class Cmd
{
    private String name;
    private String help;
    private String[] syntax;
    protected Minecraft mc;
    
    public Cmd() {
        this.mc = Minecraft.getMinecraft();
        this.name = this.getClass().getAnnotation(Info.class).name();
        this.help = this.getClass().getAnnotation(Info.class).help();
        this.syntax = this.getClass().getAnnotation(Info.class).syntax();
    }
    
    public final String getCmdName() {
        return this.name;
    }
    
    public final String getHelp() {
        return this.help;
    }
    
    public final String[] getSyntax() {
        return this.syntax;
    }
    
    public final void printHelp() {
        String[] split;
        for (int length = (split = this.help.split("\n")).length, i = 0; i < length; ++i) {
            final String line = split[i];
            ChatUtils.sendMessageToPlayer(line);
        }
    }
    
    public final void printSyntax() {
        String output = "§o." + this.name + "§r";
        if (this.syntax.length != 0) {
            output = String.valueOf(String.valueOf(String.valueOf(output))) + " " + this.syntax[0];
            for (int i = 1; i < this.syntax.length; ++i) {
                output = String.valueOf(output) + "\n    " + this.syntax[i];
            }
        }
        String[] split;
        for (int length = (split = output.split("\n")).length, j = 0; j < length; ++j) {
            final String line = split[j];
            ChatUtils.sendMessageToPlayer(line);
        }
    }
    
    protected final int[] argsToPos(final String... args) throws Error {
        int[] pos = new int[3];
        if (args.length == 3) {
            final int[] playerPos = { (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ };
            for (int i = 0; i < args.length; ++i) {
                if (MathUtils.isInteger(args[i])) {
                    pos[i] = Integer.parseInt(args[i]);
                }
                else if (args[i].startsWith("~")) {
                    if (args[i].equals("~")) {
                        pos[i] = playerPos[i];
                    }
                    else if (MathUtils.isInteger(args[i].substring(1))) {
                        pos[i] = playerPos[i] + Integer.parseInt(args[i].substring(1));
                    }
                    else {
                        this.syntaxError("Invalid coordinates.");
                    }
                }
                else {
                    this.syntaxError("Invalid coordinates.");
                }
            }
        }
        else if (args.length == 1) {
            EntityLivingBase entity = null;
            for (final Object obj : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if (obj instanceof EntityLivingBase) {
                    entity = (EntityLivingBase)obj;
                }
            }
            if (entity == null) {
                this.error("Entity \"" + args[0] + "\" could not be found.");
            }
            final BlockPos blockPos = new BlockPos(entity);
            pos = new int[] { blockPos.getX(), blockPos.getY(), blockPos.getZ() };
        }
        else {
            this.syntaxError("Invalid coordinates.");
        }
        return pos;
    }
    
    protected final void syntaxError() throws SyntaxError {
        throw new SyntaxError();
    }
    
    protected final void syntaxError(final String message) throws SyntaxError {
        throw new SyntaxError(message);
    }
    
    protected final void error(final String message) throws Error {
        throw new Error(message);
    }
    
    public abstract void execute(final String[] p0) throws Error;
    
    public class SyntaxError extends Error
    {
        public SyntaxError() {
        }
        
        public SyntaxError(final String message) {
            super(message);
        }
    }
    
    public class Error extends Throwable
    {
        public Error() {
        }
        
        public Error(final String message) {
            super(message);
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
        
        String help();
        
        String[] syntax();
        
        String tags() default "";
        
        String tutorial() default "";
    }
}
