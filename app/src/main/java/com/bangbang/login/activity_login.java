package com.bangbang.login;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bangbang.R;
import com.bangbang.register.activity_register_getcord;
import com.bangbang.register.activity_register_getphone;
import com.bangbang.taskhall.MainActivity;
import com.bangbang.utils.ActivityManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class activity_login extends AppCompatActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    String userName = "";
    String account = "";
    String finalResult = "";
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    LinearLayout layout_login_main;
    RelativeLayout img_flash;
    LinearLayout pro;
    Button register ;
    EditText user_name ;
    EditText pass_word ;
    Button login ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(activity_login.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        judgeLogin();
        getId();//得到控件
        flash();//异步加载网络资源
        listenClick();//监听点击


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void gotoLogin(final String account, String password, String connectUrl) {
        String result = ""; //用来取得返回的String；
//test
        System.out.println("account:" + account);
        System.out.println("password:" + password);
//发送post请求
        HttpPost httpRequest = new HttpPost(connectUrl);
//Post运作传送变数必须用NameValuePair[]阵列储存
        List params = new ArrayList();
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("pwd", password));
        try {
//发出HTTP请求
            Log.d("请求连接", "在发送请求");
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//取得HTTP response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
//若状态码为200则请求成功，取到返回数据
            Log.d("连接值", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//取出字符串
                Log.d("请求连接", "连接成功");
                result = new String(EntityUtils.toString(httpResponse.getEntity(),"utf8"));
                Log.d("服务器消息：", result);
                finalResult = result;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("RESULT", finalResult);
                        parseJSON(finalResult);
                        if(!finalResult.equals("login failed")) {
                            Log.d("user_name",userName);
                            pro.setVisibility(View.GONE);
                            Toast.makeText(activity_login.this, "登陆成功！"+userName,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity_login.this,MainActivity.class);
                            intent.putExtra("user_name",userName);
                            intent.putExtra("account",account);
                            editor.putString("user_name",userName);
                            editor.putString("account",account);
                            editor.putString("login","2");
                            editor.commit();
                            startActivity(intent);

                        }
                        else{
                            pro.setVisibility(View.GONE);
                            Toast.makeText(activity_login.this, "账户名或密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity_login.this, "网络出错",Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
    }
    //解析json方法
    private void parseJSON(String jsonData){
        try{
            Log.d("JSON" ,jsonData);
            jsonData = jsonData.substring(1);
            JSONObject jsonObject = new JSONObject(jsonData);
            userName = jsonObject.getString("name");
            Log.d("PARSE!!user_name",userName);
        }catch (Exception e)
        {
            finalResult = "login failed";
            e.printStackTrace();
        }
    }
    void flash(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            img_flash.setVisibility(View.GONE);
                            layout_login_main.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    void getId(){
        register = (Button) findViewById(R.id.register);
        user_name = (EditText) findViewById(R.id.user_name);
        pass_word = (EditText) findViewById(R.id.pass_word);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        pro = (LinearLayout)findViewById(R.id.pro);
        layout_login_main = (LinearLayout)findViewById(R.id.layout_login_main);
        img_flash = (RelativeLayout)findViewById(R.id.image_falsh);
    }
    void listenClick(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_name.getText().length()>0&&pass_word.getText().length()>0)
                {
                    Log.d("AAAAAAAAAA", "点击");
                    pro.setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {

                            String account = user_name.getText().toString().trim();
                            String password = pass_word.getText().toString().trim();
                            //连接到服务器的地址
                            String connectURL = "http://132.232.93.93/bangbang/login.php";
                            //填入用户名密码和连接地址
                            gotoLogin(account, password, connectURL);
                        }
                    }).start();
                }else {
                    Toast.makeText(activity_login.this, "账号与密码不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_login.this,activity_register_getphone.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.enter_zoom_go_out,R.anim.enter_zoom_go_in);
            }
        });
    }
    void judgeLogin(){
        sp = getSharedPreferences("data",MODE_PRIVATE);
        editor = sp.edit();

        if(sp.getString("login",null)==null){
            editor.putString("login","1");
            editor.commit();
        }
        if(sp.getString("login","").equals("2"))
        {
            Intent intent = new Intent(activity_login.this,MainActivity.class);
            userName = sp.getString("user_name","");
            account = sp.getString("account","");
            intent.putExtra("user_name",userName);
            intent.putExtra("account",account);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_zoom_go_in,R.anim.enter_zoom_go_out);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2) {
            if (requestCode == 1) {
                user_name.setText(data.getStringExtra("account"));//这里是账号
                pass_word.setText(data.getStringExtra("password"));
            }
        }
    }

}
