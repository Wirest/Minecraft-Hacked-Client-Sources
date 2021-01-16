package org.m0jang.crystal.Mod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.MathUtils;

public abstract class Command {
   private String name = ((Command.Info)this.getClass().getAnnotation(Command.Info.class)).name();
   private String help = ((Command.Info)this.getClass().getAnnotation(Command.Info.class)).help();
   private String[] syntax = ((Command.Info)this.getClass().getAnnotation(Command.Info.class)).syntax();
   protected Minecraft mc = Minecraft.getMinecraft();

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
      int length = (split = this.help.split("\n")).length;

      for(int i = 0; i < length; ++i) {
         String line = split[i];
         ChatUtils.sendMessageToPlayer(line);
      }

   }

   public final void printSyntax() {
      String output = "\247o." + this.name + "\247r";
      if (this.syntax.length != 0) {
         output = String.valueOf(String.valueOf(output)) + " " + this.syntax[0];

         for(int i = 1; i < this.syntax.length; ++i) {
            output = output + "\n    " + this.syntax[i];
         }
      }

      String[] split;
      int length = (split = output.split("\n")).length;

      for(int j = 0; j < length; ++j) {
         String line = split[j];
         ChatUtils.sendMessageToPlayer(line);
      }

   }

   protected final int[] argsToPos(String... args) throws Command.Error {
      int[] pos = new int[3];
      if (args.length == 3) {
         int[] var10000 = new int[3];
         Minecraft.getMinecraft();
         var10000[0] = (int)Minecraft.thePlayer.posX;
         Minecraft.getMinecraft();
         var10000[1] = (int)Minecraft.thePlayer.posY;
         Minecraft.getMinecraft();
         var10000[2] = (int)Minecraft.thePlayer.posZ;
         int[] playerPos = var10000;

         for(int i = 0; i < args.length; ++i) {
            if (MathUtils.isInteger(args[i])) {
               pos[i] = Integer.parseInt(args[i]);
            } else if (args[i].startsWith("~")) {
               if (args[i].equals("~")) {
                  pos[i] = playerPos[i];
               } else if (MathUtils.isInteger(args[i].substring(1))) {
                  pos[i] = playerPos[i] + Integer.parseInt(args[i].substring(1));
               } else {
                  this.syntaxError("Invalid coordinates.");
               }
            } else {
               this.syntaxError("Invalid coordinates.");
            }
         }
      } else if (args.length == 1) {
         EntityLivingBase entity = null;
         Minecraft.getMinecraft();
         Iterator var5 = Minecraft.theWorld.loadedEntityList.iterator();

         while(var5.hasNext()) {
            Object obj = var5.next();
            if (obj instanceof EntityLivingBase) {
               entity = (EntityLivingBase)obj;
            }
         }

         if (entity == null) {
            this.error("Entity \"" + args[0] + "\" could not be found.");
         }

         BlockPos blockPos = new BlockPos(entity);
         pos = new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
      } else {
         this.syntaxError("Invalid coordinates.");
      }

      return pos;
   }

   protected final void syntaxError() throws Command.SyntaxError {
      throw new Command.SyntaxError();
   }

   protected final void syntaxError(String message) throws Command.SyntaxError {
      throw new Command.SyntaxError(message);
   }

   protected final void error(String message) throws Command.Error {
      throw new Command.Error(message);
   }

   public abstract void execute(String[] var1) throws Command.Error;

   public class Error extends Throwable {
      public Error() {
      }

      public Error(String message) {
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

   public class SyntaxError extends Command.Error {
      public SyntaxError() {
         super();
      }

      public SyntaxError(String message) {
         super(message);
      }
   }
}
