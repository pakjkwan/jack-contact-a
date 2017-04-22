package com.hanbit.class16contacts.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hanbit.class16contacts.R;

/**
 * Created by hb2000 on 2017-04-08.
 */

public class Movie extends BaseAdapter {
    private Context context;
    private String[] movies;

    public Movie(Context context, String[] movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup g) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if(v==null){
            gridView=new View(context);
            gridView=inflater.inflate(R.layout.movie,null);
            ImageView imageView= (ImageView) gridView.findViewById(R.id.imageView);
            String movie=movies[i];
            switch (movie){
                case "mov1":imageView.setImageResource(R.drawable.mov1);break;
                case "mov2":imageView.setImageResource(R.drawable.mov2);break;
                case "mov3":imageView.setImageResource(R.drawable.mov3);break;
                case "mov4":imageView.setImageResource(R.drawable.mov4);break;
                case "mov5":imageView.setImageResource(R.drawable.mov5);break;
                case "mov6":imageView.setImageResource(R.drawable.mov6);break;
                case "mov7":imageView.setImageResource(R.drawable.mov7);break;
                case "mov8":imageView.setImageResource(R.drawable.mov8);break;
                case "mov9":imageView.setImageResource(R.drawable.mov9);break;
                case "mov10":imageView.setImageResource(R.drawable.mov10);break;
                case "mov11":imageView.setImageResource(R.drawable.mov11);break;
                case "mov12":imageView.setImageResource(R.drawable.mov12);break;
            }
        }else{
            gridView=v;
        }
        return gridView;
    }
}
