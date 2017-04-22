package com.hanbit.class16contacts.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.hanbit.class16contacts.R;

public class Album extends AppCompatActivity {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        final Context context=Album.this;
        gridView= (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new Movie(context,movies()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int i, long L) {
                Toast.makeText(context,"선택한 사진"+i,Toast.LENGTH_LONG).show();
            }
        });
    }
    public String[] movies(){
        int count=12;
        String[]arr=new String[count];
        for(int i=0;i<arr.length;i++){
            Log.d("사진 인덱스",String.valueOf(i));
            arr[i]="mov"+(i+1);
        }
        return arr;
    }
}
