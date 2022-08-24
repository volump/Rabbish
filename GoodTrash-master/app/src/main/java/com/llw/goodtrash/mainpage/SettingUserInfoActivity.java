package com.llw.goodtrash.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.llw.goodtrash.R;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class SettingUserInfoActivity extends AppCompatActivity {
    private Button buttonRegister;
    private EditText registerUserName;
    private EditText registerUserPwd;
    private EditText registerCheckUserPwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user_info);

        buttonRegister = (Button)findViewById(R.id.register);
        registerUserName = (EditText)findViewById(R.id.accountEdit);
        registerUserPwd = (EditText)findViewById(R.id.passwordEdit);
        registerCheckUserPwd = (EditText)findViewById(R.id.passwordCheckEdit);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = registerUserName.getText().toString().trim();
                String userPwd = registerUserPwd.getText().toString().trim();
                String checkUserPwd = registerCheckUserPwd.getText().toString().trim();
                Intent intent = new Intent();
                Toast.makeText(SettingUserInfoActivity.this,"账号 = "+userName, LENGTH_SHORT).show();
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(SettingUserInfoActivity.this,"请输入账号", LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(SettingUserInfoActivity.this,"请输入密码", LENGTH_SHORT).show();
                    return;
                }

                if(!userPwd.equals(checkUserPwd)){
                    Toast.makeText(SettingUserInfoActivity.this,"两次密码不相同", LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {   /*网络通信，使用子线程*/
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("newName", userName);
                            params.add("newPwd", userPwd);
                            OkHttpClient client = new OkHttpClient();  //创建客户端
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.104:8090/mvc/user/updateByName")
                                    .post(params.build())
                                    .build();   //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的命令
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String state = jsonObject.getString("state");
                            String retInfo = jsonObject.getString("retInfo");
                            Log.d("state", jsonObject.getString("state"));
                            if(state.equals("200")){
                                Toast.makeText(SettingUserInfoActivity.this,retInfo+"",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            intent.setClass(SettingUserInfoActivity.this, SettingActivity.class);
                            startActivity(intent);
                            SettingUserInfoActivity.this.finish();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SettingUserInfoActivity.this,"发送成功",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SettingUserInfoActivity.this,"网络链接失败",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();
            }
        });
    }
}