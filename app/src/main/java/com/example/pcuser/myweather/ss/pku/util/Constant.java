package com.example.pcuser.myweather.ss.pku.util;

/**
 * Created by pcuser on 15/6/12.
 */

import android.content.Context;
import com.example.pcuser.myweather.R;

public class Constant {
    public static int picSize;
    public Constant(Context c){
        picSize = (int) c.getResources().getDimension(R.dimen.abc_action_button_min_height_material);
        System.out.println(picSize);
    }

}
