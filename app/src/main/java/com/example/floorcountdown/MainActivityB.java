package com.example.floorcountdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.floorcountdown.FloorCountDownLib.Center;
import com.example.floorcountdown.FloorCountDownLib.ICountDownCenter;

import java.util.ArrayList;

public class MainActivityB extends AppCompatActivity {

    private ArrayList<TimeBean> list=new ArrayList<>();
    private ICountDownCenter countDownCenter;
    private ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        final RecyclerView recyclerView=new RecyclerView(this);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < 100; i++) {
            list.add(new TimeBean(i*10));
        }
        countDownCenter=new Center(1000,false);
        listAdapter=new ListAdapter(list,countDownCenter);
        recyclerView.setAdapter(listAdapter);
        final FrameLayout frameLayout=findViewById(R.id.f1);
        frameLayout.addView(recyclerView);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //countDownCenter.notifyAdapter();
                listAdapter.removeFloor(99);
//                测试重新setAdatper1
//                list=new ArrayList<>();
//                list.add(new TimeBean(1*10000));
//                countDownCenter.deleteObservers();
//                countDownCenter=new Center(1000);
//                recyclerView.setAdapter(new ListAdapter(list,countDownCenter));

            }
        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownCenter.deleteObservers();
    }
}
