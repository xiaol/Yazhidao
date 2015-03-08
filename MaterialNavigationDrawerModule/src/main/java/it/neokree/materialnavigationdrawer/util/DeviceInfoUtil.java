package it.neokree.materialnavigationdrawer.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by fengjigang on 15-2-2.
 */
public class DeviceInfoUtil {
    private static final String TAG = "DeviceStateUtil";

    public static int getScreenWidth(Context mContext) {
        return obtainDisMetri(mContext).widthPixels;
    }

    public static int getScreenHeight(Context mContext) {
        return obtainDisMetri(mContext).heightPixels;
    }

    private static DisplayMetrics obtainDisMetri(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

}
