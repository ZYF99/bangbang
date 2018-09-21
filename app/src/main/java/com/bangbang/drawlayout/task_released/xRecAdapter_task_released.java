package com.bangbang.drawlayout.task_released;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangbang.R;
import com.bangbang.bean.Task;
import com.bangbang.utils.MemoryCacheUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class xRecAdapter_task_released extends RecyclerView.Adapter<xRecAdapter_task_released.MyViewHolder> {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    static Context context;
    List<Task>task_releaseds = new ArrayList <Task>();
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textMoney;
        TextView textPeople;
        TextView textTime;
        TextView textTask;
        TextView textState;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textPeople=itemView.findViewById(R.id.item_people);
            this.textMoney = itemView.findViewById(R.id.item_money);
            this.textTime=itemView.findViewById(R.id.item_time);
            this.textTask=itemView.findViewById(R.id.item_task);
            this.textState=itemView.findViewById(R.id.item_state);
        }
    }
    public xRecAdapter_task_released(Context context, List<Task>task_releaseds) {
        super();
        this.context =context;
        this.task_releaseds = task_releaseds;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_released,null);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //final String icon_url = task_releaseds.get(position);
        final int id = task_releaseds.get(position).getId();
        final String account = task_releaseds.get(position).getAccoount();
        final String business = task_releaseds.get(position).getBusiness();
        final String address = task_releaseds.get(position).getAddress();
        final String time = task_releaseds.get(position).getTime();
        Log.d("SSSSSSSS", time);
        final String money = "￥"+task_releaseds.get(position).getMoney()+"";
        final String task = task_releaseds.get(position).getTask();
        //final String state = task_releaseds.get(position).getState();
        holder.textMoney.setText(money);
        holder.textState.setText("等待抢单");
        holder.textTask.setText(task);
        holder.textTime.setText(time);
        holder.textPeople.setText(account);
        sendPost(id,holder.textState);
    }
    @Override
    public int getItemCount() {
        return task_releaseds.size();
    }
    void sendPost(final int takID, final TextView text_state) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = getDutyState(takID);
                Log.d("RESULT", result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        text_state.setText("已被抢单");
                    }
                });
            }
        }).start();
    }
    String getDutyState(int taskID) {
        String result = ""; //用来取得返回的String；
        //发送post请求
        HttpPost httpRequest = new HttpPost("http://10.0.2.2/bangbang/bangbang_getstate.php");
        //Post运作传送变数必须用NameValuePair[]阵列储存
        try {
            //发出HTTP请求
            Log.d("*****获取任务状态连接*****", "在发送请求");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("taskID", taskID + ""));
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //取得HTTP response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            //若状态码为200则请求成功，取到返回数据
            Log.d("连接值", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //取出字符串
                Log.d("请求连接", "连接成功");
                Log.d("获取任务状态中", "获取数据中");
                result = new String(EntityUtils.toString(httpResponse.getEntity(), "utf8"));
            }
        } catch (Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "网络出错", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }
}
