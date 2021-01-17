// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Union;
import com.sun.jna.Structure;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public interface WinUser extends StdCallLibrary, WinDef
{
    public static final HWND HWND_BROADCAST = new HWND(Pointer.createConstant(65535));
    public static final int FLASHW_STOP = 0;
    public static final int FLASHW_CAPTION = 1;
    public static final int FLASHW_TRAY = 2;
    public static final int FLASHW_ALL = 3;
    public static final int FLASHW_TIMER = 4;
    public static final int FLASHW_TIMERNOFG = 12;
    public static final int IMAGE_BITMAP = 0;
    public static final int IMAGE_ICON = 1;
    public static final int IMAGE_CURSOR = 2;
    public static final int IMAGE_ENHMETAFILE = 3;
    public static final int LR_DEFAULTCOLOR = 0;
    public static final int LR_MONOCHROME = 1;
    public static final int LR_COLOR = 2;
    public static final int LR_COPYRETURNORG = 4;
    public static final int LR_COPYDELETEORG = 8;
    public static final int LR_LOADFROMFILE = 16;
    public static final int LR_LOADTRANSPARENT = 32;
    public static final int LR_DEFAULTSIZE = 64;
    public static final int LR_VGACOLOR = 128;
    public static final int LR_LOADMAP3DCOLORS = 4096;
    public static final int LR_CREATEDIBSECTION = 8192;
    public static final int LR_COPYFROMRESOURCE = 16384;
    public static final int LR_SHARED = 32768;
    public static final int GWL_EXSTYLE = -20;
    public static final int GWL_STYLE = -16;
    public static final int GWL_WNDPROC = -4;
    public static final int GWL_HINSTANCE = -6;
    public static final int GWL_ID = -12;
    public static final int GWL_USERDATA = -21;
    public static final int DWL_DLGPROC = 4;
    public static final int DWL_MSGRESULT = 0;
    public static final int DWL_USER = 8;
    public static final int WS_MAXIMIZE = 16777216;
    public static final int WS_VISIBLE = 268435456;
    public static final int WS_MINIMIZE = 536870912;
    public static final int WS_CHILD = 1073741824;
    public static final int WS_POPUP = Integer.MIN_VALUE;
    public static final int WS_EX_COMPOSITED = 536870912;
    public static final int WS_EX_LAYERED = 524288;
    public static final int WS_EX_TRANSPARENT = 32;
    public static final int LWA_COLORKEY = 1;
    public static final int LWA_ALPHA = 2;
    public static final int ULW_COLORKEY = 1;
    public static final int ULW_ALPHA = 2;
    public static final int ULW_OPAQUE = 4;
    public static final int AC_SRC_OVER = 0;
    public static final int AC_SRC_ALPHA = 1;
    public static final int AC_SRC_NO_PREMULT_ALPHA = 1;
    public static final int AC_SRC_NO_ALPHA = 2;
    public static final int VK_SHIFT = 16;
    public static final int VK_LSHIFT = 160;
    public static final int VK_RSHIFT = 161;
    public static final int VK_CONTROL = 17;
    public static final int VK_LCONTROL = 162;
    public static final int VK_RCONTROL = 163;
    public static final int VK_MENU = 18;
    public static final int VK_LMENU = 164;
    public static final int VK_RMENU = 165;
    public static final int MOD_ALT = 1;
    public static final int MOD_CONTROL = 2;
    public static final int MOD_NOREPEAT = 16384;
    public static final int MOD_SHIFT = 4;
    public static final int MOD_WIN = 8;
    public static final int WH_KEYBOARD = 2;
    public static final int WH_MOUSE = 7;
    public static final int WH_KEYBOARD_LL = 13;
    public static final int WH_MOUSE_LL = 14;
    public static final int WM_PAINT = 15;
    public static final int WM_CLOSE = 16;
    public static final int WM_QUIT = 18;
    public static final int WM_SHOWWINDOW = 24;
    public static final int WM_DRAWITEM = 43;
    public static final int WM_KEYDOWN = 256;
    public static final int WM_CHAR = 258;
    public static final int WM_SYSCOMMAND = 274;
    public static final int WM_MDIMAXIMIZE = 549;
    public static final int WM_HOTKEY = 786;
    public static final int WM_KEYUP = 257;
    public static final int WM_SYSKEYDOWN = 260;
    public static final int WM_SYSKEYUP = 261;
    public static final int SM_CXSCREEN = 0;
    public static final int SM_CYSCREEN = 1;
    public static final int SM_CXVSCROLL = 2;
    public static final int SM_CYHSCROLL = 3;
    public static final int SM_CYCAPTION = 4;
    public static final int SM_CXBORDER = 5;
    public static final int SM_CYBORDER = 6;
    public static final int SM_CXDLGFRAME = 7;
    public static final int SM_CYDLGFRAME = 8;
    public static final int SM_CYVTHUMB = 9;
    public static final int SM_CXHTHUMB = 10;
    public static final int SM_CXICON = 11;
    public static final int SM_CYICON = 12;
    public static final int SM_CXCURSOR = 13;
    public static final int SM_CYCURSOR = 14;
    public static final int SM_CYMENU = 15;
    public static final int SM_CXFULLSCREEN = 16;
    public static final int SM_CYFULLSCREEN = 17;
    public static final int SM_CYKANJIWINDOW = 18;
    public static final int SM_MOUSEPRESENT = 19;
    public static final int SM_CYVSCROLL = 20;
    public static final int SM_CXHSCROLL = 21;
    public static final int SM_DEBUG = 22;
    public static final int SM_SWAPBUTTON = 23;
    public static final int SM_RESERVED1 = 24;
    public static final int SM_RESERVED2 = 25;
    public static final int SM_RESERVED3 = 26;
    public static final int SM_RESERVED4 = 27;
    public static final int SM_CXMIN = 28;
    public static final int SM_CYMIN = 29;
    public static final int SM_CXSIZE = 30;
    public static final int SM_CYSIZE = 31;
    public static final int SM_CXFRAME = 32;
    public static final int SM_CYFRAME = 33;
    public static final int SM_CXMINTRACK = 34;
    public static final int SM_CYMINTRACK = 35;
    public static final int SM_CXDOUBLECLK = 36;
    public static final int SM_CYDOUBLECLK = 37;
    public static final int SM_CXICONSPACING = 38;
    public static final int SM_CYICONSPACING = 39;
    public static final int SM_MENUDROPALIGNMENT = 40;
    public static final int SM_PENWINDOWS = 41;
    public static final int SM_DBCSENABLED = 42;
    public static final int SM_CMOUSEBUTTONS = 43;
    public static final int SM_CXFIXEDFRAME = 7;
    public static final int SM_CYFIXEDFRAME = 8;
    public static final int SM_CXSIZEFRAME = 32;
    public static final int SM_CYSIZEFRAME = 33;
    public static final int SM_SECURE = 44;
    public static final int SM_CXEDGE = 45;
    public static final int SM_CYEDGE = 46;
    public static final int SM_CXMINSPACING = 47;
    public static final int SM_CYMINSPACING = 48;
    public static final int SM_CXSMICON = 49;
    public static final int SM_CYSMICON = 50;
    public static final int SM_CYSMCAPTION = 51;
    public static final int SM_CXSMSIZE = 52;
    public static final int SM_CYSMSIZE = 53;
    public static final int SM_CXMENUSIZE = 54;
    public static final int SM_CYMENUSIZE = 55;
    public static final int SM_ARRANGE = 56;
    public static final int SM_CXMINIMIZED = 57;
    public static final int SM_CYMINIMIZED = 58;
    public static final int SM_CXMAXTRACK = 59;
    public static final int SM_CYMAXTRACK = 60;
    public static final int SM_CXMAXIMIZED = 61;
    public static final int SM_CYMAXIMIZED = 62;
    public static final int SM_NETWORK = 63;
    public static final int SM_CLEANBOOT = 67;
    public static final int SM_CXDRAG = 68;
    public static final int SM_CYDRAG = 69;
    public static final int SM_SHOWSOUNDS = 70;
    public static final int SM_CXMENUCHECK = 71;
    public static final int SM_CYMENUCHECK = 72;
    public static final int SM_SLOWMACHINE = 73;
    public static final int SM_MIDEASTENABLED = 74;
    public static final int SM_MOUSEWHEELPRESENT = 75;
    public static final int SM_XVIRTUALSCREEN = 76;
    public static final int SM_YVIRTUALSCREEN = 77;
    public static final int SM_CXVIRTUALSCREEN = 78;
    public static final int SM_CYVIRTUALSCREEN = 79;
    public static final int SM_CMONITORS = 80;
    public static final int SM_SAMEDISPLAYFORMAT = 81;
    public static final int SM_IMMENABLED = 82;
    public static final int SM_CXFOCUSBORDER = 83;
    public static final int SM_CYFOCUSBORDER = 84;
    public static final int SM_TABLETPC = 86;
    public static final int SM_MEDIACENTER = 87;
    public static final int SM_STARTER = 88;
    public static final int SM_SERVERR2 = 89;
    public static final int SM_MOUSEHORIZONTALWHEELPRESENT = 91;
    public static final int SM_CXPADDEDBORDER = 92;
    public static final int SM_REMOTESESSION = 4096;
    public static final int SM_SHUTTINGDOWN = 8192;
    public static final int SM_REMOTECONTROL = 8193;
    public static final int SM_CARETBLINKINGENABLED = 8194;
    public static final int SW_HIDE = 0;
    public static final int SW_SHOWNORMAL = 1;
    public static final int SW_NORMAL = 1;
    public static final int SW_SHOWMINIMIZED = 2;
    public static final int SW_SHOWMAXIMIZED = 3;
    public static final int SW_MAXIMIZE = 3;
    public static final int SW_SHOWNOACTIVATE = 4;
    public static final int SW_SHOW = 5;
    public static final int SW_MINIMIZE = 6;
    public static final int SW_SHOWMINNOACTIVE = 7;
    public static final int SW_SHOWNA = 8;
    public static final int SW_RESTORE = 9;
    public static final int SW_SHOWDEFAULT = 10;
    public static final int SW_FORCEMINIMIZE = 11;
    public static final int SW_MAX = 11;
    public static final int RDW_INVALIDATE = 1;
    public static final int RDW_INTERNALPAINT = 2;
    public static final int RDW_ERASE = 4;
    public static final int RDW_VALIDATE = 8;
    public static final int RDW_NOINTERNALPAINT = 16;
    public static final int RDW_NOERASE = 32;
    public static final int RDW_NOCHILDREN = 64;
    public static final int RDW_ALLCHILDREN = 128;
    public static final int RDW_UPDATENOW = 256;
    public static final int RDW_ERASENOW = 512;
    public static final int RDW_FRAME = 1024;
    public static final int RDW_NOFRAME = 2048;
    public static final int GW_HWNDFIRST = 0;
    public static final int GW_HWNDLAST = 1;
    public static final int GW_HWNDNEXT = 2;
    public static final int GW_HWNDPREV = 3;
    public static final int GW_OWNER = 4;
    public static final int GW_CHILD = 5;
    public static final int GW_ENABLEDPOPUP = 6;
    public static final int SWP_NOZORDER = 4;
    public static final int SC_MINIMIZE = 61472;
    public static final int SC_MAXIMIZE = 61488;
    
    public static class GUITHREADINFO extends Structure
    {
        public int cbSize;
        public int flags;
        public HWND hwndActive;
        public HWND hwndFocus;
        public HWND hwndCapture;
        public HWND hwndMenuOwner;
        public HWND hwndMoveSize;
        public HWND hwndCaret;
        public RECT rcCaret;
        
        public GUITHREADINFO() {
            this.cbSize = this.size();
        }
    }
    
    public static class WINDOWINFO extends Structure
    {
        public int cbSize;
        public RECT rcWindow;
        public RECT rcClient;
        public int dwStyle;
        public int dwExStyle;
        public int dwWindowStatus;
        public int cxWindowBorders;
        public int cyWindowBorders;
        public short atomWindowType;
        public short wCreatorVersion;
        
        public WINDOWINFO() {
            this.cbSize = this.size();
        }
    }
    
    public static class POINT extends Structure
    {
        public int x;
        public int y;
        
        public POINT() {
        }
        
        public POINT(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class MSG extends Structure
    {
        public HWND hWnd;
        public int message;
        public WPARAM wParam;
        public LPARAM lParam;
        public int time;
        public POINT pt;
    }
    
    public static class FLASHWINFO extends Structure
    {
        public int cbSize;
        public WinNT.HANDLE hWnd;
        public int dwFlags;
        public int uCount;
        public int dwTimeout;
    }
    
    public static class SIZE extends Structure
    {
        public int cx;
        public int cy;
        
        public SIZE() {
        }
        
        public SIZE(final int w, final int h) {
            this.cx = w;
            this.cy = h;
        }
    }
    
    public static class BLENDFUNCTION extends Structure
    {
        public byte BlendOp;
        public byte BlendFlags;
        public byte SourceConstantAlpha;
        public byte AlphaFormat;
        
        public BLENDFUNCTION() {
            this.BlendOp = 0;
            this.BlendFlags = 0;
        }
    }
    
    public static class HHOOK extends WinNT.HANDLE
    {
    }
    
    public static class KBDLLHOOKSTRUCT extends Structure
    {
        public int vkCode;
        public int scanCode;
        public int flags;
        public int time;
        public BaseTSD.ULONG_PTR dwExtraInfo;
    }
    
    public static class HARDWAREINPUT extends Structure
    {
        public DWORD uMsg;
        public WORD wParamL;
        public WORD wParamH;
        
        public HARDWAREINPUT() {
        }
        
        public HARDWAREINPUT(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends HARDWAREINPUT implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    public static class INPUT extends Structure
    {
        public static final int INPUT_MOUSE = 0;
        public static final int INPUT_KEYBOARD = 1;
        public static final int INPUT_HARDWARE = 2;
        public DWORD type;
        public INPUT_UNION input;
        
        public INPUT() {
            this.input = new INPUT_UNION();
        }
        
        public INPUT(final Pointer memory) {
            super(memory);
            this.input = new INPUT_UNION();
            this.read();
        }
        
        public static class ByReference extends INPUT implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
        
        public static class INPUT_UNION extends Union
        {
            public MOUSEINPUT mi;
            public KEYBDINPUT ki;
            public HARDWAREINPUT hi;
            
            public INPUT_UNION() {
            }
            
            public INPUT_UNION(final Pointer memory) {
                super(memory);
                this.read();
            }
        }
    }
    
    public static class KEYBDINPUT extends Structure
    {
        public static final int KEYEVENTF_EXTENDEDKEY = 1;
        public static final int KEYEVENTF_KEYUP = 2;
        public static final int KEYEVENTF_UNICODE = 4;
        public static final int KEYEVENTF_SCANCODE = 8;
        public WORD wVk;
        public WORD wScan;
        public DWORD dwFlags;
        public DWORD time;
        public BaseTSD.ULONG_PTR dwExtraInfo;
        
        public KEYBDINPUT() {
        }
        
        public KEYBDINPUT(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends KEYBDINPUT implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    public static class MOUSEINPUT extends Structure
    {
        public LONG dx;
        public LONG dy;
        public DWORD mouseData;
        public DWORD dwFlags;
        public DWORD time;
        public BaseTSD.ULONG_PTR dwExtraInfo;
        
        public MOUSEINPUT() {
        }
        
        public MOUSEINPUT(final Pointer memory) {
            super(memory);
            this.read();
        }
        
        public static class ByReference extends MOUSEINPUT implements Structure.ByReference
        {
            public ByReference() {
            }
            
            public ByReference(final Pointer memory) {
                super(memory);
            }
        }
    }
    
    public interface HOOKPROC extends StdCallCallback
    {
    }
    
    public interface LowLevelKeyboardProc extends HOOKPROC
    {
        LRESULT callback(final int p0, final WPARAM p1, final KBDLLHOOKSTRUCT p2);
    }
    
    public interface WNDENUMPROC extends StdCallCallback
    {
        boolean callback(final HWND p0, final Pointer p1);
    }
}
