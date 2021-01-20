package cedo.command;

import net.minecraft.client.Minecraft;

public class Command {

    private final String name;
    private final String usage;
    protected Minecraft mc = Minecraft.getMinecraft();

    public Command(String name, String usage) {

        this.name = name;
        this.usage = usage;

    }

    public String getName() {
        return this.name;
    }

    public String getUsage() {
        return this.usage;
    }

    public void execute(String[] args) {
    }
}
