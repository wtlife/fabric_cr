package com.wtlife.boot.util;

import java.text.SimpleDateFormat;

public class DateStamp {
    public static String getDate(String stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(stamp);
        res = simpleDateFormat.format(lt * 1000);
        return res;
    }
}
