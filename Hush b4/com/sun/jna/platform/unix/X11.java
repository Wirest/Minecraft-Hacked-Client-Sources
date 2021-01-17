// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.unix;

import com.sun.jna.Callback;
import com.sun.jna.Union;
import com.sun.jna.Structure;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.FromNativeContext;
import com.sun.jna.Native;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Library;

public interface X11 extends Library
{
    public static final X11 INSTANCE = (X11)Native.loadLibrary("X11", X11.class);
    public static final int XK_0 = 48;
    public static final int XK_9 = 57;
    public static final int XK_A = 65;
    public static final int XK_Z = 90;
    public static final int XK_a = 97;
    public static final int XK_z = 122;
    public static final int XK_Shift_L = 65505;
    public static final int XK_Shift_R = 65505;
    public static final int XK_Control_L = 65507;
    public static final int XK_Control_R = 65508;
    public static final int XK_CapsLock = 65509;
    public static final int XK_ShiftLock = 65510;
    public static final int XK_Meta_L = 65511;
    public static final int XK_Meta_R = 65512;
    public static final int XK_Alt_L = 65513;
    public static final int XK_Alt_R = 65514;
    public static final int VisualNoMask = 0;
    public static final int VisualIDMask = 1;
    public static final int VisualScreenMask = 2;
    public static final int VisualDepthMask = 4;
    public static final int VisualClassMask = 8;
    public static final int VisualRedMaskMask = 16;
    public static final int VisualGreenMaskMask = 32;
    public static final int VisualBlueMaskMask = 64;
    public static final int VisualColormapSizeMask = 128;
    public static final int VisualBitsPerRGBMask = 256;
    public static final int VisualAllMask = 511;
    public static final Atom XA_PRIMARY = new Atom(1L);
    public static final Atom XA_SECONDARY = new Atom(2L);
    public static final Atom XA_ARC = new Atom(3L);
    public static final Atom XA_ATOM = new Atom(4L);
    public static final Atom XA_BITMAP = new Atom(5L);
    public static final Atom XA_CARDINAL = new Atom(6L);
    public static final Atom XA_COLORMAP = new Atom(7L);
    public static final Atom XA_CURSOR = new Atom(8L);
    public static final Atom XA_CUT_BUFFER0 = new Atom(9L);
    public static final Atom XA_CUT_BUFFER1 = new Atom(10L);
    public static final Atom XA_CUT_BUFFER2 = new Atom(11L);
    public static final Atom XA_CUT_BUFFER3 = new Atom(12L);
    public static final Atom XA_CUT_BUFFER4 = new Atom(13L);
    public static final Atom XA_CUT_BUFFER5 = new Atom(14L);
    public static final Atom XA_CUT_BUFFER6 = new Atom(15L);
    public static final Atom XA_CUT_BUFFER7 = new Atom(16L);
    public static final Atom XA_DRAWABLE = new Atom(17L);
    public static final Atom XA_FONT = new Atom(18L);
    public static final Atom XA_INTEGER = new Atom(19L);
    public static final Atom XA_PIXMAP = new Atom(20L);
    public static final Atom XA_POINT = new Atom(21L);
    public static final Atom XA_RECTANGLE = new Atom(22L);
    public static final Atom XA_RESOURCE_MANAGER = new Atom(23L);
    public static final Atom XA_RGB_COLOR_MAP = new Atom(24L);
    public static final Atom XA_RGB_BEST_MAP = new Atom(25L);
    public static final Atom XA_RGB_BLUE_MAP = new Atom(26L);
    public static final Atom XA_RGB_DEFAULT_MAP = new Atom(27L);
    public static final Atom XA_RGB_GRAY_MAP = new Atom(28L);
    public static final Atom XA_RGB_GREEN_MAP = new Atom(29L);
    public static final Atom XA_RGB_RED_MAP = new Atom(30L);
    public static final Atom XA_STRING = new Atom(31L);
    public static final Atom XA_VISUALID = new Atom(32L);
    public static final Atom XA_WINDOW = new Atom(33L);
    public static final Atom XA_WM_COMMAND = new Atom(34L);
    public static final Atom XA_WM_HINTS = new Atom(35L);
    public static final Atom XA_WM_CLIENT_MACHINE = new Atom(36L);
    public static final Atom XA_WM_ICON_NAME = new Atom(37L);
    public static final Atom XA_WM_ICON_SIZE = new Atom(38L);
    public static final Atom XA_WM_NAME = new Atom(39L);
    public static final Atom XA_WM_NORMAL_HINTS = new Atom(40L);
    public static final Atom XA_WM_SIZE_HINTS = new Atom(41L);
    public static final Atom XA_WM_ZOOM_HINTS = new Atom(42L);
    public static final Atom XA_MIN_SPACE = new Atom(43L);
    public static final Atom XA_NORM_SPACE = new Atom(44L);
    public static final Atom XA_MAX_SPACE = new Atom(45L);
    public static final Atom XA_END_SPACE = new Atom(46L);
    public static final Atom XA_SUPERSCRIPT_X = new Atom(47L);
    public static final Atom XA_SUPERSCRIPT_Y = new Atom(48L);
    public static final Atom XA_SUBSCRIPT_X = new Atom(49L);
    public static final Atom XA_SUBSCRIPT_Y = new Atom(50L);
    public static final Atom XA_UNDERLINE_POSITION = new Atom(51L);
    public static final Atom XA_UNDERLINE_THICKNESS = new Atom(52L);
    public static final Atom XA_STRIKEOUT_ASCENT = new Atom(53L);
    public static final Atom XA_STRIKEOUT_DESCENT = new Atom(54L);
    public static final Atom XA_ITALIC_ANGLE = new Atom(55L);
    public static final Atom XA_X_HEIGHT = new Atom(56L);
    public static final Atom XA_QUAD_WIDTH = new Atom(57L);
    public static final Atom XA_WEIGHT = new Atom(58L);
    public static final Atom XA_POINT_SIZE = new Atom(59L);
    public static final Atom XA_RESOLUTION = new Atom(60L);
    public static final Atom XA_COPYRIGHT = new Atom(61L);
    public static final Atom XA_NOTICE = new Atom(62L);
    public static final Atom XA_FONT_NAME = new Atom(63L);
    public static final Atom XA_FAMILY_NAME = new Atom(64L);
    public static final Atom XA_FULL_NAME = new Atom(65L);
    public static final Atom XA_CAP_HEIGHT = new Atom(66L);
    public static final Atom XA_WM_CLASS = new Atom(67L);
    public static final Atom XA_WM_TRANSIENT_FOR = new Atom(68L);
    public static final Atom XA_LAST_PREDEFINED = X11.XA_WM_TRANSIENT_FOR;
    public static final int None = 0;
    public static final int ParentRelative = 1;
    public static final int CopyFromParent = 0;
    public static final int PointerWindow = 0;
    public static final int InputFocus = 1;
    public static final int PointerRoot = 1;
    public static final int AnyPropertyType = 0;
    public static final int AnyKey = 0;
    public static final int AnyButton = 0;
    public static final int AllTemporary = 0;
    public static final int CurrentTime = 0;
    public static final int NoSymbol = 0;
    public static final int NoEventMask = 0;
    public static final int KeyPressMask = 1;
    public static final int KeyReleaseMask = 2;
    public static final int ButtonPressMask = 4;
    public static final int ButtonReleaseMask = 8;
    public static final int EnterWindowMask = 16;
    public static final int LeaveWindowMask = 32;
    public static final int PointerMotionMask = 64;
    public static final int PointerMotionHintMask = 128;
    public static final int Button1MotionMask = 256;
    public static final int Button2MotionMask = 512;
    public static final int Button3MotionMask = 1024;
    public static final int Button4MotionMask = 2048;
    public static final int Button5MotionMask = 4096;
    public static final int ButtonMotionMask = 8192;
    public static final int KeymapStateMask = 16384;
    public static final int ExposureMask = 32768;
    public static final int VisibilityChangeMask = 65536;
    public static final int StructureNotifyMask = 131072;
    public static final int ResizeRedirectMask = 262144;
    public static final int SubstructureNotifyMask = 524288;
    public static final int SubstructureRedirectMask = 1048576;
    public static final int FocusChangeMask = 2097152;
    public static final int PropertyChangeMask = 4194304;
    public static final int ColormapChangeMask = 8388608;
    public static final int OwnerGrabButtonMask = 16777216;
    public static final int KeyPress = 2;
    public static final int KeyRelease = 3;
    public static final int ButtonPress = 4;
    public static final int ButtonRelease = 5;
    public static final int MotionNotify = 6;
    public static final int EnterNotify = 7;
    public static final int LeaveNotify = 8;
    public static final int FocusIn = 9;
    public static final int FocusOut = 10;
    public static final int KeymapNotify = 11;
    public static final int Expose = 12;
    public static final int GraphicsExpose = 13;
    public static final int NoExpose = 14;
    public static final int VisibilityNotify = 15;
    public static final int CreateNotify = 16;
    public static final int DestroyNotify = 17;
    public static final int UnmapNotify = 18;
    public static final int MapNotify = 19;
    public static final int MapRequest = 20;
    public static final int ReparentNotify = 21;
    public static final int ConfigureNotify = 22;
    public static final int ConfigureRequest = 23;
    public static final int GravityNotify = 24;
    public static final int ResizeRequest = 25;
    public static final int CirculateNotify = 26;
    public static final int CirculateRequest = 27;
    public static final int PropertyNotify = 28;
    public static final int SelectionClear = 29;
    public static final int SelectionRequest = 30;
    public static final int SelectionNotify = 31;
    public static final int ColormapNotify = 32;
    public static final int ClientMessage = 33;
    public static final int MappingNotify = 34;
    public static final int LASTEvent = 35;
    public static final int ShiftMask = 1;
    public static final int LockMask = 2;
    public static final int ControlMask = 4;
    public static final int Mod1Mask = 8;
    public static final int Mod2Mask = 16;
    public static final int Mod3Mask = 32;
    public static final int Mod4Mask = 64;
    public static final int Mod5Mask = 128;
    public static final int ShiftMapIndex = 0;
    public static final int LockMapIndex = 1;
    public static final int ControlMapIndex = 2;
    public static final int Mod1MapIndex = 3;
    public static final int Mod2MapIndex = 4;
    public static final int Mod3MapIndex = 5;
    public static final int Mod4MapIndex = 6;
    public static final int Mod5MapIndex = 7;
    public static final int Button1Mask = 256;
    public static final int Button2Mask = 512;
    public static final int Button3Mask = 1024;
    public static final int Button4Mask = 2048;
    public static final int Button5Mask = 4096;
    public static final int AnyModifier = 32768;
    public static final int Button1 = 1;
    public static final int Button2 = 2;
    public static final int Button3 = 3;
    public static final int Button4 = 4;
    public static final int Button5 = 5;
    public static final int NotifyNormal = 0;
    public static final int NotifyGrab = 1;
    public static final int NotifyUngrab = 2;
    public static final int NotifyWhileGrabbed = 3;
    public static final int NotifyHint = 1;
    public static final int NotifyAncestor = 0;
    public static final int NotifyVirtual = 1;
    public static final int NotifyInferior = 2;
    public static final int NotifyNonlinear = 3;
    public static final int NotifyNonlinearVirtual = 4;
    public static final int NotifyPointer = 5;
    public static final int NotifyPointerRoot = 6;
    public static final int NotifyDetailNone = 7;
    public static final int VisibilityUnobscured = 0;
    public static final int VisibilityPartiallyObscured = 1;
    public static final int VisibilityFullyObscured = 2;
    public static final int PlaceOnTop = 0;
    public static final int PlaceOnBottom = 1;
    public static final int FamilyInternet = 0;
    public static final int FamilyDECnet = 1;
    public static final int FamilyChaos = 2;
    public static final int FamilyInternet6 = 6;
    public static final int FamilyServerInterpreted = 5;
    public static final int PropertyNewValue = 0;
    public static final int PropertyDelete = 1;
    public static final int ColormapUninstalled = 0;
    public static final int ColormapInstalled = 1;
    public static final int GrabModeSync = 0;
    public static final int GrabModeAsync = 1;
    public static final int GrabSuccess = 0;
    public static final int AlreadyGrabbed = 1;
    public static final int GrabInvalidTime = 2;
    public static final int GrabNotViewable = 3;
    public static final int GrabFrozen = 4;
    public static final int AsyncPointer = 0;
    public static final int SyncPointer = 1;
    public static final int ReplayPointer = 2;
    public static final int AsyncKeyboard = 3;
    public static final int SyncKeyboard = 4;
    public static final int ReplayKeyboard = 5;
    public static final int AsyncBoth = 6;
    public static final int SyncBoth = 7;
    public static final int RevertToNone = 0;
    public static final int RevertToPointerRoot = 1;
    public static final int RevertToParent = 2;
    public static final int Success = 0;
    public static final int BadRequest = 1;
    public static final int BadValue = 2;
    public static final int BadWindow = 3;
    public static final int BadPixmap = 4;
    public static final int BadAtom = 5;
    public static final int BadCursor = 6;
    public static final int BadFont = 7;
    public static final int BadMatch = 8;
    public static final int BadDrawable = 9;
    public static final int BadAccess = 10;
    public static final int BadAlloc = 11;
    public static final int BadColor = 12;
    public static final int BadGC = 13;
    public static final int BadIDChoice = 14;
    public static final int BadName = 15;
    public static final int BadLength = 16;
    public static final int BadImplementation = 17;
    public static final int FirstExtensionError = 128;
    public static final int LastExtensionError = 255;
    public static final int InputOutput = 1;
    public static final int InputOnly = 2;
    public static final int CWBackPixmap = 1;
    public static final int CWBackPixel = 2;
    public static final int CWBorderPixmap = 4;
    public static final int CWBorderPixel = 8;
    public static final int CWBitGravity = 16;
    public static final int CWWinGravity = 32;
    public static final int CWBackingStore = 64;
    public static final int CWBackingPlanes = 128;
    public static final int CWBackingPixel = 256;
    public static final int CWOverrideRedirect = 512;
    public static final int CWSaveUnder = 1024;
    public static final int CWEventMask = 2048;
    public static final int CWDontPropagate = 4096;
    public static final int CWColormap = 8192;
    public static final int CWCursor = 16384;
    public static final int CWX = 1;
    public static final int CWY = 2;
    public static final int CWWidth = 4;
    public static final int CWHeight = 8;
    public static final int CWBorderWidth = 16;
    public static final int CWSibling = 32;
    public static final int CWStackMode = 64;
    public static final int ForgetGravity = 0;
    public static final int NorthWestGravity = 1;
    public static final int NorthGravity = 2;
    public static final int NorthEastGravity = 3;
    public static final int WestGravity = 4;
    public static final int CenterGravity = 5;
    public static final int EastGravity = 6;
    public static final int SouthWestGravity = 7;
    public static final int SouthGravity = 8;
    public static final int SouthEastGravity = 9;
    public static final int StaticGravity = 10;
    public static final int UnmapGravity = 0;
    public static final int NotUseful = 0;
    public static final int WhenMapped = 1;
    public static final int Always = 2;
    public static final int IsUnmapped = 0;
    public static final int IsUnviewable = 1;
    public static final int IsViewable = 2;
    public static final int SetModeInsert = 0;
    public static final int SetModeDelete = 1;
    public static final int DestroyAll = 0;
    public static final int RetainPermanent = 1;
    public static final int RetainTemporary = 2;
    public static final int Above = 0;
    public static final int Below = 1;
    public static final int TopIf = 2;
    public static final int BottomIf = 3;
    public static final int Opposite = 4;
    public static final int RaiseLowest = 0;
    public static final int LowerHighest = 1;
    public static final int PropModeReplace = 0;
    public static final int PropModePrepend = 1;
    public static final int PropModeAppend = 2;
    public static final int GXclear = 0;
    public static final int GXand = 1;
    public static final int GXandReverse = 2;
    public static final int GXcopy = 3;
    public static final int GXandInverted = 4;
    public static final int GXnoop = 5;
    public static final int GXxor = 6;
    public static final int GXor = 7;
    public static final int GXnor = 8;
    public static final int GXequiv = 9;
    public static final int GXinvert = 10;
    public static final int GXorReverse = 11;
    public static final int GXcopyInverted = 12;
    public static final int GXorInverted = 13;
    public static final int GXnand = 14;
    public static final int GXset = 15;
    public static final int LineSolid = 0;
    public static final int LineOnOffDash = 1;
    public static final int LineDoubleDash = 2;
    public static final int CapNotLast = 0;
    public static final int CapButt = 1;
    public static final int CapRound = 2;
    public static final int CapProjecting = 3;
    public static final int JoinMiter = 0;
    public static final int JoinRound = 1;
    public static final int JoinBevel = 2;
    public static final int FillSolid = 0;
    public static final int FillTiled = 1;
    public static final int FillStippled = 2;
    public static final int FillOpaqueStippled = 3;
    public static final int EvenOddRule = 0;
    public static final int WindingRule = 1;
    public static final int ClipByChildren = 0;
    public static final int IncludeInferiors = 1;
    public static final int Unsorted = 0;
    public static final int YSorted = 1;
    public static final int YXSorted = 2;
    public static final int YXBanded = 3;
    public static final int CoordModeOrigin = 0;
    public static final int CoordModePrevious = 1;
    public static final int Complex = 0;
    public static final int Nonconvex = 1;
    public static final int Convex = 2;
    public static final int ArcChord = 0;
    public static final int ArcPieSlice = 1;
    public static final int GCFunction = 1;
    public static final int GCPlaneMask = 2;
    public static final int GCForeground = 4;
    public static final int GCBackground = 8;
    public static final int GCLineWidth = 16;
    public static final int GCLineStyle = 32;
    public static final int GCCapStyle = 64;
    public static final int GCJoinStyle = 128;
    public static final int GCFillStyle = 256;
    public static final int GCFillRule = 512;
    public static final int GCTile = 1024;
    public static final int GCStipple = 2048;
    public static final int GCTileStipXOrigin = 4096;
    public static final int GCTileStipYOrigin = 8192;
    public static final int GCFont = 16384;
    public static final int GCSubwindowMode = 32768;
    public static final int GCGraphicsExposures = 65536;
    public static final int GCClipXOrigin = 131072;
    public static final int GCClipYOrigin = 262144;
    public static final int GCClipMask = 524288;
    public static final int GCDashOffset = 1048576;
    public static final int GCDashList = 2097152;
    public static final int GCArcMode = 4194304;
    public static final int GCLastBit = 22;
    public static final int FontLeftToRight = 0;
    public static final int FontRightToLeft = 1;
    public static final int FontChange = 255;
    public static final int XYBitmap = 0;
    public static final int XYPixmap = 1;
    public static final int ZPixmap = 2;
    public static final int AllocNone = 0;
    public static final int AllocAll = 1;
    public static final int DoRed = 1;
    public static final int DoGreen = 2;
    public static final int DoBlue = 4;
    public static final int CursorShape = 0;
    public static final int TileShape = 1;
    public static final int StippleShape = 2;
    public static final int AutoRepeatModeOff = 0;
    public static final int AutoRepeatModeOn = 1;
    public static final int AutoRepeatModeDefault = 2;
    public static final int LedModeOff = 0;
    public static final int LedModeOn = 1;
    public static final int KBKeyClickPercent = 1;
    public static final int KBBellPercent = 2;
    public static final int KBBellPitch = 4;
    public static final int KBBellDuration = 8;
    public static final int KBLed = 16;
    public static final int KBLedMode = 32;
    public static final int KBKey = 64;
    public static final int KBAutoRepeatMode = 128;
    public static final int MappingSuccess = 0;
    public static final int MappingBusy = 1;
    public static final int MappingFailed = 2;
    public static final int MappingModifier = 0;
    public static final int MappingKeyboard = 1;
    public static final int MappingPointer = 2;
    public static final int DontPreferBlanking = 0;
    public static final int PreferBlanking = 1;
    public static final int DefaultBlanking = 2;
    public static final int DisableScreenSaver = 0;
    public static final int DisableScreenInterval = 0;
    public static final int DontAllowExposures = 0;
    public static final int AllowExposures = 1;
    public static final int DefaultExposures = 2;
    public static final int ScreenSaverReset = 0;
    public static final int ScreenSaverActive = 1;
    public static final int HostInsert = 0;
    public static final int HostDelete = 1;
    public static final int EnableAccess = 1;
    public static final int DisableAccess = 0;
    public static final int StaticGray = 0;
    public static final int GrayScale = 1;
    public static final int StaticColor = 2;
    public static final int PseudoColor = 3;
    public static final int TrueColor = 4;
    public static final int DirectColor = 5;
    public static final int LSBFirst = 0;
    public static final int MSBFirst = 1;
    
