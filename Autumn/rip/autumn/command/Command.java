package rip.autumn.command;

@FunctionalInterface
public interface Command {
   void execute(String... var1);
}
