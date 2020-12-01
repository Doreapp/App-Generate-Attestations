package com.mandin.antoine.attestations.util;

import androidx.annotation.Nullable;
public class Utils {

    public static String buildBundleKey(String key, @Nullable String prefix){
        return prefix == null ? key : prefix+"."+key;
    }
}