    Display XOpenDisplay(final String p0);
    
    int XGetErrorText(final Display p0, final int p1, final byte[] p2, final int p3);
    
    int XDefaultScreen(final Display p0);
    
    Screen DefaultScreenOfDisplay(final Display p0);
    
    Visual XDefaultVisual(final Display p0, final int p1);
    
    Colormap XDefaultColormap(final Display p0, final int p1);
    
    int XDisplayWidth(final Display p0, final int p1);
    
    int XDisplayHeight(final Display p0, final int p1);
    
    Window XDefaultRootWindow(final Display p0);
    
    Window XRootWindow(final Display p0, final int p1);
    
    int XAllocNamedColor(final Display p0, final int p1, final String p2, final Pointer p3, final Pointer p4);
    
    XSizeHints XAllocSizeHints();
    
    void XSetWMProperties(final Display p0, final Window p1, final String p2, final String p3, final String[] p4, final int p5, final XSizeHints p6, final Pointer p7, final Pointer p8);
    
    int XFree(final Pointer p0);
    
    Window XCreateSimpleWindow(final Display p0, final Window p1, final int p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8);
    
    Pixmap XCreateBitmapFromData(final Display p0, final Window p1, final Pointer p2, final int p3, final int p4);
    
    int XMapWindow(final Display p0, final Window p1);
    
