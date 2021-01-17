/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.util.MathHelper
 */
package delta.command.commands;

import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class TestTPCommand
implements ICommand {
    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        double d;
        double d2;
        if (arrstring.length != 2) {
            this.printMessage(iCommandListener, "x z stp");
            return true;
        }
        try {
            d2 = Double.parseDouble(arrstring[0]);
            d = Double.parseDouble(arrstring[1]);
        }
        catch (Exception exception) {
            this.printMessage(iCommandListener, "x z VALIDE FDP");
            return true;
        }
        double d3 = TestTPCommand.mc.thePlayer.boundingBox.minY;
        double d4 = TestTPCommand.mc.thePlayer.posY - TestTPCommand.mc.thePlayer.boundingBox.minY;
        while (d3 <= 300.0) {
            TestTPCommand.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(TestTPCommand.mc.thePlayer.posX, d3 += 9.0, d3 + d4, TestTPCommand.mc.thePlayer.posZ, true));
            iCommandListener.print("[Stage 1] up()->" + d3);
        }
        iCommandListener.print("[Stage 2] calculating");
        int n = 9;
        double d5 = TestTPCommand.mc.thePlayer.posX;
        double d6 = TestTPCommand.mc.thePlayer.posZ;
        double d7 = d2 - d5;
        double d8 = d - d6;
        double d9 = Math.sqrt(Math.pow(Math.abs(d7), 2.0) + Math.pow(Math.abs(d8), 2.0));
        int n2 = (int)Math.floor(d9 / 9.0);
        float f = (float)Math.acos(Math.abs(d8) / d9);
        for (int i = 0; i < n2; ++i) {
            double d10 = d5 + (double)(MathHelper.cos((float)f) * 9.0f * (float)i);
            double d11 = d6 + (double)(MathHelper.sin((float)f) * 9.0f * (float)i);
            iCommandListener.print("[Stage 2] moving()->" + d10 + "|" + (d3 -= 0.03126) + "|" + d11);
            TestTPCommand.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(d10, d3, d3 + d4, d11, true));
        }
        TestTPCommand.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(d2, d3 -= 0.03126, d3 + d4, d, true));
        return true;
    }

    public String getHelp() {
        return this.getName() + " <x> <y> <z>";
    }

    public String[] getAliases() {
        return new String[0];
    }

    public String getDescription() {
        return "\u00a7c\u00a7l\u00a7mCommande de test issou\u00a77 (a enlever)";
    }

    public String getName() {
        return "testtp";
    }
}

