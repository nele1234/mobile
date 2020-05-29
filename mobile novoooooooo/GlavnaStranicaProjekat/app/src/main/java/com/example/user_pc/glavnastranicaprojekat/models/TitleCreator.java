package com.example.user_pc.glavnastranicaprojekat.models;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;



public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();


        TitleParent title = new TitleParent(String.format("Fellini"));
        _titleParents.add(title);



        TitleParent title1 = new TitleParent(String.format("Restaurant Bono"));
        _titleParents.add(title1);


        TitleParent title2 = new TitleParent(String.format("Chipas"));
        _titleParents.add(title2);

        TitleParent title3 = new TitleParent(String.format("Comming soon"));
        _titleParents.add(title3);

    }

    public static TitleCreator get(Context context)
    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}