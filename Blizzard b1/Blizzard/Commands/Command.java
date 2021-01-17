/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.Commands;

public abstract class Command {
    public abstract String getAlias();

    public abstract String getSyntax();

    public abstract void onCommand(String var1, String[] var2) throws Exception;
}

