package me.chen_wei.uidemo.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * author: Chen Wei time: 16/12/3 desc: 描述
 */

public class DensityUtils {
    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int pxToDp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().getDisplayMetrics());
    }
}
