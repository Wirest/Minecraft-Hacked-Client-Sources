package me.razerboy420.weepcraft.commands.cmds;

import java.util.Iterator;
import me.razerboy420.weepcraft.files.ValuesFile;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.value.Value;
import me.razerboy420.weepcraft.commands.Command;

public class CommandAllSettings extends Command
{
    public CommandAllSettings() {
        super(new String[] { "mod", "modsettings" }, "All module settings", ".modsettings <module name> <setting name> <setting value>");
    }
    
    @Override
    public boolean runCommand(final String command, final String[] args) {
        final String[] args2 = command.split(" ");
        final String modname = args2[1];
        final String valuename = String.valueOf(modname) + "_" + args2[2];
        String valuenameclean = valuename.replace(String.valueOf(modname) + "_" + args2[2], "");
        for (final Value value : Value.modes) {
            if (value.name.equalsIgnoreCase(valuename)) {
                valuenameclean = value.name.replace(String.valueOf(modname) + "_", "");
            }
        }
        final String var21 = args2[3];
        for (final Module var23 : Weepcraft.getMods()) {
            if (var23.getAlias().equalsIgnoreCase(modname)) {
                for (final Value v : var23.getValues()) {
                    if (valuename.equalsIgnoreCase(v.name)) {
                        System.out.println(v.name);
                        if (v.isaboolean) {
                            try {
                                final boolean var25 = Boolean.parseBoolean(var21);
                                v.boolvalue = var25;
                                Wrapper.tellPlayer(String.valueOf(valuenameclean) + " set to: " + var25);
                                ValuesFile.save();
                                return true;
                            }
                            catch (Exception var32) {
                                Wrapper.tellPlayer(String.valueOf(valuenameclean) + " is a boolean! Please use either true or false!");
                                return false;
                            }
                        }
                        if (v.isafloat) {
                            try {
                                float var26 = Float.parseFloat(var21);
                                if (var26 < v.min) {
                                    var26 = v.min;
                                }
                                if (var26 > v.max) {
                                    var26 = v.max;
                                }
                                v.value = var26;
                                Wrapper.tellPlayer(String.valueOf(valuenameclean) + " set to: " + var26);
                                ValuesFile.save();
                                return true;
                            }
                            catch (Exception var33) {
                                Wrapper.tellPlayer(String.valueOf(valuenameclean) + " is a number value! Please use a valid number!");
                                return false;
                            }
                        }
                        if (v.isamode) {
                            try {
                                final String e = "";
                                final String[] var27 = v.allothers;
                                for (int var28 = v.allothers.length, var29 = 0; var29 < var28; ++var29) {
                                    final String s1 = var27[var29];
                                    if (s1.equalsIgnoreCase(var21)) {
                                        v.stringvalue = s1;
                                        Wrapper.tellPlayer(String.valueOf(valuenameclean) + " set to: " + s1);
                                        ValuesFile.save();
                                        return true;
                                    }
                                }
                                Wrapper.tellPlayer("That's not a valid mode for " + valuenameclean);
                                String s1 = "";
                                final String[] var30 = v.allothers;
                                for (int var31 = v.allothers.length, var28 = 0; var28 < var31; ++var28) {
                                    final String s2 = var30[var28];
                                    s1 = String.valueOf(s1) + s2 + ", ";
                                }
                                Wrapper.tellPlayer(s1);
                                return true;
                            }
                            catch (Exception var34) {
                                Wrapper.tellPlayer("That's not a valid mode for " + valuenameclean);
                                String s1 = "";
                                final String[] var30 = v.allothers;
                                for (int var31 = v.allothers.length, var28 = 0; var28 < var31; ++var28) {
                                    final String s2 = var30[var28];
                                    s1 = String.valueOf(s1) + s2 + ", ";
                                }
                                Wrapper.tellPlayer(s1);
                                return true;
                            }
                        }
                        if (v.iseditable) {
                            v.editvalue = var21;
                            Wrapper.tellPlayer(String.valueOf(valuenameclean) + " set to: " + var21);
                            ValuesFile.save();
                            return true;
                        }
                        continue;
                    }
                }
                Wrapper.tellPlayer("Couldn't find that value");
                return false;
            }
        }
        return false;
    }
}
