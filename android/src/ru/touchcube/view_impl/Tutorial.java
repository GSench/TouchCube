package ru.touchcube.view_impl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import ru.touchcube.R;
import ru.touchcube.domain.utils.function;
import ru.touchcube.viewholders.TutorialViewHolder;

/**
 * Created by grish on 22.10.2018.
 * This class is used as part of AndroidLauncher to show tutorial on the 1st app launch
 */

public class Tutorial {

    private Activity act;
    private function<Void> onTutorialPassed;
    private TutorialViewHolder viewHolder;
    private TutorialLvl[] tutorialMsg;
    private int cur;

    public Tutorial(ViewGroup main, Activity act, function<Void> onTutorialPassed){
        this.act=act;
        this.onTutorialPassed=onTutorialPassed;
        viewHolder = new TutorialViewHolder(main, act);
    }

    private class TutorialLvl{
        private String msg; private Drawable img;
        private TutorialLvl(String msg, Drawable img){
            this.msg=msg; this.img=img;
        }
    }

    public void start(){
        tutorialMsg = new TutorialLvl[]{
                new TutorialLvl(act.getResources().getString(R.string.tutorial_1), act.getResources().getDrawable(R.drawable.single_tap_pic)),
                new TutorialLvl(act.getResources().getString(R.string.tutorial_2), act.getResources().getDrawable(R.drawable.single_finger_rotate_pic)),
                new TutorialLvl(act.getResources().getString(R.string.tutorial_3), act.getResources().getDrawable(R.drawable.pinch_pic)),
                new TutorialLvl(act.getResources().getString(R.string.tutorial_4), act.getResources().getDrawable(R.drawable.press_hold_pic)),
                new TutorialLvl(act.getResources().getString(R.string.tutorial_5), act.getResources().getDrawable(R.drawable.double_tap_pic)),
        };
        cur = 0;
        viewHolder.tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cur>=4){
                    viewHolder.main.removeView(viewHolder.tutorial);
                    onTutorialPassed.run();
                }
                else {
                    cur++;
                    viewHolder.img.setImageDrawable(tutorialMsg[cur].img);
                    viewHolder.txt.setText(tutorialMsg[cur].msg);
                }
            }
        });
        viewHolder.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cur>0) {
                    cur--;
                    viewHolder.img.setImageDrawable(tutorialMsg[cur].img);
                    viewHolder.txt.setText(tutorialMsg[cur].msg);
                }
            }
        });

        viewHolder.img.setImageDrawable(tutorialMsg[cur].img);
        viewHolder.txt.setText(tutorialMsg[cur].msg);
        viewHolder.main.addView(viewHolder.tutorial);
    }

}