    int XMapRaised(final Display p0, final Window p1);
    
    int XMapSubwindows(final Display p0, final Window p1);
    
    int XFlush(final Display p0);
    
    int XSync(final Display p0, final boolean p1);
    
    int XEventsQueued(final Display p0, final int p1);
    
    int XPending(final Display p0);
    
    int XUnmapWindow(final Display p0, final Window p1);
    
    int XDestroyWindow(final Display p0, final Window p1);
    
    int XCloseDisplay(final Display p0);
    
    int XClearWindow(final Display p0, final Window p1);
    
    int XClearArea(final Display p0, final Window p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    Pixmap XCreatePixmap(final Display p0, final Drawable p1, final int p2, final int p3, final int p4);
    
    int XFreePixmap(final Display p0, final Pixmap p1);
    
    GC XCreateGC(final Display p0, final Drawable p1, final NativeLong p2, final XGCValues p3);
    
    int XSetFillRule(final Display p0, final GC p1, final int p2);
    
    int XFreeGC(final Display p0, final GC p1);
    
    int XDrawPoint(final Display p0, final Drawable p1, final GC p2, final int p3, final int p4);
    
    int XDrawPoints(final Display p0, final Drawable p1, final GC p2, final XPoint[] p3, final int p4, final int p5);
    
    int XFillRectangle(final Display p0, final Drawable p1, final GC p2, final int p3, final int p4, final int p5, final int p6);
    
    int XFillRectangles(final Display p0, final Drawable p1, final GC p2, final XRectangle[] p3, final int p4);
    
    int XSetForeground(final Display p0, final GC p1, final NativeLong p2);
    
    int XSetBackground(final Display p0, final GC p1, final NativeLong p2);
    
    int XFillArc(final Display p0, final Drawable p1, final GC p2, final int p3, final int p4, final int p5, final int p6, final int p7, final int p8);
    
    int XFillPolygon(final Display p0, final Drawable p1, final GC p2, final XPoint[] p3, final int p4, final int p5, final int p6);
    
    int XQueryTree(final Display p0, final Window p1, final WindowByReference p2, final WindowByReference p3, final PointerByReference p4, final IntByReference p5);
    
    boolean XQueryPointer(final Display p0, final Window p1, final WindowByReference p2, final WindowByReference p3, final IntByReference p4, final IntByReference p5, final IntByReference p6, final IntByReference p7, final IntByReference p8);
    
    int XGetWindowAttributes(final Display p0, final Window p1, final XWindowAttributes p2);
    
    int XChangeWindowAttributes(final Display p0, final Window p1, final NativeLong p2, final XSetWindowAttributes p3);
    
    int XGetGeometry(final Display p0, final Drawable p1, final WindowByReference p2, final IntByReference p3, final IntByReference p4, final IntByReference p5, final IntByReference p6, final IntByReference p7, final IntByReference p8);
    
    boolean XTranslateCoordinates(final Display p0, final Window p1, final Window p2, final int p3, final int p4, final IntByReference p5, final IntByReference p6, final WindowByReference p7);
    
    int XSelectInput(final Display p0, final Window p1, final NativeLong p2);
    
    int XSendEvent(final Display p0, final Window p1, final int p2, final NativeLong p3, final XEvent p4);
    
    int XNextEvent(final Display p0, final XEvent p1);
    
    int XPeekEvent(final Display p0, final XEvent p1);
    
    int XWindowEvent(final Display p0, final Window p1, final NativeLong p2, final XEvent p3);
    
    boolean XCheckWindowEvent(final Display p0, final Window p1, final NativeLong p2, final XEvent p3);
    
    int XMaskEvent(final Display p0, final NativeLong p1, final XEvent p2);
    
    boolean XCheckMaskEvent(final Display p0, final NativeLong p1, final XEvent p2);
    
    boolean XCheckTypedEvent(final Display p0, final int p1, final XEvent p2);
    
    boolean XCheckTypedWindowEvent(final Display p0, final Window p1, final int p2, final XEvent p3);
    
    XWMHints XGetWMHints(final Display p0, final Window p1);
    
    int XGetWMName(final Display p0, final Window p1, final XTextProperty p2);
    
    XVisualInfo XGetVisualInfo(final Display p0, final NativeLong p1, final XVisualInfo p2, final IntByReference p3);
    
    Colormap XCreateColormap(final Display p0, final Window p1, final Visual p2, final int p3);
    
    int XGetWindowProperty(final Display p0, final Window p1, final Atom p2, final NativeLong p3, final NativeLong p4, final boolean p5, final Atom p6, final AtomByReference p7, final IntByReference p8, final NativeLongByReference p9, final NativeLongByReference p10, final PointerByReference p11);
    
    int XChangeProperty(final Display p0, final Window p1, final Atom p2, final Atom p3, final int p4, final int p5, final Pointer p6, final int p7);
    
    int XDeleteProperty(final Display p0, final Window p1, final Atom p2);
    
    Atom XInternAtom(final Display p0, final String p1, final boolean p2);
    
    String XGetAtomName(final Display p0, final Atom p1);
    
    int XCopyArea(final Display p0, final Drawable p1, final Drawable p2, final GC p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9);
    
    XImage XCreateImage(final Display p0, final Visual p1, final int p2, final int p3, final int p4, final Pointer p5, final int p6, final int p7, final int p8, final int p9);
    
    int XPutImage(final Display p0, final Drawable p1, final GC p2, final XImage p3, final int p4, final int p5, final int p6, final int p7, final int p8, final int p9);
    
    int XDestroyImage(final XImage p0);
    
    XErrorHandler XSetErrorHandler(final XErrorHandler p0);
    
    String XKeysymToString(final KeySym p0);
    
    KeySym XStringToKeysym(final String p0);
    
    byte XKeysymToKeycode(final Display p0, final KeySym p1);
    
    KeySym XKeycodeToKeysym(final Display p0, final byte p1, final int p2);
    
    int XGrabKey(final Display p0, final int p1, final int p2, final Window p3, final int p4, final int p5, final int p6);
    
    int XUngrabKey(final Display p0, final int p1, final int p2, final Window p3);
    
    int XChangeKeyboardMapping(final Display p0, final int p1, final int p2, final KeySym[] p3, final int p4);
    
    KeySym XGetKeyboardMapping(final Display p0, final byte p1, final int p2, final IntByReference p3);
    
    int XDisplayKeycodes(final Display p0, final IntByReference p1, final IntByReference p2);
    
    int XSetModifierMapping(final Display p0, final XModifierKeymapRef p1);
    
    XModifierKeymapRef XGetModifierMapping(final Display p0);
    
    XModifierKeymapRef XNewModifiermap(final int p0);
    
    XModifierKeymapRef XInsertModifiermapEntry(final XModifierKeymapRef p0, final byte p1, final int p2);
    
    XModifierKeymapRef XDeleteModifiermapEntry(final XModifierKeymapRef p0, final byte p1, final int p2);
    
    int XFreeModifiermap(final XModifierKeymapRef p0);
    
    int XChangeKeyboardControl(final Display p0, final NativeLong p1, final XKeyboardControlRef p2);
    
    int XGetKeyboardControl(final Display p0, final XKeyboardStateRef p1);
    
    int XAutoRepeatOn(final Display p0);
    
    int XAutoRepeatOff(final Display p0);
    
    int XBell(final Display p0, final int p1);
    
    int XQueryKeymap(final Display p0, final byte[] p1);
    
    public static class VisualID extends NativeLong
    {
        private static final long serialVersionUID = 1L;
        
        public VisualID() {
        }
        
        public VisualID(final long value) {
            super(value);
        }
    }
    
    public static class XID extends NativeLong
    {
        private static final long serialVersionUID = 1L;
        public static final XID None;
        
        public XID() {
            this(0L);
        }
        
        public XID(final long id) {
            super(id);
        }
        
        protected boolean isNone(final Object o) {
            return o == null || (o instanceof Number && ((Number)o).longValue() == 0L);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return XID.None;
            }
            return new XID(((Number)nativeValue).longValue());
        }
        
        @Override
        public String toString() {
            return "0x" + Long.toHexString(this.longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class Atom extends XID
    {
        private static final long serialVersionUID = 1L;
        public static final Atom None;
        
        public Atom() {
        }
        
        public Atom(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            final long value = ((Number)nativeValue).longValue();
            if (value <= 2147483647L) {
                switch ((int)value) {
                    case 0: {
                        return Atom.None;
                    }
                    case 1: {
                        return X11.XA_PRIMARY;
                    }
                    case 2: {
                        return X11.XA_SECONDARY;
                    }
                    case 3: {
                        return X11.XA_ARC;
                    }
                    case 4: {
                        return X11.XA_ATOM;
                    }
                    case 5: {
                        return X11.XA_BITMAP;
                    }
                    case 6: {
                        return X11.XA_CARDINAL;
                    }
                    case 7: {
                        return X11.XA_COLORMAP;
                    }
                    case 8: {
                        return X11.XA_CURSOR;
                    }
                    case 9: {
                        return X11.XA_CUT_BUFFER0;
                    }
                    case 10: {
                        return X11.XA_CUT_BUFFER1;
                    }
                    case 11: {
                        return X11.XA_CUT_BUFFER2;
                    }
                    case 12: {
                        return X11.XA_CUT_BUFFER3;
                    }
                    case 13: {
                        return X11.XA_CUT_BUFFER4;
                    }
                    case 14: {
                        return X11.XA_CUT_BUFFER5;
                    }
                    case 15: {
                        return X11.XA_CUT_BUFFER6;
                    }
                    case 16: {
                        return X11.XA_CUT_BUFFER7;
                    }
                    case 17: {
                        return X11.XA_DRAWABLE;
                    }
                    case 18: {
                        return X11.XA_FONT;
                    }
                    case 19: {
                        return X11.XA_INTEGER;
                    }
                    case 20: {
                        return X11.XA_PIXMAP;
                    }
                    case 21: {
                        return X11.XA_POINT;
                    }
                    case 22: {
                        return X11.XA_RECTANGLE;
                    }
                    case 23: {
                        return X11.XA_RESOURCE_MANAGER;
                    }
                    case 24: {
                        return X11.XA_RGB_COLOR_MAP;
                    }
                    case 25: {
                        return X11.XA_RGB_BEST_MAP;
                    }
                    case 26: {
                        return X11.XA_RGB_BLUE_MAP;
                    }
                    case 27: {
                        return X11.XA_RGB_DEFAULT_MAP;
                    }
                    case 28: {
                        return X11.XA_RGB_GRAY_MAP;
                    }
                    case 29: {
                        return X11.XA_RGB_GREEN_MAP;
                    }
                    case 30: {
                        return X11.XA_RGB_RED_MAP;
                    }
                    case 31: {
                        return X11.XA_STRING;
                    }
                    case 32: {
                        return X11.XA_VISUALID;
                    }
                    case 33: {
                        return X11.XA_WINDOW;
                    }
                    case 34: {
                        return X11.XA_WM_COMMAND;
                    }
                    case 35: {
                        return X11.XA_WM_HINTS;
                    }
                    case 36: {
                        return X11.XA_WM_CLIENT_MACHINE;
                    }
                    case 37: {
                        return X11.XA_WM_ICON_NAME;
                    }
                    case 38: {
                        return X11.XA_WM_ICON_SIZE;
                    }
                    case 39: {
                        return X11.XA_WM_NAME;
                    }
                    case 40: {
                        return X11.XA_WM_NORMAL_HINTS;
                    }
                    case 41: {
                        return X11.XA_WM_SIZE_HINTS;
                    }
                    case 42: {
                        return X11.XA_WM_ZOOM_HINTS;
                    }
                    case 43: {
                        return X11.XA_MIN_SPACE;
                    }
                    case 44: {
                        return X11.XA_NORM_SPACE;
                    }
                    case 45: {
                        return X11.XA_MAX_SPACE;
                    }
                    case 46: {
                        return X11.XA_END_SPACE;
                    }
                    case 47: {
                        return X11.XA_SUPERSCRIPT_X;
                    }
                    case 48: {
                        return X11.XA_SUPERSCRIPT_Y;
                    }
                    case 49: {
                        return X11.XA_SUBSCRIPT_X;
                    }
                    case 50: {
                        return X11.XA_SUBSCRIPT_Y;
                    }
                    case 51: {
                        return X11.XA_UNDERLINE_POSITION;
                    }
                    case 52: {
                        return X11.XA_UNDERLINE_THICKNESS;
                    }
                    case 53: {
                        return X11.XA_STRIKEOUT_ASCENT;
                    }
                    case 54: {
                        return X11.XA_STRIKEOUT_DESCENT;
                    }
                    case 55: {
                        return X11.XA_ITALIC_ANGLE;
                    }
                    case 56: {
                        return X11.XA_X_HEIGHT;
                    }
                    case 57: {
                        return X11.XA_QUAD_WIDTH;
                    }
                    case 58: {
                        return X11.XA_WEIGHT;
                    }
                    case 59: {
                        return X11.XA_POINT_SIZE;
                    }
                    case 60: {
                        return X11.XA_RESOLUTION;
                    }
                    case 61: {
                        return X11.XA_COPYRIGHT;
                    }
                    case 62: {
                        return X11.XA_NOTICE;
                    }
                    case 63: {
                        return X11.XA_FONT_NAME;
                    }
                    case 64: {
                        return X11.XA_FAMILY_NAME;
                    }
                    case 65: {
                        return X11.XA_FULL_NAME;
                    }
                    case 66: {
                        return X11.XA_CAP_HEIGHT;
                    }
                    case 67: {
                        return X11.XA_WM_CLASS;
                    }
                    case 68: {
                        return X11.XA_WM_TRANSIENT_FOR;
                    }
                }
            }
            return new Atom(value);
        }
        
        static {
            None = null;
        }
    }
    
    public static class AtomByReference extends ByReference
    {
        public AtomByReference() {
            super(XID.SIZE);
        }
        
        public Atom getValue() {
            final NativeLong value = this.getPointer().getNativeLong(0L);
            return (Atom)new Atom().fromNative(value, null);
        }
    }
    
    public static class Colormap extends XID
    {
        private static final long serialVersionUID = 1L;
        public static final Colormap None;
        
        public Colormap() {
        }
        
        public Colormap(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return Colormap.None;
            }
            return new Colormap(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class Font extends XID
    {
        private static final long serialVersionUID = 1L;
        public static final Font None;
        
        public Font() {
        }
        
        public Font(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return Font.None;
            }
            return new Font(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class Cursor extends XID
    {
        private static final long serialVersionUID = 1L;
        public static final Cursor None;
        
        public Cursor() {
        }
        
        public Cursor(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return Cursor.None;
            }
            return new Cursor(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class KeySym extends XID
    {
        private static final long serialVersionUID = 1L;
        public static final KeySym None;
        
        public KeySym() {
        }
        
        public KeySym(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return KeySym.None;
            }
            return new KeySym(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class Drawable extends XID
    {
        private static final long serialVersionUID = 1L;
        public static final Drawable None;
        
        public Drawable() {
        }
        
        public Drawable(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return Drawable.None;
            }
            return new Drawable(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class Window extends Drawable
    {
        private static final long serialVersionUID = 1L;
        public static final Window None;
        
        public Window() {
        }
        
        public Window(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return Window.None;
            }
            return new Window(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class WindowByReference extends ByReference
    {
        public WindowByReference() {
            super(XID.SIZE);
        }
        
        public Window getValue() {
            final NativeLong value = this.getPointer().getNativeLong(0L);
            return (value.longValue() == 0L) ? Window.None : new Window(value.longValue());
        }
    }
    
    public static class Pixmap extends Drawable
    {
        private static final long serialVersionUID = 1L;
        public static final Pixmap None;
        
        public Pixmap() {
        }
        
        public Pixmap(final long id) {
            super(id);
        }
        
        @Override
        public Object fromNative(final Object nativeValue, final FromNativeContext context) {
            if (this.isNone(nativeValue)) {
                return Pixmap.None;
            }
            return new Pixmap(((Number)nativeValue).longValue());
        }
        
        static {
            None = null;
        }
    }
    
    public static class Display extends PointerType
    {
    }
    
    public static class Visual extends PointerType
    {
        public NativeLong getVisualID() {
            if (this.getPointer() != null) {
                return this.getPointer().getNativeLong(Native.POINTER_SIZE);
            }
            return new NativeLong(0L);
        }
        
        @Override
        public String toString() {
            return "Visual: VisualID=0x" + Long.toHexString(this.getVisualID().longValue());
        }
    }
    
    public static class Screen extends PointerType
    {
    }
    
    public static class GC extends PointerType
    {
    }
    
    public static class XImage extends PointerType
    {
    }
    
    public interface Xext extends Library
    {
        public static final Xext INSTANCE = (Xext)Native.loadLibrary("Xext", Xext.class);
        public static final int ShapeBounding = 0;
        public static final int ShapeClip = 1;
        public static final int ShapeInput = 2;
        public static final int ShapeSet = 0;
        public static final int ShapeUnion = 1;
        public static final int ShapeIntersect = 2;
        public static final int ShapeSubtract = 3;
        public static final int ShapeInvert = 4;
        
        void XShapeCombineMask(final Display p0, final Window p1, final int p2, final int p3, final int p4, final Pixmap p5, final int p6);
    }
    
    public interface Xrender extends Library
    {
        public static final Xrender INSTANCE = (Xrender)Native.loadLibrary("Xrender", Xrender.class);
        public static final int PictTypeIndexed = 0;
        public static final int PictTypeDirect = 1;
        
        XRenderPictFormat XRenderFindVisualFormat(final Display p0, final Visual p1);
        
        public static class XRenderDirectFormat extends Structure
        {
            public short red;
            public short redMask;
            public short green;
            public short greenMask;
            public short blue;
            public short blueMask;
            public short alpha;
            public short alphaMask;
        }
        
        public static class PictFormat extends NativeLong
        {
            private static final long serialVersionUID = 1L;
            
            public PictFormat(final long value) {
                super(value);
            }
            
            public PictFormat() {
            }
        }
        
        public static class XRenderPictFormat extends Structure
        {
            public PictFormat id;
            public int type;
            public int depth;
            public XRenderDirectFormat direct;
            public Colormap colormap;
        }
    }
    
    public interface Xevie extends Library
    {
        public static final Xevie INSTANCE = (Xevie)Native.loadLibrary("Xevie", Xevie.class);
        public static final int XEVIE_UNMODIFIED = 0;
        public static final int XEVIE_MODIFIED = 1;
        
        boolean XevieQueryVersion(final Display p0, final IntByReference p1, final IntByReference p2);
        
        int XevieStart(final Display p0);
        
        int XevieEnd(final Display p0);
        
        int XevieSendEvent(final Display p0, final XEvent p1, final int p2);
        
        int XevieSelectInput(final Display p0, final NativeLong p1);
    }
    
    public interface XTest extends Library
    {
        public static final XTest INSTANCE = (XTest)Native.loadLibrary("Xtst", XTest.class);
        
        boolean XTestQueryExtension(final Display p0, final IntByReference p1, final IntByReference p2, final IntByReference p3, final IntByReference p4);
        
        boolean XTestCompareCursorWithWindow(final Display p0, final Window p1, final Cursor p2);
        
        boolean XTestCompareCurrentCursorWithWindow(final Display p0, final Window p1);
        
        int XTestFakeKeyEvent(final Display p0, final int p1, final boolean p2, final NativeLong p3);
        
        int XTestFakeButtonEvent(final Display p0, final int p1, final boolean p2, final NativeLong p3);
        
        int XTestFakeMotionEvent(final Display p0, final int p1, final int p2, final int p3, final NativeLong p4);
        
        int XTestFakeRelativeMotionEvent(final Display p0, final int p1, final int p2, final NativeLong p3);
        
        int XTestFakeDeviceKeyEvent(final Display p0, final XDeviceByReference p1, final int p2, final boolean p3, final IntByReference p4, final int p5, final NativeLong p6);
        
        int XTestFakeDeviceButtonEvent(final Display p0, final XDeviceByReference p1, final int p2, final boolean p3, final IntByReference p4, final int p5, final NativeLong p6);
        
        int XTestFakeProximityEvent(final Display p0, final XDeviceByReference p1, final boolean p2, final IntByReference p3, final int p4, final NativeLong p5);
        
        int XTestFakeDeviceMotionEvent(final Display p0, final XDeviceByReference p1, final boolean p2, final int p3, final IntByReference p4, final int p5, final NativeLong p6);
        
        int XTestGrabControl(final Display p0, final boolean p1);
        
        void XTestSetVisualIDOfVisual(final Visual p0, final VisualID p1);
        
        int XTestDiscard(final Display p0);
    }
    
    public static class XInputClassInfoByReference extends Structure implements ByReference
    {
        public byte input_class;
        public byte event_type_base;
    }
    
    public static class XDeviceByReference extends Structure implements ByReference
    {
        public XID device_id;
        public int num_classes;
        public XInputClassInfoByReference classes;
    }
    
    public static class XWMHints extends Structure
    {
        public NativeLong flags;
        public boolean input;
        public int initial_state;
        public Pixmap icon_pixmap;
        public Window icon_window;
        public int icon_x;
        public int icon_y;
        public Pixmap icon_mask;
        public XID window_group;
    }
    
    public static class XTextProperty extends Structure
    {
        public String value;
        public Atom encoding;
        public int format;
        public NativeLong nitems;
    }
    
    public static class XSizeHints extends Structure
    {
        public NativeLong flags;
        public int x;
        public int y;
        public int width;
        public int height;
        public int min_width;
        public int min_height;
        public int max_width;
        public int max_height;
        public int width_inc;
        public int height_inc;
        public Aspect min_aspect;
        public Aspect max_aspect;
        public int base_width;
        public int base_height;
        public int win_gravity;
        
        public static class Aspect extends Structure
        {
            public int x;
            public int y;
        }
    }
    
    public static class XWindowAttributes extends Structure
    {
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public int depth;
        public Visual visual;
        public Window root;
        public int c_class;
        public int bit_gravity;
        public int win_gravity;
        public int backing_store;
        public NativeLong backing_planes;
        public NativeLong backing_pixel;
        public boolean save_under;
        public Colormap colormap;
        public boolean map_installed;
        public int map_state;
        public NativeLong all_event_masks;
        public NativeLong your_event_mask;
        public NativeLong do_not_propagate_mask;
        public boolean override_redirect;
        public Screen screen;
    }
    
    public static class XSetWindowAttributes extends Structure
    {
        public Pixmap background_pixmap;
        public NativeLong background_pixel;
        public Pixmap border_pixmap;
        public NativeLong border_pixel;
        public int bit_gravity;
        public int win_gravity;
        public int backing_store;
        public NativeLong backing_planes;
        public NativeLong backing_pixel;
        public boolean save_under;
        public NativeLong event_mask;
        public NativeLong do_not_propagate_mask;
        public boolean override_redirect;
        public Colormap colormap;
        public Cursor cursor;
    }
    
    public static class XVisualInfo extends Structure
    {
        public Visual visual;
        public VisualID visualid;
        public int screen;
        public int depth;
        public int c_class;
        public NativeLong red_mask;
        public NativeLong green_mask;
        public NativeLong blue_mask;
        public int colormap_size;
        public int bits_per_rgb;
    }
    
    public static class XPoint extends Structure
    {
        public short x;
        public short y;
        
        public XPoint() {
        }
        
        public XPoint(final short x, final short y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class XRectangle extends Structure
    {
        public short x;
        public short y;
        public short width;
        public short height;
        
        public XRectangle() {
        }
        
        public XRectangle(final short x, final short y, final short width, final short height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
    
    public static class XGCValues extends Structure
    {
        public int function;
        public NativeLong plane_mask;
        public NativeLong foreground;
        public NativeLong background;
        public int line_width;
        public int line_style;
        public int cap_style;
        public int join_style;
        public int fill_style;
        public int fill_rule;
        public int arc_mode;
        public Pixmap tile;
        public Pixmap stipple;
        public int ts_x_origin;
        public int ts_y_origin;
        public Font font;
        public int subwindow_mode;
        public boolean graphics_exposures;
        public int clip_x_origin;
        public int clip_y_origin;
        public Pixmap clip_mask;
        public int dash_offset;
        public byte dashes;
    }
    
    public static class XEvent extends Union
    {
        public int type;
        public XAnyEvent xany;
        public XKeyEvent xkey;
        public XButtonEvent xbutton;
        public XMotionEvent xmotion;
        public XCrossingEvent xcrossing;
        public XFocusChangeEvent xfocus;
        public XExposeEvent xexpose;
        public XGraphicsExposeEvent xgraphicsexpose;
        public XNoExposeEvent xnoexpose;
        public XVisibilityEvent xvisibility;
        public XCreateWindowEvent xcreatewindow;
        public XDestroyWindowEvent xdestroywindow;
        public XUnmapEvent xunmap;
        public XMapEvent xmap;
        public XMapRequestEvent xmaprequest;
        public XReparentEvent xreparent;
        public XConfigureEvent xconfigure;
        public XGravityEvent xgravity;
        public XResizeRequestEvent xresizerequest;
        public XConfigureRequestEvent xconfigurerequest;
        public XCirculateEvent xcirculate;
        public XCirculateRequestEvent xcirculaterequest;
        public XPropertyEvent xproperty;
        public XSelectionClearEvent xselectionclear;
        public XSelectionRequestEvent xselectionrequest;
        public XSelectionEvent xselection;
        public XColormapEvent xcolormap;
        public XClientMessageEvent xclient;
        public XMappingEvent xmapping;
        public XErrorEvent xerror;
        public XKeymapEvent xkeymap;
        public NativeLong[] pad;
        
        public XEvent() {
            this.pad = new NativeLong[24];
        }
    }
    
    public static class XAnyEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
    }
    
    public static class XKeyEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int state;
        public int keycode;
        public int same_screen;
    }
    
    public static class XButtonEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int state;
        public int button;
        public int same_screen;
    }
    
    public static class XButtonPressedEvent extends XButtonEvent
    {
    }
    
    public static class XButtonReleasedEvent extends XButtonEvent
    {
    }
    
    public static class XClientMessageEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Atom message_type;
        public int format;
        public Data data;
        
        public static class Data extends Union
        {
            public byte[] b;
            public short[] s;
            public NativeLong[] l;
            
            public Data() {
                this.b = new byte[20];
                this.s = new short[10];
                this.l = new NativeLong[5];
            }
        }
    }
    
    public static class XMotionEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int state;
        public byte is_hint;
        public int same_screen;
    }
    
    public static class XPointerMovedEvent extends XMotionEvent
    {
    }
    
    public static class XCrossingEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Window root;
        public Window subwindow;
        public NativeLong time;
        public int x;
        public int y;
        public int x_root;
        public int y_root;
        public int mode;
        public int detail;
        public int same_screen;
        public int focus;
        public int state;
    }
    
    public static class XEnterWindowEvent extends XCrossingEvent
    {
    }
    
    public static class XLeaveWindowEvent extends XCrossingEvent
    {
    }
    
    public static class XFocusChangeEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int mode;
        public int detail;
    }
    
    public static class XFocusInEvent extends XFocusChangeEvent
    {
    }
    
    public static class XFocusOutEvent extends XFocusChangeEvent
    {
    }
    
    public static class XExposeEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int count;
    }
    
    public static class XGraphicsExposeEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Drawable drawable;
        public int x;
        public int y;
        public int width;
        public int height;
        public int count;
        public int major_code;
        public int minor_code;
    }
    
    public static class XNoExposeEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Drawable drawable;
        public int major_code;
        public int minor_code;
    }
    
    public static class XVisibilityEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int state;
    }
    
    public static class XCreateWindowEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public int override_redirect;
    }
    
    public static class XDestroyWindowEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
    }
    
    public static class XUnmapEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int from_configure;
    }
    
    public static class XMapEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int override_redirect;
    }
    
    public static class XMapRequestEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
    }
    
    public static class XReparentEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public Window parent;
        public int x;
        public int y;
        public int override_redirect;
    }
    
    public static class XConfigureEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public Window above;
        public int override_redirect;
    }
    
    public static class XGravityEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int x;
        public int y;
    }
    
