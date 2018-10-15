package com.bangbang.task_received;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;
import com.bangbang.R;
import com.bangbang.bean.Task;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.bangbang.utils.animation_rec_item;

public class activity_task_myreceived extends AppCompatActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Thread thread_getTask = null;
    XRecyclerView recyclerView = null;
    xRecAdapter_task_myreceived xRecAdapter_task_received ;
    String account = "";
    boolean havedata = true;
    int addStart = 0;
    List<Task>task_releaseds = new ArrayList <Task>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_released);
        Intent intent = getIntent();
        account = intent.getStringExtra("account");

    }
    @Override
    public void onResume() {
        super.onResume();
        recyclerView = (XRecyclerView) findViewById(R.id.xrec);
        initView();
    }
    void initView(){

        initRec();
    }
    void initRec(){
        LinearLayoutManager xLinearLayoutManager = new LinearLayoutManager(activity_task_myreceived.this);
        xLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallRotate); //设定下拉刷新样式
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);//设定上拉加载样式
        recyclerView.setLayoutManager(xLinearLayoutManager);
        xRecAdapter_task_received = new xRecAdapter_task_myreceived(activity_task_myreceived.this,task_releaseds);
        recyclerView.setAdapter(xRecAdapter_task_received);
        //recyclerView.setArrowImageView(R.drawable.qwe);     //设定下拉刷新显示图片（不必须）
        initData();   //初始化数据
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            //上拉加载监听
            @Override
            public void onLoadMore() {
                addData();  //上拉加载添加数据
            }
            //下拉刷新监听
            @Override
            public void onRefresh() {
                initData();     //初始化数据
            }
        });
    }
    void addData() {
        if(thread_getTask!=null) {
            thread_getTask.interrupt();
        }
        thread_getTask = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("BEFORE", addStart+"");

                if(havedata&&!parseJSON(getdutyList(addStart,addStart+10)).equals("nodata")) {
                    addStart += 10;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            animation_rec_item.runLayoutAnimation(recyclerView);
                            //xRecAdapter_task_released.notifyDataSetChanged();
                            recyclerView.loadMoreComplete();    //加载数据完成（取消加载动画）
                        }
                    });
                    Log.d("AFTER", addStart+"");
                }else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            havedata = false;
                            recyclerView.loadMoreComplete();    //加载数据完成（取消加载动画）
                        }
                    });
                }
            }
        });

        thread_getTask.start();


    }
    void initData() {
        task_releaseds.clear();
        xRecAdapter_task_received.notifyDataSetChanged();
        if(thread_getTask!=null) {
            thread_getTask.interrupt();
        }
        thread_getTask = new Thread(new Runnable() {
            @Override
            public void run() {
                parseJSON(getdutyList(0,10));
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        animation_rec_item.runLayoutAnimation(recyclerView);
                        //xRecAdapter_task_released.notifyDataSetChanged();
                        recyclerView.refreshComplete();     //刷新数据完成（取消刷新动画）
                    }
                });
                addStart = 10;
            }
        });
        thread_getTask.start();
    }
    String getdutyList(int start,int end){
        String result = ""; //用来取得返回的String；
        //发送post请求
        HttpPost httpRequest = new HttpPost("http://132.232.93.93/bangbang/bangbang_getTask_received.php");
        //Post运作传送变数必须用NameValuePair[]阵列储存
        try {
            //发出HTTP请求
            Log.d("请求连接", "在发送请求");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("start", start+""));
            params.add(new BasicNameValuePair("end", end+""));
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
            }
        } catch (Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity_task_myreceived.this, "网络出错",Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }
    String parseJSON(String jsonData){
        Log.d("JSON" ,jsonData);
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            havedata = true;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                task_releaseds.add(new Task(jsonObject.getInt("id"),
                        jsonObject.getString("business"),
                        jsonObject.getString("account_send"),
                        jsonObject.getString("account_received"),
                        jsonObject.getString("task"),
                        jsonObject.getInt("money"),
                        jsonObject.getString("address"),
                        jsonObject.getString("state"),
                        jsonObject.getString("time"),
                        jsonObject.getString("name_send"),
                        jsonObject.getString("name_received")
                ));
            }

        } catch (JSONException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    havedata = false;
                    Toast.makeText(activity_task_myreceived.this, "下面没有啦！", Toast.LENGTH_SHORT).show();
                }
            });
            return "nodata";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
