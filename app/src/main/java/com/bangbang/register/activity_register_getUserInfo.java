package com.bangbang.register;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bangbang.R;
import com.bangbang.utils.ActivityManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class activity_register_getUserInfo extends AppCompatActivity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    EventHandler eventHandler;
    String connectURL = "http://132.232.93.93/bangbang/register.php";
    String account = "";
    String name = "";
    String password = "";
    String repassword = "";
    String result = "";
    LinearLayout pro = null;
    EditText input_name = null;
    EditText input_password = null;
    EditText input_repassword = null;
    Button btn_register = null;
    Button btn_return = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(activity_register_getUserInfo.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_get_user_info);
        Intent intent = getIntent();
        account = intent.getStringExtra("phone");
        getId();
    }
    void getId(){
        pro = findViewById(R.id.pro);
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        input_repassword = findViewById(R.id.input_repassword);
        btn_register = findViewById(R.id.btn_register);
        btn_return = findViewById(R.id.btn_return2);
        input_name.requestFocus();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkdata())
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            regist();
                        }
                    }).start();

                }
            }
        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter_zoom_return_out,R.anim.enter_zoom_return_in);
            }
        });
    }
    boolean checkdata(){
        name = input_name.getText().toString().trim();
        password = input_password.getText().toString().trim();
        repassword = input_repassword.getText().toString().trim();
        if(name.length()<4) {
            Toast.makeText(activity_register_getUserInfo.this,"昵称必须由4个字符以上组成",Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.length()<6) {
            Toast.makeText(activity_register_getUserInfo.this, "密码必须由6至12个英文或数字组成", Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.equals(repassword)) {
            return true;
        }else {

            Toast.makeText(activity_register_getUserInfo.this, "两次密码输入不一致", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    void regist(){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", password));
        HttpPost httpRequest = new HttpPost(connectURL);
        try {
//发出HTTP请求
            Log.d("请求连接", "在发送请求");
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//取得HTTP response
            final HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
//若状态码为200则请求成功，取到返回数据
            Log.d("连接值", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//取出字符串
                Log.d("请求连接", "连接成功");
                result = new String(EntityUtils.toString(httpResponse.getEntity(), "utf8"));
                Log.d("LENGTH", result.length()+"");

                if(!(result.length()==5))
                //字符串不为error 可以注册
                {
                    mHandler.post(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void run() {
                            Log.d("注册成功", result);

                            Toast.makeText(activity_register_getUserInfo.this, "注册成功！",Toast.LENGTH_SHORT).show();
                            pro.setVisibility(View.GONE);
                            Intent intent = new Intent(activity_register_getUserInfo.this, activity_register_end.class);
                            intent.putExtra("account",account);
                            intent.putExtra("user_name",name);
                            startActivity(intent,
                                    ActivityOptions.makeSceneTransitionAnimation(activity_register_getUserInfo.this).toBundle());

                            overridePendingTransition(R.anim.enter_zoom_go_out,R.anim.enter_zoom_go_in);

                        }
                    });
                }
                else {
                    //字符串为error 不能注册
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pro.setVisibility(View.GONE);
                            Log.d("失败", result);
                            Toast.makeText(activity_register_getUserInfo.this,"账号已存在！请重新输入",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }else {
                Log.d("请求连接", "连接失败");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
