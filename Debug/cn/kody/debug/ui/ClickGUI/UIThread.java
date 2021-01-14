package cn.kody.debug.ui.ClickGUI;


public class UIThread implements Runnable
{
    UIClick uiclick;
    
    UIThread(UIClick uiclick) {
        this.uiclick = uiclick;
    }
    
    @Override
    public void run() {
        this.uiclick.menu = new ClickMenu();
        this.uiclick.initialized = true;
    }
}
