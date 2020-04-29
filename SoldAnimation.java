package com.dryfire.sold.UI;

import android.animation.ObjectAnimator;

import androidx.recyclerview.widget.RecyclerView;


public class SoldAnimation {

    public static  void animate(RecyclerView.ViewHolder holder,boolean goes_down){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(holder.itemView,"translationY",
                goes_down == true?100:-100,0);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }
}
