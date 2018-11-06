package com.bangbang.taskhall;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bangbang.R;
import com.bangbang.bean.Task;
import com.bangbang.task_received.activity_task_myreceived;
import com.bangbang.task_released.activity_task_myreleased;
import com.bangbang.utils.ActivityManager;
import com.bangbang.utils.animation_rec_item;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.listener.OnBannerListener;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bangbang.utils.Util_getImage.getBitMBitmap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,OnBannerListener{
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    Toolbar toolbar;
    BannerView banner;
   /* myBanner banner;*/
    DrawerLayout myDrawer;
    CircleImageView touxiang;
    View view_header;
    NavigationView naview;
    RelativeLayout Rel_header;
    XRecyclerView recyclerView ;
    xRecAdapter_task_taskhall xRecAdapter_task_taskhall;
    Button sendMsg ;
    LinearLayout btn_myReceived;
    LinearLayout btn_myReleased;
    TextView draw_name;
    CircleImageView draw_touxiang;
    Thread thread_getTask;

    int addStart = 0;
    boolean havedata = true;
    String account = "";
    String user_name = "";
    List<Task> tasks = new ArrayList <Task>();
    ArrayList<String> list_path;
    ArrayList<String> list_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(MainActivity.this);

        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        user_name = intent.getStringExtra("user_name");
        getId();
        initNav();
        getBanner();
        initRec();
        initData();
        keepLongConnection(account);//Socket与服务器保持一个长连接


    }



    //初始化控件
    void getId(){
        view_header = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        Rel_header = view_header.findViewById(R.id.header_relative);
        banner=view_header.findViewById(R.id.banner);
        myDrawer=findViewById(R.id.drawerlayout);
        toolbar=findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.xrec);
        naview = findViewById(R.id.nav_list);
        //sendMsg = view_header.findViewById(R.id.button_sendmsg);
        btn_myReleased = view_header.findViewById(R.id.btn_myreleased);
        btn_myReceived = view_header.findViewById(R.id.btn_myreceived);
        touxiang = findViewById(R.id.circle_touxiang);
        View headerLayout = naview.inflateHeaderView(R.layout.header_draw);
        draw_name = headerLayout.findViewById(R.id.draw_name);
        draw_touxiang = headerLayout.findViewById(R.id.draw_touxiang);
        //sendMsg.setOnClickListener(this);
        btn_myReleased.setOnClickListener(this);
        btn_myReceived.setOnClickListener(this);
    }
    void initRec(){
        LinearLayoutManager xLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        xLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple); //设定下拉刷新样式
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);//设定上拉加载样式
        recyclerView.setLayoutManager(xLinearLayoutManager);
        xRecAdapter_task_taskhall = new xRecAdapter_task_taskhall(MainActivity.this,tasks);
        recyclerView.setAdapter(xRecAdapter_task_taskhall);
        //recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setArrowImageView(R.drawable.touxiang);     //设定下拉刷新显示图片（不必须）
        recyclerView.addHeaderView(Rel_header);
        recyclerView.setPullRefreshEnabled(true);

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
    //初始化控件


    //初始化侧拉界面
    private void initNav() {
        setSupportActionBar(toolbar);
        draw_name.setText(user_name);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String touxiang_url = getTouxiangUrl(account);
                Log.d("SSSSSSSSSS", touxiang_url);
                final Bitmap im = getBitMBitmap(touxiang_url);

                Log.d("SSSSSSSSS", touxiang_url);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        touxiang.setImageBitmap(im);
                        draw_touxiang.setImageBitmap(im);
                    }
                });
            }
        }).start();

        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawer.openDrawer(GravityCompat.START);
                myDrawer.setSelected(false);
                naview.setCheckedItem(R.id.relative);
            }
        });





        naview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                myDrawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.menu_1:

                        break;
                    case R.id.menu_2:

                        break;
                    case R.id.menu_3:

                        break;
                    case R.id.menu_4:

                        break;
                    case R.id.menu_6:

                        break;
                    case R.id.menu_7:

                        break;
                    case R.id.menu_11:

                        break;
                }
                return  true;
            }
        });
    }
    //初始化侧拉界面

    //得到任务列表
    String getdutyList(int start,int end){
        String result = ""; //用来取得返回的String；
        //发送post请求
        HttpPost httpRequest = new HttpPost("http://132.232.93.93/bangbang/bangbang_getTask_taskhall.php");
        //Post运作传送变数必须用NameValuePair[]阵列储存
        try {
            //发出HTTP请求
            Log.d("请求连接", "在发送请求");
            List params = new ArrayList();
            params.add(new BasicNameValuePair("start", start+""));
            params.add(new BasicNameValuePair("end", end+""));
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
                    Toast.makeText(MainActivity.this, "网络出错",Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }
    String parseJSON(String jsonData)   {
        Log.d("JSON" ,jsonData);
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            havedata = true;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                tasks.add(new Task(jsonObject.getInt("id"),
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
                    Toast.makeText(MainActivity.this, "下面没有啦！", Toast.LENGTH_SHORT).show();
                }
            });
            return "nodata";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
    void initData() {
        tasks.clear();
        xRecAdapter_task_taskhall.notifyDataSetChanged();
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
    //得到任务列表

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
                    Toast.makeText(MainActivity.this, "网络出错",Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }

    //Socket长连接
    void keepLongConnection(final String account){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                BufferedReader in = null;
                String msg = "";
                try {
                    socket = new Socket(InetAddress.getByName("132.232.93.93"), 6235);
                    //创建向服务器端发送信息的线程并启动
                    new ClientThread(socket,sendMsg,account).start();
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //main线程负责接收服务器发来的消息
                    while ((msg = in.readLine())!=null) {

                        final String finalMsg = msg;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            Toast.makeText(MainActivity.this,finalMsg,Toast.LENGTH_SHORT).show();

                                        }
                                    });
                            }
                        });
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    //Socket长连接

    //点击事件
    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_myreleased:
                    Intent intent1 = new Intent(MainActivity.this,activity_task_myreleased.class);
                    intent1.putExtra("account",account);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.enter_zoom_go_out,R.anim.enter_zoom_go_in);
                    break;
                case R.id.btn_myreceived:
                    Intent intent2 = new Intent(MainActivity.this,activity_task_myreceived.class);
                    intent2.putExtra("account",account);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.enter_zoom_go_out,R.anim.enter_zoom_go_in);
                    break;

            }
    }
    //点击事件


    //广告栏
    private void getBanner() {
        list_path = new ArrayList<>();
        list_title = new ArrayList<>();
        list_path.add("http://132.232.93.93/bangbang/imgs/1.jpg");
        list_path.add("http://132.232.93.93/bangbang/imgs/2.jpg");
        list_path.add("http://132.232.93.93/bangbang/imgs/3.jpg");
        list_path.add("http://132.232.93.93/bangbang/imgs/4.jpg");
        banner.setBannerData(list_path).setSmoothInterval(1500).startSmoothAuto();

    }
    @Override
    public void OnBannerClick(int position) {

    }
    //广告栏

    //back键
    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if(KeyCode ==KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(R.drawable.icon)
                    .setTitle("警告")
                    .setMessage("真的退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                            ActivityManager.getInstance().exit();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create().show();
        }
        return super.onKeyDown(KeyCode,event);
    }
    //back键
}

    class ClientThread extends Thread{
    String account;
    Socket s;
    BufferedWriter out;
    Button btn_sendMsg = null;

    public ClientThread(Socket s,Button btn_sendMsg,String account) {
        this.btn_sendMsg = btn_sendMsg;
        this.s = s;
        this.account = account;
    }
    @Override
    public void run() {

        try {
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            String str = account;
            out.write(str+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


            btn_sendMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendMsgOnSocket("17760485737");
                }
            });
        }

        void sendMsgOnSocket(final String msg)
        {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            out.write(msg+"\n");
                            out.flush();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

