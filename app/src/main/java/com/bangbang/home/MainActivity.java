package com.bangbang.home;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.bangbang.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Button button ;
    String connectURL = "http://10.0.2.2/oto/test_while.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.send_post);
        initClickListener();
    }
    void initClickListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPost();
            }
        });
    }
    void sendPost(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = getDutyState(12);
                Log.d("RESULT", result);
            }
        }).start();
    }
    String getDutyState(int start){
        String result = ""; //用来取得返回的String；
        //发送post请求
        HttpPost httpRequest = new HttpPost(connectURL);
        //Post运作传送变数必须用NameValuePair[]阵列储存
        try {
            //发出HTTP请求
            Log.d("请求连接", "在发送请求");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("start", start+""));
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            //取得HTTP response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            //若状态码为200则请求成功，取到返回数据
            Log.d("连接值", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //取出字符串
                Log.d("请求连接", "连接成功");
                Log.d("获取数据中", "获取数据中");
                result = new String(EntityUtils.toString(httpResponse.getEntity(),"utf8"));
            }
        } catch (Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "网络出错",Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }
}
