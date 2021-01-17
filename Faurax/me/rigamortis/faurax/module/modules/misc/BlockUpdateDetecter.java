package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;

public class BlockUpdateDetecter extends Module
{
    public BlockUpdateDetecter() {
        this.setKey("");
        this.setName("BlockUpdate");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
