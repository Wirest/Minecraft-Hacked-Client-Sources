// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary, WinUser
{
    public static final User32 INSTANCE = (User32)Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
    
    WinDef.HDC GetDC(final WinDef.HWND p0);
    
    int ReleaseDC(final WinDef.HWND p0, final WinDef.HDC p1);
    
    WinDef.HWND FindWindow(final String p0, final String p1);
    
    int GetClassName(final WinDef.HWND p0, final char[] p1, final int p2);
    
    boolean GetGUIThreadInfo(final int p0, final GUITHREADINFO p1);
    
    boolean GetWindowInfo(final WinDef.HWND p0, final WINDOWINFO p1);
    
    boolean GetWindowRect(final WinDef.HWND p0, final WinDef.RECT p1);
    
    int GetWindowText(final WinDef.HWND p0, final char[] p1, final int p2);
    
    int GetWindowTextLength(final WinDef.HWND p0);
    
    int GetWindowModuleFileName(final WinDef.HWND p0, final char[] p1, final int p2);
    
    int GetWindowThreadProcessId(final WinDef.HWND p0, final IntByReference p1);
    
    boolean EnumWindows(final WNDENUMPROC p0, final Pointer p1);
    
    boolean EnumChildWindows(final WinDef.HWND p0, final WNDENUMPROC p1, final Pointer p2);
    
    boolean EnumThreadWindows(final int p0, final WNDENUMPROC p1, final Pointer p2);
    
    boolean FlashWindowEx(final FLASHWINFO p0);
    
    WinDef.HICON LoadIcon(final WinDef.HINSTANCE p0, final String p1);
    
    WinNT.HANDLE LoadImage(final WinDef.HINSTANCE p0, final String p1, final int p2, final int p3, final int p4, final int p5);
    
    boolean DestroyIcon(final WinDef.HICON p0);
    
    int GetWindowLong(final WinDef.HWND p0, final int p1);
    
    int SetWindowLong(final WinDef.HWND p0, final int p1, final int p2);
    
    Pointer SetWindowLong(final WinDef.HWND p0, final int p1, final Pointer p2);
    
    BaseTSD.LONG_PTR GetWindowLongPtr(final WinDef.HWND p0, final int p1);
    
    BaseTSD.LONG_PTR SetWindowLongPtr(final WinDef.HWND p0, final int p1, final BaseTSD.LONG_PTR p2);
    
    Pointer SetWindowLongPtr(final WinDef.HWND p0, final int p1, final Pointer p2);
    
    boolean SetLayeredWindowAttributes(final WinDef.HWND p0, final int p1, final byte p2, final int p3);
    
    boolean GetLayeredWindowAttributes(final WinDef.HWND p0, final IntByReference p1, final ByteByReference p2, final IntByReference p3);
    
    boolean UpdateLayeredWindow(final WinDef.HWND p0, final WinDef.HDC p1, final POINT p2, final SIZE p3, final WinDef.HDC p4, final POINT p5, final int p6, final BLENDFUNCTION p7, final int p8);
    
    int SetWindowRgn(final WinDef.HWND p0, final WinDef.HRGN p1, final boolean p2);
    
    boolean GetKeyboardState(final byte[] p0);
    
    short GetAsyncKeyState(final int p0);
    
    HHOOK SetWindowsHookEx(final int p0, final HOOKPROC p1, final WinDef.HINSTANCE p2, final int p3);
    
    WinDef.LRESULT CallNextHookEx(final HHOOK p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    WinDef.LRESULT CallNextHookEx(final HHOOK p0, final int p1, final WinDef.WPARAM p2, final Pointer p3);
    
    boolean UnhookWindowsHookEx(final HHOOK p0);
    
    int GetMessage(final MSG p0, final WinDef.HWND p1, final int p2, final int p3);
    
    boolean PeekMessage(final MSG p0, final WinDef.HWND p1, final int p2, final int p3, final int p4);
    
    boolean TranslateMessage(final MSG p0);
    
    WinDef.LRESULT DispatchMessage(final MSG p0);
    
    void PostMessage(final WinDef.HWND p0, final int p1, final WinDef.WPARAM p2, final WinDef.LPARAM p3);
    
    void PostQuitMessage(final int p0);
    
    int GetSystemMetrics(final int p0);
    
    WinDef.HWND SetParent(final WinDef.HWND p0, final WinDef.HWND p1);
    
    boolean IsWindowVisible(final WinDef.HWND p0);
    
    boolean MoveWindow(final WinDef.HWND p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    boolean SetWindowPos(final WinDef.HWND p0, final WinDef.HWND p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    boolean AttachThreadInput(final WinDef.DWORD p0, final WinDef.DWORD p1, final boolean p2);
    
    boolean SetForegroundWindow(final WinDef.HWND p0);
    
    WinDef.HWND GetForegroundWindow();
    
    WinDef.HWND SetFocus(final WinDef.HWND p0);
    
    WinDef.DWORD SendInput(final WinDef.DWORD p0, final INPUT[] p1, final int p2);
    
    WinDef.DWORD WaitForInputIdle(final WinNT.HANDLE p0, final WinDef.DWORD p1);
    
    boolean InvalidateRect(final WinDef.HWND p0, final Structure.ByReference p1, final boolean p2);
    
    boolean RedrawWindow(final WinDef.HWND p0, final Structure.ByReference p1, final WinDef.HRGN p2, final WinDef.DWORD p3);
    
    WinDef.HWND GetWindow(final WinDef.HWND p0, final WinDef.DWORD p1);
    
    boolean UpdateWindow(final WinDef.HWND p0);
    
    boolean ShowWindow(final WinDef.HWND p0, final int p1);
    
    boolean CloseWindow(final WinDef.HWND p0);
    
    boolean RegisterHotKey(final WinDef.HWND p0, final int p1, final int p2, final int p3);
    
    boolean UnregisterHotKey(final Pointer p0, final int p1);
}
