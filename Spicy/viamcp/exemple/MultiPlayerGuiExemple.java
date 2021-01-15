package viamcp.exemple;

import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

public class MultiPlayerGuiExemple {

/*---------------------------------------------------------------------------------------------------------*/
// You need to create your button this way in the button creation method in the GuiMultiplayer.java class.
/*---------------------------------------------------------------------------------------------------------*/

    public void createButtons() {
        //this.buttonList.add(this.btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit", new Object[0])));
        //this.buttonList.add(this.btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete", new Object[0])));
        //this.buttonList.add(this.btnSelectServer = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select", new Object[0])));
        //this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct", new Object[0])));
        //this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
        //this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
        //this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));

        /* ViaVersion */
/*-->   buttonList.add(new GuiButton(69, 5, 38, 98, 20, ProtocolUtils.getProtocolName(ViaFabric.clientSideVersion)));   */
        /* ---------- */

        //this.selectServer(this.serverListSelector.func_148193_k());
    }

/*----------------------------------------------------------------------------------------------------*/
// You need to create the button id in the GuiMultiplayer.java class in the "actionPerformed" method.
/*----------------------------------------------------------------------------------------------------*/

    protected void actionPerformed(GuiButton button) throws IOException {
        /*if (button.enabled) {
            GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());*/


            /* ViaVersion */
// -->      if (button.id == 69)
// -->          mc.displayGuiScreen(new GuiProtocolSelector(this));
            /* ---------- */


        /*if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
        {
            String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;

            if (s4 != null)
            {
                this.deletingServer = true;
                String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
                String s1 = "\'" + s4 + "\' " + I18n.format("selectServer.deleteWarning", new Object[0]);
                String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
                String s3 = I18n.format("gui.cancel", new Object[0]);
                GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
                this.mc.displayGuiScreen(guiyesno);
            }
        }
        else if (button.id == 1)
        {
            this.connectToSelected();
        }
        else if (button.id == 4)
        {
            this.directConnect = true;
            this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
        }
        else if (button.id == 3)
        {
            this.addingServer = true;
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false)));
        }
        else if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal)
        {
            this.editingServer = true;
            ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
            this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
            this.selectedServer.copyFrom(serverdata);
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
        }
        else if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 8)
        {
            this.refreshServerList();
        }*/
    }
}

