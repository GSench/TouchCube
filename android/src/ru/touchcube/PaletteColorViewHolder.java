package ru.touchcube;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by grish on 19.09.2018.
 */

public class PaletteColorViewHolder {

    public LinearLayout paletteColor;
    public LinearLayout colorView;

    public PaletteColorViewHolder(Activity act, ViewGroup palette){
        paletteColor = (LinearLayout) act.getLayoutInflater().inflate(R.layout.palett_color, palette, false);
        colorView = paletteColor.findViewById(R.id.color);
    }
}
