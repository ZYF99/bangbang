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
import android.widget.Toast;
import com.bangbang.R;
import com.bangbang.utils.ActivityManager;
import com.dalimao.corelibrary.VerificationCodeInput;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class activity_register_getcord extends AppCompatActivity implements View.OnClickListener {

    EventHandler eventHandler;
    VerificationCodeInput edit_cord = null;
    //EditText edit_cord = null;
    String phone_number = "";
    String cord_number = "";
    Button btn_return = null;
    Boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(activity_register_getcord.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_getcord);
        Intent intent_o = getIntent();
        phone_number = intent_o.getStringExtra("phone");
        getId();
        edit_cord.requestFocus();

        /*******************************************************************************/
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
        /*********************************************************************************/

    }

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;

            if(result==SMSSDK.RESULT_COMPLETE)
            {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码输入正确", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity_register_getcord.this, activity_register_getUserInfo.class);
                    intent.putExtra("phone",phone_number);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_zoom_go_out,R.anim.enter_zoom_go_in);
                }
            }
            else
            {
                if(flag) {
                    Toast.makeText(getApplicationContext(), "获取验证码失败", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "验证码输入错误", Toast.LENGTH_LONG).show();
                    edit_cord.setEnabled(true);
                }
            }
        }
    };

    private void getId(){
        edit_cord = findViewById(R.id.input_code);
        btn_return = findViewById(R.id.btn_return);
        edit_cord.requestFocus();
        btn_return.setOnClickListener(this);
        edit_cord.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                if(judCord(content)) {
                    SMSSDK.submitVerificationCode("86", phone_number, cord_number);
                    flag = false;
                }
            }
        });
    }
    private boolean judCord(String cord)
    {
        if(TextUtils.isEmpty(cord.trim()))
        {
            Toast.makeText(activity_register_getcord.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            edit_cord.requestFocus();
            return false;
        }
        else if(cord.trim().length()!=4)
        {
            Toast.makeText(activity_register_getcord.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            edit_cord.requestFocus();

            return false;
        }
        else
        {
            cord_number=cord.trim();
            return true;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_zoom_return_out,R.anim.enter_zoom_return_in);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_return:
                finish();
                overridePendingTransition(R.anim.enter_zoom_return_out,R.anim.enter_zoom_return_in);
        }
    }
}
