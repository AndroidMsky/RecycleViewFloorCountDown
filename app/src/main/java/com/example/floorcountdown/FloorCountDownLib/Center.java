package com.example.floorcountdown.FloorCountDownLib;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.WeakHashMap;

/**
 * Created by air on 2019/3/13.
 */
public class Center implements ICountDownCenter {

    private final int BEAT_TIME;
    private final boolean CHANGEABLE;
    private volatile boolean isStart;
    private WeakHashMap<Observer, Object> weakHashMap = new WeakHashMap();
    private PostionFL postionFL=new PostionFL();

    /*
    * @param beatTime 更新频率
    * @param changeable 易变得，如果为true
    * 会在bind方法里访问Map，不存在则加入Map
    * 如果存在频繁的下拉刷新，分页加载建议设为true
    *
    *
    * 如果列表数据问题请设置为false，Holder会在GC后自动停止更新。
    *
    *
    * */
    public Center(int beatTime, boolean changeable) {
        BEAT_TIME = beatTime;
        CHANGEABLE = changeable;
    }
    public static class PostionFL{
        public int frist=-1;
        public int last=-1;

    }

    class CountDownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    notifyHolder();
                    sendEmptyMessageDelayed(0, BEAT_TIME);
                    break;

                default:
            }

        }
    }

    private CountDownHandler handler = new CountDownHandler();


    @Override
    public void bindRecyclerView(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int first=-1;
                int last=-1;

                RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager){
                     first=((GridLayoutManager)layoutManager).findFirstVisibleItemPosition();
                     last=((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
                }
                if (layoutManager instanceof LinearLayoutManager){
                     first=((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                     last=((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                }
                postionFL.frist=first;
                postionFL.last=last;

                Log.e("lmtlmt2","frist:"+first+"last:"+last);
            }
        });
    }
    private void notifyHolder() {
        if (isStart)
            notifyObservers();
    }

    private void notifyObservers() {

        Iterator iter = weakHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Observer observer = (Observer) entry.getKey();
            if (observer != null) {
                if (postionFL.frist>-1&&postionFL.last>-1)
                observer.update(null, postionFL);
                else observer.update(null, null);
            }


        }
        Log.e("lmtlmt", "weakHashMap size" + weakHashMap.size());

    }

    @Override
    public void startCountdown() {
        if (!isStart) {
            handler.sendEmptyMessage(0);
            isStart = true;
        }

    }

    @Override
    public void stopCountdown() {
        isStart = false;
        handler.removeCallbacksAndMessages(null);

    }

    @Override
    public void addObserver(Observer observer) {
        weakHashMap.put(observer, null);
    }

    @Override
    public void deleteObservers() {
        stopCountdown();
        weakHashMap.clear();
    }

    @Override
    public boolean containHolder(Observer observer) {
        //如果不是易变的 直接屏蔽次方法。
        if (!CHANGEABLE) return true;
        if (weakHashMap.containsKey(observer)) return true;
        return false;
    }

    @Override
    public void notifyAdapter() {
        if (!CHANGEABLE) return;
        deleteObservers();
    }
}
