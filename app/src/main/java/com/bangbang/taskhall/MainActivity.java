package com.bangbang.taskhall;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bangbang.R;
import com.bangbang.drawlayout.task_released.task_released;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,task_released.class);
        startActivity(intent);
    }
}
