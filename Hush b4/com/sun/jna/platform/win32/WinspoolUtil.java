// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public abstract class WinspoolUtil
{
    public static Winspool.PRINTER_INFO_1[] getPrinterInfo1() {
        final IntByReference pcbNeeded = new IntByReference();
        final IntByReference pcReturned = new IntByReference();
        Winspool.INSTANCE.EnumPrinters(2, null, 1, null, 0, pcbNeeded, pcReturned);
        if (pcbNeeded.getValue() <= 0) {
            return new Winspool.PRINTER_INFO_1[0];
        }
        final Winspool.PRINTER_INFO_1 pPrinterEnum = new Winspool.PRINTER_INFO_1(pcbNeeded.getValue());
        if (!Winspool.INSTANCE.EnumPrinters(2, null, 1, pPrinterEnum.getPointer(), pcbNeeded.getValue(), pcbNeeded, pcReturned)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return (Winspool.PRINTER_INFO_1[])pPrinterEnum.toArray(pcReturned.getValue());
    }
    
    public static Winspool.PRINTER_INFO_4[] getPrinterInfo4() {
        final IntByReference pcbNeeded = new IntByReference();
        final IntByReference pcReturned = new IntByReference();
        Winspool.INSTANCE.EnumPrinters(2, null, 4, null, 0, pcbNeeded, pcReturned);
        if (pcbNeeded.getValue() <= 0) {
            return new Winspool.PRINTER_INFO_4[0];
        }
        final Winspool.PRINTER_INFO_4 pPrinterEnum = new Winspool.PRINTER_INFO_4(pcbNeeded.getValue());
        if (!Winspool.INSTANCE.EnumPrinters(2, null, 4, pPrinterEnum.getPointer(), pcbNeeded.getValue(), pcbNeeded, pcReturned)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return (Winspool.PRINTER_INFO_4[])pPrinterEnum.toArray(pcReturned.getValue());
    }
}
