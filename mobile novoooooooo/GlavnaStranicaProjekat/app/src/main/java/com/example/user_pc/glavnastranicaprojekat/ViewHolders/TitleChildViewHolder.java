package com.example.user_pc.glavnastranicaprojekat.ViewHolders;


import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.example.user_pc.glavnastranicaprojekat.R;


public class TitleChildViewHolder extends ChildViewHolder {
    public TextView option1,option2;
    public TitleChildViewHolder(View itemView) {
        super(itemView);
        option1 = itemView.findViewById(R.id.option1);
        option2 = itemView.findViewById(R.id.option2);






    }


}