package com.ihl.client.util;

import com.ihl.client.Helper;

public class HelperUtil {

    public static boolean inGame() {
        return Helper.world() != null && Helper.player() != null;
    }
}
