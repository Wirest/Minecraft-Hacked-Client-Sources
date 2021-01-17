package me.rigamortis.faurax.module.modules.misc;

import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import me.rigamortis.faurax.module.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;

public class ClaimFinder extends Module
{
    public int delay;
    private int x;
    private int z;
    private int line;
    private List<String> maps;
    private File coords;
    private boolean finished;
    
    public ClaimFinder() {
        this.maps = new CopyOnWriteArrayList<String>();
        this.setName("ClaimFinder");
        this.setType(ModuleType.MISC);
        this.setModInfo("");
        this.setVisible(true);
        this.setColor(-2836728);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        this.isToggled();
    }
    
    @EventTarget
    public void receivePacket(final EventReceivePacket e) {
        this.isToggled();
    }
}
