package ru.touchcube.viewholders;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ru.touchcube.R;

/**
 * Created by grish on 19.09.2018.
 * This class is used as a part of PaletteViewHolder
 */

public class PaletteColorViewHolder {

    public LinearLayout paletteColor;
    public LinearLayout colorView;

    public PaletteColorViewHolder(Activity act, ViewGroup palette){
        paletteColor = (LinearLayout) act.getLayoutInflater().inflate(R.layout.palette_color, palette, false);
        colorView = paletteColor.findViewById(R.id.color);
    }
}
