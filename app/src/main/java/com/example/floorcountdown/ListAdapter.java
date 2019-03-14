package com.example.floorcountdown;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.floorcountdown.FloorCountDownLib.ICountDownCenter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by air on 2019/3/12.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<TimeBean> list = new ArrayList<>();
    private ICountDownCenter countDownCenter;

    public ListAdapter(ArrayList<TimeBean> list, ICountDownCenter countDownCenter) {
        this.list.addAll(list);
        this.countDownCenter = countDownCenter;
    }
    public void removeFloor(int i){
        for (int j = 0; j < i; j++) {
            list.remove(0);
        }
        countDownCenter.notifyAdapter();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        countDownCenter.addObserver(viewHolder);
        countDownCenter.startCountdown();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.lastBindPositon = position;
        holder.timeBean = list.get(position);
        bindCountView(holder.textView, holder.timeBean);
        if (!countDownCenter.containHolder(holder)){
            countDownCenter.addObserver(holder);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements Observer {
        int lastBindPositon = -1;
        TextView textView;
        TimeBean timeBean;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv1);

        }

        @Override
        public void update(Observable o, Object arg) {
            Log.e("lmtlmtupdate", "update" + lastBindPositon);
            bindCountView(textView, timeBean);

        }

        //监控内存，可删除此方法实现
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            Log.e("lmtlmt", "finalize" + lastBindPositon);
        }
    }

    //倒计时展示和结束逻辑
    private static void bindCountView(TextView textView, TimeBean timeBean) {
        if (timeBean == null) return;
        //倒计时结束
        if (timeBean.getRainTime() <= 0) {
            textView.setText("倒计时结束 活动开始");
            return;
        }
        textView.setText("距开始 ：" + timeBean.getRainTime() + "");

    }


}
