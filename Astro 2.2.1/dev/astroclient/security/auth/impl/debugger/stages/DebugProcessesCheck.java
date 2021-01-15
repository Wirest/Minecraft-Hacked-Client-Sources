package dev.astroclient.security.auth.impl.debugger.stages;

import dev.astroclient.security.auth.IStage;
import dev.astroclient.security.indirection.MethodIndirection;

/**
 * made by Xen for Astro
 * at 12/3/2019
 **/
public class DebugProcessesCheck implements IStage {
    private String[] processes = new String[]{
            "ollydbg.exe",
            "ProcessHacker.exe",
            "tcpview.exe",
            "autoruns.exe",
            "autorunsc.exe",
            "filemon.exe",
            "procmon.exe",
            "regmon.exe",
            "procexp.exe",
            "idaq.exe",
            "idaq64.exe",
            "ImmunityDebugger.exe",
            "Wireshark.exe",
            "dumpcap.exe",
            "HookExplorer.exe",
            "ImportREC.exe",
            "PETools.exe",
            "LordPE.exe",
            "dumpcap.exe",
            "SysInspector.exe",
            "proc_analyzer.exe",
            "sysAnalyzer.exe",
            "sniff_hit.exe",
            "windbg.exe",
            "joeboxcontrol.exe",
            "joeboxserver.exe",
            "fiddler.exe",
            "TeamViewer_Service.exe",
            "TeamViewer.exe",
            "tv_w32.exe",
            "tv_x64.exe",
    };


    @Override
    public byte getStage() {
        return 4;
    }

    @Override
    public boolean pass() {
        for(int i = 0;i < processes.length; i++) {
            String process = processes[i];
            if(isProcessRunning(process))
               return false;
        }

        return true;
    }


    private boolean isProcessRunning(String processName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
            Process process = processBuilder.start();
            String tasksList = process.getInputStream().toString();

            return tasksList.contains(processName);
        } catch (Exception ignored) {}

        return false;
    }
}
