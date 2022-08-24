package Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.llw.goodtrash.R;
import com.llw.goodtrash.mainpage.Main_pageActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_login;
    private Button button_register;
    private EditText login_userName;
    private EditText login_userPwd;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        button_login = (Button)findViewById(R.id.login);
        button_register = (Button)findViewById(R.id.register);
        login_userName = (EditText)findViewById(R.id.accountEdit);
        login_userPwd = (EditText)findViewById(R.id.passwordEdit);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = login_userName.getText().toString().trim();
                String password = login_userPwd.getText().toString().trim();
                Intent intent = new Intent();
                Toast.makeText(LoginActivity.this,"userName = "+userName, LENGTH_SHORT).show();
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this,"请输入账号", LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"请输入密码", LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {   /*网络通信，使用子线程*/
                    @Override
                    public void run() {
                        try {
                            FormBody.Builder params = new FormBody.Builder();
                            params.add("userName", userName);
                            params.add("userPwd", password);
                            OkHttpClient client = new OkHttpClient();  //创建客户端
                            Request request = new Request.Builder()
                                    .url("http://192.168.1.100:8090/mvc/user/login2")
                                    .post(params.build())
                                    .build();   //创造http请求
                            Response response = client.newCall(request).execute();  //执行发送的命令
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            String state = jsonObject.getString("state");

                            Log.d("state", jsonObject.getString("state"));
                            Log.d("retInfo", jsonObject.getString("retInfo"));
                            String retInfo = jsonObject.getString("retInfo");

                            if(state.equals("200")){
                                Toast.makeText(LoginActivity.this,"用户名或密码", LENGTH_SHORT).show();
                                return;
                            }
                            intent.setClass(LoginActivity.this, Main_pageActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"发送成功",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(LoginActivity.this,"网络链接失败",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initView(){
        button_login = (Button)findViewById(R.id.login);
        button_register = (Button)findViewById(R.id.register);
        login_userName = (EditText)findViewById(R.id.accountEdit);
        login_userPwd = (EditText)findViewById(R.id.passwordEdit);
        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}