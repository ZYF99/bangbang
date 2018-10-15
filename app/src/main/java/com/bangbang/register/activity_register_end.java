package com.bangbang.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bangbang.R;
import com.bangbang.taskhall.MainActivity;

public class activity_register_end extends AppCompatActivity implements View.OnClickListener{
    ImageView img1;
    ImageView img2;
    Intent intent_o;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_end);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        intent_o = getIntent();
        intent = new Intent(activity_register_end.this, MainActivity.class);
        intent.putExtra("account",intent_o.getStringExtra("account"));
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img1:

                startActivity(intent);
                break;
            case R.id.img2:

                startActivity(intent);
                break;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