    public static class XResizeRequestEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int width;
        public int height;
    }
    
    public static class XConfigureRequestEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
        public int x;
        public int y;
        public int width;
        public int height;
        public int border_width;
        public Window above;
        public int detail;
        public NativeLong value_mask;
    }
    
    public static class XCirculateEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window event;
        public Window window;
        public int place;
    }
    
    public static class XCirculateRequestEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window parent;
        public Window window;
        public int place;
    }
    
    public static class XPropertyEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Atom atom;
        public NativeLong time;
        public int state;
    }
    
    public static class XSelectionClearEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Atom selection;
        public NativeLong time;
    }
    
    public static class XSelectionRequestEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window owner;
        public Window requestor;
        public Atom selection;
        public Atom target;
        Atom property;
        public NativeLong time;
    }
    
    public static class XSelectionEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window requestor;
        public Atom selection;
        public Atom target;
        public Atom property;
        public NativeLong time;
    }
    
    public static class XColormapEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public Colormap colormap;
        public int c_new;
        public int state;
    }
    
    public static class XMappingEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public int request;
        public int first_keycode;
        public int count;
    }
    
    public static class XErrorEvent extends Structure
    {
        public int type;
        public Display display;
        public XID resourceid;
        public NativeLong serial;
        public byte error_code;
        public byte request_code;
        public byte minor_code;
    }
    
    public static class XKeymapEvent extends Structure
    {
        public int type;
        public NativeLong serial;
        public int send_event;
        public Display display;
        public Window window;
        public byte[] key_vector;
        
        public XKeymapEvent() {
            this.key_vector = new byte[32];
        }
    }
    
    public static class XModifierKeymapRef extends Structure implements ByReference
    {
        public int max_keypermod;
        public Pointer modifiermap;
    }
    
    public static class XKeyboardControlRef extends Structure implements ByReference
    {
        public int key_click_percent;
        public int bell_percent;
        public int bell_pitch;
        public int bell_duration;
        public int led;
        public int led_mode;
        public int key;
        public int auto_repeat_mode;
        
        @Override
        public String toString() {
            return "XKeyboardControlByReference{key_click_percent=" + this.key_click_percent + ", bell_percent=" + this.bell_percent + ", bell_pitch=" + this.bell_pitch + ", bell_duration=" + this.bell_duration + ", led=" + this.led + ", led_mode=" + this.led_mode + ", key=" + this.key + ", auto_repeat_mode=" + this.auto_repeat_mode + '}';
        }
    }
    
    public static class XKeyboardStateRef extends Structure implements ByReference
    {
        public int key_click_percent;
        public int bell_percent;
        public int bell_pitch;
        public int bell_duration;
        public NativeLong led_mask;
        public int global_auto_repeat;
        public byte[] auto_repeats;
        
        public XKeyboardStateRef() {
            this.auto_repeats = new byte[32];
        }
        
        @Override
        public String toString() {
            return "XKeyboardStateByReference{key_click_percent=" + this.key_click_percent + ", bell_percent=" + this.bell_percent + ", bell_pitch=" + this.bell_pitch + ", bell_duration=" + this.bell_duration + ", led_mask=" + this.led_mask + ", global_auto_repeat=" + this.global_auto_repeat + ", auto_repeats=" + this.auto_repeats + '}';
        }
    }
    
    public interface XErrorHandler extends Callback
    {
        int apply(final Display p0, final XErrorEvent p1);
    }
}
