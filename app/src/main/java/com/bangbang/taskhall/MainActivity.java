package com.bangbang.taskhall;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bangbang.R;
import com.bangbang.task_received.activity_task_myreceived;
import com.bangbang.task_released.activity_task_myreleased;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,activity_task_myreleased.class);
        startActivity(intent);
    }
}
