package com.bangbang.register;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bangbang.R;
import com.bangbang.utils.ActivityManager;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class activity_register_getphone extends AppCompatActivity implements View.OnClickListener {

    String phone = "";
    EditText input_phone = null;
    Button btn_getCord = null;
    Button btn_return = null;
    EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_getphone);
        ActivityManager.getInstance().addActivity(activity_register_getphone.this);
        getId();

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }
    void getId(){

        input_phone = (EditText)findViewById(R.id.input_account);
        btn_getCord=findViewById(R.id.btn_getcode);
        btn_return=findViewById(R.id.btn_return);
        input_phone.requestFocus();
        btn_getCord.setOnClickListener(this);
        btn_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_getcode:
                    if (judPhone())
                    {
                        SMSSDK.getVerificationCode("86", phone);
                        Intent intent = new Intent(activity_register_getphone.this,activity_register_getcord.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_zoom_go_out,R.anim.enter_zoom_go_in);
                    }
                break;
            case R.id.btn_return:
                finish();
                overridePendingTransition(R.anim.enter_zoom_return_out,R.anim.enter_zoom_return_in);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_zoom_return_out,R.anim.enter_zoom_return_in);

    }
    private boolean judPhone()
    {
        if(TextUtils.isEmpty(input_phone.getText().toString().trim()))
        {
            Toast.makeText(activity_register_getphone.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            input_phone.requestFocus();
            return false;
        }
        else if(input_phone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(activity_register_getphone.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            input_phone.requestFocus();
            return false;
        }
        else
        {
            phone=input_phone.getText().toString().trim();
            String num="[1][34578]\\d{9}";
            if(phone.matches(num))
                return true;
            else
            {
                Toast.makeText(activity_register_getphone.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
                                Toast.LENGTH_LONG).show();
                        input_phone.requestFocus();
                        return;
                    }
                }
            }
        }

    };

}
