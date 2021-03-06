package com.bangbang.task_received;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bangbang.R;
import com.bangbang.bean.Task;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.List;

public class xRecAdapter_task_myreceived extends RecyclerView.Adapter<xRecAdapter_task_myreceived.MyViewHolder> {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    static Context context;
    List<Task>task_receiveds = new ArrayList <Task>();
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textMoney;
        TextView textPeople;
        TextView textTime;
        TextView textTask;
        TextView textState;
        TextView textBusiness;
        TextView textAddrss;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textPeople=itemView.findViewById(R.id.item_people);
            this.textMoney = itemView.findViewById(R.id.item_money);
            this.textTime=itemView.findViewById(R.id.item_time);
            this.textTask=itemView.findViewById(R.id.item_task);
            this.textState=itemView.findViewById(R.id.item_state);
            this.textBusiness=itemView.findViewById(R.id.item_business);
            this.textAddrss=itemView.findViewById(R.id.item_address);
        }
    }
    public xRecAdapter_task_myreceived(Context context, List<Task>task_receiveds) {
        super();
        this.context =context;
        this.task_receiveds = task_receiveds;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_received,null);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //final String icon_url = task_releaseds.get(position);
        final int id = task_receiveds.get(position).getId();
        //final String name_send = task_releaseds.get(position).getName_send();
        final String name_received = task_receiveds.get(position).getName_received();
        final String business = task_receiveds.get(position).getBusiness();
        final String address = task_receiveds.get(position).getAddress();
        final String time = task_receiveds.get(position).getTime();
        final String money = "￥"+task_receiveds.get(position).getMoney()+"";
        final String task = task_receiveds.get(position).getTask();
        //final String state = task_releaseds.get(position).getState();
        holder.textMoney.setText(money);
        holder.textState.setText("等待抢单");
        holder.textTask.setText(task);
        holder.textTime.setText(time);
        holder.textPeople.setText(name_received);
        holder.textBusiness.setText(business);
        holder.textAddrss.setText(address);
    }
    @Override
    public int getItemCount() {
        return task_receiveds.size();
    }

}
