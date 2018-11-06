package com.bangbang.taskhall;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.bangbang.utils.MemoryCacheUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bangbang.utils.Util_getImage.comp;
import static com.bangbang.utils.Util_getImage.getBitMBitmap;

public class xRecAdapter_task_taskhall extends RecyclerView.Adapter<xRecAdapter_task_taskhall.MyViewHolder> {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    MemoryCacheUtils memoryCacheUtils = new MemoryCacheUtils();
    static Context context;

    List<Task>task_taskhall = new ArrayList <Task>();
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textMoney;
        TextView textPeople;
        TextView textTime;
        TextView textTask;
        TextView textState;
        TextView textBusiness;
        TextView textAddrss;
        CircleImageView image_people;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textPeople=itemView.findViewById(R.id.item_people);
            this.textMoney = itemView.findViewById(R.id.item_money);
            this.textTime=itemView.findViewById(R.id.item_time);
            this.textTask=itemView.findViewById(R.id.item_task);
            this.textState=itemView.findViewById(R.id.item_state);
            this.textBusiness=itemView.findViewById(R.id.item_business);
            this.textAddrss=itemView.findViewById(R.id.item_address);
            this.image_people=itemView.findViewById(R.id.people_image);
        }
    }
    public xRecAdapter_task_taskhall(Context context, List<Task>task_taskhall) {
        super();
        this.context =context;
        this.task_taskhall = task_taskhall;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_taskhall,null);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final String account_send = task_taskhall.get(position).getAccount_send();

        final int id = task_taskhall.get(position).getId();
        final String name_send = task_taskhall.get(position).getName_send();
        //final String name_received = task_taskhall.get(position).getName_received();
        final String business = task_taskhall.get(position).getBusiness();
        final String address = task_taskhall.get(position).getAddress();
        final String time = task_taskhall.get(position).getTime();
        final String money = task_taskhall.get(position).getMoney()+"";
        final String task = task_taskhall.get(position).getTask();
        //final String state = task_releaseds.get(position).getState();
        holder.textMoney.setText(money);
        holder.textState.setText("等待抢单");
        holder.textTask.setText(task);
        holder.textTime.setText(time);
        holder.textPeople.setText(name_send);
        holder.textBusiness.setText(business);
        holder.textAddrss.setText(address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String iconUrl = getTouxiangUrl(account_send);
                final Bitmap bm = getBitMBitmap(iconUrl);
                final Bitmap bitmap;
                if (memoryCacheUtils.getBitmapFromMemory(iconUrl) == null) {
                    bitmap = comp(getBitMBitmap(iconUrl));
                    memoryCacheUtils.setBitmapToMemory(iconUrl, bitmap);
                } else {
                    bitmap = comp(memoryCacheUtils.getBitmapFromMemory(iconUrl));
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.image_people.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();


    }
    @Override
    public int getItemCount() {
        return task_taskhall.size();
    }

    String getTouxiangUrl(String account){
        String result = ""; //用来取得返回的String；
        //发送post请求
        HttpPost httpRequest = new HttpPost("http://132.232.93.93/bangbang/bangbang_getTouxiang_taskhall.php");
        //Post运作传送变数必须用NameValuePair[]阵列储存
        try {
            //发出HTTP请求
            Log.d("请求连接", "头像请求");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("account", account));
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //取得HTTP response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            //若状态码为200则请求成功，取到返回数据
            Log.d("连接值", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //取出字符串
                Log.d("请求连接", "连接成功");
                result = new String(EntityUtils.toString(httpResponse.getEntity(),"utf8"));
                Log.d("QQQQQQQ", result);
            }
        } catch (Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "网络出错",Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }

}
