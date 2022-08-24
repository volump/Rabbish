package com.llw.goodtrash.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.llw.goodtrash.R;
import com.llw.goodtrash.contract.MainContract;
import com.llw.goodtrash.ui.HistoryActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingActivity extends AppCompatActivity {
    ImageView button_1 = null;
    ImageView button_2 = null;
    ImageView button_3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        button_1 = (ImageView) findViewById(R.id.user_delete);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                intent.setClass(SettingActivity.this, SettingUserInfoActivity.class);
                new Thread(new Runnable() {   /*网络通信，使用子线程*/
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            OkHttpClient client = new OkHttpClient();  //创建客户端
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.104:8090/mvc/user/deleteUser")
                                    .post(params.build())
                                    .build();   //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的命令
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String state = jsonObject.getString("state");
                            String retInfo = jsonObject.getString("retInfo");
                            Log.d("state", jsonObject.getString("state"));


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SettingActivity.this,"发送成功",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SettingActivity.this,"网络链接失败",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();
                finish();
                startActivity(intent);
            }
        });

        button_2 = (ImageView) findViewById(R.id.setting_user_info);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, SettingUserInfoActivity.class);
                startActivity(intent);
            }
        });

        button_3 = (ImageView) findViewById(R.id.Exit1);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                finish();
//                intent.setClass(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public int getLayoutId() {
        return R.layout.activity_setting;
    }


    protected MainContract.MainPresenter createPresenter() {
        return new MainContract.MainPresenter();
    }

    /**
     * 进入历史记录页面
     */
    public void jumpHistory(View view) {
        gotoActivity(HistoryActivity.class);
    }

    /**
     * 进入Activity
     *
     * @param clazz 目标Activity
     */
    private void gotoActivity(Class<?> clazz) {
        startActivity(new Intent(SettingActivity.this, clazz));
    }
}